package uniriotec.bruno.onibuscarioca.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class BusIndex {
    @SerializedName("Line")
    public String line;
    @SerializedName("Modal")
    public String bus;
    @SerializedName("LastUpdateDate")
    public String lastUpdateDate;
}
