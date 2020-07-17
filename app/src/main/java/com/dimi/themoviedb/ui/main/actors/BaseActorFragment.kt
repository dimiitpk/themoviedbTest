package com.dimi.themoviedb.ui.main.actors

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
import com.dimi.themoviedb.models.Actor
import com.dimi.themoviedb.ui.UICommunicationListener
import com.dimi.themoviedb.ui.main.MainDependencyProvider
import com.dimi.themoviedb.ui.main.actors.state.ACTOR_VIEW_STATE_BUNDLE_KEY
import com.dimi.themoviedb.ui.main.actors.state.ActorViewState
import com.dimi.themoviedb.ui.main.actors.viewmodel.*
import com.dimi.themoviedb.utils.ReceivedObjectType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseActorFragment : Fragment(), Injectable {
    val TAG: String = "AppDebug"

    lateinit var dependencyProvider: MainDependencyProvider

    lateinit var uiCommunicationListener: UICommunicationListener

    lateinit var viewModel: ActorViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /**
         *  bottomNavController.passedActorId checking if actorId is passed from movies or series graph
         *  checking if we are in ActorFragment
         *  if it is then we skipp startDestination of navigation_actors and set receivingNavigationStartFromFirstFragment to true
         */
        val bottomNavController = dependencyProvider.getBottomNavigationController()
        bottomNavController.passingObject?.let { passingObject ->
            if (passingObject.receivedObjectType is ReceivedObjectType.Actor) {
                when (this) {
                    is ActorFragment -> {
                        bottomNavController.receivingNavigationStartFromFirstFragment = true

                        Log.d("SVESASASASAS", "pozvaja ga")

                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.actorFragment, false)
                            .build()

                        bottomNavController.activity
                            .findNavController(bottomNavController.containerId)
                            .navigate(
                                R.id.action_actorFragment_to_viewActorFragment,
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
            is ViewActorFragment -> {
                val navController = dependencyProvider.getBottomNavigationController()

                /*
                * If stack of passed objects is empty and actor was already loaded earlier
                * we are clearing ViewActorFragment fields and set old actor, and in this way backStack works properly
                * */
                viewModel.getActorLoadedBeforePassing()?.let { actor ->
                    if (navController.passingBackStack.isNavigationEmpty(R.id.menu_nav_actors)) {
                        viewModel.setClickedActorId(actorId = actor.id)
                        viewModel.setActorLoadedBeforePassing(null)
                        viewModel.clearActorDetails()
                        viewModel.clearActorCreditsList()
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(R.id.actorFragment, activity as AppCompatActivity)
        setupChannel()
        /*
        * If we are in ViewActorFragment
        * If actorId comes from movies or series graph we need to set it up with ViewModel
        * bc/s we do not share viewModel between graphs and activity
        * ActorLoadedBeforePassing - checking it there is already one loaded if it is, save it for properly working backStack
        * */
        val bottomNavController = dependencyProvider.getBottomNavigationController()
        bottomNavController.passingObject?.let { passingObject ->
            if (passingObject.receivedObjectType is ReceivedObjectType.Actor) {
                when (this) {
                    is ViewActorFragment -> {
                        if (viewModel.getActorLoadedBeforePassing() == null)
                            viewModel.setActorLoadedBeforePassing(viewModel.getActor())

                        viewModel.setClickedActorId(
                            (bottomNavController.passingObject!!.passingModel as Actor).id
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
            ).get(ActorViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        // Restore state after process death
        savedInstanceState?.let { inState ->
            (inState[ACTOR_VIEW_STATE_BUNDLE_KEY] as ActorViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (isViewModelInitialized()) {
            val viewState = viewModel.viewState.value

            //clear the list. Don't want to save a large list to bundle.
            viewState?.actorFields?.actorList = ArrayList()// check

            outState.putParcelable(
                ACTOR_VIEW_STATE_BUNDLE_KEY,
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

    fun isSameObjectSend() : Boolean {
        return viewModel.getActor()?.id == viewModel.getClickedActorId()
    }

    fun isObjectNotPassedThroughNavigation(): Boolean {
        val bottomNavController = dependencyProvider.getBottomNavigationController()
        return bottomNavController.passingBackStack.isNavigationEmpty(R.id.menu_nav_actors)
    }

    private fun isViewModelInitialized() = ::viewModel.isInitialized

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