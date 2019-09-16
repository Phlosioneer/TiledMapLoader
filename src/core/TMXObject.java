package core;

import org.w3c.dom.Element;
import privateUtil.Util;
import util.FileParsingException;
import util.LayerCastException;
import util.TMXObjectCastException;
import util.Vector;

/**
 * Basic info common to all objects in objectLayers. Depending on the <i>kind</i> enum,
 * the <i>size</i> may not be used.
 */
public abstract class TMXObject implements Cloneable {
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
	/**
	 * Custom properties for this object. Can be null.
	 */
	public TMXProperties properties;

	TMXObject(Element element) {
		if (element.hasAttribute("template")) {
			throw new FileParsingException("Templates not supported yet.");
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

	/**
	 * Manually create a TMXObject instance. No fields are initialized.
	 */
	public TMXObject() {}

	@Override
	public Object clone() throws CloneNotSupportedException {
		TMXObject ret = (TMXObject) super.clone();
		ret.position = (Vector) position.clone();
		ret.size = (Vector) size.clone();
		if (properties != null) {
			ret.properties = (TMXProperties) properties.clone();
		}
		return ret;
	}

	/**
	 * Convenience method to cast this object into a TileObject.
	 * 
	 * @param <IMG>
	 *            The IMG for the casted TileObject. When casting, this function cannot check
	 *            that IMG is correct.
	 * @return This object as a TileObject.
	 * @throws LayerCastException
	 *             If this isn't a TileObject instance.
	 */
	@SuppressWarnings("unchecked")
	public <IMG> TileObject<IMG> asTile() {
		if (this.getClass() == TileObject.class) {
			return (TileObject<IMG>) this;
		} else {
			throw new TMXObjectCastException(name, id, "TileObject");
		}
	}

	/**
	 * Convenience method to cast this object into a ShapeObject.
	 * 
	 * @return This object as a ShapeObject.
	 * @throws LayerCastException
	 *             If this isn't a ShapeObject instance.
	 */
	public ShapeObject asShape() {
		if (this.getClass() == ShapeObject.class) {
			return (ShapeObject) this;
		} else {
			throw new TMXObjectCastException(name, id, "ShapeObject");
		}
	}

	/**
	 * Convenience method to cast this object into a TextObject.
	 * 
	 * @return This object as a TextObject.
	 * @throws LayerCastException
	 *             If this isn't a TextObject instance.
	 */
	public TextObject asText() {
		if (this.getClass() == TextObject.class) {
			return (TextObject) this;
		} else {
			throw new TMXObjectCastException(name, id, "TextObject");
		}
	}
}
