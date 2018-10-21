package com.fm.expensecalculator.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.fm.expensecalculator.R;
import com.fm.expensecalculator.db.ExpenseDB;

import java.util.Calendar;
import java.util.Currency;

import static com.fm.expensecalculator.utils.AppConstants.SELECTED_MONTH;
import static com.fm.expensecalculator.utils.AppConstants.SELECTED_MONTH_ID;
import static com.fm.expensecalculator.utils.AppConstants.SELECTED_YEAR;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText et_remarks, et_amount;
    private Button btn_save;
    private DatePicker datePicker;
    private RadioButton rb_regular;
    private String sel_month_id;
    private String sel_month;
    private String sel_year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        setTitle("Add Expense");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        et_remarks = (EditText) findViewById(R.id.et_remarks);
        et_amount = (EditText) findViewById(R.id.et_amount);
        btn_save = (Button) findViewById(R.id.btn_save);
        datePicker = (DatePicker) findViewById(R.id.date_picker);
        rb_regular = (RadioButton) findViewById(R.id.rb_reg);

        sel_month_id = getIntent().getStringExtra(SELECTED_MONTH_ID);
        sel_month = getIntent().getStringExtra(SELECTED_MONTH);
        sel_year = getIntent().getStringExtra(SELECTED_YEAR);


        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(sel_year), getMonthNumber(sel_month),c.getTime().getDay());
        c.add(Calendar.MONTH,0);
        datePicker.setMinDate(c.getTimeInMillis());
        c.add(Calendar.MONTH, 0);
        datePicker.setMaxDate(c.getTimeInMillis());

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    new ExpenseDB(getApplicationContext()).addExpense(et_amount.getText().toString(), et_remarks.getText().toString(), String.valueOf(datePicker.getDayOfMonth()), rb_regular.isChecked(), sel_month_id);
                    Toast.makeText(AddExpenseActivity.this, "Added", Toast.LENGTH_SHORT).show();
                    Log.d("rbb", "onClick: " + rb_regular.isChecked());
                    finish();
                }
            }
        });
    }

    //returns month number
    private int getMonthNumber(String month) {
        String[] months = getResources().getStringArray(R.array.months);
        for (int i = 0; i < months.length; i++) {
            if (months[i].equalsIgnoreCase(month)) {
                return i;
            }
        }
        return 0;
    }

    private boolean validate() {
        if (et_amount.getText().toString().length() < 0) {
            Toast.makeText(this, "Enter an amount", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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