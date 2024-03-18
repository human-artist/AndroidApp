package com.koustubh.armak;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.webkit.JavascriptInterface;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.Context;

public class JavaScriptInterfaceSetup {
    private Context mContext;

    public JavaScriptInterfaceSetup(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void nativeMethod() {
        // Native method implementation
    }

    @JavascriptInterface
    public String nativeVariable() {
        return "value";
    }

    @JavascriptInterface
    public String getBuildVersion() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("versionCode", 5);
//            jsonObject.put("versionName", BuildConfig.VERSION_NAME);
//            jsonObject.put("versionCode", BuildConfig.VERSION_CODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @JavascriptInterface
    public String getWebviewVersion() {
        // Get Android System WebView package version
        PackageManager packageManager = mContext.getPackageManager();
        String webViewPackageName = "com.google.android.webview";
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(webViewPackageName, 0);
            String webViewVersion = packageInfo.versionName;
            Log.d("WebViewVersion", "Android System WebView version: " + webViewVersion);
            return webViewVersion; // Return the version to JavaScript
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("WebViewVersion", "Android System WebView package not found");
            return null; // Return null if WebView package not found
        }
    }
}
