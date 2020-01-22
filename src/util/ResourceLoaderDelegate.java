package util;

import java.io.InputStream;
import core.TMXColor;
import core.Tileset;

/**
 * Interface for various things the Tiled parser needs while parsing, plus
 * caching of resources across separate map loads.
 *
 * @param <IMG>
 *            The image class to use.
 */
public interface ResourceLoaderDelegate<IMG> {
	/**
	 * Called to open any kind of file. There should not be any caching in this function;
	 * the getCachedTileset / getCachedImage functions are checked first.
	 * 
	 * @param filename
	 *            The file name, as given in the TMX file.
	 * @param baseDirectory
	 *            The directory of the TMX file, relative to the map file.
	 * @return An input stream for the file. Should normally be a BufferedInputStream.
	 *         Cannot be null. Throw an exception instead.
	 */
	InputStream openFile(String filename, String baseDirectory);

	/**
	 * Called to load an image for the first time. There should not be any caching in this
	 * function; the getCachedImage function is checked first, and the cacheImage function
	 * is called after successfully processing the image.
	 * 
	 * @param input
	 *            The file stream, as given by openFile.
	 * @param transparentColor
	 *            A color to be replaced with transparency, if given in the
	 *            TMX file. Null if not specified.
	 * @return An image object.
	 */
	IMG loadImage(InputStream input, TMXColor transparentColor);

	/**
	 * Called to create a new sub-image for a tile. There should not be any caching in this
	 * function; it is never called twice with the same parameters.
	 * 
	 * @param image
	 *            The image object, as given by loadImage.
	 * @param pixelBounds
	 *            The pixels to copy into a new image.
	 * @return An image object for the given pixels.
	 */
	IMG sliceImage(IMG image, Rect pixelBounds);

	/**
	 * Called to create a new, blank image. Used while rendering layers.
	 * 
	 * @param width
	 *            The width of the new image, in pixels.
	 * @param height
	 *            The height of the new image, in pixels.
	 * @return A blank, transparent image object.
	 */
	IMG blankImage(int width, int height);

	/**
	 * Called to compose newImage onto baseImage, at the given coordinates. The
	 * coordinates assume the origin of baseImage is in the upper-left corner,
	 * and the origin of newImage is its upper-left corner.
	 * 
	 * If the rendering would put the newImage partially or fully outside the
	 * bounds of baseImage, the portion that is inside baseImage should be drawn.
	 * 
	 * @param baseImage
	 *            The image being modified
	 * @param newImage
	 *            The image to draw
	 * @param transform
	 * 
	 * @return The new baseImage, if the object is different, or the original
	 *         baseImage. Never return null; throw an exception instead.
	 */
	IMG composeOntoImage(IMG baseImage, IMG newImage, ImageTransform transform);

	/**
	 * Called before attempting to load a tileset file.
	 * 
	 * @param filename
	 *            The file name, as given in the TMX file.
	 * @param baseDirectory
	 *            The directory of the TMX file, relative to the map file.
	 * @return The tileset, or null if the tileset isn't cached.
	 */
	default Tileset<IMG> getCachedTileset(String filename, String baseDirectory) {
		return null;
	}

	/**
	 * Called after parsing a tileset.
	 * 
	 * @param filename
	 *            The file name, as given in the TMX file.
	 * @param baseDirectory
	 *            The directory of the TMX file, relative to the map file.
	 * @param loadedTileset
	 *            The tileset to cache.
	 */
	default void cacheTileset(String filename, String baseDirectory, Tileset<IMG> loadedTileset) {}

	/**
	 * Called before attempting to load an image file.
	 * 
	 * @param filename
	 *            The file name, as given in the TMX file.
	 * @param baseDirectory
	 *            The directory of the TMX file, relative to the map file.
	 * @return An image object, or null if it isn't cached.
	 */
	default IMG getCachedImage(String filename, String baseDirectory) {
		return null;
	}

	/**
	 * Called after loading an image via loadImage.
	 * 
	 * @param filename
	 *            The file name, as given in the TMX file.
	 * @param baseDirectory
	 *            The directory of the TMX file, relative to the map file.
	 * @param image
	 *            The image to cache.
	 */
	default void cacheImage(String filename, String baseDirectory, IMG image) {}
}
