package br.com.stone.posandroid.hal.demo.util

import br.com.stone.posandroid.hal.api.bc.PinpadCallbacks
import io.mockk.mockk

object PinpadCallbackComponent {
    private lateinit var callback: PinpadCallbacks
    fun init(): PinpadCallbacks {
        if (!::callback.isInitialized) {
            callback = mockk(relaxed = true)
        }
        return callback
    }
}