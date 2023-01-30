package br.com.stone.posandroid.hal.demo.bc

import android.util.Log
import br.com.stone.posandroid.hal.api.bc.PinpadCallbacks.Companion.PROCESSING
import br.com.stone.posandroid.hal.api.bc.PinpadCallbacks.Companion.UPDATING_TABLES
import br.com.stone.posandroid.hal.api.bc.ext.getCardOrThrows
import br.com.stone.posandroid.hal.api.bc.ext.removeCardOrThrows
import br.com.stone.posandroid.hal.demo.bc.base.AutoLoadTableTest
import br.com.stone.posandroid.hal.demo.rule.ConditionTestRule
import br.com.stone.posandroid.hal.demo.rule.Precondition
import br.com.stone.posandroid.hal.demo.util.DEFAULT_GCR_INPUT
import br.com.stone.posandroid.hal.demo.util.REGEX_CARD_CHIP
import io.mockk.verifyOrder
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class CardCommandsTest : AutoLoadTableTest() {

    @get:Rule
    val conditionsTestRule = ConditionTestRule()


    @Test
    @Precondition("Insert Card")
    fun validateGetCard() = runBlocking {
        val subject = pinpad.getCardOrThrows(DEFAULT_GCR_INPUT)

        assertTrue(subject.contains(REGEX_CARD_CHIP.toRegex()))

//        verifyOrder {
//            callback.onEvent(UPDATING_TABLES, any())
//            callback.onEvent(PROCESSING, any())
//        }
    }

    @Test
    @Precondition("Remove card")
    fun validateRemoveCard() = runBlocking {
        val result = pinpad.removeCardOrThrows("Remova o cartão")
        Log.d("Remove", result)
        assertTrue(result.isBlank())


    }
}