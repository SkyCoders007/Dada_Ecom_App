package com.mxi.ecommerce.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.ecommerce.R;
import com.mxi.ecommerce.comman.AppController;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.model.CategoryChildData;
import com.mxi.ecommerce.model.CategoryItemData;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mxi.ecommerce.comman.ConnectionUrl.categorylisting;


public class ShopCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ln_home_bottom, ln_category_bottom, ln_deal_bottom, ln_wishlist_bottom, ln_mycc_bottom;

    String title = "Shop by Category";
    TextView tv_toolbar_title;
    LinearLayout img_back_btn;

    CommanClass cc;
    String android_id;

    ProgressDialog pDialog;

    private ExpandableListView ExpandList;

    ArrayList<CategoryChildData> ch_list;
    ExpandListAdapter ExpAdapter;
    String parent_product_id;
    String category_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopcategory);


        cc = new CommanClass(this);

        init();
        clickListner();

        if (!cc.isConnectingToInternet()) {
            cc.showToast(getString(R.string.no_internet));
        } else {
            categoryListingWS();
        }

        android_id = Settings.Secure.getString(ShopCategoryActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        ExpandList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousItem)
                    ExpandList.collapseGroup(previousItem);
                previousItem = groupPosition;
            }
        });


        ///
        img_back_btn.setVisibility(View.GONE);

        Log.e("toolbar", title);

    }

    private void init() {


        ln_home_bottom = (LinearLayout) findViewById(R.id.ln_home_bottom);
        ln_category_bottom = (LinearLayout) findViewById(R.id.ln_category_bottom);
        ln_deal_bottom = (LinearLayout) findViewById(R.id.ln_deal_bottom);
        ln_mycc_bottom = (LinearLayout) findViewById(R.id.ln_mycc_bottom);
        ln_wishlist_bottom = (LinearLayout) findViewById(R.id.ln_wishlist_bottom);
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);

        tv_toolbar_title.setText("Category");
        tv_toolbar_title.setPadding(0, 0, 40, 0);

        img_back_btn = (LinearLayout) findViewById(R.id.img_back_btn);

        ExpandList = (ExpandableListView) findViewById(R.id.exp_list);
        ExpandList.setGroupIndicator(null);
    }

    private void clickListner() {

        ln_home_bottom.setOnClickListener(this);
        ln_deal_bottom.setOnClickListener(this);
        ln_wishlist_bottom.setOnClickListener(this);
        ln_mycc_bottom.setOnClickListener(this);
        ln_category_bottom.setOnClickListener(this);
        img_back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ln_home_bottom:

                Intent intentMainActivity = new Intent(ShopCategoryActivity.this, MainActivity.class);
                intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentMainActivity);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();

                break;

            case R.id.ln_category_bottom:
                break;

            case R.id.ln_mycc_bottom:


                if (cc.loadPrefBoolean("isLogin") == true) {


                Intent intentMyAccount = new Intent(ShopCategoryActivity.this, MyAccountActivity.class);
                startActivity(intentMyAccount);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                } else {
                    cc.showDialogUserNotLogin();
                }


                break;

            case R.id.ln_wishlist_bottom:

                Intent intentWishList = new Intent(ShopCategoryActivity.this, WishlistActivity.class);
                startActivity(intentWishList);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ln_deal_bottom:


                Intent intentDeal = new Intent(ShopCategoryActivity.this, TopDealDetailActivity.class);
                startActivity(intentDeal);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;


            case R.id.img_back_btn:
                onBackPressed();
                break;
        }

    }


    private void categoryListingWS() {

        pDialog = new ProgressDialog(ShopCategoryActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, categorylisting,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        Log.e("categorylisting", response);
                        JSONArray jsonArray = null;
                        JSONArray jsonArray1 = null;

                        try {
                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }

                            String ims = "";
                            JSONObject jObject = new JSONObject(response);


                            //Category Listing
                            jsonArray = jObject.getJSONArray("categories");
                            ArrayList<CategoryItemData> list = new ArrayList<CategoryItemData>();

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                CategoryItemData categorylisting = new CategoryItemData();

                                categorylisting.setName(jsonObject.getString("name"));

                                categorylisting.setImages(jsonObject.getString("image"));
                                categorylisting.setCategory_id(jsonObject.getString("category_id"));

                                category_id = jsonObject.getString("category_id");

                                Log.e("name", jsonObject.getString("name"));
                                Log.e("category_id", jsonObject.getString("category_id"));
                                Log.e("Prentimage", category_id);

                                jsonArray1 = jsonObject.getJSONArray("children");

                                ch_list = new ArrayList<CategoryChildData>();
                                for (int j = 0; j < jsonArray1.length(); j++) {

                                    CategoryChildData categoryhild = new CategoryChildData();
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);

                                    categoryhild.setImage(jsonObject1.getString("image"));
                                    categoryhild.setName(jsonObject1.getString("name"));
                                    categoryhild.setChild_id(jsonObject1.getString("subcategory_id"));

                                    ch_list.add(categoryhild);

                                    Log.e("childrenname", jsonObject1.getString("name"));
                                    Log.e("childrennameImage", jsonObject1.getString("image"));
                                    Log.e("subcategory_id", jsonObject1.getString("subcategory_id"));

                                }

                                categorylisting.setItems(ch_list);
                                list.add(categorylisting);

                            }

                            if (list != null) {

                                ExpAdapter = new ExpandListAdapter(ShopCategoryActivity.this, list);
                                ExpandList.setAdapter(ExpAdapter);
                                pDialog.dismiss();

                            } else {

                                Log.e("name", "...................else........................");

                            }

                            if (jObject.getString("status").equals("200")) {
                                //   Categorylisting = new ArrayList<>();

                            } else if (jObject.getString("status").equals("400")) {
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
                Log.e("Error_categoryListingWS",error.toString());
            }
        }) {

            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                Log.i("response", response.headers.toString());
//                Map<String, String> responseHeaders = response.headers;
//                String rawCookies = responseHeaders.get("Set-Cookie");
//                Log.i("categorylisting_cookies", rawCookies);
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("device_id", android_id);
                return map;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("1811-token", cc.loadPrefString("1811-token"));
                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
//                headers.put("language","en-gb");
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        super.onBackPressed();
    }


    public class ExpandListAdapter extends BaseExpandableListAdapter {

        public Context context;
        private ArrayList<CategoryItemData> groups;

        ProgressBar mProgressBar, mProgressBar1;
        ExpandListAdapter expandListAdapter;
        CommanClass cc;


        public ExpandListAdapter(ShopCategoryActivity shopcategoryActivity, ArrayList<CategoryItemData> list) {
            this.context = shopcategoryActivity;
            this.groups = list;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            ArrayList<CategoryChildData> chList = groups.get(groupPosition).getItems();
            return chList.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            cc = new CommanClass(context);
            CategoryChildData child = (CategoryChildData) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) context
                        .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.category_child_items, null);
            }

            TextView tv = (TextView) convertView.findViewById(R.id.category_child_name);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    demo(groupPosition, childPosition);

                }
            });

            mProgressBar = (ProgressBar) convertView.findViewById(R.id.pgb_image_loading);
            tv.setText(Html.fromHtml(child.getName().toString()));

            return convertView;
        }

        private void demo(int groupPosition, int childPosition) {

            CategoryItemData cm = groups.get(groupPosition);
            cm.getCategory_id();

            CategoryChildData child = (CategoryChildData) getChild(groupPosition, childPosition);
            child.getChild_id();

            Intent i = new Intent(context, CategoryDetailActivity.class);
            i.putExtra("categry_id", cm.getCategory_id());
            i.putExtra("sub_categry_id", child.getChild_id());
            i.putExtra("name", cm.getName());
            context.startActivity(i);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);


        }


        @Override
        public int getChildrenCount(int groupPosition) {
            ArrayList<CategoryChildData> chList = groups.get(groupPosition).getItems();

            return chList.size();
        }

        @Override
        public Object getGroup(int groupPosition) {


            return groups.get(groupPosition);
        }

        @Override
        public int getGroupCount() {

            return groups.size();
        }

        @Override
        public long getGroupId(int groupPosition) {

            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            CategoryItemData group = (CategoryItemData) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater inf = (LayoutInflater) context
                        .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = inf.inflate(R.layout.category_exapan_item, null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.category_name);
            TextView tv_logo = (TextView) convertView.findViewById(R.id.tv_logo);
            ImageView product_image = (ImageView) convertView.findViewById(R.id.product_image);
            mProgressBar = (ProgressBar) convertView.findViewById(R.id.pgb_image_loading);

            String imageurl = group.getImages();

            Log.e("imageurl", imageurl);

            tv.setText(group.getName());


            if (group.getItems().size() > 0) {

            } else {
                tv_logo.setVisibility(View.GONE);
            }

            if (isExpanded) {

                tv_logo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);

            } else {

                tv_logo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up, 0);

            }


            if (imageurl.equalsIgnoreCase("null")) {
                Picasso.with(context).load(R.drawable.no_media).into(product_image);
                mProgressBar.setVisibility(View.GONE);
            } else {
                Picasso.with(context).load(imageurl).into(product_image, new Callback() {
                    @Override
                    public void onSuccess() {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            }
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
