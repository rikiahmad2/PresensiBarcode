package com.riki.realtimedatabase

class Login {
    var username = ""
    var password = ""

    constructor(user : String, pass : String){
        this.username = user
        this.password = pass
    }
}

class InputForm{
    var nama = ""
    var nik = ""
    var nomorhp = ""
    var email = ""
    var perusahaan = ""
    var username = ""

    constructor(nama : String, nik : String, nohp : String, mail : String, perus : String, user: String){
        this.nama = nama
        this.nik = nik
        this.nomorhp = nohp
        this.email = mail
        this.perusahaan = perus
        this.username = user
    }
}