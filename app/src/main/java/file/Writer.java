package file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by sandstranger on 13.10.16.
 */

  public class Writer {

    /**
     * Replaces a key=value in config file with the new value
     * @param path Path to the configuration file to edit
     * @param key Key to replace
     * @param value New value to put
     */
        public static void write(String path, String key, String value)
                throws IOException {
            // Create a new empty file if it doesn't already exist
            File fin = new File(path);
            //noinspection ResultOfMethodCallIgnored
            fin.createNewFile();

            FileInputStream file = new FileInputStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            StringBuilder builder = new StringBuilder();
            boolean contains = false;
            while (line != null) {
                if (line.startsWith(key) && !contains) {
                    builder.append(key + "=" + value);
                    contains = true;
                } else
                    builder.append(line);
                builder.append("\n");
                line = reader.readLine();
            }
            if (!contains)
                builder.append(key + "=" + value);

            reader.close();
            FileWriter writer = new FileWriter(path);
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        }
    }
