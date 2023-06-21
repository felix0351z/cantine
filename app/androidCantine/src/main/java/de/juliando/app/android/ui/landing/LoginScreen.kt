package de.juliando.app.android.ui.landing

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.juliando.app.android.MainActivity
import de.juliando.app.android.R
import de.juliando.app.android.ui.theme.CantineTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration.Companion.milliseconds
import androidx.compose.foundation.text.KeyboardActions as KeyboardActions1

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel = koinViewModel()) {

    val usernameInput by viewModel.usernameInput.collectAsStateWithLifecycle()
    val passwordInput by viewModel.passwordInput.collectAsStateWithLifecycle()
    val serverURLInput by viewModel.serverURLInput.collectAsStateWithLifecycle()

    val keyboardController = LocalSoftwareKeyboardController.current
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    val mContext = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .background(
                color = CantineTheme.backgroundColor
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        var passwordVisibility by remember { mutableStateOf(true) }

        Spacer(modifier = Modifier.padding(vertical = 25 .dp))

        Text(
            text = stringResource(id = R.string.landing_3),
            fontWeight = FontWeight.Bold,
            fontSize = 33.sp,
            color = CantineTheme.white,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 20.dp)
        )

        Text(
            text = stringResource(id = R.string.landing_4),
            color = CantineTheme.grey1,
            fontSize = 15.sp,
            modifier = Modifier
                .width(350.dp)
                .align(Alignment.Start)
                .padding(horizontal = 20.dp)
                .align(Alignment.Start)
        )

        Spacer(modifier = Modifier.padding(vertical = 34.dp))

        var errorMessageText = remember {
            mutableStateOf("")
        }

        //Error if sth is wrong/does not exist
        Box(
            Modifier
                .height(48.dp)
                .padding(horizontal = 18.dp, vertical = 0.dp)
                .then(
                    if (errorMessageText.value != "") Modifier
                        .background(
                            color = Color.Red.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(5.dp)
                        )
                        .border(width = 1.dp, color = Color.Red, shape = RoundedCornerShape(5.dp))
                    else Modifier
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(errorMessageText.value!="") {
                    Icon(
                        imageVector = Icons.Outlined.Warning,
                        contentDescription = "Warning",
                        tint = Color.White,
                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
                    )
                }
                Text(
                    text = errorMessageText.value,
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 0.dp),
                    maxLines = 2,
                    lineHeight = 20.sp
                )
            }

        }

        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        LoginTextField(
            keyboardController = keyboardController,
            bringIntoViewRequester = bringIntoViewRequester,
            coroutineScope = coroutineScope,
            label = "Server URL",
            value = serverURLInput,
            placeholder = "https://...",
            onValueChange = viewModel::updateServerURLInput,
            trailingIcon = { Icon(imageVector = Icons.Outlined.Dns, contentDescription = "Dns") }
        )

        Spacer(modifier = Modifier.padding(vertical = 6.dp))

        LoginTextField(
            keyboardController = keyboardController,
            bringIntoViewRequester = bringIntoViewRequester,
            coroutineScope = coroutineScope,
            label = "Nutzername",
            value = usernameInput,
            onValueChange = viewModel::updateUsernameInput,
            trailingIcon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = "Person") }
        )

        LoginTextField(
            keyboardController = keyboardController,
            bringIntoViewRequester = bringIntoViewRequester,
            coroutineScope = coroutineScope,
            label = "Passwort",
            value = passwordInput,
            onValueChange = viewModel::updatePasswordInput,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            visualTransformation = if(passwordVisibility)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    if(passwordVisibility)
                        Icon(imageVector = Icons.Outlined.VisibilityOff, contentDescription = "VisibilityOff")
                    else
                        Icon(imageVector = Icons.Outlined.Visibility, contentDescription = "Visibility")
                }
            }
        )

        Spacer(modifier = Modifier.padding(vertical = 14.5.dp))

        LandingButton(
            modifier = Modifier,
            text = stringResource(id = R.string.login),
            onClick = {
                viewModel.clearErrorMessage()
                keyboardController?.hide()
                coroutineScope.launch {
                    if(viewModel.signIn()){
                        mContext.startActivity(Intent(mContext, MainActivity::class.java))
                    }
                    errorMessageText.value = viewModel.errorMessage.value
                }
            }
        )

        Spacer(modifier = Modifier
            .padding(vertical = 8.dp)
            .bringIntoViewRequester(bringIntoViewRequester)
        )
    }

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun LoginTextField(
    keyboardController: SoftwareKeyboardController?,
    bringIntoViewRequester: BringIntoViewRequester,
    coroutineScope: CoroutineScope,
    label: String,
    value: String,
    placeholder: String? = null,
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    modifier: Modifier = Modifier
){

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(65.dp)
            .onFocusEvent { focusState ->
                if (focusState.hasFocus) {
                    coroutineScope.launch {
                        delay(200.milliseconds)
                        bringIntoViewRequester.bringIntoView()
                    }
                }
            },
        trailingIcon = trailingIcon,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = CantineTheme.white,
            unfocusedBorderColor = CantineTheme.grey1,
            focusedTextColor = CantineTheme.white
        ),
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            if (placeholder != null) {
                Text(placeholder)
            }
        },
        label = { Text(
            text = label,
            color = CantineTheme.white
        )},
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions1(onDone = { keyboardController?.hide()})
    )

}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}