package com.prilaga.data.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by macbook on 02.08.15.
 */
public final class Logger {

    public static String TAG = "Prilaga ";
    private static final String LOG_FILE_NAME = "SDK_LOG.txt";

    private static boolean enableLog = false;
    private static boolean enableLogToFile = false;
    private static ExternalLogger externalLogger;

    public static void d(String key, String message) {
        if (enableLog) {
            Log.d(TAG + key, "" + message);
            if (enableLogToFile)
                toFile(key, message, false);
        }
    }

    public static void d(String key, String message, boolean logToFile) {
        if (enableLog) {
            Log.d(TAG + key, "" + message);
            if (logToFile)
                toFile(key, message, false);
        }
    }

    public static void d(String message) {
        if (enableLog) {

//                   String cd = DataUtil.getGSON().toJson(objects);
//            for (Object object : objects) {
//                message += object.getClass().getDeclaredFields().toString() + ": " + object.toString() + " ";
//            }
//            if (BuildConfig.DEBUG) {
            Log.d(TAG, "" + message);
//            }
//            toFile(key, message, false);
        }
    }

    public static void log(String key, String message) {
        if (enableLog) {
            // Split by line, then ensure each line can fit into Log's maximum length.
            for (int i = 0, length = message.length(); i < length; i++) {
                int newline = message.indexOf('\n', i);
                newline = newline != -1 ? newline : length;
                do {
                    int end = Math.min(newline, i + 4000);
                    Log.d(TAG + key, message.substring(i, end));
                    i = end;
                } while (i < newline);
            }
        }
    }

    public static void separator() {
        if (enableLog)
            Log.d(TAG, "-----------------------------------------");
    }

    public static void setEnableLog(boolean enableLog) {
        Logger.enableLog = enableLog;
    }

    public static boolean isLogEnabled() {
        return enableLog;
    }

    public static void setTag(String tag) {
        if (!TextUtils.isEmpty(tag))
            Logger.TAG = tag + " ";
    }

    public static void enableLogToFile(boolean enableLogToFile) {
        Logger.enableLogToFile = enableLogToFile;
    }

    public static void w(String msg) {
        if (enableLog)
            Log.w(TAG, msg);
    }

    public static void i(String msg) {
        if (enableLog)
            Log.i(TAG, msg);
    }

    /**
     * Log Exception
     */

    public static void e(String msg) {
        if (enableLog)
            e(TAG, msg);
    }

    public static void e(String message, Throwable e) {
        e(TAG, message, e);
    }

    public static void e(String tag, String message, Throwable e) {
        if (enableLog)
            Log.e(tag, message, e);
        if (externalLogger != null) {
            externalLogger.logMessage(message);
            externalLogger.logException(e);
        }
    }

    public static void e(String tag, String message) {
        if (enableLog)
            Log.e(TAG + tag, message);
        Exception exception = new Exception(message);
        if (externalLogger != null) {
            externalLogger.logMessage(message);
            externalLogger.logException(exception);
        }
    }

    public static void e(Throwable e) {
        e.printStackTrace();
        if (externalLogger != null)
            externalLogger.logException(e);
    }

    public static void wtf(String message, Throwable e) {
        Log.wtf(TAG, message, e);
        if (externalLogger != null)
            externalLogger.logException(e);
    }

    /**
     * File
     */

    /**
     * READ AND WRITE LOGS TO FILE
     */

    public static void toFile(String key, String message, boolean isNewFile) {
        boolean isGranted = PermissionsUtil.isExternalStorageAvailable();
        if (isGranted) {
            String date = getDate();
            message = "[" + date + "] " + key + ": " + message + "\n";
            File directory = new File(folder());
            File logFile = new File(directory + File.separator + LOG_FILE_NAME);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            if (logFile.exists() && isNewFile)
                logFile.delete();
            if (!logFile.exists()) {          // если лог файл не существует или новый файл растения
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                //BufferedWriter for performance, true to set append to file flag
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                buf.append(message);
                buf.newLine();
                buf.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static String fromFile() {
        String value = "";
        try {
            File logFile = new File(folder() + File.separator + LOG_FILE_NAME);
            value = FileUtils.readFromFile(logFile);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return value;
    }

    public static void removeFile() {
        File directory = new File(folder());
        File logFile = new File(directory + File.separator + LOG_FILE_NAME);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (logFile.exists())
            logFile.delete();
    }

    private static String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH.mm.ss", Locale.US);
        String formattedDateString = dateFormat.format(new java.util.Date());
        return formattedDateString;
    }

    private static String folder() {
        return Environment.getExternalStorageDirectory().toString();
    }

    public static void setExternalLogger(ExternalLogger externalLogger) {
        Logger.externalLogger = externalLogger;
    }

    public interface ExternalLogger {
        void logMessage(String message);

        void logException(Throwable throwable);
    }
}