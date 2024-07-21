package com.witchpot.wiitchpots.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [Ingredient::class, Potion::class,Score::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun ingredientDao(): IngredientDao
    abstract fun potionDao(): PotionDao
    abstract fun scoreDao(): ScoreDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "app_database.db"
            ).build().also {
                val scope = CoroutineScope(Dispatchers.IO)
                val Idao = it.ingredientDao()
                val Pdao = it.potionDao()
                scope.launch {
                    if (Idao.getAllIngredients().isEmpty()) {
                        SlotItem.entries.forEach {
                            Idao.insertIngredient(
                                Ingredient(
                                    imageRes = it.drawableId,
                                    quantity = 3,
                                    weight = it.weight
                                )
                            )
                        }
                    }
                    if (Pdao.getAllPotions().isEmpty()) {
                        Potions.entries.forEach {
                            Pdao.insertPotion(
                                Potion(
                                    imageRes = it.iconRes,
                                    name = it.name,
                                    quantity = 0,
                                    score = it.score
                                )
                            )
                        }
                    }
                }
            }
        }

    }

}


