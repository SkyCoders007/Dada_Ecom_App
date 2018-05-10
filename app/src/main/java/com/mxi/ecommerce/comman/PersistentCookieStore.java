package com.mxi.ecommerce.comman;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

public class PersistentCookieStore implements CookieStore {

    private final static String PREF_DEFAULT_STRING = "";

    private final static String PREFS_NAME = PersistentCookieStore.class.getName();
    private Gson mGson;
    /**
     * The preferences session cookie key.
     */
    private final static String PREF_SESSION_COOKIE = "session_cookie";

    private CookieStore mStore;
    private Context mContext;

    CommanClass cc;

    public PersistentCookieStore(Context context) {
        // prevent context leaking by getting the application context
        mContext = context.getApplicationContext();
        mGson = new Gson();
        cc = new CommanClass(mContext);

        // get the default in memory store and if there is a cookie stored in shared preferences,
        // we added it to the cookie store
        mStore = new CookieManager().getCookieStore();

        Log.e("mstore", String.valueOf(mStore));
        String jsonSessionCookie = getJsonSessionCookieString(mContext);
        if (!jsonSessionCookie.equals(PREF_DEFAULT_STRING)) {
            Gson gson = new Gson();
            HttpCookie cookie = gson.fromJson(jsonSessionCookie, HttpCookie.class);
            mStore.add(URI.create(cookie.getDomain()), cookie);
        }


    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        if (cookie.getName().equals("sessionid")) {
            // if the cookie that the cookie store attempt to add is a session cookie,
            // we remove the older cookie and save the new one in shared preferences
            remove(URI.create(cookie.getDomain()), cookie);
            saveSessionCookie(cookie);
        }

        if (cc.loadPrefString3("PHPSESSID").isEmpty() || cc.loadPrefString3("default").isEmpty()) {
            mStore.add(URI.create(cookie.getDomain()), cookie);

            Log.e("domin", cookie.getDomain());
            Log.e("cookiename", cookie.getName());
            Log.e("cookieValue", cookie.getValue());
            Log.e("cookie", String.valueOf(cookie));
            Log.e("sessionid", String.valueOf(cookie.getName().equals("sessionid")));

            if (cookie.getName().equalsIgnoreCase("PHPSESSID")) {

                cc.savePrefString3("PHPSESSID", String.valueOf(cookie.getValue()));
            }

            if (cookie.getName().equalsIgnoreCase("default")) {
                cc.savePrefString3("default", String.valueOf(cookie.getValue()));
            }

        }


    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return mStore.get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {

        Log.e("cookies", String.valueOf(getCookies()));

        return mStore.getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return mStore.getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return mStore.remove(uri, cookie);
    }

    @Override
    public boolean removeAll() {
        return mStore.removeAll();
    }

    private String getJsonSessionCookieString(Context mContext) {
        return getPrefs().getString(PREF_SESSION_COOKIE, PREF_DEFAULT_STRING);
    }

    private void saveSessionCookie(HttpCookie cookie) {
        Gson gson = new Gson();
        String jsonSessionCookieString = gson.toJson(cookie);
        // cc.savePrefString("session_id",jsonSessionCookieString);
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putString(PREF_SESSION_COOKIE, jsonSessionCookieString);
        editor.apply();
    }

    private SharedPreferences getPrefs() {
        return mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }


}