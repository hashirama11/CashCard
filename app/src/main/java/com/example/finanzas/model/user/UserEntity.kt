package com.example.finanzas.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "user")
class UserEntity {
    @PrimaryKey
    var id: Int = 1
    var name: String = ""
    var email: String = ""
    var password: String = ""
    var year : LocalDate = LocalDate.now()

}