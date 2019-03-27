package com.prilaga.data.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by Oleg Tarashkevich on 18.03.17.
 */

public final class FileUtils {

    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        return Environment.getExternalStorageState().startsWith(Environment.MEDIA_MOUNTED) ?
                new File(context.getExternalCacheDir(), uniqueName) :
                new File(context.getCacheDir(), uniqueName);

    }

    public static File getBackupDir(String uniqueName){
        File root = Environment.getExternalStorageDirectory();
        boolean exists;
        File file = new File(root, uniqueName);
        exists = file.exists();
        if (!exists)
            exists = file.mkdir();
        return file;
    }

    public static void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath + inputFile).delete();

        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    public static void deleteFile(String inputPath, String inputFile) {
        try {
            // delete the original file
            new File(inputPath + inputFile).delete();
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    public static void copyFile(String inputPath, String inputFile, String outputPath, String outputFile) {
        copyFile(inputPath + inputFile, outputPath, outputFile);
    }

    public static void copyFile(String inputPath, String outputPath, String outputFile) {
        copyFile(inputPath, outputPath + File.separator + outputFile);
    }

    public static void copyFile(String inputPath, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    public static String readFromFile(File file) {
        String aBuffer = "";
        boolean isGranted = PermissionsUtil.isExternalStorageAvailable();
        if (isGranted) {

            try {
                FileInputStream fIn = new FileInputStream(file);
                BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
                String aDataRow = "";
                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer += aDataRow + "\n";
                }
                myReader.close();
                aBuffer = aBuffer.trim();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return aBuffer;
    }

    public static void writeToFile(File file, String text, boolean isNewFile) {
        boolean isGranted = PermissionsUtil.isExternalStorageAvailable();
        if (isGranted) {
            if (file.exists() && isNewFile)
                file.delete();
            if (!file.exists()) {          // если лог файл не существует или новый файл растения
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                //BufferedWriter for performance, true to set append to file flag
                BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
                buf.append(text);
                buf.newLine();
                buf.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
