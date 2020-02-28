package br.com.stone.posandroid.hal.demo.bc.base

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import br.com.stone.hal_demo.R
import br.com.stone.posandroid.hal.api.Properties.KEY_CONTEXT
import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.Properties.RESULTS_KEY
import br.com.stone.posandroid.hal.api.Properties.TARGET_RESULT_KEY
import br.com.stone.posandroid.hal.api.bc.Pinpad
import br.com.stone.posandroid.hal.api.bc.PinpadCallbacks
import br.com.stone.posandroid.hal.api.bc.PinpadResult
import br.com.stone.posandroid.hal.api.bc.PinpadResult.Companion.CLO
import br.com.stone.posandroid.hal.api.bc.PinpadResult.Companion.OPN
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_OK
import br.com.stone.posandroid.hal.api.bc.constants.RuntimeProperties
import br.com.stone.posandroid.hal.demo.HALConfig.deviceProvider
import br.com.stone.posandroid.hal.demo.R
import br.com.stone.posandroid.hal.mock.bc.PinpadStub.Companion.CombinedResult
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4ClassRunner::class)
abstract class AutoOpenCloseTest {

    protected lateinit var pinpad: Pinpad
    protected lateinit var callback: PinpadCallbacks
    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }

    @Before
    fun setup() {
        callback = mockk(relaxed = true)
        val queue =
            ArrayDeque(
                listOf(
                    CombinedResult(PinpadResult(OPN, PP_OK)),
                    CombinedResult(PinpadResult(CLO, PP_OK))
                )
            )

        pinpad = deviceProvider.getPinpad(
            mutableMapOf(
                KEY_CONTEXT to context
            ),
            mutableMapOf(
                RuntimeProperties.PinLayout.KEY_PIN_KBD_LAYOUT_ID to R.layout.regularkeyboard,
                RESULTS_KEY to queue,
                TARGET_RESULT_KEY to RESULTS_KEY
            ),
            callback
        )

        pinpad.open()

        pinpad.runtimeProperties[TARGET_RESULT_KEY] = RESULTS_FILE_KEY
    }

    @After
    fun tearDown() {
        pinpad.runtimeProperties[TARGET_RESULT_KEY] = RESULTS_KEY
        pinpad.close()
    }
}