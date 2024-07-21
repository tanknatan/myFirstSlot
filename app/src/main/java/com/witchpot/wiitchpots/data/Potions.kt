package com.witchpot.wiitchpots.data

import androidx.annotation.DrawableRes
import com.witchpot.wiitchpots.R


enum class Potions(
    @DrawableRes val iconRes: Int,
    val ingredients: List<SlotItem>,
    val score: Int
) {
    Yellow(
        R.drawable.yellow_potion,
        listOf(SlotItem.Corn, SlotItem.Pepper, SlotItem.Scarlet),
        8
    ),
    Green(
        R.drawable.green_potion,
        listOf(SlotItem.Cactus, SlotItem.Avocado, SlotItem.Scarlet),
        15
    ),
    Red(
        R.drawable.red_potion,
        listOf(SlotItem.Tomato, SlotItem.Pepper, SlotItem.Scarlet),
        9
    ),
    Blue(
        R.drawable.blue_potion,
        listOf(SlotItem.Flower, SlotItem.Avocado, SlotItem.Cactus),
        17
    ),
    Purple(
        R.drawable.pink_potion,
        listOf(SlotItem.Avocado, SlotItem.Tomato, SlotItem.Corn),
        9
    ),
    Orange(
        R.drawable.light_green_potion,
        listOf(SlotItem.Pepper, SlotItem.Scarlet, SlotItem.Flower),
        13
    );

    companion object {

        fun getRandomItem(): Potions {
            return Potions.entries.toTypedArray().random()
        }

        fun requiredIngredients(potion: Potions): List<SlotItem> {
            return potion.ingredients
        }

        fun getByImageRes(imageRes: Int): Potions {
            return entries.find {
                it.iconRes == imageRes
            }!!
        }


//        fun getByIngredients(ingredients: List<SlotItem>): Potions {
//            require (ingredients.size < 3) { "Must have 3 ingredients" }
//            return entries.first { it.ingredients == ingredients }
//        }
    }
}