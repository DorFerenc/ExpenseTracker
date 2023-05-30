package com.example.demo.controller

//import com.example.demo.util.execute
import com.example.demo.model.*
import com.example.demo.view.LoginScreen
import com.example.demo.view.SecureScreen
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.chart.PieChart
import tornadofx.*
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.json.JsonObject

//import java.time.LocalDate

/**
 * Controller class responsible for handling login and authentication logic.
 */
class LoginController : Controller() {
    // Define the loggedInProperty
    val superApp = "2023b.dor.ferenc"
    var loggedInProperty = SimpleBooleanProperty(false)

    private val loginScreen: LoginScreen by inject()
    private val secureScreen: SecureScreen by inject()

    //Get All items!!
//    private lateinit var listOfItems: ObservableList<ExpensesEntryJsonModel>
//    private var listOfItems: ObservableList<ExpensesEntryJsonModel> = FXCollections.observableArrayList()
    private var listOfItems: ObservableList<ExpensesEntryJsonModel> = mutableListOf<ExpensesEntryJsonModel>().asObservable()
    var allChildsIdAndInternalId: MutableMap<Int?, String?> = mutableMapOf()

    var shouldInitListOfItems = false


    //Get All items!!
//    private val listOfItems: ObservableList<ExpensesEntryModel> = execute {
//        ExpensesEntryTbl.selectAll().map {
//            ExpensesEntryModel().apply {
//                item = it.toExpensesEntry()
//            }
//        }.asObservable()
//    }

    /**
     * The list of all expense items.
     */
    var items: ObservableList<ExpensesEntryJsonModel> = FXCollections.observableArrayList<ExpensesEntryJsonModel>() // interface to use list of items

    /**
     * Data for the pie chart in the view.
     */
    var pieItemsData = FXCollections.observableArrayList<PieChart.Data>() // pass data to the pie chart in the view
//    var expenseModel = ExpensesEntryModel()

    val api: Rest by inject()
    var lastItemId: Int = 0
    var parentInternalObjectId: String = ""
    var currentUserEmail: String = ""
//    lateinit var parentObject: ObservableList<ObjectItems>

    /**
     * Initialize the controller by checking if the user is already logged in.
     * If so, automatically log in the user; otherwise, show the login screen.
     */
    init {

        // Set the base URI for the weather API
        api.baseURI = "http://localhost:8083"
//        api.baseURI = "http://13.50.168.111:8083"
//        items = listOfItems
//        items.forEach {
//            pieItemsData.add(PieChart.Data(it.itemName.value, it.itemPrice.value.toDouble()))
//        }
//        with(config) {
//            if (containsKey(USERNAME) && containsKey(PASSWORD))
//                tryLogin(USERNAME, PASSWORD)
//            else
        showLoginScreen("Log in | Sing-Up")
        items = listOfItems
//  i
//        loggedInProperty.addListener { _, _, isLoggedIn ->
//            if (isLoggedIn) {
//                initLogin()
//            }
//        }
    }


    /**
     * Show the login screen with an optional error message and optional shaking animation.
     */
    private fun showLoginScreen(message: String, shake: Boolean = false) {
        secureScreen.replaceWith(loginScreen, sizeToScene = true, centerOnScreen = true)
        runLater {
            if (shake) loginScreen.shakeStage()
            loginScreen.title = message
        }
    }

    /**
     * Show the secure screen after a successful login.
     */
    private fun showSecureScreen(msg: String?) {
        val temp_msg = msg?.let { it } ?: "Error"
        loginScreen.title = temp_msg
        loginScreen.replaceWith(secureScreen, sizeToScene = true, centerOnScreen = true)
    }


//    fun tryLogin(username: String, password: String, remember: Boolean) {
//        runAsync {
//            // Simulate the login process and check if the provided credentials are valid
//            username == adminLogin && password == adminPassword
//        } ui { successfulLogin ->
//
//            if (successfulLogin) {
//                loginScreen.clear()
//
//                if (remember) {
//                    with(config) {
//                        set(USERNAME to username)
//                        set(PASSWORD to password)
//                        save()
//                    }
//                }
//
//                showSecureScreen()
//            } else {
//                // Show the login screen with an error message and shaking animation
//                showLoginScreen("Login failed. Please try again.", true)
//            }
//        }
//    }

