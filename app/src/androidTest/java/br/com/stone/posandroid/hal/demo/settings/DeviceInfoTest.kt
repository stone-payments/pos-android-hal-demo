package br.com.stone.posandroid.hal.demo.settings

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import br.com.stone.posandroid.hal.api.Properties
import br.com.stone.posandroid.hal.api.settings.CpuInfo
import br.com.stone.posandroid.hal.api.settings.DeviceInfo
import br.com.stone.posandroid.hal.demo.HALConfig
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("TestFunctionName")
@RunWith(AndroidJUnit4ClassRunner::class)
class DeviceInfoTest {

    private val stubResultsFolder = "resources/settings/settings-test"
    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }

    private val deviceInfo by lazy {
        HALConfig.deviceProvider.getSettings(
            mapOf(
                Properties.RESULTS_FILE_KEY to "$stubResultsFolder/settings-deviceinfo.json",
                Properties.KEY_CONTEXT to context
            )
        ).getDeviceInfo()
    }

    @Test
    fun When_accessed_the_kernel_version_Should_return_a_valid_value() {
        val kernelVersion = deviceInfo.kernelVersion
        val result = kernelVersion.isEmpty().not() && kernelVersion != DeviceInfo.KERNEL_UNKNOWN
        Assert.assertEquals(true, result)
    }


    @Test
    fun When_accessed_usage_cpu_Should_return_a_valid_value() {
        val cpuUsage = deviceInfo.cpuInfo.getCpuUsage()
        val result = cpuUsage != CpuInfo.UNKNOWN_VALUE
        Assert.assertEquals(true, result)
    }

    @Test
    fun When_accessed_temperature_cpu_Should_return_a_valid_value(){
        val temp = deviceInfo.cpuInfo.getCpuTemperature()
        val result = temp != CpuInfo.UNKNOWN_VALUE
        Assert.assertEquals(true,result)
    }

}