package com.wattpad.android.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.wattpad.android.R
import com.wattpad.android.data.viewmodel.StoriesViewModel
import com.wattpad.android.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * I used Paging 3 with Kotlin Coroutines Flow for this assignment. Purpose of using Paging library
 * is that it uses the database as a source of truth for the data that needs to be displayed in the UI.
 * Whenever user doesn't have any more data in the database, he/she needs to request more from the network.
 * So in this app I fetch stories from api and then persist to local database. The stories which user
 * sees is directly fetched from local database. So we don't need to ask user to switch to offline mode,
 * because whenever user opens the app in offline mode the stories will be shown from local db.
 * That way, the database will be the source of truth for our app and we will always
 * load data from there
 *
 * But I could add a toggle button in order user switch from online to offline or vise versa
 */


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var storiesViewModel: StoriesViewModel

    private var binding: ActivityMainBinding? = null
    private val viewBindingMainActivity get() = binding!!

    private val storiesAdapter = StoriesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBindingMainActivity.root)

        viewBindingMainActivity.recyclerViewStories.adapter = storiesAdapter
        viewBindingMainActivity.recyclerViewStories.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        lifecycleScope.launchWhenStarted{
            storiesViewModel.getStoriesFlow(true).collect {
                storiesAdapter.submitData(lifecycle, it)
            }
        }

        viewBindingMainActivity.recyclerViewStories.adapter =
            storiesAdapter.withLoadStateFooter(
            footer = StoryLoadStateAdapter { storiesAdapter.retry() }
            )

        viewBindingMainActivity.editTextSearch.addTextChangedListener(object: TextWatcher{

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().trim().isNotEmpty()){
                    getSearchedStories(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //TODO("Not yet implemented")
            }


        })

    }

    fun getSearchedStories(searchedText:String){
        lifecycleScope.launch {
            storiesViewModel.getSearchedStories(searchedText).collect {
                storiesAdapter.submitData(lifecycle, it)
            }
        }
    }


    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        val toast: Toast = Toast.makeText(this@MainActivity, getString(R.string.offline_message), Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.show()
    }
}