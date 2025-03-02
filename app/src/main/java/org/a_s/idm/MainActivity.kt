package org.a_s.idm

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.a_s.idm.data.repository.DownloadMangerImpl
import org.a_s.idm.domain.local.IdGenerator
import org.a_s.idm.domain.model.DownloadEntry
import org.a_s.idm.domain.model.DownloadStatus
import org.a_s.idm.domain.repository.DownloadManager
import org.a_s.idm.domain.repository.DownloadWorker
import org.a_s.idm.ui.theme.IDMTheme
import org.a_s.idm.utlis.Constants.TAG
import org.koin.android.ext.android.inject
import java.io.File


class MainActivity : ComponentActivity() {

    private fun createPublicFolder() {
        if (VERSION.SDK_INT >= VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.MIME_TYPE, "vnd.android.document/directory") // Required for folder
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/IDM") // Path to create
            }

            val uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

            if (uri != null) {
                Toast.makeText(this, "Folder created in Downloads", Toast.LENGTH_LONG).show()
                mkdir2(uri)
            } else {
                Toast.makeText(this, "Failed to create folder", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Scoped storage requires Android 10+", Toast.LENGTH_LONG).show()
        }
    }

    private fun mkdir() {
        val directory = File(Environment.getExternalStorageDirectory(), "IDM")
        directory.mkdir()
    }

    private fun mkdir2(root : Uri) {
        val resolver = contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "example.txt")
            put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/IDM")

        }

        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                outputStream.write("Hello, world!".toByteArray())
            }
            Toast.makeText(this, "File saved in MyFolder", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mkdir3() {
        val target = this.getExternalFilesDir(null) //create on data

        val file = File(target,"IDM")
        Log.d(TAG, "mkdir3: ${file.path}")
        file.mkdir()
    }

    private fun mkdir4() {
        val type = Environment.DIRECTORY_MOVIES
        val target = Environment.getExternalStoragePublicDirectory(type)
        val file = File(target,"test.mkv").apply {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        createPublicFolder()
//        mkdir() //need permission
//        mkdir3()



        enableEdgeToEdge()
        setContent {
            IDMTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val idg : IdGenerator by inject()
                    val worker : DownloadWorker by inject()

                    TestScreen(
                        modifier = Modifier.padding(innerPadding),
                        dm = DownloadMangerImpl(),
                        entry = DownloadEntry(
                            totalBytes = 100,
                            status = DownloadStatus.PENDING
                        ),
                        idg = idg,
                        worker = worker
                    )
                }
            }
        }
    }
}

@Composable
fun TestScreen(
    modifier: Modifier,
    dm: DownloadManager,
    entry: DownloadEntry,
    idg: IdGenerator,
    worker: DownloadWorker
) {
    val scope = rememberCoroutineScope()
    var i by remember {
        mutableIntStateOf(0)
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
        ) {
            Text(
                text = i.toString()
            )
            Button(
                onClick = {
                    scope.launch {
                       try {
                           worker.initialize(url)
                       } catch (e : Exception) {
                           Log.d(TAG, "init error: $e")
                       }
                    }
                }
            ) {
                Text(
                    text = "init"
                )
            }
            Button(
                modifier = Modifier,
                onClick = {
                    scope.launch {
                        worker.download()
                    }
                },
            ) {
                Text("start")
            }
            Button(
                onClick = {
                    scope.launch {
                    }
                }
            ) {
                Text(
                    text = "Cancel"
                )
            }
        }
    }
}



val url = "https://rondl1di1.unnecessarilycomplicatedbananatheory.ir/N/anime/2025/Winter/Solo%20Leveling%20S2/720%20x265/Solo%20Leveling%20S2%20-%2001.%5BSS%5D%5B720%5D%5Bx265%5D%5BXylander%20Mortreaux%5D.mkv"