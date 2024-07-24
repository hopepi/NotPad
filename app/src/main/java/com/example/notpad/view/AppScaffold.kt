package com.example.notpad.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    topBarTitle: String,
    showBackButton: Boolean = false,
    showSaveButton : Boolean = false,
    showDeleteButton : Boolean = false,
    showAddButton : Boolean = false,
    onSaveClick: (() -> Unit)? = null,
    onDeleteClick:(() -> Unit)? = null,
    onAddClick: (()-> Unit)? =null,
    onBackClick: (() -> Unit)? = null,
    floatingActionButton: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = topBarTitle) },
                colors = TopAppBarColors(
                    containerColor =MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = { onBackClick?.invoke() },Modifier.height(25.dp)) {
                            Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                        }

                    }
                },
                actions = {
                    if (showDeleteButton){
                        IconButton(onClick = { onDeleteClick?.invoke() },Modifier.height(25.dp)) {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                        }
                    }
                    if (showSaveButton){
                        IconButton(onClick = { onSaveClick?.invoke() },Modifier.height(25.dp)) {
                            Icon(imageVector = Icons.Rounded.CheckCircle, contentDescription = "Save")
                        }
                    }
                    if (showAddButton){
                        IconButton(onClick = { onAddClick?.invoke() },Modifier.height(25.dp)) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                        }
                    }
                }
            )
        },
        floatingActionButton = if (floatingActionButton != null) {
            { floatingActionButton() }
        } else {
            {}
        },
        content = content
    )
}
