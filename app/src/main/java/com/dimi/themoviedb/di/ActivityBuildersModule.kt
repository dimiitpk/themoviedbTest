package com.dimi.themoviedb.di

import com.dimi.themoviedb.di.main.MainFragmentBuildersModule
import com.dimi.themoviedb.di.main.MainModule
import com.dimi.themoviedb.di.main.MainScope
import com.dimi.themoviedb.di.main.MainViewModelModule
import com.dimi.themoviedb.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@Module
abstract class ActivityBuildersModule {
    @MainScope
    @ContributesAndroidInjector(
        modules = [MainModule::class, MainFragmentBuildersModule::class, MainViewModelModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity
}