package com.witchpot.wiitchpots.data

import androidx.room.Entity

import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class Ingredient(
    @PrimaryKey val imageRes: Int,
    val quantity: Int,
    val weight: Int
)

@Entity(tableName = "potions")
data class Potion(
    @PrimaryKey     val name: String,
    val quantity: Int,
    val imageRes: Int,
    val score : Int


)

@Entity(tableName = "score")
data class Score(
    @PrimaryKey
    val score: Int
)
