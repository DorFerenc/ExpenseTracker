package com.example.demo.model

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javax.json.JsonObject

class UserModel: JsonModel {
    val userIdProperty = SimpleObjectProperty<UserId>()
    var userId: UserId by userIdProperty

    val roleProperty = SimpleStringProperty()
    var role: String? by roleProperty

    val usernameProperty = SimpleStringProperty()
    var username: String? by usernameProperty

    val avatarProperty = SimpleStringProperty()
    var avatar: String?  by avatarProperty


    override fun updateModel(json: JsonObject) {
        with(json) {
            userId = getJsonObject("userId").toModel()
            role = getString("role")
            username = getString("username")
            avatar = getString("avatar")
        }
    }
}