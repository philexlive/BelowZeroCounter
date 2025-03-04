package org.philexliveprojects.treedecomposer

import org.philexliveprojects.treedecomposer.data.SubnauticaCraftsAssetsRepository
import kotlin.math.ceil


/* Sample Decomposer implementation */

class SubnauticaCraftDecomposer(
    private val repository: SubnauticaCraftsAssetsRepository
) : Decomposer<String, Map<String, Int>, Map<String, Int>>() {

    private val result: MutableMap<String, Float> = mutableMapOf()

    // initial data got from onResult to use their quantities in onUpdate
    private lateinit var initialValue: Map<String, Int>

    override fun onFetch(key: String): Collection<String>? {
        return repository.getSubnauticaCraftById(key)?.materials?.keys
    }

    override fun onUpdate(key: String) {
        var prevKey = branch.getBranch().first()
        var quantity = initialValue[prevKey]?.toFloat() ?: 1.0f

        // Iterate each element in branch getting quantities of craft and its material to multiply them
        branch.getBranch().forEach {
            if (prevKey != it) {
                val craftResultQuantity = repository.getSubnauticaCraftById(prevKey)?.quantity ?: 1
                val materialQuantity = repository.getSubnauticaCraftById(prevKey)?.materials?.get(it) ?: 1

                quantity *= materialQuantity / craftResultQuantity.toFloat()

                prevKey = it
            }
        }

        if (!result.containsKey(key)) {
            result[key] = quantity
        } else {
            result[key] = result[key]!! + quantity
        }
    }

    override fun onResult(value: Map<String, Int>): Map<String, Int> {
        initialValue = value
        decompose(value.keys)
        return result.mapValues { ceil(it.value).toInt() }
    }
}