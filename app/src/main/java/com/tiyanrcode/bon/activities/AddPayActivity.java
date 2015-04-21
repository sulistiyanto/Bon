package com.tiyanrcode.bon.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
public class AddPayActivity extends ActionBarActivity implements View.OnClickListener{
    public static final String EXTRA_SELECTED_CUSTOMER_ID = "extra_key_selected_customer_id";

    private Button btnPay;
    private EditText dateOfPay;
    private EditText mPay;
    private TextView payName;
    private TextView payId;
    private int year;
    private int month;
    private int day;
    private int credit1;
    private int pay1;
    private int saldo1;
    private String customerId;
    private String customerName;
    private String month2;

    final Calendar c = Calendar.getInstance();
    private List<Transaction> mListTransactions;
    private TransactionDAO mTransactionDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pay);
        getSupportActionBar().hide();

        //get id and name customer
        Bundle bundle = this.getIntent().getExtras();
        if (bundle.containsKey("name")) {
            customerName= bundle.getString("name");
            customerId = bundle.getString("id");
            Log.d("name", customerName);
        }

        initView();

        //set date day
        setDate();

        // fill the listView
        mTransactionDAO = new TransactionDAO(this);
        mListTransactions = mTransactionDAO.getTransactionOfTransaction(Long.parseLong(customerId));
        if(mListTransactions != null && !mListTransactions.isEmpty()) {
            Transaction tran = mTransactionDAO.getItemSaldoByPosition(Long.parseLong(customerId));
            saldo1 = tran.getSaldo();
            Log.d("saldo ", String.valueOf(saldo1));
            credit1 = 0;
            if (saldo1 == 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(AddPayActivity.this).create();
                // Setting Dialog Title
                alertDialog.setTitle("Pesan Pemberitahuan!");
                // Setting Dialog Message
                alertDialog.setMessage("Pelanggan ini belum punya hutang");
                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.info20);
                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which)
                    {
                        // Write your code here to execute after dialog closed
                        finish();
                    }
                });
                // Showing Alert Message
                alertDialog.show();
            }
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(AddPayActivity.this).create();
            // Setting Dialog Title
            alertDialog.setTitle("Pesan Pemberitahuan!");
            // Setting Dialog Message
            alertDialog.setMessage("Pelanggan ini belum ada kegiatan transaksi");
            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.info20);
            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which)
                {
                    // Write your code here to execute after dialog closed
                    finish();
                }
            });
            // Showing Alert Message
            alertDialog.show();
        }
    }

    private void setDate() {
        year  = c.get(Calendar.YEAR);
        setMonth();
        day   = c.get(Calendar.DAY_OF_MONTH);

        dateOfPay.setText(new StringBuilder().append(day).append(" ").append(month2).append(" ")
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
        dateOfPay = (EditText) findViewById(R.id.date_pay);
        mPay = (EditText) findViewById(R.id.txt_pay);
        payName = (TextView) findViewById(R.id.pay_name);
        payId = (TextView) findViewById(R.id.pay_id);
        btnPay = (Button) findViewById(R.id.btn_pay);

        payName.setText(customerName);
        payId.setText(customerId);
        btnPay.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTransactionDAO.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:
                pay1 = Integer.parseInt(mPay.getText().toString());
                Log.d("credit", ""+credit1);
                final int total = pay1 - saldo1;
                Log.d("saldo new", String.valueOf(saldo1));
                final Editable date = dateOfPay.getText();
                if (total > 1) {
                    AlertDialog alertDialog = new AlertDialog.Builder(AddPayActivity.this).create();
                    // Setting Dialog Title
                    alertDialog.setTitle("Pesan Pemberitahuan!");
                    // Setting Dialog Message
                    alertDialog.setMessage("Uang kembali pelanggan adalah " + total);
                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.info20);
                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which)
                        {
                            Transaction tran = mTransactionDAO.getItemSaldoByPosition(Long.parseLong(customerId));
                            int c = tran.getCredit();
                            if (!TextUtils.isEmpty("" + credit1) && !TextUtils.isEmpty(date)) {
                                Transaction createdTransaction = mTransactionDAO.createTransaction(date.toString(), credit1, c , 0, Long.parseLong(customerId));
                                finish();
                            }
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                } else {
                    if (!TextUtils.isEmpty("" + pay1) && !TextUtils.isEmpty(date)) {
                        Transaction createdTransaction = mTransactionDAO.createTransaction(date.toString(), credit1, pay1 , total, Long.parseLong(customerId));
                        finish();
                    }
                    else {
                        Toast.makeText(this, "Kolom hutang masih kosong", Toast.LENGTH_LONG).show();
                    }
                }

                break;
            default:
                break;
        }
    }
}
