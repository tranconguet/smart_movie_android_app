package com.congtv5.data.response.castandcrewlist

import com.google.gson.annotations.SerializedName

data class CastAndCrewResponse(
    @field:SerializedName("cast") val cast: List<CastResponse>?,
    @field:SerializedName("crew") val crew: List<CrewResponse>?,
    @field:SerializedName("id") val id: Int?
)

data class CastResponse(
    @field:SerializedName("adult") val adult: Boolean?,
    @field:SerializedName("cast_id") val castId: Int?,
    @field:SerializedName("character") val character: String?,
    @field:SerializedName("credit_id") val creditId: String?,
    @field:SerializedName("gender") val gender: Int?,
    @field:SerializedName("id") val id: Int?,
    @field:SerializedName("known_for_department") val knownForDepartment: String?,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("order") val order: Int?,
    @field:SerializedName("original_name") val originalName: String?,
    @field:SerializedName("popularity") val popularity: Double?,
    @field:SerializedName("profile_path") val profilePath: String?
)

data class CrewResponse(
    @field:SerializedName("adult") val adult: Boolean?,
    @field:SerializedName("credit_id") val creditId: String?,
    @field:SerializedName("department") val department: String?,
    @field:SerializedName("gender") val gender: Int?,
    @field:SerializedName("id") val id: Int?,
    @field:SerializedName("job") val job: String?,
    @field:SerializedName("known_for_department") val knownForDepartment: String?,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("original_name") val originalName: String?,
    @field:SerializedName("popularity") val popularity: Double?,
    @field:SerializedName("profile_path") val profilePath: String?
)