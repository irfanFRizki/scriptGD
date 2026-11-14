package com.scriptgd.app.presentation.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.scriptgd.app.R

private data class DashboardEntry(
    val id: String,
    val titleRes: Int,
    val descriptionRes: Int,
    val enabled: Boolean
)

@Composable
fun DashboardHubScreen(onOpenLukisanku: () -> Unit) {
    val dashboards = listOf(
        DashboardEntry(
            id = "lukisanku",
            titleRes = R.string.dashboard_lukisanku,
            descriptionRes = R.string.dashboard_lukisanku_description,
            enabled = true
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.dashboard_title), style = MaterialTheme.typography.headlineSmall)
        LazyColumn(modifier = Modifier.padding(top = 12.dp)) {
            items(dashboards) { entry ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable(enabled = entry.enabled) {
                            if (entry.id == "lukisanku") onOpenLukisanku()
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(stringResource(entry.titleRes), style = MaterialTheme.typography.titleMedium)
                        Text(
                            stringResource(entry.descriptionRes),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}