package wo.yi.wei.wanglexiangnian;

import android.annotation.SuppressLint;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import hui.shou.tao.base.XConstanstKt;
import hui.shou.tao.base.xApp;

public class WebViewManager {
    private WebView webView;
    private WebSettings webSettings;

    public void clearCookie(){
        CookieSyncManager.createInstance(xApp.instance);
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null);
            cookieManager.removeAllCookie();
            cookieManager.flush();
        } else {
            cookieManager.removeSessionCookies(null);
            cookieManager.removeAllCookie();
            CookieSyncManager.getInstance().sync();
        }
        XConstanstKt.setUserName("");
        XConstanstKt.setUserPwd("");
    }

    public WebViewManager(WebView webView){
        this.webView = webView;
        webSettings = webView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
    }

    /**
     * 开启自适应功能
     */
    public WebViewManager enableAdaptive(){
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        return this;
    }

    /**
     * 禁用自适应功能
     */
    public WebViewManager disableAdaptive(){
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        return this;
    }

    /**
     * 开启缩放功能
     */
    public WebViewManager enableZoom(){
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        return this;
    }

    /**
     * 禁用缩放功能
     */
    public WebViewManager disableZoom(){
        webSettings.setSupportZoom(false);
        webSettings.setUseWideViewPort(false);
        webSettings.setBuiltInZoomControls(false);
        return this;
    }

    /**
     * 开启JavaScript
     */
    @SuppressLint("SetJavaScriptEnabled")
    public WebViewManager enableJavaScript(){
        webSettings.setJavaScriptEnabled(true);
        return this;
    }

    /**
     * 禁用JavaScript
     */
    public WebViewManager disableJavaScript(){
        webSettings.setJavaScriptEnabled(false);
        return this;
    }

    /**
     * 开启JavaScript自动弹窗
     */
    public WebViewManager enableJavaScriptOpenWindowsAutomatically(){
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        return this;
    }

    /**
     * 禁用JavaScript自动弹窗
     */
    public WebViewManager disableJavaScriptOpenWindowsAutomatically(){
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        return this;
    }

    /**
     * 返回
     * @return true：已经返回，false：到头了没法返回了
     */
    public boolean goBack(){
        if(webView.canGoBack()){
            webView.goBack();
            return true;
        }else{
            return false;
        }
    }
}
