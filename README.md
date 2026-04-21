# Notes App - Tugas Praktikum Minggu 7

Upgrade Notes App dengan fitur SQLDelight untuk penyimpanan lokal, fitur pencarian, dan pengaturan tema menggunakan DataStore. Aplikasi ini mendukung penuh mode Offline-First.

## Fitur Utama
- CRUD Operations: Tambah, Baca, Edit, dan Hapus catatan secara permanen.
- Local Storage (SQLDelight): Menggunakan SQLite untuk menyimpan data secara lokal di perangkat.
- Search Functionality: Mencari catatan berdasarkan judul atau isi konten secara real-time.
- Settings & DataStore: Menyimpan preferensi pengguna (Dark Mode & Sort Order) secara persisten.
- Favorite System: Menandai catatan penting sebagai favorit.
- Offline-First: Aplikasi berfungsi 100% tanpa koneksi internet.

## Tech Stack
- Kotlin Multiplatform (KMP): Target Desktop (JVM) & Android.
- Compose Multiplatform: Untuk UI yang konsisten di berbagai platform.
- SQLDelight: Database engine untuk persistensi data lokal.
- Jetpack DataStore: Untuk menyimpan preferensi pengaturan sederhana.
- Voyager: Untuk navigasi antar layar.

## Database Schema (SQLDelight)
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

## Dokumentasi dan Demo

### Video Demo Utama
- [📺 Lihat Video Demo (CRUD, Search, dan Offline Mode)](demo_local%20storage.mp4)

### Galeri Tampilan Aplikasi
Berikut adalah dokumentasi visual antarmuka pengguna aplikasi Note yang mencakup proses pembuatan catatan, fitur pencarian, menu pengaturan, serta representasi data dalam database lokal.

<div align="center">
  <table style="border: none;">
    <tr>
      <td align="center"><img src="note%20baru.JPG" width="200"/><br/><sub>Tambah Note Baru</sub></td>
      <td align="center"><img src="seaarch%20query%20funtcion.JPG" width="200"/><br/><sub>Fitur Pencarian</sub></td>
      <td align="center"><img src="setting_note%20functionf.JPG" width="200"/><br/><sub>Menu Pengaturan</sub></td>
    </tr>
  </table>
  <table style="border: none;">
    <tr>
      <td align="center"><img src="there%20is%202%20note.JPG" width="200"/><br/><sub>Daftar 2 Catatan</sub></td>
      <td align="center"><img src="there%20is%20a%20note.JPG" width="200"/><br/><sub>Daftar 1 Catatan</sub></td>
    </tr>
  </table>
</div>

---
**Pengembangan Aplikasi Mobile - ITERA 2024**  
**Nama:** (Isi Nama Anda)  
**NIM:** (Isi NIM Anda)  
**Repository:** [Febvn/pemob_7](https://github.com/Febvn/pemob_7) (Branch: week-7)
