package com.mxi.ecommerce.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    String title= "Change password";
    TextView tv_toolbar_title;
    LinearLayout img_back_btn;

    EditText et_old_psd,et_new_psd,et_confirm_new_psd;
    Button btn_chnge_password;

    CommanClass cc;


    ProgressDialog pDialog;
    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";

    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        cc=new CommanClass(this);
        token = cc.loadPrefString("1811-token");

        init();
        clickListner();

        Log.e("token", token);

        tv_toolbar_title.setText(title);
        tv_toolbar_title.setPadding(0, 0, 40, 0);
        Log.e("toolbar",title);

    }

    private void clickListner() {
        img_back_btn.setOnClickListener(this);
        btn_chnge_password.setOnClickListener(this);

    }

    private void init() {
        tv_toolbar_title=(TextView)findViewById(R.id.tv_toolbar_title);
        img_back_btn=(LinearLayout) findViewById(R.id.img_back_btn);
        et_old_psd=(EditText)findViewById(R.id.et_old_psd);
        et_new_psd=(EditText)findViewById(R.id.et_new_psd);
        et_confirm_new_psd=(EditText)findViewById(R.id.et_confirm_new_psd);


        btn_chnge_password=(Button)findViewById(R.id.btn_chnge_password);


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {


            case R.id.img_back_btn:
                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                break;


            case  R.id.btn_chnge_password:
                if (!cc.isConnectingToInternet()) {
                    cc.showToast(getString(R.string.no_internet));

                }else {
                    if (isValidate()) {

                        changepassword();

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

    private void changepassword() {

        pDialog = new ProgressDialog(ChangePasswordActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.change_passrord,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:changepassword", response);
                        try {
                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }

                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("200")) {
                                clearField();
//                                cc.showToast(jObject.getString("message"));
                                cc.showToast("Password is changed successfully");
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
                if(pDialog.isShowing()){
                    pDialog.dismiss();
                }
            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put("oldpassword", et_old_psd.getText().toString().trim());
                map.put("password", et_confirm_new_psd.getText().toString().trim());
                Log.i("@@change_password", map.toString());

                return map;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language",cc.loadPrefString("language"));
                headers.put("1811-token",token);
                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
//                headers.put("language","en-gb");
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    public boolean isValidate() {
        String msg = null;

        if (TextUtils.isEmpty(et_old_psd.getText().toString())) {
            msg = "Enter Valid Password";
            et_old_psd.requestFocus();
        } else if (TextUtils.isEmpty(et_new_psd.getText().toString())) {
            msg = "Enter Valid Password";
            et_new_psd.requestFocus();
        } else if (TextUtils.isEmpty(et_confirm_new_psd.getText().toString())) {
            msg = "Enter Valid Password";
            et_confirm_new_psd.requestFocus();
        } else if (!et_confirm_new_psd.getText().toString().equals(et_new_psd.getText().toString())) {
            msg = "Password didn't match";
            et_new_psd.requestFocus();

        }
        if (msg != null) {
            AndyUtils.showToast(ChangePasswordActivity.this, msg);
            return false;
        }
        if (msg == null) {
            return true;
        }
        AndyUtils.showToast(ChangePasswordActivity.this, msg);
        return false;
    }

    public void clearField() {
        et_new_psd.setText("");
        et_confirm_new_psd.setText("");
        et_old_psd.setText("");
    }
}
