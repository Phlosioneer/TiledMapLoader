package TiledMapLoader.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.w3c.dom.Element;
import TiledMapLoader.privateUtil.Util;
import TiledMapLoader.util.FileParsingException;
import TiledMapLoader.util.Rect;
import TiledMapLoader.util.ResourceLoaderDelegate;

/**
 * This class is the main class for the TMX library. It loads a file and all its tilesets.
 *
 * @param <IMG>
 *            The image type that <i>ImageDelegate</i> returns.
 */
public class MapFile<IMG> {

	public static enum Orientation {
		ORTHOGONAL, ISOMETRIC, STAGGERED, HEXAGONAL
	}

	/**
	 * The render order for tiles in layers. Usually, this shouldn't matter. For
	 * tilesets that are greater than the map's tileWidth or tileHeight, the draw
	 * order will matter.
	 *
	 */
	public static enum TileRenderOrder {
		RIGHT_DOWN, RIGHT_UP, LEFT_DOWN, LEFT_UP
	}

	public static enum StaggerIndex {
		ODD, EVEN
	}

	public static enum StaggerAxis {
		X, Y
	}

	/**
	 * All the tilesets found in the file. They may not be used. Cannot be null.
	 */
	public ArrayList<TilesetEntry<IMG>> tilesets;
	/**
	 * Map properties. Can be null.
	 */
	public TMXProperties properties;

	public Orientation orientation;
	public TileRenderOrder renderOrder;
	public StaggerAxis staggerAxis;
	public StaggerIndex staggerIndex;
	/**
	 * The color to display under all layers.
	 */
	public TMXColor backgroundColor;

	private float hexSideLength;
	private boolean m_hasHexSideLength;

	/**
	 * <p>
	 * The width of each tile.
	 * </p>
	 * 
	 * <p>
	 * This field is not quite the same as the tileWidth of a tileset.
	 * Tilesets in this map can have different sizes; this tile width determines
	 * the actual spacing of the tile grid. If a tileset has a larger tile width,
	 * tiles will overlap. If it's smaller, there will be gaps.
	 * </p>
	 */
	public int tileWidth;
	/**
	 * <p>
	 * The height of each tile.
	 * </p>
	 * 
	 * <p>
	 * This field is not quite the same as the tileHeight of a tileset.
	 * Tilesets in this map can have different sizes; this tile height determines
	 * the actual spacing of the tile grid. If a tileset has a larger tile width,
	 * tiles will overlap. If it's smaller, there will be gaps.
	 * </p>
	 */
	public int tileHeight;

	/**
	 * The width of the map and its layers, in tiles.
	 */
	public int mapWidth;
	/**
	 * The height of the map and its layers, in tiles.
	 */
	public int mapHeight;

	/**
	 * All layers are contained within this layer group.
	 */
	public LayerGroup<IMG> root;

	/**
	 * 
	 * <p>
	 * Open and parse a map file.
	 * </p>
	 * 
	 * <p>
	 * The provided <i>ImageDelegate</i> is used to generate images for each tile by slicing
	 * the tileset image.
	 * </p>
	 * 
	 * <p>
	 * It can take some time to parse a file. This constructor should be used in a new
	 * thread to avoid freezing the application while the file is parsed.
	 * </p>
	 * 
	 * @param filename
	 *            The file to load. The file is opened using <i>fileDelegate</i>.
	 * @param fileDelegate
	 *            An object used when opening files, loading images, and slicing images into tile
	 *            images. Cannot be null.
	 */
	public MapFile(String filename, ResourceLoaderDelegate<IMG> fileDelegate) {
		InputStream file = fileDelegate.openFile(filename, "");
		Element root = Util.loadXmlFile(file);

		try {
			file.close();
		} catch (IOException e) {
			System.out.println("TilesetEntry: Error while closing file stream: '" + filename + "'");
		}

		// Check the file's version string.
		if (!root.getAttribute("version").equals("1.2")) {
			throw new FileParsingException("Unsupported Tiled version: " + root.getAttribute("version"));
		}

		String orientationString = Util.getStringAttribute(root, "orientation");
		if (orientationString.equals("orthogonal")) {
			orientation = Orientation.ORTHOGONAL;
		} else if (orientationString.equals("isometric")) {
			orientation = Orientation.ISOMETRIC;
		} else if (orientationString.equals("staggered")) {
			orientation = Orientation.STAGGERED;
		} else if (orientationString.equals("hexagonal")) {
			orientation = Orientation.HEXAGONAL;
		} else {
			throw new FileParsingException("Unrecognized orientation: '" + orientationString + "'");
		}

		String renderString = Util.getStringAttribute(root, "renderorder", "right-down");
		if (renderString.equals("right-down")) {
			renderOrder = TileRenderOrder.RIGHT_DOWN;
		} else if (renderString.equals("right-up")) {
			renderOrder = TileRenderOrder.RIGHT_UP;
		} else if (renderString.equals("left-down")) {
			renderOrder = TileRenderOrder.LEFT_DOWN;
		} else if (renderString.equals("left-up")) {
			renderOrder = TileRenderOrder.LEFT_UP;
		} else {
			throw new FileParsingException("Unrecognized render order: '" + renderString + "'");
		}

		if (orientation == Orientation.HEXAGONAL || orientation == Orientation.STAGGERED) {
			String axis = Util.getStringAttribute(root, "staggeraxis");
			if (axis.equals("x")) {
				staggerAxis = StaggerAxis.X;
			} else if (axis.equals("y")) {
				staggerAxis = StaggerAxis.Y;
			} else {
				throw new FileParsingException("Unrecognized stagger axis: '" + axis + "' (expected 'x' or 'y')");
			}

			String index = Util.getStringAttribute(root, "staggerindex");
			if (index.equals("odd")) {
				staggerIndex = StaggerIndex.ODD;
			} else if (index.equals("even")) {
				staggerIndex = StaggerIndex.EVEN;
			} else {
				throw new FileParsingException("Unrecognized stagger index: '" + index + "' (expected 'odd' or 'even')");
			}
		}

		if (orientation == Orientation.HEXAGONAL) {
			m_hasHexSideLength = true;
			hexSideLength = Util.getFloatAttribute(root, "hexsidelength");
		} else {
			m_hasHexSideLength = false;
		}

		backgroundColor = Util.getColorAttribute(root, "backgroundcolor", new TMXColor(0, 0, 0, 0));
		tileWidth = Util.getIntAttribute(root, "tilewidth");
		tileHeight = Util.getIntAttribute(root, "tileheight");
		mapWidth = Util.getIntAttribute(root, "width");
		mapHeight = Util.getIntAttribute(root, "height");

		// Look for tilesets.
		tilesets = new ArrayList<>();
		for (Element tilesetEntryElement : Util.getAllTags(root, "tileset")) {
			tilesets.add(new TilesetEntry<>(tilesetEntryElement, fileDelegate));
		}

		// Read the map layers, using a layerGroup to do the parsing.
		this.root = new LayerGroup<>(root, this, fileDelegate, true);
		Element propertiesTag = Util.getSingleTag(root, "properties", false);
		if (propertiesTag != null) {
			properties = new TMXProperties(propertiesTag);
		} else {
			properties = null;
		}
	}

