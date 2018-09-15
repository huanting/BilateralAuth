package com.example.huanting.testhttp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * miscellaneous utilities methods that not belong to all specific utilities classes such as FileUtils/NumberUtils, etc.
 */
public final class Utils {
    private static final int BUFFER_SIZE = 1024 * 16;

    public interface ANDROID_VERSION_CODE {
        int OREO = 26;              // Android 8.0
        int NOUGAT_MR1 = 25;        // Android 7.1
        int NOUGAT = 24;             // Android 7.0
        int MARSHMALLOW = 23;       // Android 6.0
        int LOLLIPOP_MR1 = 22;      // Android 5.1
        int LOLLIPOP = 21;          // Android 5.0
        int KITKAT = 19;            // Android 4.4.X
        int JELLY_BEAN_MR1 = 17;    // Android 4.2
        int JELLY_BEAN = 16;    // Android 4.1, 4.1.1
        int ICE_CREAM_SANDWICH_MR1 = 15;    // Android 4.0.3, 4.0.4
        int ICE_CREAM_SANDWICH = 14;    // Android 4.0, 4.0.1, 4.0.2
        int HONEYCOMB_MR2 = 13; // Android 3.2
        int HONEYCOMB_MR1 = 12; // Android 3.1.x
        int HONEYCOMB = 11; // Android 3.0.x
        int GINGERBREAD_MR1 = 10;   // Android 2.3.4, Android 2.3.3
        int GINGERBREAD = 9;    // Android 2.3.2, Android 2.3.1, Android 2.3
        int FROYO = 8;  // Android 2.2.x
        int ECLAIR_MR1 = 7; // Android 2.1.x
        int ECLAIR_0_1 = 6; // Android 2.0.1
        int ECLAIR = 5; // Android 2.0
        int DONUT = 4;  // Android 1.6
        int CUPCAKE = 3;    // Android 1.5
        int BASE_1_1 = 2;   // Android 1.1
        int BASE = 1;   // Android 1.0
    }

