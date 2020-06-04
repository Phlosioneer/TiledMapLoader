package TiledMapLoader.core;

import java.util.ArrayList;
import org.w3c.dom.Element;
import TiledMapLoader.privateUtil.Util;
import TiledMapLoader.util.AttributeParsingErrorException;
import TiledMapLoader.util.ImageTransform;
import TiledMapLoader.util.ObjectNotFoundException;
import TiledMapLoader.util.Rect;
import TiledMapLoader.util.ResourceLoaderDelegate;
import TiledMapLoader.util.Vector;

/**
 * A layer containing an array of objects.
 *
 * @param <IMG>
 *            The IMG param of the parent MapFile. See MapFile for more info.
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

	/**
	 * Manually create an ObjectLayer instance. No fields are initialized.
	 */
	public ObjectLayer() {}

	/**
	 * <p>
	 * Search all member objects for one with the given name.
	 * </p>
	 * 
	 * <p>
	 * If multiple objects have the same name, the first one found is returned.
	 * </p>
	 * 
	 * @param name
	 *            The object name to look for.
	 * @return The object. Cannot be null.
	 * @throws ObjectNotFoundException
	 *             If no matching object was found.
	 */
	public TMXObject getObjectByName(String name) {
		for (TMXObject object : objects) {
			if (object.name.equals(name)) {
				return object;
			}
		}
		throw new ObjectNotFoundException(name);
	}

	/**
	 * Return all member objects with the given name.
	 * 
	 * @param name
	 *            The object name to look for.
	 * @return The objects. Cannot be null; an empty list is returned if no objects were found.
	 */
	public ArrayList<TMXObject> getObjectsByName(String name) {
		ArrayList<TMXObject> ret = new ArrayList<>();
		for (TMXObject object : objects) {
			if (object.name.equals(name)) {
				ret.add(object);
			}
		}
		return ret;
	}

	/**
	 * Return all member objects with the given type.
	 * 
	 * @param type
	 *            The type name to look for.
	 * @return The objects. Cannot be null; an empty list is returned if no objects were found.
	 */
	public ArrayList<TMXObject> getObjectsByType(String type) {
		ArrayList<TMXObject> ret = new ArrayList<>();
		for (TMXObject object : objects) {
			if (object.type.equals(type)) {
				ret.add(object);
			}
		}
		return ret;
	}

	@Override
	public <IMG2> IMG2 renderToImage(Rect pixelBounds, IMG2 baseImage, Vector renderOffset, float opacity, ResourceLoaderDelegate<IMG2> delegate) {
		IMG2 mutableBaseImage = baseImage;
		// TODO: Respect pixelBounds
		for (TMXObject object : objects) {
			if (!object.isVisible || object.isTile()) {
				continue;
			}
			int netX = Math.round(renderOffset.x + object.position.x);
			int netY = Math.round(renderOffset.y + object.position.y);
			float netOpacity = Util.combineOpacities(opacity, this.opacity);
			@SuppressWarnings("unchecked")
			IMG2 image = (IMG2) object.asTile().tile;
			ImageTransform transform = new ImageTransform(netX, netY, netOpacity);
			mutableBaseImage = delegate.composeOntoImage(mutableBaseImage, image, transform);
		}

		return mutableBaseImage;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		@SuppressWarnings("unchecked")
		ObjectLayer<IMG> ret = (ObjectLayer<IMG>) super.clone();
		ret.objects = new ArrayList<>(objects.size());
		for (TMXObject object : objects) {
			ret.objects.add((TMXObject) object.clone());
		}
		return ret;
	}
}
