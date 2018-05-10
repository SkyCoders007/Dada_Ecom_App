package com.mxi.ecommerce.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.mxi.ecommerce.activity.ProductDetailActivity;
import com.mxi.ecommerce.comman.AppController;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.comman.ConnectionUrl;
import com.mxi.ecommerce.model.CategoryListingdata;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aksahy on 28/2/18.
 */

public class SearchCategoryGridListingAdapter extends BaseAdapter {

    Context mContext;
    List<CategoryListingdata> categoryGideListingdatas;
    ProgressBar mProgressBar;

    CommanClass cc;

    private LayoutInflater inflater = null;
    ProgressDialog pDialog;
    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";

    private BitmapDrawable bitmapDrawable;
    private Bitmap bitmap1;

    public SearchCategoryGridListingAdapter(Context mContext, List<CategoryListingdata> categoryListingdataList) {
        this.mContext = mContext;
        this.categoryGideListingdatas = categoryListingdataList;

        inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        cc = new CommanClass(mContext);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return categoryGideListingdatas.size();
    }

    @Override
    public Object getItem(int position) {


        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public class Holder {
        TextView tv_productname, tv_price, tv_offer_price;
        ImageView imageView, iv_wishlist, img_addtocart, image_share;
        LinearLayout product_detail, ll_offer_price;
        ProgressBar mProgressBar;
        View v_price_check;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub


        final Holder holder = new Holder();
        View rowView;


        rowView = inflater.inflate(R.layout.search_category_grid_list_item, null);


        holder.tv_offer_price = (TextView) rowView.findViewById(R.id.tv_offer_price);
        holder.ll_offer_price = (LinearLayout) rowView.findViewById(R.id.ll_offer_price);
        holder.v_price_check = (View) rowView.findViewById(R.id.v_price_check);

        holder.tv_productname = (TextView) rowView.findViewById(R.id.tv_product_name);
        holder.tv_price = (TextView) rowView.findViewById(R.id.tv_price);
        holder.imageView = (ImageView) rowView.findViewById(R.id.grid_image);
        holder.image_share = (ImageView) rowView.findViewById(R.id.image_share);
        holder.img_addtocart = (ImageView) rowView.findViewById(R.id.img_addtocart);
        holder.iv_wishlist = (ImageView) rowView.findViewById(R.id.iv_wishlist);
        holder.product_detail = (LinearLayout) rowView.findViewById(R.id.product_detail);
        holder.mProgressBar = (ProgressBar) rowView.findViewById(R.id.pgb_image_loading);
        holder.v_price_check = (View) rowView.findViewById(R.id.v_price_check);

        CategoryListingdata ccdata = categoryGideListingdatas.get(position);


        holder.tv_productname.setText(ccdata.getProduct_name());
        holder.tv_price.setText(ccdata.getPrice());

        Log.e("@@Spc", ccdata.getSpecial() + "");

        if (ccdata.getSpecial().equals("false")) {
            holder.v_price_check.setVisibility(View.GONE);
            holder.ll_offer_price.setVisibility(View.GONE);
        } else {
            holder.ll_offer_price.setVisibility(View.VISIBLE);
            holder.tv_offer_price.setText(ccdata.getSpecial());
        }

        if (categoryGideListingdatas.get(position).isIswishlist()) {
            holder.iv_wishlist.setImageResource(R.mipmap.nav_wish);
        } else {
            holder.iv_wishlist.setImageResource(R.mipmap.faviourite);
        }

        holder.iv_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getpos1(position);
            }
        });

        holder.image_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                CategoryListingdata rm = categoryGideListingdatas.get(position);

                String product_name = rm.getProduct_name();
                String product_link = rm.getSharing_url();


                bitmapDrawable = (BitmapDrawable) holder.imageView.getDrawable();// get the from imageview or use your drawable from drawable folder
                bitmap1 = bitmapDrawable.getBitmap();
                String imgBitmapPath = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap1, "title", null);
                Uri imgBitmapUri = Uri.parse(imgBitmapPath);
                String shareText = "Share image and text";
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("*/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);
                shareIntent.putExtra(Intent.EXTRA_TEXT, product_name + "\n" + product_link);

                mContext.startActivity(Intent.createChooser(shareIntent, "Share Wallpaper using"));

            }
        });

        holder.img_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                addtocart(position);

            }
        });


        holder.product_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getpos(position);
            }
        });


        if (categoryGideListingdatas.size() != 0) {

            if (categoryGideListingdatas.get(position).getThumb().equals("")) {
                Picasso.with(mContext).load(R.drawable.no_media).into(holder.imageView);
                holder.mProgressBar.setVisibility(View.GONE);
            } else {

                Picasso.with(mContext).load(ccdata.getThumb()).into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        holder.mProgressBar.setVisibility(View.GONE);
                    }
                });

            }


        } else {
            Picasso.with(mContext).load(R.drawable.no_media).into(holder.imageView);
            holder.mProgressBar.setVisibility(View.GONE);
        }


        return rowView;
    }

    /*Add to Wishlist Webservice*/

    private void addwishlist() {

        pDialog = new ProgressDialog(mContext);
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

                            if (jObject.getString("status").equals("200")) {
                                cc.showToast(jObject.getString("message"));
                            }

//                            cc.showToast("Product added in to Wish List");
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
                headers.put("1811-token", cc.loadPrefString("1811-token"));
                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void getpos(int position) {

        CategoryListingdata nm = categoryGideListingdatas.get(position);
        nm.getProduct_id();

        Intent intentProductDetail = new Intent(mContext, ProductDetailActivity.class);
        intentProductDetail.putExtra("product_id", nm.getProduct_id());

        mContext.startActivity(intentProductDetail);
        ((Activity) mContext).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

    }

    private void getpos1(int position) {

        CategoryListingdata nm = categoryGideListingdatas.get(position);
        Log.e("token-1811", cc.loadPrefString("1811-token"));
        cc.savePrefString("productid", nm.getProduct_id());

        addwishlist();

        String product_id = cc.loadPrefString("productid");
        Log.e("product_id", product_id);

    }

    private void RemoveFromWishList(final String product_id) {
        pDialog = new ProgressDialog(mContext);
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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("remove", product_id);

                Log.i("@@req remove wishlist", map.toString());
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

    /*Add to cart Webservice*/
    private void addtocartws() {

        pDialog = new ProgressDialog(mContext);
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
                            cc.showToast("Product added in to Cart");

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
//                Log.i("addtocart_cookies", rawCookies);
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
                headers.put("1811-token", cc.loadPrefString("1811-token"));
                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void addtocart(int position) {

        CategoryListingdata nm = categoryGideListingdatas.get(position);
        nm.getProduct_id();

        cc.savePrefString("productid", nm.getProduct_id());
        addtocartws();

        String product_id = cc.loadPrefString("productid");
        Log.e("product_id", product_id);
    }

}