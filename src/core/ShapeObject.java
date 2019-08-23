package core;

import org.w3c.dom.Element;
import privateUtil.Util;
import util.AttributeParsingErrorException;
import util.Vector;

/**
 * An object that represents a shape, including Point, Rect, Ellipse,
 * Polygon, and Polyline.
 *
 */
public class ShapeObject extends TMXObject {
	/**
	 * 
	 */
	public static enum Shape {
		/**
		 * <i>position</i>, and <i>size</i> inherited from TMXObject are used to define the rectangle.
		 * <i>points</i> will be null.
		 */
		RECT,
		/**
		 * <i>position</i>, and <i>size</i> inherited from TMXObject are used to define the rectangle.
		 * <i>points</i> will be null.
		 */
		ELLIPSE,
		/**
		 * <i>position</i> inherited from TMXObject are used to define the point.
		 * <i>points</i> will be null. <i>size</i> is ignored.
		 */
		POINT,
		/**
		 * <p>
		 * The <i>points</i> array defines all the vertecies of the polygon, relative to <i>position</i> (inherited
		 * from TMXObject). The last and first vertex are connected.
		 * </p>
		 * 
		 * <p>
		 * The <i>points</i> array will always have at least 3 vertecies. The first vertex is always (0, 0).
		 * <i>size</i> is ignored.
		 */
		POLYGON,
		/**
		 * <p>
		 * Each pair of <i>points</i> defines a line, forming a chain. The points are relative to <i>position</i>
		 * (inherited from TMXObject).
		 * </p>
		 * 
		 * <p>
		 * The <i>points</i> array will always have at least 2 vertecies. The first vertex is always (0, 0).
		 * <i>size</i> is ignored.
		 */
		POLYLINE,
	}

	/**
	 * The type of shape this represents. For details on each type, see the
	 * documentation for the <i>Shape</i> enum.
	 */
	public Shape kind;
	/**
	 * If <i>kind</i> is RECT, POINT, or ELLIPSE, this is null.
	 */
	public Vector[] points;

	ShapeObject(Element element) {
		super(element);

		points = null;
		if (Util.hasChildNode(element, "point")) {
			kind = Shape.POINT;
		} else if (Util.hasChildNode(element, "ellipse")) {
			kind = Shape.ELLIPSE;
		} else if (Util.hasChildNode(element, "polygon")) {
			loadPoints(Util.getAllTags(element, "polygon").next());
			if (points.length < 3) {
				Element childElement = Util.getAllTags(element, "polygon").next();
				throw new AttributeParsingErrorException(childElement, "points", "Polygon with less than 3 points", Util.getStringAttribute(element, "points"));
			}
			kind = Shape.POLYGON;
		} else if (Util.hasChildNode(element, "polyline")) {
			loadPoints(Util.getAllTags(element, "polyline").next());
			kind = Shape.POLYLINE;
		} else {
			kind = Shape.RECT;
		}
	}

	private void loadPoints(Element element) {
		String unparsedPointString = Util.getStringAttribute(element, "points");
		if (unparsedPointString.isEmpty()) {
			points = new Vector[1];
			points[0] = new Vector();
			return;
		}

		String[] pointStrings = unparsedPointString.split(" ");

		// +1 for the implicit 0,0 starting point; and +1 because the last point will not have a space after it.
		int count = pointStrings.length + 2;
		points = new Vector[count];

		for (int i = 0; i < pointStrings.length; i++) {
			String[] coords = pointStrings[i].split(",", 2);
			if (coords.length != 2) {
				throw new AttributeParsingErrorException(element, "points", "Malformed point entry", pointStrings[i]);
			}

			try {
				float x = Float.parseFloat(coords[0]);
				float y = Float.parseFloat(coords[1]);
				points[i] = new Vector(x, y);
			} catch (NumberFormatException e) {
				throw new AttributeParsingErrorException(element, "points", e);
			}
		}
	}
}
