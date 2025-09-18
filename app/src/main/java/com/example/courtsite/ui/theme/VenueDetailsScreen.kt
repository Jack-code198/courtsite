package com.example.courtsite.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.courtsite.R
import com.example.courtsite.data.model.Venue
import com.example.courtsite.data.model.venuesByLocation
import java.net.URLEncoder
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.draw.rotate
import java.net.URLDecoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VenueDetailsScreen(navController: NavController? = null, venueName: String) {
	// Decode venue name from route param if encoded
	val decodedName = try { URLDecoder.decode(venueName, "UTF-8") } catch (e: Exception) { venueName }
	// Find venue by name across locations
	val currentVenue: Venue? = remember(decodedName) {
		venuesByLocation.values.flatten().firstOrNull { it.name.equals(decodedName, ignoreCase = true) }
	}

	// Find nearby venues in the same area
	val nearbyVenues: List<Venue> = remember(currentVenue) {
		if (currentVenue == null) {
			emptyList()
		} else {
			// Extract area name from location (e.g., "Ara Damansara" from "Ara Damansara, Selangor")
			val currentArea = currentVenue.location.split(",").first().trim()

			// Find all venues that are in the same area but exclude the current venue
			venuesByLocation.values.flatten().filter { venue ->
				val venueArea = venue.location.split(",").first().trim()
				venueArea.equals(currentArea, ignoreCase = true) && venue.name != currentVenue.name
			}
		}
	}

	// State for venue layout sport selection
	val selectedSportLayout = remember { mutableStateOf("All") }

	// State for dialogs
	val showPricingDialog = remember { mutableStateOf(false) }
	val selectedSportForDialog = remember { mutableStateOf<String?>(null) }
	val showOpeningHoursDialog = remember { mutableStateOf(false) }
	val showVenueLayoutDialog = remember { mutableStateOf(false) }
	val showVenuePolicyDialog = remember { mutableStateOf(false) }
	val showContactDialog = remember { mutableStateOf(false) }
	// State for direction dialogs
	val showDirectionsDialog = remember { mutableStateOf(false) }
	val showGoogleMapsDialog = remember { mutableStateOf(false) }
	val showWazeDialog = remember { mutableStateOf(false) }

	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(
				title = {
					Column(horizontalAlignment = Alignment.CenterHorizontally) {
						Text(
							text = currentVenue?.name ?: decodedName,
							fontWeight = FontWeight.Bold,
							fontSize = 16.sp
						)
						if (!currentVenue?.location.isNullOrBlank()) {
							Text(
								text = currentVenue!!.location,
								fontSize = 12.sp,
								color = Color.Gray
							)
						}
					}
				},
				navigationIcon = {
					IconButton(onClick = { navController?.popBackStack() }) {
						Icon(
							imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
				.background(Color.White)
				.padding(innerPadding)
				.verticalScroll(rememberScrollState())
		) {
			// Venue Image
			currentVenue?.let { venue ->
				Image(
					painter = painterResource(id = venue.imageRes),
					contentDescription = venue.name,
					modifier = Modifier
						.fillMaxWidth()
						.height(200.dp),
					contentScale = ContentScale.Crop
				)
			}

			// Special Offer Banner (if available)
			currentVenue?.specialOffer?.let { offer ->
				if (offer.isNotBlank()) {
					Card(
						modifier = Modifier
							.padding(horizontal = 16.dp, vertical = 12.dp)
							.fillMaxWidth(),
						shape = RoundedCornerShape(12.dp),
						colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF2E7)),
						elevation = CardDefaults.cardElevation(2.dp)
					) {
						Column(modifier = Modifier.padding(16.dp)) {
							Text(
								text = offer,
								fontWeight = FontWeight.Bold,
								color = Color(0xFFE65100)
							)
						}
					}
				}
			}

			// Categories & Pricing
			Text(
				text = "Categories & Pricing",
				style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
				modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
			)

			// Sport categories cards
			currentVenue?.sportType?.split(",")?.forEach { sportType ->
				val sport = sportType.trim()
				val sportIconRes = when {
					sport.contains("PICKLEBALL", true) -> R.drawable.pickleball
					sport.contains("BADMINTON", true) -> R.drawable.badminton
					sport.contains("TENNIS", true) -> R.drawable.tennis
					sport.contains("BASKETBALL", true) -> R.drawable.basketball
					sport.contains("FUTSAL", true) -> R.drawable.futsal
					sport.contains("TABLE", true) -> R.drawable.table_tennis
					sport.contains("SQUASH", true) -> R.drawable.squash
					sport.contains("PADEL", true) -> R.drawable.padel
					else -> R.drawable.sport
				}

				Card(
					modifier = Modifier
						.padding(horizontal = 16.dp, vertical = 6.dp)
						.fillMaxWidth()
						.clickable {
							selectedSportForDialog.value = sport
							showPricingDialog.value = true
						}
						.shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)),
					shape = RoundedCornerShape(12.dp),
					colors = CardDefaults.cardColors(containerColor = Color.White)
				) {
					Row(
						modifier = Modifier.padding(16.dp),
						verticalAlignment = Alignment.CenterVertically
					) {
						Image(
							painter = painterResource(id = sportIconRes),
							contentDescription = sport,
							modifier = Modifier
								.size(36.dp)
								.clip(RoundedCornerShape(8.dp)),
							contentScale = ContentScale.Fit
						)

						Spacer(modifier = Modifier.width(12.dp))

						Column(modifier = Modifier.weight(1f)) {
							Text(
								text = sport,
								fontWeight = FontWeight.Bold,
								fontSize = 16.sp
							)
							Text(
								text = "${currentVenue.courts} courts",
								color = Color.Gray,
								fontSize = 14.sp
							)
						}
					}
				}
			}

			// Pricing Dialog
			if (showPricingDialog.value && selectedSportForDialog.value != null) {
				PricingDialog(
					sport = selectedSportForDialog.value!!,
					onDismiss = { showPricingDialog.value = false }
				)
			}

			// Amenities (circular icons)
			if (!currentVenue?.amenities.isNullOrEmpty()) {
				Text(
					text = "Amenities",
					style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
					modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
				)
				LazyRow(
					modifier = Modifier.fillMaxWidth(),
					contentPadding = PaddingValues(horizontal = 16.dp),
					horizontalArrangement = Arrangement.spacedBy(16.dp)
				) {
					items(currentVenue!!.amenities) { amenity ->
						Column(horizontalAlignment = Alignment.CenterHorizontally) {
							Box(
								modifier = Modifier
									.size(44.dp)
									.clip(CircleShape)
									.background(Color(0xFFF2EEFF))
									.border(1.dp, Color(0xFF4E28CC), CircleShape),
								contentAlignment = Alignment.Center
							) {
								Text(text = amenity.first().uppercase(), color = Color(0xFF4E28CC), fontWeight = FontWeight.Bold)
							}
							Spacer(modifier = Modifier.height(6.dp))
							Text(text = amenity, fontSize = 12.sp, color = Color.Black)
						}
					}
				}
			}

			Spacer(modifier = Modifier.height(20.dp))

			// Venue Information
			Text(
				text = "Venue Information",
				style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
				modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
			)

			// Opening Hours & Pricing - Clickable Card
			Card(
				modifier = Modifier
					.padding(horizontal = 16.dp, vertical = 6.dp)
					.fillMaxWidth()
					.clickable { showOpeningHoursDialog.value = true }
					.shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)),
				shape = RoundedCornerShape(12.dp),
				colors = CardDefaults.cardColors(containerColor = Color.White)
			) {
				Row(
					modifier = Modifier.padding(16.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					Column(modifier = Modifier.weight(1f)) {
						Text(
							text = "Opening Hours & Pricing",
							fontWeight = FontWeight.Medium,
							fontSize = 16.sp
						)
						if (!currentVenue?.price.isNullOrBlank()) {
							Text(
								text = currentVenue!!.price,
								color = Color.Gray,
								fontSize = 14.sp
							)
						}
					}
				}
			}

			// Venue Layout - Clickable Card
			Card(
				modifier = Modifier
					.padding(horizontal = 16.dp, vertical = 6.dp)
					.fillMaxWidth()
					.clickable { showVenueLayoutDialog.value = true }
					.shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)),
				shape = RoundedCornerShape(12.dp),
				colors = CardDefaults.cardColors(containerColor = Color.White)
			) {
				Row(
					modifier = Modifier.padding(16.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					Column(modifier = Modifier.weight(1f)) {
						Text(
							text = "Venue Layout",
							fontWeight = FontWeight.Medium,
							fontSize = 16.sp
						)
					}
				}
			}

			// Venue Policy - Clickable Card
			Card(
				modifier = Modifier
					.padding(horizontal = 16.dp, vertical = 6.dp)
					.fillMaxWidth()
					.clickable { showVenuePolicyDialog.value = true }
					.shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)),
				shape = RoundedCornerShape(12.dp),
				colors = CardDefaults.cardColors(containerColor = Color.White)
			) {
				Row(
					modifier = Modifier.padding(16.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					Column(modifier = Modifier.weight(1f)) {
						Text(
							text = "Venue Policy",
							fontWeight = FontWeight.Medium,
							fontSize = 16.sp
						)
					}
				}
			}

			// Get Connected - Clickable Card
			Card(
				modifier = Modifier
					.padding(horizontal = 16.dp, vertical = 6.dp)
					.fillMaxWidth()
					.clickable { showContactDialog.value = true }
					.shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)),
				shape = RoundedCornerShape(12.dp),
				colors = CardDefaults.cardColors(containerColor = Color.White)
			) {
				Row(
					modifier = Modifier.padding(16.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					Column(modifier = Modifier.weight(1f)) {
						Text(
							text = "Get Connected",
							fontWeight = FontWeight.Medium,
							fontSize = 16.sp
						)
					}
				}
			}

			// Dialogs
			if (showOpeningHoursDialog.value) {
				OpeningHoursDialog(
					currentVenue = currentVenue,
					onDismiss = { showOpeningHoursDialog.value = false }
				)
			}

			if (showVenueLayoutDialog.value) {
				VenueLayoutDialog(
					currentVenue = currentVenue,
					selectedSportLayout = selectedSportLayout,
					onDismiss = { showVenueLayoutDialog.value = false }
				)
			}

			if (showVenuePolicyDialog.value) {
				VenuePolicyDialog(
					onDismiss = { showVenuePolicyDialog.value = false }
				)
			}

			if (showContactDialog.value) {
				ContactDialog(
					onDismiss = { showContactDialog.value = false }
				)
			}

			// Getting There
			Text(
				text = "Getting There",
				style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
				modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
			)
			if (!currentVenue?.address.isNullOrBlank()) {
				Card(
					modifier = Modifier
						.padding(horizontal = 16.dp)
						.fillMaxWidth(),
					shape = RoundedCornerShape(12.dp),
					elevation = CardDefaults.cardElevation(1.dp),
					colors = CardDefaults.cardColors(containerColor = Color.White)
				) {
					Column(modifier = Modifier.padding(16.dp)) {
						Text(text = currentVenue!!.name, fontWeight = FontWeight.Bold)
						Spacer(modifier = Modifier.height(4.dp))
						Text(text = currentVenue.address, color = Color.Gray, fontSize = 12.sp)
					}
				}
			}

// Directions Cards (replacing the ListItems)
			Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
				// Directions Card
				Card(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 6.dp)
						.clickable { showDirectionsDialog.value = true }
						.shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)),
					shape = RoundedCornerShape(12.dp),
					colors = CardDefaults.cardColors(containerColor = Color.White)
				) {
					Row(
						modifier = Modifier.padding(16.dp),
						verticalAlignment = Alignment.CenterVertically
					) {
						Column(modifier = Modifier.weight(1f)) {
							Text(
								text = "Directions",
								fontWeight = FontWeight.Medium,
								fontSize = 16.sp
							)
						}
					}
				}

				// Google Maps Card
				Card(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 6.dp)
						.clickable { showGoogleMapsDialog.value = true }
						.shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)),
					shape = RoundedCornerShape(12.dp),
					colors = CardDefaults.cardColors(containerColor = Color.White)
				) {
					Row(
						modifier = Modifier.padding(16.dp),
						verticalAlignment = Alignment.CenterVertically
					) {
						Column(modifier = Modifier.weight(1f)) {
							Text(
								text = "Open In Google Maps",
								fontWeight = FontWeight.Medium,
								fontSize = 16.sp
							)
						}
					}
				}

				// Waze Card
				Card(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 6.dp)
						.clickable { showWazeDialog.value = true }
						.shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)),
					shape = RoundedCornerShape(12.dp),
					colors = CardDefaults.cardColors(containerColor = Color.White)
				) {
					Row(
						modifier = Modifier.padding(16.dp),
						verticalAlignment = Alignment.CenterVertically
					) {
						Column(modifier = Modifier.weight(1f)) {
							Text(
								text = "Open In Waze",
								fontWeight = FontWeight.Medium,
								fontSize = 16.sp
							)
						}
						// Direction Dialogs
						if (showDirectionsDialog.value) {
							GenericDialog(
								title = "Directions",
								content = { Text("Directions information would go here") },
								onDismiss = { showDirectionsDialog.value = false }
							)
						}

						if (showGoogleMapsDialog.value) {
							GenericDialog(
								title = "Google Maps",
								content = { Text("Opening in Google Maps...") },
								onDismiss = { showGoogleMapsDialog.value = false }
							)
						}

						if (showWazeDialog.value) {
							GenericDialog(
								title = "Waze",
								content = { Text("Opening in Waze...") },
								onDismiss = { showWazeDialog.value = false }
							)
						}

					}
				}
			}

			// Actions
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp, vertical = 20.dp),
				horizontalArrangement = Arrangement.spacedBy(12.dp)
			) {
				// Replace the current OutlinedButton (Availability button)
				OutlinedButton(
					onClick = {
						val venueName = currentVenue?.name ?: ""
						val sport = currentVenue?.sportType?.split(",")?.firstOrNull()?.trim() ?: ""
						val encodedName = URLEncoder.encode(venueName, "UTF-8")
						val encodedSport = URLEncoder.encode(sport, "UTF-8")
						navController?.navigate("availability/$encodedName/$encodedSport")
					},
					modifier = Modifier.weight(1f),
					colors = ButtonDefaults.outlinedButtonColors(
						containerColor = Color.White,
						contentColor = Color(0xFF4E28CC)
					),
					border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4E28CC))
				) {
					Row(
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.Center
					) {
						Image(
							painter = painterResource(id = R.drawable.upcomingon),
							contentDescription = "Availability",
							modifier = Modifier
								.size(30.dp)
								.padding(end = 8.dp)
						)
						Text("Availability")
					}
				}

