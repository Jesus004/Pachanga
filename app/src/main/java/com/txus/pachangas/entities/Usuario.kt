package com.txus.pachangas.entities

import java.util.*

data class Usuario (
    val nombre:String,
    val email:String,
    val edad:Int,
    var imageUrl:String,
    val partGanadas:Int,
    val createAt:Date,
    val updateAt:Date){

    constructor():this(

        "","",0,"",0, Date(), Date()
    )

    override fun toString(): String {
        return "Usuario(nombre='$nombre', email='$email', edad=$edad, imageUrl='$imageUrl', partGanadas=$partGanadas, createAt=$createAt, updateAt=$updateAt)"
    }


}


