package main.jsonfile;

import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class OperationJsonFile extends JsonWriting {

    // get a file from the resources folder
    // works everywhere, IDEA, unit test and JAR file.
    private InputStream getFileFromResourceAsStream(final String fileName) {

        // The class loader that loaded the class
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    public final void initializeJsonFile() {
        final JSONParser parser = new JSONParser();
        final String fileName = "utente.json";

        try {
            // create jsonArray from file
            //final OperationJsonFile app = new OperationJsonFile();
            final InputStream is = getFileFromResourceAsStream(fileName);
            final JSONArray users = (JSONArray) parser.parse(new InputStreamReader(is, "UTF-8"));
            // Writing to a file
            writeJsonFile(users);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
