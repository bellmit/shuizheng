package gd.water.oking.com.cn.wateradministration_gd.BaseView;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

/**
 * Created by zhao on 2016/9/23.
 */

public abstract class AutoCompleteEditText<T> extends android.support.v7.widget.AppCompatEditText implements Filterable,TextWatcher{

    private ArrayList<T> dataList;

    public AutoCompleteEditText(Context context) {
        super(context);
    }

    public AutoCompleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterData(constraint,dataList);
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                showFilterData(constraint,(ArrayList<T>)results.values);
            }
        };
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if(dataList != null) {
            getFilter().filter(text);
        }
    }


    /**  筛选的操作过程
     * @param constraint 筛选输入的条件
     * @param dataList  筛选前的原始数据集
     * @return 筛选后的数据集
     */
    public abstract ArrayList filterData(CharSequence constraint,ArrayList<T> dataList);


    /**  用于执行筛选后的操作
     * @param constraint 筛选输入的条件
     * @param filterDataList 筛选后的数据集
     */
    public abstract void showFilterData(CharSequence constraint,ArrayList<T> filterDataList);

    public void setDataList(ArrayList<T> dataList) {
        this.dataList = dataList;
    }

}
