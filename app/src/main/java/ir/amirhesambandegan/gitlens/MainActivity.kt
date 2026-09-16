package ir.amirhesambandegan.gitlens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ir.amirhesambandegan.easify_network.createKtorClient
import ir.amirhesambandegan.easify_ui.hideKeyboardOnTapOutside
import ir.amirhesambandegan.gitlens.navigation.NavHome
import ir.amirhesambandegan.gitlens.ui.theme.GitLensTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GitLensTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .hideKeyboardOnTapOutside()
                ) { innerPadding ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        NavHome(navController)
                    }
                }
            }
        }
    }
}
