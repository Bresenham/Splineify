package com.example.standardbenutzer.splineify

import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.GestureDetector
import org.ejml.simple.SimpleMatrix


/**
 * Created by Standardbenutzer on 16.02.2018.
 */
class DrawView : View {

    private val paint = Paint()
    private var updatedPointsListener : UpdatedPointsListener? = null
    private var matrix : SimpleMatrix? = null
    private var points : MutableList<PointF>? = null
    private var preventDoubleTabs = System.currentTimeMillis()
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attr: AttributeSet) : super(context,attr) {
        init()
    }

    override fun onDraw(canvas : Canvas?){
        paint.color = ResourcesCompat.getColor(resources,R.color.colorLine,null)
        paint.strokeWidth = 2.5f
        if(matrix != null && matrix!!.numElements > 0) {
            for (i in 0 until points!!.size - 1) {
                var current = points!![i].x.toInt()
                val next = points!![i + 1].x.toInt()
                for (k in current..next - 1) {
                    val v = k
                    val j = k + 1
                    val y = (((matrix!![3, i] * v + matrix!![2, i]) * v) + matrix!![1, i]) * v + matrix!![0, i]
                    val y2 = (((matrix!![3, i] * j + matrix!![2, i]) * j) + matrix!![1, i]) * j + matrix!![0, i]
                    canvas?.drawLine(k.toFloat(), y.toFloat(), (k + 1).toFloat(), y2.toFloat(), paint)
                }
            }
        }
        points?.forEach { it ->
            paint.color = ResourcesCompat.getColor(resources, R.color.colorAccent, null)
            canvas?.drawCircle(it.x,it.y,12.0f, paint)
            paint.color = ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null)
            canvas?.drawCircle(it.x,it.y,7.0f, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(gd.onTouchEvent(event)){
            points!!.clear()
            this.invalidate()
            return false
        }
        else if(Math.abs(System.currentTimeMillis() - preventDoubleTabs) < 100) {
            preventDoubleTabs = System.currentTimeMillis()
            return false
        }

        val x = event?.x!!
        val y = event.y

        val point = PointF(x,y)

        points?.add(point)

        updatedPointsListener?.onPointsUpdated(points)
        preventDoubleTabs = System.currentTimeMillis()
        points!!.sortBy { it.x }
        return true
    }

    val gd = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            super.onLongPress(e)
        }

        override fun onDoubleTapEvent(e: MotionEvent): Boolean {
            return false
        }

        override fun onDown(e: MotionEvent): Boolean {
            return false
        }
    })

    interface UpdatedPointsListener{
        fun onPointsUpdated(points : MutableList<PointF>?)
    }

    fun setUpdatedPointsListener(listener : UpdatedPointsListener){
        this.updatedPointsListener = listener
    }

    fun setMatrix(matrix : SimpleMatrix){
        this.matrix = matrix
        this.invalidate()
    }

    private fun init(){
        points = mutableListOf()
        preventDoubleTabs = System.currentTimeMillis()
        paint.isAntiAlias = true
    }
}