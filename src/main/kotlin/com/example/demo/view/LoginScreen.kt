package com.example.demo.view

import com.example.demo.app.Styles.Companion.loginScreen
import com.example.demo.controller.LoginController
import com.example.demo.model.ExpensesEntryModel
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.chart.PieChart
import javafx.scene.control.ToggleGroup
import javafx.scene.text.Font
import javafx.util.Duration
import tornadofx.*
import java.util.Observable

/**
 * View class representing the login screen of the application.
 */
class LoginScreen : View("Login | Sing-up") {
    private val loginController: LoginController by inject()

    // ViewModel to manage the form fields' values and bindings
    private val loginModel = object : ViewModel() {
        val superApp = bind { SimpleStringProperty() }
        val email = bind { SimpleStringProperty() }
//        val remember = bind { SimpleBooleanProperty() }
    }
    private val singupModel = object : ViewModel() {
        val signUpEmail = bind { SimpleStringProperty() }
        val signUpUsername = bind { SimpleStringProperty() }
        val signUpRole = bind { SimpleStringProperty() }
        val signUpAvatar = bind { SimpleStringProperty() }
    }

    private var listOfItems: ObservableList<ExpensesEntryModel> by singleAssign()
    private var pieData = FXCollections.observableArrayList<PieChart.Data>()

    override val root = form {
        addClass(loginScreen)
//        alignment = Pos.CENTER
        fieldset {
            vbox {
                spacing = 10.0
                label("Login") { style { font = Font.font("Arial", 16.0) } }
                field("SuperApp") {
                    textfield(loginModel.superApp) {
                        required()
                        whenDocked { requestFocus() }  // Set focus on the username field when the form is displayed
                        maxWidth = 500.0
                        disableWhen(singupModel.signUpEmail.isNotEmpty)
                    }
                }
                field("Email") {
//                passwordfield(model.email).required()
                    textfield(loginModel.email) {
                        required()
                        maxWidth = 500.0
                        disableWhen(singupModel.signUpEmail.isNotEmpty)
                    }
                }
//                field(rememberLabel) {
//                    checkbox(property = loginModel.remember)
//                    disableWhen(singupModel.signUpEmail.isNotEmpty)
//                }
            }
        }

        button("Login") {
//            isDefaultButton = true
            action {
                loginModel.commit {   // Trigger the login process when the Login button is clicked
                    loginController.tryLogin(
                        loginModel.superApp.value,
                        loginModel.email.value,
                    )
                }
            }
            disableWhen(singupModel.signUpEmail.isNotEmpty)
        }

        label("______________________________________________") { style { font = Font.font("Arial", 16.0) } }

        fieldset {
            vbox {
                spacing = 10.0
                paddingTop = 20.0
                label("Sign Up") { style { font = Font.font("Arial", 16.0) } }
                field("Email") {
                    textfield(singupModel.signUpEmail) {
                        required()
                        maxWidth = 500.0
                        disableWhen(loginModel.superApp.isNotEmpty)
                    }
                }
                field("Username") {
                    textfield(singupModel.signUpUsername) {
                        required()
                        maxWidth = 500.0
                        disableWhen(loginModel.superApp.isNotEmpty)
                    }
                }
                field("Role") {
                    val toggleGroup = ToggleGroup()
//                    radiobutton("MINIAPP_USER", toggleGroup) {
//                        action {
//                            if (isSelected)
//                                singupModel.signUpRole.value = "MINIAPP_USER"
//                        }
//                        disableWhen(loginModel.superApp.isNotEmpty)
//                    }
                    radiobutton("SUPERAPP_USER", toggleGroup) {
                        isSelected = true
                        action {
                            if (isSelected)
                                singupModel.signUpRole.value = "SUPERAPP_USER"
                            singupModel.signUpRole.value = "SUPERAPP_USER"
                        }
//                        disableWhen(loginModel.superApp.isNotEmpty)
                    }
//                    radiobutton("ADMIN", toggleGroup) {
//                        action {
//                            if (isSelected)
//                                singupModel.signUpRole.value = "ADMIN"
//                        }
//                        disableWhen(loginModel.superApp.isNotEmpty)
//                    }
                }
                field("Avatar") {
                    textfield(singupModel.signUpAvatar) {
                        required()
                        maxWidth = 500.0
                        disableWhen(loginModel.superApp.isNotEmpty)
                    }
                }
            }
        }
        button("Sign Up") {
            action {
                println("hey")
                singupModel.commit {   // Trigger the login process when the Login button is clicked
                    loginController.trySingUp(
                        singupModel.signUpEmail.value,
                        singupModel.signUpUsername.value,
                        "SUPERAPP_USER",
                        singupModel.signUpAvatar.value
                    )
                }
            }
            disableWhen(loginModel.superApp.isNotEmpty)
        }
    }

    override fun onDock() {
        loginModel.validate(decorateErrors = false) // Validate the form fields when the view is displayed
    }

    /**
     * Shakes the login screen stage to provide a visual feedback when login fails.
     */
    fun shakeStage() {
        var x = 0
        var y = 0
        val cycleCount = 10
        val move = 10
        val keyframeDuration = Duration.seconds(0.04)

        val stage = FX.primaryStage

        val timelineX = Timeline(KeyFrame(keyframeDuration, {
            if (x == 0) {
                stage.x = stage.x + move
                x = 1
            } else {
                stage.x = stage.x - move
                x = 0
            }
        }))

        timelineX.cycleCount = cycleCount
        timelineX.isAutoReverse = false

        val timelineY = Timeline(KeyFrame(keyframeDuration, {
            if (y == 0) {
                stage.y = stage.y + move
                y = 1
            } else {
                stage.y = stage.y - move
                y = 0
            }
        }))

        timelineY.cycleCount = cycleCount
        timelineY.isAutoReverse = false

        timelineX.play()
        timelineY.play()
    }

    /**
     * Clears the input fields and remember checkbox on the login screen.
     */
    fun clear() {
        loginModel.superApp.value = ""
        loginModel.email.value = ""
//        loginModel.remember.value = false

        singupModel.signUpEmail.value = ""
        singupModel.signUpUsername.value = ""
//        singupModel.signUpRole.value =
        singupModel.signUpAvatar.value = ""
    }

//    companion object {
//        const val screenTitle = "Please log in"

//        const val usernameLabel = "Username"
//        const val passwordLabel = "Password"
//        const val rememberLabel = "Remember me"
//    }
}