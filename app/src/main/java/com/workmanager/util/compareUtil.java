package com.workmanager.util;

public class compareUtil {
    public static int compareVersion(String v1,String v2){
        String[] v1s = v1.split("\\.");
        String[] v2s = v2.split("\\.");
        int result = compareString(v1s,v2s);
       /* if(result>0){
            System.out.println("v2 version is smaller");
        }else if(result<0){
            System.out.println("v1 version is smaller");
        }else if(result == 0){
            System.out.println("v1 equals v2");
        }*/
        return result;
    }

    public static int compareString(String[] v1s,String[] v2s){
        int len = 0;
        if(v1s.length>v2s.length){
            len = v1s.length;
        }else{
            len = v2s.length;
        }
        for(int i=0;i<len;i++){
            try {
                int com = compareChars(v1s[i], v2s[i]);
                if (com != 0) {
                    return com;
                }
            }catch (ArrayIndexOutOfBoundsException e){
                if(v1s.length>v2s.length){
                    return 1;
                }else{
                    return -1;
                }
            }
        }
        return 0;
    }

    public static int compareChars(String v1s,String v2s){
        char[] c1 = v1s.toCharArray();
        char[] c2 = v2s.toCharArray();

        if(c1.length==c2.length){
            for (int i = 0; i < c1.length; i++) {
                if(getHashCode(c1[i])>getHashCode(c2[i])){
                    return 1;
                }else if(getHashCode(c1[i])<getHashCode(c2[i])){
                    return -1;
                }
            }
        }

        if(c1.length<c2.length){
            for (int i = 0; i < c1.length; i++) {
                if(getHashCode(c1[i])>getHashCode(c2[i])){
                    return 1;
                }else if(getHashCode(c1[i])<getHashCode(c2[i])){
                    return -1;
                }
            }
            return 1;
        }

        if(c1.length>c2.length){
            for (int i = 0; i < c2.length; i++) {
                if(getHashCode(c1[i])>getHashCode(c2[i])){
                    return 1;
                }else if(getHashCode(c1[i])<getHashCode(c2[i])){
                    return 1;
                }
            }
            return -1;
        }

        return 0;
    }

    public static int getHashCode(char c){
        return String.valueOf(c).toLowerCase().hashCode();
    }
}

