package com.example.myfirstkmpapp.util

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

class JvmShareManager : ShareManager {
    override fun shareText(text: String, title: String) {
        // Desktop doesn't have a standard share sheet, so we copy to clipboard
        val selection = StringSelection(text)
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(selection, selection)
        println("Copied to clipboard: $text")
    }
}

actual fun getShareManager(context: Any?): ShareManager = JvmShareManager()
