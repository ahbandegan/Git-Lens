package ir.amirhesambandegan.gitlens.navigation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import ir.amirhesambandegan.easify_ui.EasifySegmentedControl
import ir.amirhesambandegan.easify_ui.bounceClick
import ir.amirhesambandegan.gitlens.model.RepoItem
import ir.amirhesambandegan.gitlens.model.UserDetailResponse
import ir.amirhesambandegan.gitlens.viewModel.UserDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    username: String,
    viewModel: UserDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onNavigateToRepo: (String, String) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val user by viewModel.user.collectAsState()
    val repositories by viewModel.repositories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()

    LaunchedEffect(username) {
        viewModel.loadUser(username)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = user?.name ?: username,
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
                        val url = user?.htmlUrl ?: "https://github.com/$username"
                        context.shareText(url, "Share profile of $username")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Profile"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.background,
                    titleContentColor = colors.onBackground
                )
            )
        },
        containerColor = colors.background
    ) { innerPadding ->
        if (isLoading && user == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null && user == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage ?: "An error occurred",
                    color = colors.error,
                    style = typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(24.dp)
                )
            }
        } else if (user != null) {
            val userData = user!!
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header profile card
                item {
                    UserProfileHeader(
                        user = userData,
                        onOpenWeb = { context.openBrowser(userData.htmlUrl ?: "https://github.com/$username") },
                        onCopyUsername = {
                            context.copyToClipboard(userData.login)
                            haptic.performSuccess()
                            context.toast("Username copied to clipboard")
                        }
                    )
                }

                // Stats row
                item {
                    UserStatsRow(user = userData)
                }

                // Tabs
                item {
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        EasifySegmentedControl(
                            items = listOf(
                                "Repositories (${repositories.size})",
                                "About"
                            ),
                            selectedIndex = selectedTab,
                            onItemSelected = { index ->
                                haptic.performClick()
                                viewModel.setSelectedTab(index)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Tab content
                if (selectedTab == 0) {
                    if (repositories.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No public repositories found",
                                    color = colors.outline,
                                    style = typography.bodyMedium
                                )
                            }
                        }
                    } else {
                        items(repositories, key = { it.id }) { repo ->
                            UserRepositoryCard(
                                repo = repo,
                                onClick = {
                                    haptic.performClick()
                                    onNavigateToRepo(userData.login, repo.name)
                                },
                                onWebClick = {
                                    context.openBrowser(repo.htmlUrl)
                                }
                            )
                        }
                    }
                } else {
                    item {
                        UserDetailsTab(user = userData)
                    }
                }
            }
        }
    }
}

@Composable
fun UserProfileHeader(
    user: UserDetailResponse,
    onOpenWeb: () -> Unit,
    onCopyUsername: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceContainerLow),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = user.login,
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (!user.name.isNullOrBlank()) {
                Text(
                    text = user.name,
                    style = typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

            Surface(
                onClick = onCopyUsername,
                shape = RoundedCornerShape(12.dp),
                color = colors.surfaceContainerHighest.copy(alpha = 0.5f),
                modifier = Modifier.bounceClick { onCopyUsername() }
            ) {
                Text(
                    text = "@${user.login}",
                    style = typography.bodyMedium,
                    color = colors.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            if (!user.bio.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                EasifyExpandableText(
                    text = user.bio,
                    collapsedMaxLines = 3,
                    expandText = "Read more",
                    collapseText = "Show less",
                    textStyle = typography.bodyMedium.copy(
                        color = colors.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                )
            }

            if (!user.location.isNullOrBlank() || !user.company.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (!user.location.isNullOrBlank()) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = colors.outline,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = user.location,
                            style = typography.bodySmall,
                            color = colors.outline
                        )
                    }

                    if (!user.location.isNullOrBlank() && !user.company.isNullOrBlank()) {
                        Text(
                            text = " • ",
                            style = typography.bodySmall,
                            color = colors.outline
                        )
                    }

                    if (!user.company.isNullOrBlank()) {
                        Text(
                            text = user.company,
                            style = typography.bodySmall,
                            color = colors.outline
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onOpenWeb,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open in GitHub")
            }
        }
    }
}

@Composable
fun UserStatsRow(user: UserDetailResponse) {
    val colors = MaterialTheme.colorScheme

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

@Composable
fun StatBox(
    title: String,
    count: String,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceContainerLow)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count,
                style = typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = colors.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = typography.labelSmall,
                color = colors.outline
            )
        }
    }
}

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
                Spacer(modifier = Modifier.height(6.dp))
                EasifyExpandableText(
                    text = repo.description,
                    collapsedMaxLines = 2,
                    expandText = "Read more",
                    collapseText = "Show less",
                    textStyle = typography.bodySmall.copy(color = colors.onSurfaceVariant)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

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
                    Spacer(modifier = Modifier.width(4.dp))
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

@Composable
fun UserDetailsTab(user: UserDetailResponse) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

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

@Composable
fun DetailItemRow(
    label: String,
    value: String,
    isLink: Boolean = false,
    onLinkClick: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = typography.bodyMedium,
            color = colors.outline
        )
        if (isLink) {
            TextButton(onClick = onLinkClick) {
                Text(
                    text = value,
                    style = typography.bodyMedium,
                    color = colors.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else {
            Text(
                text = value,
                style = typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = colors.onSurface
            )
        }
    }
}
