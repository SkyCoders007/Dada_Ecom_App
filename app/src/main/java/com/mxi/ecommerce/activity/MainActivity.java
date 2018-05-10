package com.mxi.ecommerce.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mxi.ecommerce.R;
import com.mxi.ecommerce.adapter.BrowseCategoryAdapter;
import com.mxi.ecommerce.adapter.GetDiscountAdapter;
import com.mxi.ecommerce.adapter.NewPopularProductAdapter;
import com.mxi.ecommerce.adapter.SlideImageAdapter;
import com.mxi.ecommerce.adapter.TopDealsAdapter;
import com.mxi.ecommerce.comman.AppController;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.comman.ConnectionUrl;
import com.mxi.ecommerce.comman.PersistentCookieStore;
import com.mxi.ecommerce.model.CategoryDataTwo;
import com.mxi.ecommerce.model.GetDiscountData;
import com.mxi.ecommerce.model.NewPopularCatelogData;
import com.mxi.ecommerce.model.RecommendedData;
import com.mxi.ecommerce.model.SlidingData;
import com.mxi.ecommerce.model.TopDealData;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //List of data

    List<SlidingData> SlidingModelList;
    List<TopDealData> TopDealModelList;
    List<NewPopularCatelogData> NewPopularCatelogList;
    List<CategoryDataTwo> CategoryList;
    List<RecommendedData> RecommendedList;
    List<GetDiscountData> GetDiscountList;


    //Navigation menu item
    LinearLayout ln_return_product,ln_myccount, ln_language, ln_shopbycategory, ln_wishlist, ln_home, ln_deals, ln_orders, ln_blogs, ln_dealing, ln_logout, ln_login;

    TextView tv_username;


    //LinearLayout bottom button menu

    LinearLayout ln_home_bottom, ln_category_bottom, ln_deal_bottom, ln_wishlist_bottom, ln_mycc_bottom, ln_bg;


    //recycleview topdeal scrollview
    RecyclerView recycler_scrollview_1;
    RecyclerView.LayoutManager mLayoutManager_scrollview_1;

    // recycleview Browse by category 1
    RecyclerView recyclerview_browsebt_category1;
    RecyclerView.LayoutManager mLayoutManager_browseby_category;

    //  recycleview  Browse by category 2
    RecyclerView recyclerview_browsebt_category2;
    RecyclerView.LayoutManager mLayoutManager_browseby_category1;

    //  recycleview  recommended_product
    RecyclerView recycleview_recommended_product;
    RecyclerView.LayoutManager mLayoutManager_recommended_product;

    //  recycleview Newpopularproduct
    RecyclerView recyclerview_newpopularproduct;
    RecyclerView.LayoutManager mLayoutManager_recyclerview_newpopularproduct;

    //  recycleview Bottom topdeal scrollview
    RecyclerView recycler_view_category_2;
    RecyclerView.LayoutManager mLayoutManager_recycler_view_category_2;

    //  recycleview Get 60% off listing
    RecyclerView recycler_get_discount;
    RecyclerView.LayoutManager mLayoutManager_recycler_get_discount;


    RecyclerView.Adapter mAdapter;
    ArrayList<String> alName;
    ArrayList<String> alproductname;
    ArrayList<Integer> alImage;
    ArrayList<Integer> alImage1;
    ArrayList<String> alName1;
    ArrayList<Integer> alcategoryImage;


    private SlidingRootNav slidingRootNav;

    ViewPager viewPager;
    PagerAdapter adapter;
    int[] flag;

    //sliding button left and right side
    ImageView leftNav, cart_main, iv_search;
    ImageView rightNav;
    CommanClass cc;

    ProgressDialog pDialog;
    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";
    String android_id;
    String username;

    //Signout
    public FirebaseAuth firebaseAuth;
    public GoogleApiClient googleApiClient;
    private FirebaseAuth mAuth;
    String tokens;
    TextView tv_seeall, tv_cart_badge;
    //    SQLiteTD sqLiteHelper;


    boolean isAddToCartInProgress = false;
    boolean isAddToWishListInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
        cc = new CommanClass(this);

        tokens = cc.loadPrefString("1811-token");

