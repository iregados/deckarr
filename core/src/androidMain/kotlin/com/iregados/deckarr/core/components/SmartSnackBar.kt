package com.iregados.deckarr.core.components

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.iregados.deckarr.core.util.dto.Notification
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@Composable
fun SmartSnackBar(
    vararg notificationFlows: SharedFlow<Notification>,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner, notificationFlows.toList()) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            notificationFlows.forEach { flow ->
                launch {
                    flow.collect { notification ->
                        snackbarHostState.showSnackbar(notification.message)
                    }
                }
            }
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier,
        snackbar = { snackbarData ->
            Snackbar(
                snackbarData = snackbarData,
            )
        }
    )
}