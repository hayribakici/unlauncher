package com.sduduzog.slimlauncher.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sduduzog.slimlauncher.data.BaseDao
import com.sduduzog.slimlauncher.data.model.App
import javax.inject.Inject

class MainViewModel @Inject constructor(baseDao: BaseDao) : ViewModel() {

    private val _baseRepository = Repository(baseDao)

    private var _apps: LiveData<List<HomeApp>>

    init {
        _apps = _baseRepository.apps
    }

    val apps: LiveData<List<HomeApp>>
        get() = _apps

    val addAppViewModel = AddAppViewModel(baseDao)

    fun add(app: App) {
        val index = _apps.value!!.size
        _baseRepository.add(HomeApp.from(app, index))
    }

    // This is needed temporarily so that the HomeFragment can transition the max number of home apps
    fun remove(app: HomeApp) {
        _baseRepository.remove(app)
    }
}