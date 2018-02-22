package com.example.standardbenutzer.splineify

import android.graphics.Matrix
import android.graphics.PointF
import android.os.AsyncTask

/**
 * Created by Standardbenutzer on 22.02.2018.
 */
class AsyncMatrixSolver : AsyncTask<Any,Any,Any> {
    constructor() : super()

    override fun doInBackground(vararg p0: Any?): Any {
        var points = p0[0] as MutableList<PointF>
        var matrixSolvedListener = p0[1] as OnMatrixSolved
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
    }

    private fun solveMatrix(points : MutableList<PointF>) : Matrix {
        val a = FloatArray(points.size)
        val h = FloatArray(points.size-1)
        for(i in 0 until points.size)
            a[i] = points!![i].y
        for(i in 0 until points.size-1)
            h[i] = points[i+1].x - points[i].x

        val AValues = arrayOf(FloatArray(points.size-2),FloatArray(points.size-2))

        for(i in 0 until points.size-2){
            for(k in 0 until points.size-2){
                if(k == i-1 || i == k-1)
                    AValues[i][k] = h[k]
                if(k == i)
                    AValues[i][k] = 2*(h[k] + h[k+1])
            }
        }

        var r = FloatArray(points.size-2)
        for(i in 0 until points.size-2){
            val left = (a[i+2] - a[i+1]) / h[i+1]
            val right = (a[i+1] - a[i]) / h[i]
            r[i] = 3*(left - right)
        }
        var c = FloatArray(points.size)
        for(i in 0 until points.size){
            if(i == 0 || i == points.size -1)
                c[i] = 0.0f
            else
                //Solve A\r
        }
        var d = FloatArray(points.size-1)
        var b = FloatArray(points.size-1)
        for(i in 0 until points.size-1){
            d[i] = (c[i+1] - c[i]) / (3*h[i])
            b[i] = ((a[i+1] - a[i]) / (h[i])) - (1/3)*h[i]*(c[i+1]+2*c[i])
        }
    }
}