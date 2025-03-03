package org.example

import kotlin.math.ceil

class SubnauticaCraftsDecomposer(
    private val repository: SubnauticaCraftsAssetsRepository
) : Decomposer<String, Map<String, Int>, Map<String, Int>>() {
    private val result: MutableMap<String, Float> = mutableMapOf()

    private lateinit var initialValue: Map<String, Int>

    override fun onFetch(key: String): Collection<String>? {
        return repository.getSubnauticaCraftById(key)?.materials?.keys
    }

    override fun onUpdate(key: String) {
        var prevKey = branch.getBranch().first()
        var quantity = initialValue[prevKey]?.toFloat() ?: 1.0f

        branch.getBranch().forEach {
            if (prevKey != it) {
                val materialQuantity = repository.getSubnauticaCraftById(prevKey)?.materials?.get(it) ?: 1
                val craftResultQuantity = repository.getSubnauticaCraftById(prevKey)?.quantity ?: 1

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

    override fun onGetResult(value: Map<String, Int>): Map<String, Int> {
        initialValue = value
        decompose(value.keys)
        return result.mapValues { ceil(it.value).toInt() }
    }
}