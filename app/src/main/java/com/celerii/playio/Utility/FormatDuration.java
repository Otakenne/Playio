package com.celerii.playio.Utility;

public class FormatDuration {
    public static String formatDuration(long duration) {
        int quotient = (int) duration / 60;
        int remainder = (int) duration % 60;
        return String.valueOf(quotient) + ":" + String.valueOf(makeTwoDigits(remainder));
    }

    public static String makeTwoDigits(int val){
        if (val >= 0 && val < 10){
            return "0" + String.valueOf(val);
        }
        return String.valueOf(val);
    }
}
