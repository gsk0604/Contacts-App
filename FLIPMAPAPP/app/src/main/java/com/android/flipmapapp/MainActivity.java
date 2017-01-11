package com.android.flipmapapp;

import android.*;
import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetListAsynTask.AsyncResponse{

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 101;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String[] tempPhoneNumber;
    String builderFromAsyn;
    String lon ;
    String lat;
    String name;
    ContactProvider contactProvider;
    ArrayList<DataProvider> list;
    AddContact addContact;
    MyAdapter adapter;
    StringBuilder builder;
    ListView listView;
    DataProvider dataProvider;
    GetListAsynTask getListAsynTask;




    private static final int REQUEST_CODE = 6384; // onActivityResult request

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getListAsynTask = new GetListAsynTask(this);
        getListAsynTask.delegate = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.list_view);
        dataProvider = new DataProvider();
        tempPhoneNumber = new String[2];
        builder = new StringBuilder();
        addContact = new AddContact();
        contactProvider = new ContactProvider(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAndRequestPermissions()){
                    Intent intent = new Intent(MainActivity.this, AddContact.class);
                    startActivity(intent);
                }

            }
        });



            searchContants();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }


    public   boolean checkAndRequestPermissions() {
        int readContacts = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int writeContacts = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (readContacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (writeContacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS);
        }
        if (writeContacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();

                        try {
                            //Toast.makeText(this, "URI  " + uri, Toast.LENGTH_SHORT).show();
                            // Get the file path from the URI
                            final File file = FileUtils.getFile(this, uri);
                            //Toast.makeText(this, "Path " + uri.getPath(), Toast.LENGTH_SHORT).show();
                            contactProvider.UriConvertToStringBuilder(file);


                        } catch (Exception e) {

                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.choose) {
            if(checkAndRequestPermissions()){
                showChooser();
            }

            return true;
        }
        if (id == R.id.refresh) {
            if(checkAndRequestPermissions()){
                searchContants();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void searchContants() {

                if(checkAndRequestPermissions()){
                    new GetListAsynTask(this).execute();
                }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    lat = contactProvider.getArr().get(i).getMobNumber().replaceAll("[-+.^:,]","").trim().replaceAll("\\s+","");
                    lon = contactProvider.getArr().get(i).getFixNumber().replaceAll("[-+.^:,]","").trim().replaceAll("\\s+","");
                    if((!lat.equals("null"))&&(!lon.equals("null"))) {
                        name = contactProvider.getArr().get(i).getName();
                        Log.d("gsk","lat: "+lat + " lon : "+ lon);
                        if(lon.startsWith("91")){
                            lon= lon.substring(2,lon.length()-1);
                        }
                        if(lat.startsWith("91")){
                            lat = lat.substring(2,lon.length()-1);
                        }
                        Log.d("gsk","lat: "+lat + " lon : "+ lon);


                        builder = new StringBuilder(lat);
                        lat = builder.insert(2,'.').toString();
                        builder = new StringBuilder(lon);
                        lon = builder.insert(2,'.').toString();

                        Log.d("gsk","lat: "+lat + " lon : "+ lon);

                        Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                        intent.putExtra("Latitude",lat);
                        intent.putExtra("Longitude",lon);
                        intent.putExtra("Name",name);
                        startActivity(intent);

                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Latitude or Longitute out of range", Toast.LENGTH_SHORT).show();
                    }



            }
        });


    }


    @Override
    public void processFinish(StringBuilder output) {

        contactProvider.listView(output.toString());
        adapter = new MyAdapter(this, contactProvider.getArr());
        Log.d("gsk",contactProvider.getArr().size()+"");
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