	/**
	 * Find a tile given its global id.
	 * 
	 * @param gid
	 *            The ID to lookup.
	 * @return The corresponding tile.
	 * @throws RuntimeException
	 *             If there isn't a tile for the given gid.
	 */
	public Tile<IMG> getTile(int gid) {
		if (gid == 0) {
			return null;
		}

		for (TilesetEntry<IMG> entry : tilesets) {
			if (gid >= entry.firstGid && gid < entry.firstGid + entry.tiles.tileCount) {
				return entry.tiles.tiles[gid - entry.firstGid];
			}
		}

		throw new RuntimeException("Unknown tile gid '" + gid + "'");
	}

	public boolean hasHexSideLength() {
		return m_hasHexSideLength;
	}

	public float getHexSideLength() {
		if (!m_hasHexSideLength) {
			throw new RuntimeException("Hex side length not set!");
		}
		return hexSideLength;
	}

	public IMG renderToImage(ResourceLoaderDelegate<IMG> delegate) {
		return root.renderToImage(new Rect(0, 0, tileWidth * mapWidth, tileHeight * mapHeight), delegate);
	}

	public IMG renderToImage(Rect pixelBounds, ResourceLoaderDelegate<IMG> delegate) {
		return root.renderToImage(pixelBounds, delegate);
	}

	/**
	 * <p>
	 * Finds a tileset by its name field (not its file name).
	 * </p>
	 * 
	 * <p>
	 * If multiple tilesets have the same name, the first one will be returned.
	 * </p>
	 * 
	 * @param name
	 *            The name to lookup.
	 * @return The tileset, or null if not found.
	 */
	public Tileset<IMG> getTilesetByName(String name) {
		for (TilesetEntry<IMG> tileset : tilesets) {
			if (tileset.tiles.name.equals(name)) {
				return tileset.tiles;
			}
		}
		return null;
	}

	/**
	 * This is a wrapper for a Tileset that contains map-spesific fields, like <i>firstGid</i>.
	 *
	 * @param <IMG>
	 *            The IMG param of the parent MapFile. See MapFile for more info.
	 */
	public static class TilesetEntry<IMG> {
		/**
		 * The actual tileset. Cannot be null.
		 */
		public Tileset<IMG> tiles;
		/**
		 * The gid of the first tile in the tileset.
		 */
		public int firstGid;

		TilesetEntry(Element element, ResourceLoaderDelegate<IMG> fileDelegate) {
			firstGid = Util.getIntAttribute(element, "firstgid", 1);
			if (element.hasAttribute("source")) {
				String filename = element.getAttribute("source");
				// TODO: Properly handle relative paths.
				tiles = fileDelegate.getCachedTileset(filename, "");
				if (tiles == null) {
					InputStream file = fileDelegate.openFile(filename, "");
					tiles = new Tileset<>(Util.loadXmlFile(file), fileDelegate);
					try {
						file.close();
					} catch (IOException e) {
						System.out.println("TilesetEntry: Error while closing file stream: '" + filename + "'");
					}
					fileDelegate.cacheTileset(filename, "", tiles);
				}
			} else {
				tiles = new Tileset<>(element, fileDelegate);
			}
		}
	}
}
