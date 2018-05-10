package com.mxi.ecommerce.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.ecommerce.R;
import com.mxi.ecommerce.comman.AndyUtils;
import com.mxi.ecommerce.comman.AppController;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.comman.ConnectionUrl;
import com.mxi.ecommerce.model.OrderTrackingdata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderTrackingActivity extends AppCompatActivity implements View.OnClickListener {

    String title = "Order Tracking";
    TextView tv_toolbar_title;
    LinearLayout img_back_btn;
    String status;
//    String order_id;

    CommanClass cc;

    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";
    ProgressDialog pDialog;

    View pending, dispacth;
    String message;

    String strStatus, strComment, strDate;
    String product_id, order_id;

    String tokens;
    List<OrderTrackingdata> orderTrackingdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);
        cc = new CommanClass(this);
        tokens = cc.loadPrefString("1811-token");
        LocalBroadcastManager.getInstance(OrderTrackingActivity.this).registerReceiver(mMessageReceiver, new IntentFilter("TrackingStatus"));

        init();
        clickListner();


        Log.e("mainscreen_token", "__" + tokens);


        Intent get = getIntent();
        product_id = get.getStringExtra("product_id");
        order_id = get.getStringExtra("order_id");


        Log.e("order_id", order_id);
        Log.e("product_id", product_id);

        if (!cc.isConnectingToInternet()) {
            cc.showToast(getString(R.string.no_internet));

        } else {

            // OrderTracking();
        }


        tv_toolbar_title.setText(title);
        tv_toolbar_title.setPadding(0, 0, 40, 0);
        Log.e("toolbar", title);


    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got recive: " + message);
        }
    };


    private void clickListner() {
        img_back_btn.setOnClickListener(this);
    }

    private void init() {

        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        img_back_btn = (LinearLayout) findViewById(R.id.img_back_btn);
        pending = (View) findViewById(R.id.pending);
        dispacth = (View) findViewById(R.id.dispacth);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.img_back_btn:

                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void OrderTracking() {

        pDialog = new ProgressDialog(OrderTrackingActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.order_tracking,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("@@@order_tracking", response);
                        JSONArray jsonArray = null;
                        try {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            JSONObject jObject = new JSONObject(response);
                            orderTrackingdata = new ArrayList<>();
                            jsonArray = jObject.getJSONArray("products_trcking");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                OrderTrackingdata trackingdata = new OrderTrackingdata();

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                trackingdata.setStatus(jsonObject.getString("name"));

                                status = jsonObject.getString("name");

//                                if (status.equals("Shipped")) {
//                                    dispacth.setBackground(getResources().getDrawable(R.drawable.ring_golden_bg));
//
//                                }else {
//                                    pending.setBackground(getResources().getDrawable(R.drawable.ring_golden_bg));
//
//                                }

                                orderTrackingdata.add(trackingdata);
                            }
                        } catch (JSONException e) {
                            Log.e("Error : Exception", e.getMessage());
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                AndyUtils.showToast(OrderTrackingActivity.this, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put("product_id", product_id);
                map.put("order_id", order_id);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("1811-token", tokens);
                headers.put("Cookie", "PHPSESSID=" + cc.loadPrefString3("PHPSESSID") + ";" + "default=" + cc.loadPrefString3("default"));
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }
}
