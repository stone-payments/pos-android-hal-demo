package br.com.stone.posandroid.hal.demo.bc

import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.bc.PinpadCallbacks.Companion.PROCESSING
import br.com.stone.posandroid.hal.api.bc.PinpadCallbacks.Companion.UPDATING_TABLES
import br.com.stone.posandroid.hal.api.bc.ext.getCardOrThrows
import br.com.stone.posandroid.hal.api.bc.ext.removeCardOrThrows
import br.com.stone.posandroid.hal.demo.bc.base.AutoLoadTableTest
import br.com.stone.posandroid.hal.demo.rule.ConditionTestRule
import br.com.stone.posandroid.hal.demo.rule.Precondition
import br.com.stone.posandroid.hal.demo.util.DEFAULT_GCR_INPUT
import io.mockk.verifyOrder
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class CardCommandsTest : AutoLoadTableTest() {

    private val stubResultsFolder = "resources/card-commands-test"

    @get:Rule
    val conditionsTestRule = ConditionTestRule()


    @Test
    @Precondition("Insert Card")
    fun validateGetCard() = runBlocking {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_get_card.json"

        val subject = pinpad.getCardOrThrows(DEFAULT_GCR_INPUT)

        assertTrue(subject.contains("^0300108\\d{4}\\s{76}".toRegex()))

        verifyOrder {
            callback.onEvent(UPDATING_TABLES, "")
            callback.onEvent(PROCESSING, "")

        }

    }

    @Test
    @Precondition("Remove card")
    fun validateRemoveCard() = runBlocking {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_remove_card.json"

        assertTrue(pinpad.removeCardOrThrows("Remova o cartão").isBlank())
    }
}