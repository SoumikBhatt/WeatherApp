package com.soumik.weatherapp.ui.home.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soumik.weatherapp.app.WeatherApp
import com.soumik.weatherapp.databinding.ActivityHomeBinding
import com.soumik.weatherapp.ui.details.DetailsActivity
import com.soumik.weatherapp.utils.Status
import com.soumik.weatherapp.utils.showSnackBar
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "HomeActivity"
    }

    @Inject
    lateinit var mHomeViewModel: HomeViewModel

    private lateinit var binding: ActivityHomeBinding

    private val mCityListAdapter : CityListAdapter by lazy {
        CityListAdapter {
            Log.d(TAG, "Lat: ${it.coord?.lat}: Ln: ${it.coord?.lon} ")
            startActivity(Intent(this,DetailsActivity::class.java)
                .putExtra(DetailsActivity.WEATHER_DATA,it))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        (application as WeatherApp).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()

        setUpObservers()
        setUpRecyclerView()
    }

    /**
     * setting up the recycler view with adapter
     * to show the city list
     */
    private fun setUpRecyclerView() {
      binding.rvCityList.apply {
          layoutManager = LinearLayoutManager(this@HomeActivity,RecyclerView.VERTICAL,false)
          setHasFixedSize(true)
          adapter = mCityListAdapter
      }
    }

    /**
     * observing the live data from [HomeViewModel]
     * and updating the UI accordingly
     */
    private fun setUpObservers() {
        mHomeViewModel.apply {
            fetchWeatherByCity("23.68","90.35","50")

            isInternetAvailable.observe(this@HomeActivity,{
                Log.d(TAG, "setUpObservers: Connection: $it")
            })

            weatherInfo.observe(this@HomeActivity,{
                when (it.status) {
                    Status.SUCCESS -> {
                        Log.d(TAG, "setUpObservers: Success: ${it.data?.message}")
                        binding.apply {
                            progressCircular.visibility = View.GONE
                            rvCityList.visibility = View.VISIBLE
                        }

                        if (it.data?.list!=null && it.data.list!!.isNotEmpty()) {
                            mCityListAdapter.submitList(it.data.list)
                        } else {
                            mCityListAdapter.submitList(emptyList())
                        }
                    }
                    
                    Status.ERROR -> {
                        Log.e(TAG, "setUpObservers: Error: ${it.message}")
                        showSnackBar(binding.root,it.message!!)
                    }
                    
                    Status.LOADING -> {
                        Log.d(TAG, "setUpObservers: Loading: ")
                        binding.apply {
                            progressCircular.visibility = View.VISIBLE
                            rvCityList.visibility = View.GONE
                        }
                    }
                }
            })
        }
    }
}