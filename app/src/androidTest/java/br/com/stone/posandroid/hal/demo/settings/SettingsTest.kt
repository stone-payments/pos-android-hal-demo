package br.com.stone.posandroid.hal.demo.settings

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import br.com.stone.posandroid.hal.api.Properties.KEY_CONTEXT
import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.settings.DeviceInfo
import br.com.stone.posandroid.hal.api.settings.KeyboardType
import br.com.stone.posandroid.hal.api.settings.TimeData
import br.com.stone.posandroid.hal.demo.HALConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class SettingsTest {

    private val stubResultsFolder = "resources/settings/settings-test"
    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }

    @Test
    fun setSelfExamTimeTest() {
        val subject = HALConfig.deviceProvider.getSettings(mapOf(KEY_CONTEXT to context))

        val time = TimeData(0, 12, 34)
        subject.setSelfExamTime(time)

        assertEquals(time, subject.getSelfExamTime())
    }

    @Test
    fun toggleStatusBar() {

        val subject = HALConfig.deviceProvider.getSettings(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/settings-toggle-statusbar.json",
                KEY_CONTEXT to context
            )
        )

        assertTrue(subject.enableStatusBar(false))
        assertTrue(subject.enableStatusBar(true))
    }

    @Test
    fun toggleKeyCodeMenu() {
        val subject = HALConfig.deviceProvider.getSettings(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/settings-toggle-statusbar.json",
                KEY_CONTEXT to context
            )
        )

        assertTrue(subject.toggleKeyCodeMenu(true))
        assertTrue(subject.toggleKeyCodeMenu(false))
        assertTrue(subject.enableKeyCodeMenu())
    }

    @Test
    fun enableButtonsLight() {
        val subject = HALConfig.deviceProvider.getSettings(
            mapOf(
                KEY_CONTEXT to context
            )
        )

        assertTrue(subject.enableButtonsLight())
    }

    @Test
    fun disableButtonsLight() {
        val subject = HALConfig.deviceProvider.getSettings(
            mapOf(
                KEY_CONTEXT to context
            )
        )

        assertTrue(subject.disableButtonsLight())
    }

    @Test
    fun retrieveDeviceInfo() {

        val subject = HALConfig.deviceProvider.getSettings(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/settings-deviceinfo.json",
                KEY_CONTEXT to context
            )
        )

        val deviceInfo = subject.retrieveDeviceInfo()
        assertTrue("Serial number is empty",deviceInfo.serialNumber.isNotEmpty())
        assertTrue("Manufacture name is empty",deviceInfo.manufacturerName.isNotEmpty())
        assertTrue("OS build number is empty",deviceInfo.osBuildNumber.isNotEmpty())
        assertTrue("Model name is empty",deviceInfo.modelName.isNotEmpty())
        val isKernelVersionNotNullOrEmpty = deviceInfo.kernelVersion.isNullOrEmpty().not()
        val isKernelVersionUnknown = deviceInfo.kernelVersion != DeviceInfo.KERNEL_UNKNOWN
        assertTrue("Kernel version is empty",isKernelVersionNotNullOrEmpty || isKernelVersionUnknown)
        assertTrue(deviceInfo.keyboardType == KeyboardType.NONE || deviceInfo.keyboardType == KeyboardType.PINPAD)

    }

    @Test
    fun toggleButtonsLight() = runBlocking {
        val subject = HALConfig.deviceProvider.getSettings(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/settings-deviceinfo.json",
                KEY_CONTEXT to context
            )
        )

        assertTrue(subject.enableButtonsLight())
        delay(2000L)
        assertTrue(subject.disableButtonsLight())
    }

}