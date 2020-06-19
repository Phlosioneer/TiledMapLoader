package tilesets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import TiledMapLoader.core.WangSide;
import TiledMapLoader.core.WangTile;
import TiledMapLoader.util.Rect;
import util.ReflectionHelper;

class WangTiles {

	private Tileset<BufferedImage> mockTileset;
	private DocumentBuilder builder;
	private Document root;
	private ArrayList<WangColor> colors;
	private WangColor white;
	private WangColor black;
	private WangColor red;
	private WangColor blue;
	private ReflectionHelper<WangTile> reflect;
	private Class<?>[] ctorParams;

	public WangTiles() {
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(
					"ParserConfigurationException handler not yet written in makeTileset of Wangsets.", e);
		}
		colors = new ArrayList<>();
		white = new WangColor(SideType.CORNER, 1, "White");
		black = new WangColor(SideType.CORNER, 2, "Black");
		red = new WangColor(SideType.EDGE, 1, "Red");
		blue = new WangColor(SideType.EDGE, 2, "Blue");

		colors.add(white);
		colors.add(black);
		colors.add(red);
		colors.add(blue);

		reflect = new ReflectionHelper<>(WangTile.class);
		ctorParams = new Class<?>[]{Element.class, ArrayList.class, Tileset.class};
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
		Element element = root.createElement("wangtile");
		element.setAttribute("tileid", "0");
		element.setAttribute("wangid", "0x00000000");
		WangTile tile = reflect.construct(ctorParams, element, colors, mockTileset);

		assertEquals(mockTileset.tiles[0], tile.tile);
		assertNotNull(tile.sides);
		assertEquals(0, tile.sides.size());
	}

	@Test
	void testAlternating() {
		Element element = root.createElement("wangtile");
		element.setAttribute("tileid", "1");
		element.setAttribute("wangid", "0x11111111");
		WangTile tile = reflect.construct(ctorParams, element, colors, mockTileset);

		assertEquals(mockTileset.tiles[1], tile.tile);
		assertNotNull(tile.sides);
		assertEquals(white, tile.sides.get(WangSide.TOP_LEFT));
		assertEquals(white, tile.sides.get(WangSide.TOP_RIGHT));
		assertEquals(white, tile.sides.get(WangSide.BOTTOM_LEFT));
		assertEquals(white, tile.sides.get(WangSide.BOTTOM_RIGHT));

		assertEquals(red, tile.sides.get(WangSide.TOP));
		assertEquals(red, tile.sides.get(WangSide.BOTTOM));
		assertEquals(red, tile.sides.get(WangSide.LEFT));
		assertEquals(red, tile.sides.get(WangSide.RIGHT));

		assertEquals(8, tile.sides.size());
	}

	@Test
	void testAsymmetric() {
		Element element = root.createElement("wangtile");
		element.setAttribute("tileid", "0");
		element.setAttribute("wangid", Integer.toString(0x02110021));
		WangTile tile = reflect.construct(ctorParams, element, colors, mockTileset);

		assertEquals(mockTileset.tiles[0], tile.tile);
		assertNotNull(tile.sides);
		assertEquals(red, tile.sides.get(WangSide.TOP));
		assertEquals(black, tile.sides.get(WangSide.TOP_RIGHT));
		assertEquals(red, tile.sides.get(WangSide.BOTTOM));
		assertEquals(white, tile.sides.get(WangSide.BOTTOM_LEFT));
		assertEquals(blue, tile.sides.get(WangSide.LEFT));
		assertEquals(5, tile.sides.size());
	}
}
