package com.example.courtsite.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.courtsite.R

@Composable
fun ExploreScreen(navController: NavHostController) {
    var selectedCategory by remember { mutableStateOf("All") }
    var searchText by remember { mutableStateOf("") }

    val categories = listOf(
        "All", "Racquet", "Team", "Water", "Recreation",
        "Fitness", "Event Spaces", "Stay", "Classes", "Other"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 标题
        Text(
            text = "Explore",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
        )

        // 搜索栏（可输入并过滤）
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
            },
            placeholder = { Text("Search sports", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(55.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 分类筛选 - 紫色样式与 BookScreen 一致
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(categories.size) { idx ->
                val category = categories[idx]
                val isSelected = category == selectedCategory
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) Color(0xFF4E28CC) else Color.White)
                        .border(1.dp, Color(0xFF4E28CC), RoundedCornerShape(20.dp))
                        .clickable { selectedCategory = category }
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) Color.White else Color(0xFF4E28CC),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 内容区域：支持搜索并点击跳转 BookScreen
        val navigateToBook: (String) -> Unit = { sportName ->
            navController.navigate("book/${sportName}")
        }

        if (searchText.isNotBlank()) {
            val allSports = listOf(
                Sport("Pickleball", R.drawable.pickleball),
                Sport("Badminton", R.drawable.badminton),
                Sport("Padel", R.drawable.padel),
                Sport("Squash", R.drawable.squash),
                Sport("Tennis", R.drawable.tennis),
                Sport("Table Tennis", R.drawable.table_tennis),
                Sport("Futsal", R.drawable.futsal),
                Sport("Football", R.drawable.football),
                Sport("Volleyball", R.drawable.volleyball),
                Sport("3x3 Basketball", R.drawable.basketball_3x3),
                Sport("Field Hockey", R.drawable.field_hockey),
                Sport("Basketball", R.drawable.basketball),
                Sport("Dodgeball", R.drawable.dodgeball),
                Sport("Lawn Bowl", R.drawable.lawn_bowl),
                Sport("Frisbee", R.drawable.frisbee),
                Sport("Indoor Hockey", R.drawable.indoor_hockey),
                Sport("Captain Ball", R.drawable.captain_ball),
                Sport("Sepak Takraw", R.drawable.sepak_takraw),
                Sport("Handball", R.drawable.handball),
                Sport("Teqball", R.drawable.teqball),
                Sport("Flag Football", R.drawable.flag_football),
                Sport("Rugby", R.drawable.rugby),
                Sport("Free Diving", R.drawable.free_diving),
                Sport("Mermaiding", R.drawable.mermaiding),
                Sport("Scuba Diving", R.drawable.scuba_diving),
                Sport("Swimming", R.drawable.swimming),
                Sport("Bowling", R.drawable.bowling),
                Sport("Bumper Car", R.drawable.bumper_car),
                Sport("Foosball", R.drawable.foosball),
                Sport("Golf Driving Range", R.drawable.golf_driving_range),
                Sport("Go-Kart", R.drawable.go_kart),
                Sport("Martial Arts", R.drawable.martial_arts),
                Sport("Pool Table", R.drawable.pool_table),
                Sport("Rollerblading", R.drawable.rollerblading),
                Sport("Dance Studio", R.drawable.dance_studio),
                Sport("Fitness Space", R.drawable.fitness_space),
                Sport("Gym", R.drawable.gym),
                Sport("Gymnastic", R.drawable.gymnastic),
                Sport("Running Track", R.drawable.running_track),
                Sport("Wall Climbing", R.drawable.wall_climbing),
                Sport("Event Space", R.drawable.event_space),
                Sport("Sporty", R.drawable.sporty_celebration),
                Sport("Event Room", R.drawable.event_room),
                Sport("Chalet", R.drawable.chalet),
                Sport("Boxing", R.drawable.boxing),
                Sport("Brazilian Ju-Jitsu", R.drawable.brazilian_jiu_jitsu),
                Sport("Capoeira", R.drawable.capoeira),
                Sport("Fitness", R.drawable.fitness),
                Sport("Fighter's Strength And Conditioning", R.drawable.fighters_strength_and_conditioning),
                Sport("Grappling", R.drawable.grappling),
                Sport("Kickboxing", R.drawable.kickboxing),
                Sport("MMA", R.drawable.mma),
                Sport("Muay Thai", R.drawable.muay_thai),
                Sport("Muay Thai Fitness", R.drawable.muay_thai_fitness),
                Sport("Taekwondo", R.drawable.taekwondo),
                Sport("Light Volleyball", R.drawable.light_volleyball)
            )

            val filtered = remember(searchText) { allSports.filter { it.name.contains(searchText, ignoreCase = true) } }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filtered) { sport ->
                    SportGridItem(sport = sport) { selected ->
                        navigateToBook(selected.name)
                    }
                }
            }
        } else {
            when (selectedCategory) {
                "Racquet" -> RacquetSportsSection(onSelect = navigateToBook)
                "Team" -> TeamSportsSection(onSelect = navigateToBook)
                "Water" -> WaterSportsSection(onSelect = navigateToBook)
                "Recreation" -> RecreationalSportsSection(onSelect = navigateToBook)
                "Fitness" -> FitnessSection(onSelect = navigateToBook)
                "Event Spaces" -> EventSpacesSection(onSelect = navigateToBook)
                "Stay" -> StaySection(onSelect = navigateToBook)
                "Classes" -> ClassesSection(onSelect = navigateToBook)
                "Other" -> OtherSection(onSelect = navigateToBook)
                else -> AllSportsSection(onSelect = navigateToBook)
            }
        }
    }
}

