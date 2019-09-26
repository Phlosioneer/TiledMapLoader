package util;

/**
 * Class defining a point in the map. X and Y are in pixels, but can be
 * fractional.
 */
public class Vector implements Cloneable {
	/**
	 * The x coord.
	 */
	public float x;
	/**
	 * The y coord.
	 */
	public float y;

	/**
	 * The origin vector. (0, 0)
	 */
	public Vector() {
		this(0, 0);
	}

	/**
	 * Constructs a vector.
	 * 
	 * @param x
	 *            The x coord.
	 * @param y
	 *            The y coord.
	 */
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Same as <i>clone</i>, but without requiring casts or exception handling.
	 * 
	 * @return The copy.
	 */
	public Vector copy() {
		return new Vector(x, y);
	}

	/**
	 * Calculate the square of the distance between this vector and another vector.
	 * It is not an actual distance, but it is sufficient for comparing.
	 * 
	 * @param other
	 *            The other vector.
	 * @return The square of the distance between them.
	 */
	public float fastSquareDist(Vector other) {
		float xDiff = x - other.x;
		float yDiff = y - other.y;
		return xDiff * xDiff + yDiff * yDiff;
	}

	/**
	 * Calculate the square of the distance between this vector and another vector.
	 * It is not an actual distance, but it is sufficient for comparing.
	 * 
	 * @param otherX
	 *            The x coord of the other vector.
	 * @param otherY
	 *            The y coord of the other vector.
	 * @return The square of the distance between them.
	 */
	public float fastSquareDist(float otherX, float otherY) {
		float xDiff = x - otherX;
		float yDiff = y - otherY;
		return xDiff * xDiff + yDiff * yDiff;
	}

	/**
	 * Rotate the vector around the origin.
	 * 
	 * @param rotation
	 *            The amount of rotation, clockwise, in degrees.
	 */
	public void rotate(float rotation) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("rotate of Vector not yet implemented.");
	}

	/**
	 * Add another vector to this one.
	 * 
	 * @param other
	 *            The other vector.
	 */
	public void add(Vector other) {
		x += other.x;
		y += other.y;
	}

	/**
	 * Subtract another vector from this one.
	 * 
	 * @param other
	 *            The other vector.
	 */
	public void sub(Vector other) {
		x -= other.x;
		y -= other.y;
	}

	/**
	 * Scale this vector.
	 * 
	 * @param scale
	 *            The amount to scale.
	 */
	public void mult(float scale) {
		x *= scale;
		y *= scale;
	}

	/**
	 * Calculate the dot product of this vector and another one. Used for quickly comparing
	 * angles, and checking for paralell or perpendicular vectors.
	 * 
	 * @param other
	 *            The other vector.
	 * @return x1 * x2 + y1 * y2.
	 */
	public float dot(Vector other) {
		return x * other.x + y * other.y;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
