package br.com.stone.posandroid.hal.demo.util

import br.com.stone.posandroid.hal.api.bc.PinpadResult
import br.com.stone.posandroid.hal.api.bc.PinpadResultCallback
import br.com.stone.posandroid.hal.api.printer.PrintCallback
import io.mockk.spyk
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit


class PinpadResultCallbackImpl(
    private val semaphore: Semaphore,
    private val pinpadResultAssertions: (PinpadResult) -> Unit
) : PinpadResultCallback {

    override fun onPinpadResult(pinpadResult: PinpadResult) {
        pinpadResultAssertions(pinpadResult)
        semaphore.release()
    }
}

/**
 * Runs assertions in an async way. This method should be used when testing AsyncResults,
 * AsyncEvents or any other Pinpad command that returns results asynchronously.
 *
 * @param pinpadResultAssertions Assertions that should be made after receiving the PinpadResult asynchronously
 * @param pinpadCommandsAssertions Assertions that should be made synchronously for PinpadCommands
 */
fun blockingAssertions(
    pinpadResultAssertions: (PinpadResult) -> Unit,
    pinpadCommandsAssertions: (PinpadResultCallback) -> Unit
) {

    val semaphore = Semaphore(0)

    val resultCallback = spyk(PinpadResultCallbackImpl(semaphore, pinpadResultAssertions))

    pinpadCommandsAssertions(resultCallback)

    semaphore.acquire()
}

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