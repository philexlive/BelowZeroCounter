package org.example


abstract class Decomposer<K, in V, out R> {
    protected val branch: Branch<K> by lazy { Branch() }

    protected fun decompose(refs: Collection<K>?): Set<K> {
        val temp = mutableSetOf<K>()

        // Iterate craft references if not null
        refs?.forEach { ref ->

            // And there should be no copies in the branch, to prevent infinite recursion caused by "reversive" crafts.
            //
            // For example "titanium" crafts "titanium ingot", but "titanium ingot" crafts "titanium".
            //
            // If there is no copies, it's added to the branch as last
            if (branch.push(ref)) {
                val subRefs = onFetch(ref)
                temp addAll decompose(subRefs)
            }

            // Pops branch stack - we finished interaction with current resource.
        } ?: onUpdate(branch.check())

        onFinal(branch.pop())

        return temp
    }

    protected abstract fun onFetch(key: K): Collection<K>?

    protected abstract fun onUpdate(key: K)

    protected open fun onFinal(key: K?) = Unit

    abstract fun onGetResult(value: V): R


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

        /**
         * Gets last key from the branch without popping
         * */
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