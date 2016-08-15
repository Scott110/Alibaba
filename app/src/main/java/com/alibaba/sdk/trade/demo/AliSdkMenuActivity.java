package com.alibaba.sdk.trade.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.alibaba.nb.android.trade.AliTradeContext;
import com.alibaba.nb.android.trade.AliTradeEvent;
import com.alibaba.nb.android.trade.bridge.login.AliTradeLoginService;
import com.alibaba.nb.android.trade.bridge.login.callback.AliTradeLoginCallback;
import com.alibaba.sdk.android.login.callback.LogoutCallback;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * 阿里百川电商
 * 项目名称：阿里巴巴电商SDK
 * 开发团队：阿里巴巴百川商业化团队
 * 阿里巴巴电商SDK答疑群号：1200072507
 * <p/>
 * 功能说明：电商SDK Demo主页面，包含登录，退出登录，webview代理测试等相关功能
 */
@EActivity(R.layout.activity_menu)
public class AliSdkMenuActivity extends AppCompatActivity {

    /**
     * 登录
     */
    @Click(R.id.alisdk_btn_login)
    void loginTest() {
        AliTradeLoginService aliTradeLoginService = AliTradeContext.serviceRegistry
                .getService(AliTradeLoginService.class, null);

        if (null == aliTradeLoginService || !aliTradeLoginService.isServiceAvliable()) {
            Toast.makeText(AliSdkMenuActivity.this, "loginService不可用",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        aliTradeLoginService.showLogin(AliSdkMenuActivity.this, new AliTradeLoginCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(AliSdkMenuActivity.this, "登录成功 ",
                        Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(AliSdkMenuActivity.this, "登录失败 ",
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * 退出登录
     */
    @Click(R.id.alisdk_btn_logout)
    void logoutTest() {
        AliTradeLoginService aliTradeLoginService = AliTradeContext.serviceRegistry
                .getService(AliTradeLoginService.class, null);

        if (null == aliTradeLoginService || !aliTradeLoginService.isServiceAvliable()) {
            Toast.makeText(AliSdkMenuActivity.this, "loginService不可用",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        aliTradeLoginService.logout(AliSdkMenuActivity.this, new LogoutCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(AliSdkMenuActivity.this, "退出登录成功",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(AliSdkMenuActivity.this, "退出登录失败 " + code + msg,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 电商交易
     */
    @Click(R.id.alisdk_btn_transaction)
    void transactionTest() {
        Intent transactionIntent = new Intent(AliSdkMenuActivity.this, AliSdkTransactionActivity_.class);
        startActivity(transactionIntent);
    }

    /**
     * 个人信息
     */
    @Click(R.id.alisdk_btn_mine)
    void mineTest() {
        Intent mineIntent = new Intent(AliSdkMenuActivity.this, AliSdkOrderActivity_.class);
        startActivity(mineIntent);
    }

    /**
     * webview代理
     */
    @Click(R.id.alisdk_btn_webview)
    void webviewTest() {
        Intent webviewIntent = new Intent(AliSdkMenuActivity.this, AliSdkWebViewProxyActivity.class);
        startActivity(webviewIntent);
    }

    //登录须重写onActivityResult方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        AliTradeEvent.postEvent(AliTradeEvent.ACTIVTY_RESULT, requestCode, resultCode, data);
    }

}
