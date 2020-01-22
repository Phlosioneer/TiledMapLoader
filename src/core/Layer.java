package core;

import org.w3c.dom.Element;
import privateUtil.Util;
import util.LayerCastException;
import util.Rect;
import util.ResourceLoaderDelegate;
import util.Vector;

/**
 * The root class for all layers.
 */
public abstract class Layer implements Cloneable {
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
	 * are cumulative. The total of a layer's <i>offset</i> and all of its parent layers' <i>offset</i>s
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

	@Override
	public Object clone() throws CloneNotSupportedException {
		Layer ret = (Layer) super.clone();
		if (properties != null) {
			ret.properties = (TMXProperties) properties.clone();
		}
		ret.offset = (Vector) offset.clone();
		return ret;
	}

	/**
	 * Convenience method to cast this layer into a TileLayer.
	 * 
	 * @param <IMG>
	 *            The IMG for the casted TileLayer. When casting, this function cannot check
	 *            that IMG is correct.
	 * @return This object as a TileLayer.
	 * @throws LayerCastException
	 *             If this isn't a TileLayer instance.
	 */
	@SuppressWarnings("unchecked")
	public <IMG> TileLayer<IMG> asTiles() {
		if (this.getClass() == TileLayer.class) {
			return (TileLayer<IMG>) this;
		} else {
			throw new LayerCastException(name, id, "TileLayer");
		}
	}

	/**
	 * Convenience method to cast this layer into an ObjectLayer.
	 * 
	 * @param <IMG>
	 *            The IMG for the casted ObjectLayer. When casting, this function cannot check
	 *            that IMG is correct.
	 * @return This object as an ObjectLayer.
	 * @throws LayerCastException
	 *             If this isn't an ObjectLayer instance.
	 */
	@SuppressWarnings("unchecked")
	public <IMG> ObjectLayer<IMG> asObjects() {
		if (this.getClass() == ObjectLayer.class) {
			return (ObjectLayer<IMG>) this;
		} else {
			throw new LayerCastException(name, id, "ObjectLayer");
		}
	}

	/**
	 * Convenience method to cast this layer into a LayerGroup.
	 * 
	 * @param <IMG>
	 *            The IMG for the casted LayerGroup. When casting, this function cannot check
	 *            that IMG is correct.
	 * @return This object as a LayerGroup.
	 * @throws LayerCastException
	 *             If this isn't a LayerGroup instance.
	 */
	@SuppressWarnings("unchecked")
	public <IMG> LayerGroup<IMG> asGroup() {
		if (this.getClass() == LayerGroup.class) {
			return (LayerGroup<IMG>) this;
		} else {
			throw new LayerCastException(name, id, "LayerGroup");
		}
	}

	/**
	 * Convenience method to cast this layer into an ImageLayer.
	 * 
	 * @param <IMG>
	 *            The IMG for the casted ImageLayer. When casting, this function cannot check
	 *            that IMG is correct.
	 * @return This object as an ImageLayer.
	 * @throws LayerCastException
	 *             If this isn't an ImageLayer instance.
	 */
	@SuppressWarnings("unchecked")
	public <IMG> ImageLayer<IMG> asImage() {
		if (this.getClass() == ImageLayer.class) {
			return (ImageLayer<IMG>) this;
		} else {
			throw new LayerCastException(name, id, "ImageLayer");
		}
	}

	/**
	 * 
	 * @param <IMG>
	 *            The image type. MUST be the same type as other IMG parameters!
	 * @param pixelBounds
	 *            The bounds to draw.
	 * @param delegate
	 *            The image handling delegate.
	 * @return The rendered layer.
	 */
	public <IMG> IMG renderToImage(Rect pixelBounds, ResourceLoaderDelegate<IMG> delegate) {
		IMG baseImage = delegate.blankImage(pixelBounds.width, pixelBounds.height);
		return renderToImage(pixelBounds, baseImage, new Vector(), 1, delegate);
	}

	/**
	 * 
	 * @param <IMG>
	 *            THe image type. MUST be the same type as other IMG parameters!
	 * @param pixelBounds
	 *            The bounds to draw.
	 * @param baseImage
	 *            The image to draw onto.
	 * @param renderOffset
	 *            The offset for this layer on the image.
	 * @param opacity
	 *            The opacity for this layer.
	 * @param delegate
	 *            The image handling delegate.
	 * @return The rendered layer.
	 */
	abstract public <IMG> IMG renderToImage(Rect pixelBounds, IMG baseImage, Vector renderOffset, float opacity, ResourceLoaderDelegate<IMG> delegate);
}
