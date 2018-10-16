package com.fm.expensecalculator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fm.expensecalculator.R;
import com.fm.expensecalculator.db.ExpenseModel;

import java.util.ArrayList;

public class ExpenseListAdapter extends BaseAdapter {

    private ArrayList<ExpenseModel> expenseModels;
    private Context context;
    private LayoutInflater layoutInflater;


    public ExpenseListAdapter(Context context, ArrayList<ExpenseModel> expenseModels) {
        this.context = context;
        this.expenseModels = expenseModels;
    }

    public void updateList(ArrayList<ExpenseModel> expenseModels) {
        this.expenseModels = expenseModels;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return expenseModels.size();
    }

    @Override
    public Object getItem(int position) {
        return expenseModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return expenseModels.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_list_item, parent, false);

            viewHolder.tv_month = (TextView) convertView.findViewById(R.id.tv_month);
            viewHolder.tv_income = (TextView) convertView.findViewById(R.id.tv_income);
            viewHolder.tv_deficient = (TextView) convertView.findViewById(R.id.tv_deficient);
            viewHolder.tv_surplus = (TextView) convertView.findViewById(R.id.tv_surplus);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_month.setText(expenseModels.get(position).getDate());
        viewHolder.tv_income.setText(expenseModels.get(position).getAmount());
        viewHolder.tv_deficient.setText(expenseModels.get(position).getRemarks());

        return convertView;
    }


    public class ViewHolder {
        private TextView tv_month;
        private TextView tv_income;
        private TextView tv_deficient;
        private TextView tv_surplus;
    }
}