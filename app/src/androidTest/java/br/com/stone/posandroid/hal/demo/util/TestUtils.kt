package br.com.stone.posandroid.hal.demo.util

fun String.isValidHex(expectedSize: Int = 0): Boolean {
    val patternSize = if (expectedSize == 0) "+" else expectedSize.toString()
    val pattern = "[0-9A-F]{${patternSize}}"

    return this.matches(Regex(pattern))
}
