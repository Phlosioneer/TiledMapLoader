package core;

import org.w3c.dom.Element;
import privateUtil.Util;
import util.Vector;

/**
 * Basic info common to all objects in objectLayers. Depending on the <i>kind</i> enum,
 * the <i>size</i> may not be used.
 */
public abstract class TMXObject {
	/**
	 * <p>
	 * Unique global ID for this object.
	 * </p>
	 * 
	 * <p>
	 * No other object in the map file will share the same ID.
	 * </p>
	 */
	public int id;
	/**
	 * The name of the object (an arbitrary string). Cannot be null.
	 */
	public String name;
	/**
	 * <p>
	 * The user-defined type of the object (an arbitrary string). Cannot be null.
	 * </p>
	 * 
	 * <p>
	 * This field has no relation to whether the object is a ShapeObject, TextObject, etc.
	 * It is just a string that can be defined in Tiled.
	 * </p>
	 */
	public String type;
	/**
	 * <p>
	 * The origin of the object. Cannot be null.
	 * </p>
	 */
	public Vector position;
	/**
	 * <p>
	 * The width and height of the object. Cannot be null.
	 * </p>
	 * 
	 * <p>
	 * Depending on the <i>kind</i> enum, this field may be meaningless.
	 * </p>
	 */
	public Vector size;
	/**
	 * <p>
	 * The rotation of the object, in degrees clockwise. The <i>position</i> is used as the center of rotation
	 * (inherited from TMXObject).
	 * </p>
	 */
	public float rotation;
	/**
	 * Whether the object is drawn on screen.
	 */
	public boolean isVisible;
	public TMXProperties properties;

	TMXObject(Element element) {
		if (element.hasAttribute("template")) {
			throw new RuntimeException("Templates not supported yet.");
		}
		id = Util.getIntAttribute(element, "id");
		name = Util.getStringAttribute(element, "name", "");
		type = Util.getStringAttribute(element, "type", "");
		float x = Util.getFloatAttribute(element, "x");
		float y = Util.getFloatAttribute(element, "y");
		position = new Vector(x, y);
		float width = Util.getFloatAttribute(element, "width", 0);
		float height = Util.getFloatAttribute(element, "height", 0);
		size = new Vector(width, height);
		rotation = Util.getFloatAttribute(element, "rotation", 0);
		isVisible = Util.getBoolAttribute(element, "visible", true);
		Element propertiesTag = Util.getSingleTag(element, "properties", false);
		if (propertiesTag != null) {
			properties = new TMXProperties(propertiesTag);
		} else {
			properties = null;
		}
	}
}
