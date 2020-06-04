package TiledMapLoader.core;

import java.io.IOException;
import java.io.InputStream;
import org.w3c.dom.Element;
import TiledMapLoader.privateUtil.Util;
import TiledMapLoader.util.AttributeNotFoundException;
import TiledMapLoader.util.AttributeParsingErrorException;
import TiledMapLoader.util.ImageTransform;
import TiledMapLoader.util.Rect;
import TiledMapLoader.util.ResourceLoaderDelegate;
import TiledMapLoader.util.Vector;

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

	ImageLayer(Element element, ResourceLoaderDelegate<IMG> delegate) {
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

		// TODO: Handle relative paths correctly.
		image = delegate.getCachedImage(imagePath, "");
		if (image == null) {
			InputStream imageFile = delegate.openFile(imagePath, "");
			image = delegate.loadImage(imageFile, transparentColor);
			try {
				imageFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException("IOException handler not yet written in ImageLayer of ImageLayer.", e);
			}
			delegate.cacheImage(imagePath, "", image);
		}
	}

	/**
	 * Manually create an ImageLayer instance. No fields are initialized.
	 */
	public ImageLayer() {}

	@Override
	public <IMG2> IMG2 renderToImage(Rect pixelBounds, IMG2 baseImage, Vector renderOffset, float opacity, ResourceLoaderDelegate<IMG2> delegate) {
		int netOffsetX = Math.round(renderOffset.x);
		int netOffsetY = Math.round(renderOffset.y);
		float netOpacity = Util.combineOpacities(opacity, this.opacity);
		@SuppressWarnings("unchecked")
		IMG2 castImage = (IMG2) image;
		ImageTransform transform = new ImageTransform(netOffsetX, netOffsetY, netOpacity);
		// TODO: Respect pixelBounds
		return delegate.composeOntoImage(baseImage, castImage, transform);
	}

	/**
	 * WARNING: DOES NOT CLONE IMAGE.
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		@SuppressWarnings("unchecked")
		ImageLayer<IMG> ret = (ImageLayer<IMG>) super.clone();
		if (transparentColor != null) {
			ret.transparentColor = (TMXColor) transparentColor.clone();
		}
		return ret;
	}
}
