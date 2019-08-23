package util;

public class Vector {
	public float x;
	public float y;

	public Vector() {
		this(0, 0);
	}

	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector copy() {
		return new Vector(x, y);
	}

	public float fastSquareDist(Vector other) {
		float xDiff = x - other.x;
		float yDiff = y - other.y;
		return xDiff * xDiff + yDiff * yDiff;
	}

	public float fastSquareDist(float otherX, float otherY) {
		float xDiff = x - otherX;
		float yDiff = y - otherY;
		return xDiff * xDiff + yDiff * yDiff;
	}

	public void rotate(float rotation) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("rotate of Vector not yet implemented.");
	}

	public void add(Vector other) {
		x += other.x;
		y += other.y;
	}

	public void sub(Vector other) {
		x -= other.x;
		y -= other.y;
	}

	public void mult(float scale) {
		x *= scale;
		y *= scale;
	}

	public float dot(Vector other) {
		return x * other.x + y * other.y;
	}
}
