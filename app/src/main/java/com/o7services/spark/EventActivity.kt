package com.o7services.spark

import android.app.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.o7services.spark.database.DatabaseQueries
import com.o7services.spark.model.EventContent
import com.o7services.spark.model.EventContent.EventItem
import kotlinx.android.synthetic.main.activity_event.*
import java.util.*
import android.database.Cursor
import android.net.Uri
//import javax.swing.UIManager.put
import android.content.ContentValues
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
//import jdk.nashorn.internal.objects.NativeDate.getTime
import java.text.SimpleDateFormat
import java.util.jar.Manifest
import android.content.ContentUris




class EventActivity : AppCompatActivity() {

    lateinit var titleEditText : EditText
    lateinit var startDayTextView : TextView
    lateinit var endDayTextView : TextView
    lateinit var startTimeTextView : TextView
    lateinit var endTimeTextView : TextView
    lateinit var firstReminderTextView : TextView
    lateinit var secondReminderTextView : TextView
    lateinit var colorImageView : ImageView
    lateinit var colorTextView : TextView
    lateinit var descriptionEditText : EditText
    lateinit var saveButton: Button
    lateinit var backgroundImageView : ImageView
    lateinit var deleteImageView : ImageView
    lateinit var editImageView : ImageView
    lateinit var backPressImageView: ImageView
    var startMilliseconds: Long = 0
    var endMilliseconds: Long = 0

    var isFirstReminderSet : Boolean = false
    var isSecondReminderSet : Boolean = false
    var backdroundImageText : String = "Background Image 1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        supportActionBar!!.hide()

        checkPermissionRequirements()
        titleEditText = findViewById(R.id.eventTitleEditText)
        startDayTextView = findViewById(R.id.eventStartDayTextView)
        endDayTextView = findViewById(R.id.eventEndDayTextView)
        startTimeTextView = findViewById(R.id.eventStartTimeTextView)
        endTimeTextView = findViewById(R.id.eventEndTimeTextView)
        firstReminderTextView = findViewById(R.id.eventFirstReminderTextView)
        secondReminderTextView = findViewById(R.id.eventSecondReminderTextView)
        colorImageView = findViewById(R.id.eventColorImageView)
        colorTextView = findViewById(R.id.eventColorTextView)
        descriptionEditText = findViewById(R.id.eventDescriptionEditText)
        saveButton = findViewById(R.id.eventSaveButton)
        backgroundImageView = findViewById(R.id.backgroundImageView)
        deleteImageView = findViewById(R.id.eventDeleteImageView)
        editImageView = findViewById(R.id.eventEditImageView)
        backPressImageView = findViewById(R.id.eventBackPressImageView)

        val c = Calendar.getInstance()
        val currentYear = c.get(Calendar.YEAR)
        val currentMonth = c.get(Calendar.MONTH)
        val currentDay = c.get(Calendar.DAY_OF_MONTH)
        val currentHour=c.get(Calendar.HOUR_OF_DAY)
        val currentMinute=c.get(Calendar.MINUTE)

        //edit mode
        var position = intent.getIntExtra("position",999999)
        if(position == 999999){

            val day = "${getWeekDay(currentYear,currentMonth,currentDay)}, ${getMonthInWords(currentMonth)} $currentDay,$currentYear"
            val time = "${if(currentHour > 12) {currentHour-12} else currentHour}:${if(currentMinute in 0..9) "0$currentMinute" else currentMinute} ${if(currentHour > 12) "PM" else "AM"}"

            startDayTextView.text = day
            endDayTextView.text = day
            startTimeTextView.text = time
            endTimeTextView.text = time


            isFirstReminderSet = false
            isSecondReminderSet = false
            backdroundImageText = "Background Image 1"


        }
        else {
            titleEditText.setText(EventContent.ITEMS[position].title)
            startDayTextView.text = EventContent.ITEMS[position].startDay
            startTimeTextView.text = EventContent.ITEMS[position].startTime
            endDayTextView.text = EventContent.ITEMS[position].endDay
            endTimeTextView.text = EventContent.ITEMS[position].endTime
            firstReminderTextView.text = EventContent.ITEMS[position].firstReminder
            secondReminderTextView.text = EventContent.ITEMS[position].secondReminder
            colorTextView.text = EventContent.ITEMS[position].color
            descriptionEditText.setText(EventContent.ITEMS[position].description)
            colorImageView.setImageResource(getColorRes(EventContent.ITEMS[position].color))
            backgroundImageView.setImageResource(getBackgroundImageRes(EventContent.ITEMS[position].backgroundImage))
            backdroundImageText = EventContent.ITEMS[position].backgroundImage


            isFirstReminderSet = if(EventContent.ITEMS[position].firstReminder == "Don't notify me at the start of the event") false else true
            isSecondReminderSet = if(EventContent.ITEMS[position].firstReminder == "Add another reminder") true else false
            backdroundImageText = EventContent.ITEMS[position].backgroundImage
        }

