package com.moviles.unaroom

import android.app.Application
import com.moviles.unaroom.data.AppContainer

class UnaRoomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Pass the Application context so Room can locate the database file.
        AppContainer.init(this)
    }
}

