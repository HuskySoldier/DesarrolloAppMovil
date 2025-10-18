package cl.gymtastic.app.ui.trainers

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.gymtastic.app.data.local.entity.TrainerEntity
import cl.gymtastic.app.util.ServiceLocator
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TrainersScreen(nav: NavController){
    val ctx = LocalContext.current
    val flow = remember { ServiceLocator.trainers(ctx).observeAll() }
    val list: List<TrainerEntity> by flow.collectAsStateWithLifecycle(initialValue = emptyList())

    LazyColumn(Modifier.padding(16.dp)){
        items(list.size){ i ->
            val t = list[i]
            Card(Modifier.fillMaxWidth().padding(bottom=8.dp)){
                Column(Modifier.padding(16.dp)){
                    Text(t.nombre, style = MaterialTheme.typography.titleMedium)
                    Text(t.especialidad)
                    Spacer(Modifier.height(8.dp))
                    Row {
                        Button(onClick={
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${t.fono}"))
                            ctx.startActivity(intent)
                        }){ Text("Llamar") }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick={
                            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${t.email}"))
                            ctx.startActivity(intent)
                        }){ Text("Email") }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick={ nav.navigate("booking") }){ Text("Agendar") }
                    }
                }
            }
        }
    }
}