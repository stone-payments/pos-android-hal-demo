package br.com.stone.posandroid.hal.demo.bc

import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.bc.PinpadCallbacks
import br.com.stone.posandroid.hal.api.bc.ext.finishChipOrThrows
import br.com.stone.posandroid.hal.api.bc.ext.getCardOrThrows
import br.com.stone.posandroid.hal.api.bc.ext.getPINOrThrows
import br.com.stone.posandroid.hal.api.bc.ext.goOnChipOrThrows
import br.com.stone.posandroid.hal.api.bc.ext.removeCardOrThrows
import br.com.stone.posandroid.hal.demo.bc.base.AutoLoadTableTest
import br.com.stone.posandroid.hal.demo.rule.ConditionTestRule
import br.com.stone.posandroid.hal.demo.rule.Postcondition
import br.com.stone.posandroid.hal.demo.rule.Precondition
import br.com.stone.posandroid.hal.demo.util.DEFAULT_GCR_INPUT
import br.com.stone.posandroid.hal.demo.util.DEFAULT_GOC_COMMAND
import br.com.stone.posandroid.hal.demo.util.DEFAULT_LENGTH_CARD_MAG
import br.com.stone.posandroid.hal.demo.util.REGEX_CARD_CHIP
import br.com.stone.posandroid.hal.demo.util.REGEX_CARD_MAG
import br.com.stone.posandroid.hal.demo.util.VISA_TESTCARD01_OUTPUT
import br.com.stone.posandroid.hal.demo.util.isValidHex
import io.mockk.verifyOrder
import io.mockk.verifySequence
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.FixMethodOrder
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class PaymentFlowsTest : AutoLoadTableTest() {

    private val stubResultsFolder = "resources/bc/payment-flow-tests"

    @get:Rule
    val conditionTestRule = ConditionTestRule()

    @Test
    @Precondition("Insert card")
    @Postcondition("Remove the card after inserting the pin")
    fun validatePaymentWithContact() = runBlocking {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_contact_online_approved.json"

        val subject = pinpad.getCardOrThrows(DEFAULT_GCR_INPUT)
        assertTrue(subject.contains(REGEX_CARD_CHIP.toRegex()))
        val chipEMV = pinpad.goOnChipOrThrows(DEFAULT_GOC_COMMAND)

        assertTrue(chipEMV.startsWith('2') || chipEMV.startsWith('1'))
        if(chipEMV.startsWith('2')) {
            assertTrue(pinpad.finishChipOrThrows("0000000000000").startsWith('0'))
        }
        assertTrue(pinpad.removeCardOrThrows("Remova o cartão").isBlank())
        delay(1000)
        verifyOrder {
            callback.onEvent(PinpadCallbacks.UPDATING_TABLES, "")
            callback.onEvent(PinpadCallbacks.PROCESSING, "")
            callback.onEvent(PinpadCallbacks.PIN_STARTING, "")
            callback.onShowPinEntry(any(), any(), any())
            if(chipEMV.startsWith('2')) {
                callback.onEvent(PinpadCallbacks.REMOVE_CARD, "")
            }
        }
    }


    @Test
    @Precondition("Swipe the card")
    fun validatePaymentWithMag() = runBlocking {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_mag_pin_approved.json"
        delay(500)
        val subject = pinpad.getCardOrThrows(DEFAULT_GCR_INPUT)

        assertEquals(DEFAULT_LENGTH_CARD_MAG, subject.length)
        assertTrue(subject.contains(REGEX_CARD_MAG.toRegex()))

        assertTrue(
            pinpad.getPINOrThrows(
                "31600000000000000000000000000000000164391998410650011   10412DIGITE SUA SENHA                "
            ).isValidHex(expectedSize = 36)
        )

        verifyOrder {
            callback.onEvent(PinpadCallbacks.UPDATING_TABLES, "")
            callback.onEvent(PinpadCallbacks.INSERT_SWIPE_CARD, "")
            callback.onEvent(PinpadCallbacks.PIN_STARTING, "")
        }
    }


    @Test
    @Ignore("because you need the specific test card")
    fun validatePaymentWithContactDeniedOnline() = runBlocking {
        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_contact_online_denied.json"
        assertEquals(VISA_TESTCARD01_OUTPUT, pinpad.getCardOrThrows(DEFAULT_GCR_INPUT))
        assertTrue(pinpad.goOnChipOrThrows("").startsWith('2'))
        assertTrue(pinpad.finishChipOrThrows("0000000000000").startsWith('2'))
        assertTrue(pinpad.removeCardOrThrows("Remova o cartão").isBlank())
        verifySequence {
            callback.onEvent(PinpadCallbacks.INSERT_SWIPE_CARD, "")
            callback.onEvent(PinpadCallbacks.PROCESSING, "")
        }
    }


    @Test
    @Ignore("because you need the specific test card")
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
}
