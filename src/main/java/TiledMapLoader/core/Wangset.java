package TiledMapLoader.core;

import java.util.ArrayList;
import org.w3c.dom.Element;
import TiledMapLoader.privateUtil.Util;

/**
 * A set of tiles.
 */
public class Wangset {

	/**
	 * The name of this wangset.
	 */
	public String name;

	/**
	 * <p>
	 * The wang colors. Cannot be null.
	 * </p>
	 * 
	 * <p>
	 * The order of this array is arbitrary.
	 * </p>
	 */
	public ArrayList<WangColor> colors;

	/**
	 * <p>
	 * The tiles associated with this wangset. Cannot be null.
	 * </p>
	 * 
	 * <p>
	 * The order of this array is arbitrary.
	 * </p>
	 */
	public ArrayList<WangTile> tiles;

	/**
	 * Custom user-defined properties. Can be null.
	 */
	public TMXProperties properties;

	<IMG> Wangset(Element element, Tileset<IMG> parent) {
		name = Util.getStringAttribute(element, "name", "");
		Element propElement = Util.getSingleTag(element, "properties", false);
		if (propElement != null) {
			properties = new TMXProperties(propElement);
		} else {
			properties = null;
		}

		colors = new ArrayList<>();
		Element corners = Util.getSingleTag(element, "wangcornercolors", false);
		if (corners != null) {
			int colorId = 0;
			for (Element colorTag : Util.getAllTags(corners, "wangcornercolor")) {
				colors.add(new WangColor(colorTag, SideType.CORNER, colorId));
				colorId += 1;
			}
		}
		Element edges = Util.getSingleTag(element, "wangedgecolors", false);
		if (edges != null) {
			int colorId = 0;
			for (Element colorTag : Util.getAllTags(edges, "wangedgecolor")) {
				colors.add(new WangColor(colorTag, SideType.EDGE, colorId));
				colorId += 1;
			}
		}

		tiles = new ArrayList<>();
		for (Element tile : Util.getAllTags(element, "wangtile")) {
			tiles.add(new WangTile(tile, colors, parent));
		}
	}
}
