package com.hend.weatherforecastchallenge.ui.currentcity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hend.weatherforecastchallenge.R
import com.hend.weatherforecastchallenge.model.entities.Forecast
import com.hend.weatherforecastchallenge.ui.currentcity.adapter.CurrentCityAdapter
import com.hend.weatherforecastchallenge.ui.currentcity.location.GpsUtils
import com.hend.weatherforecastchallenge.ui.currentcity.location.LocationLiveData
import com.hend.weatherforecastchallenge.ui.currentcity.location.LocationModel
import com.hend.weatherforecastchallenge.utils.Error
import com.hend.weatherforecastchallenge.utils.HasData
import com.hend.weatherforecastchallenge.utils.Loading
import kotlinx.android.synthetic.main.fragment_current_city.*
import java.util.*

class CurrentCityFragment : Fragment() {

    private lateinit var adapter: CurrentCityAdapter
    private lateinit var currentCityViewModel: CurrentCityViewModel
    private lateinit var locationLiveData: LiveData<LocationModel>

    companion object {
        const val LOCATION_REQUEST = 100
        const val GPS_REQUEST = 101
        private var isGPSEnabled = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationLiveData = LocationLiveData(activity as AppCompatActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.fragment_current_city,
            container, false
        )

        currentCityViewModel = ViewModelProvider(this).get(CurrentCityViewModel::class.java)

        init(view)

        return view
    }

    private fun init(view: View) {

        initAdapter(view)
        handleUIState()
        GpsUtils(activity as AppCompatActivity).turnGPSOn(object : GpsUtils.OnGpsListener {

            override fun gpsStatus(isGPSEnable: Boolean) {
                isGPSEnabled = isGPSEnable
            }
        })
    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }

    private fun initAdapter(view: View) {
        val initData = mutableListOf<Forecast>()
        adapter = CurrentCityAdapter(initData)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager =
            LinearLayoutManager(activity as AppCompatActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    private fun handleUIState() {
        currentCityViewModel.uiState.observe(viewLifecycleOwner, Observer { state ->

            when (state) {
                Loading -> {
                    progress_bar.visibility = View.VISIBLE
                    tv_error.visibility = View.GONE
                    constraint_layout.visibility = View.INVISIBLE

                }
                HasData -> {
                    progress_bar.visibility = View.GONE
                    tv_error.visibility = View.GONE
                    constraint_layout.visibility = View.VISIBLE

                }
                is Error -> {
                    val textState = tv_error.context.getString(state.errorMsgId) to View.VISIBLE
                    tv_error.text = textState.first
                    tv_error.visibility = textState.second
                    constraint_layout.visibility = View.INVISIBLE
                    progress_bar.visibility = View.GONE
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPSEnabled = true
                invokeLocationAction()
            }
        }
    }

    private fun invokeLocationAction() {
        when {
            !isGPSEnabled -> currentCityViewModel.uiState.postValue(Error(R.string.enable_gps))

            isPermissionsGranted() -> startLocationUpdate()

            shouldShowRequestPermissionRationale() -> showPermissionAlertDialog()

            else -> requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), LOCATION_REQUEST
            )
        }
    }

    private fun startLocationUpdate() {
        locationLiveData.observe(this, Observer {
            currentCityViewModel.locationModel = LocationModel(it.latitude, it.longitude)
            currentCityViewModel.getCityForecast().observe(this, Observer {
                tv_city_name.text =
                    "%s, %s".format(Locale.getDefault(), it.city.name, it.city.country)

                adapter.items = it.list.toMutableList()
            })
        })


    }

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            activity as AppCompatActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    activity as AppCompatActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            activity as AppCompatActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            activity as AppCompatActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showPermissionAlertDialog()
                } else {
                    invokeLocationAction()
                }

            }
        }
    }

    private fun showPermissionAlertDialog() {
        currentCityViewModel.uiState.postValue(Error(R.string.permission_request))
        val builder = AlertDialog.Builder(activity as AppCompatActivity)
        builder.setMessage(activity!!.getString(R.string.permission_request))
            .setTitle(activity!!.getString(R.string.permission_required))

        builder.setPositiveButton(
            "OK"
        ) { dialog, id ->
            dialog.dismiss()
            forceStartAppSettings()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun forceStartAppSettings(){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri: Uri = Uri.fromParts("package", activity!!.packageName, null)
        intent.data = uri
        startActivity(intent)
    }

}