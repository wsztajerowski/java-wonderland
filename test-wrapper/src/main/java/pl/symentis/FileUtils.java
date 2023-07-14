package pl.symentis;

import java.nio.file.Path;

public class FileUtils {
    public static String getFilenameWithoutExtension(Path pathToFile) {
        return getFilenameWithoutExtension(pathToFile.getFileName().toString());
    }
    public static String getFilenameWithoutExtension(String filename) {
        if (filename.indexOf(".") > 0) {
            return filename.substring(0, filename.lastIndexOf("."));
        }
        return filename;
    }
}
