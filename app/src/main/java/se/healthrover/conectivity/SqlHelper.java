package se.healthrover.conectivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.List;

import se.healthrover.entities.Car;
import se.healthrover.entities.ObjectFactory;

public class SqlHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "healthrover.db";
    private static final String DATABASE_TABLE_NAME = "healthrover_name_table";
    private static final String DATABASE_COL_ID = "ID";
    private static final String DATABASE_COL_URL = "URL";
    private static final String DATABASE_COL_NAME = "name";
    private SQLiteDatabase database;


    public SqlHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(SQLiteDatabase db) {
        SQLiteStatement stmt = db.compileStatement("CREATE TABLE "+DATABASE_TABLE_NAME+" ("+DATABASE_COL_ID+" SERIAL PRIMARY KEY, "+DATABASE_COL_URL+" VARCHAR, "+ DATABASE_COL_NAME+" VARCHAR);");
        stmt.execute();
            //db.execSQL("CREATE TABLE healthrover_name_table (ID SERIAL PRIMARY KEY, URL VARCHAR, name VARCHAR);");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SQLiteStatement stmt = db.compileStatement("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME + ";");
            stmt.execute();
            onCreate(db);
    }

    public void deleteTable(){
        database = this.getWritableDatabase();
        SQLiteStatement statement = database.compileStatement("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME + ";");
        statement.execute();
    }




    public List<Car> getSavedCars(){
        database = this.getReadableDatabase();
        List<Car> cars = ObjectFactory.getInstance().getCarList();
        @SuppressLint("Recycle") Cursor cursor = database.rawQuery("Select * from " + DATABASE_TABLE_NAME + ";", null);
        if (cursor.getCount() == 0){
            return null;
        }
        else {
            while (cursor.moveToNext()){
                Car car = ObjectFactory.getInstance().makeCar( cursor.getString(1),cursor.getString(2));
                car.setID(cursor.getString(0));
                cars.add(car);

            }
            return cars;
        }
    }
    public Car getCarByName(String name){
        database = this.getReadableDatabase();
        List<Car> cars = ObjectFactory.getInstance().getCarList();
        Car carToReturn;
        @SuppressLint("Recycle") Cursor cursor = database.rawQuery("Select * from " + DATABASE_TABLE_NAME + " WHERE "+ DATABASE_COL_NAME+"="+name+";", null);
        if (cursor.getCount() == 0){
            return null;
        }
        else {
            while (cursor.moveToNext()){
                Car car = ObjectFactory.getInstance().makeCar( cursor.getString(1),cursor.getString(2));
                car.setID(cursor.getString(0));
                cars.add(car);
            }
            if (cars.size()!=1){
                return null;
            }
            else {
                return carToReturn = cars.get(0);
            }
        }
    }
    public void insertData(Car car){
        database = this.getWritableDatabase();
        database.beginTransaction();
        SQLiteStatement statement = database.compileStatement("INSERT INTO "+DATABASE_TABLE_NAME+" ("+DATABASE_COL_URL+", "+DATABASE_COL_NAME+") VALUES (?, ?)");
        statement.clearBindings();
        statement.bindString(1,car.getURL());
        statement.bindString(2, car.getName());
        statement.executeInsert();
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public void updateNameByURL(Car car){
        database = this.getWritableDatabase();
        SQLiteStatement statement = database.compileStatement("UPDATE "+DATABASE_TABLE_NAME+" SET "+DATABASE_COL_NAME+"=? "+"WHERE " + DATABASE_COL_URL +"=" + car.getURL());
        statement.bindString(2, car.getName());
        statement.executeUpdateDelete();

    }

    public void updateUrlByName(Car car){
        database = this.getWritableDatabase();
        SQLiteStatement statement = database.compileStatement("UPDATE "+DATABASE_TABLE_NAME+" SET "+DATABASE_COL_URL+"=? "+"WHERE " + DATABASE_TABLE_NAME +"=" + car.getName());
        statement.bindString(2, car.getURL());
        statement.executeUpdateDelete();
    }



}
