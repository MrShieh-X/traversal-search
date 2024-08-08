package com.mrshiehx.traversal_search

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.util.LinkedList

class MainActivity : AppCompatActivity() {
    private lateinit var directory: EditText
    private lateinit var content: EditText
    private lateinit var listView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        directory = findViewById(R.id.directory)
        content = findViewById(R.id.content)
        listView = findViewById(R.id.listView)
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val item = parent.getItemAtPosition(position) as Item
                if (item.founds.size > 1) {
                    DetailsActivity.item = item
                    startActivity(
                        Intent(this, DetailsActivity::class.java).putExtra(
                            "title",
                            item.fileName
                        )
                    )
                }
            }
    }

    fun inputLyricsPath(view: View) {
        directory.setText("/storage/emulated/0/MrShiehX/LRC")
        directory.setSelection(directory.getText().length);
    }

    fun inputMovieSubtitlesPath(view: View) {
        directory.setText("/storage/emulated/0/MrShiehX/MovieSubtitles")
        directory.setSelection(directory.getText().length);
    }

    fun search(view: View) {
        if (directory.text.isNullOrBlank()) return
        if (content.text.isNullOrEmpty()) return

        val dir = File(directory.text.toString())
        val searchContent: String = content.text.toString()

        val dialog = AlertDialog.Builder(this)
            .setMessage("搜索中...")
            .setCancelable(false)
            .show()

        val allFiles = LinkedList<File>()
        traverseGetNotEmptyFiles(allFiles, dir)

        val read = LinkedList<Item>()
        val failedToRead = LinkedList<File>()

        for (file in allFiles) {
            try {
                var item: Item? = null
                val lines = readFileContentAsLines(file)
                for ((lineCount, line) in lines.withIndex()) {
                    val indexOf: Int
                    if (line.indexOf(searchContent).also { indexOf = it } >= 0) {
                        if (item == null)
                            item = Item(file.name, LinkedList())

                        val lastCurrentAndNextLine = HashMap<Int, CharSequence>()

                        if (lineCount - 1 >= 0) {
                            lastCurrentAndNextLine[lineCount - 1 + 1/*for displaying*/] =
                                lines[lineCount - 1]
                        }

                        val msp = SpannableString(line);
                        msp.setSpan(
                            BackgroundColorSpan(Color.MAGENTA),
                            indexOf,
                            indexOf + searchContent.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        lastCurrentAndNextLine[lineCount + 1/*for displaying*/] = msp


                        if (lineCount + 1 < lines.size) {
                            lastCurrentAndNextLine[lineCount + 1 + 1/*for displaying*/] =
                                lines[lineCount + 1]
                        }

                        item.founds.add(lastCurrentAndNextLine)
                    }
                }
                item?.let { read.add(it) }
            } catch (e: Exception) {
                e.printStackTrace()
                failedToRead.add(file)
            }
        }

        Toast.makeText(this, "搜索完毕", Toast.LENGTH_SHORT).show()
        finished(read, failedToRead)
        dialog.dismiss()
    }

    private fun finished(read: List<Item>, failedToRead: List<File>) {
        if (failedToRead.isNotEmpty()) {
            val sb = StringBuilder()
            failedToRead.forEach { sb.append(it.name).append("\n") }
            AlertDialog.Builder(this)
                .setTitle("以下文件读取失败")
                .setMessage(sb.substring(0, sb.length - 1))
                .show()

        }
        listView.adapter = ItemsAdapter(this, read, false)
    }

    private fun traverseGetNotEmptyFiles(list: MutableList<File>, fileOrDir: File) {
        if (!fileOrDir.exists()) return
        if (fileOrDir.isFile && fileOrDir.length() != 0L) {
            list.add(fileOrDir)
        } else {
            val files = fileOrDir.listFiles() ?: return
            for (file in files) {
                if (file.isFile && file.length() != 0L) {
                    list.add(file)
                } else {
                    traverseGetNotEmptyFiles(list, file)
                }
            }
        }
    }

    @Throws(IOException::class)
    fun readFileContentAsLines(file: File): List<String> {
        val lines = Files.lines(file.toPath())
        val list = LinkedList<String>()
        lines.forEach { list.add(it) }
        lines.close()
        return list
    }

    /*@Throws(IOException::class)
    fun readFileContentAsLines(file: File): List<String> {
        val reader: BufferedReader
        val list=LinkedList<String>()
        val fr = FileReader(file)
        reader = BufferedReader(fr)
        var tempStr: String
        while (reader.readLine().also { tempStr = it } != null) {
            list.add(tempStr)
        }
        fr.close()
        reader.close()
        return list
    }*/
}

