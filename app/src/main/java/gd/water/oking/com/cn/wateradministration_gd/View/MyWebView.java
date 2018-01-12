package gd.water.oking.com.cn.wateradministration_gd.View;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;

/**
 * Created by zhao on 2017-5-11.
 */

public class MyWebView extends WebView {

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void loadUrl(final String url) {

        if (DefaultContants.ISHTTPLOGIN) {
            super.loadUrl(url);
        } else {
            this.stopLoading();
            this.clearView();

//            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setTitle("提示").
//                    setMessage("网络连接有误！").setNegativeButton("刷新", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    MyWebView.this.loadUrl(url);
//                }
//            }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            }).show();

//            this.loadUrl();
        }
    }
}
