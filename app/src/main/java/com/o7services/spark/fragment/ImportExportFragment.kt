package com.o7services.spark.fragment


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.loader.content.CursorLoader
import com.o7services.spark.R
import com.o7services.spark.database.DatabaseCreation
import java.io.File


class ImportExportFragment : Fragment() {

    lateinit var importImageView: ImageView
    lateinit var importTextView : TextView
    lateinit var exportTextView: TextView
    lateinit var exportImageView: ImageView
    lateinit var drawerOpener : ImageView
    lateinit var drawer : DrawerLayout

    private val MY_PERMISSIONS_REQUEST_CODE = 123
    lateinit var db: DatabaseCreation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_import_export, container, false)

        importImageView = view.findViewById(R.id.importImageView)
        importTextView = view.findViewById(R.id.importTextView)
        exportImageView = view.findViewById(R.id.exportImageView)
        exportTextView = view.findViewById(R.id.exportTextView)
        drawerOpener = view.findViewById(R.id.drawerOpenerImportExportImageView)
        drawer = activity!!.findViewById(R.id.drawer_layout)

        db = DatabaseCreation(activity!!.baseContext)
        permissions()


        importTextView.setOnClickListener(View.OnClickListener {
            importData()
        })

        importImageView.setOnClickListener(View.OnClickListener {
            importData()
        })

        exportTextView.setOnClickListener(View.OnClickListener {
            export()
        })

        exportImageView.setOnClickListener(View.OnClickListener {
            export()
        })

        drawerOpener.setOnClickListener(View.OnClickListener {
            drawer.openDrawer(GravityCompat.START)
        })


        return view
    }

    fun permissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                        + ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                )) != PackageManager.PERMISSION_GRANTED
            ) {
                // Do something, when permissions not granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    // If we should give explanation of requested permissions

                    // Show an alert dialog here with request explanation
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("Read and Write permissions are required to do the task.")
                    builder.setTitle("Please grant those permissions")
                    builder.setPositiveButton("OK") { dialogInterface, i ->
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ),
                            MY_PERMISSIONS_REQUEST_CODE
                        )
                    }
                    builder.setNeutralButton("Cancel", null)
                    val dialog = builder.create()
                    dialog.show()
                } else {
                    // Directly request for required permissions, without explanation
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        MY_PERMISSIONS_REQUEST_CODE
                    )
                }
            }
        }
    }

    fun importData() {

        performRestore(db)
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "*/*"
//        startActivityForResult(intent,1)
////        performRestore(db)
//        val folder = File(
//            Environment.getExternalStorageDirectory().toString() + File.separator + resources.getString(
//                R.string.app_name
//            )
//        )
//        if (folder.exists()) {
//
//            val files = folder.listFiles()
//
//            val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item)
//            for (file in files)
//                arrayAdapter.add(file.name)
//
//            val builderSingle = AlertDialog.Builder(this)
//            builderSingle.setTitle("Restore:")
//            builderSingle.setNegativeButton(
//                "cancel"
//            ) { dialog, which -> dialog.dismiss() }
//            builderSingle.setAdapter(
//                arrayAdapter
//            ) { dialog, which ->
//                try {
//                    db.importDB(files[which].path)
//                } catch (e: Exception) {
//                    Toast.makeText(
//                        context,
//                        "Unable to restore. Retry",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//            builderSingle.show()
//        } else
//            Toast.makeText(
//                context,
//                "Backup folder not present.\nDo a backup before a restore!",
//                Toast.LENGTH_SHORT
//            ).show()
    }

    fun export() {
        val outFileName =
            Environment.getExternalStorageDirectory().toString() + File.separator + resources.getString(R.string.app_name) + File.separator
        Log.e("PAth", outFileName)
        performBackup(db, outFileName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
//                val path = data!!.data.path.substring(data!!.data.path.indexOf('/', 1));
                    val path = getRealPathFromURI(data!!.data)
                    Log.e("Data ", path)
//                    performRestore(db, path)
                }
            }
        }
    }

    private fun getRealPathFromURI(contentUri: Uri): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(context!!, contentUri, proj, null, null, null)
        val cursor = loader.loadInBackground()
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(column_index)
        cursor.close()
        return result
    }

    //ask to the user a name for the backup and perform it. The backup will be saved to a custom folder.
    private fun performBackup(db: DatabaseCreation, outFileName: String) {

        val folder =
            File(Environment.getExternalStorageDirectory().toString() + File.separator + resources.getString(R.string.app_name))

        var success = true
        if (!folder.exists())
            success = folder.mkdirs()
        if (success) {

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Backup Name")
            val input = EditText(requireContext())
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)
            builder.setPositiveButton("Save", DialogInterface.OnClickListener { dialog, which ->
                val m_Text = input.text.toString()
                val out = "$outFileName$m_Text.db"
                db.backup(out)
            })
            builder.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            builder.show()
        } else
            Toast.makeText(requireContext(), "Unable to create directory. Retry", Toast.LENGTH_SHORT).show()
    }

    //ask to the user what backup to restore
    private fun performRestore(db: DatabaseCreation) {

//        db.importDB(path)
        Log.e("Existing",Environment.getExternalStorageDirectory().toString() + File.separator + resources.getString(R.string.app_name))

        val folder =
            File(Environment.getExternalStorageDirectory().toString() + File.separator + resources.getString(R.string.app_name))
        if (folder.exists()) {

            val files = folder.listFiles()

            val arrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.select_dialog_item)
            for (file in files)
                arrayAdapter.add(file.name)

            val builderSingle = AlertDialog.Builder(requireContext())
            builderSingle.setTitle("Restore:")
            builderSingle.setNegativeButton(
                "cancel",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            builderSingle.setAdapter(
                arrayAdapter,
                DialogInterface.OnClickListener { dialog, which ->
                    try {
                        db.importDB(files[which].path)
//                        db.importDB(path)
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Unable to restore. Retry", Toast.LENGTH_SHORT).show()
                    }
                })
            builderSingle.show()

        } else
            Toast.makeText(requireContext(), "Backup folder not present.\nDo a backup before a restore!", Toast.LENGTH_SHORT).show()
    }
}

