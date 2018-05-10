package com.mxi.ecommerce.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mxi.ecommerce.comman.ConnectionUrl;
import com.mxi.ecommerce.model.GetWishlist;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mxi.ecommerce.comman.ConnectionUrl.get_wishlist;

public class WishlistActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ln_home_bottom, ln_category_bottom, ln_deal_bottom, ln_wishlist_bottom, ln_mycc_bottom;

    String title = "Wishlist";
    TextView tv_toolbar_title, tv_no_data_available;

    RecyclerView recycler_wishlist;
    RecyclerView.LayoutManager mLayoutManager_wishlist;

    RecyclerView.Adapter mAdapter;


    ArrayList<GetWishlist> wishlists;

    LinearLayout img_back_btn;

    CommanClass cc;

    ProgressDialog pDialog;
    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";
    String tokens;
    //    SQLiteTD db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        cc = new CommanClass(this);
        tokens = cc.loadPrefString("1811-token");

        mLayoutManager_wishlist = new LinearLayoutManager(this);
        init();
        clickListner();
//        if (tokens.isEmpty()) {
//            Log.e("Token", "Empty");
//
//
//
//
//        } else {
//
//            if (!cc.isConnectingToInternet()) {
//
//                cc.showToast(getString(R.string.no_internet));
//
//            } else {
//                GetWishlist();
//            }
//
//            Log.e("Token", "NoEmpty");
//        }
//        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(this),
//                CookiePolicy.ACCEPT_ORIGINAL_SERVER);
//        CookieHandler.setDefault(cookieManager);


        GetWishlist();

        tv_toolbar_title.setText(title);
        tv_toolbar_title.setPadding(0, 0, 40, 0);
        Log.e("toolbar", title);


