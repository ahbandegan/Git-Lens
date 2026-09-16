package ir.amirhesambandegan.gitlens.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.amirhesambandegan.easify_ui.SpacerHeight
import ir.amirhesambandegan.easify_ui.SpacerWidth

@Composable
fun RepoStatItem(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector?,
    iconColor: Color
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
                SpacerWidth(4.dp)
            }
            Text(
                text = value,
                style = typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )
        }
        SpacerHeight(2.dp)
        Text(
            text = title,
            style = typography.labelSmall,
            color = colors.outline
        )
    }
}
