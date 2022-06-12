package com.sizzle.itemlistv3.screens

//---------------------------------------------------------
// Imports
//---------------------------------------------------------
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sizzle.itemlistv3.NavRoutes
import com.sizzle.itemlistv3.R				// Import String Resources
import com.sizzle.itemlistv3.MainViewModel
import com.sizzle.itemlistv3.utilities.SpacerDividerSpacer
import kotlinx.coroutines.launch

//---------------------------------------------------------
// Items List Screen
//---------------------------------------------------------
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ItemListScreen(
	navController: NavHostController,
	viewModel: MainViewModel
) {
	//--- View Model State Variables
	var showList by remember { mutableStateOf(viewModel.showItems) }
	var filterState by remember { mutableStateOf(viewModel.filtering) }
	var textFilter by remember { mutableStateOf(TextFieldValue(viewModel.filterText)) }
	var sortState by remember { mutableStateOf(viewModel.sortAscending) }

	//--- Keyboard Controller
	val keyboardController = LocalSoftwareKeyboardController.current

	//--- Scaffolding
	//	From: https://stackoverflow.com/questions/70440500/hide-soft-keyboard-on-drawer-open-jetpack-compose
	val coroutineScope = rememberCoroutineScope()
	val scaffoldState = rememberScaffoldState(rememberDrawerState(
			initialValue = DrawerValue.Closed,
			confirmStateChange = {
				//--- If Drawer is Closed Then Hide Keyboard
				if (it == DrawerValue.Closed) {
					keyboardController?.hide()
				}
				//--- Close Drawer
				true
			}
		)
	)

	Scaffold(
		scaffoldState = scaffoldState,

		//-----------------------------------------------------------------
		// Top Bar
		//-----------------------------------------------------------------
		topBar = {
			TopBar(
				onMenuClicked = {
					coroutineScope.launch {
						// to close use -> scaffoldState.drawerState.close()
						scaffoldState.drawerState.open()
					}
				})
		},

		//-----------------------------------------------------------------
		// Bottom Bar
		//-----------------------------------------------------------------
		bottomBar = { ListBottomBar() },

		//-----------------------------------------------------------------
		// Drawer
		//-----------------------------------------------------------------
		drawerContent = {

			Column {

				//--- Search Card
				Card(
					modifier = Modifier
						.fillMaxWidth()
						.padding(15.dp),
					elevation = 10.dp
				) {

					//--- Content Column
					Column(
						modifier = Modifier.padding(15.dp),
						horizontalAlignment = Alignment.CenterHorizontally
					) {

						//----------------------------------------------------------
						// Filter Control
						//----------------------------------------------------------
						Text(
							modifier = Modifier.fillMaxWidth(),
							textAlign = TextAlign.Center,
							text = "Search",
							style = MaterialTheme.typography.h4
						)
						SpacerDividerSpacer()

						//------------------------------------------------------
						// Filter Box
						//------------------------------------------------------
						OutlinedTextField(
							value = textFilter,
							modifier = Modifier.width(200.dp),
							label = { Text(text = "Search For (Case Sensitive):") },
							placeholder = { Text(text = "") },
							leadingIcon = {
								Icon(imageVector = Icons.Rounded.Search, contentDescription = "Search")
							},
							trailingIcon = {
								IconButton(
									onClick = {
										//--- Clear Search Text Field Value
										textFilter = TextFieldValue("")
										viewModel.filterText = ""

										//--- Clear Search Switch
										filterState = false
										viewModel.filtering = false

										//--- Update Show List to Trigger Recomposition
										showList = viewModel.returnItemShowList()
									},
								){
									Icon(
										Icons.Rounded.Clear,
										contentDescription = "Clear"
									)
								}

							},
							singleLine = true,
							keyboardActions = KeyboardActions(
								onDone = {
									keyboardController?.hide()
								}
							),
							keyboardOptions = KeyboardOptions( imeAction = ImeAction.Done ),
							onValueChange = {

								//--- Set Text Here and in View Model
								textFilter = it
								viewModel.filterText = it.text

								//--- Update Show List to Trigger Recomposition
								showList = viewModel.returnItemShowList()
							}
						)
						Spacer(modifier = Modifier.height(15.dp))

						//------------------------------------------------------
						// Filter Switch
						//------------------------------------------------------
						Switch(
							checked = filterState,
							onCheckedChange = {

								//--- Set Text Here and in View Model
								filterState = it
								viewModel.filtering = !viewModel.filtering

								//--- Hide Keyboard
								keyboardController?.hide()

								//--- Update Show List to Trigger Recomposition
								showList = viewModel.returnItemShowList()
							},
							modifier = Modifier
						)
						Spacer(modifier = Modifier.height(15.dp))

						//------------------------------------------------------
						// Filter Label
						//------------------------------------------------------
						val labelFilter = if (viewModel.filtering) {
							"Filter: ON"
						} else {
							"Filter: OFF"
						}
						Text(
							modifier = Modifier.fillMaxWidth(),
							textAlign = TextAlign.Center,
							text = labelFilter,
							style = MaterialTheme.typography.h5
						)

					}    // End Column

				}	// End Search Card



				//--- Sort Card
				Card(
					modifier = Modifier
						.fillMaxWidth()
						.padding(15.dp),
					elevation = 10.dp
				) {

					//--- Content Column
					Column(
						modifier = Modifier.padding(15.dp),
						horizontalAlignment = Alignment.CenterHorizontally
					) {

						//----------------------------------------------------------
						// Sort Control
						//----------------------------------------------------------
						Text(
							modifier = Modifier.fillMaxWidth(),
							textAlign = TextAlign.Center,
							text = "Sort ID",
							style = MaterialTheme.typography.h4
						)
						SpacerDividerSpacer()

						//------------------------------------------------------
						// Sort Switch Row
						//------------------------------------------------------
						Row {
							Text("Descending")
							Switch(
								checked = sortState,
								onCheckedChange = {

									//--- Set Text Here and in View Model
									sortState = it
									viewModel.sortAscending = !viewModel.sortAscending

									//--- Hide Keyboard
									keyboardController?.hide()

									//--- Update Show List to Trigger Recomposition
									showList = viewModel.returnItemShowList()
								},
								modifier = Modifier
							)
							Text("Ascending")

						}	// End Sort Switch Row

						Spacer(modifier = Modifier.height(15.dp))

					}    // End Column

				}	// End Sort Card

			}	// End Drawer Column

		},	// End Drawer Content

		//-----------------------------------------------------------------
		// Content
		//-----------------------------------------------------------------
		content = {	padding ->

			//--- Display Content
			Column(modifier = Modifier.padding(padding)) {

				//---------------------------------------------------------
				// Lazy Column to Show Items List
				//---------------------------------------------------------
				LazyColumn {

					items(showList.size) { item ->
						EachItemCard(
							viewModel = viewModel,
							navController = navController,
							itemID = showList[item].id,
							itemTitle = showList[item].title
						)
					}    // End Items

				}    // End Lazy Column

			}	// End Column

		},	// End Content

		//-----------------------------------------------------------------
		// Floating Action Button
		//-----------------------------------------------------------------
		isFloatingActionButtonDocked = true,
		floatingActionButtonPosition = FabPosition.End,
		floatingActionButton = {
			FloatingActionButton(
				modifier = Modifier.offset((-15).dp,(-50).dp),
				onClick = {
					navController.navigate(
						route = NavRoutes.ItemDetailScreen.route
								+ "/" + "0"
								+ "/" + "CREATE"
					)
				}) {
				Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add")
			}
		}   // End Floating Action Button

	)	// End Scaffold

}	// End Item List Screen Function


