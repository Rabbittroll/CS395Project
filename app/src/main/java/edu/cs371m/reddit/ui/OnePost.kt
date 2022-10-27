package edu.cs371m.reddit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import edu.cs371m.reddit.api.RedditPost
import edu.cs371m.reddit.databinding.FragmentRvBinding
import kotlin.random.Random
import edu.cs371m.reddit.databinding.ActivityOnePostBinding


// XXX Write most of this file
class OnePost:  AppCompatActivity() {

    private var title : String? = ""
    private var selfText : String? = ""
    private lateinit var binding : ActivityOnePostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = intent.getStringExtra("title").toString()
        selfText = intent.getStringExtra("selfText").toString()

        binding.title.text = title
        binding.selfText.text = selfText

        // XXX Write me Set our currentUser variable based on what MainActivity passed us

    }

    private fun finishAct() {
        //println(currentUser.toString())

        val returnIntent = Intent().apply {
        }

        //println(returnIntent)
        setResult(RESULT_OK, returnIntent)
        //var retScore = MainActivity.Score(currentUser.toString(), gameScore)
        //intent.putExtra("gameScore", gameScore)
        //setResult(RESULT_OK, intent)
        finish()
        // XXX Write me
    }
}