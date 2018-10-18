package com.fm.expensecalculator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fm.expensecalculator.R;
import com.fm.expensecalculator.db.ExpenseModel;
import com.fm.expensecalculator.utils.AppConstants;

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

            viewHolder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
            viewHolder.tv_description = (TextView) convertView.findViewById(R.id.tv_description);
            viewHolder.tv_regular = (TextView) convertView.findViewById(R.id.tv_regular);
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_regular.setText(isRegular(expenseModels.get(position).isRegular()));
        viewHolder.tv_amount.setText(AppConstants.CURRENCY + expenseModels.get(position).getAmount());
        viewHolder.tv_description.setText(expenseModels.get(position).getRemarks());
        viewHolder.tv_date.setText(expenseModels.get(position).getDate());

        return convertView;
    }

    private String isRegular(boolean b) {
        if (b)
            return "Regular";
        else
            return "Non-Regular";
    }


    public class ViewHolder {
        private TextView tv_amount;
        private TextView tv_description;
        private TextView tv_regular;
        private TextView tv_date;
    }
}