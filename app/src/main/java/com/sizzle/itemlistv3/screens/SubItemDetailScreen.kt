package com.sizzle.itemlistv3.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sizzle.itemlistv3.MainViewModel
import com.sizzle.itemlistv3.NavRoutes
import com.sizzle.itemlistv3.R
import com.sizzle.itemlistv3.models.Item
import com.sizzle.itemlistv3.models.SubItem
import com.sizzle.itemlistv3.utilities.SpacerDividerSpacer

//---------------------------------------------------------
// Items Detail Screen
//---------------------------------------------------------
@Composable
fun SubItemDetailScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    id: Int? = 0,
    subID: Int? = 0,
    crud: String? = "NONE"
) {

    //--- Local Variables
    var itemID: String
    var itemTitle: String
    var myItem: Item
    var subItemID: String
    var subItemTitle: String
    var subItemIDLink: String
    var mySubItem: SubItem

    //--- Scaffolding
    val scaffoldState =	rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    Scaffold(
        scaffoldState = scaffoldState,

        //-----------------------------------------------------------------
        // Top Bar
        //-----------------------------------------------------------------
        topBar = { SubDetailsTopBar(navController) },

        //-----------------------------------------------------------------
        // Bottom Bar
        //-----------------------------------------------------------------
        bottomBar = { SubDetailsBottomBar() },

        //-----------------------------------------------------------------
        // Content
        //-----------------------------------------------------------------
        content = { padding ->

            when (crud) {
                //-------------------------------------------------
                // Create New SubItem
                //-------------------------------------------------
                "CREATE" -> {

                    //--- Make sure id isn't null, because it's not guaranteed
                    if (id != null) {

                        //--- Populate specific item view model variable
                        viewModel.getItemByID(id)

                        //--- Get specific item info
                        myItem = viewModel.specificItem
                        itemID = myItem.id.toString()
                        itemTitle = myItem.title

                        //--- Content Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            elevation = 10.dp
                        ) {

                            //--- Content Column
                            Column(
                                modifier = Modifier.padding(5.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                //--- Card Title
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    text = "Add New SubItem for $itemTitle ($itemID)",
                                    style = MaterialTheme.typography.h4
                                )
                                SpacerDividerSpacer()

                                //--- SubItem Title
                                var textSubTitle by remember { mutableStateOf(TextFieldValue("")) }
                                OutlinedTextField(
                                    value = textSubTitle,
                                    label = { Text(text = "SubTitle") },
                                    singleLine = true,
                                    onValueChange = {
                                        textSubTitle = it
                                    }
                                )
                                subItemTitle = textSubTitle.text

                                //--- Create New Item from Parts
                                mySubItem = SubItem(
                                    sub_id = (100000..200000).random(),
                                    sub_title = subItemTitle,
                                    item_id_link = itemID.toInt()
                                )

                                SpacerDividerSpacer()

                                //--- Buttons Row
                                Row {

                                    //--- Submit Button
                                    Button(
                                        onClick = {
                                            viewModel.insertSubItem(mySubItem)    // Add SubItem to SubList
                                            navController.navigate(               // Go to Item Details Screen
                                                route = NavRoutes.ItemDetailScreen.route
                                                        + "/" + id
                                                        + "/" + "READ"
                                            )
                                            viewModel.updateSubItemShowList(itemID.toInt())         // Triggers SubItem List Recompose
                                            viewModel.updateItemShowList()                          // Maintains Item List Filter & Sort State
                                        }) {
                                        Text(
                                            text = "Submit",
                                            style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.background),
                                        )
                                    }    // End Submit Button

                                    Spacer(Modifier.width(10.dp))

                                    //--- Back Button
                                    Button(
                                        onClick = {
                                            navController.navigate(             // Go to Item Details Screen
                                                route = NavRoutes.ItemDetailScreen.route
                                                        + "/" + itemID
                                                        + "/" + "READ"
                                            ) {
                                                // Remove this Update Screen from Navigation Stack
                                                popUpTo(route = NavRoutes.ItemDetailScreen.route
                                                        + "/" + itemID
                                                        + "/" + "READ") {
                                                    inclusive = true
                                                }
                                            }
                                            viewModel.updateItemShowList()                          // Maintains Item List Filter & Sort State
                                        }) {
                                        Text(
                                            text = "Back",
                                            style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.background),
                                        )
                                    }    // End Back Button

                                }    // End Buttons Row

                            }    // End Column

                        }	// End Card

                    }      // End If Not Null

                }	// End Create

                //-------------------------------------------------
                // Read SubItem
                //-------------------------------------------------
                "READ" -> {

                    //--- Make sure id isn't null, because it's not guaranteed
                    if (subID != null) {

                        //--- Populate specific item view model variable
                        viewModel.getSubItemBySubItemID(subID)

                        //--- Get specific sub item info
                        mySubItem = viewModel.specificSubItem
                        subItemID = mySubItem.sub_id.toString()
                        subItemTitle = mySubItem.sub_title
                        subItemIDLink = mySubItem.item_id_link.toString()

                        //--- Content Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            elevation = 10.dp
                        ) {

                            //--- Content Column
                            Column(
                                modifier = Modifier.padding(5.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                //--- Card Title
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    text = "View SubItem",
                                    style = MaterialTheme.typography.h4
                                )
                                SpacerDividerSpacer()

                                //--- SubItem ID Text Field
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    text = "SubItem ID: $subItemID",
                                    style = MaterialTheme.typography.body1
                                )

                                //--- SubItem Title Text Field
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    text = subItemTitle,
                                    style = MaterialTheme.typography.h5
                                )

                                SpacerDividerSpacer()

                            }   // End Column

                        }	// End Card

                    }   // End If Not Null

                }	// End Read

                //-------------------------------------------------
                // Update SubItem
                //-------------------------------------------------
                "UPDATE" -> {

                    //--- Make sure id isn't null, because it's not guaranteed
                    if (subID != null) {

                        //--- Populate specific item view model variable
                        viewModel.getSubItemBySubItemID(subID)

                        //--- Get specific sub item info
                        mySubItem = viewModel.specificSubItem
                        subItemID = mySubItem.sub_id.toString()
                        subItemTitle = mySubItem.sub_title
                        subItemIDLink = mySubItem.item_id_link.toString()

                        //--- Content Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            elevation = 10.dp
                        ) {

                            //--- Content Column
                            Column(
                                modifier = Modifier.padding(5.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                //--- Card Title
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    text = "Update Item",
                                    style = MaterialTheme.typography.h4
                                )
                                SpacerDividerSpacer()

                                //--- SubItem Title
                                var textSubTitle by remember { mutableStateOf(TextFieldValue(subItemTitle)) }
                                OutlinedTextField(
                                    value = textSubTitle,
                                    label = { Text(text = "SubTitle") },
                                    singleLine = true,
                                    onValueChange = {
                                        textSubTitle = it
                                    }
                                )
                                subItemTitle = textSubTitle.text

                                //--- Create Updated Item from Parts
                                mySubItem = SubItem(
                                    sub_id = subItemID.toInt(),
                                    sub_title = subItemTitle,
                                    item_id_link = subItemIDLink.toInt()
                                )

                                SpacerDividerSpacer()

                                //--- Buttons Row
                                Row {

                                    //--- Update Existing Item Button
                                    Button(
                                        onClick = {
                                            navController.navigate(             // Go to Item Details Screen
                                                route = NavRoutes.ItemDetailScreen.route
                                                        + "/" + subItemIDLink
                                                        + "/" + "READ"
                                            ) {
                                                // Remove this Update Screen from Navigation Stack
                                                popUpTo(route = NavRoutes.ItemDetailScreen.route
                                                        + "/" + subItemIDLink
                                                        + "/" + "READ") {
                                                    inclusive = true
                                                }
                                            }
                                            viewModel.updateSubItem(mySubItem)        // Update SubItem in List
                                            viewModel.updateSubItemShowList(subItemIDLink.toInt())            // Triggers Item List Recompose
                                            viewModel.updateItemShowList()                          // Maintains Item List Filter & Sort State
                                        }) {
                                        Text(
                                            text = "Submit",
                                            style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.background),
                                        )
                                    }    // End Update Existing Item Button

                                    Spacer(Modifier.width(10.dp))

                                    //--- Back Button
                                    Button(
                                        onClick = {
                                            navController.navigate(             // Go to Item Details Screen
                                                route = NavRoutes.ItemDetailScreen.route
                                                        + "/" + subItemIDLink
                                                        + "/" + "READ"
                                            ) {
                                                // Remove this Update Screen from Navigation Stack
                                                popUpTo(route = NavRoutes.ItemDetailScreen.route
                                                        + "/" + subItemIDLink
                                                        + "/" + "READ") {
                                                    inclusive = true
                                                }
                                            }
                                            viewModel.updateItemShowList()                          // Maintains Item List Filter & Sort State
                                        }) {
                                        Text(
                                            text = "Back",
                                            style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.background),
                                        )
                                    }    // End Back Button

                                }    // End Buttons Row

                            }   // End Column

                        }	// End Card

                    }   // End If Not Null

                }   // End Update

                //-------------------------------------------------
                // Delete SubItem
                //-------------------------------------------------
                "DELETE" -> {

                    //--- Make sure id isn't null, because it's not guaranteed
                    if (subID != null) {

                        //--- Populate specific item view model variable
                        viewModel.getSubItemBySubItemID(subID)

                        //--- Get specific item info
                        mySubItem = viewModel.specificSubItem
                        subItemID = mySubItem.sub_id.toString()
                        subItemTitle = mySubItem.sub_title
                        subItemIDLink = mySubItem.item_id_link.toString()

                        //--- Content Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            elevation = 10.dp
                        ) {

                            //--- Content Column
                            Column(
                                modifier = Modifier.padding(5.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                //--- Card Title
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    text = "Delete SubItem",
                                    style = MaterialTheme.typography.h4
                                )
                                SpacerDividerSpacer()

                                //--- Delete Confirmation
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    text = "Are you sure you want to permanently delete:\n\n",
                                    style = MaterialTheme.typography.body1
                                )
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    text = "$subItemTitle\n",
                                    style = MaterialTheme.typography.h5
                                )

                                //--- Create SubItem from Parts
                                mySubItem = SubItem(
                                    sub_id = subItemID.toInt(),
                                    sub_title = subItemTitle,
                                    item_id_link = subItemIDLink.toInt()
                                )

                                SpacerDividerSpacer()

                                //--- Buttons Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {

                                    //--- Delete Button
                                    Button(
                                        onClick = {
                                            viewModel.deleteSubItem(mySubItem)  // Delete SubItem
                                            navController.navigate(             // Go to Item Details Screen
                                                route = NavRoutes.ItemDetailScreen.route
                                                        + "/" + subItemIDLink
                                                        + "/" + "READ"
                                            )
                                            viewModel.updateSubItemShowList(mySubItem.item_id_link)     // Triggers SubItem List Recompose
                                            viewModel.updateItemShowList()                          // Maintains Item List Filter & Sort State
                                        }) {
                                        Text(
                                            text = "Delete",
                                            style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.background),
                                        )
                                    }    // End Update Existing Item Button

                                    Spacer(Modifier.width(10.dp))

                                    //--- Cancel Button
                                    Button(
                                        onClick = {
                                            navController.navigate(             // Go to Item Details Screen
                                                route = NavRoutes.ItemDetailScreen.route
                                                        + "/" + subItemIDLink
                                                        + "/" + "READ"
                                            ) {
                                                navController.navigate(             // Go to Item Details Screen
                                                    route = NavRoutes.ItemDetailScreen.route
                                                            + "/" + subItemIDLink
                                                            + "/" + "READ"
                                                )
                                            }
                                            viewModel.updateItemShowList()                          // Maintains Item List Filter & Sort State
                                        }) {
                                        Text(
                                            text = "Cancel",
                                            style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.background),
                                        )
                                    }    // End Cancel Button

                                }    // End Buttons Row

                            }   // End Column

                        }	// End Card

                    }   // End If Not Null


                }   // End Delete

                //-------------------------------------------------
                // Else
                //-------------------------------------------------
                else -> {
                    // Nothing Else To Do
                }	// End Else

            }   // End When

        },	// End Content

    )   // End Scaffold

}   // End SubItem Details Screen Function


//---------------------------------------------------------
// Top Bar
//---------------------------------------------------------
@Composable
fun SubDetailsTopBar(navController: NavHostController) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.h5
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate(route = NavRoutes.ItemListScreen.route) {
                    popUpTo(NavRoutes.ItemListScreen.route)
                }
            }) {
                Icon(Icons.Filled.ArrowBack, "Back")
            }
        }	// End Navigation Icon
    )	// End Top App Bar
}	// End Details Top Bar Function


//---------------------------------------------------------
// Bottom Bar
//---------------------------------------------------------
@Composable
fun SubDetailsBottomBar() {
    BottomAppBar {
        Box(modifier = Modifier
            .fillMaxWidth(),
            contentAlignment = Alignment.BottomStart) {
            Text(
                text = "  " + stringResource(R.string.app_copyright),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }	// End Bottom App Bar
}	// End Details Bottom Bar Function
