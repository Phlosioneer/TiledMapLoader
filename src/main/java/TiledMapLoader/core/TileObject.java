package TiledMapLoader.core;

import org.w3c.dom.Element;
import TiledMapLoader.privateUtil.Util;

/**
 * <p>
 * An object that uses a tile as a sprite.
 * </p>
 * 
 * <p>
 * The position of the image depends on the map orientation. In orthoganal mode, <i>position</i> is
 * the bottom-left corner of the image. In isomorphic mode, <i>position</i> is the center of the bottom
 * edge of the image.
 * </p>
 * 
 * <p>
 * The <i>size</i> field is ignored.
 * </p>
 * 
 * @param <IMG>
 *            The IMG param of the parent MapFile. See MapFile for more info.
 */
public class TileObject<IMG> extends TMXObject {
	/**
	 * The tile used by this object.
	 */
	public Tile<IMG> tile;

	TileObject(Element element, MapFile<IMG> parent) {
		super(element);

		int tileId = Util.getIntAttribute(element, "gid");
		tile = parent.getTile(tileId);
	}

	/**
	 * Manually create a TileObject instance. No fields are initialized.
	 */
	public TileObject() {}

	@Override
	public boolean isTile() {
		return true;
	}
}
