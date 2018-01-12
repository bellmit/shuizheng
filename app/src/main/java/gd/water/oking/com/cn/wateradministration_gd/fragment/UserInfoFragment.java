package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import org.xutils.common.Callback;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.http.UpdateUserInfoParams;
import gd.water.oking.com.cn.wateradministration_gd.main.MainActivity;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.FileUtil;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoFragment extends BaseFragment implements View.OnClickListener {

    private static final int PHOTO_FROM_CAMERA = 100;
    private Button changePhone_btn;
    private TextView changePhone_tv, phone_tv, name_tv, dept_tv;
    private EditText phone_et;
    private ImageView user_pic_iv;
    private File picStorageDir = new File(Environment.getExternalStorageDirectory(), "oking/temp");
    private RxDialogSureCancel mDialogSureCancel;
    private Button mBt_finish;
    //    private Uri photouri;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    private File mDataPicFile;

    @Override
    public void initView(final View rootView) {

        name_tv = (TextView) rootView.findViewById(R.id.name_textView);
        dept_tv = (TextView) rootView.findViewById(R.id.dept_textView);
        phone_tv = (TextView) rootView.findViewById(R.id.phone_textView);
        phone_et = (EditText) rootView.findViewById(R.id.phone_editText);
        user_pic_iv = (ImageView) rootView.findViewById(R.id.user_pic_iv);
        mBt_finish = (Button) rootView.findViewById(R.id.bt_finish);
        mBt_finish.setOnClickListener(this);

        TextView changePic_tv = (TextView) rootView.findViewById(R.id.change_pic_textView);
        changePic_tv.getPaint().setAntiAlias(true);
        changePic_tv.setOnClickListener(this);

        changePhone_tv = (TextView) rootView.findViewById(R.id.change_phone_textView);
        changePhone_tv.getPaint().setAntiAlias(true);
        changePhone_tv.setOnClickListener(this);

        changePhone_btn = (Button) rootView.findViewById(R.id.change_phone_btn);
        changePhone_btn.setOnClickListener(this);

        Button signOutBtn = (Button) rootView.findViewById(R.id.signout_btn);
        signOutBtn.setOnClickListener(this);

        Button changePwdBtn = (Button) rootView.findViewById(R.id.change_password_btn);
        changePwdBtn.setOnClickListener(this);

        //设置数据
        name_tv.setText(DefaultContants.CURRENTUSER.getUserName());
        dept_tv.setText(DefaultContants.CURRENTUSER.getDeptName());
        phone_tv.setText(DefaultContants.CURRENTUSER.getPhone());
        if (DefaultContants.CURRENTUSER.getProfile() != null) {

            new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Void... params) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(DefaultContants.CURRENTUSER.getProfile(), 0, DefaultContants.CURRENTUSER.getProfile().length);
                    if (bitmap == null) {
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.headerimg);
                    }
                    return bitmap;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    //super.onPostExecute(bitmap);
                    if (bitmap != null) {
                        user_pic_iv.setImageBitmap(bitmap);
                    }
                }
            }.execute();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        user_pic_iv.setImageBitmap(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_FROM_CAMERA:


                    final String path2 = FileUtil.PraseUritoPath(getContext(), Uri.fromFile(mDataPicFile));
                    FileUtil.compressImage(path2);

                    //Http上传头像
                    UpdateUserInfoParams params = new UpdateUserInfoParams();
                    params.userid = DefaultContants.CURRENTUSER.getUserId();

                    params.setMultipart(true);
                    params.addBodyParameter("headimg", new File(path2), null);

                    Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Log.i("UpdateUserProfile", "onSuccess>>>> " + result);

                            user_pic_iv.setImageURI(Uri.fromFile(mDataPicFile));

                            Bitmap bitmap = BitmapFactory.decodeFile(path2);
                            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            DefaultContants.CURRENTUSER.setProfile(bos.toByteArray());
                            try {
                                bos.close();
                                bitmap.recycle();
                                bitmap = null;
                                System.gc();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //通知系统扫描文件
//                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                            intent.setData(photouri);
//                            UserInfoFragment.this.getContext().sendBroadcast(intent);
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Log.i("UpdateUserProfile", "onError>>>> " + ex.toString());
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }
                    });
                    break;

                default:
                    break;
            }


        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_password_btn:
                final AlertDialog changeDialog = new AlertDialog.Builder(getActivity()).create();
                View inflate = View.inflate(getActivity(), R.layout.dialog_change_password, null);
                changeDialog.setView(inflate);
                changeDialog.show();
                final EditText old_password_et = (EditText) inflate.findViewById(R.id.old_password_editText);
                final EditText new_password_et = (EditText) inflate.findViewById(R.id.new_password_editText);
                final EditText new_password2_et = (EditText) inflate.findViewById(R.id.new_password2_editText);
                inflate.findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!new_password_et.getText().toString().equals(new_password2_et.getText().toString())) {
                            RxToast.warning(MyApp.getApplictaion(), "新密码与确认密码不一致！", Toast.LENGTH_LONG).show();
                            return;
                        }

                        UpdateUserInfoParams params = new UpdateUserInfoParams();
                        params.userid = DefaultContants.CURRENTUSER.getUserId();
                        params.account = DefaultContants.CURRENTUSER.getAccount();
                        params.oldPassword = old_password_et.getText().toString();
                        params.password = new_password_et.getText().toString();
                        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {

                            @Override
                            public void onSuccess(String result) {
                                Log.i("UpdateUserInfo", "onSuccess>>>> " + result);

                                if ("1".equals(result)) {
                                   RxToast.success(MyApp.getApplictaion(), "修改密码成功！", Toast.LENGTH_SHORT).show();

                                    changeDialog.dismiss();

                                    FragmentManager fm = UserInfoFragment.this.getActivity().getSupportFragmentManager();

                                    DefaultContants.CURRENTUSER = null;

                                    fm.popBackStack();
                                    fm.beginTransaction().replace(R.id.activity_root, new LoginFragment()).commit();

                                } else if ("0".equals(result)) {
                                    RxToast.error(MyApp.getApplictaion(), "修改密码失败！", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                RxToast.error(MyApp.getApplictaion(), "网络错误！", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(CancelledException cex) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    }
                });

                inflate.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeDialog.dismiss();
                    }
                });

                break;
            case R.id.signout_btn:
                if (mDialogSureCancel == null) {
                    mDialogSureCancel = new RxDialogSureCancel(getContext());
                    mDialogSureCancel.setContent("是否退出当前账户？");
                }

                mDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialogSureCancel.cancel();
                        FragmentManager fm = UserInfoFragment.this.getActivity().getSupportFragmentManager();

                        DefaultContants.CURRENTUSER = null;

                        //退出环信
                        if (EMClient.getInstance().isConnected()) {
                            EMClient.getInstance().logout(true);
                        }

                        //停止请求服务器
                        MainActivity activity = (MainActivity) getActivity();
                        activity.stopGetNetServer();
                        fm.popBackStack();
                        fm.beginTransaction().replace(R.id.activity_root, new LoginFragment()).commit();

                    }
                });

                mDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialogSureCancel.cancel();
                    }
                });
                mDialogSureCancel.show();

                break;
            case R.id.bt_finish:
                getActivity().finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                break;
            case R.id.change_phone_btn:

                //http上传电话
                UpdateUserInfoParams params = new UpdateUserInfoParams();
                params.userid = DefaultContants.CURRENTUSER.getUserId();
                params.phone = phone_et.getText().toString();
                Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        Log.i("UpdateUserPhone", "onSuccess>>>> " + result);

                        phone_tv.setText(phone_et.getText().toString());
                        phone_tv.setVisibility(View.VISIBLE);
                        phone_et.setVisibility(View.INVISIBLE);
                        changePhone_btn.setVisibility(View.INVISIBLE);
                        changePhone_tv.setVisibility(View.VISIBLE);

                        DefaultContants.CURRENTUSER.setPhone(phone_et.getText().toString());
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.i("UpdateUserPhone", "onError>>>> " + ex.toString());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });

                break;

            case R.id.change_pic_textView:
                if (!picStorageDir.exists()) {
                    picStorageDir.mkdirs();
                }

                mDataPicFile = new File(picStorageDir.getPath(), android.text.format.DateFormat
                        .format("yyyyMMdd_HHmmss", System.currentTimeMillis())
                        + ".jpg");
                mDataPicFile.getParentFile().mkdirs();
                startActivityForResult(
                        new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mDataPicFile)),
                        PHOTO_FROM_CAMERA);

                break;
            case R.id.change_phone_textView:
                phone_et.setText(phone_tv.getText().toString());
                phone_tv.setVisibility(View.INVISIBLE);
                phone_et.setVisibility(View.VISIBLE);
                changePhone_btn.setVisibility(View.VISIBLE);
                changePhone_tv.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }
}
