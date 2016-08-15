package com.alibaba.sdk.trade.demo;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.nb.android.trade.AliTradeSDK;
import com.alibaba.nb.android.trade.callback.AliTradeInitCallback;
import com.alibaba.sdk.android.AlibabaSDK;

/**
 * 阿里百川电商
 * 项目名称：阿里巴巴电商SDK
 * 开发团队：阿里巴巴百川商业化团队
 * 阿里巴巴电商SDK答疑群号：1200072507
 * <p/>
 * 功能说明：全局application
 */
public class AliSdkApplication extends Application {
    private static final String TAG  = AliSdkApplication.class.getSimpleName();

    public static AliSdkApplication application = null;

    public static final String APP_KEY = "23393904";

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        try {

            AlibabaSDK.turnOnDebug();

            Log.e(TAG, "onCreate: :::: 11111111");

            //电商SDK初始化
            AliTradeSDK.asyncInit(this, APP_KEY, new AliTradeInitCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AliSdkApplication.this, "初始化成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int code, String msg) {
                    Toast.makeText(AliSdkApplication.this, "初始化失败", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
