package br.com.stone.posandroid.hal.demo

import br.com.stone.posandroid.hal.api.provider.AutoProvider
import br.com.stone.posandroid.hal.api.provider.DeviceProvider
import br.com.stone.posandroid.hal.mock.provider.MockDeviceProvider
import br.com.stone.posandroid.hal.sunmi.provider.StoneDeviceProvider

object HALConfig {
    val deviceProvider: DeviceProvider = StoneDeviceProvider()
    val runningOnEmulator = deviceProvider is MockDeviceProvider
}
