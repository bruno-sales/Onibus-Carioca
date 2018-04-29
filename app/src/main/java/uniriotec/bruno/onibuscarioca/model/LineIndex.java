package uniriotec.bruno.onibuscarioca.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class LineIndex {

    @SerializedName("Line")
    public String line;
    @SerializedName("Name")
    public String name;
    @SerializedName("LastUpdateDate")
    public String lastUpdateDate;
}