        startDayTextView.setOnClickListener(View.OnClickListener {
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDay ->
                //Sun, Jan 1,2019
                val text :String = "${getWeekDay(selectedYear,selectedMonth,selectedDay)}, ${getMonthInWords(selectedMonth)} $selectedDay,$selectedYear"
                startDayTextView.text = text
            }, currentYear, currentMonth, currentDay).show()
        })

        endDayTextView.setOnClickListener(View.OnClickListener {
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDay ->
                //Sun, Jan 1,2019
                val text :String = "${getWeekDay(selectedYear,selectedMonth,selectedDay)}, ${getMonthInWords(selectedMonth)} $selectedDay,$selectedYear"
                endDayTextView.text = text
            }, currentYear, currentMonth, currentDay).show()
        })

        startTimeTextView.setOnClickListener(View.OnClickListener {
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{ view, selectedHour, selectedMinute ->
                //1:00 AM
                val text = "${if(selectedHour > 12) {selectedHour-12} else selectedHour}:${if(selectedMinute in 0..9) "0$selectedMinute" else selectedMinute} ${if(selectedHour > 12) "PM" else "AM"}"
                startTimeTextView.text = text
            },currentHour,currentMinute,false).show()
        })

        endTimeTextView.setOnClickListener(View.OnClickListener {
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{ view, selectedHour, selectedMinute ->
                //1:00 AM
                val text = "${if(selectedHour > 12) {selectedHour-12} else selectedHour}:${if(selectedMinute in 0..9) "0$selectedMinute" else selectedMinute} ${if(selectedHour > 12) "PM" else "AM"}"
                endTimeTextView.text = text
            },currentHour,currentMinute,false).show()
        })

        firstReminderTextView.setOnClickListener(View.OnClickListener {
            if(isFirstReminderSet) {
                isFirstReminderSet = false
                firstReminderTextView.text = "Don't notify me at the start of the event"
                Toast.makeText(this,"Reminder removed",Toast.LENGTH_SHORT).show()
                //remove reminder
            }
            else {
                isFirstReminderSet = true
                firstReminderTextView.text = "Notify me at the start of the event"
                Toast.makeText(this,"Reminder set, you will be notified",Toast.LENGTH_SHORT).show()
                //set reminder
            }
        })

        secondReminderTextView.setOnClickListener(View.OnClickListener {

            if(isSecondReminderSet) {
                isSecondReminderSet = false
                secondReminderTextView.text = "Add another reminder"
                Toast.makeText(this,"Reminder removed",Toast.LENGTH_SHORT).show()
                //remove reminder
            }
            else {
                isSecondReminderSet = true
                var date : String = ""
                var time : String = ""
                DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDay ->
                    //Sun, Jan 1,2019
                    date = "${getWeekDay(selectedYear,selectedMonth,selectedDay)}, ${getMonthInWords(selectedMonth)} $selectedDay,$selectedYear"

                    TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{ view, selectedHour, selectedMinute ->
                        //1:00 AM
                        time = "${if(selectedHour > 12) {selectedHour-12} else selectedHour}:${if(selectedMinute in 0..9) "0$selectedMinute" else selectedMinute} ${if(selectedHour > 12) "PM" else "AM"}"
                        secondReminderTextView.text = "We will notify you on \n$date    $time"
                        Toast.makeText(this,"Reminder set, you will be notified",Toast.LENGTH_SHORT).show()


                        //set reminder for $date and $time

                    },currentHour,currentMinute,false).show()

                }, currentYear, currentMonth, currentDay).show()


            }




        })

        val pickColorListener = View.OnClickListener {

            val dialog = Dialog(this)
            dialog.setContentView(R.layout.pick_color_dialog)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

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
                colorTextView.text = "Default Color"
                colorImageView.setImageResource(R.drawable.ic_color_white)
                dialog .dismiss()
            }
            frolyLinearLayout.setOnClickListener {
                colorTextView.text = "Froly"
                colorImageView.setImageResource(R.drawable.ic_color_froly)
                dialog .dismiss()
            }
            bananaLinearLayout.setOnClickListener {
                colorTextView.text = "Banana"
                colorImageView.setImageResource(R.drawable.ic_color_banana)
                dialog .dismiss()
            }
            parisDaisyLinearLayout.setOnClickListener {
                colorTextView.text = "Paris Daisy"
                colorImageView.setImageResource(R.drawable.ic_color_paris_daisy)
                dialog .dismiss()
            }
            reefLinearLayout.setOnClickListener {
                colorTextView.text = "Reef"
                colorImageView.setImageResource(R.drawable.ic_color_reef)
                dialog .dismiss()
            }
            aeroBlueLinearLayout.setOnClickListener {
                colorTextView.text = "Aero Blue"
                colorImageView.setImageResource(R.drawable.ic_color_aero_blue)
                dialog .dismiss()
            }
            hummingBirdLinearLayout.setOnClickListener {
                colorTextView.text = "Humming Bird"
                colorImageView.setImageResource(R.drawable.ic_color_humming_bird)
                dialog .dismiss()
            }
            sailLinearLayout.setOnClickListener {
                colorTextView.text = "Sail"
                colorImageView.setImageResource(R.drawable.ic_color_sail)
                dialog .dismiss()
            }
            mauveLinearLayout.setOnClickListener {
                colorTextView.text = "Mauve"
                colorImageView.setImageResource(R.drawable.ic_color_mauve)
                dialog .dismiss()
            }
            lotusPinkLinearLayout.setOnClickListener {
                colorTextView.text = "Lotus Pink"
                colorImageView.setImageResource(R.drawable.ic_color_lotus_pink)
                dialog .dismiss()
            }
            cashmereLinearLayout.setOnClickListener {
                colorTextView.text = "Cashmere"
                colorImageView.setImageResource(R.drawable.ic_color_cashmere)
                dialog .dismiss()
            }
            athensGrayLinearLayout.setOnClickListener {
                colorTextView.text = "Athens Gray"
                colorImageView.setImageResource(R.drawable.ic_color_athens_gray)
                dialog .dismiss()
            }
            dialog .show()
        }

        eventColorImageView.setOnClickListener(pickColorListener)

        colorTextView.setOnClickListener(pickColorListener)

        saveButton.setOnClickListener(View.OnClickListener {
            var allSet : Boolean = true
            if(titleEditText.text.isEmpty()){
                allSet = false
                titleEditText.setError("Title is required")
            }
            if(descriptionEditText.text.isEmpty()){
                descriptionEditText.setText("No description")
            }
            if(allSet == true) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED)
                {
                    if (position == 999999) {
                        val db = DatabaseQueries(this)
                        val result = db.insertEvent(
                            titleEditText.text.toString(),
                            startDayTextView.text.toString(),
                            startTimeTextView.text.toString(),
                            endDayTextView.text.toString(),
                            endTimeTextView.text.toString(),
                            firstReminderTextView.text.toString(),
                            secondReminderTextView.text.toString(),
                            colorTextView.text.toString(),
                            descriptionEditText.text.toString(),
                            backdroundImageText
                        )
                        if (result >= 0) {
                            saveInCalendar()
                            finish()
                        } else {
                            Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else {
                        val db = DatabaseQueries(this)
                        val oldEventItem = EventContent.ITEMS[position]
                        val newEventItem = EventItem(
                            titleEditText.text.toString(),
                            startDayTextView.text.toString(),
                            startTimeTextView.text.toString(),
                            endDayTextView.text.toString(),
                            endTimeTextView.text.toString(),
                            firstReminderTextView.text.toString(),
                            secondReminderTextView.text.toString(),
                            colorTextView.text.toString(),
                            descriptionEditText.text.toString(),
                            backdroundImageText
                        )
                        val result = db.updateEvent(oldEventItem, newEventItem)
                        if (result >= 0) {
                            val CALENDAR_URI = Uri.parse(getCalendarUriBase(this) + "events")
                            val cursors = contentResolver.query(CALENDAR_URI, null, null, null, null)
                            if (cursors!!.moveToFirst()) {
                                while (cursors.moveToNext()) {
                                    val desc = cursors.getString(cursors.getColumnIndex("description"))
                                    val title = cursors.getString(cursors.getColumnIndex("title"))
                                    val id = cursors.getString(cursors.getColumnIndex("_id"))
                                    if (desc == null && title == null) {
                                    }
                                    else {
                                        if (desc == oldEventItem.description && title == oldEventItem.title) {
                                            val uri = ContentUris.withAppendedId(
                                                CALENDAR_URI,
                                                Integer.parseInt(id).toLong()
                                            )
                                            contentResolver.delete(uri, null, null)
                                            break
                                        }
                                    }
                                }
                            }
                            cursors.close()
                            saveInCalendar()
                            finish()
                        } else {
                            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else {
                    Toast.makeText(this, "Write Permissions Required!", Toast.LENGTH_SHORT).show()
                }
            }
        })

        editImageView.setOnClickListener(View.OnClickListener {

            val dialog = Dialog(this)
            dialog.setContentView(R.layout.pick_background_image_dialog)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val backgroundImageLayout0 = dialog.findViewById(R.id.backgroundImageLayout0) as ConstraintLayout
            val backgroundImageLayout1 = dialog.findViewById(R.id.backgroundImageLayout1) as ConstraintLayout
            val backgroundImageLayout2 = dialog.findViewById(R.id.backgroundImageLayout2) as ConstraintLayout
            val backgroundImageLayout3 = dialog.findViewById(R.id.backgroundImageLayout3) as ConstraintLayout
            val backgroundImageLayout4 = dialog.findViewById(R.id.backgroundImageLayout4) as ConstraintLayout
            val backgroundImageLayout5 = dialog.findViewById(R.id.backgroundImageLayout5) as ConstraintLayout
            val backgroundImageLayout6 = dialog.findViewById(R.id.backgroundImageLayout6) as ConstraintLayout
            val backgroundImageLayout7 = dialog.findViewById(R.id.backgroundImageLayout7) as ConstraintLayout
            val backgroundImageLayout8 = dialog.findViewById(R.id.backgroundImageLayout8) as ConstraintLayout
            val backgroundImageLayout9 = dialog.findViewById(R.id.backgroundImageLayout9) as ConstraintLayout
            val backgroundImageLayout10 = dialog.findViewById(R.id.backgroundImageLayout10) as ConstraintLayout
            val backgroundImageLayout11 = dialog.findViewById(R.id.backgroundImageLayout11) as ConstraintLayout

            backgroundImageLayout0.setOnClickListener {
                backgroundImageView.setImageResource(R.drawable.card_view_background_0)
                backdroundImageText = "Background Image 1"
                dialog .dismiss()
            }

            backgroundImageLayout1.setOnClickListener {
                backgroundImageView.setImageResource(R.drawable.card_view_background_1)
                backdroundImageText = "Background Image 2"
                dialog .dismiss()
            }

            backgroundImageLayout2.setOnClickListener {
                backgroundImageView.setImageResource(R.drawable.card_view_background_2)
                backdroundImageText = "Background Image 3"
                dialog .dismiss()
            }

            backgroundImageLayout3.setOnClickListener {
                backgroundImageView.setImageResource(R.drawable.card_view_background_3)
                backdroundImageText = "Background Image 4"
                dialog .dismiss()
            }

            backgroundImageLayout4.setOnClickListener {
                backgroundImageView.setImageResource(R.drawable.card_view_background_4)
                backdroundImageText = "Background Image 5"
                dialog .dismiss()
            }


            backgroundImageLayout5.setOnClickListener {
                backgroundImageView.setImageResource(R.drawable.card_view_background_5)
                backdroundImageText = "Background Image 6"
                dialog .dismiss()
            }

            backgroundImageLayout6.setOnClickListener {
                backgroundImageView.setImageResource(R.drawable.card_view_background_6)
                backdroundImageText = "Background Image 7"
                dialog .dismiss()
            }

            backgroundImageLayout7.setOnClickListener {
                backgroundImageView.setImageResource(R.drawable.card_view_background_7)
                backdroundImageText = "Background Image 8"
                dialog .dismiss()
            }

            backgroundImageLayout8.setOnClickListener {
                backgroundImageView.setImageResource(R.drawable.card_view_background_8)
                backdroundImageText = "Background Image 9"
                dialog .dismiss()
            }

            backgroundImageLayout9.setOnClickListener {
                backgroundImageView.setImageResource(R.drawable.card_view_background_9)
                backdroundImageText = "Background Image 10"
                dialog .dismiss()
            }

            backgroundImageLayout10.setOnClickListener {
                backgroundImageView.setImageResource(R.drawable.card_view_background_10)
                backdroundImageText = "Background Image 11"
                dialog .dismiss()
            }

            backgroundImageLayout11.setOnClickListener {
                backgroundImageView.setImageResource(R.drawable.card_view_background_11)
                backdroundImageText = "Background Image 12"
                dialog .dismiss()
            }
            dialog .show()

        })

        deleteImageView.setOnClickListener(View.OnClickListener {
            if(position == 999999){
                Toast.makeText(this,"Event Discarded",Toast.LENGTH_SHORT).show()
                finish()
            } else{
                val db = DatabaseQueries(this)
                db.deleteEvent(EventContent.ITEMS[position])
//                Toast.makeText(this,"Event Deleted Successfully",Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        backPressImageView.setOnClickListener(View.OnClickListener {
            Toast.makeText(this,"Event Discarded",Toast.LENGTH_SHORT).show()
            finish()
        })

    }

    private fun checkPermissionRequirements() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this, android.Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    this, android.Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, android.Manifest.permission.WRITE_CALENDAR) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        this, android.Manifest.permission.READ_CALENDAR) )
                {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Read & Write permissions are required to do the task.")
                    builder.setTitle("Please grant those permissions")
                    builder.setPositiveButton("OK") { dialogInterface, i ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                android.Manifest.permission.WRITE_CALENDAR,
                                android.Manifest.permission.READ_CALENDAR
                            ),
                            100)
                    }
                    builder.setNeutralButton("Cancel", null)
                    val dialog = builder.create()
                    dialog.show()
                }
                else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.WRITE_CALENDAR , android.Manifest.permission.READ_CALENDAR),
                        100)
                }
            }
        }

    }

    private fun saveInCalendar() {
        val EVENTS_URI = Uri.parse(getCalendarUriBase(this) + "events")
        val cr = contentResolver
        var values = ContentValues()
        values.put("calendar_id", 1)
        values.put("title", titleEditText.text.toString())
        values.put("allDay", 0)
        startMilliseconds = getTimeInMilliseconds(startDayTextView.text.toString()+" "+startTimeTextView.text.toString())
        endMilliseconds = getTimeInMilliseconds(endDayTextView.text.toString()+" "+endTimeTextView.text.toString())
        values.put("dtstart", startMilliseconds)
        values.put("dtend", endMilliseconds)
        values.put("description", descriptionEditText.text.toString())
        values.put("hasAlarm", 1)
        values.put("eventTimezone", TimeZone.getDefault().getID())
        val event = cr.insert(EVENTS_URI, values)

        val REMINDERS_URI = Uri.parse(getCalendarUriBase(this) + "reminders")
        values = ContentValues()
        values.put("event_id", java.lang.Long.parseLong(event!!.lastPathSegment!!))
        values.put("method", 1)
        values.put("minutes", 0)
        cr.insert(REMINDERS_URI, values)
    }

    private fun getTimeInMilliseconds(format: String): Long {
        val sdf = SimpleDateFormat("EEE, MMM dd,yyyy hh:mm a",Locale.getDefault())
        val date = sdf.parse(format)
        val millis = date.getTime()
        return millis
    }

    private fun getCalendarUriBase(act: Activity): String? {

        var calendarUriBase: String? = null
        var calendars = Uri.parse("content://calendar/calendars")
        var managedCursor: Cursor? = null
        try {
            managedCursor = act.managedQuery(calendars, null, null, null, null)
        } catch (e: Exception) {
        }

        if (managedCursor != null) {
            calendarUriBase = "content://calendar/"
        } else {
            calendars = Uri.parse("content://com.android.calendar/calendars")
            try {
                managedCursor = act.managedQuery(calendars, null, null, null, null)
            } catch (e: Exception) {
            }

            if (managedCursor != null) {
                calendarUriBase = "content://com.android.calendar/"
            }
        }
        return calendarUriBase
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this,"Event Discarded",Toast.LENGTH_SHORT).show()
        finish()
    }

    fun getBackgroundImageRes(backgroundImage : String): Int{
        return when(backgroundImage){
            "Background Image 1" -> R.drawable.card_view_background_0
            "Background Image 2" -> R.drawable.card_view_background_1
            "Background Image 3" -> R.drawable.card_view_background_2
            "Background Image 4" -> R.drawable.card_view_background_3
            "Background Image 5" -> R.drawable.card_view_background_4
            "Background Image 6" -> R.drawable.card_view_background_5
            "Background Image 7" -> R.drawable.card_view_background_6
            "Background Image 8" -> R.drawable.card_view_background_7
            "Background Image 9" -> R.drawable.card_view_background_8
            "Background Image 10" -> R.drawable.card_view_background_9
            "Background Image 11" -> R.drawable.card_view_background_10
            "Background Image 12" -> R.drawable.card_view_background_11
            else -> R.drawable.card_view_background_0
        }
    }

    fun getColorRes(color : String) : Int {
        return when(color){
            "Default Color" -> R.drawable.ic_color_white
            "Froly" -> R.drawable.ic_color_froly
            "Banana" -> R.drawable.ic_color_banana
            "Paris Daisy" -> R.drawable.ic_color_paris_daisy
            "Reef" -> R.drawable.ic_color_reef
            "Aero Blue" -> R.drawable.ic_color_aero_blue
            "Humming Bird" -> R.drawable.ic_color_humming_bird
            "Sail" -> R.drawable.ic_color_sail
            "Mauve" -> R.drawable.ic_color_mauve
            "Lotus Pink" -> R.drawable.ic_color_lotus_pink
            "Cashmere" -> R.drawable.ic_color_cashmere
            "Athens Gray" -> R.drawable.ic_color_athens_gray
            else -> R.drawable.ic_color_white
        }
    }

    fun getMonthInWords(month : Int ) : String {
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

    fun getWeekDay( year : Int, month : Int, day : Int) : String {
        val calendar = Calendar.getInstance()
        calendar.set(year,month,day)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==100) {
            if(grantResults[0]!=PackageManager.PERMISSION_GRANTED) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Write permissions are required to do the task.")
                builder.setTitle("Please grant those permissions")
                builder.setPositiveButton("OK") { dialogInterface, i ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            android.Manifest.permission.WRITE_CALENDAR
                        ),
                        100)
                }
                builder.setNeutralButton("Cancel", null)
                val dialog = builder.create()
                dialog.show()
            }
        }

    }


}
