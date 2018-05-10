package com.mxi.ecommerce.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.ecommerce.R;
import com.mxi.ecommerce.comman.AppController;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.comman.ConnectionUrl;
import com.mxi.ecommerce.model.AddressListingModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAddressActivity extends AppCompatActivity implements View.OnClickListener {

    String title = "My Addresses";
    TextView tv_toolbar_title;

    RecyclerView recycler_myaddress;
    RecyclerView.LayoutManager mLayoutManager_myaddress;
    RecyclerView.Adapter mAdapter;

    ArrayList<String> alName;


    FloatingActionButton fab_delviery_address;

    LinearLayout img_back_btn;
    List<AddressListingModel> addressListingModels;
    CommanClass cc;

    ProgressDialog pDialog;
    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";

    String address_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);

        cc = new CommanClass(this);
        address_token = cc.loadPrefString("1811-token");

        init();
        clickListner();

        Log.e("Address_token", address_token);

        mLayoutManager_myaddress = new LinearLayoutManager(this);

        tv_toolbar_title.setText(title);
        tv_toolbar_title.setPadding(0, 0, 40, 0);
        Log.e("toolbar", title);

    }

    private void clickListner() {


        fab_delviery_address.setOnClickListener(this);
        img_back_btn.setOnClickListener(this);
    }

    private void init() {

        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        fab_delviery_address = (FloatingActionButton) findViewById(R.id.fab_delviery_address);
        img_back_btn = (LinearLayout) findViewById(R.id.img_back_btn);
    }


    @Override
    protected void onResume() {

        if (!cc.isConnectingToInternet()) {
            cc.showToast(getString(R.string.no_internet));
        } else {
            getaddress();
        }

        super.onResume();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.fab_delviery_address:

                Intent intentAddAdress = new Intent(MyAddressActivity.this, AddAddressActivity.class);
                startActivity(intentAddAdress);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);


                break;

            case R.id.img_back_btn:

                onBackPressed();

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                break;

        }

    }


    /*Get Address Listing*/
    private void getaddress()
    {

        pDialog = new ProgressDialog(MyAddressActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.get_address,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:get_address", response);
                        JSONArray jsonArray = null;
                        addressListingModels = new ArrayList<>();

                        try {

                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }

                            JSONObject jObject = new JSONObject(response);

                            if (jObject.getString("status").equals("200")) {
                                jsonArray = jObject.getJSONArray("addresses");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    AddressListingModel addlist = new AddressListingModel();

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    addlist.setAddress_id(jsonObject.getString("address_id"));
                                    JSONObject obj = jsonObject.getJSONObject("address");

                                    addlist.setFirstname(obj.getString("firstname"));
                                    addlist.setLastname(obj.getString("lastname"));
                                    addlist.setPostcode(obj.getString("postcode"));
                                    addlist.setZone(obj.getString("zone"));
                                    addlist.setZone_code(obj.getString("zone_code"));
                                    addlist.setArea(obj.getString("area"));
                                    addlist.setWayno(obj.getString("wayno"));
                                    addlist.setHouseno(obj.getString("houseno"));
                                    addlist.setLandmark(obj.getString("landmark"));
                                    addlist.setCountry(obj.getString("country"));
                                    addlist.setCity(obj.getString("city"));


                                    addressListingModels.add(addlist);


                                    Log.e("firstname", obj.getString("firstname"));
                                    Log.e("lastname", obj.getString("lastname"));
                                    Log.e("postcode", obj.getString("postcode"));
                                    Log.e("zone", obj.getString("zone"));
                                    Log.e("zone_code", obj.getString("zone_code"));
                                    Log.e("country", obj.getString("country"));
                                    Log.e("address_id", jsonObject.getString("address_id"));

                                    System.out.println(addressListingModels.size());
                                }
                                if (addressListingModels != null) {
                                    if(pDialog.isShowing()){
                                        pDialog.dismiss();
                                    }

                                    recycler_myaddress = (RecyclerView) findViewById(R.id.recycler_myaddress);
                                    recycler_myaddress.setLayoutManager(mLayoutManager_myaddress);
                                    mAdapter = new MyaddressAdapters(MyAddressActivity.this, addressListingModels);
                                    recycler_myaddress.setAdapter(mAdapter);
                                }
//

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
                Log.e("Error_getaddress",error.toString());
            }
        }) {

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


    public class MyaddressAdapters extends RecyclerView.Adapter<MyaddressAdapters.ViewHolder> {

        ArrayList<Integer> alImage;
        Context context;
        List<AddressListingModel> categoryModels;
        private CheckBox lastChecked = null;
        private int lastCheckedPos = 0;
        String supportSchoolId;
        private int selectedPosition = -1;
        String demo;

        public MyaddressAdapters(MyAddressActivity context, List<AddressListingModel> addressListingModels) {
            this.context = context;
            this.categoryModels = addressListingModels;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.myaddresslisting, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {


            Intent get = getIntent();
            demo = get.getStringExtra("demo");


            if (demo == null) {
                viewHolder.ch_add_address.setVisibility(View.GONE);
                viewHolder.ll_edit_delete.setVisibility(View.VISIBLE);
                viewHolder.view_line_bottom.setVisibility(View.VISIBLE);
                viewHolder.view_line_top.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ch_add_address.setVisibility(View.VISIBLE);
                viewHolder.ll_edit_delete.setVisibility(View.GONE);
                viewHolder.view_line_bottom.setVisibility(View.GONE);
                viewHolder.view_line_top.setVisibility(View.GONE);
            }


            viewHolder.ch_add_address.setTag(i);
            viewHolder.tvSpecies.setText(categoryModels.get(i).getFirstname() + " " + categoryModels.get(i).getLastname());
            viewHolder.iv_address_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!cc.isConnectingToInternet()) {
                        cc.showToast(getString(R.string.no_internet));


                    } else {
                        getItemPos(viewHolder.getAdapterPosition());

                        getaddressdelete();
                    }
                }
            });
            if (i == selectedPosition) {
                viewHolder.ch_add_address.setChecked(true);
            } else viewHolder.ch_add_address.setChecked(false);

            viewHolder.ch_add_address.setOnClickListener(onStateChangedListener(viewHolder.ch_add_address, i));


            viewHolder.ch_add_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                }
            });


            String detailAddress = categoryModels.get(i).getWayno() + " " +
                    categoryModels.get(i).getArea() + "\n" + categoryModels.get(i).getHouseno() + " "
                    + categoryModels.get(i).getLandmark() + "\n" + categoryModels.get(i).getCity() + " "
                    + categoryModels.get(i).getZone() + "\n" + categoryModels.get(i).getCountry() +
                    " " + categoryModels.get(i).getPostcode();

            viewHolder.tv_address_detail.setText(detailAddress);

            viewHolder.iv_address_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!cc.isConnectingToInternet()) {
                        cc.showToast(getString(R.string.no_internet));
                    } else {

                        Log.e("Zone", categoryModels.get(i).getZone() + "");
                        Intent intentEditAddress = new Intent(MyAddressActivity.this, EditAddressActivity.class);
                        intentEditAddress.putExtra("firstname", categoryModels.get(i).getFirstname());
                        intentEditAddress.putExtra("lastname", categoryModels.get(i).getLastname());
                        intentEditAddress.putExtra("city", categoryModels.get(i).getCity());
                        intentEditAddress.putExtra("postalcode", categoryModels.get(i).getPostcode());
                        intentEditAddress.putExtra("state", categoryModels.get(i).getZone());
                        intentEditAddress.putExtra("country", categoryModels.get(i).getCountry());
                        intentEditAddress.putExtra("area", categoryModels.get(i).getArea());
                        intentEditAddress.putExtra("wayno", categoryModels.get(i).getWayno());
                        intentEditAddress.putExtra("houseno", categoryModels.get(i).getHouseno());
                        intentEditAddress.putExtra("landmark", categoryModels.get(i).getLandmark());
                        intentEditAddress.putExtra("addressid", categoryModels.get(i).getAddress_id());
                        startActivity(intentEditAddress);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }
                }
            });


        }

        private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (checkBox.isChecked()) {
                        selectedPosition = position;

                        AddressListingModel cm = categoryModels.get(position);
//                        Intent intentShopCategory = new Intent(context, AddToCartActivity.class);
                        Intent intentShopCategory = new Intent(context, CartConfirmationActivity.class);
                        intentShopCategory.putExtra("firstname", cm.getFirstname());
                        intentShopCategory.putExtra("lastname", cm.getLastname());
                        intentShopCategory.putExtra("city", cm.getCity());
                        intentShopCategory.putExtra("postalcode", cm.getPostcode());
                        intentShopCategory.putExtra("state", cm.getZone());
                        intentShopCategory.putExtra("country", cm.getCountry());
                        intentShopCategory.putExtra("area", cm.getArea());
                        intentShopCategory.putExtra("wayno", cm.getWayno());
                        intentShopCategory.putExtra("houseno", cm.getHouseno());
                        intentShopCategory.putExtra("landmark", cm.getLandmark());
                        intentShopCategory.putExtra("addressid", cm.getAddress_id());
                        context.startActivity(intentShopCategory);
                        ((Activity) context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        ((Activity) context).finish();
                    } else {
                        selectedPosition = -1;
                    }
                    notifyDataSetChanged();
                }
            };
        }

        @Override
        public int getItemCount() {
            return categoryModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView tvSpecies, tv_address_detail;
            public ImageView iv_address_delete, iv_address_edit;
            public CheckBox ch_add_address;
            LinearLayout ll_edit_delete;
            View view_line_top, view_line_bottom;


            public ViewHolder(View itemView) {
                super(itemView);

                tv_address_detail = (TextView) itemView.findViewById(R.id.tv_address_detail);
                tvSpecies = (TextView) itemView.findViewById(R.id.tv_title);
                iv_address_delete = (ImageView) itemView.findViewById(R.id.iv_address_delete);
                iv_address_edit = (ImageView) itemView.findViewById(R.id.iv_address_edit);
                ch_add_address = (CheckBox) itemView.findViewById(R.id.ch_add_address);
                ll_edit_delete = (LinearLayout) itemView.findViewById(R.id.ll_edit_delete);
                view_line_bottom = itemView.findViewById(R.id.view_line_bottom);
                view_line_top = itemView.findViewById(R.id.view_line_top);

            }


        }


        private void getItemPos(int itemPos) {
            AddressListingModel cm = categoryModels.get(itemPos);
            cc.savePrefString("address_id", cm.getAddress_id());
            Log.e("address_id", cm.getAddress_id());

        }
        /*getaddressdelete*/
        private void getaddressdelete() {

            pDialog = new ProgressDialog(MyAddressActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();


            StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.get_address_delete,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.e("response:addressdelete", response);
                            JSONArray jsonArray = null;
                            addressListingModels = new ArrayList<>();

                            try {
                                if(pDialog.isShowing()){
                                    pDialog.dismiss();
                                }

                                JSONObject jObject = new JSONObject(response);
                                jsonArray = jObject.getJSONArray("addresses");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    AddressListingModel addlist = new AddressListingModel();

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    addlist.setAddress_id(jsonObject.getString("address_id"));
                                    JSONObject obj = jsonObject.getJSONObject("address");

                                    addlist.setFirstname(obj.getString("firstname"));
                                    addlist.setLastname(obj.getString("lastname"));
                                    addlist.setPostcode(obj.getString("postcode"));
                                    addlist.setZone(obj.getString("zone"));
                                    addlist.setZone_code(obj.getString("zone_code"));
                                    addlist.setArea(obj.getString("area"));
                                    addlist.setWayno(obj.getString("wayno"));
                                    addlist.setHouseno(obj.getString("houseno"));
                                    addlist.setLandmark(obj.getString("landmark"));
                                    addlist.setCountry(obj.getString("country"));
                                    addlist.setCity(obj.getString("city"));
                                    addressListingModels.add(addlist);

                                    Log.e("firstname", obj.getString("firstname"));
                                    Log.e("lastname", obj.getString("lastname"));
                                    Log.e("postcode", obj.getString("postcode"));
                                    Log.e("zone", obj.getString("zone"));
                                    Log.e("zone_code", obj.getString("zone_code"));
                                    Log.e("country", obj.getString("country"));
                                    Log.e("address_id", jsonObject.getString("address_id"));
                                    System.out.println(addressListingModels.size());

                                }
                                if (addressListingModels != null) {
                                    pDialog.dismiss();
                                    recycler_myaddress = (RecyclerView) findViewById(R.id.recycler_myaddress);
                                    recycler_myaddress.setLayoutManager(mLayoutManager_myaddress);
                                    mAdapter = new MyaddressAdapters(MyAddressActivity.this, addressListingModels);
                                    recycler_myaddress.setAdapter(mAdapter);
                                }
//
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
                    Log.e("Error : VolleyError", error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("address_id", cc.loadPrefString("address_id"));
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
}
