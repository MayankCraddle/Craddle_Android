package com.cradle.model
import java.io.Serializable
import java.util.ArrayList

data class MajorExport(
    val countryId : Int,
    val sortname : String,
    val name : String,
    val phonecode : Int):Serializable
data class MajorExportList (
    val countries : ArrayList<MajorExport>
):Serializable
