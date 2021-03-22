package br.com.stone.posandroid.hal.demo.settings

import android.os.Build
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import br.com.stone.posandroid.hal.api.Properties.KEY_CONTEXT
import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.settings.DeviceInfo
import br.com.stone.posandroid.hal.demo.HALConfig
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SettingsTest {

    private val stubResultsFolder = "resources/settings/settings-test"
    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }

    @Test
    fun toggleStatusBar() {

        val subject = HALConfig.deviceProvider.getSettings(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/settings-toggle-statusbar.json",
                KEY_CONTEXT to context
            )
        )

        Assert.assertTrue(subject.enableStatusBar(false))
        Assert.assertTrue(subject.enableStatusBar(true))
    }

    @Test
    fun retrieveDeviceInfo() {

        val subject = HALConfig.deviceProvider.getSettings(
            mapOf(RESULTS_FILE_KEY to "$stubResultsFolder/settings-deviceinfo.json")
        )

        Assert.assertEquals(
            DeviceInfo(
                "12345",
                true,
                "Stone",
                "Stone Mock Device",
                "1.0.0",
                kernelVersion = "220121"
            ),
            subject.getDeviceInfo()
        )

        Assert.assertEquals(Build.MANUFACTURER, "SUNMI")
    }


}