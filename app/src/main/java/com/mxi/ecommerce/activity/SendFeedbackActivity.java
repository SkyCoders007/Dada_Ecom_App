package com.mxi.ecommerce.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.ecommerce.R;
import com.mxi.ecommerce.comman.AndyUtils;
import com.mxi.ecommerce.comman.AppController;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.comman.ConnectionUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendFeedbackActivity extends AppCompatActivity implements View.OnClickListener{


    CommanClass cc;
    TextView et_name,et_email;
    EditText et_send;

    Button btn_send;
    String title= "Send Feedback";

    ProgressDialog pDialog;
    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";

    String name,email;

    TextView tv_toolbar_title;
    LinearLayout img_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);

        cc=new CommanClass(this);

        ini();
        clicklistner();


        tv_toolbar_title.setText(title);
        tv_toolbar_title.setPadding(0, 0, 40, 0);
        Log.e("toolbar",title);

        name =  cc.loadPrefString("firstname");
        email = cc.loadPrefString("email");

        et_name.setText(name);
        et_email.setText(email);

    }

    private void clicklistner() {
        btn_send.setOnClickListener(this);
        img_back_btn.setOnClickListener(this);


    }

    private void ini() {

        tv_toolbar_title=(TextView)findViewById(R.id.tv_toolbar_title);
        img_back_btn=(LinearLayout) findViewById(R.id.img_back_btn);

        et_send=(EditText)findViewById(R.id.et_send);

        et_name=(TextView)findViewById(R.id.et_name);
        et_email=(TextView)findViewById(R.id.et_email);

        btn_send=(Button) findViewById(R.id.btn_send);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_send:
//
                if (isValidate()) {

                    feedback();

                }
              //  feedback();
                break;


            case R.id.img_back_btn:

                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                break;

        }

    }

    private void feedback() {

        pDialog = new ProgressDialog(SendFeedbackActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.send_feedback,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:feedback", response);
                        try {
                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }
                            JSONObject jObject = new JSONObject(response);
                            clearField();

                            cc.showToast(jObject.getString("message"));

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
                AndyUtils.showToast(SendFeedbackActivity.this,getString(R.string.ws_error));
            }
        }) {

            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                Log.i("response", response.headers.toString());
//                Map<String, String> responseHeaders = response.headers;
//                String rawCookies = responseHeaders.get("Set-Cookie");
//                Log.i("sendfeddback_cookies", rawCookies);
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put("name", name);
                map.put("email",  email);
                map.put("feedback", et_send.getText().toString().trim());
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

    private void clearField() {

        et_send.setText("");
        et_name.setText("");
        et_email.setText("");

    }

    public boolean isValidate() {
        String msg = null;

        if (TextUtils.isEmpty(et_send.getText().toString().trim())) {
            msg = "Enter Message";
            et_send.requestFocus();
        }
        if (msg != null) {
            AndyUtils.showToast(SendFeedbackActivity.this, msg);
            return false;
        }
        if (msg == null) {
            return true;
        }
        AndyUtils.showToast(SendFeedbackActivity.this, msg);
        return false;
    }

}