// Replace the current Button (Book Now button)
				Button(
					onClick = {
						val venueName = currentVenue?.name ?: ""
						val sport = currentVenue?.sportType?.split(",")?.firstOrNull()?.trim() ?: ""
						val encodedName = URLEncoder.encode(venueName, "UTF-8")
						val encodedSport = URLEncoder.encode(sport, "UTF-8")
						navController?.navigate("booknow/$encodedName/$encodedSport")
					},
					modifier = Modifier.weight(1f),
					colors = ButtonDefaults.buttonColors(
						containerColor = Color(0xFF4E28CC),
						contentColor = Color.White
					)
				) {Image(
					painter = painterResource(id = R.drawable.booknow),
					contentDescription = "Book Now",
					modifier = Modifier
						.size(30.dp)
						.padding(end = 8.dp)
				)
					Text("Book Now")
				}
			}

			// Venues Nearby
			if (nearbyVenues.isNotEmpty()) {
				Text(
					text = "Venues Nearby",
					style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
					modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
				)
				LazyRow(
					contentPadding = PaddingValues(horizontal = 16.dp),
					horizontalArrangement = Arrangement.spacedBy(12.dp)
				) {
					items(nearbyVenues) { venue ->
						Card(
							modifier = Modifier
								.width(220.dp)
								.height(280.dp)
								.clickable {
									val encodedName = URLEncoder.encode(venue.name, "UTF-8")
									navController?.navigate("venueDetails/$encodedName")
								},
							shape = RoundedCornerShape(12.dp),
							elevation = CardDefaults.cardElevation(2.dp),
							colors = CardDefaults.cardColors(containerColor = Color.White)
						) {
							Column(modifier = Modifier.fillMaxSize()) {
								Image(
									painter = painterResource(id = venue.imageRes),
									contentDescription = venue.name,
									modifier = Modifier
										.fillMaxWidth()
										.height(150.dp),
									contentScale = ContentScale.Crop
								)
								Column(
									modifier = Modifier
										.fillMaxWidth()
										.padding(12.dp)
										.weight(1f)
								) {
									Text(
										text = venue.name,
										fontWeight = FontWeight.Bold,
										maxLines = 2
									)
									Spacer(modifier = Modifier.height(4.dp))
									Text(
										text = venue.location,
										color = Color.Gray,
										fontSize = 12.sp,
										maxLines = 2
									)
									Spacer(modifier = Modifier.height(4.dp))
									Text(
										text = venue.sportType,
										color = Color(0xFF4E28CC),
										fontSize = 12.sp,
										maxLines = 1
									)
								}
							}
						}
					}
				}
			}

			Spacer(modifier = Modifier.height(24.dp))
		}
	}
}

