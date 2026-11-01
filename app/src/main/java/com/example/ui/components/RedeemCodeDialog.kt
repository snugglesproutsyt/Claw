package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.repository.RedeemResult
import com.example.ui.theme.ArcadeGold
import com.example.ui.theme.ArcadeSurface
import com.example.ui.theme.ArcadeSurfaceHighlight
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.HazardRed
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPink

@Composable
fun RedeemCodeDialog(
    onDismiss: () -> Unit,
    onRedeem: (String) -> RedeemResult
) {
    var codeText by remember { mutableStateOf("") }
    var resultState by remember { mutableStateOf<RedeemResult?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = ArcadeSurface,
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Brush.verticalGradient(listOf(ArcadeGold, CyberCyan)), RoundedCornerShape(22.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(ArcadeGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CardGiftcard,
                                contentDescription = null,
                                tint = ArcadeGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "REDEEM CODE",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Enter promotional gift codes to claim free tokens, tickets, and exclusive claws!",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = codeText,
                    onValueChange = {
                        codeText = it.uppercase()
                        resultState = null
                    },
                    placeholder = { Text("e.g. CLAW2026", color = Color(0xFF64748B)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (codeText.isNotBlank()) {
                                resultState = onRedeem(codeText)
                            }
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = ArcadeGold
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("redeem_code_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Result message feedback
                resultState?.let { res ->
                    when (res) {
                        is RedeemResult.Success -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(NeonGreen.copy(alpha = 0.15f))
                                    .border(1.dp, NeonGreen, RoundedCornerShape(10.dp))
                                    .padding(10.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = "SUCCESS!",
                                        color = NeonGreen,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Text(
                                        text = res.message,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                        is RedeemResult.AlreadyRedeemed -> {
                            Text(
                                text = "Code '${res.code}' has already been redeemed on this account.",
                                color = ArcadeGold,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                        is RedeemResult.InvalidCode -> {
                            Text(
                                text = "Invalid code '${res.code}'. Please check spelling.",
                                color = HazardRed,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Button(
                    onClick = {
                        if (codeText.isNotBlank()) {
                            resultState = onRedeem(codeText)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ArcadeGold),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("redeem_submit_button")
                ) {
                    Text(
                        text = "REDEEM REWARD",
                        color = Color.Black,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Helpful Sample Codes Pill for quick player discovery
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = ArcadeSurfaceHighlight,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ACTIVE COMMUNITY CODES:",
                            color = CyberCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "CLAW2026 • LUCKYDROP • ARCADEVIP • CYBERBOOST",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
