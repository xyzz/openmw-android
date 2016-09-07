package utils;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by sandstranger on 07.09.2016.
 */
public class Utils {
    public static void saveDataToFile(String data, String path) {
        try {
            FileWriter writer = new FileWriter(path, false);
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
