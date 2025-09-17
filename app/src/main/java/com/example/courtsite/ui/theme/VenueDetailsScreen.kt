package com.example.courtsite.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.courtsite.data.model.VenueDetail
import com.example.courtsite.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VenueDetailsScreen(navController: NavController? = null, venueName: String) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = venueName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.view),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val detail = VenueDetail(
                name = venueName,
                location = "",
                sportType = "",
                imageRes = android.R.mipmap.sym_def_app_icon
            )
            VenueDetailContent(detail = detail)
        }
    }
}
