package br.com.stone.posandroid.hal.demo.bc

import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.bc.PinpadCallbacks.Companion.INSERT_SWIPE_CARD
import br.com.stone.posandroid.hal.api.bc.PinpadCallbacks.Companion.PROCESSING
import br.com.stone.posandroid.hal.api.bc.ext.getCardOrThrows
import br.com.stone.posandroid.hal.api.bc.ext.removeCardOrThrows
import br.com.stone.posandroid.hal.demo.bc.base.AutoLoadTableTest
import br.com.stone.posandroid.hal.demo.util.DEFAULT_GCR_INPUT
import br.com.stone.posandroid.hal.demo.util.VISA_TESTCARD01_OUTPUT
import io.mockk.verifySequence
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class CardCommandsTest : AutoLoadTableTest() {

    private val stubResultsFolder = "resources/card-commands-test"

    @Test
    fun validateGetCard() = runBlocking {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_get_card.json"

        assertEquals(VISA_TESTCARD01_OUTPUT, pinpad.getCardOrThrows(DEFAULT_GCR_INPUT))

        verifySequence {
            callback.onEvent(INSERT_SWIPE_CARD, "")
            callback.onEvent(PROCESSING, "")
        }
    }

    @Test
    fun validateRemoveCard() = runBlocking {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_remove_card.json"

        assertTrue(pinpad.removeCardOrThrows("Remova o cartão").isBlank())
    }
}