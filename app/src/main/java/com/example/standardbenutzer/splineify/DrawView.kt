package com.example.standardbenutzer.splineify

import android.content.Context
import android.graphics.*
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by Standardbenutzer on 16.02.2018.
 */
class DrawView : View {

    private val paint = Paint()
    private var updatedPointsListener : UpdatedPointsListener? = null
    private var points : MutableList<PointF>? = null
    private var preventDoubleTabs = System.currentTimeMillis()
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attr: AttributeSet) : super(context,attr) {
        init()
    }

    override fun onDraw(canvas : Canvas?){
        points?.forEach { it ->
            paint.color = ResourcesCompat.getColor(resources, R.color.colorAccent, null)
            canvas?.drawCircle(it.x,it.y,12.0f, paint)
            paint.color = ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null)
            canvas?.drawCircle(it.x,it.y,7.0f, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(Math.abs(System.currentTimeMillis() - preventDoubleTabs) < 100) {
            preventDoubleTabs = System.currentTimeMillis()
            return false
        }
        val x = event?.x!!
        val y = event.y

        val point = PointF(x,y)

        points?.add(point)

        updatedPointsListener?.onPointsUpdated(points)
        this.invalidate()
        preventDoubleTabs = System.currentTimeMillis()
        return true
    }

    interface UpdatedPointsListener{
        fun onPointsUpdated(points : MutableList<PointF>?)
    }

    fun setUpdatedPointsListener(listener : UpdatedPointsListener){
        this.updatedPointsListener = listener
    }

    private fun init(){
        points = mutableListOf()
        preventDoubleTabs = System.currentTimeMillis()
    }
}