package com.example.pena.restaurantorder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Getserver {


    String item, ProductID, moBileID;
    InputStream is = null;
    String result = null;
    String line = null;
    int code;

    public Getserver(String itemCart, String id, String mobileid) {
        item = itemCart;
        ProductID = id;
        moBileID = mobileid;

    }

    public String insert() {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("item", item));
        nameValuePairs.add(new BasicNameValuePair("ProductID", ProductID));
        nameValuePairs.add(new BasicNameValuePair("moBileID", moBileID));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.1.101:8081/projectR/insertdata.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 0", "connection success ");
        } catch (Exception e) {
           /* Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();*/
            Log.e("pass 1", "Invalid IP Address ");
        }

        try {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("pass 2", "connection success ");
        } catch (Exception e) {
            Log.e("Fail 2", e.toString());
        }

        try {
            JSONObject json_data = new JSONObject(result);
            code = (json_data.getInt("code"));

            if (code == 1) {
               /* Toast.makeText(getBaseContext(), "Inserted Successfully",
                        Toast.LENGTH_SHORT).show();*/
                Log.e("pass 3", "Inserted Successfully ");
                return "Inserted Successfully";
            } else {
               /* Toast.makeText(getBaseContext(), "Sorry, Try Again",
                        Toast.LENGTH_LONG).show();*/
                Log.e("pass 3", "Sorry, Try Again ");
                return "Sorry, Try Again";
            }
        } catch (Exception e) {
            Log.e("Fail 3", e.toString());
        }
        return "55";
    }//end insert

    public void SelectData() {


    }


}//end Getdata
