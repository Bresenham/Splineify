package com.example.standardbenutzer.splineify

import android.graphics.PointF
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.ejml.simple.SimpleMatrix


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        drawView.setUpdatedPointsListener(object : DrawView.UpdatedPointsListener{
            override fun onPointsUpdated(points: MutableList<PointF>?) {
                val asyncCalc = AsyncMatrixSolver()
                asyncCalc.execute(points, object : OnMatrixSolved {
                    override fun onMatrixSolved(result : SimpleMatrix){
                        drawView.setMatrix(result)
                    }
                })
            }
        })
    }
}