//        img_back_btn.setVisibility(View.GONE);
    }

    private void GetWishlist() {

        Log.e("language", cc.loadPrefString("language"));

        pDialog = new ProgressDialog(WishlistActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, get_wishlist,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("listingwishlist", response);
                        JSONArray jsonArray = null;

                        try {
                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }
                            wishlists = new ArrayList<GetWishlist>();
                            // Create an array to populate the spinner


                            JSONObject jObject = new JSONObject(response);


                            jsonArray = jObject.getJSONArray("products");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                GetWishlist wish = new GetWishlist();


                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                wish.setWish_image(jsonObject.getString("thumb"));
                                wish.setWish_product_name(jsonObject.getString("name"));
                                wish.setWish_stock(jsonObject.getString("stock"));
                                wish.setWish_product_price(jsonObject.getString("price"));
                                wish.setWish_special_price(jsonObject.getString("special"));
                                Log.e("sp", jsonObject.getString("special"));
                                wish.setWish_model(jsonObject.getString("model"));
                                wish.setWish_product_id(jsonObject.getString("product_id"));
//                                wish.setShareProductUrl(jsonObject.getString("shareProductUrl"));
                                wish.setShareProductUrl("");
//                                wish.setTax(jsonObject.getString("tax"));
                                wish.setTax("");


                                Log.e("name", jsonObject.getString("name"));


                                wishlists.add(wish);

                            }

                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }

                            if (wishlists.size() > 0) {
                                mAdapter = new WishListAdapter(WishlistActivity.this, wishlists);
                                recycler_wishlist.setLayoutManager(mLayoutManager_wishlist);
                                recycler_wishlist.setAdapter(mAdapter);

                                tv_no_data_available.setVisibility(View.GONE);
                            } else {
                                tv_no_data_available.setVisibility(View.VISIBLE);
                            }


                        } catch (JSONException e) {
                            Log.e("Error : Exception", e.getMessage());
                            tv_no_data_available.setVisibility(View.VISIBLE);
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(pDialog.isShowing()){
                    pDialog.dismiss();
                }
                error.printStackTrace();
                tv_no_data_available.setVisibility(View.VISIBLE);
            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> map = new HashMap<String, String>();
                //   map.put("country_id",cc.loadPrefString("country_id"));
                return map;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("1811-token", tokens);
                headers.put("Cookie", "PHPSESSID=" + cc.loadPrefString3("PHPSESSID") + ";" + "default=" + cc.loadPrefString3("default"));

                Log.i("@@request wishlist", "__"+headers.toString());

                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }


    private void clickListner() {

        img_back_btn.setOnClickListener(this);

        ln_home_bottom.setOnClickListener(this);
        ln_deal_bottom.setOnClickListener(this);
        ln_wishlist_bottom.setOnClickListener(this);
        ln_mycc_bottom.setOnClickListener(this);
        ln_category_bottom.setOnClickListener(this);

    }

    private void init() {

        tv_no_data_available = (TextView) findViewById(R.id.tv_no_data_available);
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        recycler_wishlist = (RecyclerView) findViewById(R.id.recycler_wishlist);
        img_back_btn = (LinearLayout) findViewById(R.id.img_back_btn);

        ln_home_bottom = (LinearLayout) findViewById(R.id.ln_home_bottom);
        ln_category_bottom = (LinearLayout) findViewById(R.id.ln_category_bottom);
        ln_deal_bottom = (LinearLayout) findViewById(R.id.ln_deal_bottom);
        ln_mycc_bottom = (LinearLayout) findViewById(R.id.ln_mycc_bottom);
        ln_wishlist_bottom = (LinearLayout) findViewById(R.id.ln_wishlist_bottom);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ln_home_bottom:


                Intent intentMainActivity = new Intent(WishlistActivity.this, MainActivity.class);
                intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                overridePendingTransition(0, 0);
                startActivity(intentMainActivity);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();


                break;

            case R.id.ln_category_bottom:

                Intent intentShopCategory = new Intent(WishlistActivity.this, ShopCategoryActivity.class);
                startActivity(intentShopCategory);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ln_mycc_bottom:


                if (cc.loadPrefBoolean("isLogin") == true) {


                Intent intentWishList = new Intent(WishlistActivity.this, MyAccountActivity.class);
                startActivity(intentWishList);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                } else {
                    cc.showDialogUserNotLogin();
                }


                break;

            case R.id.ln_wishlist_bottom:


                break;

            case R.id.ln_deal_bottom:

                Intent intentMyAccount = new Intent(WishlistActivity.this, TopDealDetailActivity.class);
                startActivity(intentMyAccount);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.img_back_btn:
                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

        List<GetWishlist> get_wishlists;
        Context context;
        ProgressBar mProgressBar;

        CommanClass cc;


        public WishListAdapter(WishlistActivity wishlistActivity, ArrayList<GetWishlist> wishlists) {
            this.context = wishlistActivity;
            this.get_wishlists = wishlists;

        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            cc = new CommanClass(context);

            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.wishlist_listing, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            viewHolder.tvSpecies.setText(get_wishlists.get(i).getWish_product_name());
            viewHolder.wish_model.setText(get_wishlists.get(i).getWish_model());
            viewHolder.wishlist_price.setText(get_wishlists.get(i).getWish_product_price());
            viewHolder.tv_stock.setText(get_wishlists.get(i).getWish_stock());


            if (tokens.isEmpty()) {

            } else {
                if (get_wishlists.get(i).getWish_special_price().equals("false")) {
                    viewHolder.tv_offer_price.setVisibility(View.GONE);
                    viewHolder.ll_offer_price.setVisibility(View.GONE);
                    viewHolder.v_price_check.setVisibility(View.GONE);
                } else {
                    viewHolder.v_price_check.setVisibility(View.VISIBLE);
                    viewHolder.tv_offer_price.setVisibility(View.VISIBLE);
                    viewHolder.ll_offer_price.setVisibility(View.VISIBLE);
                    viewHolder.tv_offer_price.setText(get_wishlists.get(i).getWish_special_price());
                }
            }

            if (get_wishlists.size() != 0) {
                Picasso.with(context).load(get_wishlists.get(i).getWish_image()).into(viewHolder.imgThumbnail, new Callback() {
                    @Override
                    public void onSuccess() {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

            } else {
                Picasso.with(context).load(R.drawable.no_media).into(viewHolder.imgThumbnail);
                mProgressBar.setVisibility(View.GONE);
            }

            viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    if (tokens.isEmpty()) {
//
//                        bdHelper.RemoveProduct(get_wishlists.get(i).getWish_product_id());
//
//                    } else {


//                    }


                    if (tokens.isEmpty()){
                        getPositionItem1(viewHolder.getAdapterPosition());

                        get_wishlists.remove(i);
                        mAdapter.notifyDataSetChanged();

                    }else {
                        getPositionItem1(viewHolder.getAdapterPosition());

                        get_wishlists.remove(i);
                        mAdapter.notifyDataSetChanged();
                    }


                }
            });

            viewHolder.ln_wishlist_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (tokens.isEmpty()) {
                        GetWishlist rm = get_wishlists.get(viewHolder.getAdapterPosition());
                        Log.e("prodctpositionid", rm.getWish_product_id() + "");
                        removewishlist(rm.getWish_product_id(), true);


//                        Log.e("Token", "Empty");
//                        cc.showToast("Please Login to add the product in cart");
                    } else {
                        GetWishlist rm = get_wishlists.get(viewHolder.getAdapterPosition());
                        Log.e("prodctpositionid", rm.getWish_product_id() + "");
                        removewishlist(rm.getWish_product_id(), true);
                    }

                    get_wishlists.remove(i);
                    mAdapter.notifyDataSetChanged();

//                    Intent intentAddToCart = new Intent(itemView.getContext(), AddToCartActivity.class);
//                    itemView.getContext().startActivity(intentAddToCart);
                }
            });
        }


        @Override
        public int getItemCount() {

            if (get_wishlists.size() == 0) {
                tv_no_data_available.setVisibility(View.VISIBLE);
            }else{
                tv_no_data_available.setVisibility(View.GONE);
            }

            return get_wishlists.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView imgThumbnail, img_delete;
            public TextView tvSpecies, wish_model, wishlist_price, tv_offer_price, tv_stock;
            public LinearLayout ln_wishlist_cart, ll_offer_price;
            View v_price_check;


            public ViewHolder(final View itemView) {
                super(itemView);
                v_price_check = (View) itemView.findViewById(R.id.v_price_check);
                imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
                img_delete = (ImageView) itemView.findViewById(R.id.img_delete);
                tvSpecies = (TextView) itemView.findViewById(R.id.tv_title);
                wish_model = (TextView) itemView.findViewById(R.id.tv_wish_model);
                wishlist_price = (TextView) itemView.findViewById(R.id.wishlist_price);
                tv_offer_price = (TextView) itemView.findViewById(R.id.tv_offer_price);
                tv_stock = (TextView) itemView.findViewById(R.id.tv_stock);
                ln_wishlist_cart = (LinearLayout) itemView.findViewById(R.id.ln_wishlist_cart);
                ll_offer_price = (LinearLayout) itemView.findViewById(R.id.ll_offer_price);
                mProgressBar = (ProgressBar) itemView.findViewById(R.id.pgb_image_loading);


                imgThumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.e("topdealspositions:-", String.valueOf(getPosition()));

                    }
                });
            }


        }

        private void getPositionItem1(int adapterPosition) {
            GetWishlist rm = get_wishlists.get(adapterPosition);

//            cc.savePrefString("productid", String.valueOf(rm.getWish_product_id()));
            Log.e("prodctpositionid", rm.getWish_product_id() + "");

            removewishlist(rm.getWish_product_id(), false);

        }


    }

    private void removewishlist(final String product_id, final boolean move) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.get_wishlist,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:addwishlist", response);
                        JSONArray jsonArray = null;


                        try {
                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }
                            wishlists = new ArrayList<GetWishlist>();
                            // Create an array to populate the spinner


                            JSONObject jObject = new JSONObject(response);

                            if (jObject.getString("status").equals("200")) {
                                if (move) {
                                    addtocartws(product_id);
                                }
                            }

