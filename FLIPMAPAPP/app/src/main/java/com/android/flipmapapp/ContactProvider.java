package com.android.flipmapapp;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by techjini on 10/1/17.
 */

public class ContactProvider {

    BufferedReader bufferedReader;
    MyAdapter adapter;
ArrayList<DataProvider> arrayList = new ArrayList<>();
    public StringBuilder lineBuilder;
    private String contactFile;


    Context context;
    DataProvider dataProvider;

    ContactProvider(Context c){
        this.context = c;
    }

    public ContactProvider() {

    }

    public void saveContactsToPhone(String name, String email, String mobNum,String fixNum){

        ArrayList<ContentProviderOperation> ops =
                new ArrayList<ContentProviderOperation>();

        int rawContactID = ops.size();

        // Adding insert operation to operations list
        // to insert a new raw contact in the table ContactsContract.RawContacts
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        // Adding insert operation to operations list
        // to insert display name in the table ContactsContract.Data
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build());

        // Adding insert operation to operations list
        // to insert Mobile Number in the table ContactsContract.Data
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobNum)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        // Adding insert operation to operations list
        // to  insert Home Phone Number in the table ContactsContract.Data
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, fixNum)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                .build());


        // Adding insert operation to operations list
        // to insert Work Email in the table ContactsContract.Data
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, email)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());

        try{

            // Executing all the insert operations as a single database transaction
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);

        }catch (RemoteException e) {
            e.printStackTrace();
        }catch (OperationApplicationException e) {
            e.printStackTrace();
        }


    }






    public void UriConvertToStringBuilder(File file) {
        try {
            //Log.d("Flip", "Path:" + file);
            //file = "/storage/emulated/0/Download/FLIP_Android_Assignment-1/FLIP_Android_Assignment/contacts.txt";
            lineBuilder = new StringBuilder();
            bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Log.d("Flip", "Line BufferReader : " + line);
                lineBuilder.append(line);
                lineBuilder.append("\n");
            }
            bufferedReader.close();
        stringConvertToList(lineBuilder.toString());
        } catch (FileNotFoundException e) {
            Log.d("Flip","File Not Found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("Flip","IOException");
        }

    }

    public void stringConvertToList(String temp) {
        int count = 0;
        int x = 0;
        try {
            InputStream is = new ByteArrayInputStream(temp.getBytes());
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.equals("")) {
                    line = line.trim();
                    Log.d("Flip", "Line found :" + line);
                    for (int j = 0; j < line.length(); j++) {
                        char c = line.charAt(j);
                        if (c == ',') {
                            count++;
                        }

                    }
                    if (count == 3) {
                        String[] array = line.split(",");
                        if ((!array[0].equals("") || !array[0].equals(null))
                                && (!array[1].equals("") || !array[1].equals(null))
                                && (!array[2].equals("") || !array[2].equals(null))
                                && (!array[3].equals("") || !array[3].equals(null))) {
                            String name = array[0].trim();
                            String email = array[1].trim();
                            String mobNum = array[2].trim();
                            String fixNum = array[3].trim();
                            saveContactsToPhone(name,email,mobNum,fixNum);
                            Toast.makeText(context, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                            array = null;
                            x++;
                        }
                    }
                    count = 0;
                }

            }
            bufferedReader.close();

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

    }

    public void listView(String temp) {
    arrayList.clear();
        int count = 0;
        int x = 0;
        try {
            InputStream is = new ByteArrayInputStream(temp.getBytes());
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.equals("")) {
                    line = line.trim();
                    Log.d("Flip", "Line found :" + line);
                    for (int j = 0; j < line.length(); j++) {
                        char c = line.charAt(j);
                        if (c == ',') {
                            count++;
                        }

                    }
                    if (count == 3) {
                        String[] array = line.split(",");
                        if ((!array[0].equals("") || !array[0].equals("null"))
                                && (!array[1].equals("") || !array[1].equals("null"))
                                && (!array[2].equals("") || !array[2].equals("null"))
                                && (!array[3].equals("") || !array[3].equals("null"))) {
                            String name = array[0].trim();
                            String email = array[1].trim();
                            String mobNum = array[2].trim();
                            String fixNum = array[3].trim();

                            if(arrayList!=null){
                                arrayList.add(new DataProvider(name,email,mobNum,fixNum));
                                Log.d("gsk","Saved: " +name+email+mobNum+fixNum);
                            }
                            else
                            {
                                Log.d("Flip","Null in ContactProviser");
                            }

                            array = null;
                            x++;
                        }
                    }
                    count = 0;
                }

            }
            bufferedReader.close();

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        setArrayList(arrayList);
    }


    private void setArrayList(ArrayList<DataProvider> arr){
        this.arrayList = arr;
    }

    ArrayList<DataProvider> getArr(){
        return arrayList;
    }



}
