package com.example.confessionpageapplication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConfessionScreenViewModel : ViewModel() {

    private val _confessions = MutableStateFlow<List<Confession>>(emptyList())
    val confessions: StateFlow<List<Confession>> = _confessions

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var valueEventListener: ValueEventListener? = null

    init {
        fetchApprovedConfessions()
    }

    private fun fetchApprovedConfessions() {
        Log.d("ConfessionViewModel", "Starting to fetch approved confessions")

        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("ConfessionViewModel", "Data changed - Total confessions: ${snapshot.childrenCount}")

                val confessionList = mutableListOf<Confession>()

                for (child in snapshot.children) {
                    try {
                        val id = child.child("id").getValue(String::class.java) ?: ""
                        val confession = child.child("confession").getValue(String::class.java) ?: ""
                        val postedAt = child.child("postedAt").getValue(Long::class.java) ?: 0L
                        val isApproved = child.child("isApprovedByAdmin").getValue(Boolean::class.java) ?: false

                        Log.d("ConfessionViewModel", "Confession ID: $id, Approved: $isApproved")

                        if (isApproved) {
                            val confessionObj = Confession(id, confession, postedAt, isApproved)
                            confessionList.add(confessionObj)
                            Log.d("ConfessionViewModel", "Added approved confession: $id")
                        }
                    } catch (e: Exception) {
                        Log.e("ConfessionViewModel", "Error parsing confession", e)
                    }
                }

                Log.d("ConfessionViewModel", "Approved confessions count: ${confessionList.size}")

                viewModelScope.launch {
                    _confessions.emit(confessionList.sortedByDescending { it.postedAt })
                    _isLoading.emit(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ConfessionViewModel", "Database error: ${error.message}")
                viewModelScope.launch {
                    _isLoading.emit(false)
                }
            }
        }

        FirebaseUtil.getConfessionsRef().addValueEventListener(valueEventListener!!)
    }

    override fun onCleared() {
        super.onCleared()
        valueEventListener?.let {
            FirebaseUtil.getConfessionsRef().removeEventListener(it)
        }
    }
}