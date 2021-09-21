package br.com.stone.posandroid.hal.demo.installer

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import br.com.stone.posandroid.hal.api.Properties.KEY_CONTEXT
import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.demo.HALConfig
import br.com.stone.posandroid.hal.demo.util.isPackageInstalled
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import java.io.File
import java.io.InputStream

@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class InstallerTest {

    private val stubResultsFolder = "resources/installer/installer-test"
    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }
    private lateinit var pathFile: String
    private val runningOnEmulator: Boolean by lazy {
        HALConfig.runningOnEmulator
    }

    @Before
    fun setup() {
        if (!runningOnEmulator) {
            File("/sdcard/stone/").run {
                if (!this.exists()) {
                    this.mkdir()
                }
                File(this, "Test-Application.apk").also { apk ->
                    if (!apk.exists()) {
                        val classloader = Thread.currentThread().contextClassLoader
                        requireNotNull(classloader)
                        val stream: InputStream = classloader.getResourceAsStream(
                            "$stubResultsFolder/Test-Application.apk".removePrefix("resources/")
                        )
                        apk.writeBytes(stream.readBytes())

                    }
                    pathFile = apk.absolutePath
                }
            }
        }
    }

    @After
    fun tearDown() {
        if (!runningOnEmulator) {
            File(pathFile).run {
                if (this.exists()) {
                    this.deleteRecursively()
                }
            }
        }
    }

    @Test
    fun installSuccess() {
        val subject = HALConfig.deviceProvider.getInstaller(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/installer-install.json",
                KEY_CONTEXT to context
            )
        )

        assertEquals(
            0,
            subject.install(if (::pathFile.isInitialized) pathFile else "Running on Emulator")
        )

        if (!runningOnEmulator) {
            assertTrue(isPackageInstalled(context.packageManager, PACKAGE_NAME_APK))
        }
    }

    @Test
    fun uninstallSuccess() {
        val subject = HALConfig.deviceProvider.getInstaller(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/installer-uninstall.json",
                KEY_CONTEXT to context
            )
        )

        assertEquals(
            0,
            subject.uninstallApk(PACKAGE_NAME_APK)
        )

        if (!runningOnEmulator) {
            assertFalse(isPackageInstalled(context.packageManager, PACKAGE_NAME_APK))
        }
    }


    companion object {
        private const val PACKAGE_NAME_APK = "br.com.stone.testapplication"
    }
}

