package com.example.standardbenutzer.splineify

import android.graphics.PointF
import android.os.AsyncTask
import org.ejml.simple.SimpleMatrix

/**
 * Created by Standardbenutzer on 22.02.2018.
 */
class AsyncMatrixSolver : AsyncTask<Any,SimpleMatrix,SimpleMatrix> {
    constructor() : super()

    private var listener : OnMatrixSolved ? = null

    override fun doInBackground(vararg p0: Any?): SimpleMatrix {
        var points = p0[0] as MutableList<PointF>
        listener = p0[1] as OnMatrixSolved
        return solveMatrix(points)
    }

    override fun onPostExecute(result: SimpleMatrix) {
        super.onPostExecute(result)
        listener!!.onMatrixSolved(result)
    }

    private fun solveMatrix(points : MutableList<PointF>) : SimpleMatrix {
        if(points.size < 3)
            return SimpleMatrix(0,0)
        val a = SimpleMatrix(points.size,1)
        val h = SimpleMatrix(points.size-1,1)
        for(i in 0 until points.size)
            a[i,0] = points!![i].y.toDouble()
        for(i in 0 until points.size-1)
            h[i,0] = (points[i+1].x - points[i].x).toDouble()

        val A = SimpleMatrix(points.size-2,points.size-2)
        for(i in 0 until points.size-2){
            for(k in 0 until points.size-2){
                if(k == i-1 || i == k-1)
                    A[i,k] = h[k]
                if(k == i)
                    A[i,k] = 2*(h[k] + h[k+1])
            }
        }

        var r = SimpleMatrix(points.size-2,1)
        for(i in 0 until points.size-2){
            val left = (a[i+2] - a[i+1]) / h[i+1]
            val right = (a[i+1] - a[i]) / h[i]
            r[i,0] = 3*(left - right)
        }
        var c = SimpleMatrix(points.size,1)
        var solved = A.invert().mult(r)
        for(i in 0 until points.size){
            if(i == 0 || i == points.size -1)
                c[i,0] = 0.0
            else
                c[i,0] = solved[i-1]
        }
        var d = SimpleMatrix(points.size-1,1)
        var b = SimpleMatrix(points.size-1,1)

        for(i in 0 until points.size-1){
            d[i,0] = (c[i+1,0] - c[i,0]) / (3*h[i,0 ])
            b[i,0] = ((points[i+1].y - points[i].y) / (h[i,0])) - (h[i,0]*(c[i+1,0]+2*c[i,0])/3.0)
        }

        var res = SimpleMatrix(4,points.size-1)
        for(i in 0 until 4){
            var selectedMatrix = SimpleMatrix(0,0)
            when (i) {
                0 -> selectedMatrix = a
                1 -> selectedMatrix = b
                2 -> selectedMatrix = c
                3 -> selectedMatrix = d
            }
            for(k in 0 until points.size-1)
                res[i,k] = selectedMatrix[k,0]
        }

        return res
    }
}