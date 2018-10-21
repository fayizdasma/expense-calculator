package com.fm.expensecalculator.view;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fm.expensecalculator.R;
import com.fm.expensecalculator.adapters.ExpenseListAdapter;
import com.fm.expensecalculator.db.ExpenseDB;
import com.fm.expensecalculator.db.models.ExpenseModel;
import com.fm.expensecalculator.db.models.SheetModel;
import com.fm.expensecalculator.utils.AppConstants;

import java.util.ArrayList;

import static com.fm.expensecalculator.utils.AppConstants.SELECTED_MONTH;
import static com.fm.expensecalculator.utils.AppConstants.SELECTED_MONTH_ID;
import static com.fm.expensecalculator.utils.AppConstants.SELECTED_YEAR;

public class MonthDetailActivity extends AppCompatActivity {

    private ListView listView_months;
    private ExpenseListAdapter adapter;
    private TextView tv_income;
    private TextView tv_surplus;
    private TextView tv_month;
    private TextView tv_empty_info;
    private double income = 0;
    private String month_id;
    private String sel_month;
    private String sel_year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView_months = (ListView) findViewById(R.id.listview_months);
        tv_income = (TextView) findViewById(R.id.tv_income);
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_surplus = (TextView) findViewById(R.id.tv_surplus);
        tv_empty_info = (TextView) findViewById(R.id.tv_empty_info);

        month_id = getIntent().getStringExtra(SELECTED_MONTH_ID);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddExpenseActivity.class)
                        .putExtra(SELECTED_MONTH_ID, month_id)
                        .putExtra(SELECTED_MONTH, sel_month)
                        .putExtra(SELECTED_YEAR, sel_year));
            }
        });

        fetchIncomeFromDB();

        tv_income.setText("Income: " + AppConstants.CURRENCY + income);
        tv_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditIncomeDialog();
            }
        });

        fetchExpenseFromDB();
    }

    private void fetchIncomeFromDB() {
        SheetModel sheetData = new ExpenseDB(getApplicationContext()).getIncomeData(month_id);
        if (sheetData != null) {
            income = sheetData.getIncome();
            sel_month = sheetData.getMonth();
            sel_year = sheetData.getYear();
            tv_month.setText(sheetData.getMonth() + " " + sheetData.getYear());
            setTitle(sheetData.getMonth() + " " + sheetData.getYear());
        }
    }

    private void fetchExpenseFromDB() {
        ArrayList<ExpenseModel> expenseData = new ExpenseDB(getApplicationContext()).getExpenses(month_id);
        if (expenseData != null) {
            tv_empty_info.setVisibility(View.GONE);
            tv_surplus.setVisibility(View.VISIBLE);
            if (adapter == null) {
                adapter = new ExpenseListAdapter(this, expenseData);
                listView_months.setAdapter(adapter);
            } else {
                adapter.updateExpenseList(expenseData);
            }
            double total_amount = 0;
            for (int i = 0; i < expenseData.size(); i++) {
                total_amount = total_amount + Double.valueOf(expenseData.get(i).getAmount());
            }
            double surplus = income - total_amount;
            if (surplus > 0)
                tv_surplus.setText("Surplus: " + AppConstants.CURRENCY + surplus);
            else
                tv_surplus.setText("Deficit: " + AppConstants.CURRENCY + surplus);
        } else {
            tv_surplus.setVisibility(View.GONE);
            tv_empty_info.setVisibility(View.VISIBLE);
        }
    }

    private void showEditIncomeDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_income, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Edit Income");

        final EditText et_income = (EditText) dialogView.findViewById(R.id.et_income);
        et_income.setText(income + "");
        Button btn_update = (Button) dialogView.findViewById(R.id.btn_update);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_income.getText().toString().length() > 0) {
                    new ExpenseDB(getApplicationContext()).editIncome(income, month_id);
                    Toast.makeText(MonthDetailActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                } else
                    Toast.makeText(MonthDetailActivity.this, "Please enter the income", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchExpenseFromDB();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }
}