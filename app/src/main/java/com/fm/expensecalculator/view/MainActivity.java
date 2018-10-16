package com.fm.expensecalculator.view;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.fm.expensecalculator.R;
import com.fm.expensecalculator.adapters.ExpenseListAdapter;
import com.fm.expensecalculator.db.ExpenseDB;
import com.fm.expensecalculator.db.ExpenseModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView_months;
    private ExpenseListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView_months = (ListView) findViewById(R.id.listview_months);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddExpenseActivity.class));
            }
        });

        fetchExpenseFromDB();
    }

    private void fetchExpenseFromDB() {
        ArrayList<ExpenseModel> expenseData = new ExpenseDB(getApplicationContext()).getExpenses();
        if (expenseData != null) {
            if (adapter == null) {
                adapter = new ExpenseListAdapter(this, expenseData);
                listView_months.setAdapter(adapter);
            } else {
                adapter.updateList(expenseData);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchExpenseFromDB();
    }
}