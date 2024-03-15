package com.koustubh.armak;
import com.koustubh.armak.BuildConfig;

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
import android.webkit.WebChromeClient;

import android.os.Build;
import android.Manifest;
import android.content.pm.PackageManager;
import android.webkit.JavascriptInterface;
import org.json.JSONException;
import org.json.JSONObject;



// https://stackoverflow.com/questions/68878785/failed-to-apply-plugin-com-android-internal-application-android-gradle-plug
// This worked for me like a charm -> https://github.com/microsoft/appcenter/issues/2067
public class MainActivity extends AppCompatActivity {
    private static final String KEY_URL ="BOT_URL";
    private static final String KEY_LOCAL_STORE = "LOCAL_STORAGE";
    private static final String KEY_SESSION_STORE = "SESSION_STORAGE";
    private static final String MY_PREFS_NAME = "BOT_FILE";
    private static final String DEFAULT_URL = "https://nobilisperfectum.github.io/";
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    private WebView webview;
    private SharedPreferences sharedpref;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCenter.start(getApplication(), "0ab76d51-463c-4f66-9121-8a7444bea564",
                Analytics.class, Crashes.class);
        WebSettings websettings = this.webview.getSettings();

        this.webview = findViewById(R.id.webView);
        setMyStuff();
        setupInterfaceForJS();

       

        this.sharedpref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String url = this.sharedpref.getString(KEY_URL,DEFAULT_URL);
        String jsCommand = "window.localStorage=JSON.parse("+this.sharedpref.getString(KEY_LOCAL_STORE,"{}")+")";
        this.webview.evaluateJavascript(jsCommand, val-> this.print("local storage set ..."+val));
        String jsCommand2 = "window.sessionStorage=JSON.parse("+this.sharedpref.getString(KEY_SESSION_STORE,"{}")+")";
        this.webview.evaluateJavascript(jsCommand2, val-> this.print("Session storage set ..."+val));
        // Commented the following so routing can be handled by js app
        this.webview.setWebChromeClient(new WebChromeClient());
        this.webview.loadUrl(url);
        //this.webview.loadUrl(DEFAULT_URL);

        // Request camera permission if not granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }

        // this.webview.setWebViewClient(new WebViewClient() {
        //     @Override
        //     public boolean shouldOverrideUrlLoading(WebView view, String url)
        //     {
        //         view.loadUrl(url);
        //         return true;
        //     }

        //     @Override
        //     public void onReceivedError(final WebView view, int errorCode, String description,
        //                                 final String failingUrl) {
        //         // Log.d("develop"," hat!!" + description +" ghatak!"+failingUrl);
        //         super.onReceivedError(view, errorCode, description, failingUrl);
        //     }

        //     @Override
        //     public void onPageFinished(WebView view, String url) {
        //         super.onPageFinished(view, url);
        //         // Log.d("develop",url);
        //         //Save the last visited URL to shared preference
        //         //saveUrl(url);
        //     }

        //     @Override
        //     public void onLoadResource(WebView view, String url) {
        //         // Log.d("develop"," onLoadResource"+url);
        //         super.onLoadResource(view, url);
        //     }
        // });
    }
    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, reload the WebView
                this.webview.reload();
            } else {
                // Permission denied, handle accordingly (e.g., show a message)
            }
        }
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
    private void setupInterfaceForJS(){
        // Accessing native methods and variables within a WebView in Android
        this.webview.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void nativeMethod() {
                // Native method implementation
            }

            @JavascriptInterface
            public String nativeVariable() {
                // Native variable implementation
                return "value";
            }

            @JavascriptInterface
            public String getBuildVersion() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("versionName", BuildConfig.VERSION_NAME);
                    jsonObject.put("versionCode", BuildConfig.VERSION_CODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonObject.toString();
            }
        }, "NativeInterface");
    }
    private void setMyStuff(){
        WebSettings websettings = this.webview.getSettings();
        this.webview.setBackgroundColor(0x00000000);
        this.webview.setHorizontalScrollBarEnabled(false);
        websettings.setJavaScriptEnabled(true);
        

        websettings.setLoadsImagesAutomatically(true);
        websettings.setJavaScriptCanOpenWindowsAutomatically(true);
        websettings.setUseWideViewPort(true);
        websettings.setDomStorageEnabled(true);
        // websettings.setAppCacheEnabled(true);
        websettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        //websettings.setAppCacheMaxSize(1024*1024*8);
        
        websettings.setDomStorageEnabled(true);  // Open DOM storage function
        websettings.setMediaPlaybackRequiresUserGesture(false); // Allows autoplay of video elements

        // websettings.setAppCachePath(appCachePath);
        websettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        websettings.setAllowFileAccess(true);    // Readable file cache
        // websettings.setAppCacheEnabled(true);
    }
}
