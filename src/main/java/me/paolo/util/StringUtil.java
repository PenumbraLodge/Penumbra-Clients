package me.paolo.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil
{

    // TODO: Add more escape support
    public static String encodePath(String url) {
        return url.replace(" ", "%20");
    }

    public static boolean isEmpty(String str) {
        if(str == null) return true;
        return str.isEmpty();
    }

    public static boolean equals(String str0, String str1) {
        if(str0 == null && str1 == null) return true;
        if(str0 == null || str1 == null) return false;
        return str0.equals(str1);
    }

}
