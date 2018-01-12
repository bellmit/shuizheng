package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vondear.rxtools.view.RxToast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.ContactsAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Dept;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends BaseFragment {


    private PullToRefreshListView mContactsListView;
    private int limit = 10;
    private int offset = 0;
    private List<Dept.RowsBean> mRows;
    private ContactsAdapter mContactsAdapter;
    private long mTimeMillis;
    private FragmentActivity mActivity;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void initView(View rootView) {

        mContactsListView = (PullToRefreshListView) rootView.findViewById(R.id.contacts_listView);

        mContactsListView.setScrollingWhileRefreshingEnabled(true);
        //mPullRefreshListView.getMode();//得到模式
        //上下都可以刷新的模式。这里有两个选择：Mode.PULL_FROM_START，Mode.BOTH，PULL_FROM_END
        mContactsListView.setMode(PullToRefreshBase.Mode.BOTH);

        mContactsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //设置下拉时显示的日期和时间
                String label = DateUtils.formatDateTime(MyApp.getApplictaion(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // 更新显示的label
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                long timeMillis = System.currentTimeMillis();
                if (timeMillis - mTimeMillis > 5000) {
                    getNetData();
                } else {
//                    解决调用onRefreshComplete无效（时间太短）
                    mContactsListView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mContactsListView.onRefreshComplete();

                        }
                    }, 1000);
                    RxToast.warning(MyApp.getApplictaion(), "数据刷新太频繁了,请稍后再试", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                getMoreData();
            }
        });

        mContactsListView.onRefreshComplete();
        getNetData();
    }

    //获取更多
    private void getMoreData() {
        offset += 10;
        RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/user/getUserByDeptIdForAndroid?limit=" + limit + "&offset=" + offset);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result!=null){

                    RxToast.success(MyApp.getApplictaion(), "加载更多成功", Toast.LENGTH_SHORT).show();
                    mContactsListView.onRefreshComplete();
                    Gson gson = new Gson();
                    Dept dept = gson.fromJson(result, Dept.class);
                    mRows.addAll(dept.getRows());
//                ContactsAdapter contactsAdapter = new ContactsAdapter(mRows);
//                mContactsListView.setAdapter(contactsAdapter);
                    mContactsAdapter.notifyDataSetChanged();
                }else {
                    RxToast.success(MyApp.getApplictaion(),"服务器已没有更多数据",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mContactsListView.onRefreshComplete();
                RxToast.error(MyApp.getApplictaion(), "加载更多失败" + ex.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void getNetData() {

        offset = 0;
        RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/user/getUserByDeptIdForAndroid?limit=" + limit + "&offset=" + offset);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mTimeMillis = System.currentTimeMillis();
                RxToast.success(MyApp.getApplictaion(), "数据获取成功", Toast.LENGTH_SHORT).show();
                mContactsListView.onRefreshComplete();
                Gson gson = new Gson();
                Dept dept = gson.fromJson(result, Dept.class);
                mRows = dept.getRows();
                if (mContactsAdapter==null){

                    mContactsAdapter = new ContactsAdapter(mActivity,mRows);
                }
                mContactsListView.setAdapter(mContactsAdapter);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mContactsListView.onRefreshComplete();
                RxToast.error(MyApp.getApplictaion(), "获取数据失败" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }
}
