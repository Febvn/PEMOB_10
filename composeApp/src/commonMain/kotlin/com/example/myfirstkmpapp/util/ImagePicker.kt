package com.example.myfirstkmpapp.util

import androidx.compose.ui.graphics.ImageBitmap

/**
 * Membuka dialog pemilihan file gambar.
 */
expect fun pickImageFile(onImagePicked: (ByteArray?) -> Unit)

/**
 * Konversi ByteArray ke ImageBitmap.
 */
expect fun ByteArray.toImageBitmap(): ImageBitmap
