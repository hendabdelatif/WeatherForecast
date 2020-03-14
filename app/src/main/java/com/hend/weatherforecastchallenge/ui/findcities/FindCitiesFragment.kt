package com.hend.weatherforecastchallenge.ui.findcities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.hend.weatherforecastchallenge.R
import com.hend.weatherforecastchallenge.model.entities.CityWeather
import com.hend.weatherforecastchallenge.ui.findcities.adapter.FindCitiesAdapter
import com.hend.weatherforecastchallenge.utils.*
import kotlinx.android.synthetic.main.fragment_find_cities.*


class FindCitiesFragment : Fragment() {

    private lateinit var adapter: FindCitiesAdapter
    private lateinit var findCitiesViewModel: FindCitiesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(
            R.layout.fragment_find_cities,
            container, false
        )
        findCitiesViewModel = ViewModelProvider(this).get(FindCitiesViewModel::class.java)

        init(view)

        return view
    }

    private fun init(view: View) {

        initAdapter(view)
        handleUIState()
        observeViewModel()

        val tvInputField = view.findViewById<TextInputEditText>(R.id.et_input_city_names)
        tvInputField.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    startSearch()
                    true
                }
                else -> false
            }
        }
    }

    private fun observeViewModel() {
        findCitiesViewModel.cityNamesListLiveData.observe(viewLifecycleOwner, Observer {
            cityWeatherList(it)
        })

        findCitiesViewModel.cityWeatherListLiveData.observe(viewLifecycleOwner, Observer {
            adapter.items = it.toMutableList()
        })
    }

    private fun cityWeatherList(list: List<String>) = findCitiesViewModel.getCityWeatherList(list)

    private fun startSearch() {

        findCitiesViewModel.uiState.postValue(Loading)
        val cityWeatherList = et_input_city_names.text.toString().getCityNamesList()

        findCitiesViewModel.cityNamesListLiveData.postValue(cityWeatherList)

        hideKeyboard()
    }

    private fun initAdapter(view: View) {
        val initData = mutableListOf<CityWeather>()
        adapter =
            FindCitiesAdapter(
                initData
            )
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager =
            LinearLayoutManager(activity as AppCompatActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    private fun handleUIState() {
        findCitiesViewModel.uiState.observe(viewLifecycleOwner, Observer { state ->

            when (state) {
                Loading -> {
                    progress_bar.visibility = View.VISIBLE
                    tv_error.visibility = View.GONE
                    recycler_view.visibility = View.GONE

                }
                HasData -> {
                    progress_bar.visibility = View.GONE
                    tv_error.visibility = View.GONE
                    recycler_view.visibility = View.VISIBLE

                }
                is Error -> {
                    val textState = tv_error.context.getString(state.errorMsgId) to View.VISIBLE
                    tv_error.text = textState.first
                    tv_error.visibility = textState.second
                    recycler_view.visibility = View.GONE
                    progress_bar.visibility = View.GONE
                }
            }
        })
    }

}