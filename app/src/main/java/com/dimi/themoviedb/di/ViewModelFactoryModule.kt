package com.dimi.themoviedb.di

import androidx.lifecycle.ViewModelProvider
import com.dimi.themoviedb.viewmodels.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory

}