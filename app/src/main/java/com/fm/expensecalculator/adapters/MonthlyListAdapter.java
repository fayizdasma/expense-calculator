package com.fm.expensecalculator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fm.expensecalculator.R;
import com.fm.expensecalculator.db.models.SheetModel;
import com.fm.expensecalculator.utils.AppConstants;

import java.util.ArrayList;

public class MonthlyListAdapter extends BaseAdapter {

    private ArrayList<SheetModel> sheetData;
    private Context context;
    private LayoutInflater layoutInflater;

    public MonthlyListAdapter(Context context, ArrayList<SheetModel> sheetData) {
        this.context = context;
        this.sheetData = sheetData;
    }

    public void updateSheetList(ArrayList<SheetModel> sheetData) {
        this.sheetData = sheetData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return sheetData.size();
    }

    @Override
    public Object getItem(int position) {
        return sheetData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return sheetData.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_list_item_sheet, parent, false);

            viewHolder.tv_income = (TextView) convertView.findViewById(R.id.tv_income);
            viewHolder.tv_month = (TextView) convertView.findViewById(R.id.tv_month);
            viewHolder.tv_year = (TextView) convertView.findViewById(R.id.tv_year);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_income.setText("Income: " + AppConstants.CURRENCY + sheetData.get(position).getIncome());
        viewHolder.tv_month.setText(sheetData.get(position).getMonth());
        viewHolder.tv_year.setText(sheetData.get(position).getYear());

        return convertView;
    }

    private class ViewHolder {
        private TextView tv_month;
        private TextView tv_year;
        private TextView tv_income;
    }
}