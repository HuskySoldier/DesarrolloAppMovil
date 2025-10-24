package cl.gymtastic.app.ui.admin

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import cl.gymtastic.app.data.local.db.GymTasticDatabase // Importa la DB
import cl.gymtastic.app.data.local.entity.ProductEntity
import cl.gymtastic.app.data.local.entity.TrainerEntity
import cl.gymtastic.app.data.local.entity.UserEntity // Importa UserEntity
import cl.gymtastic.app.util.ImageUriUtils
import cl.gymtastic.app.util.ServiceLocator
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

// --- Pantalla Principal de Admin ---

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AdminScreen(
    nav: NavController,
    windowSizeClass: WindowSizeClass
) {
    val cs = MaterialTheme.colorScheme
    val scope = rememberCoroutineScope()

    // --- MODIFICADO: Añadida pestaña Usuarios ---
    val tabTitles = listOf("Productos", "Trainers", "Usuarios")
    val pagerState = rememberPagerState(pageCount = { tabTitles.size })

    // Fondo
    val bg = Brush.verticalGradient(
        listOf(cs.primary.copy(alpha = 0.22f), cs.surface)
    )

    // Lógica de Ancho
    val widthSizeClass = windowSizeClass.widthSizeClass
    val isCompact = widthSizeClass == WindowWidthSizeClass.Compact
    val pagerModifier = if (isCompact) {
        Modifier.fillMaxSize()
    } else {
        Modifier.fillMaxSize().padding(horizontal = 100.dp) // Centra el contenido en tablets
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administración", color = cs.onBackground) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = cs.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = cs.background,
                    titleContentColor = cs.onBackground,
                    navigationIconContentColor = cs.onBackground
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(padding)
        ) {
            // --- Tabs ---
            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(title) }
                    )
                }
            }

            // --- Pager ---
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top
            ) { page ->
                Box(
                    modifier = pagerModifier.padding(16.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    when (page) {
                        0 -> AdminProductsTab()
                        1 -> AdminTrainersTab()
                        // --- MODIFICADO: Añadido caso Usuarios ---
                        2 -> AdminUsersTab()
                    }
                }
            }
        }
    }
}

// --- Pestaña de Productos (Sin Cambios recientes) ---
@Composable
fun AdminProductsTab() {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val repo = remember { ServiceLocator.products(ctx) }
    val money = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply { maximumFractionDigits = 0 }
    }

    // --- Estado ---
    val merch by repo.observeMerch().collectAsStateWithLifecycle(initialValue = emptyList())
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<ProductEntity?>(null) }
    var editingProduct by remember { mutableStateOf<ProductEntity?>(null) }

    // --- UI ---
    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Nuevo Producto") },
                icon = { Icon(Icons.Default.Add, null) },
                onClick = {
                    editingProduct = null // Nuevo producto
                    showEditDialog = true
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            items(merch, key = { it.id }) { product ->
                ProductAdminCard(
                    product = product,
                    priceText = money.format(product.precio),
                    onEdit = {
                        editingProduct = product
                        showEditDialog = true
                    },
                    onDelete = {
                        showDeleteDialog = product
                    }
                )
            }
        }
    }

    // --- Diálogo de Edición/Creación ---
    if (showEditDialog) {
        ProductEditDialog(
            product = editingProduct,
            onDismiss = { showEditDialog = false },
            onSave = { productToSave, oldImageUri ->
                scope.launch {
                    // Si la imagen cambió, borramos la antigua
                    if (oldImageUri != productToSave.img) {
                        withContext(Dispatchers.IO) {
                            ImageUriUtils.deleteFileFromInternalStorage(oldImageUri)
                        }
                    }
                    repo.save(productToSave)
                    showEditDialog = false
                }
            }
        )
    }

    // --- Diálogo de Eliminación ---
    showDeleteDialog?.let { productToDelete ->
        DeleteConfirmDialog(
            itemName = productToDelete.nombre,
            onDismiss = { showDeleteDialog = null },
            onConfirm = {
                scope.launch {
                    // Borramos el producto Y la imagen asociada
                    withContext(Dispatchers.IO) {
                        ImageUriUtils.deleteFileFromInternalStorage(productToDelete.img)
                    }
                    repo.delete(productToDelete)
                    showDeleteDialog = null
                }
            }
        )
    }
}

