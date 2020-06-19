package TiledMapLoader.core;

/**
 * An enum that represents the edges and corners of a tile.
 * 
 * Tiled's origin is in the upper-left corner of the screen. "Top" is in the negative Y direction,
 * and "Right" is in the positive X direction.
 */
@SuppressWarnings("javadoc")
public enum WangSide {
	TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, TOP_LEFT;

	/**
	 * @return {@code SideType.EDGE} if this is an edge, {@code SideType.CORNER} otherwise.
	 * @see SideType
	 */
	public SideType getType() {
		switch (this) {
			case TOP:
			case BOTTOM:
			case LEFT:
			case RIGHT:
				return SideType.EDGE;
			default:
				return SideType.CORNER;
		}
	}

	/**
	 * @return The next wang side in the clockwise direction.
	 * 
	 * @see #clockwise(int)
	 */
	public WangSide clockwise() {
		switch (this) {
			case TOP:
				return TOP_RIGHT;
			case TOP_RIGHT:
				return RIGHT;
			case RIGHT:
				return BOTTOM_RIGHT;
			case BOTTOM_RIGHT:
				return BOTTOM;
			case BOTTOM:
				return BOTTOM_LEFT;
			case BOTTOM_LEFT:
				return LEFT;
			case LEFT:
				return TOP_LEFT;
			default:
				assert (this == TOP_LEFT);
				return TOP;
		}
	}

	/**
	 * @return The next wang side in the counter clockwise direction.
	 * @see #counterClockwise(int)
	 */
	public WangSide counterClockwise() {
		switch (this) {
			case TOP:
				return TOP_LEFT;
			case TOP_LEFT:
				return LEFT;
			case LEFT:
				return BOTTOM_LEFT;
			case BOTTOM_LEFT:
				return BOTTOM;
			case BOTTOM:
				return BOTTOM_RIGHT;
			case BOTTOM_RIGHT:
				return RIGHT;
			case RIGHT:
				return TOP_RIGHT;
			default:
				assert (this == TOP_RIGHT);
				return TOP;
		}
	}

	/**
	 * @return The opposite side or corner.
	 */
	public WangSide oppositeSide() {
		switch (this) {
			case TOP:
				return BOTTOM;
			case TOP_RIGHT:
				return BOTTOM_LEFT;
			case RIGHT:
				return LEFT;
			case BOTTOM_RIGHT:
				return TOP_LEFT;
			case BOTTOM:
				return TOP;
			case BOTTOM_LEFT:
				return TOP_RIGHT;
			case LEFT:
				return RIGHT;
			default:
				assert (this == TOP_LEFT);
				return BOTTOM_RIGHT;
		}
	}

	/**
	 * Get the nth side clockwise from this one.
	 * 
	 * The count can be any size. A negative count is interpreted as
	 * counter clockwise rotation instead.
	 * 
	 * @param count
	 *            The step size.
	 * @return The new side.
	 */
	public WangSide clockwise(int count) {
		int normalized = Math.abs(count) % 8;
		if (count < 0) {
			normalized = (8 - normalized) % 8;
		}
		switch (count % 8) {
			case 0:
				return this;
			case 1:
				return clockwise();
			case 2:
				return clockwise().clockwise();
			case 3:
				return oppositeSide().counterClockwise();
			case 4:
				return oppositeSide();
			case 5:
				return oppositeSide().clockwise();
			case 6:
				return counterClockwise().counterClockwise();
			default:
				assert (count % 8 == 7);
				return counterClockwise();
		}
	}

	/**
	 * Get the nth side counter clockwise from this one.
	 * 
	 * The count can be any size. A negative count is interpreted as
	 * clockwise rotation instead.
	 * 
	 * @param count
	 *            The step size.
	 * @return The new side.
	 */
	public WangSide counterClockwise(int count) {
		return clockwise(-count);
	}

	/**
	 * @return The Y offset of the neighboring tile.
	 */
	public int getYOffset() {
		switch (this) {
			case TOP:
			case TOP_LEFT:
			case TOP_RIGHT:
				return -1;
			case BOTTOM:
			case BOTTOM_LEFT:
			case BOTTOM_RIGHT:
				return 1;
			default:
				assert (this == LEFT || this == RIGHT);
				return 0;
		}
	}

	/**
	 * @return The X offset of the neighboring tile.
	 */
	public int getXOffset() {
		switch (this) {
			case LEFT:
			case TOP_LEFT:
			case BOTTOM_LEFT:
				return -1;
			case RIGHT:
			case TOP_RIGHT:
			case BOTTOM_RIGHT:
				return 1;
			default:
				assert (this == TOP || this == BOTTOM);
				return 0;
		}
	}
}