package com.example.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.AppStrings
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.NaturalSearchPlaceholder
import com.example.ui.theme.NaturalTextDark
import com.example.ui.theme.NaturalWarmBorder
import com.example.ui.theme.NaturalWarmSurface

/**
 * Shared visual shell for every search input in the app (Home, History, E-Codes).
 * Behavior (submit vs. live filter) is left to the caller via [onSearchAction] / [trailingIcon] -
 * this only unifies shape, border, focus color, icon placement and text styling.
 */
@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    testTag: String? = null,
    onSearchAction: (() -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = testTag?.let { modifier.testTag(it) } ?: modifier,
        textStyle = TextStyle(
            color = NaturalTextDark,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        ),
        placeholder = {
            Text(
                text = placeholder,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = NaturalSearchPlaceholder,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = EmeraldPrimary,
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = trailingIcon,
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = NaturalTextDark,
            unfocusedTextColor = NaturalTextDark,
            focusedPlaceholderColor = NaturalSearchPlaceholder,
            unfocusedPlaceholderColor = NaturalSearchPlaceholder,
            focusedContainerColor = NaturalWarmSurface,
            unfocusedContainerColor = NaturalWarmSurface,
            focusedBorderColor = EmeraldPrimary,
            unfocusedBorderColor = NaturalWarmBorder,
            cursorColor = EmeraldPrimary
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = if (onSearchAction != null) ImeAction.Search else ImeAction.Default
        ),
        keyboardActions = KeyboardActions(onSearch = { onSearchAction?.invoke() })
    )
}

/** Standard clear (X) trailing icon shared by the live-filter search fields. */
@Composable
fun SearchFieldClearButton(
    visible: Boolean,
    onClear: () -> Unit,
    language: AppLanguage,
) {
    if (visible) {
        IconButton(onClick = onClear) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = AppStrings.getClear(language),
                tint = NaturalTextDark,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
