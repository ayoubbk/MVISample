package com.bks.mvisample.ui.main

import androidx.lifecycle.*
import com.bks.mvisample.models.BlogPost
import com.bks.mvisample.models.User
import com.bks.mvisample.repository.Repository
import com.bks.mvisample.ui.main.state.MainStateEvent
import com.bks.mvisample.ui.main.state.MainStateEvent.*
import com.bks.mvisample.ui.main.state.MainViewState
import com.bks.mvisample.util.AbsentLiveData
import com.bks.mvisample.util.DataState

class MainViewModel : ViewModel() {

    private val _stateEvent : MutableLiveData<MainStateEvent> = MutableLiveData()
    private val _viewState : MutableLiveData<MainViewState> = MutableLiveData()

    val viewState : LiveData<MainViewState>
        get() = _viewState

    val dataState : LiveData<DataState<MainViewState>> = Transformations
        .switchMap(_stateEvent) { stateEvent ->
            stateEvent?.let {
                handleStateEvent(stateEvent)
            }
        }

    fun init() {
        println("DEBUG init MainViewModel stuff")
    }

    private fun handleStateEvent(stateEvent : MainStateEvent) : LiveData<DataState<MainViewState>> {
        return when (stateEvent) {
            is GetBlogPostsEvent -> {
                return Repository.getBlogPosts()
            }
            is GetUserEvent -> {
                return Repository.getUser(stateEvent.userId)
            }
            is None -> {
                AbsentLiveData.create()
            }
        }
    }

    fun setBlogListData(blogPosts: List<BlogPost>) {
        val update = getCurrentViewStateOrNew()
        update.blogPosts = blogPosts
        _viewState.value = update
    }

    fun setUserData(user : User) {
        val update = getCurrentViewStateOrNew()
        update.user = user
        _viewState.value = update
    }

    fun getCurrentViewStateOrNew() : MainViewState {
        return viewState.value?.let {
            it
        }?: MainViewState()
    }


    fun setStateEvent(event : MainStateEvent) {
        _stateEvent.value = event
    }

}