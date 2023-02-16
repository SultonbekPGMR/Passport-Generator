package com.sultonbek1547.passportgenerator

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sultonbek1547.passportgenerator.BuildConfig.APPLICATION_ID
import com.sultonbek1547.passportgenerator.database.AppDatabase
import com.sultonbek1547.passportgenerator.database.User
import com.sultonbek1547.passportgenerator.databinding.ActivityAddBinding
import com.sultonbek1547.passportgenerator.databinding.BottomSheetCamGalBinding
import com.sultonbek1547.passportgenerator.utils.MyObject.ADDED
import com.sultonbek1547.passportgenerator.utils.MyObject.USER
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class AddActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddBinding.inflate(layoutInflater) }
    private lateinit var appDatabase: AppDatabase
    private var photoSelectedState = false
    private lateinit var photoUri: Uri
    private lateinit var photoPath: String
    private var cameraUsedState = false
    var databaseState = false
    private lateinit var lastUserInDb: User
    private var imageFileName = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        appDatabase = AppDatabase.getInstance(this)

        if (appDatabase.myUserDao().getAllUsers().isEmpty()) {
            databaseState = true
        } else {
            lastUserInDb = appDatabase.myUserDao().getAllUsers().last()
        }

        binding.btnAdd.setOnClickListener {
            val bottomSheetDialog =
                BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme)
            val bottomSheetBinding = BottomSheetCamGalBinding.inflate(layoutInflater)
            bottomSheetDialog.setContentView(bottomSheetBinding.root)
            bottomSheetBinding.btnGallery.setOnClickListener {
                bottomSheetDialog.dismiss()
                getImageContent.launch("image/*")
            }
            bottomSheetBinding.btnCamera.setOnClickListener {
                bottomSheetDialog.dismiss()
                val imageFile = createFile()
                photoUri = FileProvider.getUriForFile(
                    this, APPLICATION_ID, imageFile!!
                )
                getImageContentCamera.launch(photoUri)
            }
            bottomSheetDialog.show()


        }


        var name = ""
        var surname = ""
        binding.apply {
            btnSave.setOnClickListener {
                name = edtName.text.toString().trim()
                surname = edtSurname.text.toString().trim()


                /** saving */
                if (name.isNotEmpty() && surname.isNotEmpty() && photoSelectedState) {
                    if (!cameraUsedState) {
                        savePhotoToDir()
                    }
                    val user = User()
                    user.name = name
                    user.surname  = surname
                    user.passportId = generatePassportId()
                    user.photoPath = photoPath
                    user.position = appDatabase.myUserDao().getMaxPosition() + 1

                    USER = user
                    appDatabase.myUserDao().addUser(USER!!)
                    ADDED = true
                    Toast.makeText(this@AddActivity, "Saved", Toast.LENGTH_SHORT).show()
                    finish()
                }


            }

        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }


    }

    private fun savePhotoToDir() {
        imageFileName = if (databaseState) {
            1
        } else {
            lastUserInDb.id!! + 1
        }
        val inputStream = contentResolver?.openInputStream(photoUri)
        val file = File(filesDir, "$imageFileName.jpg")
        photoPath = file.absolutePath
        val fileOutputStream = FileOutputStream(file)
        inputStream?.copyTo(fileOutputStream)
        inputStream?.close()
        fileOutputStream.close()
    }


    private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it ?: return@registerForActivityResult
        binding.image.setImageURI(it)
        photoUri = it
        photoSelectedState = true
    }

    private var getImageContentCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                binding.image.setImageURI(photoUri)
                imageFileName = if (databaseState) {
                    1
                } else {
                    lastUserInDb.id!! + 1
                }
                val inputStream = contentResolver?.openInputStream(photoUri)
                val file = File(filesDir, "$imageFileName.jpg")
                val fileOutputStream = FileOutputStream(file)
                inputStream?.copyTo(fileOutputStream)
                inputStream?.close()
                fileOutputStream.close()
                photoPath = file.absolutePath
                photoSelectedState = true
                cameraUsedState = true
            } else {
                cameraUsedState = false
            }
        }


    private fun generatePassportId(): String {
        val result = randomLetter() + randomNumber()
        return if (appDatabase.myUserDao().checkForPassportId(result).isEmpty()) {
            result
        } else {
            generatePassportId()
        }
    }

    fun randomNumber(): String {
        var result = ""
        for (i in 0..6) {
            result += Random.nextInt(10)
        }

        return result
    }

    fun randomLetter(): String {
        var result = ""
        result += Char(Random.nextInt(65, 91))
        result += Char(Random.nextInt(65, 91))

        return result
    }

    private fun createFile(): File? {
        val format = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val externalFilesDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_$format", ".jpg", externalFilesDir).apply {
            photoPath = absolutePath
        }
    }

}