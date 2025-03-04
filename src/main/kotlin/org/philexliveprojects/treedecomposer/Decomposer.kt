package org.philexliveprojects.treedecomposer

/**
 * Decomposer class for safe interaction with references in a data tree.
 *
 * [K] for keys of data tree,
 * [V] for external resources,
 * [R] for result value.
 * */
abstract class Decomposer<K, in V, out R> {
    /**
     * Contains current history of keys.
     * */
    protected val branch: Branch<K> by lazy { Branch() }

    /**
     * Decomposes current collection of top-level resources.
     * */
    protected fun decompose(resources: Collection<K>?): Set<K> {
        val result = mutableSetOf<K>()

        resources?.forEach { ref ->
            if (branch.push(ref)) {
                val subRefs = onFetch(ref)
                result addAll decompose(subRefs)
            }
        } ?: onUpdate(branch.check())

        onFinal(branch.pop())

        return result
    }

    /**
     * Fetching keys of resources related with key.
     * Returns [Collection] representing references to other resources in actual tree.
     *
     * [key] Key of current resource.
     * */
    protected abstract fun onFetch(key: K): Collection<K>?

    /**
     * Called on every last resource in the tree, that breaks that tree.
     *
     * [key] Key of last resource in the tree
     * */
    protected abstract fun onUpdate(key: K)

    /**
     * Called on final stage after resources iteration with onFetch and onUpdate, and on branch popping keys.
     *
     * [key] Is the resource's key popped from the [branch] stack.
     * */
    protected open fun onFinal(key: K?) = Unit

    /**
     * Getting user-generated [value] for convert and put into [decompose] for decomposition,
     * then returns with more appropriate data type.
     *
     * Use [decompose] function for decomposition.
     * */
    abstract fun onResult(value: V): R


    /**
     * Saves the state of past craft
     * */
    class Branch<T>(private val branch: ArrayDeque<T> = ArrayDeque()) {
        fun getBranch(): ArrayDeque<T> {
            return branch
        }

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

        fun check(): T {
            return branch.last()
        }

        fun contains(value: T): Boolean {
            return branch.contains(value)
        }


        override fun toString(): String {
            return branch.joinToString(separator = " <- ")
        }
    }


    private infix fun <T> MutableSet<T>.add(value: T) = this.add(value)

    private infix fun <T> MutableSet<T>.addAll(value: Collection<T>) = this.addAll(value)
}