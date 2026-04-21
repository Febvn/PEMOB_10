# Notes App - Tugas Praktikum Minggu 7

Notes App ini merupakan aplikasi pencatatan berbasis **Kotlin Multiplatform (KMP)** yang dirancang dengan arsitektur **Offline-First**. Fokus utama dari pembaruan materi Minggu 7 ini adalah integrasi database relasional menggunakan SQLDelight dan manajemen preferensi pengguna menggunakan Jetpack DataStore.

## Deskripsi Teknis dan Fitur

Aplikasi ini menerapkan standar pengembangan modern dengan fitur-fitur sebagai berikut:

### 1. Database Persistence (SQLDelight)
Aplikasi menggunakan **SQLDelight** sebagai engine database lokal. SQLDelight menghasilkan API Kotlin yang type-safe dari query SQL. 
- **Persistensi Data**: Seluruh catatan (Notes) disimpan dalam database SQLite lokal.
- **CRUD Operations**: Mendukung fungsionalitas penuh untuk membuat, membaca, memperbarui, dan menghapus catatan secara real-time.
- **Auto-Increment ID**: Setiap catatan memiliki ID unik yang dihasilkan secara otomatis oleh database.

### 2. Search Functionality
Fitur pencarian diimplementasikan menggunakan query SQL `LIKE` yang efisien. Pengguna dapat mencari catatan berdasarkan kata kunci pada judul maupun isi konten. Hasil pencarian akan diperbarui secara reaktif pada antarmuka pengguna.

### 3. Settings & DataStore Integration
Aplikasi menggunakan **Jetpack DataStore (Preferences)** untuk menyimpan metadata pengaturan pengguna secara persisten.
- **Theme Management**: Pengguna dapat beralih antara Mode Terang (Light Mode) dan Mode Gelap (Dark Mode).
- **Sort Order**: DataStore menyimpan pilihan urutan catatan pengguna (berdasarkan waktu terbaru atau abjad) sehingga tetap tersimpan meskipun aplikasi ditutup.

### 4. Navigasi Voyager
Implementasi navigasi menggunakan library **Voyager** yang mendukung manajemen *State* dan *Screen* dengan cara yang deklaratif dan efisien dalam ekosistem Compose.

---

## Tech Stack
- **Languages**: Kotlin (Common, JVM, Android).
- **UI Framework**: Compose Multiplatform.
- **Database**: SQLDelight (SQLite Driver).
- **Storage**: Jetpack DataStore (Preferences).
- **Navigation**: Voyager Navigation.
- **Async**: Kotlin Coroutines & Flow.

---

## Database Schema (SQLDelight)
Berikut adalah skema tabel `NoteEntity` yang digunakan untuk manajemen data catatan:
```sql
CREATE TABLE IF NOT EXISTS NoteEntity (
    id INTEGER PRIMARY KEY AUTOINCREMENT, -- Identitas Unik Note
    title TEXT NOT NULL,                  -- Judul Catatan
    content TEXT NOT NULL,                -- Isi Konten Catatan
    color INTEGER NOT NULL,               -- Representasi Warna (Hex)
    isFavorite INTEGER NOT NULL DEFAULT 0, -- Status Favorit (Boolean 0/1)
    timestamp INTEGER NOT NULL,           -- Waktu Pembuatan/Edit (Unix)
    cloudId TEXT                          -- ID untuk Sinkronisasi (Masa Depan)
);
```

---

## Dokumentasi dan Demonstrasi Sistem

### Video Demonstrasi Utama
Video di bawah ini menunjukkan alur fungsionalitas CRUD, pencarian catatan, perpindahan tema (Settings), dan pembuktian mode Offline-First.
- [📺 Lihat Video Demo (CRUD, Search, dan Offline Mode)](demo_local%20storage.mp4)

### Galeri Dokumentasi Antarmuka
Dokumentasi visual di bawah ini merepresentasikan status sistem dalam berbagai kondisi operasional:

<div align="center">
  <table style="border: none;">
    <tr>
      <td align="center">
        <img src="note%20baru.JPG" width="220"/><br/>
        <b>Form Tambah Catatan</b><br/>
        <sub>Antarmuka input judul dan konten catatan dengan desain skeuomorphic.</sub>
      </td>
      <td align="center">
        <img src="seaarch%20query%20funtcion.JPG" width="220"/><br/>
        <b>Fitur Pencarian</b><br/>
        <sub>Implementasi filter dinamis untuk mencari catatan dari database.</sub>
      </td>
      <td align="center">
        <img src="setting_note%20functionf.JPG" width="220"/><br/>
        <b>Menu Pengaturan</b><br/>
        <sub>Manajemen preferensi tema dan profil pengguna menggunakan DataStore.</sub>
      </td>
    </tr>
  </table>
  <br/>
  <table style="border: none;">
    <tr>
      <td align="center">
        <img src="there%20is%202%20note.JPG" width="220"/><br/>
        <b>Daftar 2 Catatan</b><br/>
        <sub>Visualisasi tampilan daftar saat database memiliki lebih dari satu entri.</sub>
      </td>
      <td align="center">
        <img src="there%20is%20a%20note.JPG" width="220"/><br/>
        <b>Daftar Single Catatan</b><br/>
        <sub>Representasi tampilan utama (Home) dengan data yang berhasil dimuat.</sub>
      </td>
    </tr>
  </table>
</div>

---
**Pengembangan Aplikasi Mobile - ITERA 2024**  
**Nama:** (Isi Nama Anda)  
**NIM:** (Isi NIM Anda)  
**Repository:** [Febvn/pemob_7](https://github.com/Febvn/pemob_7) (Branch: week-7)
