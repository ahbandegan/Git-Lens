package ir.amirhesambandegan.gitlens.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import ir.amirhesambandegan.easify_context.copyToClipboard
import ir.amirhesambandegan.easify_context.openBrowser
import ir.amirhesambandegan.easify_context.toast
import ir.amirhesambandegan.easify_haptic.performSuccess
import ir.amirhesambandegan.gitlens.model.UserDetailResponse

@Composable
fun UserDetailsTab(user: UserDetailResponse) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DetailItemRow(label = "User ID:", value = user.id.toString())
                if (!user.blog.isNullOrBlank()) {
                    DetailItemRow(label = "Website / Blog:", value = user.blog, isLink = true, onLinkClick = {
                        val url = if (user.blog.startsWith("http")) user.blog else "https://${user.blog}"
                        context.openBrowser(url)
                    })
                }
                if (!user.twitterUsername.isNullOrBlank()) {
                    DetailItemRow(label = "Twitter / X:", value = "@${user.twitterUsername}", isLink = true, onLinkClick = {
                        context.openBrowser("https://twitter.com/${user.twitterUsername}")
                    })
                }
                if (!user.email.isNullOrBlank()) {
                    DetailItemRow(label = "Email:", value = user.email)
                }
                DetailItemRow(label = "Public Gists:", value = user.publicGists.toString())
                if (!user.createdAt.isNullOrBlank()) {
                    DetailItemRow(label = "Joined:", value = user.createdAt.take(10))
                }
            }
        }

        OutlinedButton(
            onClick = {
                context.copyToClipboard(user.htmlUrl ?: "https://github.com/${user.login}")
                haptic.performSuccess()
                context.toast("Profile link copied to clipboard")
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Copy Profile Link")
        }
    }
}