//        sqLiteHelper = new SQLiteTD(this);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));

        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(authConfig)
                .build();

        Twitter.initialize(twitterConfig);


        android_id = Settings.Secure.getString(MainActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        username = cc.loadPrefString("firstname") + cc.loadPrefString("lastname");

        firebaseAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Log.e("deviceid", android_id);
        Log.e("username", username);

        Toolbar naviagation_ic = (Toolbar) findViewById(R.id.naviagation_ic);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(naviagation_ic)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(true)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_drawer)
                .inject();

        alImage = new ArrayList<>(Arrays.asList(R.mipmap.eyewear, R.mipmap.jwelery, R.mipmap.footwear, R.mipmap.watch, R.mipmap.mobiles));
        alcategoryImage = new ArrayList<>(Arrays.asList(R.mipmap.headphone, R.mipmap.powerbank, R.mipmap.footwear, R.mipmap.watch, R.mipmap.mobiles));
        alName = new ArrayList<>(Arrays.asList("Eyewear", "Jwellery ", "footwear", "Watch", "Mobile"));


        alImage1 = new ArrayList<>(Arrays.asList(R.mipmap.mobile_one, R.mipmap.watch, R.mipmap.footwear, R.mipmap.jwelery, R.mipmap.eyewear));
        alName1 = new ArrayList<>(Arrays.asList("Mobile", "Watch ", "footwear", "Jwellery", "Eyewear"));


        alproductname = new ArrayList<>(Arrays.asList("HeadPhone black color", "Powerbank", "footwear stylish", "Rolex Watch", "Lg s5 (Black,16GB)"));
        flag = new int[]{R.mipmap.viewpager_one, R.mipmap.viewpager_two,
                R.mipmap.viewpager_three, R.mipmap.viewpager_four,
                R.mipmap.viewpager_five};




        Log.e("mainscreen_token", "__" + tokens);

        init();
        clickListner();

//        makeJsonCallCartCounter();
        //Sliding Image adapters
//        adapter = new SlideImageAdapter(MainActivity.this, flag);
//        viewPager.setAdapter(adapter);


        //Top deals category recycle view adapters

        mLayoutManager_scrollview_1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        //Browse by category screen1 recycle view adapters

        mLayoutManager_browseby_category = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        //Recommented Product category recycle view adapters
        mLayoutManager_recommended_product = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        //Recommented New Popular Product recycle view adapters
        mLayoutManager_recyclerview_newpopularproduct = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        //Top deals category recycle view adapters
        mLayoutManager_recycler_get_discount = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        //Signout gmail
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .enableAutoManage(MainActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    private void clickListner() {

        leftNav.setOnClickListener(this);
        rightNav.setOnClickListener(this);
        ln_myccount.setOnClickListener(this);
        ln_language.setOnClickListener(this);
        ln_shopbycategory.setOnClickListener(this);
        ln_wishlist.setOnClickListener(this);
        ln_home.setOnClickListener(this);
        ln_deals.setOnClickListener(this);
        ln_orders.setOnClickListener(this);
        ln_blogs.setOnClickListener(this);
        ln_dealing.setOnClickListener(this);
        ln_logout.setOnClickListener(this);
        ln_login.setOnClickListener(this);

        ln_home_bottom.setOnClickListener(this);
        ln_deal_bottom.setOnClickListener(this);
        ln_wishlist_bottom.setOnClickListener(this);
        ln_mycc_bottom.setOnClickListener(this);
        ln_category_bottom.setOnClickListener(this);
        cart_main.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        tv_seeall.setOnClickListener(this);
        ln_return_product.setOnClickListener(this);

    }

    private void init() {

        ln_home_bottom = (LinearLayout) findViewById(R.id.ln_home_bottom);
        ln_category_bottom = (LinearLayout) findViewById(R.id.ln_category_bottom);
        ln_deal_bottom = (LinearLayout) findViewById(R.id.ln_deal_bottom);
        ln_mycc_bottom = (LinearLayout) findViewById(R.id.ln_mycc_bottom);
        ln_wishlist_bottom = (LinearLayout) findViewById(R.id.ln_wishlist_bottom);


        //Navigation items
        ln_logout = (LinearLayout) findViewById(R.id.ln_logout);
        ln_login = (LinearLayout) findViewById(R.id.ln_login);
        ln_myccount = (LinearLayout) findViewById(R.id.ln_myccount);
        ln_return_product = (LinearLayout) findViewById(R.id.ln_return_product);
        ln_language = (LinearLayout) findViewById(R.id.ln_language);
        ln_shopbycategory = (LinearLayout) findViewById(R.id.ln_shopbycategory);
        ln_wishlist = (LinearLayout) findViewById(R.id.ln_wishlist);
        ln_home = (LinearLayout) findViewById(R.id.ln_home);
        ln_deals = (LinearLayout) findViewById(R.id.ln_deals);
        ln_orders = (LinearLayout) findViewById(R.id.ln_orders);
        ln_blogs = (LinearLayout) findViewById(R.id.ln_blogs);
        ln_dealing = (LinearLayout) findViewById(R.id.ln_dealing);
        cart_main = (ImageView) findViewById(R.id.cart_main);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        tv_username = (TextView) findViewById(R.id.tv_username);
        ln_bg = (LinearLayout) findViewById(R.id.ln_bg);
        tv_username.setText(username);

        viewPager = (ViewPager) findViewById(R.id.pager);
        leftNav = (ImageView) findViewById(R.id.left_nav);
        rightNav = (ImageView) findViewById(R.id.right_nav);
        recycler_scrollview_1 = (RecyclerView) findViewById(R.id.recycler_scrollview_1);
        recyclerview_browsebt_category1 = (RecyclerView) findViewById(R.id.recyclerview_browsebt_category1);
        recyclerview_browsebt_category2 = (RecyclerView) findViewById(R.id.recyclerview_browsebt_category2);
        recycleview_recommended_product = (RecyclerView) findViewById(R.id.recycleview_recommended_product);
        recyclerview_newpopularproduct = (RecyclerView) findViewById(R.id.recyclerview_newpopularproduct);
        recycler_get_discount = (RecyclerView) findViewById(R.id.recycle_getdiscount);

        /*See All*/

        tv_seeall = (TextView) findViewById(R.id.tv_seeall);
//        View layout1=(View) findViewById(R.id.layout1);
        tv_cart_badge = (TextView) findViewById(R.id.tv_cart_badge);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_search:

                Intent tv_search = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(tv_search);

                break;
            case R.id.tv_seeall:

                Intent tv_seeall = new Intent(MainActivity.this, ShopCategoryActivity.class);
                startActivity(tv_seeall);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;


            case R.id.left_nav:
                int tab = viewPager.getCurrentItem();
                if (tab > 0) {
                    tab--;
                    viewPager.setCurrentItem(tab);
                } else if (tab == 0) {
                    viewPager.setCurrentItem(tab);
                }

                break;

            case R.id.right_nav:

                int tab1 = viewPager.getCurrentItem();
                tab1++;
                viewPager.setCurrentItem(tab1);

                break;


            case R.id.ln_home:

                Intent intentMainActivity = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intentMainActivity);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ln_deals:


                Intent intentDeal = new Intent(MainActivity.this, TodayTopDealActivity.class);
                startActivity(intentDeal);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);


                break;

            case R.id.ln_orders:

                Intent intentMyOrder = new Intent(MainActivity.this, MyOrderActivity.class);
                startActivity(intentMyOrder);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);


                break;

            case R.id.ln_blogs:

