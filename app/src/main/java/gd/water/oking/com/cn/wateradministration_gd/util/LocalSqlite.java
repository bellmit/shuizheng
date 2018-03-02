package gd.water.oking.com.cn.wateradministration_gd.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhao on 2016-11-29.
 */

public class LocalSqlite extends SQLiteOpenHelper {

    public static final String USER_TABLE = "user";
    public static final String MISSION_TABLE = "mission";
    public static final String MEMBER_TABLE = "member";
    public static final String AREA_TABLE = "area";
    public static final String EQUIPMENT_TABLE = "equipment";
    public static final String CONTACTS_TABLE = "contacts";
    public static final String DEPARTMENT_TABLE = "department";
    public static final String CASE_TABLE = "mycase";
    public static final String QUESTION_TABLE = "question";
    private static final String DB_NAME = "gdWater.db";
    private static final int version = 3;//数据库版本  

    public LocalSqlite(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String userSqlStr = "CREATE TABLE " + USER_TABLE + " (uid VARCHAR PRIMARY KEY NOT NULL, deptId VARCHAR NOT NULL," +
                "userName VARCHAR ,deptName VARCHAR, " +
                " account VARCHAR, password VARCHAR, phone VARCHAR, profile BLOB, jsonStr TEXT)";
        String missionSqlStr = "CREATE TABLE " + MISSION_TABLE + " (mid VARCHAR PRIMARY KEY NOT NULL, task_name VARCHAR NOT NULL," +
                " delivery_time BIGINT NOT NULL, begin_time BIGINT NOT NULL, end_time BIGINT NOT NULL," +
                " publisher VARCHAR, task_type INTEGER, AreaId VARCHAR, assignment VARCHAR," +
                " status INTEGER, task_content VARCHAR, contact VARCHAR, create_time BIGINT," +
                " approved_person VARCHAR, approved_person_name VARCHAR, typename VARCHAR," +
                " receiver_name VARCHAR, publisher_name VARCHAR,approved_time BIGINT, receiver VARCHAR," +
                " execute_start_time BIGINT, execute_end_time BIGINT, spyj VARCHAR, rwqyms VARCHAR," +
                " jjcd VARCHAR, rwly VARCHAR, jsr VARCHAR, jsdw VARCHAR, fbr VARCHAR, fbdw VARCHAR, spr VARCHAR,typeoftask VARCHAR, jsonStr TEXT)";
        String memberSqlStr = "CREATE TABLE " + MEMBER_TABLE + " (meid VARCHAR PRIMARY KEY NOT NULL, task_id VARCHAR NOT NULL," +
                "userid VARCHAR NOT NULL, uName VARCHAR, jsonStr TEXT )";
        String areaSqlStr = "CREATE TABLE " + AREA_TABLE + " (aid VARCHAR PRIMARY KEY NOT NULL, task_id VARCHAR NOT NULL," +
                "area_type VARCHAR ,coordinate TEXT,jsonStr TEXT )";
        String equipmentSqlStr = "CREATE TABLE " + EQUIPMENT_TABLE + " (deptid VARCHAR NOT NULL," +
                " type VARCHAR, value VARCHAR, remarks VARCHAR, ly VARCHAR," +
                " type2 VARCHAR, mc1 VARCHAR, mc2 VARCHAR)";
        String contactSqlStr = "CREATE TABLE " + CONTACTS_TABLE + " (ctid VARCHAR PRIMARY KEY NOT NULL, name VARCHAR NOT NULL," +
                "deptid VARCHAR, deptname VARCHAR, phone VARCHAR, account VARCHAR, zfzh VARCHAR, jsonStr TEXT )";
        String departmentSqlStr = "CREATE TABLE " + DEPARTMENT_TABLE + " (deptId VARCHAR PRIMARY KEY NOT NULL, sortno INTEGER NOT NULL," +
                "parentid VARCHAR, customid VARCHAR,leaf VARCHAR,remark VARCHAR," +
                "enabled VARCHAR, deptname VARCHAR, jsonStr TEXT )";
        String caseSqlStr = "CREATE TABLE " + CASE_TABLE + " (AJID VARCHAR PRIMARY KEY NOT NULL, SLRQ BIGINT," +
                " ZFBM VARCHAR, SLR BIGINT, AJLX VARCHAR," +
                " AJMC VARCHAR, AY INTEGER, AFDD VARCHAR, AFSJ BIGINT," +
                " AQJY VARCHAR, XWZSD VARCHAR, JGD VARCHAR, SSD VARCHAR," +
                " WHJGFSD VARCHAR, DSRQK VARCHAR, SQWTR VARCHAR," +
                " FLYJ VARCHAR, CFYJ VARCHAR, CFNR VARCHAR, CBR1 VARCHAR," +
                " CBRDW1 VARCHAR, ZFZH1 VARCHAR, CBR2 VARCHAR, CBRDW2 VARCHAR, " +
                " ZFZH2 VARCHAR, ZT VARCHAR, AJLY VARCHAR, AJLXID VARCHAR, CBRID1 VARCHAR," +
                " CBRID2 VARCHAR, SLXX_ZT VARCHAR, jsonStr TEXT)";
        String questionSqlStr = "CREATE TABLE " + QUESTION_TABLE + " (XH VARCHAR PRIMARY KEY NOT NULL, WTLX VARCHAR NOT NULL," +
                "AJLX VARCHAR NOT NULL, WT VARCHAR NOT NULL,JQXX VARCHAR,ZT VARCHAR NOT NULL," +
                "PX VARCHAR NOT NULL, jsonStr TEXT )";


        db.execSQL(userSqlStr);
        db.execSQL(missionSqlStr);
        db.execSQL(memberSqlStr);
        db.execSQL(areaSqlStr);
        db.execSQL(equipmentSqlStr);
        db.execSQL(contactSqlStr);
        db.execSQL(departmentSqlStr);
        db.execSQL(caseSqlStr);
        db.execSQL(questionSqlStr);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (newVersion) {
            case 2:


                db.execSQL("drop table " + MISSION_TABLE);

                String missionSqlStr = "CREATE TABLE " + MISSION_TABLE + " (mid VARCHAR PRIMARY KEY NOT NULL, task_name VARCHAR NOT NULL," +
                        " delivery_time BIGINT NOT NULL, begin_time BIGINT NOT NULL, end_time BIGINT NOT NULL," +
                        " publisher VARCHAR, task_type INTEGER, AreaId VARCHAR, assignment VARCHAR," +
                        " status INTEGER, task_content VARCHAR, contact VARCHAR, create_time BIGINT," +
                        " approved_person VARCHAR, approved_person_name VARCHAR, typename VARCHAR," +
                        " receiver_name VARCHAR, publisher_name VARCHAR,approved_time BIGINT, receiver VARCHAR," +
                        " execute_start_time BIGINT, execute_end_time BIGINT, spyj VARCHAR, rwqyms VARCHAR," +
                        " jjcd VARCHAR, rwly VARCHAR, jsr VARCHAR, jsdw VARCHAR, fbr VARCHAR, fbdw VARCHAR, spr VARCHAR,typeoftask VARCHAR, jsonStr TEXT)";

                db.execSQL(missionSqlStr);
                break;
            case 3:
                db.execSQL("drop table " + EQUIPMENT_TABLE);
                String equipmentSqlStr = "CREATE TABLE " + EQUIPMENT_TABLE + " (deptid VARCHAR NOT NULL," +
                        " type VARCHAR, value VARCHAR, remarks VARCHAR, ly VARCHAR," +
                        " type2 VARCHAR, mc1 VARCHAR, mc2 VARCHAR)";
                db.execSQL(equipmentSqlStr);
                break;

            default:
                break;
        }
    }

    public Cursor select(String table, String[] columns, String selection,
                         String[] selectionArgs, String groupBy, String having,
                         String orderBy) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        return cursor;
    }

    public long insert(final String table, final ContentValues values) {

        SQLiteDatabase db = LocalSqlite.this.getWritableDatabase();
        return db.insert(table, null, values);
    }

    public int delete(final String table, final String whereClause, final String[] whereArgs) {

        SQLiteDatabase db = LocalSqlite.this.getWritableDatabase();
        return db.delete(table, whereClause, whereArgs);
    }

    public int update(final String table, final ContentValues values, final String whereClause, final String[] whereArgs) {

        SQLiteDatabase db = LocalSqlite.this.getWritableDatabase();
        return db.update(table, values, whereClause, whereArgs);
    }
}
