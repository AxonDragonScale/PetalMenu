package util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

/**
 * Created by Ronak Harkhani on 11/12/23
 */

fun Modifier.conditionalModifier(
    condition: Boolean,
    ifTrue: Modifier.() -> Modifier,
    ifFalse: Modifier.() -> Modifier,
) =
    if (condition) ifTrue() else ifFalse()

fun Modifier.ifTrue(condition: Boolean, modifier: Modifier.() -> Modifier) =
    if (condition) modifier() else this

fun Modifier.ifFalse(condition: Boolean, modifier: Modifier.() -> Modifier) =
    if (condition) this else modifier()

fun Modifier.noIndicationClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
) = clickable(
    interactionSource = MutableInteractionSource(),
    indication = null,
    enabled = enabled,
    onClickLabel = onClickLabel,
    role = role,
    onClick = onClick
)
