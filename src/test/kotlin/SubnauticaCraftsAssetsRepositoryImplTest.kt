import org.example.SubnauticaCraftsAssetsRepositoryImpl
import org.example.SubnauticaCraftsDecomposer
import org.junit.jupiter.api.Test


class SubnauticaCraftsAssetsRepositoryImplTest {

    @Test
    fun getNaturalResourcesForCraft() {
        val crafts = mapOf(
            "power_cell" to 3,
            "compass" to 1,
            "seaglide" to 10
        )

        val repository = SubnauticaCraftsAssetsRepositoryImpl()

        val decomposer = SubnauticaCraftsDecomposer(repository)

        println(decomposer.onGetResult(crafts))
    }
}

