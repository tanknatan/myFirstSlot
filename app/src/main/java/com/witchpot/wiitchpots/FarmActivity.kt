package com.witchpot.wiitchpots

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



import com.witchpot.wiitchpots.data.AppDatabase
import com.witchpot.wiitchpots.data.IngredientDao
import com.witchpot.wiitchpots.data.SharedPreferencesManager
import com.witchpot.wiitchpots.data.SlotItem
import com.witchpot.wiitchpots.ui.theme.MySlotTheme
import com.witchpot.wiitchpots.ui.theme.ProgressBarColorRed
import kotlinx.coroutines.delay


class FarmActivity : ComponentActivity() {


    private suspend fun checkSlots(slots: List<SlotItem>, onChangeFlag: () -> Unit) {
        for (i in 0 until slots.size - 2) {
            if (slots[i].drawableId == slots[i + 1].drawableId && slots[i].drawableId == slots[i + 2].drawableId) {
                onChangeFlag()
                addIngredientToDatabase(slots[i].drawableId)
                return
            }
        }
    }

    private suspend fun addIngredientToDatabase(ingredientResId: Int) {
        val newIngredient = ingredientDao.getIngredientByRes(ingredientResId)
        if (newIngredient != null) {
            ingredientDao.updateIngredient(newIngredient.copy(quantity = newIngredient.quantity + 1))
        }
    }

    private lateinit var db: AppDatabase
    private lateinit var ingredientDao: IngredientDao
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesManager = SharedPreferencesManager(this)
        db = AppDatabase.getDatabase(this)
        ingredientDao = db.ingredientDao()

