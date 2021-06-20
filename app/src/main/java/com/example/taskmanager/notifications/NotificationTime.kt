
package com.example.taskmanager.notifications

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class NotificationTime(
    @JsonProperty("timeToNotify") var notificationTime: Calendar, @JsonProperty("isNotified") var notified: Boolean
) {

    override fun toString(): String {
        return "${notificationTime[Calendar.DAY_OF_MONTH]} ${notificationTime[Calendar.HOUR_OF_DAY]}:${notificationTime[Calendar.MINUTE]}"
    }

}