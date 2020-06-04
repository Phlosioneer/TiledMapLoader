package TiledMapLoader.util;

import TiledMapLoader.core.TileLayer.FlipState;

public class ImageTransform {

	public final int x;
	public final int y;
	public final float opacity;
	public final FlipState flipState;

	public ImageTransform(int x, int y, float opacity) {
		this(x, y, opacity, FlipState.Normal);
	}

	public ImageTransform(int x, int y, float opacity, FlipState state) {
		this.x = x;
		this.y = y;
		this.opacity = opacity;
		this.flipState = state;
	}
}
