package core;

import java.util.ArrayList;
import org.w3c.dom.Element;
import privateUtil.Util;
import util.AttributeParsingErrorException;

/**
 * A layer containing an array of objects.
 *
 * @param <IMG>
 *            See the MapFile class.
 */
public class ObjectLayer<IMG> extends Layer {
	/**
	 * The objects in this layer. Cannot be null.
	 */
	public ArrayList<TMXObject> objects;

	ObjectLayer(Element element, MapFile<IMG> parent) {
		super(element);
		String drawOrder = Util.getStringAttribute(element, "draworder", "index");
		if (!drawOrder.equals("index")) {
			throw new AttributeParsingErrorException(element, "draworder", "Only 'index' is supported", drawOrder);
		}

		objects = new ArrayList<>();
		for (Element objectData : Util.getAllTags(element, "object")) {
			if (objectData.hasAttribute("gid")) {
				objects.add(new TileObject<>(objectData, parent));
			} else if (Util.hasChildNode(objectData, "text")) {
				objects.add(new TextObject(objectData));
			} else {
				objects.add(new ShapeObject(objectData));
			}
		}
	}

	public TMXObject getObjectByName(String name) {
		for (TMXObject object : objects) {
			if (object.name.equals(name)) {
				return object;
			}
		}
		return null;
	}

	public ArrayList<TMXObject> getObjectsByName(String name) {
		ArrayList<TMXObject> ret = new ArrayList<>();
		for (TMXObject object : objects) {
			if (object.name.equals(name)) {
				ret.add(object);
			}
		}
		return ret;
	}
}
