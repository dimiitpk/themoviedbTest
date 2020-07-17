package com.dimi.themoviedb.ui.main.movies

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.codingwithmitch.openapi.di.Injectable
import com.dimi.themoviedb.R
import com.dimi.themoviedb.models.Movie
import com.dimi.themoviedb.ui.UICommunicationListener
import com.dimi.themoviedb.ui.main.MainDependencyProvider
import com.dimi.themoviedb.ui.main.movies.state.MOVIE_VIEW_STATE_BUNDLE_KEY
import com.dimi.themoviedb.ui.main.movies.state.MovieViewState
import com.dimi.themoviedb.ui.main.movies.viewmodel.*
import com.dimi.themoviedb.utils.ReceivedObjectType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseMovieFragment : Fragment(), Injectable {
    val TAG: String = "AppDebug"

    lateinit var dependencyProvider: MainDependencyProvider

    lateinit var uiCommunicationListener: UICommunicationListener

    lateinit var viewModel: MovieViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val bottomNavController = dependencyProvider.getBottomNavigationController()
        bottomNavController.passingObject?.let { passingObject ->
            if (passingObject.receivedObjectType is ReceivedObjectType.Movie) {
                when (this) {
                    is MovieFragment -> {
                        bottomNavController.receivingNavigationStartFromFirstFragment = true

                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.movieFragment, false)
                            .build()

                        bottomNavController
                            .activity
                            .findNavController(bottomNavController.containerId)
                            .navigate(
                                R.id.action_movieFragment_to_viewMovieFragment,
                                null,
                                navOptions
                            )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        when (this) {
            is ViewMovieFragment -> {
                val navController = dependencyProvider.getBottomNavigationController()

                /*
                * If stack of passed objects is empty and actor was already loaded earlier
                * we are clearing ViewActorFragment fields and set old actor, and in this way backStack works properly
                * */
                viewModel.getMovieLoadedBeforePassing()?.let { movie ->
                    if (navController.passingBackStack.isNavigationEmpty(R.id.menu_nav_movies)) {
                        viewModel.setClickedMovieId(movieId = movie.id)
                        viewModel.setMovieLoadedBeforePassing(null)
                        viewModel.clearViewMovie() // clearing actor details so then we can't get bugs with showing old old empty
                        viewModel.clearMovieCastList() // same as for videos
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(R.id.movieFragment, activity as AppCompatActivity)
        setupChannel()

        val bottomNavController = dependencyProvider.getBottomNavigationController()
        bottomNavController.passingObject?.let { passingObject ->
            if (passingObject.receivedObjectType is ReceivedObjectType.Movie) {
                when (this) {
                    is ViewMovieFragment -> {
                        if (viewModel.getMovieLoadedBeforePassing() == null)
                            viewModel.setMovieLoadedBeforePassing(viewModel.getViewMovie())

                        viewModel.setClickedMovieId(
                            (bottomNavController.passingObject!!.passingModel as Movie).id
                        )
                    }
                }
            }
        }
    }

    private fun setupChannel() = viewModel.setupChannel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(
                this,
                dependencyProvider.getVMProviderFactory()
            ).get(MovieViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        // Restore state after process death
        savedInstanceState?.let { inState ->
            (inState[MOVIE_VIEW_STATE_BUNDLE_KEY] as MovieViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    private fun isViewModelInitialized() = ::viewModel.isInitialized

    override fun onSaveInstanceState(outState: Bundle) {
        if (isViewModelInitialized()) {
            val viewState = viewModel.viewState.value

            //clear the list. Don't want to save a large list to bundle.
            viewState?.movieFields?.movieList = ArrayList()// check
            //viewState?.viewMovieFields?.castList = ArrayList()// check
            //viewState?.viewMovieFields?.videoList = ArrayList() // check

            outState.putParcelable(
                MOVIE_VIEW_STATE_BUNDLE_KEY,
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

    fun isObjectNotPassedThroughNavigation(): Boolean {
        val bottomNavController = dependencyProvider.getBottomNavigationController()
        return bottomNavController.passingBackStack.isNavigationEmpty(R.id.menu_nav_movies)
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