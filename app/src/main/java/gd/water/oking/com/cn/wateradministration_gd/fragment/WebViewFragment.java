package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;

/**
 * A k Fragment} subclass.
 */
public class WebViewFragment extends BaseFragment {

    private String urlStr;
    private int btn_position;


    private AgentWeb mAgentWeb;

    public WebViewFragment() {
        // Required empty public constructor

    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @Override
    public void initView(View rootView) {


        //传入Activity or Fragment
//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
// 使用默认进度条
//设置 Web 页面的 title 回调

        DefaultContants.syncCookie(DefaultContants.SERVER_HOST + urlStr);
        mAgentWeb = AgentWeb.with(this)//传入Activity or Fragment
                .setAgentWebParent((ViewGroup) rootView, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .useDefaultIndicator()//
                .setReceivedTitleCallback(new ChromeClientCallbackManager.ReceivedTitleCallback() {
                    @Override
                    public void onReceivedTitle(WebView webView, String s) {

                    }
                }) //设置 Web 页面的 title 回调
                .createAgentWeb()//
                .ready()
                .go(DefaultContants.SERVER_HOST+urlStr);



    }



    public String getUrlStr() {
        return urlStr;
    }

    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
    }

    public int getBtn_position() {
        return btn_position;
    }

    public void setBtn_position(int btn_position) {
        this.btn_position = btn_position;
    }


    @Override
    public void onDestroyView() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroyView();
    }



}

