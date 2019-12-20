package com.o7services.spark

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.o7services.spark.database.DatabaseQueries
import com.o7services.spark.model.NoteContent

class NoteActivity : AppCompatActivity() {

    lateinit var mainConstraintLayout: ConstraintLayout
    lateinit var archiveImageView: ImageView
    lateinit var colorImageView: ImageView
    lateinit var deleteImageView: ImageView
    lateinit var titleEditText: EditText
    lateinit var descriptionEditText: EditText
    lateinit var backPressImageView: ImageView

    lateinit var backgroundColor: String
    var isArchived : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        supportActionBar!!.hide()

        mainConstraintLayout = findViewById(R.id.noteConstraintLayout)
        archiveImageView = findViewById(R.id.noteArchiveImageView)
        colorImageView = findViewById(R.id.noteColorImageView)
        deleteImageView = findViewById(R.id.noteDeleteImageView)
        titleEditText = findViewById(R.id.noteTitleEditText)
        descriptionEditText = findViewById(R.id.noteDescriptionEditText)
        backPressImageView = findViewById(R.id.noteBackPressImageView)

        var position = intent.getIntExtra("position", 999999)
        if (position == 999999) {

            backgroundColor = "Default Color"
            isArchived = false

        } else {
            mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(NoteContent.ITEMS[position].color)))
            titleEditText.setText(NoteContent.ITEMS[position].title)
            descriptionEditText.setText(NoteContent.ITEMS[position].description)

            backgroundColor = NoteContent.ITEMS[position].color
            isArchived = NoteContent.ITEMS[position].isArchived

        }

        archiveImageView.setOnClickListener(View.OnClickListener {
            if(isArchived){
                Toast.makeText(this,"Unarchived",Toast.LENGTH_SHORT).show()
                isArchived = false
                archiveImageView.setImageResource(R.drawable.ic_archive_grey)
            } else {
                Toast.makeText(this,"Archived",Toast.LENGTH_SHORT).show()
                isArchived = true
                archiveImageView.setImageResource(R.drawable.ic_unarchive_grey)
            }

        })

        colorImageView.setOnClickListener(View.OnClickListener {

                val dialog = Dialog(this)
                dialog.setContentView(R.layout.pick_color_dialog)
                dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val whiteLinearLayout = dialog.findViewById(R.id.whiteLinearLayout) as LinearLayout
                val frolyLinearLayout = dialog.findViewById(R.id.frolyLinearLayout) as LinearLayout
                val bananaLinearLayout = dialog.findViewById(R.id.bananaLinearLayout) as LinearLayout
                val parisDaisyLinearLayout = dialog.findViewById(R.id.parisDaisyLinearLayout) as LinearLayout
                val reefLinearLayout = dialog.findViewById(R.id.reefLinearLayout) as LinearLayout
                val aeroBlueLinearLayout = dialog.findViewById(R.id.aeroBlueLinearLayout) as LinearLayout
                val hummingBirdLinearLayout = dialog.findViewById(R.id.hummingBirdLinearLayout) as LinearLayout
                val sailLinearLayout = dialog.findViewById(R.id.sailLinearLayout) as LinearLayout
                val mauveLinearLayout = dialog.findViewById(R.id.mauveLinearLayout) as LinearLayout
                val lotusPinkLinearLayout = dialog.findViewById(R.id.lotusPinkLinearLayout) as LinearLayout
                val cashmereLinearLayout = dialog.findViewById(R.id.cashmereLinearLayout) as LinearLayout
                val athensGrayLinearLayout = dialog.findViewById(R.id.athensGrayLinearLayout) as LinearLayout

                whiteLinearLayout.setOnClickListener {
                    backgroundColor = "Default Color"
                    mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                    dialog .dismiss()
                }
                frolyLinearLayout.setOnClickListener {
                    backgroundColor = "Froly"
                    mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                    dialog .dismiss()
                }
                bananaLinearLayout.setOnClickListener {
                    backgroundColor = "Banana"
                    mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                    dialog .dismiss()
                }
                parisDaisyLinearLayout.setOnClickListener {
                    backgroundColor = "Paris Daisy"
                    mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                    dialog .dismiss()
                }
                reefLinearLayout.setOnClickListener {
                    backgroundColor = "Reef"
                    mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                    dialog .dismiss()
                }
                aeroBlueLinearLayout.setOnClickListener {
                    backgroundColor = "Aero Blue"
                    mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                    dialog .dismiss()
                }
                hummingBirdLinearLayout.setOnClickListener {
                    backgroundColor = "Humming Bird"
                    mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                    dialog .dismiss()
                }
                sailLinearLayout.setOnClickListener {
                    backgroundColor = "Sail"
                    mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                    dialog .dismiss()
                }
                mauveLinearLayout.setOnClickListener {
                    backgroundColor = "Mauve"
                    mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                    dialog .dismiss()
                }
                lotusPinkLinearLayout.setOnClickListener {
                    backgroundColor = "Lotus Pink"
                    mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                    dialog .dismiss()
                }
                cashmereLinearLayout.setOnClickListener {
                    backgroundColor = "Cashmere"
                    mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                    dialog .dismiss()
                }
                athensGrayLinearLayout.setOnClickListener {
                    backgroundColor = "Athens Gray"
                    mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                    dialog .dismiss()
                }
                dialog .show()
            })

        deleteImageView.setOnClickListener(View.OnClickListener {
            if(position == 999999){
                Toast.makeText(this,"Note Discarded",Toast.LENGTH_SHORT).show()
                finish()
            } else {
                val db = DatabaseQueries(this)
                db.deleteNote(NoteContent.ITEMS[position])
//                Toast.makeText(this,"Note Deleted",Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        backPressImageView.setOnClickListener(View.OnClickListener {
            var isTitleEmpty : Boolean = false
            var isDescriptionEmpty : Boolean = false
            if(titleEditText.text.isEmpty())
                isTitleEmpty = true
            if(descriptionEditText.text.isEmpty())
                isDescriptionEmpty = true
            if(!isTitleEmpty && !isDescriptionEmpty){
                if(isArchived){
                    var db = DatabaseQueries(this)

                    if(position != 999999)
                        db.deleteNote(NoteContent.ITEMS[position])

                    var result = db.insertArchive(titleEditText.text.toString(),descriptionEditText.text.toString(),backgroundColor)
                        if(result >= 0){
                            Toast.makeText(this,"Note Archived", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this,"Error Occurred", Toast.LENGTH_SHORT).show()
                            finish()
                        }


                } else {
                    val db = DatabaseQueries(this)
                    if(position == 999999){
                        var result : Long = db.insertNote(titleEditText.text.toString(),descriptionEditText.text.toString(),backgroundColor)
                        if(result >= 0){
//                            Toast.makeText(this,"Note Saved", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this,"Error Occurred", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    } else {
                        var result : Int = db.updateNote(NoteContent.ITEMS[position],
                            NoteContent.NoteItem(titleEditText.text.toString(),descriptionEditText.text.toString(),backgroundColor,false))
                        if(result >= 0){
//                            Toast.makeText(this,"Note Updated", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this,"Error Occurred", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                }
            }
            if(isTitleEmpty && isDescriptionEmpty){
                Toast.makeText(this,"Note Discarded", Toast.LENGTH_SHORT).show()
                finish()
            }
            if(isTitleEmpty && !isDescriptionEmpty)
                titleEditText.setError("Title Required")
            if(!isTitleEmpty && isDescriptionEmpty)
                descriptionEditText.setError("Description Required")

        })

    }


    fun getColorHex(color: String): String {
        return when (color) {
            "Default Color" -> "#ffffff"
            "Froly" -> "#F28B82"
            "Banana" -> "#FBBC04"
            "Paris Daisy" -> "#FFF475"
            "Reef" -> "#CCFF90"
            "Aero Blue" -> "#A7FFEB"
            "Humming Bird" -> "#CBF0F8"
            "Sail" -> "#AECBFA"
            "Mauve" -> "#D7AEFB"
            "Lotus Pink" -> "#FDCFE8"
            "Cashmere" -> "#E6C9A8"
            "Athens Gray" -> "#E8EAED"
            else -> "#ffffff"
        }
    }
}
