package com.alibaba.sdk.trade.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.nb.android.trade.AliTradeSDK;
import com.alibaba.nb.android.trade.callback.AliTradeProcessCallback;
import com.alibaba.nb.android.trade.constants.AliTradeConstants;
import com.alibaba.nb.android.trade.model.AliOpenType;
import com.alibaba.nb.android.trade.model.AliTradeResult;
import com.alibaba.nb.android.trade.model.AliTradeShowParams;
import com.alibaba.nb.android.trade.model.AliTradeTaokeParams;
import com.alibaba.nb.android.trade.uibridge.AliTradeAddCartPage;
import com.alibaba.nb.android.trade.uibridge.AliTradeBasePage;
import com.alibaba.nb.android.trade.uibridge.AliTradeDetailPage;
import com.alibaba.nb.android.trade.uibridge.AliTradePage;
import com.alibaba.nb.android.trade.uibridge.AliTradeShopPage;
import com.alibaba.nb.android.trade.uibridge.IAliTradeService;

import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

/**
 * 阿里百川电商
 * 项目名称：阿里巴巴电商SDK
 * 开发团队：阿里巴巴百川商业化团队
 * 阿里巴巴电商SDK答疑群号：1200072507
 * <p/>
 * 功能说明：电商SDK 电商交易界面，包含直接URL加载，商品加载，店铺加载等相关内容
 */

@EActivity(R.layout.activity_transaction)
public class AliSdkTransactionActivity extends AppCompatActivity {

    @ViewById(R.id.alisdk_et_url)
    EditText etUrl;
    @ViewById(R.id.alisdk_et_itemId)
    EditText etItemId;
    @ViewById(R.id.alisdk_et_shopId)
    EditText etShopId;

