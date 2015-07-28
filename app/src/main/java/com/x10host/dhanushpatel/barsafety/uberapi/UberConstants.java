package com.x10host.dhanushpatel.barsafety.uberapi;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class UberConstants {
    private static HashMap<String, String> authParameters = new HashMap<>();

    public static final String AUTHORIZE_URL = "https://login.uber.com/oauth/authorize";
    public static final String BASE_URL = "https://login.uber.com/";
    public static final String SCOPES = "profile history_lite history";
    public static final String BASE_UBER_URL_V1 = "https://api.uber.com/v1/";
    public static final String BASE_UBER_URL_V1_1 = "https://api.uber.com/v1.1/";
    public static final double START_LATITUDE = 37.781955;
    public static final double START_LONGITUDE = -122.402367;
    public static final double END_LATITUDE = 37.744352;
    public static final double END_LONGITUDE = -122.416743;
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final String UBER_CLIENT_ID = null;
    public static final String UBER_CLIENT_SECRET = null;
    public static final String UBER_REDIRECT_URL = null;

    private static String buildUrl() {
        Uri.Builder uriBuilder = Uri.parse(AUTHORIZE_URL).buildUpon()
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("client_id", UBER_CLIENT_ID)
                .appendQueryParameter("scope", SCOPES)
                .appendQueryParameter("redirect_uri", UBER_REDIRECT_URL);
        return uriBuilder.build().toString().replace("%20", "+");
    }

    /*
    UberAuthTokenClient.getUberAuthTokenClient().getAuthToken(
                        Constants.getUberClientSecret(MainActivity.this),
                        Constants.getUberClientId(MainActivity.this),
                        "authorization_code",
                        uri.getQueryParameter("code"),
                        Constants.getUberRedirectUrl(MainActivity.this),
                        new UberCallback<User>() {
                            @Override
                            public void success(User user, Response response) {
                                DemoActivity.start(MainActivity.this, user.getAccessToken(), user.getTokenType());
                                finish();
                            }
                        });
     */
}