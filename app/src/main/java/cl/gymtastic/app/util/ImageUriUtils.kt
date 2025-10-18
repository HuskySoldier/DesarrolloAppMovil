package cl.gymtastic.app.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

object ImageUriUtils {
    fun createTempImageUri(context: Context): Uri {
        val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val imagesDir = File(context.cacheDir, "images").apply { mkdirs() }
        val image = File(imagesDir, "IMG_${time}.jpg")
        return FileProvider.getUriForFile(context, "cl.gymtastic.app.fileprovider", image)
    }
}
