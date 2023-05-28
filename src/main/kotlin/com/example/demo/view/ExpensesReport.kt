package com.example.demo.view

//import com.example.demo.controller.ItemController
import com.example.demo.controller.LoginController
import com.example.demo.model.ExpensesEntryJsonModel
import com.example.demo.model.ExpensesEntryModel
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleDoubleProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.chart.PieChart
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*
import java.time.LocalDate

/**
 * The ExpensesReport view displays a report of expenses based on selected filtering options.
 * It shows a table with filtered expense entries and a pie chart representing the distribution of expenses.
 * The user can filter the report by selecting different time periods from the combo box.
 */
class ExpensesReport : View("Report") {
//    val controller : ItemController by inject()
    val controller : LoginController by inject()
    var comboBox: ComboBox<String> by singleAssign()

//    private var listOfItems: ObservableList<ExpensesEntryJsonModel> by singleAssign()
    private var listOfItems: ObservableList<ExpensesEntryJsonModel> by singleAssign()
    private var pieData = FXCollections.observableArrayList<PieChart.Data>()

    private var totalExpensesLabel: Label by singleAssign()
    private val totalExpensesProperty = SimpleDoubleProperty(1.0)

    val today = LocalDate.of(LocalDate.now().year, LocalDate.now().month, LocalDate.now().dayOfMonth)
    init {
        listOfItems = controller.filterByEntryDates(today)
        // Initialize pieData with initial items
        listOfItems.forEach {
            pieData.add(PieChart.Data(it.itemName.value, it.itemPrice.value.toDouble()))
        }
        updateTotalExpenses()
    }
    override val root = borderpane {
        disableDelete()
        disableSave()
        disableCreate()
        disableClose()
        disableRefresh()


        left{
            form {
                fieldset {
                    field("Filter By:") {
                        maxWidth = 200.0
                        paddingRight = 20.0
                        comboBox = combobox(values = listOf("Today",
                            "Yesterday", "Week", "Month", "Year")) {
                            prefWidth = 135.0
                            value = "Today"
                            this.valueProperty().onChange {
                                getFilteredItems(it)
                                updatePie()
                                updateTotalExpenses()
                            }

                        }
                    }
                }
            }
        }

        center {
            paddingBottom = 20.0
            paddingTop = 20.0
//            paddingLeft = 20.0
            tableview<ExpensesEntryJsonModel> {
                items = listOfItems
                column("Entry Date:", ExpensesEntryJsonModel::entryDate)
                column("Item", ExpensesEntryJsonModel::itemName)
                column("Price", ExpensesEntryJsonModel::itemPrice)
            }
        }

        right = vbox(10) {
            alignment = Pos.CENTER
            paddingBottom = 10.0

            piechart("Expenses Report", data = pieData)

            totalExpensesLabel = label {
//                vboxConstraints { marginTop = 20.0 }
                if (totalExpensesProperty.doubleValue() != 0.0) {
                    style {
                        fontSize = 19.px
                        padding = box(10.px)
                        textFill = Color.GREEN
                        fontWeight = FontWeight.EXTRA_BOLD
                        borderRadius = multi(box(8.px))
                        backgroundColor += c("white", 0.8)

                    }
                    bind(Bindings.concat("Total Expenses: ", "$ ", Bindings.format("%.2f", totalExpensesProperty)))
                } else {
                    // do nothing
                }
            }
        }

    }

    /**
     * Updates the list of filtered expense items based on the selected choice from the combo box.
     * Calls the corresponding filter function in the controller to retrieve the filtered items.
     * Updates the pie chart after filtering the items.
     */
    private fun getFilteredItems(choice: String?) {
        when (choice) {
            "Today" -> listOfItems.setAll(controller.filterByEntryDates(today))
            "Yesterday" -> listOfItems.setAll(controller.filterByEntryDates(today.minusDays(1)))
//            "Week" -> listOfItems.setAll(controller.filterByEntryDates(today.minusWeeks(1)))
            "Week" -> {
//                val startDate = today.minusWeeks(1).plusDays(0) // Start from one week ago, excluding today
                val startDate = today.minusWeeks(1) // Start from one week ago, excluding today
                val endDate = today // Today's date
                listOfItems.setAll(controller.filterByDateRange(startDate, endDate))
            }

//            "Month" -> listOfItems.setAll(controller.filterByEntryDates(today.minusMonths(1)))
            "Month" -> {
                val startDate = today.minusMonths(1) // Start from one Month ago
                val endDate = today // Today's date
                listOfItems.setAll(controller.filterByDateRange(startDate, endDate))
            }

//            "Year" -> listOfItems.setAll(controller.filterByEntryDates(today.minusYears(1)))
            "Year" -> {
                val startDate = today.minusYears(1) // Start from one Month ago
                val endDate = today // Today's date
                listOfItems.setAll(controller.filterByDateRange(startDate, endDate))
            }
        }
        updateTotalExpenses()

        updatePie()
        listOfItems.forEach {
            pieData.setAll()
        }


    }

    /**
     * Updates the total expenses by calculating the sum of all expense entries.
     * Sets the total expenses property and updates the model's total expenses value.
     */
    private fun updateTotalExpenses() {
        var total = 0.0
        try {
            // fetch all expenses
            listOfItems.forEach {
                total += (it.itemPrice.value.toDouble())
            }
            totalExpensesProperty.set(total)
        }catch ( e: Exception) { totalExpensesProperty.set(0.0) }
    }

    /**
     * Updates the data in the pie chart based on the current list of expense items.
     */
    private fun updatePie() {
        listOfItems.forEach {
            pieData.add(PieChart.Data(it.itemName.value, it.itemPrice.value.toDouble()))
        }
    }

    // to override the buttons on the top


//    override fun onRefresh() {
//        super.onRefresh()
//    }
//
//    override fun onDelete() {
//        super.onDelete()
//    }
//
//    override fun onSave() {
//        super.onSave()
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//    }

    fun clearReport() {
        pieData.clear()
    }
}
