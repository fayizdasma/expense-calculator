package com.fm.expensecalculator.view;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fm.expensecalculator.R;
import com.fm.expensecalculator.adapters.ExpenseListAdapter;
import com.fm.expensecalculator.db.ExpenseDB;
import com.fm.expensecalculator.db.ExpenseModel;
import com.fm.expensecalculator.utils.AppConstants;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView_months;
    private ExpenseListAdapter adapter;
    private TextView tv_income;
    private TextView tv_surplus;
    private TextView tv_empty_info;
    private double income = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView_months = (ListView) findViewById(R.id.listview_months);
        tv_income = (TextView) findViewById(R.id.tv_income);
        tv_surplus = (TextView) findViewById(R.id.tv_surplus);
        tv_empty_info = (TextView) findViewById(R.id.tv_empty_info);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddExpenseActivity.class));
            }
        });

        fetchIncomeFromDB();

        tv_income.setText("Income: " + AppConstants.CURRENCY + income);
        tv_surplus.setText("Surplus: " + AppConstants.CURRENCY + "0");
        tv_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(income);
            }
        });

        fetchExpenseFromDB();
    }

    private void fetchIncomeFromDB() {
        income = 600;
    }

    private void fetchExpenseFromDB() {
        ArrayList<ExpenseModel> expenseData = new ExpenseDB(getApplicationContext()).getExpenses();
        if (expenseData != null) {
            tv_empty_info.setVisibility(View.GONE);
            if (adapter == null) {
                adapter = new ExpenseListAdapter(this, expenseData);
                listView_months.setAdapter(adapter);
            } else {
                adapter.updateList(expenseData);
            }
            double total_amount = 0;
            for (int i = 0; i < expenseData.size(); i++) {
                total_amount = total_amount + Double.valueOf(expenseData.get(i).getAmount());
            }
            double surplus = income - total_amount;
            tv_surplus.setText("Surplus: "+AppConstants.CURRENCY + surplus);

        } else
            tv_empty_info.setVisibility(View.VISIBLE);
    }

    public void showEditDialog(double income) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_income, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Edit Income");

        final EditText et_income = (EditText) dialogView.findViewById(R.id.et_income);
        Button btn_update = (Button) dialogView.findViewById(R.id.btn_update);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_income.getText().toString().length() > 0) {
                    Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                } else
                    Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchExpenseFromDB();
    }
}