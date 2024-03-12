package com.koustubh.armak;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import android.content.SharedPreferences;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

// https://stackoverflow.com/questions/68878785/failed-to-apply-plugin-com-android-internal-application-android-gradle-plug
// This worked for me like a charm -> https://github.com/microsoft/appcenter/issues/2067
public class MainActivity extends AppCompatActivity {
    private static final String KEY_URL ="BOT_URL";
    private static final String KEY_LOCAL_STORE = "LOCAL_STORAGE";
    private static final String KEY_SESSION_STORE = "SESSION_STORAGE";
    private static final String MY_PREFS_NAME = "BOT_FILE";
    private static final String DEFAULT_URL = "https://nobilisperfectum.github.io/";
    private WebView webview;
    private SharedPreferences sharedpref;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCenter.start(getApplication(), "0ab76d51-463c-4f66-9121-8a7444bea564",
                Analytics.class, Crashes.class);
        this.webview = findViewById(R.id.webView);
        this.webview.setBackgroundColor(0x00000000);
        this.webview.setHorizontalScrollBarEnabled(false);
        this.webview.getSettings().setJavaScriptEnabled(true);
        this.webview.getSettings().setLoadsImagesAutomatically(true);
        this.webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.webview.getSettings().setUseWideViewPort(true);
        this.webview.getSettings().setDomStorageEnabled(true);
        // this.webview.getSettings().setAppCacheEnabled(true);
        this.webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        //this.webview.getSettings().setAppCacheMaxSize(1024*1024*8);
        WebSettings websettings = this.webview.getSettings();
        websettings.setDomStorageEnabled(true);  // Open DOM storage function
        // websettings.setAppCachePath(appCachePath);
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        websettings.setAllowFileAccess(true);    // Readable file cache
        // websettings.setAppCacheEnabled(true);

        this.sharedpref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String url = this.sharedpref.getString(KEY_URL,DEFAULT_URL);
        String jsCommand = "window.localStorage=JSON.parse("+this.sharedpref.getString(KEY_LOCAL_STORE,"{}")+")";
        this.webview.evaluateJavascript(jsCommand, val-> this.print("local storage set ..."+val));
        String jsCommand2 = "window.sessionStorage=JSON.parse("+this.sharedpref.getString(KEY_SESSION_STORE,"{}")+")";
        this.webview.evaluateJavascript(jsCommand2, val-> this.print("Session storage set ..."+val));
        // Commented the following so routing can be handled by js app
        this.webview.loadUrl(url);
        //this.webview.loadUrl(DEFAULT_URL);
        this.webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(final WebView view, int errorCode, String description,
                                        final String failingUrl) {
                // Log.d("develop"," hat!!" + description +" ghatak!"+failingUrl);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Log.d("develop",url);
                //Save the last visited URL to shared preference
                //saveUrl(url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                // Log.d("develop"," onLoadResource"+url);
                super.onLoadResource(view, url);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Log.d("develop","in onStart");
    }

    @Override
    protected void onStop() {
        this.storeValues();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        this.storeValues();
        super.onDestroy();
    }

    private void storeValues(){
        String url = this.webview.getUrl();
        this.sharedpref.edit().putString(KEY_URL,url).apply();
        this.webview.evaluateJavascript("JSON.stringify(window.localStorage)",val->{
            this.print("Update local storage in android as ..."+val);
            sharedpref.edit().putString(KEY_LOCAL_STORE,val).apply();
        });
        this.webview.evaluateJavascript("JSON.stringify(window.sessionStorage)",val->{
            this.print("Update session storage in android as ..."+val);
            sharedpref.edit().putString(KEY_SESSION_STORE,val).apply();
        });
    }
    private void print(String ignoredS){
        // Log.d("develop",s);
    }
}