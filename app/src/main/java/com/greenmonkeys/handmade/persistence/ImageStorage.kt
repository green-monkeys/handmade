package com.greenmonkeys.handmade.persistence

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object ImageStorage {
    fun saveImageToInternalStorage(imageName: String, image: Bitmap, context: Context) {
        val pictureFile = File(context.filesDir, imageName)
        val fOut = FileOutputStream(pictureFile)
        image.compress(Bitmap.CompressFormat.PNG, 100, fOut)
        fOut.flush()
        fOut.close()
    }

    fun retrieveImageFromInternalStorage(imageName: String, context: Context): Bitmap {
        val imageFile = File(context.filesDir, imageName)
        //val imageFile = File(context.filesDir, "${artisan.email.replace('@', '_').replace('.','_')}.png")
        return BitmapFactory.decodeStream(FileInputStream(imageFile))
    }
}