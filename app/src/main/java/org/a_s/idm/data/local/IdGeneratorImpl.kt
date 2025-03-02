package org.a_s.idm.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.a_s.idm.domain.local.IdGenerator
import org.a_s.idm.utlis.Constants.ENTRY_ID

class IdGeneratorImpl(
    private val context: Context,
) : IdGenerator {
    override suspend fun getId(): Int = getCurrentId()

    private suspend fun getCurrentId(): Int {
        return context.dataStore.data
            .map { preference ->
                preference[key] ?: 0
            }.first().also { id ->
                withContext(Dispatchers.IO) {
                    incrementId(id)
                }
            }
    }

    private suspend fun incrementId(currentId: Int) {
        context.dataStore.edit {
            it[key] = currentId + 1
        }
    }

    companion object {
        private val key = intPreferencesKey(ENTRY_ID)
    }
}
