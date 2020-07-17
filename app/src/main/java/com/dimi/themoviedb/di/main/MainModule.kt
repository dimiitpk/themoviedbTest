package com.dimi.themoviedb.di.main

import com.dimi.themoviedb.api.main.OpenApiMainService
import com.dimi.themoviedb.persistence.ActorsDao
import com.dimi.themoviedb.persistence.AppDatabase
import com.dimi.themoviedb.persistence.MoviesDao
import com.dimi.themoviedb.persistence.TvShowsDao
import com.dimi.themoviedb.repository.main.ActorRepositoryImpl
import com.dimi.themoviedb.repository.main.MovieRepositoryImpl
import com.dimi.themoviedb.repository.main.TVShowRepositoryImpl
import com.dimi.themoviedb.session.SessionManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import retrofit2.Retrofit

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideOpenApiMainService(retrofitBuilder: Retrofit.Builder): OpenApiMainService {
        return retrofitBuilder
            .build()
            .create(OpenApiMainService::class.java)
    }

    @MainScope
    @Provides
    fun provideMoviesDao(db: AppDatabase): MoviesDao {
        return db.getMoviesDao()
    }

    @MainScope
    @Provides
    fun providesTVShowsDao(db: AppDatabase): TvShowsDao {
        return db.getTvShowsDao()
    }

    @MainScope
    @Provides
    fun provideActorsDao(db: AppDatabase): ActorsDao {
        return db.getActorDao()
    }

    @FlowPreview
    @MainScope
    @Provides
    fun provideMovieRepository(
        openApiMainService: OpenApiMainService,
        moviesDao: MoviesDao,
        sessionManager: SessionManager
    ): MovieRepositoryImpl {
        return MovieRepositoryImpl(openApiMainService, moviesDao, sessionManager)
    }

    @FlowPreview
    @MainScope
    @Provides
    fun provideTVShowRepository(
        openApiMainService: OpenApiMainService,
        tvShowsDao: TvShowsDao,
        sessionManager: SessionManager
    ): TVShowRepositoryImpl {
        return TVShowRepositoryImpl(openApiMainService, tvShowsDao, sessionManager)
    }

    @FlowPreview
    @MainScope
    @Provides
    fun provideActorRepository(
        openApiMainService: OpenApiMainService,
        actorsDao: ActorsDao,
        sessionManager: SessionManager
    ): ActorRepositoryImpl {
        return ActorRepositoryImpl(openApiMainService, actorsDao, sessionManager)
    }
}

















