package br.com.stone.posandroid.hal.demo.bc

import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.Properties.TARGET_RESULT_KEY
import br.com.stone.posandroid.hal.api.bc.Pinpad
import br.com.stone.posandroid.hal.api.bc.PinpadCallbacks
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode
import br.com.stone.posandroid.hal.api.bc.ext.getCardOrThrows
import br.com.stone.posandroid.hal.demo.HALConfig
import br.com.stone.posandroid.hal.demo.util.DEFAULT_GCR_INPUT
import br.com.stone.posandroid.hal.demo.util.VISA_TESTCARD01_OUTPUT
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class MockBehaviorTest {

    private val stubResultsFolder = "resources/mock-behavior-test"
    private lateinit var pinpad: Pinpad
    private lateinit var callback: PinpadCallbacks

    @Before
    fun setup() {
        callback = mockk(relaxed = true)
        pinpad = HALConfig.deviceProvider.getPinpad(
            emptyMap(),
            mutableMapOf(TARGET_RESULT_KEY to RESULTS_FILE_KEY),
            callback
        )
    }

    @Test
    fun validateMultipleConsecutiveCommandCalls() = runBlocking {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_multiple_get_card_calls.json"

        getCardCall()
        getCardCall()
        getCardCall()

        verifySequence {
            callback.onEvent(PinpadCallbacks.INSERT_SWIPE_CARD, "")
            callback.onEvent(PinpadCallbacks.PROCESSING, "")
            callback.onEvent(PinpadCallbacks.INSERT_SWIPE_CARD, "")
            callback.onEvent(PinpadCallbacks.PROCESSING, "")
            callback.onEvent(PinpadCallbacks.INSERT_SWIPE_CARD, "")
            callback.onEvent(PinpadCallbacks.PROCESSING, "")
        }
    }

    private suspend fun getCardCall() {
        assertEquals(ResultCode.PP_OK, pinpad.open())

        assertEquals(VISA_TESTCARD01_OUTPUT, pinpad.getCardOrThrows(DEFAULT_GCR_INPUT))

        assertEquals(ResultCode.PP_OK, pinpad.close())
    }
}