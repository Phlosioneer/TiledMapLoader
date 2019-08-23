package core;

import org.w3c.dom.Element;
import privateUtil.Util;
import util.AttributeNotFoundException;
import util.AttributeParsingErrorException;
import util.ImageDelegate;

/**
 * A layer that is a single image.
 *
 * @param <IMG>
 *            The return type of <i>ImageDelegate.loadImage()</i>.
 */
public class ImageLayer<IMG> extends Layer {
	/**
	 * The path to the image. Cannot be null.
	 */
	public String imagePath;
	/**
	 * The image returned by the ImageDelegate.
	 */
	public IMG image;
	/**
	 * <p>
	 * The image format as a file extension. Null if not provided.
	 * </p>
	 * 
	 * <p>
	 * This is intended to be used when the image is embedded as binary data within the map.
	 * This is not currently supported.
	 * </p>
	 */
	public String format;
	/**
	 * The color to consider transparent when rendering the image. Null if not provided.
	 */
	public TMXColor transparentColor;
	/**
	 * The width of the image, if provided. -1 otherwise.
	 */
	public int width;
	/**
	 * The height of the image, if provided. -1 otherwise.
	 */
	public int height;

	ImageLayer(Element element, ImageDelegate<IMG> delegate) {
		super(element);

		Element imageTag = Util.getSingleTag(element, "image", true);
		imagePath = Util.getStringAttribute(imageTag, "source");
		format = Util.getStringAttribute(imageTag, "format", null);
		transparentColor = Util.getColorAttribute(imageTag, "trans", null);

		try {
			width = Util.getIntAttribute(imageTag, "width");
			if (width < 0) {
				throw new AttributeParsingErrorException(imageTag, "width", "Width in TMX file cannot be negative", width);
			}
		} catch (AttributeNotFoundException e) {
			width = -1;
		}

		try {
			height = Util.getIntAttribute(imageTag, "height");
			if (height < 0) {
				throw new AttributeParsingErrorException(imageTag, "height", "Height in TMX file cannot be negative", height);
			}
		} catch (AttributeNotFoundException e) {
			height = -1;
		}

		if (delegate != null) {
			image = delegate.loadImage(imagePath, transparentColor);
		} else {
			image = null;
		}
	}
}
