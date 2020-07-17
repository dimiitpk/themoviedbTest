package com.dimi.themoviedb.ui.main

import com.bumptech.glide.RequestManager
import com.dimi.themoviedb.models.PassingModel
import com.dimi.themoviedb.session.SessionManager
import com.dimi.themoviedb.utils.BottomNavController
import com.dimi.themoviedb.utils.PassingObject
import com.dimi.themoviedb.viewmodels.ViewModelProviderFactory

/**
 * Provides app-level dependencies to various BaseFragments:
 *
 * Must do this because of process death issue and restoring state.
 * Why?
 * Can't set values that were saved in instance state to ViewModel because ViewModel
 * hasn't been created yet when onCreate is called.
 */
interface MainDependencyProvider{

    fun getVMProviderFactory(): ViewModelProviderFactory

    fun getGlideRequestManager(): RequestManager

    fun isInternetAvailable(): Boolean

    fun getBottomNavigationController(): BottomNavController

    fun onClickedPassingObject( passingModel: PassingModel, isTvShowActor: Boolean = false)
}