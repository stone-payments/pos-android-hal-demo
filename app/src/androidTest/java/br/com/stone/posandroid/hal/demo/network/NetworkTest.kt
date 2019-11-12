package br.com.stone.posandroid.hal.demo.network

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.network.ApnInfo
import br.com.stone.posandroid.hal.demo.HALConfig
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class NetworkTest {

    private val stubResultsFolder = "network/network-test"

    @Test
    fun configureApn() {
        val subject = HALConfig.deviceProvider.getNetwork(
            mapOf(RESULTS_FILE_KEY to "$stubResultsFolder/network-configureapn.json")
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
            mapOf(RESULTS_FILE_KEY to "$stubResultsFolder/network-toggle-mobile.json")
        )

        Assert.assertTrue(subject.enableMobileNetwork(false))
        Assert.assertTrue(subject.enableMobileNetwork(true))
    }
}

