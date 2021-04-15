package br.com.stone.posandroid.hal.demo.util

import android.content.pm.PackageManager

fun String.isValidHex(expectedSize: Int = 0): Boolean {
    val patternSize = if (expectedSize == 0) "+" else expectedSize.toString()
    val pattern = "[0-9A-F]{${patternSize}}"

    return this.matches(Regex(pattern))
}


fun isPackageInstalled(
    packageManager: PackageManager,
    packageName: String
): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}
