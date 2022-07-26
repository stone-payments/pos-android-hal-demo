package br.com.stone.posandroid.hal.demo.bc.base

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import br.com.stone.posandroid.hal.api.Properties.KEY_CONTEXT
import br.com.stone.posandroid.hal.api.Properties.KEY_SUNMI_KEYMAP
import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.Properties.RESULTS_KEY
import br.com.stone.posandroid.hal.api.Properties.TARGET_RESULT_KEY
import br.com.stone.posandroid.hal.api.bc.Pinpad
import br.com.stone.posandroid.hal.api.bc.PinpadCallbacks
import br.com.stone.posandroid.hal.api.bc.constants.RuntimeProperties
import br.com.stone.posandroid.hal.demo.HALConfig.deviceProvider
import br.com.stone.posandroid.hal.demo.R
import br.com.stone.posandroid.hal.demo.util.KEYMAP_SUNMI
import br.com.stone.posandroid.hal.demo.util.LAYOUT_PIN_SUNMI
import br.com.stone.posandroid.hal.demo.util.PinpadCallbackComponent
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
abstract class AutoOpenCloseTest {

    protected lateinit var pinpad: Pinpad
    protected val callback: PinpadCallbacks = PinpadCallbackComponent.init()
    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }

    @Before
    open fun setup() {
        initializePinpad()
    }

    protected fun initializePinpad() {
        pinpad = deviceProvider.getPinpad(
            mutableMapOf(
                KEY_CONTEXT to context,
                KEY_SUNMI_KEYMAP to KEYMAP_SUNMI
            ),

            mutableMapOf(
                RuntimeProperties.PinLayout.KEY_PIN_KBD_LAYOUT_ID to R.layout.regularkeyboard,
                RuntimeProperties.PinLayout.KEY_SUNMI_LAYOUT_INFO to LAYOUT_PIN_SUNMI,
                TARGET_RESULT_KEY to RESULTS_KEY
            ),
            callback
        )

        pinpad.open()
        pinpad.runtimeProperties[TARGET_RESULT_KEY] = RESULTS_FILE_KEY
    }

    @After
    open fun tearDown() {
        if (::pinpad.isInitialized) {
            pinpad.runtimeProperties[TARGET_RESULT_KEY] = RESULTS_KEY
            pinpad.close()
        }
        unmockkAll()
    }
}