    private static String[] CHARS = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };

    private Utils() {}

    // this will force import the java package which contains some class
    // eg: forceImportPackage(R.id.class) will force import package com.lenovo.anyshare.R
    public static void forceImportPackage(Class<?> clazz) {}

    public static String leftPad(String str, int len, char ch) {
        StringBuilder builder = new StringBuilder();
        int start = str == null ? 0 : str.length();
        for (int i = start; i < len; i++)
            builder.append(ch);
        if (str != null)
            builder.append(str);
        return builder.toString();
    }

    public static void inputStreamToOutputStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int r;
        while ((r = input.read(buffer)) != -1) {
            output.write(buffer, 0, r);
        }
    }

    // read everything in an input stream and return as string (trim-ed, and may apply optional utf8 conversion)
    public static String inputStreamToString(final InputStream is, final boolean sourceIsUTF8) throws IOException {
        InputStreamReader isr = sourceIsUTF8 ? new InputStreamReader(is, Charset.forName("UTF-8")) : new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null)
            sb.append(line);
        br.close();
        return sb.toString().trim();
    }

    // url encode a string with UTF-8 encoding
    public static String urlEncode(String src) {
        try {
            return URLEncoder.encode(src, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static int readBuffer(InputStream input, byte[] buffer) throws IOException {
        return readBuffer(input, buffer, 0, buffer.length);
    }

    /**
     * inputstream 读取byte[] buffer不能保证�?��完整读取，使用本方法可以保证填满buffer
     * @param input
     * @param buffer
     * @param offset
     * @param length
     * @return
     * @throws IOException
     */
    public static int readBuffer(InputStream input, byte[] buffer, int offset, int length) throws IOException {
        int sum = 0;
        int r;
        while (length > 0 && (r = input.read(buffer, offset, length)) != -1) {
            sum += r;
            offset += r;
            length -= r;
        }
        return sum;
    }

    /**
     * close cursor, catch and ignore all exceptions.
     * @param cursor the cursor object, may be null
     */
    public static void close(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Exception e) {}
        }
    }

    /**
     * close object quietly, catch and ignore all exceptions.
     * @param object the closeable object like inputstream, outputstream, reader, writer, randomaccessfile.
     */
    public static void close(Closeable object) {
        if (object != null) {
            try {
                object.close();
            } catch (Exception e) {}
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    public static void close(MediaMetadataRetriever retriever) {
        if (retriever != null) {
            try {
                retriever.release();
            } catch (Throwable e) {}
        }
    }

    public enum DEVICETYPE {
        DEVICE_PHONE("phone"), DEVICE_PAD("pad");

        private String mValue;

        DEVICETYPE(String value) {
            mValue = value;
        }

        private final static Map<String, DEVICETYPE> VALUES = new HashMap<String, DEVICETYPE>();
        static {
            for (DEVICETYPE item : DEVICETYPE.values())
                VALUES.put(item.mValue, item);
        }

        @SuppressLint("DefaultLocale")
        public static DEVICETYPE fromString(String value) {
            return VALUES.get(value.toLowerCase());
        }

        @Override
        public String toString() {
            return mValue;
        }
    }

    public static DEVICETYPE detectDeviceType(Context ctx) {
        double screenSize = 0D;
        try {
            DisplayMetrics displaymetrics = ctx.getApplicationContext().getResources().getDisplayMetrics();
            float width = displaymetrics.widthPixels;
            float height = displaymetrics.heightPixels;
            float xdpi = displaymetrics.densityDpi > displaymetrics.xdpi ? displaymetrics.densityDpi : displaymetrics.xdpi;
            float ydpi = displaymetrics.densityDpi > displaymetrics.ydpi ? displaymetrics.densityDpi : displaymetrics.ydpi;
            float inchW = width / xdpi;
            float inchH = height / ydpi;
            screenSize = Math.sqrt(Math.pow(inchW, 2D) + Math.pow(inchH, 2D));
        } catch (Exception exception) {
            return DEVICETYPE.DEVICE_PHONE;
        }
        if (screenSize >= 6.5D) // some device has 6" screen, such as K7 mini
            return DEVICETYPE.DEVICE_PAD;

        return DEVICETYPE.DEVICE_PHONE;
    }

    public static int getScreenWidth(Context ctx) {
        DisplayMetrics displaymetrics = ctx.getApplicationContext().getResources().getDisplayMetrics();
        return displaymetrics.widthPixels;
    }

    public static int getScreenHeight(Context ctx) {
        DisplayMetrics displaymetrics = ctx.getApplicationContext().getResources().getDisplayMetrics();
        return displaymetrics.heightPixels;
    }

    public static int getStatusBarHeihgt(Context ctx) {
        final int defaultHeight = 30;
        Resources res = ctx.getResources();
        String key = "status_bar_height";
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result == 0 ? defaultHeight : result;
    }

    public static int getNavigationBarHeight(Context ctx) {
        Resources res = ctx.getResources();
        String key = "navigation_bar_height";
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static float getDensity(Context ctx) {
        return ctx.getResources().getDisplayMetrics().density;
    }

    public static String getStringFromBundle(Bundle bundle, String key) {
        String strValue = bundle.getString(key);
        if (strValue != null)
            return strValue;
        else {
            int intValue = bundle.getInt(key);
            if (intValue != 0)
                return String.valueOf(intValue);
            else
                return null;
        }
    }

    public static int getVersionCode(Context context) {
        String pn = context.getPackageName();
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(pn, 0).versionCode;
        } catch (NameNotFoundException e) {
            return 0;
        }
    }

    public static String getVersionName(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(packageName, 0).versionName;
        } catch (NameNotFoundException e) {
            return "unknown";
        }
    }

    public static boolean isDevVersion(Context context) {
        boolean ret = true;

        try {
            String pn = context.getPackageName();
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(pn, 0);
            ret = (pi.versionCode == 1);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static boolean isSimReady(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    public static boolean isSupportPhone(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    public static String createUniqueId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String createShortUUID() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = createUniqueId();
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(CHARS[x % CHARS.length]);
        }
        return shortBuffer.toString();
    }

    /**
     * Returns {@code true} if called on the main thread, {@code false} otherwise.
     */
    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * Returns {@code true} if called on the main thread, {@code false} otherwise.
     */
    public static boolean isOnBackgroundThread() {
        return !isOnMainThread();
    }

}
