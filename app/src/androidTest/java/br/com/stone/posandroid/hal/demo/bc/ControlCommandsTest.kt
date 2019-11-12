package br.com.stone.posandroid.hal.demo.bc

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import br.com.stone.posandroid.hal.api.Properties.KEY_CONTEXT
import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.Properties.TARGET_RESULT_KEY
import br.com.stone.posandroid.hal.api.bc.Pinpad
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_ALREADYOPEN
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_NOTOPEN
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_OK
import br.com.stone.posandroid.hal.demo.HALConfig.deviceProvider
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class ControlCommandsTest {

    private val stubResultsFolder = "control-command-test"
    private lateinit var pinpad: Pinpad
    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }

    @Before
    fun setup() {
        pinpad = deviceProvider.getPinpad(
            mutableMapOf(
                KEY_CONTEXT to context
            ),
            mutableMapOf(TARGET_RESULT_KEY to RESULTS_FILE_KEY),
            mockk()
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
}