package org.example

interface SubnauticaCraftsAssetsRepository {
    fun getSubnauticaCrafts(): List<SubnauticaCraft>

    fun getSubnauticaCraftById(id: String): SubnauticaCraft?

    fun decomposeCrafts(crafts: Map<String, Int>?): Map<String, Int>
}
