package maps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;
import TiledMapLoader.core.MapFile;
import TiledMapLoader.util.AwtResourceLoader;

class EmptyProject {

	public EmptyProject() {

	}

	@Test
	void test() {
		MapFile<BufferedImage> map = new MapFile<>("empty.tmx", new AwtResourceLoader(getClass()));
		assertNotEquals(map.root, null);
		assertNotEquals(map.root.layers, null);
		assertEquals(map.root.layers.size(), 0);
	}
}
