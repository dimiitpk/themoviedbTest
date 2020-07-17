package com.dimi.themoviedb.ui.main.tv_show

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.dimi.themoviedb.R
import com.dimi.themoviedb.models.TVShow
import com.dimi.themoviedb.ui.main.tv_show.adapters.TvShowListAdapter
import com.dimi.themoviedb.ui.main.tv_show.viewmodel.*
import com.dimi.themoviedb.utils.*
import com.dimi.themoviedb.utils.ErrorHandling.Companion.isPaginationDone
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class TVShowFragment : BaseTVShowFragment(),
    TvShowListAdapter.Interaction,
    TabLayout.OnTabSelectedListener,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var searchView: SearchView
    private lateinit var recyclerAdapter: TvShowListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)

        swipe_refresh.setOnRefreshListener(this)

        if( isObjectNotPassedThroughNavigation() ) {
            setupTabs()

            initRecyclerView()
            subscribeObservers()
        }
    }

    override fun onResume() {
        super.onResume()
        if( isObjectNotPassedThroughNavigation() )
            viewModel.refreshFromCache()
    }

    override fun onPause() {
        super.onPause()
        saveLayoutManagerState()
    }

    private fun saveLayoutManagerState() {
        movies_recycler_view.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerState(lmState)
        }
    }

    private fun subscribeObservers() {


        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            if (viewState != null) {
                recyclerAdapter.apply {
                    viewState.tvShowFields.tvShowList?.let {
                        preloadGlideImages(
                            requestManager = dependencyProvider.getGlideRequestManager(),
                            list = it
                        )
                    }
                    submitList(
                        tvShowList = viewState.tvShowFields.tvShowList,
                        isQueryExhausted = viewState.tvShowFields.isQueryExhausted ?: true
                    )
                }
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer {
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.let {
                if (isPaginationDone(stateMessage.response.message)) {
                    viewModel.setQueryExhausted(true)
                    viewModel.clearStateMessage()
                } else {
                    uiCommunicationListener.onResponseReceived(
                        response = it.response,
                        stateMessageCallback = object : StateMessageCallback {
                            override fun removeMessageFromStack() {
                                viewModel.clearStateMessage()
                            }
                        }
                    )
                }
            }
        })
    }

    private fun onTvShowSearch( ) {
        viewModel.loadFirstPage( ).let {
            resetUI()
        }
    }

    private fun resetUI() {
        movies_recycler_view.smoothScrollToPosition(0)
        uiCommunicationListener.hideSoftKeyboard()
        focusable_view.requestFocus()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        initSearchView(menu)
    }

    private fun initSearchView(menu: Menu) {
        activity?.apply {
            val searchManager: SearchManager =
                getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView = menu.findItem(R.id.action_search).actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.maxWidth = Integer.MAX_VALUE
            searchView.setIconifiedByDefault(true)
            searchView.imeOptions = EditorInfo.IME_ACTION_SEARCH // search button on keyboard
            searchView.isSubmitButtonEnabled = true
        }

        // ENTER ON COMPUTER KEYBOARD OR ARROW ON VIRTUAL KEYBOARD
        val searchPlate = searchView.findViewById(R.id.search_src_text) as EditText
        if (!viewModel.getSearchQuery().isBlank()) searchPlate.setText(viewModel.getSearchQuery())
        searchPlate.setOnEditorActionListener { v, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED
                || actionId == EditorInfo.IME_ACTION_SEARCH
            ) {
                val searchQuery = v.text.toString()
                Log.e(TAG, "SearchView: (keyboard or arrow) executing search...: $searchQuery")
                handleSearchConfirmed(searchQuery)
            }
            true
        }

        // SEARCH BUTTON CLICKED (in toolbar)
        val searchButton = searchView.findViewById(R.id.search_go_btn) as View
        searchButton.setOnClickListener {
            val searchQuery = searchPlate.text.toString()
            Log.e(TAG, "SearchView: (button) executing search...: $searchQuery")
            handleSearchConfirmed(searchQuery)
        }
    }

    private fun handleSearchConfirmed(searchQuery: String) {
        viewModel.setQuery(searchQuery).let {
            // when execute search query if tab 0 is not selected, select it.
            if (!movie_tabs.getTabAt(0)?.isSelected!!) {
                movie_tabs.getTabAt(0)!!.select()
            } else {
                viewModel.clearGenre()
                onTvShowSearch()
            }
        }
    }

    private fun initRecyclerView() {

        movies_recycler_view.apply {

            layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
            // setting gapStrategy so the spans don't mix up
            (layoutManager as StaggeredGridLayoutManager).gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

            val spaceDecoration = SpacesItemDecoration(15)
            removeItemDecoration(spaceDecoration) // does nothing if not applied already
            addItemDecoration(spaceDecoration)
            recyclerAdapter =
                TvShowListAdapter(
                    dependencyProvider.getGlideRequestManager(),
                    this@TVShowFragment
                )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    // if scroll is on end of recyclerView trigger nextPage of results
                    if (!canScrollVertically(1)) {
                        viewModel.nextPage()
                    }
                }
            })
            adapter = recyclerAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        movies_recycler_view.adapter = null
    }

    override fun onRefresh() {
        onTvShowSearch()
        swipe_refresh.isRefreshing = false
    }

    override fun onItemSelected(position: Int, item: TVShow) {
        viewModel.setClickedTvShowId(item.id)
        findNavController().navigate(R.id.action_TVShowFragment_to_viewTvShowFragment)
    }

    override fun restoreListPosition() {
        viewModel.viewState.value?.tvShowFields?.layoutManagerState?.let { lmState ->
            movies_recycler_view?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    private fun setupTabs() {

        for (genre in TVShowGenres.getAllGenreIds()!!) {
            val newTab = movie_tabs.newTab()
            newTab.text = TVShowGenres.getGenreName(genre);
            movie_tabs.addTab(newTab, false)
        }
        movie_tabs.getTabAt(viewModel.getSelectedTab())?.select()
        // add listener after first tab is selected and this way we will not trigger tvShowSearch 2 or more times
        // and back from viewModel will we properly be restored to right list
        movie_tabs.addOnTabSelectedListener(this)
    }

    private fun isSearchViewInitialized() = ::searchView.isInitialized

    private fun clearSearchViewQuery() {

        searchView.onActionViewCollapsed()
        searchView.imeOptions =
            EditorInfo.IME_ACTION_SEARCH  // onActionViewCollapsed return to default.
    }

    override fun onTabReselected(tab: TabLayout.Tab) {

        if( tab.position == 0 ) {
            // if popular is reselect and some query was called it will be restarted back to popular tvShow query
            if (isSearchViewInitialized()) {
                viewModel.setQuery("")
                clearSearchViewQuery()
                viewModel.clearGenre()
                onTvShowSearch()
            }
        } else {
            // if genre is reselect pop up to top of list
            resetUI()
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
        if (tab.position == 0) {
            clearSearchViewQuery()
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        viewModel.setSelectedTab(tab.position)

        // tab.position == 0 "Popular" - this is not default API genre.
        if (tab.position != 0) {
            //viewModel.setQuery("")
            viewModel.setGenre(TVShowGenres.getGenreIdByName(tab.text as String?))
        } else
            viewModel.clearGenre()

        onTvShowSearch()
    }
}
