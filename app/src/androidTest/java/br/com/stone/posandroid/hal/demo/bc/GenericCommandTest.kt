package br.com.stone.posandroid.hal.demo.bc

import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.bc.ext.genericCmdOrThrows
import br.com.stone.posandroid.hal.demo.bc.base.AutoOpenCloseTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GenericCommandTest : AutoOpenCloseTest() {

    private val stubResultsFolder = "resources/bc"

    // Not passed
    @Test
    fun validateGenericCommand() = runBlocking {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_generic_command.json"

        val subject = pinpad.genericCmdOrThrows("040040140")
        assertTrue(subject.substring(1..2).contains("[1-9]".toRegex()))
    }
}
