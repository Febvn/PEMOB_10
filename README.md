# Notes App - Tugas Praktikum Minggu 7 📝

Upgrade Notes App dengan fitur SQLDelight untuk penyimpanan lokal, fitur pencarian, dan pengaturan tema menggunakan DataStore. Aplikasi ini mendukung penuh mode **Offline-First**.

## 🚀 Fitur Utama
- **CRUD Operations**: Tambah, Baca, Edit, dan Hapus catatan secara permanen.
- **Local Storage (SQLDelight)**: Menggunakan SQLite untuk menyimpan data secara lokal di perangkat.
- **Search Functionality**: Mencari catatan berdasarkan judul atau isi konten secara real-time.
- **Settings & DataStore**: Menyimpan preferensi pengguna (Dark Mode & Sort Order) secara persisten.
- **Favorite System**: Menandai catatan penting sebagai favorit.
- **Offline-First**: Aplikasi berfungsi 100% tanpa koneksi internet.

## 🛠️ Tech Stack
- **Kotlin Multiplatform (KMP)**: Target Desktop (JVM) & Android.
- **Compose Multiplatform**: Untuk UI yang konsisten di berbagai platform.
- **SQLDelight**: Database engine untuk persistensi data lokal.
- **Jetpack DataStore**: Untuk menyimpan preferensi pengaturan sederhana.
- **Voyager**: Untuk navigasi antar layar (Tabs & Stacks).

## 📊 Database Schema (SQLDelight)
```sql
CREATE TABLE IF NOT EXISTS NoteEntity (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    color INTEGER NOT NULL,
    isFavorite INTEGER NOT NULL DEFAULT 0,
    timestamp INTEGER NOT NULL,
    cloudId TEXT
);
```

## 📸 Screenshots & Demo

### Tampilan Aplikasi (Notes & CRUD)
![Tampilan Note Baru](note%20baru.JPG)
*Dokumentasi pembuatan catatan baru dengan UI Skeuomorphic.*

### Video Demo (Offline Mode & Local Storage)
Klik link di bawah untuk melihat demonstrasi CRUD, Search, dan Offline Mode:
- [📺 Demo Local Storage & Offline Mode](demo_local%20storage.mp4)

## 📸 Screenshot Gallery (Representasi Fungsionalitas)

### Galeri 1: Navigasi & Utama
| Profile View (Main) | Note List (Empty) | Note Detail |
|:---:|:---:|:---:|
| ![Profile](setting_note%20functionf.JPG) | ![Empty List](there%20is%20a%20note.JPG) | ![Note Detail](note%20baru.JPG) |

### Galeri 2: Fitur & Modifikasi
| Add Note Form | Note List View | Search & Query |
|:---:|:---:|:---:|
| ![Add Note](note%20baru.JPG) | ![Note List](there%20is%202%20note.JPG) | ![Search](seaarch%20query%20funtcion.JPG) |

---
**Pengembangan Aplikasi Mobile - ITERA 2024**  
**Nama:** (Isi Nama Anda)  
**NIM:** (Isi NIM Anda)  
**Repository:** [Febvn/pemob_7](https://github.com/Febvn/pemob_7) (Branch: week-7)
