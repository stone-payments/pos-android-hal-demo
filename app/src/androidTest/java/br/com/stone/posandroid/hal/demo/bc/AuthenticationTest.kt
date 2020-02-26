package br.com.stone.posandroid.hal.demo.bc

import br.com.stone.posandroid.hal.api.Properties
import br.com.stone.posandroid.hal.api.bc.PinpadResult
import br.com.stone.posandroid.hal.api.bc.PinpadResultCallback
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_OK
import br.com.stone.posandroid.hal.demo.bc.base.AutoOpenCloseTest
import br.com.stone.posandroid.hal.demo.util.blockingAssertions
import br.com.stone.posandroid.hal.demo.util.isValidHex
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthenticationTest : AutoOpenCloseTest() {

    private val stubResultsFolder = "resources/bc/authentication-tests"

    @Test
    fun validateGetPin() {

        pinpad.runtimeProperties[Properties.RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_get_pin.json"

        val pinpadResultsAssertion = { pinpadResult: PinpadResult ->
            assertEquals(PP_OK, pinpadResult.resultCode)
            assertTrue(pinpadResult.output.isValidHex(expectedSize = 36))
        }

        val pinpadCommandsAssertion = { resultCallback: PinpadResultCallback ->
            val getPinInput =
                "31600000000000000000000000000000000164391998410650011   10412DIGITE SUA SENHA                "
            assertEquals(PP_OK, pinpad.getPIN(getPinInput, resultCallback))
        }

        blockingAssertions(
            pinpadResultsAssertion,
            pinpadCommandsAssertion
        )
    }
}