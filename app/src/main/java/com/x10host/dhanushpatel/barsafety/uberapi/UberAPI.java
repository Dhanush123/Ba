package com.x10host.dhanushpatel.barsafety.uberapi;

import com.victorsima.uber.UberAuthService;
import com.victorsima.uber.UberClient;
import com.victorsima.uber.UberService;
import com.victorsima.uber.model.Product;
import com.victorsima.uber.model.Products;
import com.victorsima.uber.model.request.Request;
import com.victorsima.uber.model.request.RequestBody;
import com.victorsima.uber.model.request.RequestBody.Status;
import com.victorsima.uber.model.sandbox.SandboxProductBody;
import com.victorsima.uber.model.sandbox.SandboxRequestBody;
import com.victorsima.uber.model.sandbox.SandboxService;

import java.util.List;

import retrofit.RestAdapter;

/**
 * Created by Tehas_2 on 7/18/2015.
 */
public class UberAPI {
    private static String CLIENT_ID = null;
    private static String ClIENT_SECRET = null;
    private static String SERVER_TOKEN = null;

    public static final double START_LATITUDE   = 40.74844;
    public static final double START_LONGITUDE  = -73.985664;
    public static final double END_LATITUDE     = 40.74844;
    public static final double END_LONGITUDE    = -73.985664;

    private static UberClient client;

    public static void initialize(String clientId, String clientSecret, String serverToken,
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
     * Put a test product to simulate an actual product
     * @param productId the id of the product
     * @param surgeMultiplier the surge multiplier to put effect on the product
     * @param driversAvailable if drivers are available or not
     */
    public void putTestProduct(String productId, float surgeMultiplier, boolean driversAvailable) {
        getSandboxService().putProducts(productId, new SandboxProductBody(surgeMultiplier, driversAvailable));
    }

    /**
     * Sends a simulated request to the product given its ID
     * @param productId the id of the product
     * @param status the status of the request
     */
    public void simulateRequest(String productId, Status status) {
        getSandboxService().putRequest(productId, new SandboxRequestBody(status));
    }

    public List<Product> getProductsInRange(double latitude, double longitude) {
        Products products = getApiService().getProducts(latitude, longitude);
        return products.getProducts();
    }

    public void test1() {

        Products products = client.getApiService().getProducts(START_LATITUDE, START_LONGITUDE);
        String productId = products.getProducts().get(0).getProductId();

        RequestBody body = new RequestBody(productId, START_LATITUDE, START_LONGITUDE, END_LATITUDE, END_LONGITUDE, null);

        Request request = client.getApiService().postRequest(body);
        while (true) {
            if (client.getApiService().getRequestDetails(productId).getStatus() == RequestBody.Status.ACCEPTED)
                break;
            try{Thread.sleep(3000);}catch (Exception ex) {}
        }
        //client.getAuthService().requestAccessToken();
    }
}