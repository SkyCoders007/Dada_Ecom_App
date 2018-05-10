package com.mxi.ecommerce.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
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
import com.mxi.ecommerce.activity.ProductDetailActivity;
import com.mxi.ecommerce.comman.AppController;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.comman.ConnectionUrl;
import com.mxi.ecommerce.model.RelatedProductData;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductRelatedProduct extends RecyclerView.Adapter<ProductRelatedProduct.ViewHolder> {


    ArrayList<Integer> alcategoryImage;
    ArrayList<String> alName;
    ArrayList<String> alproductname;
    Context context;
    List<RelatedProductData> relatedProductModels;
    static ProgressBar mProgressBar;
    CommanClass cc;
    private BitmapDrawable bitmapDrawable1;
    private Bitmap bitmap11;

    private LayoutInflater inflater;


    String tokens;

    ProgressDialog pDialog;
    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";

    boolean isAddToCartInProgress = false;
    boolean isAddToWishListInProgress = false;

    public ProductRelatedProduct(ProductDetailActivity productDetailActivity, List<RelatedProductData> relatedProductModels) {


        this.context=productDetailActivity;
        this.relatedProductModels=relatedProductModels;
        cc = new CommanClass(context);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {


        tokens = cc.loadPrefString("1811-token");

        View v = inflater.inflate(R.layout.recommendedproduct, viewGroup, false);

        final ViewHolder viewHolder = new ViewHolder(v);


        viewHolder.tvSpecies.setText(relatedProductModels.get(i).getProductname());
        viewHolder.tv_price.setText(relatedProductModels.get(i).getPrice());

        if (relatedProductModels.size()!=0){
            Picasso.with(context).load(relatedProductModels.get(i).getImageUrl()).into(viewHolder.imgThumbnail, new Callback() {
                @Override
                public void onSuccess() {
                    mProgressBar.setVisibility(View.GONE);
                }
                @Override
                public void onError() {
                    mProgressBar.setVisibility(View.GONE);
                }
            });

        }else {
            Picasso.with(context).load(R.drawable.no_media).into(viewHolder.imgThumbnail);
            mProgressBar.setVisibility(View.GONE);
        }

        viewHolder.imgThumbnail.setOnClickListener(new View.OnClickListener() {
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
        viewHolder.iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RelatedProductData rm = relatedProductModels.get(i);

                String product_name=rm.getProductname();
                String product_link=rm.getSharing_url();





                String image_url= rm.getImageUrl();

                bitmapDrawable1 = (BitmapDrawable) viewHolder.imgThumbnail.getDrawable();// get the from imageview or use your drawable from drawable folder
                bitmap11 = bitmapDrawable1.getBitmap();
                String imgBitmapPath= MediaStore.Images.Media.insertImage(context.getContentResolver(),bitmap11,"title",null);
                Uri imgBitmapUri=Uri.parse(imgBitmapPath);
                String shareText="Share image and text";
                Intent shareIntent=new Intent(Intent.ACTION_SEND);
                shareIntent.setType("*/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM,imgBitmapUri);
                shareIntent.putExtra(Intent.EXTRA_TEXT, product_name +"\n"+product_link);

                context.startActivity(Intent.createChooser(shareIntent,"Share Wallpaper using"));

            }

        });

        viewHolder.iv_recoomented_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                cc.showToast("Click");

                if (!isAddToCartInProgress) {
                    isAddToCartInProgress = true;
                    addtocart(viewHolder.getAdapterPosition());
                }

            }
        });


        viewHolder.iv_recoomented_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isAddToWishListInProgress) {
                    isAddToWishListInProgress = true;

                    if (relatedProductModels.get(viewHolder.getAdapterPosition()).iswishlist()) {
                        relatedProductModels.get(viewHolder.getAdapterPosition()).setIswishlist(false);
                        viewHolder.iv_recoomented_wishlist.setImageResource(R.mipmap.faviourite);
                        Log.e("ProductWishList","False");
                    } else {
                        relatedProductModels.get(viewHolder.getAdapterPosition()).setIswishlist(true);
                        viewHolder.iv_recoomented_wishlist.setImageResource(R.mipmap.nav_wish);
                        Log.e("ProductWishList","True");
                    }
                    getPositionItem(viewHolder.getAdapterPosition(), relatedProductModels.get(viewHolder.getAdapterPosition()).iswishlist());
                }

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        if (relatedProductModels.get(i).iswishlist()) {
            viewHolder.iv_recoomented_wishlist.setImageResource(R.mipmap.nav_wish);
        } else {
            viewHolder.iv_recoomented_wishlist.setImageResource(R.mipmap.faviourite);
        }

        if (relatedProductModels.get(i).getSpecial().equalsIgnoreCase("false")) {
            viewHolder.v_price_check.setVisibility(View.GONE);
            viewHolder.ll_offer_price.setVisibility(View.GONE);
        } else {
            viewHolder.v_price_check.setVisibility(View.VISIBLE);
            viewHolder.ll_offer_price.setVisibility(View.VISIBLE);
            viewHolder.tv_offer_price.setText(relatedProductModels.get(i).getSpecial());
        }

        viewHolder.tvSpecies.setText(relatedProductModels.get(i).getProductname());
        viewHolder.tv_price.setText(relatedProductModels.get(i).getPrice());

        if (relatedProductModels.size() != 0) {
            Picasso.with(context).load(relatedProductModels.get(i).getImageUrl()).into(viewHolder.imgThumbnail, new Callback() {
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

    @Override
    public int getItemCount() {
        return relatedProductModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public ImageView imgThumbnail,iv_recoomented_cart,iv_recoomented_wishlist,iv_share;
        public TextView tvSpecies,tv_price,tv_offer_price;

        View v_price_check;

        public LinearLayout ln_view_category_mainscreen, ll_offer_price;


        public ViewHolder (final View itemView) {
            super(itemView);


            v_price_check = (View) itemView.findViewById(R.id.v_price_check);
            ll_offer_price = (LinearLayout) itemView.findViewById(R.id.ll_offer_price);
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
        RelatedProductData nm = relatedProductModels.get(adapterPosition);
        nm.getProduct_id();

        cc.savePrefString("productid", String.valueOf(nm.getProduct_id()));

        Log.e("prodctpositionid",nm.getImageUrl()+"");

    }

    public void addtocart(int adapterPosition) {

        RelatedProductData rm = relatedProductModels.get(adapterPosition);
        cc.savePrefString("productid", String.valueOf(rm.getProduct_id()));

        if (tokens.isEmpty()) {
//                sqLiteHelper.insertData(id, cart_product_name, cart_product_price, cart_product_imagepath);
            Log.e("Token", "Empty");
            isAddToCartInProgress = false;
            cc.showToast("Please Login to add the product in cart");

        } else {
            addtocartws();
            Log.e("Token", "Not Empty");
        }
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
                                // makeJsonCallCartCounter();
                            }
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
                headers.put("1811-token", tokens);
                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void getPositionItem(int adapterPosition, boolean removeItem) {


        RelatedProductData rm = relatedProductModels.get(adapterPosition);
        cc.savePrefString("productid", String.valueOf(rm.getProduct_id()));


        if (tokens.isEmpty()) {

            Log.e("Token", "Empty");

//            if (removeItem) {
//                dbHelper.InsertWishlistData(rm.getSharing_url(), rm.getProduct_id() + "", rm.getImageUrl()
//                        , rm.getProductname(), rm.getModel(), rm.getPrice(), rm.getSpecial(), rm.getTax());
//            } else {
//                dbHelper.RemoveProduct(rm.getProduct_id() + "");
                relatedProductModels.remove(adapterPosition);
//                mAdapter.notifyDataSetChanged();
//            }

            isAddToWishListInProgress = false;

        } else {
            cc.savePrefString("productid", String.valueOf(rm.getProduct_id()));

            if (removeItem) {
                addwishlist();
            } else {
                RemoveFromWishList(rm.getProduct_id() + "");
            }
        }

    }

    /*Add to Wishlist Webservice*/
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
                Log.i("response", response.headers.toString());
//                Map<String, String> responseHeaders = response.headers;
//                String rawCookies = responseHeaders.get("Set-Cookie");
//                Log.i("addtowishlist_cookies", rawCookies);
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

    /*Add to Remove Wishlist Webservice*/
    private void RemoveFromWishList(final String product_id) {
        pDialog = new ProgressDialog(context);
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
                Log.i("@@request remove", map.toString());
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