package com.example.demo.model

import javafx.beans.property.SimpleStringProperty
import tornadofx.ItemViewModel
import tornadofx.JsonModel
import tornadofx.getValue
import tornadofx.setValue
import javax.json.JsonObject

class ObjectId: JsonModel {
    val superappProperty = SimpleStringProperty()
    var superapp: String? by superappProperty

    val internalObjectIdProperty = SimpleStringProperty()
    var internalObjectId: String? by internalObjectIdProperty


    /**
     * Updates the UserId model with data from a JsonObject.
     * @param json The JsonObject containing city data.
     */
    override fun updateModel(json: JsonObject) {
        with(json) {
            superapp = getString("superapp")
            internalObjectId = getString("internalObjectId")
        }
    }
}


/**
 * View model for UserId class.
 */
class ObjectIdModel : ItemViewModel<ObjectId>() {
    val superapp = bind(ObjectId::superappProperty)
    val objectId =  bind(ObjectId::internalObjectIdProperty)
}