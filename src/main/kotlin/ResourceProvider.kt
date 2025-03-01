package org.example


const val CRAFTS_JSON = "crafts.json" // TODO Replace with actual crafts json


object ResourceProvider {
    fun getJson(name: String) = javaClass.classLoader.getResource(name)?.readText().orEmpty()
}