//---------------------------------------------------------
// Each Item Card
//---------------------------------------------------------
@Composable
fun EachItemCard(
	navController: NavHostController,
	viewModel: MainViewModel,
	itemID: Int,
	itemTitle: String
) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(15.dp)
			.clickable {
				viewModel.getItemByID(itemID)
				navController.navigate(            // Go to Item Details Screen
					route = NavRoutes.ItemDetailScreen.route
							+ "/" + itemID
							+ "/" + "READ"
				)
			},
		elevation = 10.dp
	) {

		Row {

			//--- Text Column
			Column(
				modifier = Modifier.padding(15.dp),
				verticalArrangement = Arrangement.Top,
				horizontalAlignment = Alignment.Start,
			) {
				Text(text = "Item $itemID", style = MaterialTheme.typography.body2)
				Text(text = itemTitle, style = MaterialTheme.typography.h5)
			}    // End Text Column

			//--- Dynamic Width Spacer Between Item Title and Icons
			Spacer(Modifier.weight(1f))

			//--- Edit Icon
			IconButton(
				onClick = {
					viewModel.getItemByID(itemID)
					navController.navigate(            // Go to Item Details Screen
						route = NavRoutes.ItemDetailScreen.route
								+ "/" + itemID
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
					viewModel.getItemByID(itemID)
					navController.navigate(            // Go to Item Details Screen
						route = NavRoutes.ItemDetailScreen.route
								+ "/" + itemID
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

}	// End Each Item Card Function


//---------------------------------------------------------
// Top Bar
//---------------------------------------------------------
@Composable
fun TopBar( onMenuClicked: () -> Unit ) {
	TopAppBar(
		title = {
			Text(
				text = stringResource(R.string.app_name),
				style = MaterialTheme.typography.h5
			)
		},
		navigationIcon = {
			Icon(
				imageVector = Icons.Default.Menu,
				contentDescription = "Menu",
				modifier = Modifier.clickable(onClick = onMenuClicked),
			)
		}
	)
}

//---------------------------------------------------------
// Bottom Bar
//---------------------------------------------------------
@Composable
fun ListBottomBar() {
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
	}
}