@Composable
fun GenericDialog(
	title: String,
	content: @Composable () -> Unit,
	onDismiss: () -> Unit
) {
	Dialog(
		onDismissRequest = onDismiss,
		properties = DialogProperties(usePlatformDefaultWidth = false)
	) {
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp)
				.shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp)),
			shape = RoundedCornerShape(12.dp),
			colors = CardDefaults.cardColors(containerColor = Color.White)
		) {
			Column(
				modifier = Modifier.padding(24.dp)
			) {
				Text(
					text = title,
					style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
					modifier = Modifier.padding(bottom = 16.dp)
				)

				content()

				Spacer(modifier = Modifier.height(16.dp))

				Button(
					onClick = onDismiss,
					modifier = Modifier
						.fillMaxWidth()
						.height(48.dp),
					colors = ButtonDefaults.buttonColors(
						containerColor = Color(0xFF4E28CC),
						contentColor = Color.White
					),
					shape = RoundedCornerShape(8.dp)
				) {
					Text("Close", fontSize = 16.sp)
				}
			}
		}
	}
}

@Composable
fun OpeningHoursDialog(currentVenue: Venue?, onDismiss: () -> Unit) {
	Dialog(
		onDismissRequest = onDismiss,
		properties = DialogProperties(usePlatformDefaultWidth = false)
	) {
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.heightIn(max = 700.dp)
				.padding(16.dp)
				.shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp)),
			shape = RoundedCornerShape(12.dp),
			colors = CardDefaults.cardColors(containerColor = Color.White)
		) {
			Column(
				modifier = Modifier
					.verticalScroll(rememberScrollState())
			) {
				// Close button at the top right
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp),
					horizontalArrangement = Arrangement.End
				) {
					IconButton(
						onClick = onDismiss,
						modifier = Modifier.size(24.dp)
					) {
						Icon(
							imageVector = Icons.Default.Close,
							contentDescription = "Close"
						)
					}
				}

				Column(
					modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
				) {
					Text(
						text = "Opening Hours & Pricing",
						style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
						modifier = Modifier.padding(bottom = 16.dp)
					)

					GeneralPricingInformation(currentVenue)

					Spacer(modifier = Modifier.height(16.dp))

					Button(
						onClick = onDismiss,
						modifier = Modifier
							.fillMaxWidth()
							.height(48.dp),
						colors = ButtonDefaults.buttonColors(
							containerColor = Color(0xFF4E28CC),
							contentColor = Color.White
						),
						shape = RoundedCornerShape(8.dp)
					) {
						Text("Close", fontSize = 16.sp)
					}
				}
			}
		}
	}
}


