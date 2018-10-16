package com.fm.expensecalculator.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ExpenseDB extends SQLiteOpenHelper {

    public static String DB_NAME = "expense_calc.db";
    public static int DB_VERSION = 1;
    public static final String FIELD_ID = "id";
    public static final String FIELD__REMARKS = "remarks";
    public static final String FIELD_AMOUNT = "amount";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_IS_REGULAR = "is_regular";
    public static final String TABLE_NAME = "expense";

    private Context context;
    private SQLiteDatabase database;

    private static final String CREATE_TABLE = "create table if not exists " + TABLE_NAME
            + " ("
            + FIELD_ID + " integer primary key autoincrement,"
            + FIELD__REMARKS + " text,"
            + FIELD_AMOUNT + " text,"
            + FIELD_IS_REGULAR + " text,"
            + FIELD_DATE + " text" +
            ")";

    public ExpenseDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        this.database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addExpense(String amount, String remarks, String date, boolean checkedRadioButtonId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD__REMARKS, remarks);
        contentValues.put(FIELD_AMOUNT, amount);
        contentValues.put(FIELD_DATE, date);
        contentValues.put(FIELD_IS_REGULAR, checkedRadioButtonId);
        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }

    public ArrayList<ExpenseModel> getExpenses() {
        ArrayList<ExpenseModel> expenseModelList = new ArrayList<>();
        Cursor cursor = database.rawQuery("select *  from " + TABLE_NAME, null);
        if (cursor.getCount() <= 0)
            return null;

        cursor.moveToFirst();
        do {
            ExpenseModel expenseMode = new ExpenseModel();
            expenseMode.setId(cursor.getInt(cursor.getColumnIndex(FIELD_ID)));
            expenseMode.setRemarks(cursor.getString(cursor.getColumnIndex(FIELD__REMARKS)));
            expenseMode.setAmount(cursor.getString(cursor.getColumnIndex(FIELD_AMOUNT)));
            expenseMode.setDate(cursor.getString(cursor.getColumnIndex(FIELD_DATE)));
            expenseMode.setRegular(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(FIELD_IS_REGULAR))));
            expenseModelList.add(expenseMode);
        } while (cursor.moveToNext());
        cursor.close();
        database.close();
        return expenseModelList;
    }
}