package com.example.soulsnotes

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.annotation.OptIn
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import kotlinx.coroutines.delay

val undertaleFont = FontFamily(
    Font(R.font.dtm_mono, FontWeight.Normal)
)

@OptIn(UnstableApi::class)
@Composable
fun InicialScreen(navController: NavController) {

    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(R.raw.queen_dance))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
            play()
            volume = 1f
            repeatMode = Player.REPEAT_MODE_ALL
        }
    }

    var animationStarted by remember { mutableStateOf(false) }

    val titleOffset by animateDpAsState(
        targetValue = if(animationStarted) 0.dp else (LocalConfiguration.current.screenHeightDp.dp / 2 - 24.dp),
        animationSpec = tween(durationMillis = 1000),
        label = "titleOffSet"
    )

    var buttonVisible by remember { mutableStateOf(false) }

    val buttomOffsetY by animateDpAsState(
        targetValue = if(buttonVisible) 250.dp else 800.dp,
        animationSpec = tween(durationMillis = 1000),
        label = "buttonOffsetY"
    )

    LaunchedEffect(Unit) {
        delay(10)
        animationStarted = true
        buttonVisible = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = exoPlayer
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .offset(y = titleOffset)
                .background(Color.Black)
        ) {
            Image(
                painter = painterResource(R.drawable.titulo),
                contentDescription = "titulo SoulsNotes",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(250.dp)
            )
        }

        if (animationStarted) {
            Button(
                onClick = { /**/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                border = BorderStroke(2.dp, Color.White),
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = buttomOffsetY)
                    .padding(bottom = 32.dp)
                    .height(64.dp)
                    .fillMaxWidth(0.6f)
            ) {
                Text(
                    text = "Começar",
                    color = Color.White,
                    fontFamily = undertaleFont,
                    fontSize = 30.sp
                )

            }
        }
    }

}