package com.mxi.ecommerce.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
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
import com.mxi.ecommerce.adapter.TopDealGridListingAdapter;
import com.mxi.ecommerce.comman.AppController;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.comman.ConnectionUrl;
import com.mxi.ecommerce.model.TopDealData;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodayTopDealActivity extends AppCompatActivity implements View.OnClickListener {

    List<TopDealData> topDealDatas;

    LinearLayout ln_home_bottom, ln_category_bottom, ln_deal_bottom, ln_wishlist_bottom, ln_mycc_bottom;
    RecyclerView recycleview_listing_items;
    RecyclerView.LayoutManager mLayoutManager_listing_items;

    RecyclerView.Adapter mAdapter;
    TopDealGridListingAdapter adapter;

    ImageView img_listing, img_gridelisting;

    LinearLayout ln_list, ln_gride;

    String title = "Today's Deals";
    TextView tv_toolbar_title, tv_no_data_available;

    GridView grid;

    LinearLayout img_back_btn;


    CommanClass cc;
    ProgressDialog pDialog;
    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";
    String tokens;

    Button sorting_list;


    boolean isGridViewOn = true;
    boolean isListViewOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_top_deal);
        cc = new CommanClass(this);

        tokens = cc.loadPrefString("1811-token");
        isGridViewOn = true;
        isListViewOn = false;
