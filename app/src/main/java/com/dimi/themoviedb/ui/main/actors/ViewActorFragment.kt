package com.dimi.themoviedb.ui.main.actors

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimi.themoviedb.R
import com.dimi.themoviedb.models.Actor
import com.dimi.themoviedb.ui.main.actors.state.ActorStateEvent
import com.dimi.themoviedb.api.ApiConstants
import com.dimi.themoviedb.models.CombinedCredit
import com.dimi.themoviedb.ui.main.actors.adapters.CreditsListAdapter
import com.dimi.themoviedb.utils.SpacesItemDecoration
import com.dimi.themoviedb.utils.StateMessageCallback
import kotlinx.android.synthetic.main.fragment_view_actor.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@ExperimentalCoroutinesApi
@FlowPreview
class ViewActorFragment : BaseActorFragment(),
    CreditsListAdapter.Interaction {

    private lateinit var recyclerAdapter: CreditsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_actor, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        uiCommunicationListener.expandAppBar() // if u are scrolled to middle myb on last fragment then toolbar will not be shown probably
        if ( !isSameObjectSend()) {
            loadActor()
            loadCredits()
        }
        initRecyclerView()
        subscribeObservers()
    }

    private fun initRecyclerView() {
        credits_recycler_view.apply {

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val spaceDecoration = SpacesItemDecoration(15)
            removeItemDecoration(spaceDecoration)
            addItemDecoration(spaceDecoration)
            recyclerAdapter = CreditsListAdapter(
                dependencyProvider.getGlideRequestManager(),
                this@ViewActorFragment
            )
            adapter = recyclerAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        credits_recycler_view.adapter = null
    }

    private fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {

                viewState.actorViewFields.actor?.let { actor ->
                    setActorProperties(actor)
                }
                viewState.actorViewFields.creditsList?.let { list ->
                    if (list.isNotEmpty()) {
                        recyclerAdapter.apply {
                            preloadGlideImages(
                                requestManager = dependencyProvider.getGlideRequestManager(),
                                list = list
                            )
                            submitList(
                                list
                            )
                        }
                    }
                }
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer {
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.let {

                if (stateMessage.response.message?.contains("Reason: Data is NULL")!!) {
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

    private fun setActorProperties(actor: Actor) {

        dependencyProvider.getGlideRequestManager()
            .load(ApiConstants.SMALL_IMAGE_URL_PREFIX + actor.profilePath)
            .into(movie_image)

        actor_name.text = actor.name
        actor_birthday.text = String.format("Birthday: %s", actor.birthday)
        actor_place_of_birth.text = actor.placeOfBirth
        actor_biography.text = actor.biography
        actor_gender.text = if (actor.gender == 0) "Gender: Male" else "Gender: Female"
    }

    private fun loadActor() {
        viewModel.setStateEvent(
            ActorStateEvent.ActorDetails()
        )
    }

    private fun loadCredits() {
        viewModel.setStateEvent(
            ActorStateEvent.ActorCredits()
        )
    }

    override fun onItemSelected(position: Int, item: CombinedCredit) {
        if (item.isMovie()) {
            dependencyProvider.onClickedPassingObject(item.toMovie())
        } else if (item.isTvShow()) {
            dependencyProvider.onClickedPassingObject(item.toTvShow())
        }
    }
}
