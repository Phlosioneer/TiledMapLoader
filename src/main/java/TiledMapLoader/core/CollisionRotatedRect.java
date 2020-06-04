package TiledMapLoader.core;

import org.w3c.dom.Element;
import TiledMapLoader.privateUtil.Util;
import TiledMapLoader.util.AttributeParsingErrorException;
import TiledMapLoader.util.Vector;

public class CollisionRotatedRect extends CollisionBounds {
	// Core info
	private Vector center;
	private float width;
	private float height;
	private float rotation; // radians

	// Cached for collision detection. Corners are stored in order of adjacency,
	// so that consecutive pairs are always parallel. Put another way, line([0], [1])
	// and line([2], [3]) are parallel; similarly with line([1], [2]) and line([3], [0]).
	// A boolean cacheIsValid is used to avoid re-allocating memory for the
	// arrays and the vectors.
	//
	// axes[0] corresponds to line([0], [1]). axes[1] corresponds to line([1], [2]).
	private Vector[] corners;
	private Vector[] axes;
	private boolean cacheIsValid;

	CollisionRotatedRect(Element data) {
		super(data, 10);
		float x = Util.getFloatAttribute(data, "x");
		float y = Util.getFloatAttribute(data, "y");
		width = Util.getFloatAttribute(data, "width", 0);
		if (width < 0) {
			throw new AttributeParsingErrorException(data, "width", "Value cannot be negative", width);
		}
		height = Util.getFloatAttribute(data, "height", 0);
		if (height < 0) {
			throw new AttributeParsingErrorException(data, "height", "Value cannot be negative", height);
		}
		float degreeRotation = Util.getFloatAttribute(data, "rotation");
		if (degreeRotation == 0) {
			throw new RuntimeException("Internal error: Used a CollisionRotatedRect when a CollisionBox would have been sufficient");
		}
		rotation = (float) Math.toRadians(degreeRotation);

		// Rotation in tmx happens around the origin of the tile. For non-tile objects, that's the top-left corner.
		center = new Vector(-width / 2, -height / 2);
		center.rotate(rotation);

		// Translate to the final coords
		center.x += x;
		center.y += y;

		cacheIsValid = false;
		Vector v = new Vector();
		corners = new Vector[]{
			v.copy(), v.copy(), v.copy(), v.copy()
		};
		axes = new Vector[]{
			v.copy(), v
		};
	}

	public CollisionRotatedRect(float x, float y, float width, float height, float rotation) {
		super(10);
		center = new Vector(x, y);
		this.width = width;
		this.height = height;
		this.rotation = rotation;
		cacheIsValid = false;
		Vector v = new Vector();
		corners = new Vector[]{
			v.copy(), v.copy(), v.copy(), v.copy()
		};
		axes = new Vector[]{
			v.copy(), v
		};
	}

	@Override
	public CollisionBounds copy() {
		return new CollisionRotatedRect(center.x, center.y, width, height, rotation);
	}

	@Override
	protected void setupBounds() {
		if (!cacheIsValid) {
			calculateBounds();
		}

		float minX_float = corners[0].x;
		float minY_float = corners[0].y;
		float maxX_float = minX_float;
		float maxY_float = minY_float;
		for (int i = 1; i < corners.length; i++) {
			Vector current = corners[i];
			minX_float = Math.min(minX_float, current.x);
			minY_float = Math.min(minY_float, current.y);
			maxX_float = Math.max(maxX_float, current.x);
			maxY_float = Math.max(maxY_float, current.y);
		}

		minX = (int) Math.floor(minX_float);
		minY = (int) Math.floor(minY_float);
		maxX = (int) Math.ceil(maxX_float);
		maxY = (int) Math.ceil(maxY_float);
	}

	@Override
	protected void invalidate() {
		super.invalidate();
		cacheIsValid = false;
	}

	private void calculateBounds() {
		// Make Vectors for each corner relative to the center.
		// We re-use the existing corner vectors rather than re-allocating.
		corners[0].x = -width / 2;
		corners[0].y = -height / 2;

		corners[1].x = width / 2;
		corners[1].y = -height / 2;

		corners[2].x = width / 2;
		corners[2].y = height / 2;

		corners[3].x = -width / 2;
		corners[3].y = height / 2;

		// Rotate and translate the vectors.
		for (Vector corner : corners) {
			corner.rotate(rotation);
			corner.add(center);
		}

		// Save two axes. The position doesn't matter, and the order of subtraction
		// doesn't matter.
		axes[0].x = corners[0].x;
		axes[0].y = corners[0].y;
		axes[1].x = corners[1].x;
		axes[1].y = corners[1].y;
		axes[0].sub(corners[1]);
		axes[1].sub(corners[2]);

		cacheIsValid = true;
	}

	public float getRotation() {
		return rotation;
	}

	public float getCenterX() {
		return center.x;
	}

	public float getCenterY() {
		return center.y;
	}

	public void setRotation(float value) {
		invalidate();
		rotation = value;
	}

	public void setCenterX(float x) {
		changeCenterX(x - center.x);
	}

