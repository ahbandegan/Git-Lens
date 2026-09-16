package ir.amirhesambandegan.gitlens.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ir.amirhesambandegan.easify_context.toast
import ir.amirhesambandegan.gitlens.navigation.modals.HomeModal
import ir.amirhesambandegan.gitlens.navigation.modals.RepoDetailModal
import ir.amirhesambandegan.gitlens.navigation.modals.UserDetailModal
import ir.amirhesambandegan.gitlens.navigation.screens.HomeScreen
import ir.amirhesambandegan.gitlens.navigation.screens.RepoDetailScreen
import ir.amirhesambandegan.gitlens.navigation.screens.UserDetailScreen

@Composable
fun NavHome(
    navController: NavHostController
) {
    val animDuration = 300

    NavHost(
        navController = navController,
        startDestination = HomeModal,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(durationMillis = animDuration, easing = FastOutSlowInEasing)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth / 4 },
                animationSpec = tween(durationMillis = animDuration, easing = FastOutSlowInEasing)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth / 4 },
                animationSpec = tween(durationMillis = animDuration, easing = FastOutSlowInEasing)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(durationMillis = animDuration, easing = FastOutSlowInEasing)
            )
        }
    ) {
        composable<HomeModal> {
            val context = LocalContext.current
            var lastBackPressTime by remember { mutableLongStateOf(0L) }

            BackHandler {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastBackPressTime < 2000L) {
                    (context as? Activity)?.finish()
                } else {
                    lastBackPressTime = currentTime
                    context.toast("Press back again to exit")
                }
            }

            HomeScreen(
                onNavigateToUser = { username ->
                    navController.navigate(UserDetailModal(username))
                },
                onNavigateToRepo = { owner, repo ->
                    navController.navigate(RepoDetailModal(owner, repo))
                }
            )
        }

        composable<UserDetailModal> { backStackEntry ->
            val route = backStackEntry.toRoute<UserDetailModal>()

            BackHandler {
                navController.popBackStack()
            }

            UserDetailScreen(
                username = route.username,
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigateToRepo = { owner, repo ->
                    navController.navigate(RepoDetailModal(owner, repo))
                }
            )
        }

        composable<RepoDetailModal> { backStackEntry ->
            val route = backStackEntry.toRoute<RepoDetailModal>()

            BackHandler {
                navController.popBackStack()
            }

            RepoDetailScreen(
                owner = route.owner,
                repoName = route.repoName,
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigateToUser = { username ->
                    navController.navigate(UserDetailModal(username))
                }
            )
        }
    }
}