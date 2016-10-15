package file.utils;

import android.util.Log;

import java.io.FileWriter;

public class FileUtils {
    public static void saveDataToFile(String data, String path,boolean append) {
        try {
            FileWriter writer = new FileWriter(path, append);
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFileName(String fileName, boolean getOnlyExtension) {
        try {
            String[] splittedFileName = fileName.split("\\.");
            return getOnlyExtension ? splittedFileName[1] : splittedFileName[0];
        }
        catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
}
