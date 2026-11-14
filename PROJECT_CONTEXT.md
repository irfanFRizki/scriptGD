# scriptGD — Project Context

## Tujuan
Membangun aplikasi dashboard Android (APK) berdasarkan pembelajaran dari aplikasi web Google Apps Script (GAS): `code.gs`, `Index.html`, dan URL web app GAS (`.../exec`). UI dashboard APK akan berbeda dari HTML GAS. Source GAS adalah referensi untuk memahami struktur, data, dan fungsi backend, bukan untuk dikonversi/disalin secara langsung.

## Analisis source yang ditinjau
Source terbaru menunjukkan aplikasi "Lukisanku": HTML satu halaman untuk memilih foto, preview, upload melalui `google.script.run.uploadImage(...)`, lalu menampilkan foto baru ke galeri pada sesi saat ini. GAS menyimpan foto ke folder Drive bernama `Lukisanku` dan mengembalikan URL. Tidak ada integrasi Google Sheets, galeri persisten, atau autentikasi pengguna eksplisit pada source; akses bergantung pada deployment dan konteks web app. Source menjadi dasar perilaku backend, bukan dasar tampilan dashboard.

## Fitur tujuan
- Menampilkan dashboard/data yang disediakan GAS dalam aplikasi Android.
- Menyediakan alur dari Telegram: pengguna mengirim instruksi untuk membuat/menambahkan card baru; aplikasi/layanan meneruskan data dengan aman ke GAS atau API yang disepakati.
- Mendukung URL web app GAS sebagai sumber backend; metode autentikasi, payload, dan izin harus dikonfirmasi dari kode dan konfigurasi nyata.

## Status saat ini
- Repository GitHub: `https://github.com/irfanFRizki/scriptGD`, branch `main`.
- Source Code.gs dan Index.html terbaru telah dibaca dan diserahkan untuk peninjauan Rea.
- Source saat ini tidak memiliki Google Sheets, fitur card, atau dashboard; URL GAS perlu diuji/read-only dan struktur data nyata perlu diperiksa sebelum desain final.
- Lingkungan lokal belum memiliki Java/Gradle/Android SDK; build APK lokal belum tersedia. CI belum dikonfirmasi.
- Belum ada APK, endpoint, bot command, ataupun mekanisme penambahan card yang diimplementasikan.
- Perubahan lokal belum di-commit/push.

## Rencana awal setelah sumber diterima
1. Simpan dan tinjau ketiga sumber; petakan UI, fungsi GAS, data, dan alur tambah card.
2. Uji URL secara aman, identifikasi kebutuhan akses, parameter, serta respons. Jangan mengirim perubahan data saat eksplorasi.
3. Tentukan arsitektur Android (misalnya WebView/Capacitor versus UI native) berdasarkan HTML dan perilaku aplikasi nyata.
4. Implementasikan dan validasi build APK, lalu uji alur dashboard dan tambah card.
5. Integrasi Telegram melalui pendekatan yang disepakati, tanpa menyimpan token bot/secret di APK atau repository. Verifikasi otorisasi dan pembatasan akses.

## Guardrails
- Jangan commit/push, deploy GAS, memodifikasi spreadsheet/data, atau mengirim pesan Telegram yang membuat card tanpa persetujuan eksplisit.
- Jangan memasukkan token, secret, kredensial, atau data sensitif ke Git/APK. Prefer backend/server-side secret storage; APK tidak dapat menjaga secret.
- Verifikasi endpoint, skema payload, dan izin dari berkas asli, bukan asumsi. Catat ketidakpastian dan minta konfirmasi bila operasi dapat membuat/mengubah data.
- Jangan klaim APK siap sebelum build nyata sukses dan artefak diverifikasi.

## Perintah
Belum ditetapkan; tentukan setelah stack dipilih dari analisis sumber.

