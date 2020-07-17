package com.dimi.themoviedb.ui.main

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import com.bumptech.glide.RequestManager
import com.dimi.themoviedb.R
import com.dimi.themoviedb.models.Actor
import com.dimi.themoviedb.models.Movie
import com.dimi.themoviedb.models.PassingModel
import com.dimi.themoviedb.models.TVShow
import com.dimi.themoviedb.ui.BaseActivity
import com.dimi.themoviedb.ui.main.actors.ViewActorFragment
import com.dimi.themoviedb.ui.main.movies.ViewMovieFragment
import com.dimi.themoviedb.ui.main.tv_show.ViewEpisodeFragment
import com.dimi.themoviedb.ui.main.tv_show.ViewSeasonFragment
import com.dimi.themoviedb.ui.main.tv_show.ViewTvShowFragment
import com.dimi.themoviedb.utils.*
import com.dimi.themoviedb.utils.BottomNavController.*
import com.dimi.themoviedb.viewmodels.ViewModelProviderFactory
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject


@FlowPreview
@ExperimentalCoroutinesApi
class MainActivity : BaseActivity(),
    NavGraphProvider,
    OnNavigationGraphChanged,
    OnNavigationReselectedListener,
    MainDependencyProvider{

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    lateinit var requestManager: RequestManager

    private lateinit var bottomNavigationView: BottomNavigationView

    private val bottomNavController by lazy(LazyThreadSafetyMode.NONE) {
        BottomNavController(
            this,
            R.id.main_nav_host_fragment,
            R.id.menu_nav_movies,
            this,
            this
        )
    }

    override fun displayProgressBar(isLoading: Boolean) {
        if (isLoading) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()
        setupBottomNavigationView(savedInstanceState)
    }

    override fun expandAppBar() {
        findViewById<AppBarLayout>(R.id.app_bar).setExpanded(true)
    }

    override fun getVMProviderFactory() = providerFactory

    override fun getGlideRequestManager() = requestManager

    override fun isInternetAvailable() = sessionManager.isInternetAvailable()

    override fun getBottomNavigationController() = bottomNavController

    override fun getNavGraphId(itemId: Int) = when (itemId) {
        R.id.menu_nav_movies -> {
            R.navigation.nav_movies
        }
        R.id.menu_nav_series -> {
            R.navigation.nav_series
        }
        R.id.menu_nav_actors -> {
            R.navigation.nav_actors
        }
        else -> {
            R.navigation.nav_movies
        }
    }

    override fun onGraphChange(itemId: Int) {
        expandAppBar()
    }


    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onReselectNavItem(
        navController: NavController,
        fragment: Fragment
    ) {
        Log.d(TAG, "logInfo: onReSelectItem")
        when (fragment) {

            is ViewMovieFragment -> {
                navController.navigate(R.id.action_viewMovieFragment_to_home)
            }

            is ViewActorFragment -> {
                navController.navigate(R.id.action_viewActorFragment_to_home)
            }

            is ViewTvShowFragment -> {
                navController.navigate(R.id.action_viewTvShowFragment_to_home)
            }

            is ViewSeasonFragment -> {
                navController.navigate(R.id.action_viewSeasonFragment_to_home)
            }

            is ViewEpisodeFragment -> {
                navController.navigate(R.id.action_viewEpisodeFragment_to_home)
            }

            else -> {
                // do nothing
            }
        }
    }

    private fun setupBottomNavigationView(savedInstanceState: Bundle?) {
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setUpNavigation(bottomNavController, this)
        if (savedInstanceState == null) {
            bottomNavController.setupBottomNavigationBackStack(null)
            bottomNavController.setupBottomNavigationPassingStack(null)
            bottomNavController.onNavigationItemSelected()
        } else {
            (savedInstanceState[BOTTOM_NAV_BACK_STACK_KEY] as IntArray?)?.let { items ->
                val backStack = BackStack()
                backStack.addAll(items.toTypedArray())
                bottomNavController.setupBottomNavigationBackStack(backStack)
            }
            (savedInstanceState[BOTTOM_NAV_PASSING_STACK_KEY] as PassingStack?)?.let { items ->
                val passingStack = PassingStack()
                passingStack.addAll(items)
                bottomNavController.setupBottomNavigationPassingStack(passingStack)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(
            BOTTOM_NAV_BACK_STACK_KEY,
            bottomNavController.navigationBackStack.toIntArray()
        )
        outState.putParcelable(
            BOTTOM_NAV_PASSING_STACK_KEY,
            bottomNavController.passingBackStack
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() = bottomNavController.onBackPressed()

    private fun setupActionBar() {
        setSupportActionBar(tool_bar)
    }

    override fun onClickedPassingObject(passingModel: PassingModel, isTvShowActor: Boolean ) {
        when(passingModel) {
            is Actor -> {
                // must check from which fragment comes so we can navigate back to same
                if( isTvShowActor )
                    bottomNavController.setPassingObject(R.id.menu_nav_series, R.id.menu_nav_actors, passingModel, ReceivedObjectType.Actor)
                else
                    bottomNavController.setPassingObject(R.id.menu_nav_movies, R.id.menu_nav_actors, passingModel, ReceivedObjectType.Actor)
            }
            is Movie -> {
                bottomNavController.setPassingObject(R.id.menu_nav_actors, R.id.menu_nav_movies, passingModel, ReceivedObjectType.Movie)
            }
            is TVShow -> {
                bottomNavController.setPassingObject(R.id.menu_nav_actors, R.id.menu_nav_series, passingModel, ReceivedObjectType.TVShow)
            }
        }
    }
}
