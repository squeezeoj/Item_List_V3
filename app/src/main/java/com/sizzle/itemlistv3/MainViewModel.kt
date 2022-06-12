package com.sizzle.itemlistv3

import com.sizzle.itemlistv3.models.Item
import com.sizzle.itemlistv3.models.SubItem

//----------------------------------------------------------
// Main View Model
//----------------------------------------------------------
class MainViewModel {

    //------------------------------------------------------
    // Items
    //------------------------------------------------------
    private var allItems: List<Item>	// All Items List
    var showItems: List<Item>		    // Show Items List
    var specificItem: Item              // Specific Item

    //------------------------------------------------------
    // Sub Items
    //------------------------------------------------------
    private var allSubItems: List<SubItem>  // All SubItems List
    var showSubItems: List<SubItem>		    // Show SubItems List
    var specificSubItem: SubItem            // Specific SubItem

    //------------------------------------------------------
    // Filtering Variables
    //------------------------------------------------------
    var filtering: Boolean			// Filtering Boolean
    var filterText: String			// Filter Text

    //------------------------------------------------------
    // Sorting Variables
    //------------------------------------------------------
    var sortAscending: Boolean		// Sort Order

    //------------------------------------------------------
    // Initialize View Model
    //------------------------------------------------------
    init {

        val item01 = Item(id = 1, title = "First Item")
        val item02 = Item(id = 2, title = "Second Item")
        val item03 = Item(id = 3, title = "Third Item")
        val item04 = Item(id = 4, title = "Fourth Item")
        val item05 = Item(id = 5, title = "Fifth Item")

        allItems = mutableListOf(item01, item02, item03, item04, item05)
        specificItem = Item(id = 0, title = "Initial Item")
        showItems = allItems		//	Start by Showing All Items


        val subItem0101 = SubItem(sub_id = 1, sub_title = "First Item's First SubItem", item_id_link = 1)
        val subItem0102 = SubItem(sub_id = 2, sub_title = "First Item's Second SubItem", item_id_link = 1)
        val subItem0103 = SubItem(sub_id = 3, sub_title = "First Item's Third SubItem", item_id_link = 1)

        val subItem0201 = SubItem(sub_id = 4, sub_title = "Second Item's First SubItem", item_id_link = 2)
        val subItem0202 = SubItem(sub_id = 5, sub_title = "Second Item's Second SubItem", item_id_link = 2)
        val subItem0203 = SubItem(sub_id = 6, sub_title = "Second Item's Third SubItem", item_id_link = 2)

        val subItem0301 = SubItem(sub_id = 7, sub_title = "Third Item's First SubItem", item_id_link = 3)
        val subItem0302 = SubItem(sub_id = 8, sub_title = "Third Item's Second SubItem", item_id_link = 3)
        val subItem0303 = SubItem(sub_id = 9, sub_title = "Third Item's Third SubItem", item_id_link = 3)

        allSubItems = mutableListOf(subItem0101, subItem0102, subItem0103, subItem0201, subItem0202, subItem0203, subItem0301, subItem0302, subItem0303)
        specificSubItem = SubItem(sub_id = 0, sub_title = "Initial SubItem", item_id_link = 0)
        showSubItems = allSubItems		//	Start by Showing All SubItems


        filtering = false
        filterText = ""

        sortAscending = true

    }   // End Initializer

    //------------------------------------------------------
    // Item Methods
    //------------------------------------------------------
    fun insertItem(item: Item) {
        allItems = allItems + item
    }

    fun updateItem(item: Item) {
        allItems.find { it.id == item.id }?.title = item.title
    }

    fun updateItemShowList() {		// Used on Item Details Screen

        var tempItems: List<Item> = if (filtering) {
            allItems.filter { it.title.contains(filterText) }
        } else {
            allItems
        }

        tempItems = if (sortAscending) {
            tempItems.sortedBy { it.id }
        } else {
            tempItems.sortedByDescending { it.id }
        }

        showItems = tempItems

    }

    fun returnItemShowList():List<Item> {	// Used on Item List Screen

        var tempItems: List<Item> = if (filtering) {
            allItems.filter { it.title.contains(filterText) }
        } else {
            allItems
        }

        tempItems = if (sortAscending) {
            tempItems.sortedBy { it.id }
        } else {
            tempItems.sortedByDescending { it.id }
        }

        return tempItems

    }

    fun getItemByID(id: Int) {
        allItems.forEach {
            if (it.id == id) {
                specificItem.id = it.id
                specificItem.title = it.title
            }
        }
    }

    fun deleteItem(item: Item) {

        //--- Delete All SubItems Associated with this Item
        allSubItems.forEach {
            if (it.item_id_link == item.id) {
                val mySubItemID: Int = it.sub_id
                val mySubItemTitle: String = it.sub_title
                val mySubItemIDLink: Int = it.item_id_link
                val mySubItem = SubItem(mySubItemID, mySubItemTitle, mySubItemIDLink)
                allSubItems = allSubItems - mySubItem
            }
        }

        //--- Delete this Item
        allItems = allItems - item

    }


    //------------------------------------------------------
    // SubItem Methods
    //------------------------------------------------------
    fun insertSubItem(subItem: SubItem) {
        allSubItems = allSubItems + subItem
    }

    fun updateSubItem(subItem: SubItem) {
        allSubItems.find { it.sub_id == subItem.sub_id }?.sub_title = subItem.sub_title
    }

    fun updateSubItemShowList(id: Int) {
        showSubItems = allSubItems.filter { it.item_id_link == id }
    }

    fun returnSubItemShowList(id: Int):List<SubItem> {
        showSubItems = allSubItems.filter { it.item_id_link == id }
        return showSubItems

    }

    fun getSubItemBySubItemID(sub_id: Int) {
        allSubItems.forEach {
            if (it.sub_id == sub_id) {
                specificSubItem.sub_id = it.sub_id
                specificSubItem.sub_title = it.sub_title
                specificSubItem.item_id_link = it.item_id_link
            }
        }
    }

    fun deleteSubItem(subItem: SubItem) {
        allSubItems = allSubItems - subItem
    }

}   // End Main View Model
