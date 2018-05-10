package com.mxi.ecommerce.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.mxi.ecommerce.adapter.MyOrderAdapter;
import com.mxi.ecommerce.comman.AndyUtils;
import com.mxi.ecommerce.comman.AppController;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.comman.ConnectionUrl;
import com.mxi.ecommerce.model.OrderHistorydata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrderActivity extends AppCompatActivity implements View.OnClickListener {

    String title= "My Order";
    TextView tv_toolbar_title;


    RecyclerView recycler_myorder;
    RecyclerView.LayoutManager mLayoutManager_recycler_myorder;


    RecyclerView.Adapter mAdapter;

    ArrayList<String> alName;
    ArrayList<Integer> alImage;

    LinearLayout img_back_btn;
    CommanClass cc;


    List<OrderHistorydata> oder_history_data;


    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";
    String android_id;
    ProgressDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        cc=new CommanClass(this);

        init();
        clickListner();

//        alName = new ArrayList<>(Arrays.asList("Eyewear lenscart", "Gold Jwellery ","Amazing footwear", "Rolex Watch", "Redmi note-4 black.."));
//        alImage = new ArrayList<>(Arrays.asList(R.mipmap.eyewear, R.mipmap.jwelery,R.mipmap.footwear, R.mipmap.watch,R.mipmap.mobiles));

        if (!cc.isConnectingToInternet()) {
            cc.showToast(getString(R.string.no_internet));
        }else {
            OrderHistroy();
        }

        tv_toolbar_title.setText("Order history");
        tv_toolbar_title.setPadding(0, 0, 40, 0);
        Log.e("toolbar",title);
    }


    /*Order Histroy*/

    private void OrderHistroy() {

        pDialog = new ProgressDialog(MyOrderActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, ConnectionUrl.order_history,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:orderhistory", response);
                        JSONArray jsonArray = null;
                        JSONArray jsonArray1 = null;
                        try {
                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }

                            oder_history_data = new ArrayList<>();

                            JSONObject jObject = new JSONObject(response);


                            jsonArray = jObject.getJSONArray("orders");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                OrderHistorydata orderinfo = new OrderHistorydata();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                orderinfo.setOrder_id(jsonObject.getString("order_id"));
                                orderinfo.setOrder_status(jsonObject.getString("status"));
                                orderinfo.setOrder_date_added(jsonObject.getString("date"));
                                orderinfo.setOrder_total(jsonObject.getString("total"));


                                Log.e("order_id",jsonObject.getString("order_id"));

                                oder_history_data.add(orderinfo);


                            } if(oder_history_data!=null){

                                  mLayoutManager_recycler_myorder = new LinearLayoutManager(MyOrderActivity.this);
                                  recycler_myorder.setLayoutManager(mLayoutManager_recycler_myorder);
                                  mAdapter = new MyOrderAdapter(MyOrderActivity.this,oder_history_data);
                                  recycler_myorder.setLayoutManager(mLayoutManager_recycler_myorder);
                                  recycler_myorder.setAdapter(mAdapter);

                            }

                            if (jObject.getString("status").equals("200")) {

                            } else if (jObject.getString("status").equals("400")) {
                                cc.showToast(jObject.getString("message"));
                            }

                        } catch (JSONException e) {
                            Log.e("Error : Exception", e.getMessage());
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(pDialog.isShowing()){
                    pDialog.dismiss();
                }
                AndyUtils.showToast(MyOrderActivity.this,getString(R.string.ws_error));
            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language",cc.loadPrefString("language"));
                headers.put("1811-token",cc.loadPrefString("1811-token"));
                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }


    private void clickListner() {
        img_back_btn.setOnClickListener(this);

    }

    private void init() {

        tv_toolbar_title=(TextView)findViewById(R.id.tv_toolbar_title);
        recycler_myorder=(RecyclerView) findViewById(R.id.recycler_myorder);

        img_back_btn=(LinearLayout) findViewById(R.id.img_back_btn);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {

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
}
