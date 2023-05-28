//package com.example.demo.util
//
//import com.example.demo.model.ExpensesEntryJsonModel
//import com.example.demo.model.ExpensesEntryTbl
//import javafx.collections.ObservableList
//import org.jetbrains.exposed.sql.SchemaUtils
//import org.jetbrains.exposed.sql.StdOutSqlLogger
//import org.jetbrains.exposed.sql.Transaction
//import org.jetbrains.exposed.sql.addLogger
//import org.jetbrains.exposed.sql.transactions.TransactionManager
//import java.sql.Connection
//
//private var LOG_TO_CONSOLE: Boolean = false
//
///**
// * Creates a new transaction and returns it. If a transaction is already in progress, it returns the current transaction.
// * The transaction has the serializable isolation level and can be used for database operations.
// * If `LOG_TO_CONSOLE` flag is set to `true`, it enables SQL logging to the console.
// *
// * @return The newly created or current transaction.
// */
//fun newTransaction(): Transaction =
//    TransactionManager.currentOrNew(Connection.TRANSACTION_SERIALIZABLE).apply {
//        if (LOG_TO_CONSOLE) addLogger(StdOutSqlLogger)
//    }
//
///**
// * Enables logging of SQL statements to the console for debugging purposes.
// * This function sets the `LOG_TO_CONSOLE` flag to `true`.
// */
//fun enableConsoleLogger() {
//    LOG_TO_CONSOLE = true
//}
//
///**
// * Creates database tables by executing the necessary schema creation commands.
// * It uses a new transaction and applies the schema creation commands specific to the `ExpensesEntryTbl`.
// */
//fun createTables() { //create perant  super app object
//    with(newTransaction()) {
//        SchemaUtils.create(ExpensesEntryTbl)
//    }
//}
//
//
///**
// * Executes a command in a database transaction and handles the commit and close operations.
// * It creates a new transaction, executes the provided `command`, commits the transaction, and then closes it.
// * The return value of the `command` is returned as the result of this function.
// *
// * @param command The command to be executed in the transaction.
// * @return The result of the `command`.
// */
//fun <T> execute(command: () -> ObservableList<ExpensesEntryJsonModel>) : T {
//    with(newTransaction()) {
//        return  command().apply {
//            commit()
//            close()
//        }
//    }
//}