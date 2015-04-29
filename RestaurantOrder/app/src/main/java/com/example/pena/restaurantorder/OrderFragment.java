package com.example.pena.restaurantorder;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.StrictMode;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class OrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MyReceiver r;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderFragment() {
        // Required empty public constructor
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order, container, false);


        Button btn1 = (Button) v.findViewById(R.id.buttonorder);


       /* btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Showdata();
            }
        });*/


        return v;
    }

    public void refresh() {
        //yout code in refresh.
        try {
            Showdata();
        } catch (Exception e) {
            Log.e("No connect", e.toString());
        }

        Log.i("Refresh", "YES");
    }

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);
    }

    public void onResume() {
        super.onResume();
        r = new MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r,
                new IntentFilter("TAG_REFRESH"));
    }

    private void Showdata() {

        // listView1
        final ListView lisView2 = (ListView) getView().findViewById(R.id.listVieworder);

      /*  // editText1
        final EditText inputText = (EditText) getView().findViewById(R.id.editText1);
*/
        String key = "001";
        String url = "http://192.168.1.101:8081/projectR/cn_orderdata.php";

        // Paste Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("txtKeyword", key));

        try {
            JSONArray data = new JSONArray(getJSONUrl(url, params));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<String, String>();
                map.put("ProductID", c.getString("ProductID"));
                map.put("ProductName", c.getString("IDcart"));
                map.put("Price", c.getString("mobileID"));
                map.put("ProductPic", c.getString("item"));
                map.put("item", c.getString("item"));
                MyArrList.add(map);

            }


            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(getActivity(), MyArrList, R.layout.activity_columnorder,
                    new String[]{"ProductPic", "ProductName", "Price","item",""}, new int[]{R.id.ColProductPic, R.id.ColProductName, R.id.ColPrice,R.id.Colitem,R.id.checkBoxOrder});
            lisView2.setAdapter(sAdap);

            final AlertDialog.Builder viewDetail = new AlertDialog.Builder(getActivity());
            // OnClick Item

            lisView2.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> myAdapter, View myView,
                                        int position, long mylng) {

                    final String strProductID = MyArrList.get(position).get("ProductID")
                            .toString();
                    String sProductName = MyArrList.get(position).get("ProductName")
                            .toString();
                    String strPrice = MyArrList.get(position).get("Price")
                            .toString();
                    String strProductPic = MyArrList.get(position).get("ProductPic")
                            .toString();

                    viewDetail.setIcon(android.R.drawable.btn_star_big_on);
                    viewDetail.setTitle("Member Detail");
                    viewDetail.setMessage(
                            "Pic : " + strProductPic + "\n"
                                    + "ProductID : " + strProductID + "\n"
                                    + "Name : " + sProductName + "\n"
                                    + "Price : " + strPrice + "\n"
                    );
                    final EditText input = new EditText(getActivity());
                    input.setText("1");
                    viewDetail.setView(input);
                    viewDetail.setPositiveButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    dialog.dismiss();

                                }
                            });
                    viewDetail.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // ใส่ค่าที่ต้องการส่งลงไป
                            String item = input.getEditableText().toString().trim();
                            item = item.replaceAll("\\s", "");
                            if (item.equalsIgnoreCase("") || item.equalsIgnoreCase(null)) {
                                item = "1";
                            }
                            Log.e("check item =", item);
                            Setdata(item, strProductID);
                        }
                    });
                    viewDetail.show();
                }
            });


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }//end loaddata()

    private void Setdata(String item, String strProductID) {

        String moBileID = "001";
        Getserver server = new Getserver(item, strProductID, moBileID);
        String messages = server.insert();
        Toast.makeText(getActivity(), messages, Toast.LENGTH_SHORT).show();

    }

    public String getJSONUrl(String url, List<NameValuePair> params) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Download OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download file..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();

    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            OrderFragment.this.refresh();
        }

    }


}//end OrderFragment
