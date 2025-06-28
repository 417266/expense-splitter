package com.example.splitter.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp

@Composable
fun SelectionField(
    value: String,
    label: String,
    hasError: Boolean,
    onClick: () -> Unit,
    fontStyle: FontStyle = FontStyle.Normal,
) {
    val defaultColors = TextFieldDefaults.colors()
    val textColor by
        animateColorAsState(
            if (hasError) defaultColors.errorTextColor else defaultColors.unfocusedTextColor
        )
    val indicatorColor by
        animateColorAsState(
            if (hasError) {
                defaultColors.errorIndicatorColor
            } else {
                defaultColors.unfocusedIndicatorColor
            }
        )
    val trailingIconColor by
        animateColorAsState(
            if (hasError) {
                defaultColors.errorTrailingIconColor
            } else {
                defaultColors.unfocusedTrailingIconColor
            }
        )
    val labelColor by
        animateColorAsState(
            if (hasError) defaultColors.errorLabelColor else defaultColors.unfocusedLabelColor
        )
    Box {
        TextField(
            value = value,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            textStyle = LocalTextStyle.current.copy(fontStyle = fontStyle),
            label = { Text(label) },
            trailingIcon = { Icon(Icons.AutoMirrored.Default.ArrowRight, null) },
            isError = hasError,
            colors =
                TextFieldDefaults.colors(
                    disabledTextColor = textColor,
                    disabledContainerColor = defaultColors.unfocusedContainerColor,
                    disabledIndicatorColor = indicatorColor,
                    disabledLeadingIconColor = defaultColors.unfocusedLeadingIconColor,
                    disabledTrailingIconColor = trailingIconColor,
                    disabledLabelColor = labelColor,
                    disabledPlaceholderColor = defaultColors.unfocusedPlaceholderColor,
                    disabledSupportingTextColor = defaultColors.unfocusedSupportingTextColor,
                    disabledPrefixColor = defaultColors.unfocusedPrefixColor,
                    disabledSuffixColor = defaultColors.unfocusedSuffixColor,
                ),
        )
        Box(
            Modifier.fillMaxWidth()
                .height(56.dp)
                .clip(TextFieldDefaults.shape)
                .clickable(onClick = onClick)
        )
    }
}
