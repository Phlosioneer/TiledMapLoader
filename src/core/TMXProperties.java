package core;

import java.util.HashMap;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import privateUtil.Util;
import util.AttributeParsingErrorException;
import util.PropertyNotFoundException;

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

	/**
	 * <p>
	 * Tries to find the given property. If found, returns the type of that property, as a string.
	 * </p>
	 * 
	 * @param propertyName
	 *            The property to look for.
	 * @return Null if the property does not exist. Otherwise, one of "String", "int", "float",
	 *         "boolean", or "color".
	 */
	public String getTypeForProperty(String propertyName) {
		if (hasString(propertyName)) {
			return "String";
		} else if (hasInt(propertyName)) {
			return "int";
		} else if (hasFloat(propertyName)) {
			return "float";
		} else if (hasBool(propertyName)) {
			return "boolean";
		} else if (hasColor(propertyName)) {
			return "color";
		} else {
			return null;
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

	/**
	 * Returns the value of the given String property.
	 * 
	 * @param name
	 *            The name of the property.
	 * @return The value. Cannot be null.
	 * @throws PropertyNotFoundException
	 *             If a property with the given name can't be found, or
	 *             if it isn't an int property.
	 */
	public String getString(String name) {
		if (stringProps == null) {
			String actualType = getTypeForProperty(name);
			if (actualType == null) {
				throw new PropertyNotFoundException(name, "String");
			} else {
				throw new PropertyNotFoundException(name, "String", actualType);
			}
		}
		return stringProps.get(name);
	}

	/**
	 * Returns the value of the given String property.
	 * 
	 * @param name
	 *            The name of the property.
	 * @param defaultValue
	 *            The default value.
	 * @return The value, or the default value if the property does not exist,
	 *         or if it isn't a String property. Cannot be null, unless the
	 *         default value is null.
	 */
	public String getString(String name, String defaultValue) {
		if (!hasString(name)) {
			return defaultValue;
		}
		return stringProps.get(name);
	}

	/**
	 * Returns the value of the given int property.
	 * 
	 * @param name
	 *            The name of the property.
	 * @return The value.
	 * @throws PropertyNotFoundException
	 *             If a property with the given name can't be found, or
	 *             if it isn't an int property.
	 */
	public int getInt(String name) {
		if (!hasInt(name)) {
			String actualType = getTypeForProperty(name);
			if (actualType == null) {
				throw new PropertyNotFoundException(name, "int");
			} else {
				throw new PropertyNotFoundException(name, "int", actualType);
			}
		}
		return intProps.get(name);
	}

	/**
	 * Returns the value of the given int property.
	 * 
	 * @param name
	 *            The name of the property.
	 * @param defaultValue
	 *            The default value.
	 * @return The value, or the default value if the property does not exist,
	 *         or if it isn't an int property.
	 */
	public int getInt(String name, int defaultValue) {
		if (!hasInt(name)) {
			return defaultValue;
		}
		return intProps.get(name);
	}

	/**
	 * Returns the value of the given float property.
	 * 
	 * @param name
	 *            The name of the property.
	 * @return The value.
	 * @throws PropertyNotFoundException
	 *             If a property with the given name can't be found, or
	 *             if it isn't a float property.
	 */
	public float getFloat(String name) {
		if (!hasFloat(name)) {
			String actualType = getTypeForProperty(name);
			if (actualType == null) {
				throw new PropertyNotFoundException(name, "float");
			} else {
				throw new PropertyNotFoundException(name, "float", actualType);
			}
		}
		return floatProps.get(name);
	}

	/**
	 * Returns the value of the given float property.
	 * 
	 * @param name
	 *            The name of the property.
	 * @param defaultValue
	 *            The default value.
	 * @return The value, or the default value if the property does not exist,
	 *         or if it isn't a float property.
	 */
	public float getFloat(String name, float defaultValue) {
		if (!hasFloat(name)) {
			return defaultValue;
		}
		return floatProps.get(name);
	}

	/**
	 * Returns the value of the given boolean property.
	 * 
	 * @param name
	 *            The name of the property.
	 * @return The value.
	 * @throws PropertyNotFoundException
	 *             If a property with the given name can't be found, or
	 *             if it isn't a boolean property.
	 */
	public boolean getBool(String name) {
		if (!hasBool(name)) {
			String actualType = getTypeForProperty(name);
			if (actualType == null) {
				throw new PropertyNotFoundException(name, "boolean");
			} else {
				throw new PropertyNotFoundException(name, "boolean", actualType);
			}
		}
		return boolProps.get(name);
	}

	/**
	 * Returns the value of the given boolean property.
	 * 
	 * @param name
	 *            The name of the property.
	 * @param defaultValue
	 *            The default value.
	 * @return The value, or the default value if the property does not exist,
	 *         or if it isn't a boolean property.
	 */
	public boolean getBool(String name, boolean defaultValue) {
		if (!hasBool(name)) {
			return defaultValue;
		}
		return boolProps.get(name);
	}

	/**
	 * Returns the value of the given color property.
	 * 
	 * @param name
	 *            The name of the property.
	 * @return The value.
	 * @throws PropertyNotFoundException
	 *             If a property with the given name can't be found, or
	 *             if it isn't a color property.
	 */
	public TMXColor getColor(String name) {
		if (!hasColor(name)) {
			String actualType = getTypeForProperty(name);
			if (actualType == null) {
				throw new PropertyNotFoundException(name, "color");
			} else {
				throw new PropertyNotFoundException(name, "color", actualType);
			}
		}
		return colorProps.get(name);
	}

	/**
	 * Returns the value of the given color property.
	 * 
	 * @param name
	 *            The name of the property.
	 * @param defaultValue
	 *            The default value.
	 * @return The value, or the default value if the property does not exist,
	 *         or if it isn't a color property. Cannot be null, unless the
	 *         default value is null.
	 */
	public TMXColor getColor(String name, TMXColor defaultValue) {
		if (!hasColor(name)) {
			return defaultValue;
		}
		return colorProps.get(name);
	}

	private void doRemoveCheck(String name, boolean stringCheck, boolean intCheck, boolean floatCheck, boolean boolCheck, boolean colorCheck) {
		if (stringCheck && stringProps != null) {
			stringProps.remove(name);
		}
		if (intCheck && intProps != null) {
			intProps.remove(name);
		}
		if (floatCheck && floatProps != null) {
			floatProps.remove(name);
		}
		if (boolCheck && boolProps != null) {
			boolProps.remove(name);
		}
		if (colorCheck && colorProps != null) {
			colorProps.remove(name);
		}
	}

	/**
	 * <p>
	 * Set the named property to the given value.
	 * </p>
	 * 
	 * <p>
	 * Warning: No check is done to ensure that the name is not already in use
	 * with a different type. For example, it is possible to have an int property
	 * named "foo" and a String property named "foo" at the same time. This will
	 * result in undefined behavior.
	 * </p>
	 * 
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The new value.
	 * @throws RuntimeException
	 *             If <i>value</i> is null.
	 */
	public void setString(String name, String value) {
		if (value == null) {
			throw new RuntimeException("Cannot set a property to null!");
		}
		if (stringProps == null) {
			stringProps = new HashMap<>();
		}
		stringProps.put(name, value);
	}

	/**
	 * <p>
	 * Set the named property to the given value.
	 * </p>
	 * <p>
	 * This method does a check to remove any other properties with the
	 * same name.
	 * </p>
	 * 
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The new value.
	 * @throws RuntimeException
	 *             If <i>value</i> is null.
	 */
	public void setStringChecked(String name, String value) {
		doRemoveCheck(name, false, true, true, true, true);
		setString(name, value);
	}

	/**
	 * <p>
	 * Set the named property to the given value.
	 * </p>
	 * 
	 * <p>
	 * Warning: No check is done to ensure that the name is not already in use
	 * with a different type. For example, it is possible to have an int property
	 * named "foo" and a String property named "foo" at the same time. This will
	 * result in undefined behavior.
	 * </p>
	 * 
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The new value.
	 */
	public void setInt(String name, int value) {
		if (intProps == null) {
			intProps = new HashMap<>();
		}
		intProps.put(name, value);
	}

	/**
	 * <p>
	 * Set the named property to the given value.
	 * </p>
	 * <p>
	 * This method does a check to remove any other properties with the
	 * same name.
	 * </p>
	 * 
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The new value.
	 */
	public void setIntChecked(String name, int value) {
		doRemoveCheck(name, true, false, true, true, true);
		setInt(name, value);
	}

	/**
	 * <p>
	 * Set the named property to the given value.
	 * </p>
	 * 
	 * <p>
	 * Warning: No check is done to ensure that the name is not already in use
	 * with a different type. For example, it is possible to have an int property
	 * named "foo" and a String property named "foo" at the same time. This will
	 * result in undefined behavior.
	 * </p>
	 * 
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The new value.
	 */
	public void setFloat(String name, float value) {
		if (floatProps == null) {
			floatProps = new HashMap<>();
		}
		floatProps.put(name, value);
	}

	/**
	 * <p>
	 * Set the named property to the given value.
	 * </p>
	 * <p>
	 * This method does a check to remove any other properties with the
	 * same name.
	 * </p>
	 * 
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The new value.
	 */
	public void setFloatChecked(String name, float value) {
		doRemoveCheck(name, true, true, false, true, true);
		setFloat(name, value);
	}

	/**
	 * <p>
	 * Set the named property to the given value.
	 * </p>
	 * 
	 * <p>
	 * Warning: No check is done to ensure that the name is not already in use
	 * with a different type. For example, it is possible to have an int property
	 * named "foo" and a String property named "foo" at the same time. This will
	 * result in undefined behavior.
	 * </p>
	 * 
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The new value.
	 */
	public void setBool(String name, boolean value) {
		if (boolProps == null) {
			boolProps = new HashMap<>();
		}
		boolProps.put(name, value);
	}

	/**
	 * <p>
	 * Set the named property to the given value.
	 * </p>
	 * <p>
	 * This method does a check to remove any other properties with the
	 * same name.
	 * </p>
	 * 
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The new value.
	 */
	public void setBoolChecked(String name, boolean value) {
		doRemoveCheck(name, true, true, true, false, true);
		setBool(name, value);
	}

	/**
	 * <p>
	 * Set the named property to the given value.
	 * </p>
	 * 
	 * <p>
	 * Warning: No check is done to ensure that the name is not already in use
	 * with a different type. For example, it is possible to have an int property
	 * named "foo" and a String property named "foo" at the same time. This will
	 * result in undefined behavior.
	 * </p>
	 * 
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The new value.
	 * @throws RuntimeException
	 *             If <i>value</i> is null.
	 */
	public void setColor(String name, TMXColor value) {
		if (value == null) {
			throw new RuntimeException("Cannot set a property to null!");
		}
		if (colorProps == null) {
			colorProps = new HashMap<>();
		}
		colorProps.put(name, value);
	}

	/**
	 * <p>
	 * Set the named property to the given value.
	 * </p>
	 * <p>
	 * This method does a check to remove any other properties with the
	 * same name.
	 * </p>
	 * 
	 * @param name
	 *            The name of the property.
	 * @param value
	 *            The new value.
	 * @throws RuntimeException
	 *             If <i>value</i> is null.
	 */
	public void setColorChecked(String name, TMXColor value) {
		doRemoveCheck(name, true, true, true, true, false);
		setColor(name, value);
	}
}