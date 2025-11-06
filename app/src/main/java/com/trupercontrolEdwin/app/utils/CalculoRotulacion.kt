package com.trupercontrolEdwin.app.utils

object CalculoRotulacion {

    fun calcular(m2: Double, tarifaTipo: String, figuras: Int): Triple<Double, Double, Double> {
        val tarifa = when (tarifaTipo) {
            "1-100" -> 117.0
            "101-300" -> 131.0
            else -> 147.0
        }
        val subtotalM2 = m2 * tarifa
        val subtotalFiguras = figuras * 325.0
        val subtotal = subtotalM2 + subtotalFiguras
        val iva = subtotal * 0.16
        val total = subtotal + iva
        return Triple(subtotal, iva, total)
    }
}
