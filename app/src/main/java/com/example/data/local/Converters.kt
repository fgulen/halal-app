package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.AppLanguage
import com.example.data.model.HalalStatus
import com.example.data.model.Madhhab

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return value?.joinToString("|||") ?: ""
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        return value.split("|||").filter { it.isNotBlank() }
    }

    @TypeConverter
    fun fromHalalStatus(status: HalalStatus): String {
        return status.name
    }

    @TypeConverter
    fun toHalalStatus(value: String): HalalStatus {
        return try {
            HalalStatus.valueOf(value)
        } catch (e: Exception) {
            HalalStatus.BULUNAMADI
        }
    }

    @TypeConverter
    fun fromMadhhab(madhhab: Madhhab): String {
        return madhhab.name
    }

    @TypeConverter
    fun toMadhhab(value: String): Madhhab {
        return try {
            Madhhab.valueOf(value)
        } catch (e: Exception) {
            Madhhab.UNSPECIFIED
        }
    }

    @TypeConverter
    fun fromAppLanguage(language: AppLanguage): String {
        return language.name
    }

    @TypeConverter
    fun toAppLanguage(value: String): AppLanguage {
        return try {
            AppLanguage.valueOf(value)
        } catch (e: Exception) {
            AppLanguage.EN
        }
    }
}
