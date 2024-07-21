package com.witchpot.wiitchpots.data

import com.witchpot.wiitchpots.R

enum class SlotItem (val drawableId : Int,  val weight: Int = 0,val index:Int) {


    Pepper(R.drawable.pepper, 1, 0),
    Corn(R.drawable.corn, 2, 1),
    Tomato(R.drawable.tomato,   3, 2),
    Avocado(R.drawable.avocado, 4,3),
    Scarlet(R.drawable.scarlet, 5,4),
    Cactus(R.drawable.cactus,   6,5),
    Flower(R.drawable.flower, 7,6);

    companion object {
        fun getRandomItem(): SlotItem {
            return entries.toTypedArray().random()
        }



        fun getByImageRes(imageRes: Int): SlotItem {
            return SlotItem.entries.find {
                it.drawableId == imageRes
            }!!
        }
        fun getByImageIndex(index: Int): SlotItem {
            return SlotItem.entries.find {
                it.index == index
            }!!
        }

    }

}