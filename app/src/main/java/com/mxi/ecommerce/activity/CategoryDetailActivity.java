package com.mxi.ecommerce.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.mxi.ecommerce.R;
import com.mxi.ecommerce.adapter.CategoryGridListingAdapter;
import com.mxi.ecommerce.comman.AppController;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.comman.ConnectionUrl;
import com.mxi.ecommerce.model.BrandParentData;
import com.mxi.ecommerce.model.CategoryBrandChildData;
import com.mxi.ecommerce.model.CategoryGidListingdata;
import com.mxi.ecommerce.model.CategoryListingdata;
import com.mxi.ecommerce.model.FilterParentCategoryData;
import com.mxi.ecommerce.model.FilterSubChildCategoryData;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryDetailActivity extends AppCompatActivity implements View.OnClickListener {

    CommanClass cc;
    ProgressDialog pDialog;

    Button sorting_list, category_filter;
    String title = "Category name";
    TextView tv_toolbar_title;

    List<CategoryListingdata> categoryListingdataList;
    List<CategoryGidListingdata> categoryGideListingdatas;


    //recycleview topdeal scrollview
    RecyclerView recycleview_listing_items;
    RecyclerView.LayoutManager mLayoutManager_listing_items;
    RecyclerView.Adapter mAdapter;

    LinearLayout ln_list, ln_gride;

    // Linearlayout button bootom menu
    LinearLayout ln_home_bottom, ln_category_bottom, ln_deal_bottom, ln_wishlist_bottom, ln_mycc_bottom;

    ImageView img_listing, img_gridelisting;

    LinearLayout img_back_btn;
    GridView grid;

    int position;

    String parent_product_id;
    String child_product_id;
    String name;
    String brand_id;

    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";
    String finalcategoryid;

    // Brand CAtegory
    ArrayList<CategoryBrandChildData> cb_list;
    BrandListAdapter ExpAdapter;
    FilterSubChildCategoryAdapters ExpAdapter1;
    ExpandableListView ExpandList, ExpandList1;
    LinearLayout ln_show_list_layout;


    // Subcategory demo
    ArrayList<FilterSubChildCategoryData> filter_subchild;
//    FilterSubChildCategoryAdapters ExpAdapter1;


    //Dialog Box data

    TextView tv_attributs_memory, tv_attributs_processers;
    TextView disocunt_maxvalue, disocunt_minvaluevalue;
    LinearLayout ln_pricing;
    LinearLayout ln_pricing1;
    LinearLayout ln_pricing2;
    LinearLayout ln_brandlist, ln_brandlist_view, ln_attributs_memory_view, ln_attributs_prosessor_view, iv_filter_close;
    LinearLayout ln_category_list, ln_pricre_layout;
    LinearLayout ln_attributs_memary, ln_attributs_prosessor;
    FrameLayout fm;


    RadioButton rb_attributes_memory, rb_attributes_processers;
    CrystalRangeSeekbar rangeSeekbar;
    Button btn_apply;
    Dialog dialog;


    TextView tvMin, tvMax;
    String min_price, max_price;
    boolean isGridViewOn = false;
    boolean isListViewOn = false;

    ImageView iv_null_image;

    String tokens;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        cc = new CommanClass(this);
        tokens = cc.loadPrefString("1811-token");


        isGridViewOn = true;
        isListViewOn = false;

        init();
        clickListner();

        Log.e("toolbar", title);

        mLayoutManager_listing_items = new LinearLayoutManager(this);
        recycleview_listing_items.setLayoutManager(mLayoutManager_listing_items);

        Intent get = getIntent();
        if (cc.loadPrefBoolean("withoutSubCategory")) {
            cc.savePrefBoolean("withoutSubCategory", false);
            parent_product_id = get.getStringExtra("categry_id");
            name = get.getStringExtra("name");

            finalcategoryid = parent_product_id;

        } else {

            parent_product_id = get.getStringExtra("categry_id");
            child_product_id = get.getStringExtra("sub_categry_id");
            name = get.getStringExtra("name");

            finalcategoryid = parent_product_id + "_" + child_product_id;

        }


        tv_toolbar_title.setText(name);
        tv_toolbar_title.setPadding(0, 0, 40, 0);

        Log.e("parent_product_id", parent_product_id);


        Log.e("sorting_id", finalcategoryid);


        Log.e("category_token", tokens);

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

    private void clickListner() {
        sorting_list.setOnClickListener(this);
        category_filter.setOnClickListener(this);


        //LinearLayout bottom

        ln_home_bottom.setOnClickListener(this);
        ln_deal_bottom.setOnClickListener(this);
        ln_wishlist_bottom.setOnClickListener(this);
        ln_mycc_bottom.setOnClickListener(this);
        ln_category_bottom.setOnClickListener(this);

        img_gridelisting.setOnClickListener(this);
        img_listing.setOnClickListener(this);

        img_back_btn.setOnClickListener(this);


    }

    private void init() {

        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        category_filter = (Button) findViewById(R.id.category_filter);
        sorting_list = (Button) findViewById(R.id.sorting_list);
        grid = (GridView) findViewById(R.id.grid);
        img_gridelisting = (ImageView) findViewById(R.id.img_gridelisting);
        img_listing = (ImageView) findViewById(R.id.img_listing);

        img_back_btn = (LinearLayout) findViewById(R.id.img_back_btn);


        //LinearLayout bottom


        ln_home_bottom = (LinearLayout) findViewById(R.id.ln_home_bottom);
        ln_category_bottom = (LinearLayout) findViewById(R.id.ln_category_bottom);
        ln_deal_bottom = (LinearLayout) findViewById(R.id.ln_deal_bottom);
        ln_mycc_bottom = (LinearLayout) findViewById(R.id.ln_mycc_bottom);
        ln_wishlist_bottom = (LinearLayout) findViewById(R.id.ln_wishlist_bottom);
        recycleview_listing_items = (RecyclerView) findViewById(R.id.recycleview_listing);
        ln_list = (LinearLayout) findViewById(R.id.ln_list);
        ln_gride = (LinearLayout) findViewById(R.id.ln_gride);

        img_listing.setColorFilter(getResources().getColor(R.color.color_purple));
        img_gridelisting.setColorFilter(getResources().getColor(R.color.colorAccent));


        iv_null_image = (ImageView) findViewById(R.id.iv_null_image);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.ln_home_bottom:


                Intent intentMainActivity = new Intent(CategoryDetailActivity.this, MainActivity.class);
                intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentMainActivity);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();


                break;

            case R.id.ln_category_bottom:

                Intent intentShopCategory = new Intent(CategoryDetailActivity.this, ShopCategoryActivity.class);
                startActivity(intentShopCategory);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ln_mycc_bottom:

                Intent intentMyAccount = new Intent(CategoryDetailActivity.this, MyAccountActivity.class);
                startActivity(intentMyAccount);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ln_wishlist_bottom:

                Intent intentWishList = new Intent(CategoryDetailActivity.this, WishlistActivity.class);
                startActivity(intentWishList);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ln_deal_bottom:


                Intent intentDeal = new Intent(CategoryDetailActivity.this, TopDealDetailActivity.class);
                startActivity(intentDeal);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.sorting_list:

                PopupMenu popup = new PopupMenu(CategoryDetailActivity.this, sorting_list);
                popup.getMenuInflater().inflate(R.menu.category_sortings_list, popup.getMenu());


                if (cc.loadPrefString("order").equals("DESC") && cc.loadPrefString("name").equals("rating")) {

                    MenuItem item = popup.getMenu().findItem(R.id.model_z_a);
                    item.setChecked(true);

//                    case R.id.model_z_a:
//
//                        String order = "DESC";
//                        String name = "rating";

                } else if (cc.loadPrefString("order").equals("DESC") && cc.loadPrefString("name").equals("model")) {

                    MenuItem item = popup.getMenu().findItem(R.id.model_a_z);
                    item.setChecked(true);

//                    case R.id.model_a_z:
//
//                        String order = "DESC";
//                        String name = "model";

                } else if (cc.loadPrefString("order").equals("ASC") && cc.loadPrefString("name").equals("model")) {

                    MenuItem item = popup.getMenu().findItem(R.id.rate_lowest);
                    item.setChecked(true);

//                    case R.id.rate_lowest:
//
//                        String order = "ASC";
//                        String name = "model";

                } else if (cc.loadPrefString("order").equals("ASC") && cc.loadPrefString("name").equals("rating")) {

                    MenuItem item = popup.getMenu().findItem(R.id.rate_highest);
                    item.setChecked(true);

//                    case R.id.rate_highest:
//
//                        String order = "ASC";
//                        String name = "rating";

                } else if (cc.loadPrefString("order").equals("ASC") && cc.loadPrefString("name").equals("price")) {


                    MenuItem item = popup.getMenu().findItem(R.id.price_l_h);
                    item.setChecked(true);

//                    case R.id.price_l_h:
//
//                        String order = "ASC";
//                        String name = "price";

                } else if (cc.loadPrefString("order").equals("DESC") && cc.loadPrefString("name").equals("price")) {

                    MenuItem item = popup.getMenu().findItem(R.id.price_H_L);
                    item.setChecked(true);

//                    case R.id.price_H_L:
//
//                        String order = "DESC";
//                        String name = "price";

                } else if (cc.loadPrefString("order").equals("DESC") && cc.loadPrefString("name").equals("name")) {

                    MenuItem item = popup.getMenu().findItem(R.id.name_z_to_a);
                    item.setChecked(true);

//                    case R.id.name_z_to_a:
//
//                        String order = "DESC";
//                        String name = "name";

                } else if (cc.loadPrefString("order").equals("ASC") && cc.loadPrefString("name").equals("name")) {


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
                                    cc.savePrefString("order", order);
                                    cc.savePrefString("name", name);

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
                                    cc.savePrefString("order", order);
                                    cc.savePrefString("name", name);

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
                                    cc.savePrefString("order", order);
                                    cc.savePrefString("name", name);

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

                                        cc.savePrefString("order", order);
                                        cc.savePrefString("name", name);

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
                                        cc.savePrefString("order", order);
                                        cc.savePrefString("name", name);

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
                                    cc.savePrefString("order", order);
                                    cc.savePrefString("name", name);

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
                                    cc.savePrefString("order", order);
                                    cc.savePrefString("name", name);

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
                                    cc.savePrefString("order", order);
                                    cc.savePrefString("name", name);

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
                                    cc.savePrefString("order", order);
                                    cc.savePrefString("name", name);

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


            case R.id.category_filter:

                showfilter();

                break;

            case R.id.img_gridelisting:
                if (isListViewOn) {

                    isGridViewOn = true;
                    isListViewOn = false;
                    recycleview_listing_items.setAdapter(null);
                    grid.setAdapter(null);

                    mAdapter = null;

                    categoryDetailWS();

                    ln_gride.setVisibility(View.VISIBLE);
                    ln_list.setVisibility(View.GONE);
                    img_listing.setColorFilter(getResources().getColor(R.color.color_purple));
                    img_gridelisting.setColorFilter(getResources().getColor(R.color.colorAccent));
                }

                break;

            case R.id.img_listing:

                if (isGridViewOn) {

                    isGridViewOn = false;
                    isListViewOn = true;

                    recycleview_listing_items.setAdapter(null);
                    grid.setAdapter(null);

                    mAdapter = null;

                    categoryDetailWS();

                    ln_list.setVisibility(View.VISIBLE);
                    ln_gride.setVisibility(View.GONE);

                    img_gridelisting.setColorFilter(getResources().getColor(R.color.color_purple));
                    img_listing.setColorFilter(getResources().getColor(R.color.colorAccent));
                }

                break;


            case R.id.img_back_btn:

                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                break;
        }
    }

    private void alert() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CategoryDetailActivity.this);
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

    private void showfilter() {

        if (!cc.isConnectingToInternet()) {
            cc.showToast(getString(R.string.no_internet));
        } else {
            filterparametrs();
        }

        String child = cc.loadPrefString1("brand_list");
        Log.e("select_child_bride", child);

        dialog = new Dialog(CategoryDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_filter);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        // Button btn_dialog_cancle=(Button)dialog.findViewById(R.id.btn_dialog_cancle);
        rangeSeekbar = (CrystalRangeSeekbar) dialog.findViewById(R.id.rangeSeekbar1);
        CrystalRangeSeekbar discountseekbar = (CrystalRangeSeekbar) dialog.findViewById(R.id.seekbar_discout);
        ExpandList = (ExpandableListView) dialog.findViewById(R.id.expan_list);
        ExpandList1 = (ExpandableListView) dialog.findViewById(R.id.expan_list_category);
        tv_attributs_memory = (TextView) dialog.findViewById(R.id.tv_attributs_memory);
        tv_attributs_processers = (TextView) dialog.findViewById(R.id.tv_attributs_processers);
        disocunt_minvaluevalue = (TextView) dialog.findViewById(R.id.discount_minvalue);
        disocunt_maxvalue = (TextView) dialog.findViewById(R.id.disocunt_maxvalue);
        iv_filter_close = (LinearLayout) dialog.findViewById(R.id.iv_filter_close);
        ln_brandlist = (LinearLayout) dialog.findViewById(R.id.ln_brandlist);
        ln_brandlist_view = (LinearLayout) dialog.findViewById(R.id.ln_brandlist_view);
        ln_attributs_prosessor_view = (LinearLayout) dialog.findViewById(R.id.ln_attributs_prosessor_view);
        ln_attributs_memory_view = (LinearLayout) dialog.findViewById(R.id.ln_attributs_memory_view);
        ln_category_list = (LinearLayout) dialog.findViewById(R.id.ln_category_list);
        ln_pricre_layout = (LinearLayout) dialog.findViewById(R.id.ln_pricre_layout);
        ln_attributs_memary = (LinearLayout) dialog.findViewById(R.id.ln_attributs_memary);
        ln_attributs_prosessor = (LinearLayout) dialog.findViewById(R.id.ln_attributs_prosessor);
        rb_attributes_memory = (RadioButton) dialog.findViewById(R.id.rb_attributes_memory);
        rb_attributes_processers = (RadioButton) dialog.findViewById(R.id.rb_attributes_processers);
        btn_apply = (Button) dialog.findViewById(R.id.btn_apply);


        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cc.isConnectingToInternet()) {
                    cc.showToast(getString(R.string.no_internet));
                } else {
                    filterdata();
                }


            }
        });


        tvMin = (TextView) dialog.findViewById(R.id.minvalue);
        tvMax = (TextView) dialog.findViewById(R.id.maxvalue);


        iv_filter_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

      /*  btn_dialog_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });*/
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                //  Log.e("value",min_price);

                tvMin.setText(String.valueOf(minValue));
                tvMax.setText(String.valueOf(maxValue));
            }
        });

        ln_attributs_memary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb_attributes_processers.setChecked(false);
                rb_attributes_memory.setChecked(true);


                String attributes = "Memory";
                cc.savePrefString1("attribute", attributes);


            }
        });

        ln_attributs_prosessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb_attributes_processers.setChecked(true);
                rb_attributes_memory.setChecked(false);

                String attributes = "Processor";
                cc.savePrefString1("attribute", attributes);


            }
        });


        rb_attributes_memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb_attributes_processers.setChecked(false);
                rb_attributes_memory.setChecked(true);

                String attributes = "Memory";
                cc.savePrefString1("attribute", attributes);
            }
        });

        rb_attributes_processers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb_attributes_processers.setChecked(true);
                rb_attributes_memory.setChecked(false);

                String attributes = "Processor";
                cc.savePrefString1("attribute", attributes);
            }
        });


        discountseekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                disocunt_minvaluevalue.setText(String.valueOf(minValue));
                disocunt_maxvalue.setText(String.valueOf(maxValue));
            }
        });


        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.e("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));


                int a = Integer.parseInt(String.valueOf(minValue));
                int b = Integer.parseInt(String.valueOf(maxValue));


                Log.e("minimum", a + "");


                cc.savePrefString1("min_prices", a + "");
                cc.savePrefString1("max_prices", b + "");

                Log.e("FinalMaxProse", a + "");
                Log.e("FinalMinPrice", b + "");


            }
        });


        discountseekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.e("DiscountCRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));

                int a = Integer.parseInt(String.valueOf(minValue));
                int b = Integer.parseInt(String.valueOf(maxValue));

                cc.savePrefString1("discount_min_price", a + "");
                cc.savePrefString1("discount_max_price", b + "");

                Log.e("FinalDiscountMaxProse", a + "");
                Log.e("FinalDiscountMinPrice", b + "");
            }
        });


        dialog.show();
    }

    //Filter Data
    private void filterdata() {

        pDialog = new ProgressDialog(CategoryDetailActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.FilterData,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:filterdata", response);
                        JSONArray jsonArray = null;

                        try {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }

                            JSONObject jObject = new JSONObject(response);

                            if (jObject.getString("status").equals("200")) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }

                                categoryListingdataList = new ArrayList<>();
                                categoryGideListingdatas = new ArrayList<>();

                                jsonArray = jObject.getJSONArray("products");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    CategoryListingdata categoryListingdata = new CategoryListingdata();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    categoryListingdata.setPrice(jsonObject.getString("price"));
                                    categoryListingdata.setThumb(jsonObject.getString("thumb"));
                                    categoryListingdata.setProduct_name(jsonObject.getString("name"));
                                    categoryListingdata.setProduct_id(jsonObject.getString("product_id"));
                                    categoryListingdata.setSpecial(jsonObject.getString("special"));
                                    categoryListingdata.setTax(jsonObject.getString("tax"));
                                    categoryListingdata.setModel(jsonObject.getString("model"));
                                    categoryListingdata.setInCart(jsonObject.getBoolean("isInCart"));

//                                    if (tokens.isEmpty()) {
//                                        if (dbHelper.ProductExist(categoryListingdata.getProduct_id() + "")) {
//                                            categoryListingdata.setIswishlist(true);
//                                        } else {
//                                            categoryListingdata.setIswishlist(false);
//                                        }
//                                    } else {
                                    categoryListingdata.setIswishlist(jsonObject.getBoolean("iswishlist"));
//                                    }
                                    Log.e("product_name", jsonObject.getString("name"));
                                    categoryListingdataList.add(categoryListingdata);
                                }

                                if (categoryListingdataList.size() > 0) {
                                    if (pDialog.isShowing()) {
                                        pDialog.dismiss();
                                    }

                                    mAdapter = new ShowCategoryListingAdapter(CategoryDetailActivity.this, categoryListingdataList);
                                    recycleview_listing_items.setAdapter(mAdapter);
                                    CategoryGridListingAdapter adapter = new CategoryGridListingAdapter(CategoryDetailActivity.this, categoryListingdataList);
                                    grid.setAdapter(adapter);
                                    iv_null_image.setVisibility(View.GONE);

                                } else {
                                    Log.e("nothingsss", "...........");

                                    iv_null_image.setVisibility(View.VISIBLE);


                                }

                                cc.logoutapp1();


                            } else if (jObject.getString("status").equals("400")) {

                                cc.showToast(jObject.getString("message"));

                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                cc.logoutapp1();


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
                if(dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                error.printStackTrace();
                Log.e("Error_filterdata", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("category_id", parent_product_id);
                map.put("min_price", "0");
                map.put("max_price", cc.loadPrefString1("max_prices"));
                map.put("brand_id", cc.loadPrefString1("brand_id"));
                map.put("attribute", cc.loadPrefString1("attribute"));
                map.put("min_discount", cc.loadPrefString1("discount_min_price"));
                map.put("max_discount", cc.loadPrefString1("discount_max_price"));
                Log.i("@@request SignUp", map.toString());
                return map;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", "en-gb");
                headers.put("1811-token", tokens);
                headers.put("Cookie", "PHPSESSID=" + cc.loadPrefString3("PHPSESSID") + ";" + "default=" + cc.loadPrefString3("default"));
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


    //All Category Data

    private void categoryDetailWS() {

        pDialog = new ProgressDialog(CategoryDetailActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.CategoryListing,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:categorydetail", response);
                        JSONArray jsonArray = null;

                        try {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }

                            JSONObject jObject = new JSONObject(response);

                            categoryListingdataList = new ArrayList<>();
                            categoryGideListingdatas = new ArrayList<>();

                            jsonArray = jObject.getJSONArray("products");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                CategoryListingdata categoryListingdata = new CategoryListingdata();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                categoryListingdata.setSharing_url(jsonObject.getString("shareProductUrl"));
                                categoryListingdata.setPrice(jsonObject.getString("price"));
                                categoryListingdata.setThumb(jsonObject.getString("thumb"));
                                categoryListingdata.setProduct_name(jsonObject.getString("name"));
                                categoryListingdata.setProduct_id(jsonObject.getString("product_id"));
                                categoryListingdata.setSpecial(jsonObject.getString("special"));
                                categoryListingdata.setTax(jsonObject.getString("tax"));
                                categoryListingdata.setModel(jsonObject.getString("model"));

                                categoryListingdata.setInCart(jsonObject.getBoolean("isInCart"));
                                categoryListingdata.setIswishlist(jsonObject.getBoolean("iswishlist"));

                                Log.e("product_name", jsonObject.getString("name"));

                                categoryListingdataList.add(categoryListingdata);

                            }

                            if (categoryListingdataList.size() > 0) {
                                pDialog.dismiss();

                                mAdapter = new ShowCategoryListingAdapter(CategoryDetailActivity.this, categoryListingdataList);
                                recycleview_listing_items.setAdapter(mAdapter);
                                CategoryGridListingAdapter adapter = new CategoryGridListingAdapter(CategoryDetailActivity.this, categoryListingdataList);
                                grid.setAdapter(adapter);
                                iv_null_image.setVisibility(View.GONE);


                            } else {
                                Log.e("nothingsss", "...........");

                                iv_null_image.setVisibility(View.VISIBLE);
                            }

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
                error.printStackTrace();
                Log.e("Error_CategoryDetail", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("order", cc.loadPrefString("order"));
                map.put("sort", cc.loadPrefString("name"));
                map.put("path", finalcategoryid);
                Log.i("@@Allsortingselected", map.toString());
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


    //Filter Parameter

    private void filterparametrs() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.FilterPramaters,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("FilterParamters", response);
                        JSONArray jsonArray = null;
                        JSONArray jsonArray1 = null;
                        JSONArray jsonArray2 = null;

                        String json = "";
                        try {
                            JSONObject jObject = new JSONObject(response);
                            jsonArray = jObject.getJSONArray("brands");
                            jsonArray1 = jObject.getJSONArray("subcategories");

                            ArrayList<BrandParentData> list = new ArrayList<BrandParentData>();
                            ArrayList<FilterParentCategoryData> subcategorylist = new ArrayList<FilterParentCategoryData>();

                            BrandParentData brandParentModel = new BrandParentData();
                            FilterParentCategoryData filterParentCategory = new FilterParentCategoryData();

                            //Brand Listing
                            cb_list = new ArrayList<CategoryBrandChildData>();
                            filter_subchild = new ArrayList<FilterSubChildCategoryData>();

                            for (int i = 0; i < jsonArray.length(); i++) {

                                CategoryBrandChildData categoryBrandChild = new CategoryBrandChildData();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                categoryBrandChild.setName(jsonObject.getString("name"));
                                categoryBrandChild.setBrand_id(jsonObject.getString("brand_id"));
                                cb_list.add(categoryBrandChild);

                                Log.e("sixe", String.valueOf(cb_list.size()));
                                Log.e("brandname", jsonObject.getString("name"));
                                Log.e("brand_id", jsonObject.getString("brand_id"));

                            }

                            brandParentModel.setItems(cb_list);
                            list.add(brandParentModel);

                            if (jsonArray.length() == 0) {

                                Log.e("BrandCAtegory", "BrandCAtegory...if");
                                ln_brandlist.setVisibility(View.GONE);
                                ln_brandlist_view.setVisibility(View.GONE);
                                ln_attributs_memory_view.setVisibility(View.GONE);
                                ln_attributs_prosessor_view.setVisibility(View.GONE);

                            } else {
                                ExpAdapter = new BrandListAdapter(CategoryDetailActivity.this, list);
                                ExpandList.setAdapter(ExpAdapter);
                                Log.e("BrandCAtegory", "BrandCAtegory...else");
                            }

                            //Filter Sub category

                            for (int j = 0; j < jsonArray1.length(); j++) {

                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                FilterSubChildCategoryData filterSubChildCategory = new FilterSubChildCategoryData();


                                filterSubChildCategory.setName(jsonObject1.getString("name"));
                                filterSubChildCategory.setCategory_subchild(jsonObject1.getString("subcategory_id"));
                                filter_subchild.add(filterSubChildCategory);

                                Log.e("sixe", String.valueOf(filter_subchild.size()));
                                Log.e("@@@subname", jsonObject1.getString("name"));
                            }

                            filterParentCategory.setItems(filter_subchild);
                            subcategorylist.add(filterParentCategory);

                            if (jsonArray1.length() == 0) {

                                Log.e("FilterSubCAtegory", "FilterSubCAtegory...if");
                                ln_category_list.setVisibility(View.GONE);

                            } else {

                                ExpAdapter1 = new FilterSubChildCategoryAdapters(CategoryDetailActivity.this, subcategorylist);
                                ExpandList1.setAdapter(ExpAdapter1);
                                Log.e("FilterSubCAtegory", "FilterSubCAtegory...else");

                            }

                            String price_filter2 = jObject.getString("price_filter");
                            Log.e("price_filter2", price_filter2);
                            if (price_filter2.isEmpty()) {
                                Log.e("price_filter", "........isEmpty");

                                ln_pricre_layout.setVisibility(View.GONE);

                            } else {
                                JSONObject obj = jObject.getJSONObject("price_filter");
                                cc.savePrefString1("min_price", obj.getString("min_price"));
                                cc.savePrefString1("max_price", obj.getString("max_price"));
                                Log.e("price_filter", "........isNotEmpty");


                                max_price = cc.loadPrefString1("max_price");

                                tvMax.setText(max_price);

                            }


                            JSONArray a = jObject.getJSONArray("attribute");
                            String sr = "";

                            if (a.length() == 0) {

                                Log.e("@@NummAttribute", ".......if");
                                ln_attributs_memary.setVisibility(View.GONE);
                                ln_attributs_prosessor.setVisibility(View.GONE);

                                rb_attributes_memory.setVisibility(View.GONE);
                                rb_attributes_processers.setVisibility(View.GONE);

                            } else {

                                for (int i = 0; i < a.length(); i++) {

                                    if (a.getString(i).equals("Processor") || a.getString(i).equals("Memory")) {
                                        Log.e("@@Processor", a.getString(i));
                                        tv_attributs_processers.setText("Processor");
                                        Log.e("@@memory", a.getString(i));
                                        tv_attributs_memory.setText("Memory");


                                        Log.e("Firsttime", "right...");

                                    } else if (a.getString(i).equals("Memory")) {

                                        tv_attributs_memory.setText("Memory");
                                        Log.e("Onetime", "Memoryright...");


                                    } else if (a.getString(i).equals("Processor")) {

                                        tv_attributs_processers.setText("Processor");
                                        Log.e("LastProcessor", "Processor...");

                                    } else {
                                        Log.e("NUll", "Log........");
                                    }
                                }

                                Log.e("@@NummAttribute", ".......else");
                            }
                        } catch (JSONException e) {
                            Log.e("Error : Exception", e.getMessage());
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("Error_FilterPrameters", error.toString());
            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("category_id", child_product_id);
                Log.i("@@Allsortingselected", map.toString());
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
    protected void onDestroy() {
        super.onDestroy();
    }

    //Brand adapter
    public class BrandListAdapter extends BaseExpandableListAdapter {

        public Context context;
        private ArrayList<BrandParentData> groups;
        ProgressBar mProgressBar, mProgressBar1;

        CommanClass cc;

        ImageView iv_filter_down;


        public BrandListAdapter(CategoryDetailActivity categoryDetailActivity, ArrayList<BrandParentData> list) {
            this.context = categoryDetailActivity;
            this.groups = list;
        }


        @Override
        public Object getChild(int groupPosition, int childPosition) {
            ArrayList<CategoryBrandChildData> chList = groups.get(groupPosition).getItems();


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
            CategoryBrandChildData child = (CategoryBrandChildData) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) context
                        .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.brand_child, null);
            }

            TextView tv = (TextView) convertView.findViewById(R.id.category_child_name);
            CheckBox cb = (CheckBox) convertView.findViewById(R.id.child_check);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    demo(groupPosition, childPosition);
                }
            });

            cb.setOnClickListener(new View.OnClickListener() {
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

            CategoryBrandChildData child = (CategoryBrandChildData) getChild(groupPosition, childPosition);
            child.getBrand_id();
            Log.e("BRAND", child.getBrand_id());
            cc.savePrefString1("brand_id", child.getBrand_id());

        }


        @Override
        public int getChildrenCount(int groupPosition) {
            ArrayList<CategoryBrandChildData> chList = groups.get(groupPosition).getItems();
            //  Log.e("childcount", String.valueOf(groups.get(groupPosition).getItems()));
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
            BrandParentData group = (BrandParentData) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater inf = (LayoutInflater) context
                        .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = inf.inflate(R.layout.brand_parent, null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.category_name);
            iv_filter_down = (ImageView) convertView.findViewById(R.id.iv_filter_down);

            tv.setText("Brand");

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {

            Log.e("isChildSelectable", ".....");

            return true;
        }


    }

    //Filter adapter

    public class FilterSubChildCategoryAdapters extends BaseExpandableListAdapter {

        public Context context;
        private ArrayList<FilterParentCategoryData> groups;
        ProgressBar mProgressBar, mProgressBar1;

        CommanClass cc;

        public FilterSubChildCategoryAdapters(CategoryDetailActivity categoryDetailActivity, ArrayList<FilterParentCategoryData> subcategorylist) {
            this.context = categoryDetailActivity;
            this.groups = subcategorylist;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            ArrayList<FilterSubChildCategoryData> chList = groups.get(groupPosition).getItems();
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
            FilterSubChildCategoryData child = (FilterSubChildCategoryData) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) context
                        .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.brand_child, null);
            }

            TextView tv = (TextView) convertView.findViewById(R.id.category_child_name);
            CheckBox cb = (CheckBox) convertView.findViewById(R.id.child_check);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    demo(groupPosition, childPosition);
                }
            });

            cb.setOnClickListener(new View.OnClickListener() {
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

            FilterSubChildCategoryData child = (FilterSubChildCategoryData) getChild(groupPosition, childPosition);
            child.getCategory_subchild();

            Log.e("filer_subcategory", child.getCategory_subchild());

            cc.savePrefString1("brand_id", child.getCategory_subchild());

        }


        @Override
        public int getChildrenCount(int groupPosition) {
            ArrayList<FilterSubChildCategoryData> chList = groups.get(groupPosition).getItems();
            //  Log.e("childcount", String.valueOf(groups.get(groupPosition).getItems()));
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
            FilterParentCategoryData group = (FilterParentCategoryData) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater inf = (LayoutInflater) context
                        .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = inf.inflate(R.layout.brand_parent, null);
            }

            TextView tv = (TextView) convertView.findViewById(R.id.category_name);
            tv.setText("Categories");


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

    /*Adapter Listing data*/

    public class ShowCategoryListingAdapter extends RecyclerView.Adapter<ShowCategoryListingAdapter.ViewHolder> {

        private Context mContext;
        List<CategoryListingdata> categoryListingdatas;
        ProgressBar mProgressBar;
        CommanClass cc;
        ProgressDialog pDialog;
        String res = "";
        String json_str = "";
        String status_code_signup = "";
        String status_message_signup = "";
        private BitmapDrawable bitmapDrawable;
        private Bitmap bitmap1;

        public ShowCategoryListingAdapter(CategoryDetailActivity categoryDetailActivity, List<CategoryListingdata> categoryListingdataList) {
            this.mContext = categoryDetailActivity;
            this.categoryListingdatas = categoryListingdataList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {

            cc = new CommanClass(mContext);

            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.category_listing_sorting, viewGroup, false);
            final ViewHolder viewHolder = new ViewHolder(v);

            return viewHolder;
        }

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

                                if (jObject.getString("status").equals("200")) {

                                    cc.showToast("Product added in to Cart");
                                } else if (jObject.getString("status").equals("400")) {


                                    JSONObject jObject1 = new JSONObject(response);
                                    JSONObject jObject2 = jObject1.getJSONObject("error");
                                    JSONObject jObject3 = jObject2.getJSONObject("option");
                                    String erro_message = jObject3.getString("328");

                                    Log.e("error_message", erro_message);

                                    cc.showToast(erro_message);
                                    Intent intentProductDetail = new Intent(mContext, ProductDetailActivity.class);
                                    intentProductDetail.putExtra("product_id", cc.loadPrefString("productid"));
                                    startActivity(intentProductDetail);

                                } else {

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


            final CategoryListingdata ccl = categoryListingdatas.get(i);

            viewHolder.tvSpecies.setText(ccl.getProduct_name());
            viewHolder.tv_price.setText(ccl.getPrice());


            if (ccl.isIswishlist()) {
                viewHolder.img_wishlist.setImageResource(R.mipmap.nav_wish);
            } else {
                viewHolder.img_wishlist.setImageResource(R.mipmap.faviourite);
            }

            if (ccl.getSpecial().equals("false")) {
                viewHolder.v_price_check.setVisibility(View.GONE);
                viewHolder.ll_offer_price.setVisibility(View.GONE);
            } else {
                viewHolder.ll_offer_price.setVisibility(View.VISIBLE);
                viewHolder.tv_offer_price.setText(ccl.getSpecial());
            }

            viewHolder.img_wishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (ccl.isIswishlist()) {
                        ccl.setIswishlist(false);
                        viewHolder.img_wishlist.setImageResource(R.mipmap.faviourite);
                    } else {
                        ccl.setIswishlist(true);
                        viewHolder.img_wishlist.setImageResource(R.mipmap.nav_wish);
                    }
//                    notifyItemChanged(position);

                    getPositionItem(i, ccl.isIswishlist());
                }
            });


            viewHolder.product_deatil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getPositionItem1(i);

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

                    CategoryListingdata rm = categoryListingdatas.get(i);

                    String product_name = rm.getProduct_name();
                    String product_link = rm.getSharing_url();


                    bitmapDrawable = (BitmapDrawable) viewHolder.imgThumbnail.getDrawable();// get the from imageview or use your drawable from drawable folder
                    bitmap1 = bitmapDrawable.getBitmap();
                    String imgBitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap1, "title", null);
                    Uri imgBitmapUri = Uri.parse(imgBitmapPath);
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("*/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, product_name + "\n" + product_link);

                    startActivity(Intent.createChooser(shareIntent, "Share Product :" + product_name));
                }
            });


            Log.e("gride_positions", ccl.getProduct_name());

            if (categoryListingdatas.size() != 0) {
                Picasso.with(mContext).load(categoryListingdatas.get(i).getThumb()).into(viewHolder.imgThumbnail, new Callback() {
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
            return categoryListingdatas.size();
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

                imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
                img_share = (ImageView) itemView.findViewById(R.id.img_share);
                img_addtocart = (ImageView) itemView.findViewById(R.id.img_addtocart);
                img_wishlist = (ImageView) itemView.findViewById(R.id.img_wishlist);
                tvSpecies = (TextView) itemView.findViewById(R.id.tv_title);
                tv_price = (TextView) itemView.findViewById(R.id.tv_price);
                mProgressBar = (ProgressBar) itemView.findViewById(R.id.pgb_image_loading);
                product_deatil = (LinearLayout) itemView.findViewById(R.id.product_deatil);

                tv_offer_price = (TextView) itemView.findViewById(R.id.tv_offer_price);
                ll_offer_price = (LinearLayout) itemView.findViewById(R.id.ll_offer_price);
                v_price_check = (View) itemView.findViewById(R.id.v_price_check);

            }
        }

        private void getPositionItem1(int adapterPosition) {


            CategoryListingdata nm = categoryListingdatas.get(adapterPosition);

            Intent intentProductDetail = new Intent(mContext, ProductDetailActivity.class);
            intentProductDetail.putExtra("product_id", nm.getProduct_id());
            mContext.startActivity(intentProductDetail);
            ((Activity) mContext).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }

        private void getPositionItem(int adapterPosition, boolean removeItem) {
            CategoryListingdata rm = categoryListingdatas.get(adapterPosition);

            if (tokens.isEmpty()) {
                Log.e("Token", "Empty");

//                if (removeItem) {
//
//                    dbHelper.InsertWishlistData(rm.getSharing_url(), rm.getProduct_id() + "", rm.getThumb()
//                            , rm.getProduct_name(), rm.getModel(), rm.getPrice(), rm.getSpecial(), rm.getTax());
//
//                } else {
//                    dbHelper.RemoveProduct(rm.getProduct_id());
                categoryListingdatas.remove(adapterPosition);
                mAdapter.notifyDataSetChanged();
//                }


            } else {
                cc.savePrefString("productid", String.valueOf(rm.getProduct_id()));

                if (removeItem) {
                    addwishlist();
                } else {
                    RemoveFromWishList(rm.getProduct_id());
                }
            }

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

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("remove", product_id);

                    Log.i("@@request", map.toString());
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

        private void addtocart(int adapterPosition) {

            if (tokens.isEmpty()) {
//                sqLiteHelper.insertData(id, cart_product_name, cart_product_price, cart_product_imagepath);
                Log.e("Token", "Empty");
                cc.showToast("Please Login to add the product in cart");
            } else {
                CategoryListingdata rm = categoryListingdatas.get(adapterPosition);
                cc.savePrefString("productid", String.valueOf(rm.getProduct_id()));
                addtocartws();
                Log.e("Token", "Not Empty");
            }
        }

    }

}