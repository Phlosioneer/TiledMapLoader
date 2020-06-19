package tilesets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import TiledMapLoader.core.SideType;
import TiledMapLoader.core.Tile;
import TiledMapLoader.core.Tileset;
import TiledMapLoader.core.WangColor;
import TiledMapLoader.core.Wangset;
import TiledMapLoader.util.Rect;
import util.ArrayAssertionHelper;
import util.ReflectionHelper;

class Wangsets {

	private Tileset<BufferedImage> mockTileset;
	private DocumentBuilder builder;
	private Document root;
	private ReflectionHelper<Wangset> wangsetReflect;
	private Class<?>[] ctorParams;

	public Wangsets() {
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(
					"ParserConfigurationException handler not yet written in makeTileset of Wangsets.", e);
		}
		wangsetReflect = new ReflectionHelper<>(Wangset.class);
		ctorParams = new Class<?>[]{Element.class, Tileset.class};
	}

	@SuppressWarnings("unchecked")
	@BeforeEach
	void makeTileset() {
		mockTileset = new Tileset<>();
		mockTileset.tiles = new Tile[20];
		mockTileset.tileCount = 20;
		for (int i = 0; i < 20; i++) {
			mockTileset.tiles[i] = new Tile<BufferedImage>(mockTileset, i, new Rect(i, 0, 1, 1));
		}

		root = builder.newDocument();
	}

	@AfterEach
	void removeTileset() {
		mockTileset = null;
		root = null;
	}

	@Test
	void testEmpty() {
		Element wangsetNode = root.createElement("wangset");
		wangsetNode.setAttribute("name", "test");
		wangsetNode.setAttribute("tileid", "0");
		Wangset set = wangsetReflect.construct(ctorParams, wangsetNode, mockTileset);
		assertEquals("test", set.name);
		if (set.colors != null) {
			assertEquals(new ArrayList<>(), set.colors);
		}
		if (set.tiles != null) {
			assertEquals(new ArrayList<>(), set.tiles);
		}
	}

	@Test
	void testCornerColors() {
		Element wangsetNode = root.createElement("wangset");
		wangsetNode.setAttribute("name", "test");
		wangsetNode.setAttribute("tileid", "0");

		Element colorsNode = root.createElement("wangcornercolors");
		wangsetNode.appendChild(colorsNode);

		Element blackNode = root.createElement("wangcornercolor");
		blackNode.setAttribute("name", "Black");
		blackNode.setAttribute("color", "#000000");
		blackNode.setAttribute("tileid", "0");
		colorsNode.appendChild(blackNode);

		Element whiteNode = root.createElement("wangcornercolor");
		whiteNode.setAttribute("name", "White");
		whiteNode.setAttribute("color", "#FFFFFF");
		whiteNode.setAttribute("tileid", "0");
		colorsNode.appendChild(whiteNode);

		Wangset wangset = wangsetReflect.construct(ctorParams, wangsetNode, mockTileset);
		new ArrayAssertionHelper<WangColor>()
				.add("Black", item -> wangColorTest(item, "Black", 0, SideType.CORNER))
				.add("White", item -> wangColorTest(item, "White", 1, SideType.CORNER))
				.checkArray(wangset.colors);
	}

	@Test
	void testMixedColorsColors() {
		Element wangsetNode = root.createElement("wangset");
		wangsetNode.setAttribute("name", "test");
		wangsetNode.setAttribute("tileid", "0");

		Element cornerColorsNode = root.createElement("wangcornercolors");
		wangsetNode.appendChild(cornerColorsNode);

		Element blackNode = root.createElement("wangcornercolor");
		blackNode.setAttribute("name", "Black");
		blackNode.setAttribute("color", "#000000");
		blackNode.setAttribute("tileid", "0");
		cornerColorsNode.appendChild(blackNode);

		Element whiteNode = root.createElement("wangcornercolor");
		whiteNode.setAttribute("name", "White");
		whiteNode.setAttribute("color", "#FFFFFF");
		whiteNode.setAttribute("tileid", "0");
		cornerColorsNode.appendChild(whiteNode);

		Element edgeColorsNode = root.createElement("wangedgecolors");
		wangsetNode.appendChild(edgeColorsNode);

		Element blueNode = root.createElement("wangedgecolor");
		blueNode.setAttribute("name", "Blue");
		blueNode.setAttribute("color", "#0000FF");
		blueNode.setAttribute("tileid", "0");
		edgeColorsNode.appendChild(blueNode);

		Element redNode = root.createElement("wangedgecolor");
		redNode.setAttribute("name", "Red");
		redNode.setAttribute("color", "#FF0000");
		redNode.setAttribute("tileid", "0");
		edgeColorsNode.appendChild(redNode);

		Wangset wangset = wangsetReflect.construct(ctorParams, wangsetNode, mockTileset);
		new ArrayAssertionHelper<WangColor>()
				.add("Black Color", item -> wangColorTest(item, "Black", 0, SideType.CORNER))
				.add("White Color", item -> wangColorTest(item, "White", 1, SideType.CORNER))
				.add("Blue Color", item -> wangColorTest(item, "Blue", 0, SideType.EDGE))
				.add("Red Color", item -> wangColorTest(item, "Red", 1, SideType.EDGE))
				.checkArray(wangset.colors);
	}

	private static void wangColorTest(WangColor wangColor, String name, int id, SideType type) {
		assertEquals(wangColor.name, name);
		assertEquals(wangColor.colorId, id);
		assertEquals(wangColor.type, type);
	}
}