    private AliTradeShowParams aliTradeShowParams;//页面打开方式，默认，H5，Native
    private AliTradeTaokeParams aliTradeTaokeParams = null;//淘客参数，包括pid，unionid，subPid
    private Boolean isTaoke = false;//是否是淘客商品类型
    private String itemId = "522166121586";//默认商品id
    private String shopId = "60552065";//默认店铺id
    private Map<String, String> exParams;//yhhpass参数
    private IAliTradeService aliTradeService;
    private AliTradeBasePage aliTradeBasePage;//页面类型必填，不可为null

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.transaction_test));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        aliTradeShowParams = new AliTradeShowParams(AliOpenType.Auto, false);
        aliTradeService = AliTradeSDK.getService(IAliTradeService.class);

        exParams = new HashMap<>();
        exParams.put(AliTradeConstants.ISV_CODE, "appisvcode");
        exParams.put(AliTradeConstants.SCM, "scm");
        exParams.put(AliTradeConstants.PVID, "pvid");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
    }

    /**
     * @param view
     * 打开指定链接
     */
    @Click(R.id.alisdk_btn_show_url)
    public void showUrl(View view) {

        if (null == aliTradeService) {
            Toast.makeText(AliSdkTransactionActivity.this, "无法获取tradeService",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String text = etUrl.getText().toString();
        if(TextUtils.isEmpty(text)) {
            Toast.makeText(AliSdkTransactionActivity.this, "URL为空",
                    Toast.LENGTH_SHORT).show();
            return;
        };

        aliTradeService.show(this, new AliTradePage(text), aliTradeShowParams, null, exParams , new AliTradeProcessCallback() {
            @Override
            public void onTradeSuccess(AliTradeResult tradeResult) {
                Toast.makeText(AliSdkTransactionActivity.this, "打开链接成功",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(AliSdkTransactionActivity.this, "打开链接失败" + code + msg,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @param view
     * 显示商品详情页
     */
    @Click(R.id.alisdk_btn_show_item)
    public void showDetail(View view) {

        if (null == aliTradeService) {
            Toast.makeText(AliSdkTransactionActivity.this, "无法获取tradeService",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(etItemId.getText().toString())) {
            aliTradeBasePage = new AliTradeDetailPage(etItemId.getText().toString());
        } else {
            aliTradeBasePage = new AliTradeDetailPage(itemId.trim());
        }

        if (isTaoke) {
            aliTradeTaokeParams = new AliTradeTaokeParams("mm_26632322_6858406_23810104", "", "");
        } else {
            aliTradeTaokeParams = null;
        }

        aliTradeService.show(this, aliTradeBasePage, aliTradeShowParams, aliTradeTaokeParams, exParams , new AliTradeProcessCallback() {
            @Override
            public void onTradeSuccess(AliTradeResult tradeResult) {
                Toast.makeText(AliSdkTransactionActivity.this, "显示商品详情页成功",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(AliSdkTransactionActivity.this, "显示商品详情页失败 " + code + msg,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @param view
     * 添加到购物车
     */
    @Click(R.id.alisdk_btn_add_cart)
    public void addToCart(View view) {

        if (null == aliTradeService) {
            Toast.makeText(AliSdkTransactionActivity.this, "无法获取tradeService",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(etItemId.getText().toString())) {
            aliTradeBasePage = new AliTradeAddCartPage(etItemId.getText().toString());
        } else {
            aliTradeBasePage = new AliTradeAddCartPage(itemId.trim());
        }

        if (isTaoke) {
            aliTradeTaokeParams = new AliTradeTaokeParams("mm_26632322_6858406_23810104", "mm_26632322_6858406_23810104", null);
        } else {
            aliTradeTaokeParams = null;
        }

        aliTradeService.show(this, aliTradeBasePage, aliTradeShowParams, aliTradeTaokeParams, exParams , new AliTradeProcessCallback() {
            @Override
            public void onTradeSuccess(AliTradeResult tradeResult) {
                Toast.makeText(AliSdkTransactionActivity.this, "添加到购物车成功",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(AliSdkTransactionActivity.this, "添加到购物车失败 " + code + msg,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @param view
     * 显示店铺
     */
    @Click(R.id.alisdk_btn_show_shop)
    public void showShop(View view) {

        if (null == aliTradeService) {
            Toast.makeText(AliSdkTransactionActivity.this, "无法获取tradeService",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(etShopId.getText().toString())) {
            aliTradeBasePage = new AliTradeShopPage(etShopId.getText().toString());
        } else {
            aliTradeBasePage = new AliTradeShopPage(shopId.trim());
        }

        aliTradeService.show(this, aliTradeBasePage, aliTradeShowParams, aliTradeTaokeParams, exParams , new AliTradeProcessCallback() {
            @Override
            public void onTradeSuccess(AliTradeResult tradeResult) {
                Toast.makeText(AliSdkTransactionActivity.this, "显示店铺成功",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(AliSdkTransactionActivity.this, "显示店铺失败 " + code + msg,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    //设置打开方式：默认方式
    @CheckedChange(R.id.alisdk_rb_tran_default)
    void defaultChecked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            aliTradeShowParams = new AliTradeShowParams(AliOpenType.Auto, false);
            Toast.makeText(this, buttonView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }
    //设置打开方式：Native方式
    @CheckedChange(R.id.alisdk_rb_tran_native)
    void nativeChecked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            aliTradeShowParams = new AliTradeShowParams(AliOpenType.Native, false);
            Toast.makeText(this, buttonView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }
    //设置打开方式：H5方式
    @CheckedChange(R.id.alisdk_rb_tran_h5)
    void h5Checked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            aliTradeShowParams = new AliTradeShowParams(AliOpenType.H5, false);
            Toast.makeText(this, buttonView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //设置商品类型：普通商品
    @CheckedChange(R.id.alisdk_rb_common)
    void commonChecked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            isTaoke = false;
            Toast.makeText(this, buttonView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //设置商品类型：淘客商品
    @CheckedChange(R.id.alisdk_rb_taoke)
    void taokeChecked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            isTaoke = true;
            Toast.makeText(this, buttonView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //设置商品id是否混淆：不混淆
    @CheckedChange(R.id.alisdk_rb_notconfuse)
    void notConfuseChecked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            itemId = "522166121586";
            if (etItemId.getText().toString().equals("AAHPt-dcABxGVVui-VRMv5Gm")) {
                etItemId.setText(itemId);
            }
            Toast.makeText(this, buttonView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //设置商品id是否混淆：混淆
    @CheckedChange(R.id.alisdk_rb_confuse)
    void confuseChecked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            itemId = "AAHPt-dcABxGVVui-VRMv5Gm";
            if (etItemId.getText().toString().equals("522166121586")) {
                etItemId.setText(itemId);
            }
            Toast.makeText(this, buttonView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
