package com.fm.expensecalculator.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fm.expensecalculator.db.models.ExpenseModel;
import com.fm.expensecalculator.db.models.SheetModel;

import java.util.ArrayList;

public class ExpenseDB extends SQLiteOpenHelper {

    private static String DB_NAME = "expense_calc.db";
    private static int DB_VERSION = 1;
    private static final String TABLE_EXPENSE = "expense";
    private static final String FIELD_ID = "id_expense";
    private static final String FIELD__REMARKS = "remarks";
    private static final String FIELD_AMOUNT = "amount";
    private static final String FIELD_DATE = "date";
    private static final String FIELD_IS_REGULAR = "is_regular";

    private static final String TABLE_SHEETS = "sheets";
    private static final String FIELD_MONTH_ID = "id_month";
    private static final String FIELD_MONTH = "month";
    private static final String FIELD_YEAR = "year";
    private static final String FIELD_INCOME = "income";

    private Context context;
    private SQLiteDatabase database;


    private static final String CREATE_TABLE_EXPENSE = "create table if not exists " + TABLE_EXPENSE
            + " ("
            + FIELD_ID + " integer primary key autoincrement,"
            + FIELD__REMARKS + " text,"
            + FIELD_AMOUNT + " text,"
            + FIELD_IS_REGULAR + " text,"
            + FIELD_DATE + " text,"
            + FIELD_MONTH_ID + " integer"
            + ")";

    private static final String CREATE_TABLE_SHEETS = "create table if not exists " + TABLE_SHEETS
            + " ("
            + FIELD_MONTH_ID + " integer primary key autoincrement,"
            + FIELD_MONTH + " text,"
            + FIELD_YEAR + " text,"
            + FIELD_INCOME + " double"
            + ")";

    public ExpenseDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        this.database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SHEETS);
        db.execSQL(CREATE_TABLE_EXPENSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHEETS);

        onCreate(db);
    }

    //add a new expense entry in expense table
    public void addExpense(String amount, String remarks, String date, boolean bool) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD__REMARKS, remarks);
        contentValues.put(FIELD_AMOUNT, amount);
        contentValues.put(FIELD_DATE, date);
        contentValues.put(FIELD_IS_REGULAR, bool);
        database.insert(TABLE_EXPENSE, null, contentValues);
        database.close();
    }

    //add a new sheet in sheets table
    public void addNewSheet(String month, String year, double income) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_MONTH, month);
        contentValues.put(FIELD_YEAR, year);
        contentValues.put(FIELD_INCOME, income);
        database.insert(TABLE_SHEETS, null, contentValues);
        database.close();
    }

    //returns the list of all expenses from the expense table
    public ArrayList<ExpenseModel> getExpenses() {
        ArrayList<ExpenseModel> expenseModelList = new ArrayList<>();
        Cursor cursor = database.rawQuery("select *  from " + TABLE_EXPENSE, null);
        if (cursor.getCount() <= 0)
            return null;

        cursor.moveToFirst();
        do {
            ExpenseModel expenseMode = new ExpenseModel();
            expenseMode.setId(cursor.getInt(cursor.getColumnIndex(FIELD_ID)));
            expenseMode.setRemarks(cursor.getString(cursor.getColumnIndex(FIELD__REMARKS)));
            expenseMode.setAmount(cursor.getString(cursor.getColumnIndex(FIELD_AMOUNT)));
            expenseMode.setDate(cursor.getString(cursor.getColumnIndex(FIELD_DATE)));
            expenseMode.setRegular(getBooleanFormat(cursor.getInt(cursor.getColumnIndex(FIELD_IS_REGULAR))));
            expenseModelList.add(expenseMode);
        } while (cursor.moveToNext());
        cursor.close();
        database.close();
        return expenseModelList;
    }

    //returns the list of all sheets from the sheet table
    public ArrayList<SheetModel> getSheets() {
        ArrayList<SheetModel> sheetModelList = new ArrayList<>();
        Cursor cursor = database.rawQuery("select *  from " + TABLE_SHEETS, null);
        if (cursor.getCount() <= 0)
            return null;

        cursor.moveToFirst();
        do {
            SheetModel sheetModel = new SheetModel();
            sheetModel.setId(cursor.getInt(cursor.getColumnIndex(FIELD_MONTH_ID)));
            sheetModel.setIncome(cursor.getDouble(cursor.getColumnIndex(FIELD_INCOME)));
            sheetModel.setMonth(cursor.getString(cursor.getColumnIndex(FIELD_MONTH)));
            sheetModel.setYear(cursor.getString(cursor.getColumnIndex(FIELD_YEAR)));
            sheetModelList.add(sheetModel);
        } while (cursor.moveToNext());
        cursor.close();
        database.close();
        return sheetModelList;
    }

    //converts sqlite boolean value from 0/1 to true/false
    private boolean getBooleanFormat(int x) {
        return x == 1;
    }

}