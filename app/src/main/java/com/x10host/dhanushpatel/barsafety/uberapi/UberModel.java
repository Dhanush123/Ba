package com.x10host.dhanushpatel.barsafety.uberapi;


/**
 * Helper class that overrides the {@link #toString()} method to print pretty json. All models
 * extend this class.
 */
public class UberModel {

    @Override
    public String toString() {
        return UberConstants.GSON.toJson(this);
    }

}
