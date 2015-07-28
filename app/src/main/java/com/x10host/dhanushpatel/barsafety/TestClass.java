package com.x10host.dhanushpatel.barsafety;

import android.util.Log;

import com.x10host.dhanushpatel.barsafety.uberapi.Product;
import com.x10host.dhanushpatel.barsafety.uberapi.ProductList;
import com.x10host.dhanushpatel.barsafety.uberapi.UberAPIClient;
import com.x10host.dhanushpatel.barsafety.uberapi.UberAuthTokenClient;
import com.x10host.dhanushpatel.barsafety.uberapi.UberConstants;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Tehas_2 on 7/17/2015.
 */
public class TestClass {
    //used for testing random shit
    public void testGetProducts() {
        UberAPIClient.getUberV1APIClient().getProducts("",
                UberConstants.START_LATITUDE,
                UberConstants.START_LONGITUDE,
                new Callback<ProductList>() {
            @Override
            public void success(ProductList productList, Response response) {
                for (Product product : productList.getProducts()) {
                    Log.d("UBER DEBUG", product.toString());
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                retrofitError.printStackTrace();
            }
        });
    }

    public void test() {
        UberAPIClient.getUberV1APIClient();
        //UberAuthTokenClient.getUberAuthTokenClient().getAuthToken();
    }
}