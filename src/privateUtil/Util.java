package privateUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import core.TMXColor;
import util.AttributeNotFoundException;
import util.AttributeParsingErrorException;

/**
 * Utility class containing static methods used in the rest of the library.
 *
 */
public abstract class Util {

	/**
	 * Load the given file into a Dom, then return the root element of the Dom. The filename should include
	 * the extension to the file.
	 * 
	 * @param stream
	 *            The TMX file to open.
	 * @return The root element of the Dom tree.
	 * @throws RuntimeException
	 *             when an IO error or Dom parsing error happens.
	 */
	public static Element loadXmlFile(InputStream stream) {
		BufferedInputStream bufferedInput = null;
		if (stream instanceof BufferedInputStream) {
			bufferedInput = (BufferedInputStream) stream;
		} else {
			bufferedInput = new BufferedInputStream(stream);
		}

		try {
			Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(bufferedInput);
			return dom.getDocumentElement();

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("ParserConfigurationException handler not yet written in constructor of MapFile.", e);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("SAXException handler not yet written in loadXmlFile of Util.", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("IOException handler not yet written in loadXmlFile of Util.", e);
		}
	}

	/**
	 * <p>
	 * Returns <i>true</i> if any of the children in of <i>element</i> are <i>childName</i> tags.
	 * </p>
	 * 
	 * <p>
	 * This method only searches the immediate children of <i>element</i>.
	 * </p>
	 * 
	 * @param element
	 *            The parent element.
	 * @param childName
	 *            The tag name to search for.
	 * @return True if at least one <i>childName</i> tag is found in <i>element</i>.
	 */
	public static boolean hasChildNode(Element element, String childName) {
		if (!element.hasChildNodes()) {
			return false;
		}
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeName().equals(childName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Returns the full path of <i>elemen</i> within the Dom tree.
	 * </p>
	 * 
	 * <p>
	 * Each part of the path is separated by colons. An element might have several children
	 * tags. The specific child in the path is referenced by its index in an array of children,
	 * starting at 0.
	 * </p>
	 * 
	 * <p>
	 * For example, for the tree:</br>
	 * &lt;parent&gt;</br>
	 * &nbsp;&nbsp;&lt;child1&gt;</br>
	 * &nbsp;&nbsp;&lt;child2&gt;</br>
	 * &lt;/parent&gt;</br>
	 * The paths for child1 and child2 are "root:parent[0]:child1" and "root:parent[1]:child2".
	 * </p>
	 * 
	 * <p>
	 * Paths always begin with "root:".
	 * </p>
	 * 
	 * @param element
	 *            The element to search.
	 * @return The full path to the element as a string.
	 */
	public static String getFullXmlPath(Element element) {
		StringBuilder builder = new StringBuilder();
		ArrayList<String> parentList = new ArrayList<>();
		ArrayList<String> positionList = new ArrayList<>();
		Node parent = element.getParentNode();
		Node child = element;
		while (parent != null) {
			parentList.add(parent.getNodeName());

			NodeList siblings = parent.getChildNodes();
			int position = -1;
			int positionSkipText = 0;
			for (int i = 0; i < siblings.getLength(); i++) {
				if (siblings.item(i) == child) {
					position = i;
					break;
				}

				// There's technically a Node.TEXT_NODE before and after every node.
				if (siblings.item(i).getNodeType() != Node.TEXT_NODE) {
					positionSkipText += 1;
				}
			}

			if (position == -1) {
				positionList.add("?");
			} else {
				positionList.add(Integer.toString(positionSkipText));
			}

			child = parent;
			parent = parent.getParentNode();
		}

		for (int i = parentList.size() - 1; i >= 0; i--) {
			builder.append(parentList.get(i));
			builder.append('[');
			builder.append(positionList.get(i));
			builder.append("]:");
		}

		builder.append(element.getNodeName());
		return builder.toString();
	}

	/**
	 * <p>
	 * Parse a color attribute from an element.
	 * </p>
	 * 
	 * <p>
	 * If the attribute does not exist, an AttributeNotFound error will be thrown.
	 * The color must be in the format "#RRGGBB" or "#RRGGBBAA".
	 * </p>
	 * 
	 * @param e
	 *            The parent element.
	 * @param name
	 *            The name of the attribute.
	 * @return The parsed color.
	 * @throws AttributeNotFoundException
	 *             When <i>e</i> doesn't contain a <i>name</i> attribute.
	 * @throws AttributeParsingErrorException
	 *             When the format is invalid.
	 */
	public static TMXColor getColorAttribute(Element e, String name) {
		String unparsed = getStringAttribute(e, name);
		return parseColor(e, name, unparsed);
	}

	/**
	 * <p>
	 * Parse a color attribute from an element.
	 * </p>
	 * 
	 * <p>
	 * If the attribute does not exist, <i>defaultColor</i> is returned.
	 * The color must be in the format "#RRGGBB" or "#RRGGBBAA".
	 * </p>
	 * 
	 * @param e
	 *            The parent element.
	 * @param name
	 *            The name of the attribute.
	 * @param defaultColor
	 *            The default value to return. Can be null.
	 * @return The parsed color or <i>defaultColor</i>.
	 * @throws AttributeNotFoundException
	 *             When <i>e</i> doesn't contain a <i>name</i> attribute.
	 * @throws AttributeParsingErrorException
	 *             When the format is invalid.
	 */
	public static TMXColor getColorAttribute(Element e, String name, TMXColor defaultColor) {
		String unparsed = getStringAttribute(e, name, null);
		if (unparsed == null) {
			return defaultColor;
		}
		return parseColor(e, name, unparsed);
	}

	private static TMXColor parseColor(Element e, String attributeName, String unparsedColor) {
		if (unparsedColor.charAt(0) != '#' || (unparsedColor.length() != 7 && unparsedColor.length() != 9)) {
			throw new AttributeParsingErrorException(e, attributeName, "Invalid color format; expected #RRGGBB or #RRGGBBAA.", unparsedColor);
		}

		int red = -1;
		int green = -1;
		int blue = -1;
		int alpha = -1;
		String redString = unparsedColor.substring(1, 3);
		String greenString = unparsedColor.substring(3, 5);
		String blueString = unparsedColor.substring(5, 7);
		String alphaString = null;
		if (unparsedColor.length() == 9) {
			alphaString = unparsedColor.substring(7, 9);
		}

		try {
			red = Integer.parseInt(redString, 16);
			green = Integer.parseInt(greenString, 16);
			blue = Integer.parseInt(blueString, 16);
			if (alphaString != null) {
				alpha = Integer.parseInt(alphaString, 16);
			} else {
				alpha = 0;
			}
		} catch (NumberFormatException exception) {
			// Figure out which one failed.
			if (red != -1) {
				throw new AttributeParsingErrorException(e, attributeName, "Invalid red value", redString, exception);
			}
			if (green != -1) {
				throw new AttributeParsingErrorException(e, attributeName, "Invalid green value", greenString, exception);
			}
			if (blue != -1) {
				throw new AttributeParsingErrorException(e, attributeName, "Invalid blue value", blueString, exception);
			}
		}

		return new TMXColor(red, blue, green, alpha);
	}

	/**
	 * <p>
	 * Get a string attribute from an element.
	 * </p>
	 * 
	 * <p>
	 * If the attribute does not exist, an AttributeNotFound error will be thrown.
	 * </p>
	 * 
	 * @param e
	 *            The parent element.
	 * @param name
	 *            The name of the attribute.
	 * @return The string.
	 * @throws AttributeNotFoundException
	 *             When <i>e</i> doesn't contain a <i>name</i> attribute.
	 */
	public static String getStringAttribute(Element e, String name) {
		String ret = Util.getStringAttribute(e, name, null);
		if (ret == null) {
			throw new AttributeNotFoundException(e, name);
		}
		return ret;
	}

	/**
	 * <p>
	 * Get a string attribute from an element.
	 * </p>
	 * 
	 * <p>
	 * If the attribute does not exist, <i>defaultString</i> is returned instead.
	 * </p>
	 * 
	 * @param e
	 *            The parent element.
	 * @param name
	 *            The name of the attribute.
	 * @param defaultString
	 *            The default value to return. Can be null.
	 * @return The string or <i>defaultString</i>.
	 */
	public static String getStringAttribute(Element e, String name, String defaultString) {
		if (!e.hasAttribute(name)) {
			return defaultString;
		}
		return e.getAttribute(name);
	}

	/**
	 * <p>
	 * Get a long integer attribute from an element.
	 * </p>
	 * 
	 * <p>
	 * If the attribute does not exist, an AttributeNotFound exception will be thrown.
	 * </p>
	 * 
	 * @param e
	 *            The parent element.
	 * @param name
	 *            The name of the attribute.
	 * @return The long int.
	 * @throws AttributeNotFoundException
	 *             When <i>e</i> doesn't contain a <i>name</i> attribute.
	 * @throws AttributeParsingErrorException
	 *             When the value cannot be parsed as a long integer.
	 */
	public static long getLongAttribute(Element e, String name) {
		if (!e.hasAttribute(name)) {
			throw new AttributeNotFoundException(e, name);
		}
		String unparsed = e.getAttribute(name);
		try {
			return Long.parseLong(unparsed);
		} catch (NumberFormatException except) {
			throw new AttributeParsingErrorException(e, name, except);
		}
	}

	/**
	 * <p>
	 * Get a long integer attribute from an element.
	 * </p>
	 * 
	 * <p>
	 * If the attribute does not exist, <i>defaultValue</i> is returned instead.
	 * </p>
	 * 
	 * @param e
	 *            The parent element.
	 * @param name
	 *            The name of the attribute.
	 * @param defaultValue
	 *            The default value.
	 * @return The parsed long or <i>defaultValue</i>.
	 * @throws AttributeParsingErrorException
	 *             When the format is invalid.
	 */
	public static long getLongAttribute(Element e, String name, long defaultValue) {
		try {
			return getLongAttribute(e, name);
		} catch (AttributeNotFoundException except) {
			return defaultValue;
		}
	}

	/**
	 * <p>
	 * Get an integer attribute from an element.
	 * </p>
	 * 
	 * <p>
	 * If the attribute does not exist, an AttributeNotFound exception will be thrown.
	 * </p>
	 * 
	 * @param e
	 *            The parent element.
	 * @param name
	 *            The name of the attribute.
	 * @return The parsed int.
	 * @throws AttributeNotFoundException
	 *             When <i>e</i> doesn't contain a <i>name</i> attribute.
	 * @throws AttributeParsingErrorException
	 *             When the value cannot be parsed as an integer.
	 */
	public static int getIntAttribute(Element e, String name) {
		if (!e.hasAttribute(name)) {
			throw new AttributeNotFoundException(e, name);
		}
		String unparsed = e.getAttribute(name);
		try {
			return Integer.parseInt(unparsed);
		} catch (NumberFormatException except) {
			throw new AttributeParsingErrorException(e, name, except);
		}
	}

	/**
	 * <p>
	 * Get an integer attribute from an element.
	 * </p>
	 * 
	 * <p>
	 * If the attribute does not exist, return <i>defaultValue</i> instead.
	 * </p>
	 * 
	 * @param e
	 *            The parent element.
	 * @param name
	 *            The name of the attribute.
	 * @param defaultValue
	 *            The default value.
	 * @return The parsed int or <i>defaultValue</i>.
	 * @throws AttributeParsingErrorException
	 *             When the value cannot be parsed as an int.
	 */
	public static int getIntAttribute(Element e, String name, int defaultValue) {
		try {
			return getIntAttribute(e, name);
		} catch (AttributeNotFoundException except) {
			return defaultValue;
		}
	}

	/**
	 * <p>
	 * Get a float attribute from an element.
	 * </p>
	 * 
	 * <p>
	 * If the attribute does not exist, an AttributeNotFound exception will be thrown.
	 * </p>
	 * 
	 * @param e
	 *            The parent element.
	 * @param name
	 *            The name of the attribute.
	 * @return The parsed float.
	 * @throws AttributeNotFoundException
	 *             When <i>e</i> doesn't contain a <i>name</i> attribute.
	 * @throws AttributeParsingErrorException
	 *             When the value cannot be parsed as a float.
	 */
	public static float getFloatAttribute(Element e, String name) {
		if (!e.hasAttribute(name)) {
			throw new AttributeNotFoundException(e, name);
		}
		String unparsed = e.getAttribute(name);
		try {
			return Float.parseFloat(unparsed);
		} catch (NumberFormatException except) {
			throw new AttributeParsingErrorException(e, name, except);
		}
	}

	/**
	 * <p>
	 * Get a float attribute from an element.
	 * </p>
	 * 
	 * <p>
	 * If the attribute does not exist, <i>defaultValue</i> is used instead.
	 * </p>
	 * 
	 * @param e
	 *            The parent element.
	 * @param name
	 *            The name of the attribute.
	 * @param defaultValue
	 *            The default value.
	 * @return The parsed float or <i>defaultValue</i>
	 * @throws AttributeParsingErrorException
	 *             When the value cannot be parsed as a float.
	 */
	public static float getFloatAttribute(Element e, String name, float defaultValue) {
		try {
			return getFloatAttribute(e, name);
		} catch (AttributeNotFoundException except) {
			return defaultValue;
		}
	}

	/**
	 * <p>
	 * Get a boolean attribute from an element.
	 * </p>
	 * 
	 * <p>
	 * As defined in the TMX format, "true" is represented by 1, and "false" is represented by 0.
	 * </p>
	 * 
	 * <p>
	 * If the attribute does not exist, an AttributeNotFound exception will be thrown.
	 * </p>
	 * 
	 * @param e
	 *            The parent element.
	 * @param name
	 *            The name of the attribute.
	 * @return The parsed boolean.
	 * @throws AttributeNotFoundException
	 *             When <i>e</i> doesn't contain a <i>name</i> attribute.
	 * @throws AttributeParsingErrorException
	 *             When the value cannot be parsed as a number, or if the value is not 0 or 1.
	 */
	public static boolean getBoolAttribute(Element e, String name) {
		int intValue = getIntAttribute(e, name);
		if (intValue == 0) {
			return false;
		}
		if (intValue == 1) {
			return true;
		}
		throw new AttributeParsingErrorException(e, name, "Invalid boolean value; expected 1 or 0", intValue);
	}

	/**
	 * <p>
	 * Get a boolean attribute from an element.
	 * </p>
	 * 
	 * <p>
	 * As defined in the TMX format, "true" is represented by 1, and "false" is represented by 0.
	 * </p>
	 * 
	 * <p>
	 * If the attribute does not exist, <i>defaultValue<i> is returned instead.
	 * </p>
	 * 
	 * @param e
	 *            The parent element.
	 * @param name
	 *            The name of the attribute.
	 * @param defaultValue
	 *            The default value.
	 * @return The parsed boolean or <i>defaultValue</i>.
	 * @throws AttributeParsingErrorException
	 *             When the value cannot be parsed as a number, or if the value is not 0 or 1.
	 */
	public static boolean getBoolAttribute(Element e, String name, boolean defaultValue) {
		try {
			return getBoolAttribute(e, name);
		} catch (AttributeNotFoundException except) {
			return defaultValue;
		}
	}

	/**
	 * <p>
	 * Get a child tag by name.
	 * </p>
	 * 
	 * <p>
	 * This method will search the immediate children of <i>e</i> for a tag with the given name. If
	 * more than one matching child is found, a RuntimeException will be thrown.
	 * </p>
	 * 
	 * <p>
	 * If <i>isRequired</i> is true, then a RuntimeException will be thrown if no matching child is found.
	 * Otherwise, null is returned if no matching child is found.
	 * </p>
	 * 
	 * @param e
	 *            The parent element.
	 * @param tagName
	 *            The name of the child tag to find.
	 * @param isRequired
	 *            If true, throw an exception if the child is missing.
	 * @return The child element or null.
	 * @throws RuntimeException
	 *             if more than one matching child tag is found.
	 * @throws RuntimeException
	 *             if <i>isRequired</i> is true and no matching child tag was found.
	 */
	public static Element getSingleTag(Element e, String tagName, boolean isRequired) {
		NodeList children = e.getChildNodes();
		Element ret = null;
		int foundChildrenCount = 0;
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			Element childElement = (Element) child;
			if (childElement.getTagName().equals(tagName)) {
				ret = (Element) child;
				foundChildrenCount += 1;
			}
		}
		if (foundChildrenCount == 0) {
			if (isRequired) {
				throw new RuntimeException(getFullXmlPath(e) + ": Expected '" + tagName + "' tag");
			}

			return null;
		}
		if (foundChildrenCount != 1) {
			throw new RuntimeException(getFullXmlPath(e) + ": Expected only one tag named '" + tagName + "', found " + foundChildrenCount);
		}
		return ret;
	}

	/**
	 * <p>
	 * Iterate over all children with the given tag name.
	 * </p>
	 * 
	 * <p>
	 * This iterator only searches immediate children of <i>e</i>.
	 * </p>
	 * 
	 * @param e
	 *            The parent element.
	 * @param tagName
	 *            The tag name to search for.
	 * @return An iterator.
	 */
	public static ElementIterator getAllTags(Element e, String tagName) {
		return new ElementIterator(e.getChildNodes(), tagName);
	}

	/**
	 * An iterator over a Nodelist that only contains <i>Element</i>s.
	 *
	 */
	public static class ElementIterator implements Iterator<Element>, Iterable<Element> {
		private int currentIndex;
		private NodeList parent;
		private String tagName;
		private Element prefetchedElement;

		ElementIterator(NodeList parent, String tagName) {
			this.parent = parent;
			this.tagName = tagName;
			currentIndex = 0;
			prefetchedElement = null;
		}

		@Override
		public boolean hasNext() {
			if (prefetchedElement == null) {
				prefetchedElement = getNextElement();
				return prefetchedElement != null;
			}
			return true;
		}

		@Override
		public Element next() {
			if (prefetchedElement == null) {
				return getNextElement();
			} else {
				Element ret = prefetchedElement;
				prefetchedElement = null;
				if (ret == null) {
					throw new RuntimeException("Called next() on empty iterator");
				}
				return ret;
			}
		}

		private Element getNextElement() {
			while (currentIndex < parent.getLength()) {
				Node retNode = parent.item(currentIndex);
				currentIndex += 1;
				if (retNode.getNodeType() != Node.ELEMENT_NODE) {
					continue;
				}
				Element retElement = (Element) retNode;
				if (retElement.getTagName().equals(tagName)) {
					return retElement;
				}
			}
			return null;
		}

		@Override
		public Iterator<Element> iterator() {
			return this;
		}
	}
}
