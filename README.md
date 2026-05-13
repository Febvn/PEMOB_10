# Laporan Praktikum Minggu 10 - Pengembangan Aplikasi Mobile

Proyek ini berisi implementasi Dependency Injection (DI) menggunakan Koin dan pengujian otomatis (Testing) pada aplikasi Notes menggunakan Kotlin Multiplatform.

## Implementasi Dependency Injection (Koin)

Manajemen dependensi telah dipisahkan menjadi dua modul utama untuk menjaga kebersihan kode dan kemudahan pengujian:
1. **dataModule**: Mengelola instance database SQLDelight, NoteRepository, SettingsRepository, dan GeminiService.
2. **viewModelModule**: Mengelola semua ViewModel aplikasi (NoteViewModel, ProfileViewModel, dan SettingsViewModel).

Kedua modul tersebut digabungkan dalam commonModule yang diinisialisasi pada saat aplikasi dimulai.

## Daftar Test Cases

### 1. Unit Test: NoteRepository
Pengujian dilakukan untuk memastikan integritas data pada layer repository:
- **getAllNotes returns notes correctly**: Memastikan repository dapat mengambil dan memetakan data dari database.
- **insertNote calls queries insertNote**: Memastikan fungsi penambahan catatan memanggil query database yang tepat.
- **updateNote calls queries updateNote**: Memastikan fungsi pembaruan catatan memanggil query database yang tepat.
- **deleteNote calls queries deleteNoteById**: Memastikan fungsi penghapusan catatan memanggil query database yang tepat.
- **toggleFavorite calls queries updateFavoriteStatus**: Memastikan fungsi pengubahan status favorit memperbarui database dengan benar.

### 2. Unit Test & Flow Test: NoteViewModel
Pengujian pada layer ViewModel menggunakan MockK untuk dependensi dan Turbine untuk pengujian Flow:
- **initial state is correct**: Memastikan StateFlow UI menginisialisasi state dengan benar (termasuk transisi loading).
- **search query updates state**: Memastikan perubahan query pencarian diperbarui pada state secara reaktif.
- **addNote calls repository insert**: Memastikan perintah penambahan catatan diteruskan ke repository.
- **deleteNote calls repository delete**: Memastikan perintah penghapusan catatan diteruskan ke repository.
- **uiState updates when repository emits new notes**: Menggunakan Turbine untuk memvalidasi bahwa UI State bereaksi terhadap emisi data baru dari repository.

### 3. UI Test: NoteListScreen
Pengujian antarmuka menggunakan Compose Test Rule:
- **emptyList_showsEmptyMessage**: Memastikan pesan "Empty Note List" muncul jika tidak ada data.
- **notesList_showsNotes**: Memastikan daftar catatan ditampilkan dengan benar saat data tersedia.
- **searchNoMatch_showsNoMatchMessage**: Memastikan pesan "No matches found" muncul saat hasil pencarian kosong.

## Video Demo Pengujian

Berikut adalah rekaman proses menjalankan semua unit test dan UI test:

https://raw.githubusercontent.com/Febvn/PEMOB_10/week-10/Demo%20video%20baru.mp4

---
**Program Studi Teknik Informatika**  
**Institut Teknologi Sumatera**
