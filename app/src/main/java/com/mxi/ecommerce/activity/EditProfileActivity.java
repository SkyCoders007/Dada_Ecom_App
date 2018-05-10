package com.mxi.ecommerce.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{
    String title= "Edit Profile";
    TextView tv_toolbar_title;
    LinearLayout img_back_btn;

    CommanClass cc;
    ProgressDialog pDialog;

    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";

    EditText et_first_name, et_last_name, et_mobile_number, et_email_id;
    Button btn_update;
    String tokens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        cc=new CommanClass(this);
        tokens = cc.loadPrefString("1811-token");

        Log.e("edit_token", "__" + tokens);
        init();
        clickListner();

        tv_toolbar_title.setText(title);
        tv_toolbar_title.setPadding(0, 0, 40, 0);
        Log.e("toolbar",title);


        if (!cc.isConnectingToInternet()) {
            cc.showToast(getString(R.string.no_internet));

        }else {

                getEditProfile();


        }

    }

    private void clickListner() {

        img_back_btn.setOnClickListener(this);
        btn_update.setOnClickListener(this);
    }

    private void init() {
        tv_toolbar_title=(TextView)findViewById(R.id.tv_toolbar_title);
        img_back_btn=(LinearLayout) findViewById(R.id.img_back_btn);

        et_first_name = (EditText) findViewById(R.id.et_first_name);
        et_last_name = (EditText) findViewById(R.id.et_last_name);
        et_mobile_number = (EditText) findViewById(R.id.et_mobile_number);
        et_email_id = (EditText) findViewById(R.id.et_email_id);
        btn_update = (Button) findViewById(R.id.btn_update);


    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {


            case R.id.img_back_btn:

                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                break;


            case R.id.btn_update:

                if (!cc.isConnectingToInternet()) {
                    cc.showToast(getString(R.string.no_internet));

                }else {
                    if (isValidate()) {

                        editprofile();

                    }




                }

                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


    // Get Edit Data

    private void getEditProfile() {

        pDialog = new ProgressDialog(EditProfileActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, ConnectionUrl.get_edit_profile,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("getEditdata", response);
                        try {
                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }
                            JSONObject jObject = new JSONObject(response);
                            JSONObject obj = jObject.getJSONObject("customerData");

                            cc.savePrefString("fristname",  obj.getString("firstname"));
                            cc.savePrefString("lastname",   obj.getString("lastname"));
                            cc.savePrefString("email",  obj.getString("email"));
                            cc.savePrefString("mobile",  obj.getString("mobile"));


                            et_first_name.setText(cc.loadPrefString("fristname"));
                            et_last_name.setText(cc.loadPrefString("lastname"));
                            et_email_id.setText(cc.loadPrefString("email"));
                            et_mobile_number.setText(cc.loadPrefString("mobile"));


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
                AndyUtils.showToast(EditProfileActivity.this,getString(R.string.ws_error));
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
                headers.put("1811-token",tokens);
                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }


    private void editprofile() {

        pDialog = new ProgressDialog(EditProfileActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.edit_profile,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:edittext", response);

                        try {
                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }

                            JSONObject jObject = new JSONObject(response);

                            if (jObject.getString("status").equals("200")) {
                                Toast.makeText(getApplicationContext(), "Your account is updated", Toast.LENGTH_LONG).show();
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
                error.printStackTrace();
                Log.e("Error_EditProfile",error.toString());

                if(pDialog.isShowing()){
                    pDialog.dismiss();
                }
                Log.e("error", error + "");
                Log.e("error1", error.getCause()+ "");
                Log.e("error2", error.getLocalizedMessage()+ "");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put("firstname",et_first_name.getText().toString().trim());
                map.put("lastname",et_last_name.getText().toString().trim());
                map.put("email", et_email_id.getText().toString().trim());
                map.put("mobile", et_mobile_number.getText().toString().trim());

                Log.i("@@request edittext", map.toString());

                return map;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language","en-gb");
                headers.put("1811-token",cc.loadPrefString("1811-token"));
                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
//                headers.put("language","en-gb");
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }


    public boolean isValidate() {
        String msg = null;

        if (!AndyUtils.eMailValidation(et_email_id.getText().toString())) {
            msg = "Enter Valid Email Id";
            et_email_id.requestFocus();


        } else if (!AndyUtils.nameValidation(et_first_name.getText().toString())) {
            msg = "Enter FirstName";
            et_first_name.requestFocus();

        } else if (et_mobile_number.getText().length() != 10) {
            msg = getResources().getString(R.string.phone_alert);
            et_mobile_number.requestFocus();
        } else if (!AndyUtils.nameValidation(et_last_name.getText().toString())) {
            msg = "Enter LastName";
            et_last_name.requestFocus();

        }

        if (msg != null) {
            AndyUtils.showToast(EditProfileActivity.this, msg);
            return false;
        }
        if (msg == null) {
            return true;
        }
        AndyUtils.showToast(EditProfileActivity.this, msg);
        return false;
    }
}
