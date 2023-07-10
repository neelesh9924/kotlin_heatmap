package com.example.kotlin_heatmap
import com.google.gson.annotations.SerializedName

class PostModel {

    @SerializedName("chip_id" ) var chipId : String?         = null
    @SerializedName("data"    ) var data   : ArrayList<Data> = arrayListOf()


    data class Data (

        @SerializedName("x"     ) var x     : Float?    = null,
        @SerializedName("y"     ) var y     : Float?    = null,
        @SerializedName("value" ) var value : Double? = null

    )
}