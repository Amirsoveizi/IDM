package org.a_s.idm.utlis

import android.icu.text.DecimalFormat

fun Double.formatDecimal(maxFractionDigits: Int): String =
    DecimalFormat().apply {
        isGroupingUsed = false
        minimumFractionDigits = 0
        maximumFractionDigits = maxFractionDigits
        isDecimalSeparatorAlwaysShown = false
    }.format(this)
