package br.com.stone.posandroid.updateso.data

import android.content.Context
import org.slf4j.LoggerFactory

class UpdatedSORepository(private val context: Context) {

    object DataSource {

        private const val SHARED_PREFS_NAME = "updated_so"


        fun isUpdated(context: Context): Boolean =
            context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE).getBoolean("UPDATED_SO", false)


        fun save(context: Context, updated: Boolean) {
            context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE).edit().apply {
                putBoolean("UPDATED_SO", updated)
            }.apply()
        }

    }


    val isUpdated = DataSource.isUpdated(context).also {
        logger.info("isUpdated = $it")
    }

    fun save() {
        logger.info("saving updated")
        DataSource.save(context,true)
    }

    companion object {
        private val logger = LoggerFactory.getLogger("UpdatedSoRepository")
    }

}