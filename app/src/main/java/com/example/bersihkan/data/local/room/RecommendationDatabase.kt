package com.example.bersihkan.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bersihkan.R
import com.example.bersihkan.data.local.model.Recommendation
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
    entities = [Recommendation::class],
    exportSchema = false
)
abstract class RecommendationDatabase : RoomDatabase() {

    abstract fun recommendationDao(): RecommendationDao

    companion object {
        @Volatile
        private var INSTANCE: RecommendationDatabase? = null

        fun getInstance(context: Context): RecommendationDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    RecommendationDatabase::class.java,
                    "recommendation.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    fillWithStartingData(context, database.recommendationDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE as RecommendationDatabase
            }
        }

        private fun fillWithStartingData(context: Context, dao: RecommendationDao) {
            val task = loadJsonArray(context)
            try {
                if (task != null) {
                    for (i in 0 until task.length()) {
                        val item = task.getJSONObject(i)
                        CoroutineScope(Dispatchers.IO).launch {
                            dao.insert(
                                Recommendation(
                                    postalCode = item.getInt("postalCode"),
                                    r1 = item.getInt("r1"),
                                    r2 = item.getInt("r2"),
                                    r3 = item.getInt("r3"),
                                    r4 = item.getInt("r4"),
                                    r5 = item.getInt("r5"),
                                    r6 = item.getInt("r6"),
                                    r7 = item.getInt("r7"),
                                    r8 = item.getInt("r8"),
                                    r9 = item.getInt("r9"),
                                    r10 = item.getInt("r10"),
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
            val `in` = context.resources.openRawResource(R.raw.recommendation)
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