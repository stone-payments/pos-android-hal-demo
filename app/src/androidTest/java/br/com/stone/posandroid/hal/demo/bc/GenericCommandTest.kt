package br.com.stone.posandroid.hal.demo.bc

import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.bc.PinpadResult
import br.com.stone.posandroid.hal.api.bc.PinpadResultCallback
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_OK
import br.com.stone.posandroid.hal.demo.bc.base.AutoOpenCloseTest
import br.com.stone.posandroid.hal.demo.util.blockingAssertions
import org.junit.Assert.assertEquals
import org.junit.Test

class GenericCommandTest : AutoOpenCloseTest() {

    private val stubResultsFolder = "bc"

    @Test
    fun validateGenericCommand() {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_generic_command.json"

        val pinpadCommandAssertions = { resultCallback: PinpadResultCallback ->
            val input = "040040140"
            assertEquals(PP_OK, pinpad.genericCmd(input, resultCallback))
        }

        val pinpadResultAssertions = { pinpadResult: PinpadResult ->
            val expectedOutput = ""
            assertEquals(expectedOutput, pinpadResult.output)
            assertEquals(PP_OK, pinpadResult.resultCode)
        }

        blockingAssertions(
            pinpadResultAssertions,
            pinpadCommandAssertions
        )
    }
}