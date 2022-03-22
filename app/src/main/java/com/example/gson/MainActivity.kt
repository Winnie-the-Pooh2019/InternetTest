package com.example.gson

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okio.IOException
import timber.log.Timber
import timber.log.Timber.Forest.plant
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        plant(Timber.DebugTree())


        findViewById<Button>(R.id.button).setOnClickListener(::clickOk)
    }
}

fun clickHttp(view: View) {
    CoroutineScope(Dispatchers.IO).launch {
        val url = URL(view.context.getString(R.string.link))
        val connection = withContext(Dispatchers.IO) {
            url.openConnection()
        } as HttpURLConnection

        try {
            Timber.tag("Flickr cats").d(connection.inputStream.bufferedReader().readText())
        } catch (e: Exception) {
            Timber.e(e)
        } finally {
            connection.disconnect()
        }
    }
}

fun clickOk(view: View) {
    val request = Request.Builder()
        .url(URL(view.context.getString(R.string.link))).build()

    OkHttpClient().newCall(request).enqueue(object: Callback {
        override fun onFailure(call: Call, e: IOException) {
            Timber.e(e)
        }

        override fun onResponse(call: Call, response: Response) {
            Timber.tag("Flickr OkCats").i(response.body?.string().toString())
        }
    })
}