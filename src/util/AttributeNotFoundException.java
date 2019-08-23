package util;

import org.w3c.dom.Element;
import privateUtil.Util;

public class AttributeNotFoundException extends RuntimeException {

	public AttributeNotFoundException(Element element, String name) {
		super("Element '" + Util.getFullXmlPath(element) + "' does not have attribute '" + name + "'");
	}
}
