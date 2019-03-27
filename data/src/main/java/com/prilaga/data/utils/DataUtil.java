package com.prilaga.data.utils;

import android.content.Context;
import android.support.annotation.RawRes;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Администратор on 25.01.2015.
 */
public final class DataUtil {

    private volatile static DataUtil instance;

    /**
     * Returns singleton class instance
     */
    public static DataUtil getInstance() {
        DataUtil localInstance = instance;

        if (localInstance == null) {
            synchronized (DataUtil.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DataUtil();
                }
            }
        }
        return localInstance;
    }

    private TinyDB tinyDB;
    private Encryption encryption;
    private Context context;
    private String ndkKey;

    private DataUtil() {
        getEncryption();
    }

    public void requiredSetup(Context context, String ndkKey) {
        this.context = context;
        this.ndkKey = ndkKey;
    }

    public TinyDB getTinyDB() {
        if (tinyDB == null)
            tinyDB = new TinyDB(context);
        return tinyDB;
    }

    public Encryption getEncryption() {
        if (encryption == null)
            encryption = new Encryption();
        return encryption;
    }

    public String getNdkKey() {
        return ndkKey;
    }

    public Context getContext() {
        return context;
    }

    /**
     * Save & Load object
     */

    public void save(String key, String value) {
        String encryptedString = getEncryption().encrypt(ndkKey, value);
        getTinyDB().putString(key, encryptedString);
    }

    public String load(String key) {
        String encryptedString = getTinyDB().getString(key, "");
        return getEncryption().decrypt(ndkKey, encryptedString);
    }

    /**
     * Read from raw file
     */
    public static String stringFromRaw(Context context, @RawRes int rawId) {
        String values = null;
        try {
            InputStream is = context.getResources().openRawResource(rawId);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            values = writer.toString();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return values;
    }

    /**
     * Zip text
     */

    public static String compress(String str) {
        byte[] compressed = new byte[]{};
        try {
            byte[] blockcopy = ByteBuffer
                    .allocate(4)
                    .order(java.nio.ByteOrder.LITTLE_ENDIAN)
                    .putInt(str.length())
                    .array();
            ByteArrayOutputStream os = new ByteArrayOutputStream(str.length());
            SGZIPOutputStream gos = null;
            gos = new SGZIPOutputStream(os);
            gos.setLevel(Deflater.BEST_COMPRESSION);
            gos.write(str.getBytes());
            gos.close();
            os.close();
            compressed = new byte[4 + os.toByteArray().length];
            System.arraycopy(blockcopy, 0, compressed, 0, 4);
            System.arraycopy(os.toByteArray(), 0, compressed, 4, os.toByteArray().length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(compressed, Base64.DEFAULT);
    }

    public static String decompress(String zipText) {
        String sReturn = "";
        try {
            byte[] compressed = Base64.decode(zipText, Base64.DEFAULT);
            if (compressed.length > 4) {
                GZIPInputStream gzipInputStream = new GZIPInputStream(
                        new ByteArrayInputStream(compressed, 4, compressed.length - 4));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                for (int value = 0; value != -1; ) {
                    value = gzipInputStream.read();
                    if (value != -1) {
                        baos.write(value);
                    }
                }
                gzipInputStream.close();
                baos.close();
                sReturn = new String(baos.toByteArray(), "UTF-8");
                return sReturn;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sReturn;
    }

    private static class SGZIPOutputStream extends GZIPOutputStream {

        public SGZIPOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        public void setLevel(int level) {
            def.setLevel(level);
        }
    }
}
