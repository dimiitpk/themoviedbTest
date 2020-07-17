package com.dimi.themoviedb.utils

import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class ErrorHandling{

    companion object {

        private val TAG: String = "AppDebug"

        const val ERROR_UNKNOWN = "Unknown error"
        const val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
        const val UNABLE_TODO_OPERATION_WO_INTERNET = "Can't do that operation without an internet connection"


        const val INVALID_PAGE = "Invalid page."
        const val ERROR_CHECK_NETWORK_CONNECTION = "Check network connection."

        const val NETWORK_ERROR = "Network error"
        const val NETWORK_ERROR_TIMEOUT = "Network timeout"
        const val CACHE_ERROR_TIMEOUT = "Cache timeout"
        const val UNKNOWN_ERROR = "Unknown error"
        const val INVALID_STATE_EVENT = "Invalid state event"

        fun isNetworkError(msg: String): Boolean{
            return when{
                msg.contains(UNABLE_TO_RESOLVE_HOST) -> true
                else-> false
            }
        }

        fun isPaginationDone(errorResponse: String?): Boolean{
            // if error response = '{"detail":"Invalid page."}' then pagination is finished
            return errorResponse?.contains(INVALID_PAGE)?: false
        }
    }
}