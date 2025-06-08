package com.example.soulsnotes

import android.media.MediaPlayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.estimateAnimationDurationMillis
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTarget
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(NavController: NavHostController){

    val context = LocalContext.current
    val mediaPlayer = remember {
        MediaPlayer.create(context,R.raw.snd_break1_c)
    }

    val mediaPlayer2 = remember {
        MediaPlayer.create(context,R.raw.snd_arrow)
    }

    //variavel para animação do coracao inteiro
    val offsetX = remember { Animatable(0f) }

    //variavel para animação do coracao na metade
    val splitOffset = remember { Animatable(0f) }

    //variavel que controla se aparecera o coração inteiro ou metade
    var showFullHeart by remember { mutableStateOf(true) }

    //variavel para controle da animação do logo
    val scaleLogo = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        //esperar 3 segundos antes da animação
        delay(3000)

        //programação da animação da tremida
        offsetX.animateTo(
            targetValue = 24f,
            animationSpec = repeatable(
                iterations = 6,
                animation = tween(durationMillis = 50),
                repeatMode = RepeatMode.Reverse
            )
        )

        //reseta o valor para a posição inicial
        offsetX.snapTo(0f)

        //esperar 300 milesec
        delay(2000)

        //animação da tremida novamente
        offsetX.animateTo(
            targetValue = 24f,
            animationSpec = repeatable(
                iterations = 6,
                animation = tween(durationMillis = 50),
                repeatMode = RepeatMode.Reverse
            )
        )

        //reseta o valor para a posição inicial
        offsetX.snapTo(0f)

        delay(1000)

        //troca para o coracao na metade
        showFullHeart = false

        mediaPlayer.start()

        //programação para para a animação das duas metades para fazer uma pequena rachadura
        splitOffset.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 50)
        )

        delay(1000)

        //programação para a animação das duas metades sairem da tela
        splitOffset.animateTo(
            targetValue = 200f,
            animationSpec = tween(durationMillis = 1000)
        )

        mediaPlayer2.start()

        //programação para a animação crescer de zero ate o tamanho normal
        scaleLogo.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500)
        )

        delay(2000)
        NavController.navigate("inicial") {
            popUpTo("splash") {inclusive = true}
        }

        mediaPlayer.release()
        mediaPlayer2.release()

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(R.drawable.titulo),
            contentDescription = "Titulo: SoulsNotes",
            modifier = Modifier
                .size(250.dp)
                //Usado para o controle de animação de zoom
                .graphicsLayer(
                    scaleX = scaleLogo.value,
                    scaleY = scaleLogo.value
                )
        )

        if(showFullHeart) {
            Image(
                painter = painterResource(R.drawable.coracaosplash),
                contentDescription = "Coração Delta",
                modifier = Modifier
                    .size(150.dp)
                    .offset { IntOffset(offsetX.value.toInt(), 0) }
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Image(
                    painter = painterResource(R.drawable.coracaosplashdireito),
                    contentDescription = "metade direita do coração",
                    modifier = Modifier
                        .size(150.dp)
                        .offset(x = (-splitOffset.value).dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Image(
                    painter = painterResource(R.drawable.coracaosplashesquerdo),
                    contentDescription = "metade esquerda do coração",
                    modifier = Modifier
                        .size(150.dp)
                        .offset(x = (splitOffset.value).dp)
                )
            }
        }
    }
}

@Composable
@Preview
fun SplashPreview(
    showSystemUi: Boolean = true,
    showBackground: Boolean = true
){
    SplashScreen(NavController = rememberNavController())
}