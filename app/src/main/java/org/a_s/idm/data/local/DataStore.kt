package org.a_s.idm.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.a_s.idm.utlis.Constants.IDM_DATASTORE

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = IDM_DATASTORE)
