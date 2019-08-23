package core;

import org.w3c.dom.Element;
import privateUtil.Util;
import util.AttributeParsingErrorException;
import util.Vector;

public class CollisionCircle extends CollisionBounds {
	private Vector center;
	private float radius;

	public CollisionCircle(Element data) {
		super(data, 1);
		float x = Util.getFloatAttribute(data, "x");
		float y = Util.getFloatAttribute(data, "y");
		float width = Util.getFloatAttribute(data, "width", 0);
		if (width < 0) {
			throw new AttributeParsingErrorException(data, "width", "Value cannot be negative", width);
		}
		float height = Util.getFloatAttribute(data, "height", 0);
		if (height != width) {
			throw new AttributeParsingErrorException(data, "height", "Must be equal to 'width' attribute", height);
		}
		radius = width;
		center.x = x + radius / 2;
		center.y = y + radius / 2;
	}

	public CollisionCircle(float x, float y, float radius) {
		super(1);
		center = new Vector(x, y);
		this.radius = radius;
		if (radius < 0) {
			throw new RuntimeException();
		}
	}

	@Override
	public CollisionBounds copy() {
		return new CollisionCircle(center.x, center.y, radius);
	}

	@Override
	protected void setupBounds() {
		minX = (int) Math.floor(center.x - radius);
		minY = (int) Math.floor(center.y - radius);
		maxX = (int) Math.ceil(center.x + radius);
		maxY = (int) Math.ceil(center.y + radius);
	}

	@Override
	protected boolean fullCollisionCheck(CollisionBounds other) {
		if (other instanceof CollisionCircle) {
			CollisionCircle otherCircle = (CollisionCircle) other;
			float sqDist = center.fastSquareDist(otherCircle.center);
			float combinedRadius = radius + otherCircle.radius;
			return (sqDist <= combinedRadius * combinedRadius);
		}

		// Only other option
		assert (other instanceof CollisionBox);
		CollisionBox otherBox = (CollisionBox) other;

		return circleAlignedBoxCollisionCheck(center, radius, otherBox.getX(), otherBox.getY(), otherBox.getWidth(), otherBox.getHeight());
	}

	public Vector getCenter() {
		return center.copy();
	}

	public float getCenterX() {
		return center.x;
	}

	public float getCenterY() {
		return center.y;
	}

	public float getRadius() {
		return radius;
	}

	public void setCenter(Vector value) {
		center.x = value.x;
		center.y = value.y;
		invalidate();
	}

	public void setRadius(float value) {
		radius = value;
		invalidate();
	}

	public void setCenterX(float value) {
		center.x = value;
		invalidate();
	}

	public void setCenterY(float value) {
		center.y = value;
		invalidate();
	}

	@Override
	public void changeCenter(Vector value) {
		center.x += value.x;
		center.y += value.y;
		invalidate();
	}

	@Override
	public void changeCenterX(float value) {
		center.x += value;
		invalidate();
	}

	@Override
	public void changeCenterY(float value) {
		center.y += value;
		invalidate();
	}

	public static boolean circleAlignedBoxCollisionCheck(Vector center, float radius, float boxX, float boxY, float boxWidth, float boxHeight) {
		// Case 1: The circle's center is inside the rectangle.
		if (center.x > boxX && center.x < boxX + boxWidth) {
			if (center.y > boxY && center.y < boxY + boxHeight) {
				return true;
			}
		}

		// Case 2: An edge (and only an edge) is inside the circle.
		// Since the rectangle is aligned to the grid, we just have to show that the center is perpendicular
		// to one of the sides, and the radius overlaps with the nearest edge.
		if (center.x > boxX && center.x < boxX + boxWidth) {
			// The circle is directly above/below.
			// Use the "min > other.max || other.min > max" patern.
			return !(center.y - radius > boxY + boxHeight || boxY > center.y + radius);
		}
		if (center.y > boxY && center.y < boxY + boxHeight) {
			return !(center.x - radius > boxX + boxWidth || boxX > center.x + radius);
		}

		// Case 3: An edge and a corner are inside the circle.
		// Since the rectangle is aligned to the grid, it's easy to find the closest corner and just
		// check that. To check for the closest corner, we guess and compare the distance to the corner
		// with the length of the edge. If we're past the half-way mark, then the other corner is closer.
		float cornerX = boxX;;
		if (Math.abs(center.x - cornerX) > boxWidth / 2) {
			cornerX = boxX + boxWidth;
		}
		float cornerY = boxY;
		if (Math.abs(center.y - cornerY) > boxHeight / 2) {
			cornerY = boxY + boxHeight;
		}
		return (center.fastSquareDist(cornerX, cornerY) < radius * radius);
	}
}