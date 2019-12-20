package com.o7services.spark.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager

import com.o7services.spark.R
import com.o7services.spark.prefrences.MySharedPrefrences

class DiaryLockFragment : Fragment() {


    lateinit var firstCircleImageView : ImageView
    lateinit var secondCircleImageView : ImageView
    lateinit var thirdCircleImageView : ImageView
    lateinit var fourthCircleImageView : ImageView
    lateinit var oneTextView : TextView
    lateinit var twoTextView : TextView
    lateinit var threeTextView : TextView
    lateinit var fourTextView : TextView
    lateinit var fiveTextView : TextView
    lateinit var sixTextView : TextView
    lateinit var sevenTextView : TextView
    lateinit var eightTextView : TextView
    lateinit var nineTextView : TextView
    lateinit var zeroTextView : TextView
    lateinit var backSpaceImageView: ImageView
    lateinit var incorrectTextView : TextView
    lateinit var fm : FragmentManager
    lateinit var drawerOpenerImageView : ImageView
    lateinit var drawer : DrawerLayout

    var password : String = ""
    var savedPassword : String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_diary_lock, container, false)


        fm = activity!!.supportFragmentManager
        firstCircleImageView = view.findViewById(R.id.dFirstCircleImageView)
        secondCircleImageView = view.findViewById(R.id.dSecondCircleImageView)
        thirdCircleImageView = view.findViewById(R.id.dThirdCircleImageView)
        fourthCircleImageView = view.findViewById(R.id.dFourthCircleImageView)
        oneTextView = view.findViewById(R.id.dOneTextView)
        twoTextView = view.findViewById(R.id.dTwoTextView)
        threeTextView = view.findViewById(R.id.dThreeTextView)
        fourTextView = view.findViewById(R.id.dFourTextView)
        fiveTextView = view.findViewById(R.id.dFiveTextView)
        sixTextView = view.findViewById(R.id.dSixTextView)
        sevenTextView = view.findViewById(R.id.dSevenTextView)
        eightTextView = view.findViewById(R.id.dEightTextView)
        nineTextView = view.findViewById(R.id.dNineTextView)
        zeroTextView = view.findViewById(R.id.dZeroTextView)
        backSpaceImageView = view.findViewById(R.id.dBackSpaceImageView)
        incorrectTextView = view.findViewById(R.id.dIncorrectTextView)
        drawerOpenerImageView = view.findViewById(R.id.dDrawerOpenerImageView)
        drawer = activity!!.findViewById(R.id.drawer_layout)

        var sp : MySharedPrefrences = MySharedPrefrences(activity!!.baseContext)
        savedPassword = sp.getDiaryPassword()

        if(savedPassword.equals("")){
            incorrectTextView.setText("Type your new password")
            incorrectTextView.visibility = View.VISIBLE
        } else{
            incorrectTextView.setText("Incorrect PIN. Try Again")
            incorrectTextView.visibility = View.GONE
        }

        drawerOpenerImageView.setOnClickListener(View.OnClickListener {
            drawer.openDrawer(GravityCompat.START)
        })

        var allClickListner : View.OnClickListener = View.OnClickListener {

            if(savedPassword.equals("")){
                incorrectTextView.setText("Type your new password")
                incorrectTextView.visibility = View.VISIBLE
            } else {
                incorrectTextView.visibility = View.GONE
            }


            when(it.id){
                R.id.dOneTextView -> password += "1"
                R.id.dTwoTextView -> password += "2"
                R.id.dThreeTextView -> password += "3"
                R.id.dFourTextView -> password += "4"
                R.id.dFiveTextView -> password += "5"
                R.id.dSixTextView -> password += "6"
                R.id.dSevenTextView -> password += "7"
                R.id.dEightTextView -> password += "8"
                R.id.dNineTextView -> password += "9"
                R.id.dZeroTextView -> password += "0"
                R.id.dBackSpaceImageView -> {
                    if(password.length != 0){
                        password = password.substring(0,password.length-1)
                    }
                }
            }

            when(password.length){
                0 -> {
                    firstCircleImageView.setImageResource(R.drawable.ic_circle_no_fill_white)
                    secondCircleImageView.setImageResource(R.drawable.ic_circle_no_fill_white)
                    thirdCircleImageView.setImageResource(R.drawable.ic_circle_no_fill_white)
                    fourthCircleImageView.setImageResource(R.drawable.ic_circle_no_fill_white)
                }
                1 -> {
                    firstCircleImageView.setImageResource(R.drawable.ic_circle_fill_white)
                    secondCircleImageView.setImageResource(R.drawable.ic_circle_no_fill_white)
                    thirdCircleImageView.setImageResource(R.drawable.ic_circle_no_fill_white)
                    fourthCircleImageView.setImageResource(R.drawable.ic_circle_no_fill_white)
                }
                2 -> {
                    firstCircleImageView.setImageResource(R.drawable.ic_circle_fill_white)
                    secondCircleImageView.setImageResource(R.drawable.ic_circle_fill_white)
                    thirdCircleImageView.setImageResource(R.drawable.ic_circle_no_fill_white)
                    fourthCircleImageView.setImageResource(R.drawable.ic_circle_no_fill_white)
                }
                3 -> {
                    firstCircleImageView.setImageResource(R.drawable.ic_circle_fill_white)
                    secondCircleImageView.setImageResource(R.drawable.ic_circle_fill_white)
                    thirdCircleImageView.setImageResource(R.drawable.ic_circle_fill_white)
                    fourthCircleImageView.setImageResource(R.drawable.ic_circle_no_fill_white)
                }
                4 -> {
                    firstCircleImageView.setImageResource(R.drawable.ic_circle_fill_white)
                    secondCircleImageView.setImageResource(R.drawable.ic_circle_fill_white)
                    thirdCircleImageView.setImageResource(R.drawable.ic_circle_fill_white)
                    fourthCircleImageView.setImageResource(R.drawable.ic_circle_fill_white)
                }
            }

            if(password.length == 4){
                if(savedPassword.equals("")){
                    sp.setDiaryPassword(password)
                    Toast.makeText(activity,"PIN set. Enter again to login", Toast.LENGTH_SHORT).show()
                    fm.beginTransaction().replace(R.id.main_layout,DiaryLockFragment()).commit()
//                    password = ""
//                    firstCircleImageView.setImageResource(R.drawable.ic_circle_no_fill_white)
//                    secondCircleImageView.setImageResource(R.drawable.ic_circle_no_fill_white)
//                    thirdCircleImageView.setImageResource(R.drawable.ic_circle_no_fill_white)
//                    fourthCircleImageView.setImageResource(R.drawable.ic_circle_no_fill_white)
//                    incorrectTextView.setText("Incorrect PIN. Try Again")
//                    incorrectTextView.visibility = View.GONE
                } else {
                    if(password.equals(savedPassword)){
                        fm.beginTransaction().replace(R.id.main_layout,DiaryFragment()).commit()
                    } else {
                        incorrectTextView.setText("Incorrect PIN. Try Again")
                        incorrectTextView.visibility = View.VISIBLE
                        password = ""
                        firstCircleImageView.setImageResource(R.drawable.ic_circle_no_fill_white)
                        secondCircleImageView.setImageResource(R.drawable.ic_circle_no_fill_white)
                        thirdCircleImageView.setImageResource(R.drawable.ic_circle_no_fill_white)
                        fourthCircleImageView.setImageResource(R.drawable.ic_circle_no_fill_white)
                    }
                }
            }
        }

        oneTextView.setOnClickListener(allClickListner)
        twoTextView.setOnClickListener(allClickListner)
        threeTextView.setOnClickListener(allClickListner)
        fourTextView.setOnClickListener(allClickListner)
        fiveTextView.setOnClickListener(allClickListner)
        sixTextView.setOnClickListener(allClickListner)
        sevenTextView.setOnClickListener(allClickListner)
        eightTextView.setOnClickListener(allClickListner)
        nineTextView.setOnClickListener(allClickListner)
        zeroTextView.setOnClickListener(allClickListner)
        backSpaceImageView.setOnClickListener(allClickListner)

        return view
    }


}
