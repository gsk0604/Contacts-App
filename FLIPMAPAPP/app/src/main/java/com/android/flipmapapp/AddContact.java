package com.android.flipmapapp;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.name;

/**
 * Created by techjini on 10/1/17.
 */
public class   AddContact extends AppCompatActivity implements View.OnClickListener {
    EditText etName;
    EditText etEmail;
    EditText etMobNum;
    EditText etFixNum;
    Button btnAdd;
    Button btnContact;
    String name;
    String email;
    String mobNum;
    String fixNum;
    ContactProvider contantprovider;
    MainActivity main ;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = new MainActivity();
        setContentView(R.layout.activity_add_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        contantprovider = new ContactProvider(this);
        // Getting reference to Name EditText
        etName = (EditText) findViewById(R.id.et_name);
        // Getting reference to Mobile EditText
        etMobNum = (EditText) findViewById(R.id.et_mobile_phone);
        // Getting reference to HomePhone EditText
        etFixNum = (EditText) findViewById(R.id.et_home_phone);
        // Getting reference to "Add Contact" button
        etEmail = (EditText) findViewById(R.id.et_work_email);
        // Getting reference to "Add Contact" button
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getText() {
        name = etName.getText().toString();
        email = etEmail.getText().toString();
        mobNum = etMobNum.getText().toString();
        fixNum = etFixNum.getText().toString();
    }

    @Override
    public void onClick(View view) {

            getText();
            if ((!name.equals("")) && (!email.equals("")) && (!mobNum.equals("")) && (!fixNum.equals(""))) {
                contantprovider.saveContactsToPhone(name, email, mobNum, fixNum);
                Toast.makeText(this, "Contact saved Successfully", Toast.LENGTH_SHORT).show();
                flush();
            } else {
                Toast.makeText(this, "Please check fields!!!!", Toast.LENGTH_SHORT).show();
            }
            etName.setFocusable(true);


   }


    private void flush() {
        etName.setText("");
        etEmail.setText("");
        etMobNum.setText("");
        etFixNum.setText("");
    }

}
