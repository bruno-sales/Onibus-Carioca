package uniriotec.bruno.onibuscarioca.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class BusDetails {

    @SerializedName("Line")
    public String line;
    @SerializedName("Bus")
    public String bus;
    @SerializedName("LastUpdateDate")
    public String lastUpdateDate;
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
