package br.com.stone.posandroid.hal.demo.installer

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import br.com.stone.posandroid.hal.api.Properties.KEY_CONTEXT
import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.demo.HALConfig
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class InstallerTest {

    private val stubResultsFolder = "installer/installer-test"
    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }

    @Test
    fun installSuccess() {
        val subject = HALConfig.deviceProvider.getInstaller(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/installer-install.json",
                KEY_CONTEXT to context
            )
        )

        Assert.assertEquals(
            0,
            subject.install("/sdcard/stone/test-application.apk")
        )
    }

    @Test
    fun uninstallSuccess() {
        val subject = HALConfig.deviceProvider.getInstaller(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/installer-uninstall.json",
                KEY_CONTEXT to context
            )
        )

        Assert.assertEquals(
            0,
            subject.uninstallApk("br.com.stone.testapplication")
        )
    }
}

