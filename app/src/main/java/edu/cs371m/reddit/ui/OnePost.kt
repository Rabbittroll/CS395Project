package edu.cs371m.reddit

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import edu.cs371m.reddit.databinding.ActivityOnePostBinding
import edu.cs371m.reddit.glide.Glide


// XXX Write most of this file
class OnePost:  AppCompatActivity() {

    private var title : String? = ""
    private var truncTitle: String = ""
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

        if (title != null) {
            if (title!!.length > 30) {
                truncTitle = title!!.take(30) + "..."
            } else {
                truncTitle = title!!
            }
            binding.title.text = truncTitle
            supportActionBar?.title = truncTitle
        } else {
            binding.title.text = title
            supportActionBar?.title = title
        }

        binding.selfText.text = selfText
        Glide.glideFetch(imageURL!!, thumbnailURL!!, binding.image)


        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Menu is already inflated by main activity
            }
            // XXX Write me, onMenuItemSelected
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if(menuItem.itemId == android.R.id.home){
                    finishAct()
                }
                return true
            }
        })

        // XXX Write me Set our currentUser variable based on what MainActivity passed us

    }

    private fun finishAct() {


        val returnIntent = Intent().apply {
        }

        setResult(RESULT_OK, returnIntent)
        finish()
        // XXX Write me
    }
}