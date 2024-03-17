package com.koustubh.armak;

import android.webkit.JavascriptInterface;
import org.json.JSONException;
import org.json.JSONObject;

public class JavaScriptInterfaceSetup {
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
}
