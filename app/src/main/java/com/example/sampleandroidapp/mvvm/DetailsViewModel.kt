package com.example.sampleandroidapp.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailsViewModel : ViewModel() {

    val dataFetched: MutableLiveData<DataModel> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val message: MutableLiveData<String> = MutableLiveData()

    fun fetchData() {
        isLoading.value = true
        message.value = "Fetching Data"

        ApiCall().getdetails { details ->
            dataFetched.postValue(details)
            isLoading.postValue(false)
            message.postValue("Data Fetched")
        }
    }
}
