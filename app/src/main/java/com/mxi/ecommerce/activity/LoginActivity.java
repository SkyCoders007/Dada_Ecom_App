package com.mxi.ecommerce.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.mxi.ecommerce.R;
import com.mxi.ecommerce.comman.AndyUtils;
import com.mxi.ecommerce.comman.AppController;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.comman.ConnectionUrl;
import com.mxi.ecommerce.comman.PersistentCookieStore;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    CommanClass cc;
    ProgressDialog pDialog;
    Dialog dialog;

    TextView tv_forgotPassword;
    TextView txtregisters, tv_skip;

    EditText et_login_email, et_login_password;
    EditText et_forgot_email;

    Button btn_login_submit;

    LinearLayout ln_register;

    String res = "";
    String json_str = "";
    String status_code_signup = "";
    String status_message_signup = "";
    String android_id;


    //Gmail Integration

    public static final String TAG2 = "MainActivity";
    public static final int RequestSignInCode = 1;
    public FirebaseAuth firebaseAuth;
    public GoogleApiClient googleApiClient;
    Button SignOutButton;
    com.google.android.gms.common.SignInButton signInButton;
    ImageButton ib_gmail_login;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;


    //Facebook Integration

    private FirebaseAuth.AuthStateListener mAuthListener;
    public static final int FacebookSing = 2;
    LoginButton loginButton;
    CallbackManager mCallbackManager;
    private AccessTokenTracker accessTokenTracker;
    String TAG1 = "Hey";
    String name;
    private ProfileTracker profileTracker;
    ImageButton ib_fb_login;

    //Twitter Integration

    private static final String TAG = "TwitterLogin";
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    public static final int TwitterSign = 3;
    private FirebaseAuth mAuth;


    private TwitterLoginButton mLoginButton;
    ImageButton ib_twitter_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));

        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(authConfig)
                .build();

        Twitter.initialize(twitterConfig);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        cc = new CommanClass(this);

        if (cc.loadPrefString3("PHPSESSID").isEmpty() && cc.loadPrefString3("default").isEmpty()) {
            CookieManager cookieManager = new CookieManager(new PersistentCookieStore(this),
                    CookiePolicy.ACCEPT_ORIGINAL_SERVER);
            CookieHandler.setDefault(cookieManager);
        }

        firebaseAuth = FirebaseAuth.getInstance();

        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        mCallbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