// 修改通用网格布局函数为3列
@Composable
fun SportsGrid(sports: List<Sport>, onSelect: (Sport) -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 将运动项目分成每行三个
        sports.chunked(3).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 第一项
                SportGridItem(
                    sport = rowItems[0],
                    modifier = Modifier.weight(1f)
                ) { onSelect(rowItems[0]) }

                // 第二项
                if (rowItems.size > 1) {
                    SportGridItem(
                        sport = rowItems[1],
                        modifier = Modifier.weight(1f)
                    ) { onSelect(rowItems[1]) }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                // 第三项
                if (rowItems.size > 2) {
                    SportGridItem(
                        sport = rowItems[2],
                        modifier = Modifier.weight(1f)
                    ) { onSelect(rowItems[2]) }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// 修改运动项目网格项 - 图片和按钮范围一样大
@Composable
fun SportGridItem(sport: Sport, modifier: Modifier = Modifier, onClick: (Sport) -> Unit = {}) {
    Surface(
        modifier = modifier
            .aspectRatio(1f),
        shape = RoundedCornerShape(12.dp),
        color = Color.Transparent,
        onClick = { onClick(sport) }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = sport.iconResId),
                contentDescription = sport.name,
                modifier = Modifier
                    .fillMaxWidth() // 图片和按钮范围一样大
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop // 使用Crop确保图片填满整个区域
            )
        }
    }
}

// 各个section保持不变...
@Composable
fun RacquetSportsSection(onSelect: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Racquet Sports",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )

        val sports = listOf(
            Sport("Pickleball", R.drawable.pickleball),
            Sport("Badminton", R.drawable.badminton),
            Sport("Padel", R.drawable.padel),
            Sport("Squash", R.drawable.squash),
            Sport("Tennis", R.drawable.tennis),
            Sport("Table Tennis", R.drawable.table_tennis)
        )

        SportsGrid(sports = sports) { onSelect(it.name) }
    }
}

// Team Sports Section
@Composable
fun TeamSportsSection(onSelect: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Team Sports",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )

        val teamSports = listOf(
            Sport("Futsal", R.drawable.futsal),
            Sport("Football", R.drawable.football),
            Sport("Volleyball", R.drawable.volleyball),
            Sport("3x3 Basketball", R.drawable.basketball_3x3),
            Sport("Field Hockey", R.drawable.field_hockey),
            Sport("Basketball", R.drawable.basketball),
            Sport("Dodgeball", R.drawable.dodgeball),
            Sport("Lawn Bowl", R.drawable.lawn_bowl),
            Sport("Frisbee", R.drawable.frisbee),
            Sport("Indoor Hockey", R.drawable.indoor_hockey),
            Sport("Captain Ball", R.drawable.captain_ball),
            Sport("Sepak Takraw", R.drawable.sepak_takraw),
            Sport("Handball", R.drawable.handball),
            Sport("Teqball", R.drawable.teqball),
            Sport("Flag Football", R.drawable.flag_football),
            Sport("Rugby", R.drawable.rugby)
        )

        SportsGrid(sports = teamSports) { onSelect(it.name) }
    }
}

