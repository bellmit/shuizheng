package gd.water.oking.com.cn.wateradministration_gd.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/31.
 */

public class ChapterDomain {
    private String chapterDirectory;
    private ArrayList<SectionDomain> section;

    public String getChapterDirectory() {
        return chapterDirectory;
    }

    public void setChapterDirectory(String chapterDirectory) {
        this.chapterDirectory = chapterDirectory;
    }

    public ArrayList<SectionDomain> getSection() {
        return section;
    }

    public void setSection(ArrayList<SectionDomain> section) {
        this.section = section;
    }
}
