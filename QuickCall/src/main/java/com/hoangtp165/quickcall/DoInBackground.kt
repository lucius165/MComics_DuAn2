package com.hoangtp165.quickcall

import android.os.AsyncTask

class DoInBackground<InputType, ProgressUnitType, OnPostExcType> {
    private var doInBackground: ((input: Array<out InputType>) -> OnPostExcType)? = null
    private var onPreExecute: (() -> Unit)? = null
    private var onCancelled: ((result: OnPostExcType?) -> Unit)? = null
    private var onProgressUpdate: ((values: Array<out ProgressUnitType>) -> Unit)? = null
    private var onPostExecute: ((value: OnPostExcType) -> Unit)? = null

    fun setDoInBackground(doInBackground: ((input: Array<out InputType>) -> OnPostExcType)): DoInBackground<InputType, ProgressUnitType, OnPostExcType> {
        this.doInBackground = doInBackground
        return this
    }

    fun setPreExecute(onPreExecute: (() -> Unit)): DoInBackground<InputType, ProgressUnitType, OnPostExcType> {
        this.onPreExecute = onPreExecute
        return this
    }

    fun setCancelled(onCancel: ((result: OnPostExcType?) -> Unit)): DoInBackground<InputType, ProgressUnitType, OnPostExcType> {
        this.onCancelled = onCancel
        return this
    }

    fun setProgressUpdate(onProgressUpdate: ((values: Array<out ProgressUnitType>) -> Unit)): DoInBackground<InputType, ProgressUnitType, OnPostExcType> {
        this.onProgressUpdate = onProgressUpdate
        return this
    }

    fun setPostExecute(onPostExecute: ((value: OnPostExcType) -> Unit)): DoInBackground<InputType, ProgressUnitType, OnPostExcType> {
        this.onPostExecute = onPostExecute
        return this
    }

    fun build() = if (doInBackground == null) {
        throw DoInBackgroundNotDefinedException()
    } else {
        DoInBackgroundAsyncTask(doInBackground, onPreExecute, onCancelled, onProgressUpdate, onPostExecute)
    }
}

class DoInBackgroundAsyncTask<InputType, ProgressUnitType, OnPostExcType>(val doInBackground: ((input: Array<out InputType>) -> OnPostExcType)? = null,
                                                                          val onPreExecute: (() -> Unit)? = null,
                                                                          val onCancelled: ((result: OnPostExcType?) -> Unit)? = null,
                                                                          val onProgressUpdate: ((values: Array<out ProgressUnitType>) -> Unit)? = null,
                                                                          val onPostExecute: ((value: OnPostExcType) -> Unit)? = null) : AsyncTask<InputType, ProgressUnitType, OnPostExcType>() {

    override fun doInBackground(vararg input: InputType): OnPostExcType? {
        val mFun = doInBackground
        return if (mFun != null) {
            mFun(input)
        } else {
            null
        }
    }

    override fun onPreExecute() {
        val mFun = onPreExecute
        if (mFun != null) {
            return mFun()
        }
    }

    override fun onCancelled() {
        val mFun = onCancelled
        if (mFun != null) {
            return mFun(null)
        }
    }

    override fun onCancelled(result: OnPostExcType) {
        val mFun = onCancelled
        if (mFun != null) {
            return mFun(result)
        }
    }

    override fun onProgressUpdate(vararg values: ProgressUnitType) {
        val mFun = onProgressUpdate
        if (mFun != null) {
            return mFun(values)
        }
    }

    override fun onPostExecute(result: OnPostExcType) {
        val mFun = onPostExecute
        if (mFun != null) {
            return mFun(result)
        }
    }

}

class DoInBackgroundNotDefinedException : Exception()
