package ir.amirhesambandegan.gitlens.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.amirhesambandegan.easify_format.toCompactFormat
import ir.amirhesambandegan.gitlens.model.UserDetailResponse

@Composable
fun UserStatsRow(user: UserDetailResponse) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatBox(
            title = "Repositories",
            count = user.publicRepos.toLong().toCompactFormat(),
            modifier = Modifier.weight(1f)
        )
        StatBox(
            title = "Followers",
            count = user.followers.toLong().toCompactFormat(),
            modifier = Modifier.weight(1f)
        )
        StatBox(
            title = "Following",
            count = user.following.toLong().toCompactFormat(),
            modifier = Modifier.weight(1f)
        )
    }
}
