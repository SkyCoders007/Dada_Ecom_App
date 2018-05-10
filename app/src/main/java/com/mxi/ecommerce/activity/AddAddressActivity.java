package com.mxi.ecommerce.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.mxi.ecommerce.model.AddressListingModel;
import com.mxi.ecommerce.model.CountryListing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener {


    TextView tv_toolbar_title;
    LinearLayout img_back_btn;
    Spinner spinner, spinner_state;
    ArrayList<String> CountryName;
    ProgressDialog pDialog;
    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";

    ArrayList<String> worldlist;
    ArrayList<String> statelist;
    ArrayList<CountryListing> country;

    ArrayList<CountryListing> state;

    CommanClass cc;

    EditText et_first_name, et_last_name, et_area, et_way_number, et_building_number, et_postal_code, et_city, et_landmark;

    Button btn_submit;

    String address_token;

    List<AddressListingModel> addressListingModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaddress_activiy);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        cc = new CommanClass(this);
        address_token = cc.loadPrefString("1811-token");

        cc.savePrefString("state_id", "");
        cc.savePrefString("country_id", "");

        init();
        clickListner();
        String title = getString(R.string.deli_address);
        tv_toolbar_title.setText(title);
        tv_toolbar_title.setPadding(0, 0, 40, 0);
        Log.e("toolbar", title);


        Log.e("Address_token", address_token);


        if (!cc.isConnectingToInternet()) {
            cc.showToast(getString(R.string.no_internet));

        } else {
            Getcountry();
        }

    }


    private void clickListner() {

        img_back_btn.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

    }


    private void init() {

        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        img_back_btn = (LinearLayout) findViewById(R.id.img_back_btn);


        et_first_name = (EditText) findViewById(R.id.et_first_name);
        et_last_name = (EditText) findViewById(R.id.et_last_name);
        et_area = (EditText) findViewById(R.id.et_area);
        et_way_number = (EditText) findViewById(R.id.et_way_number);
        et_building_number = (EditText) findViewById(R.id.et_building_number);
        et_postal_code = (EditText) findViewById(R.id.et_postal_code);
        et_city = (EditText) findViewById(R.id.et_city);
        et_landmark = (EditText) findViewById(R.id.et_landmark);

        btn_submit = (Button) findViewById(R.id.btn_submit);

        spinner_state = (Spinner) findViewById(R.id.state_Name);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back_btn:

                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                break;


            case R.id.btn_submit:

                if (!cc.isConnectingToInternet()) {
                    cc.showToast(getString(R.string.no_internet));

                } else {

                    if (isValidate()) {

                        Addaddress();
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

    /*Country  data Listing*/

    private void Getcountry() {

        pDialog = new ProgressDialog(AddAddressActivity.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, ConnectionUrl.get_county_name,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:getcounrtyname", response);
                        JSONArray jsonArray = null;

                        try {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }

                            country = new ArrayList<CountryListing>();
                            // Create an array to populate the spinner
                            worldlist = new ArrayList<String>();
                            worldlist.add(getString(R.string.select_country));
                            JSONObject jObject = new JSONObject(response);

                            jsonArray = jObject.getJSONArray("countries");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                CountryListing countrys = new CountryListing();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                countrys.setCountry_name(jsonObject.getString("name"));
                                countrys.setCountry_id(jsonObject.getString("country_id"));

                                country.add(countrys);
                                worldlist.add(jsonObject.optString("name"));
                            }


                            if (jObject.getString("status").equals("200")) {
//                                cc.showToast(jObject.getString("message"));

                            } else if (jObject.getString("status").equals("400")) {
                                cc.showToast(jObject.getString("message"));
                            }

                            spinner = (Spinner) findViewById(R.id.country_Name);
                            // Spinner adapter
                            spinner.setAdapter(new ArrayAdapter<String>(AddAddressActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, worldlist));
                            // Spinner on item click listener
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> arg0,
                                                           View arg1, int position, long arg3) {

                                    if (position > 0) {

                                        CountryListing countryListings = new CountryListing();
                                        cc.savePrefString("country_id", country.get(position - 1).getCountry_id());
                                        spinner_state.setVisibility(View.VISIBLE);

                                        if (!cc.isConnectingToInternet()) {
                                            cc.showToast(getString(R.string.no_internet));

                                        } else {
                                            Log.e("country", country.get(position - 1).getCountry_name());
                                            Log.e("country_id", country.get(position - 1).getCountry_id());
                                            getstate(country.get(position - 1).getCountry_id());
                                        }

                                    } else {
                                        Log.e("Nothings", ".........");
                                    }

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                }
                            });

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
                Log.e("Error_GetCountry",error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", "arabi");
                headers.put("1811-token", cc.loadPrefString("1811-token"));
                headers.put("Cookie", "PHPSESSID=" + cc.loadPrefString3("PHPSESSID") + ";" + "default=" + cc.loadPrefString3("default"));
//                headers.put("language","en-gb");
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    /*State data Listing*/

    private void getstate(final String country_id) {

        pDialog = new ProgressDialog(AddAddressActivity.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.get_state_name,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:statedata", response);
                        JSONArray jsonArray = null;

                        try {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }

                            state = new ArrayList<CountryListing>();
                            // Create an array to populate the spinner
                            statelist = new ArrayList<String>();
                            statelist.add(getString(R.string.select_state));
                            JSONObject jObject = new JSONObject(response);


                            jsonArray = jObject.getJSONArray("countries");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                CountryListing countrys = new CountryListing();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                countrys.setState_name(jsonObject.getString("name"));
                                countrys.setState_id(jsonObject.getString("country_id"));
                                countrys.setState_zone_id(jsonObject.getString("zone_id"));

                                Log.e("state_zone_id", jsonObject.getString("zone_id"));
                                Log.e("state_zone_name", jsonObject.getString("name"));
                                Log.e("country_id", jsonObject.getString("country_id"));

                                state.add(countrys);
                                statelist.add(jsonObject.optString("name"));
                            }

                            if (jObject.getString("status").equals("200")) {

                            } else if (jObject.getString("status").equals("400")) {
                                cc.showToast(jObject.getString("message"));
                            }

                            // Spinner adapter
                            spinner_state.setAdapter(new ArrayAdapter<String>(AddAddressActivity.this, android.R.layout.simple_spinner_dropdown_item, statelist));

                            // Spinner on item click listener
                            spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> arg0,
                                                           View arg1, int position, long arg3) {
////
                                    if (position > 0) {

                                        CountryListing countryListings = new CountryListing();
                                        Log.e("state_zone_id", state.get(position - 1).getState_zone_id());
                                        Log.e("state_id", state.get(position - 1).getState_id());
                                        Log.e("state", state.get(position - 1).getState_name());


//                                      Log.e("state_id", country.get(position-1).getCountry_id());
                                        cc.savePrefString("state_id", state.get(position - 1).getState_id());

                                    } else {

                                        Log.e("Nothings", ".........");
                                    }

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                    // TODO Auto-generated method stub
                                }
                            });


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
                Log.e("error_getState",error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("country_id", country_id);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("1811-token", cc.loadPrefString("1811-token"));
                headers.put("Cookie", "PHPSESSID=" + cc.loadPrefString3("PHPSESSID") + ";" + "default=" + cc.loadPrefString3("default"));
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    /* Validations */

    public boolean isValidate() {
        String msg = null;

        if (!AndyUtils.nameValidation(et_first_name.getText().toString())) {
            msg = getString(R.string.enter_fname);
            et_first_name.requestFocus();

        } else if (!AndyUtils.nameValidation(et_last_name.getText().toString())) {
            msg = getString(R.string.enter_lname);
            et_last_name.requestFocus();

        } else if (!AndyUtils.nameValidation(et_area.getText().toString())) {
            msg = getString(R.string.enter_area);
            et_area.requestFocus();

        } else if (et_way_number.getText().length() == 0) {
            msg = getString(R.string.enter_way_number);
            et_way_number.requestFocus();

        } else if (et_building_number.getText().length() == 0) {
            msg = getString(R.string.enter_building_number);
            et_building_number.requestFocus();

        } else if (et_postal_code.getText().length() == 0 && et_postal_code.getText().length() < 6) {
            msg = getString(R.string.enter_postal_code);
            et_postal_code.requestFocus();

        } else if (!AndyUtils.nameValidation(et_city.getText().toString())) {
            msg = getString(R.string.enter_cityname);
            et_city.requestFocus();

        } else if (cc.loadPrefString("country_id").isEmpty()) {
            msg = getString(R.string.choose_country);
        } else if (cc.loadPrefString("state_id").isEmpty()) {
            msg = getString(R.string.choose_state);
        } else if (!AndyUtils.nameValidation(et_landmark.getText().toString())) {
            msg = getString(R.string.enter_landmark);
            et_landmark.requestFocus();
        }

        if (msg != null) {
            AndyUtils.showToast(AddAddressActivity.this, msg);
            return false;
        }
        if (msg == null) {
            return true;
        }


        AndyUtils.showToast(AddAddressActivity.this, msg);
        return false;
    }


    public void clearField() {
        et_first_name.setText("");
        et_last_name.setText("");
        et_area.setText("");
        et_landmark.setText("");
        et_city.setText("");
        et_way_number.setText("");
        et_postal_code.setText("");
        et_building_number.setText("");

    }


    private void Addaddress() {

        pDialog = new ProgressDialog(AddAddressActivity.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.add_address,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:Addadrres", response);
                        JSONArray jsonArray = null;
                        addressListingModels = new ArrayList<>();


                        try {
                            // pDialog.dismiss();
                            //
                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }

                            JSONObject jObject = new JSONObject(response);
//
                            if (jObject.getString("status").equals("200")) {
                                clearField();

                                cc.showToast(getString(R.string.add_success_added));
                                finish();

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
                Log.e("Error_address",error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put("firstname", et_first_name.getText().toString().trim());
                map.put("lastname", et_last_name.getText().toString().trim());
                map.put("wayno", et_way_number.getText().toString().trim());
                map.put("area", et_area.getText().toString().trim());
                map.put("houseno", et_building_number.getText().toString().trim());
                map.put("postcode", et_postal_code.getText().toString().trim());
                map.put("city", et_city.getText().toString().trim());
                map.put("landmark", et_landmark.getText().toString().trim());
                map.put("country_id", cc.loadPrefString("country_id"));
                map.put("company", "Mxi");
                map.put("zone_id", cc.loadPrefString("state_id"));
                Log.i("@@request Add Addres", map.toString());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("1811-token", address_token);
                headers.put("Cookie", "PHPSESSID=" + cc.loadPrefString3("PHPSESSID") + ";" + "default=" + cc.loadPrefString3("default"));
//                headers.put("language","en-gb");
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }


}
