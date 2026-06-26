package com.example.drugdose.ui.screens.home

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector
//Classe temp, serve a gestire le icone importate come Resource o Vector
sealed class MenuIcon {
    data class Vector(val imageVector: ImageVector) : MenuIcon()
    data class Resource(@DrawableRes val resId: Int) : MenuIcon()
}