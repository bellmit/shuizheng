package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.api.maps.offlinemap.OfflineMapStatus;

import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.OfflineMapDownload;
import gd.water.oking.com.cn.wateradministration_gd.main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfflineMapManagerFragment extends BaseFragment {

    private ArrayList<OfflineMapCity> cityList;
    private ArrayList<OfflineMapDownload> offlineMapDownloads = new ArrayList<>();
    private OfflineMapManager aMapManager;

    private ListView offlineMap_ListView;
    private Button checkUpdateBtn;
    private BaseAdapter adapter;

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
                            Log.i("OfflineMapManager", "onDownload>>>>Success>>>>" + s);
                            omp.setState(OfflineMapDownload.Normal);
                        } else if (i == OfflineMapStatus.ERROR ||
                                i == OfflineMapStatus.EXCEPTION_NETWORK_LOADING ||
                                i == OfflineMapStatus.EXCEPTION_SDCARD ||
                                i == OfflineMapStatus.EXCEPTION_AMAP) {
                            omp.setState(OfflineMapDownload.Error);
                        }

                        adapter.notifyDataSetChanged();

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

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onRemove(boolean b, String s, String s1) {

            }
        });

        cityList = aMapManager.getItemByProvinceName(MainActivity.Area).getCityList();
        setOfflineMapDownloads();

        offlineMap_ListView = (ListView) rootView.findViewById(R.id.offlineMap_listView);
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return offlineMapDownloads.size();
            }

            @Override
            public Object getItem(int position) {
                return offlineMapDownloads.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_offlinemaps, null);

                TextView cityName_tv = (TextView) view.findViewById(R.id.cityName_tv);
                TextView state_tv = (TextView) view.findViewById(R.id.state_tv);
                ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
                Button download_btn = (Button) view.findViewById(R.id.download_btn);
                download_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        downloadOffLoadMap(offlineMapDownloads.get(position).getCity());
                    }
                });

                cityName_tv.setText(offlineMapDownloads.get(position).getCity().getCity());
                progressBar.setProgress(offlineMapDownloads.get(position).getProgress());
                switch (offlineMapDownloads.get(position).getState()) {
                    case OfflineMapDownload.Normal:
                        progressBar.setVisibility(View.INVISIBLE);
                        download_btn.setVisibility(View.INVISIBLE);
                        break;
                    case OfflineMapDownload.OnDownload:
                        state_tv.setText("下载中：" + offlineMapDownloads.get(position).getProgress() + "%");
                        progressBar.setVisibility(View.VISIBLE);
                        download_btn.setVisibility(View.INVISIBLE);
                        break;
                    case OfflineMapDownload.OnUnZIP:
                        state_tv.setText("解压中：" + offlineMapDownloads.get(position).getProgress() + "%");
                        progressBar.setVisibility(View.VISIBLE);
                        download_btn.setVisibility(View.INVISIBLE);
                        break;
                    case OfflineMapDownload.NewVersionOrUnDownload:
                        progressBar.setVisibility(View.INVISIBLE);
                        download_btn.setVisibility(View.VISIBLE);
                        break;
                    case OfflineMapDownload.Waiting:
                        state_tv.setText("等待下载");
                        progressBar.setVisibility(View.INVISIBLE);
                        download_btn.setVisibility(View.INVISIBLE);
                        break;
                }

                return view;
            }
        };
        offlineMap_ListView.setAdapter(adapter);

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
