package core;

import org.w3c.dom.Element;
import privateUtil.Util;
import util.AttributeParsingErrorException;

public class TileLayer<IMG> extends Layer {
	private static final long FLIPPED_HORIZ_FLAG = 0x80000000;
	private static final long FLIPPED_VERT_FLAG = 0x40000000;
	private static final long FLIPPED_DIAG_FLAG = 0x20000000;
	private static final long GID_MASK = ~(FLIPPED_HORIZ_FLAG | FLIPPED_VERT_FLAG | FLIPPED_DIAG_FLAG);

	public Tile<IMG>[][] tiles;
	public final int width;
	public final int height;

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
			throw new RuntimeException("Unsupported data encoding: '" + encoding + "'");
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
					throw new RuntimeException("Tile flipping and rotating not supported yet!");
				}

				tiles[col][row] = parent.getTile((int) gid);
			}
		}
	}
}
