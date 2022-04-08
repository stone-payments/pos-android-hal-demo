package br.com.stone.posandroid.hal.demo

import br.com.stone.posandroid.hal.api.provider.AutoProvider
import br.com.stone.posandroid.hal.api.provider.DeviceProvider
import br.com.stone.posandroid.hal.mock.provider.MockDeviceProvider

object HALConfig {
    val deviceProvider: DeviceProvider = MockDeviceProvider()
    val runningOnEmulator = deviceProvider is MockDeviceProvider
}
