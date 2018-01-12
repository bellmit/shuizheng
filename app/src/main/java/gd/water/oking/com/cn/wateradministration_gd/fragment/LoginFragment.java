package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.vondear.rxtools.RxDeviceUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.vondear.rxtools.view.dialog.RxDialogSure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.http.cookie.DbCookieStore;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpCookie;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.MenuBund;
import gd.water.oking.com.cn.wateradministration_gd.bean.MenuGsonBean;
import gd.water.oking.com.cn.wateradministration_gd.bean.User;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.http.LoginParams;
import gd.water.oking.com.cn.wateradministration_gd.http.MapResponse;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.LocalSqlite;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LoginFragment extends BaseFragment {

    private CircularProgressButton loginBtn;
    private TextInputEditText userNameEditText;
    private TextInputEditText passwordEditText;
    private CheckBox savePwdCheckBox;
    private RxDialogSure mRxDialogSure;
    private RxDialogLoading mRxDialogLoading;
    private Handler handler = new Handler();
    private String mName;
    private String mPwd;
    private MenuBund mMenuBund;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        return rootView;
    }


    @Override
    public void initView(View rootView) {
        TextView tv_version = rootView.findViewById(R.id.tv_version);

        tv_version.setText("当前版本号：v" + RxDeviceUtils.getAppVersionName(MyApp.getApplictaion()));
        //检测更新
        detectionUpdate();

        loginBtn = rootView.findViewById(R.id.login_button);
        loginBtn.setIndeterminateProgressMode(true);
        userNameEditText = rootView.findViewById(R.id.userName_editText);
        passwordEditText = rootView.findViewById(R.id.password_editText);

        savePwdCheckBox = rootView.findViewById(R.id.save_pwd_button);

        final SharedPreferences sharedPreferences = LoginFragment.this.getActivity().getSharedPreferences("lastAccount", Context.MODE_PRIVATE);
        final String spname = sharedPreferences.getString("name", "");
        final String spwd = sharedPreferences.getString("password", "");
        if (TextUtils.isEmpty(spname) || TextUtils.isEmpty(spwd)) {
            //第一次登录去打开vpn
            RxToast.warning(MyApp.getApplictaion(), "初次登录请打开VPN", Toast.LENGTH_LONG).show();
            Intent vpnIntent = new Intent();
            vpnIntent.setAction("android.net.vpn.SETTINGS");
            getActivity().startActivity(vpnIntent);
        }
        userNameEditText.setText(spname);
        passwordEditText.setText(spwd);
        savePwdCheckBox.setChecked(sharedPreferences.getBoolean("isSave", false));

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mName = userNameEditText.getText().toString().trim();
                mPwd = passwordEditText.getText().toString().trim();
//                if (name.equals(spname)&&pwd.equals(spwd)){
//                    login();
//                }else {
//
//                    //如果是新账户登录，先清空缓存再登录/*
//                    FileUtil.cleanDatabases(MyApp.getApplictaion());
//                    FileUtil.cleanSharedPreference(MyApp.getApplictaion());*/
//                }
                if (TextUtils.isEmpty(mName) || TextUtils.isEmpty(mPwd)) {
                    RxToast.warning(MyApp.getApplictaion(), "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                userNameEditText.setEnabled(false);
                passwordEditText.setEnabled(false);
                loginBtn.setProgress(50);
                loginBtn.setClickable(false);
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        login(e);


                    }
                }).flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        if (s.equals("0")){
                            return new Observable<String>() {
                                @Override
                                protected void subscribeActual(Observer<? super String> observer) {
                                    reQuestMenu(observer);
                                }
                            };
                        }else if (s.equals("1")){    //离线登录
                            return new Observable<String>() {
                                @Override
                                protected void subscribeActual(Observer<? super String> observer) {

                                    observer.onNext("1");
                                }
                            };
                        }else {
                            return new Observable<String>() {
                                @Override
                                protected void subscribeActual(Observer<? super String> observer) {

                                    observer.onNext("-1");
                                }
                            };
                        }


                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(String value) {
                                if (value.equals("0")) {
                                    loginBtn.setCompleteText("登录成功");
                                    loginBtn.setProgress(100);


                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            BaseFragment f = MainFragment.newInstance(mMenuBund, mName);
                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_root, f).commitAllowingStateLoss();
                                        }
                                    }, 800);

                                } else if (value.equals("1")) {
                                    loginBtn.setCompleteText("离线登录成功");
                                    loginBtn.setProgress(100);


                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            BaseFragment f = MainFragment.newInstance(mMenuBund, userNameEditText.getText().toString().trim());
                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_root, f).commitAllowingStateLoss();
                                        }
                                    }, 800);
                                }else {
                                    userNameEditText.setEnabled(true);
                                    passwordEditText.setEnabled(true);
                                    loginBtn.setErrorText("登录失败，请重新登录");
                                    loginBtn.setProgress(-1);

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            loginBtn.setClickable(true);
                                            loginBtn.setProgress(0);
                                        }
                                    }, 1600);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

            }

            /**\
             *
             * @param emitter
             */
            private void login(final ObservableEmitter<String> emitter) {

                final LoginParams params = new LoginParams();
                params.account = mName;
                params.password = mPwd;
                x.http().post(params, new Callback.CommonCallback<MapResponse>() {
                    @Override
                    public void onSuccess(final MapResponse result) {

                        boolean isOk = (Boolean) result.getMap().get("success");
                        if (isOk) {


                            loginChatServer();

                            MyApp.getGlobalThreadPool().execute(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences sharedPreferences1 = MyApp.getApplictaion().getSharedPreferences("logintime", Context.MODE_PRIVATE);
                                    long logintime = sharedPreferences1.getLong("logintime", 0);
                                    if (logintime != 0) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        if (!sdf.format(new Date(logintime)).equals(sdf.format(new Date(Long.valueOf((String) result.getMap().get("logintime")))))) {
                                            SharedPreferences.Editor editor = sharedPreferences1.edit();
                                            editor.putLong("logintime", Long.valueOf((String) result.getMap().get("logintime")));
                                            editor.commit();

                                        }
                                    } else {
                                        SharedPreferences.Editor editor = sharedPreferences1.edit();
                                        editor.putLong("logintime", Long.valueOf((String) result.getMap().get("logintime")));
                                        editor.commit();

                                    }

                                    DbCookieStore instance = DbCookieStore.INSTANCE;
                                    List<HttpCookie> cookies = instance.getCookies();
                                    for (int i = 0; i < cookies.size(); i++) {
                                        HttpCookie cookie = cookies.get(i);
                                        if ("JSESSIONID".equals(cookie.getName())) {
                                            DefaultContants.ISHTTPLOGIN = true;
                                            DefaultContants.JSESSIONID = cookie.getValue();
                                            DefaultContants.DOMAIN = cookie.getDomain();
                                            DefaultContants.PATH = cookie.getPath();
                                            DefaultContants.CURRENTUSER = new User();
                                        }
                                    }

                                    SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("lastAccount", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    if (savePwdCheckBox.isChecked()) {
                                        //记录上一次登陆
                                        editor.putString("name", mName);
                                        editor.putString("password", mPwd);
                                        editor.putBoolean("isSave", savePwdCheckBox.isChecked());
                                        editor.commit();
                                    } else {
                                        editor.putString("name", "");
                                        editor.putString("password", "");
                                        editor.putBoolean("isSave", savePwdCheckBox.isChecked());
                                        editor.commit();
                                    }

                                    //登录成功，本地数据库存入数据或更新数据
                                    if (DefaultContants.CURRENTUSER != null) {
                                        DefaultContants.CURRENTUSER.setUserId((String) result.getMap().get("userid"));
                                        DefaultContants.CURRENTUSER.setDeptId((String) result.getMap().get("dept_id"));
                                        DefaultContants.CURRENTUSER.setUserName((String) result.getMap().get("userName"));
                                        DefaultContants.CURRENTUSER.setDeptName((String) result.getMap().get("deptname"));
                                        DefaultContants.CURRENTUSER.setAccount(mName);
                                        DefaultContants.CURRENTUSER.setPassword(mPwd);
                                        DefaultContants.CURRENTUSER.setPhone((String) result.getMap().get("phone"));
                                        DefaultContants.CURRENTUSER.setProfile(Base64.decode((String) result.getMap().get("headimg"), Base64.DEFAULT));

                                        MyApp.localSqlite.delete(LocalSqlite.USER_TABLE,
                                                "uid=?", new String[]{DefaultContants.CURRENTUSER.getUserId()});

                                        DefaultContants.CURRENTUSER.insertDB(MyApp.localSqlite);

                                        emitter.onNext("0");
                                    } else {
                                        emitter.onNext("-1");
                                    }
                                }
                            });



                        } else {
                            emitter.onNext("-1");

                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                        DefaultContants.ISHTTPLOGIN = false;
                        DefaultContants.JSESSIONID = "";

                        if (ex instanceof HttpException) { // 网络错误
                            HttpException httpEx = (HttpException) ex;
                            int responseCode = httpEx.getCode();
                            String responseMsg = httpEx.getMessage();
                            String errorResult = httpEx.getResult();
                            httpEx.printStackTrace();
                            RxToast.error(MyApp.getApplictaion(), ex.getMessage(), Toast.LENGTH_LONG).show();
                        } else { // 其他错误
                            // ...
                        }

                        RxToast.warning(MyApp.getApplictaion(), "网络错误，进行离线登录", Toast.LENGTH_SHORT).show();
                        Cursor cursor = MyApp.localSqlite.select(LocalSqlite.USER_TABLE,
                                new String[]{"uid", "deptId", "userName", "deptName", "account", "password", "phone", "profile", "jsonStr"},
                                "account=? and password=?",
                                new String[]{mName, mPwd},
                                null, null, null);
                        if (cursor.moveToNext()) {


                            DefaultContants.CURRENTUSER = DataUtil.praseJson(cursor.getString(cursor.getColumnIndex("jsonStr")), new TypeToken<User>() {
                            });
                            try {
                                ObjectInputStream inputStream = new ObjectInputStream(MyApp.getApplictaion().openFileInput(DefaultContants.CURRENTUSER.getUserName() + ".txt"));
                                mMenuBund = (MenuBund) inputStream.readObject();
                                inputStream.close();
                                if (mMenuBund != null) {
                                    emitter.onNext("1");    //离线登录
                                }
                            } catch (IOException e) {
                                emitter.onNext("-1");
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                emitter.onNext("-1");
                                e.printStackTrace();
                            }


                        } else {
                            emitter.onNext("-1");
                        }
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        RxToast.warning(MyApp.getApplictaion(), "cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinished() {

                    }
                });

            }
        });

    }

    private void detectionUpdate() {


        RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/cs/cscx");
        params.addBodyParameter("lx", "app_version");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String value = jsonObject.getString("VALUE");
                        String bz = jsonObject.getString("BZ");
                        if (!value.equals(RxDeviceUtils.getAppVersionName(MyApp.getApplictaion()))) {
//                            loginBtn.setEnabled(false);
                            if (mRxDialogSure == null) {
                                mRxDialogSure = new RxDialogSure(getContext(), false, new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        dialogInterface.cancel();
                                    }
                                });
                                mRxDialogSure.setIvLogo("APP需要更新");
                                mRxDialogSure.setTitle("本次更新内容：");
                                mRxDialogSure.setContent(bz);
                                mRxDialogSure.getTvSure().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mRxDialogSure.cancel();


                                        donlowdApk();
                                    }
                                });
                            }

                            mRxDialogSure.show();

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    RxToast.error(MyApp.getApplictaion(), "服务器内部错误", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                RxToast.error(MyApp.getApplictaion(), "网络或服务器错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }
    //下载apk
    private void donlowdApk() {
        final NumberFormat format = NumberFormat.getPercentInstance();// 获取格式化类实例
        format.setMinimumFractionDigits(1);// 设置小数位
        if (mRxDialogLoading == null) {

            mRxDialogLoading = new RxDialogLoading(getContext(), false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialogInterface.cancel();
                }
            });
        }
        mRxDialogLoading.setLoadingText("下载进度:0%");
        mRxDialogLoading.show();
        RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/app/gdWater.apk");
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/oking/app/gdWater.apk");
        if (file.exists()) {
            file.delete();
        }
        params.setSaveFilePath(Environment.getExternalStorageDirectory().getPath() + "/oking/app/gdWater.apk");
        x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {

                RxToast.success(MyApp.getApplictaion(), "下载成功", Toast.LENGTH_SHORT).show();
                mRxDialogLoading.cancel();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(result),
                        "application/vnd.android.package-archive");
                getActivity().startActivity(intent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                getActivity().finish();
                mRxDialogLoading.cancel();
                RxToast.error(MyApp.getApplictaion(), "下载失败，请检查网络和SD卡", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

                mRxDialogLoading.setLoadingText("下载进度:" + format.format((double) current / total));

            }
        });


    }

    //请求菜单
    private void reQuestMenu(final Observer<? super String> observer) {
        RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/menu/getMenuTreeForAndroid");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                List<MenuGsonBean> menuGsonBeen = new ArrayList<MenuGsonBean>();
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        MenuGsonBean gsonBean = new MenuGsonBean();

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String leftMenu = jsonObject.getString("text");
                        gsonBean.setText(leftMenu);
                        JSONArray children = jsonObject.getJSONArray("children");
                        List<MenuGsonBean.TopMenu> topMenus = new ArrayList<MenuGsonBean.TopMenu>();
                        for (int j = 0; j < children.length(); j++) {
                            MenuGsonBean.TopMenu topMenu = new MenuGsonBean.TopMenu();
                            topMenu.setText(children.getJSONObject(j).getString("text"));
                            topMenu.setRequest(children.getJSONObject(j).getString("request"));
                            topMenus.add(topMenu);
                        }
                        gsonBean.setTopMenus(topMenus);
                        menuGsonBeen.add(gsonBean);
                    }

                    mMenuBund = new MenuBund();
                    mMenuBund.setMenuGsonBeen(menuGsonBeen);
                    //数据序列化到本地

                    try {
                        ObjectOutputStream outputStream = new ObjectOutputStream(MyApp.getApplictaion().openFileOutput(DefaultContants.CURRENTUSER.getUserName() + ".txt", MyApp.getApplictaion().MODE_PRIVATE));
                        outputStream.writeObject(mMenuBund);
                        outputStream.close();

                        observer.onNext("0");


                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    observer.onNext("-1");
                    RxToast.error(MyApp.getApplictaion(), "服务器内部错误" + e.getMessage(), Toast.LENGTH_SHORT, true).show();

                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                observer.onNext("-1");
                RxToast.error(MyApp.getApplictaion(), "请求失败" + ex.getMessage(), Toast.LENGTH_SHORT, true).show();

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void loginChatServer() {
        MyApp.getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(mName, "888888");
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    if (e.getErrorCode() == 203) {

                        EMClient.getInstance().login(mName, "888888", new EMCallBack() {//回调
                            @Override
                            public void onSuccess() {
                                System.out.println(" 环信登录成功");
                                EMClient.getInstance().groupManager().loadAllGroups();
                                EMClient.getInstance().chatManager().loadAllConversations();

                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }

                            @Override
                            public void onError(int code, String message) {
                                System.out.println("环信登录失败" + code + message);

                            }

                        });
                    }
                }
            }
        });


    }

}
