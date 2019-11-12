package br.com.stone.posandroid.hal.demo.installer

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.demo.HALConfig
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class InstallerTest {

    private val stubResultsFolder = "installer/installer-test"

    @Test
    fun installSuccess() {
        val subject = HALConfig.deviceProvider.getInstaller(
            mapOf(RESULTS_FILE_KEY to "$stubResultsFolder/installer-install.json")
        )

        Assert.assertEquals(
            0,
            subject.install("/sdcard/stone/test-application.apk")
        )
    }

    @Test
    fun uninstallSuccess() {
        val subject = HALConfig.deviceProvider.getInstaller(
            mapOf(RESULTS_FILE_KEY to "$stubResultsFolder/installer-uninstall.json")
        )

        Assert.assertEquals(
            0,
            subject.uninstallApk("br.com.stone.testapplication")
        )
    }
}

