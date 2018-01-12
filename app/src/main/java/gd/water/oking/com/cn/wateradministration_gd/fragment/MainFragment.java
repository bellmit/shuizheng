package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;
import com.vondear.rxtools.RxDeviceUtils;
import com.vondear.rxtools.view.RxToast;
import com.yinghe.whiteboardlib.fragment.WhiteBoardFragment;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.x;

import java.io.EOFException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.MenuAdapter;
import gd.water.oking.com.cn.wateradministration_gd.Adapter.MenuItemAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.View.CustomPopWindow;
import gd.water.oking.com.cn.wateradministration_gd.bean.MenuBean;
import gd.water.oking.com.cn.wateradministration_gd.bean.MenuBund;
import gd.water.oking.com.cn.wateradministration_gd.bean.MenuGsonBean;
import gd.water.oking.com.cn.wateradministration_gd.db.LawDao;
import gd.water.oking.com.cn.wateradministration_gd.http.CheckConnectParams;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.http.LoginParams;
import gd.water.oking.com.cn.wateradministration_gd.http.MapResponse;
import gd.water.oking.com.cn.wateradministration_gd.main.MainActivity;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.LocalSqlite;
import gd.water.oking.com.cn.wateradministration_gd.util.Utils;
import io.reactivex.Flowable;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class MainFragment extends BaseFragment {
    private MainActivity activity;
    private ImageButton chatBtn;
    private ImageButton gpsBtn, signalBtn;
    private LinearLayout chatContainer;
    //    private ContactListFragment contactListFragment;
    private int currentTabIndex = 1;
    private ValueAnimator flashesAnimation;
    private MediaPlayer player;
    private ImageView user_imageView;
    private TextView user_textView, signal_tv, gpsStar_tv;
    private UpcomingFragment mUpcomingFragment;


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case MainActivity.START_ALARM_UI_LIST:
                    try {
                        if (player == null) {
                            player = new MediaPlayer();
                            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            player.setDataSource(getContext(), alert);
                        }
                        final AudioManager audioManager = (AudioManager) MyApp.getApplictaion().getSystemService(Context.AUDIO_SERVICE);
                        if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0) {
                            player.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                            try {
                                if (!player.isPlaying()) {
                                    player.prepare();
                                    player.start();
                                }
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case MainActivity.STOP_ALARM_UI_LIST:

                    if (player != null && player.isPlaying()) {
                        player.stop();
                        player = null;
                    }
                    break;
                case MainActivity.NewMission:

                    if (!(getChildFragmentManager().findFragmentById(R.id.fragment_root) instanceof MissionFragment)) {
                        BaseFragment f = new MissionFragment();

                        FragmentTransaction ft = MainFragment.this.getChildFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_root, f).commit();
                    }
                    break;
                case MainActivity.UPDATE_GPS_STATE_UI:

                    mLatitude = intent.getDoubleExtra("latitude", 0);
                    mLongitude = intent.getDoubleExtra("longitude", 0);
                    mAltitude = intent.getDoubleExtra("altitude", 0);
                    mSpeed = intent.getDoubleExtra("speed", 0);
                    mAccuracy = intent.getDoubleExtra("accuracy", 0);
                    mDateTime = intent.getDoubleExtra("dateTime", 0);
                    mUseCount = intent.getIntExtra("useCount", 0);
                    int level = intent.getIntExtra("SignalLevel", 0);

                    switch (level) {
                        case 0:
                            gpsBtn.setImageResource(R.drawable.gpsfailure);
                            break;
                        case 1:
                            gpsBtn.setImageResource(R.drawable.gpssuccess);
                            break;
                        case 2:
                            gpsBtn.setImageResource(R.drawable.gpssuccess);
                            break;
                        case 3:
                            gpsBtn.setImageResource(R.drawable.gpssuccess);
                            break;
                        default:
                            gpsBtn.setImageResource(R.drawable.gpsfailure);
                            break;
                    }

                    gpsStar_tv.setText(mUseCount == 0 ? "" : mUseCount + "");


                    break;
                case MainActivity.UPDATE_SIGNAL_UI:

                    int signalLLevel = intent.getIntExtra("SignalLevel", 0);
                    signal_tv.setText(intent.getStringExtra("state"));
                    switch (signalLLevel) {
                        case 0:
                            signalBtn.setImageResource(R.drawable.signal0);
                            break;
                        case 1:
                            signalBtn.setImageResource(R.drawable.signal1);
                            break;
                        case 2:
                            signalBtn.setImageResource(R.drawable.signal2);
                            break;
                        case 3:
                            signalBtn.setImageResource(R.drawable.signal3);
                            break;
                        case 4:
                            signalBtn.setImageResource(R.drawable.signal4);
                            break;
                        case 5:
                            signalBtn.setImageResource(R.drawable.signal5);
                            break;
                        default:
                            signalBtn.setImageResource(R.drawable.signal0);
                            break;
                    }

                    break;
                case MainActivity.CALL_CASE_MANAGER:


                    CaseManagerFragment caseManagerFragment = (CaseManagerFragment) mMenus.get(2).getFragments().get(1);
                    FragmentManager fm = MainFragment.this.getChildFragmentManager();
                    fm.beginTransaction().replace(R.id.fragment_root, caseManagerFragment).commit();

                    break;
                default:
                    break;
            }
        }
    };
    private int mPostion = 1;
    private ArrayList<MenuBean> mMenus;

    private double mLatitude;
    private double mLongitude;
    private double mAltitude;
    private double mSpeed;
    private double mAccuracy;
    private double mDateTime;

    private int mUseCount;
    private int mUnreadMsgCount = 0;
    private TextView mTv_tilecunt;
    private DrawerLayout mDrawer_layout;
    private RigthChatFragment mRigthChatFragment;
    private MsgListener mMsgListener;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MenuBund mMenuBund;
    private String username;
    private Button mBt_sos;

    private RecyclerView mLv_tbitem;
    private Intent mIntentFilter;
    private List<String> mNewItems;
    private int mSelePos = -1;
    private int mScreenWidths;
    private int mScreenHeights;
    private Subscription mSubscription;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(MenuBund menuBund, String trim) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, menuBund);
        args.putString(ARG_PARAM2, trim);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMenuBund = (MenuBund) getArguments().getSerializable(ARG_PARAM1);
            username = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyApp.getApplictaion().registerReceiver(mReceiver, new IntentFilter(MainActivity.START_ALARM_UI_LIST));
        MyApp.getApplictaion().registerReceiver(mReceiver, new IntentFilter(MainActivity.STOP_ALARM_UI_LIST));
        MyApp.getApplictaion().registerReceiver(mReceiver, new IntentFilter(MainActivity.NewMission));
        MyApp.getApplictaion().registerReceiver(mReceiver, new IntentFilter(MainActivity.CALL_CASE_MANAGER));
        MyApp.getApplictaion().registerReceiver(mReceiver, new IntentFilter(MainActivity.UPDATE_GPS_STATE_UI));
        MyApp.getApplictaion().registerReceiver(mReceiver, new IntentFilter(MainActivity.UPDATE_SIGNAL_UI));

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onDestroyView() {
        MyApp.getApplictaion().unregisterReceiver(mReceiver);
        if (mSubscription!=null){

            mSubscription.cancel();
            mSubscription=null;
        }
        super.onDestroyView();
    }

    @Override
    public void initView(final View rootView) {
        mScreenWidths = RxDeviceUtils.getScreenWidths(MyApp.getApplictaion());
        mScreenHeights = RxDeviceUtils.getScreenHeights(MyApp.getApplictaion());
        mIntentFilter = new Intent(MainActivity.UPDATE_MISSION_GET);
        MyApp.getApplictaion().sendBroadcast(mIntentFilter);
        mTv_tilecunt = (TextView) rootView.findViewById(R.id.tv_tilecunt);
        mBt_sos = (Button) rootView.findViewById(R.id.bt_sos);
        mBt_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //临时紧急任务列表
                EmergencyTaskListFragment mEmergencyTaskListFragment = EmergencyTaskListFragment.newInstance(null, null);
                mLv_tbitem.setVisibility(View.GONE);
                getChildFragmentManager().beginTransaction().replace(R.id.fragment_root, mEmergencyTaskListFragment).commit();


            }
        });
        loginChatServer();
        mMsgListener = new MsgListener();
        EMClient.getInstance().chatManager().addMessageListener(mMsgListener);
        mDrawer_layout = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
        if (mUpcomingFragment == null) {

            mUpcomingFragment = new UpcomingFragment();
        }

        ImageButton ib_vpn = (ImageButton) rootView.findViewById(R.id.ib_vpn);
        ib_vpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent vpnIntent = new Intent();
                vpnIntent.setAction("android.net.vpn.SETTINGS");
                activity.startActivity(vpnIntent);
            }
        });
        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_root, mUpcomingFragment).commit();
        mDrawer_layout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mUnreadMsgCount = 0;
                mTv_tilecunt.setVisibility(View.GONE);
                mMsgListener = new MsgListener();
                EMClient.getInstance().chatManager().addMessageListener(mMsgListener);
                if (mRigthChatFragment != null) {

                    getChildFragmentManager().beginTransaction().replace(R.id.fragment_chat, mRigthChatFragment).commit();
                } else {
                    RxToast.warning(MyApp.getApplictaion(), "环信服务器登录失败了~~~", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                EMClient.getInstance().chatManager().removeMessageListener(mMsgListener);
                mMsgListener = null;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        //菜单
        ListView lv_menu = (ListView) rootView.findViewById(R.id.lv_menu);
        final ArrayList<MenuBean> menus = initMenuData();
        final MenuAdapter menuAdapter = new MenuAdapter(menus);
        menuAdapter.setSelectItem(0);
        lv_menu.setAdapter(menuAdapter);
        //menu子标签tabItem
        mLv_tbitem = rootView.findViewById(R.id.rc_tbitem);
        mLv_tbitem.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mLv_tbitem.setItemAnimator(new DefaultItemAnimator());
        final List<String> items = menus.get(1).getItems();
        final MenuItemAdapter menuItemAdapter = new MenuItemAdapter(R.layout.menu_tab_item, items);
        menuItemAdapter.openLoadAnimation();
        mLv_tbitem.setAdapter(menuItemAdapter);
//        menuItemAdapter.getViewByPosition(mLv_tbitem, 0, R.id.tv).setBackgroundColor(Color.DKGRAY);

        menuItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mSelePos != -1) {
                    View viewByPosition = adapter.getViewByPosition(mLv_tbitem, mSelePos, R.id.tv);
                    if (viewByPosition != null) {

                        viewByPosition.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
                view.setBackgroundColor(Color.DKGRAY);
                mSelePos = position;
                ArrayList<Fragment> fragmentArrayList = menus.get(mPostion).getFragments();
                if (fragmentArrayList.get(position)!=null){

                    getChildFragmentManager().beginTransaction().replace(R.id.fragment_root, fragmentArrayList.get(position)).commit();
                }


            }
        });


        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mLv_tbitem.setVisibility(View.VISIBLE);
                menuAdapter.setSelectItem(i);
                mPostion = i;
                if (i == 0) {
                    mLv_tbitem.setVisibility(View.GONE);
                    if (mUpcomingFragment == null) {

                        mUpcomingFragment = new UpcomingFragment();
                    }
                    FragmentManager fm = getChildFragmentManager();
                    fm.beginTransaction().replace(R.id.fragment_root, mUpcomingFragment).commit();


                } else {
                    mLv_tbitem.setVisibility(View.VISIBLE);
                    mNewItems = menus.get(i).getItems();
                    menuItemAdapter.setNewData(mNewItems);

                    ArrayList<Fragment> fragmentArrayList = menus.get(i).getFragments();
                    getChildFragmentManager().beginTransaction().replace(R.id.fragment_root, fragmentArrayList.get(0)).commit();    //默认选中第一tabitem

                }
                menuAdapter.notifyDataSetChanged();
            }
        });


        gpsBtn = (ImageButton) rootView.findViewById(R.id.gpsBtn);
        gpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View inflate = View.inflate(activity, R.layout.pop_view_gps, null);
                TextView tv_longitude = (TextView) inflate.findViewById(R.id.tv_longitude);
                TextView tv_latitude = (TextView) inflate.findViewById(R.id.tv_latitude);
                TextView tv_altitude = (TextView) inflate.findViewById(R.id.tv_altitude);
                TextView tv_speed = (TextView) inflate.findViewById(R.id.tv_speed);
                TextView tv_accuracy = (TextView) inflate.findViewById(R.id.tv_accuracy);
                TextView tv_count = (TextView) inflate.findViewById(R.id.tv_count);
                TextView tv_dateTime = (TextView) inflate.findViewById(R.id.tv_dateTime);

                tv_longitude.setText("经度：" + mLongitude);
                tv_latitude.setText("纬度：" + mLatitude);
                tv_altitude.setText("高度：" + mAltitude);
                tv_speed.setText("速度：" + mSpeed);
                tv_accuracy.setText("精度：" + mAccuracy);
                tv_count.setText("在用卫星：" + mUseCount);
                tv_dateTime.setText("定位时间：" + mDateTime);
                //PopupWindow
                CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(MyApp.getApplictaion())
                        .setView(inflate)//显示的布局
                        .size((int) (mScreenWidths / 2.5), (int) (mScreenHeights / 2.5)) //设置显示的大小，不设置就默认包裹内容
                        .create()//创建PopupWindow
                        .showAsDropDown(gpsBtn, 25, 25);//显示PopupWindow


            }
        });
        signalBtn = (ImageButton) rootView.findViewById(R.id.signalBtn);
        signal_tv = (TextView) rootView.findViewById(R.id.signal_tv);
        gpsStar_tv = (TextView) rootView.findViewById(R.id.gpsStar_tv);
        chatContainer = (LinearLayout) rootView.findViewById(R.id.fragment_chat);

        chatBtn = rootView.findViewById(R.id.chatBtn);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRigthChatFragment != null) {
                    mRigthChatFragment.addData(mContacts);
                    if (mDrawer_layout.isDrawerOpen(chatContainer)) {
                        mDrawer_layout.closeDrawer(chatContainer);
                    } else {

                        mDrawer_layout.openDrawer(chatContainer);

                    }
                }

            }
        });

        user_imageView = (ImageView) rootView.findViewById(R.id.user_imageView);
        user_imageView.setOnClickListener(new View.OnClickListener() {

            private UserInfoFragment mUserInfoFragment;

            @Override
            public void onClick(View view) {

                if (mUserInfoFragment == null) {
                    mUserInfoFragment = new UserInfoFragment();
                }
                menuAdapter.setSelectItem(0);
                mLv_tbitem.setVisibility(View.GONE);
                getChildFragmentManager().beginTransaction().replace(R.id.fragment_root, mUserInfoFragment).commit();
            }
        });
        user_textView = (TextView) rootView.findViewById(R.id.user_textView);
        if (DefaultContants.CURRENTUSER != null && DefaultContants.CURRENTUSER.getProfile() != null) {

            new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Void... params) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(DefaultContants.CURRENTUSER.getProfile(), 0, DefaultContants.CURRENTUSER.getProfile().length);
                    Matrix matrix = new Matrix();
                    matrix.setScale(0.5f, 0.5f);
                    if (bitmap == null) {
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.headerimg);
                    }
                    while (bitmap.getByteCount() > 100 * 1000) {
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    }
                    return bitmap;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    //super.onPostExecute(bitmap);
                    if (bitmap != null) {
                        user_imageView.setImageBitmap(bitmap);
                    }
                }
            }.execute();
        }
        if (DefaultContants.CURRENTUSER != null) {
            user_textView.setText(DefaultContants.CURRENTUSER.getUserName());
        }


