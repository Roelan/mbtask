package com.example.filechecker.logger;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Logger utility class which by default directs messages to logcat
 *
 * It's horribly hacky, but a static instance needs to be created externally at app launch:
 *
 * L.instance = appComponent.getLogger();
 */
public class L {

    private static final int LOG_MESSAGE_LENGTH = 700;
    /**
     * A reusable builder, cleared and used to join each log message
     */
    private final StringBuilder BUILDER = new StringBuilder();
    /**
     * When true, log messages will not attempt to use instrumented classes
     */
    private static final boolean IS_IN_TEST;
    /**
     * A timestamp converter so we can log long timestamps without wrapping and converting them
     */
    @SuppressLint("ConstantLocale")
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS dd.MM.yyyy", Locale.getDefault());
    /**
     * A remote server, such as firebase
     */
    private static Remote REMOTE;
    /**
     * Connection to the diagnostics module so we can log when required
     */
    private final DiagModule diag;

    private static L INSTANCE;

    static {
        boolean isInTestTemp = false;
        try {
            Class.forName("androidx.test.runner.AndroidJUnitRunner", true, L.class.getClassLoader()); //NOTE: This is designed to fail in production
            isInTestTemp = true;
        } catch (ClassNotFoundException ignored1) {
            try {
                Class.forName("com.example.filechecker.qa.CustomTestRunner", true, L.class.getClassLoader());
                isInTestTemp = true;
            } catch (ClassNotFoundException ignored2) {
                //do nothing
            }
        }
        IS_IN_TEST = isInTestTemp;
    }

    /**
     * Singleton expected - Load this from a scoped Dependency Injection Provider only
     */
    public L(@NonNull DiagModule diagModule) {
        this.diag = diagModule;
        L.INSTANCE = this;
    }

    ////////////[ CORE / SPECIAL ]//////////////////////////////////////////////////////////////////

    /**
     * Send the exception to a remote server such as Firebase
     */
    private static void logRemotely(@NonNull Throwable e) {
        if (!L.isInTest()) {
            if (REMOTE != null) {
                //we clip a little so we don't print every except as caused in the L class
                RuntimeException runtimeException = new RuntimeException(e);
                StackTraceElement[] els = runtimeException.getStackTrace();
                //offset of 2 is one for this method plus 1 for the method in L which calls it
                runtimeException.setStackTrace(Arrays.copyOfRange(els, 2, els.length));
                REMOTE.logException(runtimeException);
            } else {
                getInstance().applyLog(Log.ERROR, L.class, "Attempted to log remotely without configuring a remote. Call L.setRemote() first.", null);
            }
        }
    }

    private void applyLog(int logType, @NonNull Object tag, @Nullable String msg, @Nullable Throwable t) {
        applyLog(logType, getLabelForTag(tag), msg, t);
    }

    /**
     * This is the master logging method, all other log methods should always direct here
     * This method takes care of formatting and tag flags
     *
     * for some reason we get ArrayIndexOutOfBoundsException that is why try-catch block added
     */
    private void applyLog(int logType, @NonNull String category, @Nullable String msg, @Nullable Throwable t) {
        try {
            if (!TextUtils.isEmpty(msg)) {
                BUILDER.append(msg);
                BUILDER.append(" => ");
                BUILDER.append(getLocation());
            }
            if (t != null) {
                if (!TextUtils.isEmpty(msg)) {
                    BUILDER.append("\n");
                }
                if (isInTest()) {
                    t.printStackTrace();  //print to test stream
                } else {
                    BUILDER.append(Log.getStackTraceString(t));  //create logcat properly
                }
            }
            if (isInTest()) {
                System.out.println(category + ": " + BUILDER.toString());
            } else {
                Log.println(logType, category, BUILDER.toString());
            }
            if (diag.isRecordingActive()) {
                diag.addDiagnosticsEntry(getLogSeverityPrefix(logType) + category, BUILDER.toString());
            }
        } catch (IndexOutOfBoundsException exc) {
            Log.e("L", "Failed to apply log", exc);
        }

        BUILDER.setLength(0); //clear the message we've just printed
    }

    private void applyLog(int logType, @NonNull Object tag, @Nullable String msg, @Nullable Throwable t, boolean isForceChunking) {
        if (isForceChunking && msg != null) {
            int chunkCount = (int) Math.ceil(msg.length() / (float) LOG_MESSAGE_LENGTH);
            applyLog(Log.DEBUG, "L", "The next message is broken into " + chunkCount + " individual chunks due to length " + msg.length() + ".", null);
            int min, max;
            for (int i = 0; i < chunkCount; i++) {
                min = LOG_MESSAGE_LENGTH * i;
                max = LOG_MESSAGE_LENGTH * (i + 1);
                if (max > msg.length()-1) {
                    max = msg.length()-1;
                }
                applyLog(logType, getLabelForTag(tag) + " [Chunk " + (i+1) + "]", msg.substring(min, max), null);
            }
            if (t != null) {
                applyLog(logType, tag, "Exception for above message: ", t);
            }
        } else {
            applyLog(logType, tag, msg, t);
        }
    }