@Composable
fun VenueLayoutDialog(currentVenue: Venue?, selectedSportLayout: androidx.compose.runtime.MutableState<String>, onDismiss: () -> Unit) {
	Dialog(
		onDismissRequest = onDismiss,
		properties = DialogProperties(usePlatformDefaultWidth = false)
	) {
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.heightIn(max = 700.dp)
				.padding(16.dp)
				.shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp)),
			shape = RoundedCornerShape(12.dp),
			colors = CardDefaults.cardColors(containerColor = Color.White)
		) {
			Column(
				modifier = Modifier
					.verticalScroll(rememberScrollState())
			) {
				// Close button at the top right
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp),
					horizontalArrangement = Arrangement.End
				) {
					IconButton(
						onClick = onDismiss,
						modifier = Modifier.size(24.dp)
					) {
						Icon(
							imageVector = Icons.Default.Close,
							contentDescription = "Close"
						)
					}
				}

				Column(
					modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
				) {
					Text(
						text = "Venue Layout",
						style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
						modifier = Modifier.padding(bottom = 16.dp)
					)

					VenueLayoutInformation(currentVenue, selectedSportLayout)

					Spacer(modifier = Modifier.height(16.dp))

					Button(
						onClick = onDismiss,
						modifier = Modifier
							.fillMaxWidth()
							.height(48.dp),
						colors = ButtonDefaults.buttonColors(
							containerColor = Color(0xFF4E28CC),
							contentColor = Color.White
						),
						shape = RoundedCornerShape(8.dp)
					) {
						Text("Close", fontSize = 16.sp)
					}
				}
			}
		}
	}
}

