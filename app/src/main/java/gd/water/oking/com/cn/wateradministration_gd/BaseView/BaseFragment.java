package gd.water.oking.com.cn.wateradministration_gd.BaseView;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

public abstract class BaseFragment extends Fragment implements Serializable {



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = createView(inflater, container, savedInstanceState);
        initView(v);

        return v;
    }

    /**
     * 用于初始化Fragment的View
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 用于初始化Fragment的界面上的控件
     *
     * @param rootView Fragment的View对象
     */
    public abstract void initView(View rootView);




    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

}
