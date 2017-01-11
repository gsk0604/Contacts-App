package com.android.flipmapapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by techjini on 10/1/17.
 */

public class GetListAsynTask extends AsyncTask<Void,Void,StringBuilder> {
    Context context;
    String name,email,phonenumber;
    ContactProvider contactProvider;
    StringBuilder builder;
    GetListAsynTask(Context c ){
        this.context=c;
    }
    ProgressDialog progressDialog;
    AsyncResponse delegate = null;




    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        builder = new StringBuilder();
        delegate = (AsyncResponse) context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Fetching Contacts.......");
        progressDialog.show();
    }

    @Override
    protected StringBuilder doInBackground(Void... voids) {

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);


            Cursor emailCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,

                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ? ", new String[]{id}, null);
            String emailTemp[] = new String[2];
            int j=0;
            while (emailCursor.moveToNext()) {
                if(j<2){
                    email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    emailTemp[j]=email;
                    j++;
                }

            }
            String temp[] = new String[2];
            int i=0;
            while (phoneCursor.moveToNext()) {
                if(i<2){
                    phonenumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    temp[i]= phonenumber;
                    i++;

                }
            }

            builder.append(name+","+emailTemp[0]+","+temp[0]+","+temp[1]);
            builder.append("\n");
            Log.d("Flip","Name : "+name+" PhoneNumber: " + temp[0]+" PhoneNumber 1:: " + temp[1]+ " Email : "+ emailTemp[0]);

        }


        return builder;
    }

    @Override
    protected void onPostExecute(StringBuilder result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        delegate.processFinish(result);
        Log.d("gsk",result.toString());

    }
    public interface AsyncResponse {
        void processFinish(StringBuilder output);
    }
}
