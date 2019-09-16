package util;

/**
 * Exception used by the TMXObject class when one of the convenience casting functions fail.
 *
 */
public class TMXObjectCastException extends ClassCastException {

	/**
	 * @param objectName
	 *            The name of the object that caused the error.
	 * @param objectId
	 *            The unique id of the object.
	 * @param className
	 *            The type of object the user requested.
	 */
	public TMXObjectCastException(String objectName, int objectId, String className) {
		super(formatString(objectName, objectId, className));
	}

	private static String formatString(String objectName, int objectId, String className) {
		char firstChar = Character.toLowerCase(className.charAt(0));
		String article = "a";
		if (firstChar == 'a' || firstChar == 'e' || firstChar == 'o' || firstChar == 'u') {
			article = "an";
		}

		return "Object '" + objectName + "' (" + objectId + ") is not " + article + " " + className + ".";
	}
}