//                Toast.makeText(getApplicationContext(), "Blogs : Comming soon", Toast.LENGTH_SHORT).show();

//                cc.showToast("Blogs : Comming soon");

//                Intent ReturnProductActivity = new Intent(MainActivity.this, ReturnProductActivity.class);
//                startActivity(ReturnProductActivity);
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);


                Log.e("hello","hello");
                break;


            case R.id.ln_return_product:

//                Toast.makeText(getApplicationContext(), "Blogs : Comming soon", Toast.LENGTH_SHORT).show();
                Intent ReturnProductActivity = new Intent(MainActivity.this, ReturnProductActivity.class);
                startActivity(ReturnProductActivity);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ln_dealing:


                Intent intentDeal2 = new Intent(MainActivity.this, TopDealDetailActivity.class);
                startActivity(intentDeal2);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);


                break;


            case R.id.ln_myccount:

                if (cc.loadPrefBoolean("isLogin") == true) {

                    Intent intentMyAccount = new Intent(MainActivity.this, MyAccountActivity.class);
                    startActivity(intentMyAccount);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                } else {
//                    Toast.makeText(this,"Plz Login...",Toast.LENGTH_SHORT).show();
                    cc.showDialogUserNotLogin();
                }

                break;

            case R.id.ln_language:

                Intent intentLanguage = new Intent(MainActivity.this, LanguageActivity.class);
                startActivity(intentLanguage);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ln_shopbycategory:

                Intent intentShopCategory = new Intent(MainActivity.this, ShopCategoryActivity.class);
                startActivity(intentShopCategory);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;


            case R.id.ln_wishlist:

                Intent intentWishList = new Intent(MainActivity.this, WishlistActivity.class);
                startActivity(intentWishList);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ln_logout:

                if (!cc.isConnectingToInternet()) {
                    cc.showToast(getString(R.string.no_internet));

                } else {

                    Logout();

                }

                break;

            case R.id.ln_home_bottom:


                break;

            case R.id.ln_category_bottom:

                Intent intentShopCategory2 = new Intent(MainActivity.this, ShopCategoryActivity.class);
                startActivity(intentShopCategory2);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ln_mycc_bottom:

                if (cc.loadPrefBoolean("isLogin") == true) {

                    Intent intentMyAccount2 = new Intent(MainActivity.this, MyAccountActivity.class);
                    startActivity(intentMyAccount2);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                } else {
                    cc.showDialogUserNotLogin();
                }


                break;

            case R.id.ln_wishlist_bottom:
