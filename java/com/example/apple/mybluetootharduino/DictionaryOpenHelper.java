package com.example.apple.mybluetootharduino;

/**
 * Created by caozj on 9/12/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DictionaryOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE_TEMPERATURE =
            "create table temperature (userid integer, tempvalue real NOT NULL DEFAULT (0), datatime TimeStamp NOT NULL DEFAULT (datetime('now','localtime')) )";
    private static final String CREATE_TABLE_FORCE =
            "create table force (userid integer,force1 real NOT NULL DEFAULT (0),force2 real NOT NULL DEFAULT (0)," +
                    "force3 real NOT NULL DEFAULT (0),force4 real NOT NULL DEFAULT (0),force5 real NOT NULL DEFAULT (0)," +
                    "force6 real NOT NULL DEFAULT (0),datatime TimeStamp NOT NULL DEFAULT (datetime('now','localtime')))";
    private static final String CREATE_TABLE_HEARTRATE =
            "create table heartrate (userid integer, heartrate integer, datatime TimeStamp NOT NULL DEFAULT (datetime('now','localtime')))";
    private static final String CREATE_TABLE_ACCELERATOR =
            "create table accelerator(userid integer, acc_x real NOT NULL DEFAULT(0), acc_y real NOT NULL DEFAULT(0)," +
                    "acc_z real NOT NULL DEFAULT(0), datatime TimeStamp NOT NULL DEFAULT (datetime('now','localtime')))";
    private static final String CREATE_TABLE_GYRO =
            "create table gyro(userid integer, temperature real, angle_speed_x real NOT NULL DEFAULT(0)," +
                    "angle_speed_y real NOT NULL DEFAULT(0), angle_speed_z real NOT NULL DEFAULT(0), angle_acc_x real NOT NULL DEFAULT(0)," +
                    "angle_acc_y real NOT NULL DEFAULT(0), angle_acc_z real NOT NULL DEFAULT(0), datatime TimeStamp NOT NULL DEFAULT (datetime('now','localtime')))";

    DictionaryOpenHelper(Context context) {
        super(context, "localdata.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL("drop table if exists temperature");
        db.execSQL("drop table if exists force");
        db.execSQL("drop table if exists accelerator");
        db.execSQL("drop table if exists heartrate");
        db.execSQL("drop table if exists gyro");
        db.execSQL(CREATE_TABLE_TEMPERATURE);
        db.execSQL(CREATE_TABLE_FORCE);
        db.execSQL(CREATE_TABLE_HEARTRATE);
        db.execSQL(CREATE_TABLE_ACCELERATOR);
        db.execSQL(CREATE_TABLE_GYRO);
    }

    //添加行
    public long temperatureAdd(int userid, double tempvalue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues temp = new ContentValues();
        temp.put("userid", userid);
        temp.put("tempvalue",tempvalue);
        return db.insert("temperature",null,temp);
    }
    public long forceAdd(int userid, double force1, double force2, double force3, double force4, double force5, double force6) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues temp = new ContentValues();
        temp.put("userid", userid);
        temp.put("force1",force1);
        temp.put("force2",force2);
        temp.put("force3",force3);
        temp.put("force4",force4);
        temp.put("force5",force5);
        temp.put("force6",force6);
        return db.insert("force",null,temp);
    }
    public long heartrateAdd(int userid, int heartrate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues temp = new ContentValues();
        temp.put("userid", userid);
        temp.put("heartrate",heartrate);
        return db.insert("heartrate",null,temp);
    }
    public long acceleratorAdd(int userid, double acc_x, double acc_y, double acc_z) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues temp = new ContentValues();
        temp.put("userid", userid);
        temp.put("acc_x", acc_x);
        temp.put("acc_y", acc_y);
        temp.put("acc_z", acc_z);
        return db.insert("accelerator",null,temp);
    }
    public long gyroAdd(int userid, double temperature, double angle_speed_x, double angle_speed_y, double angle_speed_z, double angle_acc_x, double angle_acc_y, double angle_acc_z) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues temp = new ContentValues();
        temp.put("userid", userid);
        temp.put("temperature", temperature);
        temp.put("angle_speed_x", angle_speed_x);
        temp.put("angle_speed_y", angle_speed_y);
        temp.put("angle_speed_z", angle_speed_z);
        temp.put("angle_acc_x", angle_acc_x);
        temp.put("angle_acc_y", angle_acc_y);
        temp.put("angle_acc_z", angle_acc_z);
        return db.insert("gyro",null,temp);
    }

    //获取数据总数
    public long countTemperature() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from temperature", null);
        cursor.moveToFirst();
        long result = cursor.getLong(0);
        return result;
    }
    public long countForce() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from force", null);
        cursor.moveToFirst();
        long result = cursor.getLong(0);
        return result;
    }
    public long countHeartrate() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from heartrate", null);
        cursor.moveToFirst();
        long result = cursor.getLong(0);
        return result;
    }
    public long countAccelerator() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from accelerator", null);
        cursor.moveToFirst();
        long result = cursor.getLong(0);
        return result;
    }
    public long countGyro() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from gyro", null);
        cursor.moveToFirst();
        long result = cursor.getLong(0);
        return result;
    }

    //获取最新数据
    public int getLastuseridFromTemperature() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from temperature", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("userid");
        int result = cursor.getInt(tempCL);
        return result;
    }
    public double getLasttempvalueFromTemperature() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from temperature", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("tempvalue");
        double result = cursor.getDouble(tempCL);
        return result;
    }
    public int getLastuseridFromForce() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from force", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("userid");
        int result = cursor.getInt(tempCL);
        return result;
    }
    public double getLastforce1FromForce() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from force", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("force1");
        double result = cursor.getDouble(tempCL);
        return result;
    }
    public double getLastforce2FromForce() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from force", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("force2");
        double result = cursor.getDouble(tempCL);
        return result;
    }
    public double getLastforce3FromForce() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from force", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("force3");
        double result = cursor.getDouble(tempCL);
        return result;
    }
    public double getLastforce4FromForce() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from force", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("force4");
        double result = cursor.getDouble(tempCL);
        return result;
    }
    public double getLastforce5FromForce() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from force", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("force5");
        double result = cursor.getDouble(tempCL);
        return result;
    }
    public double getLastforce6FromForce() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from force", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("force6");
        double result = cursor.getDouble(tempCL);
        return result;
    }
    public int getLastuseridFromHeartrate() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from heartrate", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("userid");
        int result = cursor.getInt(tempCL);
        return result;
    }
    public int getLasheartrateFromHeartrate() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from heartrate", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("heartrate");
        int result = cursor.getInt(tempCL);
        return result;
    }
    public int getLastuseridFromAccelerator() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from accelerator", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("userid");
        int result = cursor.getInt(tempCL);
        return result;
    }
    public double getLastacc_xFromAccelerator() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from accelerator", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("acc_x");
        double result = cursor.getDouble(tempCL);
        return result;
    }
    public double getLastacc_yFromAccelerator() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from accelerator", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("acc_y");
        double result = cursor.getDouble(tempCL);
        return result;
    }
    public double getLastacc_zFromAccelerator() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from accelerator", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("acc_z");
        double result = cursor.getDouble(tempCL);
        return result;
    }
    public int getLastuseridFromGyro() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from gyro", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("userid");
        int result = cursor.getInt(tempCL);
        return result;
    }
    public double getLasttemperatureFromGyro() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from gyro", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("temperature");
        double result = cursor.getDouble(tempCL);
        return result;
    }
    public double getLastangle_speed_xFromGyro() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from gyro", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("angle_speed_x");
        double result = cursor.getDouble(tempCL);
        return result;
    }
    public double getLastangle_speed_yFromGyro() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from gyro", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("angle_speed_y");
        double result = cursor.getDouble(tempCL);
        return result;
    }
    public double getLastangle_speed_zFromGyro() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from gyro", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("angle_speed_z");
        double result = cursor.getDouble(tempCL);
        return result;
    }
    public double getLastangle_acc_xFromGyro() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from gyro", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("angle_acc_x");
        double result = cursor.getDouble(tempCL);
        return result;
    }
    public double getLastangle_acc_yFromGyro() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from gyro", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("angle_acc_y");
        double result = cursor.getDouble(tempCL);
        return result;
    }
    public double getLastangle_acc_zFromGyro() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from gyro", null);
        cursor.moveToLast();
        int tempCL = cursor.getColumnIndex("angle_acc_z");
        double result = cursor.getDouble(tempCL);
        return result;
    }

    //清除表
    public void clearTemperature()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists temperature");
        db.execSQL(CREATE_TABLE_TEMPERATURE);
    }
    public void clearForce()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists force");
        db.execSQL(CREATE_TABLE_FORCE);
    }
    public void clearHeartrate()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists heartrate");
        db.execSQL(CREATE_TABLE_HEARTRATE);
    }
    public void clearAccelerator()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists accelerator");
        db.execSQL(CREATE_TABLE_ACCELERATOR);
    }
    public void clearGyro()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists gyro");
        db.execSQL(CREATE_TABLE_GYRO);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old_version, int new_version) {

    }
}
