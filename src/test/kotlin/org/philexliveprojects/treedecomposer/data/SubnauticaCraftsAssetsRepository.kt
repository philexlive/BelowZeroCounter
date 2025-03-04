package org.philexliveprojects.treedecomposer.data

import kotlinx.serialization.json.Json

class SubnauticaCraftsAssetsRepository {

    fun getSubnauticaCraftById(id: String): SubnauticaCraft? = getSubnauticaCrafts().find { it.id == id }


    private fun getSubnauticaCrafts(): List<SubnauticaCraft> =
        Json.decodeFromString(javaClass.classLoader.getResource(CRAFTS_JSON)?.readText().orEmpty())


    companion object {
        private const val CRAFTS_JSON = "crafts.json"
    }
}
