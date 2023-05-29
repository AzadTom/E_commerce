package com.example.e_commerce.Screen

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.e_commerce.Model.Product
import com.example.e_commerce.R
import java.util.*


@Composable
fun PublishScreen(ondiscard: () -> Unit, onSubmit: (Product) -> Unit) {

    var title by remember { mutableStateOf("") }
    var subDescription by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    Scaffold(
        topBar = { PublishTopAppBar({ ondiscard() }) }, bottomBar = {
            PublishBottomBar(ondiscard = { ondiscard() }, onSubmit = {
                onSubmit(
                    Product(
                        id = UUID.randomUUID().toString(),
                        productname = title,
                        subdescription = subDescription,
                        description = description,
                        imageUrl = imageUrl,
                        price = price.toInt()
                    )
                )
            })
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize().background(MaterialTheme.colors.primary)
                .padding(paddingValues = padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {

                    RequestContentPermission {

                        imageUrl = it.toString()

                    }


                Spacer(modifier = Modifier.height(18.dp))

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.Start) {

                    Text(text = "Product Details", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,   color = MaterialTheme.colors.primaryVariant,)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Input(input = title, placeholder = "Name",1f,type= KeyboardType.Text, line = true) {

                    title = it
                }

                Spacer(modifier = Modifier.height(12.dp))

                Input(input = price, placeholder = "Price",1f, type = KeyboardType.Number, line = true) {

                    price = it
                }


                Spacer(modifier = Modifier.height(12.dp))


                Input(input = subDescription, placeholder = "Subdescription",1f, type = KeyboardType.Text) {

                    subDescription = it
                }

                Spacer(modifier = Modifier.height(12.dp))

                Input(input = description, placeholder = "Description",1f, type = KeyboardType.Text, imageAction = ImeAction.Done) {

                    description = it
                }



                Spacer(modifier = Modifier.height(120.dp))
            }

        }

    }

}


@Composable
fun Input(
    input: String,
    placeholder: String,
    width:Float,
    type:KeyboardType,
    imageAction: ImeAction = ImeAction.Next,
    line:Boolean = false,
    onvalueChange: (String) -> Unit
) {


    OutlinedTextField(
        value = input,
        label = {
            Text(
                text = placeholder
            )
        },
        singleLine = line
        ,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor =   MaterialTheme.colors.onPrimary,
            unfocusedIndicatorColor =  MaterialTheme.colors.onPrimary,
            textColor =  MaterialTheme.colors.onPrimary,
            focusedLabelColor =  MaterialTheme.colors.onPrimary,
            cursorColor =  MaterialTheme.colors.onPrimary,
        )
        ,
        modifier = Modifier
            .fillMaxWidth(width)
            .padding(horizontal = 16.dp),
        onValueChange = { onvalueChange(it) },
        keyboardOptions = KeyboardOptions(keyboardType = type, imeAction = imageAction),
        placeholder = { Text(placeholder.replaceFirstChar { it.uppercase() }) })

}

@Composable
fun PublishBottomBar(onSubmit: () -> Unit, ondiscard: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth().background(MaterialTheme.colors.primary)
            .padding(16.dp), horizontalArrangement = Arrangement.End
    ) {


        OutlinedButton(onClick = { ondiscard() }) {

            Text(text = "Discard", color = MaterialTheme.colors.primaryVariant)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = {
                onSubmit()
            },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primaryVariant)
        ) {
            Text(text = "Submit", color = MaterialTheme.colors.primary)
        }

    }
}

@Composable
fun PublishTopAppBar(ondiscard: () -> Unit) {

    Row(
        Modifier
            .fillMaxWidth().background(MaterialTheme.colors.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .clickable { ondiscard() },
            tint = MaterialTheme.colors.primaryVariant
        )
        Text(
            text = "Publish",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = MaterialTheme.colors.primaryVariant,
        )


    }

}

@Composable
fun RequestContentPermission(imageUrl: (Uri) -> Unit) {

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
        imageUri?.let { imageUrl(it) }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(modifier = Modifier.height(8.dp))

        AsyncImage(
            model = if (imageUri != null) ImageRequest.Builder(LocalContext.current)
                .data(imageUri)
                .crossfade(true)
                .build()
            else R.drawable.insert,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp).clickable {

                    launcher.launch("image/*")
                }

        )



    }
}

