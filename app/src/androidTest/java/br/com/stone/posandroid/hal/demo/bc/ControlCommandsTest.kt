package br.com.stone.posandroid.hal.demo.bc

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import br.com.stone.posandroid.hal.api.Properties.KEY_CONTEXT
import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.Properties.TARGET_RESULT_KEY
import br.com.stone.posandroid.hal.api.bc.Pinpad
import br.com.stone.posandroid.hal.api.bc.PinpadCallbacks
import br.com.stone.posandroid.hal.api.bc.PinpadResult
import br.com.stone.posandroid.hal.api.bc.PinpadResultCallback
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_ALREADYOPEN
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_NOTOPEN
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_OK
import br.com.stone.posandroid.hal.demo.HALConfig.deviceProvider
import br.com.stone.posandroid.hal.demo.util.DEFAULT_GCR_INPUT
import br.com.stone.posandroid.hal.demo.util.blockingAssertions
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class ControlCommandsTest {

    private val stubResultsFolder = "resources/control-command-test"
    private lateinit var pinpad: Pinpad
    private lateinit var pinpadCallbacks: PinpadCallbacks
    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }

    @Before
    fun setup() {
        pinpadCallbacks = mockk(relaxed = true)
        pinpad = deviceProvider.getPinpad(
            mutableMapOf(
                KEY_CONTEXT to context
            ),
            mutableMapOf(TARGET_RESULT_KEY to RESULTS_FILE_KEY),
            pinpadCallbacks
        )
    }

    @Test
    fun validateOpen() {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_open.json"

        assertEquals(PP_OK, pinpad.open())
    }

    @Test
    fun validateAlreadyOpen() {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_already_open.json"

        assertEquals(PP_OK, pinpad.open())
        assertEquals(PP_ALREADYOPEN, pinpad.open())
    }

    @Test
    fun validateOpenAndClose() {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_open_and_close.json"

        assertEquals(PP_OK, pinpad.open())
        assertEquals(PP_OK, pinpad.close())
    }

    @Test
    fun validateNotOpen() {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_not_open.json"

        assertEquals(PP_NOTOPEN, pinpad.close())
    }

    @Test
    fun validateAbortCommand() {
        val instrumentedTestThread = Thread.currentThread()

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_abort_command.json"

        every { pinpadCallbacks.onAbort() } answers { instrumentedTestThread.interrupt() }

        val pinpadResultAssertions = { _: PinpadResult -> }

        var callback: PinpadResultCallback? = null

        val pinpadCommandsAssertions = { resultCallback: PinpadResultCallback ->

            callback = resultCallback

            assertEquals(
                PP_OK,
                pinpad.getCard(
                    DEFAULT_GCR_INPUT,
                    resultCallback
                )
            )

            pinpad.abort()
        }

        blockingAssertions(
            pinpadResultAssertions,
            pinpadCommandsAssertions
        )

//        verify(exactly = 0) { callback?.onPinpadResult(any()) }
        verify(exactly = 1) { pinpadCallbacks.onAbort() }
    }
}