@Composable
fun VenuePolicyDialog(onDismiss: () -> Unit) {
	Dialog(
		onDismissRequest = onDismiss,
		properties = DialogProperties(usePlatformDefaultWidth = false)
	) {
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.heightIn(max = 700.dp)
				.padding(16.dp)
				.shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp)),
			shape = RoundedCornerShape(12.dp),
			colors = CardDefaults.cardColors(containerColor = Color.White)
		) {
			Column(
				modifier = Modifier
					.verticalScroll(rememberScrollState())
			) {
				// Close button at the top right
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp),
					horizontalArrangement = Arrangement.End
				) {
					IconButton(
						onClick = onDismiss,
						modifier = Modifier.size(24.dp)
					) {
						Icon(
							imageVector = Icons.Default.Close,
							contentDescription = "Close"
						)
					}
				}

				Column(
					modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
				) {
					Text(
						text = "Venue Policy",
						style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
						modifier = Modifier.padding(bottom = 16.dp)
					)

					VenuePolicyInformation()

					Spacer(modifier = Modifier.height(16.dp))

					Button(
						onClick = onDismiss,
						modifier = Modifier
							.fillMaxWidth()
							.height(48.dp),
						colors = ButtonDefaults.buttonColors(
							containerColor = Color(0xFF4E28CC),
							contentColor = Color.White
						),
						shape = RoundedCornerShape(8.dp)
					) {
						Text("Close", fontSize = 16.sp)
					}
				}
			}
		}
	}
}


@Composable
fun ContactDialog(onDismiss: () -> Unit) {
	Dialog(
		onDismissRequest = onDismiss,
		properties = DialogProperties(usePlatformDefaultWidth = false)
	) {
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.heightIn(max = 700.dp)
				.padding(16.dp)
				.shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp)),
			shape = RoundedCornerShape(12.dp),
			colors = CardDefaults.cardColors(containerColor = Color.White)
		) {
			Column(
				modifier = Modifier
					.verticalScroll(rememberScrollState())
			) {
				// Close button at the top right
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp),
					horizontalArrangement = Arrangement.End
				) {
					IconButton(
						onClick = onDismiss,
						modifier = Modifier.size(24.dp)
					) {
						Icon(
							imageVector = Icons.Default.Close,
							contentDescription = "Close"
						)
					}
				}

				Column(
					modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
				) {
					Text(
						text = "Get Connected",
						style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
						modifier = Modifier.padding(bottom = 16.dp)
					)

					ContactInformation()

					Spacer(modifier = Modifier.height(16.dp))

					Button(
						onClick = onDismiss,
						modifier = Modifier
							.fillMaxWidth()
							.height(48.dp),
						colors = ButtonDefaults.buttonColors(
							containerColor = Color(0xFF4E28CC),
							contentColor = Color.White
						),
						shape = RoundedCornerShape(8.dp)
					) {
						Text("Close", fontSize = 16.sp)
					}
				}
			}
		}
	}
}

@Composable
fun PricingDialog(sport: String, onDismiss: () -> Unit) {
	Dialog(
		onDismissRequest = onDismiss,
		properties = DialogProperties(usePlatformDefaultWidth = false)
	) {
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.heightIn(max = 700.dp)
				.padding(16.dp)
				.shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp)),
			shape = RoundedCornerShape(12.dp),
			colors = CardDefaults.cardColors(containerColor = Color.White)
		) {
			Column(
				modifier = Modifier
					.verticalScroll(rememberScrollState())
			) {
				// Close button at the top right
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp),
					horizontalArrangement = Arrangement.End
				) {
					IconButton(
						onClick = onDismiss,
						modifier = Modifier.size(24.dp)
					) {
						Icon(
							imageVector = Icons.Default.Close,
							contentDescription = "Close"
						)
					}
				}

				Column(
					modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
				) {
					Text(
						text = sport,
						style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
						modifier = Modifier.padding(bottom = 16.dp)
					)

					when {
						sport.contains("PICKLEBALL", true) -> {
							PickleballPricingDetails()
						}
						sport.contains("BADMINTON", true) -> {
							BadmintonPricingDetails()
						}
						else -> {
							Text(
								text = "Pricing information coming soon",
								fontSize = 14.sp,
								color = Color.Gray,
								modifier = Modifier.padding(vertical = 8.dp)
							)
						}
					}

					Spacer(modifier = Modifier.height(16.dp))

					Button(
						onClick = onDismiss,
						modifier = Modifier
							.fillMaxWidth()
							.height(48.dp),
						colors = ButtonDefaults.buttonColors(
							containerColor = Color(0xFF4E28CC),
							contentColor = Color.White
						),
						shape = RoundedCornerShape(8.dp)
					) {
						Text("Close", fontSize = 16.sp)
					}
				}
			}
		}
	}
}

