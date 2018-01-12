package gd.water.oking.com.cn.wateradministration_gd.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/31.
 */

public class LawsRegulation {
    private String articlesContent;
    private String mmId;
    private ArrayList<ChapterDomain> chapterDirectory;

    public String getArticlesContent() {
        return articlesContent;
    }

    public void setArticlesContent(String articlesContent) {
        this.articlesContent = articlesContent;
    }

    public String getMmId() {
        return mmId;
    }

    public void setMmId(String mmId) {
        this.mmId = mmId;
    }

    public ArrayList<ChapterDomain> getChapterDirectory() {
        return chapterDirectory;
    }

    public void setChapterDirectory(ArrayList<ChapterDomain> chapterDirectory) {
        this.chapterDirectory = chapterDirectory;
    }

    @Override
    public String toString() {
        return "LawsRegulation{" +
                "articlesContent='" + articlesContent + '\'' +
                ", mmId='" + mmId + '\'' +
                ", chapterDirectory=" + chapterDirectory +
                '}';
    }
}
