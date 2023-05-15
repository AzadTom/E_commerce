package com.example.e_commerce.Screen

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.e_commerce.R


@Composable
fun PublishScreen() {

    var title  by  remember { mutableStateOf("") }
    var subDescription  by  remember { mutableStateOf("") }
    var description by  remember { mutableStateOf("") }
    var type by  remember { mutableStateOf("") }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {

            Text(text = "ProductPublish", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            RequestContentPermission()

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(modifier = Modifier.fillMaxWidth(),value = title, onValueChange = { title = it } , placeholder = {Text("Enter Product Name")})
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(modifier = Modifier.fillMaxWidth(),value = subDescription, onValueChange = { subDescription = it } , placeholder = {Text("Enter SubDescription")})
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(modifier = Modifier.fillMaxWidth(),value = description, onValueChange = { description = it } , placeholder = {Text("Enter Description")})
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(modifier = Modifier.fillMaxWidth(),value = type, onValueChange = { type = it } , placeholder = {Text("Enter Product Type (0-1)")})

            Spacer(modifier = Modifier.height(24.dp))


            OutlinedButton(onClick = {  } , modifier = Modifier.fillMaxWidth()) {

                Text(text="Discard")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { },modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Submit")
            }



            Spacer(modifier = Modifier.height(16.dp))
        }

    }

}

@Composable
fun RequestContentPermission() {

    //imageUri
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    //context
    val context = LocalContext.current

    //bitmap
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    //launcher
    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {




            AsyncImage(
                model = if (imageUri != null) ImageRequest.Builder(LocalContext.current)
                    .data(imageUri)
                    .crossfade(true)
                    .build()
                else R.drawable.pictures,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(16.dp)).padding(16.dp)
            )


            Spacer(modifier = Modifier.height(8.dp))

            Button(
                modifier = Modifier.fillMaxWidth(0.5f),
                onClick = {
                    launcher.launch("image/*")
                }) {
                Text(text = "Pick image", color = Color.White)
            }



    }
}