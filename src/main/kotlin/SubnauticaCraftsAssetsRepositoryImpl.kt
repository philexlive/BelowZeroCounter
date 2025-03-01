package org.example

import kotlinx.serialization.json.Json

/**
 * Getting crafts and its decomposition result.
 * */
class SubnauticaCraftsAssetsRepositoryImpl : SubnauticaCraftsAssetsRepository {
    private val branch by lazy { CraftBranch(ArrayDeque()) }

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

        // Iterate craft references if not null
        crafts?.forEach { ref ->
            // Get craft from data source by ref.key representing craft's id
            val craft = getSubnauticaCraftById(ref.key)

            // Craft must be not null. That's because if craft is null it considered "natural" resource.
            // And there should be no copies in the branch, to prevent infinite recursion caused by "reversive" crafts.
            //
            // For example "titanium" crafts "titanium ingot", but "titanium ingot" crafts "titanium".
            //
            //
            // If there is no copies, it's added to the branch as last.
            if (craft != null && branch.push(ref.key)) {
                result.putAll(decomposeCrafts(craft.materials))
            }
            // If the craft is null, or it is already in the current branch it adds current craft to the result
            else  {
                // Checks if it added to branch and there is no copies in again, if yes, we just "check" it
                // and adds to the result map.
                //
                // Else we just use current ref as the result.
                val key: String? = if (branch.push(ref.key)){
                    branch.check()
                } else{
                    ref.key
                }

                println("${branch.show()} $key")

                if(key != null) {
                    result[key] = ref.value
                }
            }
            // Pops branch stack - we finished interaction with current resource.
            branch.pop()
        }

        return result
    }
}

/**
 * Saves the state of past craft
 * */
private class CraftBranch(private var branch: ArrayDeque<String>) {
    /**
     * Add key to the branch as last if it has no this key yet.
     * */
    fun push(key: String): Boolean {
        return if (branch.contains(key)) {
            false
        } else {
            branch.addLast(key)
            true
        }
    }

    /**
     * Pops the last key from the branch or returns null if branch is empty
     * */
    fun pop(): String? {
        val result = if (branch.isNotEmpty()) {
            branch.removeLast()
        } else {
            null
        }
        return result
    }

    /**
     * Gets last key from the branch without popping
     * */
    fun check(): String? {
        val result = if (branch.isNotEmpty()) {
            branch.last()
        } else {
            null
        }
        return result
    }

    /**
     * returns current branch for reading
     * */
    fun show() = branch
}
