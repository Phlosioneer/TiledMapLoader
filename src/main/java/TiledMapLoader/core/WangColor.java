package TiledMapLoader.core;

import org.w3c.dom.Element;
import TiledMapLoader.privateUtil.Util;

public class WangColor {

	public SideType type;
	public String name;
	public int colorId;

	public WangColor(SideType type, int colorId, String name) {
		this.type = type;
		this.colorId = colorId;
		this.name = name;
	}

	public WangColor(Element element, SideType type, int colorId) {
		this.type = type;
		this.colorId = colorId;
		name = Util.getStringAttribute(element, "name", "");
	}
}
