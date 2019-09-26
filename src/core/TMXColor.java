package core;

/**
 * A color embedded in a TMX file.
 *
 */
public class TMXColor implements Cloneable {

	/**
	 * The red component.
	 */
	public int red;
	/**
	 * The green component.
	 */
	public int green;
	/**
	 * The blue component.
	 */
	public int blue;
	/**
	 * The alpha component.
	 */
	public int alpha;

	/**
	 * The default color, fully opaque black.
	 */
	public TMXColor() {
		this(0, 0, 0, 255);
	}

	/**
	 * Construct a fully opaque color.
	 * 
	 * @param red
	 *            The red channel value. 0 <= red < 256.
	 * @param blue
	 *            The blue channel value. 0 <= blue < 256.
	 * @param green
	 *            The green channel value. 0 <= green < 256.
	 */
	public TMXColor(int red, int blue, int green) {
		this(red, blue, green, 255);
	}

	/**
	 * Construct a color.
	 * 
	 * @param red
	 *            The red channel value. 0 <= red < 256.
	 * @param blue
	 *            The blue channel value. 0 <= blue < 256.
	 * @param green
	 *            The green channel value. 0 <= green < 256.
	 * @param alpha
	 *            The alpha channel value. 0 <= alpha < 256.
	 *            0 = transparent, 255 = opaque.
	 */
	public TMXColor(int red, int blue, int green, int alpha) {
		this.red = red;
		this.blue = blue;
		this.green = green;
		this.alpha = alpha;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
