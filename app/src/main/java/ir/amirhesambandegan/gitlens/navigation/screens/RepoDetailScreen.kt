package ir.amirhesambandegan.gitlens.navigation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ir.amirhesambandegan.easify_context.copyToClipboard
import ir.amirhesambandegan.easify_context.openBrowser
import ir.amirhesambandegan.easify_context.shareText
import ir.amirhesambandegan.easify_context.toast
import ir.amirhesambandegan.easify_format.toCompactFormat
import ir.amirhesambandegan.easify_haptic.performClick
import ir.amirhesambandegan.easify_haptic.performSuccess
import ir.amirhesambandegan.easify_ui.EasifyExpandableText
import ir.amirhesambandegan.easify_ui.bounceClick
import ir.amirhesambandegan.gitlens.component.BadgeChip
import ir.amirhesambandegan.gitlens.component.RepoStatItem
import ir.amirhesambandegan.gitlens.viewModel.RepoDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RepoDetailScreen(
    owner: String,
    repoName: String,
    viewModel: RepoDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onNavigateToUser: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val repo by viewModel.repo.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(owner, repoName) {
        viewModel.loadRepo(owner, repoName)
    }

    Column(
        Modifier.background(colors.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = repoName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    haptic.performClick()
                    onBackClick()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    haptic.performClick()
                    val url = repo?.htmlUrl ?: "https://github.com/$owner/$repoName"
                    context.shareText(url, "Share $repoName repository")
                }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Repo"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colors.background,
                titleContentColor = colors.onBackground
            )
        )
        if (isLoading && repo == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null && repo == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage ?: "Failed to load repository details",
                    color = colors.error,
                    style = typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(24.dp)
                )
            }
        } else if (repo != null) {
            val r = repo!!
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Main Info Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.surfaceContainerLow),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            // Owner row
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .bounceClick {
                                        haptic.performClick()
                                        if (r.owner != null) {
                                            onNavigateToUser(r.owner.login)
                                        }
                                    }
                            ) {
                                if (r.owner?.avatarUrl != null) {
                                    AsyncImage(
                                        model = r.owner.avatarUrl,
                                        contentDescription = r.owner.login,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                }

                                Text(
                                    text = r.owner?.login ?: owner,
                                    style = typography.titleSmall,
                                    color = colors.primary,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "/",
                                    style = typography.titleSmall,
                                    color = colors.outline
                                )
                                Spacer(modifier = Modifier.width(6.dp))

                                Text(
                                    text = r.name,
                                    style = typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Badges (visibility, branch, language)
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BadgeChip(
                                    text = if (r.private) "Private" else "Public",
                                    bgColor = if (r.private) colors.errorContainer else colors.primaryContainer,
                                    textColor = if (r.private) colors.onErrorContainer else colors.onPrimaryContainer
                                )

                                BadgeChip(
                                    text = "Branch: ${r.defaultBranch}",
                                    bgColor = colors.surfaceContainerHighest,
                                    textColor = colors.onSurfaceVariant
                                )

                                if (!r.language.isNullOrBlank()) {
                                    BadgeChip(
                                        text = r.language,
                                        bgColor = colors.secondaryContainer,
                                        textColor = colors.onSecondaryContainer
                                    )
                                }
                            }

                            if (!r.description.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(14.dp))
                                EasifyExpandableText(
                                    text = r.description,
                                    collapsedMaxLines = 3,
                                    expandText = "Read more",
                                    collapseText = "Show less",
                                    textStyle = typography.bodyMedium.copy(color = colors.onSurfaceVariant)
                                )
                            }

                            // Topics
                            if (r.topics.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(14.dp))
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    r.topics.forEach { topic ->
                                        BadgeChip(
                                            text = topic,
                                            bgColor = colors.tertiaryContainer.copy(alpha = 0.5f),
                                            textColor = colors.onTertiaryContainer
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Stats Grid Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.surfaceContainerLow)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            RepoStatItem(
                                title = "Stars",
                                value = r.stargazersCount.toCompactFormat(),
                                icon = Icons.Default.Star,
                                iconColor = Color(0xFFFFB300)
                            )
                            RepoStatItem(
                                title = "Forks",
                                value = r.forksCount.toCompactFormat(),
                                icon = null,
                                iconColor = colors.primary
                            )
                            RepoStatItem(
                                title = "Watchers",
                                value = r.watchersCount.toCompactFormat(),
                                icon = null,
                                iconColor = colors.primary
                            )
                            RepoStatItem(
                                title = "Issues",
                                value = r.openIssuesCount.toCompactFormat(),
                                icon = null,
                                iconColor = colors.primary
                            )
                        }
                    }
                }

                // Git Clone Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.surfaceContainerLow)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Clone Repository",
                                style = typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = colors.onSurface
                            )

                            val cloneUrl = r.cloneUrl ?: "https://github.com/${r.fullName}.git"
                            val gitCommand = "git clone $cloneUrl"

                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = colors.surfaceContainerHighest
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = gitCommand,
                                        style = typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                                        color = colors.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = {
                                            context.copyToClipboard(gitCommand)
                                            haptic.performSuccess()
                                            context.toast("Clone command copied to clipboard")
                                        },
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Copy", style = typography.labelSmall)
                                    }
                                }
                            }

                            if (!r.sshUrl.isNullOrBlank()) {
                                val sshCommand = "git clone ${r.sshUrl}"
                                OutlinedButton(
                                    onClick = {
                                        context.copyToClipboard(sshCommand)
                                        haptic.performSuccess()
                                        context.toast("SSH clone command copied to clipboard")
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Copy SSH Clone Command")
                                }
                            }
                        }
                    }
                }

                // Action Buttons
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                context.openBrowser(r.htmlUrl)
                            },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Open in GitHub")
                        }

                        OutlinedButton(
                            onClick = {
                                context.shareText(r.htmlUrl, "Share ${r.name} repository")
                            },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Share Repository")
                        }
                    }
                }
            }
        }
    }
}
