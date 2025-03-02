package org.example

import kotlinx.serialization.json.Json

/**
 * Getting crafts and its decomposition result.
 * */
class SubnauticaCraftsAssetsRepositoryImpl : SubnauticaCraftsAssetsRepository {
    override fun getSubnauticaCrafts(): List<SubnauticaCraft> =
        Json.decodeFromString(ResourceProvider.getJson(CRAFTS_JSON))


    override fun getSubnauticaCraftById(id: String): SubnauticaCraft? = getSubnauticaCrafts().find { it.id == id }

    /**
     * Decomposes crafts and counts number of resources
     * TODO Lacks counter of resources for desomposition
     * TODO "Resource is natural produced" checking required. Can return unexpected result for multiple crafts for "natural" resource.
     * */
    override fun decomposeCrafts(crafts: Map<String, Int>?): Map<String, Int> {

        val result = mutableMapOf<String, Int>()

        return result
    }
}
