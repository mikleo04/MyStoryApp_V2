package com.example.mystoryapp.ui.story.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mystoryapp.database.Injection
import com.example.mystoryapp.databinding.ActivityAddStoryBinding
import com.example.mystoryapp.preferences.UserPreference
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import com.example.mystoryapp.database.Result

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var addStoryModel: AddStoryViewModel
    private var photoFile: File? = null

    companion object{
        const val CAMERA_X_RESULT_CODE = 22
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addStoryModel = AddStoryViewModel(Injection.provideRepository(this, UserPreference(this).getUser().token.toString()))

        binding.btnAddstorygalery.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            launcherIntentGallery.launch(Intent.createChooser(intent, "Choose a Picture"))
        }

        binding.btnAddstoryupload.setOnClickListener{
            if (photoFile == null){
                Toast.makeText(this, "Photo is required", Toast.LENGTH_SHORT).show()
            }else{
                addStoryModel.addNewStory(binding.etAddstorynote.text.toString(), 1.0, 1.0, photoFile!!).observe(this){
                    when (it) {
                        is Result.Loading -> {
                            Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show()
                        }
                        is Result.Error -> {
                            Toast.makeText(this, "Add story gagal", Toast.LENGTH_SHORT).show()
                        }
                        is Result.Success -> {
                            if(it.data.error == false){
                                finish()
                            }else{
                                Toast.makeText(this, "Add story gagal", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        
        binding.btnAddstorycamera.setOnClickListener {
            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    10
                )
            }else{
                launcherIntentCameraX.launch(Intent(this, CameraxActivity::class.java))
            }

        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT_CODE) {
            photoFile = it.data?.getSerializableExtra("picture") as File
        }

        if (it.resultCode == CAMERA_X_RESULT_CODE) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            binding.ivAddstorypreview.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            photoFile = createTemporaryFile(selectedImg)
            binding.ivAddstorypreview.setImageURI(selectedImg)
        }
    }
    private fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
        val matrix = Matrix()
        return if (isBackCamera) {
            matrix.postRotate(90f)
            Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        } else {
            matrix.postRotate(-90f)
            matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
            Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        }
    }

    private fun createTemporaryFile(uri: Uri): File {
        val tempFile = File.createTempFile("tempFile", ".jpg", this.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        val buffer = ByteArray(1024)
        var len: Int

        val inputStream = contentResolver.openInputStream(uri) as InputStream
        val outputStream: OutputStream = FileOutputStream(tempFile)

        while (inputStream.read(buffer).also { len = it } > 0 ){
            outputStream.write(buffer, 0, len)
        }
        outputStream.close()
        inputStream.close()
        return tempFile
    }
}