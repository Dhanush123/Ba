package com.x10host.dhanushpatel.barsafety.uberapi;

import com.victorsima.uber.UberAuthService;
import com.victorsima.uber.UberClient;
import com.victorsima.uber.UberService;
import com.victorsima.uber.exception.ConflictException;
import com.victorsima.uber.model.Price;
import com.victorsima.uber.model.Prices;
import com.victorsima.uber.model.Product;
import com.victorsima.uber.model.Products;
import com.victorsima.uber.model.Promotion;
import com.victorsima.uber.model.Time;
import com.victorsima.uber.model.Times;
import com.victorsima.uber.model.UserActivity;
import com.victorsima.uber.model.UserHistory;
import com.victorsima.uber.model.UserProfile;
import com.victorsima.uber.model.request.Request;
import com.victorsima.uber.model.request.RequestBody;
import com.victorsima.uber.model.request.RequestBody.Status;
import com.victorsima.uber.model.sandbox.SandboxProductBody;
import com.victorsima.uber.model.sandbox.SandboxRequestBody;
import com.victorsima.uber.model.sandbox.SandboxService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

import com.squareup.okhttp.Response;
/**
 * Created by Tehas_2 on 7/18/2015.
 */
public class UberAPI {
    public static final double START_LATITUDE   = 40.74844;
    public static final double START_LONGITUDE  = -73.985664;
    public static final double END_LATITUDE     = 40.74844;
    public static final double END_LONGITUDE    = -73.985664;

    private UberClient client;

    public class IdConflictException extends Exception {}

    public UberAPI(String clientId, String clientSecret, String serverToken,
                                  String redirectUri, boolean isDebugMode) {
        if (client == null) {
            client = new UberClient(
                    clientId,
                    clientSecret,
                    redirectUri,
                    serverToken,
                    null, //client for handling GET/POST requests, set to null if you no wanna vorry bout it
                    isDebugMode, //use sandbox mode?
                    isDebugMode ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.BASIC); //loglevel
        }
    }

    public UberService getApiService() {
        return client.getApiService();
    }

    public UberAuthService getAuthService() {
        return client.getAuthService();
    }

    public SandboxService getSandboxService() {
        return client.getSandboxService();
    }

    /**
     * Puts a test product to simulate an actual product
     * @param productId the id of the product
     * @param surgeMultiplier the surge multiplier to put effect on the product
     * @param driversAvailable if drivers are available or not
     * @return if it was successful in making the request
     */
    public boolean putTestProduct(String productId, float surgeMultiplier, boolean driversAvailable) {
        Response response = getSandboxService().putProducts(productId, new SandboxProductBody(surgeMultiplier, driversAvailable));
        return response.isSuccessful();
    }

    /**
     * Sends a simulated request to the product given its ID
     * @param productId the id of the product
     * @param status the status of the request
     * @return if it was successful in making the request
     */
    public boolean simulateRequest(String productId, Status status) {
        Response response = getSandboxService().putRequest(productId, new SandboxRequestBody(status));
        return response.isSuccessful();
    }

    /**
     * Posts a request for a product (synchronously)
     * @param productId the id of the product
     * @param startLatitude the latitude part of the start location
     * @param startLongitude the longitude part of the start location
     * @param endLatitude the latitude part of the end location
     * @param endLongitude the longitude part of the end location
     * @return the Request object
     */
    public Request postRequestSync(String productId,
                                   double startLatitude, double startLongitude,
                                   double endLatitude, double endLongitude) {
        return postRequestSync(productId, startLatitude, startLongitude,
                endLatitude, endLongitude, null);
    }

    /**
     * Posts a request for a product (synchronously)
     * @param productId the id of the product
     * @param startLatitude the latitude part of the start location
     * @param startLongitude the longitude part of the start location
     * @param endLatitude the latitude part of the end location
     * @param endLongitude the longitude part of the end location
     * @param surgeConfirmationId the surge confirmation id, used for surge pricing calculations
     * @return the Request object
     */
    public Request postRequestSync(String productId,
                                   double startLatitude, double startLongitude,
                                   double endLatitude, double endLongitude,
                                   String surgeConfirmationId){
        RequestBody body = new RequestBody(productId, startLatitude, startLongitude,
                                           endLatitude, endLongitude, surgeConfirmationId);
        return getApiService().postRequest(body);
    }

