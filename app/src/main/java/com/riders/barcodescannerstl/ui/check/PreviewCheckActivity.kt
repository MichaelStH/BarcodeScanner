package com.riders.barcodescannerstl.ui.check

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.barcodescannerstl.R
import com.riders.barcodescannerstl.core.compose.theme.BarcodeScannerSTLTheme
import com.riders.barcodescannerstl.core.utils.findActivity
import com.riders.barcodescannerstl.data.local.model.OrderModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews


///////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////
@Composable
fun NoItemFound() {
    BarcodeScannerSTLTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No item found.\nPlease try to scan a valid barcode",
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun OrderFound(order: OrderModel) {

    BarcodeScannerSTLTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Order number
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Order Number", style = TextStyle(fontWeight = FontWeight.W200))
                Text(
                    text = order.number,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W700
                    )
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Buyer name
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Buyer's name", style = TextStyle(fontWeight = FontWeight.W200))
                    Text(
                        text = order.buyerName,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.W700
                        )
                    )
                }

                // Order state
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Order state", style = TextStyle(fontWeight = FontWeight.W200))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = order.state,
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.W700
                            )
                        )

                        if (order.state.contains(other = "valid", ignoreCase = true)) {
                            Icon(
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(color = Color.Green, shape = CircleShape),
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Check icon"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CheckContent(viewModel: CheckViewModel) {
    val context = LocalContext.current

    BarcodeScannerSTLTheme {
        Scaffold(
            bottomBar = {
                BottomAppBar(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { (context.findActivity() as CheckActivity).finish() }) {
                        Text(text = stringResource(id = R.string.action_back))
                    }
                }
            }
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                AnimatedContent(
                    targetState = viewModel.order != null,
                    label = "order state transition"
                ) {
                    if (!it) {
                        NoItemFound()
                    } else {
                        OrderFound(viewModel.order!!)
                    }
                }
            }
        }
    }
}

///////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewNoItemFound() {
    BarcodeScannerSTLTheme {
        NoItemFound()
    }
}

@DevicePreviews
@Composable
private fun PreviewOrderFound() {
    val viewModel: CheckViewModel = hiltViewModel()
    BarcodeScannerSTLTheme {
        OrderFound(viewModel.order!!)
    }
}

@DevicePreviews
@Composable
private fun PreviewCheckContent() {
    val viewModel: CheckViewModel = hiltViewModel()
    BarcodeScannerSTLTheme {
        CheckContent(viewModel)
    }
}

