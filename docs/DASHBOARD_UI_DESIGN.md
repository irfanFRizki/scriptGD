# Rancangan awal UI Dashboard APK — scriptGD

## Prinsip
Dashboard Kotlin + Jetpack Compose akan berbeda dari HTML GAS. Namun layar dan data final menunggu identifikasi tiga sumber; daftar fitur berikut kandidat, belum dipastikan tersedia.

## Kandidat layar
1. **LoginScreen** — tombol masuk Google; jelaskan akses yang diminta setelah scope dan sumber data dikonfirmasi.
2. **DashboardScreen** — ringkasan gabungan tiga sumber, hanya setelah endpoint dan field yang dibutuhkan tersedia serta diuji.
3. **Source detail** — data per sumber, menampilkan loading, empty, error dan last updated state.
4. **SettingsScreen** — akun yang terhubung, kelola akses/logout, info versi.

## Opsi UI awal
- Material 3, Navigation Compose, tema terang/gelap mengikuti sistem.
- App bar berisi nama aplikasi dan status akun.
- Kartu ringkasan dan daftar data adaptif terhadap jenis sumber yang telah diverifikasi.
- Loading indicator, empty state, error state dengan coba lagi, serta pull-to-refresh jika API mendukung.
- Aksesibilitas: target sentuh minimal 48 dp, label deskriptif untuk ikon, kontras memadai.

## Yang belum ditetapkan
Belum dapat menjanjikan grid foto Drive, tab Spreadsheet, thumbnail, atau tombol unduh sebelum memastikan tiga sumber itu memang Drive/Sheets dan API mengembalikan data yang dibutuhkan. Source GAS lama hanya membuktikan upload gambar, bukan listing/galeri persisten.
