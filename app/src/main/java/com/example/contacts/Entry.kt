package com.example.contacts

import java.util.*


data class Entry(
    var id: Int = autoGenerate(),
    var name: String = "",
    var lastname: String = "",
    var telephone: String = ""
) {
    companion object {
        fun autoGenerate(): Int {
            val random = Random()
            return random.nextInt(100)
        }
    }
}
