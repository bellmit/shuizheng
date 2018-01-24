package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.vondear.rxtools.RxAppUtils;
import com.vondear.rxtools.RxFileUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogLoading;

import java.io.File;
import java.util.Calendar;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.FileUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.Utils;

/**
 * 责令停止违法行为
 */
public class StopTheIllegalActivitiesFragment extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE_CAMERA = 102;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView mTv_time;
    private TextInputEditText mTet_parties_concerned_natural;
    private RadioButton mRb_natural_person;
    private TextInputEditText mTet_sex_natural;
    private TextInputEditText mTet_card_natural;
    private TextInputEditText mTet_phone_natural;
    private TextInputEditText mTet_address_natural;
    private TextInputEditText mTet_parties_concerned_other;
    private TextInputEditText mTet_position_other;
    private TextInputEditText mTet_phone_other;
    private TextInputEditText mTet_representative_other;
    private TextInputEditText mTet_credit_code_oher;
    private TextInputEditText mTet_address_other;
    private Button mBt_idcard;
    private int mMYear;
    private int mMMonth;
    private int mMDay;
    private Button mBt_prin;
    private RxDialogLoading mRxDialogLoading;
    private String mNaturalInfo;
    private EditText mEt_illegal_facts;
    private EditText mEt_legal_provisions1;
    private EditText mEt_legal_provisions2;
    private TextInputEditText mTet_contact;
    private TextInputEditText mTet_phone;
    private TextInputEditText mTet_addr;

    public StopTheIllegalActivitiesFragment() {
        // Required empty public constructor
    }

    public static StopTheIllegalActivitiesFragment newInstance(String param1, String param2) {
        StopTheIllegalActivitiesFragment fragment = new StopTheIllegalActivitiesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stop_the_illegal_activities, container, false);
    }

    @Override
    public void initView(View rootView) {
        mTv_time = rootView.findViewById(R.id.tv_time);
        mRb_natural_person = rootView.findViewById(R.id.rb_natural_person);
        mTet_parties_concerned_natural = rootView.findViewById(R.id.tet_parties_concerned_natural);
        mTet_sex_natural = rootView.findViewById(R.id.tet_sex_natural);
        mTet_card_natural = rootView.findViewById(R.id.tet_card_natural);
        mTet_phone_natural = rootView.findViewById(R.id.tet_phone_natural);
        mTet_address_natural = rootView.findViewById(R.id.tet_address_natural);
        mTet_parties_concerned_other = rootView.findViewById(R.id.tet_parties_concerned_other);
        mTet_position_other = rootView.findViewById(R.id.tet_position_other);
        mTet_phone_other = rootView.findViewById(R.id.tet_phone_other);
        mTet_representative_other = rootView.findViewById(R.id.tet_representative_other);
        mTet_credit_code_oher = rootView.findViewById(R.id.tet_credit_code_oher);
        mTet_address_other = rootView.findViewById(R.id.tet_address_other);
        mBt_idcard = rootView.findViewById(R.id.bt_idcard);
        mBt_prin = rootView.findViewById(R.id.bt_prin);
        mEt_illegal_facts = rootView.findViewById(R.id.et_illegal_facts);
        mEt_legal_provisions1 = rootView.findViewById(R.id.et_legal_provisions);
        mEt_legal_provisions2 = rootView.findViewById(R.id.et_legal_provisions2);
        mTet_contact = rootView.findViewById(R.id.tet_contact);
        mTet_phone = rootView.findViewById(R.id.tet_phone);
        mTet_addr = rootView.findViewById(R.id.tet_addr);

        initData();
        iniListener();
    }

    private void initData() {
        Utils.setEditTextInhibitInputSpace(mTet_representative_other);
        Utils.setEditTextInhibitInputSpeChat(mTet_representative_other);

        Utils.setEditTextInhibitInputSpace(mTet_address_natural);
        Utils.setEditTextInhibitInputSpeChat(mTet_address_natural);

        Utils.setEditTextInhibitInputSpace(mTet_address_other);
        Utils.setEditTextInhibitInputSpeChat(mTet_address_other);

        Utils.setEditTextInhibitInputSpace(mTet_addr);
        Utils.setEditTextInhibitInputSpeChat(mTet_addr);

        Utils.setEditTextInhibitInputSpace(mTet_contact);
        Utils.setEditTextInhibitInputSpeChat(mTet_contact);

        Calendar c = Calendar.getInstance();//
        // 获取当前年份
        mMYear = c.get(Calendar.YEAR);
        // 获取当前月份
        mMMonth = c.get(Calendar.MONTH) + 1;
        // 获取当日期
        mMDay = c.get(Calendar.DAY_OF_MONTH);

        mTv_time.setText(mMYear + "年" + mMMonth + "月" + mMDay + "日");

        //初始化OCR
        if (!DefaultContants.HASGOTTOKEN) {

            initAccessToken();
        }
    }


    private void iniListener() {
        mBt_prin.setOnClickListener(this);
        mRb_natural_person.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mRb_natural_person.isChecked()) {
                    mTet_parties_concerned_natural.setEnabled(true);
                    mTet_parties_concerned_other.setEnabled(false);
                    mTet_parties_concerned_other.setText("");
                    mTet_sex_natural.setEnabled(true);
                    mTet_card_natural.setEnabled(true);
                    mTet_phone_natural.setEnabled(true);
                    mTet_phone_other.setEnabled(false);
                    mTet_phone_other.setText("");
                    mTet_address_natural.setEnabled(true);
                    mTet_address_other.setEnabled(false);
                    mTet_address_other.setText("");
                    mTet_position_other.setEnabled(false);
                    mTet_position_other.setText("");
                    mTet_representative_other.setEnabled(false);
                    mTet_representative_other.setText("");
                    mTet_credit_code_oher.setEnabled(false);
                    mTet_credit_code_oher.setText("");
                    mBt_idcard.setEnabled(true);
                } else {
                    mTet_parties_concerned_natural.setEnabled(false);
                    mTet_parties_concerned_other.setEnabled(true);
                    mTet_sex_natural.setEnabled(false);
                    mTet_card_natural.setEnabled(false);
                    mTet_phone_natural.setEnabled(false);
                    mTet_phone_other.setEnabled(true);
                    mTet_address_natural.setEnabled(false);
                    mTet_address_other.setEnabled(true);
                    mTet_position_other.setEnabled(true);
                    mTet_representative_other.setEnabled(true);
                    mTet_credit_code_oher.setEnabled(true);
                    mBt_idcard.setEnabled(false);
                }
            }
        });

        mBt_idcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkTokenStatus()) {
                    //初始化OCR
                    initAccessToken();
                } else {

                    Intent intent = new Intent(getActivity(), CameraActivity.class);
                    intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                            FileUtil.getSaveFile(MyApp.getApplictaion()).getAbsolutePath());
                    intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                }
            }
        });

    }

    private boolean checkTokenStatus() {
        if (!DefaultContants.HASGOTTOKEN) {
            RxToast.warning(MyApp.getApplictaion(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return DefaultContants.HASGOTTOKEN;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            RxToast.warning(MyApp.getApplictaion(), "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
        }


    }

    private void initAccessToken() {
        OCR.getInstance().initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                DefaultContants.HASGOTTOKEN = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
//                RxToast.error(MyApp.getApplictaion(), "licence方式获取token失败" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, MyApp.getApplictaion());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = FileUtil.getSaveFile(MyApp.getApplictaion()).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
                    }
                }
            }
        }
    }


    private void recIDCard(String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(20);

        OCR.getInstance().recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
                    RxToast.success(MyApp.getApplictaion(), "识别成功!", Toast.LENGTH_SHORT).show();
                    mTet_sex_natural.setText(result.getGender().toString());
                    mTet_parties_concerned_natural.setText(result.getName().toString());
                    mTet_card_natural.setText(result.getIdNumber().toString());
                    mTet_address_natural.setText(result.getAddress().toString());
                }
            }

            @Override
            public void onError(OCRError error) {
                RxToast.error(MyApp.getApplictaion(), "证件识别失败" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 释放内存资源
        if (OCR.getInstance().hasGotToken()) {

            OCR.getInstance().release();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_prin:      //打印
                boolean installApp = RxAppUtils.isInstallApp(MyApp.getApplictaion(), "com.dynamixsoftware.printershare");
                if (installApp) {
                    if (mRb_natural_person.isChecked()) {    //当事人为自然人


                        final String partiesConcernedNatural = mTet_parties_concerned_natural.getText().toString().trim();
                        final String sexNatural = mTet_sex_natural.getText().toString().trim();
                        final String tetCardNatural = mTet_card_natural.getText().toString().trim();
                        final String tetPhoneNatural = mTet_phone_natural.getText().toString().trim();
                        final String tetAddressNatural = mTet_address_natural.getText().toString().trim();
                        if (TextUtils.isEmpty(partiesConcernedNatural) && TextUtils.isEmpty(sexNatural)
                                && TextUtils.isEmpty(tetCardNatural) && TextUtils.isEmpty(tetPhoneNatural)
                                && TextUtils.isEmpty(tetAddressNatural)) {
                            RxToast.warning(MyApp.getApplictaion(), "填入信息不能有空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mNaturalInfo = "<p>当事人:<u>&nbsp;" + partiesConcernedNatural + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;性别: <u>&nbsp;" + sexNatural + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;身份证号:\n" +
                                "    <u>&nbsp;" + tetCardNatural + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;电话:\n" +
                                "    <u>&nbsp;" + tetPhoneNatural + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;住址:\n" +
                                "    <u>&nbsp;" + tetAddressNatural + "&nbsp;</u>\n" +
                                "</p>";
                        //---------------------------------------------------


                    } else {
                        String partiesConcernedOther = mTet_parties_concerned_other.getText().toString().trim();
                        String positionOther = mTet_position_other.getText().toString().trim();
                        String phoneOther = mTet_phone_other.getText().toString().trim();
                        String representativeOther = mTet_representative_other.getText().toString().trim();
                        String creditCodeOher = mTet_credit_code_oher.getText().toString().trim();
                        String addressOther = mTet_address_other.getText().toString().trim();

                        if (TextUtils.isEmpty(partiesConcernedOther) && TextUtils.isEmpty(positionOther)
                                && TextUtils.isEmpty(phoneOther) && TextUtils.isEmpty(representativeOther)
                                && TextUtils.isEmpty(creditCodeOher) && TextUtils.isEmpty(addressOther)) {
                            RxToast.warning(MyApp.getApplictaion(), "填入信息不能有空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mNaturalInfo = "<p>当事人:<u>&nbsp;" + partiesConcernedOther + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;职务: <u>&nbsp;" + positionOther + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;电话:\n" +
                                "    <u>&nbsp;" + phoneOther + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;法定代表人:\n" +
                                "    <u>&nbsp;" + representativeOther + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;统一社会信用代码:\n" +
                                "    <u>&nbsp;" + creditCodeOher + "&nbsp;</u>\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;住所:<u>&nbsp;" + addressOther + "&nbsp;</u>" +
                                "</p>";

                    }


                    final String illegalFacts = mEt_illegal_facts.getText().toString().trim();
                    final String legalProvisions1 = mEt_legal_provisions1.getText().toString().trim();
                    final String legalProvisions2 = mEt_legal_provisions2.getText().toString().trim();
                    final String contact = mTet_contact.getText().toString().trim();
                    final String phone = mTet_phone.getText().toString().trim();
                    final String addr = mTet_addr.getText().toString().trim();
                    if (!TextUtils.isEmpty(illegalFacts)
                            && !TextUtils.isEmpty(legalProvisions1) && !TextUtils.isEmpty(legalProvisions2)
                            && !TextUtils.isEmpty(contact) && !TextUtils.isEmpty(phone)
                            && !TextUtils.isEmpty(addr)) {
                        //写文件html
                        MyApp.getGlobalThreadPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                writeHtml(illegalFacts, legalProvisions1, legalProvisions2, contact, phone, addr);
                            }
                        });
                        ComponentName comp = new ComponentName("com.dynamixsoftware.printershare", "com.dynamixsoftware.printershare.ActivityWeb");
                        Intent intent = new Intent();
                        intent.setComponent(comp);
                        intent.setAction("android.intent.action.VIEW");
                        intent.setType("text/html");
                        intent.setData(Uri.parse("file:///" + Environment.getExternalStorageDirectory().getPath() + "/oking/print/temp1.html"));
                        startActivity(intent);
                    } else {
                        RxToast.warning(MyApp.getApplictaion(), "填入信息不能有空", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    if (mRxDialogLoading == null) {

                        mRxDialogLoading = new RxDialogLoading(getActivity(), false, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                dialogInterface.cancel();
                            }
                        });
                        mRxDialogLoading.setLoadingText("正在解压插件...");
                    }
                    mRxDialogLoading.show();
                    MyApp.getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            final File assetFileToCacheDir = Utils.getAssetFileToCacheDir("PrinterShare.apk");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mRxDialogLoading.cancel();
                                    RxAppUtils.InstallAPK(MyApp.getApplictaion(), assetFileToCacheDir.getPath());
                                }
                            });
                        }
                    });


                }

                break;
            default:
                break;
        }
    }


    private void writeHtml(String illegalFacts, String legalProvisions1, String legalProvisions2, String contact, String phone, String addr) {
        File destDir = new File(Environment.getExternalStorageDirectory().getPath() + "/oking/print/temp1.html");
        String contetnt = "<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                "\n" +
                "\n" +
                "    <style>\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1 align=\"center\">水行政责令停止违法行为通知书</h1>\n" +
                "<p align=\"right\">x水当罚字[&nbsp;]第&nbsp;&nbsp;号</p>\n" +
                mNaturalInfo +
                "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;据初步调查,你(单位)&nbsp; <u>&nbsp;" + illegalFacts + "&nbsp;</u>&nbsp;涉嫌违反了 &nbsp;<u>&nbsp;" + legalProvisions1 + "&nbsp;</u>&nbsp;\n" +
                "    的规定,现根据:<u>&nbsp;" + legalProvisions2 + "&nbsp;</u>的规定,责令你(单位)立即停止违法行为,听后处理。</p>\n" +
                "\n" +
                "<p align=\"right\">" + mMYear + "年" + mMMonth + "月" + mMDay + "日&nbsp;&nbsp;</p>\n" +
                "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联系人:<u>&nbsp;" + contact + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联系电话:&nbsp;<u>&nbsp;" + phone + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联系地址:&nbsp;<u>&nbsp;" + addr + "&nbsp;</u></p>\n" +
                "</body>\n" +
                "</html>";
        RxFileUtils.writeFileFromString(destDir, contetnt, false);
    }
}
