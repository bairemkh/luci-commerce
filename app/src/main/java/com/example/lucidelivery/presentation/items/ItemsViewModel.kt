package com.example.lucidelivery.presentation.items


import androidx.lifecycle.viewModelScope
import com.example.lucidelivery.core.Resource
import com.example.lucidelivery.core.network.NetworkMonitor
import com.example.lucidelivery.domain.model.Item
import com.example.lucidelivery.domain.usecase.ObserveItemsUseCase
import com.example.lucidelivery.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class ItemsUiState(
    val items: List<Item> = emptyList(),
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val isOffline: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ItemsViewModel @Inject constructor(
    observeItems: ObserveItemsUseCase,
    networkMonitor: NetworkMonitor
) : BaseViewModel() {

    private val refreshTrigger = MutableStateFlow(false)   // true = force refresh

    val state: StateFlow<ItemsUiState> = combine(
        refreshTrigger.flatMapLatest { force -> observeItems(force) },
        networkMonitor.isOnline
    ) { res, online ->
        when (res) {
            is Resource.Loading -> ItemsUiState(res.cached.orEmpty(), true, null, !online)
            is Resource.Success -> ItemsUiState(res.data, false, null, !online)
            is Resource.Error -> ItemsUiState(res.cached.orEmpty(), false, res.message, !online)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ItemsUiState(isRefreshing = true))

    fun refresh() { refreshTrigger.value = false; forceReload() }

    private fun forceReload() {
        // toggle through a distinct emission so flatMapLatest re-runs with force = true
        refreshTrigger.value = false
        refreshTrigger.value = true
    }
}