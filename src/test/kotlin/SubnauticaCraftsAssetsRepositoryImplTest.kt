import org.example.SubnauticaCraftsAssetsRepositoryImpl
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class SubnauticaCraftsAssetsRepositoryImplTest {

    @Test
    fun getNaturalResourcesForCraft() {
        val crafts = mapOf(
            "seatruck_depth_upgrade_mk3" to 1,
            "titanium" to 1
        )
        val expected = "{ruby=1, quartz=2, lead=1, diamond=1, metal_salvage=1, lithium=2, spiral_plant_clipping=1, nickel_ore=3, kyanite=2}"

        val repository = SubnauticaCraftsAssetsRepositoryImpl()

        val result = repository.decomposeCrafts(crafts)

        println(result)
        assertEquals(expected, result.toString())
    }
}