//
//                if (cc.loadPrefBoolean("isLogin") == true) {
//
//
//                    Intent intentWishList2 = new Intent(MainActivity.this, WishlistActivity.class);
//                    startActivity(intentWishList2);
//                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//
//                } else {
//                    cc.showDialogUserNotLogin();
//                }


                Intent intentWishList2 = new Intent(MainActivity.this, WishlistActivity.class);
                startActivity(intentWishList2);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);



                break;

            case R.id.ln_deal_bottom:


                Intent intentDeal3 = new Intent(MainActivity.this, TopDealDetailActivity.class);
                startActivity(intentDeal3);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.cart_main:

//                if (tokens.isEmpty()) {
//                    cc.showDialogUserNotLogin();
//                } else {
//                    Intent intentAddToCart = new Intent(MainActivity.this, AddToCartActivity.class);
//                    startActivity(intentAddToCart);
//                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//                }

                Intent intentAddToCart = new Intent(MainActivity.this, AddToCartActivity.class);
                startActivity(intentAddToCart);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);


                break;

            case R.id.ln_login:

                Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;

        }

    }

    // Logout webservice
    private void Logout() {

        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.logout,
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

                                cc.showToast(jObject.getString("message"));
                                firebaseAuth.signOut();
                                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback() {
                                    @Override
                                    public void onResult(@NonNull Result result) {
                                        Log.e("GoogleSignInApi",result.toString());
                                    }
                                });

                                LoginManager.getInstance().logOut();
                                mAuth.signOut();
                                cc.logoutapp();
                                TwitterCore.getInstance().getSessionManager().clearActiveSession();

