package gd.water.oking.com.cn.wateradministration_gd.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

import org.xclcharts.chart.DountChart;
import org.xclcharts.chart.PieData;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.view.ChartView;

import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.R;

/**
 * Created by zhao on 2017-5-2.
 */

public class DountChartMissionView extends ChartView {

    private DountChart chart = new DountChart();
    private ArrayList<PieData> chartData = new ArrayList<>();

    public DountChartMissionView(Context context) {
        super(context);
        initView();
    }

    public DountChartMissionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DountChartMissionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        chart.setPadding(20, 20, 20, 20);

        if (chartData != null && chartData.size() > 1) {

            chart.setDataSource(chartData);

            chart.setCenterText(chartData.get(1).getPercentage() + "%\n未完成");
            chart.getCenterTextPaint().setColor(getResources().getColor(R.color.colorMain1));

            //标签显示(隐藏，显示在中间，显示在扇区外面)
            chart.setLabelStyle(XEnum.SliceLabelStyle.HIDE);
            chart.getCenterTextPaint().setTextSize(20f);
            chart.getLabelPaint().setColor(Color.WHITE);

            //显示图例
//            PlotLegend legend = chart.getPlotLegend();
//            legend.show();
//            legend.setType(XEnum.LegendType.ROW);
//            legend.setHorizontalAlign(XEnum.HorizontalAlign.LEFT);
//            legend.setVerticalAlign(XEnum.VerticalAlign.MIDDLE);

            //图背景色
            chart.setApplyBackgroundColor(true);
            chart.setBackgroundColor(getResources().getColor(R.color.colorMain2));

//            //内环背景色
//            chart.getInnerPaint().setColor(Color.rgb(19, 163, 224));

            //保存标签位置
            chart.saveLabelsPosition(XEnum.LabelSaveType.ALL);

            //激活点击监听
            chart.ActiveListenItemClick();
            chart.showClikedFocus();

            chart.setInnerRadius(0.6f);

            chart.disablePanMode();
            chart.disableScale();

            this.bindTouch(this, chart);
//        }
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        chart.setChartRange(w, h);
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        try {
            chart.render(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setChartData(ArrayList<PieData> chartData) {
        this.chartData = chartData;
        initView();
    }
}
