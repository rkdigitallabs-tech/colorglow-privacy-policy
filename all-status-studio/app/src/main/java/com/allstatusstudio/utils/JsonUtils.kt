package com.allstatusstudio.utils

import android.content.Context
import com.allstatusstudio.data.models.CaptionModel
import com.allstatusstudio.data.models.TemplateModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

object JsonUtils {

    fun loadCaptions(context: Context): Map<String, List<String>> {
        return try {
            val inputStream = context.assets.open("captions.json")
            val reader = InputStreamReader(inputStream)
            val json = reader.readText()
            reader.close()

            val type = object : TypeToken<CaptionsData>() {}.type
            val data: CaptionsData = Gson().fromJson(json, type)
            data.categories
        } catch (e: Exception) {
            e.printStackTrace()
            emptyMap()
        }
    }

    fun loadTemplates(context: Context): List<TemplateModel> {
        return try {
            val inputStream = context.assets.open("templates.json")
            val reader = InputStreamReader(inputStream)
            val json = reader.readText()
            reader.close()

            val type = object : TypeToken<TemplatesData>() {}.type
            val data: TemplatesData = Gson().fromJson(json, type)
            data.templates
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun loadDailyPacks(context: Context): Map<String, DailyPack> {
        return try {
            val inputStream = context.assets.open("daily_packs.json")
            val reader = InputStreamReader(inputStream)
            val json = reader.readText()
            reader.close()

            val type = object : TypeToken<DailyPacksData>() {}.type
            val data: DailyPacksData = Gson().fromJson(json, type)
            data.days
        } catch (e: Exception) {
            e.printStackTrace()
            emptyMap()
        }
    }

    data class CaptionsData(val categories: Map<String, List<String>>)
    data class TemplatesData(val templates: List<TemplateModel>)
    data class DailyPacksData(val days: Map<String, DailyPack>)
    data class DailyPack(val caption: String, val templateId: Int)
}
