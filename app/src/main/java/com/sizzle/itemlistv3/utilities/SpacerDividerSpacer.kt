package com.sizzle.itemlistv3.utilities

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SpacerDividerSpacer(
    spacerHeight: Int = 15
) {
    Spacer(modifier = Modifier.height(spacerHeight.dp)); Divider(); Spacer(modifier = Modifier.height(spacerHeight.dp))
}