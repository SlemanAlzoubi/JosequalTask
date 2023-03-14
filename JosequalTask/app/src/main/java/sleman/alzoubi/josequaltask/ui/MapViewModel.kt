package sleman.alzoubi.josequaltask.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sleman.alzoubi.josequaltask.model.MapResult
import sleman.alzoubi.josequaltask.network.ApiMap
import sleman.alzoubi.josequaltask.network.JsonPlaceHolderApi


class MapViewModel: ViewModel() {
    private var _liveDataList:MutableLiveData<List<MapResult>> = MutableLiveData()
     var liveDataList:LiveData<List<MapResult>> = _liveDataList
    init {
        makeApiCall()
    }


    private fun makeApiCall(){
        val instanceApiMap = ApiMap.getInstance()
        val apiservice = instanceApiMap.create(JsonPlaceHolderApi::class.java)
        val call = apiservice.getMapList()
        call.enqueue(object : Callback<List<MapResult>> {
            override fun onResponse(
                call: Call<List<MapResult>>,
                response: Response<List<MapResult>>
            ) {
                _liveDataList.postValue(response.body())
            }

            override fun onFailure(call: Call<List<MapResult>>, t: Throwable) {
                _liveDataList.postValue(null)
            }

        })

    }
}