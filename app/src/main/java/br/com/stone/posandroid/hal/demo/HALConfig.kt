package br.com.stone.posandroid.hal.demo

import br.com.stone.posandroid.hal.api.provider.AutoProvider
import br.com.stone.posandroid.hal.api.provider.DeviceProvider
import br.com.stone.posandroid.hal.mock.provider.MockProvider

object HALConfig {
    val deviceProvider: DeviceProvider = AutoProvider.provider
    val runningOnEmulator = deviceProvider is MockProvider
}