package uniriotec.bruno.onibuscarioca.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class LineDetails {

    @SerializedName("Line")
    public String Line;
    @SerializedName("Name")
    public String Name;
    @SerializedName("LastUpdateDate")
    public String lastUpdateDate;
    //@SerializedName("Buses")
    //public List<LineBus> buses;

    /*public class LineBus
    {
        @SerializedName("Bus")
        public String bus;
        @SerializedName("LastUpdateDate")
        public String lastUpdateDate;
    }*/
}

