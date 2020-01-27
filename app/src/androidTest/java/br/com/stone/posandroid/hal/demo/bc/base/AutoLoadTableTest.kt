package br.com.stone.posandroid.hal.demo.bc.base

import androidx.test.platform.app.InstrumentationRegistry
import br.com.stone.hal_demo.R
import br.com.stone.posandroid.hal.api.Properties
import br.com.stone.posandroid.hal.api.bc.Pinpad
import br.com.stone.posandroid.hal.api.bc.PinpadCallbacks
import br.com.stone.posandroid.hal.api.bc.PinpadResult
import br.com.stone.posandroid.hal.api.bc.PinpadResult.Companion.TLI
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_OK
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_TABEXP
import br.com.stone.posandroid.hal.api.bc.constants.RuntimeProperties
import br.com.stone.posandroid.hal.demo.HALConfig
import br.com.stone.posandroid.hal.mock.bc.PinpadStub.Companion.CombinedResult
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import java.util.*


abstract class AutoLoadTableTest {

    lateinit var pinpad: Pinpad
    protected lateinit var callback: PinpadCallbacks
    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }

    @Before
    open fun setup() {
        callback = mockk(relaxed = true)

        val queue =
            ArrayDeque(
                listOf(
                    CombinedResult(PinpadResult(PinpadResult.OPN, PP_OK)),
                    CombinedResult(PinpadResult(TLI, PP_OK)),
                    CombinedResult(PinpadResult(PinpadResult.CLO, PP_OK))
                )
            )

        pinpad = HALConfig.deviceProvider.getPinpad(
            mutableMapOf(
                Properties.KEY_CONTEXT to context
            ),
            mutableMapOf(
                RuntimeProperties.PinLayout.KEY_PIN_KBD_LAYOUT_ID to R.layout.regularkeyboard,
                Properties.RESULTS_KEY to queue,
                Properties.TARGET_RESULT_KEY to Properties.RESULTS_KEY
            ), callback
        )

        pinpad.open()

        loadTableIfNeeded()

        pinpad.runtimeProperties[Properties.TARGET_RESULT_KEY] = Properties.RESULTS_FILE_KEY
    }

    @After
    fun tearDown() {
        pinpad.runtimeProperties[Properties.TARGET_RESULT_KEY] = Properties.RESULTS_KEY
        pinpad.close()
    }

    private fun loadTableIfNeeded() {

        if (pinpad.getTimeStamp(ACQUIRER_ID).output != TABLE_STUB_TIMESTAMP) {

            pinpad.tableLoadInit("$ACQUIRER_ID$TABLE_STUB_TIMESTAMP")

            TABLE_STUB_RECORDS.forEach {
                pinpad.tableLoadRec($REC$it)
            }

            pinpad.tableLoadEnd()
        }
    }

    companion object {
        const val ACQUIRER_ID = "08"
        const val RECORD_AMOUNT = "01"
        const val TABLE_STUB_TIMESTAMP = "1808200202"
        //        val TABLE_STUB_TIMESTAMP = UUID.randomUUID().toString().substring(0, 10) //force load table
        val TABLE_STUB_RECORDS = arrayOf(

        )
    }
}