@Composable
fun PickleballPricingDetails() {
	Column {
		Text(
			text = "Indoor Coaching Court Rental",
			fontWeight = FontWeight.Bold,
			fontSize = 16.sp,
			modifier = Modifier.padding(bottom = 12.dp)
		)

		Text(
			text = "Monday - Friday",
			fontWeight = FontWeight.Medium,
			fontSize = 14.sp,
			modifier = Modifier.padding(bottom = 4.dp)
		)
		Text(
			text = "08:00AM - 05:00PM : RM20 per hour",
			fontSize = 14.sp,
			color = Color.Gray,
			modifier = Modifier.padding(bottom = 4.dp)
		)
		Text(
			text = "05:00PM - 11:00PM : RM30 per hour",
			fontSize = 14.sp,
			color = Color.Gray,
			modifier = Modifier.padding(bottom = 12.dp)
		)

		Text(
			text = "Saturday - Sunday",
			fontWeight = FontWeight.Medium,
			fontSize = 14.sp,
			modifier = Modifier.padding(bottom = 4.dp)
		)
		Text(
			text = "08:00AM - 11:00PM : RM30 per hour",
			fontSize = 14.sp,
			color = Color.Gray,
			modifier = Modifier.padding(bottom = 16.dp)
		)

		Text(
			text = "Indoor Court Rental",
			fontWeight = FontWeight.Bold,
			fontSize = 16.sp,
			modifier = Modifier.padding(bottom = 12.dp)
		)

		Text(
			text = "Monday - Friday",
			fontWeight = FontWeight.Medium,
			fontSize = 14.sp,
			modifier = Modifier.padding(bottom = 4.dp)
		)
		Text(
			text = "08:00AM - 05:00PM : RM20 per hour",
			fontSize = 14.sp,
			color = Color.Gray,
			modifier = Modifier.padding(bottom = 4.dp)
		)
		Text(
			text = "05:00PM - 11:00PM : RM35 per hour",
			fontSize = 14.sp,
			color = Color.Gray,
			modifier = Modifier.padding(bottom = 12.dp)
		)

		Text(
			text = "Saturday - Sunday",
			fontWeight = FontWeight.Medium,
			fontSize = 14.sp,
			modifier = Modifier.padding(bottom = 4.dp)
		)
		Text(
			text = "08:00AM - 11:00PM : RM35 per hour",
			fontSize = 14.sp,
			color = Color.Gray,
			modifier = Modifier.padding(bottom = 12.dp)
		)

		Text(
			text = "Paddle Rental: RM5 per paddle (2 Balls included)",
			fontSize = 14.sp,
			color = Color.Gray
		)
	}
}

@Composable
fun BadmintonPricingDetails() {
	Column {
		Text(
			text = "VIP Room Court Rental",
			fontWeight = FontWeight.Bold,
			fontSize = 16.sp,
			modifier = Modifier.padding(bottom = 12.dp)
		)

		Text(
			text = "Monday - Friday",
			fontWeight = FontWeight.Medium,
			fontSize = 14.sp,
			modifier = Modifier.padding(bottom = 4.dp)
		)
		Text(
			text = "08:00AM - 02:00AM : RM40 per hour",
			fontSize = 14.sp,
			color = Color.Gray,
			modifier = Modifier.padding(bottom = 12.dp)
		)

		Text(
			text = "Saturday - Sunday",
			fontWeight = FontWeight.Medium,
			fontSize = 14.sp,
			modifier = Modifier.padding(bottom = 4.dp)
		)
		Text(
			text = "08:00AM - 02:00AM : RM30 per hour",
			fontSize = 14.sp,
			color = Color.Gray,
			modifier = Modifier.padding(bottom = 16.dp)
		)

		Text(
			text = "Badminton Court Rental",
			fontWeight = FontWeight.Bold,
			fontSize = 16.sp,
			modifier = Modifier.padding(bottom = 12.dp)
		)

		Text(
			text = "Monday - Friday",
			fontWeight = FontWeight.Medium,
			fontSize = 14.sp,
			modifier = Modifier.padding(bottom = 4.dp)
		)
		Text(
			text = "08:00AM - 06:00PM : RM15 per hour",
			fontSize = 14.sp,
			color = Color.Gray,
			modifier = Modifier.padding(bottom = 4.dp)
		)
		Text(
			text = "06:00PM - 08:30PM : RM25 per hour",
			fontSize = 14.sp,
			color = Color.Gray,
			modifier = Modifier.padding(bottom = 4.dp)
		)
		Text(
			text = "08:30PM - 02:00AM : RM30 per hour",
			fontSize = 14.sp,
			color = Color.Gray,
			modifier = Modifier.padding(bottom = 12.dp)
		)

		Text(
			text = "Saturday - Sunday",
			fontWeight = FontWeight.Medium,
			fontSize = 14.sp,
			modifier = Modifier.padding(bottom = 4.dp)
		)
		Text(
			text = "08:00AM - 02:00AM : RM22 per hour",
			fontSize = 14.sp,
			color = Color.Gray
		)
	}
}

