package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.vondear.rxtools.RxDeviceUtils;
import com.vondear.rxtools.RxNetUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.vondear.rxtools.view.dialog.RxDialogSure;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.NumberFormat;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.MenuBund;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.interfaces.OkingCallBack;
import gd.water.oking.com.cn.wateradministration_gd.main.MainActivity;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.model.AppVersionModel;
import gd.water.oking.com.cn.wateradministration_gd.model.LoginModel;

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
    private MainActivity mActivity;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) getActivity();
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

                new LoginModel(mName, mPwd, savePwdCheckBox.isChecked(), new OkingCallBack.LoginCallBack() {
                    @Override
                    public void loginSucc(final MenuBund menuBund) {
                        loginBtn.setCompleteText("登录成功");
                        loginBtn.setProgress(100);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                BaseFragment f = MainFragment.newInstance(menuBund, mName);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_root, f).commitAllowingStateLoss();
                            }
                        }, 500);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mActivity.resetMissionList();
                            }
                        },500);
                    }

                    @Override
                    public void offlineLoginSucc(final MenuBund menuBund) {
                        loginBtn.setCompleteText("离线登录成功");
                        loginBtn.setProgress(100);
                        mActivity.resetMissionList();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                BaseFragment f = MainFragment.newInstance(menuBund, mName);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_root, f).commitAllowingStateLoss();
                            }
                        }, 1000);
                    }

                    @Override
                    public void loginFail(Exception e) {
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
                }).login();

            }
        });

    }


    private void detectionUpdate() {


        if (RxNetUtils.isConnected(MyApp.getApplictaion())) {
            new AppVersionModel(new OkingCallBack.AppVersionCallBck() {
                @Override
                public void reqSucc(String result) {
                    if (mRxDialogSure == null) {
                        mRxDialogSure = new RxDialogSure(getContext(), false, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                dialogInterface.cancel();
                            }
                        });
                        mRxDialogSure.setTitle("APP需要更新");
                        mRxDialogSure.setContent("本次更新内容：");
                        mRxDialogSure.setContent(result);
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

                @Override
                public void reqFail(Throwable ex) {
                    RxToast.error(MyApp.getApplictaion(), "网络或服务器错误", Toast.LENGTH_SHORT).show();
                }
            }).reqAppVersion();
        } else {
            RxToast.warning(MyApp.getApplictaion(), "网络无连接", Toast.LENGTH_SHORT).show();
        }

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


}
