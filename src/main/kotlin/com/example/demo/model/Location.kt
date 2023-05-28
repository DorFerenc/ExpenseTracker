package com.example.demo.model

import javafx.beans.property.SimpleDoubleProperty
import tornadofx.*
import javax.json.JsonObject

class Location: JsonModel {
    val latProperty = SimpleDoubleProperty()
    var lat by latProperty

    val lngProperty = SimpleDoubleProperty()
    var lng by lngProperty

    /**
     * Updates the UserId Location with data from a JsonObject.
     * @param json The JsonObject containing city data.
     */
    override fun updateModel(json: JsonObject) {
        with(json) {
            lat = getDouble("lat")
            lng = getDouble("lng")
        }
    }
}

/**
 * View model for UserId class.
 */
class LocationModel : ItemViewModel<Location>() {
    val lat = bind(Location::latProperty)
    val lng = bind(Location::lngProperty)
}