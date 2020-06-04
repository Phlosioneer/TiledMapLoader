package TiledMapLoader.util;

/**
 * Exception used by the Layer class when one of the convenience casting functions fail.
 *
 */
public class LayerCastException extends ClassCastException {

	/**
	 * 
	 * @param layerName
	 *            The name of the layer that caused the error.
	 * @param layerId
	 *            The unique ID of the layer that caused the error.
	 * @param className
	 *            The type of layer that the user requested.
	 */
	public LayerCastException(String layerName, int layerId, String className) {
		super(formatString(layerName, layerId, className));
	}

	private static String formatString(String layerName, int layerId, String className) {
		char firstChar = Character.toLowerCase(className.charAt(0));
		String article = "a";
		if (firstChar == 'a' || firstChar == 'e' || firstChar == 'o' || firstChar == 'u') {
			article = "an";
		}

		return "Layer '" + layerName + "' (" + layerId + ") is not " + article + " " + className + ".";
	}
}
