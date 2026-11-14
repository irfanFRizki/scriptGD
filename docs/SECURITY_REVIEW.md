# Threat Model & Security Review — scriptGD APK (OAuth Google Drive/Sheets)

**Tanggal:** 2026-11-14
**Status:** Threat model ringan + security review (berbasis desain; APK belum dibangun)
**Sumber:** `docs/OAUTH_SECURITY.md`, `docs/ARCHITECTURE.md`

## 1. Ruang lingkup

Aplikasi Android (APK) dashboard yang mengakses Google Drive dan Google Sheets via Google APIs OAuth 2.0 atas nama akun Google pengguna. Backend Google Apps Script (`code.gs`) hanya menjadi referensi perilaku backend; data diambil langsung dari akun pengguna via API.

## 2. Boundary kepercayaan (Trust Boundaries)

| Boundary | Kendali keamanan yang direkomendasikan |
|----------|----------------------------------------|
| Internet → APK (OAuth flow) | TLS 1.2+, PKCE, Custom Tabs (bukan WebView) |
| APK → Google OAuth endpoints (`oauth2.googleapis.com`) | PKCE, HTTPS, code exchange single-use |
| APK → Google Drive/Sheets API | Bearer token, auto-refresh on 401, scope minimum readonly |
| APK token store → OS (Android Keystore) | AES-256-GCM, key diikat StrongBox/user-auth |
| Pengguna ↔ APK | Screen lock / biometric opsional |

## 3. Threat model ringan (Threat List)

| ID | Ancaman | Boundary | Severity | Mitigasi existing | Rekomendasi mitigasi |
|----|---------|----------|----------|-------------------|----------------------|
| T-01 | Token disimpan inkonsisten/tidak terenkripsi | Storage | Medium | DataStore Preferences + EncryptedSharedPreferences | **Unifikasi**: Android Keystore (Tink AES-256-GCM) + DataStore ciphertext; elarilah `EncryptedSharedPreferences` (deprecated di androidx.security) |
| T-02 | Token bocor bila perangkat diakses tanpa kunci/keamanan device | Storage → OS | Medium | Enkripsi token + screen lock | Bind key Android Keystore ke user-authentication (StrongBox); wajib screen lock device |
| T-03 | Cleartext / MITM / TLS lemah | Jaringan | High | HTTPS + Network Security Config | Pastikan `cleartextTraffic=false`, TLS 1.2+, cipher suite kuat; jangan percaya default OkHttp sepenuhnya |
| T-04 | Token tersedot ke log jaringan/error | Jaringan / Logging | Medium | Hapus Authorization header dari log, HTTPS saja | Release logging disabled, redaction token, generic error response (no stack trace) |
| T-05 | Redirect URI / custom scheme salah skema | Auth flow | High | `com.scriptgd.app:/oauth2redirect` | Wajib menggunakan reverse client ID Google: `com.googleusercontent.apps.<CLIENT_ID>:/oauth2redirect` |
| T-06 | Refresh token tidak issued / tidak terkelola | Auth flow | Medium | Logout hapus token | Pakai `access_type=offline` + `prompt=consent`; revoke via Google revoke endpoint; tangani refresh token expiry (7 hari di mode Testing) |
| T-07 | Alur auth lewat WebView / consent tidak eksplisit | Auth flow | Medium | Custom Tabs + PKCE + scope min | Pertahankan Custom Tabs; gunakan AppAuth/Credential Manager; konfirmasi scope sebelum consent |
| T-08 | APK decompile → token/secret tersedot | APK | High | Tidak ada client_secret, obfuscasi ProGuard/R8 | R8/ProGuard release minify+obfuscate; Play App Signing; Play Integrity API; hapus secrets dari semua tempat |
| T-09 | Backup/restore → token ikut tersimpan | Storage | Medium | EncryptedSharedPreferences/DataStore | `android:allowBackup=false` atau auto-restore terenkripsi; token tidak pernah plaintext di backup |
| T-10 | IDOR / akses data tidak berhak | API / Authz | Medium | Readonly scopes | Server-side enforcement Google; APK wajib cek kepemilikan folder sebelum aksi baca/tulis |
| T-11 | Auto-refresh loop / 401 tidak terkelola | API / Network | Medium | Interceptor refresh on 401 | Batasi retry, logout + revoke on refresh failure |

## 4. Ringkasan risiko prioritas

1. **High** — Konfigurasi OAuth client (redirect URI, offline access) dan konsistensi pengetahuan token storage.
2. **High** — APK tidak boleh menyimpan secret; desain saat ini sudah benar (tanpa client_secret), tapi wajib build release dengan R8/ProGuard + Play App Signing.
3. **Medium** — TLS/cleartext, logging token, error handling.
4. **Medium** — Refresh token lifecycle dan revocation.

## 5. Checklist keamanan untuk implementasi (dari framework Security Architect)

- [ ] **Auth**: PKCE tervalidasi (signature, expiry, issuer/audience); larang `alg=none`; MFA/ekspektasi sesuai kebutuhan.
- [ ] **Authz**: enforcment server-side oleh Google; validasi ownership lokal sebelum aksi.
- [ ] **Input**: validasi error response dari Google; generic messages (no stack trace).
- [ ] **Network**: HTTPS/TLS 1.2+, no cleartext, rate limit (Google-side; app layer untuk auth).
- [ ] **Error handling**: fail securely; no token di response/error.
- [ ] **Session**: logout → hapus token + revoke; auto-refresh on 401 dengan fallback logout.
- [ ] **Business logic**: TOCTOU tidak relevan (read-only), tapi pastikan tidak ada workflow bypass.

## 6. Rekomendasi mitigasi akhir (prioritas)

| Prioritas | Rekomendasi |
|-----------|-------------|
| P0 | Unifikasi token storage ke Android Keystore + Tink (AES-256-GCM) + DataStore; bind StrongBox/user-auth; backup handling aman. |
| P0 | Konfigurasi OAuth client Google: type `android`, redirect URI `com.googleusercontent.apps.<CLIENT_ID>:/oauth2redirect`; wajib `access_type=offline` + `prompt=consent`. |
| P1 | Network Security Config (cleartext diblokir, TLS min 1.2), logging token dimatikan di release, error response generic. |
| P1 | Auto-refresh token + logout/revoke flow yang benar; guard terhadap refresh failure. |
| P2 | Build release dengan R8/ProGuard + obfuscation; Play App Signing + Play Integrity; review ulang scope sebelum fitur upload (`drive.file`) ditambahkan. |

## 7. Status verifikasi

Threat model ini bersifat **ringan (design-level)**. Belum ada APK yang dibangun di lingkungan ini (tanpa Java/Gradle/Android SDK), sehingga semua mitigasi di atas adalah rekomendasi desain yang harus di-verify setelah build nyata: threat model update, security checklist, dan pengujian autentikasi/refresh/revoke.
