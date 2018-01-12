package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.View.MyWebView;
import gd.water.oking.com.cn.wateradministration_gd.View.SignatureView;
import gd.water.oking.com.cn.wateradministration_gd.bean.Case;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.http.GetDoucmentIdParams;

/**
 * A simple {@link Fragment} subclass.
 */
public class DocumentSignDetailFragment extends BaseFragment implements View.OnClickListener, View.OnLayoutChangeListener {

    private Case myCase;
    private String documentId;
    private File dir = new File(Environment.getExternalStorageDirectory(), "oking/case_doucument");

    private MyWebView webView;
    private String urlStr;
    private ArrayList<Button> buttonArrayList = new ArrayList<>();

    private LinearLayout signArea;
    private Button signButton;

    private View documentLayout;

    public DocumentSignDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return inflater.inflate(R.layout.fragment_document_sign_detail, container, false);
    }

    @Override
    public void onDestroyView() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onDestroyView();
    }

    @Override
    public void initView(final View rootView) {

        Button backBtn = (Button) rootView.findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentSignDetailFragment.this.getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        Button btn1 = (Button) rootView.findViewById(R.id.button_1);
        btn1.setTag("xccftzs");
        btn1.setOnClickListener(this);
        buttonArrayList.add(btn1);
        Button btn2 = (Button) rootView.findViewById(R.id.button_2);
        btn2.setTag("zlzgtzs");
        btn2.setOnClickListener(this);
        buttonArrayList.add(btn2);

        signArea = (LinearLayout) rootView.findViewById(R.id.signArea);
        signButton = (Button) rootView.findViewById(R.id.sign_button);
        final SignatureView signature_View = (SignatureView) rootView.findViewById(R.id.signature_View);

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signArea.getVisibility() == View.GONE) {
                    signArea.setVisibility(View.VISIBLE);
                    signature_View.setFocusable(true);
                    signature_View.setFocusableInTouchMode(true);
                    signature_View.requestFocus();
                } else {
                    signArea.setVisibility(View.GONE);
                }
            }
        });

        signature_View.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    signArea.setVisibility(View.GONE);
                }
            }
        });

        Button clear_button = (Button) rootView.findViewById(R.id.clear_button);
        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signature_View.clear();
            }
        });

        documentLayout = rootView.findViewById(R.id.document_layout);
        documentLayout.addOnLayoutChangeListener(this);

        Button upload_button = (Button) rootView.findViewById(R.id.upload_button);
        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = signature_View.save();

                if (bitmap != null) {

                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    File signatureFile = new File(dir, documentId + ".jpg");

                    try {
                        OutputStream os = new FileOutputStream(signatureFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        os.flush();
                        os.close();
                        bitmap.recycle();
                        bitmap = null;
                        System.gc();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                    intent.setData(Uri.fromFile(signatureFile));
//                    getActivity().sendBroadcast(intent);

                    if (!"".equals(documentId)) {

//                    上传签名
                        RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/UploadMrg/saveWsfiles");
                        // 加到url里的参数, http://xxxx/s?wd=xUtils
                        params.addQueryStringParameter("wsid", documentId);
                        params.addQueryStringParameter("type", "WSQM");
                        params.addQueryStringParameter("user_id", DefaultContants.CURRENTUSER.getUserId());
                        // 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.

                        // 使用multipart表单上传文件
                        params.setMultipart(true);
                        params.addBodyParameter(
                                "files", signatureFile, null); // 如果文件没有扩展名, 最好设置contentType参数.

                        x.http().post(params, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                Log.i("UploadDocumentSign", "onSuccess>>>>>>" + result);
                                try {
                                    JSONArray object = new JSONArray(result);
                                    int code = object.getJSONObject(0).getInt("code");
                                    if (code == 200) {
                                        Toast.makeText(getContext(), "上传成功", Toast.LENGTH_LONG).show();
                                        signature_View.clear();
                                    } else {
                                        Toast.makeText(getContext(), object.getJSONObject(0).getString("msg"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                Log.i("UploadDocumentSign", "onError>>>>>>" + ex.toString());
                            }

                            @Override
                            public void onCancelled(CancelledException cex) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "未获取文书不能上传！", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "未签名不能上传！", Toast.LENGTH_LONG).show();
                }
            }
        });

        webView = (MyWebView) rootView.findViewById(R.id.webView);
        int screenDensity = getResources().getDisplayMetrics().densityDpi;
        WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
        switch (screenDensity) {

            case DisplayMetrics.DENSITY_LOW:
                zoomDensity = WebSettings.ZoomDensity.CLOSE;
                break;

            case DisplayMetrics.DENSITY_MEDIUM:
                zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                break;

            case DisplayMetrics.DENSITY_HIGH:
                zoomDensity = WebSettings.ZoomDensity.FAR;
                break;
        }
        webView.getSettings().setDefaultZoom(zoomDensity);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setInitialScale(100);
        webView.requestFocus();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);

        btn1.callOnClick();
    }

    public void setMyCase(Case myCase) {
        this.myCase = myCase;
    }

    @Override
    public void onClick(final View v) {
        if (v.getTag() != null) {

            GetDoucmentIdParams params = new GetDoucmentIdParams();
            params.caseid = myCase.getAJID();
            params.option = (String) v.getTag();
            Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    Log.i("GetDoucmentIdParams", "onSuccess>>>>>>" + result);

                    try {
                        JSONObject object = new JSONObject(result);
                        documentId = object.getString("id");
                        if (object.getBoolean("sfscwsqm")) {
                            signButton.setVisibility(View.GONE);
                        } else {
                            signButton.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    urlStr = DefaultContants.SERVER_HOST + "/page/case/ws_xccf.jsp?id=" + documentId;

                    DefaultContants.syncCookie(urlStr);
                    webView.loadUrl(urlStr);
                    webView.setWebViewClient(new WebViewClient() {
                        public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                            view.loadUrl(url);
                            return true;
                        }
                    });
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.i("GetDoucmentIdParams", "onError>>>>>>" + ex.toString());
                    documentId = "";
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });

            for (int i = 0; i < buttonArrayList.size(); i++) {
                buttonArrayList.get(i).setBackgroundResource(R.drawable.btn_bg);
            }
            v.setBackgroundResource(R.drawable.menu_btn_bg);
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > 100)) {
            Log.i("onLayoutChange", "监听到软键盘弹起...");
            signButton.setVisibility(View.GONE);
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > 100)) {
            Log.i("onLayoutChange", "监听到软件盘关闭...");
            signButton.setVisibility(View.VISIBLE);
        }
    }
}
