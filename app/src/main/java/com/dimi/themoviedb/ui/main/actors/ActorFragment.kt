package com.dimi.themoviedb.ui.main.actors

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
import com.dimi.themoviedb.models.Actor
import com.dimi.themoviedb.ui.main.actors.adapters.ActorListAdapter
import com.dimi.themoviedb.ui.main.actors.viewmodel.*
import com.dimi.themoviedb.utils.ErrorHandling
import com.dimi.themoviedb.utils.SpacesItemDecoration
import com.dimi.themoviedb.utils.StateMessageCallback
import kotlinx.android.synthetic.main.fragment_actor.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class ActorFragment : BaseActorFragment(),
    ActorListAdapter.Interaction,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var searchView: SearchView
    private lateinit var recyclerAdapter: ActorListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_actor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)

        swipe_refresh.setOnRefreshListener(this)

        if( isObjectNotPassedThroughNavigation() ) {
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
        actors_recycler_view.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerState(lmState)
        }
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            if (viewState != null) {
                recyclerAdapter.apply {
                    viewState.actorFields.actorList?.let {
                        preloadGlideImages(
                            requestManager = dependencyProvider.getGlideRequestManager(),
                            list = it
                        )
                    }
                    submitList(
                        actorList = viewState.actorFields.actorList,
                        isQueryExhausted = viewState.actorFields.isQueryExhausted ?: true
                    )
                }
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer {
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.let {
                if (ErrorHandling.isPaginationDone(stateMessage.response.message)) {
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

    private fun onMovieSearch( ) {
        viewModel.loadFirstPage( ).let {
            resetUI()
        }
    }

    private fun resetUI() {
        actors_recycler_view.smoothScrollToPosition(0)
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
                viewModel.setQuery(searchQuery)
                onMovieSearch()
            }
            true
        }

        // SEARCH BUTTON CLICKED (in toolbar)
        val searchButton = searchView.findViewById(R.id.search_go_btn) as View
        searchButton.setOnClickListener {
            val searchQuery = searchPlate.text.toString()
            Log.e(TAG, "SearchView: (button) executing search...: $searchQuery")
            viewModel.setQuery(searchQuery)
            onMovieSearch()
        }
    }

    private fun initRecyclerView() {

        actors_recycler_view.apply {

            layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
            // setting gapStrategy so the spans don't mix up
            (layoutManager as StaggeredGridLayoutManager).gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

            val spaceDecoration = SpacesItemDecoration(15)
            removeItemDecoration(spaceDecoration) // does nothing if not applied already
            addItemDecoration(spaceDecoration)
            recyclerAdapter =
                ActorListAdapter(
                    dependencyProvider.getGlideRequestManager(),
                    this@ActorFragment
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
        actors_recycler_view.adapter = null
    }

    override fun onRefresh() {
        onMovieSearch()
        swipe_refresh.isRefreshing = false
    }

    override fun onItemSelected(position: Int, item: Actor) {
        viewModel.setClickedActorId(item.id)
        findNavController().navigate(R.id.action_actorFragment_to_viewActorFragment)
    }

    override fun restoreListPosition() {
        viewModel.viewState.value?.actorFields?.layoutManagerState?.let { lmState ->
            actors_recycler_view?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }
}
