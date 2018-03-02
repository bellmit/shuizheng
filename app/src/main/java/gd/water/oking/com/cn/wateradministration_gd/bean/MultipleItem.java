package gd.water.oking.com.cn.wateradministration_gd.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/2/6.
 */

public class MultipleItem implements MultiItemEntity {
    private int itemType;
    private ArrayList<ArrayList<Item>>mArrayLists ;
    private ArrayList<Item> mItems;

    public MultipleItem(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }



    public static class Item{
        private boolean isok;
        private String title;

        public boolean isIsok() {
            return isok;
        }

        public void setIsok(boolean isok) {
            this.isok = isok;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public ArrayList<ArrayList<Item>> getArrayLists() {
        return mArrayLists;
    }

    public void setArrayLists(ArrayList<ArrayList<Item>> arrayLists) {
        mArrayLists = arrayLists;
    }
}
