package ua.kpi.its.lab.security.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import ua.kpi.its.lab.security.dto.CinemaRequestDto
import ua.kpi.its.lab.security.dto.CinemaResponseDto

@Composable
fun CinemaScreen(
    token: String,
    scope: CoroutineScope,
    client: HttpClient,
    snackbarHostState: SnackbarHostState
) {
    var cinemas by remember { mutableStateOf<List<CinemaResponseDto>>(listOf()) }
    var loading by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }
    var selectedCinema by remember { mutableStateOf<CinemaResponseDto?>(null) }

    LaunchedEffect(token) {
        loading = true
        delay(1000)
        cinemas = withContext(Dispatchers.IO) {
            try {
                val response = client.get("http://localhost:8080/api/cinemas") {
                    bearerAuth(token)
                }
                loading = false
                response.body()
            } catch (e: Exception) {
                val msg = e.toString()
                snackbarHostState.showSnackbar(msg, withDismissAction = true, duration = SnackbarDuration.Indefinite)
                cinemas
            }
        }
    }

    if (loading) {
        LinearProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        )
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedCinema = null
                    openDialog = true
                },
                content = {
                    Icon(Icons.Filled.Add, "add cinema")
                }
            )
        }
    ) {
        if (cinemas.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text("No cinemas to show", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
        } else {
            LazyColumn(
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant).fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cinemas) { cinema ->
                    CinemaItem(
                        cinema = cinema,
                        onEdit = {
                            selectedCinema = cinema
                            openDialog = true
                        },
                        onRemove = {
                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    try {
                                        val response = client.delete("http://localhost:8080/api/cinemas/${cinema.id}") {
                                            bearerAuth(token)
                                        }
                                        require(response.status.isSuccess())
                                    } catch (e: Exception) {
                                        val msg = e.toString()
                                        snackbarHostState.showSnackbar(msg, withDismissAction = true, duration = SnackbarDuration.Indefinite)
                                    }
                                }

                                loading = true

                                cinemas = withContext(Dispatchers.IO) {
                                    try {
                                        val response = client.get("http://localhost:8080/api/cinemas") {
                                            bearerAuth(token)
                                        }
                                        loading = false
                                        response.body()
                                    } catch (e: Exception) {
                                        val msg = e.toString()
                                        snackbarHostState.showSnackbar(msg, withDismissAction = true, duration = SnackbarDuration.Indefinite)
                                        cinemas
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }

        if (openDialog) {
            CinemaDialog(
                cinema = selectedCinema,
                token = token,
                scope = scope,
                client = client,
                onDismiss = {
                    openDialog = false
                },
                onError = {
                    scope.launch {
                        snackbarHostState.showSnackbar(it, withDismissAction = true, duration = SnackbarDuration.Indefinite)
                    }
                },
                onConfirm = {
                    openDialog = false
                    loading = true
                    scope.launch {
                        cinemas = withContext(Dispatchers.IO) {
                            try {
                                val response = client.get("http://localhost:8080/api/cinemas") {
                                    bearerAuth(token)
                                }
                                loading = false
                                response.body()
                            } catch (e: Exception) {
                                loading = false
                                cinemas
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun CinemaDialog(
    cinema: CinemaResponseDto?,
    token: String,
    scope: CoroutineScope,
    client: HttpClient,
    onDismiss: () -> Unit,
    onError: (String) -> Unit,
    onConfirm: () -> Unit,
) {
    var name by remember { mutableStateOf(cinema?.name ?: "") }
    var address by remember { mutableStateOf(cinema?.address ?: "") }
    var openingDate by remember { mutableStateOf(cinema?.openingDate ?: "") }
    var seats by remember { mutableStateOf(cinema?.seats?.toString() ?: "") }
    var screens by remember { mutableStateOf(cinema?.screens?.toString() ?: "") }
    var soundTechnology by remember { mutableStateOf(cinema?.soundTechnology ?: "") }
    var has3D by remember { mutableStateOf(cinema?.has3D ?: false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.padding(16.dp).wrapContentSize()) {
            Column(
                modifier = Modifier.padding(16.dp, 8.dp).width(IntrinsicSize.Max).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (cinema == null) {
                    Text("Create Cinema")
                } else {
                    Text("Update Cinema")
                }

                HorizontalDivider()
                TextField(name, { name = it }, label = { Text("Name") })
                TextField(address, { address = it }, label = { Text("Address") })
                TextField(openingDate, { openingDate = it }, label = { Text("Opening Date") })
                TextField(seats, { seats = it }, label = { Text("Seats") })
                TextField(screens, { screens = it }, label = { Text("Screens") })
                TextField(soundTechnology, { soundTechnology = it }, label = { Text("Sound Technology") })
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(has3D, { has3D = it })
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Has 3D")
                }

                HorizontalDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.fillMaxWidth(0.1f))
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            scope.launch {
                                try {
                                    val request = CinemaRequestDto(
                                        name, address, openingDate, seats.toInt(), screens.toInt(), soundTechnology, has3D
                                    )
                                    val response = if (cinema == null) {
                                        client.post("http://localhost:8080/api/cinemas") {
                                            bearerAuth(token)
                                            setBody(request)
                                            contentType(ContentType.Application.Json)
                                        }
                                    } else {
                                        client.put("http://localhost:8080/api/cinemas/${cinema.id}") {
                                            bearerAuth(token)
                                            setBody(request)
                                            contentType(ContentType.Application.Json)
                                        }
                                    }
                                    require(response.status.isSuccess())
                                    onConfirm()
                                } catch (e: Exception) {
                                    val msg = e.toString()
                                    onError(msg)
                                }
                            }
                        }
                    ) {
                        if (cinema == null) {
                            Text("Create")
                        } else {
                            Text("Update")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CinemaItem(cinema: CinemaResponseDto, onEdit: () -> Unit, onRemove: () -> Unit) {
    Card(shape = CardDefaults.elevatedShape, elevation = CardDefaults.elevatedCardElevation()) {
        ListItem(
            overlineContent = {
                Text(cinema.name)
            },
            headlineContent = {
                Text(cinema.address)
            },
            supportingContent = {
                Text("Seats: ${cinema.seats}, Screens: ${cinema.screens}")
            },
            trailingContent = {
                Row(modifier = Modifier.padding(0.dp, 20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clip(CircleShape).clickable(onClick = onEdit)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remove",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clip(CircleShape).clickable(onClick = onRemove)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        )
    }
}
