package com.example.week2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.week2.ui.theme.Week2_assignmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Week2_assignmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DevClusterScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DevClusterPreview() {
    Week2_assignmentTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            DevClusterScreen(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}


@androidx.compose.runtime.Immutable
data class HardwareItem(
    val id: String,
    val name: String,
    val category: String,
    val cost: Double,
    val stockCount: Int
)

private val initialHardwareItems = listOf(
    HardwareItem(
        id = "101",
        name = "MacBook Pro M3 Max",
        category = "Geliştirici Bilgisayarı",
        cost = 3499.0,
        stockCount = 5
    ),
    HardwareItem(
        id = "102",
        name = "NVIDIA RTX 4090 Sunucu",
        category = "Yapay Zeka / GPU",
        cost = 1599.0,
        stockCount = 2
    ),
    HardwareItem(
        id = "103",
        name = "Google Pixel 8 Pro",
        category = "Mobil Test Cihazı",
        cost = 999.0,
        stockCount = 8
    ),
    HardwareItem(
        id = "104",
        name = "Dell UltraSharp 49\"",
        category = "Monitör",
        cost = 1199.0,
        stockCount = 4
    ),
    HardwareItem(
        id = "105",
        name = "Sony WH-1000XM5 Kulaklık",
        category = "Odaklanma Ekipmanı",
        cost = 399.0,
        stockCount = 12
    ),
    HardwareItem(
        id = "106",
        name = "Raspberry Pi 5 8GB",
        category = "IoT Geliştirme Kiti",
        cost = 85.0,
        stockCount = 25
    ),
    HardwareItem(
        id = "107",
        name = "HHKB Professional Keyboard",
        category = "Ergonomik Klavye",
        cost = 299.0,
        stockCount = 0
    ),
    HardwareItem(
        id = "108",
        name = "AMD Ryzen Threadripper",
        category = "Yapay Zeka / GPU",
        cost = 4999.0,
        stockCount = 1
    )
)

@Composable
fun DevClusterScreen(modifier: Modifier = Modifier) {
    var stockCounts by rememberSaveable {
        mutableStateOf(
            initialHardwareItems.map { it.stockCount }.toIntArray()
        )
    }

    val hardwareItems = remember(stockCounts) {
        initialHardwareItems.mapIndexed { index, hardware ->
            hardware.copy(stockCount = stockCounts[index])
        }
    }

    val totalSpent = remember(stockCounts) {
        initialHardwareItems.mapIndexed { index, hardware ->
            val allocatedCount = hardware.stockCount - stockCounts[index]
            allocatedCount * hardware.cost
        }.sum()
    }
    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }
    var selectedCategory by rememberSaveable {
        mutableStateOf("Tümü")
    }

    val categories = remember {
        listOf("Tümü") + initialHardwareItems
            .map { it.category }
            .distinct()
    }

    val filteredItems = remember(
        searchQuery,
        selectedCategory,
        hardwareItems
    ) {
        val query = searchQuery.trim()

        hardwareItems.filter { hardware ->
            val matchesSearch =
                hardware.name.contains(query, ignoreCase = true) ||
                        hardware.category.contains(query, ignoreCase = true)

            val matchesCategory =
                selectedCategory == "Tümü" ||
                        hardware.category == selectedCategory

            matchesSearch && matchesCategory
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "DevCluster",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Toplam harcama: ${
                String.format(
                    java.util.Locale.forLanguageTag("tr-TR"),
                    "%,.2f",
                    totalSpent
                )
            }",
            style = MaterialTheme.typography.titleMedium
        )

        HardwareSearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it }
        )
        HardwareCategoryFilter(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it }
        )

        Text(
            text = "${filteredItems.size} donanım bulundu",
            style = MaterialTheme.typography.bodyMedium
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (filteredItems.isEmpty()) {
                item {
                    Text("Aramanızla eşleşen donanım bulunamadı.")
                }
            }

            items(
                items = filteredItems,
                key = { it.id }
            ) { hardware ->
                HardwareCard(
                    item = hardware,
                    onAllocate = {
                        val index = initialHardwareItems.indexOfFirst {
                            it.id == hardware.id
                        }

                        if (index >= 0 && stockCounts[index] > 0) {
                            val updatedCounts = stockCounts.copyOf()
                            updatedCounts[index] -= 1
                            stockCounts = updatedCounts
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun HardwareCard(
    item: HardwareItem,
    onAllocate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = item.stockCount > 0,
                    onClickLabel = "Bir adet kaynak ayır",
                    onClick = onAllocate
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = item.category,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Birim fiyat: ${
                    String.format(
                        java.util.Locale.forLanguageTag("tr-TR"),
                        "%,.2f",
                        item.cost
                    )
                }",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = if (item.stockCount > 0) {
                    "Stok: ${item.stockCount} adet"
                } else {
                    "Stok tükendi"
                },
                color = if (item.stockCount > 0) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.error
                }
            )

            if (item.stockCount > 0) {
                Text(
                    text = "Bir adet kaynak ayırmak için dokun",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun HardwareSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = modifier.fillMaxWidth(),
        label = {
            Text("Donanım veya kategori ara")
        },
        singleLine = true
    )
}

@Composable
fun HardwareCategoryFilter(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = categories,
            key = { it }
        ) { category ->
            FilterChip(
                selected = category == selectedCategory,
                onClick = {
                    onCategorySelected(category)
                },
                label = {
                    Text(category)
                }
            )
        }
    }
}