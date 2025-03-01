package org.example

import kotlinx.serialization.Serializable

/**
 * Represents craft in subnautica
 * */
@Serializable
data class SubnauticaCraft(
    val id: String,
    val category: String,
    val materials: Map<String, Int>,
    val quantity: Int
)