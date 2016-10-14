package file.utils;

import java.io.FileWriter;

public class FileUtils {
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
