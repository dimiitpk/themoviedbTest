package com.dimi.themoviedb.di.main

import androidx.lifecycle.ViewModel
import com.dimi.themoviedb.di.ViewModelKey
import com.dimi.themoviedb.ui.main.actors.viewmodel.ActorViewModel
import com.dimi.themoviedb.ui.main.movies.viewmodel.MovieViewModel
import com.dimi.themoviedb.ui.main.tv_show.viewmodel.TVShowViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@Module
abstract class MainViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MovieViewModel::class)
    abstract fun bindMovieViewModel(movieViewModel: MovieViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ActorViewModel::class)
    abstract fun bindActorViewModel(actorViewModel: ActorViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(TVShowViewModel::class)
    abstract fun bindTVShowViewModel(tvShowViewModel: TVShowViewModel): ViewModel
}








