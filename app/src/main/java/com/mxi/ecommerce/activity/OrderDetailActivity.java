package com.mxi.ecommerce.activity;

import android.app.Activity;
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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.ecommerce.R;
import com.mxi.ecommerce.adapter.OrdertitleAdapter;
import com.mxi.ecommerce.comman.AndyUtils;
import com.mxi.ecommerce.comman.AppController;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.comman.ConnectionUrl;
import com.mxi.ecommerce.model.OrderDetialdata;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener {

    String order_id,status;

    CommanClass cc;

    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";
    ProgressDialog pDialog;


    String title = "Order Detail";
    TextView tv_toolbar_title;
    LinearLayout img_back_btn, ln_your_order;


    List<OrderDetialdata> order_data_listing;
    List<OrderDetialdata> order_data_listing2;
    List<OrderDetialdata> order_data_listing3;

    TextView order_ids, order_date, order_payment_addres, shipping_address, shipping_methos;

    TextView sub_total, rate, vat, total_amount;

    RecyclerView recycler_myorder,recycler_rv_titles;

    RecyclerView.LayoutManager mLayoutManager_recycler_myorder;
    RecyclerView.LayoutManager mLayoutManager_recycler_rv_titles;


    RecyclerView.Adapter mAdapter;
    RecyclerView.Adapter mAdapter1;

    String demos;

    String rates;
    String SubTotal;
    String Total;
    String vates;
    String titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        cc = new CommanClass(this);

        Intent get = getIntent();
        order_id = get.getStringExtra("order_id");
        status = get.getStringExtra("status");

        Log.e("order_id", order_id);

//        tv_toolbar_title.setText(title);
//        tv_toolbar_title.setPadding(0, 0, 40, 0);
        Log.e("toolbar", title);

        init();
        clickListner();

        mLayoutManager_recycler_myorder = new LinearLayoutManager(this);
        mLayoutManager_recycler_rv_titles = new LinearLayoutManager(this);

        if (!cc.isConnectingToInternet()) {
            cc.showToast(getString(R.string.no_internet));
        } else {
            order_listing();
        }


        rates = "Flat Shipping Rate";
        SubTotal = "Sub-Total";
        Total = "Total";
        vates = "VAT (20%)";


    }

    private void clickListner() {

        img_back_btn.setOnClickListener(this);
    }

    private void init() {
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        img_back_btn = (LinearLayout) findViewById(R.id.img_back_btn);
        ln_your_order = (LinearLayout) findViewById(R.id.ln_your_order);


        order_ids = (TextView) findViewById(R.id.order_id);
        order_date = (TextView) findViewById(R.id.order_date);
        order_payment_addres = (TextView) findViewById(R.id.order_payment_addres);
//        shipping_address = (TextView) findViewById(R.id.shipping_address);
//        shipping_methos = (TextView) findViewById(R.id.shipping_methos);






        recycler_myorder = (RecyclerView) findViewById(R.id.recycleview_order);
        recycler_rv_titles = (RecyclerView) findViewById(R.id.rv_titles);

        tv_toolbar_title.setText(title);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.img_back_btn:

                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                break;
        }
    }

    private void order_listing() {

        pDialog = new ProgressDialog(OrderDetailActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.order_get_history,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("order_histrys_detail", response);
                        JSONArray jsonArray = null;
                        JSONArray jsonArray1 = null;
                        JSONArray jsonArray2 = null;

                        order_data_listing = new ArrayList<>();
                        order_data_listing2 = new ArrayList<>();
                        order_data_listing3 = new ArrayList<>();

                        if(pDialog.isShowing()){
                            pDialog.dismiss();
                        }

                        try {
                            JSONObject jObject = new JSONObject(response);

                            String order_id = jObject.getString("order_id");
                            String date_added = jObject.getString("date_added");
                            String date = jObject.getString("date");
                            String payment_address = jObject.getString("payment_address");


                            String CurrentString = date;
                            String[] separated = CurrentString.split(" ");
                            separated[0] = separated[0].trim();
                            separated[1] = separated[1].trim();


                            String dates=separated[0];
                            Log.e("dates",dates);


                            String mStringDate = dates;
                            String oldFormat= "yyyy-MM-dd";
                            String newFormat= "dd-MM-yyyy";

                            String formatedDate = "";
                            SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat);
                            Date myDate = null;
                            try {
                                myDate = dateFormat.parse(mStringDate);
                            } catch (java.text.ParseException e) {
                                e.printStackTrace();
                            }

                            SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat);
                            formatedDate = timeFormat.format(myDate);

                            Log.e("@@@newDate",formatedDate);


                            cc.savePrefString("date_added",date_added);

                            jsonArray2 = jObject.getJSONArray("histories");

                            for (int h = 0; h < jsonArray2.length(); h++) {

                                OrderDetialdata order_data = new OrderDetialdata();
                                JSONObject jsonObject = jsonArray2.getJSONObject(h);

                                order_data.setHistroi_status(jsonObject.getString("status"));
                                order_data_listing3.add(order_data);

                                Log.e("history_status", jsonObject.getString("status"));


                            }
                            for (int i = 0; i < order_data_listing3.size(); i++) {
                                OrderDetialdata order_data = order_data_listing3.get(i);
                                String title = order_data_listing3.get(i).getHistroi_status();
                            }

                            jsonArray = jObject.getJSONArray("products");
                            for (int j = 0; j < jsonArray.length(); j++) {

                                OrderDetialdata order_data = new OrderDetialdata();
                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                order_data.setProduct_id(jsonObject.getString("product_id"));
                                order_data.setProduct_name(jsonObject.getString("name"));
                                order_data.setProduct_model(jsonObject.getString("model"));
                                order_data.setProduct_price(jsonObject.getString("price"));
                                order_data.setProduct_thumb(jsonObject.getString("product_thumb"));
                                // order_data.setProduct_thumb(jsonObject.getString("product_thumb"));

                                order_data.setProduct_thumb(jsonObject.getString("product_thumb"));

                                Log.e("product_name",jsonObject.getString("product_id"));

//                                JSONArray trackArray = jsonObject.getJSONArray("tracking");
//
//                                if (trackArray != null) {
//                                    if (trackArray.length() > 0) {
//
//                                        for (int i = 0; i < trackArray.length(); i++) {
//                                            JSONObject trackObject = trackArray.getJSONObject(i);
//
//                                            order_data.setTrack_date(trackObject.getString("date_added"));
//                                            order_data.setTrack_comment(trackObject.getString("status"));
//                                            order_data.setTrack_status(trackObject.getString("comment"));
//                                            order_data.setTrack_notify(trackObject.getString("notify"));
//
//                                        }
//
//
//                                    }
//                                }

                                order_data_listing.add(order_data);

                                Log.e("product_name", jsonObject.getString("name"));

                            }
                            jsonArray1 = jObject.getJSONArray("totals");
                            for (int k = 0; k < jsonArray1.length(); k++) {

                                OrderDetialdata order_datas = new OrderDetialdata();
                                JSONObject jsonObject = jsonArray1.getJSONObject(k);

                                order_datas.setValue(jsonObject.getString("text"));
                                order_datas.setTitle(jsonObject.getString("title"));

                                order_data_listing2.add(order_datas);

                            }
                            if (order_data_listing2.size() > 0) {
//                                recycler_myorder.setLayoutManager(mLayoutManager_recycler_myorder);
                                mAdapter = new OrdertitleAdapter(OrderDetailActivity.this, order_data_listing2);
                                recycler_rv_titles.setLayoutManager(mLayoutManager_recycler_rv_titles);
                                recycler_rv_titles.setAdapter(mAdapter);
                                Log.e("yes", "ProductInterstedAdapter..GONE");

                            } else {
                                Log.e("nothing", "ProductInterstedAdapter..GONE");
                            }

                            Log.e("array_size", String.valueOf(order_data_listing2.size()));
//
//                            for (int i = 0; i < order_data_listing2.size(); i++) {
//
//                                OrderDetialdata order_data = order_data_listing2.get(i);
//
//                                String title = order_data_listing2.get(i).getTitle();
//
//                                if (title.equals(rates)) {
//                                    rate.setText(order_data.getValue());
//                                } else if (title.equals(SubTotal)) {
//                                    sub_total.setText(order_data.getValue());
//                                } else if (title.equals(Total)) {
//                                    total_amount.setText(order_data.getValue());
//                                } else if (title.equals(vates)) {
//                                    vat.setText(order_data.getValue());
//                                }
//                            }
                            if (order_data_listing.size() > 0) {
//                                recycler_myorder.setLayoutManager(mLayoutManager_recycler_myorder);
                                mAdapter = new OrderDetailAdapter(OrderDetailActivity.this, order_data_listing);
                                recycler_myorder.setLayoutManager(mLayoutManager_recycler_myorder);
                                recycler_myorder.setAdapter(mAdapter);

                            } else {
                                Log.e("nothing", "ProductInterstedAdapter..GONE");
                                ln_your_order.setVisibility(View.INVISIBLE);
                            }

                            if (jObject.getString("status").equals("200")) {

                            } else if (jObject.getString("status").equals("400")) {
                                cc.showToast(jObject.getString("message"));
                            }

                            if (order_id.equals("null")) {
                                order_ids.setVisibility(View.GONE);
                            } else {
                                order_ids.setText(order_id);
                            }

                            if (date_added.equals("null")) {
                                order_date.setVisibility(View.GONE);
                            } else {
                                order_date.setText(formatedDate);
                            }

                            if (payment_address.equals("null")) {
                                order_payment_addres.setVisibility(View.GONE);
                            } else {
                                order_payment_addres.setText(payment_address);
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
                AndyUtils.showToast(OrderDetailActivity.this, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("order_id", order_id);
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

    public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

        Context context;
        List<OrderDetialdata> order_data;
        ProgressBar mProgressBar;
        String messages;

        public OrderDetailAdapter(OrderDetailActivity myOrderActivity, List<OrderDetialdata> order_data_listing) {

            this.context = myOrderActivity;
            this.order_data = order_data_listing;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.order_product_listing, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);

//            LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
//                    new IntentFilter("custom-event-name"));

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

            viewHolder.tv_name.setText(order_data.get(i).getProduct_name());
            viewHolder.tv_model.setText(order_data.get(i).getProduct_model());
            viewHolder.tv_price.setText(order_data.get(i).getProduct_price());

            viewHolder.ln_order_tracking_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intentShopCategory = new Intent(context, OrderTrackingActivity.class);

                    intentShopCategory.putExtra("product_id", order_data.get(i).getProduct_id());
                    intentShopCategory.putExtra("order_id", order_id);

//                    intentShopCategory.putExtra("status", order_data.get(i).getTrack_status());
//                    intentShopCategory.putExtra("comment", order_data.get(i).getTrack_comment());
//                    intentShopCategory.putExtra("date", order_data.get(i).getTrack_date());

                    context.startActivity(intentShopCategory);
                    ((Activity) context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);


                }
            });




            if (order_data.get(i).getProduct_thumb().equals("")) {
                Picasso.with(context).load(R.drawable.logo_1811).into(viewHolder.imgThumbnail);
                mProgressBar.setVisibility(View.GONE);

            } else {

                Picasso.with(context).load(order_data.get(i).getProduct_thumb()).into(viewHolder.imgThumbnail, new Callback() {
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

            viewHolder.product_deatil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getPositionItem1(viewHolder.getAdapterPosition());

                    Intent intentProductDetail = new Intent(context, ProductDetailActivity.class);
                    intentProductDetail.putExtra("product_id",cc.loadPrefString("productid"));

                    String product_id = cc.loadPrefString("productid");

                    context.startActivity(intentProductDetail);

                    Log.e("productidholder",viewHolder.getAdapterPosition()+"");
                    Log.e("productidholder1",product_id);

                }
            });


            viewHolder.ln_return_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getPositionItem1(viewHolder.getAdapterPosition());