    /**
     * Attempts to log in with the provided username and password.
     * If the login is successful, it clears the login screen, saves the credentials if "remember" is true,
     * and shows the secure screen. If the login fails, it shows an error message on the login screen.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     */
    fun tryLogin(superApp: String, email: String) {
        //todo remove hard coded:
//        val email = "afasdf@demo.com"
//        val superApp = "2023b.dor.ferenc" // Replace with the actual superapp name

//        val response = api.get("http://localhost:8083/superapp/users/login/${superApp}/${email}")
        val response = api.get("/superapp/users/login/${superApp}/${email}")
        if (response.ok()) {                 // Process the response data
            val currentUser = response.list().toModel<UserModel>()
            println("DID LOGING GET currentUser: ${currentUser[0].userId.email}")
            loginScreen.clear()


            currentUserEmail = email

            if (getParentSuperAppObject()) {
                initItemsList()
                showSecureScreen((currentUser[0].userId.email))
            }
            else
                println("LOGIN FAILED")



        } else {                 // Handle the error based on the status code
            val statusCode = response.statusCode
            println("Request failed with status code: $statusCode")
        }
    }

    //    fun getParentSupperAppObject() {
//        val response = api.get("/superapp/objects/search/byType/initialItemsTableObject?userSuperapp=${superApp}&userEmail=${currentUserEmail}&size=999&page=9")
//        if (response.ok()) {                 // Process the response data
//            println("Finding DADDY")
//            val allInitObject = response.list().toModel<ObjectItems>()
//            for (initObject in allInitObject) {
//                if (initObject.createdBy.userId.email == currentUserEmail) {
//                    println("FOUND DADDY")
//                    lastItemId = initObject.objectDetails.id
//                    parentInternalObjectId = initObject.objectId.internalObjectId!!
//                    println("DADDY lastItemId: $lastItemId, parentInternalObjectId: $parentInternalObjectId")
//                    return
//                }
//            }
//        }
//    }
//fun getParentSupperAppObject() {
//    val response = api.get("/superapp/objects/search/byType/initialItemsTableObject?userSuperapp=$superApp&userEmail=$currentUserEmail&size=999&page=9")
//    if (response.ok()) {
//        println("Finding DADDY")
//        val allInitObject = response.list().toModel<ObjectItems>()
//        val initObject = allInitObject.find { it.createdBy.userId.email == currentUserEmail }
//        if (initObject != null) {
//            println("FOUND DADDY")
//            lastItemId = initObject.objectDetails.id
//            parentInternalObjectId = initObject.objectId.internalObjectId!!
//            println("DADDY lastItemId: $lastItemId, parentInternalObjectId: $parentInternalObjectId")
//        }
//        else
//            println("DID NOT FIND DAD")
//    }
//}
    fun getParentSuperAppObject() : Boolean {
        println("WAIT API DADDY")
        val response =
            api.get("/superapp/objects/search/byType/initialItemsTableObject_$currentUserEmail?userSuperapp=$superApp&userEmail=$currentUserEmail&size=30&page=0")

        if (response.ok()) {
            println("Finding DADDY")
            val allInitObject = response.list().toModel<ObjectItems>()
            println("make model")
//            allInitObject.forEach { println(it.createdBy.userId.email) } // Print every email
//            println("make model2")
//            val initObject = allInitObject.find { it.createdBy.userId.email == currentUserEmail }
//            println("make model3")
//            if (initObject != null) {
            println("FOUND DADDY")
//            lastItemId = allInitObject[0].objectDetails.id
            parentInternalObjectId = allInitObject[0].objectId.internalObjectId!!
            println("DADDY lastItemId: $lastItemId, parentInternalObjectId: $parentInternalObjectId")
            return true
//            }
        }
        return false
    }


    fun trySingUp(email: String, userName: String, role: String, avatar: String) {
        //Build Json
        println("IN TRY SINGUP")
        val jsonPayload = JsonBuilder().apply {
            add("email", email)
            add("role", role)
            add("username", userName)
            add("avatar", avatar)
        }.build()
        println(jsonPayload) // print the Json sent

//        val response = api.post("http://localhost:8083/superapp/users", jsonPayload) //send Post request
        val response = api.post("/superapp/users", jsonPayload) //send Post request

        if (response.ok()) {                 // Process the response data
            val currentUser = response.list().toModel<UserModel>()
//            println("currentUser: ${currentUser[0].userId.email}")
            loginScreen.clear()
            loggedInProperty.value = true
            println("DID SINGUP BUILDING INIT SUPERAPP")

            buildInitialItemsObject(email)

        } else {                 // Handle the error based on the status code
            val statusCode = response.statusCode
            println("Request failed with status code: $statusCode")
        }
    }

