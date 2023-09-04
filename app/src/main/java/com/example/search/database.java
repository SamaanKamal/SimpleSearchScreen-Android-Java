package com.example.search;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class database extends SQLiteOpenHelper {
    public static final String databaseName = "database";
    SQLiteDatabase database;

    public database(Context context) {
        super(context, databaseName, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table Customers " +
                "(C_id INTEGER Primary Key AUTOINCREMENT," +
                " C_Name TEXT not null," +
                " Username TEXT not null ," +
                " Password TEXT not null ," +
                " Birthdate TEXT not null ," +
                " flag INTEGER DEFAULT 0 ," +
                " job TEXT , gender TEXT not null )");

        db.execSQL("Create Table Categories " +
                "(Cat_id INTEGER Primary Key AUTOINCREMENT ," +
                " Cat_name TEXT not null )");

//        db.execSQL("Create Table Products " +
//                "(P_id INTEGER Primary Key ِِAUTOINCREMENT ," +
//                " P_name TEXT ," +
//                " Price Float  ," +
//                " Quantity INTEGER ," +
//                " Fk_Cat_id INTEGER ," +
//                " Foreign Key(Fk_Cat_id) References Categories(Cat_id))");

        db.execSQL("Create Table Products(P_id INTEGER  PRIMARY KEY AUTOINCREMENT , P_name TEXT ,Price Float  ,Quantity INTEGER ,Fk_Cat_id INTEGER, Foreign Key(Fk_Cat_id) References Categories(Cat_id))");


        db.execSQL("Create Table Orders " +
                " (O_id Integer Primary Key AUTOINCREMENT ," +
                " Order_date date ," +
                " Latitude number ," +
                " Longitude number ," +
                " name text not null ," +
                " Cust_id integer ," +
                "Foreign Key(Cust_id) References Customers(C_id) )");

        db.execSQL("create table order_details " +
                " (Ord_id integer not null," +
                " prod_ID integer not null," +
                " cat_id integer not null," +
                " quantity integer not null," +
                " PRIMARY KEY (Ord_id,prod_ID)," +
                " FOREIGN KEY(Ord_id) REFERENCES Orders(O_id)," +
                " FOREIGN KEY(cat_id) REFERENCES Categories(Cat_id)," +
                " FOREIGN KEY(prod_ID) REFERENCES Products(P_id))");

        db.execSQL("create table Cart" +
                "(pro_ID integer not null ," +
                "qty integer not null ," +
                "cat_id integer not null," +
                "PRIMARY KEY (pro_ID,cat_id))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop Table if Exists Customers");
        db.execSQL("Drop Table if Exists Categories");
        db.execSQL("Drop Table if Exists Products");
        db.execSQL("Drop Table if Exists Orders");
        db.execSQL("Drop Table if Exists OrderDetails");
        onCreate(db);
    }
    public Cursor Select_Products(int id) {
        database = getReadableDatabase();
        Cursor c = database.rawQuery("Select * From Products where Fk_Cat_id Like '" + id + "'", null);
        if (c != null) {
            c.moveToFirst();
        }
        database.close();
        //c.close();
        return c;
    }

    public Cursor getProductInfo(int productID, int cat_id) {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from Products where P_id like '" + productID + "' AND Fk_Cat_id like  '" + cat_id + "' ", null);
        if (cursor.getCount() != 0)
            cursor.moveToFirst();
        database.close();
        return cursor;
    }
    public Cursor fetchCart() {
        database = getReadableDatabase();
        String[] rowDetails = {"pro_ID", "qty", "cat_id"};
        Cursor cur = database.query("Cart", rowDetails, null, null, null, null, null);
        if (cur != null)
            cur.moveToFirst();
        database.close();
        return cur;
    }
    public void deleteItem(int id) {
        database = getWritableDatabase();
        database.delete("Cart", "pro_ID='" + id + "'", null);
        database.close();
    }
    public int getQuantity(int id, int cat_id) {
        database = getReadableDatabase();
        @SuppressLint("Recycle") Cursor cur = database.rawQuery("select qty from Cart where pro_ID like '" + id + "'AND cat_id like  '" + cat_id + "' ", null);
        Integer qnty = null;
        if (cur != null) {
            cur.moveToFirst();
            qnty = cur.getInt(0);
        }
        database.close();
        return qnty;
    }

    public String getProductPrice(int productID, int cat_id) {
        database = getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = database.rawQuery("select Price from Products where P_id like '" + productID + "' AND Fk_Cat_id like  '" + cat_id + "' ", null);
        String price = null;
        if (cursor != null) {
            cursor.moveToFirst();
            price = cursor.getString(0);
        }
        database.close();
        return price;
    }

    public void addToCart(int id, int cat_id, int q) {
        database = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("pro_ID", id);
        row.put("qty", q);
        row.put("cat_id", cat_id);
        database.insert("Cart", null, row);
        database.close();
    }
    public void editQuantity(int id, int newQuantity) {
        database = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("qty", newQuantity);
        database.update("Cart", row, "pro_ID like '" + id + "' ", null);
        database.close();
    }
    public void AddCategories() {
        ContentValues row1 = new ContentValues();
        database=getWritableDatabase();
        row1.put("Cat_name","Devices");
        database.insert("Categories",null,row1);

        ContentValues row2 = new ContentValues();
        database=getWritableDatabase();
        row2.put("Cat_name","؛Food");
        database.insert("Categories",null,row2);

        database.close();
    }

    public void CreateProducts() {
        ContentValues row1 = new ContentValues();
        database=getWritableDatabase();
        row1.put("P_name","Laptop");
        row1.put("Price","500");
        row1.put("Quantity","4");
        database.insert("Products",null,row1);

        ContentValues row2 = new ContentValues();
        database=getWritableDatabase();
        row2.put("P_name","؛Phone");
        row2.put("Price","200");
        row2.put("Quantity","6");
        database.insert("Products",null,row2);

        database.close();
    }

    public Cursor getProducts(String choice) {
        database = getReadableDatabase();
        //String args[] ={choice};
        Cursor cursor = database.rawQuery("select * from Products where P_name like  '%" + choice +"%'", null);
        if (cursor.getCount() != 0)
            cursor.moveToFirst();
        database.close();
        return cursor;
    }
}
