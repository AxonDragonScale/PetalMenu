import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutBack
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import util.conditionalModifier
import util.ifTrue
import util.noIndicationClickable

@Composable
fun PetalMenu(
    modifier: Modifier = Modifier,
    colors: List<Color>,
    selectedColor: Color,
    onSelectionChange: (Color) -> Unit,
) {
    var isMenuOpen by remember { mutableStateOf(false) }
    val animationSpec = tween<Float>(
        durationMillis = 500,
        easing = EaseInOutBack
    )
    val menuSize by animateFloatAsState(
        targetValue = if (isMenuOpen) 0.9F else 0.4F,
        animationSpec = animationSpec,
    )

    var isPortrait by remember { mutableStateOf(true) }
    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged {
                isPortrait = it.height > it.width
            }
            .noIndicationClickable {
                isMenuOpen = false
            },
    )

    Box(
        modifier = Modifier
            .conditionalModifier(
                condition = isPortrait,
                ifTrue = { fillMaxWidth(menuSize) },
                ifFalse = { fillMaxHeight(menuSize) },
            )
            .aspectRatio(1F)
            .shadow(12.dp, CircleShape)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 1F)),
        // Bug in Compose: Using alpha = 0.7F here causes shadow artifacts
        contentAlignment = Alignment.Center,
    ) {

        val colorAlpha by animateFloatAsState(
            targetValue = if (isMenuOpen) 1F else 0.2F,
            animationSpec =
                if (isMenuOpen) tween(500, easing = LinearOutSlowInEasing)
                else tween(350, 150, easing = LinearEasing)
        )
        val degreeOffset = 360F / colors.size
        colors.forEachIndexed { index, color ->
            val rotationDegree by animateFloatAsState(
                targetValue = if (isMenuOpen) degreeOffset * index else 0F,
                animationSpec = animationSpec,
            )
            val width by animateFloatAsState(
                targetValue = if (isMenuOpen) 0.45F else 0.4F,
                animationSpec = animationSpec
            )
            var capsuleSize = IntSize(0, 0)
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.25F)
                    .fillMaxWidth(width)
                    .onSizeChanged {
                        capsuleSize = it
                    }
                    .absoluteOffset {
                        IntOffset(capsuleSize.width / 2, 0)
                    }
                    .graphicsLayer {
                        transformOrigin = TransformOrigin(0F, 0.5F)
                        rotationZ = rotationDegree
                    }
                    .ifTrue(isMenuOpen) {
                        // Issues -
                        // 1. Shadow peeks out on the right side on opening menu
                        // 2. On jvm/wasm, the transculent part of petal has border due to shadow
                        shadow(4.dp, RoundedCornerShape(50))
                    }
                    .clip(RoundedCornerShape(50))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                color.copy(alpha = 0.7F * colorAlpha),
                                color.copy(alpha = colorAlpha),
                                color.copy(alpha = colorAlpha),
                            )
                        )
                    )
                    .noIndicationClickable {
                        onSelectionChange(color)
                        isMenuOpen = false
                    }
            )
        }

        AnimatedVisibility(
            visible = !isMenuOpen,
            enter = fadeIn(tween(100, 250)),
            exit = fadeOut(tween(300)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(0.8F)
                    .clip(CircleShape)
                    .background(color = selectedColor.copy(alpha = 1F))
                    .noIndicationClickable {
                        isMenuOpen = true
                    }
            )
        }
    }
}
