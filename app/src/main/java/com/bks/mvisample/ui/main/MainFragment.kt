package com.bks.mvisample.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bks.mvisample.R
import com.bks.mvisample.ui.main.state.MainStateEvent

class MainFragment : Fragment() {

    /**  this is how we initialize ViewModel using SavedStateViewModelFactory */
//    private val viewModel1: MainViewModel by viewModels {
//        SavedStateViewModelFactory(application, activity)
//    }

    /**  We can initialize viewModel by viewModels extension */
    // private val viewModel by viewModels<MainViewModel>()

    /** #1 we can pass ViewModelFactory to instantiate ViewModel  */
    //val viewModelTest by viewModels<MainViewModel> { getViewModel() }

    /** the ViewModel will be scoped to the activity and will live as long as it lives,
     * and there will be only one instance. This is a super effective way to share data
     * between fragments attached to the same activity, and allows them to react to changes
     * super effectively if data binding is being used. */
//    val viewModel2 by lazy {
//        activity?.getViewModel{ MainViewModel() }
//    }

//    val viewModel1 by lazy {
//        activity?.getViewModel {
//            MainViewModel().apply { init() }
//        }
//    }

    private lateinit var viewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = activity?.let {
            ViewModelProvider(this).get(MainViewModel::class.java)
        }?: throw Exception("Invalid activity")

        subscribeObserver()
    }

    fun subscribeObserver() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer {dataState ->
            println("DEBUG : dataState : $dataState")
            dataState.blogPosts?.let { blogPosts ->
                // set BlogPosts data
                viewModel.setBlogListData(blogPosts)
            }

            dataState.user?.let {user ->
                // set User data
                viewModel.setUserData(user)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer {viewState ->
            viewState.blogPosts?.let {
                println("DEBUG setting BlogPosts to RecyclerView : $it")
            }

            viewState.user?.let {
                println("DEBUG set User data : $it")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_get_user -> triggerGetUserEvent()
            R.id.action_get_blogs -> triggerGetBlogsEvent()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun triggerGetBlogsEvent() {
        viewModel.setStateEvent(MainStateEvent.GetBlogPostsEvent)
    }

    private fun triggerGetUserEvent() {
        viewModel.setStateEvent(MainStateEvent.GetUserEvent("1"))
    }


}