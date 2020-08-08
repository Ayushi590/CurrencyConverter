package com.example.currencyconverter

import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_conversion.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class Conversion : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversion)
    }

    public  fun onconversion(view: View) {
        val usdentered = displayusd.text.toString()
        if (usdentered.isEmpty()){
            Toast.makeText(this,"Please enter the number", Toast.LENGTH_LONG).show()
        }
        else{
            if(isnetworkavailable()){
                convertask().execute(usdentered)
            }
            else
                Toast.makeText(this,"Check your network and try again", Toast.LENGTH_LONG).show()
        }
    }

    fun isnetworkavailable(): Boolean{
        val cmanager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cmanager.activeNetwork
        if (network != null)
            return true

        return false
    }

    inner class convertask:  AsyncTask<String, Void, String>(){
        override fun doInBackground(vararg args: String?): String {
            var result = ""

            val urls = "https://api.exchangeratesapi.io/latest?base=USD&symbols=INR"
            var url = URL(urls)
            val connection = url.openConnection() as HttpURLConnection

            connection.connectTimeout  = 15000
            connection.readTimeout = 15000

            val reader = BufferedReader(InputStreamReader(connection.inputStream))

            result = reader.readLine()
            Log.d("Convert task", "Result: $result")

            return result
        }

        override fun onPostExecute(result: String?) {
            val res = JSONObject(result)
            val thelist = res.getJSONObject("rates")
            val rate = thelist.getDouble("INR")
            val usdenter = displayusd.text.toString()
            val usdfinal = usdenter.toDouble()
            val amount =  rate * usdfinal
            val final = String.format("%.2f", amount)
            displayinr.setText("Rs $final")

            super.onPostExecute(result)

        }

    }

}