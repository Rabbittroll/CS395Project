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
import edu.cs371m.reddit.glide.Glide


// XXX Write most of this file
class OnePost:  AppCompatActivity() {

    private var title : String? = ""
    private var selfText : String? = ""
    private var imageURL : String? = ""
    private var thumbnailURL : String? = ""
    private lateinit var binding : ActivityOnePostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        title = intent.getStringExtra("title").toString()
        selfText = intent.getStringExtra("selfText").toString()
        imageURL = intent.getStringExtra("imageURL").toString()
        thumbnailURL = intent.getStringExtra("thumbnailURL").toString()

        supportActionBar?.title = title
        binding.title.text = title
        binding.selfText.text = selfText
        Glide.glideFetch(imageURL!!, thumbnailURL!!, binding.image)

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