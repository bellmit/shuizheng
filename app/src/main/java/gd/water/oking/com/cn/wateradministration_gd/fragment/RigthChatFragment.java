package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import gd.water.oking.com.cn.wateradministration_gd.R;

@SuppressLint({"NewApi", "ValidFragment"})
public class RigthChatFragment extends Fragment implements EMMessageListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EaseConversationListFragment mEaseConversationListFragment;
    private EaseContactListFragment mEaseContactListFragment;
    private RadioGroup mRg;
    private View mInflate;
    private RadioButton mRb_content;
    private RadioButton mRb_chat;
    private Map<String, EaseUser> mContacts = new Hashtable<String, EaseUser>();
    private TextView mTv_tag;

    public static RigthChatFragment newInstance(String param1, String param2) {
        RigthChatFragment fragment = new RigthChatFragment(null);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RigthChatFragment(Map<String, EaseUser> mContacts) {
        this.mContacts = mContacts;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mInflate == null) {

            mInflate = inflater.inflate(R.layout.fragment_rigth_chat, container, false);
            EMClient.getInstance().chatManager().addMessageListener(this);

        }
        initViews();
        initData();
        setListener();
        return mInflate;
    }

    private void initData() {
//        getContacts();
        mRb_chat.setChecked(true);
        if (mContacts.size() > 0) {

            mEaseContactListFragment.setContactsMap(mContacts);
        }


        getChildFragmentManager().beginTransaction().replace(R.id.fl_chat, mEaseConversationListFragment).commit();
        mRb_content.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        mRb_chat.setBackgroundColor(getResources().getColor(R.color.colorMain7));

    }

    private void setListener() {
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rb_chat:
                        mTv_tag.setVisibility(View.GONE);
                        getChildFragmentManager().beginTransaction().replace(R.id.fl_chat, mEaseConversationListFragment).commit();
                        mRb_content.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                        mRb_chat.setBackgroundColor(getResources().getColor(R.color.colorMain7));
                        break;
                    case R.id.rb_content:
                        mTv_tag.setVisibility(View.GONE);
                        getChildFragmentManager().beginTransaction().replace(R.id.fl_chat, mEaseContactListFragment).commit();
                        mRb_content.setBackgroundColor(getResources().getColor(R.color.colorMain7));
                        mRb_chat.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
//                        getContacts();
                        break;
                    default:
                        break;

                }
            }
        });


        //点击事件
        mEaseContactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {
                mTv_tag.setVisibility(View.GONE);
                ChatFragment chatFragment = new ChatFragment();

                Bundle args = new Bundle();
                args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                args.putString(EaseConstant.EXTRA_USER_ID, user.getUsername());
                chatFragment.setArguments(args);
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                ft.add(R.id.fl_chat, chatFragment);
                ft.addToBackStack("chat");
                ft.commit();
            }
        });


        mEaseConversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                mTv_tag.setVisibility(View.GONE);
                ChatFragment chatFragment = new ChatFragment();
                Bundle args = new Bundle();
                args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                args.putString(EaseConstant.EXTRA_USER_ID, conversation.getLastMessage().getUserName());
                chatFragment.setArguments(args);
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                ft.add(R.id.fl_chat, chatFragment);
                ft.addToBackStack("chat");
                ft.commit();
            }
        });

    }

    private void initViews() {
        mRg = (RadioGroup) mInflate.findViewById(R.id.rg);
        mRb_chat = (RadioButton) mInflate.findViewById(R.id.rb_chat);
        mRb_content = (RadioButton) mInflate.findViewById(R.id.rb_content);
        mTv_tag = (TextView) mInflate.findViewById(R.id.tv_tag);
        mEaseContactListFragment = new EaseContactListFragment();
        mEaseConversationListFragment = new EaseConversationListFragment();

    }

    public void addData(Map<String, EaseUser> userMap) {
        mContacts = userMap;
//        mEaseContactListFragment.setContactsMap(userMap);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mInflate != null) {
            ViewGroup parent = (ViewGroup) mInflate.getParent();
            if (parent != null) {
                parent.removeView(mInflate);
            }

            EMClient.getInstance().chatManager().removeMessageListener(this);
        }
    }


    @Override
    public void onMessageReceived(List<EMMessage> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mEaseConversationListFragment != null) {
                    mEaseConversationListFragment.refresh();
                    if (!mTv_tag.isShown()) {

                        mTv_tag.setVisibility(View.VISIBLE);
                    }
                }
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


    //获取联系人
//    public void getContacts() {
//
//
//        MyApp.getGlobalThreadPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    mContacts = new HashMap<String, EaseUser>();
//                    List<String> usernames = null;
//                    usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
//                    if (usernames != null && usernames.size() > 0) {
//                        for (String s : usernames) {
//                            EaseUser easeUser = new EaseUser(s);
//                            mContacts.put(s, easeUser);
//
//                        }
//                        mEaseContactListFragment.setContactsMap(mContacts);
//                        mEaseContactListFragment.refresh();
//                    }
//                    System.out.println(usernames+"KKKKKKKKKKKK");
//                } catch (HyphenateException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//
//
//
//
//    }
}
