package core;

public class TMXColor {

	public int red;
	public int green;
	public int blue;
	public int alpha;

	public TMXColor() {
		this(0, 0, 0, 255);
	}

	public TMXColor(int red, int blue, int green) {
		this(red, blue, green, 255);
	}

	public TMXColor(int red, int blue, int green, int alpha) {
		this.red = red;
		this.blue = blue;
		this.green = green;
		this.alpha = alpha;
	}
}