    /**
     * Shorthand for {@link System#currentTimeMillis()}
     * @return current time in milliseconds
     */
    public long now(){
        return System.currentTimeMillis();
    }

    /**
     * Takes any given object and returns a string for the log tag
     */
    @NonNull
    private String getLabelForTag(@NonNull Object tag) {
        String label;
        if (tag instanceof String) {
            if (!TextUtils.isEmpty((String) tag)) {
                label = (String) tag;
            } else {
                label = "(?)";
            }
        } else if (tag instanceof Class) {
            //noinspection rawtypes
            label = ((Class) tag).getSimpleName();
        } else {
            label = tag.getClass().getSimpleName();
        }
        label = label.trim();
        if (label.isEmpty()) {
            label = "L";
        }
        return label;
    }

    /**
     * Adds clickable trace location to every logged message
     */
    private String getLocation() {
        final String logClassName = L.class.getName();
        final StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        boolean found = false;
        for (StackTraceElement trace : traces) {
            if (found) {
                if (!trace.getClassName().startsWith(logClassName)) {
                    return String.format(Locale.getDefault(), "(%s:%d)", trace.getFileName(), trace.getLineNumber());
                }
            } else if (trace.getClassName().startsWith(logClassName)) {
                found = true;
            }
        }
        return "(?): ";
    }

    public static L getInstance() {
        //We get an instance from dependency injection instance (because we need diag injected)
        // this relies on app class ensuring an instance is called as soon as possible:
        // E.g. create and call appComponent.getLogger(); on DI
        return INSTANCE;
    }

    ////////////[ DEBUG ]///////////////////////////////////////////////////////////////////////////

    public static void d(@NonNull Object tag, @NonNull String msg) {
        getInstance().applyLog(Log.DEBUG, tag, msg, null, false);
    }

    /**
     * @param isForceChunkingLargeMessage if true, will break the message into chunks over several
     *                                    log messages, to avoid default truncation behaviou
     */
    public static void d(@NonNull Object tag, @NonNull String msg, boolean isForceChunkingLargeMessage) {
        getInstance().applyLog(Log.DEBUG, tag, msg, null, isForceChunkingLargeMessage);
    }

    /**
     * @param timestampArg this timestamp will be converted to a formatted date, then appended to the
     *                   message.
     */
    public static void d(@NonNull Object tag, @NonNull String msg, long timestampArg) {
        getInstance().applyLog(Log.DEBUG, tag, msg + timestamp(timestampArg), null, false);
    }

    ////////////[ ERROR ]///////////////////////////////////////////////////////////////////////////

    public static void e(@NonNull Object tag, @NonNull String methodOrAttempt) {
        e(tag, methodOrAttempt, null, false);
    }

    //NOTE: Nullable to stop "maybe's" being flagged by lint, but if we use this signature we really
    // expect an exception to be non-null
    public static void e(@NonNull Object tag, @NonNull String methodOrAttempt, @Nullable Throwable e) {
        e(tag, methodOrAttempt, e, false);
        if (e == null) {
            e(tag, "Null Error was provided, unexpectedly");
        }
    }

    /**
     * @param logRemotely when true, the app will send telemetry to crashlytics when this
     *                            event occurs in prod (but not from debug)
     */
    public static void e(@NonNull Object tag, @NonNull String methodOrAttempt, @Nullable Throwable e, boolean logRemotely) {
        getInstance().applyLog(Log.ERROR, tag, methodOrAttempt, e, false);
        if (e != null && logRemotely) {
            logRemotely(e);
        }
    }

    ////////////[ WARN ]////////////////////////////////////////////////////////////////////////////

    public static void w(@NonNull Object tag, @NonNull String msg) {
        getInstance().applyLog(Log.WARN, tag, msg, null, false);
    }

    ////////////[ INFO ]////////////////////////////////////////////////////////////////////////////

    public static void i(@NonNull Object tag, @NonNull String msg) {
        getInstance().applyLog(Log.INFO, tag, msg, null, false);
    }

    ////////////[ REMOTE ]//////////////////////////////////////////////////////////////////////////

    /**
     * Define a remote place to log exceptions, such as firebase
     */
    @SuppressWarnings("unused")
    public static void setRemote(@Nullable Remote remote) {
        REMOTE = remote;
    }

    /**
     * @return a string representation of the given timestamp
     */
    public static String timestamp(long ts) {
        String formatResult;
        synchronized (sdf) {
            formatResult = sdf.format(ts);
        }
        return formatResult;
    }

    /**
     * returns log prefix from the log severity
     * @param logType (DEBUG, ERROR, INFO)
     * @return log prefix
     */
    private String getLogSeverityPrefix(int logType) {
        switch (logType) {
            case Log.DEBUG:
                return "D/";
            case Log.ERROR:
                return "E/";
            case Log.INFO:
                return "I/";
            case Log.WARN:
                return "W/";
            case Log.ASSERT:
                return "A/";
            default:
                return "";
        }
    }

    public static boolean isInTest() {
        return IS_IN_TEST;
    }

    /**
     * Define a remote place to log exceptions, such as firebase
     */
    public interface Remote {
        void logException(@NonNull Exception runtimeException);
    }

}