package TiledMapLoader.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import TiledMapLoader.core.TMXColor;
import TiledMapLoader.core.TileLayer.FlipState;

/**
 * A default implementation of ResourceLoaderDelegate that uses AWT's BufferedImage class.
 *
 */
public class AwtResourceLoader implements ResourceLoaderDelegate<BufferedImage> {

	private int[] cachedPixels;
	private Class<?> appClass;

	public AwtResourceLoader(Class<?> appClass) {
		this.appClass = appClass;
		cachedPixels = null;
	}

	@Override
	public InputStream openFile(String filename, String baseDirectory) {
		Path path = Paths.get(baseDirectory, filename);
		InputStream ret = appClass.getResourceAsStream("/" + path);
		if (ret == null) {
			throw new RuntimeException("AwtResourceLoader could not find resource: " + path);
		}
		return ret;
	}

	@Override
	public BufferedImage loadImage(InputStream input, TMXColor transparentColor) {
		BufferedImage image;
		try {
			image = ImageIO.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("IOException handler not yet written in loadImage of AwtResourceLoader.", e);
		}

		int pixelCount = image.getWidth() * image.getHeight();
		if (cachedPixels == null || cachedPixels.length < pixelCount) {
			cachedPixels = new int[pixelCount];
		}

		if (transparentColor != null) {
			int transparentColorAsInt = transparentColor.toRGBA();
			int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), cachedPixels, 0, image.getWidth());
			boolean alphaFound = false;
			// Can't use pixels.length; cachedPixels.length may be larger than pixelCount.
			for (int i = 0; i < pixelCount; i++) {
				if (pixels[i] == transparentColorAsInt) {
					alphaFound = true;
					pixels[i] = 0xFF000000;
				}
			}

			// Only modify the image if we have to.
			if (alphaFound) {
				image.setRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
			}
		}

		return image;
	}

	@Override
	public BufferedImage sliceImage(BufferedImage image, Rect pixelBounds) {
		return image.getSubimage(pixelBounds.x, pixelBounds.y, pixelBounds.width, pixelBounds.height);
	}

	@Override
	public BufferedImage blankImage(int width, int height) {
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	@Override
	public BufferedImage composeOntoImage(BufferedImage baseImage, BufferedImage newImage, ImageTransform transform) {
		// TODO: Transform.flipState, Transform.opacity
		if (transform.opacity != 0 || transform.flipState != FlipState.Normal) {
			throw new UnsupportedOperationException("Flip State and Opacity not yet supported by AwtResourceLoader.");
		}
		boolean success = baseImage.getGraphics().drawImage(newImage, transform.x, transform.y, null);
		if (!success) {
			throw new RuntimeException("Images are not fully loaded!");
		}
		return baseImage;
	}
}
