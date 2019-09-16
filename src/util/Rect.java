package util;

// Note: This class is immutable.
public final class Rect implements Cloneable {
	public final int x;
	public final int y;
	public final int width;
	public final int height;

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
