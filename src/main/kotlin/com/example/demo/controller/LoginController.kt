package com.example.demo.controller

import java.util.*

import com.example.demo.model.*
import com.example.demo.util.execute
import com.example.demo.view.LoginScreen
import com.example.demo.view.SecureScreen
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.chart.PieChart
import org.jetbrains.exposed.sql.insert
import tornadofx.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.json.JsonObject

//import java.time.LocalDate

/**
 * Controller class responsible for handling login and authentication logic.
 */
class LoginController : Controller() {
    // Define the loggedInProperty
    val loggedInProperty = SimpleBooleanProperty(false)

    private val loginScreen: LoginScreen by inject()
    private val secureScreen: SecureScreen by inject()

    //Get All items!!
    private lateinit var listOfItems: ObservableList<ExpensesEntryModel>

    /**
     * The list of all expense items.
     */
    var items: ObservableList<ExpensesEntryModel> by singleAssign() // interface to use list of items

    /**
     * Data for the pie chart in the view.
     */
    var pieItemsData = FXCollections.observableArrayList<PieChart.Data>() // pass data to the pie chart in the view
//    var expenseModel = ExpensesEntryModel()

    val api: Rest by inject()

    /**
     * Initialize the controller by checking if the user is already logged in.
     * If so, automatically log in the user; otherwise, show the login screen.
     */
    init {

        // Set the base URI for the weather API
        api.baseURI = "http://localhost:8083"

//        with(config) {
//            if (containsKey(USERNAME) && containsKey(PASSWORD))
//                tryLogin(USERNAME, PASSWORD)
//            else
        showLoginScreen("Log in | Sing-Up")

        loggedInProperty.addListener { _, _, isLoggedIn ->
            if (isLoggedIn) {
                initLogin()
            }
        }
    }

    fun initLogin() {
//        listOfItems = execute {
//            ExpensesEntryTbl.selectAll().map {
//                ExpensesEntryModel().apply {
//                    item = it.toExpensesEntry()
//                }
//            }.asObservable()
//        }
//
//        items = listOfItems
//
//        items.forEach {
//            pieItemsData.add(PieChart.Data(it.itemName.value, it.itemPrice.value.toDouble()))
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
        val email = "afasdf@demo.com"
        val superApp = "2023b.dor.ferenc" // Replace with the actual superapp name

//        val response = api.get("http://localhost:8083/superapp/users/login/${superApp}/${email}")
        val response = api.get("/superapp/users/login/${superApp}/${email}")
        if (response.ok()) {                 // Process the response data
            val currentUser = response.list().toModel<UserModel>()
            println("currentUser: ${currentUser[0].userId.email}")
            loginScreen.clear()

            loggedInProperty.value = true
            showSecureScreen((currentUser[0].userId.email))
        } else {                 // Handle the error based on the status code
            val statusCode = response.statusCode
            println("Request failed with status code: $statusCode")
        }
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
            showSecureScreen(currentUser[0].userId.email)
            buildInitialItemsObject(email)

        } else {                 // Handle the error based on the status code
            val statusCode = response.statusCode
            println("Request failed with status code: $statusCode")
        }
    }

    fun builSuperappObjectBody(email: String, id: Int = 0, entryDate: LocalDate = LocalDate.now(), itemName: String = "d", itemPrice: Double = 0.0): JsonObject {
        val creationTimestamp2 = OffsetDateTime.now()
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        val formattedTimestamp2 = creationTimestamp2.format(formatter2)

        val location = JsonBuilder().apply {
            add("lat", 32.1133)
            add("lng", 34.818)
        }.build()

        val userId = JsonBuilder().apply {
            add("superapp", "2023b.dor.ferenc")
            add("email", email)
        }.build()
        val createdBy = JsonBuilder().apply {
            add("userId", userId)
        }.build()

        val objectDetails = JsonBuilder().apply {
            add("id", id)
            add("entryDate",entryDate)
            add("itemName", itemName)
            add("itemPrice", itemPrice)
        }.build()

        val jsonObjectBody = JsonBuilder().apply {
            add("type", "initialItemsTableObject")
            add("alias", "expenseTrackerINIT")
            add("active", true)
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
            println(response)
            val parentObject = response.list().toModel<ObjectItems>()
//            println(parentObject)
//            initLogin()

        } else {                 // Handle the error based on the status code
            val statusCode = response.statusCode
            println("Request failed with status code: $statusCode")
        }
    }


//    /**
//     * Adds a new expense entry.
//     * @param newEntryDate The date of the new entry.
//     * @param newItem The name of the item.
//     * @param newPrice The price of the item.
//     * @return The created [ExpensesEntry] object.
//     */
//    fun add(newEntryDate: Date, newItem: String, newPrice: Double): ExpensesEntry {
//        val newEntry = execute {
//            ExpensesEntryTbl.insert {
//                it[entryDate] = newEntryDate
//                it[itemName] = newItem
//                it[itemPrice] = BigDecimal.valueOf(newPrice)
//            }
//        }
//        val newEntryAsExpensesEntry = ExpensesEntry(newEntry[ExpensesEntryTbl.id], newEntryDate, newItem, newPrice)
//        listOfItems.add(ExpensesEntryModel().apply { item = newEntryAsExpensesEntry })
//        pieItemsData.add(PieChart.Data(newItem, newPrice))
//        return newEntryAsExpensesEntry
//    }

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