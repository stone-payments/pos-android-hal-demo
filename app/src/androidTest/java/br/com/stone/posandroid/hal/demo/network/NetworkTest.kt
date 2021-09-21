package br.com.stone.posandroid.hal.demo.network

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import br.com.stone.posandroid.hal.api.Properties.KEY_CONTEXT
import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.network.ApnInfo
import br.com.stone.posandroid.hal.demo.HALConfig
import br.com.stone.posandroid.hal.demo.rule.Precondition
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import java.net.InetSocketAddress

@RunWith(AndroidJUnit4ClassRunner::class)
class NetworkTest {

    private val stubResultsFolder = "resources/network/network-test"
    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }

    @Test
    @Precondition("Insert SIM Card")
    fun configureApn() {
        val subject = HALConfig.deviceProvider.getNetwork(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/network-configureapn.json",
                KEY_CONTEXT to context
            )
        )

        Assert.assertTrue(
            subject.configureApn(
                ApnInfo("Vivo", "stone.vivo.com.br", "stone", "stone")
            )
        )
    }

    @Test
    fun toggleMobileNetwork() {
        val subject = HALConfig.deviceProvider.getNetwork(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/network-toggle-mobile.json",
                KEY_CONTEXT to context
            )
        )

        Assert.assertTrue(subject.enableMobileNetwork(false))
        Assert.assertTrue(subject.enableMobileNetwork(true))
    }

    @Test
    fun configureGlobalProxy() {
        val subject = HALConfig.deviceProvider.getNetwork(mapOf(
            RESULTS_FILE_KEY to "$stubResultsFolder/network-toggle-mobile.json",
            KEY_CONTEXT to context
        ))
        val inetSocketAddress = InetSocketAddress(1111)
        Assert.assertTrue(subject.configureGlobalProxy(inetSocketAddress))
    }

    @Test
    fun getNetworkInfo() {
        val subject = HALConfig.deviceProvider.getNetwork(mapOf(
            RESULTS_FILE_KEY to "$stubResultsFolder/network-toggle-mobile.json",
            KEY_CONTEXT to context
        ))

        val networkInfo = subject.getNetworkInfo()
        Assert.assertFalse(networkInfo.apnName.isEmpty())
        Assert.assertFalse(networkInfo.carrierName.isEmpty())
        Assert.assertFalse(networkInfo.simSerialNumber.isEmpty())
    }
}