## Keputusan & Arsitektur (revisi sesuai kebutuhan baca/tulis)
- Bahasa/UI: Kotlin + Jetpack Compose (Material 3), Navigation Compose.
- Aplikasi akan login ke akun Google pengguna dan menyediakan baca/tulis untuk layanan Google yang diperlukan fitur. OAuth tetap meminta scope minimum yang sesuai, dengan izin tambahan dijelaskan dan diminta saat pengguna memakai fitur yang memerlukannya. Baca/tulis tidak berarti akses tak terbatas ke seluruh Drive; izin efektif mengikuti grant OAuth, file sharing, konfigurasi API, dan kelayakan/publikasi aplikasi.
- Source GAS yang ditinjau menggunakan DriveApp: mencari folder bernama `Lukisanku`, membuat folder jika belum ada, lalu mengunggah gambar dengan nama unik. HTML juga memasukkan hasil unggahan ke galeri sesi. Ia tidak membaca ulang daftar file dari Drive, tidak menggunakan Sheets, dan tidak menghapus/mengedit file.
- MVP yang direkomendasikan: login Google; layar Drive dengan daftar/galeri isi folder `Lukisanku` dan refresh; unggah foto; buat folder bila belum ada hanya setelah pengguna menekan aksi yang memerlukan folder; buka file; edit metadata nama bila API/izin mendukung dengan konfirmasi; hapus file hanya dengan konfirmasi. Sheets: akses baca/tulis disiapkan sebagai konektor/fitur lanjutan, namun butuh file spreadsheet/schema/range nyata agar bisa diuji dan UI-nya dirancang dengan benar.
- Scope Drive dipilih sesudah verifikasi pola akses: untuk file/folder yang pengguna pilih gunakan scope sempit seperti `drive.file`; untuk menelusuri folder `Lukisanku` yang sudah ada di Drive mungkin perlu scope Drive lebih luas/terbatas yang sesuai API, plus consent-screen dan kebijakan Google. Jangan otomatis meminta `drive` penuh sebelum kebutuhan dan kelayakan scope diverifikasi. `drive.file` mungkin tidak mengizinkan listing file lama yang tidak dibuat/dipilih melalui aplikasi.
- Untuk Google Sheets, gunakan Sheets API dengan scope minimum yang mendukung kebutuhan baca/tulis; `spreadsheets` adalah scope sensitif dan wajib diaktifkan/diungkapkan sesuai kebijakan. ID spreadsheet dan struktur tidak boleh direka.
- Auth/token ditentukan berdasarkan library Android resmi yang dipilih dan diuji. Jangan membangun redirect/PKCE manual dari asumsi; jangan gunakan embedded WebView atau masukkan client secret ke APK.
- Belum ada scaffold APK. Host tidak memiliki Java/Gradle/Android SDK, jadi build lokal belum dapat dilakukan. Build dan distribusi APK untuk testing akan disiapkan tanpa commit/push, melalui jalur yang tidak memerlukan publikasi repository; perlu verifikasi toolchain/build runner sebelum menjanjikan artefak.

## Struktur Multi-Dashboard & Git Workflow
- Arsitektur UI: Hub multi-dashboard. Rute awal menampilkan pilihan/daftar dashboard; dashboard pertama yang aktif adalah "Lukisanku" (Google Drive), dengan arsitektur yang siap menerima modul dashboard berikutnya (seperti Google Sheets atau dashboard masa depan).
- Lingkup MVP saat ini: Login Google -> Hub Dashboard -> Dashboard Lukisanku (baca daftar isi folder Drive, pratinjau foto, refresh, upload foto baru, hapus/rename dengan konfirmasi). Dashboard berikutnya belum diaktifkan sebelum data/skemanya tersedia.
- Build & Distribusi: Menggunakan GitHub Actions workflow di repo `https://github.com/irfanFRizki/scriptGD` untuk build APK dan publikasi release/artifact APK secara otomatis saat push tag versi.
- Git Workflow: Komit bertahap, berurutan, terstruktur. Versi dimulai dari `v0.1.0` (atau tag rilis yang disetujui), dipush ke branch `main`, dan pembuatan tag resmi untuk memicu CI/CD APK release. Tidak memasukkan kredensial/token/rahasia ke dalam repositori.


## Pemeliharaan
Perbarui dokumen ini saat arsitektur, cara build, endpoint, atau batas keamanan berubah.
