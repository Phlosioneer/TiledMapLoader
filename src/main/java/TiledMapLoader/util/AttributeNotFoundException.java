package TiledMapLoader.util;

import org.w3c.dom.Element;
import TiledMapLoader.privateUtil.Util;

/**
 * This error is raised if a required attribute is missing from a TMX file.
 *
 */
public class AttributeNotFoundException extends FileParsingException {

	/**
	 * @param element
	 *            The element object that is missing the attribute.
	 * @param name
	 *            The name of the attribute.
	 */
	public AttributeNotFoundException(Element element, String name) {
		super("Element '" + Util.getFullXmlPath(element) + "' does not have attribute '" + name + "'");
	}
}