//        cc.savePrefString("ttd_order", "ASC");
//        cc.savePrefString("ttd_name", "name");

        init();
        clickListner();

        mLayoutManager_listing_items = new LinearLayoutManager(this);
        recycleview_listing_items.setLayoutManager(mLayoutManager_listing_items);

        Log.e("category_token", tokens);

        tv_toolbar_title.setText(title);
        tv_toolbar_title.setPadding(0, 0, 40, 0);
        Log.e("toolbar", title);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!cc.isConnectingToInternet()) {
            alert();

        } else {
            categoryDetailWS();
        }
    }

    private void alert() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TodayTopDealActivity.this);
        alertDialogBuilder
                .setMessage(R.string.dialog_internet_alert)
                .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void clickListner() {

        sorting_list.setOnClickListener(this);
        ln_home_bottom.setOnClickListener(this);
        ln_deal_bottom.setOnClickListener(this);
        ln_wishlist_bottom.setOnClickListener(this);
        ln_mycc_bottom.setOnClickListener(this);
        ln_category_bottom.setOnClickListener(this);
        img_back_btn.setOnClickListener(this);

        img_gridelisting.setOnClickListener(this);
        img_listing.setOnClickListener(this);

    }

    private void init() {

//        tv_toolbar_title =(TextView)findViewById(R.id.topdeal_title);
        grid = (GridView) findViewById(R.id.grid);

        img_gridelisting = (ImageView) findViewById(R.id.img_gridelisting);
        img_listing = (ImageView) findViewById(R.id.img_listing);

        sorting_list = (Button) findViewById(R.id.sorting_list);

        ln_home_bottom = (LinearLayout) findViewById(R.id.ln_home_bottom);
        ln_category_bottom = (LinearLayout) findViewById(R.id.ln_category_bottom);
        ln_deal_bottom = (LinearLayout) findViewById(R.id.ln_deal_bottom);
        ln_mycc_bottom = (LinearLayout) findViewById(R.id.ln_mycc_bottom);
        ln_wishlist_bottom = (LinearLayout) findViewById(R.id.ln_wishlist_bottom);
        img_back_btn = (LinearLayout) findViewById(R.id.img_back_btn);
        recycleview_listing_items = (RecyclerView) findViewById(R.id.recycleview_listing);
        ln_list = (LinearLayout) findViewById(R.id.ln_list);
        ln_gride = (LinearLayout) findViewById(R.id.ln_gride);


        img_listing.setColorFilter(getResources().getColor(R.color.color_purple));
        img_gridelisting.setColorFilter(getResources().getColor(R.color.colorAccent));
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        tv_no_data_available = (TextView) findViewById(R.id.tv_no_data_available);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.sorting_list:

                PopupMenu popup = new PopupMenu(TodayTopDealActivity.this, sorting_list);
                popup.getMenuInflater().inflate(R.menu.category_sortings_list, popup.getMenu());


                if (cc.loadPrefString("ttd_order").equals("DESC") && cc.loadPrefString("ttd_name").equals("rating")) {

                    MenuItem item = popup.getMenu().findItem(R.id.model_z_a);
                    item.setChecked(true);

//                    case R.id.model_z_a:
//
//                        String order = "DESC";
//                        String name = "rating";

                } else if (cc.loadPrefString("ttd_order").equals("DESC") && cc.loadPrefString("ttd_name").equals("model")) {

                    MenuItem item = popup.getMenu().findItem(R.id.model_a_z);
                    item.setChecked(true);

//                    case R.id.model_a_z:
//
//                        String order = "DESC";
//                        String name = "model";

                } else if (cc.loadPrefString("ttd_order").equals("ASC") && cc.loadPrefString("ttd_name").equals("model")) {

                    MenuItem item = popup.getMenu().findItem(R.id.rate_lowest);
                    item.setChecked(true);

//                    case R.id.rate_lowest:
//
//                        String order = "ASC";
//                        String name = "model";

                } else if (cc.loadPrefString("ttd_order").equals("ASC") && cc.loadPrefString("ttd_name").equals("rating")) {

                    MenuItem item = popup.getMenu().findItem(R.id.rate_highest);
                    item.setChecked(true);

//                    case R.id.rate_highest:
//
//                        String order = "ASC";
//                        String name = "rating";

                } else if (cc.loadPrefString("ttd_order").equals("ASC") && cc.loadPrefString("ttd_name").equals("price")) {


                    MenuItem item = popup.getMenu().findItem(R.id.price_l_h);
                    item.setChecked(true);

//                    case R.id.price_l_h:
//
//                        String order = "ASC";
//                        String name = "price";

                } else if (cc.loadPrefString("ttd_order").equals("DESC") && cc.loadPrefString("ttd_name").equals("price")) {

                    MenuItem item = popup.getMenu().findItem(R.id.price_H_L);
                    item.setChecked(true);

//                    case R.id.price_H_L:
//
//                        String order = "DESC";
//                        String name = "price";

                } else if (cc.loadPrefString("ttd_order").equals("DESC") && cc.loadPrefString("ttd_name").equals("name")) {

                    MenuItem item = popup.getMenu().findItem(R.id.name_z_to_a);
                    item.setChecked(true);

//                    case R.id.name_z_to_a:
//
//                        String order = "DESC";
//                        String name = "name";

                } else if (cc.loadPrefString("ttd_order").equals("ASC") && cc.loadPrefString("ttd_name").equals("name")) {


                    MenuItem item = popup.getMenu().findItem(R.id.name_a_z);
                    item.setChecked(true);
//                    case R.id.name_a_z:
//
//                        String order = "ASC";
//                        String name = "name";

                } else {
                    MenuItem item = popup.getMenu().findItem(R.id.defult);
                    item.setChecked(true);
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.defult:

                                if (item.isChecked()) {
                                    item.setChecked(false);
                                } else {
                                    item.setChecked(true);

                                    String order = "DEFAULT";
                                    String name = "name";
                                    cc.savePrefString("ttd_order", order);
                                    cc.savePrefString("ttd_name", name);

                                    if (!cc.isConnectingToInternet()) {

                                        alert();

                                    } else {
                                        categoryDetailWS();

                                    }

                                }

                                break;

                            case R.id.name_a_z:

                                if (item.isChecked()) {
                                    item.setChecked(false);
                                } else {
                                    item.setChecked(true);

                                    String order = "ASC";
                                    String name = "name";
                                    cc.savePrefString("ttd_order", order);
                                    cc.savePrefString("ttd_name", name);

                                    if (!cc.isConnectingToInternet()) {
                                        alert();

                                    } else {
                                        categoryDetailWS();
                                    }

                                }
                                break;


                            case R.id.name_z_to_a:

                                if (item.isChecked()) {
                                    item.setChecked(false);
                                } else {
                                    item.setChecked(true);

                                    String order = "DESC";
                                    String name = "name";
                                    cc.savePrefString("ttd_order", order);
                                    cc.savePrefString("ttd_name", name);

                                    if (!cc.isConnectingToInternet()) {
                                        alert();

                                    } else {
                                        categoryDetailWS();
                                    }

                                }
                                break;

                            case R.id.price_H_L:

                                if (item.isChecked()) {
                                    item.setChecked(false);
                                } else {


                                    if (!cc.isConnectingToInternet()) {


                                        alert();

                                    } else {

                                        item.setChecked(true);
                                        String order = "DESC";
                                        String name = "price";

                                        cc.savePrefString("ttd_order", order);
                                        cc.savePrefString("ttd_name", name);

                                        categoryDetailWS();
                                    }
                                }

                                break;

                            case R.id.price_l_h:

                                if (item.isChecked()) {
                                    item.setChecked(false);
                                } else {


                                    if (!cc.isConnectingToInternet()) {
                                        alert();
                                    } else {
                                        item.setChecked(true);


                                        String order = "ASC";
                                        String name = "price";
                                        cc.savePrefString("ttd_order", order);
                                        cc.savePrefString("ttd_name", name);

                                        categoryDetailWS();
                                    }
                                }

                                break;

                            case R.id.rate_highest:

                                if (item.isChecked()) {
                                    item.setChecked(false);
                                } else {
                                    item.setChecked(true);

                                    String order = "ASC";
                                    String name = "rating";
                                    cc.savePrefString("ttd_order", order);
                                    cc.savePrefString("ttd_name", name);

                                    if (!cc.isConnectingToInternet()) {
                                        alert();
                                    } else {
                                        categoryDetailWS();
                                    }
                                }

                                break;

                            case R.id.rate_lowest:

                                if (item.isChecked()) {
                                    item.setChecked(false);
                                } else {
                                    item.setChecked(true);

                                    String order = "ASC";
                                    String name = "model";
                                    cc.savePrefString("ttd_order", order);
                                    cc.savePrefString("ttd_name", name);

                                    if (!cc.isConnectingToInternet()) {
                                        alert();
                                    } else {
                                        categoryDetailWS();
                                    }
                                }

                                break;

                            case R.id.model_a_z:

                                if (item.isChecked()) {
                                    item.setChecked(false);
                                } else {
                                    item.setChecked(true);

                                    String order = "DESC";
                                    String name = "model";
                                    cc.savePrefString("ttd_order", order);
                                    cc.savePrefString("ttd_name", name);

                                    if (!cc.isConnectingToInternet()) {
                                        alert();

                                    } else {
                                        categoryDetailWS();
                                    }
                                }

                                break;


                            case R.id.model_z_a:

                                if (item.isChecked()) {
                                    item.setChecked(false);
                                } else {
                                    item.setChecked(true);

                                    String order = "DESC";
                                    String name = "rating";
                                    cc.savePrefString("ttd_order", order);
                                    cc.savePrefString("ttd_name", name);

                                    if (!cc.isConnectingToInternet()) {
                                        alert();
                                    } else {
                                        categoryDetailWS();
                                    }
                                }

                                break;
                        }
                        return false;
                    }
                });

                popup.show();
                break;


            case R.id.ln_home_bottom:

                Intent intentMainActivity = new Intent(TodayTopDealActivity.this, MainActivity.class);
                intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentMainActivity);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);


                break;

            case R.id.ln_category_bottom:

                Intent intentShopCategory = new Intent(TodayTopDealActivity.this, ShopCategoryActivity.class);
                startActivity(intentShopCategory);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ln_mycc_bottom:

                Intent intentMyAccount = new Intent(TodayTopDealActivity.this, MyAccountActivity.class);
                startActivity(intentMyAccount);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ln_wishlist_bottom:

                Intent intentWishList = new Intent(TodayTopDealActivity.this, WishlistActivity.class);
                startActivity(intentWishList);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ln_deal_bottom:

                Intent intentDeal = new Intent(TodayTopDealActivity.this, TopDealDetailActivity.class);
                startActivity(intentDeal);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;


            case R.id.img_back_btn:

                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                break;


            case R.id.img_gridelisting:

                if(isListViewOn){

                    isGridViewOn = true;
                    isListViewOn = false;
                    recycleview_listing_items.setAdapter(null);
                    grid.setAdapter(null);

                    adapter = null;
                    mAdapter = null;

                    categoryDetailWS();

                    ln_gride.setVisibility(View.VISIBLE);
                    ln_list.setVisibility(View.GONE);
                    img_listing.setColorFilter(getResources().getColor(R.color.color_purple));
                    img_gridelisting.setColorFilter(getResources().getColor(R.color.colorAccent));
                }

                break;

            case R.id.img_listing:


                if(isGridViewOn){
                    isGridViewOn = false;
                    isListViewOn = true;

                    recycleview_listing_items.setAdapter(null);
                    grid.setAdapter(null);

                    adapter = null;
                    mAdapter = null;

                    categoryDetailWS();

                    ln_list.setVisibility(View.VISIBLE);
                    ln_gride.setVisibility(View.GONE);
                    img_listing.setColorFilter(getResources().getColor(R.color.colorAccent));
                    img_gridelisting.setColorFilter(getResources().getColor(R.color.color_purple));

                }



                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private void categoryDetailWS() {

        pDialog = new ProgressDialog(TodayTopDealActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.todaytopdeal,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:todaydeal", response);
                        JSONArray jsonArray = null;
                        if(pDialog.isShowing()){
                            pDialog.dismiss();
                        }
                        topDealDatas = new ArrayList<>();
                        try {
                            JSONObject jObject = new JSONObject(response);

                            jsonArray = jObject.getJSONArray("products");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                TopDealData categoryListingdata = new TopDealData();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                categoryListingdata.setPrice(jsonObject.getString("price"));
                                categoryListingdata.setShare_url(jsonObject.getString("shareProductUrl"));
                                categoryListingdata.setImageUrl(jsonObject.getString("thumb"));
                                categoryListingdata.setTopdealname(jsonObject.getString("name"));
                                categoryListingdata.setProductid(jsonObject.getString("product_id"));
                                categoryListingdata.setSpecial(jsonObject.getString("special"));
                                categoryListingdata.setModel(jsonObject.getString("model"));
                                categoryListingdata.setTax(jsonObject.getString("tax"));
                                categoryListingdata.setInCart(jsonObject.getBoolean("isInCart"));

//                                if (tokens.isEmpty()) {
//                                    if (dbHelper.ProductExist(categoryListingdata.getProductid() + "")) {
//                                        categoryListingdata.setIswishlist(true);
//                                    } else {
//                                        categoryListingdata.setIswishlist(false);
//                                    }
//                                } else {
                                    categoryListingdata.setIswishlist(jsonObject.getBoolean("iswishlist"));
//                                }

                                Log.e("product_name", jsonObject.getString("name"));

                                topDealDatas.add(categoryListingdata);

                            }

                            if (topDealDatas.size() > 0) {
                                pDialog.dismiss();

                                mAdapter = new TopDealsListingAdapter(TodayTopDealActivity.this);
                                recycleview_listing_items.setAdapter(mAdapter);
                                adapter = new TopDealGridListingAdapter(TodayTopDealActivity.this, topDealDatas);
                                grid.setAdapter(adapter);


                            } else {
                                Log.e("nothingsss", "...........");

                                tv_no_data_available.setVisibility(View.VISIBLE);
                                sorting_list.setVisibility(View.GONE);
                                img_gridelisting.setVisibility(View.GONE);
                                img_listing.setVisibility(View.GONE);

                            }


                            if (jObject.getString("status").equals("200")) {

                            } else if (jObject.getString("status").equals("400")) {
                                cc.showToast(jObject.getString("message"));

                                tv_no_data_available.setVisibility(View.VISIBLE);
                                sorting_list.setVisibility(View.GONE);
                                img_gridelisting.setVisibility(View.GONE);
                                img_listing.setVisibility(View.GONE);
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
                Log.e("Error_CatDetail",error.toString());
            }
        }) {

            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                Log.i("response", response.headers.toString());
//                Map<String, String> responseHeaders = response.headers;
//                String rawCookies = responseHeaders.get("Set-Cookie");
//                Log.i("categorydetail_cookies", rawCookies);
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();

                map.put("order", cc.loadPrefString("ttd_order"));
                map.put("sort", cc.loadPrefString("ttd_name"));
//                map.put("path", finalcategoryid);


                Log.i("@@Allsortingselected", map.toString());
                return map;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("1811-token", tokens);
                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
                return headers;
            }

        };
        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    public class TopDealsListingAdapter extends RecyclerView.Adapter<TopDealsListingAdapter.ViewHolder> {

        private Context mContext;

        //        List<TopDealData> categoryListingdatas;
        ProgressBar mProgressBar;

        CommanClass cc;


        ProgressDialog pDialog;
        String res = "";
        String json_str = "";
        String status_code_signup = "";
        String status_message_signup = "";
        private BitmapDrawable bitmapDrawable1;
        private Bitmap bitmap11;

        public TopDealsListingAdapter(TodayTopDealActivity categoryDetailActivity) {
            this.mContext = categoryDetailActivity;
//            this.categoryListingdatas = categoryListingdataList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {

            cc = new CommanClass(mContext);

            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.category_listing_sorting, viewGroup, false);
            final ViewHolder viewHolder = new ViewHolder(v);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

            TopDealData cc = topDealDatas.get(i);


            if (topDealDatas.get(i).isIswishlist()) {
                viewHolder.img_wishlist.setImageResource(R.mipmap.nav_wish);
            } else {
                viewHolder.img_wishlist.setImageResource(R.mipmap.faviourite);
            }

            if (cc.getSpecial().equals("false")) {
                viewHolder.v_price_check.setVisibility(View.GONE);
                viewHolder.ll_offer_price.setVisibility(View.GONE);
            } else {
                viewHolder.ll_offer_price.setVisibility(View.VISIBLE);
                viewHolder.tv_offer_price.setText(cc.getSpecial());
            }

            viewHolder.tvSpecies.setText(cc.getTopdealname());
            viewHolder.tv_price.setText(cc.getPrice());


            viewHolder.ll_offer_price.setVisibility(View.VISIBLE);

            viewHolder.tv_offer_price.setText(topDealDatas.get(i).getSpecial());


            viewHolder.product_deatil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getPositionItem1(i);

                }
            });

            viewHolder.img_wishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (topDealDatas.get(i).isIswishlist()) {
                        topDealDatas.get(i).setIswishlist(false);
                        viewHolder.img_wishlist.setImageResource(R.mipmap.faviourite);
                    } else {
                        topDealDatas.get(i).setIswishlist(true);
                        viewHolder.img_wishlist.setImageResource(R.mipmap.nav_wish);
                    }

                    getPositionItem(i, topDealDatas.get(i).isIswishlist());

                }
            });

            viewHolder.img_addtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    addtocart(i);

                }
            });


            viewHolder.img_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TopDealData rm = topDealDatas.get(i);

                    String product_name = rm.getTopdealname();
                    String product_link = rm.getShare_url();

                    String image_url = rm.getImageUrl();

                    bitmapDrawable1 = (BitmapDrawable) viewHolder.imgThumbnail.getDrawable();// get the from imageview or use your drawable from drawable folder
                    bitmap11 = bitmapDrawable1.getBitmap();
                    String imgBitmapPath = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap11, "title", null);
                    Uri imgBitmapUri = Uri.parse(imgBitmapPath);

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("*/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, product_name + "\n" + product_link);

                    mContext.startActivity(Intent.createChooser(shareIntent, "Share Product : " + product_name));
                }
            });


            Log.e("gride_positions", cc.getTopdealname());

            if (topDealDatas.size() != 0) {
                Picasso.with(mContext).load(topDealDatas.get(i).getImageUrl()).into(viewHolder.imgThumbnail, new Callback() {
                    @Override
                    public void onSuccess() {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {


                    }
                });

            } else {
                Picasso.with(mContext).load(R.drawable.no_media).into(viewHolder.imgThumbnail);
                mProgressBar.setVisibility(View.GONE);
            }


        }

        @Override
        public int getItemCount() {
            return topDealDatas.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView imgThumbnail, img_addtocart, img_share;
            public TextView tvSpecies, tv_price, tv_offer_price;
            public LinearLayout product_deatil, ll_offer_price;
            public CardView card_view_category_listing;
            public ImageView img_wishlist;
            View v_price_check;

            public ViewHolder(View itemView) {
                super(itemView);
                v_price_check = itemView.findViewById(R.id.v_price_check);
                imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
                img_addtocart = (ImageView) itemView.findViewById(R.id.img_addtocart);
                img_wishlist = (ImageView) itemView.findViewById(R.id.img_wishlist);
                img_share = (ImageView) itemView.findViewById(R.id.img_share);
                tvSpecies = (TextView) itemView.findViewById(R.id.tv_title);
                tv_price = (TextView) itemView.findViewById(R.id.tv_price);
                mProgressBar = (ProgressBar) itemView.findViewById(R.id.pgb_image_loading);
                product_deatil = (LinearLayout) itemView.findViewById(R.id.product_deatil);

                ll_offer_price = (LinearLayout) itemView.findViewById(R.id.ll_offer_price);
                tv_offer_price = (TextView) itemView.findViewById(R.id.tv_offer_price);
            }
        }

    }


    public void getPositionItem1(int adapterPosition) {

        TopDealData nm = topDealDatas.get(adapterPosition);

        Intent intentProductDetail = new Intent(TodayTopDealActivity.this, ProductDetailActivity.class);
        intentProductDetail.putExtra("product_id", nm.getProductid());
        startActivity(intentProductDetail);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }


    public void getPositionItem(int adapterPosition, boolean removeItem) {
        TopDealData rm = topDealDatas.get(adapterPosition);

        cc.savePrefString("productid", String.valueOf(rm.getProductid()));

        if (tokens.isEmpty()) {
            Log.e("Token", "Empty");

//            if (removeItem) {
//
//                dbHelper.InsertWishlistData(rm.getShare_url(), rm.getProductid() + "", rm.getImageUrl()
//                        , rm.getTopdealname(), rm.getModel(), rm.getPrice(), rm.getSpecial(), rm.getTax());
//
//            } else {
//                dbHelper.RemoveProduct(rm.getProductid());
                topDealDatas.remove(adapterPosition);
//            }

        } else {
            cc.savePrefString("productid", String.valueOf(rm.getProductid()));

            if (removeItem) {
                addwishlist();
            } else {
                RemoveFromWishList(rm.getProductid());
            }
        }
    }


    public void RemoveFromWishList(final String product_id) {
        pDialog = new ProgressDialog(TodayTopDealActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.get_wishlist,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:removeWishlist", response);
                        try {
                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }
                            JSONObject jObject = new JSONObject(response);

                            if (jObject.getString("status").equals("200")) {
                                cc.showToast("Product removed from Wish List");
                            }

//                                cc.showToast("Product removed from Wish List");

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
                Log.e("error_wishlist", error + "");
            }

        }) {

            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                Log.i("response", response.headers.toString());
//                Map<String, String> responseHeaders = response.headers;
//                String rawCookies = responseHeaders.get("Set-Cookie");
//                Log.i("Mremovewishlist_cookies", rawCookies);
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("remove", product_id);

                Log.i("remove wishlist", map.toString());
                return map;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("1811-token", tokens);
                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }


    public void addtocart(int adapterPosition) {

        if (tokens.isEmpty()) {
            Log.e("Token", "Empty");
            cc.showToast("Please Login to add the product in cart");
        } else {
            TopDealData rm = topDealDatas.get(adapterPosition);

            cc.savePrefString("productid", String.valueOf(rm.getProductid()));

            addwishlist();

        }

    }

    private void addwishlist() {

        pDialog = new ProgressDialog(TodayTopDealActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.add_wishlist,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:addwishlist", response);
                        try {
                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }
                            JSONObject jObject = new JSONObject(response);
                            cc.showToast("Product added in to Wish List");

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
                Log.e("errror_wishlist", error + "");
            }
        }) {

            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                Log.i("response", response.headers.toString());
//                Map<String, String> responseHeaders = response.headers;
//                String rawCookies = responseHeaders.get("Set-Cookie");
//                Log.i("addwishlist_cookies", rawCookies);
                return super.parseNetworkResponse(response);

            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("product_id", cc.loadPrefString("productid"));
                Log.i("@@request wishlist", map.toString());
                return map;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("1811-token", tokens);
                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

}