	public void setCenterY(float y) {
		changeCenterY(y - center.y);
	}

	public void changeRotation(float delta) {
		invalidate();
		rotation += delta;
	}

	@Override
	public void changeCenterX(float delta) {
		center.x += delta;
		if (cacheIsValid) {
			for (Vector corner : corners) {
				corner.x += delta;
			}
		}
		super.invalidate();
	}

	@Override
	public void changeCenterY(float delta) {
		center.y += delta;
		if (cacheIsValid) {
			for (Vector corner : corners) {
				corner.y += delta;
			}
		}
		super.invalidate();
	}

	public void setCenter(Vector value) {
		changeCenterX(value.x - center.x);
		changeCenterY(value.y - center.y);
	}

	@Override
	public void changeCenter(Vector delta) {
		changeCenterX(delta.x);
		changeCenterY(delta.y);
	}

	public Vector getCenter() {
		return center.copy();
	}

	@Override
	protected boolean fullCollisionCheck(CollisionBounds other) {
		if (other instanceof CollisionCircle) {
			CollisionCircle otherCircle = (CollisionCircle) other;

			// Transform the circle into this rectangle's coords.
			Vector otherCenter = otherCircle.getCenter();
			otherCenter.sub(center);
			otherCenter.mult(-1);
			otherCenter.rotate(-1 * rotation);
			otherCenter.add(center);

			// Now this can be treated as a box aligned to the grid.
			return CollisionCircle.circleAlignedBoxCollisionCheck(otherCenter, otherCircle.getRadius(), center.x - width / 2, center.y - height / 2, width, height);
		}

		// After this point, we need our cached calculations.
		if (cacheIsValid) {
			calculateBounds();
		}

		Vector[] otherCorners;
		if (other instanceof CollisionBox) {
			CollisionBox otherBox = (CollisionBox) other;
			float otherX = otherBox.getX();
			float otherY = otherBox.getY();
			float otherWidth = otherBox.getWidth();
			float otherHeight = otherBox.getHeight();

			// Checking the two "axes" of the other rectangle is easy; it's just a standard
			// bounds check.
			float minX_float = corners[0].x;
			float minY_float = corners[0].y;
			float maxX_float = minX_float;
			float maxY_float = minY_float;
			for (int i = 1; i < corners.length; i++) {
				Vector corner = corners[i];
				minX_float = Math.min(minX_float, corner.x);
				minY_float = Math.min(minY_float, corner.y);
				maxX_float = Math.max(maxX_float, corner.x);
				maxY_float = Math.max(maxY_float, corner.y);
			}
			// Conditions follow the "min > other.max || other.min > max" pattern.
			if (minX_float > otherX + otherWidth || otherX > maxX_float) {
				return false;
			}
			if (minY_float > otherY + otherHeight || otherY > maxY_float) {
				return false;
			}

			// We'll need the box's corners as Vectors for the next checks.
			otherCorners = new Vector[]{
				new Vector(otherX, otherY), new Vector(otherX + otherWidth, otherY),
				new Vector(otherX + otherWidth, otherY + otherHeight), new Vector(otherX, otherY + otherHeight)
			};
		} else {
			// The other object has to be a rotating box.
			assert (other instanceof CollisionRotatedRect);
			CollisionRotatedRect otherRotRect = (CollisionRotatedRect) other;
			otherCorners = otherRotRect.corners;

			// Run the checks for the other box's axis.
			for (Vector otherAxis : otherRotRect.axes) {
				if (!doSeparatingAxisCheck(otherAxis, otherCorners, corners)) {
					return false;
				}
			}
		}

		// Run the checks for this box's axis.
		for (Vector axis : axes) {
			if (!doSeparatingAxisCheck(axis, corners, otherCorners)) {
				return false;
			}
		}

		// No separating axis, so they must be colliding.
		return true;
	}

	// Returns true if the two rects overlap when projected onto the given axis.
	private static boolean doSeparatingAxisCheck(Vector axis, Vector[] corners, Vector[] otherCorners) {
		// For our axes, we don't need to check all four corners. By definition, for each axis,
		// two of the sides of our rect are parallel to it. So we only need to use two diagonal
		// corners, and that will always give us all the unique dot products for both of our axes.
		Vector diag1 = corners[0];
		Vector diag2 = corners[2];

		float dot1 = axis.dot(diag1);
		float dot2 = axis.dot(diag2);
		float minDot = Math.min(dot1, dot2);
		float maxDot = Math.max(dot1, dot2);

		// For the other rect, we have to check the pojection of each corner.
		float otherMinDot = axis.dot(otherCorners[0]);
		float otherMaxDot = otherMinDot;
		for (int i = 1; i < otherCorners.length; i++) {
			float currentDot = axis.dot(otherCorners[i]);
			otherMinDot = Math.min(otherMinDot, currentDot);
			otherMaxDot = Math.max(otherMaxDot, currentDot);
		}

		// Now check for overlap.
		return !(minDot > otherMaxDot || otherMinDot > maxDot);
	}
}
