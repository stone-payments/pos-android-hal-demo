package br.com.stone.posandroid.hal.demo.bc

import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.Properties.TARGET_RESULT_KEY
import br.com.stone.posandroid.hal.api.bc.Pinpad
import br.com.stone.posandroid.hal.api.bc.PinpadCallbacks
import br.com.stone.posandroid.hal.api.bc.PinpadResult
import br.com.stone.posandroid.hal.api.bc.PinpadResultCallback
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode
import br.com.stone.posandroid.hal.demo.HALConfig
import br.com.stone.posandroid.hal.demo.bc.base.AutoLoadTableTest.Companion.TABLE_STUB_TIMESTAMP
import br.com.stone.posandroid.hal.demo.util.blockingAssertions
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class MockBehaviorTest {

    private val stubResultsFolder = "mock-behavior-test"
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
    fun validateMultipleConsecutiveCommandCalls() {

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

    private fun getCardCall() {

        val pinpadResultAssertions = { pinpadResult: PinpadResult ->

            val expectedOutput =
                "03001010500                                                                            29376436871651006=0305000523966        000                                                                                                        15376436871651006    01AMEX GREEN      246SERGIO SANTOS             05013100                   00000000076000"
            assertEquals(expectedOutput, pinpadResult.output)
        }

        val pinpadCommandsAssertions = { resultCallback: PinpadResultCallback ->
            assertEquals(
                ResultCode.PP_OK,
                pinpad.getCard(
                    "0099000000023850020904164230${TABLE_STUB_TIMESTAMP}000",
                    resultCallback
                )
            )
        }

        assertEquals(ResultCode.PP_OK, pinpad.open())

        blockingAssertions(
            pinpadResultAssertions,
            pinpadCommandsAssertions
        )

        assertEquals(ResultCode.PP_OK, pinpad.close())
    }
}