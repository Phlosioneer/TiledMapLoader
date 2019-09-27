package core;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import privateUtil.Util;
import util.AttributeNotFoundException;
import util.FileParsingException;
import util.LayerNotFoundException;
import util.ResourceLoaderDelegate;
import util.Vector;

/**
 * A nested grouping of layers.
 *
 * @param <IMG>
 *            The IMG param of the parent MapFile. See MapFile for more info.
 */
public class LayerGroup<IMG> extends Layer {
	/**
	 * The sub-layers, bottom-first. Cannot be null.
	 */
	public ArrayList<Layer> layers;

	LayerGroup(Element element, MapFile<IMG> parent, ResourceLoaderDelegate<IMG> delegate) {
		super(element);
		this.init(element, parent, delegate);
	}

	LayerGroup(Element element, MapFile<IMG> parent, ResourceLoaderDelegate<IMG> delegate, boolean ignoreMissing) {
		super();
		try {
			Layer temp = new Layer(element) {};
			this.id = temp.id;
			this.isVisible = temp.isVisible;
			this.name = temp.name;
			this.offset = temp.offset;
			this.opacity = temp.opacity;
			this.properties = temp.properties;
		} catch (AttributeNotFoundException e) {
			if (ignoreMissing) {
				this.id = -1;
				this.isVisible = true;
				this.name = "root";
				this.offset = new Vector();
				this.opacity = 1;
				this.properties = null;
			} else {
				throw e;
			}
		}
		super.isVisible = true;
		this.init(element, parent, delegate);
	}

	private void init(Element element, MapFile<IMG> parent, ResourceLoaderDelegate<IMG> delegate) {
		layers = new ArrayList<>();
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
	}

	/**
	 * Manually create a LayerGroup instance. No fields are initialized.
	 */
	public LayerGroup() {}

	/**
	 * Search the direct descendents of this group for a layer with the given name.
	 * 
	 * @param name
	 *            The name of the layer to find.
	 * @return The layer. Cannot be null.
	 * @throws LayerNotFoundException
	 *             If no matching layer is found.
	 */
	public Layer getLayerByName(String name) {
		for (Layer layer : layers) {
			if (layer.name.equals(name)) {
				return layer;
			}
		}
		throw new LayerNotFoundException(name, layers, false);
	}

	/**
	 * Search the direct descendents of this group for a layer with the given name.
	 * 
	 * @param name
	 *            The name of the layer to find.
	 * @return The layers. Cannot be null; if no layers are found, an empty array is
	 *         returned.
	 */
	public ArrayList<Layer> getLayersByName(String name) {
		ArrayList<Layer> ret = new ArrayList<>();
		for (Layer layer : layers) {
			if (layer.name.equals(name)) {
				ret.add(layer);
			}
		}
		return ret;
	}

	/**
	 * Same as <i>getLayerByName</i>, but also searches sub-groups recursively.
	 * 
	 * @param name
	 *            The name of the layer to find.
	 * @return The layer. Cannot be null.
	 * @throws LayerNotFoundException
	 *             If no matching layer is found.
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
		throw new LayerNotFoundException(name, layers, true);
	}

	/**
	 * <p>
	 * Same as <i>getLayersByName</i>, but also searches sub-groups recursively.
	 * </p>
	 * 
	 * <p>
	 * If a LayerGroup with the given name is found, it is also recursed.
	 * </p>
	 * 
	 * @param name
	 *            The name of the layer to find.
	 * @return The layer. Cannot be null; if no layers are found, an empty array is returned.
	 */
	public ArrayList<Layer> getLayersByNameRecursive(String name) {
		ArrayList<Layer> ret = new ArrayList<>();
		for (Layer layer : layers) {
			if (layer.name.equals(name)) {
				ret.add(layer);
			}
			if (layer instanceof LayerGroup) {
				// Technically, we can't know for sure that the layer's IMG type is the same as our IMG type.
				@SuppressWarnings("unchecked")
				ArrayList<Layer> recurse = ((LayerGroup<IMG>) layer).getLayersByNameRecursive(name);
				ret.addAll(recurse);
			}
		}
		return ret;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		@SuppressWarnings("unchecked")
		LayerGroup<IMG> ret = (LayerGroup<IMG>) super.clone();
		ret.layers = new ArrayList<>(layers.size());
		for (Layer layer : layers) {
			ret.layers.add((Layer) layer.clone());
		}
		return ret;
	}
}
