package com.sizzle.itemlistv3.screens

//---------------------------------------------------------
// Imports
//---------------------------------------------------------
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sizzle.itemlistv3.NavRoutes
import com.sizzle.itemlistv3.R
import com.sizzle.itemlistv3.models.Item
import com.sizzle.itemlistv3.MainViewModel
import com.sizzle.itemlistv3.models.SubItem
import com.sizzle.itemlistv3.utilities.SpacerDividerSpacer

//---------------------------------------------------------
// Items Detail Screen
//---------------------------------------------------------
@Composable
fun ItemDetailScreen(
	navController: NavHostController,
	viewModel: MainViewModel,
	id: Int? = 0,
	crud: String? = "NONE"
) {

	//--- Local Variables
	var itemID: String
	var itemTitle: String
	var myItem: Item

	//--- Scaffolding
	val scaffoldState =	rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
	Scaffold(
		scaffoldState = scaffoldState,

		//-----------------------------------------------------------------
		// Top Bar
		//-----------------------------------------------------------------
		topBar = { DetailsTopBar(navController) },

		//-----------------------------------------------------------------
		// Bottom Bar
		//-----------------------------------------------------------------
		bottomBar = { DetailsBottomBar() },

		//-----------------------------------------------------------------
		// Content
		//-----------------------------------------------------------------
		content = {	padding ->

			when (crud) {
				//-------------------------------------------------
				// Create New Item
				//-------------------------------------------------
				"CREATE" -> {

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
							horizontalAlignment = CenterHorizontally
						) {

							//--- Card Title
							Text(
								modifier = Modifier.fillMaxWidth(),
								textAlign = TextAlign.Center,
								text = "Add New Item",
								style = MaterialTheme.typography.h4
							)
							SpacerDividerSpacer()

							//--- Item Title
							var textTitle by remember { mutableStateOf(TextFieldValue("")) }
							OutlinedTextField(
								value = textTitle,
								label = { Text(text = "Title") },
								singleLine = true,
								onValueChange = {
									textTitle = it
								}
							)
							itemTitle = textTitle.text

							//--- Create New Item from Parts
							myItem = Item(
								id = (100000..200000).random(),
								title = itemTitle
							)

							SpacerDividerSpacer()

							//--- Buttons Row
							Row {

								//--- Submit Button
								Button(
									onClick = {
										navController.navigate(route = NavRoutes.ItemListScreen.route) {
											popUpTo(NavRoutes.ItemListScreen.route)
										}                                    // Go to Item List Screen
										viewModel.insertItem(myItem)        // Add Item to List
										viewModel.updateItemShowList()            // Triggers Item List Recompose
									}) {
									Text(
										text = "Submit",
										style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.background),
									)
								}    // End Submit Button

								Spacer(Modifier.width(10.dp))

								//--- Cancel Button
								Button(
									onClick = {
										navController.navigate(route = NavRoutes.ItemListScreen.route) {
											popUpTo(NavRoutes.ItemListScreen.route)
										}                                    // Go to Item List Screen
									}) {
									Text(
										text = "Cancel",
										style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.background),
									)
								}    // End Cancel Button

							}    // End Buttons Row

						}    // End Column

					}	// End Card

				}	// End Create

				//-------------------------------------------------
				// Read Item
				//-------------------------------------------------
				"READ" -> {

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
								horizontalAlignment = CenterHorizontally
							) {

								//--- Card Title
								Text(
									modifier = Modifier.fillMaxWidth(),
									textAlign = TextAlign.Center,
									text = "View Item",
									style = MaterialTheme.typography.h4
								)
								SpacerDividerSpacer()

								//--- Item ID Text Field
								Text(
									modifier = Modifier.fillMaxWidth(),
									textAlign = TextAlign.Center,
									text = "Item: $itemID",
									style = MaterialTheme.typography.body1
								)

								//--- Item Title Text Field
								Text(
									modifier = Modifier.fillMaxWidth(),
									textAlign = TextAlign.Center,
									text = "Title: $itemTitle",
									style = MaterialTheme.typography.h5
								)

								SpacerDividerSpacer()

								//--- SubItems for this Item
								val showSubItemList: List<SubItem> = viewModel.returnSubItemShowList(itemID.toInt())
								LazyColumn {
									items(showSubItemList.size) { subItem ->
										EachSubItemCard(
											viewModel = viewModel,
											navController = navController,
											itemID = itemID.toInt(),
											itemTitle = itemTitle,
											subItemID = showSubItemList[subItem].sub_id,
											subItemTitle = showSubItemList[subItem].sub_title
										)
									}    // End Items
								}    // End Lazy Column

								SpacerDividerSpacer()

								//--- Buttons Row
								Row {

									//--- Create New SubItem Button
									Button(
										onClick = {
											navController.navigate(            // Go to Item Details Screen
												route = NavRoutes.SubItemDetailScreen.route
														+ "/" + id
														+ "/" + 0
														+ "/" + "CREATE"
											)
										}) {
										Text(
											text = "New SubItem",
											style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.background),
										)
									}    // End Update Existing Item Button

									Spacer(Modifier.width(10.dp))

									//--- Cancel Button
									Button(
										onClick = {
											navController.navigate(route = NavRoutes.ItemListScreen.route) {
												popUpTo(NavRoutes.ItemListScreen.route)
											}                                    // Go to Item List Screen
											viewModel.updateItemShowList()       // Maintains Item List Filter & Sort State
										}) {
										Text(
											text = "Back",
											style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.background),
										)
									}    // End Cancel Button

								}    // End Buttons Row

							}   // End Column

						}	// End Card

					}   // End If Not Null

				}	// End Read

				//-------------------------------------------------
				// Update Existing Item
				//-------------------------------------------------
				"UPDATE" -> {

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
								horizontalAlignment = CenterHorizontally
							) {

								//--- Card Title
								Text(
									modifier = Modifier.fillMaxWidth(),
									textAlign = TextAlign.Center,
									text = "Update Item",
									style = MaterialTheme.typography.h4
								)
								SpacerDividerSpacer()

								//--- Item ID Text Field (read only)
								OutlinedTextField(
									value = itemID,
									enabled = false,
									readOnly = true,
									label = { Text(text = "ID") },
									singleLine = true,
									onValueChange = { }
								)

								//--- Item Title Text Field
								var textTitle by remember { mutableStateOf(TextFieldValue(itemTitle)) }
								OutlinedTextField(
									value = textTitle,
									label = { Text(text = "Title") },
									singleLine = true,
									onValueChange = {
										textTitle = it
									}
								)
								itemTitle = textTitle.text

								//--- Create Updated Item from Parts
								myItem = Item(
									id = itemID.toInt(),
									title = itemTitle
								)

								SpacerDividerSpacer()

								//--- Buttons Row
								Row {

									//--- Update Existing Item Button
									Button(
										onClick = {
											navController.navigate(route = NavRoutes.ItemListScreen.route) {
												popUpTo(NavRoutes.ItemListScreen.route)
											}                                    // Go to Item List Screen
											viewModel.updateItem(myItem)        // Update Item in List
											viewModel.updateItemShowList()            // Triggers Item List Recompose
										}) {
										Text(
											text = "Submit",
											style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.background),
										)
									}    // End Update Existing Item Button

									Spacer(Modifier.width(10.dp))

									//--- Cancel Button
									Button(
										onClick = {
											navController.navigate(route = NavRoutes.ItemListScreen.route) {
												popUpTo(NavRoutes.ItemListScreen.route)
											}                                    // Go to Item List Screen
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

				}	// End Update

				//-------------------------------------------------
				// Delete Item
				//-------------------------------------------------
				"DELETE" -> {

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
								horizontalAlignment = CenterHorizontally
							) {

								//--- Card Title
								Text(
									modifier = Modifier.fillMaxWidth(),
									textAlign = TextAlign.Center,
									text = "Delete Item",
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
									text = "$itemTitle\n",
									style = MaterialTheme.typography.h5
								)

								//--- Create Updated Item from Parts
								myItem = Item(
									id = itemID.toInt(),
									title = itemTitle
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
											navController.navigate(route = NavRoutes.ItemListScreen.route) {
												popUpTo(NavRoutes.ItemListScreen.route)
											}                                    // Go to Item List Screen
											viewModel.deleteItem(myItem)        // Delete Item in List
											viewModel.updateItemShowList()            // Triggers Item List Recompose
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
											navController.navigate(route = NavRoutes.ItemListScreen.route) {
												popUpTo(NavRoutes.ItemListScreen.route)
											}                                    // Go to Item List Screen
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

				}	// End Delete

				//-------------------------------------------------
				// Else
				//-------------------------------------------------
				else -> {
					// Nothing Else To Do
				}	// End Else

			}   // End When

		},	// End Content

	)	// End Scaffold

}   	// End Item Details Screen


