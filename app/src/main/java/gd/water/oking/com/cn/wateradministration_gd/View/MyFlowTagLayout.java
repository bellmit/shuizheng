package gd.water.oking.com.cn.wateradministration_gd.View;

import android.content.Context;
import android.util.AttributeSet;

import com.hhl.library.FlowTagLayout;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.TagAdapter;

/**
 * Created by zhao on 2017-3-15.
 */

public class MyFlowTagLayout extends FlowTagLayout {
    
    public MyFlowTagLayout(Context context) {
        super(context);
    }

    public MyFlowTagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFlowTagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        ((TagAdapter) this.getAdapter()).notifyDataSetChanged();
    }


}
