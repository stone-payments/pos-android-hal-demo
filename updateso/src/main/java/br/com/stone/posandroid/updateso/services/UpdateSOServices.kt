package br.com.stone.posandroid.updateso.services

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import br.com.stone.posandroid.hal.api.Properties
import br.com.stone.posandroid.hal.api.installer.Installer
import br.com.stone.posandroid.hal.api.provider.AutoProvider
import br.com.stone.posandroid.updateso.R
import br.com.stone.posandroid.updateso.data.UpdatedSORepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.logging.Logger


class UpdateSOServices(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val repository = UpdatedSORepository(applicationContext)
            if (repository.isUpdated) {
                logger.info("update has already been performed")
                return@withContext Result.success()
            }

            surpassPermision(applicationContext)


            val installer: Deferred<Installer> = async {
                logger.info("getInstaller()")
                AutoProvider.provider.getInstaller(
                    mapOf(Properties.KEY_CONTEXT to applicationContext)
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
                            val stream = applicationContext.resources.openRawResource(R.raw.custom_tz_data)
                            apk.writeBytes(stream.readBytes())
                        }
                    }
                }
            }

            val file = apkFile.await()
            logger.info("installing pkg ${file.name}...")
            val result = installer.await().install(file.absolutePath)

            if (result == SUCCESS) {
                logger.info("successful installation. ")
                val removePkgJob = launch {
                    removePkg(file)
                }

                val saveUpdatedJob = launch {
                    saveUpdated(repository)
                }

                val disableUpdateJob = launch {
                    disableBroadcast(applicationContext)
                }

                removePkgJob.invokeOnCompletion {
                    it?.let {
                        logger.throwing(this::class.simpleName, "disableBroadcast()", it)
                    } ?: logger.info("pkg removed successfully")
                }
                saveUpdatedJob.invokeOnCompletion {
                    it?.let {
                        logger.throwing(this::class.simpleName, "saveUpdated()", it)
                    } ?: logger.info("update performed successfully")
                }

                disableUpdateJob.invokeOnCompletion {
                    it?.let {
                        logger.throwing(this::class.simpleName, "disableBroadcast()", it)
                    } ?: logger.info("Broadcast disabled successfully")
                }

                removePkgJob.join()
                saveUpdatedJob.join()
                disableUpdateJob.join()

                Result.success()
            }
            else {

                logger.throwing("UpdateSOServices", "doWork", InstallationException(result))

                val data = Data.Builder()
                    .putInt("Result fail code", result)
                    .build()

                return@withContext Result.failure(data)

            }
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
        private val logger = Logger.getLogger("UpdateSOServices")
        const val SUCCESS = 0
    }

    inner class InstallationException(private val code: Int) : Exception() {
        override val message: String
            get() = "Error installing PKG code = $code."
    }
}