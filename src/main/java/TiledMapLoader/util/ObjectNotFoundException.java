package TiledMapLoader.util;

/**
 * Thrown by getObjectByName if no matches are found.
 */
public class ObjectNotFoundException extends RuntimeException {

	/**
	 * @param name
	 *            The name the user searched for.
	 */
	public ObjectNotFoundException(String name) {
		super("No object named '" + name + "' was found.");
	}
}
