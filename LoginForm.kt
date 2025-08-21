package org.babetech.borastock.ui.screens.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import borastock.composeapp.generated.resources.Res
import borastock.composeapp.generated.resources.google_icon
import org.babetech.borastock.ui.screens.auth.viewmodel.LoginViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginForm(
    onSubmit: () -> Unit = {}
) {
    val viewModel: LoginViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    // Animation states
    var isVisible by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition()
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(Unit) {
        isVisible = true
    }

    LaunchedEffect(currentUser) {
        if (currentUser?.isLoggedIn == true) {
            onSubmit()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.02f)
                    )
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(800, easing = EaseOutCubic)
            ) + fadeIn(animationSpec = tween(800))
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 12.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(40.dp),
                    verticalArrangement = Arrangement.spacedBy(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header avec icône et titre
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Icône de sécurité avec animation
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Sécurité",
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Text(
                            text = "Connexion sécurisée",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Accédez à votre espace BoraStock en toute sécurité",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }

                    // Bouton Google amélioré
                    AnimatedContent(
                        targetState = uiState.isProcessing,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) with
                                    fadeOut(animationSpec = tween(300))
                        }
                    ) { isProcessing ->
                        if (isProcessing) {
                            // État de chargement
                            OutlinedButton(
                                onClick = { },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                enabled = false,
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary.copy(alpha = shimmerAlpha),
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                            MaterialTheme.colorScheme.primary.copy(alpha = shimmerAlpha)
                                        )
                                    )
                                )
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Text(
                                        "Connexion en cours...",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            }
                        } else {
                            // Bouton normal
                            OutlinedButton(
                                onClick = { viewModel.signInWithGoogle() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    width = 1.5.dp,
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.outline,
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                            MaterialTheme.colorScheme.outline
                                        )
                                    )
                                ),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 2.dp,
                                    pressedElevation = 6.dp
                                )
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(resource = Res.drawable.google_icon),
                                        contentDescription = "Google",
                                        modifier = Modifier.size(24.dp),
                                        tint = Color.Unspecified
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Text(
                                        "Continuer avec Google",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 16.sp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Message d'erreur amélioré
                    AnimatedVisibility(
                        visible = uiState.errorMessage != null,
                        enter = slideInVertically(
                            initialOffsetY = { -it / 2 }
                        ) + fadeIn(),
                        exit = slideOutVertically(
                            targetOffsetY = { -it / 2 }
                        ) + fadeOut()
                    ) {
                        uiState.errorMessage?.let { error ->
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "⚠️ $error",
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    modifier = Modifier.padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    // Note de sécurité
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Connexion sécurisée SSL",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}