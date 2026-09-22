package com.presentation.profile


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.domain.model.AvatarOption
import com.presentation.home.homeBackground.StarryBackgroundLayer
import moodtracker.shared.generated.resources.Res
import moodtracker.shared.generated.resources.banner
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    isDarkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    onLoggedOut: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState
    var showEditProfileDialog by remember { mutableStateOf(false) }

    val bannerGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF9575CD), Color(0xFFB39DDB))
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))        ) {
            Image(
                painter = painterResource(Res.drawable.banner),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Box(
            modifier = Modifier
                .offset(y = (-50).dp)
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(uiState.avatar.imageRes),
                contentDescription = "Foto de perfil",
                modifier = Modifier.size(88.dp).clip(CircleShape)
            )
        }

        Text(
            text = uiState.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.offset(y = (-40).dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-24).dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { showEditProfileDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.DarkGray),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.DarkGray)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Editar perfil", color = Color.DarkGray, style = MaterialTheme.typography.bodyLarge)
                }
            }

//            Surface(
//                shape = RoundedCornerShape(50),
//                border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 20.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Icon(Icons.Default.DarkMode, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.DarkGray)
//                        Spacer(modifier = Modifier.width(12.dp))
//                        Text("Modo oscuro", color = Color.DarkGray, style = MaterialTheme.typography.bodyLarge)
//                    }
//                    Switch(checked = isDarkTheme, onCheckedChange = onToggleTheme)
//                }
//            }

            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(
                onClick = { viewModel.logout(onLoggedOut) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD81B60)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFD81B60))
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFFD81B60)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar sesión", fontWeight = FontWeight.Bold)
            }
        }
    }
    if (showEditProfileDialog) {
        var newName by remember { mutableStateOf(uiState.name) }
        var selectedAvatar by remember { mutableStateOf(uiState.avatar) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Editar perfil") },
            text = {
                Column {
                    Text("Foto de perfil", style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.height(160.dp)
                    ) {
                        items(AvatarOption.entries) { avatar ->
                            val isSelected = avatar == selectedAvatar
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clickable { selectedAvatar = avatar },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(avatar.imageRes),
                                    contentDescription = avatar.id,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .then(
                                            if (isSelected)
                                                Modifier.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                            else Modifier
                                        )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text("Nombre", style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        singleLine = true,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newName != uiState.name) viewModel.updateName(newName)
                    if (selectedAvatar != uiState.avatar) viewModel.updateAvatar(selectedAvatar)
                    showEditProfileDialog = false
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}