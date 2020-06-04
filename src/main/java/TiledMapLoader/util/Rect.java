package TiledMapLoader.util;

/**
 * Immutable class defining a rectangle of pixels in an image.
 */
public final class Rect implements Cloneable {
	/**
	 * The x coord.
	 */
	public final int x;
	/**
	 * The y coord.
	 */
	public final int y;
	/**
	 * The width. Cannot be negative.
	 */
	public final int width;
	/**
	 * The height. Cannot be netative.
	 */
	public final int height;

	/**
	 * @param x
	 *            The x coord.
	 * @param y
	 *            The y coord.
	 * @param width
	 *            The width. Cannot be negative.
	 * @param height
	 *            The height. Cannot be negative.
	 */
	public Rect(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
