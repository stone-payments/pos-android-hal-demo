package br.com.stone.posandroid.updateso.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import br.com.stone.posandroid.updateso.services.UpdateSOServices
import org.slf4j.LoggerFactory

class UpdateSOBroadCast : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        logger.info("onReceive()")
        requireNotNull(context)
        val request = OneTimeWorkRequestBuilder<UpdateSOServices>()
            .build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork("UpdateSO", ExistingWorkPolicy.KEEP, request)

    }

    companion object {
        private val logger = LoggerFactory.getLogger("UpdateSOBroadCast")
    }
}


