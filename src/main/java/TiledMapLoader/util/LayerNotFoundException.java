package TiledMapLoader.util;

import java.util.ArrayList;
import java.util.HashSet;
import TiledMapLoader.core.Layer;

/**
 * Thrown by getLayerByName if no matches are found.
 */
public class LayerNotFoundException extends RuntimeException {

	/**
	 * @param name
	 *            The name or type that was searched for.
	 * @param searchedLayers
	 *            The layers that were searched.
	 * @param isRecursive
	 *            Whether the layers were searched recursively.
	 */
	public LayerNotFoundException(String name, ArrayList<Layer> searchedLayers, boolean isRecursive) {
		super("No layer named '" + name + "' found. Searched layer names: " + formatSearch(searchedLayers, isRecursive));
	}

	private static String formatSearch(ArrayList<Layer> searchedLayers, boolean isRecursive) {
		HashSet<String> names = new HashSet<>();
		for (Layer layer : searchedLayers) {
			names.add(layer.name);
			if (isRecursive) {
				// TODO: Handle recursion.
				names.add("<More layers not shown; recursive listing not implemented yet.>");
			}
		}
		return names.toString();
	}
}
