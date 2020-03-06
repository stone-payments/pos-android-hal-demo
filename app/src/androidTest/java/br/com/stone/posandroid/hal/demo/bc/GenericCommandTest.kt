package br.com.stone.posandroid.hal.demo.bc

import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.bc.ext.genericCmdOrThrows
import br.com.stone.posandroid.hal.demo.bc.base.AutoOpenCloseTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GenericCommandTest : AutoOpenCloseTest() {

    private val stubResultsFolder = "resources/bc"

    @Test
    fun validateGenericCommand() = runBlocking {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_generic_command.json"

        assertEquals("", pinpad.genericCmdOrThrows("040040140"))
    }
}
