package util;

import org.w3c.dom.Element;
import privateUtil.Util;

/**
 * This exception is thrown when an attribute could not be parsed.
 */
public class AttributeParsingErrorException extends FileParsingException {

	/**
	 * @param element
	 *            The element containing this attribute.
	 * @param name
	 *            The name of the attribute.
	 * @param cause
	 *            An exception raised while parsing the attribute.
	 */
	public AttributeParsingErrorException(Element element, String name, Throwable cause) {
		super("Error while parsing '" + name + "' attribute of element '" + Util.getFullXmlPath(element) + "'", cause);
	}

	/**
	 * @param element
	 *            The element containing this attribute.
	 * @param name
	 *            The name of the attribute.
	 * @param description
	 *            A short description of the error.
	 * @param badValue
	 *            The value that caused the error.
	 */
	public AttributeParsingErrorException(Element element, String name, String description, Object badValue) {
		super("Error while parsing '" + name + "' attribute of element '" + Util.getFullXmlPath(element) + "': " + description + " (" + badValue.toString() + ")");
	}

	/**
	 * 
	 * @param element
	 *            The element containing this attribute.
	 * @param name
	 *            The name of the attribute.
	 * @param description
	 *            A short description of the error.
	 * @param badValue
	 *            The value that caused the error.
	 * @param cause
	 *            An exception raised while parsing the attribute.
	 */
	public AttributeParsingErrorException(Element element, String name, String description, Object badValue, Throwable cause) {
		super("Error while parsing '" + name + "' attribute of element '" + Util.getFullXmlPath(element) + "': " + description + " (" + badValue.toString() + ")", cause);
	}
}