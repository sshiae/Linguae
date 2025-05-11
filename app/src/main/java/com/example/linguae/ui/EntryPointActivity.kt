package com.example.linguae.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.linguae.ui.NavigationKeys.Arg.BOOK_ID
import com.example.linguae.ui.NavigationKeys.Route.ADD_BOOK
import com.example.linguae.ui.NavigationKeys.Route.BOOK_LIST
import com.example.linguae.ui.NavigationKeys.Route.DICTIONARY
import com.example.linguae.ui.NavigationKeys.Route.WELCOME
import com.example.linguae.ui.feature.addBook.AddBookScreen
import com.example.linguae.ui.feature.bookList.BookListScreen
import com.example.linguae.ui.feature.bookReader.BookReaderScreen
import com.example.linguae.ui.feature.dictionary.DictionaryScreen
import com.example.linguae.ui.feature.welcome.WelcomeScreen
import com.example.linguae.ui.theme.LinguaeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntryPointActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LinguaeTheme {
                LinguaeApp()
            }
        }
    }
}

@Composable
private fun LinguaeApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = WELCOME
    ) {
        composable(route = WELCOME) {
            WelcomeDestination(navController)
        }
        composable(route = BOOK_LIST) {
            BookListDestination(navController)
        }
        composable(
            route = "$BOOK_LIST/{$BOOK_ID}",
            arguments = listOf(
                navArgument(BOOK_ID) {
                    type = NavType.StringType
                }
            )
        ) {
            BookReaderDestination(navController)
        }
        composable(route = ADD_BOOK) {
            BookAddDestination(navController)
        }
        composable(
            route = "$DICTIONARY/{$BOOK_ID}",
            arguments = listOf(
                navArgument(BOOK_ID) {
                    type = NavType.StringType
                }
            )
        ) {
            DictionaryDestination(navController)
        }
    }
}

@Composable
fun WelcomeDestination(
    navController: NavHostController
) {
    WelcomeScreen(
        onStartReading = {
            navController.navigate(BOOK_LIST)
        }
    )
}

@Composable
fun BookListDestination(
    navController: NavHostController
) {
    BookListScreen(
        onClicked = { id ->
            navController.navigate("$BOOK_LIST/$id")
        },
        onClickedAddBook = {
            navController.navigate(ADD_BOOK)
        }
    )
}

@Composable
fun BookReaderDestination(
    navController: NavHostController
) {
    BookReaderScreen(
        onClickShowDictionary = { id ->
            navController.navigate("$DICTIONARY/$id")
        }
    )
}

@Composable
fun BookAddDestination(
    navController: NavHostController
) {
    AddBookScreen(
        onBackClicked = {
            navController.popBackStack()
        }
    )
}

@Composable
fun DictionaryDestination(
    navController: NavHostController
) {
    DictionaryScreen()
}

object NavigationKeys {
    object Arg {
        const val BOOK_ID = "bookId"
    }

    object Route {
        const val WELCOME = "welcome"
        const val BOOK_LIST = "book_list"
        const val ADD_BOOK = "add_book"
        const val DICTIONARY = "dictionary"
    }
}