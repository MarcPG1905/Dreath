package com.marcpg.modules.algorithm

class MarkovChain(
    samples: List<String>,
    private val order: Int = 2
) {
    private val transitions = mutableMapOf<String, MutableList<Char>>()

    init {
        train(samples)
    }

    private fun train(samples: List<String>) {
        for (word in samples) {
            val padded = "^".repeat(order) + word.lowercase() + "$"  // Start and end markers
            for (i in 0..<padded.length - order) {
                val key = padded.substring(i, i + order)
                val nextChar = padded[i + order]
                transitions.computeIfAbsent(key) { mutableListOf() }.add(nextChar)
            }
        }
    }

    fun generate(maxLength: Int = 16): String {
        var key = "^".repeat(order)
        val name = StringBuilder()

        while (name.length < maxLength) {
            val nextChar = transitions[key]?.randomOrNull() ?: return name.toString()
            if (nextChar == '$') break
            name.append(nextChar)
            key = (key + nextChar).takeLast(order)
        }

        return name.toString().replaceFirstChar { it.uppercaseChar() }
    }
}