// Water Sports Section
@Composable
fun WaterSportsSection(onSelect: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Water Sports",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )

        val waterSports = listOf(
            Sport("Free Diving", R.drawable.free_diving),
            Sport("Mermaiding", R.drawable.mermaiding),
            Sport("Scuba Diving", R.drawable.scuba_diving),
            Sport("Swimming", R.drawable.swimming)
        )

        SportsGrid(sports = waterSports) { onSelect(it.name) }
    }
}

// Recreational Sports Section
@Composable
fun RecreationalSportsSection(onSelect: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Recreational Sports",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )

        val recreationalSports = listOf(
            Sport("Bowling", R.drawable.bowling),
            Sport("Bumper Car", R.drawable.bumper_car),
            Sport("Foosball", R.drawable.foosball),
            Sport("Golf Driving Range", R.drawable.golf_driving_range),
            Sport("Go-Kart", R.drawable.go_kart),
            Sport("Martial Arts", R.drawable.martial_arts),
            Sport("Pool Table", R.drawable.pool_table),
            Sport("Rollerblading", R.drawable.rollerblading)
        )

        SportsGrid(sports = recreationalSports) { onSelect(it.name) }
    }
}

// Fitness Section
@Composable
fun FitnessSection(onSelect: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Fitness",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )

        val fitnessItems = listOf(
            Sport("Dane Studio", R.drawable.dance_studio),
            Sport("Fitness Space", R.drawable.fitness_space),
            Sport("Gym", R.drawable.gym),
            Sport("Gymnastic", R.drawable.gymnastic),
            Sport("Running Track", R.drawable.running_track),
            Sport("Wall Climbing", R.drawable.wall_climbing)
        )

        SportsGrid(sports = fitnessItems) { onSelect(it.name) }
    }
}

// Event Spaces Section
@Composable
fun EventSpacesSection(onSelect: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Event Spaces",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )

        val eventSpaces = listOf(
            Sport("Dance Studio", R.drawable.dance_studio),
            Sport("Event Space", R.drawable.event_space),
            Sport("Fitness Space", R.drawable.fitness_space),
            Sport("Sporty", R.drawable.sporty_celebration),
            Sport("Event Room", R.drawable.event_room),
        )

        SportsGrid(sports = eventSpaces) { onSelect(it.name) }
    }
}

// Stay Section
@Composable
fun StaySection(onSelect: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Place to Stay",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )

        val stayItems = listOf(
            Sport("Chalet", R.drawable.chalet),
        )

        SportsGrid(sports = stayItems) { onSelect(it.name) }
    }
}

// Classes Section
@Composable
fun ClassesSection(onSelect: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Classes",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )

        val classes = listOf(
            Sport("Boxing", R.drawable.boxing),
            Sport("Brazilian Ju-Jitsu", R.drawable.brazilian_jiu_jitsu),
            Sport("Capoeira", R.drawable.capoeira),
            Sport("Fitness", R.drawable.fitness),
            Sport("Fighter's Strength And Conditioning", R.drawable.fighters_strength_and_conditioning),
            Sport("Grappling", R.drawable.grappling),
            Sport("Kickboxing", R.drawable.kickboxing),
            Sport("MMA", R.drawable.mma),
            Sport("Muay Thai", R.drawable.muay_thai),
            Sport("Muay Thai Fitness", R.drawable.muay_thai_fitness),
            Sport("Taekwondo", R.drawable.taekwondo)
        )

        SportsGrid(sports = classes) { onSelect(it.name) }
    }
}

// Other Section
@Composable
fun OtherSection(onSelect: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Other",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )

        val otherItems = listOf(
            Sport("Light Volleyball", R.drawable.light_volleyball)
        )

        SportsGrid(sports = otherItems) { onSelect(it.name) }
    }
}