    /**
     * Posts a request for a product (asynchronously)
     * WARNING, the callback must check for a 409 error, which means that a surgeConfirmationId is
     * required. See the other overloaded method which requires the id
     * @param productId the id of the product
     * @param startLatitude the latitude part of the start location
     * @param startLongitude the longitude part of the start location
     * @param endLatitude the latitude part of the end location
     * @param endLongitude the longitude part of the end location
     * @param callback the callback to use when the request has been successfully POSTed
     */
    public void postRequestAsync(String productId,
                                 double startLatitude, double startLongitude,
                                 double endLatitude, double endLongitude,
                                 Callback<Request> callback) {
        postRequestAsync(productId, startLatitude, startLongitude,
                         endLatitude, endLongitude, null, callback);
    }

    /**
     * Posts a request for a product (asynchronously)
     * @param productId the id of the product
     * @param startLatitude the latitude part of the start location
     * @param startLongitude the longitude part of the start location
     * @param endLatitude the latitude part of the end location
     * @param endLongitude the longitude part of the end location
     * @param surgeConfirmationId the surge confirmation id, used for surge pricing calculations
     * @param callback the callback to use when the request has been successfully POSTed
     */
    public void postRequestAsync(String productId,
                                 double startLatitude, double startLongitude,
                                 double endLatitude, double endLongitude,
                                 String surgeConfirmationId,
                                 Callback<Request> callback){
        RequestBody body = new RequestBody(productId, startLatitude, startLongitude,
                                           endLatitude, endLongitude, surgeConfirmationId);
        getApiService().postRequest(body, callback);
    }

    /**
     * Attempts to cancel/delete a request (synchronously)
     * @param requestId the id of the request
     * @return if it was successful or not
     */
    public boolean deleteRequestSync(String requestId) {
        Response response = getApiService().deleteRequest(requestId);
        return response.isSuccessful();
    }

    /**
     * Attempts to cancel/delete a request (asynchronously)
     * @param requestId the id of the request
     * @param callback the callback to use when the status of the response has been received
     */
    public void deleteRequestAsync(String requestId, final Callback<Boolean> callback) {
        getApiService().deleteRequest(requestId, new Callback<Response>() {
            @Override
            public void success(Response response, retrofit.client.Response response2) {
                callback.success(response.isSuccessful(), response2);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                callback.failure(retrofitError);
            }
        });
    }

    /**
     * Retrieves the list of products within given coordinates (synchronously)
     * @param latitude the latitude part of the location
     * @param longitude the longitude part of the location
     * @return the list of available products in range
     */
    public List<Product> getProductsSync(double latitude, double longitude) {
        Products products = getApiService().getProducts(latitude, longitude);
        return products.getProducts();
    }

    /**
     * Retrieves the list of products within given coordinates (asynchronously)
     * @param latitude the latitude part of the location
     * @param longitude the longitude part of the location
     * @param callback the callback to use when the whole list of products has been received
     */
    public void getProductsAsync(double latitude, double longitude, final Callback<List<Product>> callback) {
        getApiService().getProducts(latitude, longitude, new Callback<Products>() {
            @Override
            public void success(Products products, retrofit.client.Response response) {
                callback.success(products.getProducts(), response);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                callback.failure(retrofitError);
            }
        });
    }

    /**
     * Retrieves the list of price estimates with the given start and end coordinates (synchronously)
     * @param startLatitude the latitude part of the start location
     * @param startLongitude the longitude part of the start location
     * @param endLatitude the latitude part of the end location
     * @param endLongitude the longitude part of the end location
     * @return the list of price estimates
     */
    public List<Price> getPriceEstimatesSync(double startLatitude, double startLongitude,
                                             double endLatitude, double endLongitude) {
        Prices prices = getApiService().getPriceEstimates(startLatitude, startLongitude, endLatitude, endLongitude);
        return prices.getPrices();
    }

