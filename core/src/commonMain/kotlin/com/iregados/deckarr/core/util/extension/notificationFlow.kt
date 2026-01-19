package com.iregados.deckarr.core.util.extension

import com.iregados.deckarr.core.util.dto.Notification
import com.iregados.deckarr.core.util.dto.NotificationType
import kotlinx.coroutines.flow.MutableSharedFlow

suspend fun MutableSharedFlow<Notification>.emitError(message: String) {
    emit(Notification(message, NotificationType.Error))
}

suspend fun MutableSharedFlow<Notification>.emitSuccess(message: String) {
    emit(Notification(message, NotificationType.Success))
}
