package br.com.stone.posandroid.updateso.broadcast

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import br.com.stone.posandroid.hal.api.Properties
import br.com.stone.posandroid.hal.api.installer.Installer
import br.com.stone.posandroid.hal.api.provider.AutoProvider
import br.com.stone.posandroid.hal.demo.R
import br.com.stone.posandroid.updateso.data.UpdatedSORepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.util.logging.Logger

class UpdateSOBroadCast : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        logger.info("onReceive()")
        CoroutineScope(Dispatchers.IO).launch {
            requireNotNull(context)
            val repository = UpdatedSORepository(context)
            if (repository.isUpdated) {
                logger.info("update has already been performed")
                return@launch
            }


            surpassPermision(context)


            val installer: Deferred<Installer> = async {
                logger.info("getInstaller()")
                AutoProvider.provider.getInstaller(
                    mapOf(Properties.KEY_CONTEXT to context)
                ).also {
                    logger.info("getInstaller")
                }
            }

            val apkFile: Deferred<File> = async {
                File(Environment.getExternalStorageDirectory(), "/stone/").apply {
                    if (!exists()) {
                        mkdir()
                    }

                    return@async File(this, "Custom-TZ-Data-1.0.pkg").also { apk ->
                        if (!apk.exists()) {
                            val stream = context.resources.openRawResource(R.raw.custom_tz_data)
                            apk.writeBytes(stream.readBytes())
                        }
                    }
                }
            }


            val result = installer.await().install(apkFile.await().absolutePath)
            logger.info("installing pkg...")

            if (result == SUCCESS) {
                logger.info("successful installation. ")
                launch {
                    removePkg(apkFile.await())
                }.invokeOnCompletion {
                    it?.let {
                        logger.throwing(this::class.simpleName, "disableBroadcast()", it)
                    } ?: logger.info("pkg removed successfully")
                }

                launch {
                    saveUpdated(repository)
                }.invokeOnCompletion {
                    it?.let {
                        logger.throwing(this::class.simpleName, "saveUpdated()", it)
                    } ?: logger.info("update performed successfully")
                }

                launch {
                    disableBroadcast(context)
                }.invokeOnCompletion {
                    it?.let {
                        logger.throwing(this::class.simpleName, "disableBroadcast()", it)
                    } ?: logger.info("Broadcast disabled successfully")
                }
            } else {
                throw InstallationException(result)
            }

        }
            .invokeOnCompletion {
                it?.let {
                    logger.throwing("UpdateSOBroadcast", "updateSo", it)
                }?: logger.info("update ")
            }
    }

    private fun surpassPermision(context: Context) {
        AutoProvider.provider.getSettings(
            mapOf(Properties.KEY_CONTEXT to context)
        ).surpassPermissionsRequest(context.packageName)
    }


    private fun removePkg(pkg: File) {
        logger.info("Remove PKG")
        if (pkg.exists()) {
            pkg.delete()
        }
    }

    private fun saveUpdated(repository: UpdatedSORepository) {
        logger.info("Persist info in shared preferences")
        repository.save()
    }

    private fun disableBroadcast(context: Context) {
        logger.info("disable broadcast")
        val pm = context.packageManager
        val componentName = ComponentName(context, this::class.java)
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
    }

    companion object {
        private val logger = Logger.getLogger("UpdateSOBroadCast")
        const val SUCCESS = 0
    }

    inner class InstallationException(private val code: Int) : Exception() {
        override val message: String
            get() = "Error installing PKG code = $code."
    }
}
