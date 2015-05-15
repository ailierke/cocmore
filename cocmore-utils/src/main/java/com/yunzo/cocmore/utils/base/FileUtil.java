package com.yunzo.cocmore.utils.base;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class FileUtil {
	/**
     *下载
     * 
     * @param is
     * @param uploadedFileLocation
     * @return
     */
    public static File writeToFile(InputStream is, String uploadedFileLocation) {
        File file = new File(uploadedFileLocation);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            while ((is.read(buffer)) != -1) {
                os.write(buffer);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // System.out.println(uploadedFileLocation + "--" + file.length());
        if (file.length() < 5) {
            file.delete();
            return null;
        }
        return file;

    }

    public static String getEndWith(String fileName) {
        int i = fileName.lastIndexOf(".");
        if (i >= 0) {
            return fileName.substring(i + 1, fileName.length());
        }
        return "";
    }

    public static String getParent(String fileName) {
        int i = fileName.lastIndexOf("\\");
        if (i >= 0) {
            return fileName.substring(0, i);
        }
        return "";
    }
}
