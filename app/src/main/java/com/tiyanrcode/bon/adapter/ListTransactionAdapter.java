package com.tiyanrcode.bon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tiyanrcode.bon.R;
import com.tiyanrcode.bon.model.Transaction;

import java.util.List;

/**
 * Created by sulistiyanto on 21-Apr-15.
 */
public class ListTransactionAdapter extends BaseAdapter{

    public static final String TAG = "ListTransactionAdapter";

    private List<Transaction> mItems;
    private LayoutInflater mInflater;

    public ListTransactionAdapter(Context context, List<Transaction> listTransactions) {
        this.setItems(listTransactions);
        this.mInflater = LayoutInflater.from(context);
    }

    public List<Transaction> getItems() {
        return mItems;
    }

    public void setItems(List<Transaction> mItems) {
        this.mItems = mItems;
    }

    @Override
    public int getCount() {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0;
    }

    @Override
    public Transaction getItem(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null ;
    }

    @Override
    public long getItemId(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position).getId() : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.list_item_transaction, parent, false);
            holder = new ViewHolder();
            holder.txtDate = (TextView) view.findViewById(R.id.txt_date);
            holder.txtKredit = (TextView) view.findViewById(R.id.credit);
            holder.txtPay = (TextView) view.findViewById(R.id.pay);
            holder.txtSaldo = (TextView) view.findViewById(R.id.saldo);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        //fill row data
        Transaction currentItem = getItem(position);
        if (currentItem != null) {
            holder.txtDate.setText(currentItem.getDate());
            holder.txtKredit.setText("" + currentItem.getCredit());
            holder.txtPay.setText("" + currentItem.getPay());
            holder.txtSaldo.setText("" + currentItem.getSaldo());
        }
        return view;
    }

    class ViewHolder {
        TextView txtDate;
        TextView txtKredit;
        TextView txtPay;
        TextView txtSaldo;
    }
}