//                nextActivity(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Creating and Configuring Google Api Client.
        googleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                .enableAutoManage(LoginActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        android_id = Settings.Secure.getString(LoginActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d(TAG, "facebook:onSuccess:" + loginResult);

                loginResult.getAccessToken();

                handleFacebookAccessToken(loginResult.getAccessToken()); //  Toast.makeText(MainActivity.this, "Token:"+loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();


                Log.e("getToken", String.valueOf(loginResult.getAccessToken()));

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + mUser.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };


        mLoginButton = findViewById(R.id.button_twitter_login);
        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                Log.d(TAG, "twitterLogin:success" + result);
                handleTwitterSession(result.data);


            }

            @Override
            public void failure(TwitterException exception) {
                Log.w(TAG, "twitterLogin:failure", exception);

            }
        });


        Log.e("deviceid", android_id);

        String languages = cc.loadPrefString("language");

        Log.e("language", languages);

        iniliation();
        clicklistner();

    }

    private void clicklistner() {

        ln_register.setOnClickListener(this);
        tv_forgotPassword.setOnClickListener(this);
        tv_skip.setOnClickListener(this);

        btn_login_submit.setOnClickListener(this);

        ib_gmail_login.setOnClickListener(this);
        ib_fb_login.setOnClickListener(this);
        ib_twitter_login.setOnClickListener(this);
        // signInButton.setOnClickListener(this);
    }


    private void iniliation() {

        ln_register = (LinearLayout) findViewById(R.id.tv_register);
        ln_register = (LinearLayout) findViewById(R.id.tv_register);

        tv_skip = (TextView) findViewById(R.id.tv_skip);
        tv_forgotPassword = (TextView) findViewById(R.id.tv_forgotPassword);

        btn_login_submit = (Button) findViewById(R.id.btn_login_submit);

        et_login_email = (EditText) findViewById(R.id.et_login_email);
        et_login_password = (EditText) findViewById(R.id.et_login_password);


        ib_gmail_login = (ImageButton) findViewById(R.id.ib_gmail_login);
        ib_fb_login = (ImageButton) findViewById(R.id.ib_fb_login);
        ib_twitter_login = (ImageButton) findViewById(R.id.ib_twitter_login);


    }

    @Override

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register:

                Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();

                break;

            case R.id.tv_forgotPassword:

                showForgotPasswordPopup();

                break;

            case R.id.btn_login_submit:

                if (!cc.isConnectingToInternet()) {
                    cc.showToast(getString(R.string.no_internet));

                } else {
                    if (isValidate()) {

                        Login();

                    }
                }


                break;

            case R.id.tv_skip:

                Intent intentMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intentMainActivity);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;


            case R.id.ib_gmail_login:


                Intent AuthIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(AuthIntent, RequestSignInCode);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//                finish();
                break;

            case R.id.ib_fb_login:

                loginButton.performClick();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ib_twitter_login:


                mLoginButton.performClick();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;


        }
    }

    //Gamil Data

    private void Login() {

        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.login,
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

                                clearField();

                                JSONObject obj = jObject.getJSONObject("customerData");

                                cc.savePrefString("customer_id", obj.getString("customer_id"));
                                cc.savePrefString("firstname", obj.getString("firstname"));

                                Log.e("username", obj.getString("firstname"));
                                cc.savePrefBoolean("isLogin", true);


                                cc.savePrefString("lastname", obj.getString("lastname"));
                                cc.savePrefString("email", obj.getString("email"));
                                cc.savePrefString("mobile", obj.getString("mobile"));

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();


                            } else if (jObject.getString("status").equals("400")) {
                                Toast.makeText(getApplicationContext(), "Email Id and Password didn't match", Toast.LENGTH_LONG).show();

                                //cc.showToast(jObject.getString("message"));
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
                AndyUtils.showToast(LoginActivity.this, getString(R.string.ws_error));
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                String resHead = response.headers.get("1811-token");
                Log.e("@@resHead", resHead + "");
                cc.savePrefString("1811-token", resHead);
                return super.parseNetworkResponse(response);

            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put("email", et_login_email.getText().toString().trim());
                map.put("password", et_login_password.getText().toString().trim());
                map.put("device_id", android_id);
                map.put("gcm_id", "123456");
                map.put("login_with", "android");
                map.put("apn_id", "hghgyf");

                Log.e("@@@@loginemail", et_login_email.getText().toString().trim());
                Log.e("@@@LoginPassword", et_login_password.getText().toString().trim());

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("Cookie", "PHPSESSID=" + cc.loadPrefString3("PHPSESSID") + ";" + "default=" + cc.loadPrefString3("default"));
                Log.i("@@request headers", "__" + headers.toString());
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    public void clearField() {

        et_login_email.setText("");
        et_login_password.setText("");

    }

    public boolean isValidate() {
        String msg = null;

        if (!AndyUtils.eMailValidation(et_login_email.getText().toString())) {
            msg = "Enter Valid Email Id";
            et_login_email.requestFocus();
        } else if (TextUtils.isEmpty(et_login_password.getText().toString())) {
            msg = "Enter Valid Password";
            et_login_password.requestFocus();
        }

        if (msg != null) {
            AndyUtils.showToast(LoginActivity.this, msg);
            return false;
        }

        if (msg == null) {
            return true;
        }

        AndyUtils.showToast(LoginActivity.this, msg);
        return false;
    }

    private void showForgotPasswordPopup() {

        dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_forgot_password);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);

        et_forgot_email = (EditText) dialog.findViewById(R.id.et_email_id);

        TextView tv_close = (TextView) dialog.findViewById(R.id.tv_cancel);
        TextView tv_apply = (TextView) dialog.findViewById(R.id.tv_apply);

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cc.isConnectingToInternet()) {
                    cc.showToast(getString(R.string.no_internet));

                } else {
                    if (isValidateForgot()) {

                        forgotPasswordWS();

                    }
                }


            }
        });
        dialog.show();
    }

    public boolean isValidateForgot() {
        String msg = null;

        if (!AndyUtils.eMailValidation(et_forgot_email.getText().toString())) {
            msg = "Enter Valid Email Id";
            et_forgot_email.requestFocus();

        }


        if (msg != null) {
            AndyUtils.showToast(LoginActivity.this, msg);
            return false;
        }
        if (msg == null) {
            return true;
        }
        AndyUtils.showToast(LoginActivity.this, msg);
        return false;
    }

    private void forgotPasswordWS() {

        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.forgotpassword,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:forgot", response);
                        try {
                            if(dialog.isShowing()){
                                dialog.dismiss();
                            }

                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }

                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("200")) {
                                clearField();
                                cc.showToast(jObject.getString("message"));
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
                AndyUtils.showToast(LoginActivity.this, getString(R.string.ws_error));
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                String resHead = response.headers.get("1811-token");
                Log.e("@@resHead", resHead + "");
                cc.savePrefString("1811-token", resHead);
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();

                map.put("email", et_forgot_email.getText().toString().trim());
                Log.e("@@@@ForgotEmail", et_forgot_email.getText().toString().trim());

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("Cookie", "PHPSESSID=" + cc.loadPrefString3("PHPSESSID") + ";" + "default=" + cc.loadPrefString3("default"));
//                headers.put("language","en-gb");
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestSignInCode) {

            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (googleSignInResult.isSuccess()) {

                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

                FirebaseUserAuth(googleSignInAccount);
            }

        } else if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE) {
            mLoginButton.onActivityResult(requestCode, resultCode, data);

        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    // Gmail Integration
    public void FirebaseUserAuth(final GoogleSignInAccount googleSignInAccount) {

        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);


        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task AuthResultTask) {

                        if (AuthResultTask.isSuccessful()) {

                            // Getting Current Login user details.
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


                            String usermail = firebaseUser.getEmail();
                            String token = firebaseUser.getUid();
                            String user_token = googleSignInAccount.getIdToken();


                            cc.savePrefString("email", usermail);
                            cc.savePrefString("identity_token", user_token);
                            cc.savePrefString("user_token", token);


                            //       Log.e("Google: usermail",usermail);
                            Log.e("Google: token", token);
                            Log.e("Google: user_token", user_token);


                            isAlreadyRegistered(usermail, false);


                            // Setting up name into TextView.


                        } else {
                            Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void isAlreadyRegistered(final String usermail, boolean b) {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.getcustomerbyemail,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("isAlreadyRegistered", response);
                        try {

                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("200")) {
                                clearField();
                                JSONObject obj = jObject.getJSONObject("customerData");

                                String id = obj.getString("customer_id");
                                String firstname = obj.getString("firstname");
                                String lastname = obj.getString("lastname");
                                String email = obj.getString("email");
                                String mobile = obj.getString("mobile");

                                cc.savePrefString("firstname", firstname);
                                cc.savePrefString("lastname", lastname);
                                cc.savePrefBoolean("isLogin", true);

                                Log.e("firstname", firstname);
                                Log.e("lastname", lastname);
                                Log.e("email", email);
                                Log.e("mobile", mobile);
                                Log.e("id", id);

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                                Toast.makeText(getApplicationContext(), "Login successfully.", Toast.LENGTH_SHORT).show();

                            } else {
                                if (jObject.getString("status").equals("400")) {
                                    verifyUser(usermail);
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
                error.printStackTrace();
                Log.e("isAlreadyRegistered",error.toString());
                AndyUtils.showToast(LoginActivity.this, getString(R.string.ws_error));
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                String resHead = response.headers.get("1811-token");
                Log.e("@@resHead", resHead + "");
                cc.savePrefString("1811-token", resHead);
                return super.parseNetworkResponse(response);

            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put("email", cc.loadPrefString("email"));
                map.put("device_id", android_id);
                map.put("gcm_id", "123456");
                map.put("login_with", "android");
                map.put("apn_id", "hghgyf");
                map.put("login_by", "Facebook");

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("Cookie", "PHPSESSID=" + cc.loadPrefString3("PHPSESSID") + ";" + "default=" + cc.loadPrefString3("default"));
//                headers.put("language","en-gb");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    private void facebook(final String usermail, boolean b) {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.getcustomerbyemail,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("isAlreadyRegistered", response);
                        try {

                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("200")) {
                                clearField();
                                JSONObject obj = jObject.getJSONObject("customerData");

                                String id = obj.getString("customer_id");
                                String firstname = obj.getString("firstname");
                                String lastname = obj.getString("lastname");
                                String email = obj.getString("email");
                                String mobile = obj.getString("mobile");

                                cc.savePrefString("firstname", firstname);
                                cc.savePrefString("lastname", lastname);
                                cc.savePrefBoolean("isLogin", true);

                                Log.e("firstname", firstname);
                                Log.e("lastname", lastname);
                                Log.e("email", email);
                                Log.e("mobile", mobile);
                                Log.e("id", id);

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();

                                Toast.makeText(getApplicationContext(), "Login successfully.", Toast.LENGTH_SHORT).show();

                            } else {
                                if (jObject.getString("status").equals("400")) {


                                    verifyUser(usermail);

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
                error.printStackTrace();
                Log.e("facebook",error.toString());
                AndyUtils.showToast(LoginActivity.this, getString(R.string.ws_error));
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                String resHead = response.headers.get("1811-token");
                Log.e("@@resHead", resHead + "");
                cc.savePrefString("1811-token", resHead);
                return super.parseNetworkResponse(response);

            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put("email", cc.loadPrefString("email"));
                map.put("device_id", android_id);
                map.put("gcm_id", "123456");
                map.put("login_with", "android");
                map.put("apn_id", "hghgyf");
                map.put("login_by", "Facebook");

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("Cookie", "PHPSESSID=" + cc.loadPrefString3("PHPSESSID") + ";" + "default=" + cc.loadPrefString3("default"));
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    private void twitter(final String usermail, boolean b) {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.getcustomerbyemail,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("isAlreadyRegistered", response);
                        try {

                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("200")) {
                                clearField();
                                JSONObject obj = jObject.getJSONObject("customerData");

                                String id = obj.getString("customer_id");
                                String firstname = obj.getString("firstname");
                                String lastname = obj.getString("lastname");
                                String email = obj.getString("email");
                                String mobile = obj.getString("mobile");

                                Log.e("firstname", firstname);
                                Log.e("lastname", lastname);
                                Log.e("email", email);
                                Log.e("mobile", mobile);
                                Log.e("id", id);

                                cc.savePrefString("firstname", firstname);
                                cc.savePrefString("lastname", lastname);
                                cc.savePrefBoolean("isLogin", true);

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();

                                Toast.makeText(getApplicationContext(), "Login successfully.", Toast.LENGTH_SHORT).show();
                            } else {
                                if (jObject.getString("status").equals("400")) {
                                    verifyUser(usermail);
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
                error.printStackTrace();
                Log.e("twitter",error.toString());
                AndyUtils.showToast(LoginActivity.this, getString(R.string.ws_error));
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String resHead = response.headers.get("1811-token");
                Log.e("@@resHead", resHead + "");
                cc.savePrefString("1811-token", resHead);
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("email", cc.loadPrefString("email"));
                map.put("device_id", android_id);
                map.put("gcm_id", "123456");
                map.put("login_with", "android");
                map.put("apn_id", "hghgyf");
                map.put("login_by", "Facebook");

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("Cookie", "PHPSESSID=" + cc.loadPrefString3("PHPSESSID") + ";" + "default=" + cc.loadPrefString3("default"));
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }


    //Facebook Integration
    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG1, "signInWithCredential:success");

                            Log.e("identity_token", token.getToken());

                            String user_token = token.getToken();

                            FirebaseUser user = mAuth.getCurrentUser();
                            String usermail = mAuth.getCurrentUser().getEmail();
                            String token = mAuth.getCurrentUser().getUid();

                            cc.savePrefString("email", usermail);
                            cc.savePrefString("identity_token", user_token);
                            cc.savePrefString("user_token", token);


                            facebook(usermail, false);


                        } else {

                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    //Twitter Integration
    private void handleTwitterSession(final TwitterSession session) {
        Log.e(TAG, "handleTwitterSession:" + session);

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Log.e("identity_token", session.getAuthToken().token);

                            String identity_token = session.getAuthToken().token;
                            String userid = mAuth.getCurrentUser().getUid();
                            String usermail = mAuth.getCurrentUser().getEmail();


                            cc.savePrefString("email", usermail);
                            cc.savePrefString("identity_token", identity_token);
                            cc.savePrefString("user_token", userid);

                            twitter(usermail, false);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    private void verifyUser(final String userEmail) {

        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_signup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.MATCH_PARENT);


        final EditText et_Fname = (EditText) dialog.findViewById(R.id.etRegFname);
        final EditText et_Lname = (EditText) dialog.findViewById(R.id.last_name);
        final EditText et_Email = (EditText) dialog.findViewById(R.id.etRegEmail);
        final EditText et_Phone = (EditText) dialog.findViewById(R.id.etRegPhone);
        final ImageView iv_back = (ImageView) dialog.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        et_Email.setText(userEmail);
        et_Email.setFocusable(false);

        Button btn_Register = (Button) dialog.findViewById(R.id.btnNewRegister);
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_Phone.getText().toString().trim();
                String mail = et_Email.getText().toString().trim();
                String firstname = et_Fname.getText().toString().trim();
                String lastname = et_Lname.getText().toString().trim();

                if (!cc.isConnectingToInternet()) {
                    cc.showToast(getResources().getString(R.string.internet_alert));
                } else if (phone.equals("")) {
                    cc.showToast(getResources().getString(R.string.phone_alert));
                } else if (phone.length() != 10) {
                    cc.showToast(getResources().getString(R.string.phone_alert));
                } else {
                    dialog.dismiss();
                    SocialMediaSignUP(phone, mail, firstname, lastname, userEmail);
                }
            }
        });
        dialog.show();


    }

    private void SocialMediaSignUP(final String phone, String mail, final String firstname, final String lastname, final String userEmail) {

        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.socialLogin,
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
                                clearField();
                                JSONObject obj = jObject.getJSONObject("customerData");

                                cc.savePrefString("customer_id", obj.getString("customer_id"));
                                cc.savePrefString("firstname", obj.getString("firstname"));
                                cc.savePrefString("lastname", obj.getString("lastname"));
                                cc.savePrefString("email", obj.getString("email"));
                                cc.savePrefString("mobile", obj.getString("mobile"));

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();

                                cc.showToast(jObject.getString("message"));

                            } else if
                                    (jObject.getString("status").equals("400")) {
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
                Log.e("SocialMediaSignUP",error.toString());
                AndyUtils.showToast(LoginActivity.this, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("firstname", firstname);
                map.put("lastname", lastname);
                map.put("mobileno", phone);
                map.put("email", userEmail);
                map.put("device_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                map.put("login_with", "android");
                map.put("apn_id", "dsfsfd");
                map.put("login_by", "Facebook");
                map.put("user_token", cc.loadPrefString("user_token"));
                map.put("identity_token", cc.loadPrefString("identity_token"));


                Log.i("@@request SignUp", map.toString());

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("Cookie", "PHPSESSID=" + cc.loadPrefString3("PHPSESSID") + ";" + "default=" + cc.loadPrefString3("default"));
                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

}