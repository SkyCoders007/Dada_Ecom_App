package com.mxi.ecommerce.activity;

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

/**
 * Created by aksahy on 4/1/18.
 */

public class EditAddressActivity extends AppCompatActivity implements View.OnClickListener {

    String title = "Edit address";
    TextView tv_toolbar_title;
    LinearLayout img_back_btn;
    Spinner spinner, spinner_state;
    ArrayList<String> CountryName;
    //    ProgressDialog pDialog;
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

    String strCountry, strState, strStateId, strCountryId, addressId;
    int stateSelectIndex = 0;

    boolean isFirstTime = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaddress_activiy);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        cc = new CommanClass(this);
        address_token = cc.loadPrefString("1811-token");

        init();
        clickListner();

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

        et_first_name.setText(getIntent().getStringExtra("firstname"));
        et_last_name.setText(getIntent().getStringExtra("lastname"));
        et_city.setText(getIntent().getStringExtra("city"));
        et_postal_code.setText(getIntent().getStringExtra("postalcode"));
        et_area.setText(getIntent().getStringExtra("area"));
        et_way_number.setText(getIntent().getStringExtra("wayno"));
        et_building_number.setText(getIntent().getStringExtra("houseno"));
        et_landmark.setText(getIntent().getStringExtra("landmark"));

        strCountry = getIntent().getStringExtra("country");
        strState = getIntent().getStringExtra("state");
        addressId = getIntent().getStringExtra("addressid");

        Log.e("Zone", getIntent().getStringExtra("state"));
        Log.e("strState ", strState);

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

                        EditAddress();
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

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, ConnectionUrl.get_county_name,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:getcounrtyname", response);
                        JSONArray jsonArray = null;

                        try {
                            // pDialog.dismiss();
                            //
//                            pDialog.dismiss();
                            country = new ArrayList<CountryListing>();
                            // Create an array to populate the spinner
                            worldlist = new ArrayList<String>();
                            worldlist.add("Select Country");
                            JSONObject jObject = new JSONObject(response);


                            jsonArray = jObject.getJSONArray("countries");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                CountryListing countrys = new CountryListing();

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                countrys.setCountry_name(jsonObject.getString("name"));
                                countrys.setCountry_id(jsonObject.getString("country_id"));

                                if (countrys.getCountry_name().equals(strCountry)) {
                                    strCountryId = countrys.getCountry_id();

                                }
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
                            spinner.setAdapter(new ArrayAdapter<String>(EditAddressActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, worldlist));

                            if (isFirstTime) {
                                spinner.setSelection(worldlist.indexOf(strCountry));
                            }


//                            if (strCountryId != null) {
//                                getstate(strCountryId);
//                            }

                            // Spinner on item click listener
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> arg0,
                                                           View arg1, int position, long arg3) {


                                    if (position > 0) {

                                        CountryListing countryListings = new CountryListing();

//                                        cc.savePrefString("country_id", country.get(position - 1).getCountry_id());
                                        strCountryId = country.get(position - 1).getCountry_id();
                                        spinner_state.setVisibility(View.VISIBLE);

                                        if (!cc.isConnectingToInternet()) {
                                            cc.showToast(getString(R.string.no_internet));

                                        } else {
                                            Log.e("country", country.get(position - 1).getCountry_name());
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
                error.printStackTrace();
                Log.e("Error_getCountry",error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", "arabi");
                headers.put("1811-token", cc.loadPrefString("1811-token"));
                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
//                headers.put("language","en-gb");
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    /*State data Listing*/

    private void getstate(final String country_id) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.get_state_name,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:statedata", response);
                        JSONArray jsonArray = null;

                        try {
                            // pDialog.dismiss();
                            //
//                            pDialog.dismiss();
                            state = new ArrayList<CountryListing>();
                            // Create an array to populate the spinner
                            statelist = new ArrayList<String>();
                            statelist.add("Select State");
                            JSONObject jObject = new JSONObject(response);


                            jsonArray = jObject.getJSONArray("countries");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                CountryListing countrys = new CountryListing();


                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                countrys.setState_name(jsonObject.getString("name"));
                                countrys.setState_id(jsonObject.getString("country_id"));
                                countrys.setState_zone_id(jsonObject.getString("zone_id"));

                                Log.e("strState", strState);
                                Log.e("stateName", countrys.getState_name());

                                if (countrys.getState_name().equals(strState)) {
                                    strStateId = countrys.getState_zone_id();
                                    stateSelectIndex = i;
                                }

                                Log.e("state_zone_id", jsonObject.getString("zone_id"));


                                state.add(countrys);

                                statelist.add(jsonObject.optString("name"));


                            }
                            if (jObject.getString("status").equals("200")) {
//                                cc.showToast(jObject.getString("message"));

                            } else if (jObject.getString("status").equals("400")) {
                                cc.showToast(jObject.getString("message"));
                            }

                            // Spinner adapter
                            spinner_state.setAdapter(new ArrayAdapter<String>(EditAddressActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, statelist));

                            if (isFirstTime) {
                                Log.e("indexOF", stateSelectIndex + "");
                                Log.e("indexOFName", strState + "");
                                isFirstTime = false;
                                spinner_state.setSelection(statelist.indexOf(strState));
//                                spinner_state.setSelection(stateSelectIndex);

                            }


                            // Spinner on item click listener
                            spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> arg0,
                                                           View arg1, int position, long arg3) {
////
                                    if (position > 0) {

                                        strStateId = state.get(position - 1).getState_zone_id();
                                        Log.e("zipcode_id", state.get(position - 1).getState_zone_id());
                                        Log.e("name", state.get(position - 1).getState_name());
//                                        Log.e("country_id", state.get(position - 1).getCountry_id());

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
                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    /* Validations */

    public boolean isValidate() {
        String msg = null;

        if (!AndyUtils.nameValidation(et_first_name.getText().toString())) {
            msg = "Enter FirstName";
            et_first_name.requestFocus();

        } else if (!AndyUtils.nameValidation(et_last_name.getText().toString())) {
            msg = "Enter LastName";
            et_last_name.requestFocus();

        } else if (!AndyUtils.nameValidation(et_area.getText().toString())) {
            msg = "Enter Area";
            et_area.requestFocus();

        } else if (et_way_number.getText().length() == 0) {
            msg = "Enter Way Number properly";
            et_way_number.requestFocus();

        } else if (et_building_number.getText().length() == 0) {
            msg = "Enter Building Number";
            et_building_number.requestFocus();

        } else if (et_postal_code.getText().length() == 0 && et_postal_code.getText().length() < 6) {
            msg = "Enter Postal code";
            et_postal_code.requestFocus();

        } else if (!AndyUtils.nameValidation(et_city.getText().toString())) {
            msg = "Enter City Name";
            et_city.requestFocus();

        } else if (!AndyUtils.nameValidation(et_landmark.getText().toString())) {
            msg = "Enter Landmark";
            et_landmark.requestFocus();

        }
        if (msg != null) {
            AndyUtils.showToast(EditAddressActivity.this, msg);
            return false;
        }
        if (msg == null) {
            return true;
        }
        AndyUtils.showToast(EditAddressActivity.this, msg);
        return false;
    }

    /* Clear Fields*/

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

    private void EditAddress() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.edit_address,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:Addadrres", response);
                        JSONArray jsonArray = null;
                        addressListingModels = new ArrayList<>();

                        try {
                            JSONObject jObject = new JSONObject(response);

                            if (jObject.getString("status").equals("200")) {
                                clearField();
                                cc.showToast("Address successfully added");
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
                error.printStackTrace();
                Log.e("Error_editAddress",error.toString());
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
                map.put("country_id", strCountryId);
                map.put("company", "Mxi");
                map.put("zone_id", strStateId);
                map.put("address_id", addressId);

                Log.i("@@request Add Addres", map.toString());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("1811-token", address_token);
                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
//                headers.put("language","en-gb");
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }
}
