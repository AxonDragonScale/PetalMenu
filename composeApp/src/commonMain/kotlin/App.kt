import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import theme.DefaultPalette

@Composable
fun App() {
    MaterialTheme {
        val colors = DefaultPalette
        var selectedColor by remember { mutableStateOf(colors.first()) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            selectedColor.copy(alpha = 0.2F),
                            selectedColor.copy(alpha = 0.5F),
                            selectedColor.copy(alpha = 1F)
                        )
                    )
                ),
            contentAlignment = Alignment.Center,
        ) {
            PetalMenu(
                colors = colors,
                selectedColor = selectedColor,
                onSelectionChange = { selectedColor = it }
            )
        }
    }
}
