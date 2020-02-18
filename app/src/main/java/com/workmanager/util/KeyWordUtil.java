package com.workmanager.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyWordUtil {


    public static SpannableString matcherSearchTitle(int color, String text, String keyword) {
        SpannableString s = new SpannableString(text);
        keyword=escapeExprSpecialWord(keyword);
        text=escapeExprSpecialWord(text);
        if (text.contains(keyword)&&!TextUtils.isEmpty(keyword)){
            try {
                Pattern p = Pattern.compile(keyword);
                Matcher m = p.matcher(s);
                while (m.find()) {
                    int start = m.start();
                    int end = m.end();
                    s.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }catch (Exception e){
            }
        }
        return s;
    }

    public static String escapeExprSpecialWord(String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }
}