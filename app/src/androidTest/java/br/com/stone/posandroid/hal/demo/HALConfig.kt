package br.com.stone.posandroid.hal.demo

import br.com.stone.posandroid.hal.api.provider.DeviceProvider
import br.com.stone.posandroid.hal.gertec.provider.GertecDeviceProvider
import br.com.stone.posandroid.hal.mock.provider.MockDependencyProvider

object HALConfig {
    // TODO change to your Provider
//    val deviceProvider: DeviceProvider = MockDependencyProvider()
    val deviceProvider: DeviceProvider = GertecDeviceProvider()

    val runningOnEmulator = deviceProvider is MockDependencyProvider
}