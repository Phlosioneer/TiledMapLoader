package core;

import org.w3c.dom.Element;
import privateUtil.Util;
import util.Vector;

/**
 * The root class for all layers.
 */
public abstract class Layer {
	/**
	 * The ID of the layer. This is unique within the entire map file.
	 */
	public int id;
	/**
	 * The name of the layer. Arbitrary string. Cannot be null.
	 */
	public String name;
	/**
	 * Whether the layer is currently visible. If this is a group layer, it affects all children.
	 */
	public boolean isVisible;
	/**
	 * <p>
	 * The horizontal and vertical offset of the layer.
	 * </p>
	 * 
	 * <p>
	 * If this is a group layer, layer offsets
	 * are cumulative. The total of a layer's <i>offset</i> and all of its parent layers' <i>offset<i>s
	 * gives its actual offset.
	 * </p>
	 * 
	 * <p>
	 * Cannot be null.
	 * </p>
	 */
	public Vector offset;
	/**
	 * <p>
	 * The opacity of this layer, between 0.0 (not visible) and 1.0 (fully visible).
	 * </p>
	 * 
	 * <p>
	 * Avoid using opacity to indicate layer visibility. Instead of setting opacity to 0.0,
	 * set <i>isVisible</i> to false.
	 * </p>
	 */
	public float opacity;
	/**
	 * The properties of this layer. Cannot be null.
	 */
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

	/**
	 * Manually create a Layer instance. No fields are initialized.
	 */
	public Layer() {}
}
