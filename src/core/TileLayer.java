package core;

import org.w3c.dom.Element;
import privateUtil.Util;
import util.AttributeParsingErrorException;
import util.FileParsingException;
import util.ImageTransform;
import util.Rect;
import util.ResourceLoaderDelegate;
import util.Vector;

/**
 * A layer of tiles.
 * 
 * @param <IMG>
 *            The IMG param of the parent MapFile. See MapFile for more info.
 */
public class TileLayer<IMG> extends Layer {
	private static final long FLIPPED_HORIZ_FLAG = 0x80000000;
	private static final long FLIPPED_VERT_FLAG = 0x40000000;
	private static final long FLIPPED_DIAG_FLAG = 0x20000000;
	private static final long GID_MASK = ~(FLIPPED_HORIZ_FLAG | FLIPPED_VERT_FLAG | FLIPPED_DIAG_FLAG);

	/**
	 * The tiles in this layer. Tiles are stored in <i>tile[x][y]</i> format. Cannot be null.
	 */
	public Tile<IMG>[][] tiles;
	/**
	 * The width of the <i>tiles</i> field. Must match <i>tiles.length</i>.
	 */
	public int width;
	/**
	 * The height of the <i>tiles</i> field. Must match <i>tiles[_].length</i>.
	 */
	public int height;
	public FlipState[][] tileFlipStates;

	public enum FlipState {
		Normal, Rot90, Rot180, Rot270, HFlip, HFlipRot90, HFlipRot180, HFlipRot270
	}

	// Needed for rendering
	private int tileWidth;
	private int tileHeight;

	@SuppressWarnings("unchecked")
	TileLayer(Element element, MapFile<IMG> parent) {
		super(element);

		width = Util.getIntAttribute(element, "width");
		if (width < 0) {
			throw new AttributeParsingErrorException(element, "width", "Value cannot be negative", width);
		}

		height = Util.getIntAttribute(element, "height");
		if (height < 0) {
			throw new AttributeParsingErrorException(element, "height", "Value cannot be negative", height);
		}

		Element dataElement = Util.getSingleTag(element, "data", true);

		String encoding = Util.getStringAttribute(dataElement, "encoding", "individual");
		if (!encoding.equals("csv")) {
			throw new FileParsingException("Unsupported data encoding: '" + encoding + "'");
		}

		String csv = dataElement.getTextContent();
		String[] gidStrings = csv.split(",");
		tiles = new Tile[width][height];
		tileFlipStates = new FlipState[width][height];
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				long gid = Long.parseLong(gidStrings[col + row * width].trim());

				// Check for tile flipping.
				boolean flippedHoriz = (gid & 0x80000000) != 0;
				boolean flippedVert = (gid & 0x40000000) != 0;
				boolean flippedDiag = (gid & 0x20000000) != 0;
				gid &= GID_MASK;

				FlipState state;
				if (flippedHoriz) {
					state = FlipState.HFlip;
				} else if (flippedHoriz && flippedVert) {
					state = FlipState.Rot180;
				} else if (flippedVert) {
					state = FlipState.HFlipRot180;
				} else if (flippedDiag) {
					state = FlipState.HFlipRot90;
				} else if (flippedHoriz && flippedDiag) {
					state = FlipState.Rot90;
				} else if (flippedVert && flippedDiag) {
					state = FlipState.HFlipRot270;
				} else if (flippedHoriz && flippedVert && flippedDiag) {
					state = FlipState.Rot270;
				} else {
					state = FlipState.Normal;
				}
				tileFlipStates[col][row] = state;

				tiles[col][row] = parent.getTile((int) gid);
			}
		}

		tileWidth = parent.tileWidth;
		tileHeight = parent.tileHeight;
	}

	/**
	 * Manually create a TileLayer instance. No fields are initialized.
	 */
	public TileLayer() {}

	@Override
	public <IMG2> IMG2 renderToImage(Rect pixelBounds, IMG2 baseImage, Vector renderOffset, float opacity, ResourceLoaderDelegate<IMG2> delegate) {
		// TODO: Respect pixelBounds
		IMG2 mutableBaseImage = baseImage;
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				int netX = Math.round(col * tileWidth + renderOffset.x);
				int netY = Math.round(row * tileHeight + renderOffset.y);
				float netOpacity = Util.combineOpacities(opacity, this.opacity);
				Tile tile = tiles[col][row];
				@SuppressWarnings("unchecked")
				IMG2 image = (IMG2) tile.image;
				ImageTransform transform = new ImageTransform(netX, netY, netOpacity, tileFlipStates[col][row]);
				mutableBaseImage = delegate.composeOntoImage(mutableBaseImage, image, transform);
			}
		}
		return mutableBaseImage;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object clone() throws CloneNotSupportedException {
		TileLayer<IMG> ret = (TileLayer<IMG>) super.clone();
		ret.tiles = new Tile[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				ret.tiles[i][j] = tiles[i][j];
			}
		}
		return ret;
	}
}
