package com.example.chooseyourownapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.bumptech.glide.Glide
import okhttp3.Headers
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var videoTitle: TextView
    private lateinit var channelName: TextView
    private lateinit var thumbnailImage: ImageView
    private lateinit var newVideoBtn: Button

    private val API_KEY = "AIzaSyCV_BcG_IbROnlRpNhBt9EyIwb9I0v8mP8"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        videoTitle = findViewById(R.id.videoTitle)
        channelName = findViewById(R.id.channelName)
        thumbnailImage = findViewById(R.id.thumbnailImage)
        newVideoBtn = findViewById(R.id.newVideoBtn)

        fetchRandomVideo()

        newVideoBtn.setOnClickListener {
            fetchRandomVideo()
        }
    }

    private fun fetchRandomVideo() {
        val client = AsyncHttpClient()
        val queryList = listOf("gaming", "laughter", "team", "gameplay", "technology", "animation")
        val randomQuery = queryList.random()

        val url = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q=$randomQuery&key=$API_KEY"

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                val items = json.jsonObject.getJSONArray("items")
                if (items.length() > 0) {
                    val video = items.getJSONObject(0)
                    val snippet = video.getJSONObject("snippet")
                    val title = snippet.getString("title")
                    val channel = snippet.getString("channelTitle")
                    val thumbnailUrl = snippet.getJSONObject("thumbnails")
                        .getJSONObject("medium")
                        .getString("url")

                    videoTitle.text = title
                    channelName.text = channel
                    Glide.with(this@MainActivity).load(thumbnailUrl).into(thumbnailImage)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                videoTitle.text = "Error: Could not load video."
                channelName.text = ""
            }
        })
    }
}