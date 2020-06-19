package tilesets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.awt.image.BufferedImage;
import java.io.InputStream;
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
import TiledMapLoader.core.WangSide;
import TiledMapLoader.core.WangTile;
import TiledMapLoader.core.Wangset;
import TiledMapLoader.privateUtil.Util;
import TiledMapLoader.util.Rect;
import util.ArrayAssertionHelper;
import util.ReflectionHelper;

class Wangsets {

	private static final int MAX_MOCK_TILESET_SIZE = 80;

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
		mockTileset.tiles = new Tile[MAX_MOCK_TILESET_SIZE];
		mockTileset.tileCount = mockTileset.tiles.length;
		for (int i = 0; i < mockTileset.tiles.length; i++) {
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

		Element blackNode = root.createElement("wangcornercolor");
		blackNode.setAttribute("name", "Black");
		blackNode.setAttribute("color", "#000000");
		blackNode.setAttribute("tileid", "0");
		wangsetNode.appendChild(blackNode);

		Element whiteNode = root.createElement("wangcornercolor");
		whiteNode.setAttribute("name", "White");
		whiteNode.setAttribute("color", "#FFFFFF");
		whiteNode.setAttribute("tileid", "0");
		wangsetNode.appendChild(whiteNode);

		Wangset wangset = wangsetReflect.construct(ctorParams, wangsetNode, mockTileset);
		new ArrayAssertionHelper<WangColor>()
				.add("Black", item -> wangColorTest(item, "Black", 1, SideType.CORNER))
				.add("White", item -> wangColorTest(item, "White", 2, SideType.CORNER))
				.checkArray(wangset.colors);
	}

	@Test
	void testMixedColorsColors() {
		Element wangsetNode = root.createElement("wangset");
		wangsetNode.setAttribute("name", "test");
		wangsetNode.setAttribute("tileid", "0");

		Element blackNode = root.createElement("wangcornercolor");
		blackNode.setAttribute("name", "Black");
		blackNode.setAttribute("color", "#000000");
		blackNode.setAttribute("tileid", "0");
		wangsetNode.appendChild(blackNode);

		Element whiteNode = root.createElement("wangcornercolor");
		whiteNode.setAttribute("name", "White");
		whiteNode.setAttribute("color", "#FFFFFF");
		whiteNode.setAttribute("tileid", "0");
		wangsetNode.appendChild(whiteNode);

		Element blueNode = root.createElement("wangedgecolor");
		blueNode.setAttribute("name", "Blue");
		blueNode.setAttribute("color", "#0000FF");
		blueNode.setAttribute("tileid", "0");
		wangsetNode.appendChild(blueNode);

		Element redNode = root.createElement("wangedgecolor");
		redNode.setAttribute("name", "Red");
		redNode.setAttribute("color", "#FF0000");
		redNode.setAttribute("tileid", "0");
		wangsetNode.appendChild(redNode);

		Wangset wangset = wangsetReflect.construct(ctorParams, wangsetNode, mockTileset);
		new ArrayAssertionHelper<WangColor>()
				.add("Black Color", item -> wangColorTest(item, "Black", 1, SideType.CORNER))
				.add("White Color", item -> wangColorTest(item, "White", 2, SideType.CORNER))
				.add("Blue Color", item -> wangColorTest(item, "Blue", 1, SideType.EDGE))
				.add("Red Color", item -> wangColorTest(item, "Red", 2, SideType.EDGE))
				.checkArray(wangset.colors);
	}

	@Test
	void testWangTiles() {
		InputStream testCase = getClass().getResourceAsStream("case1.xml");
		assertNotNull(testCase);
		Element element = Util.loadXmlFile(testCase);
		Wangset wangset = wangsetReflect.construct(ctorParams, element, mockTileset);
		new ArrayAssertionHelper<WangColor>()
				.add("Any", item -> wangColorTest(item, "Any", 1, SideType.CORNER))
				.add("LooseRock", item -> wangColorTest(item, "LooseRock", 2, SideType.CORNER))
				.checkArray(wangset.colors);

		new ArrayAssertionHelper<WangTile>()
				.add("6", item -> wangCornerTileTest(item, 6, new String[]{"Any", "Any", "LooseRock", "Any"}))
				.add("7", item -> wangCornerTileTest(item, 7, new String[]{"Any", "LooseRock", "LooseRock", "Any"}))
				.add("8", item -> wangCornerTileTest(item, 8, new String[]{"Any", "LooseRock", "Any", "Any"}))
				.add("9", item -> wangCornerTileTest(item, 9, new String[]{"LooseRock", "LooseRock", "Any", "LooseRock"}))
				.add("10", item -> wangCornerTileTest(item, 10, new String[]{"LooseRock", "Any", "LooseRock", "LooseRock"}))
				.add("22", item -> wangCornerTileTest(item, 22, new String[]{"Any", "Any", "LooseRock", "LooseRock"}))
				.add("24", item -> wangCornerTileTest(item, 24, new String[]{"LooseRock", "LooseRock", "Any", "Any"}))
				.add("25", item -> wangCornerTileTest(item, 25, new String[]{"LooseRock", "LooseRock", "LooseRock", "Any"}))
				.add("26", item -> wangCornerTileTest(item, 26, new String[]{"Any", "LooseRock", "LooseRock", "LooseRock"}))
				.add("38", item -> wangCornerTileTest(item, 38, new String[]{"Any", "Any", "Any", "LooseRock"}))
				.add("39", item -> wangCornerTileTest(item, 39, new String[]{"LooseRock", "Any", "Any", "LooseRock"}))
				.add("40", item -> wangCornerTileTest(item, 40, new String[]{"LooseRock", "Any", "Any", "Any"}))
				.add("41", item -> wangCornerTileTest(item, 41, new String[]{"LooseRock", "Any", "LooseRock", "Any"}))
				.add("42", item -> wangCornerTileTest(item, 42, new String[]{"Any", "LooseRock", "Any", "LooseRock"}))
				.checkArray(wangset.tiles);
	}

	private static void wangColorTest(WangColor wangColor, String name, int id, SideType type) {
		assertEquals(wangColor.name, name);
		assertEquals(wangColor.colorId, id);
		assertEquals(wangColor.type, type);
	}

	/**
	 * Sides is in the same order as the TMX file, left-to-right: [TOP_LEFT, BOTTOM_LEFT, BOTTOM_RIGHT, TOP_RIGHT]
	 */
	private static void wangCornerTileTest(WangTile tile, int tileId, String[] sides) {
		assertEquals(tileId, tile.tile.localId);

		testSide(tile, sides[0], WangSide.TOP_LEFT);
		testSide(tile, sides[1], WangSide.BOTTOM_LEFT);
		testSide(tile, sides[2], WangSide.BOTTOM_RIGHT);
		testSide(tile, sides[3], WangSide.TOP_RIGHT);

		testSide(tile, null, WangSide.TOP);
		testSide(tile, null, WangSide.RIGHT);
		testSide(tile, null, WangSide.BOTTOM);
		testSide(tile, null, WangSide.LEFT);
	}

	private static void testSide(WangTile tile, String expected, WangSide side) {
		if (expected == null) {
			assertNull(tile.sides.get(side), "Extra side present: " + side);
		} else {
			WangColor color = tile.sides.get(side);
			assertNotNull(color, "Side missing: " + side);
			assertEquals(expected, color.name, "Mismatched side: " + side);
		}
	}
}
