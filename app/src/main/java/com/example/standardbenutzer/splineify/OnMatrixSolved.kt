package com.example.standardbenutzer.splineify

import org.ejml.simple.SimpleMatrix

/**
 * Created by Standardbenutzer on 22.02.2018.
 */
interface OnMatrixSolved {
    fun onMatrixSolved(functions : SimpleMatrix)
}