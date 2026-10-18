package com.example.lucidelivery.presentation.items


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lucidelivery.core.toPriceString
import com.example.lucidelivery.domain.model.Item

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(vm: ItemsViewModel = hiltViewModel()) {
    val state by vm.state.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state.error) { state.error?.let { snackbar.showSnackbar(it) } }

    Scaffold(snackbarHost = { SnackbarHost(snackbar) }) { padding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = vm::refresh,
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            Column {
                if (state.isOffline) {
                    Text("Offline mode", modifier = Modifier.fillMaxWidth().padding(8.dp))
                }
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.items, key = { it.id }) { ItemRow(it) }
                }
            }
        }
    }
}

@Composable
private fun ItemRow(item: Item) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(item.name, style = MaterialTheme.typography.titleMedium)
            item.description?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
            Spacer(Modifier.height(4.dp))
            Text(item.priceCents.toPriceString(item.currency), style = MaterialTheme.typography.labelLarge)
        }
    }
}