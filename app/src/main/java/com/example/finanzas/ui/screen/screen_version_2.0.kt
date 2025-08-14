package com.example.finanzas.ui.screen



import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanzas.R

@Composable
fun DashboardScreenGreen() {
    Scaffold(
        bottomBar = { BottomNavBarGreen() },
        floatingActionButton = { CenterFabGreen() },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF0F7F4))
        ) {
            TopSectionGreen()
            BalanceCardGreen()
            StatisticsSectionGreen()
            UpcomingPaymentsGreen()
            RecentTransactionsGreen()
        }
    }
}

@Composable
fun TopSectionGreen() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Versión 2.0", fontSize = 16.sp, color = Color.Gray)
            Text("Hola Ariadne Gasta pues", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CircleButtonGreen(painterResource(id = R.drawable.search))
            CircleButtonGreen(painterResource(id = R.drawable.notifications))
        }
    }
}

@Composable
fun CircleButtonGreen(icon: Painter) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.9f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = Color(0xFF1B5E20)
        )
    }
}

@Composable
fun BalanceCardGreen() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF43A047), Color(0xFF66BB6A))
                )
            )
            .padding(20.dp)
    ) {
        Column {
            Text("Saldo Actual", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
            Text("$4,570.80", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun StatisticsSectionGreen() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text("Estadísticas", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1B5E20))
        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Distribución de gastos", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                Spacer(modifier = Modifier.height(8.dp))
                PieChartSampleGreen(
                    colors = listOf(
                        Color(0xFF43A047),
                        Color(0xFF66BB6A),
                        Color(0xFF81C784),
                        Color(0xFFA5D6A7)
                    ),
                    proportions = listOf(0.45f, 0.25f, 0.20f, 0.10f)
                )
            }
        }
    }
}

@Composable
fun PieChartSampleGreen(colors: List<Color>, proportions: List<Float>) {
    Canvas(modifier = Modifier.size(130.dp)) {
        var startAngle = -90f
        proportions.forEachIndexed { index, proportion ->
            drawArc(
                color = colors[index],
                startAngle = startAngle,
                sweepAngle = proportion * 360f,
                useCenter = true
            )
            startAngle += proportion * 360f
        }
    }
}

@Composable
fun UpcomingPaymentsGreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Próximos pagos", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1B5E20))
            Text("Ver todos", color = Color.Gray, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(2) {
                PaymentCardGreen()
            }
        }
    }
}

@Composable
fun PaymentCardGreen() {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF66BB6A)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.wallet),
                contentDescription = null,
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Adobe Premium", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text("$30/mes", color = Color.Gray, fontSize = 12.sp)
        Text("2 días restantes", color = Color.Gray, fontSize = 10.sp)
    }
}

@Composable
fun RecentTransactionsGreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Transacciones recientes", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1B5E20))
            Text("Ver todas", color = Color.Gray, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(3) {
                TransactionItemGreen()
            }
        }
    }
}

@Composable
fun TransactionItemGreen() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.wallet),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("Apple Inc.", fontWeight = FontWeight.Bold)
                Text("21 Sep, 03:02 PM", color = Color.Gray, fontSize = 12.sp)
            }
        }
        Text("-$230.50", color = Color.Red, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BottomNavBarGreen() {
    NavigationBar(
        containerColor = Color.White.copy(alpha = 0.95f),
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.shopping), contentDescription = null) },
            selected = true,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.attach_money), contentDescription = null) },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.users), contentDescription = null) },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.settings_24dp_1f1f1f_fill0_wght400_grad0_opsz24), contentDescription = null) },
            selected = false,
            onClick = {}
        )
    }
}

@Composable
fun CenterFabGreen() {
    FloatingActionButton(
        onClick = {},
        containerColor = Color(0xFF43A047),
        shape = CircleShape
    ) {
        Icon(painter = painterResource(id = R.drawable.add), contentDescription = null, tint = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenGreenPreview() {
    DashboardScreenGreen()
}
