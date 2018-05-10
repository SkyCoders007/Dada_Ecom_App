package com.mxi.ecommerce.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.comman.ConnectionUrl;
import com.mxi.ecommerce.R;
import com.mxi.ecommerce.comman.AndyUtils;
import com.mxi.ecommerce.comman.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    CommanClass cc;

    public boolean checked = false;
    ProgressDialog pDialog;

    LinearLayout ln_register_gotologin,ln_register_home;
    Button btn_register;

    EditText etRegFname,etRegLname,etRegMobilenumber,etRegEmail,etRegPassword,etRegConfirmPassword;

    CheckBox ch_term_condition;

    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";
    String android_id;
    String languages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        cc = new CommanClass(this);

        init();
        clickListner();

        languages = cc.loadPrefString("language");
        Log.e("languafge",languages);


    }

    private void clickListner() {
        ln_register_gotologin.setOnClickListener(this);
        ln_register_home.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        ch_term_condition.setOnCheckedChangeListener(this);

    }

    private void init() {

        android_id = Settings.Secure.getString(RegisterActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Log.e("deviceid",android_id);

        ln_register_gotologin = (LinearLayout) findViewById(R.id.ln_register_gotologin);
        ln_register_home = (LinearLayout) findViewById(R.id.ln_register_home);
        btn_register =(Button) findViewById(R.id.btn_Registers);

        etRegFname =(EditText)findViewById(R.id.etRegFname);
        etRegLname =(EditText)findViewById(R.id.etRegLname);
        etRegMobilenumber =(EditText)findViewById(R.id.etRegMobilenumber);
        etRegEmail =(EditText)findViewById(R.id.etRegEmail);
        etRegPassword =(EditText)findViewById(R.id.etRegPassword);
        etRegConfirmPassword =(EditText)findViewById(R.id.etRegConfirmPassword);
        ch_term_condition = (CheckBox) findViewById(R.id.ch_term_condition);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ln_register_gotologin:

                Intent intentLogin = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intentLogin);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                finish();

                break;

            case R.id.ln_register_home:


                Intent intentMainActivity = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intentMainActivity);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;

            case R.id.btn_Registers:
                if (!cc.isConnectingToInternet()) {
                    cc.showToast(getString(R.string.no_internet));

                }else {
                    if (isValidate()) {

                        RegisterWS();

                    }
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (checked) {
            checked = false;
            ch_term_condition.setChecked(false);
            Log.e("checkbox_false", checked + "");

        } else {
            checked = true;
            ch_term_condition.setChecked(true);

            Log.e("checkbox_true", checked + "");

        }
    }


    public boolean isValidate() {
        String msg = null;

        if (!AndyUtils.nameValidation(etRegFname.getText().toString())) {
            msg = "Enter FirstName";
            etRegFname.requestFocus();

        } else if (!AndyUtils.nameValidation(etRegLname.getText().toString())) {
            msg = "Enter LastName";
            etRegLname.requestFocus();

        } else if (!AndyUtils.eMailValidation(etRegEmail.getText().toString())) {
            msg = "Enter Valid Email Id";
            etRegEmail.requestFocus();

        } else if (TextUtils.isEmpty(etRegPassword.getText().toString())) {
            msg = "Enter Valid Password";
            etRegPassword.requestFocus();
        } else if (TextUtils.isEmpty(etRegConfirmPassword.getText().toString())) {
            msg = "Enter Valid Password";
            etRegConfirmPassword.requestFocus();
        } else if (!etRegConfirmPassword.getText().toString().equals(etRegPassword.getText().toString())) {
            msg = "Password didn't match";
            etRegPassword.requestFocus();

        } else if (checked != true) {
            msg = getString(R.string.select_term);

        } else if (etRegPassword.getText().toString().length() < 6 || etRegPassword.getText().toString().length() > 15) {
            msg = "Password Length Must Be 6 To 15";
            etRegPassword.requestFocus();
        } else if (etRegMobilenumber.getText().length() != 10) {
            msg = getResources().getString(R.string.phone_alert);
            etRegMobilenumber.requestFocus();
        }
        if (msg != null) {
            AndyUtils.showToast(RegisterActivity.this, msg);
            return false;
        }
        if (msg == null) {
            return true;
        }
        AndyUtils.showToast(RegisterActivity.this, msg);
        return false;
    }

    private void RegisterWS() {

        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.signup,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:login", response);
                        try {

                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }
                            JSONObject jObject = new JSONObject(response);

                            if (jObject.getString("status").equals("200")) {
                                clearField();
                                JSONObject obj = jObject.getJSONObject("customerData");

                                cc.savePrefString("customer_id", obj.getString("customer_id"));
                                cc.savePrefString("firstname", obj.getString("firstname"));
                                cc.savePrefString("lastname", obj.getString("lastname"));
                                cc.savePrefString("email", obj.getString("email"));
                                cc.savePrefString("mobile", obj.getString("mobile"));

                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                finish();


                                cc.showToast(jObject.getString("message"));

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
                error.printStackTrace();
                Log.e("Error_reg",error.toString());
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                String resHead=response.headers.get("1811-token");
                Log.e("@@resHead",resHead+"");
                cc.savePrefString("1811-token",resHead);
                return super.parseNetworkResponse(response);

            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put("firstname", etRegFname.getText().toString().trim());
                map.put("lastname", etRegLname.getText().toString().trim());
                map.put("mobileno", etRegMobilenumber.getText().toString().trim());
                map.put("email", etRegEmail.getText().toString().trim());
                map.put("password", etRegPassword.getText().toString().trim());
                map.put("device_id", android_id);
                map.put("gcm_id", "123456");
                map.put("login_with", "android");
                map.put("apn_id", "hghgyf");

                Log.e("firstname",etRegFname.getText().toString().trim());
                Log.e("lastname",etRegLname.getText().toString().trim());
                Log.e("etRegMobilenumber",etRegMobilenumber.getText().toString().trim());
                Log.e("etRegEmail",etRegEmail.getText().toString().trim());
                Log.e("etRegPassword",etRegPassword.getText().toString().trim());

                Log.e("@@request SignUp", map.toString());

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language",cc.loadPrefString("language"));
                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }


    public void clearField() {
        etRegFname.setText("");
        etRegLname.setText("");
        etRegEmail.setText("");
        etRegPassword.setText("");
        etRegConfirmPassword.setText("");
        etRegMobilenumber.setText("");
    }

}
