package core;

import org.w3c.dom.Element;
import privateUtil.Util;
import util.AttributeParsingErrorException;
import util.FileParsingException;

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
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				long gid = Long.parseLong(gidStrings[col + row * width].trim());

				// Check for tile flipping.
				boolean flippedHoriz = (gid & 0x80000000) != 0;
				boolean flippedVert = (gid & 0x40000000) != 0;
				boolean flippedDiag = (gid & 0x20000000) != 0;
				gid &= GID_MASK;

				if (flippedHoriz || flippedVert || flippedDiag) {
					throw new FileParsingException("Tile flipping and rotating not supported yet!");
				}

				tiles[col][row] = parent.getTile((int) gid);
			}
		}
	}

	/**
	 * Manually create a TileLayer instance. No fields are initialized.
	 */
	public TileLayer() {}

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
