package ru.skillbranch.skillarticles.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop

/*
 * Created by yasina on 2020-02-01
*/

fun View.setMarginOptionally(left:Int = marginLeft, top : Int = marginTop,
                             right : Int = marginRight, bottom : Int = marginBottom) {
    (layoutParams as ViewGroup.MarginLayoutParams).setMargins(left, top, right,bottom)
}