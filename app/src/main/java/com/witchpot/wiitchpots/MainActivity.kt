package com.witchpot.wiitchpots

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import com.witchpot.wiitchpots.ui.theme.MySlotTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MySlotTheme {
                MainScreen(
                    onFarmClick = { navigateTo(FarmActivity::class.java) },
                    onWheelClick = { navigateTo(WheelActivity::class.java) },
                    onStartClick = { navigateTo(PlayActivity::class.java) }
                )
            }
        }
    }

}
fun Context.navigateTo(activityClass: Class<out ComponentActivity>) {
    startActivity(Intent(this, activityClass).apply {
        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    })
}

@Composable
fun MainScreen(onFarmClick: () -> Unit, onWheelClick: () -> Unit, onStartClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.back_ground),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(
                onClick = onFarmClick,
                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp),
//                shape = RoundedCornerShape(50)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_farm),
                    contentDescription = "Farm Icon",
                    modifier = Modifier.size(225.dp)
                )
//                Spacer(modifier = Modifier.width(8.dp))

            }

            TextButton(
                onClick = onStartClick,
                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp),
//                shape = RoundedCornerShape(50)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_start),
                    contentDescription = "Start Icon",
                    modifier = Modifier.size(150.dp)
                )
//                Spacer(modifier = Modifier.width(8.dp))
            }

            TextButton(
                onClick = onWheelClick,
                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp),
//                shape = RoundedCornerShape(50)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_wheel),
                    contentDescription = "Wheel Icon",
                    modifier = Modifier.size(225.dp)
                )
//                Spacer(modifier = Modifier.width(8.dp))
            }


        }
    }
}