# My Profile & News App - Pemob_6 (KMP Edition)

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org/)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.10.0-orange.svg?style=flat&logo=jetpack-compose)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20Repo-brightgreen.svg?style=flat)](https://developer.android.com/topic/architecture)

**My Profile & News App** merupakan aplikasi Multiplatform berbasis Kotlin yang mengimplementasikan arsitektur MVVM (Model-View-ViewModel) dan Repository Pattern. Project ini menggabungkan manajemen profil skeuomorphic dengan sistem pembaca berita modern.

---

## News Reader (WEEK_6) - NEW!

Fitur pembaca berita canggih yang mengambil data dari API publik dengan performa optimal:
*   **Networking:** Menggunakan **Ktor Client** untuk pengambilan data asinkron.
*   **Data Parsing:** Integrasi **Kotlinx Serialization** yang efisien.
*   **Image Loading:** Render gambar cepat dengan **Coil 3 (Multiplatform)**.
*   **UX Features:** Implementasi *Pull to Refresh*, *Shimmer Loading*, dan *Error States*.
*   **API Source:** [Spaceflight News API](https://api.spaceflightnewsapi.net/v4/articles/) (No Key Required).

---

## Previous Features (WEEK_4 & 5)

*   **Integrated Note-Taking System:** Fitur manajemen catatan (Add, Edit, Delete, Favorite) berbasis teks yang efisien.
*   **Professional MVVM Architecture:** Pemisahan tegas antara UI, ViewModel, dan Data.
*   **Dark Monochrome Design:** Estetika skeuomorphic yang elegan dengan palet warna monokrom.
*   **Optimized Performance:** Kecepatan build maksimal dengan manajemen memori 2GB.

---

## Video Demo Navigasi (WEEK_5)

![Demo Navigasi](https://raw.githubusercontent.com/Febvn/pemob_5/WEEK_5/demo-navigation.mp4)

---

## Screenshot Gallery (Aplikasi Pemob_4)

Berikut adalah galeri tampilan aplikasi sebagai representasi fungsionalitas sistem:

### Galeri 1: Navigasi & Utama
| Profile View (Main) | Note List (Empty) | Note Detail |
| :---: | :---: | :---: |
| ![Profile](1.JPG) | ![Empty List](2.JPG) | ![Note Detail](3.JPG) |

### Galeri 2: Fitur & Modifikasi
| Add Note Form | Note List View | Edit Profile |
| :---: | :---: | :---: |
| ![Add Note](4.JPG) | ![Note List](5.JPG) | ![Edit Profile](7.JPG) |

---

## Struktur Proyek

Navigasi paket aplikasi mengikuti standar MVVM yang modular:

```text
├── composeApp/
│   ├── src/commonMain/kotlin/com/example/myfirstkmpapp/
│   │   ├── data/           # Layer Data (Profile)
│   │   ├── news/           # Fitur News Reader (NEW!)
│   │   ├── viewmodel/      # Layer Logika
│   │   ├── ui/             # Layer Presentasi
│   │   └── App.kt          # Main Entry Point
```

---

## Instalasi & Cara Menjalankan

Langkah-langkah menjalankan aplikasi pada platform Desktop (JVM):

1. **Clone & Setup:**
   ```powershell
   git clone https://github.com/Febvn/pemob_5.git
   cd pemob_5
   ```
2. **Run Perintah Berikut:**
   ```powershell
   ./gradlew :composeApp:run
   ```

---

## Author (WEEK_5 Solution)

**Febrian Valentino Nugroho**
*   **GitHub:** [@Febvn](https://github.com/Febvn)
*   **Branch Repo:** [github.com/Febvn/pemob_5/tree/WEEK_5](https://github.com/Febvn/pemob_5/tree/WEEK_5)
*   **Kelas:** Pemrograman Mobile (Pemob)

---

## Lisensi

Proyek ini dilisensikan di bawah **MIT License**.
