package com.tiyanrcode.bon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tiyanrcode.bon.dao.TransactionDAO;
import com.tiyanrcode.bon.model.Transaction;
import com.tiyanrcode.bon.R;

import java.util.Calendar;
import java.util.List;

/**
 * Created by sulistiyanto on 19-Apr-15.
 */
public class AddCreditActivity extends ActionBarActivity implements View.OnClickListener{

    public static final String EXTRA_SELECTED_CUSTOMER_ID = "extra_key_selected_customer_id";

    private EditText dateOfCredit;
    private EditText mCredit;
    private TextView cusName;
    private TextView cusId;
    private Button btnCredit;

    private int year;
    private int month;
    private int day;
    private int credit;
    private int pay;
    private int saldo;
    private String customerId;
    private String month2;
    private String customerName;
    private String date;

    final Calendar c = Calendar.getInstance();
    private List<Transaction> mListTransactions;
    private TransactionDAO mTransactionDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credit);
        getSupportActionBar().hide();

        initView();
        //set date day
        setDate();
        //get id and name customer
        Bundle bundle = this.getIntent().getExtras();
        if (bundle.containsKey("name")) {
            customerName = bundle.getString("name");
            customerId = bundle.getString("id");
        }

        // fill the listView
        mTransactionDAO = new TransactionDAO(this);
        mListTransactions = mTransactionDAO.getTransactionOfTransaction(Long.parseLong(customerId));
        if(mListTransactions != null && !mListTransactions.isEmpty()) {
            Transaction tran = mTransactionDAO.getItemSaldoByPosition(Long.parseLong(customerId));
            saldo = tran.getSaldo();
            Log.d("saldo new", String.valueOf(saldo));
            pay = 0;
        }
        else {
            saldo = 0;
            pay = 0;
        }
    }

    private void setDate() {
        year  = c.get(Calendar.YEAR);
        setMonth();
        day   = c.get(Calendar.DAY_OF_MONTH);

        dateOfCredit.setText(new StringBuilder().append(day).append(" ").append(month2).append(" ")
                .append(year).append(" "));
    }

    private void setMonth() {
        month = c.get(Calendar.MONTH);
        int m = month + 1;
        if (m == 1){
            month2 = "Januari";
        } else if (m == 2) {
            month2 = "Pebruari";
        } else if (m == 3) {
            month2 = "Maret";
        } else if (m == 4) {
            month2 = "April";
        } else if (m == 5) {
            month2 = "Mei";
        } else if (m == 6) {
            month2 = "Juni";
        } else if (m == 7) {
            month2 = "Juli";
        } else if (m == 8) {
            month2 = "Agustus";
        } else if (m == 9) {
            month2 = "September";
        }else if (m == 10) {
            month2 = "Oktober";
        }else if (m == 11) {
            month2 = "Nopember";
        }else if (m == 12) {
            month2 = "Desember";
        }
    }

    private void initView() {
        dateOfCredit = (EditText) findViewById(R.id.date_credit);
        mCredit = (EditText) findViewById(R.id.txt_credit);
        cusName = (TextView) findViewById(R.id.cre_name);
        cusId = (TextView) findViewById(R.id.cre_id);
        btnCredit = (Button) findViewById(R.id.btn_credit);

        cusName.setText(customerName);
        cusId.setText(customerId);
        btnCredit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_credit:
                credit = Integer.parseInt(mCredit.getText().toString());
                Log.d("credit", ""+credit);
                Editable date = dateOfCredit.getText();
                if (!TextUtils.isEmpty(""+credit) && !TextUtils.isEmpty(date)) {
                    Transaction createdTransaction = mTransactionDAO.createTransaction(date.toString(), credit, pay , credit + saldo, Long.parseLong(customerId));
                   /* Bundle bundle = new Bundle();
                    Intent intent = new Intent(AddCreditActivity.this, ListTransactionActivity.class);
                    intent.putExtra(ListCustomerActivity.EXTRA_ADDED_CUSTOMER, createdTransaction);
                    setResult(RESULT_OK, intent);
                    bundle.putString("id", "" + customerId);
                    bundle.putString("name", customerName);
                    Log.d("no ", customerName);
                    intent.putExtras(bundle);
                    startActivity(intent);*/
                    Toast.makeText(this, "Transaksi sukses", Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    Toast.makeText(this, "Kolom hutang masih kosong", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTransactionDAO.close();
    }
}
