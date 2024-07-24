package com.example.notpad.view

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.notpad.R
import com.example.notpad.viewmodel.NoteDetailsViewModel

@Composable
fun NoteDetailsScreen(
    navController: NavController,
    viewModel: NoteDetailsViewModel = hiltViewModel(),
    id: Int
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.loadNoteDetailsSQLite(id)
    }
    val notes by viewModel.note
    val context = LocalContext.current
    var textHead by remember { mutableStateOf("")}
    var textContent by remember { mutableStateOf("") }
    val toastMessage =stringResource(id = R.string.noteDetailsScreenToastMessage)


    //Dinleyici(Listener)
    LaunchedEffect(notes) {
        notes?.let {
            textHead = it.noteHead ?: ""
            textContent = it.noteContent ?: ""
        }
    }


    AppScaffold(
        topBarTitle = stringResource(id = R.string.noteDetailsTitle),
        showBackButton = true,
        showSaveButton = true,
        showDeleteButton = true,
        onSaveClick = { save(
            navController = navController,
            textContent = textContent,
            textHead = textHead,
            viewModel = viewModel,
            context = context,
            toastMessage = toastMessage
        )},
        onDeleteClick = {
            delete(
                navController=navController,
                viewModel=viewModel
            )
        },
        onBackClick = { navController.navigate("main_note_screen") }
    ) { paddingValues ->
        DetailsContent(
            modifier = Modifier.padding(paddingValues),
            textContent = textContent,
            textHead = textHead,
            onTextHeadChange = { newTextHead -> textHead = newTextHead },
            onTextContentChange = { newTextContent -> textContent = newTextContent }
        )
    }
}

@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    onTextHeadChange: (String) -> Unit,
    onTextContentChange: (String) -> Unit,
    textHead: String,
    textContent: String,
) {

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            NoteDetailsTextFieldHead(
                textHead = textHead,
                onTextHeadChange = onTextHeadChange
            )
            Spacer(modifier = Modifier.height(16.dp))
            NoteDetailsTextFieldContent(
                textContent = textContent,
                onTextContentChange = onTextContentChange
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun save(
    navController: NavController,
    textHead: String,
    textContent: String,
    viewModel: NoteDetailsViewModel,
    context: Context,
    toastMessage : String
){

    if (textHead.isEmpty() && textContent.isEmpty()){
        Toast.makeText(context, toastMessage,Toast.LENGTH_LONG).show()
    }else{
        viewModel.updateNoteSQLite(textHead,textContent).apply {
            navController.navigateUp()
        }
    }

}

private fun delete(
    navController: NavController,
    viewModel: NoteDetailsViewModel

){
    viewModel.note.value?.let {
        viewModel.deleteNoteSQLite(it.uuid).also {
            navController.navigateUp()
        }
    }
}

/*
//Alternatif button
@Composable
fun NoteDetailsButtons(
    modifier: Modifier = Modifier,
    navController: NavController,
    textHead: String,
    textContent: String,
    viewModel: NoteDetailsViewModel,
    id: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = {
                viewModel.deleteNoteSQLite(id).also {
                    navController.navigateUp()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF64B5F6),  // Light Blue
                contentColor = Color.White
            ),
            modifier = Modifier
                .weight(1f)
                .animateContentSize()
        ) {
            Text(text = "Delete Note")
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Button(
            onClick = {
                viewModel.updateNoteSQLite(textHead, textContent).also {
                    viewModel.updateDateNoteSQLite(id).also {
                        navController.navigateUp()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF64B5F6),  // Light Blue
                contentColor = Color.White
            ),
            modifier = Modifier
                .weight(1f)
                .animateContentSize()
        ) {
            Text(text = "Save Changes")
        }
    }
}

 */

@Composable
fun NoteDetailsTextFieldHead(
    modifier: Modifier = Modifier,
    textHead: String,
    onTextHeadChange: (String) -> Unit,
) {
    val shape: Shape = RoundedCornerShape(16.dp)
    var blur by remember { mutableIntStateOf(0) }
    Box(modifier = modifier) {
        OutlinedTextField(
            value = textHead,
            onValueChange = {
                if (it.length <= 117) {
                    onTextHeadChange(it.uppercase())
                    blur = 0
                } else {
                    blur = 1
                }
            },
            minLines = 5,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF64B5F6),  // Light Blue
                unfocusedBorderColor = Color.Gray,
                errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                errorTextColor = MaterialTheme.colorScheme.onError,
            ),
            textStyle = TextStyle(fontSize = 18.sp
            ),
            label = { Text(stringResource(id = R.string.noteDetailsContainerTitle), color =  MaterialTheme.colorScheme.tertiary) },
            modifier = Modifier
                .fillMaxWidth()
                .blur(blur.dp)
                .background(Color.Transparent, shape = shape)
        )
    }
}

@Composable
fun NoteDetailsTextFieldContent(
    modifier: Modifier = Modifier,
    textContent: String,
    onTextContentChange: (String) -> Unit
) {
    Box(modifier = modifier) {
        OutlinedTextField(
            value = textContent,
            onValueChange = {
                onTextContentChange(it)
            },
            maxLines = 30,
            minLines = 10,
            textStyle = TextStyle(
                textMotion = TextMotion.Animated,
                fontSize = 16.sp
            ),
            singleLine = false,
            label = { Text(stringResource(id = R.string.noteAddContainerContent), color = MaterialTheme.colorScheme.tertiary) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(16.dp)
                .animateContentSize()
                .wrapContentHeight()
        )
    }
}
