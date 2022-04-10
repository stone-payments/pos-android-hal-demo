package br.com.stone.posandroid.hal.demo.settings

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import br.com.stone.posandroid.hal.api.Properties
import br.com.stone.posandroid.hal.api.settings.CpuInfo
import br.com.stone.posandroid.hal.api.settings.DeviceInfo
import br.com.stone.posandroid.hal.demo.HALConfig
import com.google.gson.Gson
import com.google.gson.JsonParser.parseString
import java.io.InputStreamReader
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("TestFunctionName")
@RunWith(AndroidJUnit4ClassRunner::class)
class DeviceInfoTest {

    private val stubResultsFolder = "settings/settings-test"
    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }
    private val versionRegex = Regex("^(?:(\\d+\\.){1,3}\\d{1,3}|\\d{4,16})\$")

    private val deviceInfo by lazy {
        HALConfig.deviceProvider.getSettings(
            mapOf(
                Properties.RESULTS_FILE_KEY to "$stubResultsFolder/settings-deviceinfo.json",
                Properties.KEY_CONTEXT to context
            )
        ).retrieveDeviceInfo()
    }

    private val expectedDeviceInfo: DeviceInfo? by lazy {
        val gson = Gson()
        val rootElement = parseString(getFileContent("$stubResultsFolder/expected-deviceinfo.json"))
        gson.fromJson(
            rootElement.asJsonObject.get(deviceInfo.manufacturerModel),
            DeviceInfo::class.java
        )
    }

    @Test
    fun serialNumber() {
        val serialNumber = deviceInfo.serialNumber
        assertFalse(serialNumber.isEmpty())
    }

    @Test
    fun isPosAndroid() {
        val isPosAndroid = deviceInfo.isPosAndroid
        assertEquals(expectedDeviceInfo?.isPosAndroid, isPosAndroid)
    }

    @Test
    fun manufacturerModel() {
        val manufacturerModel = deviceInfo.manufacturerModel
        val supportedDevices = listOf(
            "Positivo L3",
            "SUNMI P2-B",
            "Ingenico APOS A8",
            "Stone Mock Device"
        )
        assertTrue(manufacturerModel in supportedDevices)
    }

    @Test
    fun osBuildNumber() {
        val osBuildNumber = deviceInfo.osBuildNumber
        assertFalse("osBuildNumber is empty", osBuildNumber.isEmpty())
        assertTrue("osBuildNumber doesn't match the expected pattern", osBuildNumber.matches(versionRegex))
    }

    @Test
    fun kernelVersion() {
        val kernelVersion = deviceInfo.kernelVersion
        assertFalse("kernelVersion is null or empty", kernelVersion.isNullOrEmpty())
        assertNotEquals("kernelVersion returned KERNEL_UNKNOWN", DeviceInfo.KERNEL_UNKNOWN, kernelVersion)
        assertTrue("kernelVersion doesn't match the expected pattern", kernelVersion!!.matches(versionRegex))
    }

    @Test
    fun customResourceVersion() {
        val customResourceVersion = deviceInfo.customResourceVersion
        assertFalse("customResourceVersion is or empty", customResourceVersion.isEmpty())
        if (expectedDeviceInfo?.customResourceVersion != DeviceInfo.CUSTOM_RESOURCE_UNKNOWN) {
            assertNotEquals("customResourceVersion returned CUSTOM_RESOURCE_UNKNOWN", DeviceInfo.CUSTOM_RESOURCE_UNKNOWN, customResourceVersion)
            assertTrue("customResourceVersion doesn't match the expected pattern", customResourceVersion.matches(versionRegex))
        }
    }

    @Test
    fun cpuUsage() {
        val cpuUsage = deviceInfo.cpuInfo.getCpuUsage()
        assertTrue("cpuUsage is less than zero", cpuUsage > 0.0)
        assertNotEquals("getCpuUsage returned UNKNOWN_VALUE", CpuInfo.UNKNOWN_VALUE, cpuUsage)
    }

    @Test
    fun cpuTemperature() {
        val cpuTemperature = deviceInfo.cpuInfo.getCpuTemperature()
        assertNotEquals(CpuInfo.UNKNOWN_VALUE, cpuTemperature)
        assertNotEquals("getCpuTemperature returned UNKNOWN_VALUE", CpuInfo.UNKNOWN_VALUE, cpuTemperature)
    }

    @Test
    fun iccReaderPosition() {
        assertEquals(expectedDeviceInfo?.iccReaderPosition, deviceInfo.iccReaderPosition)
    }

    @Test
    fun magReaderPosition() {
        assertEquals(expectedDeviceInfo?.magReaderPosition, deviceInfo.magReaderPosition)
    }

    @Test
    fun nfcReaderPosition() {
        assertEquals(expectedDeviceInfo?.nfcReaderPosition, deviceInfo.nfcReaderPosition)
    }

    @Test
    fun keyBoardType() {
        assertEquals(expectedDeviceInfo?.keyboardType, deviceInfo.keyboardType)
    }


    // Criar arquivo ReadFile no hal-mock
    private fun getFileContent(path: String): String {
        return InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(path)).readText()
    }

}