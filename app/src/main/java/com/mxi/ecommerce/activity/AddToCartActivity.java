package com.mxi.ecommerce.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.mxi.ecommerce.model.CartDetailData;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddToCartActivity extends AppCompatActivity implements View.OnClickListener {


    boolean isInstock = true;
    TextView tv_toolbar_title, tv_price, tv_sub_total, tv_cart_total_price;

    Button btn_addcart_continue;

    LinearLayout ln_addresses;
    LinearLayout img_back_btn;

    //Recycleview Topdeal Scrollview
    RecyclerView recycleview_cart_one, recycleview_cart_two;
    RecyclerView.LayoutManager mLayoutManager_cart_one, mLayoutManager_cart_two;
    RecyclerView.Adapter mAdapter;

    ArrayList<String> alName;
    ArrayList<Integer> alImage;



    CommanClass cc;

    ProgressDialog pDialog;
    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";
    String android_id;
    String username;


    List<CartDetailData> cartdetaildatas;
    List<CartDetailData> cartdetaildatas_rate;

    String SubTotal;
    String Total;

    LinearLayout ln_nodata;

    TextView tv_no_media;

    String address_id = "";
    String detailAddress;
    String title_name;

    FrameLayout fl_adreess;
    TextView tv_address_detail, tv_title;
    String tokens;


    String price_total = "";

    boolean isFirstTime = true;

    public static String cart_total = "";
    public static String price_item = "";

    boolean removeInProcess = false;
    boolean moveInProcess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addto_cart_activiy);

        cc = new CommanClass(this);
        tokens = cc.loadPrefString("1811-token");

        Log.e("tokens",tokens);

        pDialog = new ProgressDialog(AddToCartActivity.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
//        db = new SQLiteTD(this);

        init();
        clickListner();
        String title = getString(R.string.cart);

        mLayoutManager_cart_one = new LinearLayoutManager(this);
        tv_toolbar_title.setText(title);
        tv_toolbar_title.setPadding(0, 0, 40, 0);


        Log.e("toolbar", title);

        SubTotal = getString(R.string.sub_total);
        Total = getString(R.string.total);
//
//        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(this),
//                CookiePolicy.ACCEPT_ORIGINAL_SERVER);
//        CookieHandler.setDefault(cookieManager);




//        CookieStore cookieStore = new PersistentCookieStore(this);
//        CookieManager manager = new CookieManager( cookieStore, CookiePolicy.ACCEPT_ALL );
//        CookieHandler.setDefault( manager  );

    }

    @Override
    protected void onResume() {
        if (tokens.isEmpty()) {
            Log.e("Token", "Empty");


            if (!cc.isConnectingToInternet()) {

                cc.showToast(getString(R.string.no_internet));

            } else {
                CartDetail();
            }

        } else {

            if (!cc.isConnectingToInternet()) {

                cc.showToast(getString(R.string.no_internet));

            } else {
                CartDetail();
            }
        }

        Log.e("Token", "NoEmpty");


        super.onResume();
    }


    /*Cart detail WebService*/
    public void CartDetail() {
        isInstock = true;
        if (!pDialog.isShowing()) {
            pDialog.show();
        }


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.cart_detail,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("@@@Cart_Detail", response);
                        JSONArray jsonArray = null;
                        JSONArray jsonArray1 = null;
                        if(pDialog.isShowing()){
                            pDialog.dismiss();
                        }
                        try {

                            String ims = "";
                            JSONObject jObject = new JSONObject(response);

                            cartdetaildatas = new ArrayList<>();
                            cartdetaildatas_rate = new ArrayList<>();
                            if (jObject.getString("status").equals("200")) {


//                                String cookies = CookieManager.getInstance().getCookie(url);
//                                Log.d(TAG, "All the cookies in a string:" + cookies);



                                jsonArray = jObject.getJSONArray("products");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    CartDetailData cartdata = new CartDetailData();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    cartdata.setCart_id(jsonObject.getString("cart_id"));
                                    cartdata.setProduct_id(jsonObject.getString("product_id"));
                                    cartdata.setCart_image(jsonObject.getString("thumb"));
                                    cartdata.setName(jsonObject.getString("name"));
                                    cartdata.setModels(jsonObject.getString("model"));
                                    cartdata.setPrice(jsonObject.getString("price"));
                                    cartdata.setQuantity(jsonObject.getString("quantity"));
                                    cartdata.setStock(jsonObject.getBoolean("stock"));

                                    cartdetaildatas.add(cartdata);
                                    Log.e("cart_name", jsonObject.getString("name"));

                                    if (!jsonObject.getBoolean("stock")) {
                                        isInstock = false;
                                    }

                                }
                                jsonArray1 = jObject.getJSONArray("totals");
                                for (int k = 0; k < jsonArray1.length(); k++) {

                                    CartDetailData cartdata = new CartDetailData();
                                    JSONObject jsonObject = jsonArray1.getJSONObject(k);

                                    cartdata.setTotal(jsonObject.getString("text"));
                                    cartdata.setTitle(jsonObject.getString("title"));


                                    cartdetaildatas_rate.add(cartdata);



                                    Log.e("cart_name_total", jsonObject.getString("text"));

                                }
                                for (int i = 0; i < cartdetaildatas_rate.size(); i++) {

                                    CartDetailData order_data = cartdetaildatas_rate.get(i);
                                    String title = cartdetaildatas_rate.get(i).getTitle();

                                    if (title.equals(Total)) {

                                        tv_price.setText(order_data.getTotal());
                                    } else if (title.equals(SubTotal)) {

                                        String total = order_data.getTotal();

                                        cc.savePrefString("total_amount", total);

                                        tv_sub_total.setText(order_data.getTotal());
                                        tv_cart_total_price.setText(order_data.getTotal());
                                    }
                                }


                                if (cartdetaildatas != null) {
                                    recycleview_cart_one = (RecyclerView) findViewById(R.id.recycleview_cart_one);
                                    mAdapter = new AddToCartAdapter(AddToCartActivity.this, cartdetaildatas);
                                    recycleview_cart_one.setLayoutManager(mLayoutManager_cart_one);
                                    recycleview_cart_one.setAdapter(mAdapter);

                                    if (cartdetaildatas.size() == 0) {

                                        ln_nodata.setVisibility(View.GONE);
//                                        iv_no_media.setVisibility(View.VISIBLE);
                                        tv_no_media.setVisibility(View.VISIBLE);

                                        tv_sub_total.setText("OMR. 0.000");
                                        tv_cart_total_price.setText("OMR. 0.000");
                                    }

                                } else {

                                    ln_nodata.setVisibility(View.GONE);
//                                    iv_no_media.setVisibility(View.VISIBLE);
                                    tv_no_media.setVisibility(View.VISIBLE);

                                    tv_sub_total.setText("OMR. 0.000");
                                    tv_cart_total_price.setText("OMR. 0.000");


                                    Log.e("Cart.....Nothing", "....................");
                                }
                            } else if (jObject.getString("status").equals("400")) {

//                                Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_LONG).show();
                                ln_nodata.setVisibility(View.GONE);
//                                iv_no_media.setVisibility(View.VISIBLE);
                                tv_no_media.setVisibility(View.VISIBLE);

                            }

                        } catch (JSONException e) {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            e.printStackTrace();
                            Log.e("Error : Exception", e.getMessage());
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                error.printStackTrace();

            }
        }) {
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//
//
////                MyApp.get().checkSessionCookie(response.headers);
////
////                 String rawCookies=  cc.loadPrefString("session_cookie");
//
////                Log.i("responsecart",response.headers.toString());
////                Map<String, String> responseHeaders = response.headers;
////              //String rawCookies = responseHeaders.get("Set-Cookie");
////              String rawCookies = responseHeaders.get("");
////                Log.i("cartdetail_cookies",rawCookies);
////                return super.parseNetworkResponse(response);
//
//        }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("1811-token", tokens);
                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
//                headers.put("Cookie", "default="+cc.loadPrefString("default"));
//                for (int i = 0; i < 2; i++) {
//
//                    if(i==0){
//
//                    }else{
//
//                    }
//
//                }



//                headers.put("PHPSESSID", cc.loadPrefString("PHPSESSID"));
//                headers.put("default", cc.loadPrefString("default"));

                String tokens=cc.loadPrefString("1811-token");

                Log.e("cartdetail","__"+tokens);

                Log.i("@@request headers", "__"+headers.toString());


                return headers;
            }


        };


        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void clickListner() {

        btn_addcart_continue.setOnClickListener(this);
        ln_addresses.setOnClickListener(this);
        img_back_btn.setOnClickListener(this);
    }

    private void init() {

        tv_no_media = (TextView) findViewById(R.id.tv_no_media);
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_address_detail = (TextView) findViewById(R.id.tv_address_detail);
        tv_cart_total_price = (TextView) findViewById(R.id.tv_cart_total_price);
        tv_sub_total = (TextView) findViewById(R.id.tv_sub_total);
        tv_price = (TextView) findViewById(R.id.tv_price);
//        recycleview_cart_one = (RecyclerView) findViewById(R.id.recycleview_cart_one);
//        recycleview_cart_two = (RecyclerView) findViewById(R.id.recycleview_cart_two);
        btn_addcart_continue = (Button) findViewById(R.id.btn_addcart_continue);
        ln_addresses = (LinearLayout) findViewById(R.id.ln_addresses);
        img_back_btn = (LinearLayout) findViewById(R.id.img_back_btn);
        ln_nodata = (LinearLayout) findViewById(R.id.ln_nodata);

//        iv_no_media = (ImageView) findViewById(R.id.iv_no_media);
        fl_adreess = (FrameLayout) findViewById(R.id.fl_adreess);

        ln_addresses.setVisibility(View.GONE);
        fl_adreess.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_addcart_continue:

//                if (isValidate()) {

//                    Intent intentPayment = new Intent(AddToCartActivity.this, PaymentActivity.class);
//                    Intent intentPayment = new Intent(AddToCartActivity.this, CartConfirmationActivity.class);
//                    startActivity(intentPayment);
//                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

//                }
                if (tokens.isEmpty()) {
                    cc.showDialogUserNotLogin();

                    Log.e("Token","EMPATY");

                } else {
                    if (isInstock) {
                        Intent intentPayment = new Intent(AddToCartActivity.this, CartConfirmationActivity.class);
                        startActivity(intentPayment);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    } else {
                        cc.showToast(getString(R.string.out_of_stock));
                    }

                    Log.e("Token","Not-EMPATY");

                }




                break;

            case R.id.ln_addresses:

//                String demo = "demo";
//                Intent intentAddAddress = new Intent(AddToCartActivity.this, MyAddressActivity.class);
//                intentAddAddress.putExtra("demo", demo);
//                startActivity(intentAddAddress);
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//                finish();

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

    public class AddToCartAdapter extends RecyclerView.Adapter<AddToCartAdapter.ViewHolder> {


        Context context;
        ProgressDialog pDialog;
        CommanClass cc;
        ProgressBar mProgressBar;

        List<CartDetailData> cartDetailDatas;

        String quantity = "";
        String cart_id;

        ArrayAdapter<String> adapter;
        String[] value = {getString(R.string.select_qt),getString(R.string.qt1),getString(R.string.qt2),getString(R.string.qt3),getString(R.string.qt4),getString(R.string.qt5),getString(R.string.qt6),getString(R.string.qt7),getString(R.string.qt8),getString(R.string.qt9),getString(R.string.qt10)};


        public AddToCartAdapter(AddToCartActivity addToCartActivity, List<CartDetailData> cartdetaildatas) {
            this.context = addToCartActivity;
            this.cartDetailDatas = cartdetaildatas;
            cc = new CommanClass(context);
            adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, value);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.addtocart, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

            viewHolder.tvSpecies.setText(cartDetailDatas.get(i).getName());
            viewHolder.tv_model.setText(cartDetailDatas.get(i).getModels());
            viewHolder.tv_price.setText(cartDetailDatas.get(i).getPrice());

            if (cartDetailDatas.get(i).isStock()) {
                viewHolder.ll_stock.setVisibility(View.VISIBLE);
                viewHolder.tv_stock.setText(getString(R.string.in_stock));
                viewHolder.tv_stock.setTextColor(getResources().getColor(R.color.color_green));
            } else {
                viewHolder.ll_stock.setVisibility(View.VISIBLE);
                viewHolder.tv_stock.setText(getString(R.string.out_stock));
                viewHolder.tv_stock.setTextColor(getResources().getColor(R.color.color_red));
            }

            if (tokens.isEmpty()) {


                if (cartDetailDatas.size() != 0) {

                    String image = cartDetailDatas.get(i).getCart_image();

                    Log.e("cart_imge", image);

                    Picasso.with(context).load(cartDetailDatas.get(i).getCart_image()).into(viewHolder.imgThumbnail, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });

                } else {
                    Picasso.with(context).load(R.drawable.no_media).into(viewHolder.imgThumbnail);


                }

            } else {

                if (cartDetailDatas.size() != 0) {
                    Picasso.with(context).load(cartDetailDatas.get(i).getCart_image()).into(viewHolder.imgThumbnail, new Callback() {
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
            }


            viewHolder.ln_moveto_wishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!moveInProcess) {
                        moveInProcess = true;
                        getPositionItem1(viewHolder.getAdapterPosition());
                    }

                }
            });

            viewHolder.ln_cart_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!removeInProcess) {
                        removeInProcess = true;


                        if (tokens.isEmpty()){
                            getPositionItem(viewHolder.getAdapterPosition());
                        }else {
                            getPositionItem(viewHolder.getAdapterPosition());
                        }

                    }


                }
            });

            viewHolder.spinner.setAdapter(adapter);
            viewHolder.spinner.setSelected(true);
            Log.e("@@@cart_update", "Nothing Selected");
            if (cartDetailDatas.get(i).getQuantity() != null) {
                if (!cartDetailDatas.get(i).getQuantity().equals("0")) {
                    viewHolder.spinner.setSelected(true);
                    viewHolder.spinner.setSelection(Integer.parseInt(cartDetailDatas.get(i).getQuantity()), true);
                }
            }


            viewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    if (tokens.isEmpty()) {

                        CartDetailData cm = cartDetailDatas.get(i);
                        cart_id = cm.getCart_id();
                        if (position == 0) {

                        } else if (position == 1) {
                            quantity = "1";
                        } else if (position == 2) {
                            quantity = "2";
                        } else if (position == 3) {
                            quantity = "3";
                        } else if (position == 4) {
                            quantity = "4";
                        } else if (position == 5) {
                            quantity = "5";
                        } else if (position == 6) {
                            quantity = "6";
                        } else if (position == 7) {
                            quantity = "7";

                        } else if (position == 8) {
                            quantity = "8";

                        } else if (position == 9) {
                            quantity = "9";

                        } else if (position == 10) {
                            quantity = "10";
                        }


                        Log.e("Qty_Token","Empty");
                        Log.e("@@@cart_update", quantity + " ok");
                        cart_update(quantity, cart_id);

                    } else {
                        CartDetailData cm = cartDetailDatas.get(i);
                        cart_id = cm.getCart_id();
                        if (position == 0) {

                        } else if (position == 1) {
                            quantity = "1";
                        } else if (position == 2) {
                            quantity = "2";
                        } else if (position == 3) {
                            quantity = "3";
                        } else if (position == 4) {
                            quantity = "4";
                        } else if (position == 5) {
                            quantity = "5";
                        } else if (position == 6) {
                            quantity = "6";
                        } else if (position == 7) {
                            quantity = "7";

                        } else if (position == 8) {
                            quantity = "8";

                        } else if (position == 9) {
                            quantity = "9";

                        } else if (position == 10) {
                            quantity = "10";
                        }

                        Log.e("Qty_Token","Full");

                        Log.e("@@@cart_update", quantity + " ok");
                        cart_update(quantity, cart_id);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


        }

        private void cart_update(final String quantity, final String cart_id) {

            StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.cart_update,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.e("response:cart_update", response);
                            try {

//                                pDialog.dismiss();

                                JSONObject jObject = new JSONObject(response);
                                if (jObject.getString("status").equals("200")) {

//                                    if (cc.loadPrefBoolean("fromRemoveItem")) {
//                                        cc.savePrefBoolean("fromRemoveItem", false);
//                                    } else {
//                                        CartDetail();
//                                    }
                                    CartDetail();

//                                    cc.showToast(jObject.getString("message"));


                                } else if (jObject.getString("status").equals("400")) {

                                    //cc.showToast(jObject.getString("message"));
                                }

                                // CartDetail();
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


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("cart_id", cart_id);
                    map.put("quantity", quantity);
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

        @Override
        public int getItemCount() {
            return cartDetailDatas.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView imgThumbnail;
            public TextView tvSpecies, tv_model, tv_price, tv_stock;
            public Spinner spinner;
            public LinearLayout ln_moveto_wishlist;
            LinearLayout ln_cart_remove, ll_stock;


            public ViewHolder(View itemView) {
                super(itemView);

                imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
                tv_stock = (TextView) itemView.findViewById(R.id.tv_stock);
                tvSpecies = (TextView) itemView.findViewById(R.id.tv_title);
                tv_model = (TextView) itemView.findViewById(R.id.tv_model);
                tv_price = (TextView) itemView.findViewById(R.id.tv_price);
                spinner = (Spinner) itemView.findViewById(R.id.spinner);
                ln_moveto_wishlist = (LinearLayout) itemView.findViewById(R.id.ln_moveto_wishlist);
                ln_cart_remove = (LinearLayout) itemView.findViewById(R.id.ln_cart_remove);
                ll_stock = (LinearLayout) itemView.findViewById(R.id.ll_stock);
                mProgressBar = (ProgressBar) itemView.findViewById(R.id.pgb_image_loading);

                imgThumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.e("topdealspositions:-", String.valueOf(getPosition()));

                    }
                });


            }

        }

        private void getPositionItem(int adapterPosition) {
            CartDetailData nm = cartdetaildatas.get(adapterPosition);

            cc.savePrefString("cardid", String.valueOf(nm.getCart_id()));
            Log.e("cardid", nm.getCart_id() + "");

            if (tokens.isEmpty()){
                removecart(nm.getCart_id() + "", adapterPosition);

                Log.e("Remove_Token","Empty");

            }else {
                removecart(nm.getCart_id() + "", adapterPosition);


                Log.e("Remove_Token","Avilable");
            }




        }

        private void getPositionItem1(int adapterPosition) {

            CartDetailData nm = cartdetaildatas.get(adapterPosition);

            cc.savePrefString("cardid", String.valueOf(nm.getCart_id()));
            Log.e("cardid", nm.getCart_id() + "");

            if (tokens.isEmpty()) {

//                db.insertwishData(wishid, wish_product_name, wish_product_price, wish_product_imagepath);
                Log.e("Token", "Empty");
//                cc.showToast(getString(R.string.pro_added_wishlist));
                addwishlist(nm.getProduct_id(), nm.getCart_id(), adapterPosition);


            } else {
                addwishlist(nm.getProduct_id(), nm.getCart_id(), adapterPosition);
//                cc.showToast("No data Availble");
                Log.e("Token", "No data Availble");
            }


        }

        private void removecart(final String cart_id, final int position) {

            StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.remove_cart,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.e("response:removecart", response);
                            try {

//                                pDialog.dismiss();
                                JSONObject jObject = new JSONObject(response);
//                                cc.showToast(getString(R.string.cart_remove));


                                cartdetaildatas.remove(position);
                                mAdapter.notifyDataSetChanged();

                                if (cartdetaildatas.size() == 0) {

                                    ln_nodata.setVisibility(View.GONE);
//                                    iv_no_media.setVisibility(View.VISIBLE);
                                    tv_no_media.setVisibility(View.VISIBLE);

                                    tv_price.setText("OMR. 0.000");
                                    tv_sub_total.setText("OMR. 0.000");
                                    tv_cart_total_price.setText("OMR. 0.000");
                                }

                                removeInProcess = false;
                                moveInProcess = false;
//                                recycleview_cart_one.setAdapter(null);
//                                cc.savePrefBoolean("fromRemoveItem", true);
//                                CartDetail();
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



                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("cart_id", cart_id);
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

        private void addwishlist(final String product_id, final String cart_id, final int pos) {

            pDialog = new ProgressDialog(context);
            pDialog.setMessage(getString(R.string.please_wait));
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

                                if (jObject.getString("status").equals("200")) {
                                    removecart(cart_id, pos);
                                    cc.showToast(getString(R.string.pro_moved));
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
//                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                    // since we don't know which of the two underlying network vehicles
//                    // will Volley use, we have to handle and store session cookies manually
//                    Log.i("response",response.headers.toString());
//                    Map<String, String> responseHeaders = response.headers;
//                    String rawCookies = responseHeaders.get("Set-Cookie");
//                    Log.i("Addtowishlist_cookies",rawCookies);
//                    return super.parseNetworkResponse(response);
//                }

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

    public boolean isValidate() {
        String msg = null;

        if (TextUtils.isEmpty(address_id)) {
            msg = getString(R.string.select_address);

        }
        if (msg != null) {
            AndyUtils.showToast(AddToCartActivity.this, msg);
            return false;
        }
        if (msg == null) {
            return true;
        }
        AndyUtils.showToast(AddToCartActivity.this, msg);
        return false;
    }


}