    fun builSuperappObjectBody(
        email: String,
        id: Int = 0,
        entryDate: LocalDate = LocalDate.now(),
        itemName: String = "d",
        itemPrice: Double = 0.0,
        type: String = "initialItemsTableObject_$email",
        alias: String = "expenseTrackerINIT_$email",
        active: Boolean = true,
        shouldCreationTimestamp: Boolean = true
    ): JsonObject {
        var formattedTimestamp2: String = ""
        if (shouldCreationTimestamp) {
            val creationTimestamp2 = OffsetDateTime.now()
            val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            formattedTimestamp2 = creationTimestamp2.format(formatter2)
        }


        val location = JsonBuilder().apply {
            add("lat", 32.1133)
            add("lng", 34.818)
        }.build()

        val userId = JsonBuilder().apply {
            add("superapp", superApp)
            add("email", email)
        }.build()
        val createdBy = JsonBuilder().apply {
            add("userId", userId)
        }.build()

        val objectDetails = JsonBuilder().apply {
            add("id", id)
            add("entryDate", entryDate)
            add("itemName", itemName)
            add("itemPrice", itemPrice)
        }.build()

        val jsonObjectBody = JsonBuilder().apply {
            add("type", type)
            add("alias", alias)
            add("active", active)
            add("creationTimestamp", formattedTimestamp2)

            add("location", location)
            add("createdBy", createdBy)
            add("objectDetails", objectDetails)
        }.build()

        return jsonObjectBody
    }

    fun buildInitialItemsObject(email: String) {
        println("IN INIT SUPERAPP")

        val jsonObjectBody = builSuperappObjectBody(email)

        println(jsonObjectBody) // print the Json sent

        val response = api.post("/superapp/objects", jsonObjectBody) //send Post request

        if (response.ok()) {                 // Process the response data
            println("Fully Loaded: Singup completed && Parent Object Created")
            val parentObject = response.list().toModel<ObjectItems>()
            println("ID: ${parentObject[0].objectId.internalObjectId}")
            lastItemId = parentObject[0].objectDetails.id
            parentInternalObjectId = parentObject[0].objectId.internalObjectId!!
            currentUserEmail = parentObject[0].createdBy.userId.email!!

            println("lastItemId: $lastItemId, parentInternalObjectId: $parentInternalObjectId,  currentUserEmail: $currentUserEmail")
//            initItemsList()

            items = listOfItems
            items.forEach {
                pieItemsData.add(PieChart.Data(it.itemName.value, it.itemPrice.value.toDouble()))
            }


            shouldInitListOfItems = true

            showSecureScreen(currentUserEmail)
            loggedInProperty.value = true

        } else {                 // Handle the error based on the status code
            val statusCode = response.statusCode
            println("Request failed with status code: $statusCode")
        }
    }


    /**
     * Adds a new expense entry.
     * @param newEntryDate The date of the new entry.
     * @param newItem The name of the item.
     * @param newPrice The price of the item.
     * @return The created [ExpensesEntry] object.
     */
    fun add(newEntryDate: LocalDate, newItem: String, newPrice: Double): ExpensesEntryJson? {
        println("ADDING TO SUPERAPP")

        val jsonObjectBody = builSuperappObjectBody(
            currentUserEmail,
            (lastItemId + 1),
            newEntryDate,
            newItem,
            newPrice,
            "newItem_$currentUserEmail",
            "newItem_$newItem"
        )
        println(jsonObjectBody) // print the Json sent

        val response1 = api.post("/superapp/objects", jsonObjectBody) //send Post request to build child
        if (!response1.ok()) {
            println("Couldn't post")
            return null
        }
        val childObject = response1.list().toModel<ObjectItems>()

        val userId = JsonBuilder().apply {
            add("superapp", "2023b.dor.ferenc")
            add("internalObjectId", childObject[0].objectId.internalObjectId)
        }.build()
        println(userId)
        println("/superapp/objects/${superApp}/${parentInternalObjectId}/children?userSuperapp=${superApp}&userEmail=${currentUserEmail}")
        val response = api.put(
            "/superapp/objects/${superApp}/${parentInternalObjectId}/children?userSuperapp=${superApp}&userEmail=${currentUserEmail}",
            userId
        ) //send Put request
        if (response.ok()) {                 // Process the response data
            println("Fully added")
            lastItemId += 1

//
//            if (shouldInitListOfItems) {
//                println("DID WHAT NEEDED TO BE DONE WITH ITEMS")
//                items = listOfItems
//                shouldInitListOfItems = false
//            }

            val newEntryAsExpensesEntry = ExpensesEntryJson(lastItemId, newEntryDate, newItem, newPrice)

//            allChildsNameAndInternalId.putAll(allChildrenObjectsList.associate { it.alias to it.objectId.internalObjectId })
            allChildsIdAndInternalId.put(childObject[0].objectDetails.id, childObject[0].objectId.internalObjectId)

            listOfItems.add(ExpensesEntryJsonModel().apply { item = newEntryAsExpensesEntry })
            pieItemsData.add(PieChart.Data(newItem, newPrice))


            return newEntryAsExpensesEntry
        } else {
            println("SHIIT $response")
            return null
        }
//        val newEntry = execute {
//            ExpensesEntryTbl.insert {
//                it[entryDate] = newEntryDate
//                it[itemName] = newItem
//                it[itemPrice] = BigDecimal.valueOf(newPrice)
//            }
//        }
        return null
    }

