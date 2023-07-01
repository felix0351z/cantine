package de.juliando.app.android.ui.scanner

import androidx.lifecycle.ViewModel


class ScannerViewModel : ViewModel() {

    private var lastCode = ""

    // Prevents to scan the same QR Code multiple times
    fun checkQrCode(code: String): Boolean {
        return if (code != lastCode) {
            lastCode = code
            true
        } else false
    }


}