package br.com.stone.posandroid.hal.demo.settings

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.settings.DeviceInfo
import br.com.stone.posandroid.hal.demo.HALConfig
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SettingsTest {

    private val stubResultsFolder = "settings/settings-test"

    @Test
    fun toggleStatusBar() {

        val subject = HALConfig.deviceProvider.getSettings(
            mapOf(RESULTS_FILE_KEY to "$stubResultsFolder/settings-toggle-statusbar.json")
        )

        Assert.assertTrue(subject.enableStatusBar(true))
        Assert.assertTrue(subject.enableStatusBar(false))
    }

    @Test
    fun retrieveDeviceInfo() {

        val subject = HALConfig.deviceProvider.getSettings(
            mapOf(RESULTS_FILE_KEY to "$stubResultsFolder/settings-deviceinfo.json")
        )

        Assert.assertEquals(
            DeviceInfo("12345", true, "Stone", "Stone Mock Device", "1.0.0"),
            subject.getDeviceInfo()
        )
    }
}