package com.mxi.ecommerce.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.mxi.ecommerce.adapter.ProductRelatedProduct;
import com.mxi.ecommerce.adapter.ProductSpecificationAdapter;
import com.mxi.ecommerce.adapter.UserReviewAdapter;
import com.mxi.ecommerce.comman.AndyUtils;
import com.mxi.ecommerce.comman.AppController;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.comman.ConnectionUrl;
import com.mxi.ecommerce.model.Attribute;
import com.mxi.ecommerce.model.AttributeGroups;
import com.mxi.ecommerce.model.IntrestedCategoryData;
import com.mxi.ecommerce.model.ProductImagesData;
import com.mxi.ecommerce.model.ProductSize;
import com.mxi.ecommerce.model.RelatedProductData;
import com.mxi.ecommerce.model.UserReview;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ImageView btn_img_chnge_one, btn_img_chnge_two, iv_product_image, iv_product_banner;
    String title = "Product Detail";
    TextView tv_product_intrested_tag, tv_product_id, tv_toolbar_title, tv_produxt_name, tv_product_review, tv_product_rating, tv_product_price, tv_produxt_instock, tv_product_realted_tag;

    //RecyclerView Related Product

    RecyclerView recycleview_relatedproduct;
    RecyclerView.LayoutManager mLayoutManager_relatedproduct;
    String erro_message;
    //RecyclerView Interested Product

    RecyclerView recycleview_intrestedproduct, rv_review_1;
    RecyclerView.LayoutManager mLayoutManager_intrestedproduct;

    RecyclerView.Adapter mAdapter;

    ArrayList<Integer> alcategoryImage;
    ArrayList<String> alproductname;


    Button btn_add_cart, btn_add_to_wishlist;
    LinearLayout img_back_btn;
    private BitmapDrawable bitmapDrawable;
    private Bitmap bitmap1;
    LinearLayout ln_bg_img;
    LinearLayout ln_bg_img_two;

    View v_price_check;
    TextView tv_product_special_price, tv_product_brand;
    RecyclerView rv_product_specification;
    TextView tv_product_description;
    TextView tv_bottom_rating;
    TextView tv_bottom_review_text;

    ProgressBar pb_five_rating, pb_four_rating, pb_three_rating, pb_two_rating, pb_one_rating;
    TextView tv_five_rating, tv_four_rating, tv_three_rating, tv_two_rating, tv_one_rating;

    CommanClass cc;

    Integer img1;
    boolean isAddToCartInProgress = false;
    boolean isAddToWishListInProgress = false;
    //Product Listing

    ProgressDialog pDialog;
    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";
    String android_id;
    String product_id;
    String msgs = "";
    static ProgressBar mProgressBar, mProgressBar1;
    static ProgressBar pgb_image_loading_popup, pgb_image_loading_thumb;

    //Related Products
    List<RelatedProductData> relatedProductModels;
    List<IntrestedCategoryData> intrestedCategoryModels;
    List<ProductImagesData> productimageslisting;


    List<Integer> listRatingCsm;
    List<Integer> listRatingCsm1;
    ArrayList<AttributeGroups> listAttributeGroup;


    String popupimge;
    String thumb;


    //recycleview Get 60% off listing
    RecyclerView recycleview_image;
    RecyclerView.LayoutManager mLayoutManager_recycleview_image;


    String position_images = "";
    List<ProductImagesData> prosuctsimage;

    ArrayList<UserReview> listUserReview;

    String manufacturer = "";
    String price = "";
    String special = "";
    String yousave = "";
    String weight = "";
    String tax = "";

    String minimum = "";
    String review_status = "";
    boolean review_guest;
    String customer_name = "";

    String product_review = "";
    String rating = "";

    String productimage = "";
    String productName = "";
    String productModel = "";

    String productRewards = "";
    String productPoints = "";
    String productDescription = "";

    String productimage_banner = "";
    String stock = "";
    String tokens;


    Spinner product_list, sp_avilable_quanty;
    ArrayList<String> spiner_data;
    ArrayList<ProductSize> productSizes;
    LinearLayout ln_spinner_gone;
    String qty = "";
    String options = "";
    String options_name = "option";

    private boolean validate;

    String colorName = null;
    String share_url = "";
    int id;

    ImageView iv_cart, iv_wishlist, iv_shareing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prouctdeailactivity);
        cc = new CommanClass(this);

        tokens = cc.loadPrefString("1811-token");

        cc.savePrefString("product_option_value_id1", "");
        cc.savePrefString("option_value_id1", "");

        init();
        clickListner();

        tv_toolbar_title.setText(title);
        tv_toolbar_title.setPadding(0, 0, 40, 0);
        Log.e("toolbar", title);
        product_list = (Spinner) findViewById(R.id.sp_avilable_size);
        alcategoryImage = new ArrayList<>(Arrays.asList(R.mipmap.headphone, R.mipmap.powerbank, R.mipmap.footwear, R.mipmap.watch, R.mipmap.mobiles));
        alproductname = new ArrayList<>(Arrays.asList("HeadPhone black color", "Powerbank", "footwear stylish", "Rolex Watch", "Lg s5 (Black,16GB)"));

        mLayoutManager_relatedproduct = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mLayoutManager_intrestedproduct = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mLayoutManager_recycleview_image = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


        Intent get = getIntent();
        product_id = get.getStringExtra("product_id");
        Log.e("product_id ", product_id);

        if (!cc.isConnectingToInternet()) {
            cc.showToast(getString(R.string.no_internet));

        } else {
            ProductDetailWS();
        }

        List<String> categories = new ArrayList<String>();
        categories.add("Select Quantity");
        categories.add("1");
        categories.add("2");
        categories.add("3");
        categories.add("4");
        categories.add("5");
        categories.add("6");
        categories.add("7");
        categories.add("8");
        categories.add("9");
        categories.add("10");


        Log.e("WS", "Runnning");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_avilable_quanty.setAdapter(dataAdapter);


        iv_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate()) {
                    addtocartws(product_id);
                }
            }
        });


        iv_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addProductInWishList(product_id);
            }

        });

        iv_shareing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bitmapDrawable = (BitmapDrawable) iv_product_image.getDrawable();// get the from imageview or use your drawable from drawable folder
                bitmap1 = bitmapDrawable.getBitmap();
                String imgBitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap1, "title", null);
                Uri imgBitmapUri = Uri.parse(imgBitmapPath);
                String shareText = "Share image and text";
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("*/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);
                shareIntent.putExtra(Intent.EXTRA_TEXT, productName + "\n" + share_url);

                startActivity(Intent.createChooser(shareIntent, "Share Wallpaper using"));
            }

        });


    }

    private void clickListner() {

        btn_add_cart.setOnClickListener(this);

        btn_add_to_wishlist.setOnClickListener(this);

        img_back_btn.setOnClickListener(this);
        iv_shareing.setOnClickListener(this);
        sp_avilable_quanty.setOnItemSelectedListener(this);


    }

    private void init() {
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        iv_cart = (ImageView) findViewById(R.id.iv_cart);
        iv_wishlist = (ImageView) findViewById(R.id.iv_wishlist);
        iv_shareing = (ImageView) findViewById(R.id.iv_shareing);
        iv_product_image = (ImageView) findViewById(R.id.iv_product_image);
        iv_product_banner = (ImageView) findViewById(R.id.iv_product_banner);
        btn_add_cart = (Button) findViewById(R.id.btn_add_cart);
        btn_add_to_wishlist = (Button) findViewById(R.id.btn_add_to_wishlist);
        img_back_btn = (LinearLayout) findViewById(R.id.img_back_btn);
        recycleview_image = (RecyclerView) findViewById(R.id.recycleview_image);
        recycleview_relatedproduct = (RecyclerView) findViewById(R.id.recycleview_relatedproduct);
        recycleview_intrestedproduct = (RecyclerView) findViewById(R.id.recycleview_intrestedproduct);

        tv_produxt_name = (TextView) findViewById(R.id.tv_produxt_name);
        tv_product_id = (TextView) findViewById(R.id.tv_product_id);
        tv_product_review = (TextView) findViewById(R.id.tv_product_review);
        tv_product_rating = (TextView) findViewById(R.id.tv_product_rating);
        tv_product_price = (TextView) findViewById(R.id.tv_product_price);
        tv_produxt_instock = (TextView) findViewById(R.id.tv_produxt_instock);
        tv_product_realted_tag = (TextView) findViewById(R.id.tv_product_realted_tag);
        tv_product_intrested_tag = (TextView) findViewById(R.id.tv_product_intrested_tag);
        ln_spinner_gone = (LinearLayout) findViewById(R.id.ln_spinner_gone);
        mProgressBar = (ProgressBar) findViewById(R.id.pgb_image_loading);
        mProgressBar1 = (ProgressBar) findViewById(R.id.pgb_image_loading1);

        rv_review_1 = (RecyclerView) findViewById(R.id.rv_review_1);
        v_price_check = findViewById(R.id.v_price_check);
        tv_product_special_price = findViewById(R.id.tv_product_special_price);
        tv_product_brand = findViewById(R.id.tv_product_brand);
        rv_product_specification = findViewById(R.id.rv_product_specification);
        tv_product_description = findViewById(R.id.tv_product_description);
        tv_bottom_rating = findViewById(R.id.tv_bottom_rating);
        tv_bottom_review_text = findViewById(R.id.tv_bottom_review_text);

        pb_five_rating = findViewById(R.id.pb_five_rating);
        pb_four_rating = findViewById(R.id.pb_four_rating);
        pb_three_rating = findViewById(R.id.pb_three_rating);
        pb_two_rating = findViewById(R.id.pb_two_rating);
        pb_one_rating = findViewById(R.id.pb_one_rating);

        tv_five_rating = (TextView) findViewById(R.id.tv_five_rating);
        tv_four_rating = (TextView) findViewById(R.id.tv_four_rating);
        tv_three_rating = (TextView) findViewById(R.id.tv_three_rating);
        tv_two_rating = (TextView) findViewById(R.id.tv_two_rating);
        tv_one_rating = (TextView) findViewById(R.id.tv_one_rating);


        sp_avilable_quanty = (Spinner) findViewById(R.id.sp_avilable_quanty);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_add_cart:
                if (isValidate()) {
                    addtocartws(product_id);
                }


                break;


            case R.id.btn_add_to_wishlist:

                addProductInWishList(product_id);

                break;

            case R.id.img_back_btn:

                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                break;


        }


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.sp_avilable_quanty:
                qty = adapterView.getItemAtPosition(i).toString();
                Log.e("qty", qty);
                break;

        }

    }

    public void onNothingSelected(AdapterView<?> arg0) {


    }

    private void addtocartws(final String ProductId) {

        pDialog = new ProgressDialog(ProductDetailActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.add_to_cart,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:addtocart", response);
                        if (pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        try {
                            JSONObject jObject = new JSONObject(response);

                            if (jObject.getString("status").equals("200")) {

                                cc.showToast("Product added in to Cart");

                            } else if (jObject.getString("status").equals("400")) {

                                JSONObject jObject1 = new JSONObject(response);
                                JSONObject jObject2 = jObject1.getJSONObject("error");
                                JSONObject jObject3 = jObject2.getJSONObject("option");
                                erro_message = jObject3.getString("328");
                                Log.e("error_message", erro_message);
                                cc.showToast(erro_message);
                                Intent intentProductDetail = new Intent(ProductDetailActivity.this, ProductDetailActivity.class);
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
                error.printStackTrace();
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Log.e("errror_cart", error + "");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put("product_id", ProductId);
                map.put("quantity", qty);
                map.put(options, String.valueOf(id));
                Log.e("@@requestcart", map.toString());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("1811-token", cc.loadPrefString("1811-token"));
                headers.put("Cookie", "PHPSESSID=" + cc.loadPrefString3("PHPSESSID") + ";" + "default=" + cc.loadPrefString3("default"));
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    private void addProductInWishList(final String product_id)

    {

        pDialog = new ProgressDialog(ProductDetailActivity.this);
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
                            if (pDialog.isShowing()) {
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
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Log.e("errror_wishlist", error + "");
            }
        }) {

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
                headers.put("1811-token", cc.loadPrefString("1811-token"));
                headers.put("Cookie", "PHPSESSID=" + cc.loadPrefString3("PHPSESSID") + ";" + "default=" + cc.loadPrefString3("default"));
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    private void ProductDetailWS() {
        pDialog = new ProgressDialog(ProductDetailActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.Productlisting,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:product", response);
                        JSONArray jsonArray = null;
                        JSONArray jsonArray1 = null;
                        JSONArray jsonimagearray = null;
                        JSONArray product_size = null;
                        JSONArray product_size_array = null;
                        try {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            productSizes = new ArrayList<ProductSize>();
                            spiner_data = new ArrayList<String>();

                            JSONObject jObject = new JSONObject(response);

                            if (jObject.getString("status").equals("200")) {

                                relatedProductModels = new ArrayList<>();
                                intrestedCategoryModels = new ArrayList<>();
                                productimageslisting = new ArrayList<>();

                                listRatingCsm = new ArrayList<>();
                                listRatingCsm1 = new ArrayList<>();
                                listAttributeGroup = new ArrayList<>();
                                listUserReview = new ArrayList<>();


                                manufacturer = jObject.getString("manufacturer");
                                productName = jObject.getString("name");
                                productModel = jObject.getString("model");
                                productRewards = jObject.getString("reward");
                                productPoints = jObject.getString("points");
                                productDescription = jObject.getString("description");

                                product_review = jObject.getString("reviews");
                                rating = jObject.getString("rating");
                                price = jObject.getString("price");
                                special = jObject.getString("special");
                                yousave = jObject.getString("yousave");
                                weight = jObject.getString("weight");
                                tax = jObject.getString("tax");

                                share_url = jObject.getString("share_product");

                                Log.e("share_url", share_url);

                                productimage = jObject.getString("thumb");
                                product_id = jObject.getString("product_id");
                                productimage_banner = jObject.getString("cat_img_url");
                                stock = jObject.getString("stock");

                                jsonimagearray = jObject.getJSONArray("images");
                                product_size = jObject.getJSONArray("options");

                                if (product_size.length() == 0) {


                                    ln_spinner_gone.setVisibility(View.GONE);
                                    cc.savePrefString2("message", "notrequire");

                                    msgs = cc.loadPrefString2("message");

                                    Log.e("Gone", "Gone");
                                } else {
                                    spiner_data.add(("Select Color"));
                                    Log.e("NotGone", "NotGone");
                                }

                                for (int k = 0; k < product_size.length(); k++) {
                                    JSONObject jsonObject = product_size.getJSONObject(k);
                                    String color = jsonObject.getString("name");
                                    Log.e("Color", color);

                                    String product_option_ids = jsonObject.getString("product_option_id");
                                    Log.e("product_option_ids", product_option_ids);
                                    options = options_name + "[" + product_option_ids + "]";
                                    Log.e("OptionArray", options);
                                    product_size_array = jsonObject.getJSONArray("product_option_value");
                                    for (int a = 0; a < product_size_array.length(); a++) {


                                        ProductSize productSize = new ProductSize();
                                        JSONObject jsonObject1 = product_size_array.getJSONObject(a);

                                        productSize.setProduct_size_name(jsonObject1.getString("name"));
                                        productSize.setProduct_option_value_id(jsonObject1.getString("option_value_id"));
                                        productSize.setProductsize_option_value_id(jsonObject1.getString("product_option_value_id"));
                                        productSize.setProduct_size_price(jsonObject1.getString("price"));

                                        String price = jsonObject1.getString("price");

//
                                        tv_product_price.setText(price);
                                        productSizes.add(productSize);
                                        spiner_data.add(jsonObject1.optString("name"));

                                    }
                                }

                                if (jsonimagearray != null) {
                                    if (jsonimagearray.length() > 0) {
                                        for (int j = 0; j < jsonimagearray.length(); j++) {

                                            ProductImagesData productimages = new ProductImagesData();
                                            JSONObject jsonObject = jsonimagearray.getJSONObject(j);
                                            productimages.setImages(jsonObject.getString("thumb"));
                                            productimageslisting.add(productimages);
                                            Log.e("THumb", jsonObject.getString("thumb"));

                                        }
                                    }
                                }

                                jsonArray1 = jObject.getJSONArray("products_ymai");

                                if (jsonArray1 != null) {
                                    if (jsonArray1.length() > 0) {
                                        for (int j = 0; j < jsonArray1.length(); j++) {

                                            IntrestedCategoryData tm = new IntrestedCategoryData();
                                            JSONObject jsonObject = jsonArray1.getJSONObject(j);
                                            tm.setImageUrl(jsonObject.getString("thumb"));
                                            tm.setSharing_url(jsonObject.getString("shareProductUrl"));
                                            tm.setModel(jsonObject.getString("model"));
                                            tm.setProductname(jsonObject.getString("name"));
                                            tm.setSpecial(jsonObject.getString("special"));
                                            tm.setPrice(jsonObject.getString("price"));
                                            tm.setProduct_id(jsonObject.getString("product_id"));
                                            tm.setTax(jsonObject.getString("tax"));


                                            if (tm.getSpecial().equals("false")) {
                                                Log.e("@@Special", "False");
                                            }

//                                            if (tokens.isEmpty()) {
//                                                if (dbHelper.ProductExist(tm.getProduct_id() + "")) {
//                                                    tm.setIswishlist(true);
//                                                } else {
//                                                    tm.setIswishlist(false);
//                                                }
//                                            } else {
                                            tm.setIswishlist(jsonObject.getBoolean("iswishlist"));
//                                            }

                                            tm.setInCart(jsonObject.getBoolean("isInCart"));

                                            intrestedCategoryModels.add(tm);
                                            Log.e("image", jsonObject.getString("thumb"));
                                            Log.e("iswishlist", jsonObject.getString("iswishlist"));
                                            Log.e("isInCart", jsonObject.getString("isInCart"));
                                        }


                                    }


                                    if (intrestedCategoryModels.size() > 0) {
                                        mAdapter = new ProductInterstedAdapter(ProductDetailActivity.this, intrestedCategoryModels);
                                        recycleview_intrestedproduct.setLayoutManager(mLayoutManager_intrestedproduct);
                                        recycleview_intrestedproduct.setAdapter(mAdapter);

                                    } else {
                                        Log.e("nothing", "ProductInterstedAdapter..GONE");
                                        tv_product_intrested_tag.setVisibility(View.GONE);
                                    }

                                }

                                //Related Product
                                jsonArray = jObject.getJSONArray("relatedProducts");

                                if (jsonArray != null) {
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            RelatedProductData rm = new RelatedProductData();
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            rm.setSharing_url(jsonObject.getString("shareProductUrl"));
                                            rm.setImageUrl(jsonObject.getString("thumb"));
                                            rm.setProductname(jsonObject.getString("name"));
                                            rm.setPrice(jsonObject.getString("price"));
                                            rm.setProduct_id(jsonObject.getString("product_id"));
                                            rm.setSpecial(jsonObject.getString("special"));
                                            rm.setModel(jsonObject.getString("model"));
                                            rm.setTax(jsonObject.getString("tax"));

                                            if (rm.getSpecial().equals("false")) {
                                                Log.e("@@Special", "False");
                                            }

                                            if (tokens.isEmpty()) {
//                                            if (dbHelper.ProductExist(rm.getProduct_id() + "")) {
//                                                rm.setIswishlist(true);
//                                            } else {
//                                                rm.setIswishlist(false);
//                                            }
                                            } else {
                                                rm.setIswishlist(jsonObject.getBoolean("iswishlist"));
                                            }

                                            rm.setInCart(jsonObject.getBoolean("isInCart"));
                                            relatedProductModels.add(rm);
                                            Log.e("image", jsonObject.getString("thumb"));
                                        }
                                    }
                                }

                                JSONArray jsonArrayAttribute = jObject.getJSONArray("attribute_groups");

                                if (jsonArrayAttribute != null) {

                                    Log.e("@@AttrLen", jsonArrayAttribute.length() + "");
                                    if (jsonArrayAttribute.length() > 0) {

                                        for (int i = 0; i < jsonArrayAttribute.length(); i++) {

                                            ArrayList<Attribute> attributes = new ArrayList<>();
                                            JSONObject jAObject = jsonArrayAttribute.getJSONObject(i);

                                            AttributeGroups attributeGroups = new AttributeGroups();

                                            attributeGroups.setAttribute_group_id(jAObject.getString("attribute_group_id"));
                                            attributeGroups.setName(jAObject.getString("name"));

                                            JSONArray jArray = jAObject.getJSONArray("attribute");
                                            if (jArray != null) {

                                                if (jArray.length() > 0) {

                                                    for (int j = 0; j < jArray.length(); j++) {

                                                        JSONObject jAttrO = (JSONObject) jArray.get(j);
                                                        Attribute attribute = new Attribute();
                                                        attribute.setName(jAttrO.getString("name"));
                                                        attribute.setAttribute_id(jAttrO.getString("attribute_id"));
                                                        attribute.setText(jAttrO.getString("text"));
                                                        attributes.add(attribute);
                                                    }
                                                }
                                                attributeGroups.setAttribute(attributes);
                                            }

                                            listAttributeGroup.add(attributeGroups);
                                        }
                                    }
                                }


                                JSONArray jsonArrayCsm = jObject.getJSONArray("rating_csm");
                                for (int i = 0; i < jsonArrayCsm.length(); i++) {
                                    listRatingCsm.add(jsonArrayCsm.getInt(i));
                                }

                                Log.e("@@csm", listRatingCsm.size() + "");

                                JSONArray jsonArrayCsm1 = jObject.getJSONArray("rating_csm1");
                                for (int i = 0; i < jsonArrayCsm1.length(); i++) {
                                    listRatingCsm1.add(jsonArrayCsm1.getInt(i));
                                }

                                Log.e("@@csm1", listRatingCsm1.size() + "");


                                JSONArray jArrayUserReview = jObject.getJSONArray("reviews1");

                                if (jArrayUserReview != null)

                                    if (jArrayUserReview.length() > 0) {

                                        for (int i = 0; i < jArrayUserReview.length(); i++) {

                                            JSONObject jUserReviewObject = jArrayUserReview.getJSONObject(i);

                                            UserReview userReview = new UserReview();
                                            userReview.setAuthor(jUserReviewObject.getString("author"));
                                            userReview.setRating(jUserReviewObject.getString("rating"));
                                            userReview.setText(jUserReviewObject.getString("text"));
                                            userReview.setDate_added(jUserReviewObject.getString("date_added"));
                                            listUserReview.add(userReview);
                                        }
                                    }


                                setUpLayout();


                            } else if (jObject.getString("status").equals("400")) {
                                cc.showToast(jObject.getString("message"));
                            }


                            product_list.setAdapter(new ArrayAdapter<String>(ProductDetailActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, spiner_data));
                            product_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> arg0,
                                                           View arg1, int position, long arg3) {

                                    if (position > 0) {
/*vishal*/
                                        ProductSize countryListings = new ProductSize();

                                        colorName = productSizes.get(position - 1).getProduct_size_name();
                                        id = Integer.parseInt(productSizes.get(position - 1).getProductsize_option_value_id());
                                        cc.savePrefString("option_value_id1", productSizes.get(position - 1).getProduct_option_value_id());

                                        Log.e("COLORNAME", colorName);

                                        if (colorName.equals("Select Color")) {

//                                            cc.savePrefString("product_option_value_id1","");

                                            Log.e("COLORNAME", "...........if");

                                        } else {

                                            Log.e("COLORNAME", "...........else");
                                        }

                                        if (!cc.isConnectingToInternet()) {
                                            cc.showToast(getString(R.string.no_internet));

                                        } else {
                                            Log.e("option_value_id", productSizes.get(position - 1).getProduct_option_value_id());
                                            Log.e("product_option_value_id", productSizes.get(position - 1).getProductsize_option_value_id());
                                        }

                                    } else {

                                        Log.e("Nothings", ".........");

                                        colorName = null;
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
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                error.printStackTrace();
                Log.e("Error_ProductDetail", error.toString());

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put("product_id", product_id);


                return map;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("1811-token", cc.loadPrefString("1811-token"));
                headers.put("Cookie", "PHPSESSID=" + cc.loadPrefString3("PHPSESSID") + ";" + "default=" + cc.loadPrefString3("default"));
//                headers.put("language","en-gb");
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    private void setUpLayout() {

        if (productimage.equalsIgnoreCase("")) {
            Picasso.with(ProductDetailActivity.this).load(R.drawable.no_media).into(iv_product_image);
            mProgressBar.setVisibility(View.GONE);
        } else {

            Picasso.with(ProductDetailActivity.this).load(productimage).into(iv_product_image, new Callback() {
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


        // Product Image List
        if (productimageslisting.size() > 0) {

            pDialog.dismiss();
            recycleview_image.setLayoutManager(mLayoutManager_recycleview_image);
            mAdapter = new Productthumbimage(ProductDetailActivity.this, productimageslisting);
            recycleview_image.setLayoutManager(mLayoutManager_recycleview_image);
            recycleview_image.setAdapter(mAdapter);

        } else {
            Log.e("nothing", "ProductInterstedAdapter..GONE");
            tv_product_intrested_tag.setVisibility(View.GONE);
        }

        tv_produxt_name.setText(productName);
        tv_produxt_instock.setText(stock);

        if (rating.equals("")) {
            tv_product_rating.setText("0");
            tv_bottom_rating.setText("0");
        } else {
            tv_product_rating.setText(rating);
            tv_bottom_rating.setText(rating);
        }

        if (product_review.equals("")) {
            tv_product_review.setText("0 reviews");
            tv_bottom_review_text.setText("0 reviews");
        } else {
            tv_product_review.setText(product_review);
            tv_bottom_review_text.setText(product_review);
        }

        tv_product_price.setText(price);
        if (special.equals("false")) {
            v_price_check.setVisibility(View.GONE);
            tv_product_special_price.setVisibility(View.GONE);
        } else {
            tv_product_special_price.setVisibility(View.VISIBLE);
            v_price_check.setVisibility(View.VISIBLE);
            tv_product_special_price.setText(special);
        }

        if (manufacturer != null) {
            if (manufacturer.equals("")) {
                tv_product_brand.setText("Not Available");
            } else {
                tv_product_brand.setText(manufacturer);
            }
        }

        if (productModel != null) {
            if (productModel.equals("")) {
                tv_product_id.setText("Not Available");
            } else {
                tv_product_id.setText(productModel);
            }
        }

        if (listAttributeGroup != null) {
            if (listAttributeGroup.size() > 0) {
                LinearLayoutManager llm_ps = new LinearLayoutManager(ProductDetailActivity.this);
                ProductSpecificationAdapter specificationAdapter = new ProductSpecificationAdapter(ProductDetailActivity.this, listAttributeGroup);
                rv_product_specification.setLayoutManager(llm_ps);
                rv_product_specification.setAdapter(specificationAdapter);
            }
        }

        if (productDescription != null) {
            if (productDescription.equals("")) {
                tv_product_description.setText("No Description Available");
            } else {


                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    tv_product_description.setText(Html.fromHtml(productDescription));
                } else {
                    tv_product_description.setText(Html.fromHtml(productDescription, Html.FROM_HTML_MODE_COMPACT));
                }

            }
        }

        if (relatedProductModels.size() > 0) {
            pDialog.dismiss();
            recycleview_relatedproduct.setLayoutManager(mLayoutManager_relatedproduct);
            mAdapter = new ProductRelatedProduct(ProductDetailActivity.this, relatedProductModels);
            recycleview_relatedproduct.setLayoutManager(mLayoutManager_relatedproduct);
            recycleview_relatedproduct.setAdapter(mAdapter);
            Log.e("if.............nothing", "ProductRelatedProduct..Not..GONE");
        } else {
            Log.e("else...........nothing", "ProductRelatedProduct..GONE");
            tv_product_realted_tag.setVisibility(View.GONE);
        }


        if (productimage_banner.equalsIgnoreCase("")) {
            Picasso.with(ProductDetailActivity.this).load(R.drawable.no_media).into(iv_product_banner);
            mProgressBar1.setVisibility(View.GONE);
        } else {

            Picasso.with(ProductDetailActivity.this).load(productimage_banner).into(iv_product_banner, new Callback() {
                @Override
                public void onSuccess() {
                    mProgressBar1.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    mProgressBar1.setVisibility(View.GONE);
                }
            });
        }


        if (listRatingCsm != null) {
            if (listRatingCsm.size() > 0) {
                for (int i = 0; i < listRatingCsm.size(); i++) {

                    if (i == 0) {
                        tv_one_rating.setText(String.valueOf(listRatingCsm.get(i)));
                    } else if (i == 1) {
                        tv_two_rating.setText(String.valueOf(listRatingCsm.get(i)));
                    } else if (i == 2) {
                        tv_three_rating.setText(String.valueOf(listRatingCsm.get(i)));
                    } else if (i == 3) {
                        tv_four_rating.setText(String.valueOf(listRatingCsm.get(i)));
                    } else if (i == 4) {
                        tv_five_rating.setText(String.valueOf(listRatingCsm.get(i)));
                    }
                }
            }
        }


        if (listRatingCsm1 != null) {
            if (listRatingCsm1.size() > 0) {
                for (int i = 0; i < listRatingCsm1.size(); i++) {
                    if (i == 0) {
                        pb_one_rating.setProgress(listRatingCsm1.get(i));
                    } else if (i == 1) {
                        pb_two_rating.setProgress(listRatingCsm1.get(i));
                    } else if (i == 2) {
                        pb_three_rating.setProgress(listRatingCsm1.get(i));
                    } else if (i == 3) {
                        pb_four_rating.setProgress(listRatingCsm1.get(i));
                    } else if (i == 4) {
                        pb_five_rating.setProgress(listRatingCsm1.get(i));
                    }
                }
            }
        }

        if (listUserReview != null) {

            if (listUserReview.size() > 0) {
                LinearLayoutManager llm = new LinearLayoutManager(ProductDetailActivity.this);
                UserReviewAdapter reviewAdapter =
                        new UserReviewAdapter(ProductDetailActivity.this, listUserReview);

                rv_review_1.setLayoutManager(llm);
                rv_review_1.setAdapter(reviewAdapter);
            }
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public boolean isValidate() {

        String msg = null;

        Log.e("NotColor", msgs);

        if (qty.equalsIgnoreCase("Select Quantity")) {

            Log.e("Click", "Able");

            msg = getString(R.string.qty);

        } else if (colorName == null) {

            if (msgs.equals("notrequire")) {


            } else {

                msg = getString(R.string.select_color);

            }
        }

        if (msg != null) {
            AndyUtils.showToast(ProductDetailActivity.this, msg);
            return false;
        }
        if (msg == null) {
            return true;
        }

        AndyUtils.showToast(ProductDetailActivity.this, msg);
        return false;


    }

    public class Productthumbimage extends RecyclerView.Adapter<Productthumbimage.ViewHolder> {


        ArrayList<Integer> alcategoryImage;
        ArrayList<String> alName;
        ArrayList<String> alproductname;
        Context context;
        List<ProductImagesData> prosuctsimage;
        ProgressBar mProgressBar;
        CommanClass cc;
        ViewGroup vv;

        public Productthumbimage(ProductDetailActivity productDetailActivity, List<ProductImagesData> productimageslisting) {
            this.context = productDetailActivity;
            this.prosuctsimage = productimageslisting;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            cc = new CommanClass(context);

            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.prductthumb, viewGroup, false);


            final ViewHolder viewHolder = new ViewHolder(v);

            vv = viewGroup;

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int i) {

            if (i == 0) {

                viewHolder.top_layout.setBackground(getResources().getDrawable(R.drawable.purple_btn_bg));
            }

            if (prosuctsimage.size() != 0) {
                Picasso.with(context).load(prosuctsimage.get(i).getImages()).into(viewHolder.imgThumbnail, new Callback() {
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

            viewHolder.top_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//
//                    for (int i=0;i<productimageslisting.size();i++){
//
//
//                    }

                    for (int i = 0; i < vv.getChildCount(); i++) {

                        View c = vv.getChildAt(i);

                        c.findViewById(R.id.top_layout).setBackgroundResource(R.drawable.sqaure_background_btn);


                    }

                    getItemPos(viewHolder.getAdapterPosition());
                    viewHolder.top_layout.setBackground(getResources().getDrawable(R.drawable.purple_btn_bg));


                    ProductImagesData cm = prosuctsimage.get(viewHolder.getAdapterPosition());
                    Log.e("imgepostion", cm.getImages());

                    String imges = cm.getImages();

                    viewHolder.top_layout.setBackground(getResources().getDrawable(R.drawable.purple_btn_bg));


                }
            });


        }

        @Override
        public int getItemCount() {
            return prosuctsimage.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView imgThumbnail, iv_recoomented_cart, iv_recoomented_wishlis;
            public TextView tvSpecies, tv_price;
            LinearLayout top_layout;

            public ViewHolder(final View itemView) {
                super(itemView);

                imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
                mProgressBar = (ProgressBar) itemView.findViewById(R.id.pgb_image_loading);
                top_layout = (LinearLayout) itemView.findViewById(R.id.top_layout);


            }

        }

        private void getItemPos(int itemPos) {

            ProductImagesData cm = prosuctsimage.get(itemPos);
            Log.e("imgepostion", cm.getImages());

            String imges = cm.getImages();


            if (imges.equalsIgnoreCase("")) {

                Picasso.with(ProductDetailActivity.this).load(R.drawable.no_media).into(iv_product_image);
                mProgressBar.setVisibility(View.GONE);

            } else {

                Picasso.with(ProductDetailActivity.this).load(imges).into(iv_product_image, new Callback() {

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
        }
    }

    public class ProductInterstedAdapter extends RecyclerView.Adapter<ProductInterstedAdapter.ViewHolder> {


        ArrayList<Integer> alcategoryImage;
        ArrayList<String> alName;
        ArrayList<String> alproductname;
        Context context;
        List<IntrestedCategoryData> intrestedCategoryModels;
        ProgressBar mProgressBar;
        CommanClass cc;
        private BitmapDrawable bitmapDrawable1;
        private Bitmap bitmap11;
        ProgressDialog pDialog;
        String res = "";
        String json_str = "";
        String status_code_signup = "";
        String status_message_signup = "";
        private LayoutInflater inflater;


        public ProductInterstedAdapter(ProductDetailActivity productDetailActivity, List<IntrestedCategoryData> intrestedCategoryModels) {

            this.context = productDetailActivity;
            this.intrestedCategoryModels = intrestedCategoryModels;
            cc = new CommanClass(context);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {


            View v = inflater.inflate(R.layout.recommendedproduct, viewGroup, false);
            final ViewHolder viewHolder = new ViewHolder(v);

//
//            viewHolder.tvSpecies.setText(intrestedCategoryModels.get(i).getProductname());
//            viewHolder.tv_price.setText(intrestedCategoryModels.get(i).getPrice());
//            tokens = cc.loadPrefString("1811-token");
//
//            if (intrestedCategoryModels.size()!=0){
//                Picasso.with(context).load(intrestedCategoryModels.get(i).getImageUrl()).into(viewHolder.imgThumbnail, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        mProgressBar.setVisibility(View.GONE);
//                    }
//                    @Override
//                    public void onError() {
//                        mProgressBar.setVisibility(View.GONE);
//                    }
//                });
//
//            }else {
//                Picasso.with(context).load(R.drawable.no_media).into(viewHolder.imgThumbnail);
//                mProgressBar.setVisibility(View.GONE);
//            }

            viewHolder.imgThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getPositionItem1(viewHolder.getAdapterPosition());

                    Intent intentProductDetail = new Intent(context, ProductDetailActivity.class);
                    intentProductDetail.putExtra("product_id", cc.loadPrefString("productid"));

                    String product_id = cc.loadPrefString("productid");

                    context.startActivity(intentProductDetail);

                    Log.e("productidholder", viewHolder.getAdapterPosition() + "");
                    Log.e("productidholder1", product_id);

                }
            });


            viewHolder.iv_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    IntrestedCategoryData rm = intrestedCategoryModels.get(i);

                    String product_name = rm.getProductname();
                    String product_link = rm.getSharing_url();

                    String image_url = rm.getImageUrl();

                    bitmapDrawable1 = (BitmapDrawable) viewHolder.imgThumbnail.getDrawable();// get the from imageview or use your drawable from drawable folder
                    bitmap11 = bitmapDrawable1.getBitmap();
                    String imgBitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap11, "title", null);
                    Uri imgBitmapUri = Uri.parse(imgBitmapPath);
                    String shareText = "Share image and text";
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("*/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, product_name + "\n" + product_link);

                    context.startActivity(Intent.createChooser(shareIntent, "Share Wallpaper using"));

                }

            });


            viewHolder.iv_recoomented_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


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

                        if (intrestedCategoryModels.get(viewHolder.getAdapterPosition()).iswishlist()) {
                            intrestedCategoryModels.get(viewHolder.getAdapterPosition()).setIswishlist(false);
                            viewHolder.iv_recoomented_wishlist.setImageResource(R.mipmap.faviourite);

                            Log.e("WishList", "False");


                        } else {
                            intrestedCategoryModels.get(viewHolder.getAdapterPosition()).setIswishlist(true);
                            viewHolder.iv_recoomented_wishlist.setImageResource(R.mipmap.nav_wish);

                            Log.e("WishList", "True");

                        }
                        getPositionItem(viewHolder.getAdapterPosition(), intrestedCategoryModels.get(viewHolder.getAdapterPosition()).iswishlist());

                    }


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
                                if (pDialog.isShowing()) {
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
                    if (pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Log.e("errror_wishlist", error + "");
                }

            }) {
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
                    headers.put("Cookie", "PHPSESSID=" + cc.loadPrefString3("PHPSESSID") + ";" + "default=" + cc.loadPrefString3("default"));
                    return headers;
                }
            };
            AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            if (intrestedCategoryModels.get(i).iswishlist()) {
                viewHolder.iv_recoomented_wishlist.setImageResource(R.mipmap.nav_wish);
            } else {
                viewHolder.iv_recoomented_wishlist.setImageResource(R.mipmap.faviourite);
            }

            if (intrestedCategoryModels.get(i).getSpecial().equalsIgnoreCase("false")) {
                viewHolder.v_price_check.setVisibility(View.GONE);
                viewHolder.ll_offer_price.setVisibility(View.GONE);
            } else {
                viewHolder.v_price_check.setVisibility(View.VISIBLE);
                viewHolder.ll_offer_price.setVisibility(View.VISIBLE);
                viewHolder.tv_offer_price.setText(intrestedCategoryModels.get(i).getSpecial());
            }

            viewHolder.tvSpecies.setText(intrestedCategoryModels.get(i).getProductname());
            viewHolder.tv_price.setText(intrestedCategoryModels.get(i).getPrice());

            if (intrestedCategoryModels.size() != 0) {
                Picasso.with(context).load(intrestedCategoryModels.get(i).getImageUrl()).into(viewHolder.imgThumbnail, new Callback() {
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
            return intrestedCategoryModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView imgThumbnail, iv_recoomented_cart, iv_recoomented_wishlist, iv_share;
            public TextView tvSpecies, tv_price, tv_offer_price;

            View v_price_check;

            public LinearLayout ln_view_category_mainscreen, ll_offer_price;

            public ViewHolder(final View itemView) {
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
            IntrestedCategoryData nm = intrestedCategoryModels.get(adapterPosition);
            nm.getProduct_id();

            cc.savePrefString("productid", String.valueOf(nm.getProduct_id()));

            Log.e("prodctpositionid", nm.getImageUrl() + "");

        }

        public void addtocart(int adapterPosition) {

            IntrestedCategoryData rm = intrestedCategoryModels.get(adapterPosition);
            cc.savePrefString("productid", String.valueOf(rm.getProduct_id()));

            if (tokens.isEmpty()) {
//                sqLiteHelper.insertData(id, cart_product_name, cart_product_price, cart_product_imagepath);
                Log.e("Token", "Empty");
//                isAddToCartInProgress = false;
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
                    headers.put("Cookie", "PHPSESSID=" + cc.loadPrefString3("PHPSESSID") + ";" + "default=" + cc.loadPrefString3("default"));
                    return headers;
                }


            };

            AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

        }

        private void getPositionItem(int adapterPosition, boolean removeItem) {


            IntrestedCategoryData rm = intrestedCategoryModels.get(adapterPosition);
            cc.savePrefString("productid", String.valueOf(rm.getProduct_id()));

            if (tokens.isEmpty()) {

                Log.e("Token", "Empty");

//                if (removeItem) {
//                    dbHelper.InsertWishlistData(rm.getSharing_url(), rm.getProduct_id() + "", rm.getImageUrl()
//                            , rm.getProductname(), rm.getModel(), rm.getPrice(), rm.getSpecial(), rm.getTax());
//                   } else {
//                    dbHelper.RemoveProduct(rm.getProduct_id() + "");
                intrestedCategoryModels.remove(adapterPosition);
                mAdapter.notifyDataSetChanged();
//                }

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
                                if (pDialog.isShowing()) {
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
                    if (pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Log.e("error_wishlist", error + "");
                }

            }) {

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
                    headers.put("Cookie", "PHPSESSID=" + cc.loadPrefString3("PHPSESSID") + ";" + "default=" + cc.loadPrefString3("default"));
                    return headers;
                }
            };

            AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

        }

    }

}