@Composable
fun GeneralPricingInformation(currentVenue: Venue?) {
	Card(
		modifier = Modifier
			.padding(16.dp)
			.fillMaxWidth(),
		shape = RoundedCornerShape(12.dp),
		elevation = CardDefaults.cardElevation(2.dp),
		colors = CardDefaults.cardColors(containerColor = Color.White)
	) {
		Column(modifier = Modifier.padding(16.dp)) {
			Text(
				text = "General Pricing Information",
				fontWeight = FontWeight.Bold,
				fontSize = 18.sp,
				modifier = Modifier.padding(bottom = 12.dp)
			)

			// Add general pricing information here
			Text(
				text = "For detailed sport-specific pricing, please click on the individual sport categories above.",
				fontSize = 14.sp,
				color = Color.Gray
			)
		}
	}
}

@Composable
fun ExpandableListItem(
	title: String,
	supportingText: String? = null,
	expanded: Boolean,
	onExpandChange: () -> Unit,
	content: @Composable () -> Unit
) {
	Column {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.clickable { onExpandChange() }
				.padding(horizontal = 16.dp, vertical = 12.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Column(modifier = Modifier.weight(1f)) {
				Text(
					text = title,
					fontWeight = FontWeight.Medium,
					fontSize = 16.sp
				)
				if (supportingText != null) {
					Text(
						text = supportingText,
						color = Color.Gray,
						fontSize = 14.sp
					)
				}
			}
			Icon(
				imageVector = Icons.Default.ArrowDropDown,
				contentDescription = if (expanded) "Collapse" else "Expand",
				modifier = Modifier
					.size(24.dp)
					.rotate(if (expanded) 180f else 0f)
			)
		}

		if (expanded) {
			content()
		}

		// Make the divider less visible
		Divider(
			modifier = Modifier.padding(horizontal = 16.dp),
			thickness = 0.5.dp,
			color = Color.LightGray.copy(alpha = 0.3f)
		)
	}
}

@Composable
fun VenueLayoutInformation(currentVenue: Venue?, selectedSportLayout: androidx.compose.runtime.MutableState<String>) {
	Card(
		modifier = Modifier
			.padding(16.dp)
			.fillMaxWidth(),
		shape = RoundedCornerShape(12.dp),
		elevation = CardDefaults.cardElevation(2.dp)
	) {
		Column(modifier = Modifier.padding(16.dp)) {
			Text(
				text = "Venue Layout",
				fontWeight = FontWeight.Bold,
				fontSize = 18.sp,
				modifier = Modifier.padding(bottom = 12.dp)
			)

			// Sport selection tabs - REMOVED THE "All" CATEGORY
			if (currentVenue?.sportType?.contains(",") == true) {
				val sports = currentVenue.sportType.split(",").map { it.trim() }
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 12.dp),
					horizontalArrangement = Arrangement.SpaceEvenly
				) {
					sports.forEach { sport ->
						FilterChip(
							selected = selectedSportLayout.value == sport,
							onClick = { selectedSportLayout.value = sport },
							label = { Text(sport) },
							modifier = Modifier.padding(end = 8.dp)
						)
					}
					// REMOVED THE "All" FILTER CHIP
				}
			}

			// Display venue layout image based on selected sport
			Image(
				painter = painterResource(id = R.drawable.venue_layout_sample),
				contentDescription = "Venue Layout for ${selectedSportLayout.value}",
				modifier = Modifier
					.fillMaxWidth()
					.height(200.dp),
				contentScale = ContentScale.Fit
			)

			Spacer(modifier = Modifier.height(12.dp))

			Divider(modifier = Modifier.padding(vertical = 8.dp))

			// Venue layout details based on selected sport
			when (selectedSportLayout.value) {
				"PICKLEBALL" -> {
					Text(
						text = "1.4 Pickle-Sport",
						fontWeight = FontWeight.Bold,
						fontSize = 16.sp,
						modifier = Modifier.padding(bottom = 8.dp)
					)
					Text(
						text = "Pickleball Court Layout:",
						fontWeight = FontWeight.Medium,
						fontSize = 14.sp,
						modifier = Modifier.padding(bottom = 4.dp)
					)
					Text(
						text = "• JJ Pickle-Sport\n• Rest Area\n• Wall\n• Resx Area\n• Edit\n• Entrance",
						fontSize = 12.sp,
						color = Color.Gray
					)
				}
				"BADMINTON" -> {
					Text(
						text = "Badminton Court Layout:",
						fontWeight = FontWeight.Medium,
						fontSize = 14.sp,
						modifier = Modifier.padding(bottom = 4.dp)
					)
					Text(
						text = "• VIP 1\n• Proshop\n• Counter\n• Courts 1-16",
						fontSize = 12.sp,
						color = Color.Gray
					)
				}
				else -> {
					// Default view when no sport is selected or only one sport available
					Text(
						text = "Court Layout:",
						fontWeight = FontWeight.Medium,
						fontSize = 14.sp,
						modifier = Modifier.padding(bottom = 4.dp)
					)
					Text(
						text = "• Professional courts\n• Rest areas\n• Equipment rental\n• Changing facilities",
						fontSize = 12.sp,
						color = Color.Gray
					)
				}
			}
		}
	}
}

