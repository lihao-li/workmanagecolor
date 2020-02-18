package com.workmanager.util;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9.]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    public static boolean ispass(String str){
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }


    public static String parseTextRecord(NdefRecord ndefRecord) {

        if (ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN) {
            return null;
        }
        if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
            return null;
        }
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 0x80) == 0) ? "UTF-8" : "UTF-16";
            int languageCodeLength = payload[0] & 0x3f;
            String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            String textRecord = new String(payload, languageCodeLength + 1,
                    payload.length - languageCodeLength - 1, textEncoding);
            return textRecord;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
    public static String readNfcTag(Intent intent) {
        String textRecord="";
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msgs[] = null;
            int contentSize = 0;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    contentSize += msgs[i].toByteArray().length;
                }
            }
            try {
                if (msgs != null) {
                    NdefRecord record = msgs[0].getRecords()[0];
                    textRecord = parseTextRecord(record);
                }
            } catch (Exception e) {
            }
        }
        return textRecord;
    }
}
