package com.mxi.ecommerce.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    String title = "Payment";
    TextView tv_toolbar_title, tv_final_total, tv_price, tv_amount_pay;
    LinearLayout img_back_btn, ln_cashhome, ln_credit, ln_debit;

    CheckBox rb_cashhome, rb_vredit, rb_debit;

    String total_amount, address_id;

    CommanClass cc;

    Button btn_addcart_continue;

    public boolean checked = false;

    ProgressDialog pDialog;
    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";
    String android_id;
    String username;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        cc = new CommanClass(this);

        init();
        clickListner();

        total_amount = cc.loadPrefString("total_amount");
        address_id = cc.loadPrefString("address_id");

        Log.e("total_amount", total_amount);
        Log.e("address_id", address_id);

        tv_final_total.setText(total_amount);
        tv_price.setText(total_amount);
        tv_amount_pay.setText(total_amount);

        tv_toolbar_title.setText(title);
        tv_toolbar_title.setPadding(0, 0, 40, 0);
        Log.e("toolbar", title);

        if (!cc.isConnectingToInternet()) {
            cc.showToast(getString(R.string.no_internet));

        } else {

            payment();

        }
    }

    private void clickListner() {

        img_back_btn.setOnClickListener(this);
        btn_addcart_continue.setOnClickListener(this);
        rb_cashhome.setOnCheckedChangeListener(this);

    }

    private void init() {

        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        btn_addcart_continue = (Button) findViewById(R.id.btn_payment_continue);
        tv_final_total = (TextView) findViewById(R.id.tv_final_total);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_amount_pay = (TextView) findViewById(R.id.tv_amount_pay);
        img_back_btn = (LinearLayout) findViewById(R.id.img_back_btn);
        rb_cashhome = (CheckBox) findViewById(R.id.rb_cashon);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.img_back_btn:

                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                break;


            case R.id.btn_payment_continue:
                if (!cc.isConnectingToInternet()) {
                    cc.showToast(getString(R.string.no_internet));

                } else {


                    if (isValidate()) {
                        Checkout();
                    }
                }
                break;
        }

    }

    /*Check Out Data*/
    private void Checkout() {
        pDialog = new ProgressDialog(PaymentActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.checkout,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("@@@checkout", response);
                        if(pDialog.isShowing()){
                            pDialog.dismiss();
                        }
                        try {
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("200")) {


                                //cc.showToast(jObject.getString("message"));

                                String message = jObject.getString("message");
                                JSONObject obj = jObject.getJSONObject("order");
                                String order_id = obj.getString("order_id");


                                cc.savePrefString("thank_you_message", message);
                                cc.savePrefString("thank_you_order_id", order_id);


                                Log.e("message", message);
                                Log.e("order_id", order_id);


                                startActivity(new Intent(PaymentActivity.this, ProceedPaymentActivity.class));
                                finish();
                            } else if (jObject.getString("status").equals("400")) {
                                cc.showToast(jObject.getString("message"));
                            }

                            // CartDetail();
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("delivery_method_code", "flat.flat");
                map.put("shipping_address_id", address_id);
                map.put("billing_address_id", address_id);
                map.put("delivery_method", "flat.flat");
                map.put("payment_method", code);

                return map;
            }

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

    /*Payment Method Using COD On create*/
    private void payment() {
        pDialog = new ProgressDialog(PaymentActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.payment_method,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("@@@Paymetn", response);
                        JSONArray jsonArray = null;
                        if(pDialog.isShowing()){
                            pDialog.dismiss();
                        }

                        try {
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("200")) {


                                JSONObject obj = jObject.getJSONObject("payment_methods");
                                jsonArray = obj.getJSONArray("cod");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    code = jsonObject.getString("code");
                                    Log.e("code", code);
                                }

                            } else if (jObject.getString("status").equals("400")) {

                                //cc.showToast(jObject.getString("message"));
                            }
                            // CartDetail();
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

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("billing_address_id", address_id);
                Log.i("@@request wishlist", map.toString());

                return map;
            }


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if (checked) {
            checked = false;
            rb_cashhome.setChecked(false);
            Log.e("checkbox_false", checked + "");

        } else {
            checked = true;
            rb_cashhome.setChecked(true);

            Log.e("checkbox_true", checked + "");

        }

    }


    /*Validation button*/
    public boolean isValidate() {
        String msg = null;

        if (checked != true) {
            msg = "Select Payment Method";

        }
        if (msg != null) {
            AndyUtils.showToast(PaymentActivity.this, msg);
            return false;
        }
        if (msg == null) {
            return true;
        }
        AndyUtils.showToast(PaymentActivity.this, msg);
        return false;
    }

}
