package com.koustubh.armak;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.PermissionRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.facebook.stetho.Stetho;



public class MainActivity extends AppCompatActivity {
    private static final String KEY_URL = "https://inepay.github.io/";//"https://nobilisperfectum.github.io/";
    private static final String KEY_LOCAL_STORE = "LOCAL_STORAGE";
    private static final String KEY_SESSION_STORE = "SESSION_STORAGE";
    private static final String MY_PREFS_NAME = "BOT_FILE";
    private static final String DEFAULT_URL = "https://inepay.github.io/";//"https://nobilisperfectum.github.io/";
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    private WebView webView;
    private SharedPreferences sharedPreferences;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);

        AppCenter.start(getApplication(), "0ab76d51-463c-4f66-9121-8a7444bea564",
                Analytics.class, Crashes.class);

        webView = findViewById(R.id.webView);
        setWebViewSettings();
        setupInterfaceForJS();
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("WebView", consoleMessage.message());
                return true;
            }
        });

        WebView.setWebContentsDebuggingEnabled(true);
        WebSettings webSettings = webView.getSettings();


        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String url = sharedPreferences.getString(KEY_URL, DEFAULT_URL);
        String jsCommand = "window.localStorage=JSON.parse(" + sharedPreferences.getString(KEY_LOCAL_STORE, "{}") + ")";
        webView.evaluateJavascript(jsCommand, val -> print("local storage set ..." + val));
        String jsCommand2 = "window.sessionStorage=JSON.parse(" + sharedPreferences.getString(KEY_SESSION_STORE, "{}") + ")";
        webView.evaluateJavascript(jsCommand2, val -> print("Session storage set ..." + val));
        webView.loadUrl(url);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }
//    @Override
//    public void onPermissionRequest(final PermissionRequest request) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            request.grant(request.getResources());
//        }
//    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                webView.reload();
            } else {
                // Handle permission denied
            }
        }
    }

    @Override
    protected void onStop() {
        storeValues();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        storeValues();
        super.onDestroy();
    }

    private void storeValues() {
        String url = webView.getUrl();
        sharedPreferences.edit().putString(KEY_URL, url).apply();
        webView.evaluateJavascript("JSON.stringify(window.localStorage)", val -> {
            print("Update local storage in android as ..." + val);
            sharedPreferences.edit().putString(KEY_LOCAL_STORE, val).apply();
        });
        webView.evaluateJavascript("JSON.stringify(window.sessionStorage)", val -> {
            print("Update session storage in android as ..." + val);
            sharedPreferences.edit().putString(KEY_SESSION_STORE, val).apply();
        });
    }

    private void print(String s) {
        // Log.d("develop", s);
    }

    private void setupInterfaceForJS() {
        webView.addJavascriptInterface(new JavaScriptInterfaceSetup(this), "NativeInterface");
    }

    private void setWebViewSettings() {
        WebSettings webSettings = webView.getSettings();
        webView.setBackgroundColor(0x00000000);
        webView.setHorizontalScrollBarEnabled(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAllowFileAccess(true);
    }
}
