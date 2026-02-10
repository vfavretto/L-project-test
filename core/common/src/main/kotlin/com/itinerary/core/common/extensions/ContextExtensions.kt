package com.itinerary.core.common.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.openMapsWithCoordinates(latitude: Double, longitude: Double) {
    val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.google.android.apps.maps")
    }
    
    // Fallback to browser if Google Maps is not installed
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude")
        )
        startActivity(browserIntent)
    }
}

fun Context.shareLocation(latitude: Double, longitude: Double, locationName: String?) {
    val message = if (locationName != null) {
        "$locationName\nhttps://www.google.com/maps/search/?api=1&query=$latitude,$longitude"
    } else {
        "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"
    }
    
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    startActivity(Intent.createChooser(intent, "Compartilhar localização"))
}
