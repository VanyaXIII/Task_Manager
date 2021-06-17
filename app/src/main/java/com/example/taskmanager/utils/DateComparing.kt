package com.example.taskmanager.utils

import java.util.*

operator fun Calendar.minus(calendar: Calendar) : Calendar {
    return GregorianCalendar(this[Calendar.YEAR] - calendar[Calendar.YEAR],
        this[Calendar.MONTH] - calendar[Calendar.MONTH],
        this[Calendar.DAY_OF_MONTH] - calendar[Calendar.DAY_OF_MONTH],
        this[Calendar.HOUR_OF_DAY] - calendar[Calendar.HOUR_OF_DAY],
        this[Calendar.MINUTE] - calendar[Calendar.MINUTE],
        this[Calendar.SECOND] - calendar[Calendar.SECOND],
    )
}

operator fun Calendar.plus(calendar: Calendar) : Calendar {
    return GregorianCalendar(this[Calendar.YEAR] + calendar[Calendar.YEAR],
        this[Calendar.MONTH] + calendar[Calendar.MONTH],
        this[Calendar.DAY_OF_MONTH] + calendar[Calendar.DAY_OF_MONTH],
        this[Calendar.HOUR_OF_DAY] + calendar[Calendar.HOUR_OF_DAY],
        this[Calendar.MINUTE] + calendar[Calendar.MINUTE],
        this[Calendar.SECOND] + calendar[Calendar.SECOND],
    )
}