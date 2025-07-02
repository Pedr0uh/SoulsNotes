package com.example.soulsnotes

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@Composable
fun DeltaruneSondsScreen(NavController: NavHostController) {

    data class MusicItem(val name: String, val uri: android.net.Uri)

    val context =
        LocalContext.current //variavel que armazena o contexto da aplicação (obrigatorio para player)

    val musicList = listOf(
        MusicItem(
            "TobyFox - My Castle Town (Orchestral cover)",
            RawResourceDataSource.buildRawResourceUri(R.raw.my_castle_town)
        ),
        MusicItem(
            "TobyFox - A Cyber World?",
            RawResourceDataSource.buildRawResourceUri(R.raw.a_cyber_world)
        ),
        MusicItem(
            "Laura Shigihara - Don´t Forget (Extended)",
            RawResourceDataSource.buildRawResourceUri(R.raw.laura_shigihara_dont_forget_extended)
        )
    )

    var currentTrackIndex by remember { mutableStateOf(0) }

    var progress by remember { mutableStateOf(0f) }

    val exoPlayerMusic = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(musicList[currentTrackIndex].uri))
            prepare()
            playWhenReady = false
        }
    }

    //variavel para criação do player
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val videoUri =
                RawResourceDataSource.buildRawResourceUri(R.raw.cyber_world_vide) //armazena o video
            setMediaItem(MediaItem.fromUri(videoUri)) //define o video que irá tocar
            prepare() //prepara o player
            playWhenReady = true //toca o video quando estiver preparado
            play()
            volume = 0f //volume 0 sem som, 1 com som
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
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            currentPlayer.release()
            exoPlayerMusic.release()
        }

    }

    LaunchedEffect(currentTrackIndex) {
        exoPlayerMusic.setMediaItem(MediaItem.fromUri(musicList[currentTrackIndex].uri))
        exoPlayerMusic.prepare()
        exoPlayerMusic.play()
    }

    LaunchedEffect(Unit) {
        delay(500)
        while (true) {
            progress = if (exoPlayerMusic.duration > 0)
                exoPlayerMusic.currentPosition.toFloat() / exoPlayerMusic.duration.toFloat()
            else 0f
            delay(500)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = exoPlayer
                    useController = false
                    resizeMode =
                        AspectRatioFrameLayout.RESIZE_MODE_ZOOM //preenche a tela com o video cortando as bordas
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
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

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = musicList[currentTrackIndex].name,
                color = Color.White,
                modifier = Modifier.padding(16.dp),
                fontFamily = undertaleFont,
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )

            Slider(
                value = progress,
                onValueChange = {
                    val newPos = (exoPlayerMusic.duration * it).toLong()
                    exoPlayerMusic.seekTo(newPos)
                    progress = it
                },
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .height(40.dp),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFB0F2C2),
                    activeTrackColor = Color(0xFFB0F2C2),
                    inactiveTrackColor = Color.Black
                ),

                )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)

            ) {
                IconButton(
                    onClick = {
                        if (currentTrackIndex > 0) {
                            currentTrackIndex--
                        }
                    },
                    modifier = Modifier.background(Color.Transparent), // Fundo transparente
                    colors = IconButtonDefaults.iconButtonColors(  // Remover efeito padrão
                        containerColor = Color.Transparent,
                        contentColor = Color.Unspecified
                    )
                ) {
                    Image(
                        painter = painterResource(R.drawable.skip_to_anterior),
                        contentDescription = "anterior",
                        modifier = Modifier.size(60.dp)
                    )
                }

                IconButton(
                    onClick = {
                        if (exoPlayerMusic.isPlaying) exoPlayerMusic.pause() else exoPlayerMusic.play()
                    },
                    modifier = Modifier.background(Color.Transparent), // Fundo transparente
                    colors = IconButtonDefaults.iconButtonColors(  // Remover efeito padrão
                        containerColor = Color.Transparent,
                        contentColor = Color.Unspecified
                    )
                ) {
                    val iconRes =
                        if (exoPlayerMusic.isPlaying) R.drawable.pause else R.drawable.reproduzir
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = "Play/Pause",
                        modifier = Modifier.size(74.dp)
                    )
                }

                IconButton(
                    onClick = {
                        if (currentTrackIndex < musicList.size - 1) {
                            currentTrackIndex++
                        }
                    },
                    modifier = Modifier.background(Color.Transparent), // Fundo transparente
                    colors = IconButtonDefaults.iconButtonColors(  // Remover efeito padrão
                        containerColor = Color.Transparent,
                        contentColor = Color.Unspecified
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.skip_to_start),
                        contentDescription = "proxima",
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }
    }
}