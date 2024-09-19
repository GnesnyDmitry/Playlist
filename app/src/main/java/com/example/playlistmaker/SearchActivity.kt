package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_TEXT_KEY = "SEARCH_TEXT"
        private const val NOT_BE_NULL = "response should not be null"
    }

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private lateinit var search: EditText
    private lateinit var clearing: ImageView
    private lateinit var placeholderNoConnection: View
    private lateinit var placeholderNothingFound: View
    private lateinit var btnUpdate: Button
    private lateinit var recycler: RecyclerView
    private var searchText: String? = null

    private val trackList = ArrayList<Track>()

    private val adapter = TrackAdapter()

    private val retrofit =
        Retrofit.Builder().baseUrl(iTunesBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()

    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        search = findViewById(R.id.edittext_search_root)
        clearing = findViewById(R.id.icon_clear_text)
        placeholderNoConnection = findViewById(R.id.placeholder_no_connection)
        placeholderNothingFound = findViewById(R.id.placeholder_nothing_found)
        btnUpdate = findViewById(R.id.btn_update)

        recycler = findViewById(R.id.recyclerView)
        recycler.adapter = adapter

        adapter.trackList = trackList

        placeholderNoConnection.visibility = View.GONE
        placeholderNothingFound.visibility = View.GONE

        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(SEARCH_TEXT_KEY)
            search.setText(searchText)
        }

        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Заглушка для будущих задач
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearing.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                searchText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // Заглушка для будущих задач
            }
        })

        // Логика для кнопки очистки текста
        clearing.setOnClickListener {
            search.text.clear()
            val hideKeyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            hideKeyboard?.hideSoftInputFromWindow(clearing.windowToken, 0)
            clearing.isVisible = false
        }

        btnUpdate.setOnClickListener {
            if (search.text.isNotEmpty()) {
                sendSearchRequest()
            }
        }

        search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (search.text.isNotEmpty()) {
                    sendSearchRequest()
                }
                true
            }
            false
        }
    }

    private fun sendSearchRequest() {
        iTunesService.search(search.text.toString())
            .enqueue(object : Callback<TrackResponse> {

                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.isSuccessful) {
                        trackList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            val newTrackList =
                                requireNotNull(response.body()?.results) { "$NOT_BE_NULL}" }
                            trackList.addAll(newTrackList)
                            adapter.notifyDataSetChanged()
                            hideAllPlaceholders()
                        }
                        if (trackList.isEmpty()) {
                            showPlaceholderNothingFound() // если треков не найдено
                        }
                    } else {
                        showPlaceholderNoConnection() // если возникла ошибка
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showPlaceholderNoConnection() // если произошла ошибка
                }
            })
    }

    private fun hideAllPlaceholders() {
        placeholderNoConnection.visibility = View.GONE
        placeholderNothingFound.visibility = View.GONE
    }

    private fun showPlaceholderNoConnection() {
        hideAllPlaceholders()
        placeholderNoConnection.visibility = View.VISIBLE
    }

    private fun showPlaceholderNothingFound() {
        hideAllPlaceholders()
        placeholderNothingFound.visibility = View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT_KEY, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT_KEY)
        search.setText(searchText)
    }
}