package com.prilaga.data.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.TextUtils;

//import com.google.android.gms.ads.identifier.AdvertisingIdClient;
//import com.prilaga.data.utils.Logger;
//import com.sdk.SDK;
//import com.sdk.helper.SimpleCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleg Tarashkevich on 17.05.16.
 */
public final class TechUtil {

    private static String advertisingId;

    public static boolean isRunningOnEmulator() {
        boolean result =//
                Build.FINGERPRINT.startsWith("generic")//
                        || Build.FINGERPRINT.startsWith("unknown")//
                        || Build.MODEL.contains("google_sdk")//
                        || Build.MODEL.contains("Emulator")//
                        || Build.MODEL.contains("Android SDK built for x86")
                        || Build.MANUFACTURER.contains("Genymotion");
        if (result)
            return true;
        result |= Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic");
        if (result)
            return true;
        result |= "google_sdk".equals(Build.PRODUCT);
        return result;
    }

    public boolean isRooted() {
        boolean found = false;
        if (!found) {
            String[] places = {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/",
                    "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
            for (String where : places) {
                if (new File(where + "su").exists()) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    public static String getDeviceModel() {
        return Build.MANUFACTURER + " - " + Build.MODEL;
    }

    public static String getAndroidID() {
        return Settings.Secure.getString(DataUtil.getInstance().getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getAdvertisingId() {
//      return getAdvertisingId(null);
      return advertisingId;
    }

//    public static String getAdvertisingId(final SimpleCallback callback) {
//        if (TextUtils.isEmpty(advertisingId)) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        advertisingId = AdvertisingIdClient.getAdvertisingIdInfo(SDK.getContext().getApplicationContext()).getId();
//                        Logger.d("advertisingId", advertisingId);
//                        if (callback != null)
//                            callback.onSuccessAction();
//                    } catch (Throwable e) {
//                        Logger.e("TechUtil", e);
//                        if (callback != null)
//                            callback.onErrorAction(e);
//                    }
//                }
//            }).start();
//        }
//        return advertisingId;
//    }

    public String getAndroidVersion() {
        return Build.VERSION.SDK_INT + " (" + Build.VERSION.RELEASE + ") root: " + isRooted();
    }

    public int getVersionSDK() {
        return Build.VERSION.SDK_INT;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static String getBuildVersion() {
        String buildVersion = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String[] ABIS = Build.SUPPORTED_ABIS;
            buildVersion = ABIS[0];
        } else
            buildVersion = Build.CPU_ABI;

        return buildVersion;
    }

    @SuppressLint("MissingPermission")
    public static void phone(String number) {
        if (!TextUtils.isEmpty(number)) {
            number = "tel:" + number;
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                DataUtil.getInstance().getContext().startActivity(callIntent);
            } catch (Throwable e) {
                Logger.e(e);
            }
        }
    }

    public static String displayName(String phoneNumber) {
        String contactName = "";
        try {
            List<Person> persons = new ArrayList<>();

            if (!TextUtils.isEmpty(phoneNumber)) {
                ContentResolver cr = DataUtil.getInstance().getContext().getContentResolver();

                String whereName = ContactsContract.Data.MIMETYPE + " = ?";
                String[] whereNameParams = new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
                Cursor cursor = cr.query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
                cursor.moveToFirst();
                while (cursor.moveToNext()) {
                    Person person = new Person();
                    person.given = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                    person.middle = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME));
                    person.family = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                    person.display = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
                    persons.add(person);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactName;
    }

    public static String getContactDisplayNameByNumber(String number) {
        String name = "?";
        try {

            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

            ContentResolver contentResolver = DataUtil.getInstance().getContext().getContentResolver();
            Cursor contactLookup = contentResolver.query(uri, new String[]{BaseColumns._ID,
                    ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

            try {
                if (contactLookup != null && contactLookup.getCount() > 0) {
                    contactLookup.moveToNext();
                    name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                    //String contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
                }
            } finally {
                if (contactLookup != null) {
                    contactLookup.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return name;
    }

    public void readContacts() {
        ContentResolver cr = DataUtil.getInstance().getContext().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    System.out.println("name : " + name + ", ID : " + id);

                    // get the phone number
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        System.out.println("phone" + phone);
                    }
                    pCur.close();


                    // get email and type

                    Cursor emailCur = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCur.moveToNext()) {
                        // This would allow you get several email addresses
                        // if the email addresses were stored in an array
                        String email = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        String emailType = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

                        System.out.println("Email " + email + " Email Type : " + emailType);
                    }
                    emailCur.close();

                    // Get note.......
                    String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] noteWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
                    Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
                    if (noteCur.moveToFirst()) {
                        String note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
                        System.out.println("Note " + note);
                    }
                    noteCur.close();

                    //Get Postal Address....

                    String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] addrWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
                    Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI,
                            null, null, null, null);
                    while (addrCur.moveToNext()) {
                        String poBox = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
                        String street = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                        String city = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                        String state = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                        String postalCode = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                        String country = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                        String type = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));

                        // Do something with these....

                    }
                    addrCur.close();

                    // Get Instant Messenger.........
                    String imWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] imWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE};
                    Cursor imCur = cr.query(ContactsContract.Data.CONTENT_URI,
                            null, imWhere, imWhereParams, null);
                    if (imCur.moveToFirst()) {
                        String imName = imCur.getString(
                                imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
                        String imType;
                        imType = imCur.getString(
                                imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE));
                    }
                    imCur.close();

                    // Get Organizations.........

                    String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] orgWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                    Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI,
                            null, orgWhere, orgWhereParams, null);
                    if (orgCur.moveToFirst()) {
                        String orgName = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                        String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                    }
                    orgCur.close();
                }
            }
        }
    }

    public static boolean hasPackage(Context context, String packageName){
        boolean hasPackage = false;
        try {
            PackageManager pm = context.getPackageManager();
            List<ApplicationInfo> apps = pm.getInstalledApplications(0);
            for (ApplicationInfo app : apps) {
                if (app.packageName.equalsIgnoreCase(packageName)){
                    hasPackage = true;
                    break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return hasPackage;
    }


    public static class Person {
        String name;
        String given;
        String middle;
        String family;
        String display;
    }

}
