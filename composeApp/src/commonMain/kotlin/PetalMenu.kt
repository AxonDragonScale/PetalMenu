import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInOutElastic
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

@Composable
fun PetalMenu(
    modifier: Modifier = Modifier,
    colors: List<Color>,
    selectedColor: Color,
    onSelectionChange: (Color) -> Unit,
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
            ) {
                isMenuOpen = false
            },
    )

    val menuSize by animateFloatAsState(
        targetValue = if (isMenuOpen) 0.9F else 0.4F,
        animationSpec = tween(),
//        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    )
    Box(
        modifier = Modifier
            .fillMaxSize(menuSize)
            .aspectRatio(1F)
            .clip(CircleShape)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center,
    ) {

        val degreeOffset = 360F / colors.size
        colors.forEachIndexed { index, color ->
            val rotationDegree by animateFloatAsState(
                targetValue = if (isMenuOpen) degreeOffset * index else 0F,
                animationSpec = tween(),
//                animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
            )
            var capsuleSize = IntSize(0, 0)
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.25F)
                    .fillMaxWidth(if (isMenuOpen) 0.4F else 0.4F)
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
                    .clip(RoundedCornerShape(50))
                    .background(
                        Brush.horizontalGradient(
                            listOf(color.copy(alpha = 0.6F), color, color)
                        )
                    )
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        if (isMenuOpen) onSelectionChange(color)
                    }
            )
        }

        if (!isMenuOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize(0.8F)
                    .clip(CircleShape)
                    .background(color = selectedColor)
                    .clickable {
                        isMenuOpen = true
                    }
            )
        }
    }
}
