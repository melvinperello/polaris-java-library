package afterschoolcreatives.polaris.java.util;

import java.util.HashMap;
import afterschoolcreatives.polaris.java.io.FileReading;
import afterschoolcreatives.polaris.java.io.FileWriting;
import java.io.IOException;

/**
 * This class acts as a normal HashMap but has extended features to read
 * contents from a property file with a key value pair and also has the ability
 * to modify its contents.
 *
 * @author Jhon Melvin
 */
public class PropertyHashMap extends HashMap<String, String>
        implements FileReading, FileWriting {

    /**
     * Serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Read contents of a property file.
     *
     * @throws IOException
     */
    @Override
    public void fileRead() throws IOException {

    }

    /**
     * Write contents to a property file.
     *
     * @throws IOException
     */
    @Override
    public void fileWrite() throws IOException {

    }

}
