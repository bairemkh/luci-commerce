package com.example.lucidelivery.presentation.base


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lucidelivery.core.toUserMessage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/** One-shot events (snackbar, navigation) separate from state. */
sealed interface UiEvent {
    data class ShowMessage(val text: String) : UiEvent
    data class Navigate(val route: String) : UiEvent
}

abstract class BaseViewModel : ViewModel() {
    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    protected fun sendEvent(e: UiEvent) { _events.trySend(e) }

    protected fun launchSafe(
        onError: (Throwable) -> Unit = { sendEvent(UiEvent.ShowMessage(it.toUserMessage())) },
        block: suspend () -> Unit
    ) = viewModelScope.launch {
        try { block() } catch (e: CancellationException) { throw e } catch (e: Throwable) { onError(e) }
    }
}