package com.example.lucidelivery.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.lucidelivery.domain.model.AccountType
import com.example.lucidelivery.domain.model.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(private val store: DataStore<Preferences>) {
    private object K {
        val TOKEN = stringPreferencesKey("token")
        val TYPE = stringPreferencesKey("account_type")
        val ID = stringPreferencesKey("account_id")
    }

    val token: Flow<String?> = store.data.map { it[K.TOKEN] }

    val session: Flow<Session?> = store.data.map { p ->
        val t = p[K.TOKEN]; val type = p[K.TYPE]; val id = p[K.ID]
        if (t != null && type != null && id != null)
            Session(t, AccountType.valueOf(type), id) else null
    }

    suspend fun save(s: Session) = store.edit {
        it[K.TOKEN] = s.token; it[K.TYPE] = s.accountType.name; it[K.ID] = s.accountId
    }

    suspend fun clear() = store.edit { it.clear() }
}