# Keamanan OAuth dan akses Google — scriptGD

> Ini dokumen desain, bukan implementasi teruji. Timses mengulas pendekatan dan menemukan beberapa hal yang wajib divalidasi sebelum kode dibuat.

## Fakta dan asumsi
- Login dengan Google dapat memberi aplikasi otorisasi berdasarkan client, scope, consent, dan izin file yang diberikan. Itu tidak otomatis menyediakan data, endpoint, spreadsheet, atau akses ke akun/sumber lain.
- Source GAS yang ditinjau mengunggah gambar ke Drive; source tidak membuktikan adanya tiga sumber data, listing Drive, integrasi Sheets, atau dashboard.
- `drive.readonly` cocok untuk baca Drive, bukan upload. Upload memerlukan akses tulis dan review consent tersendiri.
- Scope yang lebih luas tidak dipilih sampai kebutuhan file/field dan sumber data dipetakan.

## Persyaratan sebelum implementasi
- Pilih library autentikasi Android yang saat itu direkomendasikan Google dan dukung lifecycle token yang benar. Jangan membuat flow OAuth/redirect secara manual dari asumsi.
- Jangan memakai embedded WebView untuk autentikasi Google. Jangan menaruh `client_secret`, password, refresh token pengguna, atau credential bot di APK atau repo.
- Verifikasi konfigurasi OAuth Android (package name, signing certificate, consent screen, API enablement) dengan akun pengujian.
- Pakai scope tersempit yang memenuhi kebutuhan, tambahkan hanya setelah sumber/fitur terbukti memerlukan.
- Jangan asumsikan token refresh selalu terbit atau berlaku; uji perilaku library dan consent-screen pada akun yang dipakai.
- Hindari menyimpan access token jika library dapat mengelolanya di memori. Jika token perlu disimpan, ikuti solusi terpelihara dan API Android yang didukung, lalu uji enkripsi, backup/restore, logout, dan revoke pada perangkat target. Jangan mengasumsikan Tink, StrongBox, atau user-auth-bound key tersedia untuk semua perangkat.
- Redaksi token dan Authorization header dari log, crash report, analytics, clipboard, dan backup.
- Permintaan API harus HTTPS. Batas refresh/retry, penanganan 401, kegagalan refresh, dan logout harus diuji.

## Redirect dan PKCE
Gunakan alur Android yang didukung oleh library Google terpilih. Jangan menganggap custom URI `com.googleusercontent.apps.<CLIENT_ID>:/oauth2redirect` atau localhost sebagai callback yang didukung tanpa verifikasi dokumentasi terkini dan konfigurasi aktual. Bila library menangani Authorization Code/PKCE, biarkan library mengelola `state`, verifier/challenge, callback, dan pertukaran code sesuai panduan resminya.

## Verifikasi data aktual
Untuk setiap dari tiga sumber, catat service, ID/URL, pemilik, field yang diperlukan, izin akun, endpoint/API, dan hasil uji read-only. Validasi folder/file listing, thumbnail, dan link Drive dari respons nyata; jangan anggap `thumbnailLink` atau akses berkas selalu tersedia. Konfirmasi spreadsheet ID/range/schema sebelum mengaktifkan Sheets API.

## Peninjauan timses
Security Architect dan Identity & Access Engineer memberi temuan desain: scope masih belum tervalidasi, refresh token/lifecycle belum dipastikan, serta redirect URI dan metode penyimpanan tidak boleh diasumsikan. Temuan ini dimasukkan ke revisi dokumen. Rea dijalankan dengan `rea doctor`, hasil aktual menunjukkan environment tidak sehat pada host Alpine yang tidak didukung; tidak ada perintah analisis APK yang dijalankan.
