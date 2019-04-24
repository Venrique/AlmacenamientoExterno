package com.venrique.external_storage

import android.content.pm.PackageManager
import android.nfc.Tag
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    val TAG = "MAIN_ACTIVITY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        fun isExternalStorageWritable():Boolean {
            return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
        }

        fun isExternalStorageReadable():Boolean {
            return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())
        }

        btn_save.setOnClickListener{
            if(isExternalStorageWritable() && checkPermiso(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                var textfile = File(getExternal(),"External_File_Test.txt")
                var fos = FileOutputStream(textfile)
                fos.write(frase_et.text.toString().toByteArray())
                fos.close()

                Toast.makeText(this,"File Saved", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"File NOT Saved", Toast.LENGTH_SHORT).show()

            }
        }

        btn_read.setOnClickListener {
            if (isExternalStorageReadable() && checkPermiso(android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                var sb = StringBuilder()
                var textfile_read = File(getExternal(),"External_File_Test.txt")
                var fis = FileInputStream(textfile_read)

                if (fis!=null){
                    var isr = InputStreamReader(fis)
                    var br = BufferedReader(isr)

                    sb.append(br.readLine())

                    fis.close()
                }
                frase_tv.text = sb
            }
        }

    }

    private fun getExternal(): String? {
        val storages = System.getenv("SECONDARY_STORAGE")
        var sd: String? = ""
        for (i in 0..storages.length){
            if(storages[i].toString() == ":"){
                Log.d(TAG,sd.toString())
                return sd
            }
            sd+= storages[i]
        }
        return sd
    }

    fun checkPermiso(permisos:String): Boolean{
        val check = ContextCompat.checkSelfPermission(this,permisos)
        return (check == PackageManager.PERMISSION_GRANTED)
    }
}
