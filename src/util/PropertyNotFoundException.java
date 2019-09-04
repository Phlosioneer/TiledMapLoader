package util;

/**
 * Thrown by TMXProperty's <i>get___(String)</i> methods if the property is not found.
 * 
 * @author Phlosioneer
 *
 */
public class PropertyNotFoundException extends RuntimeException {

	/**
	 * 
	 * @param propertyName
	 *            The name of the missing property.
	 * @param propertyType
	 *            The type of the property.
	 */
	public PropertyNotFoundException(String propertyName, String propertyType) {
		super("No " + propertyType + " property found named '" + propertyName + "'.");
	}

	/**
	 * @param propertyName
	 *            The name of the missing property.
	 * @param expectedPropertyType
	 *            The type the user requested.
	 * @param actualPropertyType
	 *            The type of the actual property.
	 */
	public PropertyNotFoundException(String propertyName, String expectedPropertyType, String actualPropertyType) {
		super("'" + propertyName + "' property accessed as " + expectedPropertyType + ", but is " + actualPropertyType);
	}
}
