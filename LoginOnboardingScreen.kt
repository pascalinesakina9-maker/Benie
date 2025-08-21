package org.babetech.borastock.ui.screens.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.babetech.borastock.ui.components.CompottieAnimation

@Composable
fun LoginOnboardingScreen(
    onStartLogin: () -> Unit,
    isLoginVisible: Boolean
) {
    // Animation states
    var isVisible by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition()
    
    // Animation pour l'effet de pulsation du bouton
    val buttonScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(Unit) {
        isVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
                    )
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it / 3 },
                animationSpec = tween(1000, easing = EaseOutCubic)
            ) + fadeIn(animationSpec = tween(1000))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier
                    .wrapContentHeight()
                    .widthIn(max = 500.dp)
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(28.dp),
                        ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(40.dp)
            ) {
                // Animation Lottie avec cadre décoratif
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.02f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    CompottieAnimation(
                        lottiePath = "drawable/animations/Login.json",
                        modifier = Modifier.size(160.dp)
                    )
                }

                // Titre principal avec style amélioré
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Bienvenue sur",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    
                    Text(
                        text = "BoraStock",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 36.sp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            )
                        )
                    )
                }

                // Description avec meilleur espacement
                Text(
                    text = "Votre solution complète pour la gestion intelligente de stock. Connectez-vous pour accéder à votre tableau de bord personnalisé.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 24.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                // Fonctionnalités clés avec icônes
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    FeatureItem(
                        icon = Icons.Default.Inventory,
                        title = "Gestion",
                        subtitle = "Stock"
                    )
                    FeatureItem(
                        icon = Icons.Default.TrendingUp,
                        title = "Analytics",
                        subtitle = "Temps réel"
                    )
                    FeatureItem(
                        icon = Icons.Default.Security,
                        title = "Sécurité",
                        subtitle = "Garantie"
                    )
                }

                // Bouton de connexion amélioré
                AnimatedVisibility(
                    visible = !isLoginVisible,
                    enter = scaleIn(
                        animationSpec = tween(600, delayMillis = 400)
                    ) + fadeIn(animationSpec = tween(600, delayMillis = 400))
                ) {
                    Button(
                        onClick = onStartLogin,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 12.dp
                        ),
                        modifier = Modifier
                            .height(56.dp)
                            .width(220.dp)
                            .graphicsLayer {
                                scaleX = buttonScale
                                scaleY = buttonScale
                            }
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Commencer",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            )
                            Spacer(Modifier.width(12.dp))
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Note de confiance
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = if (index < 5) MaterialTheme.colorScheme.primary 
                                  else MaterialTheme.colorScheme.outline
                        )
                        if (index < 4) Spacer(Modifier.width(2.dp))
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Plateforme sécurisée et fiable",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun FeatureItem(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}