@Composable
fun ProductAdminCard(
    product: ProductEntity,
    priceText: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- Miniatura de la imagen ---
            SubcomposeAsyncImage(
                model = product.img, // Carga la URL/URI
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                loading = { CircularProgressIndicator(Modifier.size(24.dp))},
                error = {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Category, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                success = { SubcomposeAsyncImageContent() }
            )

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(product.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    "Stock: ${product.stock ?: "N/A"}  •  Precio: $priceText",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, "Editar", tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ProductEditDialog(
    product: ProductEntity?,
    onDismiss: () -> Unit,
    // Devuelve el producto Y la URI de la imagen antigua (para borrarla si cambia)
    onSave: (ProductEntity, String?) -> Unit
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    val oldImageUri = product?.img // Guardamos la URI antigua

    var name by remember { mutableStateOf(product?.nombre ?: "") }
    var price by remember { mutableStateOf(product?.precio?.toInt()?.toString() ?: "") } // Int para precio
    var stock by remember { mutableStateOf(product?.stock?.toString() ?: "") }
    var desc by remember { mutableStateOf(product?.descripcion ?: "") }
    var imgUriString by remember { mutableStateOf(product?.img ?: "") }

    val isFormValid by derivedStateOf {
        name.isNotBlank() && price.toIntOrNull() != null && stock.toIntOrNull() != null
    }

    // --- Launcher para el selector de imágenes ---
    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            // Copiamos la imagen al almacenamiento interno en un hilo IO
            scope.launch(Dispatchers.IO) {
                val internalUri = ImageUriUtils.copyUriToInternalStorage(
                    context = ctx,
                    uri = uri,
                    fileNamePrefix = "prod_${product?.id ?: "new"}"
                )
                withContext(Dispatchers.Main) {
                    if (internalUri != null) {
                        // Borramos la imagen anterior SI es diferente y existe
                        if (oldImageUri != null && oldImageUri != internalUri) {
                            withContext(Dispatchers.IO){
                                ImageUriUtils.deleteFileFromInternalStorage(oldImageUri)
                            }
                        }
                        imgUriString = internalUri
                    } else {
                        Toast.makeText(ctx, "Error al copiar la imagen", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (product == null) "Nuevo Producto" else "Editar Producto") },
        text = {
            // Envolvemos en un Scroll por si no cabe
            Column(Modifier.verticalScroll(rememberScrollState())) {

                // --- Selector de Imagen ---
                Spacer(Modifier.height(8.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            pickMedia.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(ctx)
                            .data(imgUriString.ifBlank { null }) // Carga la URI (String)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Imagen del producto",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        loading = { CircularProgressIndicator(Modifier.size(32.dp)) },
                        error = {
                            // Placeholder si no hay imagen
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(Icons.Default.PhotoCamera, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Seleccionar Imagen", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        },
                        success = { SubcomposeAsyncImageContent() }
                    )

                    // Botón para quitar la imagen
                    if (imgUriString.isNotBlank()) {
                        IconButton(
                            onClick = {
                                // Borra la imagen del storage ANTES de limpiar la URI del estado
                                scope.launch(Dispatchers.IO){
                                    ImageUriUtils.deleteFileFromInternalStorage(imgUriString)
                                }
                                imgUriString = ""
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f), CircleShape)
                        ) {
                            Icon(Icons.Default.Clear, "Quitar imagen", tint = Color.White)
                        }
                    }
                }
                // --- Fin Selector de Imagen ---

                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Precio (CLP)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = stock,
                        onValueChange = { stock = it },
                        label = { Text("Stock") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Descripción (opcional)") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val entity = ProductEntity(
                        id = product?.id ?: 0, // 0 para autogenerar
                        nombre = name.trim(),
                        descripcion = desc.trim(),
                        precio = price.toDoubleOrNull() ?: 0.0, // Precio como Double
                        stock = stock.toIntOrNull(), // Stock como Int?
                        tipo = "merch", // Asume "merch" por defecto
                        img = imgUriString.trim().ifBlank { null } // Guarda la URI (String)
                    )
                    onSave(entity, oldImageUri)
                },
                enabled = isFormValid
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}


// --- Pestaña de Trainers (Sin Cambios recientes) ---

@Composable
fun AdminTrainersTab() {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val repo = remember { ServiceLocator.trainers(ctx) }

    // --- Estado ---
    val trainers by repo.observeAll().collectAsStateWithLifecycle(initialValue = emptyList())
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<TrainerEntity?>(null) }
    var editingTrainer by remember { mutableStateOf<TrainerEntity?>(null) }

    // --- UI ---
    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Nuevo Trainer") },
                icon = { Icon(Icons.Default.Add, null) },
                onClick = {
                    editingTrainer = null // Nuevo trainer
                    showEditDialog = true
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            items(trainers, key = { it.id }) { trainer ->
                TrainerAdminCard( // <-- Tarjeta actualizada
                    trainer = trainer,
                    onEdit = {
                        editingTrainer = trainer
                        showEditDialog = true
                    },
                    onDelete = {
                        showDeleteDialog = trainer
                    }
                )
            }
        }
    }

    // --- Diálogo de Edición/Creación ---
    if (showEditDialog) {
        TrainerEditDialog( // <-- Diálogo actualizado
            trainer = editingTrainer,
            onDismiss = { showEditDialog = false },
            onSave = { trainerToSave, oldImageUri -> // <-- Firma actualizada
                scope.launch {
                    // Borra la imagen antigua si cambió
                    if (oldImageUri != trainerToSave.img) {
                        withContext(Dispatchers.IO) {
                            ImageUriUtils.deleteFileFromInternalStorage(oldImageUri)
                        }
                    }
                    repo.save(trainerToSave)
                    showEditDialog = false
                }
            }
        )
    }

    // --- Diálogo de Eliminación ---
    showDeleteDialog?.let { trainerToDelete ->
        DeleteConfirmDialog(
            itemName = trainerToDelete.nombre,
            onDismiss = { showDeleteDialog = null },
            onConfirm = {
                scope.launch {
                    // Borra también la imagen
                    withContext(Dispatchers.IO) {
                        ImageUriUtils.deleteFileFromInternalStorage(trainerToDelete.img)
                    }
                    repo.delete(trainerToDelete)
                    showDeleteDialog = null
                }
            }
        )
    }
}

@Composable
fun TrainerAdminCard( // <-- Tarjeta actualizada
    trainer: TrainerEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- Miniatura de la imagen ---
            SubcomposeAsyncImage(
                model = trainer.img,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape) // Círculo para trainers
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                loading = { CircularProgressIndicator(Modifier.size(24.dp)) },
                error = {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                success = { SubcomposeAsyncImageContent() }
            )

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(trainer.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    trainer.especialidad,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, "Editar", tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun TrainerEditDialog( // <-- Diálogo actualizado
    trainer: TrainerEntity?,
    onDismiss: () -> Unit,
    onSave: (TrainerEntity, String?) -> Unit // <-- Firma actualizada
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val oldImageUri = trainer?.img // Guardamos la URI antigua

    var name by remember { mutableStateOf(trainer?.nombre ?: "") }
    var especialidad by remember { mutableStateOf(trainer?.especialidad ?: "") }
    var fono by remember { mutableStateOf(trainer?.fono ?: "") }
    var email by remember { mutableStateOf(trainer?.email ?: "") }
    var imgUriString by remember { mutableStateOf(trainer?.img ?: "") } // <-- Estado de imagen añadido

    val isFormValid by derivedStateOf {
        name.isNotBlank() && especialidad.isNotBlank() && email.contains("@") && fono.isNotBlank() // Valida email
    }

    // --- Launcher para el selector de imágenes ---
    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scope.launch(Dispatchers.IO) {
                val internalUri = ImageUriUtils.copyUriToInternalStorage(
                    context = ctx,
                    uri = uri,
                    fileNamePrefix = "trainer_${trainer?.id ?: "new"}"
                )
                withContext(Dispatchers.Main) {
                    if (internalUri != null) {
                        // Borra la imagen anterior si es diferente
                        if (oldImageUri != null && oldImageUri != internalUri) {
                            withContext(Dispatchers.IO){
                                ImageUriUtils.deleteFileFromInternalStorage(oldImageUri)
                            }
                        }
                        imgUriString = internalUri
                    } else {
                        Toast.makeText(ctx, "Error al copiar la imagen", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (trainer == null) "Nuevo Trainer" else "Editar Trainer") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {

                // --- Selector de Imagen (Circular) ---
                Spacer(Modifier.height(8.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(150.dp), // Lo mantenemos cuadrado
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        Modifier
                            .size(150.dp) // Tamaño del círculo
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                CircleShape
                            )
                            .clickable {
                                pickMedia.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(ctx)
                                .data(imgUriString.ifBlank { null })
                                .crossfade(true)
                                .build(),
                            contentDescription = "Foto del Trainer",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            loading = { CircularProgressIndicator(Modifier.size(32.dp))},
                            error = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.PhotoCamera, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("Seleccionar Foto", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            },
                            success = { SubcomposeAsyncImageContent() }
                        )
                    }
                    // Botón para quitar la imagen
                    if (imgUriString.isNotBlank()) {
                        IconButton(
                            onClick = {
                                // Borra la imagen antes de limpiar
                                scope.launch(Dispatchers.IO){
                                    ImageUriUtils.deleteFileFromInternalStorage(imgUriString)
                                }
                                imgUriString = ""
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd) // Posición diferente
                                .padding(4.dp)
                                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f), CircleShape)
                        ) {
                            Icon(Icons.Default.Clear, "Quitar imagen", tint = Color.White)
                        }
                    }
                }
                // --- Fin Selector de Imagen ---

                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre Completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = especialidad,
                    onValueChange = { especialidad = it },
                    label = { Text("Especialidad") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = fono,
                    onValueChange = { fono = it },
                    label = { Text("Fono (ej: +569...)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val entity = TrainerEntity(
                        id = trainer?.id ?: 0, // 0 para autogenerar
                        nombre = name.trim(),
                        especialidad = especialidad.trim(),
                        fono = fono.trim(),
                        email = email.trim(),
                        img = imgUriString.trim().ifBlank { null } // <-- Guardar imagen
                    )
                    onSave(entity, oldImageUri) // <-- Devolver URI antigua
                },
                enabled = isFormValid
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}


// --- Pestaña de Usuarios (NUEVA) ---

@Composable
fun AdminUsersTab() {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val usersDao = remember { GymTasticDatabase.get(ctx).users() }
    val authRepo = remember { ServiceLocator.auth(ctx) } // Para hashing
    val authPrefs = remember { authRepo.prefs() }

    // Email del admin actual (para excluirlo y no auto-eliminarse)
    val adminEmail by authPrefs.userEmailFlow.collectAsStateWithLifecycle("")

    // --- Estado ---
    val users by remember(adminEmail) {
        if (adminEmail.isNotBlank()) {
            usersDao.observeAllExcept(adminEmail)
        } else {
            flowOf(emptyList()) // No mostrar nada si no se sabe quién es el admin
        }
    }.collectAsStateWithLifecycle(initialValue = emptyList())

    var showAddDialog by remember { mutableStateOf(false) }
    var showResetPassDialog by remember { mutableStateOf<UserEntity?>(null) }
    var showDeleteDialog by remember { mutableStateOf<UserEntity?>(null) }

    // --- UI ---
    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Nuevo Usuario") },
                icon = { Icon(Icons.Default.Add, null) },
                onClick = { showAddDialog = true }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            items(users, key = { it.email }) { user ->
                UserAdminCard(
                    user = user,
                    onResetPassword = { showResetPassDialog = user },
                    onDelete = { showDeleteDialog = user }
                )
            }
        }
    }

    // --- Diálogo Añadir Usuario ---
    if (showAddDialog) {
        UserAddDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { email, name, password, rol ->
                scope.launch {
                    // Usamos AuthRepository para registrar (maneja hashing y chequeo de existencia)
                    val success = authRepo.register(email, password, name) // Asume rol 'user' por defecto
                    if (success) {
                        // Opcional: Si quieres cambiar el rol después de registrar
                        // usersDao.updateRole(email, rol)
                        Toast.makeText(ctx, "Usuario $email creado", Toast.LENGTH_SHORT).show()
                        showAddDialog = false
                    } else {
                        Toast.makeText(ctx, "El email $email ya existe", Toast.LENGTH_SHORT).show()
                        // Mantenemos el diálogo abierto
                    }
                }
            }
        )
    }

    // --- Diálogo Resetear Contraseña ---
    showResetPassDialog?.let { userToReset ->
        UserResetPasswordDialog(
            user = userToReset,
            onDismiss = { showResetPassDialog = null },
            onConfirm = { newPassword ->
                scope.launch {
                    val newHash = authRepo.hashPassword(newPassword)
                    val updatedRows = usersDao.updatePasswordHash(userToReset.email, newHash)
                    if (updatedRows > 0) {
                        Toast.makeText(ctx, "Contraseña de ${userToReset.email} actualizada", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(ctx, "Error al actualizar contraseña", Toast.LENGTH_SHORT).show()
                    }
                    showResetPassDialog = null
                }
            }
        )
    }

    // --- Diálogo Eliminar Usuario ---
    showDeleteDialog?.let { userToDelete ->
        // Usamos el mismo diálogo genérico de confirmación
        DeleteConfirmDialog(
            itemName = userToDelete.email, // Identificamos por email
            onDismiss = { showDeleteDialog = null },
            onConfirm = {
                scope.launch {
                    val deletedRows = usersDao.deleteByEmail(userToDelete.email)
                    if (deletedRows > 0) {
                        Toast.makeText(ctx, "Usuario ${userToDelete.email} eliminado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(ctx, "Error al eliminar usuario", Toast.LENGTH_SHORT).show()
                    }
                    showDeleteDialog = null
                }
            }
        )
    }
}

@Composable
fun UserAdminCard(
    user: UserEntity,
    onResetPassword: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(user.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // Opcional: Mostrar rol si lo necesitas
                // Text("Rol: ${user.rol}", style = MaterialTheme.typography.bodySmall)
            }
            // Botón para resetear contraseña
            IconButton(onClick = onResetPassword) {
                Icon(Icons.Default.LockReset, "Resetear Contraseña", tint = MaterialTheme.colorScheme.secondary)
            }
            // Botón eliminar
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Eliminar Usuario", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun UserAddDialog(
    onDismiss: () -> Unit,
    onAdd: (email: String, name: String, password: String, rol: String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    // Por ahora, rol fijo 'user', podrías añadir un Dropdown si necesitas 'admin'
    val rol = "user"

    val isFormValid by derivedStateOf {
        email.contains("@") && name.isNotBlank() && password.length >= 6
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Usuario") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    singleLine = true
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    singleLine = true
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña Inicial") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { if(isFormValid) onAdd(email, name, password, rol) }),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, null)
                        }
                    },
                    singleLine = true,
                    supportingText = { Text("Mínimo 6 caracteres") }
                )
                // Opcional: Dropdown para seleccionar rol si es necesario
            }
        },
        confirmButton = {
            Button(
                onClick = { onAdd(email.trim().lowercase(), name.trim(), password, rol) }, // Normaliza email y nombre
                enabled = isFormValid
            ) { Text("Crear") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun UserResetPasswordDialog(
    user: UserEntity,
    onDismiss: () -> Unit,
    onConfirm: (newPassword: String) -> Unit
) {
    var newPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isValid by derivedStateOf { newPassword.length >= 6 }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Resetear Contraseña") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Usuario: ${user.email}")
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Nueva Contraseña") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { if(isValid) onConfirm(newPassword) }),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, null)
                        }
                    },
                    singleLine = true,
                    supportingText = { Text("Mínimo 6 caracteres") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(newPassword) },
                enabled = isValid
            ) { Text("Actualizar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}


// --- Diálogo Genérico de Confirmación (Sin cambios) ---

@Composable
fun DeleteConfirmDialog(
    itemName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar Eliminación") },
        text = {
            Text("¿Estás seguro de que quieres eliminar \"$itemName\"? Esta acción no se puede deshacer.")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
