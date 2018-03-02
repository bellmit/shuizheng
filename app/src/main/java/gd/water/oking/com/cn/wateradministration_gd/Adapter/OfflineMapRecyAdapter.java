package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.OfflineMapDownload;

/**
 * Created by Administrator on 2018/2/11.
 */

public class OfflineMapRecyAdapter extends BaseQuickAdapter<OfflineMapDownload, BaseViewHolder> {
    public OfflineMapRecyAdapter(int layoutResId, @Nullable List<OfflineMapDownload> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OfflineMapDownload item) {
        helper.setText(R.id.cityName_tv, item.getCity().getCity());
        helper.addOnClickListener(R.id.download_btn);
        helper.setProgress(R.id.progress_bar, item.getProgress());

        switch (item.getState()) {
            case OfflineMapDownload.Normal:
                helper.setVisible(R.id.progress_bar,false);
                helper.setVisible(R.id.download_btn,false);
                break;
            case OfflineMapDownload.OnDownload:
                helper.setText(R.id.state_tv,"下载中："+item.getProgress()+"%");
                helper.setVisible(R.id.progress_bar,true);
                helper.setVisible(R.id.download_btn,false);
                break;
            case OfflineMapDownload.OnUnZIP:
                helper.setText(R.id.state_tv,"解压中："+item.getProgress()+"%");
                helper.setVisible(R.id.progress_bar,true);
                helper.setVisible(R.id.download_btn,false);
                break;
            case OfflineMapDownload.NewVersionOrUnDownload:
                helper.setVisible(R.id.progress_bar,false);
                helper.setVisible(R.id.download_btn,true);
                break;
            case OfflineMapDownload.Waiting:
                helper.setText(R.id.state_tv,"等待下载");
                helper.setVisible(R.id.progress_bar,false);
                helper.setVisible(R.id.download_btn,false);
                break;
            default:
                break;
        }


    }
}