    /**
     * Retrieves the list of price estimates with the given start and end coordinates (asynchronously)
     * @param startLatitude the latitude part of the start location
     * @param startLongitude the longitude part of the start location
     * @param endLatitude the latitude part of the end location
     * @param endLongitude the longitude part of the end location
     * @param callback the callback to use when the whole list of price estimates has been received
     */
    public void getPriceEstimatesAsync(double startLatitude, double startLongitude,
                                       double endLatitude, double endLongitude,
                                       final Callback<List<Price>> callback) {
        getApiService().getPriceEstimates(startLatitude, startLongitude, endLatitude, endLongitude, new Callback<Prices>() {
            @Override
            public void success(Prices prices, retrofit.client.Response response) {
                callback.success(prices.getPrices(), response);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                callback.failure(retrofitError);
            }
        });

    }

    /**
     * Retrieves any possible promotions with the given start and end coordinates (synchronously)
     * @param startLatitude the latitude part of the start location
     * @param startLongitude the longitude part of the start location
     * @param endLatitude the latitude part of the end location
     * @param endLongitude the longitude part of the end location
     * @return the promotion, if it exists
     */
    public Promotion getPromotionsSync(double startLatitude, double startLongitude,
                                       double endLatitude, double endLongitude) {
        Promotion promo = getApiService().getPromotions(startLatitude, startLongitude, endLatitude, endLongitude);
        return promo;
    }

    /**
     * Retrieves any possible promotions with the given start and end coordinates (asynchronously)
     * @param startLatitude the latitude part of the start location
     * @param startLongitude the longitude part of the start location
     * @param endLatitude the latitude part of the end location
     * @param endLongitude the longitude part of the end location
     * @param callback the callback to use when the promotion has been received
     */
    public void getPromotionsAsync(double startLatitude, double startLongitude,
                                   double endLatitude, double endLongitude,
                                   Callback<Promotion> callback) {
        getApiService().getPromotions(startLatitude, startLongitude, endLatitude, endLongitude, callback);
    }

    /**
     * Retrieves the time estimates for the product (synchronously)
     * @param startLatitude the latitude part of the start location
     * @param startLongitude the longitude part of the start location
     * @param customerUuid the uuid of the customer
     * @param productId the id of the product to look up
     * @return the list of time estimates
     */
    public List<Time> getTimeEstimatesSync(double startLatitude, double startLongitude, String customerUuid, String productId) {
        Times times = getApiService().getTimeEstimates(startLatitude, startLongitude, customerUuid, productId);
        return times.getTimes();
    }

    /**
     * Retrieves the time estimates for the product (asynchronously)
     * @param startLatitude the latitude part of the start location
     * @param startLongitude the longitude part of the start location
     * @param customerUuid the uuid of the customer
     * @param productId the id of the product to look up
     * @param callback the callback to use when the whole list of time estimates has been received
     */
    public void getTimeEstimatesAsync(double startLatitude, double startLongitude, String customerUuid, String productId, final Callback<List<Time>> callback) {
        getApiService().getTimeEstimates(startLatitude, startLongitude, customerUuid, productId, new Callback<Times>() {
            @Override
            public void success(Times times, retrofit.client.Response response) {
                callback.success(times.getTimes(), response);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                callback.failure(retrofitError);
            }
        });
    }

    /**
     * Gets the details of a Request given its id (synchronously)
     * @param requestId the id of the request
     * @return the Request object from the id
     */
    public Request getRequestDetailsSync(String requestId) {
        return getApiService().getRequestDetails(requestId);
    }

    /**
     * Gets the details of a Request given its id (asynchronously)
     * @param requestId the id of the request
     * @param callback the callback to use when the Request object has been retrieved
     */
    public void getRequestDetailsAsync(String requestId, Callback<Request> callback) {
        getApiService().getRequestDetails(requestId, callback);
    }

