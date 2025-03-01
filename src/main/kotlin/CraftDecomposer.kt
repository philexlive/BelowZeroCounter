package org.example


class CraftDecomposer<K, V>(
    private val decomposable: Array<out Craft<K, V>>,
    private val crafts: Array<Craft<K, V>>
) {
    fun decompose(): Array<Craft<K, V>> {
        return crafts
    }
}

fun <K : Any, V : Any> craftDecomposer(
    vararg decomposable: Craft<K, V>,
    crafts: () -> Array<Craft<K, V>>
): CraftDecomposer<K, V> = CraftDecomposer(decomposable, crafts())


abstract class Craft<K, out V>(
    val id: K,
    val materials: ArrayList<out V>
) {

}
