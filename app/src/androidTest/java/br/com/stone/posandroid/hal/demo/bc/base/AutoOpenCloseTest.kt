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
import java.util.ArrayDeque


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
                KEY_CONTEXT to context,
                KEY_SUNMI_KEYMAP to "160331\n161332\n160301\n161302\n"
            ),

            mutableMapOf(
                RuntimeProperties.PinLayout.KEY_PIN_KBD_LAYOUT_ID to R.layout.regularkeyboard,
                RuntimeProperties.PinLayout.KEY_SUNMI_LAYOUT_INFO to "{\"layout\":${R.layout.regularkeyboard},\"numX\":16,\"numY\":644,\"numW\":172,\"numH\":170,\"lineW\":0,\"cancelX\":532,\"cancelY\":644,\"cancelW\":172,\"cancelH\":170,\"rows\":4,\"clos\":4,\"keymap0\":49,\"keymap1\":50,\"keymap2\":51,\"keymap3\":27,\"keymap4\":52,\"keymap5\":53,\"keymap6\":54,\"keymap7\":12,\"keymap8\":55,\"keymap9\":56,\"keymap10\":57,\"keymap11\":13,\"keymap12\":0,\"keymap13\":48,\"keymap14\":0,\"keymap15\":13}",
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
        if (::pinpad.isInitialized) {
            pinpad.runtimeProperties[TARGET_RESULT_KEY] = RESULTS_KEY
            pinpad.close()
        }
    }
}