package com.example.myfirstkmpapp.util

import androidx.compose.ui.graphics.ImageBitmap

actual fun pickImageFile(onImagePicked: (ByteArray?) -> Unit) {
    onImagePicked(null)
}

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    // Kembalikan ImageBitmap kosong untuk keperluan kompilasi tes
    return ImageBitmap(1, 1)
}
