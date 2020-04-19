package com.dclfactor.course.kotlinjpaoauth2swagger.util

import java.util.*


class DateUtil {
    companion object {
        fun createExpirationDate(): Date = Calendar.getInstance()
                .apply { add(Calendar.MINUTE, 60 * 24) }
                .time
    }
}
