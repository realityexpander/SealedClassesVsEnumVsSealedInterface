package com.example.sealedclassesvsenumvssealedinterface

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sealedclassesvsenumvssealedinterface.ui.theme.SealedClassesVsEnumVsSealedInterfaceTheme
import kotlin.random.Random

class MainActivity(code: Int) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SealedClassesVsEnumVsSealedInterfaceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    Column {

                        //////////////////////////////////////////////////////////
                        // Sealed class with no constructors
                        val errorSealed1: HttpErrorSealed1 = arrayOf(
                            HttpErrorSealed1.NotFound,
                            HttpErrorSealed1.Unauthorized,
                        ).random()
                        when (errorSealed1) {
                            HttpErrorSealed1.NotFound -> Text("errorSealed1: Not Found")
                            HttpErrorSealed1.Unauthorized -> Text("errorSealed1: Unauthorized")
                            else -> {
                                Text("errorSealed1: Unknown")
                            }
                        }
                        Text("errorSealed1.getErrorMessage: ${errorSealed1.getErrorMessage()}")
                        Spacer(modifier = Modifier.height(16.dp))


                        //////////////////////////////////////////////////////////
                        // Sealed class with constructors
                        val errorSealed2: HttpErrorSealed2 = arrayOf(
                            HttpErrorSealed2.NotFound,
                            HttpErrorSealed2.Unauthorized,
                            HttpErrorSealed2.UnauthorizedWithMessage("Unauthorized with message")
                        ).random()
                        when (errorSealed2) {
                            HttpErrorSealed2.NotFound -> Text("errorSealed2: Not Found: ${errorSealed2.code}")
                            HttpErrorSealed2.Unauthorized -> Text("errorSealed2: Unauthorized: ${errorSealed2.code}")
                            is HttpErrorSealed2.UnauthorizedWithMessage -> Text("errorSealed2: UnauthorizedWithMessage: ${errorSealed2.code}, ${errorSealed2.message}")

                            // Note: when using sealed classes and all cases are covered, the else case is not needed.
                            //else -> {
                            //    Text("errorSealed2: Unknown")
                            //}
                        }
                        Text("errorSealed2.getErrorMessage: ${errorSealed2.getErrorMessage()}")
                        Spacer(modifier = Modifier.height(4.dp))

                        // Sealed classes are harder to iterate
                        // show all sealed class members
                        HttpErrorSealed2::class.nestedClasses.forEachIndexed { index, item ->
                            Text("errorSealed2 nestedClasses $index: ${item.simpleName}")
                        }
                        HttpErrorSealed2::class.sealedSubclasses.forEachIndexed { index, item ->
                            Text("errorSealed2 sealedSubclasses $index: ${item.simpleName}")
                        }

                        Spacer(modifier = Modifier.height(16.dp))


                        //////////////////////////////////////////////////////////
                        // Enums
                        val errorEnum: HttpErrorEnum = arrayOf(
                            HttpErrorEnum.NotFound,
                            HttpErrorEnum.Unauthorized,
                            HttpErrorEnum.UnauthorizedWithMessage, // uses the default message from the enum
                            HttpErrorEnum.UnauthorizedWithMessage.apply {
                                message = "Message Changed at runtime" // override the default message
                            }
                        ).random()
                        when (errorEnum) {
                            HttpErrorEnum.NotFound -> Text("errorEnum: Not Found: ${errorEnum.code}")
                            HttpErrorEnum.Unauthorized -> Text("errorEnum: Unauthorized: ${errorEnum.code}")
                            HttpErrorEnum.UnauthorizedWithMessage -> Text("errorEnum: UnauthorizedWithMessage: ${errorEnum.code}, ${errorEnum.message}")

                            // Note: when using enum classes and all cases are covered, the else case is not needed.
                            //else -> {
                            //    Text("errorEnum: Unknown")
                            //}
                        }
                        Text("errorEnum.getErrorMessage: ${errorEnum.getErrorMessage()}")
                        Spacer(modifier = Modifier.height(4.dp))

