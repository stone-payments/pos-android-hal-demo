package br.com.stone.posandroid.hal.demo.bc

import br.com.stone.posandroid.hal.api.Properties
import br.com.stone.posandroid.hal.api.bc.ext.getPINOrThrows
import br.com.stone.posandroid.hal.demo.bc.base.AutoOpenCloseTest
import br.com.stone.posandroid.hal.demo.rule.ConditionTestRule
import br.com.stone.posandroid.hal.demo.rule.Postcondition
import br.com.stone.posandroid.hal.demo.util.isValidHex
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AuthenticationTest : AutoOpenCloseTest() {

    private val stubResultsFolder = "resources/bc/authentication-tests"

    @get:Rule
    val preconditionsTestRule = ConditionTestRule()

    // Not Passed Incompatible command. Expected: GPN Actual: CLO
    @Test
    @Postcondition("Enter a 6-digit password and confirm")
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
