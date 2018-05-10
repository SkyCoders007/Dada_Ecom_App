package com.mxi.ecommerce.activity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mobisys.android.autocompleteview.AutoCompleteView;
import com.mxi.ecommerce.R;
import com.mxi.ecommerce.adapter.SearchCategoryGridListingAdapter;
import com.mxi.ecommerce.comman.AppController;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.comman.ConnectionUrl;
import com.mxi.ecommerce.model.CategoryListingdata;
import com.mxi.ecommerce.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    AutoCompleteView auto_text;
    ImageView iv_search;
    ProgressDialog pDialog;
    GridView grid;

    CommanClass cc;

    ArrayList<CategoryListingdata> categoryListingdataList;

    String searchProductString = "";
    String tokens = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        cc = new CommanClass(this);

        tokens = cc.loadPrefString("1811-token");

        grid = (GridView) findViewById(R.id.grid);
        auto_text = (AutoCompleteView) findViewById(R.id.auto_text);
        iv_search = (ImageView) findViewById(R.id.iv_search);

        if (cc.loadPrefString("language").equals("arabi")) {
            ((AutoCompleteView) findViewById(R.id.auto_text)).setAutocompleteUrl("http://mbdbtechnology.com/projects/1811/index.php?route=api/product/search&language=arabi&search=");
        } else if (cc.loadPrefString("language").equals("en-gb")) {
            ((AutoCompleteView) findViewById(R.id.auto_text)).setAutocompleteUrl("http://mbdbtechnology.com/projects/1811/index.php?route=api/product/search&language=en-gb&search=");
        } else {
            ((AutoCompleteView) findViewById(R.id.auto_text)).setAutocompleteUrl("http://mbdbtechnology.com/projects/1811/index.php?route=api/product/search&language=en-gb&search=");
        }


        ((AutoCompleteView) findViewById(R.id.auto_text)).setParser(new AutoCompleteView.AutoCompleteResponseParser() {
            @Override
            public ArrayList<? extends Object> parseAutoCompleteResponse(String response) {
                ArrayList<Product> products = null;
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    final JSONArray predsJsonArray = jsonObj.getJSONArray("products");

                    products = new ArrayList<Product>();
                    for (int i = 0; i < predsJsonArray.length(); i++) {
                        String placeName = predsJsonArray.getJSONObject(i).getString("name");

                        Log.e("@@SearchNameHint",predsJsonArray.getJSONObject(i).getString("name"));
                        Product product = new Product();
                        product.setName(placeName);
                        products.add(product);
                    }
                } catch (JSONException e) {
                    Log.e("AppUtil", "Cannot process JSON results", e);
                }

                return products;
            }
        });
        ((AutoCompleteView) findViewById(R.id.auto_text)).setSelectionListener(new AutoCompleteView.AutoCompleteItemSelectionListener() {
            @Override
            public void onItemSelection(Object obj) {
                Product product = (Product) obj;
                ((com.mobisys.android.autocompleteview.AutoCompleteView) findViewById(R.id.auto_text)).setText(product.getName());
                ((com.mobisys.android.autocompleteview.AutoCompleteView) findViewById(R.id.auto_text)).clearFocus();
                makeJsonCallForProductSearch(product.getName());
            }
        });


        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeJsonCallForProductSearch(auto_text.getText().toString());
            }
        });
    }

    private void makeJsonCallForProductSearch(final String string) {

        pDialog = new ProgressDialog(SearchActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        String finalUrl = ConnectionUrl.searchUrlEng + string;
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, finalUrl,
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

                            JSONObject jObject = new JSONObject(response);

                            if (jObject.getString("status").equals("200")) {
                                categoryListingdataList = new ArrayList<>();

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
//                                    categoryListingdata.setModel(jsonObject.getString("model"));
                                    categoryListingdata.setSharing_url(jsonObject.getString("href"));

//                                    categoryListingdata.setInCart(jsonObject.getBoolean("isInCart"));

//                                    if (tokens.isEmpty()) {
//                                        if (dbHelper.ProductExist(categoryListingdata.getProduct_id() + "")) {
//                                            categoryListingdata.setIswishlist(true);
//                                        } else {
//                                            categoryListingdata.setIswishlist(false);
//                                        }
//                                    } else {
//                                        categoryListingdata.setIswishlist(jsonObject.getBoolean("iswishlist"));
//                                    }
                                    Log.e("product_name", jsonObject.getString("name"));
                                    categoryListingdataList.add(categoryListingdata);
                                }

                                if (categoryListingdataList != null) {

//                                    CategoryGridListingAdapter adapter = new CategoryGridListingAdapter(SearchActivity.this, categoryListingdataList);
                                    SearchCategoryGridListingAdapter adapter = new SearchCategoryGridListingAdapter(SearchActivity.this, categoryListingdataList);
                                    grid.setAdapter(adapter);
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
                Log.e("Error_productSearch",error.toString());
            }
        }) {

            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                Log.i("response", response.headers.toString());
//                Map<String, String> responseHeaders = response.headers;
//                String rawCookies = responseHeaders.get("Set-Cookie");
//                Log.i("productsearch_cookies", rawCookies);
                return super.parseNetworkResponse(response);
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }
}
