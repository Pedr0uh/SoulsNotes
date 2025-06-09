package com.example.soulsnotes

import android.media.MediaPlayer
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.media3.common.Player
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.ui.AspectRatioFrameLayout


@OptIn(UnstableApi::class)
@Composable
fun HomeScreen(NavController: NavHostController){

    val context = LocalContext.current //variavel que armazena o contexto da aplicação (obrigatorio para player)

    val mediaPlayer1 = remember {
        MediaPlayer.create(context,R.raw.menu_undertale)
    }

    //variavel para criação do player
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val videoUri = RawResourceDataSource.buildRawResourceUri(R.raw.home_video) //armazena o video
            setMediaItem(MediaItem.fromUri(videoUri)) //define o video que irá tocar
            prepare() //prepara o player
            playWhenReady = true //toca o video quando estiver preparado
            play()
            volume = 1f //volume 0 sem som, 1 com som
            repeatMode = Player.REPEAT_MODE_ALL //video em loop infinito
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val currentPlayer by rememberUpdatedState(newValue = exoPlayer)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> currentPlayer.pause()
                Lifecycle.Event.ON_RESUME -> currentPlayer.play()
                else -> { }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            currentPlayer.release()
        }

    }

    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = exoPlayer
                    useController = false
                    resizeMode =
                        AspectRatioFrameLayout.RESIZE_MODE_ZOOM //preenche a tela com o video cortando as bordas
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .offset(y = 0.dp)
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
    }
    Column(
        modifier = Modifier.fillMaxSize()
            .offset(y = (-100.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Text(
            text = "Escolha qual deseja o ouvir:",
            fontFamily = undertaleFont,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .size(width = 220.dp, height = 65.dp)
                .align(Alignment.CenterHorizontally)
        )

        Button(
            onClick = { NavController.navigate("undertaleSonds") },
            modifier = Modifier
                .padding(8.dp)
                .size(width = 220.dp, height = 65.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray
            ),
            shape = RoundedCornerShape(12.dp),
        ){
            Image(
                painter = painterResource(id = R.drawable.undertale_titulo),
                contentDescription = "Botão Undertale Sons"
            )
        }

        Spacer(modifier = Modifier.size(3.dp))

        Button(
            onClick = { },
            modifier = Modifier
                .padding(8.dp)
                .size(width = 220.dp, height = 65.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),
            shape = RoundedCornerShape(12.dp),
        ){
            Image(
                painter = painterResource(id = R.drawable.deltarune_titulo),
                contentDescription = "Botão Deltarune Sons"
            )
        }
    }
}