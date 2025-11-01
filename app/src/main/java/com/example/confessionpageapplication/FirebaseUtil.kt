package com.example.confessionpageapplication

import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

// Handles Firebase operations for the user app
object FirebaseUtil {

    private val database = FirebaseDatabase.getInstance()
    private val confessionsRef = database.getReference("confessions")

    fun getConfessionsRef() = confessionsRef

    fun postConfession(confessionText: String, onResult: (Boolean) -> Unit) {
        val confessionId = UUID.randomUUID().toString()

        val confessionMap = hashMapOf(
            "id" to confessionId,
            "confession" to confessionText,
            "postedAt" to System.currentTimeMillis(),
            "isApprovedByAdmin" to false
        )

        confessionsRef.child(confessionId)
            .setValue(confessionMap)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                onResult(false)
            }
    }
}