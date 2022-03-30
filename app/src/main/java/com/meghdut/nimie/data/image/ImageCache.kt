package com.meghdut.nimie.data.image

import android.content.Context
import android.util.Base64
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ImageCache @Inject constructor(@ApplicationContext val context: Context) {

    private val imageDir: File = context.getDir("imageCache", Context.MODE_PRIVATE)
    private val md = MessageDigest.getInstance("SHA-256")

    fun cacheImage(imageBytes: ByteArray): String {
        val digest: ByteArray = md.digest(imageBytes)
        val hashname = Base64.encodeToString(digest, Base64.URL_SAFE).trim()
        val imageFile = getImageFile(hashname)

        imageFile.writeBytes(imageBytes)

        return hashname
    }

     fun getImageFile(hashname: String): File {
        val name = "$hashname.png"
        val imageFile = File(imageDir, name)
         println("File stored at ${imageFile.path}")
        return imageFile
    }

}