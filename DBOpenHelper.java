package com.example.menu;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.jar.Attributes;


public class DBOpenHelper {
    private static final String DATABASE_NAME = "DATABASE2.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase db;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DataBases._CREATE);
            db.execSQL(DataBases._CREATE2);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DataBases._TABLENAME1);
            db.execSQL("DROP TABLE IF EXISTS "+DataBases._TABLENAME2);
            onCreate(db);
        }
    }

    public DBOpenHelper(Context context){
        this.mCtx = context;
    }

    public DBOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        db = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        mDBHelper.onCreate(db);
    }

    public void close(){
        db.close();
    }

    // Insert DB
    public long insertColumn(String Name, int Height, int Weight, int Age, String Sex){
        ContentValues values = new ContentValues();
        values.put(DataBases.UserID,Name);
        values.put(DataBases.HEIGHT, Height);
        values.put(DataBases.WEIGHT, Weight);
        values.put(DataBases.AGE, Age);
        values.put(DataBases.SEX, Sex);
        return db.insert(DataBases._TABLENAME1, null, values);
    }

    public long insert_StepData(String Name, String Date, int Step){
        ContentValues values = new ContentValues();
        values.put(DataBases.UserID,Name);
        values.put(DataBases.DATE, Date);
        values.put(DataBases.STEPS, Step);
        return db.insert(DataBases._TABLENAME2, null, values);

    }


    // Delete All
    public void deleteAllColumns() {
        db.delete(DataBases._TABLENAME1, null, null);
    }

    // Delete DB
    public boolean deleteColumn(long id){
        return db.delete(DataBases._TABLENAME1, "_id="+id, null) > 0;
    }
    // Select DB
    public Cursor selectColumns(){
        return db.query(DataBases._TABLENAME1, null, null, null, null, null, null);
    }

    // sort by column
    public Cursor sortColumn(String userid){
        Cursor c = db.rawQuery( "SELECT date, steps FROM "+DataBases._TABLENAME2+" WHERE userid = '"+userid+"' ORDER BY date;", null);
        return c;
    }
    public void Update_steps(String Name, String time, int step){
        String sql = "update stepsRecord set steps = "+step+ " where userid =  '"+Name+"' and date = '"+time+"';";
        db.execSQL(sql);
    }

    public void Update_profile(String Name, int height, int weight, int age, String sex){
        String sql = "update information set height = "+height+ ",weight = "+weight+", age = "+age+", sex = '"+sex+"' where userid =  '"+Name+"';";
        db.execSQL(sql);
    }
    public Cursor sortColumn_profile(String userid){
        Cursor c = db.rawQuery( "SELECT * FROM "+DataBases._TABLENAME1+" WHERE userid = '"+userid+"';", null);
        return c;
    }
    public Cursor sortColumn_date(String userid){
        Cursor c = db.rawQuery( "SELECT date FROM "+DataBases._TABLENAME2+" WHERE userid = '"+userid+"' ORDER BY date;", null);
        return c;
    }
}
