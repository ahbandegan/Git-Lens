package ir.amirhesambandegan.gitlens.navigation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import ir.amirhesambandegan.easify_context.openBrowser
import ir.amirhesambandegan.easify_format.toCompactFormat
import ir.amirhesambandegan.easify_haptic.performClick
import ir.amirhesambandegan.easify_ui.EasifyExpandableText
import ir.amirhesambandegan.easify_ui.EasifySegmentedControl
import ir.amirhesambandegan.easify_ui.bounceClick
import ir.amirhesambandegan.easify_ui.shimmer
import ir.amirhesambandegan.gitlens.model.RepoItem
import ir.amirhesambandegan.gitlens.model.SearchItem
import ir.amirhesambandegan.gitlens.viewModel.HomeViewModel
import ir.amirhesambandegan.gitlens.viewModel.SearchTab
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToUser: (String) -> Unit = {},
    onNavigateToRepo: (String, String) -> Unit = { _, _ -> }
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    val currentTab by viewModel.currentTab.collectAsState()
    val users by viewModel.users.collectAsState()
    val repositories by viewModel.repositories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var search by remember { mutableStateOf("") }
    var page by remember { mutableIntStateOf(1) }
    val pageSize = 15

    val scrollController = rememberLazyListState()

    LaunchedEffect(search, currentTab) {
        if (search.isBlank()) {
            page = 1
            return@LaunchedEffect
        }
        delay(500.milliseconds)
        page = 1
        if (currentTab == SearchTab.USERS) {
            viewModel.getUsers(search.trim(), page)
        } else {
            viewModel.searchRepositories(search.trim(), page)
        }
    }

    val pageCount = remember(currentTab, users, repositories) {
        if (currentTab == SearchTab.USERS) {
            users?.let { minOf((it.totalCount + pageSize - 1) / pageSize, 100) } ?: 0
        } else {
            repositories?.let { minOf((it.totalCount + pageSize - 1) / pageSize, 100) } ?: 0
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = "Git Lens",
                style = typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = colors.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Inspect GitHub users and repositories",
                style = typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = colors.outline
            )

            Spacer(modifier = Modifier.height(16.dp))

            // EasifySegmentedControl for switching between Users and Repositories
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                EasifySegmentedControl(
                    items = listOf("Users", "Repositories"),
                    selectedIndex = if (currentTab == SearchTab.USERS) 0 else 1,
                    onItemSelected = { index ->
                        haptic.performClick()
                        val newTab = if (index == 0) SearchTab.USERS else SearchTab.REPOSITORIES
                        viewModel.setTab(newTab)
                        page = 1
                        if (search.isNotBlank()) {
                            if (newTab == SearchTab.USERS) {
                                viewModel.getUsers(search.trim(), page)
                            } else {
                                viewModel.searchRepositories(search.trim(), page)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                maxLines = 1,
                shape = RoundedCornerShape(24.dp),
                label = {
                    Text(
                        if (currentTab == SearchTab.USERS) "GitHub Username"
                        else "GitHub Repository"
                    )
                },
                placeholder = {
                    Text(
                        if (currentTab == SearchTab.USERS) "e.g. ahbandegan or torvalds"
                        else "e.g. Easify-Android or ktor"
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = colors.primary
                    )
                },
                trailingIcon = {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 4.dp),
                            strokeWidth = 2.dp
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.outline.copy(alpha = 0.4f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceContainerLow),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)
                ) {
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage ?: "",
                            color = colors.error,
                            style = typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 8.dp)
                        )
                    }

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        state = scrollController,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (isLoading && ((currentTab == SearchTab.USERS && users == null) || (currentTab == SearchTab.REPOSITORIES && repositories == null))) {
                            // Shimmer loading placeholders
                            items(5) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(84.dp)
                                        .padding(horizontal = 16.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .shimmer(showShimmer = true)
                                )
                            }
                        } else if (currentTab == SearchTab.USERS) {
                            val userList = users?.items
                            if (!userList.isNullOrEmpty()) {
                                items(userList, key = { it.login }) { user ->
                                    UserItemCard(
                                        user = user,
                                        onUserClick = {
                                            haptic.performClick()
                                            onNavigateToUser(user.login)
                                        },
                                        onWebClick = {
                                            context.openBrowser(user.htmlUrl)
                                        }
                                    )
                                }
                            } else if (search.isEmpty()) {
                                item {
                                    EmptyPrompt(message = "Enter a username to search")
                                }
                            } else {
                                item {
                                    EmptyPrompt(message = "No users found")
                                }
                            }
                        } else {
                            val repoList = repositories?.items
                            if (!repoList.isNullOrEmpty()) {
                                items(repoList, key = { it.id }) { repo ->
                                    RepositoryItemCard(
                                        repo = repo,
                                        onRepoClick = {
                                            haptic.performClick()
                                            onNavigateToRepo(repo.owner?.login ?: "", repo.name)
                                        },
                                        onWebClick = {
                                            context.openBrowser(repo.htmlUrl)
                                        }
                                    )
                                }
                            } else if (search.isEmpty()) {
                                item {
                                    EmptyPrompt(message = "Enter a repository name to search")
                                }
                            } else {
                                item {
                                    EmptyPrompt(message = "No repositories found")
                                }
                            }
                        }
                    }

                    // Pagination row
                    if (pageCount > 1) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = {
                                    if (page > 1) {
                                        haptic.performClick()
                                        page--
                                        if (currentTab == SearchTab.USERS) {
                                            viewModel.getUsers(search.trim(), page)
                                        } else {
                                            viewModel.searchRepositories(search.trim(), page)
                                        }
                                        scope.launch { scrollController.animateScrollToItem(0) }
                                    }
                                },
                                enabled = page > 1
                            ) {
                                Text("Previous")
                            }

                            Text(
                                text = "Page $page of $pageCount",
                                style = typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = colors.onSurfaceVariant
                            )

                            TextButton(
                                onClick = {
                                    if (page < pageCount) {
                                        haptic.performClick()
                                        page++
                                        if (currentTab == SearchTab.USERS) {
                                            viewModel.getUsers(search.trim(), page)
                                        } else {
                                            viewModel.searchRepositories(search.trim(), page)
                                        }
                                        scope.launch { scrollController.animateScrollToItem(0) }
                                    }
                                },
                                enabled = page < pageCount
                            ) {
                                Text("Next")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserItemCard(
    user: SearchItem,
    onUserClick: () -> Unit,
    onWebClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .bounceClick { onUserClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = user.login,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.login,
                    style = typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = colors.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "View profile & repositories",
                    style = typography.bodySmall,
                    color = colors.primary
                )
            }

            TextButton(
                onClick = onWebClick
            ) {
                Text("Web", style = typography.labelMedium)
            }
        }
    }
}

@Composable
fun RepositoryItemCard(
    repo: RepoItem,
    onRepoClick: () -> Unit,
    onWebClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .bounceClick { onRepoClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (repo.owner?.avatarUrl != null) {
                    AsyncImage(
                        model = repo.owner.avatarUrl,
                        contentDescription = repo.owner.login,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = repo.fullName,
                    style = typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = colors.primary,
                    modifier = Modifier.weight(1f)
                )

                TextButton(
                    onClick = onWebClick
                ) {
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

            Spacer(modifier = Modifier.height(10.dp))

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
fun EmptyPrompt(message: String) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = typography.bodyMedium,
            color = colors.outline,
            textAlign = TextAlign.Center
        )
    }
}