package com.dimi.themoviedb.di.main

import com.dimi.themoviedb.ui.main.actors.ActorFragment
import com.dimi.themoviedb.ui.main.actors.ViewActorFragment
import com.dimi.themoviedb.ui.main.movies.MovieFragment
import com.dimi.themoviedb.ui.main.movies.ViewMovieFragment
import com.dimi.themoviedb.ui.main.tv_show.TVShowFragment
import com.dimi.themoviedb.ui.main.tv_show.ViewEpisodeFragment
import com.dimi.themoviedb.ui.main.tv_show.ViewSeasonFragment
import com.dimi.themoviedb.ui.main.tv_show.ViewTvShowFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@Module
abstract class MainFragmentBuildersModule {
    @ContributesAndroidInjector()
    abstract fun contributeMovieFragment(): MovieFragment

    @ContributesAndroidInjector()
    abstract fun contributeViewMovieFragment(): ViewMovieFragment

    @ContributesAndroidInjector()
    abstract fun contributeActorFragment(): ActorFragment

    @ContributesAndroidInjector()
    abstract fun contributeViewActorFragment(): ViewActorFragment

    @ContributesAndroidInjector()
    abstract fun contributeTVShowFragment(): TVShowFragment

    @ContributesAndroidInjector()
    abstract fun contributeViewTvShowFragment(): ViewTvShowFragment

    @ContributesAndroidInjector()
    abstract fun contributeViewSeasonFragment(): ViewSeasonFragment

    @ContributesAndroidInjector()
    abstract fun contributeViewEpisodeFragment(): ViewEpisodeFragment

}