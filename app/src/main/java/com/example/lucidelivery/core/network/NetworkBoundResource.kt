package com.example.lucidelivery.core.network

import com.example.lucidelivery.core.Resource
import com.example.lucidelivery.core.toUserMessage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*

/**
 * Single source of truth = Room.
 * 1. Read cache. 2. If stale/forced -> emit Loading(cache), fetch, save.
 * 3. Always end by streaming the DB (so UI updates reactively).
 * On failure: stream DB wrapped in Error(cache) so the UI still has data offline.
 */
fun <T, R> networkBoundResource(
    query: () -> Flow<T>,
    shouldFetch: suspend (cached: T) -> Boolean,
    fetch: suspend () -> R,
    saveFetchResult: suspend (R) -> Unit
): Flow<Resource<T>> = flow {
    val cached = query().first()
    if (shouldFetch(cached)) {
        emit(Resource.Loading(cached))
        try {
            saveFetchResult(fetch())
            emitAll(query().map { Resource.Success(it) })
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            emitAll(query().map { Resource.Error(e.toUserMessage(), it) })
        }
    } else {
        emitAll(query().map { Resource.Success(it) })
    }
}