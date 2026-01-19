package com.iregados.deckarr.core.util.dto

data class Notification(
    val message: String,
    val type: NotificationType
)

sealed class NotificationType {
    object Success : NotificationType()
    object Error : NotificationType()
    object Info : NotificationType()
    object Warning : NotificationType()
}
