package util;

/**
 * Thrown by getLayerByName if no matches are found.
 */
public class LayerNotFoundException extends RuntimeException {

	/**
	 * @param name
	 *            The name or type that was searched for.
	 */
	public LayerNotFoundException(String name) {
		super("No layer named '" + name + "' found.");
	}
}
