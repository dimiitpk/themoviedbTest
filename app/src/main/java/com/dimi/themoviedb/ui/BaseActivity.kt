package com.dimi.themoviedb.ui

import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.dimi.themoviedb.R
import com.dimi.themoviedb.session.SessionManager
import com.dimi.themoviedb.utils.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseActivity: DaggerAppCompatActivity(),
    UICommunicationListener
{
    val TAG: String = "AppDebug"

    private var dialogInView: MaterialDialog? = null

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ) {

        when(response.uiComponentType){

            is UIComponentType.AreYouSureDialog -> {

                response.message?.let {
                    areYouSureDialog(
                        message = it,
                        callback = response.uiComponentType.callback,
                        stateMessageCallback = stateMessageCallback
                    )
                }
            }

            is UIComponentType.Toast -> {
                response.message?.let {
                    displayToast(
                        message = it,
                        stateMessageCallback = stateMessageCallback
                    )
                }
            }

            is UIComponentType.Dialog -> {
                displayDialog(
                    response = response,
                    stateMessageCallback = stateMessageCallback
                )
            }

            is UIComponentType.None -> {
                // This would be a good place to send to your Error Reporting
                // software of choice (ex: Firebase crash reporting)
                Log.i(TAG, "onResponseReceived: ${response.message}")
                stateMessageCallback.removeMessageFromStack()
            }
        }
    }

    private fun displayDialog(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ){
        Log.d(TAG, "displayDialog: ")
        response.message?.let { message ->

            dialogInView = when (response.messageType) {

                is MessageType.Error -> {
                    displayErrorDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                is MessageType.Success -> {
                    displaySuccessDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                is MessageType.Info -> {
                    displayInfoDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                else -> {
                    // do nothing
                    stateMessageCallback.removeMessageFromStack()
                    null
                }
            }
        }?: stateMessageCallback.removeMessageFromStack()
    }

    abstract override fun displayProgressBar(isLoading: Boolean)

    override fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager
                .hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun onPause() {
        super.onPause()
        if(dialogInView != null){
            (dialogInView as MaterialDialog).dismiss()
            dialogInView = null
        }
    }

    private fun displaySuccessDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show{
                title(R.string.text_success)
                message(text = message)
                positiveButton(R.string.text_ok){
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    private fun displayErrorDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show{
                title(R.string.text_error)
                message(text = message)
                positiveButton(R.string.text_ok){
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    private fun displayInfoDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show{
                title(R.string.text_info)
                message(text = message)
                positiveButton(R.string.text_ok){
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    private fun areYouSureDialog(
        message: String,
        callback: AreYouSureCallback,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show{
                title(R.string.are_you_sure)
                message(text = message)
                negativeButton(R.string.text_cancel){
                    callback.cancel()
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                positiveButton(R.string.text_yes){
                    callback.proceed()
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }
}