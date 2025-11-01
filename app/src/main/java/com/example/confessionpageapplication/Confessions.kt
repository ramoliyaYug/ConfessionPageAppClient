package com.example.confessionpageapplication

import java.util.UUID

data class Confession(
    val id: String = UUID.randomUUID().toString(),
    val confession: String = "",
    val postedAt: Long = System.currentTimeMillis(),
    val isApprovedByAdmin: Boolean = false
) {
    constructor() : this("", "", 0L, false)
}