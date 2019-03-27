package com.prilaga.data.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Oleg Tarashkevich on 22.07.16.
 */
public final class NumberUtil {

//    public static long uniqueId() {
//        long uniqueId = SDKConstants.NONE_LONG;
//        try {
//            UUID uuid = UUID.randomUUID();
//            Field nodeField = UUID.class.getDeclaredField("node");
//            nodeField.setAccessible(true);
//            uniqueId = (Long) nodeField.get(uuid);
//            uniqueId = Math.abs(uniqueId);
//
//            long id1 = UUID.randomUUID().getMostSignificantBits();
//            long id2 = UUID.randomUUID().getLeastSignificantBits();
//
//            int idInt1 = (int)id1;
//            int idInt2 = (int)id2;
//
//            Logger.d("","");
//        } catch (Exception e) {
//            Logger.e(e);
//        }
//
//        return uniqueId;
//    }

    private static Random random = new Random();

    /**
     * converts int number 1000 to string 1,000
     */
    public static String formatNumber(int number) {
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setGroupingSeparator(',');

        DecimalFormat df = new DecimalFormat("###,###", sym);
        df.setMaximumFractionDigits(0);
        df.setMinimumIntegerDigits(1);
        df.setGroupingSize(3);
        return df.format(number);
    }

    public static String formatDouble(double number) {
        return String.format("%.2f", number);
    }

    public static int randomInt(int max, int min) {
        int range = max - min + 1;
        int randomNum = random.nextInt(range) + min;
        return randomNum;
    }

    public static long uniqueLong() {
        long uniqueLong = UUID.randomUUID().getMostSignificantBits();
        uniqueLong = Math.abs(uniqueLong);
        return uniqueLong;
    }

    public static int uniqueInt() {
        int uniqueInt = (int)uniqueLong();
        uniqueInt = Math.abs(uniqueInt);
        return uniqueInt;
    }
}
