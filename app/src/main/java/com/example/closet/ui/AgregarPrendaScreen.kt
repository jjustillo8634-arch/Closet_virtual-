package com.example.closet.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.closet.data.Categoria

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AgregarPrendaScreen(vm: ClosetViewModel, onTerminar: () -> Unit) {
    var uri by remember { mutableStateOf<Uri?>(null) }
    var categoria by remember { mutableStateOf(Categoria.SUPERIOR) }

    val selector = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { resultado -> if (resultado != null) uri = resultado }

    Scaffold(topBar = { TopAppBar(title = { Text("Nueva prenda") }) }) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.8f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        selector.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (uri != null) {
                    AsyncImage(
                        model = uri,
                        contentDescription = "Foto de la prenda",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("Toca para elegir una foto")
                }
            }

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Categoria.entries.forEach { c ->
                    FilterChip(
                        selected = categoria == c,
                        onClick = { categoria = c },
                        label = { Text(c.etiqueta) }
                    )
                }
            }

            Button(
                onClick = { uri?.let { vm.guardarPrenda(it, categoria, onTerminar) } },
                enabled = uri != null,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar prenda") }
        }
    }
}