//                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                startActivity(new Intent(MainActivity.this, PopLanguageScreen.class));
                                overridePendingTransition(0, 0);
                                finish();

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
                Log.e("Logout",error.toString());
            }
        }) {

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
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    /// Home detail web service and logout button visible/invisible
    @Override
    protected void onResume() {

        if (!cc.isConnectingToInternet()) {
            cc.showToast(getString(R.string.no_internet));

        } else {

            HomeListing();

            if(cc.loadPrefString3("PHPSESSID").isEmpty() && cc.loadPrefString3("default").isEmpty()){
                CookieManager cookieManager = new CookieManager(new PersistentCookieStore(this),
                        CookiePolicy.ACCEPT_ORIGINAL_SERVER);
                CookieHandler.setDefault(cookieManager);
            }
            makeJsonCallCartCounter();
        }

        if (cc.loadPrefBoolean("isLogin") == true) {

            ln_logout.setVisibility(View.VISIBLE);
            ln_login.setVisibility(View.GONE);
            ln_bg.setVisibility(View.VISIBLE);

        } else {
            ln_logout.setVisibility(View.GONE);
            ln_login.setVisibility(View.VISIBLE);
            ln_bg.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    private void makeJsonCallCartCounter() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.CartCounter,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:cart_counter", response);
                        try {
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("200")) {

                                String badge = jObject.getString("totalProduct");

                                Log.e("badge",badge);

                                if (badge.equals("0")) {
                                    tv_cart_badge.setText("");
                                } else {
                                    tv_cart_badge.setText(badge);
                                }
                            }
//                            cc.showToast("Product added in to Cart");
                        } catch (JSONException e) {
                            Log.e("Error cart_counter:", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error_cart_counter", error + "");
            }
        }) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

//                String resHead=response.headers.get("1811-token");
//                Log.e("@@resHead",resHead+"");
//                cc.savePrefString("1811-token",resHead);

                Log.i("response",response.headers.toString());
//                Map<String, String> responseHeaders = response.headers;
//                String rawCookies = responseHeaders.get("Set-Cookie");
//                Log.i("cartcounter_cookies",rawCookies);
                return super.parseNetworkResponse(response);

            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("1811-token", tokens);
                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
                Log.e("@@Header_cart_counter",headers.toString());
                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    // Home Screen webservice
    private void HomeListing() {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.homelisting,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:login", response);
                        JSONArray jsonArray = null;
                        try {
                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }

                            String ims = "";

                            JSONObject jObject = new JSONObject(response);

//
//                            String badge = jObject.getString("totalProductIncart");
//
//                            if (badge.equals("0")) {
//                                tv_cart_badge.setText("");
//                            } else {
//                                tv_cart_badge.setText(badge);
//                            }


                            if (jObject.getString("status").equals("200")) {
                                SlidingModelList = new ArrayList<>();
                                TopDealModelList = new ArrayList<>();
                                NewPopularCatelogList = new ArrayList<>();
                                CategoryList = new ArrayList<>();
                                RecommendedList = new ArrayList<>();
                                GetDiscountList = new ArrayList<>();




                                // Sliding Viewpagers
                                jsonArray = jObject.getJSONArray("sliderData");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    SlidingData stylesInfo = new SlidingData();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String title = jsonObject.getString("title");
                                    stylesInfo.setImageUrl(jsonObject.getString("image"));
                                    SlidingModelList.add(stylesInfo);
                                    Log.e("title", title);


                                }
                                if (SlidingModelList != null) {

                                    adapter = new SlideImageAdapter(MainActivity.this, SlidingModelList);
                                    viewPager.setAdapter(adapter);

                                }
                                //Top Deals
                                jsonArray = jObject.getJSONArray("topdeals");
                                for (int i = 0; i < jsonArray.length(); i++) {


                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    TopDealData topdeals = new TopDealData();

                                    topdeals.setImageUrl(jsonObject.getString("thumb"));
                                    topdeals.setTopdealname(jsonObject.getString("name"));
                                    topdeals.setProductid(jsonObject.getString("product_id"));
                                    TopDealModelList.add(topdeals);

                                    Log.e("@@todeals: thumb", jsonObject.getString("thumb"));
                                    Log.e("@@todeals: name", jsonObject.getString("name"));
                                    Log.e("@@todeals: product_id", jsonObject.getString("product_id"));

                                }
                                if (TopDealModelList != null) {
                                    pDialog.dismiss();

                                    recycler_scrollview_1.setLayoutManager(mLayoutManager_scrollview_1);
                                    mAdapter = new TopDealsAdapter(MainActivity.this, TopDealModelList);
                                    recycler_scrollview_1.setLayoutManager(mLayoutManager_scrollview_1);
                                    recycler_scrollview_1.setAdapter(mAdapter);

                                }
                                //New Popular Catelog
                                jsonArray = jObject.getJSONArray("newpopular");
                                for (int i = 0; i < jsonArray.length(); i++) {


                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    NewPopularCatelogData popularlist = new NewPopularCatelogData();

                                    popularlist.setImageUrl(jsonObject.getString("thumb"));
                                    popularlist.setProductid(jsonObject.getInt("product_id"));

                                    NewPopularCatelogList.add(popularlist);

                                    Log.e("@@Popular: thumb", jsonObject.getString("thumb"));

                                }
                                if (TopDealModelList != null) {
                                    pDialog.dismiss();

                                    recyclerview_newpopularproduct.setLayoutManager(mLayoutManager_recyclerview_newpopularproduct);
                                    mAdapter = new NewPopularProductAdapter(MainActivity.this, NewPopularCatelogList);
                                    recyclerview_newpopularproduct.setLayoutManager(mLayoutManager_recyclerview_newpopularproduct);
                                    recyclerview_newpopularproduct.setAdapter(mAdapter);

                                }
                                //BROWSE BY CATEGORY
                                jsonArray = jObject.getJSONArray("categories");
                                for (int i = 0; i < jsonArray.length(); i++) {


                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    CategoryDataTwo categorylists = new CategoryDataTwo();

                                    categorylists.setImageUrl(jsonObject.getString("image"));
                                    categorylists.setCategoryname(jsonObject.getString("name"));
                                    categorylists.setCategoey_id(jsonObject.getString("category_id"));

                                    CategoryList.add(categorylists);

                                    Log.e("@@categories:image", jsonObject.getString("image"));
                                    Log.e("@@categories:name", jsonObject.getString("name"));
                                    Log.e("@@categories:categoryid",jsonObject.getString("category_id"));

                                }
                                if (CategoryList != null) {
                                    pDialog.dismiss();

                                    recyclerview_browsebt_category1.setLayoutManager(mLayoutManager_browseby_category);
                                    mAdapter = new BrowseCategoryAdapter(MainActivity.this,CategoryList);
                                    recyclerview_browsebt_category1.setLayoutManager(mLayoutManager_browseby_category);
                                    recyclerview_browsebt_category1.setAdapter(mAdapter);

                                }

                                // Recomded product
                                jsonArray = jObject.getJSONArray("recommended");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    RecommendedData recommendedlisting = new RecommendedData();

                                    recommendedlisting.setImageUrl(jsonObject.getString("thumb"));
                                    recommendedlisting.setShareurl(jsonObject.getString("shareProductUrl"));
                                    recommendedlisting.setModel(jsonObject.getString("model"));
                                    recommendedlisting.setPrice(jsonObject.getString("price"));
                                    recommendedlisting.setProductid(jsonObject.getInt("product_id"));
                                    recommendedlisting.setRecommendedName(jsonObject.getString("name"));
                                    recommendedlisting.setTax(jsonObject.getString("tax"));

                                    recommendedlisting.setSpecial(jsonObject.getString("special"));

                                    if (recommendedlisting.getSpecial().equals("false")) {
                                        Log.e("@@Special", "False");
                                    }

//                                    if (tokens.isEmpty()) {
//                                        if (dbHelper.ProductExist(recommendedlisting.getProductid() + "")) {
//                                            recommendedlisting.setIswishlist(true);
//                                        } else {
//                                            recommendedlisting.setIswishlist(false);
//                                        }
//                                    } else {
                                        recommendedlisting.setIswishlist(jsonObject.getBoolean("iswishlist"));
//                                    }

                                    recommendedlisting.setInCart(jsonObject.getBoolean("isInCart"));

                                    RecommendedList.add(recommendedlisting);

                                }

                                if (RecommendedList != null) {
                                    pDialog.dismiss();

//                                    recycleview_recommended_product.setLayoutManager(mLayoutManager_recommended_product);
                                    mAdapter = new RecomandedProductAdapter(MainActivity.this, RecommendedList);
                                    recycleview_recommended_product.setLayoutManager(mLayoutManager_recommended_product);
                                    recycleview_recommended_product.setAdapter(mAdapter);

                                }

                                //Get Discount
                                jsonArray = jObject.getJSONArray("get60%discount");
                                for (int i = 0; i < jsonArray.length(); i++) {


                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    GetDiscountData getdiscountlisting = new GetDiscountData();

                                    getdiscountlisting.setImageUrl(jsonObject.getString("thumb"));
                                    getdiscountlisting.setGetDiscountProductname(jsonObject.getString("model"));
                                    getdiscountlisting.setProductid(jsonObject.getInt("product_id"));

                                    GetDiscountList.add(getdiscountlisting);

                                    Log.e("@@Recommended: image", jsonObject.getString("thumb"));
                                    Log.e("@@Recommended: model", jsonObject.getString("model"));

                                }
                                if (GetDiscountList != null) {

                                    pDialog.dismiss();


                                    recycler_get_discount.setLayoutManager(mLayoutManager_recycler_get_discount);
                                    mAdapter = new GetDiscountAdapter(MainActivity.this, GetDiscountList);
                                    recycler_get_discount.setLayoutManager(mLayoutManager_recycler_get_discount);
                                    recycler_get_discount.setAdapter(mAdapter);


                                }


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
                Log.e("HomeListing",error.toString());
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                Log.i("response",response.headers.toString());
//                Map<String, String> responseHeaders = response.headers;
//                String rawCookies = responseHeaders.get("Set-Cookie");
//                Log.i("mainactivity_cookies",rawCookies);
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
                return headers;
            }

        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public class RecomandedProductAdapter extends RecyclerView.Adapter<RecomandedProductAdapter.ViewHolder> {


        ArrayList<Integer> alcategoryImage;
        ArrayList<String> alName;
        ArrayList<String> alproductname;
        Context context;
        List<RecommendedData> recommendedModels;
        ProgressBar mProgressBar;
        CommanClass cc;

        ProgressDialog pDialog;
        String res = "";
        String json_str = "";
        String status_code_signup = "";
        String status_message_signup = "";
        private BitmapDrawable bitmapDrawable;
        private Bitmap bitmap1;
        Boolean press = false;
        private LayoutInflater inflater;

        // private ArrayList<Boolean> bool = new ArrayList<Boolean>();
        public RecomandedProductAdapter(MainActivity mainActivity, List<RecommendedData> recommendedList) {

            this.context = mainActivity;
            this.recommendedModels = recommendedList;
            cc = new CommanClass(context);
            inflater = LayoutInflater.from(context);

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {

            View v = inflater.inflate(R.layout.recommendedproduct, viewGroup, false);
//            View v = LayoutInflater.from(context)
//                    .inflate(R.layout.recommendedproduct, viewGroup, false);
            final ViewHolder viewHolder = new ViewHolder(v);
//


            viewHolder.imgThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getPositionItem1(viewHolder.getAdapterPosition());

                    Intent intentProductDetail = new Intent(context, ProductDetailActivity.class);
                    intentProductDetail.putExtra("product_id", cc.loadPrefString("productid"));

                    String product_id = cc.loadPrefString("productid");

                    context.startActivity(intentProductDetail);

                    ((Activity) context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                    Log.e("productidholder", viewHolder.getAdapterPosition() + "");
                    Log.e("productidholder1", product_id);

                }
            });

            viewHolder.iv_recoomented_wishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!isAddToWishListInProgress) {
                        isAddToWishListInProgress = true;

                        if (recommendedModels.get(viewHolder.getAdapterPosition()).isIswishlist()) {
                            recommendedModels.get(viewHolder.getAdapterPosition()).setIswishlist(false);
                            viewHolder.iv_recoomented_wishlist.setImageResource(R.mipmap.faviourite);

                            Log.e("WishList","False");

                        } else {
                            recommendedModels.get(viewHolder.getAdapterPosition()).setIswishlist(true);
                            viewHolder.iv_recoomented_wishlist.setImageResource(R.mipmap.nav_wish);

                            Log.e("WishList","True");
                        }
                        getPositionItem(viewHolder.getAdapterPosition(), recommendedModels.get(viewHolder.getAdapterPosition()).isIswishlist());
                    }


                }
            });

            viewHolder.iv_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RecommendedData rm = recommendedModels.get(i);

                    String product_name = rm.getRecommendedName();
                    String product_link = rm.getShareurl();

                    String image_url = rm.getImageUrl();

                    bitmapDrawable = (BitmapDrawable) viewHolder.imgThumbnail.getDrawable();// get the from imageview or use your drawable from drawable folder
                    bitmap1 = bitmapDrawable.getBitmap();
                    String imgBitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap1, "title", null);
                    Uri imgBitmapUri = Uri.parse(imgBitmapPath);
                    String shareText = "Share image and text";
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("*/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, product_name + "\n" + product_link);

                    startActivity(Intent.createChooser(shareIntent, "Share Wallpaper using"));
                }

            });


            viewHolder.iv_recoomented_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.e("CArt","yes");
                    addtocart(viewHolder.getAdapterPosition());

                }
            });

            return viewHolder;

        }

        public void addwishlist() {

            pDialog = new ProgressDialog(context);
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
                                isAddToWishListInProgress = false;
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
                    Log.i("response",response.headers.toString());
//                    Map<String, String> responseHeaders = response.headers;
//                    String rawCookies = responseHeaders.get("Set-Cookie");
//                    Log.i("addtowishlist_cookies",rawCookies);
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

        @Override
        public void onBindViewHolder(final ViewHolder vHolder, int i) {

            if (recommendedModels.get(i).isIswishlist()) {
                vHolder.iv_recoomented_wishlist.setImageResource(R.mipmap.nav_wish);
            } else {
                vHolder.iv_recoomented_wishlist.setImageResource(R.mipmap.faviourite);
            }

            if (recommendedModels.get(i).getSpecial().equalsIgnoreCase("false")) {
                vHolder.v_price_check.setVisibility(View.GONE);
                vHolder.ll_offer_price.setVisibility(View.GONE);
            } else {
                vHolder.v_price_check.setVisibility(View.VISIBLE);
                vHolder.ll_offer_price.setVisibility(View.VISIBLE);
                vHolder.tv_offer_price.setText(recommendedModels.get(i).getSpecial());
            }

            vHolder.tvSpecies.setText(recommendedModels.get(i).getRecommendedName());
            vHolder.tv_price.setText(recommendedModels.get(i).getPrice());

            if (recommendedModels.size() != 0) {
                Picasso.with(context).load(recommendedModels.get(i).getImageUrl()).into(vHolder.imgThumbnail, new Callback() {
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
                Picasso.with(context).load(R.drawable.no_media).into(vHolder.imgThumbnail);
                mProgressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return recommendedModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView imgThumbnail, iv_recoomented_cart, iv_recoomented_wishlist, iv_share;
            public TextView tvSpecies, tv_price, tv_offer_price;
            public LinearLayout ln_view_category_mainscreen, ll_offer_price;
            View v_price_check;

            public ViewHolder(final View itemView) {
                super(itemView);

                ll_offer_price = (LinearLayout) itemView.findViewById(R.id.ll_offer_price);
                v_price_check = (View) itemView.findViewById(R.id.v_price_check);
                tv_offer_price = (TextView) itemView.findViewById(R.id.tv_offer_price);

                imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
                iv_recoomented_cart = (ImageView) itemView.findViewById(R.id.iv_recoomented_cart);
                iv_share = (ImageView) itemView.findViewById(R.id.iv_share);
                iv_recoomented_wishlist = (ImageView) itemView.findViewById(R.id.iv_recoomented_wishlist);

                tvSpecies = (TextView) itemView.findViewById(R.id.tvSpecies);
                tv_price = (TextView) itemView.findViewById(R.id.tv_price);
                ln_view_category_mainscreen = (LinearLayout) itemView.findViewById(R.id.ln_view_category_mainscreen);
                mProgressBar = (ProgressBar) itemView.findViewById(R.id.pgb_image_loading);

            }
        }

        private void getPositionItem1(int adapterPosition) {
            RecommendedData rm = recommendedModels.get(adapterPosition);

            cc.savePrefString("productid", String.valueOf(rm.getProductid()));

            Log.e("prodctpositionid", rm.getProductid() + "");

        }

        private void getPositionItem(int adapterPosition, boolean removeItem) {


            RecommendedData rm = recommendedModels.get(adapterPosition);
            cc.savePrefString("productid", String.valueOf(rm.getProductid()));


            if (tokens.isEmpty()) {

                Log.e("Token", "Empty");
//
////                if (removeItem) {
////                    dbHelper.InsertWishlistData(rm.getShareurl(), rm.getProductid() + "", rm.getImageUrl()
////                            , rm.getRecommendedName(), rm.getModel(), rm.getPrice(), rm.getSpecial(), rm.getTax());
////                } else {
////                    dbHelper.RemoveProduct(rm.getProductid() + "");
//                    recommendedModels.remove(adapterPosition);
//                    mAdapter.notifyDataSetChanged();
////                }
//
                isAddToWishListInProgress = false;

                cc.savePrefString("productid", String.valueOf(rm.getProductid()));

                if (removeItem) {
                    addwishlist();
                } else {
                    RemoveFromWishList(rm.getProductid() + "");
                }



            } else {
                cc.savePrefString("productid", String.valueOf(rm.getProductid()));

                if (removeItem) {
                    addwishlist();
                } else {
                    RemoveFromWishList(rm.getProductid() + "");
                }
            }

        }

        private void RemoveFromWishList(final String product_id) {
            pDialog = new ProgressDialog(MainActivity.this);
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
                                    isAddToWishListInProgress = false;
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
                    Log.e("error_wishlist", error + "");
                }

            }) {

                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    // since we don't know which of the two underlying network vehicles
                    // will Volley use, we have to handle and store session cookies manually
                    Log.i("response",response.headers.toString());
//                    Map<String, String> responseHeaders = response.headers;
//                    String rawCookies = responseHeaders.get("Set-Cookie");
//                    Log.i("main_RemoveFromWishList",rawCookies);
                    return super.parseNetworkResponse(response);
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("remove", product_id);

                    Log.i("@@request remove ", map.toString());
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

            RecommendedData rm = recommendedModels.get(adapterPosition);
            cc.savePrefString("productid", String.valueOf(rm.getProductid()));

            if (tokens.isEmpty()) {
                Log.e("Token", "Empty");

                addtocartws();
//                isAddToCartInProgress = false;
//                cc.showToast("Please Login to add the product in cart");

            } else {
                addtocartws();
            }

            Log.e("Token", "Not Empty");

        }

        /*Add to cart Webservice*/
        public void addtocartws() {

            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();


            StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.add_to_cart,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.e("response:addtocart", response);
                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }
                            try {
                                JSONObject jObject = new JSONObject(response);

                                if (jObject.getString("status").equals("200")) {
                                    isAddToCartInProgress = false;
                                    makeJsonCallCartCounter();
                                    cc.showToast("Product added in to Cart");
                                }else  if (jObject.getString("status").equals("400")){

                                    JSONObject jObject1 = new JSONObject(response);
                                    JSONObject jObject2=jObject1.getJSONObject("error");
                                    JSONObject jObject3= jObject2.getJSONObject("option");
                                    String erro_message = jObject3.getString("328");

                                    Log.e("error_message",erro_message);
                                    Log.e("responsedata",response);

                                    String message = erro_message;
                                    cc.showToast(message);
                                    Log.e("message",message);

                                    if (message.equalsIgnoreCase("Color required!")){

                                        Intent intentProductDetail = new Intent(context, ProductDetailActivity.class);
                                        intentProductDetail.putExtra("product_id", cc.loadPrefString("productid"));
                                        startActivity(intentProductDetail);
                                    }
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
                    Log.e("errror_wishlist", error + "");
                }

            }) {
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    // since we don't know which of the two underlying network vehicles
                    // will Volley use, we have to handle and store session cookies manually
                    Log.i("response",response.headers.toString());
//                    Map<String, String> responseHeaders = response.headers;
//                    String rawCookies = responseHeaders.get("Set-Cookie");
//                    Log.i("addtocart_cookies",rawCookies);
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
}