    /**
     * Retrieves information about the authorized user (synchronously)
     * @return the UserProfile object containing info about the authorized user
     */
    public UserProfile getMeSync() {
        return getApiService().getMe();
    }

    /**
     * Retrieves information about the authorized user (asynchronously)
     * @param callback the callback to use when the UserProfile object has been retrieved
     */
    public void getMeAsync(Callback<UserProfile> callback) {
        getApiService().getMe(callback);
    }

    /**
     * Retrieves a limited amount of the history of the user's lifetime interaction with Uber (synchronously)
     * @param offset how much old history items to skip, (i.e offest = 1 will skip oldest history item)
     * @param limit the amount of history items to retrieve, default = 5, max = 50
     * @return the list of UserHistory objects
     */
    public List<UserHistory> getUserHistorySync(int offset, int limit) {
        return getApiService().getUserActivity(offset, limit).getHistory();
    }

    /**
     * Retrieves a limited amount of the history of the user's lifetime interaction with Uber (asynchronously)
     * @param offset how much old history items to skip, (i.e offest = 1 will skip oldest history item)
     * @param limit the amount of history items to retrieve, default = 5, max = 50
     * @param callback the callback to use when the whole list of UserHistory objects ha been retrieved
     */
    public void getUserHistoryAsync(int offset, int limit, final Callback<List<UserHistory>> callback) {
        getApiService().getUserActivity(offset, limit, new Callback<UserActivity>() {
            @Override
            public void success(UserActivity userActivity, retrofit.client.Response response) {
                callback.success(userActivity.getHistory(), response);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                callback.failure(retrofitError);
            }
        });
    }

    /**
     * WARNING: NOT TESTED, IF BROKEN, EMAIL AT tejashah88@gmail.com or msg on facebook to tejashah88
     * Retrieves all of the history of the user's lifetime interaction with Uber (synchronously)
     * @return all of the UserHistory objects
     */
    public List<UserHistory> getAllUserHistorySync() {
        int count = getApiService().getUserActivity(0, 1).getCount();
        List<UserHistory> userHistoryList = new ArrayList<>(count);
        int offset = 0;
        int limit;
        while (count < 0) {
            if (count <= 50)
                limit = count;
            else
                limit = 50;

            List<UserHistory> tmpList = getUserHistorySync(offset, limit);
            userHistoryList.addAll(tmpList);
            offset += limit;
            count -= limit;
        }

        return userHistoryList;
    }

    /**
     * WARNING: NOT TESTED, IF BROKEN, EMAIL AT tejashah88@gmail.com or msg on facebook to tejashah88
     * Retrieves all of the history of the user's lifetime interaction with Uber (asynchronously)
     * @param callback the callback used when the whole list of UserHistory objects has been retrieved
     */
    public void getAllUserHistoryAsync(final Callback<List<UserHistory>> callback) {
        int count = getApiService().getUserActivity(0, 1).getCount();
        final List<UserHistory> userHistoryList = new ArrayList<>(count);

        int offset = 0;
        int limit;
        while (count < 0) {
            if (count <= 50)
                limit = count;
            else
                limit = 50;

            getUserHistoryAsync(offset, limit, new Callback<List<UserHistory>>() {
                @Override
                public void success(List<UserHistory> userHistories, retrofit.client.Response response) {
                    synchronized (this) {
                        userHistoryList.addAll(userHistories);
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    callback.failure(retrofitError);
                }
            });

            offset += limit;
            count -= limit;
        }

        callback.success(userHistoryList, null);
    }

    public void test1() {
        putTestProduct("123456", 1.0f, true);
        simulateRequest("123456", Status.ACCEPTED);

        List<Product> products = getProductsSync(START_LATITUDE, START_LONGITUDE);
        String productId = products.get(0).getProductId();

        Request request = postRequestSync(productId, START_LATITUDE, START_LONGITUDE, END_LATITUDE, END_LONGITUDE);
        while (true) {
            if (getRequestDetailsSync(request.getRequestId()).getStatus() == RequestBody.Status.ACCEPTED)
                break;
            try{Thread.sleep(3000);}catch (Exception ignored) {}
        }
    }
}