import org.example.Decomposer
import org.example.SubnauticaCraft
import org.example.SubnauticaCraftsAssetsRepositoryImpl
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SubnauticaCraftsAssetsRepositoryImplTest {

    @Test
    fun getSimpleDataFromReferences() {
        val list = listOf(
            1
        )

        val data = mapOf(
            1 to listOf(2, 3),
            3 to listOf(5, 6),
            4 to listOf(2),
            2 to listOf(4)
        )

        val expected: Set<Any> = setOf("4", "5", "6")


        val decomposer = object : Decomposer<Int, Craft, String>() {
            override fun getItemFromSource(key: Int): Craft? {
                return if (data[key] != null) key to data[key] else null
            }

            override fun getItemReferences(item: Craft): List<Int>? {
                return item.second
            }

            override fun onItemResultFromKey(key: Int): String {
                return key.toString()
            }

        }

        assertEquals(expected, decomposer.decompose(list))
    }


    @Test
    fun getNaturalResourcesForCraft() {
        val crafts = mapOf(
            "seatruck_depth_upgrade_mk3" to 1,
            "titanium" to 1
        )

        val repository = SubnauticaCraftsAssetsRepositoryImpl()

        val decomposer = object : Decomposer<String, SubnauticaCraft, String>() {
            override fun getItemFromSource(key: String): SubnauticaCraft? {
                return repository.getSubnauticaCraftById(key)
            }

            override fun getItemReferences(item: SubnauticaCraft): Set<String> {
                return item.materials.keys
            }

            override fun onItemResultFromKey(key: String): String {
                return key
            }

        }

        println(decomposer.decompose(crafts.keys))
    }
}

typealias Craft = Pair<Int, List<Int>?>

