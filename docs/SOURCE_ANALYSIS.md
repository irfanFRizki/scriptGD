# Analisis Source GAS (scriptGD)

## File sumber
- `in/Code.gs` (GAS server-side)
- `in/Index.html` (GAS client-side, di-serve via HtmlService)

## Perilaku aktual
### Code.gs
- `doGet()`: menyajikan `Index` sebagai Web App (`HtmlService.createHtmlOutputFromFile('Index')`, title "Lukisanku", meta viewport).
- `uploadImage(base64Data, fileName, mimeType)`:
  - mencari/membuat folder Drive bernama **`Lukisanku`**
  - decode base64 -> Blob, nama unik `Foto_<timestamp>_<fileName>`
  - `folder.createFile(blob)` lalu mengembalikan `file.getUrl()`
  - tidak ada akses Google Sheets, tidak ada listing/metadata terstruktur.

### Index.html
- UI upload foto tunggal (Tailwind CDN) + galeri gambar yang baru diunggah pada sesi saat ini.
- Upload: `FileReader` -> base64 -> `google.script.run.uploadImage(...)`.
- Sukses: konversi URL Drive `/file/d/<id>/view` ke `https://drive.google.com/uc?export=view&id=<id>` dan prepend ke grid.
- Fallback simulasi bila `google.script.run` tidak tersedia.
- Tidak ada login Google di klien (mengandalkan konteks eksekusi GAS), tidak ada pembacaan Drive/Sheets.

## Implikasi untuk APK (dashboard berbeda)
- Sumber GAS dipakai sebagai referensi perilaku upload/penyimpanan Drive, **bukan** UI yang disalin.
- Kebutuhan baru yang harus dirancang/diimplementasi:
  - Login akun Google di Android (OAuth/native flow, bukan WebView login Google).
  - Akses Drive & Sheets sesuai akun login (scope minimal, consent eksplisit).
  - Model data dashboard (mis. metadata di Google Sheets: id, nama, driveFileId/url, timestamp, kategori/status) — **belum ada di source saat ini**.
  - UI dashboard Android baru (terpisah dari Index.html).
- Keamanan: token/secret tidak di-APK/repo; validasi izin & skema data dari sumber nyata sebelum tulis data.

## Status build lokal
- Lingkungan saat ini: tanpa Java/Gradle/Android SDK (`ANDROID_HOME` kosong) -> build APK lokal belum dapat diverifikasi di sini.
- Rencana: siapkan scaffold proyek Android + workflow CI (lokal, tanpa push) agar build dapat dijalankan di GitHub Actions saat disepakati.
