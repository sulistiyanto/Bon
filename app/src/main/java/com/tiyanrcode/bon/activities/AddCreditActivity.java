package com.tiyanrcode.bon.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tiyanrcode.bon.dao.TransactionDAO;
import com.tiyanrcode.bon.function.Money;
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
    private Button btnDate;

    private int year;
    private int month;
    private int day;
    private int credit;
    private int pay;
    private int saldo;
    static final int DATE_PICKER_ID = 1111;
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
        btnDate = (Button) findViewById(R.id.tgl);

        cusName.setText(customerName);
        cusId.setText(customerId);
        btnCredit.setOnClickListener(this);
        btnDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_credit:
                credit = Integer.parseInt(mCredit.getText().toString());
                Log.d("credit", ""+credit);
                final Editable date = dateOfCredit.getText();
                Money m = new Money();
                if (!TextUtils.isEmpty(""+credit) && !TextUtils.isEmpty(date)) {
                    AlertDialog alertDialog = new AlertDialog.Builder(AddCreditActivity.this).create();
                    // Setting Dialog Title
                    alertDialog.setTitle("Pesan Pemberitahuan!");
                    // Setting Dialog Message
                    alertDialog.setMessage("Total hutang pelanggan '" + customerName + "' adalah Rp. " + m.money(credit + saldo));
                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.info20);
                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which)
                        {
                            Transaction createdTransaction = mTransactionDAO.createTransaction(date.toString(), credit, pay, credit + saldo, Long.parseLong(customerId));
                            finish();
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                }
                else {
                    Toast.makeText(this, "Kolom hutang masih kosong", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tgl:
                showDialog(DATE_PICKER_ID);
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

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;
            //setMonth();
            dateOfCredit.setText(new StringBuilder().append(day).append(" ").append(month2).append(" ")
                    .append(year).append(" "));
        }
    };
}