@Composable
fun VenuePolicyInformation() {
	Card(
		modifier = Modifier
			.padding(16.dp)
			.fillMaxWidth()
			.heightIn(max = 400.dp), // Set a maximum height
		shape = RoundedCornerShape(12.dp),
		elevation = CardDefaults.cardElevation(2.dp)
	) {
		Column(
			modifier = Modifier
				.verticalScroll(rememberScrollState()) // Add scrolling
				.padding(16.dp)
		) {
			Text(
				text = "Venue Policy",
				fontWeight = FontWeight.Bold,
				fontSize = 18.sp,
				modifier = Modifier.padding(bottom = 12.dp)
			)

			Text(
				text = "General",
				fontWeight = FontWeight.Medium,
				fontSize = 14.sp,
				modifier = Modifier.padding(bottom = 4.dp)
			)
			Text(
				text = "1. Strictly for social games only. You must book directly from management for Tournament / Event Pricing. Management reserves the right to deny entry if pricing policy is not followed.",
				fontSize = 12.sp,
				color = Color.Gray,
				modifier = Modifier.padding(bottom = 4.dp)
			)
			Text(
				text = "2. Players cannot bring cartons of drinks and food into the center, but are welcome to bring hand-carry bottles or food for personal consumption.",
				fontSize = 12.sp,
				color = Color.Gray,
				modifier = Modifier.padding(bottom = 4.dp)
			)
			Text(
				text = "3. Coaches must book directly from management for Coach Pricing. Management reserves the right to deny entry if pricing policy is not followed.",
				fontSize = 12.sp,
				color = Color.Gray,
				modifier = Modifier.padding(bottom = 8.dp)
			)

			Text(
				text = "Venue Policy",
				fontWeight = FontWeight.Medium,
				fontSize = 14.sp,
				modifier = Modifier.padding(bottom = 4.dp)
			)
			Text(
				text = "1. Strictly no refund or carry forward of the non-utilized sessions.",
				fontSize = 12.sp,
				color = Color.Gray,
				modifier = Modifier.padding(bottom = 4.dp)
			)
			Text(
				text = "2. 48 hours advance notice must be given for any change of booking time. Any request to change the booking time is subject to court availability and can only be made once.",
				fontSize = 12.sp,
				color = Color.Gray,
				modifier = Modifier.padding(bottom = 8.dp)
			)

			Text(
				text = "Others",
				fontWeight = FontWeight.Medium,
				fontSize = 14.sp,
				modifier = Modifier.padding(bottom = 4.dp)
			)
			Text(
				text = "1. Please do not leave your valuables unattended. We will not be responsible for any theft.",
				fontSize = 12.sp,
				color = Color.Gray
			)

			// Add more policy content here if needed to test scrolling
			Text(
				text = "4. All bookings must be confirmed with payment. No reservation is considered confirmed until payment is received.",
				fontSize = 12.sp,
				color = Color.Gray,
				modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
			)
			Text(
				text = "5. Proper sports attire and non-marking shoes are required for all court activities.",
				fontSize = 12.sp,
				color = Color.Gray,
				modifier = Modifier.padding(bottom = 4.dp)
			)
			Text(
				text = "6. Children under 12 must be accompanied by an adult at all times.",
				fontSize = 12.sp,
				color = Color.Gray,
				modifier = Modifier.padding(bottom = 4.dp)
			)
		}
	}
}

@Composable
fun ContactInformation() {
	Card(
		modifier = Modifier
			.padding(16.dp)
			.fillMaxWidth(),
		shape = RoundedCornerShape(12.dp),
		elevation = CardDefaults.cardElevation(2.dp)
	) {
		Column(modifier = Modifier.padding(16.dp)) {
			Text(
				text = "Get Connected",
				fontWeight = FontWeight.Bold,
				fontSize = 18.sp,
				modifier = Modifier.padding(bottom = 12.dp)
			)

			Text(
				text = "012-822 8032",
				fontWeight = FontWeight.Medium,
				fontSize = 16.sp,
				modifier = Modifier.padding(bottom = 8.dp)
			)

			Divider(modifier = Modifier.padding(vertical = 8.dp))

			Text(
				text = "Centre:",
				fontWeight = FontWeight.Medium,
				fontSize = 14.sp,
				modifier = Modifier.padding(bottom = 4.dp)
			)
			Text(
				text = "Seksyen 1 Wangsa Maju, Kuala Lumpur, Lumpur",
				fontSize = 12.sp,
				color = Color.Gray
			)
		}
	}
}