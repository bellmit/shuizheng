package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;

/**
 * 执法流程指引
 */
public class LawEnforcementProcessGuidanceFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RadioButton mRb_punishment_procedures;
    private RadioButton mRb_compulsory_measures;
    private RadioButton mRb_enforcement_program;
//    private PunishmentProceduresFragment mPunishmentProceduresFragment;
//    private AdministrativeEnforcementProceduresFragment mAdministrativeEnforcementProceduresFragment;
    private EnforcementProgramFragment mEnforcementProgramFragment;
    private TestFragment mTestFragment;
    private TestFragment2 mTestFragment2;


    public LawEnforcementProcessGuidanceFragment() {
        // Required empty public constructor
    }

    public static LawEnforcementProcessGuidanceFragment newInstance(String param1, String param2) {
        LawEnforcementProcessGuidanceFragment fragment = new LawEnforcementProcessGuidanceFragment();
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
        View inflate = inflater.inflate(R.layout.fragment_law_enforcement_process_guidance, container, false);
        return inflate;

    }

    @Override
    public void initView(View rootView) {

        mRb_punishment_procedures = rootView.findViewById(R.id.rb_punishment_procedures);
        mRb_compulsory_measures = rootView.findViewById(R.id.rb_compulsory_measures);
        mRb_enforcement_program = rootView.findViewById(R.id.rb_enforcement_program);
        initData();
        setListener();

    }

    private void initData() {
//        if (mPunishmentProceduresFragment == null) {
//
//            mPunishmentProceduresFragment = PunishmentProceduresFragment.newInstance(null, null);
//        }
//       getChildFragmentManager().beginTransaction().replace(R.id.fl_law_enforcement_content, mPunishmentProceduresFragment).commit();

        if (mTestFragment==null){

            mTestFragment = TestFragment.newInstance(null, null);
        }
        getChildFragmentManager().beginTransaction().replace(R.id.fl_law_enforcement_content, mTestFragment).commit();

    }

    private void setListener() {
        mRb_punishment_procedures.setOnCheckedChangeListener(this);
        mRb_compulsory_measures.setOnCheckedChangeListener(this);
        mRb_enforcement_program.setOnCheckedChangeListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.rb_punishment_procedures:
                if (mRb_punishment_procedures.isChecked()) {
                    if (mTestFragment == null) {

                        mTestFragment = TestFragment.newInstance(null, null);
                    }
                    getChildFragmentManager().beginTransaction().replace(R.id.fl_law_enforcement_content, mTestFragment).commit();

                }
                break;
            case R.id.rb_compulsory_measures:
                if (mRb_compulsory_measures.isChecked()) {
//                    if (mAdministrativeEnforcementProceduresFragment == null) {
//
//                        mAdministrativeEnforcementProceduresFragment = AdministrativeEnforcementProceduresFragment.newInstance(null, null);
//                    }
//                    getChildFragmentManager().beginTransaction().replace(R.id.fl_law_enforcement_content, mAdministrativeEnforcementProceduresFragment).commit();


                    if (mTestFragment2==null){
                        mTestFragment2 = TestFragment2.newInstance(null, null);

                    }
                    getChildFragmentManager().beginTransaction().replace(R.id.fl_law_enforcement_content, mTestFragment2).commit();

                }
                break;
            case R.id.rb_enforcement_program:
                if (mRb_enforcement_program.isChecked()){

                    if (mEnforcementProgramFragment==null){

                        mEnforcementProgramFragment = EnforcementProgramFragment.newInstance(null, null);
                    }
                    getChildFragmentManager().beginTransaction().replace(R.id.fl_law_enforcement_content, mEnforcementProgramFragment).commit();
                }

                break;
            default:
                break;
        }

    }
}
