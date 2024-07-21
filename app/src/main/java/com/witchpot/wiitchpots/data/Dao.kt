package com.witchpot.wiitchpots.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredients")
    suspend fun getAllIngredients(): List<Ingredient>

    @Query("SELECT * FROM ingredients ")
    fun getIngredientFlow(): Flow<List<Ingredient>>

    @Query("SELECT * FROM ingredients WHERE imageRes = :imageRes")
    suspend fun getIngredientByRes(imageRes: Int): Ingredient?

    @Query("SELECT quantity FROM ingredients WHERE imageRes = :imageRes")
    suspend fun getIngradientCount(imageRes: Int): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: Ingredient)

    @Update
    suspend fun updateIngredient(ingredient: Ingredient)

    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient)
}

@Dao
interface PotionDao {
    @Query("SELECT * FROM potions")
    suspend fun getAllPotions(): List<Potion>

    @Query("SELECT * FROM potions WHERE name = :imageRes")
    suspend fun getPotionByRes(imageRes: Int): Potion?

    @Query("SELECT * FROM potions WHERE name = :name")
    suspend fun getPotionByName(name: String): Potion?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPotion(potion: Potion)

    @Update
    suspend fun updatePotion(potion: Potion)

    @Delete
    suspend fun deletePotion(potion: Potion)
}

@Dao
interface ScoreDao {
    @Insert
    suspend fun insertScore(score: Score)

    @Query("SELECT * FROM score")
    suspend fun getAllScores(): List<Score>

    @Update
    suspend fun updateScore(score: Score)

    @Query("SELECT score FROM score")
    suspend fun getScore(): Score
}