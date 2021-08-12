package cn.idu.iwan.ext

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cn.idu.anno.TraceTime
import kotlinx.coroutines.flow.first


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

public suspend fun Context.save(key: String, value: String) {
    val dataStoreKey = stringPreferencesKey(key)
    dataStore.edit { settings ->
        settings[dataStoreKey] = value
    }
}

public suspend fun Context.read(key: String): String? {
    val dataStoreKey = stringPreferencesKey(key)
    val preferences = dataStore.data.first()
    return preferences[dataStoreKey]
}