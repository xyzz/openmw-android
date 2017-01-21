package utils;

import java.io.File;

/**
 * Created by sandstranger on 15.01.17.
 */

public class Utils {
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public static boolean fileExists(String path) {
        return new File(path).exists();
    }
}
