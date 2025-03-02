package org.example


abstract class Decomposer<K, V, out R> {
    private val branch: Branch<K> by lazy { Branch() }

    fun decompose(keys: Collection<K>?): Set<R> {
        val result = mutableSetOf<R>()

        // Iterate craft references if not null
        keys?.forEach { key ->
            // Get craft from data source by ref.key representing craft's id
            val item = getItemFromSource(key)

            // Craft must be not null. That's because if craft is null it considered "natural" resource.
            // And there should be no copies in the branch, to prevent infinite recursion caused by "reversive" crafts.
            //
            // For example "titanium" crafts "titanium ingot", but "titanium ingot" crafts "titanium".
            //
            // If there is no copies, it's added to the branch as last.
            if (item != null && branch.push(key)) {
                val refs = getItemReferences(item)
                result addAll decompose(refs)
            }

            // If the craft is null, or it is already in the current branch it adds current craft to the result
            else {
                // Checks if it added to branch and there is no copies in again, if yes, we just "check" it
                // and adds to the result map.
                //
                // Else we just use current ref as the result.

                val last = if (branch.contains(key)) {
                    branch.check()
                } else {
                    key
                }

                if (last != null) {
                    result add onItemResultFromKey(last)
                }

                println("$branch")
            }

            // Pops branch stack - we finished interaction with current resource.
            branch.pop()
        }

        return result
    }

    abstract fun getItemFromSource(key: K): V?

    abstract fun getItemReferences(item: V): Collection<K>?

    abstract fun onItemResultFromKey(key: K): R

    /**
     * Saves the state of past craft
     * */
    private data class Branch<T>(
        private val branch: ArrayDeque<T> = ArrayDeque()
    ) {
        /**
         * Add key to the branch as last if it has no this key yet.
         * */
        fun push(value: T): Boolean {
            return if (branch.contains(value)) {
                false
            } else {
                branch.addLast(value)
                true
            }
        }

        /**
         * Pops the last key from the branch or returns null if branch is empty
         * */
        fun pop(): T? {
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
        fun check(): T? {
            val result = if (branch.isNotEmpty()) {
                branch.last()
            } else {
                null
            }
            return result
        }

        fun contains(value: T): Boolean {
            return branch.contains(value)
        }


        override fun toString(): String {
            return branch.joinToString(separator = " <- ")
        }
    }
}

private infix fun <T> MutableSet<T>.add(value: T) = this.add(value)

private infix fun <T> MutableSet<T>.addAll(value: Collection<T>) = this.addAll(value)
