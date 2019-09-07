package core;

import org.w3c.dom.Element;
import privateUtil.Util;
import util.AttributeParsingErrorException;

/**
 * <p>
 * A text object displays text within its bounding rectangle.
 * </p>
 * 
 * <p>
 * According to the TMX specification, if part of the text would be rendered
 * outside of the bounding box, the result is undefined. The origin of the text
 * to use for alignment is also undefined. This library follows the rules that
 * the Tiled editor uses:
 * </p>
 * 
 * <p>
 * First, check if the horizontal alignment is JUSTIFY. If it is, expand the
 * text as needed to occupy the full width of the TMXObject.
 * </p>
 * 
 * <p>
 * Then, render the text into a buffer image, that is exactly large enough to
 * accomodate the font. This is the "inner" bounds, while the TMXObject defines
 * the "outer" outer bounds. To determine the position of the "inner" bounds,
 * use the <i>horizontalAlign</i> and <i>verticalAlign</i> fields.
 * </p>
 * 
 * <p>
 * If the "inner" bounds extend past the "outer" bounds, clip the text to the
 * size of the "outer" bounds.
 * </p>
 * 
 * <p>
 * Finally, apply rotation, around the <i>position</i> (in the upper-left corner).
 * </p>
 */
public class TextObject extends TMXObject {
	/**
	 * The name of the font family. Cannot be null.
	 */
	public String fontFamily;
	/**
	 * <p>
	 * The size of the font, in pixels. (Not points.)
	 * </p>
	 * 
	 * <p>
	 * 3 points = 4 pixels.
	 * </p>
	 */
	public float pixelHeight;
	/**
	 * <p>
	 * Whether text should be moved to a second line if it's too wide for
	 * the the text box.
	 * </p>
	 * 
	 * <p>
	 * Lines are broken by whitespace characters and hyphens ("-").
	 * </p>
	 */
	public boolean wrapText;
	/**
	 * The color of the text. Cannot be null.
	 */
	public TMXColor color;
	@SuppressWarnings("javadoc")
	public boolean bold;
	@SuppressWarnings("javadoc")
	public boolean italic;
	@SuppressWarnings("javadoc")
	public boolean underline;
	@SuppressWarnings("javadoc")
	public boolean strikeThrough;
	@SuppressWarnings("javadoc")
	public boolean useKerning;
	/**
	 * The horizontal alignment of the text. See <i>HorizontalAlign</i> for
	 * details.
	 */
	public HorizontalAlign horizontalAlign;
	/**
	 * The vertical alignment of the text. See <i>VerticalAlign</i> for details.
	 */
	public VerticalAlign verticalAlign;
	/**
	 * The text. Cannot be null.
	 */
	public String text;

	/**
	 * 
	 */
	public static enum HorizontalAlign {
		/**
		 * The left side of the inner bounds is aligned with the left side of the
		 * outer bounds.
		 */
		LEFT,
		/**
		 * The center of the inner bounds is aligned with the center of the outer
		 * bounds.
		 */
		CENTER,
		/**
		 * The right side of the inner bounds is aligned with the right side of the
		 * outer bounds.
		 */
		RIGHT,
		/**
		 * <p>
		 * The text is expanded to occupy the full space, expanding spaces proportionally.
		 * </p>
		 * 
		 * <p>
		 * Note that expanding spaces proportionally is not the same as adding more space
		 * characters.
		 * </p>
		 */
		JUSTIFY
	}

	/**
	 * 
	 */
	public static enum VerticalAlign {
		/**
		 * The ascender height of the font is aligned with the top of the outer bounds.
		 */
		TOP,
		/**
		 * The halfway point between the ascender height and descender height of the font is
		 * aligned with the center of the outer bounds.
		 */
		CENTER,
		/**
		 * The descender height of the font is aligned with the bottom of the outer bounds.
		 */
		BOTTOM
	}

	TextObject(Element element) {
		super(element);

		Element textElement = Util.getSingleTag(element, "text", true);
		fontFamily = Util.getStringAttribute(textElement, "fontfamily", "sans-serif");
		pixelHeight = Util.getFloatAttribute(textElement, "pixelsize", 16);
		wrapText = Util.getBoolAttribute(textElement, "wrap", false);
		color = Util.getColorAttribute(textElement, "color", new TMXColor(0, 0, 0));
		bold = Util.getBoolAttribute(textElement, "bold", false);
		italic = Util.getBoolAttribute(textElement, "italic", false);
		underline = Util.getBoolAttribute(textElement, "underline", false);
		strikeThrough = Util.getBoolAttribute(textElement, "strikeout", false);
		useKerning = Util.getBoolAttribute(textElement, "kerning", true);
		text = textElement.getTextContent();
		if (text == null) {
			text = "";
		} else {
			text = text.trim();
		}
		String horizontalAlignString = Util.getStringAttribute(textElement, "halign", "left");
		String verticalAlignString = Util.getStringAttribute(textElement, "valign", "top");

		if (horizontalAlignString.equals("left")) {
			horizontalAlign = HorizontalAlign.LEFT;
		} else if (horizontalAlignString.equals("center")) {
			horizontalAlign = HorizontalAlign.CENTER;
		} else if (horizontalAlignString.equals("right")) {
			horizontalAlign = HorizontalAlign.RIGHT;
		} else if (horizontalAlignString.equals("justify")) {
			horizontalAlign = HorizontalAlign.JUSTIFY;
		} else {
			throw new AttributeParsingErrorException(textElement, "halign", "Expected \"left\", \"center\", \"right\", or \"justify\"", horizontalAlignString);
		}

		if (verticalAlignString.equals("top")) {
			verticalAlign = VerticalAlign.TOP;
		} else if (verticalAlignString.equals("center")) {
			verticalAlign = VerticalAlign.CENTER;
		} else if (verticalAlignString.equals("bottom")) {
			verticalAlign = VerticalAlign.BOTTOM;
		} else {
			throw new AttributeParsingErrorException(textElement, "valign", "Expected \"top\", \"center\", or \"bottom\"", verticalAlignString);
		}
	}

	/**
	 * Manually create a TextObject instance. No fields are initialized.
	 */
	public TextObject() {}

	/**
	 * Work-around for Processing apps. Processing does not allow accessing fields named "color".
	 * 
	 * @return The color field.
	 */
	public TMXColor getColor() {
		return color;
	}

	/**
	 * Work-around for Processing apps. Processing does not allow accessing fields named "color".
	 * 
	 * @param color
	 *            The new color.
	 */
	public void setColor(TMXColor color) {
		this.color = color;
	}

	/**
	 * Convert the pixelHeight to traditional font point height.
	 * 
	 * @return The height, in points.
	 * 
	 */
	public float getPointHeight() {
		final float pixelsPerIn = 96;
		final float pointsPerIn = 72;
		return pixelHeight * pointsPerIn / pixelsPerIn;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		TextObject ret = (TextObject) super.clone();
		ret.color = (TMXColor) color.clone();
		return ret;
	}
}
