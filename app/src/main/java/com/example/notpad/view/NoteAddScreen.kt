package com.example.notpad.view

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.notpad.model.Note
import com.example.notpad.viewmodel.NoteAddViewModel
import com.example.notpad.R

@Composable
fun NoteAddScreen(
    navController: NavController,
    viewModel: NoteAddViewModel = hiltViewModel()
) {
    var add by remember { mutableStateOf(false) }
    var textHead by remember { mutableStateOf("")}
    var textContent by remember { mutableStateOf("") }
    val context = LocalContext.current
    val toastMessage =stringResource(id = R.string.noteAddScreenToastMessage)

    LaunchedEffect(add) {
        if (add){
            val note = Note(textHead,textContent,
                viewModel.dateTime(),
                viewModel.dateTime())

            if (textHead.isEmpty() && textContent.isEmpty()){
                Toast.makeText(context,  toastMessage ,Toast.LENGTH_LONG).show()
            }
            else{
                viewModel.newNoteAddSQLite(note).apply {
                    navController.navigateUp()
                }
            }
            add = false
        }
    }
    AppScaffold(
        topBarTitle = stringResource(id = R.string.noteAddTitle),
        showBackButton = true,
        onBackClick = { navController.navigate("main_note_screen") },
        showAddButton = true,
        onAddClick = {
            add = true
        }
    ) { paddingValues ->
        NoteAddContent(
            modifier = Modifier.padding(paddingValues),
            textContent = textContent,
            textHead = textHead,
            onTextHeadChange = { newTextHead -> textHead = newTextHead },
            onTextContentChange = { newTextContent -> textContent = newTextContent }
        )
    }
}


@Composable
fun NoteAddContent(
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
        ,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            NoteTextFieldHead(
                textHead = textHead,
                onTextHeadChange = onTextHeadChange,
            )
            Spacer(modifier = Modifier.height(16.dp))
            NoteTextFieldContent(
                textContent = textContent,
                onTextContentChange = onTextContentChange
            )
            Spacer(modifier = Modifier.height(16.dp))


        }
    }
}
/*
//Alternatif button
@Composable
fun NoteButtons(
    modifier: Modifier = Modifier,
    navController: NavController,
    textHead: String,
    textContent: String,
    viewModel: NoteAddViewModel
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Ekle butonu
        Button(
            onClick = {
                val note = Note(textHead,textContent,
                    viewModel.dateTime(),
                    viewModel.dateTime())

                if (textHead.isEmpty() && textContent.isEmpty()){
                    Toast.makeText(context,"Başlık veya İçeriğiniz boş lütfen doldurup deneyiniz",Toast.LENGTH_LONG).show()
                }
                else{
                    viewModel.newNoteAddSQLite(note).apply {
                        navController.navigateUp()
                    }
                }
            },colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF64B5F6),  // Light Blue
                contentColor = Color.White
            ),
            modifier = Modifier
                .weight(1f)
                .animateContentSize()
                .padding(15.dp)
        ) {
            Text(text = "Add")
        }
    }
}

 */


@Composable
fun NoteTextFieldHead(
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
            label = { Text(stringResource(id =  R.string.noteAddContainerTitle), color = MaterialTheme.colorScheme.tertiary) },
            modifier = Modifier
                .fillMaxWidth()
                .blur(blur.dp)
                .background(Color.Transparent, shape = shape)
        )
    }
}


@Composable
fun NoteTextFieldContent(
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
