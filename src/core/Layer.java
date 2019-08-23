package core;

import org.w3c.dom.Element;
import privateUtil.Util;
import util.Vector;

public abstract class Layer {
	public int id;
	public String name;
	public boolean isVisible;
	public Vector offset;
	public float opacity;
	public TMXProperties properties;

	protected Layer(Element element) {
		id = Util.getIntAttribute(element, "id");
		name = Util.getStringAttribute(element, "name");
		isVisible = (Util.getIntAttribute(element, "visible", 1) == 1);
		float x = Util.getFloatAttribute(element, "offsetx", 0);
		float y = Util.getFloatAttribute(element, "offsety", 0);
		offset = new Vector(x, y);
		opacity = Util.getFloatAttribute(element, "opacity", 1);
		Element propertiesTag = Util.getSingleTag(element, "properties", false);
		if (propertiesTag != null) {
			properties = new TMXProperties(propertiesTag);
		}
	}
}
