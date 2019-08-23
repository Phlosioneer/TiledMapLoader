package util;

import core.TMXColor;

public interface ImageDelegate<IMG> {
	IMG loadImage(String filename, TMXColor transparentColor);

	IMG sliceImage(IMG image, Rect pixelBounds);
}
