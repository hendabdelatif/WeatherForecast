package com.hend.weatherforecastchallenge.ui.currentcity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hend.weatherforecastchallenge.R
import com.hend.weatherforecastchallenge.model.entities.CityForecast
import com.hend.weatherforecastchallenge.model.repository.Repository
import com.hend.weatherforecastchallenge.ui.currentcity.location.LocationModel
import com.hend.weatherforecastchallenge.utils.Error
import com.hend.weatherforecastchallenge.utils.HasData
import com.hend.weatherforecastchallenge.utils.Loading
import com.hend.weatherforecastchallenge.utils.UIState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class CurrentCityViewModel : ViewModel() {

    private var repository: Repository = Repository()
    private var compositeDisposable = CompositeDisposable()

    /** UIState Live Data **/
    private val _uiState = MutableLiveData<UIState>()
    val uiState: MutableLiveData<UIState> = _uiState

    var locationModel  : LocationModel = LocationModel(0.0,0.0)

    /** City Forecast Live Data **/
    private val _cityForecast = MutableLiveData<CityForecast>()

    /**
     * lazy value to get response only once if no changes happened
     */
    private val cityForecast : MutableLiveData<CityForecast> by lazy {

        compositeDisposable.add(
            repository.getFiveDaysWeather(locationModel.latitude, locationModel.longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnSubscribe {
                    _uiState.postValue(Loading)
                }
                .subscribe({ forecast ->
                    _cityForecast.postValue(forecast)
                    _uiState.postValue(HasData)
                }, {
                    _uiState.postValue(Error(R.string.error_message))
                })
        )
        return@lazy _cityForecast
    }

    fun getCityForecast(): LiveData<CityForecast> = cityForecast

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}