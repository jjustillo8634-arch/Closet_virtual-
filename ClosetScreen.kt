package com.example.closet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.closet.data.Categoria
import com.example.closet.data.Prenda
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClosetScreen(vm: ClosetViewModel, onAgregar: () -> Unit) {
    val prendas by vm.prendas.collectAsState()
    var filtro by remember { mutableStateOf<Categoria?>(null) }
    val visibles = prendas.filter { filtro == null || it.categoria == filtro }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mi closet") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregar) {
                Icon(Icons.Default.Add, contentDescription = "Agregar prenda")
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = filtro == null,
                        onClick = { filtro = null },
                        label = { Text("Todo") }
                    )
                }
                items(Categoria.entries) { c ->
                    FilterChip(
                        selected = filtro == c,
                        onClick = { filtro = c },
                        label = { Text(c.etiqueta) }
                    )
                }
            }

            if (visibles.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aún no hay prendas. Toca + para agregar la primera.")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(visibles, key = { it.id }) { prenda ->
                        PrendaCard(prenda, onEliminar = { vm.eliminar(prenda) })
                    }
                }
            }
        }
    }
}

@Composable
private fun PrendaCard(prenda: Prenda, onEliminar: () -> Unit) {
    Card {
        Box {
            AsyncImage(
                model = File(prenda.fotoPath),
                contentDescription = prenda.categoria.etiqueta,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.8f)
            )
            Box(
                Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color(prenda.color))
                    .border(1.dp, Color.White, CircleShape)
            )
            IconButton(onClick = onEliminar, modifier = Modifier.align(Alignment.TopEnd)) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}
