package core;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.w3c.dom.Element;
import privateUtil.Util;
import util.FileLocatorDelegate;
import util.FileParsingException;
import util.ImageDelegate;

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

	public static enum RenderOrder {
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
	 * <p>
	 * All the layers in the file, from the bottom up. Cannot be null.
	 * </p>
	 * 
	 * <p>
	 * Layers should be rendered starting at the first element, and then each layer
	 * is rendered on top of the previous ones.
	 * </p>
	 */
	public ArrayList<Layer> layers;
	/**
	 * Map properties. Can be null.
	 */
	public TMXProperties properties;

	public Orientation orientation;
	public RenderOrder renderOrder;
	public StaggerAxis staggerAxis;
	public StaggerIndex staggerIndex;
	public TMXColor backgroundColor;

	private float hexSideLength;
	private boolean m_hasHexSideLength;

	public int tileWidth;
	public int tileHeight;

	public int mapWidth;
	public int mapHeight;

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
	 *            An object used when opening files. Cannot be null.
	 * @param imageDelegate
	 *            An object to use when slicing tileset images into tile images. Can be null.
	 */
	public MapFile(String filename, FileLocatorDelegate fileDelegate, ImageDelegate<IMG> imageDelegate) {
		InputStream file = fileDelegate.openFile(filename);
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
			renderOrder = RenderOrder.RIGHT_DOWN;
		} else if (renderString.equals("right-up")) {
			renderOrder = RenderOrder.RIGHT_UP;
		} else if (renderString.equals("left-down")) {
			renderOrder = RenderOrder.LEFT_DOWN;
		} else if (renderString.equals("left-up")) {
			renderOrder = RenderOrder.LEFT_UP;
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
			tilesets.add(new TilesetEntry<>(tilesetEntryElement, fileDelegate, imageDelegate));
		}

		// Read the map layers, using a layerGroup to do the parsing.
		this.layers = LayerGroup.parseLayers(root, this, imageDelegate);
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

	/**
	 * Search the direct descendents of this group for a layer with the given name.
	 * 
	 * @param name
	 *            The name of the layer to find.
	 * @return The layer, or <i>null</i> if no matching layer is found.
	 */
	// Basically copy-pasted from LayerGroup.
	public Layer getLayerByName(String name) {
		for (Layer layer : layers) {
			if (layer.name.equals(name)) {
				return layer;
			}
		}
		return null;
	}

	/**
	 * Same as <i>getLayerByName</i>, but also searches sub-groups recursively.
	 * 
	 * @param name
	 *            The name of the layer to find.
	 * @return The layer, or <i>null</i> if no matching layer is found.
	 */
	// Basically copy-pasted from LayerGroup.
	public Layer getLayerByNameRecursive(String name) {
		for (Layer layer : layers) {
			if (layer.name.equals(name)) {
				return layer;
			}
			if (layer instanceof LayerGroup) {
				// Technically, we can't know for sure that the layer's IMG type is the same as our IMG type.
				@SuppressWarnings("unchecked")
				Layer recurse = ((LayerGroup<IMG>) layer).getLayerByNameRecursive(name);
				if (recurse != null) {
					return recurse;
				}
			}
		}
		return null;
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

		TilesetEntry(Element element, FileLocatorDelegate fileDelegate, ImageDelegate<IMG> imageDelegate) {
			firstGid = Util.getIntAttribute(element, "firstgid", 1);
			if (element.hasAttribute("source")) {
				String filename = element.getAttribute("source");
				InputStream file = fileDelegate.openFile(filename);
				tiles = new Tileset<>(Util.loadXmlFile(file), imageDelegate);
				try {
					file.close();
				} catch (IOException e) {
					System.out.println("TilesetEntry: Error while closing file stream: '" + filename + "'");
				}
			} else {
				tiles = new Tileset<>(element, imageDelegate);
			}
		}
	}
}