// All Sports Section - 完整实现
@Composable
fun AllSportsSection(onSelect: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        // Racquet Sports
        Text(
            text = "Racquet Sports",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )
        SportsGrid(sports = listOf(
            Sport("Pickleball", R.drawable.pickleball),
            Sport("Badminton", R.drawable.badminton),
            Sport("Padel", R.drawable.padel),
            Sport("Squash", R.drawable.squash),
            Sport("Tennis", R.drawable.tennis),
            Sport("Table Tennis", R.drawable.table_tennis)
        )) { onSelect(it.name) }

        Spacer(modifier = Modifier.height(24.dp))

        // Team Sports
        Text(
            text = "Team Sports",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        SportsGrid(sports = listOf(
            Sport("Futsal", R.drawable.futsal),
            Sport("Football", R.drawable.football),
            Sport("Volleyball", R.drawable.volleyball),
            Sport("3x3 Basketball", R.drawable.basketball_3x3),
            Sport("Field Hockey", R.drawable.field_hockey),
            Sport("Basketball", R.drawable.basketball),
            Sport("Dodgeball", R.drawable.dodgeball),
            Sport("Lawn Bowl", R.drawable.lawn_bowl),
            Sport("Frisbee", R.drawable.frisbee),
            Sport("Indoor Hockey", R.drawable.indoor_hockey),
            Sport("Captain Ball", R.drawable.captain_ball),
            Sport("Sepak Takraw", R.drawable.sepak_takraw),
            Sport("Handball", R.drawable.handball),
            Sport("Teqball", R.drawable.teqball),
            Sport("Flag Football", R.drawable.flag_football),
            Sport("Rugby", R.drawable.rugby)
        )) { onSelect(it.name) }

        Spacer(modifier = Modifier.height(24.dp))

        // Water Sports
        Text(
            text = "Water Sports",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        SportsGrid(sports = listOf(
            Sport("Free Diving", R.drawable.free_diving),
            Sport("Mermaiding", R.drawable.mermaiding),
            Sport("Scuba Diving", R.drawable.scuba_diving),
            Sport("Swimming", R.drawable.swimming)
        )) { onSelect(it.name) }

        Spacer(modifier = Modifier.height(24.dp))

        // Recreational Sports
        Text(
            text = "Recreational Sports",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        SportsGrid(sports = listOf(
            Sport("Bowling", R.drawable.bowling),
            Sport("Bumper Car", R.drawable.bumper_car),
            Sport("Foosball", R.drawable.foosball),
            Sport("Golf Driving Range", R.drawable.golf_driving_range),
            Sport("Go-Kart", R.drawable.go_kart),
            Sport("Martial Arts", R.drawable.martial_arts),
            Sport("Pool Table", R.drawable.pool_table),
            Sport("Rollerblading", R.drawable.rollerblading)
        )) { onSelect(it.name) }

        Spacer(modifier = Modifier.height(24.dp))

        // Fitness
        Text(
            text = "Fitness",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        SportsGrid(sports = listOf(
            Sport("Dance Studio", R.drawable.dance_studio),
            Sport("Fitness Space", R.drawable.fitness_space),
            Sport("Gym", R.drawable.gym),
            Sport("Gymnastic", R.drawable.gymnastic),
            Sport("Running Track", R.drawable.running_track),
            Sport("Wall Climbing", R.drawable.wall_climbing)
        )) { onSelect(it.name) }

        Spacer(modifier = Modifier.height(24.dp))

        // Event Spaces
        Text(
            text = "Event Spaces",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        SportsGrid(sports = listOf(
            Sport("Dance Studio", R.drawable.dance_studio),
            Sport("Event Space", R.drawable.event_space),
            Sport("Fitness Space", R.drawable.fitness_space),
            Sport("Sporty", R.drawable.sporty_celebration),
            Sport("Event Room", R.drawable.event_room)
        )) { onSelect(it.name) }

        Spacer(modifier = Modifier.height(24.dp))

        // Stay
        Text(
            text = "Place to Stay",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        SportsGrid(sports = listOf(
            Sport("Chalet", R.drawable.chalet)
        )) { onSelect(it.name) }

        Spacer(modifier = Modifier.height(24.dp))

        // Classes
        Text(
            text = "Classes",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        SportsGrid(sports = listOf(
            Sport("Boxing", R.drawable.boxing),
            Sport("Brazilian Ju-Jitsu", R.drawable.brazilian_jiu_jitsu),
            Sport("Capoeira", R.drawable.capoeira),
            Sport("Fitness", R.drawable.fitness),
            Sport("Fighter's Strength And Conditioning", R.drawable.fighters_strength_and_conditioning),
            Sport("Grappling", R.drawable.grappling),
            Sport("Kickboxing", R.drawable.kickboxing),
            Sport("MMA", R.drawable.mma),
            Sport("Muay Thai", R.drawable.muay_thai),
            Sport("Muay Thai Fitness", R.drawable.muay_thai_fitness),
            Sport("Taekwondo", R.drawable.taekwondo)
        )) { onSelect(it.name) }

        Spacer(modifier = Modifier.height(24.dp))

        // Other
        Text(
            text = "Other",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        SportsGrid(sports = listOf(
            Sport("Light Volleyball", R.drawable.light_volleyball)
        )) { onSelect(it.name) }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// 数据类保持不变
data class Sport(
    val name: String,
    val iconResId: Int
)