package br.com.stone.posandroid.hal.demo.bc

import br.com.stone.posandroid.hal.api.Properties
import br.com.stone.posandroid.hal.api.bc.ext.getPINOrThrows
import br.com.stone.posandroid.hal.demo.bc.base.AutoOpenCloseTest
import br.com.stone.posandroid.hal.demo.util.isValidHex
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthenticationTest : AutoOpenCloseTest() {

    private val stubResultsFolder = "resources/bc/authentication-tests"

    @Test
    fun validateGetPin() = runBlocking {

        pinpad.runtimeProperties[Properties.RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_get_pin.json"

        assertTrue(
            pinpad.getPINOrThrows(
                "31600000000000000000000000000000000164391998410650011   10412DIGITE SUA SENHA                "
            ).isValidHex(expectedSize = 36)
        )
    }
}