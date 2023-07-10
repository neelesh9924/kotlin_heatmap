package com.example.kotlin_heatmap

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.ArrayMap
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import ca.hss.heatmaplib.HeatMap
import com.example.kotlin_heatmap.databinding.FragmentHeatMapBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.security.cert.X509Certificate
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Random
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class HeatMapFragment : Fragment() {

    lateinit var binding: FragmentHeatMapBinding

    var currentModel = ""

    var date = Date()

    var name = ""

    var isLoaded = false

    private val database by lazy {
        AppDatabase.getDatabase(requireContext()).roomDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHeatMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialiseHeatMap()


    }

    private fun initialiseHeatMap() {

        binding.heatmap.setMinimum(0.0)
        binding.heatmap.setMaximum(80.0)


        //make the colour gradient
        val colorStops: ArrayMap<Float, Int> = ArrayMap()
        //blue - 08209D
        colorStops.put(0.1f, 0xff08209d.toInt());
//        //bright blue- 0096FF
        colorStops.put(0.35f, 0xff0096FF.toInt());
        //CYAN -00FFFF
//        colorStops.put(0.3f, 0xff00FFFF.toInt());
        //Robin Egg Blue - 96DED1
//        colorStops.put(0.4f, 0xff08209d.toInt());
        //green - 25DB1B
        colorStops.put(0.5f, 0xff25DB1B.toInt());
        //lemon -B2EF0A
        colorStops.put(0.65f, 0xffB2EF0A.toInt());
        //yell0w - EFDA0A
        colorStops.put(0.8f, 0xffEFDA0A.toInt());
        //orange - EF9E0A
        colorStops.put(0.85f, 0xffEF9E0A.toInt());
//        red- ff0000
        colorStops.put(1.0f, 0xffff0000.toInt());
        binding.heatmap.setColorStops(colorStops)


        loadData();
    }

    private fun loadData() {

        var xArray: ArrayList<Float> = ArrayList()
        var yArray: ArrayList<Float> = ArrayList()
        var valueArray: ArrayList<Double> = ArrayList()


        var rf = getClient()
        var API = rf.create(Retrofit_Interface::class.java)
        var call = API.posts

        call?.enqueue(object : Callback<PostModel?> {
            override fun onResponse(call: Call<PostModel?>, response: Response<PostModel?>) {

                currentModel = response.body().toString()
                isLoaded = true


                response.body()?.data?.forEach(action = {

//                    tempArray.add(it.value.toString())
                    it.x?.let { it1 -> xArray.add(it1.toFloat()) }
                    it.y?.let { it1 -> yArray.add(it1.toFloat()) }
                    it.value?.let { it1 -> valueArray.add(it1.toDouble()) }

                })

                Log.i("responseXFromIP", xArray.toString())
                Log.i("X Size", xArray.size.toString())
                Log.i("responseYFromIP", yArray.toString())
                Log.i("Y Size", yArray.size.toString())
                Log.i("responseValueFromIP", valueArray.toString())
                Log.i("Value Size", valueArray.size.toString())

//                    xDataArray.addAll(xArray)
//                    yDataArray.addAll(yArray)
//                    valueDataArray.addAll(valueArray)

                if (valueArray.size != 0) {
                    val df = DecimalFormat("#.#")
                    var pv1 = valueArray.min()
                    var pv5 = valueArray.max()
                    var pv3 = (pv1 + pv5) / 2
                    var pv2 = (pv1 + pv3) / 2
                    var pv4 = (pv3 + pv5) / 2

                    binding.tv1.setText(df.format(pv1).toString())
                    binding.tv2.setText(df.format(pv2).toString())
                    binding.tv3.setText(df.format(pv3).toString())
                    binding.tv4.setText(df.format(pv4).toString())
                    binding.tv5.setText(df.format(pv5).toString())

                }



                try {
                    for (i in 0..103) {

                        var a = 0.15 + (xArray.get(i) * 0.095)

                        if (a < 0.88) {
                            var b = 0.14

                            if (b <= 0.91) {
                                b += (yArray.get(i) * 0.07)
                            } else {
                                b += (yArray.get(i) * 0.04)
                            }
                            if (b < 1.0) {
                                var value = 0.00
                                if (valueArray.size != 0) {
                                    value = (valueArray.get(i))
                                }


                                //TO SHOW THE COORDINATES

                                val pt = HeatMap.DataPoint(a.toFloat(), b.toFloat(), value!!)
                                binding.heatmap.setRadius(600.0) //to check the points
                                binding.heatmap.addData(pt)
//
//                                    if((a in 0.3..0.5) && (b in 0.84..0.88)) {
//                                        //heel
//                                        val heel = HeatMap.DataPoint(a.toFloat(), b.toFloat(), 700.00)
//                                        heatMap.setRadius(200.0) //to check the points
//                                        heatMap.addData(heel)
//                                    }
//                                    else if((a in 0.4825..0.815) && (b in 0.33..0.88)){
//                                        //lateral
//                                        val lat = HeatMap.DataPoint(a.toFloat(),b.toFloat(),8.00)
//                                        heatMap.setMaxDrawingHeight(100)
//                                        heatMap.setRadius(2000.0) //to check the points
//                                        heatMap.addData(lat)
//                                    }
//                                    else if((a in 0.155..0.25) && (b in 0.25..0.36)){
//                                        //forefoot1
//                                        val forefoot1 = HeatMap.DataPoint(a.toFloat(),b.toFloat(),1000.00)
//                                        heatMap.setRadius(200.0) //to check the points
//                                        heatMap.addData(forefoot1)
//
//                                    }
//                                    else if((a in 0.65..0.78) && (b in 0.25..0.36)){
//                                        //forefoot2
//                                        val forefoot2 = HeatMap.DataPoint(a.toFloat(),b.toFloat(),1000.00)
//                                        heatMap.setRadius(200.0) //to check the points
//                                        heatMap.addData(forefoot2)
//
//                                    }
//                                    else if((a in 0.5..0.6) && (b in 0.25..0.36)){
//                                        //forefootM
//                                        val forefootM = HeatMap.DataPoint(a.toFloat(),b.toFloat(),8.00)
//                                        heatMap.setRadius(100.0) //to check the points
//                                        heatMap.addData(forefootM)
//
//                                    }
////
                            }

                        }
                    }
                } finally {
                    binding.heatmap.forceRefresh()
                }
            }

            override fun onFailure(call: Call<PostModel?>, t: Throwable) {
                Log.i("MainActivity", "failure $t")
                Toast.makeText(requireContext(), "failed", Toast.LENGTH_LONG).show()
            }
        })


        //handling functions are below

        binding.nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                name = p0.toString().trim()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        date = java.sql.Date(System.currentTimeMillis())

        //formatting the date
        binding.timeStamp.text = SimpleDateFormat("dd/MM/yyyy HH:mm").format(date)


        binding.saveToDB.setOnClickListener {

            object : MaterialAlertDialogBuilder(requireContext()){}
                .setTitle("Save to Database?")
                .setMessage("Proceed only when the data is loaded completely?")
                .setPositiveButton("Yes") { dialog, which ->

                    currentModel?.let {

                        lifecycleScope.launch {

                            val data = DBData(0, name, date as java.sql.Date, it)

                            database.insert(data)
                        }

                    }

                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .show()

        }

    }

    //get client - security bypass
    fun getClient(): Retrofit {

        val interceptor = HttpLoggingInterceptor()

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val cookieHandler = CookieManager()

        val trustAllCertificates: Array<TrustManager> = arrayOf(
            object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )

        val sslContext: SSLContext = SSLContext.getInstance("SSL")

        sslContext.init(null, trustAllCertificates, java.security.SecureRandom())

        val sslSocketFact: SSLSocketFactory = sslContext.socketFactory

        val client: OkHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFact, trustAllCertificates[0] as X509TrustManager)
            .hostnameVerifier(HostnameVerifier { s, sslSession -> return@HostnameVerifier true })
            .addNetworkInterceptor(interceptor).addInterceptor(Interceptor.invoke { chain ->
                return@invoke chain.proceed(
                    chain.request().newBuilder().build()
                )
            })
            .cookieJar(JavaNetCookieJar(cookieHandler))
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(Retrofit_Interface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()


    }
}