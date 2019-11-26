package br.com.stone.posandroid.hal.demo.bc

import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.bc.PinpadCallbacks
import br.com.stone.posandroid.hal.api.bc.PinpadResult
import br.com.stone.posandroid.hal.api.bc.PinpadResultCallback
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_OK
import br.com.stone.posandroid.hal.demo.bc.base.AutoLoadTableTest
import br.com.stone.posandroid.hal.demo.util.DEFAULT_GCR_INPUT
import br.com.stone.posandroid.hal.demo.util.VISA_TESTCARD01_OUTPUT
import br.com.stone.posandroid.hal.demo.util.blockingAssertions
import br.com.stone.posandroid.hal.demo.util.isValidHex
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PaymentFlowsTest : AutoLoadTableTest() {

    private val stubResultsFolder = "bc/payment-flow-tests"

    @Test
    fun validatePaymentWithContactApprovedOnline() {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_contact_online_approved.json"

        val getCardPinpadCommandAssertions = { resultCallback: PinpadResultCallback ->
            assertEquals(
                PP_OK,
                pinpad.getCard(
                    DEFAULT_GCR_INPUT,
                    resultCallback
                )
            )
        }

        val getCardPinpadResultAssertions = { pinpadResult: PinpadResult ->

            val expectedOutput = VISA_TESTCARD01_OUTPUT
            assertEquals(expectedOutput, pinpadResult.output)

            verifySequence {
                callback.onEvent(PinpadCallbacks.INSERT_SWIPE_CARD, "")
                callback.onEvent(PinpadCallbacks.PROCESSING, "")
            }
        }

        blockingAssertions(
            getCardPinpadResultAssertions,
            getCardPinpadCommandAssertions
        )

        val goOnChipPinpadCommandAssertions = { resultCallback: PinpadResultCallback ->
            assertEquals(PP_OK, pinpad.goOnChip("", resultCallback))
        }

        val goOnChipPinpadResultAssertions = { pinpadResult: PinpadResult ->
            assertTrue(pinpadResult.output[0] == '2')
        }

        blockingAssertions(
            goOnChipPinpadResultAssertions,
            goOnChipPinpadCommandAssertions
        )

        val finishChipInput = "0000000000000"
        pinpad.finishChip(finishChipInput).run {
            assertEquals(PP_OK, resultCode)
            assertTrue(output[0] == '0')
        }

        val removeCardPinpadCommandsAssertions = { resultCallback: PinpadResultCallback ->
            val removeCardMessage = "Remova o cartão"
            assertEquals(PP_OK, pinpad.removeCard(removeCardMessage, resultCallback))
        }

        val removeCardPinpadResultAssertions = { pinpadResult: PinpadResult ->
            assertTrue(pinpadResult.output.isBlank())
        }

        blockingAssertions(
            removeCardPinpadResultAssertions,
            removeCardPinpadCommandsAssertions
        )
    }

    @Test
    fun validatePaymentWithContactDeniedOffline() {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_contact_offline_denied.json"

        val getCardPinpadCommandAssertions = { resultCallback: PinpadResultCallback ->
            assertEquals(PP_OK, pinpad.getCard("", resultCallback))
        }

        val getCardPinpadResultAssertions = { pinpadResult: PinpadResult ->

            val expectedOutput = VISA_TESTCARD01_OUTPUT
            assertEquals(expectedOutput, pinpadResult.output)

            verifySequence {
                callback.onEvent(PinpadCallbacks.INSERT_SWIPE_CARD, "")
                callback.onEvent(PinpadCallbacks.PROCESSING, "")
            }
        }

        blockingAssertions(
            getCardPinpadResultAssertions,
            getCardPinpadCommandAssertions
        )

        val goOnChipPinpadCommandAssertions = { resultCallback: PinpadResultCallback ->
            assertEquals(PP_OK, pinpad.goOnChip("", resultCallback))
        }

        val goOnChipPinpadResultAssertions = { pinpadResult: PinpadResult ->
            assertTrue(pinpadResult.output[0] == '1')
        }

        blockingAssertions(
            goOnChipPinpadResultAssertions,
            goOnChipPinpadCommandAssertions
        )

        val removeCardPinpadCommandsAssertions = { resultCallback: PinpadResultCallback ->
            val removeCardMessage = "Remova o cartão"
            assertEquals(PP_OK, pinpad.removeCard(removeCardMessage, resultCallback))
        }

        val removeCardPinpadResultAssertions = { pinpadResult: PinpadResult ->
            assertTrue(pinpadResult.output.isBlank())
        }

        blockingAssertions(
            removeCardPinpadResultAssertions,
            removeCardPinpadCommandsAssertions
        )
    }

    @Test
    fun validatePaymentWithMagApprovedWithPin() {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_mag_pin_approved.json"

        val getCardPinpadCommandAssertions = { resultCallback: PinpadResultCallback ->
            assertEquals(PP_OK, pinpad.getCard("", resultCallback))
        }

        val getCardPinpadResultAssertions = { pinpadResult: PinpadResult ->

            val expectedOutput =
                "00001010500                                                                            29376436871651006=0305000523966        000                                                                                                        15376436871651006    01AMEX GREEN      246SERGIO SANTOS             05013100                   00000000076000"
            assertEquals(expectedOutput, pinpadResult.output)

            val cardType = expectedOutput.take(2)
            assertTrue(cardType == "00" || cardType == "05")
        }

        blockingAssertions(
            getCardPinpadResultAssertions,
            getCardPinpadCommandAssertions
        )

        val getPinCommandAssertions = { resultCallback: PinpadResultCallback ->
            val getPinInput =
                "30800000000000000000000000000000000164391998410650011   10412DIGITE SUA SENHA                "
            assertEquals(PP_OK, pinpad.getPIN(getPinInput, resultCallback))
        }

        val getPinResultAssertions = { pinpadResult: PinpadResult ->
            assertEquals(PP_OK, pinpadResult.resultCode)
            assertTrue(pinpadResult.output.isValidHex(expectedSize = 36))
        }

        blockingAssertions(
            getPinResultAssertions,
            getPinCommandAssertions
        )

        verifySequence {
            callback.onEvent(PinpadCallbacks.INSERT_SWIPE_CARD, "")
            callback.onEvent(PinpadCallbacks.PROCESSING, "")
            callback.onEvent(PinpadCallbacks.PIN_STARTING, "")
            callback.onShowPinEntry(any(), any(), any())
        }
    }
}