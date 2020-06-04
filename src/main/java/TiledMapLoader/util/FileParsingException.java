package TiledMapLoader.util;

/**
 * This type of exception is used for any errors while parsing a TMX file.
 *
 */
@SuppressWarnings("javadoc")
public class FileParsingException extends RuntimeException {

	/**
	 * Just calls <i>RuntimeException()</i>.
	 */
	public FileParsingException() {
		super();
	}

	/**
	 * Just calls <i>RuntimeException(String)</i>.
	 */
	public FileParsingException(String message) {
		super(message);
	}

	/**
	 * Just calls <i>RuntimeException(Throwable)</i>.
	 */
	public FileParsingException(Throwable cause) {
		super(cause);
	}

	/**
	 * Just calls <i>RuntimeException(String, Throwable)</i>.
	 */
	public FileParsingException(String message, Throwable cause) {
		super(message, cause);
	}
}
