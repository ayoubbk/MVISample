package com.bks.mvisample.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bks.mvisample.R
import com.bks.mvisample.models.BlogPost
import com.bks.mvisample.models.User
import com.bks.mvisample.ui.DataStateListener
import com.bks.mvisample.ui.main.state.MainStateEvent
import com.bks.mvisample.util.TopSpacingItemDecoration
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_main.*
import java.lang.ClassCastException

class MainFragment : Fragment(), BlogPostsAdapter.Interaction {


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


    override fun onItemSelected(position: Int, item: BlogPost) {
        Log.d(TAG, "onItemSelected: position $position")
        Log.d(TAG, "onItemSelected: item :  $item")
    }
    
    private lateinit var viewModel: MainViewModel
    
    lateinit var dataStateHandler : DataStateListener

    lateinit var blogPostsAdapter: BlogPostsAdapter

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
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            val itemDecorator = TopSpacingItemDecoration(30)
            addItemDecoration(itemDecorator)
            blogPostsAdapter = BlogPostsAdapter(this@MainFragment)
            adapter = blogPostsAdapter
        }
    }


    private fun subscribeObserver() {
        // Get the DataState result and set it
        viewModel.dataState.observe(viewLifecycleOwner, Observer {dataState ->
            println("DEBUG : dataState : $dataState")

            // handle error and message
            dataStateHandler.onDataStateChange(dataState)

            // Handle Data<T>
            dataState.data?.let {event ->
                event.getContentIfNotHandled()?.let {mainViewState ->
                    Log.d(TAG, "subscribeObserver: MainViewState : $mainViewState")
                    mainViewState.blogPosts?.let { blogPosts ->
                        // set BlogPosts data
                        viewModel.setBlogListData(blogPosts)
                    }
                    mainViewState.user?.let {user ->
                        // set User data
                        viewModel.setUserData(user)
                    }
                }
            }
        })

        // Observe the ViewState
        viewModel.viewState.observe(viewLifecycleOwner, Observer {viewState ->
            viewState.blogPosts?.let {
                println("DEBUG setting BlogPosts to RecyclerView : $it")
                blogPostsAdapter.submitList(it)
            }

            viewState.user?.let {
                println("DEBUG set User data : $it")
                setUserProperties(it)
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

    private fun setUserProperties(user : User) {
        email.text = user.email
        username.text = user.username
        view?.let {
            Glide.with(it.context)
                .load(user.image)
                .into(image)
        }
    }

    private fun triggerGetBlogsEvent() {
        viewModel.setStateEvent(MainStateEvent.GetBlogPostsEvent)
    }

    private fun triggerGetUserEvent() {
        viewModel.setStateEvent(MainStateEvent.GetUserEvent("1"))
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataStateHandler = context as DataStateListener
        } catch (ex : ClassCastException) {
            Log.e(TAG, "onAttach: $context must implement DataStateListener")
        }
    }

    companion object {
        private const val TAG = "MainFragment"
    }

}