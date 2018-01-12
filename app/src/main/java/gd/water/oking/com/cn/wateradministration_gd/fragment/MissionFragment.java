package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolygonOptions;
import com.google.gson.reflect.TypeToken;
import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogLoading;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.ReceivivingExpandableListAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.View.SearchMissionEditText;
import gd.water.oking.com.cn.wateradministration_gd.bean.Mission;
import gd.water.oking.com.cn.wateradministration_gd.bean.MissionLog;
import gd.water.oking.com.cn.wateradministration_gd.bean.Point;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.http.GetMissionRecordFilePathParams;
import gd.water.oking.com.cn.wateradministration_gd.http.GetMissionRecordParams;
import gd.water.oking.com.cn.wateradministration_gd.http.SetMissionRecordParams;
import gd.water.oking.com.cn.wateradministration_gd.interfaces.MyCallBack;
import gd.water.oking.com.cn.wateradministration_gd.main.MainActivity;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.FileUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.LocalSqlite;

/**
 * A simple {@link BaseFragment} subclass.
 * <p>
 * 任务执行
 */
public class MissionFragment extends BaseFragment {
    private MyCallBack mMyCallBack;
    private MapView mapView;
    private AgentWeb mAgentWeb;
    private boolean isArcgis = false;

    private int selectItemIndex = -1;
    private ArrayList<Mission> allMissionList = MainActivity.missionList;
    private ArrayList<Mission> missionList = new ArrayList<>();
    private ReceivivingExpandableListAdapter missionListAdapter;
    private File root = Environment.getExternalStorageDirectory();
    private File mediaStorageDir = new File(root, "oking/mission_pic");
    private File mediaStorageSignDir = new File(root, "oking/mission_signature");
    private File mediaStorageVideoDir = new File(root, "oking/mission_video");


    private String defaultTaskID = "";

    private Runnable DialogDismissRunnable = new Runnable() {
        @Override
        public void run() {
            if (mRxDialogLoading != null) {
                mRxDialogLoading.dismiss();
            }
        }
    };
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case MainActivity.UPDATE_MISSION_LIST:
                    missionList.clear();
                    for (int i = 0; i < allMissionList.size(); i++) {
                        if (allMissionList.get(i).getStatus() == 3 || allMissionList.get(i).getStatus() == 4) {
                            missionList.add(allMissionList.get(i));
                        }
                    }

                    if (missionListAdapter != null) {
                        missionListAdapter.setDataList(missionList);
                    }

