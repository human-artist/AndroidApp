package com.koustubh.armak;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // Add your custom behavior here
        // Return true if the WebView should handle the URL, false otherwise
        return super.shouldOverrideUrlLoading(view, url);
    }
    //@Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        // Log console messages to the system log (Logcat)
        android.util.Log.d("WebViewConsole", consoleMessage.message());

        // You can also display console messages in a Toast or a custom view
        // Example:
        // showToast(consoleMessage.message());

        // Return true to indicate that the message has been handled
        return true;
    }
}
