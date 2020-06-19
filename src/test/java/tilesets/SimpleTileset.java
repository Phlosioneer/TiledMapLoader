package tilesets;

import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;
import TiledMapLoader.core.Tileset;
import TiledMapLoader.util.AwtResourceLoader;

class SimpleTileset {

	public SimpleTileset() {

	}

	@Test
	void test() {
		Tileset<BufferedImage> tileset = new Tileset("ui_tiles.tsx", new AwtResourceLoader(getClass()));
	}
}
