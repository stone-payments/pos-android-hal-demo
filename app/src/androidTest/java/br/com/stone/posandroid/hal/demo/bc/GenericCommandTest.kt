package br.com.stone.posandroid.hal.demo.bc

import br.com.stone.posandroid.hal.api.bc.ext.genericCmdOrThrows
import br.com.stone.posandroid.hal.demo.bc.base.AutoOpenCloseTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class GenericCommandTest : AutoOpenCloseTest() {

    @Test
    fun validateGenericCommand() = runBlocking {

        val subject = pinpad.genericCmdOrThrows("040040140")
        assertTrue(subject.substring(1..2).contains("[1-9]".toRegex()))

    }
}
