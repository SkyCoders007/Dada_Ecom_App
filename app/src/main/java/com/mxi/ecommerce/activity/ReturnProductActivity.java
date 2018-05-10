package com.mxi.ecommerce.activity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import com.mxi.ecommerce.adapter.ReturnProductAdapter;
import com.mxi.ecommerce.comman.AppController;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.comman.ConnectionUrl;
import com.mxi.ecommerce.model.ReturnProductData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReturnProductActivity extends AppCompatActivity {

    RecyclerView recyclerview_browsebt_category1;
    RecyclerView.LayoutManager mLayoutManager_browseby_category;

    LinearLayout img_back_btn;
    ProgressDialog pDialog;
    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";
    String android_id;
    String username;

    CommanClass cc;

    List<ReturnProductData> returnProductDataList;
    RecyclerView.Adapter mAdapter;


    String title = "Return Product";
    TextView tv_toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_product);

        cc = new CommanClass(this);

        recyclerview_browsebt_category1 = (RecyclerView) findViewById(R.id.recyclerview_browsebt_category1);
        mLayoutManager_browseby_category = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        img_back_btn = (LinearLayout) findViewById(R.id.img_back_btn);

        img_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        tv_toolbar_title.setText(title);
        tv_toolbar_title.setPadding(0, 0, 40, 0);
        Log.e("toolbar", title);


        if (!cc.isConnectingToInternet()) {
            cc.showToast(getString(R.string.no_internet));

        } else {
            ReturnProductLising();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private void ReturnProductLising() {

        pDialog = new ProgressDialog(ReturnProductActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.returnorder,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:login", response);
                        JSONArray jsonArray = null;
                        if(pDialog.isShowing()){
                            pDialog.dismiss();
                        }
                        try {
                            String ims = "";

                            JSONObject jObject = new JSONObject(response);

                            if (jObject.getString("status").equals("200")) {
                                returnProductDataList = new ArrayList<>();

                                jsonArray = jObject.getJSONArray("returns");
                                for (int i = 0; i < jsonArray.length(); i++) {


                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    ReturnProductData getdiscountlisting = new ReturnProductData();

                                    getdiscountlisting.setReturn_id(jsonObject.getString("return_id"));
                                    getdiscountlisting.setOrder_id(jsonObject.getString("order_id"));
                                    getdiscountlisting.setName(jsonObject.getString("name"));
                                    getdiscountlisting.setDate_add(jsonObject.getString("date_added"));


                                    getdiscountlisting.setEmail(jsonObject.getString("email"));
                                    getdiscountlisting.setMobilenumber(jsonObject.getString("mobile"));
                                    getdiscountlisting.setStatus(jsonObject.getString("status"));
                                    getdiscountlisting.setProductname(jsonObject.getString("product_name"));
                                    getdiscountlisting.setModel(jsonObject.getString("model"));
                                    getdiscountlisting.setImage(jsonObject.getString("image"));
                                    getdiscountlisting.setQuantity(jsonObject.getString("quantity"));
                                    getdiscountlisting.setProduct_id(jsonObject.getString("product_id"));


                                    Log.e("imgaesssssss",jsonObject.getString("image"));


                                    returnProductDataList.add(getdiscountlisting);


                                }
                                if (returnProductDataList != null) {

                                    pDialog.dismiss();


                                    recyclerview_browsebt_category1.setLayoutManager(mLayoutManager_browseby_category);
                                    mAdapter = new ReturnProductAdapter(ReturnProductActivity.this, returnProductDataList);
                                    recyclerview_browsebt_category1.setLayoutManager(mLayoutManager_browseby_category);
                                    recyclerview_browsebt_category1.setAdapter(mAdapter);


                                }
                            } else if (
                                    jObject.getString("status").equals("400")) {

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
                error.printStackTrace();
                Log.e("Error_returnProduct",error.toString());
            }
        }) {



            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("1811-token", cc.loadPrefString("1811-token"));
                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

}