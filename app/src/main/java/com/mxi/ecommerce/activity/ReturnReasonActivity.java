package com.mxi.ecommerce.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.ecommerce.Helper.ReturnProductReason;
import com.mxi.ecommerce.R;
import com.mxi.ecommerce.comman.AndyUtils;
import com.mxi.ecommerce.comman.AppController;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.comman.ConnectionUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReturnReasonActivity extends AppCompatActivity {

    CommanClass cc;
    ProgressDialog pDialog;
    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";
    String android_id;
    String username;
    RadioGroup radio_group;
    ArrayList<ReturnProductReason> questionsArrayList;
    String answer = "";
    String return_reason_id = "";
    RadioButton radioButtonyes, radioButtonno;
    String opened="0";
    LinearLayout ln_reason_next_two;
    EditText et_comment;
    Dialog dialog;
    public boolean checked = false;
    ImageView return_images;
    LinearLayout ln_confirm_gone;
    NestedScrollView ln_confirm_gone2;
    TextView conformation_text;
    String done="Done";
    TextView tv_doone;

    String title = "Product Detail";
    String titles = "Confirmation";
    LinearLayout img_back_btn;
    TextView tv_toolbar_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_reason);

        cc = new CommanClass(this);
        radio_group = (RadioGroup) findViewById(R.id.radio_group);
        radioButtonyes = (RadioButton) findViewById(R.id.radio_yes);
        radioButtonno = (RadioButton) findViewById(R.id.radio_no);
        ln_reason_next_two = (LinearLayout) findViewById(R.id.ln_reason_next_two);
        et_comment = (EditText) findViewById(R.id.et_comment);
        return_images = (ImageView) findViewById(R.id.return_images);

        ln_confirm_gone2=(NestedScrollView) findViewById(R.id.ln_confirm_gone2);
        conformation_text=(TextView) findViewById(R.id.conformation_text);
        tv_doone=(TextView) findViewById(R.id.tv_doone);
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





        ln_reason_next_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate()) {

                    showForgotPasswordPopup();


                }
            }
        });

        radioButtonyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opened = "0";
                Log.e("Opened__YES", opened);


            }
        });
        radioButtonno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opened = "1";
                Log.e("Opened__NO", opened);

            }
        });

        if (!cc.isConnectingToInternet()) {
            cc.showToast(getString(R.string.no_internet));

        } else {
            ReturnResonList();
        }

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton choose_answer = (RadioButton) group.findViewById(checkedId);
                Log.e("checkedId", checkedId + "");
                if (null != choose_answer && checkedId > -1) {
                    answer = choose_answer.getText().toString().trim();
                    Log.e("answer", answer);

                    Log.e("Matched_id", questionsArrayList.get(checkedId).getAchive_id());
                    cc.savePrefString("selected_reason_id", questionsArrayList.get(checkedId).getAchive_id());
                    cc.savePrefString("selected_reason_text", questionsArrayList.get(checkedId).getAchive_question());

                    return_reason_id = questionsArrayList.get(checkedId).getAchive_id();

                }

            }
        });



    }

    private void showForgotPasswordPopup() {

        dialog = new Dialog(ReturnReasonActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_returnproduct);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);



        TextView tv_close = (TextView) dialog.findViewById(R.id.tv_cancel);
        TextView tv_apply = (TextView) dialog.findViewById(R.id.tv_apply);

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cc.isConnectingToInternet()) {
                    cc.showToast(getString(R.string.no_internet));

                }else {


                    ProductReason();


                }


            }
        });
        dialog.show();
    }
    private void ProductReason() {

        pDialog = new ProgressDialog(ReturnReasonActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.returnorder_reason_submit,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:login", response);
                        JSONArray jsonArray = null;
                        if(pDialog.isShowing()){
                            pDialog.dismiss();
                        }

                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }

                        try {
                            String ims = "";


                            JSONObject jObject = new JSONObject(response);
                            String message=jObject.getString("message");
                            conformation_text.setText(message);
                            tv_doone.setText(done);

                            tv_toolbar_title.setText(titles);
                            tv_toolbar_title.setPadding(0, 0, 40, 0);
                            Log.e("toolbar", title);


                            ln_reason_next_two.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i=new Intent(ReturnReasonActivity.this,MainActivity.class);
                                    startActivity(i);
                                }
                            });



                            Log.e("message",message);

                            return_images.setImageResource(R.drawable.return_step_three);
//                            ln_confirm_gone.setVisibility(View.GONE);
                            ln_confirm_gone2.setVisibility(View.GONE);

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

                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                error.printStackTrace();
                Log.e("Error_productReason",error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();

                map.put("order_id", cc.loadPrefString("return_order_id"));
                map.put("firstname", cc.loadPrefString("firstname"));
                map.put("product_id", cc.loadPrefString("return_product_id"));
                map.put("lastname",cc.loadPrefString("lastname"));
                map.put("email", cc.loadPrefString("email"));
                map.put("telephone",cc.loadPrefString("mobile"));
                map.put("product", cc.loadPrefString("return_product_name"));
                map.put("model",  cc.loadPrefString("return_product_model"));
                map.put("quantity",  cc.loadPrefString("return_product_quantity"));
                map.put("opened", opened);
                map.put("return_reason_id", return_reason_id);
                map.put("comment",et_comment.getText().toString().trim());
                map.put("date_ordered", cc.loadPrefString("return_product_date"));

                Log.i("@@request SignUp", map.toString());

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



    private void ReturnResonList() {

        pDialog = new ProgressDialog(ReturnReasonActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.returnorder_reason,
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
                                questionsArrayList = new ArrayList<>();

                                jsonArray = jObject.getJSONArray("return_reasons");
                                for (int i = 0; i < jsonArray.length(); i++) {


                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    ReturnProductReason getdiscountlisting = new ReturnProductReason();

                                    getdiscountlisting.setAchive_id(jsonObject.getString("return_reason_id"));
                                    getdiscountlisting.setAchive_question(jsonObject.getString("name"));

                                    questionsArrayList.add(getdiscountlisting);


                                    AppCompatRadioButton button = new AppCompatRadioButton(ReturnReasonActivity.this);
                                    button.setId(i);
                                    button.setText(jsonObject.getString("name"));
                                    radio_group.addView(button);
                                    button.setTextColor(Color.parseColor("#000000"));
                                    button.setHighlightColor(Color.WHITE);
                                    button.setTextSize(16);


                                }
//
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
                Log.e("Error_ReturnReason",error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }


    public boolean isValidate() {
        String msg = null;

       if (answer.isEmpty()) {
            msg = "Select Reason";


        }
        if (msg != null) {
            AndyUtils.showToast(ReturnReasonActivity.this, msg);
            return false;
        }
        if (msg == null) {
            return true;
        }
        AndyUtils.showToast(ReturnReasonActivity.this, msg);
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}

