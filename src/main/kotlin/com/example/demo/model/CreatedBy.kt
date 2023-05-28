package com.example.demo.model

import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import javax.json.JsonObject

class CreatedBy: JsonModel {
    val userIdProperty = SimpleObjectProperty<UserId>()
    var userId: UserId by userIdProperty

    override fun updateModel(json: JsonObject) {
        with(json) {
            userId = getJsonObject("userId").toModel()
        }
    }
}

/**
 * View model for CreatedBy class.
 */
class CreatedByModel : ItemViewModel<CreatedBy>() {
    val userId = bind(CreatedBy::userIdProperty)
}