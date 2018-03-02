package gd.water.oking.com.cn.wateradministration_gd.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.bean.ChapterDomain;
import gd.water.oking.com.cn.wateradministration_gd.bean.LawBean;
import gd.water.oking.com.cn.wateradministration_gd.bean.LawsRegulation;
import gd.water.oking.com.cn.wateradministration_gd.bean.ProblemBean;
import gd.water.oking.com.cn.wateradministration_gd.bean.SectionDomain;

/**
 * Created by Administrator on 2017/10/27.
 */

public class LawDao {
    public static final String path = "/data/data/gd.water.oking.com.cn.wateradministration_gd/files/law.db";

    public static List<LawBean> getLaw() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

        String sql = "SELECT * FROM law_water";
        Cursor cursor = db.rawQuery(sql, null);
        List<LawBean> lawBeans = new ArrayList<LawBean>();
        while (cursor.moveToNext()) {
            LawBean bean = new LawBean();
            bean.setMmid(cursor.getString(cursor.getColumnIndex("mmid")));
            bean.setImplementationTime(cursor.getString(cursor.getColumnIndex("implementation_time")));
            bean.setLevelEffectiveness(cursor.getString(cursor.getColumnIndex("level_effectiveness")));
            bean.setPublishingDepartment(cursor.getString(cursor.getColumnIndex("publishing_department")));
            bean.setReleaseTime(cursor.getString(cursor.getColumnIndex("release_time")));
//            bean.setRowId(cursor.getLong(cursor.getColumnIndex("rowId")));
            bean.setRulesContent(cursor.getString(cursor.getColumnIndex("rules_content")));
            bean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            lawBeans.add(bean);
        }
        cursor.close();
        db.close();
        return lawBeans;
    }


    public static ArrayList<LawsRegulation> getLawChapter(String mmid) {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

        Cursor cursor = db.query("rules", new String[]{"chapter_directory", "item_title", "articles_content"}, "mmid=?", new String[]{mmid}, null, null, null);
        ArrayList<LawsRegulation> lawsRegulations = new ArrayList<>();
        ArrayList<ChapterDomain> chapterDomains = new ArrayList<>();
        ArrayList<SectionDomain> sectionDomains = new ArrayList<>();
        Cursor itemCursor = null;
        while (cursor.moveToNext()) {
            String articles_content = cursor.getString(cursor.getColumnIndex("articles_content"));
            String chapter_directory = cursor.getString(cursor.getColumnIndex("chapter_directory"));
            String item_title = cursor.getString(cursor.getColumnIndex("item_title"));
            LawsRegulation lawsRegulation = new LawsRegulation();
            lawsRegulation.setArticlesContent(articles_content);
            if (item_title == null && chapter_directory != null) {

                ChapterDomain chapterDomain = new ChapterDomain();
                sectionDomains = new ArrayList<>();
                chapterDomain.setChapterDirectory(chapter_directory);
                chapterDomain.setSection(sectionDomains);
                chapterDomains.add(chapterDomain);
                itemCursor = db.query("rules", new String[]{"item_title", "chapter_item"}, "chapter_directory=? and mmid=?", new String[]{chapter_directory, mmid}, null, null, null);

                lawsRegulation.setChapterDirectory(chapterDomains);
                lawsRegulations.add(lawsRegulation);
            } else {
                if (itemCursor != null) {
                    while (itemCursor.moveToNext()) {
                        String item = itemCursor.getString(itemCursor.getColumnIndex("item_title"));
                        String chapterItem = itemCursor.getString(itemCursor.getColumnIndex("chapter_item"));

                        if (item != null && chapterItem != null) {
                            SectionDomain sectionDomain = new SectionDomain();
                            sectionDomain.setChapterItem(chapterItem);
                            sectionDomain.setItemTitle(item);
                            sectionDomains.add(sectionDomain);
                        }
                    }

                }

            }


        }

        if (itemCursor != null) {

            itemCursor.close();
        }
        if (cursor != null) {

            cursor.close();
        }
        if (db != null) {

            db.close();
        }
        return lawsRegulations;

    }

    public static String getArticlesContent(String chapterItem) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

        Cursor cursor = db.query("rules", new String[]{"articles_content"}, "chapter_item=?", new String[]{chapterItem}, null, null, null);
        String articles_content = null;
        while (cursor.moveToNext()) {
            articles_content = cursor.getString(cursor.getColumnIndex("articles_content"));
        }
        cursor.close();
        db.close();
        return articles_content;
    }


    public static ArrayList<ProblemBean> getProblemContent(String typename) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

        //查询rowid必须使用此语句
        String sql = "select rowid, * from questions where typename = '"+typename+"'";
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<ProblemBean> problemBeans = new ArrayList<>();
        while (cursor.moveToNext()) {
            ProblemBean problemBean = new ProblemBean();
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String mtypename = cursor.getString(cursor.getColumnIndex("typename"));
            String mtype = cursor.getString(cursor.getColumnIndex("type"));
            int rowid = cursor.getInt(cursor.getColumnIndex("rowid"));
            problemBean.setRowid(rowid);
            problemBean.setAsk(content);
            problemBean.setTypename(mtypename);
            problemBean.setType(mtype);
            problemBeans.add(problemBean);

        }
        cursor.close();
        db.close();
        return problemBeans;
    }

    public static int deletProblemContent(int rowid) {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);

        int delete = db.delete("questions", "rowid=?", new String[]{rowid + ""});
        db.close();
        return delete;
    }

    public static long insetProblemContent(ProblemBean problemBean) {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        ContentValues values = new ContentValues();
        values.put("content",problemBean.getAsk());
        values.put("typename",problemBean.getTypename());
        values.put("type",problemBean.getType());
        long questions = db.insert("questions", null, values);
        db.close();
        return questions;
    }

    public static String getGdWaterContact(String phone) {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.query("contact",new String[]{"nick"},"phone=?",new String[]{phone},null,null,null);
        String nick = null;
        while (cursor.moveToNext()) {
            nick = cursor.getString(cursor.getColumnIndex("nick"));
        }
        cursor.close();
        db.close();
        return nick;

    }

//    public static void slectorXXX(String name,String code,String age){
//        String CName = "";
//        String CCode = "";
//        String CAge = "";
//        if (name!=null){
//            CName = "Name";
//        }
//
//        if (code==null){
//            CCode = "Code";
//        }
//
//        if (age==null){
//            CAge = "Age";
//        }
//        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
//        Cursor cursor = db.query("contact",new String[]{CName,CCode,CAge},CName+"=?"
//                ,new String[]{name,code,age},null,null,null);
//
//    }

}
