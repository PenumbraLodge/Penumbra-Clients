package me.paolo.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class StringUtil
{

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