//---------------------------------------------------------
// Each SubItem Card
//---------------------------------------------------------
@Composable
fun EachSubItemCard(
	navController: NavHostController,
	viewModel: MainViewModel,
	itemID: Int,
	itemTitle: String,
	subItemID: Int,
	subItemTitle: String
) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(5.dp)
			.clickable {
				viewModel.getSubItemBySubItemID(subItemID)
				navController.navigate(            // Go to SubItem Details Screen
					route = NavRoutes.SubItemDetailScreen.route
							+ "/" + itemID
							+ "/" + subItemID
							+ "/" + "READ"
				)
			},
		elevation = 10.dp
	) {

		Row {

			//--- Text Column
			Column(
				modifier = Modifier.padding(5.dp),
				verticalArrangement = Arrangement.Top,
				horizontalAlignment = Alignment.Start,
			) {
				Text(text = "SubItem $subItemID", style = MaterialTheme.typography.body2)
				Text(text = subItemTitle, style = MaterialTheme.typography.body1)
			}    // End Text Column

			//--- Dynamic Width Spacer Between Item Title and Icons
			Spacer(Modifier.weight(1f))

			//--- Edit Icon
			IconButton(
				onClick = {
					viewModel.getSubItemBySubItemID(subItemID)
					navController.navigate(            // Go to SubItem Details Screen
						route = NavRoutes.SubItemDetailScreen.route
								+ "/" + itemID
								+ "/" + subItemID
								+ "/" + "UPDATE"
					)
				},
			) {
				Icon(
					Icons.Rounded.Edit,
					tint = MaterialTheme.colors.onBackground,
					contentDescription = "Edit"
				)
			}    // End Edit Icon Button
			Spacer(Modifier.width(5.dp))

			//--- Delete Icon
			IconButton(
				onClick = {
					viewModel.getSubItemBySubItemID(subItemID)
					navController.navigate(            // Go to SubItem Details Screen
						route = NavRoutes.SubItemDetailScreen.route
								+ "/" + itemID
								+ "/" + subItemID
								+ "/" + "DELETE"
					)
				},
			) {
				Icon(
					Icons.Rounded.Delete,
					tint = MaterialTheme.colors.onBackground,
					contentDescription = "Edit"
				)
			}    // End Delete Icon Button

		}	// End Row

	}	// End Card

}	// End Each SubItem Card Function


//---------------------------------------------------------
// Top Bar
//---------------------------------------------------------
@Composable
fun DetailsTopBar(navController: NavHostController) {
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
fun DetailsBottomBar() {
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
