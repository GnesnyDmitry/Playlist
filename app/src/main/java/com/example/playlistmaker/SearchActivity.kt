package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.adapter.TrackAdapter
import com.example.playlistmaker.model.Track
import com.example.playlistmaker.okhttp.ITunesSearchApi
import com.example.playlistmaker.okhttp.TrackResponse
import com.example.playlistmaker.preferenceStorage.TrackStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private lateinit var search: EditText
    private lateinit var clearing: ImageView
    private lateinit var placeholderNoConnection: View
    private lateinit var placeholderNothingFound: View
    private lateinit var btnUpdate: Button
    private lateinit var recycler: RecyclerView
    private var searchText: String? = null
    private lateinit var btnClearHistory: Button
    private lateinit var header: TextView

    private val trackList = ArrayList<Track>()

    private val adapter = TrackAdapter(this::setOnClickTrack)

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
        btnClearHistory = findViewById(R.id.btn_clear_history)
        header = findViewById(R.id.header_search_root)

        recycler = findViewById(R.id.recyclerView)
        recycler.adapter = adapter

        adapter.trackList = trackList

        showHistorySearch()

        placeholderNoConnection.isVisible = false
        placeholderNothingFound.isVisible = false

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

                if(s.isNullOrEmpty()) {
                    trackList.clear()
                    adapter.notifyDataSetChanged()
                    hideAllPlaceholders()
                }

                    if (search.hasFocus() && s?.isEmpty() == true) showHistorySearch()
            }

            override fun afterTextChanged(s: Editable?) {
                // Заглушка для будущих задач
            }
        })

        search.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && search.text.isEmpty()) {
                showHistorySearch() // показываем историю
            } else {
                hideHeaderAndFooter() // прячем историю при потере фокуса
            }
        }



        btnClearHistory.setOnClickListener {
            App.instance.trackStorage.clearTracks()
            adapter.trackList.clear()
            adapter.notifyDataSetChanged()
        }

        // Логика для кнопки очистки текста
        clearing.setOnClickListener {
            search.text.clear()
            val hideKeyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            hideKeyboard?.hideSoftInputFromWindow(clearing.windowToken, 0)
            clearing.isVisible = false
        }

        btnUpdate.setOnClickListener {
            if (search.text.isNotEmpty()) {
                hideHeaderAndFooter()
                sendSearchRequest()
            }
        }

        search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (search.text.isNotEmpty()) {
                    hideHeaderAndFooter()
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
                        val body = response.body()
                        trackList.clear()
                        if (body != null && body.results.isNotEmpty()) {
                            trackList.addAll(body.results)
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

    private fun showHistorySearch() {
            header.isVisible = true
            btnClearHistory.isVisible = true
            adapter.trackList.clear()
            adapter.trackList.addAll(App.instance.trackStorage.getTrackList())
            adapter.notifyDataSetChanged()
    }

    private fun hideHeaderAndFooter() {
        btnClearHistory.isVisible = false
        header.isVisible = false
    }

    private fun hideAllPlaceholders() {
        placeholderNoConnection.isVisible = false
        placeholderNothingFound.isVisible = false
    }

    private fun showPlaceholderNoConnection() {
        hideAllPlaceholders()
        placeholderNoConnection.isVisible = true
    }

    private fun showPlaceholderNothingFound() {
        hideAllPlaceholders()
        placeholderNothingFound.isVisible = true
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

    fun setOnClickTrack(track: Track) {
        App.instance.trackStorage.addTrack(track)
    }

    companion object {
        private const val SEARCH_TEXT_KEY = "SEARCH_TEXT"
        private const val NOT_BE_NULL = "response should not be null"
    }
}