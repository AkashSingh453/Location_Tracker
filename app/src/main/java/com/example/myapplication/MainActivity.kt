package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.myapplication.ui.theme.MyApplicationTheme
import android.Manifest
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0
        )
        setContent {
            MyApplicationTheme {
                 Column (Modifier.fillMaxSize()){
                     Button(onClick = {
                         Intent(applicationContext,LocationService::class.java).apply {
                             action = LocationService.ACTION_START
                             startService(this)
                         }
                     }) {
                         Text(text = "Start")
                     }
                     Button(onClick = {
                         Intent(applicationContext,LocationService::class.java).apply {
                             action = LocationService.ACTION_STOP
                             startService(this)
                         }
                     }) {
                         Text(text = "Stop")
                     }
                 }
            }
        }
    }
}

