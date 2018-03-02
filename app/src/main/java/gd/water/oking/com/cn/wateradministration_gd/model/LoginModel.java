package gd.water.oking.com.cn.wateradministration_gd.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Base64;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.vondear.rxtools.RxNetUtils;
import com.vondear.rxtools.view.RxToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.bean.MenuBund;
import gd.water.oking.com.cn.wateradministration_gd.bean.MenuGsonBean;
import gd.water.oking.com.cn.wateradministration_gd.bean.User;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.http.LoginParams;
import gd.water.oking.com.cn.wateradministration_gd.http.MapResponse;
import gd.water.oking.com.cn.wateradministration_gd.interfaces.OkingCallBack;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.LocalSqlite;
import gd.water.oking.com.cn.wateradministration_gd.util.Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/2/1.
 */

public class LoginModel {
    private String mName;
    private String mPwd;
    private boolean mSavePwdCheck;
    private MenuBund mMenuBund;
    private OkingCallBack.LoginCallBack mLoginCallBack;

    public LoginModel(String name, String pwd, boolean savePwdCheck, OkingCallBack.LoginCallBack loginCallBack) {
        this.mName = name;
        this.mPwd = pwd;
        this.mSavePwdCheck = savePwdCheck;
        this.mLoginCallBack = loginCallBack;
    }


    /**
     * 用户登录
     */
    public void login() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                loginTo(e);


            }
        }).concatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String s) throws Exception {
                if (s.equals("0")) {
                    return new Observable<String>() {
                        @Override
                        protected void subscribeActual(Observer<? super String> observer) {
                            reQuestMenu(observer);
                        }
                    };
                } else if (s.equals("1")) {    //离线登录
                    return new Observable<String>() {
                        @Override
                        protected void subscribeActual(Observer<? super String> observer) {

                            observer.onNext("1");
                        }
                    };
                } else {
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

                            mLoginCallBack.loginSucc(mMenuBund);


                        } else if (value.equals("1")) {
                            mLoginCallBack.offlineLoginSucc(mMenuBund);

                        } else {
                            mLoginCallBack.loginFail(new Exception("密码或网络错误"));

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


    /**
     * @param emitter
     */
    private void loginTo(final ObservableEmitter<String> emitter) {
        if (RxNetUtils.isConnected(MyApp.getApplictaion())) {
            final LoginParams params = new LoginParams();
            params.account = mName;
            params.password = mPwd;
            x.http().post(params, new Callback.CommonCallback<MapResponse>() {
                @Override
                public void onSuccess(final MapResponse result) {
                    boolean isOk = (Boolean) result.getMap().get("success");
                    if (isOk) {

                        DefaultContants.ISHTTPLOGIN = true;

                        Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> e) throws Exception {
                                        SharedPreferences sharedPreferences1 = MyApp.getApplictaion().getSharedPreferences("logintime", Context.MODE_PRIVATE);
                                        long logintime = sharedPreferences1.getLong("logintime", 0);
                                        if (logintime != 0) {
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                            if (!sdf.format(logintime).equals(sdf.format(Long.valueOf((String) result.getMap().get("logintime"))))) {
                                                SharedPreferences.Editor editor = sharedPreferences1.edit();
                                                editor.putLong("logintime", Long.valueOf((String) result.getMap().get("logintime")));
                                                editor.commit();

                                            }
                                        } else {
                                            SharedPreferences.Editor editor = sharedPreferences1.edit();
                                            editor.putLong("logintime", Long.valueOf((String) result.getMap().get("logintime")));
                                            editor.commit();

                                        }

                                        Utils.getCookies2DB();

                                        SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("lastAccount", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        if (mSavePwdCheck) {
                                            //记录上一次登陆
                                            editor.putString("name", mName);
                                            editor.putString("password", mPwd);
                                            editor.putBoolean("isSave", mSavePwdCheck);
                                            editor.commit();
                                        } else {
                                            editor.putString("name", "");
                                            editor.putString("password", "");
                                            editor.putBoolean("isSave", mSavePwdCheck);
                                            editor.commit();
                                        }

                                        String userid = (String) result.getMap().get("userid");
                                        String dept_id = (String) result.getMap().get("dept_id");
                                        String userName = (String) result.getMap().get("userName");
                                        String deptname = (String) result.getMap().get("deptname");
                                        String phone = (String) result.getMap().get("phone");
                                        byte[] headimgs = Base64.decode((String) result.getMap().get("headimg"), Base64.DEFAULT);

                                        //登录成功，本地数据库存入数据或更新数据
                                        if (DefaultContants.CURRENTUSER != null) {
                                            DefaultContants.CURRENTUSER.setUserId(userid);
                                            DefaultContants.CURRENTUSER.setDeptId(dept_id);
                                            DefaultContants.CURRENTUSER.setUserName(userName);
                                            DefaultContants.CURRENTUSER.setDeptName(deptname);
                                            DefaultContants.CURRENTUSER.setAccount(mName);
                                            DefaultContants.CURRENTUSER.setPassword(mPwd);
                                            DefaultContants.CURRENTUSER.setPhone(phone);
                                            DefaultContants.CURRENTUSER.setProfile(headimgs);


                                        } else {
                                            User user = new User();
                                            user.setUserId(userid);
                                            user.setDeptId(dept_id);
                                            user.setUserName(userName);
                                            user.setDeptName(deptname);
                                            user.setAccount(mName);
                                            user.setPassword(mPwd);
                                            user.setPhone(phone);
                                            user.setProfile(headimgs);
                                            DefaultContants.CURRENTUSER = user;

                                        }
                                        MyApp.localSqlite.delete(LocalSqlite.USER_TABLE,
                                                "uid=?", new String[]{DefaultContants.CURRENTUSER.getUserId()});

                                        DefaultContants.CURRENTUSER.insertDB(MyApp.localSqlite);
                                e.onNext("0");
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(String value) {
                                emitter.onNext(value);
                            }

                            @Override
                            public void onError(Throwable e) {
                                emitter.onNext("-1");
                            }

                            @Override
                            public void onComplete() {

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


                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            RxToast.warning(MyApp.getApplictaion(), "网络错误，进行离线登录", Toast.LENGTH_SHORT).show();
                        }
                    });


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
                                cursor.close();

                            } else {
                                emitter.onNext("-1");
                            }

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });

        } else {

            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(final ObservableEmitter<String> em) throws Exception {
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
                                        em.onNext("1");
                                    } else {
                                        em.onNext("-1");
                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    em.onNext("-1");
                                } finally {

                                    cursor.close();
                                }

                            } else {
                                em.onNext("-1");
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
                            emitter.onNext(value);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        }


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


                } catch (final JSONException e) {
                    e.printStackTrace();
                    observer.onNext("-1");
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            RxToast.error(MyApp.getApplictaion(), "服务器内部错误" + e.getMessage(), Toast.LENGTH_SHORT, true).show();

                        }
                    });

                }


            }

            @Override
            public void onError(final Throwable ex, boolean isOnCallback) {
                observer.onNext("-1");
                AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
                        RxToast.error(MyApp.getApplictaion(), "请求失败" + ex.getMessage(), Toast.LENGTH_SHORT, true).show();

                    }
                });

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

}
