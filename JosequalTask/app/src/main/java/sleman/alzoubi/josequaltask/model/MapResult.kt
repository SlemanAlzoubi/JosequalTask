package sleman.alzoubi.josequaltask.model

import com.google.gson.annotations.SerializedName


data class MapResult(
    @SerializedName("name") var name : String? = null,
    @SerializedName("lat") var lat  : String? = null,
    @SerializedName("lng") var lng  : String? = null

)

