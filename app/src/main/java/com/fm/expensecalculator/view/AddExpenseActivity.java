package com.fm.expensecalculator.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.fm.expensecalculator.R;
import com.fm.expensecalculator.db.ExpenseDB;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText et_remarks, et_amount;
    private Button btn_save;
    private DatePicker datePicker;
    private RadioButton rb_regular;

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

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    new ExpenseDB(getApplicationContext()).addExpense(et_amount.getText().toString(), et_remarks.getText().toString(), String.valueOf(datePicker.getDayOfMonth()), rb_regular.isChecked());
                    Toast.makeText(AddExpenseActivity.this, "Added", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
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