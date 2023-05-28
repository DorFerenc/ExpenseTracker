package com.example.demo.model

import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javax.json.JsonObject

/**
 * Represents a UserId object.
 */
class UserId: JsonModel {
    val superappProperty = SimpleStringProperty()
    var superapp: String? by superappProperty

    val emailProperty = SimpleStringProperty()
    var email: String? by emailProperty


    /**
     * Updates the UserId model with data from a JsonObject.
     * @param json The JsonObject containing city data.
     */
    override fun updateModel(json: JsonObject) {
        with(json) {
            superapp = getString("superapp")
            email = getString("email")
        }
    }

    override fun toString() = email!!
}


/**
 * View model for UserId class.
 */
class UserIdModel : ItemViewModel<UserId>() {
    val superapp = bind(UserId::superappProperty)
    val email =  bind(UserId::emailProperty)
}