        setContent {

            MySlotTheme {
                FarmScreen(
                    onMenuClick = { navigateTo(MainActivity::class.java) },
                    sharedPreferencesManager
                )
            }
        }
    }

    @Composable
    fun FarmScreen(onMenuClick: () -> Unit, sharedPreferencesManager: SharedPreferencesManager) {
        var score by remember { mutableStateOf(sharedPreferencesManager.getScore()) }
        var flag by remember { mutableStateOf(false) }
        val slots1 = remember { mutableStateOf(List(4) { SlotItem.getRandomItem() }) }
        val slots2 = remember { mutableStateOf(List(4) { SlotItem.getRandomItem() }) }
        val slots3 = remember { mutableStateOf(List(4) { SlotItem.getRandomItem() }) }
        val slots4 = remember { mutableStateOf(List(4) { SlotItem.getRandomItem() }) }
        val slots5 = remember { mutableStateOf(List(4) { SlotItem.getRandomItem() }) }
        var isSpin by remember {
            mutableStateOf(false)
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
            OutlinedText(
                text = score.toString(),
                outlineColor = ProgressBarColorRed,
                fillColor = Color.White,
                fontSize = 28.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(0.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.slotbackground),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .height(110.dp)
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),

                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    items(slots1.value) { slot ->
                        Image(
                            painter = painterResource(id = slot.drawableId),
                            modifier = Modifier.width(80.dp)
                                .height(140.dp),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )


                    }
                }
                if (flag) {
                    ShowImageWithEffect(
                        ingredientResId = slots1.value[3].drawableId,
                        onChangeFlag = {
                            flag = false
                        })
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.slotbackground),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp),

                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    items(slots2.value) { slot ->
                        Image(
                            painter = painterResource(id = slot.drawableId),
                            modifier = Modifier.width(80.dp)
                                .height(140.dp),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )


                    }
                }
                if (flag) {
                    ShowImageWithEffect(
                        ingredientResId = slots2.value[3].drawableId,
                        onChangeFlag = {
                            flag = false
                        })
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.slotbackground),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp),

                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    items(slots3.value) { slot ->
                        Image(
                            painter = painterResource(id = slot.drawableId),
                            modifier = Modifier.width(80.dp)
                                .height(140.dp),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )


                    }
                }
                if (flag) {
                    ShowImageWithEffect(
                        ingredientResId = slots3.value[3].drawableId,
                        onChangeFlag = {
                            flag = false
                        })
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.slotbackground),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize()

                        .padding(0.dp),

                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    items(slots4.value) { slot ->
                        Image(
                            painter = painterResource(id = slot.drawableId),
                            modifier = Modifier.width(80.dp)
                                .height(140.dp),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )


                    }
                }
                if (flag) {
                    ShowImageWithEffect(
                        ingredientResId = slots4.value[3].drawableId,
                        onChangeFlag = {
                            flag = false
                        })
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.slotbackground),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .height(110.dp)
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),

                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    items(slots5.value) { slot ->
                        Image(
                            painter = painterResource(id = slot.drawableId),
                            modifier = Modifier.width(80.dp)
                                .height(140.dp),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )


                    }
                }
                if (flag) {
                    ShowImageWithEffect(
                        ingredientResId = slots1.value[3].drawableId,
                        onChangeFlag = {
                            flag = false
                        })
                }
            }

            LaunchedEffect(isSpin) {

                if (!isSpin) return@LaunchedEffect

                delay(500) // Simulate spin delay
                slots1.value = List(4) { SlotItem.getRandomItem() }
                slots2.value = List(4) { SlotItem.getRandomItem() }
                slots3.value = List(4) { SlotItem.getRandomItem() }
                slots4.value = List(4) { SlotItem.getRandomItem() }
                slots5.value = List(4) { SlotItem.getRandomItem() }
                delay(500)
                slots1.value = List(4) { SlotItem.getRandomItem() }
                slots2.value = List(4) { SlotItem.getRandomItem() }
                slots3.value = List(4) { SlotItem.getRandomItem() }
                slots4.value = List(4) { SlotItem.getRandomItem() }
                slots5.value = List(4) { SlotItem.getRandomItem() }
                isSpin = false
                checkSlots(slots1.value, onChangeFlag = { flag = true })
                checkSlots(slots2.value, onChangeFlag = { flag = true })
                checkSlots(slots3.value, onChangeFlag = { flag = true })
                checkSlots(slots4.value, onChangeFlag = { flag = true })
                checkSlots(slots5.value, onChangeFlag = { flag = true })

            }

            TextButton(onClick = {
                if (score > 0) {
                    isSpin = true
                    score -= 1
                    sharedPreferencesManager.saveScore(score)
                    slots1.value = List(4) { SlotItem.getRandomItem() }
                    slots2.value = List(4) { SlotItem.getRandomItem() }
                    slots3.value = List(4) { SlotItem.getRandomItem() }
                    slots4.value = List(4) { SlotItem.getRandomItem() }
                    slots5.value = List(4) { SlotItem.getRandomItem() }

                } else {
                    Toast.makeText(this@FarmActivity, "Недостаточно очков", Toast.LENGTH_SHORT)
                        .show()
                }


            }) {
                Image(
                    painter = painterResource(id = R.drawable.slotbutton),
                    contentDescription = null,
                    modifier = Modifier
                        .size(225.dp)
                )

            }
        }
    }

    @Composable
    fun ShowImageWithEffect(ingredientResId: Int, onChangeFlag: () -> Unit) {
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
                    painter = painterResource(id = ingredientResId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp) // Задайте размер изображения
                        .graphicsLayer {
                            shadowElevation = 20.dp.toPx()
                            shape = CircleShape
                            clip = true
                            ambientShadowColor = Color(0xFFFFF174)
                            spotShadowColor = Color(0xFFFFF174)
                        }
                        .shadow(
                            elevation = 20.dp,
                            shape = CircleShape,
                            clip = true

                        )
                        .graphicsLayer {
                            shadowElevation = 20.dp.toPx()
                            shape = CircleShape
                            clip = true
                            ambientShadowColor = Color(0xFFFFF174)
                            spotShadowColor = Color(0xFFFFF174)
                        }
                        .graphicsLayer {
                            shadowElevation = 20.dp.toPx()
                            shape = CircleShape
                            clip = true
                            ambientShadowColor = Color(0xFFFFF174)
                            spotShadowColor = Color(0xFFFFF174)
                        }
                )
            }
        }
    }
//    @Composable
//    fun SlotBar(slotItems: List<String>, index: Int) {
//        var currentPosition by remember { mutableStateOf(0) }
//        val coroutineScope = rememberCoroutineScope()
//
//        val infiniteTransition = rememberInfiniteTransition()
//        val position by infiniteTransition.animateInt(
//            initialValue = 0,
//            targetValue = slotItems.size,
//            animationSpec = infiniteRepeatable(
//                animation = tween(durationMillis = 2000, easing = LinearEasing),
//                repeatMode = RepeatMode.Restart
//            )
//        )
//
//        LaunchedEffect(key1 = position) {
//            currentPosition = position
//        }
//
//        Box(
//            modifier = Modifier
//                .background(color = Color.LightGray)
//        ) {
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                repeat(slotItems.size) { i ->
//                    val itemIndex = (currentPosition + i) % slotItems.size
//                    Text(text = slotItems[itemIndex], fontSize = 32.sp)
//                }
//            }
//        }
//    }
}








