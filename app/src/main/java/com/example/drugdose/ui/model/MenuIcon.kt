package com.example.drugdose.ui.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MenuIcon {
    data class Vector(val imageVector: ImageVector) : MenuIcon()
    data class Resource(@DrawableRes val resId: Int) : MenuIcon()
}