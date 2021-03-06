package uniriotec.bruno.onibuscarioca.model;

import com.google.gson.annotations.SerializedName;

public class BusNearby {

    @SerializedName("Modal")
    public String bus ;
    @SerializedName("Line")
    public String line ;
    @SerializedName("Distance")
    public double distance;
    @SerializedName("Gps")
    public Gps gps;

    public class Gps {
        @SerializedName("Latitude")
        public float latitude;
        @SerializedName("Longitude")
        public float longitude;
        @SerializedName("Direction")
        public String direction;
    }
}
