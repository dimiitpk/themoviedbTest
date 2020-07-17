package com.dimi.themoviedb.ui

import com.dimi.themoviedb.utils.Response
import com.dimi.themoviedb.utils.StateMessageCallback
import com.dimi.themoviedb.utils.UIMessage

interface UICommunicationListener {

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

    fun displayProgressBar( isLoading: Boolean )

    fun expandAppBar()

    fun hideSoftKeyboard()
}