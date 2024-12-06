package com.example.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.search.data.network.ITunesSearchApi
import com.example.playlistmaker.search.presentation.SearchView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), SearchView {

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
    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { sendSearchRequest() }

    private val adapter = TrackAdapter()

    private val presenter = Creator.createSearchPresenter(
        this,
        SearchRouter(this),
    )

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
        toolbar = findViewById(R.id.search_root_toolbar)
        progressBar = findViewById(R.id.progressBar)


        recycler = findViewById(R.id.recyclerView)
        recycler.adapter = adapter

        placeholderNoConnection.isVisible = false
        placeholderNothingFound.isVisible = false

        adapter.action = { track ->
            App.instance.trackStorage.addTrack(track)
            presenter.onClickedTrack(track)
        }

        toolbar.setNavigationOnClickListener { finish() }

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
                if (s.isNullOrEmpty()) {
                    showHistorySearch()
                    hideAllPlaceholders()
                } else {
                    hideHeaderAndFooter()
                    adapter.trackList.clear()
                }
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                // Заглушка для будущих задач
            }
        })

        search.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && search.text.isEmpty()) {
                showHistorySearch()
            }
        }

        btnClearHistory.setOnClickListener {
            val hideKeyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            hideKeyboard?.hideSoftInputFromWindow(clearing.windowToken, 0)
            hideHeaderAndFooter()
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
            showHistorySearch()
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
        if (search.text.isNotEmpty()) {

            progressBar.isVisible = true
            recycler.isVisible = false

            presenter.searchTrack(search.text.toString())
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, POST_DELAY_SEARCH_DEBOUNCE)
    }

    private fun showHistorySearch() {
        val trackList = App.instance.trackStorage.getTrackList()
        if (trackList.isNotEmpty()) {
            header.isVisible = true
            btnClearHistory.isVisible = true
            adapter.trackList.clear()
            adapter.trackList.addAll(trackList)
            adapter.notifyDataSetChanged()
        }
    }

    override fun showTrackList() {
        progressBar.isVisible = false
        recycler.isVisible = true
    }

    override fun setTrackList(bodyResponse: List<Track>) {
        adapter.trackList.addAll(bodyResponse)
        adapter.notifyDataSetChanged()
        hideAllPlaceholders()
    }

    override fun showLoadingTrackList() {
        progressBar.isVisible = true
        recycler.isVisible = false
    }

    override fun hideHeaderAndFooter() {
        btnClearHistory.isVisible = false
        header.isVisible = false
    }

    override fun hideAllPlaceholders() {
        placeholderNoConnection.isVisible = false
        placeholderNothingFound.isVisible = false
    }

    override fun showPlaceholderNoConnection() {
        hideAllPlaceholders()
        placeholderNoConnection.isVisible = true
    }

    override fun showPlaceholderNothingFound() {
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

    companion object {
        private const val SEARCH_TEXT_KEY = "SEARCH_TEXT"
        const val TRACK_KEY = "track"
        private const val POST_DELAY_SEARCH_DEBOUNCE = 2000L
    }
}