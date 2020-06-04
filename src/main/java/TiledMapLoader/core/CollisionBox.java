package TiledMapLoader.core;

import org.w3c.dom.Element;
import TiledMapLoader.privateUtil.Util;
import TiledMapLoader.util.AttributeParsingErrorException;
import TiledMapLoader.util.Vector;

public class CollisionBox extends CollisionBounds {
	private float x;
	private float y;
	private float width;
	private float height;

	public CollisionBox(Element data) {
		super(data, 0);
		x = Util.getFloatAttribute(data, "x");
		y = Util.getFloatAttribute(data, "y");
		width = Util.getFloatAttribute(data, "width", 0);
		if (width < 0) {
			throw new AttributeParsingErrorException(data, "width", "Value cannot be negative", width);
		}
		height = Util.getFloatAttribute(data, "height", 0);
		if (height < 0) {
			throw new AttributeParsingErrorException(data, "height", "Value cannot be negative", height);
		}
	}

	public CollisionBox(float x, float y, float width, float height) {
		super(0);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		assert (width >= 0);
		assert (height >= 0);
	}

	@Override
	public CollisionBounds copy() {
		return new CollisionBox(x, y, width, height);
	}

	@Override
	protected void setupBounds() {
		minX = (int) Math.floor(x);
		maxX = (int) Math.ceil(x + width);
		minY = (int) Math.floor(y);
		maxY = (int) Math.ceil(y + height);
	}

	@Override
	protected boolean fullCollisionCheck(CollisionBounds other) {
		// This has priority 0, the lowest priority, so it only has to worry about colliding with itself.
		assert (other instanceof CollisionBox);
		CollisionBox otherBox = (CollisionBox) other;

		if (x > otherBox.x + otherBox.width || otherBox.x > x + width) {
			return false;
		}
		if (y > otherBox.y + otherBox.height || otherBox.y > y + height) {
			return false;
		}
		return true;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public void setX(float value) {
		x = value;
		invalidate();
	}

	public void setY(float value) {
		y = value;
		invalidate();
	}

	public void setWidth(float value) {
		width = value;
		invalidate();
	}

	public void setHeight(float value) {
		height = value;
		invalidate();
	}

	@Override
	public void changeCenter(Vector value) {
		x += value.x;
		y += value.y;
		invalidate();
	}

	@Override
	public void changeCenterX(float value) {
		x += value;
		invalidate();
	}

	@Override
	public void changeCenterY(float value) {
		y += value;
		invalidate();
	}
}