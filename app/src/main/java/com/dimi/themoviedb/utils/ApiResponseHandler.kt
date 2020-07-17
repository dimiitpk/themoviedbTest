package com.dimi.themoviedb.utils

import com.dimi.themoviedb.utils.ApiResult.*
import com.dimi.themoviedb.utils.ErrorHandling.Companion.NETWORK_ERROR

abstract class ApiResponseHandler <ViewState, Data>(
    private val response: ApiResult<Data?>,
    private val stateEvent: StateEvent
){

    private val TAG: String = "AppDebug"

    suspend fun getResult(): DataState<ViewState>{

        return when(response){

            is GenericError -> {
                DataState.error(
                    response = Response(
                        message = "${stateEvent.errorInfo()}\n\nReason: ${response.errorMessage.toString()}",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    ),
                    stateEvent = stateEvent
                )
            }

            is NetworkError -> {
                DataState.error(
                    response = Response(
                        message = "${stateEvent.errorInfo()}\n\nReason: $NETWORK_ERROR",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    ),
                    stateEvent = stateEvent
                )
            }

            is Success -> {
                if(response.value == null){
                    DataState.error(
                        response = Response(
                            message = "${stateEvent.errorInfo()}\n\nReason: Data is NULL.",
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Error()
                        ),
                        stateEvent = stateEvent
                    )
                }
                else{
                    handleSuccess(resultObj = response.value)
                }
            }

        }
    }

    abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>

}