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
 * 执法规范
 */
public class LawEnforcementSpecificationFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RadioButton mRb_administrative_enforcement;
    private AdministrativeEnforcementFragment mAdministrativeEnforcementFragment;
    private View mInflate;
    private RadioButton mRb_language_specification;
    private RadioButton mRb_inspection_norms;
    private EnforcementLanguageSpecificationFragment mEnforcementLanguageSpecificationFragment;
    private EnforcementInspectionNormsFragment mEnforcementInspectionNormsFragment;


    public LawEnforcementSpecificationFragment() {
        // Required empty public constructor
    }

    public static LawEnforcementSpecificationFragment newInstance(String param1, String param2) {
        LawEnforcementSpecificationFragment fragment = new LawEnforcementSpecificationFragment();
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
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_law_enforcement_specification, container, false);
        }
        return mInflate;

    }

    @Override
    public void initView(View rootView) {
        mRb_administrative_enforcement = rootView.findViewById(R.id.rb_administrative_enforcement);
        mRb_language_specification = rootView.findViewById(R.id.rb_language_specification);
        mRb_inspection_norms = rootView.findViewById(R.id.rb_inspection_norms);
        initData();
        setListener();
    }

    private void setListener() {
        mRb_administrative_enforcement.setOnCheckedChangeListener(this);
        mRb_language_specification.setOnCheckedChangeListener(this);
        mRb_inspection_norms.setOnCheckedChangeListener(this);

    }

    private void initData() {
        if (mEnforcementInspectionNormsFragment ==null){
            mEnforcementInspectionNormsFragment = EnforcementInspectionNormsFragment.newInstance(null, null);

        }
        getChildFragmentManager().beginTransaction().replace(R.id.fl_administrative_content, mEnforcementInspectionNormsFragment).commit();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mInflate != null) {
            ViewGroup parent = (ViewGroup) mInflate.getParent();
            if (parent != null) {
                parent.removeView(mInflate);
            }

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.rb_administrative_enforcement:     //水行政执法禁令
                if (mRb_administrative_enforcement.isChecked()) {
                    if (mAdministrativeEnforcementFragment == null) {
                        mAdministrativeEnforcementFragment = AdministrativeEnforcementFragment.newInstance(null, null);
                    }
                    getChildFragmentManager().beginTransaction().replace(R.id.fl_administrative_content, mAdministrativeEnforcementFragment).commit();

                }
                break;
            case R.id.rb_language_specification:     //水行政执法用语规范
                if (mRb_language_specification.isChecked()) {
                    if (mEnforcementLanguageSpecificationFragment ==null){
                        mEnforcementLanguageSpecificationFragment = EnforcementLanguageSpecificationFragment.newInstance(null, null);
                    }
                    getChildFragmentManager().beginTransaction().replace(R.id.fl_administrative_content, mEnforcementLanguageSpecificationFragment).commit();

                }
                break;
            case R.id.rb_inspection_norms:     //水行政执法检查行为规范
                if (mRb_inspection_norms.isChecked()) {
                    if (mEnforcementInspectionNormsFragment ==null){
                        mEnforcementInspectionNormsFragment = EnforcementInspectionNormsFragment.newInstance(null, null);

                    }
                    getChildFragmentManager().beginTransaction().replace(R.id.fl_administrative_content, mEnforcementInspectionNormsFragment).commit();

                }
                break;
            default:
                break;
        }
    }
}
