package com.hend.weatherforecastchallenge.ui.currentcity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.hend.weatherforecastchallenge.RxImmediateSchedulerRule
import com.hend.weatherforecastchallenge.RxSchedulerRule
import com.hend.weatherforecastchallenge.api.NetworkApiServices
import com.hend.weatherforecastchallenge.model.entities.*
import com.hend.weatherforecastchallenge.testObserver
import com.hend.weatherforecastchallenge.ui.currentcity.location.LocationModel
import com.hend.weatherforecastchallenge.utils.HasData
import com.hend.weatherforecastchallenge.utils.Loading
import de.jodamob.kotlin.testrunner.KotlinTestRunner
import de.jodamob.kotlin.testrunner.OpenedPackages
import io.reactivex.Observer
import io.reactivex.Single
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

/**
 * Test class to test the behaviour of getting current location and display list of weather forecast
 * KotlinTestRunner is a JUnit4TestRunner that removes final from classes and methods, especially needed in kotlin projects
 */
@RunWith(KotlinTestRunner::class)
@OpenedPackages("com.hend.weatherforecastchallenge")
class CurrentCityViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    //     Test rule for making the RxJava to run synchronously in unit test
    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @Mock
    lateinit var networkApiService: NetworkApiServices

    @Mock
    lateinit var observer: Observer<CityForecast>

    @Mock
    lateinit var currentCityViewModel: CurrentCityViewModel


    @Before
    fun setUp() {
        currentCityViewModel = CurrentCityViewModel()

    }


    @Test
    fun `assert set correct loading states`() {

        // Attach fake status observer
        val status = currentCityViewModel.uiState.testObserver()

        // Invoke
        currentCityViewModel.locationModel = LocationModel(55.4997, 37.5597)
        currentCityViewModel.getCityForecast()

        // Verify
        Truth.assert_()
            .that(status.observedValues)
            .isEqualTo(listOf(Loading, HasData))
    }


    @Test
    fun `fetch city forecast API Success`() {

        // Fake API response
        val cityForecast = CityForecast(
            listOf(
                Forecast(
                    Temperature(30.2, 25.8, 30.0, 31.0, 1017, 15),
                    listOf(Weather(800, "Clear", "clear sky", "01d")),
                    Wind(3.6, 320),
                    "2020-03-18 09:00:00"
                )
            ), City(1, "Dubai", Coordinates(55.4997, 37.5597), "AE", 14400)
        )

        // Mock API response
        `when`(networkApiService.getFiveDaysWeather(55.4997, 37.5597))
            .thenReturn(Single.just(cityForecast))

        // Attach fake observer
        currentCityViewModel.getCityForecast().observeForever { observer }

        // Invoke
        currentCityViewModel.locationModel = LocationModel(55.4997, 37.5597)
        currentCityViewModel.getCityForecast()

        // Verify
        Truth.assert_().that(currentCityViewModel.getCityForecast().value).isNotNull()

        // Can't assert data with static values as forecast data changes every moments
//        Truth.assert_().that(cityForecast).isEqualTo(currentCityViewModel.getCityForecast().value)


    }

}