package com.hend.weatherforecastchallenge.utils

import androidx.annotation.StringRes

/**
 * Base class to represent common UI states
 */
sealed class UIState

/**
 * the screen is currently loading it's data.
 */
object Loading : UIState()

/**
 * data was successfully loaded for the screen.
 */
object HasData : UIState()

/**
 * some type of error occurred.
 */
class Error(@StringRes val errorMsgId:Int = 0) : UIState()
