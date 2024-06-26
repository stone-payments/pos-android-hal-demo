package br.com.stone.posandroid.hal.demo.bc

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import br.com.stone.posandroid.hal.api.Properties
import br.com.stone.posandroid.hal.api.Properties.KEY_CONTEXT
import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.Properties.TARGET_RESULT_KEY
import br.com.stone.posandroid.hal.api.bc.Pinpad
import br.com.stone.posandroid.hal.api.bc.PinpadCallbacks
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_ALREADYOPEN
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_CANCEL
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_NOTOPEN
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_OK
import br.com.stone.posandroid.hal.api.bc.constants.RuntimeProperties
import br.com.stone.posandroid.hal.api.bc.exception.PinpadException
import br.com.stone.posandroid.hal.api.bc.ext.getCardOrThrows
import br.com.stone.posandroid.hal.demo.HALConfig.deviceProvider
import br.com.stone.posandroid.hal.demo.rule.ConditionTestRule
import br.com.stone.posandroid.hal.demo.rule.Precondition
import br.com.stone.posandroid.hal.demo.util.DEFAULT_GCR_INPUT
import br.com.stone.posandroid.hal.demo.util.KEYMAP_SUNMI
import br.com.stone.posandroid.hal.demo.util.LAYOUT_PIN_SUNMI
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class ControlCommandsTest {

    private val stubResultsFolder = "resources/control-command-test"
    private lateinit var pinpad: Pinpad
    private lateinit var pinpadCallbacks: PinpadCallbacks
    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }

    @get:Rule
    val conditionsTestRule = ConditionTestRule()

    @Before
    fun setup() {
        pinpadCallbacks = mockk(relaxed = true)
        pinpad = deviceProvider.getPinpad(
            mutableMapOf(
                KEY_CONTEXT to context,
                Properties.KEY_SUNMI_KEYMAP to KEYMAP_SUNMI
            ),
            mutableMapOf(
                TARGET_RESULT_KEY to RESULTS_FILE_KEY,
                RuntimeProperties.PinLayout.KEY_SUNMI_LAYOUT_INFO to LAYOUT_PIN_SUNMI
            ),
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
    @Ignore("Because feature not implement")
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
    @Ignore("Because feature not implement")
    fun validateNotOpen() {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_not_open.json"

        assertEquals(PP_NOTOPEN, pinpad.close())
    }

    @Test
    @Precondition("Insert the card")
    fun validateAbortCommand() = runBlocking {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_abort_command.json"

        var subject: Int = Int.MIN_VALUE


        launch(Dispatchers.IO) {
            delay(100)
            pinpad.abort()
        }

        try {
            pinpad.getCardOrThrows(DEFAULT_GCR_INPUT)
        } catch (pinpad: PinpadException) {
            subject = pinpad.result.resultCode
        }
        assertEquals(PP_CANCEL, subject)

        verify(exactly = 1) {
            pinpadCallbacks.onAbort()
        }

    }
}