//        //定时检测重新登录
        Flowable.interval(5, 10, TimeUnit.SECONDS)
                .onBackpressureDrop()
                .subscribe(new Subscriber<Long>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        mSubscription = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        reLogin();
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    private Map<String, EaseUser> mContacts;

    //获取联系人
    public void getContacts() {


        mContacts = new HashMap<String, EaseUser>();
        List<String> usernames = null;
        try {
            usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        if (usernames != null && usernames.size() > 0) {
            for (String s : usernames) {
                String nick = LawDao.getGdWaterContact(s);
                EaseUser easeUser = new EaseUser(s + "(" + nick + ")");
                easeUser.setNickname(nick);
                mContacts.put(s, easeUser);
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(s);

                if (conversation != null) {

                    mUnreadMsgCount = mUnreadMsgCount + conversation.getUnreadMsgCount();
                }
            }

        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRigthChatFragment == null) {
                    mRigthChatFragment = new RigthChatFragment(mContacts);

                }
                if (mUnreadMsgCount == 0) {
                    mTv_tilecunt.setVisibility(View.GONE);
                } else {
                    mTv_tilecunt.setVisibility(View.VISIBLE);
                    mTv_tilecunt.setText(mUnreadMsgCount + "");
                }
            }
        });


    }


    @NonNull
    private ArrayList<MenuBean> initMenuData() {
        mMenus = new ArrayList<MenuBean>();
        MenuBean menuBean0 = new MenuBean();
        menuBean0.setIcon(R.mipmap.home);
        menuBean0.setTitle("首页");
        menuBean0.setItems(null);
        mMenus.add(menuBean0);

        List<MenuGsonBean> menuGsonBeen = mMenuBund.getMenuGsonBeen();
        for (MenuGsonBean bean : menuGsonBeen) {
            String menuTag = bean.getText();
            if (menuTag.contains("巡查管理")) {
                //任务管理
                MenuBean menuBean1 = new MenuBean();
                menuBean1.setIcon(R.mipmap.renwgl);
                menuBean1.setTitle("任务管理");
                List<String> item1 = new ArrayList<String>();
                item1.add("巡查任务发布");
                item1.add("巡查任务审核");
                item1.add("巡查任务接收安排");
                item1.add("任务执行");
                item1.add("任务上报");
                item1.add("领导批示");
                List<MenuGsonBean.TopMenu> topMenus = bean.getTopMenus();
                List<String> item2 = new ArrayList<String>();
                for (MenuGsonBean.TopMenu topMenu : topMenus) {

                    item2.add(topMenu.getText());

                }
                item1.retainAll(item2);
                ArrayList<Fragment> fragments1 = new ArrayList<Fragment>();
                for (String s : item1) {
                    if ("巡查任务发布".equals(s)) {
                        InspectTaskListFragment inspectTaskListFragment = InspectTaskListFragment.newInstance(null, null);
                        fragments1.add(inspectTaskListFragment);
                    } else if ("巡查任务审核".equals(s)) {
                        WebViewFragment checkfragment = new WebViewFragment();
                        checkfragment.setUrlStr("/page/task/rwfbsp2.jsp");
                        fragments1.add(checkfragment);
                    } else if ("巡查任务接收安排".equals(s)) {
                        ArrangeMissionFragment arrangeMissionFragment = new ArrangeMissionFragment();

                        fragments1.add(arrangeMissionFragment);

                    } else if ("任务执行".equals(s)) {
                        MissionFragment missionFragment = new MissionFragment();
                        fragments1.add(missionFragment);

                    } else if ("任务上报".equals(s)) {
                        MissionReportFragment missionReportFragment = new MissionReportFragment();
                        fragments1.add(missionReportFragment);

                    } else if ("领导批示".equals(s)) {
                        WebViewFragment instructionsfragment = new WebViewFragment();
                        instructionsfragment.setUrlStr("/page/task/xcrz/xcrzhi.jsp");
                        fragments1.add(instructionsfragment);
                    }


                }

                menuBean1.setItems(item1);
                menuBean1.setFragments(fragments1);
                mMenus.add(menuBean1);

            } else if ("水行政处罚".equals(menuTag)) {

                //执法管理   >案件受理
                MenuBean menuBean2 = new MenuBean();
                menuBean2.setIcon(R.mipmap.zfgl);
                menuBean2.setTitle("执法管理");
                List<String> item1 = new ArrayList<String>();
//                item1.add("现场取证");
                item1.add("调查笔录");
                item1.add("责令停止违法行为通知");
                item1.add("水行政当场处罚决定书");
                item1.add("一般水行政处罚");
                item1.add("预立案");
                item1.add("现场勘验");

                menuBean2.setItems(item1);
                ArrayList<Fragment> fragments2 = new ArrayList<Fragment>();
//                WebViewFragment openCasesFragment = new WebViewFragment();
//                if (DefaultContants.CURRENTUSER != null) {
//                    openCasesFragment.setUrlStr("/page/xzcfybcx/ajsl.jsp?option=add&deptid=" + DefaultContants.CURRENTUSER.getDeptId());
//                }
//                fragments2.add(openCasesFragment);

                //案件基本信息
//                CaseManagerFragment basicInformationCaseMenuFragment = new CaseManagerFragment();
//                WebViewFragment basicInformationCaseDetail = new WebViewFragment();
//                basicInformationCaseDetail.setUrlStr("/page/xzcfybcx/ybcxindexByPad.jsp?iframeUrl=page/xzcfybcx/ajxx.jsp?");
//                basicInformationCaseMenuFragment.setListItemClickOpenFragment(basicInformationCaseDetail);
//                fragments2.add(basicInformationCaseMenuFragment);

                //立案呈批
//                CaseManagerFragment caseInBatchMenuFragment = new CaseManagerFragment();
//                WebViewFragment caseInBatchDetailFragment = new WebViewFragment();
//                caseInBatchDetailFragment.setUrlStr("/page/xzcfybcx/ybcxindexByPad.jsp?iframeUrl=page/xzcfybcx/chengpibiao.jsp?");
//                caseInBatchMenuFragment.setListItemClickOpenFragment(caseInBatchDetailFragment);
//                fragments2.add(caseInBatchMenuFragment);

                //现场取证
//                CaseManagerFragment sceneForensicsFragment = new CaseManagerFragment();
//                CaseEvidenceFragment caseEvidenceFragment = new CaseEvidenceFragment();
//                sceneForensicsFragment.setListItemClickOpenFragment(caseEvidenceFragment);
//                fragments2.add(sceneForensicsFragment);
//
//                //调查笔录
//                CaseManagerFragment writtenRecordFragment = new CaseManagerFragment();
//                SurveyRecordListFragment surveyRecordListFragment = new SurveyRecordListFragment();
//                writtenRecordFragment.setListItemClickOpenFragment(surveyRecordListFragment);
//                fragments2.add(writtenRecordFragment);

                //调查笔录
                WrittenRecordFragment writtenRecordFragment = WrittenRecordFragment.newInstance(null, null);
                fragments2.add(writtenRecordFragment);
                //责令停止违法行为通知
                StopTheIllegalActivitiesFragment stopTheIllegalActivitiesFragment = StopTheIllegalActivitiesFragment.newInstance(null, null);
                fragments2.add(stopTheIllegalActivitiesFragment);

                //水行政当场处罚决定书
                PenaltyTheSpotFragment penaltyTheSpotFragment = PenaltyTheSpotFragment.newInstance(null, null);
                fragments2.add(penaltyTheSpotFragment);


                //一般水行政处罚
                WebViewFragment administrativePenaltyFragment = new WebViewFragment();
                administrativePenaltyFragment.setUrlStr("/page/main/mainFramePad.jsp?c=1");
                fragments2.add(administrativePenaltyFragment);
                menuBean2.setFragments(fragments2);


                //预立案
                CaseInAdvanceFragment caseInAdvanceFragment = CaseInAdvanceFragment.newInstance(null, null);
                fragments2.add(caseInAdvanceFragment);


                //现场勘验
                WhiteBoardFragment whiteBoardFragment = WhiteBoardFragment.newInstance();
                fragments2.add(whiteBoardFragment);
                mMenus.add(menuBean2);

            } else if ("执法指导".equals(menuTag)) {
                //执法指导
                MenuBean menuBean3 = new MenuBean();
                menuBean3.setIcon(R.mipmap.zhifazd);
                menuBean3.setTitle("执法指导");
                List<String> item1 = new ArrayList<String>();
                item1.add("执法流程指引");
                item1.add("法律法规库");
                item1.add("执法规范");
                item1.add("案例库");
                ArrayList<Fragment> fragments3 = new ArrayList<Fragment>();
                LawEnforcementProcessGuidanceFragment enforcementProcessFragment = LawEnforcementProcessGuidanceFragment.newInstance(null, null);
                fragments3.add(enforcementProcessFragment);

                LawsAndRegulationsFragment lawFragment = LawsAndRegulationsFragment.newInstance(null, null);
                fragments3.add(lawFragment);


                LawEnforcementSpecificationFragment lawEnforcementSpecificationFragment = LawEnforcementSpecificationFragment.newInstance(null, null);
                fragments3.add(lawEnforcementSpecificationFragment);

                PuttedForwardFragment puttedForwardFragment = PuttedForwardFragment.newInstance(null, null);
                fragments3.add(puttedForwardFragment);
                menuBean3.setFragments(fragments3);
                menuBean3.setItems(item1);
                mMenus.add(menuBean3);
            } else if ("综合查询".equals(menuTag)) {
                //统计查询
                MenuBean menuBean4 = new MenuBean();
                menuBean4.setIcon(R.mipmap.tjcx);
                menuBean4.setTitle("统计查询");
                List<String> item1 = new ArrayList<String>();
                item1.add("违法人员");
                item1.add("违法企业");
                item1.add("船只查询");
                item1.add("建筑查询");
                item1.add("立案统计");
                item1.add("现场执法统计");
                item1.add("巡查日志管理");
                item1.add("地图查询");
                item1.add("地图任务展示");
                item1.add("区域历史执法记录查询");
                item1.add("巡查轨迹管理");
//                item1.add("个人区域执法历史记录");
//                item1.add("部门区域执法历史记录");
                menuBean4.setItems(item1);

                ArrayList<Fragment> fragments4 = new ArrayList<Fragment>();
//                IllegalWorkersFragment illegalWorkersFragment = IllegalWorkersFragment.newInstance(null, null);
//                fragments4.add(illegalWorkersFragment);
                WebViewFragment illegalManSearchFragment = new WebViewFragment();
                illegalManSearchFragment.setUrlStr("/page/case/wfry.jsp");
                fragments4.add(illegalManSearchFragment);

                WebViewFragment illegalCompanySearchFragment = new WebViewFragment();
                illegalCompanySearchFragment.setUrlStr("/page/case/wfqy.jsp");
                fragments4.add(illegalCompanySearchFragment);

                WebViewFragment shipSearchFragment = new WebViewFragment();
                shipSearchFragment.setUrlStr("/page/tjcx/qzxx.jsp");
                fragments4.add(shipSearchFragment);

                WebViewFragment buildingSearchFragment = new WebViewFragment();
                buildingSearchFragment.setUrlStr("/building/initPage");
                fragments4.add(buildingSearchFragment);

                WebViewFragment caseStatisticsFragment = new WebViewFragment();
                caseStatisticsFragment.setUrlStr("/page/tjcx/latj.jsp");
                fragments4.add(caseStatisticsFragment);

                //现场执法统计
                WebViewFragment punishStatisticsFragment = new WebViewFragment();
                punishStatisticsFragment.setUrlStr("/page/case/cftj.jsp");
                fragments4.add(punishStatisticsFragment);

                //巡查日志管理
                PatrolLogManagementFragment patrolLogManagementFragment = PatrolLogManagementFragment.newInstance(null, null);
                fragments4.add(patrolLogManagementFragment);

                //地图查询
                MapQueryFragment mapQueryFragment = MapQueryFragment.newInstance(null, null);
                fragments4.add(mapQueryFragment);

                //地图任务展示
                MapTaskFragment mapTaskFragment = MapTaskFragment.newInstance(null, null);
                fragments4.add(mapTaskFragment);

                //区域历史执法记录
                RegionalHistoryEnforcementFragment regionalHistoryEnforcementFragment = RegionalHistoryEnforcementFragment.newInstance(null, null);
                fragments4.add(regionalHistoryEnforcementFragment);


                //巡查轨迹管理
                WebViewFragment trackManagementFragment = new WebViewFragment();
                trackManagementFragment.setUrlStr("/arcgis/padMap/padLineMap.jsp");
                fragments4.add(trackManagementFragment);

//                UserAllMissionFragment userAllMissionFragment = new UserAllMissionFragment();
//                fragments4.add(userAllMissionFragment);
//
//                DeptAllMissionFragment deptAllMissionFragment = new DeptAllMissionFragment();
//                fragments4.add(deptAllMissionFragment);
                menuBean4.setFragments(fragments4);
                mMenus.add(menuBean4);

            }

        }


        //设置中心
        MenuBean menuBean5 = new MenuBean();
        menuBean5.setIcon(R.mipmap.setting);
        menuBean5.setTitle("设置中心");
        List<String> item1 = Arrays.asList("通讯录", "离线地图管理", "个人信息");

        menuBean5.setItems(item1);

        ArrayList<Fragment> fragments5 = new ArrayList<Fragment>();
        ContactsFragment contactsFragment = new ContactsFragment();
        fragments5.add(contactsFragment);

        OfflineMapManagerFragment offlineMapManagerFragment = new OfflineMapManagerFragment();
        fragments5.add(offlineMapManagerFragment);

        UserInfoFragment userInfoFragment = new UserInfoFragment();
        fragments5.add(userInfoFragment);
        menuBean5.setFragments(fragments5);
        mMenus.add(menuBean5);
        return mMenus;
    }

    @Override
    public void onResume() {
        EaseUI.getInstance().getNotifier().reset();
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;//保存Context引用
    }

    private void loginChatServer() {
        if (!TextUtils.isEmpty(username)) {
            EMClient.getInstance().logout(true, new EMCallBack() {
                @Override
                public void onSuccess() {
                    MyApp.getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            EMClient.getInstance().login(username, "888888", new EMCallBack() {//回调
                                @Override
                                public void onSuccess() {

                                    getContacts();

                                }

                                @Override
                                public void onProgress(int progress, String status) {

                                }

                                @Override
                                public void onError(int code, String message) {
                                }
                            });
                        }
                    });

                }

                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onProgress(int i, String s) {

                }
            });

        }


    }

    private void reLogin() {
        //测试与服务器通讯
//        GetContactsListParams params = new GetContactsListParams();
//        params.deptid = "-1";
        CheckConnectParams params = new CheckConnectParams();
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                if (!DefaultContants.ISHTTPLOGIN) {
                    //尝试重新登录
                    LoginParams params = new LoginParams();
                    params.account = DefaultContants.CURRENTUSER.getAccount();
                    params.password = DefaultContants.CURRENTUSER.getPassword();
                    Cancelable cancelable = x.http().post(params, new CommonCallback<MapResponse>() {
                        @Override
                        public void onSuccess(MapResponse result) {
                            boolean isOk = (Boolean) result.getMap().get("success");
                            if (isOk) {

                                SharedPreferences sharedPreferences1 = MyApp.getApplictaion().getSharedPreferences("logintime", Context.MODE_PRIVATE);
                                long logintime = sharedPreferences1.getLong("logintime", 0);
                                if (logintime != 0) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    if (!sdf.format(new Date(logintime)).equals(sdf.format(new Date(Long.valueOf((String) result.getMap().get("logintime")))))) {
                                        SharedPreferences.Editor editor = sharedPreferences1.edit();
                                        editor.putLong("logintime", Long.valueOf((String) result.getMap().get("logintime")));
                                        editor.commit();

                                    }
                                } else {
                                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                                    editor.putLong("logintime", Long.valueOf((String) result.getMap().get("logintime")));
                                    editor.commit();

                                }

                                Utils.getCookies2DB();

                                //登录成功，本地数据库存入数据或更新数据
                                if (DefaultContants.CURRENTUSER != null) {
                                    DefaultContants.CURRENTUSER.setUserId((String) result.getMap().get("userid"));
                                    DefaultContants.CURRENTUSER.setDeptId((String) result.getMap().get("dept_id"));
                                    DefaultContants.CURRENTUSER.setUserName((String) result.getMap().get("userName"));
                                    DefaultContants.CURRENTUSER.setDeptName((String) result.getMap().get("deptname"));
                                    DefaultContants.CURRENTUSER.setAccount(DefaultContants.CURRENTUSER.getAccount());
                                    DefaultContants.CURRENTUSER.setPassword(DefaultContants.CURRENTUSER.getPassword());
                                    DefaultContants.CURRENTUSER.setPhone((String) result.getMap().get("phone"));
                                    DefaultContants.CURRENTUSER.setProfile(Base64.decode((String) result.getMap().get("headimg"), Base64.DEFAULT));

                                    MyApp.localSqlite.delete(LocalSqlite.USER_TABLE,
                                            "uid=?", new String[]{DefaultContants.CURRENTUSER.getUserId()});

                                    DefaultContants.CURRENTUSER.insertDB(MyApp.localSqlite);


                                }

//                                connectBtn.setImageResource(DefaultContants.isHttpLogin ? R.drawable.online : R.drawable.outline);
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            DefaultContants.ISHTTPLOGIN = false;
                            DefaultContants.JSESSIONID = "";


                            if (ex instanceof HttpException) { // 网络错误
                                HttpException httpEx = (HttpException) ex;
                                int responseCode = httpEx.getCode();
                                String responseMsg = httpEx.getMessage();
                                String errorResult = httpEx.getResult();
                                httpEx.printStackTrace();
                                Toast.makeText(MyApp.getApplictaion(), ex.getMessage(), Toast.LENGTH_LONG).show();
                            } else { // 其他错误
                                // ...
                            }
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {
                            Toast.makeText(MyApp.getApplictaion(), "cancelled", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFinished() {

                        }
                    });
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("CheckConnectParams", "onError>>>>>>" + ex.toString());

                if (ex instanceof EOFException) {//经常出现EOFException
                    DefaultContants.ISHTTPLOGIN = false;
//                    connectBtn.setImageResource(DefaultContants.isHttpLogin ? R.drawable.online : R.drawable.outline);
                } else {
                    DefaultContants.ISHTTPLOGIN = false;
//                    connectBtn.setImageResource(DefaultContants.isHttpLogin ? R.drawable.online : R.drawable.outline);
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


    class MsgListener implements EMMessageListener {

        @Override
        public void onMessageReceived(List<EMMessage> list) {
            mUnreadMsgCount++;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!mTv_tilecunt.isShown()) {
                        mTv_tilecunt.setVisibility(View.VISIBLE);
                    }
                    mTv_tilecunt.setText(mUnreadMsgCount + "");
                }
            });


        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }


        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    }
}
