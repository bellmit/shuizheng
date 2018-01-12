package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class BaseSearchFragment extends BaseFragment {


    public BaseSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base_search, container, false);

        Button backBtn = (Button) rootView.findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseSearchFragment.this.getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return rootView;
    }

    @Override
    public void initView(View rootView) {

    }

}
