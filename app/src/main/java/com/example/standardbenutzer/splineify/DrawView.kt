package com.example.standardbenutzer.splineify

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by Standardbenutzer on 16.02.2018.
 */
class DrawView : View {

    private

    constructor(context: Context) : super(context) {

    }
    constructor(context: Context, attr: AttributeSet) : super(context,attr) {

    }

    override fun onDraw(canvas : Canvas?){

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return true
    }
}