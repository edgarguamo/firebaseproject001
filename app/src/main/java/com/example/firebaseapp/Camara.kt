package com.example.firebaseapp

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.content.contentValuesOf


import kotlinx.android.synthetic.main.activity_camara.*
import java.io.File
import java.util.jar.Manifest

private const val REQUEST_CODE = 42
private lateinit var fotofile: File
private const val FILE_NAME = "foto.jpg"

class Camara : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camara)

        btn_camara.setOnClickListener{
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            fotofile = getfotofile(FILE_NAME)

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fotofile)
            val fileProvider = FileProvider.getUriForFile(this,
                "com.example.firebaseapp.fileprovider", fotofile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            if ( takePictureIntent.resolveActivity( this.packageManager ) != null ){
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            } else {
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getfotofile( filename :String ):File {
        val almacenamiento = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(filename, ".jpg", almacenamiento)
    }

    override fun onActivityResult( requestCode: Int, resultCode: Int, data: Intent? ){
        if ( requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK ){

            val takenImage = BitmapFactory.decodeFile(fotofile.absolutePath)
            iv_camara.setImageBitmap(takenImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}