package com.example.demo.app

//import com.example.demo.controller.ItemController
import com.example.demo.controller.LoginController
//import com.example.demo.util.createTables
//import com.example.demo.util.enableConsoleLogger
import com.example.demo.view.ExpensesEditor
import com.example.demo.view.ExpensesReport
import com.example.demo.view.LoginScreen
import com.example.demo.view.SecureScreen
import javafx.scene.control.TabPane
import org.jetbrains.exposed.sql.Database
import tornadofx.*


/**
 * The main application class representing the Budget Tracker workspace.
 * It extends the TornadoFX `Workspace` class and serves as the entry point for the application.
 *
 * @param title The title of the application window.
 * @param navigationMode The navigation mode for the workspace. In this case, it is set to `NavigationMode.Tabs`.
 */
class BudgetTrackerWorkspace : Workspace("Budget Tracker Workerspace", NavigationMode.Tabs) {

    private val expensesEditor: ExpensesEditor by inject()
    private val expensesReport: ExpensesReport by inject()
    private val loginController: LoginController by inject()


    init {
        // Enable console logging and set up the database
//        enableConsoleLogger()
        Database.connect("jdbc:sqlite:./app-budget-tracker.db", "org.sqlite.JDBC")
//        createTables()

        // Initialize the controllers
//        ItemController()
        LoginController()


        // Bind visibility of views to login status
        loginController.loggedInProperty.addListener { _, _, isLoggedIn ->
            if (isLoggedIn) {
                dock<ExpensesReport>()
                dock<ExpensesEditor>()
            } else {
//                expensesEditor.clearEditor()
//                expensesReport.clearReport()
//                println("in here")
//                ExpensesEditor.clearEditor()
            }
        }


        // Dock the views
//        dock<ExpensesEditor>()
//        dock<ExpensesReport>()
        dock<LoginScreen>()
//        dock<SecureScreen>()

        // Configure the tab container
        tabContainer.tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
    }
}
