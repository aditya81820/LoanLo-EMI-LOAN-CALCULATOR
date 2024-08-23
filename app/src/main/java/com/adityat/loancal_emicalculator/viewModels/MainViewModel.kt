package com.adityat.loancal_emicalculator.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.adityat.loancal_emicalculator.Screen

class MainViewModel : ViewModel() {
    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.DrawerScreens.Dashboard)
    val currentScreen : MutableState<Screen>
        get() = _currentScreen
    fun set(screen: Screen){
        _currentScreen.value = screen
    }


}