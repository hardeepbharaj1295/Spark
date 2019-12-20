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
import com.o7services.spark.model.DiaryContent
import java.util.*

class DiaryActivity : AppCompatActivity() {


    lateinit var mainConstraintLayout: ConstraintLayout
    lateinit var colorImageView: ImageView
    lateinit var deleteImageView: ImageView
    lateinit var dateTextView: TextView
    lateinit var descriptionEditText: EditText
    lateinit var backPressImageView: ImageView

    lateinit var backgroundColor: String

    var isNewItem = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        supportActionBar!!.hide()

        mainConstraintLayout = findViewById(R.id.diaryConstraintLayout)
        colorImageView = findViewById(R.id.diaryColorImageView)
        deleteImageView = findViewById(R.id.diaryDeleteImageView)
        dateTextView = findViewById(R.id.diaryDateTextView)
        descriptionEditText = findViewById(R.id.diaryDescriptionEditText)
        backPressImageView = findViewById(R.id.diaryBackPressImageView)


        val c = Calendar.getInstance()
        val currentYear = c.get(Calendar.YEAR)
        val currentMonth = c.get(Calendar.MONTH)
        val currentDay = c.get(Calendar.DAY_OF_MONTH)
        val currentHour = c.get(Calendar.HOUR_OF_DAY)
        val currentMinute = c.get(Calendar.MINUTE)
        val day = "${getWeekDay(
            currentYear,
            currentMonth,
            currentDay
        )} ${getMonthInWords(currentMonth)} $currentDay,$currentYear"


        var position = intent.getIntExtra("position", 999999)
        if (position == 999999) {
            if(DiaryContent.ITEMS.size != 0)
            for(i in 0..DiaryContent.ITEMS.size-1){
                if(DiaryContent.ITEMS[i].date.equals(day))
                    isNewItem = false
                position = i
            }
            if(isNewItem){
                dateTextView.setText(day)
                backgroundColor = "Default Color"
            } else {
                dateTextView.setText(DiaryContent.ITEMS[position].date)
                descriptionEditText.setText(DiaryContent.ITEMS[position].description)
                backgroundColor = DiaryContent.ITEMS[position].color
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(DiaryContent.ITEMS[position].color)))
            }

        } else {
            dateTextView.setText(DiaryContent.ITEMS[position].date)
            descriptionEditText.setText(DiaryContent.ITEMS[position].description)
            backgroundColor = DiaryContent.ITEMS[position].color
            mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(DiaryContent.ITEMS[position].color)))
        }

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
                dialog.dismiss()
            }
            frolyLinearLayout.setOnClickListener {
                backgroundColor = "Froly"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog.dismiss()
            }
            bananaLinearLayout.setOnClickListener {
                backgroundColor = "Banana"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog.dismiss()
            }
            parisDaisyLinearLayout.setOnClickListener {
                backgroundColor = "Paris Daisy"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog.dismiss()
            }
            reefLinearLayout.setOnClickListener {
                backgroundColor = "Reef"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog.dismiss()
            }
            aeroBlueLinearLayout.setOnClickListener {
                backgroundColor = "Aero Blue"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog.dismiss()
            }
            hummingBirdLinearLayout.setOnClickListener {
                backgroundColor = "Humming Bird"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog.dismiss()
            }
            sailLinearLayout.setOnClickListener {
                backgroundColor = "Sail"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog.dismiss()
            }
            mauveLinearLayout.setOnClickListener {
                backgroundColor = "Mauve"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog.dismiss()
            }
            lotusPinkLinearLayout.setOnClickListener {
                backgroundColor = "Lotus Pink"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog.dismiss()
            }
            cashmereLinearLayout.setOnClickListener {
                backgroundColor = "Cashmere"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog.dismiss()
            }
            athensGrayLinearLayout.setOnClickListener {
                backgroundColor = "Athens Gray"
                mainConstraintLayout.setBackgroundColor(Color.parseColor(getColorHex(backgroundColor)))
                dialog.dismiss()
            }
            dialog.show()
        })

        deleteImageView.setOnClickListener(View.OnClickListener {
            if (position == 999999) {
                Toast.makeText(this, "Item Discarded", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                val db = DatabaseQueries(this)
                db.deleteDiary(DiaryContent.ITEMS[position])
//                Toast.makeText(this, "Item Deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        backPressImageView.setOnClickListener(View.OnClickListener {
            var isDescriptionEmpty = false
            if (descriptionEditText.text.toString().equals("")) {
                if (position == 999999) {
                    finish()
                } else {
                    descriptionEditText.setError("Description Required")
                }
            } else {
                var db = DatabaseQueries(this)
                if (position == 999999) {
                    var result = db.insertDiary(day, descriptionEditText.text.toString(), backgroundColor)
                    if (result >= 0) {
//                        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    var result = db.updateDiary(
                        DiaryContent.DiaryItem(
                            dateTextView.text.toString(),
                            descriptionEditText.text.toString(),
                            backgroundColor
                        ), DiaryContent.ITEMS[position]
                    )
                    if (result >= 0) {
//                        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

            }
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


    fun getMonthInWords(month: Int): String {
        return when (month) {
            0 -> "Jan"
            1 -> "Feb"
            2 -> "Mar"
            3 -> "Apr"
            4 -> "May"
            5 -> "Jun"
            6 -> "Jul"
            7 -> "Aug"
            8 -> "Sep"
            9 -> "Oct"
            10 -> "Nov"
            11 -> "Dec"
            else -> ""
        }
    }

    fun getWeekDay(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val day = calendar.get(Calendar.DAY_OF_WEEK)
        return when (day) {
            Calendar.SUNDAY -> "Sun"
            Calendar.MONDAY -> "Mon"
            Calendar.TUESDAY -> "Tue"
            Calendar.WEDNESDAY -> "Wed"
            Calendar.THURSDAY -> "Thu"
            Calendar.FRIDAY -> "Fri"
            Calendar.SATURDAY -> "Sat"
            else -> ""
        }
    }

}

