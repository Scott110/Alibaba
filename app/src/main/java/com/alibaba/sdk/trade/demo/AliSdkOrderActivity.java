package com.alibaba.sdk.trade.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.alibaba.nb.android.trade.AliTradeSDK;
import com.alibaba.nb.android.trade.callback.AliTradeProcessCallback;
import com.alibaba.nb.android.trade.constants.AliTradeConstants;
import com.alibaba.nb.android.trade.model.AliOpenType;
import com.alibaba.nb.android.trade.model.AliTradeResult;
import com.alibaba.nb.android.trade.model.AliTradeShowParams;
import com.alibaba.nb.android.trade.uibridge.AliTradeBasePage;
import com.alibaba.nb.android.trade.uibridge.AliTradeMyCartsPage;
import com.alibaba.nb.android.trade.uibridge.AliTradeMyOrdersPage;
import com.alibaba.nb.android.trade.uibridge.IAliTradeService;
import com.alibaba.nb.android.trade.utils.AliTradeResultCode;

import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 阿里百川电商
 * 项目名称：阿里巴巴电商SDK
 * 开发团队：阿里巴巴百川商业化团队
 * 阿里巴巴电商SDK答疑群号：1200072507
 * <p/>
 * 功能说明：订单与购物车页，包括打开我的订单，打开我的购物车等相关内容
 */
@EActivity(R.layout.activity_mine)
public class AliSdkOrderActivity extends AppCompatActivity {

    private int orderType = 0;//订单页面参数，仅在H5方式下有效
    private AliTradeShowParams aliTradeShowParams;//页面打开方式，默认，H5，Native
    private Map<String, String> exParams;//yhhpass参数
    private IAliTradeService aliTradeService;
    private AliTradeBasePage aliTradeBasePage;//页面类型必填，不可为null
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.mine_test));

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
     * 分域显示我的订单，或者全部显示我的订单
     */
    @Click({R.id.alisdk_btn_show_order, R.id.alisdk_btn_show_allorder})
    public void showOrder(View view) {

        Boolean isAllOrder = true;
        switch (view.getId()) {
            case R.id.alisdk_btn_show_order:
                isAllOrder = false;
                break;
            case R.id.alisdk_btn_show_allorder:
                isAllOrder = true;
                break;
            default:
                break;
        }

        if (null == aliTradeService) {
            Toast.makeText(AliSdkOrderActivity.this, "无法获取tradeService",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        aliTradeBasePage = new AliTradeMyOrdersPage(orderType, isAllOrder);
        aliTradeService.show(this, aliTradeBasePage, aliTradeShowParams, null, exParams, new AliTradeProcessCallback() {
            @Override
            public void onTradeSuccess(AliTradeResult tradeResult) {
                Toast.makeText(AliSdkOrderActivity.this, "显示我的订单成功",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(AliSdkOrderActivity.this, "显示我的订单失败 " + code + msg,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @param view
     * 显示我的购物车
     */
    @Click(R.id.alisdk_btn_show_cart)
    public void showCart(View view) {

        if (null == aliTradeService) {
            Toast.makeText(AliSdkOrderActivity.this, "无法获取tradeService",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        aliTradeBasePage = new AliTradeMyCartsPage();
        aliTradeService.show(this, aliTradeBasePage, aliTradeShowParams, null, exParams, new AliTradeProcessCallback() {
            @Override
            public void onTradeSuccess(AliTradeResult tradeResult) {
                Toast.makeText(AliSdkOrderActivity.this, "打开购物车成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                if (code == AliTradeResultCode.QUERY_ORDER_RESULT_EXCEPTION.code) {
                    Toast.makeText(AliSdkOrderActivity.this, "打开购物车失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AliSdkOrderActivity.this, "取消购物车失败" + code, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //设置打开方式：默认方式
    @CheckedChange(R.id.alisdk_rb_mine_default)
    void defaultChecked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            aliTradeShowParams = new AliTradeShowParams(AliOpenType.Auto, false);
            Toast.makeText(this, buttonView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //设置打开方式：Native方式
    @CheckedChange(R.id.alisdk_rb_mine_native)
    void nativeChecked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            aliTradeShowParams = new AliTradeShowParams(AliOpenType.Native, false);
            Toast.makeText(this, buttonView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //设置打开方式：H5方式
    @CheckedChange(R.id.alisdk_rb_mine_h5)
    void h5Checked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            aliTradeShowParams = new AliTradeShowParams(AliOpenType.H5, false);
            Toast.makeText(this, buttonView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //设置订单页面参数：全部
    @CheckedChange(R.id.alisdk_rb_all)
    void allChecked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            orderType = 0;
            Toast.makeText(this, buttonView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //设置订单页面参数：待付款
    @CheckedChange(R.id.alisdk_rb_topay)
    void topayChecked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            orderType = 1;
            Toast.makeText(this, buttonView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //设置订单页面参数：待发货
    @CheckedChange(R.id.alisdk_rb_tosend)
    void tosendChecked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            orderType = 2;
            Toast.makeText(this, buttonView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //设置订单页面参数：待收货
    @CheckedChange(R.id.alisdk_rb_toreceive)
    void toreceiveChecked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            orderType = 3;
            Toast.makeText(this, buttonView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }
    //设置订单页面参数：待评价
    @CheckedChange(R.id.alisdk_rb_tocomment)
    void tocommentChecked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            orderType = 4;
            Toast.makeText(this, buttonView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
