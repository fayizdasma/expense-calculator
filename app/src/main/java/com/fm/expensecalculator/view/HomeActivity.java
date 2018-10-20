package com.fm.expensecalculator.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fm.expensecalculator.R;
import com.fm.expensecalculator.adapters.ExpenseListAdapter;
import com.fm.expensecalculator.adapters.MonthlyListAdapter;
import com.fm.expensecalculator.db.ExpenseDB;
import com.fm.expensecalculator.db.models.ExpenseModel;
import com.fm.expensecalculator.db.models.SheetModel;
import com.fm.expensecalculator.utils.AppConstants;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ListView listview_annual;
    private MonthlyListAdapter adapter;
    private TextView tv_empty_info;
    private double income = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listview_annual = (ListView) findViewById(R.id.listview_annual);
        tv_empty_info = (TextView) findViewById(R.id.tv_empty_info);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddNewSheetDialog();
            }
        });

        fetchSheetsFromDB();

        listview_annual.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HomeActivity.this, "MonthID " + parent.getAdapter().getItemId(position), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private void fetchSheetsFromDB() {
        ArrayList<SheetModel> sheetData = new ExpenseDB(getApplicationContext()).getSheets();
        if (sheetData != null) {
            tv_empty_info.setVisibility(View.GONE);
            if (adapter == null) {
                adapter = new MonthlyListAdapter(this, sheetData);
                listview_annual.setAdapter(adapter);
            } else {
                adapter.updateSheetList(sheetData);
            }
        } else
            tv_empty_info.setVisibility(View.VISIBLE);
    }


    private void showAddNewSheetDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_new_sheet, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Add New Sheet");

        final EditText et_income = (EditText) dialogView.findViewById(R.id.et_income);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
        Button btn_update = (Button) dialogView.findViewById(R.id.btn_add);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_income.getText().toString().length() > 0) {
                    new ExpenseDB(getApplicationContext()).addNewSheet(String.valueOf(datePicker.getMonth()), String.valueOf(datePicker.getYear()), Double.parseDouble(et_income.getText().toString()));
                    Toast.makeText(HomeActivity.this, "added new sheet", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    onResume();
                } else
                    Toast.makeText(HomeActivity.this, "enter income", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchSheetsFromDB();
    }
}