                    break;
            }
        }
    };
    private Handler handler;
    private ExpandableListView mMissionListView;
    private RxDialogLoading mRxDialogLoading;
    private RelativeLayout mRl_web;

    public MissionFragment() {
        // Required empty public constructor
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        MyApp.getApplictaion().registerReceiver(mReceiver, new IntentFilter(MainActivity.UPDATE_MISSION_LIST));

        missionList.clear();

        for (int i = 0; i < allMissionList.size(); i++) {
            if (allMissionList.get(i).getStatus() == 3 || allMissionList.get(i).getStatus() == 4) {
                missionList.add(allMissionList.get(i));
            }

        }

        final View rootView = inflater.inflate(R.layout.fragment_mission, container, false);
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        MyApp.getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HttpClient client = new DefaultHttpClient();
                client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
                HttpGet httpGet = new HttpGet("http://10.44.3.168:8399/arcgis/rest/services");
                try {
                    HttpResponse httpResponse = client.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        if (handler != null){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mapView.getMap().setMapType(AMap.MAP_TYPE_NORMAL);
                                    mapView.setVisibility(View.GONE);
                                    isArcgis = true;
                                }
                            });}
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

    @Override
    public void initView(View rootView) {
        mRl_web = (RelativeLayout) rootView.findViewById(R.id.rl_web);

        mMissionListView = (ExpandableListView) rootView.findViewById(R.id.mission_listView);

        missionListAdapter = new ReceivivingExpandableListAdapter(missionList, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mMissionListView.setAdapter(missionListAdapter);
        for (int i = 0; i < missionList.size(); i++) {
            if (missionList.get(i).getId().equals(defaultTaskID)&&mMissionListView!=null) {
                mMissionListView.setSelectedGroup(i);
                //展开选择的条目
                mMissionListView.expandGroup(i);
            }
        }

        //        //默认展开第一个分组
//        mMissionListView.expandGroup(0);


//展开某个分组时，并关闭其他分组。注意这里设置的是 ExpandListener
        mMissionListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < missionList.size(); i++) {
                    if (i != groupPosition) {
                        mMissionListView.collapseGroup(i); //收起某个指定的组
                    }
                }
                if (missionList.size() > groupPosition && groupPosition >= 0 && missionList.get(groupPosition) != null) {
                    //显示地图

                    String urlMap = DefaultContants.SERVER_HOST + "/arcgis/xcdgl/task_select_area.jsp?busiTaskId=" + missionList.get(groupPosition).getId();

                    DefaultContants.syncCookie(urlMap);

                    mAgentWeb = AgentWeb.with(MissionFragment.this)//传入Activity or Fragment
                            .setAgentWebParent(mRl_web, new RelativeLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                            .useDefaultIndicator()//
                            .setReceivedTitleCallback(new ChromeClientCallbackManager.ReceivedTitleCallback() {
                                @Override
                                public void onReceivedTitle(WebView webView, String s) {

                                }
                            }) //设置 Web 页面的 title 回调
                            .createAgentWeb()//
                            .ready()
                            .go(urlMap);



                    missionListAdapter.notifyDataSetInvalidated();

                    if (mapView != null) {
                        setMap(mapView.getMap(), missionList.get(groupPosition));
                    }
                }

            }
        });
        missionListAdapter.setOnBtnClickListener(new ReceivivingExpandableListAdapter.OnBtnClickListener() {
            @Override
            public void onItemRecordBtnClick(final int position) {
                final Mission mission = missionList.get(position);

                SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("logRecord", Context.MODE_PRIVATE);
                final String jsonStr = sharedPreferences.getString(missionList.get(position).getId(), "");


                if ("".equals(jsonStr)) {
                    //网络获取Log(尝试用http获取服务器的Log，获取不了再单机生成新Log)
                    getHttpMissionLog(position);


                } else {
                    final MissionLog log = DataUtil.praseJson(jsonStr, new TypeToken<MissionLog>() {

                    });
                    if (log.getMembers().size() == 1 || log.getMembers().size() == 0) {
                        log.setMembers(mission.getMembers());
                    }


                    FragmentTransaction ft = MissionFragment.this.getParentFragment().getChildFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    MissionRecordFragment f = new MissionRecordFragment();
                    f.setMission(mission);
                    f.setLog(log);
                    selectItemIndex = -1;
                    ft.replace(R.id.fragment_root, f).commit();


                }


            }
        });


        SearchMissionEditText searchMissionEditText = (SearchMissionEditText) rootView.findViewById(R.id.searchMission_editText);
        searchMissionEditText.setDataList(missionList);
        searchMissionEditText.setMissionListView(mMissionListView);

    }

    @Override
    public void onPause() {
        mapView.onPause();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        handler = new Handler();
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        MyApp.getApplictaion().unregisterReceiver(mReceiver);
        if (mAgentWeb!=null){

            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        mapView.onDestroy();
        super.onDestroyView();
    }


    private void getHttpMissionLog(int position) {

        final Mission mission = missionList.get(position);

        if (!DefaultContants.ISHTTPLOGIN) {
            getActivity().runOnUiThread(DialogDismissRunnable);

            if (mission.getStatus() == 5 || mission.getStatus() == 9) {
                RxToast.error(MyApp.getApplictaion(), "连接网络失败", Toast.LENGTH_SHORT).show();
            } else {
                MissionLog log = new MissionLog();
                log.setTask_id(mission.getId());
                log.setName(DefaultContants.CURRENTUSER.getUserId());
                log.setStatus(0);
                log.setMembers(mission.getMembers());

                FragmentTransaction ft = MissionFragment.this.getParentFragment().getChildFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                MissionRecordFragment f = new MissionRecordFragment();
                f.setMission(mission);
                f.setLog(log);
                selectItemIndex = -1;
                ft.replace(R.id.fragment_root, f).commit();
            }

            return;
        }


        mRxDialogLoading = new RxDialogLoading(getContext(), true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.cancel();
            }
        });
        mRxDialogLoading.setLoadingText("初始化数据中，请稍等...");
        mRxDialogLoading.show();

        GetMissionRecordParams params = new GetMissionRecordParams();
        params.task_id = mission.getId();
        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("GetMissionRecord", "onSuccess>>>>>>" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int count = object.getInt("total");
                    if (count > 0) {
                        final MissionLog log = DataUtil.praseJson(object.getJSONArray("rows").getJSONObject(0).toString(),
                                new TypeToken<MissionLog>() {
                                });
                        log.setMembers(mission.getMembers());

                        ///////////////////////////获取日志图片
                        final GetMissionRecordFilePathParams params = new GetMissionRecordFilePathParams();
                        params.log_id = log.getId();
                        params.type = 0;
                        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {

                                try {
                                    JSONArray paths = new JSONArray(result);
                                    for (int i = 0; i < paths.length(); i++) {
                                        String[] p = paths.getJSONObject(i).getString("path").split("/");
                                        String fileName = p[p.length - 1];

                                        if (!mediaStorageDir.exists()) {
                                            mediaStorageDir.mkdirs();
                                        }
                                        File file = new File(mediaStorageDir, fileName);
                                        log.getPhotoUriList().add(Uri.fromFile(file));
                                        if (!file.exists()) {
                                            //图片不存在，则下载图片
                                            String path = paths.getJSONObject(i).getString("path");
                                            RequestParams params = new RequestParams(DefaultContants.SERVER + '/' + path);
                                            //设置断点续传
                                            params.setAutoResume(true);
                                            File pic = new File(mediaStorageDir, fileName);
                                            params.setSaveFilePath(pic.getPath());
                                            x.http().get(params, new Callback.ProgressCallback<File>() {

                                                @Override
                                                public void onSuccess(File result) {
                                                    Log.i("DownloadPic", "onSuccess>>>>>>" + result.getPath());
//                                                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                                                    intent.setData(Uri.fromFile(result));
//                                                    MissionFragment.this.getContext().sendBroadcast(intent);

                                                    //通知日志界面更新
                                                    Intent uiIntent = new Intent(MainActivity.UPDATE_MISSIONLOG_UI_LIST);
                                                    getActivity().sendBroadcast(uiIntent);
                                                }

                                                @Override
                                                public void onError(Throwable ex, boolean isOnCallback) {

                                                }

                                                @Override
                                                public void onCancelled(CancelledException cex) {

                                                }

                                                @Override
                                                public void onFinished() {
                                                    Log.i("DownloadPic", "onFinished>>>>>>");
                                                }

                                                @Override
                                                public void onWaiting() {

                                                }

                                                @Override
                                                public void onStarted() {
                                                    Log.i("DownloadPic", "onStarted>>>>>>");
                                                }

                                                @Override
                                                public void onLoading(long total, long current, boolean isDownloading) {

                                                }
                                            });
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                ///////////////////////////获取签名图片
                                final GetMissionRecordFilePathParams params2 = new GetMissionRecordFilePathParams();
                                params2.log_id = log.getId();
                                params2.type = 1;
                                Callback.Cancelable cancelable2 = x.http().post(params2, new Callback.CommonCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        Log.i("GetMissionRecordPicPath", "onSuccess>>>>>>" + result);

                                        try {
                                            JSONArray paths = new JSONArray(result);
                                            for (int i = 0; i < paths.length(); i++) {
                                                String[] p = paths.getJSONObject(i).getString("path").split("/");
                                                String fileName = p[p.length - 1];

                                                if (!mediaStorageSignDir.exists()) {
                                                    mediaStorageSignDir.mkdirs();
                                                }

                                                File file = new File(mediaStorageSignDir, fileName);

                                                String url = paths.getJSONObject(i).getString("userid");
                                                for (int j = 0; j < log.getMembers().size(); j++) {
                                                    if (log.getMembers().get(j).getUserid().equals(url)) {
                                                        log.getMembers().get(j).setSignPic(Uri.fromFile(file));
                                                    }
                                                }

                                                if (!file.exists()) {
                                                    //图片不存在，则下载图片
                                                    String path = paths.getJSONObject(i).getString("path");
                                                    RequestParams params = new RequestParams(DefaultContants.SERVER + '/' + path);
                                                    //设置断点续传
                                                    params.setAutoResume(true);
                                                    File pic = new File(mediaStorageSignDir, fileName);
                                                    params.setSaveFilePath(pic.getPath());
                                                    x.http().get(params, new Callback.ProgressCallback<File>() {

                                                        @Override
                                                        public void onSuccess(File result) {
                                                            Log.i("DownloadPic", "onSuccess>>>>>>" + result.getPath());
//                                                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                                                            intent.setData(Uri.fromFile(result));
//                                                            MissionFragment.this.getContext().sendBroadcast(intent);
                                                        }

                                                        @Override
                                                        public void onError(Throwable ex, boolean isOnCallback) {

                                                        }

                                                        @Override
                                                        public void onCancelled(CancelledException cex) {

                                                        }

                                                        @Override
                                                        public void onFinished() {
                                                            Log.i("DownloadPic", "onFinished>>>>>>");
                                                        }

                                                        @Override
                                                        public void onWaiting() {

                                                        }

                                                        @Override
                                                        public void onStarted() {
                                                            Log.i("DownloadPic", "onStarted>>>>>>");
                                                        }

                                                        @Override
                                                        public void onLoading(long total, long current, boolean isDownloading) {

                                                        }
                                                    });
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        ///////////////////////////获取巡查视频
                                        final GetMissionRecordFilePathParams params2 = new GetMissionRecordFilePathParams();
                                        params2.log_id = log.getId();
                                        params2.type = 2;
                                        Callback.Cancelable cancelable2 = x.http().post(params2, new Callback.CommonCallback<String>() {

                                            @Override
                                            public void onSuccess(String result) {

                                                Log.i("GetMissionRecordPicPath", "onSuccess>>>>>>" + result);

                                                try {
                                                    JSONArray paths = new JSONArray(result);
                                                    for (int i = 0; i < paths.length(); i++) {
                                                        String[] p = paths.getJSONObject(i).getString("path").split("/");
                                                        String fileName = p[p.length - 1];

                                                        if (!mediaStorageVideoDir.exists()) {
                                                            mediaStorageVideoDir.mkdirs();
                                                        }

                                                        File file = new File(mediaStorageVideoDir, fileName);
                                                        log.getVideoUriList().add(Uri.fromFile(file));
                                                        if (!file.exists()) {
                                                            //视频不存在，则下载视频
                                                            String path = paths.getJSONObject(i).getString("path");
                                                            RequestParams params = new RequestParams(DefaultContants.SERVER + '/' + path);
                                                            //设置断点续传
                                                            params.setAutoResume(true);
                                                            File pic = new File(mediaStorageVideoDir, fileName);
                                                            params.setSaveFilePath(pic.getPath());
                                                            x.http().get(params, new Callback.ProgressCallback<File>() {

                                                                @Override
                                                                public void onSuccess(File result) {
                                                                    Log.i("DownloadVideo", "onSuccess>>>>>>" + result.getPath());
//                                                                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                                                                    intent.setData(Uri.fromFile(result));
//                                                                    MissionFragment.this.getContext().sendBroadcast(intent);

                                                                    //通知日志界面更新
                                                                    Intent uiIntent = new Intent(MainActivity.UPDATE_MISSIONLOG_UI_LIST);
                                                                    getActivity().sendBroadcast(uiIntent);
                                                                }

                                                                @Override
                                                                public void onError(Throwable ex, boolean isOnCallback) {

                                                                }

                                                                @Override
                                                                public void onCancelled(CancelledException cex) {

                                                                }

                                                                @Override
                                                                public void onFinished() {
                                                                    Log.i("DownloadVideo", "onFinished>>>>>>");
                                                                }

                                                                @Override
                                                                public void onWaiting() {

                                                                }

                                                                @Override
                                                                public void onStarted() {
                                                                    Log.i("DownloadVideo", "onStarted>>>>>>");
                                                                }

                                                                @Override
                                                                public void onLoading(long total, long current, boolean isDownloading) {

                                                                }
                                                            });
                                                        }
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                getActivity().runOnUiThread(DialogDismissRunnable);
                                                FragmentTransaction ft = MissionFragment.this.getParentFragment().getChildFragmentManager().beginTransaction();
                                                ft.addToBackStack(null);
                                                MissionRecordFragment f = new MissionRecordFragment();
                                                f.setMission(mission);
                                                f.setLog(log);
                                                selectItemIndex = -1;
                                                ft.replace(R.id.fragment_root, f).commit();
                                            }

                                            @Override
                                            public void onError(Throwable ex, boolean isOnCallback) {

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
                                    public void onError(Throwable ex, boolean isOnCallback) {

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
                            public void onError(Throwable ex, boolean isOnCallback) {

                            }

                            @Override
                            public void onCancelled(CancelledException cex) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    } else {
                        SetMissionRecordParams params = new SetMissionRecordParams();
                        params.mode = 0;
                        params.task_id = mission.getId();
                        params.name = DefaultContants.CURRENTUSER.getUserId();
                        params.status = 0;
                        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                Log.i("SetMissionRecord", "onSuccess>>>>>>" + result);
                                try {
                                    JSONObject object = new JSONObject(result);
                                    int code = object.getInt("code");
                                    if (code == 0) {
                                        getActivity().runOnUiThread(DialogDismissRunnable);
                                        MissionLog log = new MissionLog();
                                        log.setTask_id(mission.getId());
                                        log.setName(DefaultContants.CURRENTUSER.getUserId());
                                        log.setStatus(0);
                                        log.setMembers(mission.getMembers());

                                        FragmentTransaction ft = MissionFragment.this.getParentFragment().getChildFragmentManager().beginTransaction();
                                        ft.addToBackStack(null);
                                        MissionRecordFragment f = new MissionRecordFragment();
                                        f.setMission(mission);
                                        f.setLog(log);
                                        selectItemIndex = -1;
                                        ft.replace(R.id.fragment_root, f).commit();
                                    } else {
                                        Toast.makeText(getContext(), object.getString("msg"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {

                            }

                            @Override
                            public void onCancelled(CancelledException cex) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //http获取任务日志失败
                getActivity().runOnUiThread(DialogDismissRunnable);

                if (mission.getStatus() == 5 || mission.getStatus() == 9) {
                    Toast.makeText(getContext(), "连接网络失败", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("GetMissionRecord", "onError>>>>" + ex.toString());
                    MissionLog log = new MissionLog();
                    log.setTask_id(mission.getId());
                    log.setName(DefaultContants.CURRENTUSER.getUserId());
                    log.setStatus(0);
                    log.setMembers(mission.getMembers());

                    FragmentTransaction ft = MissionFragment.this.getParentFragment().getChildFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    MissionRecordFragment f = new MissionRecordFragment();
                    f.setMission(mission);
                    f.setLog(log);
                    selectItemIndex = -1;
                    ft.replace(R.id.fragment_root, f).commit();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void setMap(AMap map, Mission mission) {
        if (isArcgis && DefaultContants.ISHTTPLOGIN) {
            return;
        }

        mapView.setVisibility(View.VISIBLE);
        mRl_web.setVisibility(View.GONE);
        Cursor c = MyApp.localSqlite.select(LocalSqlite.AREA_TABLE, new String[]{"coordinate,area_type"}, "task_id=?", new String[]{mission.getId()}, null, null, null);
        map.clear();
        Point p = FileUtil.getLastLocationFromFile(System.currentTimeMillis(), 3600000);
        if (p != null) {
            CoordinateConverter converter = new CoordinateConverter(getContext());
            converter.from(CoordinateConverter.CoordType.GPS);
            LatLng sourceLatLng = new LatLng(p.getLatitude(), p.getLongitude());
            converter.coord(sourceLatLng);
            LatLng desLatLng = converter.convert();
            map.moveCamera(CameraUpdateFactory.newLatLng(desLatLng));
        }
        while (c.moveToNext()) {
            String coordinate = c.getString(c.getColumnIndex("coordinate"));
            String area_type = c.getString(c.getColumnIndex("area_type"));

            try {
                if ("point".equals(area_type)) {
                    JSONArray ja = new JSONArray(coordinate);

                    CoordinateConverter converter = new CoordinateConverter(getContext());
                    converter.from(CoordinateConverter.CoordType.GPS);
                    LatLng sourceLatLng = new LatLng(ja.getDouble(1), ja.getDouble(0));
                    converter.coord(sourceLatLng);
                    LatLng desLatLng = converter.convert();

                    CircleOptions cr = new CircleOptions().radius(20).center(desLatLng).
                            strokeWidth(3).fillColor(Color.parseColor("#22000000"));
                    map.addCircle(cr);

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(desLatLng, 12));
                } else if ("polygon".equals(area_type)) {
                    JSONArray ja = new JSONArray(coordinate).getJSONArray(0);
                    PolygonOptions po = new PolygonOptions();
                    for (int i = 0; i < ja.length(); i++) {
                        CoordinateConverter converter = new CoordinateConverter(getContext());
                        converter.from(CoordinateConverter.CoordType.GPS);
                        LatLng sourceLatLng = new LatLng(ja.getJSONArray(i).getDouble(1), ja.getJSONArray(i).getDouble(0));
                        converter.coord(sourceLatLng);
                        LatLng desLatLng = converter.convert();

                        po.add(desLatLng);
                    }
                    po.strokeColor(Color.BLACK).strokeWidth(2).fillColor(Color.parseColor("#22000000"));
                    map.addPolygon(po);

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(po.getPoints().get(0), 12));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if (activity != null) {
            mMyCallBack = (MyCallBack) activity;
        }
    }


    public void setDefaultTaskID(String defaultTaskID) {
        this.defaultTaskID = defaultTaskID;
    }

}
