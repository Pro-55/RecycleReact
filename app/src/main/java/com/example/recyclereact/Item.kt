package com.example.recyclereact

data class Item(val _id:String,val type: Int, val img: Int) {

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false
        other as Item

        if (_id != other._id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _id.hashCode()
        result = 31 * result + type
        result = 31 * result + img
        return result
    }

}