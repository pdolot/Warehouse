package com.example.patryk.warehouse.Models;

import com.google.gson.annotations.SerializedName;

public class LocationsToChange {
    @SerializedName("location")
    private Location location;
    @SerializedName("oldLocation")
    private Location oldLocation;

    public LocationsToChange(Location location, Location oldLocation) {
        this.location = location;
        this.oldLocation = oldLocation;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getOldLocation() {
        return oldLocation;
    }

    public void setOldLocation(Location oldLocation) {
        this.oldLocation = oldLocation;
    }
}
