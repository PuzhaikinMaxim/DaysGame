package com.example.daysgame

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class DifficultyLevel: Parcelable {
    CHILD, EASY, NORMAL, HARD
}