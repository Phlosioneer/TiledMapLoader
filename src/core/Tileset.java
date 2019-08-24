package core;

import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.Element;
import privateUtil.Util;
import privateUtil.Util.ElementIterator;
import util.AttributeParsingErrorException;
import util.ImageDelegate;
import util.Rect;

/**
 * Tileset metadata and tiles.
 *
 * @param <IMG>
 */
public class Tileset<IMG> {
	/**
	 * The image used by this tileset. Cannot be null.
	 */
	public String imageFilePath;
	/**
	 * The tiles in this tileset, in order. Cannot be null.
	 */
	public Tile<IMG>[] tiles;
	/**
	 * The name of the tileset. Cannot be null.
	 */
	public String name;
	/**
	 * The width of each tile.
	 */
	public int tileWidth;
	/**
	 * The height of each tile.
	 */
	public int tileHeight;
	/**
	 * <p>
	 * The number of tiles in each row of the tilesheet.
	 * </p>
	 * 
	 * <p>
	 * Used for determining the layout of the tiles in the tileset
	 * image. If manually generating images for tiles, don't use this
	 * for calculations; instead, use each tile's <i>pixelRect</i>.
	 * </p>
	 * 
	 */
	public int columns;
	/**
	 * <p>
	 * Custom properties for the tileset.
	 * </p>
	 * 
	 * <p>
	 * These properties apply to the whole tileset; for properties of individual
	 * tiles, see <i>Tile.properties</i>
	 * </p>
	 */
	public TMXProperties properties;
	/**
	 * The number of tiles in this tileset.
	 */
	public int tileCount;
	public IMG tilesetImage;
	public TMXColor transparentColor;

	@SuppressWarnings("unchecked")
	Tileset(Element element, ImageDelegate<IMG> delegate) {
		// Read layout info.
		tileWidth = Util.getIntAttribute(element, "tilewidth");
		if (tileWidth <= 0) {
			throw new AttributeParsingErrorException(element, "tilewidth", "Value must be greater than 0", tileWidth);
		}
		tileHeight = Util.getIntAttribute(element, "tileheight");
		if (tileHeight <= 0) {
			throw new AttributeParsingErrorException(element, "tileheight", "Value must be greater than 0", tileHeight);
		}
		int spacing = Util.getIntAttribute(element, "spacing", 0);
		if (spacing < 0) {
			throw new AttributeParsingErrorException(element, "spacing", "Value cannot be negative", spacing);
		}
		int margin = Util.getIntAttribute(element, "margin", 0);
		if (margin < 0) {
			throw new AttributeParsingErrorException(element, "margin", "Value cannot be negative", margin);
		}
		columns = Util.getIntAttribute(element, "columns");
		if (columns <= 0) {
			throw new AttributeParsingErrorException(element, "columns", "Value must be greater than 0", columns);
		}
		tileCount = Util.getIntAttribute(element, "tilecount");
		if (tileCount <= 0) {
			throw new AttributeParsingErrorException(element, "tilecount", "Value must be greater than 0", tileCount);
		}
		name = Util.getStringAttribute(element, "name", "");

		// Get the image file.
		Element imageTag = Util.getSingleTag(element, "image", true);
		imageFilePath = Util.getStringAttribute(imageTag, "source");
		TMXColor transparentColor = Util.getColorAttribute(imageTag, "trans", null);

		IMG mainImage = null;
		if (delegate != null) {
			mainImage = delegate.loadImage(imageFilePath, transparentColor);
		}

		// Get tile metadata.
		HashMap<Integer, Element> tileElementsById = new HashMap<>();
		for (Element tileEntry : Util.getAllTags(element, "tile")) {
			int id = Util.getIntAttribute(tileEntry, "id");
			if (id < 0 || id >= tileCount) {
				throw new AttributeParsingErrorException(element, "id", "Id must be between 0 and tilecount", id);
			}

			tileElementsById.put(id, tileEntry);
		}

		// Make tiles.
		tiles = (Tile<IMG>[]) new Tile[tileCount];
		for (int i = 0; i < tileCount; i++) {
			// Calculate the position of the tile in the spritesheet.
			int x = i % columns;
			int y = i / columns;
			int pixelX = margin + x * (tileWidth + spacing);
			int pixelY = margin + y * (tileHeight + spacing);
			Rect pixelRect = new Rect(pixelX, pixelY, tileWidth, tileHeight);

			// Make the tile's sub-image.
			IMG tileImg = null;
			if (delegate != null) {
				tileImg = delegate.sliceImage(mainImage, pixelRect);
			}

			// Parse metadata.
			TMXProperties properties = null;
			ArrayList<CollisionBounds> geometry = null;
			String tileType = null;
			if (tileElementsById.containsKey(i)) {
				Element tileEntry = tileElementsById.get(i);
				tileType = Util.getStringAttribute(tileEntry, "type", "");

				Element propertiesTag = Util.getSingleTag(tileEntry, "properties", false);
				if (propertiesTag != null) {
					properties = new TMXProperties(propertiesTag);
				}

				Element geometryTag = Util.getSingleTag(tileEntry, "objectgroup", false);
				if (geometryTag != null) {
					ElementIterator collisionBoxEntries = Util.getAllTags(element, "object");
					if (collisionBoxEntries.hasNext()) {
						geometry = new ArrayList<>();
						for (Element collisionBoxEntry : Util.getAllTags(geometryTag, "object")) {
							geometry.add(new CollisionBox(collisionBoxEntry));
						}
					}
				}
			}

			// Create the tile.
			Tile<IMG> tile = null;
			if (properties == null) {
				tile = new Tile<>(this, i, pixelRect);
			} else {
				tile = new Tile<>(this, i, pixelRect, properties);
			}
			tile.image = tileImg;
			if (geometry != null) {
				tile.collisionBoxes = geometry;
			}
			if (tileType != null) {
				tile.tileType = tileType;
			}
			tiles[i] = tile;
		}

	}
}
