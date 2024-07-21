package com.witchpot.wiitchpots


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.witchpot.wiitchpots.R
import com.witchpot.wiitchpots.data.AppDatabase
import com.witchpot.wiitchpots.data.Ingredient
import com.witchpot.wiitchpots.data.IngredientDao
import com.witchpot.wiitchpots.data.Potion
import com.witchpot.wiitchpots.data.PotionDao
import com.witchpot.wiitchpots.data.Potions
import com.witchpot.wiitchpots.data.ScoreDao
import com.witchpot.wiitchpots.data.SharedPreferencesManager
import com.witchpot.wiitchpots.data.SlotItem
import com.witchpot.wiitchpots.ui.theme.MySlotTheme
import com.witchpot.wiitchpots.ui.theme.ProgressBarColorRed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PlayActivity : ComponentActivity() {

    private lateinit var db: AppDatabase
    private lateinit var ingredientDao: IngredientDao
    private lateinit var potionDao: PotionDao
    private lateinit var scoreDao: ScoreDao
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = AppDatabase.getDatabase(this)
        ingredientDao = db.ingredientDao()
        potionDao = db.potionDao()
        scoreDao = db.scoreDao()
        sharedPreferencesManager = SharedPreferencesManager(this)


        setContent {
            MySlotTheme {
                PlayScreen(
                    onMenuClick = { navigateTo(MainActivity::class.java) }, sharedPreferencesManager
                )
            }
        }
    }

    @Composable
    fun PlayScreen(onMenuClick: () -> Unit, sharedPreferencesManager: SharedPreferencesManager) {
        var score by remember { mutableStateOf(sharedPreferencesManager.getScore()) }
        val scope = rememberCoroutineScope()
        var selectedPotion by remember { mutableStateOf<Int?>(null) }
        var selectedPotionIng1 by remember { mutableStateOf<Int?>(null) }
        var selectedPotionIng2 by remember { mutableStateOf<Int?>(null) }
        var selectedPotionIng3 by remember { mutableStateOf<Int?>(null) }
        var selectedPotionScore by remember { mutableStateOf<Int?>(null) }
        val ingredientsData = remember { mutableStateOf<List<Ingredient>>(emptyList()) }
        val potionsData = remember { mutableStateOf<List<Potion>>(emptyList()) }
        val potions = Potions.entries
        val ingradients = SlotItem.entries



        LaunchedEffect(Unit) {
            ingredientsData.value = ingredientDao.getAllIngredients().orEmpty()
            potionsData.value = potionDao.getAllPotions()

        }

//        suspend fun checkIngredients(requiredIngredients: List<SlotItem>)= withContext(Dispatchers.IO) {
//            requiredIngredients.
//        }


        suspend fun checkIngredients(requiredIngredients: List<SlotItem>) =
            withContext(Dispatchers.IO) {
                requiredIngredients.all {
                    ingredientDao.getIngredientByRes(it.drawableId)!!.quantity >= 1
                }
            }


        fun showInsufficientIngredientsMessage() {
            Toast.makeText(this, "Недостаточно ингредиентов", Toast.LENGTH_SHORT).show()
        }

        fun showSeccessMessage() {
            Toast.makeText(this, "Зелье успешно создано", Toast.LENGTH_SHORT).show()
        }


        fun createPotion(imageRes: Int) {
            val potion = Potions.getByImageRes(imageRes)
            val requiredIngredients = Potions.requiredIngredients(potion)
            lifecycleScope.launch {
                if (checkIngredients(requiredIngredients)) {
                    requiredIngredients.forEach {
                        val ingredient =
                            ingredientDao.getIngredientByRes(it.drawableId)
                        if (ingredient != null) {
                            ingredientDao.updateIngredient(ingredient.copy(quantity = ingredient.quantity - 1))
                        }
                    }
                    val potionEntry = potionDao.getPotionByName(potion.name)
                    if (potionEntry != null) {
                        score += potionEntry.score
                        sharedPreferencesManager.saveScore(score)
                    }

                    ingredientsData.value = ingredientDao.getAllIngredients().orEmpty()
                    potionsData.value = potionDao.getAllPotions()
                    showSeccessMessage()
                } else {
                    showInsufficientIngredientsMessage()
                }


            }
        }



        Image(
            painter = painterResource(id = R.drawable.back_ground),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.fillMaxSize(),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top menu button
            TextButton(
                onClick = onMenuClick,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                ingradients.forEachIndexed { index, ingredientResId ->
                    Image(
                        painter = painterResource(id = ingradients.get(index).drawableId),
                        contentDescription = "Ingredient ${index + 1}",
                        modifier = Modifier
                            .size(50.dp)

                    )


                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {


                ingradients.forEachIndexed { index, ingredientResId ->


                    OutlinedText(
                        text = ingredientsData.value.getOrNull(index)?.quantity?.toString() ?: "0",
                        outlineColor = ProgressBarColorRed,
                        fillColor = Color.White,
                        fontSize = 20.sp
                    )
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                potions.forEachIndexed { index, potionResId ->
                    Image(
                        painter = painterResource(id = potions.get(index).iconRes),
                        contentDescription = "Potion ${index + 1}",
                        modifier = Modifier
                            .size(60.dp)
                            .clickable {
                                selectedPotion = potions.get(index).iconRes
                                selectedPotionIng1 = potions.get(index).ingredients[0].drawableId
                                selectedPotionIng2 = potions.get(index).ingredients[1].drawableId
                                selectedPotionIng3 = potions.get(index).ingredients[2].drawableId
                                selectedPotionScore = potions.get(index).score
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))


            if (selectedPotion != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Image(
                        painter = painterResource(id = selectedPotionIng1!!),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                    )
                    OutlinedText(
                        text = "+",
                        outlineColor = ProgressBarColorRed,
                        fillColor = Color.White,
                        fontSize = 45.sp
                    )

                    Image(
                        painter = painterResource(id = selectedPotionIng2!!),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                    )
                    OutlinedText(
                        text = "+",
                        outlineColor = ProgressBarColorRed,
                        fillColor = Color.White,
                        fontSize = 45.sp
                    )

                    Image(
                        painter = painterResource(id = selectedPotionIng3!!),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                    )
                    OutlinedText(
                        text = "=   ",
                        outlineColor = ProgressBarColorRed,
                        fillColor = Color.White,
                        fontSize = 45.sp
                    )

                    Image(
                        painter = painterResource(id = selectedPotion!!),
                        contentDescription = "Selected Potion",
                        modifier = Modifier
                            .size(50.dp)
                                            )
                    OutlinedText(
                        text = selectedPotionScore.toString(),
                        outlineColor = ProgressBarColorRed,
                        fillColor = Color.White,
                        fontSize = 20.sp
                    )

                }
                TextButton(
                    onClick = {
                        scope.launch {
                            createPotion(selectedPotion!!)

                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cauldron),
                        contentDescription = "Cauldron",
                        modifier = Modifier.size(250.dp)
                    )
                }
            }
        }
    }
}

