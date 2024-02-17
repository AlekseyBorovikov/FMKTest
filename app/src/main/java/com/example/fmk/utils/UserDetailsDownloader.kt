package com.example.fmk.utils

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.fmk.data.entity.LoadUserDetailsState
import com.example.fmk.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class UserDetailsDownloader<T> @Inject constructor(
    private val repository: UserRepository,
) : DefaultLifecycleObserver {

    private val requestMap = ConcurrentHashMap<T, String>()
    private val downloadFlow = MutableSharedFlow<T>()
    private var downloadJob: Job? = null
    private var listener: ((T, LoadUserDetailsState) -> Unit)? = null

    override fun onCreate(owner: LifecycleOwner) {
        downloadJob = CoroutineScope(Dispatchers.IO).launch {
            downloadFlow.collect { target ->
                val login = requestMap[target] ?: return@collect
                repository.getUserDetail(login).fold(
                    onSuccess = { user ->

                        onMainTread {
                            if (requestMap[target] == login) {
                                requestMap.remove(target)
                                listener?.invoke(target, LoadUserDetailsState.Loaded(user))
                            }
                        }

                    },
                    onFailure = {
                        onMainTread { listener?.invoke(target, LoadUserDetailsState.Error(it)) }
                    }
                )
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        downloadJob?.cancel()
        listener = null
        requestMap.clear()
    }

    suspend fun queueThumbnail(target: T, url: String) {
        requestMap[target] = url
        downloadFlow.emit(target)
    }

    fun setListener(listener: (T, LoadUserDetailsState) -> Unit) {
        this.listener = listener
    }



    private suspend fun onMainTread(block: suspend CoroutineScope.() -> Unit) {
        withContext(Dispatchers.Main, block)
    }
}