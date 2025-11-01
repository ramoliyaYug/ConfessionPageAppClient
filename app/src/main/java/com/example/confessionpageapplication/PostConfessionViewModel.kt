package com.example.confessionpageapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostConfessionViewModel : ViewModel() {

    private val _isPosting = MutableStateFlow(false)
    val isPosting: StateFlow<Boolean> = _isPosting

    private val _postSuccess = MutableStateFlow<Boolean?>(null)
    val postSuccess: StateFlow<Boolean?> = _postSuccess

    fun postConfession(confessionText: String) {
        if (confessionText.isBlank()) return

        viewModelScope.launch {
            _isPosting.emit(true)
            _postSuccess.emit(null)

            FirebaseUtil.postConfession(confessionText) { success ->
                viewModelScope.launch {
                    _isPosting.emit(false)
                    _postSuccess.emit(success)
                }
            }
        }
    }

    fun resetPostStatus() {
        viewModelScope.launch {
            _postSuccess.emit(null)
        }
    }
}