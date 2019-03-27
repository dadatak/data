/*
 * Copyright 2014 KC Ochibili
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.prilaga.data.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class TinyDB {
    Context mContext;
    SharedPreferences preferences;
    String DEFAULT_APP_IMAGEDATA_DIRECTORY;
    File mFolder = null;
    public static String lastImagePath = "";

    public TinyDB(Context appContext) {
        mContext = appContext;
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public TinyDB(Context appContext, String fileKey) {
        mContext = appContext;
        preferences = mContext.getSharedPreferences(fileKey, Context.MODE_PRIVATE);
    }

    public Bitmap getImage(String path) {
        Bitmap theGottenBitmap = null;
        try {
            theGottenBitmap = BitmapFactory.decodeFile(path);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return theGottenBitmap;
    }

    /**
     * Returns the String filePath of the last image that was saved with this Object
     * <p>
     */
    public String getSavedImagePath() {
        return lastImagePath;
    }

    /**
     * Returns the String filePath of the last image that was saved with this
     * tnydbobj
     * <p>
     * <p>
     * String
     * the theFolder - the folder filePath dir you want to save it to e.g
     * "DropBox/WorkImages"
     * String
     * the theImageName - the name you want to assign to the image file e.g
     * "MeAtlunch.png"
     */
    public String putImagePNG(String theFolder, String theImageName,
                              Bitmap theBitmap) {
        this.DEFAULT_APP_IMAGEDATA_DIRECTORY = theFolder;
        String mFullPath = setupFolderPath(theImageName);
        saveBitmapPNG(mFullPath, theBitmap);
        lastImagePath = mFullPath;
        return mFullPath;
    }

    private String setupFolderPath(String imageName) {
        File sdcard_path = Environment.getExternalStorageDirectory();
        mFolder = new File(sdcard_path, DEFAULT_APP_IMAGEDATA_DIRECTORY);
        if (!mFolder.exists()) {
            if (!mFolder.mkdirs()) {
                Log.e("While creatingsave path",
                        "Default Save Path Creation Error");
                // Toast("Default Save Path Creation Error");
            }
        }
        String savePath = mFolder.getPath() + '/' + imageName;
        return savePath;
    }

    private boolean saveBitmapPNG(String strFileName, Bitmap bitmap) {
        if (strFileName == null || bitmap == null)
            return false;
        boolean bSuccess1 = false;
        boolean bSuccess2;
        boolean bSuccess3;
        File saveFile = new File(strFileName);

        if (saveFile.exists()) {
            if (!saveFile.delete())
                return false;
        }

        try {
            bSuccess1 = saveFile.createNewFile();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        OutputStream out = null;
        try {
            out = new FileOutputStream(saveFile);
            bSuccess2 = bitmap.compress(CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
            bSuccess2 = false;
        }
        try {
            if (out != null) {
                out.flush();
                out.close();
                bSuccess3 = true;
            } else
                bSuccess3 = false;

        } catch (IOException e) {
            e.printStackTrace();
            bSuccess3 = false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return (bSuccess1 && bSuccess2 && bSuccess3);
    }

    public int getInt(String key, int defaultKey) {
        return preferences.getInt(key, defaultKey);
    }

    public long getLong(String key, int defaultKey) {
        return preferences.getLong(key, defaultKey);
    }

    public String getString(String key, String defaultKey) {
        return preferences.getString(key, defaultKey);
    }

    public double getDouble(String key, double defaultKey) {
        String number = getString(key, defaultKey + "");
        try {
            double value = Double.parseDouble(number);
            return value;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void putDouble(String key, double value) {
        putString(key, String.valueOf(value));
    }

    public void putString(String key, String value) {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putList(String key, ArrayList<String> marray) {

        SharedPreferences.Editor editor = preferences.edit();
        String[] mystringlist = marray.toArray(new String[marray.size()]);
        // the comma like character used below is not a comma it is the SINGLE
        // LOW-9 QUOTATION MARK unicode 201A and unicode 2017 they are used for
        // seprating the items in the list
        editor.putString(key, TextUtils.join("‚‗‚", mystringlist));
        editor.apply();
    }

    public ArrayList<String> getList(String key) {
        // the comma like character used below is not a comma it is the SINGLE
        // LOW-9 QUOTATION MARK unicode 201A and unicode 2017 they are used for
        // seprating the items in the list
        String[] mylist = TextUtils
                .split(preferences.getString(key, ""), "‚‗‚");
        ArrayList<String> gottenlist = new ArrayList<String>(
                Arrays.asList(mylist));
        return gottenlist;
    }

    public void putListInt(String key, ArrayList<Integer> marray,
                           Context context) {
        SharedPreferences.Editor editor = preferences.edit();
        Integer[] mystringlist = marray.toArray(new Integer[marray.size()]);
        // the comma like character used below is not a comma it is the SINGLE
        // LOW-9 QUOTATION MARK unicode 201A and unicode 2017 they are used for
        // seprating the items in the list
        editor.putString(key, TextUtils.join("‚‗‚", mystringlist));
        editor.apply();
    }

    public ArrayList<Integer> getListInt(String key,
                                         Context context) {
        // the comma like character used below is not a comma it is the SINGLE
        // LOW-9 QUOTATION MARK unicode 201A and unicode 2017 they are used for
        // seprating the items in the list
        String[] mylist = TextUtils
                .split(preferences.getString(key, ""), "‚‗‚");
        ArrayList<String> gottenlist = new ArrayList<String>(
                Arrays.asList(mylist));
        ArrayList<Integer> gottenlist2 = new ArrayList<Integer>();
        for (int i = 0; i < gottenlist.size(); i++) {
            gottenlist2.add(Integer.parseInt(gottenlist.get(i)));
        }

        return gottenlist2;
    }

    public void putListBoolean(String key, ArrayList<Boolean> marray) {
        ArrayList<String> origList = new ArrayList<String>();
        for (Boolean b : marray) {
            if (b == true) {
                origList.add("true");
            } else {
                origList.add("false");
            }
        }
        putList(key, origList);
    }

    public ArrayList<Boolean> getListBoolean(String key) {
        ArrayList<String> origList = getList(key);
        ArrayList<Boolean> mBools = new ArrayList<Boolean>();
        for (String b : origList) {
            if (b.equals("true")) {
                mBools.add(true);
            } else {
                mBools.add(false);
            }
        }
        return mBools;
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defaultKey) {
        return preferences.getBoolean(key, defaultKey);
    }

    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public float getFloat(String key) {
        return getFloat(key, 0f);
    }

    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public Boolean deleteImage(String path) {
        File tobedeletedImage = new File(path);
        Boolean isDeleted = tobedeletedImage.delete();
        return isDeleted;
    }

    public void clear() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public <N> List<N> getAll(String fileKey) {


        Map<String, ?> keys = preferences.getAll();
        List<N> list = new ArrayList<>();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            try {
                list.add((N) entry.getValue());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        return list;
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

}
