/*
 * Copyright (C) 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.hypeapp.fixmath.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class GameHelper {

    static final String TAG = "GameHelper";

    /** Listener for sign-in success or failure events. */
    public interface GameHelperListener {
        /**
         * Called when sign-in fails. As a result, a "Sign-In" button can be
         * shown to the user; when that button is clicked, call
         *
         * @link{GamesHelper#beginUserInitiatedSignIn . Note that not all calls
         *                                            to this method mean an
         *                                            error; it may be a result
         *                                            of the fact that automatic
         *                                            sign-in could not proceed
         *                                            because user interaction
         *                                            was required (consent
         *                                            dialogs). So
         *                                            implementations of this
         *                                            method should NOT display
         *                                            an error message unless a
         *                                            call to @link{GamesHelper#
         *                                            hasSignInError} indicates
         *                                            that an error indeed
         *                                            occurred.
         */
        void onSignInFailed();

        /** Called when sign-in succeeds. */
        void onSignInSucceeded();
    }

    // configuration done?
    private boolean mSetupDone = false;

    // are we currently connecting?
    private boolean mConnecting = false;

    // Are we expecting the result of a resolution flow?
    boolean mExpectingResolution = false;

    // was the sign-in flow cancelled when we tried it?
    // if true, we know not to try again automatically.
    boolean mSignInCancelled = false;

    /**
     * The Activity we are bound to. We need to keep a reference to the Activity
     * because some games methods require an Activity (a Context won't do). We
     * are careful not to leak these references: we release them on onStop().
     */
    AppCompatActivity mActivity = null;

    // app context
    Context mAppContext = null;

    // Request code we use when invoking other Activities to complete the
    // sign-in flow.
    final static int RC_RESOLVE = 9001;

    // Request code when invoking Activities whose result we don't care about.
    final static int RC_UNUSED = 9002;

    // Client request flags
    public final static int CLIENT_NONE = 0x00;
    public final static int CLIENT_GAMES = 0x01;
    public final static int CLIENT_PLUS = 0x02;
    public final static int CLIENT_SNAPSHOT = 0x08;
    public final static int CLIENT_ALL = CLIENT_GAMES | CLIENT_PLUS
            | CLIENT_SNAPSHOT;

    // What clients were requested? (bit flags)
    int mRequestedClients = CLIENT_NONE;

    // Whether to automatically try to sign in on onStart(). We only set this
    // to true when the sign-in process fails or the user explicitly signs out.
    // We set it back to false when the user initiates the sign in process.
    boolean mConnectOnStart = true;

    /*
     * Whether user has specifically requested that the sign-in process begin.
     * If mUserInitiatedSignIn is false, we're in the automatic sign-in attempt
     * that we try once the Activity is started -- if true, then the user has
     * already clicked a "Sign-In" button or something similar
     */
    boolean mUserInitiatedSignIn = false;

    // The connection result we got from our last attempt to sign-in.

    // The error that happened during sign-in.
    SignInFailureReason mSignInFailureReason = null;

    // Should we show error dialog boxes?
    boolean mShowErrorDialogs = true;

    // Print debug logs?
    boolean mDebugLog = false;

    Handler mHandler;

    // Listener
    GameHelperListener mListener = null;

    // Should we start the flow to sign the user in automatically on startup? If
    // so, up to
    // how many times in the life of the application?
    static final int DEFAULT_MAX_SIGN_IN_ATTEMPTS = 3;
    int mMaxAutoSignInAttempts = DEFAULT_MAX_SIGN_IN_ATTEMPTS;

    /**
     * Construct a GameHelper object, initially tied to the given Activity.
     * After constructing this object, call @link{setup} from the onCreate()
     * method of your Activity.
     *
     * @param clientsToUse
     *            the API clients to use (a combination of the CLIENT_* flags,
     *            or CLIENT_ALL to mean all clients).
     */
    public GameHelper(AppCompatActivity activity, int clientsToUse) {
        mActivity = activity;
        mAppContext = activity.getApplicationContext();
        mRequestedClients = clientsToUse;
        mHandler = new Handler();
    }

    /**
     * Sets the maximum number of automatic sign-in attempts to be made on
     * application startup. This maximum is over the lifetime of the application
     * (it is stored in a SharedPreferences file). So, for example, if you
     * specify 2, then it means that the user will be prompted to sign in on app
     * startup the first time and, if they cancel, a second time the next time
     * the app starts, and, if they cancel that one, never again. Set to 0 if
     * you do not want the user to be prompted to sign in on application
     * startup.
     */
    public void setMaxAutoSignInAttempts(int max) {
        mMaxAutoSignInAttempts = max;
    }

    void assertConfigured(String operation) {
//        if (!mSetupDone) {
//            String error = "GameHelper error: Operation attempted without setup: "
//                    + operation
//                    + ". The setup() method must be called before attempting any other operation.";
//            logError(error);
//            throw new IllegalStateException(error);
//        }
    }

    // Set whether to show error dialogs or not.
    public void setShowErrorDialogs(boolean show) {
        mShowErrorDialogs = show;
    }

    /** Call this method from your Activity's onStart(). */
    public void onStart(AppCompatActivity act) {
        mActivity = act;
        mAppContext = act.getApplicationContext();

        debugLog("onStart");
        assertConfigured("onStart");
    }

    /** Call this method from your Activity's onStop(). */
    public void onStop() {
        debugLog("onStop");
        assertConfigured("onStop");
        mConnecting = false;
        mExpectingResolution = false;

        // let go of the Activity reference
        mActivity = null;
    }

    /** Enables debug logging */
    public void enableDebugLog(boolean enabled) {
        mDebugLog = enabled;
        if (enabled) {
            debugLog("Debug log enabled.");
        }
    }

    @Deprecated
    public void enableDebugLog(boolean enabled, String tag) {
        Log.w(TAG, "GameHelper.enableDebugLog(boolean,String) is deprecated. "
                + "Use GameHelper.enableDebugLog(boolean)");
        enableDebugLog(enabled);
    }

    /**
     * Handle activity result. Call this method from your Activity's
     * onActivityResult callback. If the activity result pertains to the sign-in
     * process, processes it appropriately.
     */
    public void onActivityResult(int requestCode, int responseCode,
                                 Intent intent) {
        debugLog("onActivityResult: req="
                + (requestCode == RC_RESOLVE ? "RC_RESOLVE" : String
                .valueOf(requestCode)) + ", resp="
                + GameHelperUtils.activityResponseCodeToString(responseCode));
        if (requestCode != RC_RESOLVE) {
            debugLog("onActivityResult: request code not meant for us. Ignoring.");
            return;
        }

        // no longer expecting a resolution
        mExpectingResolution = false;

        if (!mConnecting) {
            debugLog("onActivityResult: ignoring because we are not connecting.");
            return;
        }
    }

    void notifyListener(boolean success) {
        debugLog("Notifying LISTENER of sign-in "
                + (success ? "SUCCESS"
                : mSignInFailureReason != null ? "FAILURE (error)"
                : "FAILURE (no error)"));
        if (mListener != null) {
            if (success) {
                mListener.onSignInSucceeded();
            } else {
                mListener.onSignInFailed();
            }
        }
    }

    /**
     * Starts a user-initiated sign-in flow. This should be called when the user
     * clicks on a "Sign In" button. As a result, authentication/consent dialogs
     * may show up. At the end of the process, the GameHelperListener's
     * onSignInSucceeded() or onSignInFailed() methods will be called.
     */
    public void beginUserInitiatedSignIn() {
        debugLog("beginUserInitiatedSignIn: resetting attempt count.");
        resetSignInCancellations();
        mSignInCancelled = false;
        mConnectOnStart = true;

        debugLog("Starting USER-INITIATED sign-in flow.");

        // indicate that user is actively trying to sign in (so we know to
        // resolve
        // connection problems by showing dialogs)
        mUserInitiatedSignIn = true;
    }

    private final String GAMEHELPER_SHARED_PREFS = "GAMEHELPER_SHARED_PREFS";
    private final String KEY_SIGN_IN_CANCELLATIONS = "KEY_SIGN_IN_CANCELLATIONS";

    // Return the number of times the user has cancelled the sign-in flow in the
    // life of the app
    int getSignInCancellations() {
        SharedPreferences sp = mAppContext.getSharedPreferences(
                GAMEHELPER_SHARED_PREFS, Context.MODE_PRIVATE);
        return sp.getInt(KEY_SIGN_IN_CANCELLATIONS, 0);
    }

    // Increments the counter that indicates how many times the user has
    // cancelled the sign in
    // flow in the life of the application
    int incrementSignInCancellations() {
        int cancellations = getSignInCancellations();
        SharedPreferences.Editor editor = mAppContext.getSharedPreferences(
                GAMEHELPER_SHARED_PREFS, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_SIGN_IN_CANCELLATIONS, cancellations + 1);
        editor.commit();
        return cancellations + 1;
    }

    // Reset the counter of how many times the user has cancelled the sign-in
    // flow.
    void resetSignInCancellations() {
        SharedPreferences.Editor editor = mAppContext.getSharedPreferences(
                GAMEHELPER_SHARED_PREFS, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_SIGN_IN_CANCELLATIONS, 0);
        editor.commit();
    }

    static Dialog makeSimpleDialog(AppCompatActivity activity, String text) {
        return (new AlertDialog.Builder(activity)).setMessage(text)
                .setNeutralButton(android.R.string.ok, null).create();
    }

    static Dialog
    makeSimpleDialog(AppCompatActivity activity, String title, String text) {
        return (new AlertDialog.Builder(activity)).setMessage(text)
                .setTitle(title).setNeutralButton(android.R.string.ok, null)
                .create();
    }

    public Dialog makeSimpleDialog(String text) {
        if (mActivity == null) {
            logError("*** makeSimpleDialog failed: no current Activity!");
            return null;
        }
        return makeSimpleDialog(mActivity, text);
    }

    public Dialog makeSimpleDialog(String title, String text) {
        if (mActivity == null) {
            logError("*** makeSimpleDialog failed: no current Activity!");
            return null;
        }
        return makeSimpleDialog(mActivity, title, text);
    }

    void debugLog(String message) {
        if (mDebugLog) {
            Log.d(TAG, "GameHelper: " + message);
        }
    }

    void logWarn(String message) {
        Log.w(TAG, "!!! GameHelper WARNING: " + message);
    }

    void logError(String message) {
        Log.e(TAG, "*** GameHelper ERROR: " + message);
    }

    // Represents the reason for a sign-in failure
    public static class SignInFailureReason {
        public static final int NO_ACTIVITY_RESULT_CODE = -100;
        int mServiceErrorCode = 0;
        int mActivityResultCode = NO_ACTIVITY_RESULT_CODE;

        public int getServiceErrorCode() {
            return mServiceErrorCode;
        }

        public int getActivityResultCode() {
            return mActivityResultCode;
        }

        public SignInFailureReason(int serviceErrorCode, int activityResultCode) {
            mServiceErrorCode = serviceErrorCode;
            mActivityResultCode = activityResultCode;
        }

        public SignInFailureReason(int serviceErrorCode) {
            this(serviceErrorCode, NO_ACTIVITY_RESULT_CODE);
        }

        @Override
        public String toString() {
            return "SignInFailureReason(serviceErrorCode:"
                    + GameHelperUtils.errorCodeToString(mServiceErrorCode)
                    + ((mActivityResultCode == NO_ACTIVITY_RESULT_CODE) ? ")"
                    : (",activityResultCode:"
                    + GameHelperUtils
                    .activityResponseCodeToString(mActivityResultCode) + ")"));
        }
    }

    // Not recommended for general use. This method forces the
    // "connect on start" flag
    // to a given state. This may be useful when using GameHelper in a
    // non-standard
    // sign-in flow.
    public void setConnectOnStart(boolean connectOnStart) {
        debugLog("Forcing mConnectOnStart=" + connectOnStart);
        mConnectOnStart = connectOnStart;
    }
}
