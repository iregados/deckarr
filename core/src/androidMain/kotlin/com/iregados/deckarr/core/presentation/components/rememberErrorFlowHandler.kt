package com.iregados.deckarr.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@Composable
fun rememberErrorFlowHandler(
    errorFlow: SharedFlow<String>,
    autoDismissDuration: Long = 3000L,
): MutableState<String?> {
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                errorFlow.collect { event ->
                    errorMessage.value = event
                }
            }
        }
    }

    LaunchedEffect(errorMessage.value) {
        if (errorMessage.value != null) {
            delay(autoDismissDuration)
            // Only auto-dismiss if the message hasn't been manually cleared
            if (errorMessage.value != null) {
                errorMessage.value = null
            }
        }
    }

    return errorMessage
}