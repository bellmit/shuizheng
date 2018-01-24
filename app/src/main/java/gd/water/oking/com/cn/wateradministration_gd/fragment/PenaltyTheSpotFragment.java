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
import android.widget.CheckBox;
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
 * 当场处罚决定书
 */
public class PenaltyTheSpotFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE_CAMERA = 102;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private TextView mTv_time;
    private String mParam2;
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
    private TextInputEditText mTet_punishment_place_other;
    private Button mBt_idcard;
    private Button mBt_prin;
    private RxDialogLoading mRxDialogLoading;
    private EditText mEt_illegal_facts;
    private EditText mEt_legal_provisions;
    private EditText mEt_legal_provisions2;
    private CheckBox mCb_warning;
    private CheckBox mCb_fine;
    private EditText mTv_amount;
    private String fineStr;
    private String paymentFormdStr;
    private CheckBox mCb_spot;
    private CheckBox mCb_within_time;
    private EditText mEt_year;
    private EditText mEt_month;
    private EditText mEt_day;
    private EditText mEt_banknum;
    private TextInputEditText mTet_enforcement1;
    private TextInputEditText mTet_enforcement2;
    private int mMYear;
    private int mMMonth;
    private int mMDay;
    private TextInputEditText mTet_addr;
    private TextInputEditText mTet_sign_people;
    private TextInputEditText mTet_sign_time;
    private String mNaturalInfo;

    public PenaltyTheSpotFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PenaltyTheSpotFragment newInstance(String param1, String param2) {
        PenaltyTheSpotFragment fragment = new PenaltyTheSpotFragment();
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
        return inflater.inflate(R.layout.fragment_penalty_the_spot, container, false);
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
        mTet_punishment_place_other = rootView.findViewById(R.id.tet_punishment_place_other);
        mEt_illegal_facts = rootView.findViewById(R.id.et_illegal_facts);
        mEt_legal_provisions = rootView.findViewById(R.id.et_legal_provisions);
        mEt_legal_provisions2 = rootView.findViewById(R.id.et_legal_provisions2);
        mTv_amount = rootView.findViewById(R.id.et_amount);
        mCb_warning = rootView.findViewById(R.id.cb_warning);
        mCb_fine = rootView.findViewById(R.id.cb_fine);
        mCb_spot = rootView.findViewById(R.id.cb_spot);
        mCb_within_time = rootView.findViewById(R.id.cb_within_time);
        mEt_year = rootView.findViewById(R.id.et_year);
        mEt_month = rootView.findViewById(R.id.et_month);
        mEt_day = rootView.findViewById(R.id.et_day);
        mEt_banknum = rootView.findViewById(R.id.et_banknum);
        mTet_enforcement1 = rootView.findViewById(R.id.tet_enforcement1);
        mTet_enforcement2 = rootView.findViewById(R.id.tet_enforcement2);
        mTet_addr = rootView.findViewById(R.id.tet_addr);
        mTet_sign_people = rootView.findViewById(R.id.tet_sign_people);
        mTet_sign_time = rootView.findViewById(R.id.tet_sign_time);
        mBt_idcard = rootView.findViewById(R.id.bt_idcard);
        mBt_prin = rootView.findViewById(R.id.bt_prin);

        initData();
        iniListener();
    }

    private void iniListener() {
        mRb_natural_person.setOnCheckedChangeListener(this);
        mCb_warning.setOnCheckedChangeListener(this);
        mCb_spot.setOnCheckedChangeListener(this);
        mCb_within_time.setOnCheckedChangeListener(this);
        mCb_fine.setOnCheckedChangeListener(this);
        mBt_prin.setOnClickListener(this);
        mBt_idcard.setOnClickListener(this);
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

    private void initData() {

        Utils.setEditTextInhibitInputSpace(mTet_parties_concerned_natural);
        Utils.setEditTextInhibitInputSpeChat(mTet_parties_concerned_natural);

        Utils.setEditTextInhibitInputSpace(mTet_address_natural);
        Utils.setEditTextInhibitInputSpeChat(mTet_address_natural);

        Utils.setEditTextInhibitInputSpace(mTet_parties_concerned_other);
        Utils.setEditTextInhibitInputSpeChat(mTet_parties_concerned_other);

          Utils.setEditTextInhibitInputSpace(mTet_position_other);
        Utils.setEditTextInhibitInputSpeChat(mTet_position_other);

        Utils.setEditTextInhibitInputSpace(mTet_representative_other);
        Utils.setEditTextInhibitInputSpeChat(mTet_representative_other);

        Utils.setEditTextInhibitInputSpace(mTet_address_other);
        Utils.setEditTextInhibitInputSpeChat(mTet_address_other);

        Utils.setEditTextInhibitInputSpace(mTet_punishment_place_other);
        Utils.setEditTextInhibitInputSpeChat(mTet_punishment_place_other);

        Utils.setEditTextInhibitInputSpace(mTet_enforcement1);
        Utils.setEditTextInhibitInputSpeChat(mTet_enforcement1);

        Utils.setEditTextInhibitInputSpace(mTet_enforcement2);
        Utils.setEditTextInhibitInputSpeChat(mTet_enforcement2);

        Utils.setEditTextInhibitInputSpace(mTet_addr);
        Utils.setEditTextInhibitInputSpeChat(mTet_addr);

        Utils.setEditTextInhibitInputSpace(mTet_sign_people);
        Utils.setEditTextInhibitInputSpeChat(mTet_sign_people);

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

    private boolean checkTokenStatus() {
        if (!DefaultContants.HASGOTTOKEN) {
            RxToast.warning(MyApp.getApplictaion(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return DefaultContants.HASGOTTOKEN;
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
            case R.id.bt_idcard:
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
                break;
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
                        String punishmentPlaceOther = mTet_punishment_place_other.getText().toString().trim();

                        if (TextUtils.isEmpty(partiesConcernedOther) && TextUtils.isEmpty(positionOther)
                                && TextUtils.isEmpty(phoneOther) && TextUtils.isEmpty(representativeOther)
                                && TextUtils.isEmpty(creditCodeOher) && TextUtils.isEmpty(addressOther) && TextUtils.isEmpty(punishmentPlaceOther)) {
                            RxToast.warning(MyApp.getApplictaion(), "填入信息不能有空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mNaturalInfo = "<p>当事人:<u>&nbsp;" + partiesConcernedOther + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;职务: <u>&nbsp;" + positionOther + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;电话:\n" +
                                "    <u>&nbsp;" + phoneOther + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;法定代表人:\n" +
                                "    <u>&nbsp;" + representativeOther + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;统一社会信用代码:\n" +
                                "    <u>&nbsp;" + creditCodeOher + "&nbsp;</u>\n" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;住所:<u>&nbsp;" + addressOther + "&nbsp;</u>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;处罚地点:<u>&nbsp;" + punishmentPlaceOther + "&nbsp;</u>" +
                                "</p>";

                    }
                    final String etillegalFacts = mEt_illegal_facts.getText().toString().trim();
                    final String etLegalProvisions1 = mEt_legal_provisions.getText().toString().trim();
                    final String etLegalProvisions2 = mEt_legal_provisions2.getText().toString().trim();
                    String amount = mTv_amount.getText().toString().trim();
                    String banknum = mEt_banknum.getText().toString().trim();
                    final String enforcement1 = mTet_enforcement1.getText().toString().trim();
                    final String enforcement2 = mTet_enforcement2.getText().toString().trim();
                    final String addr = mTet_addr.getText().toString().trim();
                    final String signPeople = mTet_sign_people.getText().toString().trim();
                    final String signTime = mTet_sign_time.getText().toString().trim();
                    String year = mEt_year.getText().toString().trim();
                    String month = mEt_month.getText().toString().trim();
                    String day = mEt_day.getText().toString().trim();
                    if (mCb_warning.isChecked()) {

                        fineStr = "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"checkbox\" value=\"1\"checked = true/>1.警告;<input type=\"checkbox\" value=\"2\"/>2.罚款&nbsp;<u>&nbsp;&nbsp;</u>&nbsp;元(金额大写)。</p>";
                    }

                    if (mCb_fine.isChecked()) {
                        if (!TextUtils.isEmpty(amount)){

                            fineStr = "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"checkbox\" value=\"1\"/>1.警告;<input type=\"checkbox\" value=\"2\"checked = true/>2.罚款&nbsp;<u>&nbsp;" + amount + "&nbsp;</u>&nbsp;元(金额大写)。</p>";
                        }else {

                            RxToast.warning(MyApp.getApplictaion(), "罚款金额不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }

                    if (mCb_spot.isChecked()) {
                        paymentFormdStr = "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;收款形式:<input type=\"checkbox\" value=\"1\"checked = true/>(1)当场收缴;&nbsp;<input type=\"checkbox\" value=\"1\"/>(2)限于<u>&nbsp;&nbsp;&nbsp;</u>年<u>&nbsp;&nbsp;&nbsp;</u>月<u>&nbsp;&nbsp;&nbsp;</u>日前将罚款交至(银行名称及账户、帐号)&nbsp;<u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>&nbsp;逾期不缴的，每日按罚款数额的3%加处罚款。如不服本决定，可在收到本决定书之日起60日内向（同级人民政府或上一级水行政主管部门）申请行政复议；也可以在收到本决定之日起六个月内直接向人民法院起诉。复议和起诉期间本决定不停止执行；逾期不申请复议也不起诉，又不履行本决定的，本机关将依法申请人民法院强制执行。</p>";
                    }

                    if (mCb_within_time.isChecked()) {
                        if (!TextUtils.isEmpty(year)&&!TextUtils.isEmpty(month)&&!TextUtils.isEmpty(day)){

                            paymentFormdStr = "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;收款形式:<input type=\"checkbox\" value=\"1\"/>(1)当场收缴;&nbsp;<input type=\"checkbox\" value=\"1\"checked = true/>(2)限于<u>&nbsp;" + year + "&nbsp;</u>年<u>&nbsp;" + month + "&nbsp;</u>月<u>&nbsp;" + day + "&nbsp;</u>日前将罚款交至(银行名称及账户、帐号)&nbsp;<u>&nbsp;" + banknum + "&nbsp;</u>&nbsp;逾期不缴的，每日按罚款数额的3%加处罚款。如不服本决定，可在收到本决定书之日起60日内向（同级人民政府或上一级水行政主管部门）申请行政复议；也可以在收到本决定之日起六个月内直接向人民法院起诉。复议和起诉期间本决定不停止执行；逾期不申请复议也不起诉，又不履行本决定的，本机关将依法申请人民法院强制执行。</p>";
                        }else {
                            RxToast.warning(MyApp.getApplictaion(), "年月日不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }

                    if (!TextUtils.isEmpty(etillegalFacts)
                            && !TextUtils.isEmpty(etLegalProvisions1) && !TextUtils.isEmpty(etLegalProvisions2)
                            && !TextUtils.isEmpty(enforcement1) && !TextUtils.isEmpty(enforcement2)
                            && !TextUtils.isEmpty(addr) && !TextUtils.isEmpty(signPeople)
                            && !TextUtils.isEmpty(signTime)) {
                        //写文件html
                        MyApp.getGlobalThreadPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                writeHtml(etillegalFacts, etLegalProvisions1, etLegalProvisions2, enforcement1, enforcement2, addr, signPeople, signTime);
                            }
                        });
                        ComponentName comp = new ComponentName("com.dynamixsoftware.printershare", "com.dynamixsoftware.printershare.ActivityWeb");
                        Intent intent = new Intent();
                        intent.setComponent(comp);
                        intent.setAction("android.intent.action.VIEW");
                        intent.setType("text/html");
                        intent.setData(Uri.parse("file:///" + Environment.getExternalStorageDirectory().getPath() + "/oking/print/temp2.html"));
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

    private void writeHtml(String etillegalFacts, String etLegalProvisions1, String etLegalProvisions2, String enforcement1, String enforcement2, String addr, String signPeople, String signTime) {
        File destDir = new File(Environment.getExternalStorageDirectory().getPath() + "/oking/print/temp2.html");
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
                "<h1 align=\"center\">水行政当场处罚决定书</h1>\n" +
                "<p align=\"right\">x水当罚字[&nbsp;]第&nbsp;&nbsp;号</p>\n" +
                mNaturalInfo +
                "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当事人&nbsp; <u>&nbsp;" + etillegalFacts + "&nbsp;</u>&nbsp;违反了 &nbsp;<u>&nbsp;" + etLegalProvisions1 + "&nbsp;</u>的规定，现根据《行政处罚法》第三十三条和&nbsp;<u>&nbsp;" + etLegalProvisions2 +
                "&nbsp;</u>的规定,决定给予如下处罚:</p>\n" + fineStr + paymentFormdStr +
                "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;执法人员（姓名及执法证号）:&nbsp;<u>&nbsp;" + enforcement1 + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;执法人员（姓名及执法证号）:&nbsp;<u>&nbsp;" + enforcement2 + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联系地址:&nbsp;\n" +
                "    <u>&nbsp;" + addr + "&nbsp;</u>\n" +
                "</p>\n" +
                "<p align=\"right\">" + mMYear + "年" + mMMonth + "月" + mMDay + "日&nbsp;&nbsp;</p>\n" +
                "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;签收人:<u>&nbsp;" + signPeople + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;签收时间:&nbsp;<u>&nbsp;" + signTime + "&nbsp;</u></p>\n" +
                "</body>\n" +
                "</html>";
        RxFileUtils.writeFileFromString(destDir, contetnt, false);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.rb_natural_person:
                if (b) {
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
                    mTet_punishment_place_other.setEnabled(false);
                    mTet_punishment_place_other.setText("");
                    mBt_idcard.setEnabled(true);
                } else {
                    mTet_parties_concerned_natural.setEnabled(false);
                    mTet_parties_concerned_natural.setText("");
                    mTet_parties_concerned_other.setEnabled(true);
                    mTet_sex_natural.setEnabled(false);
                    mTet_sex_natural.setText("");
                    mTet_card_natural.setEnabled(false);
                    mTet_card_natural.setText("");
                    mTet_phone_natural.setEnabled(false);
                    mTet_phone_natural.setText("");
                    mTet_phone_other.setEnabled(true);
                    mTet_address_natural.setEnabled(false);
                    mTet_address_natural.setText("");
                    mTet_address_other.setEnabled(true);
                    mTet_position_other.setEnabled(true);
                    mTet_representative_other.setEnabled(true);
                    mTet_credit_code_oher.setEnabled(true);
                    mTet_punishment_place_other.setEnabled(true);
                    mBt_idcard.setEnabled(false);

                }
                break;
            case R.id.cb_warning:
                if (b) {
                    mTv_amount.setText("");
                    mTv_amount.setEnabled(false);
                    mCb_fine.setChecked(false);
                }
                break;
            case R.id.cb_spot:
                if (b) {
                    mEt_year.setText("");
                    mEt_year.setEnabled(false);
                    mEt_month.setText("");
                    mEt_month.setEnabled(false);
                    mEt_day.setText("");
                    mEt_day.setEnabled(false);
                    mEt_banknum.setText("");
                    mEt_banknum.setEnabled(false);
                    mCb_within_time.setChecked(false);
                }
                break;
            case R.id.cb_within_time:
                if (b) {
                    mEt_year.setEnabled(true);
                    mEt_month.setEnabled(true);
                    mEt_day.setEnabled(true);
                    mEt_banknum.setEnabled(true);
                    mCb_spot.setChecked(false);
                }
                break;
            case R.id.cb_fine:
                if (b) {
                    mTv_amount.setEnabled(true);
                    mCb_warning.setChecked(false);
                }
                break;
            default:
                break;
        }
    }
}
