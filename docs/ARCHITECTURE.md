# Arsitektur APK Dashboard — scriptGD

## Status keputusan
- Bahasa dan UI: Kotlin + Jetpack Compose (Material 3), Navigation Compose.
- Source GAS (`Code.gs`, `Index.html`) dipelajari sebagai referensi, bukan dikonversi. Source yang ditinjau hanya mengunggah gambar ke folder Drive `Lukisanku`; tidak ada listing galeri persisten, integrasi Sheets, dashboard terstruktur, atau login Google.
- Login Google dapat memberi aplikasi token OAuth, tetapi tidak otomatis menciptakan tiga sumber data. Data, izin akses, API Google Cloud, serta struktur setiap sumber perlu tersedia dan diverifikasi.
- Belum membuat scaffold APK: sumber data yang dimaksud dengan “3 sumber”, ID/schema, dan kebutuhan baca/tulis masih harus dipetakan. Implementasi di bawah adalah kandidat arah, bukan komitmen fitur final.

## Arsitektur kandidat
- Lapisan: `data`, `domain`, `presentation`; ViewModel + StateFlow.
- UI: Jetpack Compose Material 3, Navigation Compose.
- Google sign-in dan akses Drive/Sheets: gunakan library Android/OAuth resmi yang didukung Google saat implementasi; jangan membangun redirect OAuth secara manual berdasarkan asumsi.
- Scope diminta sesuai kebutuhan nyata dan bertahap. Folder/file Drive dapat diuji dengan akses paling sempit yang memadai; Sheets API hanya ditambahkan jika sumber spreadsheet dan skemanya dikonfirmasi.
- Retrofit/OkHttp dapat dipilih untuk panggilan API, dengan token mengikuti lifecycle library auth yang dipilih, refresh terbatas, retry maksimum satu kali, dan tidak mencatat token/header ke log.
- Sebelum menetapkan penyimpanan token, validasi rekomendasi resmi library terhadap Android versi target, backup/restore, logout, pencabutan akses, dan lifecycle token. Jangan mengunci desain ke mekanisme enkripsi yang belum diuji.

## Alur yang akan divalidasi sebelum scaffold
1. Buat daftar tiga sumber: jenis layanan, owner, ID/URL, data/field yang dibutuhkan, dan izin yang tersedia.
2. Pastikan sumber benar-benar di Google Drive/Sheets atau layanan lain; login Google tidak memberi akses ke data di luar grant/API yang tersedia.
3. Buat konfigurasi OAuth/API Google Cloud yang sesuai untuk aplikasi Android dan uji sign-in dengan library yang dipilih.
4. Jalankan panggilan read-only terbatas ke setiap sumber, catat hasil, izin dan batasan aktual.
5. Baru rancang model domain, dashboard, scope, dan scaffold berdasarkan respons API yang terbukti.

## Kandidat struktur proyek
`app/src/main/java/<package>/` berisi `data/auth`, `data/drive`, `data/sheets`, `domain/model`, `domain/repository`, `presentation/navigation`, `presentation/dashboard`, dan `presentation/components`. Ini masih rancangan, bukan implementasi teruji.

## Pembangunan dan verifikasi
Lingkungan saat ini tidak menyediakan Java, Gradle, atau Android SDK. Build lokal belum dapat dilakukan. Build dapat diverifikasi di GitHub Actions setelah proyek siap, tetapi tidak ada perubahan yang akan di-commit atau di-push tanpa persetujuan eksplisit.

## Keamanan dan batasan
Lihat `docs/OAUTH_SECURITY.md` dan `docs/SECURITY_REVIEW.md`. Review desain tidak membuktikan bahwa akses ke sumber atau implementasi OAuth berfungsi; verifikasi perlu memakai akun dan sumber yang dimaksud, tanpa meminta pengguna membagikan password atau kode verifikasi.