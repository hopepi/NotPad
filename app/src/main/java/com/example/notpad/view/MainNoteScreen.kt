package com.example.notpad.view

import android.graphics.Paint
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults.shape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.notpad.MainActivity
import com.example.notpad.R
import com.example.notpad.model.Note
import com.example.notpad.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.graphics.pdf.PdfDocument
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.loadNotesSQLite()
    }
    val notes by viewModel.notes
    val activity = LocalContext.current as MainActivity
    val context =LocalContext.current
    AppScaffold(
        topBarTitle = stringResource(id = R.string.appName),
        showBackButton = false,
        floatingActionButton = {
            FloatingAction(onClick = {
                navController.navigate("note_add_screen")
            })
        },
    ) { paddingValues ->
        MainContext(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            notes = notes,
            viewModel=viewModel
        )
    }
}

@Composable
fun MainContext(modifier: Modifier = Modifier,
                navController: NavHostController,
                notes: List<Note>,
                viewModel: MainViewModel) {

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxSize()
    ) {
        Column {
            SearchBar(
                hint = stringResource(id = R.string.searchingIsHint),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                viewModel.searchNote(it)
            }
            NoteList(navController = navController, notes = notes)
        }
    }
}

@Composable
fun NoteList(navController: NavController, notes: List<Note>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize(),

    ) {
        items(notes) { note ->
            NoteItem(navController, note)
        }
    }
}

@Composable
fun NoteItem(navController: NavController, note: Note) {
    Surface(
        //shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            //.border(border = BorderStroke(0.7.dp, color = MaterialTheme.colorScheme.tertiary))
            .clickable {
                navController.navigate("note_details_screen/${note.uuid}")
            }
    ) {
        note.noteHead?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun FloatingAction(onClick: () -> Unit) {
    var clicked by remember { mutableStateOf(false) }
    val scale by animateDpAsState(
        targetValue = (if (clicked) 1.2f else 1f).dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessHigh
        )
    )
    val coroutineScope = rememberCoroutineScope()
    FloatingActionButton(
        onClick = {
            clicked = !clicked
            coroutineScope.launch {
                delay(75)
                onClick()
                clicked = false
            }
        },
        modifier = Modifier.scale(scale.value)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(5.dp)) {
            Icon(Icons.Filled.Edit, contentDescription = stringResource(id = R.string.floatingActionText))
            Text(text = stringResource(id = R.string.floatingActionText), fontSize = 14.sp)
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf("") }
    var isHintDisplay by remember { mutableStateOf(hint.isNotEmpty()) }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            cursorBrush = Brush.linearGradient(colors = listOf(MaterialTheme.colorScheme.surface,MaterialTheme.colorScheme.tertiary)),
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                .padding(12.dp)
                .onFocusChanged {
                    isHintDisplay = it.isFocused.not() && text.isEmpty()
                }
        )
        if (isHintDisplay) {
            Text(
                text = hint,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(9.dp)
            )
        }
    }
}
