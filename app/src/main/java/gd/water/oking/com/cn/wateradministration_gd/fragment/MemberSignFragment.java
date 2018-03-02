package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogEditSureCancel;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.MissionMemberAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.View.SignatureView;
import gd.water.oking.com.cn.wateradministration_gd.bean.Member;
import gd.water.oking.com.cn.wateradministration_gd.bean.MissionLog;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.FileUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemberSignFragment extends BaseFragment {

    private ArrayList<Member> memberList;
    private MissionMemberAdapter memberAdapter;
    private MissionLog log;

    private int selection = -1;

    private File dir = new File(Environment.getExternalStorageDirectory(), "oking/mission_signature");
    private RxDialogSureCancel mRxDialogSureCancel;
    private RxDialogEditSureCancel mRxDialogEditSureCancel;

    public MemberSignFragment() {
        // Required empty public constructor
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_memeber_sign, container, false);
    }

    @Override
    public void initView(View rootView) {
        final TextView name_tv = (TextView) rootView.findViewById(R.id.name_tv);
        final SignatureView signature_View = (SignatureView) rootView.findViewById(R.id.signature_View);

        signature_View.setCanPaint(false);

        final Button clearBtn = (Button) rootView.findViewById(R.id.clear_btn);
        final Button saveBtn = (Button) rootView.findViewById(R.id.save_btn);

        ListView memberListView = (ListView) rootView.findViewById(R.id.member_listView);
        memberAdapter = new MissionMemberAdapter(getContext(), memberList);
        memberListView.setAdapter(memberAdapter);
        memberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selection = position;
                name_tv.setText(memberList.get(position).getUsername());
                signature_View.clear();
                signature_View.setCanPaint(true);

                clearBtn.setVisibility(memberList.get(position).getSignPic() == null ? View.VISIBLE : View.INVISIBLE);
                saveBtn.setVisibility(memberList.get(position).getSignPic() == null ? View.VISIBLE : View.INVISIBLE);
            }
        });
        memberListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int postion, long l) {
                Member member = memberList.get(postion);
                if (!TextUtils.isEmpty(member.getUserid())){
                    RxToast.warning(MyApp.getApplictaion(),"该队员不能删除",Toast.LENGTH_SHORT).show();
                }else {
                    if (member.getSignPic()!=null){
                        RxToast.warning(MyApp.getApplictaion(),"已签名不能删除",Toast.LENGTH_SHORT).show();

                    }else {
                        if (mRxDialogSureCancel==null){

                            mRxDialogSureCancel = new RxDialogSureCancel(getActivity());
                            mRxDialogSureCancel.setContent("是否删除该队员？");
                        }
                        mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                memberList.remove(postion);
                                memberAdapter.notifyDataSetChanged();
                                mRxDialogSureCancel.cancel();
                            }
                        });
                        mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mRxDialogSureCancel.cancel();
                            }
                        });

                        mRxDialogSureCancel.show();
                    }

                }
                return false;
            }
        });
        ImageButton backBtn =  rootView.findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemberSignFragment.this.getParentFragment().getChildFragmentManager().popBackStack();
            }
        });

        Button complete_button = rootView.findViewById(R.id.complete_button);
        Button btn_add =  rootView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRxDialogEditSureCancel==null){

                        mRxDialogEditSureCancel = new RxDialogEditSureCancel(getActivity(), false, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                dialogInterface.cancel();
                            }
                        });
                }
                mRxDialogEditSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String edname = mRxDialogEditSureCancel.getEditText().getText().toString().trim();
                        if (!TextUtils.isEmpty(edname)){
                            Member member = new Member();
                            member.setUsername(edname);
                            member.setPost("组员");
                            memberList.add(member);
                            memberAdapter.notifyDataSetChanged();
                            mRxDialogEditSureCancel.cancel();
                            RxToast.success(MyApp.getApplictaion(),"添加队员成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                mRxDialogEditSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mRxDialogEditSureCancel.cancel();
                    }
                });
                mRxDialogEditSureCancel.show();

            }
        });
        complete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemberSignFragment.this.getParentFragment().getChildFragmentManager().popBackStack();
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signature_View.clear();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getContext());//提示弹窗
                rxDialogSureCancel.setContent("保存签名后不能修改，是否继续？");
                rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rxDialogSureCancel.cancel();
                        if (selection != -1) {
                            Schedulers.io().createWorker().schedule(new Runnable() {
                                @Override
                                public void run() {
                                    Bitmap bitmap = signature_View.save();

                                    if (bitmap != null) {

                                        if (!dir.exists()) {
                                            dir.mkdirs();
                                        }

                                        File signatureFile = new File(dir, android.text.format.DateFormat
                                                .format("yyyyMMdd_HHmmss", System.currentTimeMillis())
                                                + ".jpg");

                                        try {
                                            OutputStream os = new FileOutputStream(signatureFile);
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                                            os.flush();
                                            os.close();
//                                            bitmap.recycle();
                                            bitmap = null;
                                            System.gc();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        if (memberList.get(selection) != null) {
//                                            if(memberList.get(selection).getSignPic() != null){
//                                                File file = new File(FileUtil.PraseUritoPath(getContext(),memberList.get(selection).getSignPic()));
//                                                file.delete();
//                                            }
                                            if (memberList.get(selection).getSignPic() != null && new File(FileUtil.PraseUritoPath(getContext(), memberList.get(selection).getSignPic())).exists()) {

                                                AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        RxToast.warning(MyApp.getApplictaion(), "已签名，不能再次签名！", Toast.LENGTH_SHORT, true).show();

                                                    }
                                                });
                                                signatureFile.delete();
                                            } else {
                                                memberList.get(selection).setSignPic(Uri.fromFile(signatureFile));
                                            }

                                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                            intent.setData(Uri.fromFile(signatureFile));
                                            MemberSignFragment.this.getActivity().sendBroadcast(intent);

                                            Intent resultIntent = new Intent();
                                            resultIntent.setData(Uri.fromFile(signatureFile));
                                            MemberSignFragment.this.getActivity().setResult(RESULT_OK, intent);



                                            AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                                                @Override
                                                public void run() {
                                                    memberAdapter.notifyDataSetChanged();
                                                    clearBtn.setVisibility(View.INVISIBLE);
                                                    saveBtn.setVisibility(View.INVISIBLE);
                                                }
                                            });


                                            saveLog(log);
                                        }
                                    } else {
                                        AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                                            @Override
                                            public void run() {
                                                RxToast.warning(MyApp.getApplictaion(), "未签名不能保存！", Toast.LENGTH_SHORT, true).show();

                                            }
                                        });

                                    }
                                }
                            });

                        } else {
                            RxToast.warning(MyApp.getApplictaion(), "请选择要签名的人员", Toast.LENGTH_SHORT, true).show();

                        }

                    }
                });

                rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogSureCancel.cancel();
                    }
                });
                rxDialogSureCancel.show();
            }
        });
    }

    private void saveLog(MissionLog log) {

        String logStr = DataUtil.toJson(log);

        //记录日志记录
        SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("logRecord", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(log.getTask_id(), logStr);
        editor.commit();
    }

    public void setLog(MissionLog log) {
        this.log = log;
        if (log != null) {
            this.memberList = log.getMembers();
        }
    }
}
