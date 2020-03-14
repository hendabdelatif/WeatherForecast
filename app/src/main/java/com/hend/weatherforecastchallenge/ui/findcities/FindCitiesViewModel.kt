package com.hend.weatherforecastchallenge.ui.findcities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hend.weatherforecastchallenge.R
import com.hend.weatherforecastchallenge.model.entities.CityWeather
import com.hend.weatherforecastchallenge.model.repository.Repository
import com.hend.weatherforecastchallenge.utils.Error
import com.hend.weatherforecastchallenge.utils.HasData
import com.hend.weatherforecastchallenge.utils.Loading
import com.hend.weatherforecastchallenge.utils.UIState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class FindCitiesViewModel : ViewModel() {

    private var repository: Repository = Repository()
    private var compositeDisposable = CompositeDisposable()

    /** UIState Live Data **/
    private val _uiState = MutableLiveData<UIState>()
    val uiState: MutableLiveData<UIState> = _uiState

    /** City Names List Live Data **/
    private val _cityNamesListLiveData = MutableLiveData<List<String>>()
    val cityNamesListLiveData: MutableLiveData<List<String>> = _cityNamesListLiveData

    /** City Weather List Live Data **/
    private val _cityWeatherListLiveData = MutableLiveData<List<CityWeather>>()
    val cityWeatherListLiveData: MutableLiveData<List<CityWeather>> = _cityWeatherListLiveData


    init {
        _cityWeatherListLiveData.postValue(emptyList())
    }

    /**
     * Repeat search city by name according to the number of cities input coming synchronously
     */
    fun getCityWeatherList(cityNamesList: List<String>): LiveData<List<CityWeather>> {

        val weatherList = mutableListOf<CityWeather>()

        if (cityNamesList.size < 3 || cityNamesList.size > 7) {
            _uiState.postValue(Error(R.string.instructions_find_cities))

        } else {
            for (cityName in cityNamesList) {
                compositeDisposable.add(
                    repository.getCityWeather(cityName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .doOnSubscribe {
                            _cityWeatherListLiveData.postValue(emptyList())
                            _uiState.postValue(Loading)
                        }
                        .subscribe({ cityWeather ->
                            weatherList.add(cityWeather)
                            _cityWeatherListLiveData.postValue(weatherList)
                            _uiState.postValue(HasData)
                        }, {
                            _uiState.postValue(Error(R.string.error_message))
                        })
                )
            }
        }
        return _cityWeatherListLiveData
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}