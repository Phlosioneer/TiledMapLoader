package util;

import java.io.InputStream;

public interface FileLocatorDelegate {
	InputStream openFile(String filename);
}
