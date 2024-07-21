package com.witchpot.wiitchpots

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.witchpot.wiitchpots.R
import com.witchpot.wiitchpots.data.AppDatabase
import com.witchpot.wiitchpots.data.Ingredient
import com.witchpot.wiitchpots.data.IngredientDao
import com.witchpot.wiitchpots.data.ItemUtil
import com.witchpot.wiitchpots.data.Potion
import com.witchpot.wiitchpots.data.PotionDao
import com.witchpot.wiitchpots.data.SharedPreferencesManager
import com.witchpot.wiitchpots.ui.theme.MySlotTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class WheelActivity : ComponentActivity() {

    private suspend fun addIngredientToDatabase(ingredientResId: Int) {
        val newIngredient = ingredientDao.getIngredientByRes(ingredientResId)
        if (newIngredient != null) {
            ingredientDao.updateIngredient(newIngredient.copy(quantity = newIngredient.quantity + 1))
        }
    }



    private lateinit var db: AppDatabase
    private lateinit var ingredientDao: IngredientDao
    private lateinit var potionDao: PotionDao

    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = AppDatabase.getDatabase(this)
        ingredientDao = db.ingredientDao()
        potionDao = db.potionDao()
        sharedPreferencesManager = SharedPreferencesManager(this)

        setContent {
            MySlotTheme {
                WheelScreen(onMenuClick = { navigateTo(MainActivity::class.java) },sharedPreferencesManager)
            }
        }
    }

    @Composable
    fun WheelScreen(onMenuClick: () -> Unit,sharedPreferencesManager: SharedPreferencesManager) {
        var score by remember { mutableStateOf(sharedPreferencesManager.getScore()) }

         suspend fun addPotionToDatabase(iconRes: Int) {
            val newPotion = potionDao.getPotionByRes(iconRes)
            if (newPotion != null) {
                score+=newPotion.score
                sharedPreferencesManager.saveScore(score)

            }

        }



        val ingredients = remember { mutableStateOf<List<Ingredient>>(emptyList()) }
        val potionsData = remember { mutableStateOf<List<Potion>>(emptyList()) }

        LaunchedEffect(Unit) {
            ingredients.value = ingredientDao.getAllIngredients().orEmpty()
            potionsData.value = potionDao.getAllPotions()
        }

        var rotationValue by remember { mutableStateOf(0f) }
        var number by remember { mutableStateOf(0) }
        var flag by remember { mutableStateOf(false) }
        val angle: Float by animateFloatAsState(
            targetValue = rotationValue,
            animationSpec = tween(3000),
            finishedListener = {

                val index = (((360f - (it % 360f)) / (360f / 9)).roundToInt())%9
                number = ItemUtil.list[index]

                if (index != 2) {
                    flag = true
                    lifecycleScope.launch {
                        addIngredientToDatabase(ItemUtil.list[index])
                    }
                } else {
                    flag = true
                    lifecycleScope.launch {
                        addPotionToDatabase(ItemUtil.list[index])
                    }
                }
            })

        fun onWheelClick() {
            rotationValue = (1080..2160).random().toFloat() + angle
        }


        Image(
            painter = painterResource(id = R.drawable.back_ground),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween)
        {
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.wheel),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(angle)
                )
                Image(
                    painter = painterResource(id = R.drawable.vector),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
                if (flag) {
                    ShowImageWithEffect(number, onChangeFlag = { flag = false })

                }


            }
            TextButton(
                onClick = { onWheelClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(50)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.find),
                    contentDescription = "Wheel Icon",
                    modifier = Modifier.size(225.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }


        }
    }

    @Composable
    fun ShowImageWithEffect(number: Int, onChangeFlag: () -> Unit) {
        var visible by remember { mutableStateOf(true) }

        // LaunchedEffect для запуска задержки
        LaunchedEffect(Unit) {
            delay(2000) // Задержка в 2 секунды
            visible = false
            onChangeFlag()

        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(1000)), // Анимация появления
                exit = fadeOut(animationSpec = tween(1000)) // Анимация исчезновения
            ) {
                Image(
                    painter = painterResource(id = number),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp) // Задайте размер изображения
                        .graphicsLayer {
                            shadowElevation = 20.dp.toPx()
                            shape = androidx.compose.foundation.shape.CircleShape
                            clip = true
                            ambientShadowColor = Color(0xFFFFF174)
                            spotShadowColor = Color(0xFFFFF174)
                        }
                        .shadow(
                            elevation = 20.dp,
                            shape = androidx.compose.foundation.shape.CircleShape,
                            clip = true

                        )
                        .graphicsLayer {
                            shadowElevation = 20.dp.toPx()
                            shape = androidx.compose.foundation.shape.CircleShape
                            clip = true
                            ambientShadowColor = Color(0xFFFFF174)
                            spotShadowColor = Color(0xFFFFF174)
                        }
                        .graphicsLayer {
                            shadowElevation = 20.dp.toPx()
                            shape = androidx.compose.foundation.shape.CircleShape
                            clip = true
                            ambientShadowColor = Color(0xFFFFF174)
                            spotShadowColor = Color(0xFFFFF174)
                        }
                )
            }
        }
    }
}








