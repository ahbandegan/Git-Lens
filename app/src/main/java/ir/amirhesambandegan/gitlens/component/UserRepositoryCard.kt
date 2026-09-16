package ir.amirhesambandegan.gitlens.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ir.amirhesambandegan.easify_format.toCompactFormat
import ir.amirhesambandegan.easify_ui.EasifyExpandableText
import ir.amirhesambandegan.easify_ui.SpacerHeight
import ir.amirhesambandegan.easify_ui.SpacerWidth
import ir.amirhesambandegan.easify_ui.bounceClick
import ir.amirhesambandegan.gitlens.model.RepoItem

@Composable
fun UserRepositoryCard(
    repo: RepoItem,
    onClick: () -> Unit,
    onWebClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .bounceClick { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = repo.name,
                    style = typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                TextButton(onClick = onWebClick) {
                    Text("Web", style = typography.labelSmall)
                }
            }

            if (!repo.description.isNullOrBlank()) {
                SpacerHeight(6.dp)
                EasifyExpandableText(
                    text = repo.description,
                    collapsedMaxLines = 2,
                    expandText = "Read more",
                    collapseText = "Show less",
                    textStyle = typography.bodySmall.copy(color = colors.onSurfaceVariant)
                )
            }

            SpacerHeight(12.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (!repo.language.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = colors.primaryContainer.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = repo.language,
                            style = typography.labelSmall,
                            color = colors.onPrimaryContainer,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Stars",
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(16.dp)
                    )
                    SpacerWidth(4.dp)
                    Text(
                        text = repo.stargazersCount.toCompactFormat(),
                        style = typography.labelSmall,
                        color = colors.onSurfaceVariant
                    )
                }

                Text(
                    text = "Forks: ${repo.forksCount.toCompactFormat()}",
                    style = typography.labelSmall,
                    color = colors.onSurfaceVariant
                )
            }
        }
    }
}