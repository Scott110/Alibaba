package com.alibaba.sdk.trade.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.nb.android.trade.AliTradeEvent;
import com.alibaba.nb.android.trade.AliTradeSDK;
import com.alibaba.nb.android.trade.callback.AliTradeProcessCallback;
import com.alibaba.nb.android.trade.constants.AliTradeConstants;
import com.alibaba.nb.android.trade.model.AliOpenType;
import com.alibaba.nb.android.trade.model.AliTradeResult;
import com.alibaba.nb.android.trade.model.AliTradeShowParams;
import com.alibaba.nb.android.trade.model.AliTradeTaokeParams;
import com.alibaba.nb.android.trade.uibridge.AliTradeBasePage;
import com.alibaba.nb.android.trade.uibridge.AliTradeDetailPage;
import com.alibaba.nb.android.trade.uibridge.IAliTradeService;

import java.util.HashMap;
import java.util.Map;

/**
 * 阿里百川电商
 * 项目名称：阿里巴巴电商SDK
 * 开发团队：阿里巴巴百川商业化团队
 * 阿里巴巴电商SDK答疑群号：1200072507
 * <p/>
 * 功能说明：WebView代理页面
 */
public class AliSdkWebViewProxyActivity extends Activity {


    private WebView webView;
    private Map<String, String> exParams = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this, null);
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.addView(webView, params);
        setContentView(linearLayout);

        exParams.put(AliTradeConstants.ISV_CODE, "appisvcode");
        exParams.put(AliTradeConstants.SCM, "scm");
        exParams.put(AliTradeConstants.PVID, "pvid");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改

        IAliTradeService aliTradeService = AliTradeSDK.getService(IAliTradeService.class);
        AliTradeTaokeParams aliTradeTaokeParams = new AliTradeTaokeParams("mm_26632322_6858406_23810104", "mm_26632322_6858406_23810104", null); // 若非淘客taokeParams设置为null即可
        AliTradeBasePage aliTradeBasePage = new AliTradeDetailPage("532128520567");
        AliTradeShowParams aliTradeShowParams = new AliTradeShowParams(AliOpenType.H5, false);
        aliTradeService.show(this, webView, null, aliTradeBasePage, aliTradeShowParams, aliTradeTaokeParams, exParams, new AliTradeProcessCallback() {

            @Override
            public void onTradeSuccess(AliTradeResult tradeResult) {
                Toast.makeText(AliSdkWebViewProxyActivity.this, "WebView代理打开成功",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(AliSdkWebViewProxyActivity.this, "WebView代理打开失败" + code + msg,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    //登录须重写onActivityResult方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        AliTradeEvent.postEvent(AliTradeEvent.ACTIVTY_RESULT, requestCode, resultCode, data);
    }
}