    /**
     * Updates an existing expense entry.
     * @param updatedItem The [ExpensesEntryModel] object containing the updated values.
     * @return The number of rows affected in the database.
     */
    fun update(updatedItem: ExpensesEntryJsonModel): Int {
        println("UPDATING: ${updatedItem.itemName.value}")

        val jsonObjectBody = builSuperappObjectBody(
            currentUserEmail,
            updatedItem.id.value,
            updatedItem.entryDate.value,
            updatedItem.itemName.value,
            updatedItem.itemPrice.value.toDouble(),
            "newItem_${currentUserEmail}",
            "newItem_${updatedItem.itemName.value}",
            true,
            false
        )
        println(jsonObjectBody) // print the Json sent

        val currentItemInternalId = allChildsIdAndInternalId[updatedItem.id.value]
        println("/superapp/objects/${superApp}/${currentItemInternalId}?userSuperapp=${superApp}&userEmail=${currentUserEmail}")
        val response = api.put("/superapp/objects/${superApp}/${currentItemInternalId}?userSuperapp=${superApp}&userEmail=${currentUserEmail}", jsonObjectBody)
        if (response.ok())  {
            println("UPDATED Item ${updatedItem.itemName.value} Updating listItems")
//            initItemsList()
            return 1
        }
        else
            println("FAILED to update: ${updatedItem.itemName.value}, Status: ${response.status}, code: ${response.statusCode}")
        return 0
//        return execute {
//            ExpensesEntryTbl.update ({ ExpensesEntryTbl.id eq(updatedItem.id.value.toInt()) }) {
//                it[entryDate] = updatedItem.entryDate.value
//                it[itemName] = updatedItem.itemName.value
//                it[itemPrice] = BigDecimal.valueOf(updatedItem.itemPrice.value.toDouble())
//            }
//        }
    }

    /**
     * Deletes an expense entry.
     * @param model The [ExpensesEntryModel] object to be deleted.
     */
    fun delete(model: ExpensesEntryJsonModel) {
        println("DELETING: ${model.itemName.value}")

        val jsonObjectBody = builSuperappObjectBody(
            currentUserEmail,
            model.id.value,
            model.entryDate.value,
            model.itemName.value,
            model.itemPrice.value.toDouble(),
            "newItem_${currentUserEmail}_deleted",
            "newItem_${model.itemName.value}_deleted",
            false,
            false
        )
        println(jsonObjectBody) // print the Json sent

        val currentItemInternalId = allChildsIdAndInternalId[model.id.value]
        println("/superapp/objects/${superApp}/${currentItemInternalId}?userSuperapp=${superApp}&userEmail=${currentUserEmail}")
        val response = api.put("/superapp/objects/${superApp}/${currentItemInternalId}?userSuperapp=${superApp}&userEmail=${currentUserEmail}", jsonObjectBody)
        if (response.ok())  {
            println("Deleted Item ${model.itemName.value} Updating listItems")
//            initItemsList()
            listOfItems.remove(model)
            removeModelFromPie(model)
        }
        else
            println("FAILED to delete: ${model.itemName.value}, Status: ${response.status}, code: ${response.statusCode}")
//        execute {
//            ExpensesEntryTbl.deleteWhere {
//                ExpensesEntryTbl.id eq(model.id.value.toInt())
//            }
//        }
//        listOfItems.remove(model)
//        removeModelFromPie(model)
    }

    /**
     * Updates the corresponding pie chart data for a specific expense entry.
     * @param model The [ExpensesEntryModel] object to update the pie chart data for.
     */
    fun updatePiecePie(model: ExpensesEntryJsonModel) {
        val modelId = model.id
        var currentIndex: Int
        items.forEachIndexed { index, data ->
            if (modelId == data.id) {
                //we have the right object to update
                currentIndex = index
                pieItemsData[currentIndex].name = data.itemName.value
                pieItemsData[currentIndex].pieValue = data.itemPrice.value.toDouble()
            } else {
                // Ignore
            }
        }
    }

