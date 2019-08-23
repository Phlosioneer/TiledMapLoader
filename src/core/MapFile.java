package core;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.w3c.dom.Element;
import privateUtil.Util;
import util.FileLocatorDelegate;
import util.ImageDelegate;

/**
 * This class is the main class for the TMX library. It loads a file and all its tilesets.
 *
 * @param <IMG>
 *            The image type that <i>ImageDelegate</i> returns.
 */
public class MapFile<IMG> {

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
			throw new RuntimeException("Unsupported Tiled version: " + root.getAttribute("version"));
		}

		// Check that unsupported features are not being used.
		if (!root.getAttribute("orientation").equals("orthogonal")) {
			throw new RuntimeException("'orientation' must be 'orthogonal'");
		}
		if (!root.getAttribute("renderorder").equals("right-down")) {
			throw new RuntimeException("'renderOrder' must be 'right-down'");
		}

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

	public Layer getLayerByName(String name) {
		for (Layer layer : layers) {
			if (layer.name.equals(name)) {
				return layer;
			}
		}
		return null;
	}

	public Layer getLayerByNameRecursive(String name) {
		for (Layer layer : layers) {
			if (layer.name.equals(name)) {
				return layer;
			}
			if (layer instanceof LayerGroup) {
				Layer recurse = ((LayerGroup<IMG>) layer).getLayerByNameRecursive(name);
				if (recurse != null) {
					return recurse;
				}
			}
		}
		return null;
	}

	public static class TilesetEntry<IMG> {
		public Tileset<IMG> tiles;
		public int firstGid;

		public TilesetEntry(Element element, FileLocatorDelegate fileDelegate, ImageDelegate<IMG> imageDelegate) {
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
