//package com.example.demo.controller
//
//import com.example.demo.model.ExpensesEntry
//import com.example.demo.model.ExpensesEntryModel
//import com.example.demo.model.ExpensesEntryTbl
//import com.example.demo.model.toExpensesEntry
//import com.example.demo.util.execute
//import javafx.collections.FXCollections
//import javafx.collections.ObservableList
//import javafx.scene.chart.PieChart
//import org.jetbrains.exposed.sql.*
//import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
//import tornadofx.*
//import java.math.BigDecimal
//import java.time.LocalDate
//
///**
// * The controller responsible for managing expense items.
// */
//class ItemController: Controller() {
//
//    //Get All items!!
////    private lateinit var listOfItems: ObservableList<ExpensesEntryModel>
//    private val listOfItems: ObservableList<ExpensesEntryModel> = execute {
//        ExpensesEntryTbl.selectAll().map {
//            ExpensesEntryModel().apply {
//                item = it.toExpensesEntry()
//            }
//        }.asObservable()
//    }
//
//    /**
//     * The list of all expense items.
//     */
//    var items: ObservableList<ExpensesEntryModel> by singleAssign() // interface to use list of items
//    /**
//     * Data for the pie chart in the view.
//     */
//    var pieItemsData = FXCollections.observableArrayList<PieChart.Data>() // pass data to the pie chart in the view
////    var expenseModel = ExpensesEntryModel()
//
//    init {
//        items = listOfItems
//
//        items.forEach {
//            pieItemsData.add(PieChart.Data(it.itemName.value, it.itemPrice.value.toDouble()))
//        }
//    }
//
////    /**
////     * Adds a new expense entry.
////     * @param newEntryDate The date of the new entry.
////     * @param newItem The name of the item.
////     * @param newPrice The price of the item.
////     * @return The created [ExpensesEntry] object.
////     */
////    fun add(newEntryDate: LocalDate, newItem: String, newPrice: Double): ExpensesEntry {
////        val newEntry = execute {
//////            ExpensesEntryTbl.insert {
//////                it[entryDate] = newEntryDate
//////                it[itemName] = newItem
//////                it[itemPrice] = BigDecimal.valueOf(newPrice)
//////            }
////        }
////        val newEntryAsExpensesEntry = ExpensesEntry(newEntry[ExpensesEntryTbl.id], newEntryDate, newItem, newPrice)
////        listOfItems.add(ExpensesEntryModel().apply { item = newEntryAsExpensesEntry })
////        pieItemsData.add(PieChart.Data(newItem, newPrice))
////        return newEntryAsExpensesEntry
////    }
//
//    /**
//     * Updates an existing expense entry.
//     * @param updatedItem The [ExpensesEntryModel] object containing the updated values.
//     * @return The number of rows affected in the database.
//     */
//    fun update(updatedItem: ExpensesEntryModel): Int {
//        return execute {
//            ExpensesEntryTbl.update ({ ExpensesEntryTbl.id eq(updatedItem.id.value.toInt()) }) {
//                it[entryDate] = updatedItem.entryDate.value
//                it[itemName] = updatedItem.itemName.value
//                it[itemPrice] = BigDecimal.valueOf(updatedItem.itemPrice.value.toDouble())
//            }
//        }
//    }
//
//    /**
//     * Deletes an expense entry.
//     * @param model The [ExpensesEntryModel] object to be deleted.
//     */
//    fun delete(model: ExpensesEntryModel) {
//        execute {
//            ExpensesEntryTbl.deleteWhere {
//                ExpensesEntryTbl.id eq(model.id.value.toInt())
//            }
//        }
//        listOfItems.remove(model)
//        removeModelFromPie(model)
//    }
//
//    /**
//     * Updates the corresponding pie chart data for a specific expense entry.
//     * @param model The [ExpensesEntryModel] object to update the pie chart data for.
//     */
//    fun updatePiecePie(model: ExpensesEntryModel) {
//        val modelId = model.id
//        var currentIndex: Int
//        items.forEachIndexed { index, data ->
//            if (modelId == data.id) {
//                //we have the right object to update
//                currentIndex = index
//                pieItemsData[currentIndex].name = data.itemName.value
//                pieItemsData[currentIndex].pieValue = data.itemPrice.value.toDouble()
//            } else {
//                // Ignore
//            }
//        }
//    }
//
//    /**
//     * Removes an expense entry from the pie chart data.
//     * @param model The [ExpensesEntryModel] object to be removed from the pie chart.
//     */
//    private fun removeModelFromPie(model: ExpensesEntryModel) {
//        var currentIndex = 0
//        pieItemsData.forEachIndexed { index, data ->
//            if (data.name == model.itemName.value && index != -1)
//                currentIndex = index
//        }
//        pieItemsData.removeAt(currentIndex)
//    }
//
//
//    /**
//     * Filters expense entries by a specific entry date.
//     * @param today The entry date to filter by.
//     * @return An [ObservableList] of filtered [ExpensesEntryModel] objects.
//     */
//    fun filterByEntryDates(today: LocalDate?): ObservableList<ExpensesEntryModel> = execute {
//        ExpensesEntryTbl
//            .select { ExpensesEntryTbl.entryDate eq today!! }
//            .map {
//                ExpensesEntryModel().apply {
//                    item = it.toExpensesEntry()
//                }
//            }.asObservable()
//    }
//
//    /**
//     * Filters the expenses by the specified date range.
//     *
//     * @param startDate The start date of the range.
//     * @param endDate The end date of the range.
//     * @return A list of expenses within the specified date range.
//     */
//    fun filterByDateRange(startDate: LocalDate, endDate: LocalDate): List<ExpensesEntryModel> {
//    fun filterByDateRange(startDate: LocalDate, endDate: LocalDate): List<ExpensesEntryModel> {
//        return items.filter { entry ->
//            val entryDate = entry.entryDate.value
//            entryDate in startDate..endDate
//        }
//    }
//}