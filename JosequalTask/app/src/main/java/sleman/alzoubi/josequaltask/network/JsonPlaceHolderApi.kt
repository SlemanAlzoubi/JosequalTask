package sleman.alzoubi.josequaltask.network

import retrofit2.Call
import retrofit2.http.GET
import sleman.alzoubi.josequaltask.model.MapResult

interface JsonPlaceHolderApi {
    @GET("/maps.php")
    fun getMapList(): Call<List<MapResult>>
}