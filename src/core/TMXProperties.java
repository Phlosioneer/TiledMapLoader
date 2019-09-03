package core;

import java.util.HashMap;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import privateUtil.Util;
import util.AttributeParsingErrorException;

/**
 * An immutable dictionary for custom TMX properties.
 *
 */
public final class TMXProperties {
	private HashMap<String, Integer> intProps;
	private HashMap<String, String> stringProps;
	private HashMap<String, Float> floatProps;
	private HashMap<String, Boolean> boolProps;
	private HashMap<String, TMXColor> colorProps;

	/**
	 * Create an empty TMXProperties object.
	 */
	public TMXProperties() {
		intProps = null;
		stringProps = null;
		floatProps = null;
		boolProps = null;
		colorProps = null;
	}

	TMXProperties(Element rootElement) {
		this();

		NodeList properties = rootElement.getElementsByTagName("property");
		for (int i = 0; i < properties.getLength(); i++) {
			if (properties.item(i).getNodeType() != Node.ELEMENT_NODE) {
				throw new RuntimeException();
			}
			Element prop = (Element) properties.item(i);

			String name = Util.getStringAttribute(prop, "name");
			String type = Util.getStringAttribute(prop, "type", "string");

			if (type.equals("string")) {
				if (stringProps == null) {
					stringProps = new HashMap<>();
				}
				stringProps.put(name, Util.getStringAttribute(prop, "value"));
			} else if (type.equals("int")) {
				if (intProps == null) {
					intProps = new HashMap<>();
				}
				intProps.put(name, Util.getIntAttribute(prop, "value"));
			} else if (type.equals("float")) {
				if (floatProps == null) {
					floatProps = new HashMap<>();
				}
				floatProps.put(name, Util.getFloatAttribute(prop, "value"));
			} else if (type.equals("bool")) {
				if (boolProps == null) {
					boolProps = new HashMap<>();
				}
				boolProps.put(name, Util.getBoolAttribute(prop, "value"));
			} else if (type.equals("color")) {
				if (colorProps == null) {
					colorProps = new HashMap<>();
				}
				colorProps.put(name, Util.getColorAttribute(prop, "value"));
			} else {
				throw new AttributeParsingErrorException(prop, "value", "Unrecognized property type", type);
			}
		}
	}

	public boolean hasString(String name) {
		if (stringProps == null) {
			return false;
		}
		return stringProps.containsKey(name);
	}

	public boolean hasInt(String name) {
		if (intProps == null) {
			return false;
		}
		return intProps.containsKey(name);
	}

	public boolean hasFloat(String name) {
		if (floatProps == null) {
			return false;
		}
		return floatProps.containsKey(name);
	}

	public boolean hasBool(String name) {
		if (boolProps == null) {
			return false;
		}
		return boolProps.containsKey(name);
	}

	public boolean hasColor(String name) {
		if (colorProps == null) {
			return false;
		}
		return colorProps.containsKey(name);
	}

	public String getString(String name) {
		if (stringProps == null) {
			throw new RuntimeException("No string property named '" + name + "'");
		}
		return stringProps.get(name);
	}

	public String getString(String name, String defaultValue) {
		if (!hasString(name)) {
			return defaultValue;
		}
		return stringProps.get(name);
	}

	public int getInt(String name) {
		if (!hasInt(name)) {
			throw new RuntimeException("No int property named '" + name + "'");
		}
		return intProps.get(name);
	}

	public int getInt(String name, int defaultValue) {
		if (!hasInt(name)) {
			return defaultValue;
		}
		return intProps.get(name);
	}

	public float getFloat(String name) {
		if (!hasFloat(name)) {
			throw new RuntimeException("No float property named '" + name + "'");
		}
		return floatProps.get(name);
	}

	public float getFloat(String name, float defaultValue) {
		if (!hasFloat(name)) {
			return defaultValue;
		}
		return floatProps.get(name);
	}

	public boolean getBool(String name) {
		if (!hasBool(name)) {
			throw new RuntimeException("No bool property named '" + name + "'");
		}
		return boolProps.get(name);
	}

	public boolean getBool(String name, boolean defaultValue) {
		if (!hasBool(name)) {
			return defaultValue;
		}
		return boolProps.get(name);
	}

	public TMXColor getColor(String name) {
		if (!hasColor(name)) {
			throw new RuntimeException("No color property named '" + name + "'");
		}
		return colorProps.get(name);
	}

	public TMXColor getColor(String name, TMXColor defaultValue) {
		if (!hasColor(name)) {
			return defaultValue;
		}
		return colorProps.get(name);
	}

	public void setString(String name, String value) {
		if (stringProps == null) {
			stringProps = new HashMap<>();
		}
		stringProps.put(name, value);
	}

	public void setInt(String name, int value) {
		if (intProps == null) {
			intProps = new HashMap<>();
		}
		intProps.put(name, value);
	}

	public void setFloat(String name, float value) {
		if (floatProps == null) {
			floatProps = new HashMap<>();
		}
		floatProps.put(name, value);
	}

	public void setBool(String name, boolean value) {
		if (boolProps == null) {
			boolProps = new HashMap<>();
		}
		boolProps.put(name, value);
	}

	public void setColor(String name, TMXColor value) {
		if (colorProps == null) {
			colorProps = new HashMap<>();
		}
		colorProps.put(name, value);
	}
}