    /**
     * Removes an expense entry from the pie chart data.
     * @param model The [ExpensesEntryModel] object to be removed from the pie chart.
     */
    private fun removeModelFromPie(model: ExpensesEntryJsonModel) {
        var currentIndex = 0
        pieItemsData.forEachIndexed { index, data ->
            if (data.name == model.itemName.value && index != -1)
                currentIndex = index
        }
        pieItemsData.removeAt(currentIndex)
    }

//    /**
//     * Filters expense entries by a specific entry date.
//     * @param today The entry date to filter by.
//     * @return An [ObservableList] of filtered [ExpensesEntryModel] objects.
//     */
//    fun filterByEntryDates(today: LocalDate?): ObservableList<ExpensesEntryJsonModel> = execute {
////        ExpensesEntryTbl
////            .select { ExpensesEntryTbl.entryDate eq today!! }
////            .map {
////                ExpensesEntryModel().apply {
////                    item = it.toExpensesEntry()
////                }
////            }.asObservable()
//    }

    /**
     * Filters expense entries by a specific entry date.
     * @param today The entry date to filter by.
     * @return An [ObservableList] of filtered [ExpensesEntryJsonModel] objects.
     */
    fun filterByEntryDates(today: LocalDate?): ObservableList<ExpensesEntryJsonModel> {
        val filteredList = items.filter { entry ->
            val entryDate = entry.entryDate.value
            entryDate == today
        }
        return observableListOf(*filteredList.toTypedArray())
    }

    /**
     * Filters the expenses by the specified date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of expenses within the specified date range.
     */
    fun filterByDateRange(startDate: LocalDate, endDate: LocalDate): List<ExpensesEntryJsonModel> {
        return items.filter { entry ->
            val entryDate = entry.entryDate.value
            entryDate in startDate..endDate
        }
    }

    fun findObjectWithLargestId(list: List<ExpensesEntryJsonModel>): Int? {
        var largestIdObject: ExpensesEntryJsonModel? = null
        var largestId: Int = Integer.MIN_VALUE

        for (item in list) {
            if (item.id > largestId)
                largestId = item.id.value
        }

        return largestId
    }
    fun initItemsList(num: Int = 30): Boolean {
        println("initItemsList STARTED")
        println("/superapp/objects/${superApp}/${parentInternalObjectId}/children?userSuperapp=${superApp}&userEmail=${currentUserEmail}&size=${num}&page=0")

        val response = api.get("/superapp/objects/${superApp}/${parentInternalObjectId}/children?userSuperapp=${superApp}&userEmail=${currentUserEmail}&size=${num}&page=0")
        if (response.ok()) {
            println("initItemsList -> response ok")
//            val activeIsTrue = response.list().toModel<ObjectItems>().removeIf { it.active }

            // create the list of allChildrenObjectsList
            val allChildrenObjectsList = response.list().toModel<ObjectItems>()
            allChildsIdAndInternalId.putAll(allChildrenObjectsList.associate { it.objectDetails.id to it.objectId.internalObjectId })

            val filteredList = allChildrenObjectsList.filter { it.active }

            listOfItems = //: ExpensesEntryJsonModel
                filteredList.map {
                    ExpensesEntryJsonModel().apply {
                            item = it.objectDetails
                    }
                }.asObservable()
            println("allChildrenObjectsList: $allChildrenObjectsList, size = ${allChildrenObjectsList.size}")
            println("listOfItems: $listOfItems, size = ${listOfItems.size}")

            if (listOfItems.size != 0) {
                lastItemId = listOfItems.maxByOrNull { it.id.value }!!.id.value
//            if (lastItemId > listOfItems.size) {
//                return initItemsList(lastItemId + 1)
//            }
                println("lastItemId: ${lastItemId}")
            }

            items = listOfItems
            items.forEach {
                pieItemsData.add(PieChart.Data(it.itemName.value, it.itemPrice.value.toDouble()))
            }

            println("BOOTING UP SCREENS")
            showSecureScreen(currentUserEmail)
            loggedInProperty.value = true
            return true
        }
        println("Couldn't Get all")
        return false
    }

    /**
     * Logs out the current user by removing the saved username and password from the config
     * and shows the login screen.
     */
    fun logout() {
        with(config) {
            remove(USERNAME)
            remove(PASSWORD)
            save()
        }
        loggedInProperty.value = false
        println("iasdasdn here")
        showLoginScreen("Login | Sing-up")
    }

    companion object {
        const val USERNAME = "username"
        const val PASSWORD = "password"

        // Default admin login credentials
        const val adminLogin = "admin"
        const val adminPassword = "secret"
    }

}