package com.egoriku.familybox.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Card(
        @Id var id: Long = 0,
        var barcodeValue: String? = null,
        var barcodeFormat: String? = null
)