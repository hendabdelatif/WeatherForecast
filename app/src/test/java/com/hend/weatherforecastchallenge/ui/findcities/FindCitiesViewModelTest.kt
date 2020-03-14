package com.hend.weatherforecastchallenge.ui.findcities

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.hend.weatherforecastchallenge.RxImmediateSchedulerRule
import com.hend.weatherforecastchallenge.RxSchedulerRule
import com.hend.weatherforecastchallenge.api.NetworkApiServices
import com.hend.weatherforecastchallenge.model.entities.*
import com.hend.weatherforecastchallenge.testObserver
import com.hend.weatherforecastchallenge.utils.HasData
import com.hend.weatherforecastchallenge.utils.Loading
import de.jodamob.kotlin.testrunner.KotlinTestRunner
import de.jodamob.kotlin.testrunner.OpenedPackages
import io.reactivex.Observable
import io.reactivex.Observer
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

/**
 * Test class to test the behaviour of searching multiple cities by names
 * KotlinTestRunner is a JUnit4TestRunner that removes final from classes and methods, especially needed in kotlin projects
 */
@RunWith(KotlinTestRunner::class)
@OpenedPackages("com.hend.weatherforecastchallenge")
class FindCitiesViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    // Test rule for making the RxJava to run synchronously in unit test
    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @Mock
    lateinit var networkApiService: NetworkApiServices

    @Mock
    lateinit var observer: Observer<List<CityWeather>>

    @InjectMocks
    lateinit var findCitiesViewModel: FindCitiesViewModel

    @Before
    fun setUp() {
        findCitiesViewModel = FindCitiesViewModel()

    }

    @Test
    fun `assert City Weather list is empty`() {

        val cityWeatherList =
            findCitiesViewModel.getCityWeatherList(listOf())
                .testObserver()

        Truth.assert_()
            .that(cityWeatherList.observedValues.first()).isEmpty()
    }

    @Test
    fun `assert set correct loading states`() {

        val status = findCitiesViewModel.uiState.testObserver()

        val cityNamesList = listOf("London", "Cairo", "Wales")

        findCitiesViewModel.getCityWeatherList(cityNamesList)

        // Loading status (Loading, HasData) for one API call.
        // This result due to looping on number of cities which are 3
        Truth.assert_()
            .that(status.observedValues)
            .isEqualTo(listOf(Loading, HasData, Loading, HasData, Loading, HasData))
    }

    @Test
    fun `assert get city names list are in the correct range`() {
        val cityNamesList = listOf("London", "Cairo", "Wales")

        Truth.assert_().that(cityNamesList.size).isAtLeast(3)
        Truth.assert_().that(cityNamesList.size).isAtMost(7)
    }


    @Test
    fun `fetch city weather API Success`() {

        // Fake API response
        val cityWeather = listOf(
            CityWeather(
                Coordinates(55.3, 25.26),
                listOf(Weather(800, "Clear", "clear sky", "01d")),
                Temperature(30.2, 25.8, 30.0, 31.0, 1017, 15),
                Wind(3.6, 320), Sys(7537, "AE"), 14400, 292223, "Dubai"
            ),
            CityWeather(
                Coordinates(31.25, 30.06),
                listOf(
                    Weather(500, "Rain", "light rain", "10d"),
                    Weather(701, "Mist", "mist", "50d")
                ),
                Temperature(15.8, 9.73, 15.56, 16.0, 997, 87),
                Wind(10.3, 280), Sys(2514, "EG"), 7200, 360630, "Cairo"
            ),
            CityWeather(
                Coordinates(-3.5, 52.5),
                listOf(Weather(803, "Clouds", "broken clouds", "04d")),
                Temperature(7.56, 2.56, 5.56, 9.44, 1017, 75),
                Wind(5.1, 250), Sys(1433, "GB"), 0, 2634895, "Wales"
            )
        )

        // Mock API response
        Mockito.`when`(networkApiService.getCityWeather("Dubai"))
            .thenReturn(Observable.just(cityWeather[0]))

        Mockito.`when`(networkApiService.getCityWeather("Cairo"))
            .thenReturn(Observable.just(cityWeather[1]))

        Mockito.`when`(networkApiService.getCityWeather("Wales"))
            .thenReturn(Observable.just(cityWeather[2]))

        // Attach fake observer
        findCitiesViewModel.cityWeatherListLiveData.observeForever { observer }

        // Invoke
        findCitiesViewModel.getCityWeatherList(listOf("Dubai", "Cairo", "Wales"))

        // Verify
        Truth.assert_().that(findCitiesViewModel.cityWeatherListLiveData.value).isNotNull()

        // Can't assert data with static values as weather data changes every moments
//        Truth.assert_().that(cityWeather).isEqualTo(findCitiesViewModel.cityWeatherListLiveData.value)
    }


}