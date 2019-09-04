package core;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import privateUtil.Util;
import util.FileParsingException;
import util.ImageDelegate;

/**
 * A nested grouping of layers.
 *
 * @param <IMG>
 *            The IMG param of the parent MapFile. See MapFile for more info.
 */
public class LayerGroup<IMG> extends Layer {
	/**
	 * The sub-layers. Cannot be null.
	 */
	public ArrayList<Layer> layers;

	LayerGroup(Element element, MapFile<IMG> parent, ImageDelegate<IMG> delegate) {
		super(element);
		layers = parseLayers(element, parent, delegate);
	}

	/**
	 * Manually create a LayerGroup instance. No fields are initialized.
	 */
	public LayerGroup() {}

	static <IMG> ArrayList<Layer> parseLayers(Element element, MapFile<IMG> parent, ImageDelegate<IMG> delegate) {
		ArrayList<Layer> layers = new ArrayList<>();
		NodeList allNodes = element.getChildNodes();
		for (int i = 0; i < allNodes.getLength(); i++) {
			Node node = allNodes.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			Element layer = (Element) node;

			String name = layer.getNodeName();
			if (name.equals("layer")) {
				layers.add(new TileLayer<IMG>(layer, parent));
			} else if (name.equals("objectgroup")) {
				layers.add(new ObjectLayer<IMG>(layer, parent));
			} else if (name.equals("group")) {
				layers.add(new LayerGroup<>(layer, parent, delegate));
			} else if (name.equals("imagelayer")) {
				layers.add(new ImageLayer<>(layer, delegate));
			} else if (name.equals("tileset")) {
				if (layers.size() != 0) {
					throw new FileParsingException(Util.getFullXmlPath(layer) + ": Found 'tileset' element after layer elements");
				}
			} else {
				throw new FileParsingException("Unknown element type: '" + name + "'");
			}
		}

		return layers;
	}

	/**
	 * Search the direct descendents of this group for a layer with the given name.
	 * 
	 * @param name
	 *            The name of the layer to find.
	 * @return The layer, or <i>null</i> if no matching layer is found.
	 */
	public Layer getLayerByName(String name) {
		for (Layer layer : layers) {
			if (layer.name.equals(name)) {
				return layer;
			}
		}
		return null;
	}

	/**
	 * Same as <i>getLayerByName</i>, but also searches sub-groups recursively.
	 * 
	 * @param name
	 *            The name of the layer to find.
	 * @return The layer, or <i>null</i> if no matching layer is found.
	 */
	public Layer getLayerByNameRecursive(String name) {
		for (Layer layer : layers) {
			if (layer.name.equals(name)) {
				return layer;
			}
			if (layer instanceof LayerGroup) {
				// Technically, we can't know for sure that the layer's IMG type is the same as our IMG type.
				@SuppressWarnings("unchecked")
				Layer recurse = ((LayerGroup<IMG>) layer).getLayerByNameRecursive(name);
				if (recurse != null) {
					return recurse;
				}
			}
		}
		return null;
	}
}
