package com.example.demo.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.time.LocalDate
import java.time.OffsetDateTime
import javax.json.JsonObject

class ObjectItems: JsonModel {

    val objectIdProperty = SimpleObjectProperty<ObjectId>()
    var objectId: ObjectId by objectIdProperty

    val typeProperty = SimpleStringProperty()
    var type: String? by typeProperty

    val aliasProperty = SimpleStringProperty()
    var alias: String? by aliasProperty

    val activeProperty = SimpleBooleanProperty()
    var active: Boolean by activeProperty

    val creationTimestampProperty = SimpleStringProperty()
    var creationTimestamp by creationTimestampProperty

    val locationProperty = SimpleObjectProperty<Location>()
    var location: Location by locationProperty

    val createdByProperty = SimpleObjectProperty<CreatedBy>()
    var createdBy by createdByProperty

    val objectDetailsProperty = SimpleObjectProperty<ExpensesEntryJson>()
    var objectDetails by objectDetailsProperty

    override fun updateModel(json: JsonObject) {
        with(json) {
            objectId = getJsonObject("objectId").toModel()
            type = getString("type")
            alias = getString("alias")
            active = getBoolean("active")
//            val creationTimestampString = getString("creationTimestamp")
//            creationTimestamp = OffsetDateTime.parse(creationTimestampString).toString()
            creationTimestamp = getString("creationTimestamp")
            location = getJsonObject("location").toModel()
            createdBy = getJsonObject("createdBy").toModel()
            objectDetails = getJsonObject("objectDetails").toModel()
        }
    }
}