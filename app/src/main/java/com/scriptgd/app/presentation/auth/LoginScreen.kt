package com.scriptgd.app.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.scriptgd.app.R

@Composable
fun LoginScreen(onLoggedIn: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(R.string.login_title), style = MaterialTheme.typography.headlineMedium)
        Text(stringResource(R.string.login_subtitle), modifier = Modifier.padding(top = 8.dp))
        Button(
            onClick = { /* Google auth wired in the next step */ onLoggedIn() },
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
                .width(280.dp)
        ) {
            Text(stringResource(R.string.login_google))
        }
    }
}