//                            jsonArray = jObject.getJSONArray("products");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//
//                                GetWishlist wish = new GetWishlist();
//
//
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                wish.setWish_image(jsonObject.getString("thumb"));
//                                wish.setWish_product_name(jsonObject.getString("name"));
//                                wish.setWish_stock(jsonObject.getString("stock"));
//                                wish.setWish_product_price(jsonObject.getString("price"));
//                                wish.setWish_special_price(jsonObject.getString("special"));
//                                wish.setWish_model(jsonObject.getString("model"));
//                                wish.setWish_product_id(jsonObject.getString("product_id"));
//
//
//                                Log.e("name", jsonObject.getString("name"));
//
//
//                                Toast.makeText(getApplicationContext(), "Your item has been remove in wishlist", Toast.LENGTH_LONG).show();
//
//
//                                wishlists.add(wish);
//
//                            }
//
//                            if (wishlists != null) {
//                                pDialog.dismiss();
//                                recycler_wishlist.setLayoutManager(mLayoutManager_wishlist);
//                                mAdapter = new WishListAdapter(WishlistActivity.this, wishlists);
//                                recycler_wishlist.setLayoutManager(mLayoutManager_wishlist);
//                                recycler_wishlist.setAdapter(mAdapter);
//                            }
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
//                Log.i("removewishlist_cookies", rawCookies);
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put("remove", product_id);
                Log.i("@@request wishlist", map.toString());

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


    private void addtocartws(final String product_id) {


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.add_to_cart,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:addtocart", response);
                        try {
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("200")) {
                                cc.showToast("Product successfully moved to Cart");

                            }else  if (jObject.getString("status").equals("400")){

                            JSONObject jObject1 = new JSONObject(response);
                            JSONObject jObject2=jObject1.getJSONObject("error");
                            JSONObject jObject3= jObject2.getJSONObject("option");
                            String erro_message = jObject3.getString("328");

                            Log.e("error_message",erro_message);
                            cc.showToast(erro_message);
                            Intent intentProductDetail = new Intent(WishlistActivity.this, ProductDetailActivity.class);
                            intentProductDetail.putExtra("product_id", cc.loadPrefString("productid"));
                            startActivity(intentProductDetail);
                        }

                    } catch (JSONException e) {
                            Log.e("Error : Exception", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("errror_wishlist", error + "");
            }

        }) {

            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                Log.i("response", response.headers.toString());
//                Map<String, String> responseHeaders = response.headers;
//                String rawCookies = responseHeaders.get("Set-Cookie");
//                Log.i("addtocart_cookies", rawCookies);
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("product_id", product_id);
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