//                    Intent intentProductDetail = new Intent(context, ReturnProductDetailActivity.class);
//                    intentProductDetail.putExtra("product_id",cc.loadPrefString("productid"));
//                    intentProductDetail.putExtra(order_id,cc.loadPrefString("orderid"));
//
//                    String product_id = cc.loadPrefString("productid");
//
//                    context.startActivity(intentProductDetail);
//
//                    Log.e("productidholder",viewHolder.getAdapterPosition()+"");
//                    Log.e("productidholder1",product_id);
//                    Log.e("order_id",order_id);


                    Intent intentShopCategory = new Intent(context, ReturnProductDetailActivity.class);

                    intentShopCategory.putExtra("order_id",order_id);
                    intentShopCategory.putExtra("name",cc.loadPrefString("firstname"));
                    intentShopCategory.putExtra("email",cc.loadPrefString("email"));
                    intentShopCategory.putExtra("mobile",cc.loadPrefString("mobile"));
                    intentShopCategory.putExtra("status",status);
                    intentShopCategory.putExtra("date_added",cc.loadPrefString("date_added"));
                    intentShopCategory.putExtra("product_id",cc.loadPrefString("productid"));
                    intentShopCategory.putExtra("product_name",order_data.get(i).getProduct_name());
                    intentShopCategory.putExtra("model",order_data.get(i).getProduct_model());
                    intentShopCategory.putExtra("image",order_data.get(i).getProduct_thumb());
                    Log.e("@@product_image",order_data.get(i).getProduct_thumb()
                    );

                    intentShopCategory.putExtra("quantity","");
                    context.startActivity(intentShopCategory);
                    ((Activity)context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);


                }
            });
        }

        @Override
        public int getItemCount() {
            return order_data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView imgThumbnail;
            public TextView tv_name, tv_model, tv_price;
            public LinearLayout ln_order_tracking_status, ln_return_order, product_deatil;

            public ViewHolder(final View itemView) {
                super(itemView);

                imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
                tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                tv_model = (TextView) itemView.findViewById(R.id.tv_model);
                tv_price = (TextView) itemView.findViewById(R.id.tv_price);
                mProgressBar = (ProgressBar) itemView.findViewById(R.id.pgb_image_loading);
                ln_order_tracking_status = (LinearLayout) itemView.findViewById(R.id.ln_order_tracking_status);
                product_deatil = (LinearLayout) itemView.findViewById(R.id.product_deatil);
                ln_return_order = (LinearLayout) itemView.findViewById(R.id.ln_return_orders);

            }
        }

//        private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                // Get extra data included in the Intent
//                messages = intent.getStringExtra("message");
//                Log.e("message",messages);
//                Log.d("receiver", "Got message: " + messages);
//            }
//        };



        private void getPositionItem1(int adapterPosition) {
            OrderDetialdata nm = order_data.get(adapterPosition);
            nm.getProduct_id();

            cc.savePrefString("productid", String.valueOf(nm.getProduct_id()));

            Log.e("prodctpositionid",nm.getProduct_id()+"");

        }

    }

}
