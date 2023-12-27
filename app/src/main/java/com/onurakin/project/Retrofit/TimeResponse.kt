package com.onurakin.project.Retrofit

import com.google.gson.annotations.SerializedName

data class TimeResponse(
    @SerializedName("abbreviation") val abbreviation: String,
    @SerializedName("client_ip") val clientIp: String,
    @SerializedName("datetime") val datetime: String,
    @SerializedName("day_of_week") val dayOfWeek: Int,
    @SerializedName("day_of_year") val dayOfYear: Int,
    @SerializedName("dst") val dst: Boolean,
    @SerializedName("dst_from") val dstFrom: String?,
    @SerializedName("dst_offset") val dstOffset: Int,
    @SerializedName("dst_until") val dstUntil: String?,
    @SerializedName("raw_offset") val rawOffset: Int,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("unixtime") val unixtime: Long,
    @SerializedName("utc_datetime") val utcDatetime: String,
    @SerializedName("utc_offset") val utcOffset: String,
    @SerializedName("week_number") val weekNumber: Int
)
