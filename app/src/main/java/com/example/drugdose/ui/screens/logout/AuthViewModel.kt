package com.example.drugdose.ui.screens.logout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drugdose.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel
    (val authRepo : AuthRepository) : ViewModel()
{
    fun logout(){
        viewModelScope.launch {
            authRepo.logout()
        }
    }

}