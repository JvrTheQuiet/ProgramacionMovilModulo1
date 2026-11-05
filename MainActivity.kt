package com.example.ejerciciomodulo1programacionmoviljoyarzun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//LET'S DO THIS
//Registrar Tareas, asignarles prioridad y categoría.
data class Recordatorio(
    val id: String,
    val titulo: String,
    val descripcion: String?,
    val categoria: String,
    val prioridad: Prioridad,
    val estado: EstadoRecordatorio,
)
sealed class Prioridad{
    object Alta: Prioridad()
    object Media: Prioridad()
    object Baja: Prioridad()
}
sealed class EstadoRecordatorio{
    object Pendiente: EstadoRecordatorio()
    object Completado: EstadoRecordatorio()
}
//Vamos con la interface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavegacionPrincipal()
                }
            }
        }
    }
}

enum class Pantalla {
    BIENVENIDA,
    LISTA_RECORDATORIOS
}

@Composable
fun NavegacionPrincipal() {
    var pantallaActual by remember { mutableStateOf(Pantalla.BIENVENIDA) }

    when (pantallaActual) {
        Pantalla.BIENVENIDA -> {
            PantallaBienvenida(
                onComenzarClicked = {
                    pantallaActual = Pantalla.LISTA_RECORDATORIOS
                }
            )
        }
        Pantalla.LISTA_RECORDATORIOS -> {
            PantallaInicioRecordatorios()
        }
    }
}

//Pantalla de bienvenida
@Composable
fun PantallaBienvenida(onComenzarClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¡Bienvenido a la App Modulo 1!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "por Javier Oyarzún Orellana.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onComenzarClicked,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(text = "Comenzar", fontSize = 16.sp)
        }
    }
}

//Pantalla principal
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicioRecordatorios() {
    val coloresCategorias = mapOf(
        "Trabajo" to Color(0xFF2F4F4F), //"A Color Similar to Slate"
        "Ocio" to Color(0xFFBCDDB3), //"A Mann's Mint"
        "Estudios" to Color(0xFFE7B53B), //"Australium Gold"
        "Hogar" to Color(0xFF808000) //"Drably Olive"
    )
//Colores extraidos de https://wiki.teamfortress.com/wiki/Template:Paint_Can
    val listaRecordatorios = listOf(
        Recordatorio("1", "Marcar entrada", "Entrada a las 5:45 am", "Trabajo", Prioridad.Alta, EstadoRecordatorio.Pendiente),
        Recordatorio("2", "Terminar tarea Módulo 1", "Programación Móvil", "Estudios", Prioridad.Alta, EstadoRecordatorio.Pendiente),
        Recordatorio("3", "Escribir para el libro", "", "Ocio", Prioridad.Baja, EstadoRecordatorio.Pendiente),
        Recordatorio("4", "Sacar la basura", "Atrae las moscas", "Hogar", Prioridad.Media, EstadoRecordatorio.Completado),
    )
    val categoriasUnicas: Set<String> = listaRecordatorios.map { it.categoria }.toSet()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Tareas") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listaRecordatorios) { recordatorio ->
                val colorCategoria = coloresCategorias[recordatorio.categoria] ?: Color.Gray
                TarjetaRecordatorio(recordatorio = recordatorio, colorCategoria = colorCategoria)
            }
        }
    }
}

@Composable
fun TarjetaRecordatorio(recordatorio: Recordatorio, colorCategoria: Color) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(color = colorCategoria, shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = recordatorio.titulo, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Categoría: ${recordatorio.categoria}", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            val textoEstado = if (recordatorio.estado is EstadoRecordatorio.Pendiente) "Pendiente" else "Completado"
            Text(
                text = textoEstado,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (recordatorio.estado is EstadoRecordatorio.Pendiente) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }
    }
}