package com.example.bersihkan.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bersihkan.R
import com.example.bersihkan.data.local.model.PostalCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@Database(
    version = 1,
    entities = [PostalCode::class],
    exportSchema = false
)
abstract class PostalCodeDatabase : RoomDatabase() {

    abstract fun postalCodeDao(): PostalCodeDao

    companion object {
        @Volatile
        private var INSTANCE: PostalCodeDatabase? = null

        fun getInstance(context: Context): PostalCodeDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    PostalCodeDatabase::class.java,
                    "postalcode.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    fillWithStartingData(context, database.postalCodeDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE as PostalCodeDatabase
            }
        }

        private fun fillWithStartingData(context: Context, dao: PostalCodeDao) {
            val task = loadJsonArray(context)
            try {
                if (task != null) {
                    for (i in 0 until task.length()) {
                        val item = task.getJSONObject(i)
                        CoroutineScope(Dispatchers.IO).launch {
                            dao.insert(
                                PostalCode(
                                    postalCode = item.getInt("postal_code"),
                                    postalCodeIdx = item.getInt("postal_code_index"),
                                )
                            )
                        }
                    }
                }
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
        }

        private fun loadJsonArray(context: Context): JSONArray? {
            val builder = StringBuilder()
            val `in` = context.resources.openRawResource(R.raw.postal_code)
            val reader = BufferedReader(InputStreamReader(`in`))
            var line: String?
            try {
                while (reader.readLine().also { line = it } != null) {
                    builder.append(line)
                }
                val json = JSONObject(builder.toString())
                return json.getJSONArray("data")
            } catch (exception: IOException) {
                exception.printStackTrace()
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
            return null
        }
    }

}