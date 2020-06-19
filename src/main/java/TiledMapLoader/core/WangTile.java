package TiledMapLoader.core;

import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.Element;
import TiledMapLoader.privateUtil.Util;
import TiledMapLoader.util.AttributeParsingErrorException;

/**
 * The wang info for one tile.
 */
public class WangTile {
	/**
	 * The sides that have wangs. Cannot be null.
	 */
	public HashMap<WangSide, WangColor> sides;
	/**
	 * <p>
	 * The tile that these wangs are attached to. Cannot be null.
	 * </p>
	 * 
	 * <p>
	 * The type argument is omitted; it is the same as the parent tile set's IMG type. Use
	 * {@link #getTile() getTile} to cast it without compiler warnings.
	 * </p>
	 */
	@SuppressWarnings("rawtypes")
	public Tile tile;

	<IMG> WangTile(Element element, ArrayList<WangColor> colors, Tileset<IMG> parentTileset) {
		int tileId = Util.getIntAttribute(element, "tileid");
		if (tileId < 0) {
			throw new AttributeParsingErrorException(element, "tileid", "Tile id cannot be negative.", tileId);
		}
		if (tileId >= parentTileset.tiles.length) {
			throw new AttributeParsingErrorException(element, "tileid", "Tile id does not exist.", tileId);
		}
		tile = parentTileset.tiles[tileId];

		int rawWangId = Util.getIntAttribute(element, "wangid");
		sides = new HashMap<>();

		for (int i = 0; i < 8; i++) {
			int shift = i * 4;
			int mask = 0xF;
			int colorId = (rawWangId >> shift) & mask;
			if (colorId == 0) {
				continue;
			}
			WangSide side = WangSide.TOP.clockwise(i);

			boolean colorFound = false;
			for (WangColor color : colors) {
				if (color.colorId == colorId && color.type == side.getType()) {
					sides.put(side, color);
					colorFound = true;
					break;
				}
			}
			if (!colorFound) {
				throw new AttributeParsingErrorException(element, "wangid", "Invalid color for side " + side + ".",
						colorId);
			}
		}
	}

	/**
	 * @param <IMG>
	 *            The IMG type parameter of the parent tileset.
	 * @return The tile cast to the appropriate type.
	 */
	@SuppressWarnings("unchecked")
	public <IMG> Tile<IMG> getTile() {
		return tile;
	}
}
