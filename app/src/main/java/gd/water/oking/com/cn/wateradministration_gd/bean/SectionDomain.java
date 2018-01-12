package gd.water.oking.com.cn.wateradministration_gd.bean;

/**
 * Created by Administrator on 2017/10/31.
 */

public class SectionDomain {
    private String chapterItem;
    private String itemTitle;

    public String getChapterItem() {
        return chapterItem;
    }

    public void setChapterItem(String chapterItem) {
        this.chapterItem = chapterItem;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    @Override
    public String toString() {
        return "SectionDomain{" +
                "chapterItem='" + chapterItem + '\'' +
                ", itemTitle='" + itemTitle + '\'' +
                '}';
    }
}
