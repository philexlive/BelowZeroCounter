package org.philexliveprojects.treedecomposer

import org.philexliveprojects.treedecomposer.data.SubnauticaCraftsAssetsRepository
import org.junit.jupiter.api.Test


class SubnauticaCraftDecomposerTest {

    @Test
    fun getNaturalResourcesForCraft() {
        val crafts = mapOf(
            "power_cell" to 3,
            "compass" to 1,
            "seaglide" to 10
        )

        val repository = SubnauticaCraftsAssetsRepository()

        val decomposer = SubnauticaCraftDecomposer(repository)

        println(decomposer.onResult(crafts))
    }
}

