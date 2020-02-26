package br.com.stone.posandroid.hal.demo.bc

import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.bc.PinpadCallbacks.Companion.INSERT_SWIPE_CARD
import br.com.stone.posandroid.hal.api.bc.PinpadCallbacks.Companion.PROCESSING
import br.com.stone.posandroid.hal.api.bc.PinpadResult
import br.com.stone.posandroid.hal.api.bc.PinpadResultCallback
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_OK
import br.com.stone.posandroid.hal.api.bc.ext.removeCardOrThrows
import br.com.stone.posandroid.hal.demo.bc.base.AutoLoadTableTest
import br.com.stone.posandroid.hal.demo.util.DEFAULT_GCR_INPUT
import br.com.stone.posandroid.hal.demo.util.VISA_TESTCARD01_OUTPUT
import br.com.stone.posandroid.hal.demo.util.blockingAssertions
import io.mockk.verifySequence
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class CardCommandsTest : AutoLoadTableTest() {

    private val stubResultsFolder = "resources/card-commands-test"

    @Test
    fun validateGetCard() {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_get_card.json"

        val pinpadResultAssertions = { pinpadResult: PinpadResult ->

            val expectedOutput = VISA_TESTCARD01_OUTPUT
            assertEquals(PP_OK, pinpadResult.resultCode)
            assertEquals(expectedOutput, pinpadResult.output)

            verifySequence {
                callback.onEvent(INSERT_SWIPE_CARD, "")
                callback.onEvent(PROCESSING, "")
            }
        }

        val pinpadCommandsAssertions = { resultCallback: PinpadResultCallback ->
            assertEquals(
                PP_OK,
                pinpad.getCard(
                    DEFAULT_GCR_INPUT,
                    resultCallback
                )
            )
        }

        blockingAssertions(
            pinpadResultAssertions,
            pinpadCommandsAssertions
        )
    }

    @Test
    fun validateRemoveCard() = runBlocking {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_remove_card.json"

        assertTrue(pinpad.removeCardOrThrows("Remova o cartão").isBlank())
    }
}