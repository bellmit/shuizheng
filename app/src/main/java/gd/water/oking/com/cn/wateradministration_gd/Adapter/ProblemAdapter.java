package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.ProblemBean;
import gd.water.oking.com.cn.wateradministration_gd.db.LawDao;
import gd.water.oking.com.cn.wateradministration_gd.fragment.WrittenRecordFragment;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * Created by Administrator on 2017/11/13.
 */

public class ProblemAdapter extends BaseAdapter {
    private ArrayList<ProblemBean> datas;
    private RxDialogSureCancel mRxDialogSureCancel;
    private WrittenRecordFragment mWrittenRecordFragment;
    private Activity mActivity;
    public ProblemAdapter(Activity mActivity,WrittenRecordFragment writtenRecordFragment) {
        this.mActivity = mActivity;
        this.mWrittenRecordFragment = writtenRecordFragment;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View contentView, ViewGroup viewGroup) {
        ViewHold viewHold;
        if (contentView == null) {
            viewHold = new ViewHold();
            contentView = View.inflate(mActivity, R.layout.problem_item, null);
            contentView.setTag(viewHold);
            AutoUtils.auto(contentView);
        }
        final ProblemBean problemBean = datas.get(position);
        viewHold = (ViewHold) contentView.getTag();
        viewHold.tv_content = contentView.findViewById(R.id.tv_content);
        viewHold.iv_delete = contentView.findViewById(R.id.iv_delete);
        viewHold.tv_typename = contentView.findViewById(R.id.tv_typename);
        viewHold.tv_content.setText(problemBean.getAsk());
        viewHold.tv_typename.setText(problemBean.getTypename());
        viewHold.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (problemBean.getType().equals("1") && problemBean.getRowid() > 5) {


                    if (mRxDialogSureCancel == null) {
                        mRxDialogSureCancel = new RxDialogSureCancel(mWrittenRecordFragment.getActivity());
                        mRxDialogSureCancel.setContent("确定删除此问题？");
                    }

                    mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int delete = LawDao.deletProblemContent(problemBean.getRowid());
                            if (delete == 1) {
                                RxToast.success(MyApp.getApplictaion(), "删除成功", Toast.LENGTH_SHORT).show();
                                datas.remove(position);
                                mWrittenRecordFragment.notyAskData();
                                notifyDataSetChanged();
                            } else {
                                RxToast.error(MyApp.getApplictaion(), "删除失败，该问题可能已经被删除了", Toast.LENGTH_SHORT).show();
                            }
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
                } else if (problemBean.getType().equals("2") && problemBean.getRowid() > 9) {
                    if (mRxDialogSureCancel == null) {
                        mRxDialogSureCancel = new RxDialogSureCancel(mWrittenRecordFragment.getActivity());
                        mRxDialogSureCancel.setContent("确定删除此问题？");
                    }

                    mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int delete = LawDao.deletProblemContent(problemBean.getRowid());
                            if (delete == 1) {
                                RxToast.success(MyApp.getApplictaion(), "删除成功", Toast.LENGTH_SHORT).show();
                                datas.remove(position);
                                mWrittenRecordFragment.notyAskData();
                                notifyDataSetChanged();
                            } else {
                                RxToast.error(MyApp.getApplictaion(), "删除失败，该问题可能已经被删除了", Toast.LENGTH_SHORT).show();
                            }
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
                } else {
                    RxToast.warning(MyApp.getApplictaion(), "系统模版问题不能删除", Toast.LENGTH_SHORT).show();

                }

            }
        });
        return contentView;
    }

    public void setDatas(ArrayList<ProblemBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    class ViewHold {
        private TextView tv_content;
        private TextView tv_typename;
        private ImageButton iv_delete;
    }
}
