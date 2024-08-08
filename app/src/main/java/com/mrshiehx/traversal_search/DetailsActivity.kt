package com.mrshiehx.traversal_search

//noinspection SuspiciousImport
import android.R
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {
    companion object {
        var item: Item? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        intent?.getStringExtra("title")?.let { setTitle(it) }
        if (item != null) {
            val listView = ListView(this)
            listView.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            ).also { it.topMargin = 110 }
            listView.dividerHeight = 2
            listView.adapter = ItemsAdapter(this, List(item!!.founds.size) { item!! }, true)
            setContentView(listView)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            finish()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }
}