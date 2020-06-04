package TiledMapLoader.core;

import org.w3c.dom.Element;
import TiledMapLoader.privateUtil.Util;
import TiledMapLoader.util.Vector;

public abstract class CollisionBounds {
	public String name;

	// This is an integer that is used to decide which object in a
	// collision handles the check. The fullCollisionCheck on the object
	// with a higher priority will be called. If there is a tie, the
	// instance executing isCollidingWith will use its own implementation
	// of fullCollisionCheck.
	private int boundsCheckPriority;

	// These are cached values, though they're usually constant.
	private boolean boundsSet;
	int minX;
	int minY;
	int maxX;
	int maxY;

	protected CollisionBounds(Element element, int priority) {
		name = Util.getStringAttribute(element, "name", null);
		boundsSet = false;
		boundsCheckPriority = priority;
	}

	protected CollisionBounds(int priority) {
		name = null;
		boundsSet = false;
		boundsCheckPriority = priority;
	}

	public static CollisionBounds parseCollisionElement(Element element) {

		if (element.hasAttribute("rotation") && Util.getFloatAttribute(element, "rotation") != 0) {
			return new CollisionRotatedRect(element);
		} else if (element.getChildNodes().getLength() != 0) {
			return new CollisionCircle(element);
		} else {
			return new CollisionBox(element);
		}
	}

	protected abstract void setupBounds();

	public final boolean isCollidingWith(CollisionBounds other) {
		if (!boundsSet) {
			this.setupBounds();
			assert (minX <= maxX);
			assert (minY <= maxY);
			boundsSet = true;
		}
		if (!other.boundsSet) {
			other.setupBounds();
			assert (other.minX <= other.maxX);
			assert (other.minY <= other.maxY);
			other.boundsSet = true;
		}

		if (minX > other.maxX || other.minX > maxX) {
			return false;
		}
		if (minY > other.maxY || other.minY > maxY) {
			return false;
		}
		if (other.boundsCheckPriority > boundsCheckPriority) {
			return other.fullCollisionCheck(this);
		} else {
			return this.fullCollisionCheck(other);
		}
	}

	protected abstract boolean fullCollisionCheck(CollisionBounds other);

	protected void invalidate() {
		boundsSet = false;
	}

	public abstract CollisionBounds copy();

	public abstract void changeCenter(Vector value);

	public abstract void changeCenterX(float value);

	public abstract void changeCenterY(float value);
}
