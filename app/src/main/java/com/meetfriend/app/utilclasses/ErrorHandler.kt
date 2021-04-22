package com.meetfriend.app.utilclasses

import android.accounts.NetworkErrorException
import android.content.Context
import android.os.NetworkOnMainThreadException
import com.meetfriend.app.R
import retrofit2.HttpException
import java.io.FileNotFoundException
import java.io.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class ErrorHandler {
    companion object {
        /**
         * Returns appropriate message which is to be displayed to the user
         * against the specified error object.
         *
         * @param error
         * @param context
         * @return
         */

        fun getMessage(error: Throwable, context: Context): String {
            when (error) {
                is SocketTimeoutException -> return context.getResources().getString(R.string.err_internet_conn)
                is ConnectException -> return context.getResources().getString(R.string.err_internet_conn)
                is TimeoutException -> return context.getResources().getString(R.string.err_timeout)
                is UnknownHostException -> return context.getResources().getString(R.string.err_ip_address)
                is NetworkErrorException -> return context.getResources().getString(R.string.err_internet_conn)
                is NetworkOnMainThreadException -> return context.getResources().getString(R.string.err_internet_conn)
                is IllegalStateException -> return context.getResources().getString(R.string.err_view_must_attach)
                is IllegalArgumentException -> return context.getResources().getString(R.string.err_illegal_arguement)
                is FileNotFoundException -> return context.getResources().getString(R.string.err_file_not_found)
                is NullPointerException -> return context.getResources().getString(R.string.err_null_pointer)
                is NoRouteToHostException -> return context.getResources().getString(R.string.err_no_route)
                is HttpException -> when (error.code()) {
                    304 -> return context.getResources().getString(R.string.not_modified_error)
                    400 -> return context.getResources().getString(R.string.bad_request_error)
                    401 -> return context.getResources().getString(R.string.unauthorized_error)
                    403 -> return context.getResources().getString(R.string.forbidden_error)
                    404 -> return context.getResources().getString(R.string.not_found_error)
                    405 -> return context.getResources().getString(R.string.method_not_allowed_error)
                    409 -> return context.getResources().getString(R.string.conflict_error)
                    422 -> return context.getResources().getString(R.string.unprocessable_error)
                    500 -> return context.getResources().getString(R.string.server_error_error)
                    else -> return context.getResources().getString(R.string.err_unknown_exception)
                }
                is IOException -> return context.getResources().getString(R.string.err_internet_conn)
                else -> context.getResources().getString(R.string.err_unknown_exception)
            }
            return context.getResources().getString(R.string.err_unknown_exception)

        }


    }
}