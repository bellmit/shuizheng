package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.api.maps.offlinemap.OfflineMapStatus;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.OfflineMapRecyAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.View.DividerItemDecoration;
import gd.water.oking.com.cn.wateradministration_gd.bean.OfflineMapDownload;
import gd.water.oking.com.cn.wateradministration_gd.main.MainActivity;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfflineMapManagerFragment extends BaseFragment {

    private ArrayList<OfflineMapCity> cityList;
    private ArrayList<OfflineMapDownload> offlineMapDownloads = new ArrayList<>();
    private OfflineMapManager aMapManager;

    private RecyclerView offlineMap_ry;
    private Button checkUpdateBtn;
    private OfflineMapRecyAdapter mOfflineMapRecyAdapter;

    public OfflineMapManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offline_map_manager, container, false);
    }

    @Override
    public void initView(View rootView) {
        MapsInitializer.sdcardDir = Environment.getExternalStorageDirectory() + "/oking/amap";

        aMapManager = new OfflineMapManager(getContext(), new OfflineMapManager.OfflineMapDownloadListener() {
            @Override
            public void onDownload(int i, int i1, String s) {

                for (int j = 0; j < offlineMapDownloads.size(); j++) {
                    OfflineMapDownload omp = offlineMapDownloads.get(j);
                    if (omp.getCity().getCity().equals(s)) {

                        omp.setProgress(i1);

                        if (i == OfflineMapStatus.LOADING) {
                            omp.setState(OfflineMapDownload.OnDownload);
                        } else if (i == OfflineMapStatus.WAITING) {
                            omp.setState(OfflineMapDownload.Waiting);
                        } else if (i == OfflineMapStatus.UNZIP) {
                            omp.setState(OfflineMapDownload.OnUnZIP);
                        } else if (i == OfflineMapStatus.SUCCESS) {
                            omp.setState(OfflineMapDownload.Normal);
                        } else if (i == OfflineMapStatus.ERROR ||
                                i == OfflineMapStatus.EXCEPTION_NETWORK_LOADING ||
                                i == OfflineMapStatus.EXCEPTION_SDCARD ||
                                i == OfflineMapStatus.EXCEPTION_AMAP) {
                            omp.setState(OfflineMapDownload.Error);
                        }

                        mOfflineMapRecyAdapter.notifyDataSetChanged();

                        break;
                    }
                }
            }

            @Override
            public void onCheckUpdate(boolean b, String s) {
                if (b) {
                    Log.i("OfflineMapManager", "onCheckUpdate>>>>HasNew>>>>" + s);
                    for (int i = 0; i < offlineMapDownloads.size(); i++) {
                        OfflineMapDownload omp = offlineMapDownloads.get(i);
                        if (omp.getCity().getCity().equals(s)) {
                            omp.setState(OfflineMapDownload.NewVersionOrUnDownload);
                            break;
                        }
                    }

                    mOfflineMapRecyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onRemove(boolean b, String s, String s1) {

            }
        });

        cityList = aMapManager.getItemByProvinceName(MainActivity.Area).getCityList();
        setOfflineMapDownloads();

        offlineMap_ry = rootView.findViewById(R.id.offlineMap_listView);
        offlineMap_ry.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        offlineMap_ry.setItemAnimator(new DefaultItemAnimator());
        offlineMap_ry.addItemDecoration(new DividerItemDecoration(MyApp.getApplictaion(), 1));
        mOfflineMapRecyAdapter = new OfflineMapRecyAdapter(R.layout.list_item_offlinemaps, offlineMapDownloads);
        mOfflineMapRecyAdapter.openLoadAnimation();
        offlineMap_ry.setAdapter(mOfflineMapRecyAdapter);
        mOfflineMapRecyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                downloadOffLoadMap(offlineMapDownloads.get(position).getCity());
            }
        });

        checkUpdateBtn = (Button) rootView.findViewById(R.id.checkUpdate_btn);
        checkUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadOffLoadMap();
            }
        });
    }

    private void setOfflineMapDownloads() {
        for (int i = 0; i < cityList.size(); i++) {
            if (offlineMapDownloads != null) {
                OfflineMapDownload omp = new OfflineMapDownload(cityList.get(i), 100, OfflineMapDownload.Normal);
                offlineMapDownloads.add(omp);
            }
        }

        updateOffLoadMap();
    }

    private void downloadOffLoadMap() {

        try {
            if (cityList != null) {
                for (int i = 0; i < cityList.size(); i++) {
                    aMapManager.downloadByCityName(cityList.get(i).getCity());
                }
            }
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    private void downloadOffLoadMap(OfflineMapCity city) {

        try {
            if (city != null) {
                aMapManager.downloadByCityName(city.getCity());
            }
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    private void updateOffLoadMap() {

        try {
            if (cityList != null) {
                for (int i = 0; i < cityList.size(); i++) {
                    aMapManager.updateOfflineCityByName(cityList.get(i).getCity());
                }
            }
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }
}
