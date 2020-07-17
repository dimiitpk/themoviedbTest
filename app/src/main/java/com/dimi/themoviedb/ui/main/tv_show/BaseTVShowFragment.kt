package com.dimi.themoviedb.ui.main.tv_show

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.codingwithmitch.openapi.di.Injectable
import com.dimi.themoviedb.R
import com.dimi.themoviedb.models.TVShow
import com.dimi.themoviedb.ui.UICommunicationListener
import com.dimi.themoviedb.ui.main.MainDependencyProvider
import com.dimi.themoviedb.ui.main.tv_show.state.TVShowViewState
import com.dimi.themoviedb.ui.main.tv_show.state.TV_SHOW_VIEW_STATE_BUNDLE_KEY
import com.dimi.themoviedb.ui.main.tv_show.viewmodel.*
import com.dimi.themoviedb.utils.ReceivedObjectType
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_view_episode.*
import kotlinx.android.synthetic.main.fragment_view_season.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseTVShowFragment : Fragment(), Injectable {
    val TAG: String = "AppDebug"

    lateinit var dependencyProvider: MainDependencyProvider

    lateinit var uiCommunicationListener: UICommunicationListener

    lateinit var viewModel: TVShowViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // handling what happens when u have passing object and going to tvShow navigation tab and this fragments was before ViewTVShowFragment
        val bottomNavController = dependencyProvider.getBottomNavigationController()
        bottomNavController.passingObject?.let { passingObject ->
            if (passingObject.receivedObjectType is ReceivedObjectType.TVShow) {
                when (this) {
                    is TVShowFragment -> {
                        bottomNavController.receivingNavigationStartFromFirstFragment = true

                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.TVShowFragment, false)
                            .build()

                        bottomNavController
                            .activity
                            .findNavController(bottomNavController.containerId)
                            .navigate(
                                R.id.action_TVShowFragment_to_viewTvShowFragment,
                                null,
                                navOptions
                        )
                    }
                    is ViewSeasonFragment -> {

                        if( (passingObject.passingModel as TVShow).id != viewModel.getTvShow()?.id ) {
                            if( viewModel.getTVShowLoadedBeforePassing() != null ) {

                                bottomNavController
                                    .activity
                                    .findNavController(bottomNavController.containerId).popBackStack()
                            } else {
                                nested_scroll.visibility = View.INVISIBLE
                                val navOptions = NavOptions.Builder()
                                    .setPopUpTo(R.id.viewSeasonFragment, false)
                                    .build()

                                bottomNavController
                                    .activity
                                    .findNavController(bottomNavController.containerId)
                                    .navigate(
                                        R.id.action_viewSeasonFragment_to_viewTvShowFragment,
                                        null,
                                        navOptions
                                    )
                            }
                        }
                    }
                    is ViewEpisodeFragment -> {
                        if( (passingObject.passingModel as TVShow).id != viewModel.getTvShow()?.id ) {
                            if( viewModel.getTVShowLoadedBeforePassing() != null ) {

                                bottomNavController
                                    .activity
                                    .findNavController(bottomNavController.containerId)
                                    .popBackStack(R.id.viewTvShowFragment, false)
                            } else {
                                nested_scroll_episode.visibility = View.INVISIBLE
                                val navOptions = NavOptions.Builder()
                                    .setPopUpTo(R.id.viewEpisodeFragment, false)
                                    .build()

                                bottomNavController
                                    .activity
                                    .findNavController(bottomNavController.containerId)
                                    .navigate(
                                        R.id.action_viewEpisodeFragment_to_viewTvShowFragment,
                                        null,
                                        navOptions
                                    )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        when (this) {
            is ViewTvShowFragment -> {
                val navController = dependencyProvider.getBottomNavigationController()

                /*
                * If stack of passed objects is empty and tvShow was already loaded earlier
                * we are clearing ViewTvShowFragment fields and set old tvShow, and in this way backStack works properly
                * */
                viewModel.getTVShowLoadedBeforePassing()?.let { tvShow ->
                    if (navController.passingBackStack.isNavigationEmpty(R.id.menu_nav_series)) {
                        viewModel.setClickedTvShowId(tvShowId = tvShow.id)
                        viewModel.setTvShowLoadedBeforePassing(null)
                        viewModel.clearTvShowCastList()
                        viewModel.clearTvShowSeasonList()
                    }
                }
            }
            is ViewSeasonFragment -> {
                val navController = dependencyProvider.getBottomNavigationController()

                viewModel.getTVSeasonLoadedBeforePassing()?.let { tvSeason ->
                    if (navController.passingBackStack.isNavigationEmpty(R.id.menu_nav_series)) {
                        viewModel.setTvSeason( tvSeason)
                        viewModel.setTVSeasonLoadedBeforePassing(null)
                    }
                }
            }
            is ViewEpisodeFragment -> {
                val navController = dependencyProvider.getBottomNavigationController()

                viewModel.getTVEpisodeLoadedBeforePassing()?.let { tvEpisode ->
                    if (navController.passingBackStack.isNavigationEmpty(R.id.menu_nav_series)) {
                        viewModel.setEpisode( tvEpisode )
                        viewModel.setTvEpisodeLoadedBeforePassing(null)
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(R.id.movieFragment, activity as AppCompatActivity)
        setupChannel()

        // handling what happens when u have passing object and going to tvShow navigation tab and he was already select
        val bottomNavController = dependencyProvider.getBottomNavigationController()
        bottomNavController.passingObject?.let { passingObject ->
            if (passingObject.receivedObjectType is ReceivedObjectType.TVShow) {
                when (this) {
                    is ViewTvShowFragment -> {
                        if (viewModel.getTVShowLoadedBeforePassing() == null)
                            viewModel.setTvShowLoadedBeforePassing(viewModel.getTvShow())

                        viewModel.setClickedTvShowId(
                            (bottomNavController.passingObject!!.passingModel as TVShow).id
                        )

                        if (viewModel.getTVSeasonLoadedBeforePassing() == null && viewModel.getTvSeason().id != -1L )
                            viewModel.setTVSeasonLoadedBeforePassing(viewModel.getTvSeason())

                        if (viewModel.getTVEpisodeLoadedBeforePassing() == null && viewModel.getTVEpisode() != null)
                            viewModel.setTvEpisodeLoadedBeforePassing(viewModel.getTVEpisode())
                    }
                }
            }
        }
    }

    fun isObjectNotPassedThroughNavigation(): Boolean {
        val bottomNavController = dependencyProvider.getBottomNavigationController()
        return bottomNavController.passingBackStack.isNavigationEmpty(R.id.menu_nav_series)
    }

    private fun setupChannel() = viewModel.setupChannel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(
                this,
                dependencyProvider.getVMProviderFactory()
            ).get(TVShowViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        // Restore state after process death
        savedInstanceState?.let { inState ->
            (inState[TV_SHOW_VIEW_STATE_BUNDLE_KEY] as TVShowViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    private fun isViewModelInitialized() = ::viewModel.isInitialized

    override fun onSaveInstanceState(outState: Bundle) {
        if (isViewModelInitialized()) {
            val viewState = viewModel.viewState.value

            //clear the list. Don't want to save a large list to bundle.
            viewState?.tvShowFields?.tvShowList = ArrayList()// check
            //viewState?.viewMovieFields?.castList = ArrayList()// check
            //viewState?.viewMovieFields?.videoList = ArrayList() // check

            outState.putParcelable(
                TV_SHOW_VIEW_STATE_BUNDLE_KEY,
                viewState
            )
        }
        super.onSaveInstanceState(outState)
    }

    private fun setupActionBarWithNavController(fragmentId: Int, activity: AppCompatActivity) {
        val appBarConfiguration = AppBarConfiguration(setOf(fragmentId))
        NavigationUI.setupActionBarWithNavController(
            activity,
            findNavController(),
            appBarConfiguration
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            uiCommunicationListener = context as UICommunicationListener
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement UICommunicationListener")
        }

        try {
            dependencyProvider = context as MainDependencyProvider
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement DependencyProvider")
        }
    }
}