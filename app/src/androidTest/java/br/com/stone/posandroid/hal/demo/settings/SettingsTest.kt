package br.com.stone.posandroid.hal.demo.settings

import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import br.com.stone.posandroid.hal.api.Properties.KEY_CONTEXT
import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.settings.TimeData
import br.com.stone.posandroid.hal.demo.HALConfig
import br.com.stone.posandroid.hal.demo.util.isPackageInstalled
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.InputStream
import java.util.Date
import java.util.TimeZone


@RunWith(AndroidJUnit4ClassRunner::class)
class SettingsTest {

    private val stubResultsFolder = "resources/settings/settings-test"
    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }
    private val runningOnEmulator: Boolean by lazy {
        HALConfig.runningOnEmulator
    }


    @Test
    fun getSelfExamTime_should_return_time(){
        val subject = HALConfig.deviceProvider.getSettings(mapOf(KEY_CONTEXT to context))
        assertNotNull(subject.getSelfExamTime())
    }

    @Test
    @Ignore("The test may reboot the device.")
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
        assertTrue(subject.disableKeyCodeMenu())
    }

    @Test
    fun retrieveDeviceInfo() {
        val subject = HALConfig.deviceProvider.getSettings(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/settings-deviceinfo.json",
                KEY_CONTEXT to context
            )
        )
        
        try {
            val deviceInfo = subject.retrieveDeviceInfo()
        } catch (t: Throwable) {
            Log.e(TAG, "retrieveDeviceInfo: ", t)
            fail(t.localizedMessage)
        }
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

    @Test
    fun intentToSelfTestApp() {
        val subject = HALConfig.deviceProvider.getSettings(mapOf(KEY_CONTEXT to context))
        val intent = subject.intentToSelfTestApp()
        assertFalse(intent?.`package`.isNullOrEmpty())
    }

    @Test
    fun setSystemTime() {
        val subject = HALConfig.deviceProvider.getSettings(mapOf(KEY_CONTEXT to context))
        val oneHour = 60 * 60 * 1000
        val oneMinute = 60 * 1000
        val currentTime = Date().time
        val expectedTime = currentTime + oneHour
        subject.setSystemTime(Date().time + oneHour)

        val expectedTimeRange = expectedTime..(expectedTime + oneMinute)
        assertTrue(expectedTimeRange.contains(Date().time))
        subject.setSystemTime(Date().time - oneHour)
    }

    @Test
    fun setTimeZone() {
        val subject = HALConfig.deviceProvider.getSettings(mapOf(KEY_CONTEXT to context))
        var timeZone = TimeZone.getTimeZone("GMT-5")
        assertTrue(subject.setTimeZone(timeZone))
        assertEquals(timeZone.id, TimeZone.getDefault().id)

        timeZone = TimeZone.getTimeZone("GMT-3")
        assertTrue(subject.setTimeZone(timeZone))
        assertEquals(timeZone.id, TimeZone.getDefault().id)
    }

    @Test
    fun surpassPermissionsRequest() {
        if (Build.VERSION.SDK_INT > 22) {

            val pm = context.packageManager
            if (isPackageInstalled(pm, PERMISSION_PACKAGE_NAME_APK).not()) {
                assertEquals(
                    "Setup Permission Test App failed",
                    0,
                    setupPermissionTestApplication()
                )
            }
            val subject = HALConfig.deviceProvider.getSettings(mapOf(KEY_CONTEXT to context))

            subject.surpassPermissionsRequest(PERMISSION_PACKAGE_NAME_APK)

            val requestedPermission = pm.getPackageInfo(
                PERMISSION_PACKAGE_NAME_APK,
                PackageManager.GET_PERMISSIONS
            ).requestedPermissions
            for (permission in requestedPermission) {
                assertEquals(
                    PackageManager.PERMISSION_GRANTED,
                    pm.checkPermission(permission, PERMISSION_PACKAGE_NAME_APK)
                )
            }

            if (isPackageInstalled(pm, PERMISSION_PACKAGE_NAME_APK)) {
                val installer = HALConfig.deviceProvider.getInstaller(mapOf(KEY_CONTEXT to context))
                installer.uninstallApk(PERMISSION_PACKAGE_NAME_APK)
            }
        }
    }

    private fun setupPermissionTestApplication(): Int {
        var pathFile: String? = null
        if (!runningOnEmulator) {
            File("/sdcard/stone/").run {
                if (!this.exists()) {
                    this.mkdir()
                }
                File(this, "Test-Application-Permission.apk").also { apk ->
                    if (!apk.exists()) {
                        val classloader = Thread.currentThread().contextClassLoader
                        requireNotNull(classloader)
                        val stream: InputStream = classloader.getResourceAsStream(
                            "$stubResultsFolder/Test-Application-Permission.apk".removePrefix("resources/")
                        )
                        apk.writeBytes(stream.readBytes())

                    }
                    pathFile = apk.absolutePath
                }
            }
        }
        val installer = HALConfig.deviceProvider.getInstaller(
            mapOf(
                KEY_CONTEXT to context
            )
        )

        pathFile?.let {
            return installer.install(it)
        }
        return -1
    }

    @Test
    @Ignore("Not running the reboot test")
    fun reboot() {
        val subject = HALConfig.deviceProvider.getSettings(
            mapOf(
                KEY_CONTEXT to context
            )
        )

        subject.reboot()
    }

    companion object {
        const val TAG = "SettingsTest"
        private const val PERMISSION_PACKAGE_NAME_APK = "com.example.testapplicationpermission"
    }
}