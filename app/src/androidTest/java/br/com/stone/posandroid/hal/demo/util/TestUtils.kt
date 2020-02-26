package br.com.stone.posandroid.hal.demo.util

import br.com.stone.posandroid.hal.api.printer.PrintCallback
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

/**
 *
 */
fun blockingPrinterAssertions(
    successAssertions: () -> Unit,
    errorAssertions: (Int) -> Unit,
    functionAssertions: (PrintCallback) -> Unit
) {

    val semaphore = Semaphore(0)

    val printCallback = object : PrintCallback {
        override fun onSuccess() {
            successAssertions()
            semaphore.release()
        }

        override fun onError(errorCode: Int) {
            errorAssertions(errorCode)
            semaphore.release()
        }
    }

    functionAssertions(printCallback)

    semaphore.tryAcquire(100, TimeUnit.MILLISECONDS)
}

fun String.isValidHex(expectedSize: Int = 0): Boolean {

    val patternSize = if (expectedSize == 0) "+" else expectedSize.toString()
    val pattern = "[0-9A-F]{${patternSize}}"

    return this.matches(Regex(pattern))
}