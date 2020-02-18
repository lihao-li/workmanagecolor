package com.workmanager.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;

import com.necer.ndialog.ConfirmDialog;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

import static android.nfc.NdefRecord.RTD_TEXT;


public class NFCUtil {
    private static final String TAG = "NFCUtil";
    private static final boolean isDebug = true;
    public NFCUtil(Context context) {
        init(context);
    }

    public boolean isSupportNFC() {
        boolean ret = mNfcAdapter != null;
        log("isSupportNFC: " + ret);
        return ret;
    }

    public boolean isEnableNFC() {
        boolean ret = mNfcAdapter != null && mNfcAdapter.isEnabled();
        log("isEnableNFC: " + ret);
        return ret;
    }

    public void registerNFC(Activity activity) {
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(activity, mPendingIntent, null, null);
            log("registerNFC: 注册NFC广播监听");
        }
    }

    public void unRegisterNFC(Activity activity) {
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(activity);
            log("unRegisterNFC: unRegisterNFC");
        }
    }


    public void showFNCSetting(final Context context) {

            new ConfirmDialog(context, true)
                    .setTtitle("確認")
                    .setMessage("NFCリーダーを開いてください！")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent("/");
                            ComponentName cm = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                            intent.setComponent(cm);
                            intent.setAction("android.intent.action.VIEW");
                            context.startActivity( intent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
    }


    /**
     * 读取NFC TAG 的16进制ID
     *
     * @param intent
     * @return
     */
    public String readNFC_ID(Intent intent) {
        if (intent == null)
            return null;
        byte[] bytesId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        String tagID = bytesToHexString(bytesId);
        log("readNFC_ID: " + tagID);
        return tagID;
    }

    /**
     * 读取NFC NDEF数据
     *
     * @param intent
     * @return
     */
    public String readNFC_NDEF(Intent intent) {
        if (intent != null && TextUtils.equals(intent.getAction(), NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawArray != null && rawArray.length > 0) {
                NdefMessage ndefMessage = (NdefMessage) rawArray[0];
                NdefRecord[] records = ndefMessage.getRecords();
                if (records != null && records.length > 0) {
                    NdefRecord ndefRecord = records[0];
                    return parseTextRecord(ndefRecord);
                }
            }
        }
        return null;
    }


    /**
     * 向NFC标签中写入NDEF格式数据
     *
     * @param intent
     * @param text
     * @return
     */
    public boolean writeNFC_NDEF(Intent intent, String text) {
        if (intent != null && text != null) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            return writeTag(tag, new NdefMessage(new NdefRecord[]{createTextRecord(Locale.CHINA.getLanguage(), text)}));
//            return writeTag(tag, new NdefMessage(new NdefRecord[]{NdefRecord.createTextRecord(Locale.CHINA.getLanguage(), text)}));
        }
        return false;
    }

    /**
     * 向NFC标签中写入自动运行程序包名
     *
     * @param intent
     * @param packageName
     * @return
     */
    public boolean writeNFC_RunApp(Intent intent, String packageName) {
        if (intent != null && packageName != null) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            return writeTag(tag, new NdefMessage(new NdefRecord[]{NdefRecord.createApplicationRecord(packageName)}));
        }
        return false;
    }

    /**
     * 向NFC标签中写入Uri数据
     *
     * @param intent
     * @param uri
     * @return
     */
    public boolean writeNFC_Uri(Intent intent, String uri) {
        if (intent != null && uri != null) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            return writeTag(tag, new NdefMessage(new NdefRecord[]{NdefRecord.createUri(uri)}));
        }
        return false;
    }

    /**
     * 向NFC标签中写入非NDEF格式数据,要求标签支持MifareUltralight数据格式
     *
     * @param intent
     * @param text
     * @return
     */
    public boolean writeNFC_MU(Intent intent, String text) {
        if (intent != null && text != null) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            for (String tech : techList) {
                if (TextUtils.equals(tech, MifareUltralight.class.getName())) {
                    return writeMUTag(tag, text);
                }
            }
            log("writeNFC_MU: 当前NFC标签不支持 MifareUltralight 数据格式");
        }
        return false;
    }

    /**
     * 读取非NDEF格式的数据
     *
     * @param intent
     * @return
     */
    public String readNFC_MU(Intent intent) {
        if (intent != null) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            for (String tech : techList) {
                if (TextUtils.equals(MifareUltralight.class.getName(), tech)) {
                    MifareUltralight ultralight = MifareUltralight.get(tag);
                    if (ultralight != null) {
                        try {
                            StringBuilder sb = new StringBuilder();
                            ultralight.connect();
                            for (int i = 4; i < 16; i = i + 4) {
                                byte[] bytes = ultralight.readPages(i);
                                sb.append(new String(bytes, Charset.forName("GB2312")));
                            }
                            log("readNFC_MU: " + sb.toString());
                            return sb.toString();

                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                ultralight.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            log("readNFC_MU: 当前NFC标签不支持 MifareUltralight 数据格式");
        }
        return null;
    }

    /*
   public Uri readNFC_Uri(Intent intent) {
        if (intent != null && TextUtils.equals(intent.getAction(), NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawArray != null && rawArray.length > 0) {
                NdefMessage ndefMessage = (NdefMessage) rawArray[0];
                NdefRecord[] records = ndefMessage.getRecords();
                if (records != null && records.length > 0) {
                    NdefRecord ndefRecord = records[0];
                    short tnf = ndefRecord.getTnf();
                    if (tnf == NdefRecord.TNF_WELL_KNOWN) {
                        return parseWellKnown(ndefRecord);
                    } else if (tnf == NdefRecord.TNF_ABSOLUTE_URI) {
                        return parseAbsolute(ndefRecord);
                    }
                    log("readNFC_Uri: Unknown TNF" + tnf);
                }
            }
        }
        return null;
    }



    //处理已知类型的Uri
   private Uri parseWellKnown(NdefRecord ndefRecord) {
        //判断数据是否是Uri类型的
        if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_URI))
            return null;
        //获取所有的字节数据
        byte[] payload = ndefRecord.getPayload();
        String prefix = UriPrefix.URI_PREFIX_MAP.get(payload[0]);
        byte[] prefixBytes = prefix.getBytes(Charset.forName("UTF-8"));
        byte[] fullUri = new byte[prefixBytes.length + payload.length - 1];
        System.arraycopy(prefixBytes, 0, fullUri, 0, prefixBytes.length);
        System.arraycopy(payload, 1, fullUri, prefixBytes.length, payload.length - 1);
        Uri uri = Uri.parse(new String(fullUri, Charset.forName("UTF-8")));
        log("parseWellKnown: Uri = " + uri);
        return uri;
    }*/

    //处理绝对的Uri  没有Uri识别码，也就是没有Uri前缀，存储的全部是字符串
    private Uri parseAbsolute(NdefRecord ndefRecord) {
        //获取所有的字节数据
        byte[] payload = ndefRecord.getPayload();
        Uri uri = Uri.parse(new String(payload, Charset.forName("UTF-8")));
        log("parseAbsolute: Uri = " + uri);
        return uri;
    }

    //向TAG中写入非NDEF格式数据
    private boolean writeMUTag(Tag tag, String text) {
        if (tag != null && text != null) {
            MifareUltralight ultralight = MifareUltralight.get(tag);
            if (ultralight != null) {
                byte[] textBytes = text.getBytes(Charset.forName("GB2312"));
                int length = textBytes.length;
                int page;
                if (length % 4 == 0) {
                    page = length / 4;
                } else {
                    log("writeMUTag: 数据字节长度必须为4的倍数");
                    return false;
                }
                if (page > 0 && page <= 12) {
                    try {
                        ultralight.connect();
                        for (int i = 0; i < page; i++) {
                            byte[] bytes = new byte[4];
                            System.arraycopy(textBytes, 4 * i, bytes, 0, 4);
                            ultralight.writePage(4 + i, bytes);
                            log("writeMUTag: page = " + (4 + i) + ", 数据写入成功--> ");
                        }
                        return true;

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            ultralight.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        log("writeMUTag: 数据写入失败");
        return false;
    }


    //向TAG中写入NDEF格式数据
    private boolean writeTag(Tag tag, NdefMessage message) {
        if (tag != null && message != null) {
            Ndef ndef = Ndef.get(tag);
            return ndef == null ? writeNoNdefMessage(tag, message) : writeNdefMessage(ndef, message);
        }
        return false;
    }

    //nfc标签未格式化,或没有分区,先进行格式化,并将数据写入
    private boolean writeNoNdefMessage(Tag tag, NdefMessage message) {
        //Ndef格式类
        NdefFormatable format = NdefFormatable.get(tag);
        if (format != null) {
            try {
                format.connect();
                format.format(message);
                log("writeNoNdefMessage: 数据写入成功--> " + message);
                return true;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (FormatException e) {
                e.printStackTrace();
            } finally {
                try {
                    format.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    //nfc标签支持NDEF数据,向nfc标签中写入NDEF数据
    private boolean writeNdefMessage(Ndef ndef, NdefMessage message) {
        //判断是否可写
        if (ndef.isWritable()) {
            int size = message.toByteArray().length;
            //判断容量是否够用
            if (ndef.getMaxSize() > size) {
                try {
                    ndef.connect();
                    ndef.writeNdefMessage(message);
                    log("writeNdefMessage: 数据写入成功--> ");
                    return true;

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (FormatException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        ndef.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }


    //生成NDEF格式的数据
    private NdefRecord createTextRecord(String languageCode, String text) {
        if (text == null)
            throw new NullPointerException("text is null");

        byte[] textBytes = text.getBytes(Charset.forName("UTF-8"));

        byte[] languageCodeBytes = null;
        if (languageCode != null && !languageCode.isEmpty()) {
            languageCodeBytes = languageCode.getBytes(Charset.forName("US-ASCII"));
        } else {
            languageCodeBytes = Locale.getDefault().getLanguage().getBytes(Charset.forName("US-ASCII"));
        }
        // We only have 6 bits to indicate ISO/IANA language code.
        if (languageCodeBytes.length >= 64) {
            throw new IllegalArgumentException("language code is too long, must be <64 bytes.");
        }
        ByteBuffer buffer = ByteBuffer.allocate(1 + languageCodeBytes.length + textBytes.length);

        byte status = (byte) (languageCodeBytes.length & 0xFF);
        buffer.put(status);
        buffer.put(languageCodeBytes);
        buffer.put(textBytes);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, null, buffer.array());
    }

    private void log(String content) {
        if (isDebug) {
            Log.e(TAG, content);
        }
    }

    //字节数组转换成十六进制字符串
    private String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (byte aBArray : bArray) {
            sTemp = Integer.toHexString(0xFF & aBArray);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    //解析NdefRecord成String字符串
    private String parseTextRecord(NdefRecord ndefRecord) {
        //判断数据是否为NDEF格式
        //判断可变的长度的类型
        if (ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN || !Arrays.equals(ndefRecord.getType(), RTD_TEXT)) {
            log("parseTextRecord: NFC卡数据类型不正确");
            return null;
        }
        try {
            //获得字节数组，然后进行分析
            byte[] payload = ndefRecord.getPayload();
            //下面开始NDEF文本数据第一个字节，状态字节
            //判断文本是基于UTF-8还是UTF-16的，取第一个字节"位与"上16进制的80，16进制的80也就是最高位是1，
            //其他位都是0，所以进行"位与"运算后就会保留最高位
            String textEncoding = ((payload[0] & 0x80) == 0) ? "UTF-8" : "UTF-16";
            //3f最高两位是0，第六位是1，所以进行"位与"运算后获得第六位
            int languageCodeLength = payload[0] & 0x3f;
            //下面开始NDEF文本数据第二个字节，语言编码
            //获得语言编码
//            String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            //下面开始NDEF文本数据后面的字节，解析出文本
            String ret = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
            log("parseTextRecord: " + ret);
            return ret;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log("parseTextRecord: 数据解析失败");
        return null;
    }

    /**
     * 手机震动提醒
     *
     * @param context
     */
    public void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            long[] pattern = {0, 100, 50, 100};
            vibrator.vibrate(pattern, -1);
        }
    }


    private NfcAdapter mNfcAdapter;
    private IntentFilter[] mIntentFilter;
    private PendingIntent mPendingIntent;
    private String[][] mTechList;

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
        mPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, context.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mIntentFilter = new IntentFilter[]{
                new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
                , new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
                , new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        };
        mTechList = new String[][]{
                new String[]{android.nfc.tech.IsoDep.class.getName()}
                , new String[]{android.nfc.tech.NfcA.class.getName()}
                , new String[]{android.nfc.tech.NfcB.class.getName()}
                , new String[]{android.nfc.tech.NfcF.class.getName()}
                , new String[]{android.nfc.tech.NfcV.class.getName()}
                , new String[]{android.nfc.tech.Ndef.class.getName()}
                , new String[]{android.nfc.tech.NdefFormatable.class.getName()}
                , new String[]{android.nfc.tech.MifareClassic.class.getName()}
                , new String[]{android.nfc.tech.MifareUltralight.class.getName()}
        };
    }

}