                        // Enums values are easily iterable
                        HttpErrorEnum.values().forEachIndexed { index, value ->
                            Text("errorEnum values: $index = $value")
                        }
                        Spacer(modifier = Modifier.height(16.dp))


                        //////////////////////////////////////////////////////////
                        // Sealed interface - simple
                        val errorSealedInterface1 = arrayOf(
                            HttpErrorSealedInterface1.NotFound,
                            HttpErrorSealedInterface1.Unauthorized,
                            HttpErrorSealedInterface1.UnauthorizedWithMessage("Unauthorized with message"),
                        ).random()
                        when (errorSealedInterface1) {
                            HttpErrorSealedInterface1.NotFound ->
                                Text("errorSealedInterface1: Not Found")
                            HttpErrorSealedInterface1.Unauthorized ->
                                Text("errorSealedInterface1: Unauthorized")
                            is HttpErrorSealedInterface1.UnauthorizedWithMessage ->
                                Text("errorSealedInterface1: UnauthorizedWithMessage: message= ${errorSealedInterface1.message}")
                            else -> {
                                Text("errorSealedInterface1: Unknown")
                            }
                        }
                        Text("errorSealedInterface1.getErrorMessage: ${errorSealedInterface1.getErrorMessage()}")
                        Spacer(modifier = Modifier.height(16.dp))


                        //////////////////////////////////////////////////////////
                        // Sealed interface - complex
//                        val errorSealedInterface2: HttpErrorSealedInterface2 = HttpErrorSealedInterface2.Default  // NOTE: must use specific type to initialize the variable
//                        val errorSealedInterface2: HttpErrorSealedInterface2 = HttpErrorSealedInterface2.Extra.ExtraWithData("Extra Data")
                        val errorSealedInterface2: HttpErrorSealedInterface2 = HttpErrorSealedInterface2.Extra.ExtraWithDataAndMessage("message", "Extra Data")

//                        val errorSealedInterface2 = arrayOf(
//                            HttpErrorSealedInterface2.NotFoundBase.NotFound,
//                            HttpErrorSealedInterface2.UnauthorizedBase.Unauthorized,
//                            HttpErrorSealedInterface2.UnauthorizedBase.UnauthorizedWithMessage("Unauthorized with message"),
//                            HttpErrorSealedInterface2.Default, // objects are ok
//                            // HttpErrorSealedInterface2.Impossible(100, "Impossible message") // cannot instantiate sealed classes without having its own inner classes
//                            HttpErrorSealedInterface2.Extra.ExtraWithData("Extra Data"),
//                            HttpErrorSealedInterface2.Extra.ExtraWithDataAndMessage("message", "Extra Data"),
//                        ).random()
                        when (errorSealedInterface2) {
                            HttpErrorSealedInterface2.Default ->
                                Text("errorSealedInterface2: Default, ${errorSealedInterface2.code}, ${errorSealedInterface2.message}")
                            HttpErrorSealedInterface2.NotFoundBase.NotFound ->
                                Text("errorSealedInterface2: Not Found")
                            HttpErrorSealedInterface2.UnauthorizedBase.Unauthorized ->
                                Text("errorSealedInterface2: Unauthorized")
                            is HttpErrorSealedInterface2.UnauthorizedBase.UnauthorizedWithMessage ->
                                Text("errorSealedInterface2: UnauthorizedWithMessage: message= ${errorSealedInterface2.message}")
                            is HttpErrorSealedInterface2.Extra.ExtraWithDataAndMessage ->
                                Text("errorSealedInterface2: ExtraWithDataAndMessage: " +
                                        "message= ${errorSealedInterface2.message}, " +
                                        "extra= ${errorSealedInterface2.data}")
                            else -> {
                                Text("errorSealedInterface2: Unknown")
                            }
                        }
                        Text("errorSealedInterface2.getErrorMessage: ${errorSealedInterface2.getErrorMessage()}")
                        Spacer(modifier = Modifier.height(16.dp))


                    }
                }
            }
        }
    }
}


// select on item of array at random
fun <T> Array<T>.random(): T = this[Random.nextInt(size)]