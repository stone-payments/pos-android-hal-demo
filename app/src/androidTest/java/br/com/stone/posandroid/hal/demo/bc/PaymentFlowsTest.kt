package br.com.stone.posandroid.hal.demo.bc

import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.bc.PinpadCallbacks
import br.com.stone.posandroid.hal.api.bc.ext.finishChipOrThrows
import br.com.stone.posandroid.hal.api.bc.ext.getCardOrThrows
import br.com.stone.posandroid.hal.api.bc.ext.getPINOrThrows
import br.com.stone.posandroid.hal.api.bc.ext.goOnChipOrThrows
import br.com.stone.posandroid.hal.api.bc.ext.removeCardOrThrows
import br.com.stone.posandroid.hal.demo.bc.base.AutoLoadTableTest
import br.com.stone.posandroid.hal.demo.util.DEFAULT_GCR_INPUT
import br.com.stone.posandroid.hal.demo.util.VISA_TESTCARD01_OUTPUT
import br.com.stone.posandroid.hal.demo.util.isValidHex
import io.mockk.verifySequence
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test

class PaymentFlowsTest : AutoLoadTableTest() {

    private val stubResultsFolder = "resources/bc/payment-flow-tests"

    @Test
    fun validatePaymentWithContactApprovedOnline() = runBlocking {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_contact_online_approved.json"

        assertEquals(VISA_TESTCARD01_OUTPUT, pinpad.getCardOrThrows(DEFAULT_GCR_INPUT))

        assertTrue(pinpad.goOnChipOrThrows("").startsWith('2'))

        assertTrue(pinpad.finishChipOrThrows("0000000000000").startsWith('0'))

        assertTrue(pinpad.removeCardOrThrows("Remova o cartão").isBlank())

        verifySequence {
            callback.onEvent(PinpadCallbacks.INSERT_SWIPE_CARD, "")
            callback.onEvent(PinpadCallbacks.PROCESSING, "")
        }
    }

    @Ignore
    @Test
    fun validatePaymentWithContactDeniedOffline() = runBlocking {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_contact_offline_denied.json"

        assertEquals(VISA_TESTCARD01_OUTPUT, pinpad.getCardOrThrows(DEFAULT_GCR_INPUT))

        assertTrue(pinpad.goOnChipOrThrows("").startsWith('1'))

        assertTrue(pinpad.removeCardOrThrows("Remova o cartão").isBlank())

        verifySequence {
            callback.onEvent(PinpadCallbacks.INSERT_SWIPE_CARD, "")
            callback.onEvent(PinpadCallbacks.PROCESSING, "")
        }
    }

    @Ignore
    @Test
    fun validatePaymentWithMagApprovedWithPin() = runBlocking {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_mag_pin_approved.json"

        val expectedOutput =
            "00001010500                                                                            29376436871651006=0305000523966        000                                                                                                        15376436871651006    01AMEX GREEN      246SERGIO SANTOS             05013100                   00000000076000"

        assertEquals(expectedOutput, pinpad.getCardOrThrows(DEFAULT_GCR_INPUT))

        assertTrue(
            pinpad.getPINOrThrows(
                "31600000000000000000000000000000000164391998410650011   10412DIGITE SUA SENHA                "
            ).isValidHex(expectedSize = 36)
        )

        verifySequence {
            callback.onEvent(PinpadCallbacks.INSERT_SWIPE_CARD, "")
            callback.onEvent(PinpadCallbacks.PROCESSING, "")
            callback.onEvent(PinpadCallbacks.PIN_STARTING, "")
            callback.onShowPinEntry(any(), any(), any())
        }
    }
}
