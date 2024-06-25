package com.example.demo.repository.nguyen;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {

    public static String toLowerCaseNonAccentVietnamese(String str) {
        if (str == null) {
            return null;
        }
        String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").toLowerCase();
    }
}
