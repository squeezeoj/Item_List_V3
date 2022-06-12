package com.sizzle.itemlistv3

//----------------------------------------------------------
// Imports
//----------------------------------------------------------
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sizzle.itemlistv3.screens.ItemDetailScreen
import com.sizzle.itemlistv3.screens.ItemListScreen
import com.sizzle.itemlistv3.screens.SubItemDetailScreen
import com.sizzle.itemlistv3.ui.theme.ItemListV3Theme

//----------------------------------------------------------
// Navigation Routes
//----------------------------------------------------------
sealed class NavRoutes(val route: String) {
    object ItemListScreen : NavRoutes("itemList")
    object ItemDetailScreen : NavRoutes("itemDetail")
    object SubItemDetailScreen : NavRoutes("subItemDetail")
}

//----------------------------------------------------------
// Main Activity
//----------------------------------------------------------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ItemListV3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AppSetup()
                }
            }
        }
    }
}

//---------------------------------------------------------
// Setup App
//---------------------------------------------------------
@Composable
fun AppSetup(
    viewModel: MainViewModel = MainViewModel()
) {

    //--- Navigation Setup
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavRoutes.ItemListScreen.route,
    ) {

        //--- Item List Screen
        composable(NavRoutes.ItemListScreen.route) {
            ItemListScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        //--- Item Detail Screen
        composable(NavRoutes.ItemDetailScreen.route + "/{id}" + "/{crud}") { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            val crud = backStackEntry.arguments?.getString("crud")
            ItemDetailScreen(
                navController = navController,
                viewModel = viewModel,
                id = id,
                crud = crud
            )
        }

        //--- SubItem Detail Screen
        composable(NavRoutes.SubItemDetailScreen.route + "/{id}" + "/{subID}" + "/{crud}") { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            val subID = backStackEntry.arguments?.getInt("subID")
            val crud= backStackEntry.arguments?.getString("crud")
            SubItemDetailScreen(
                navController = navController,
                viewModel = viewModel,
                id = id,
                subID = subID,
                crud = crud
            )
        }

    }	// End Nav Host

}