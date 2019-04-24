# Tugas Besar 1 IF3230 Sistem Paralel dan Terdistribusi

## Peer-to-Peer Collaborative Editing menggunakan Conflict-free Replicated Data Type (CRDT)



#### Kelompok 4

13516005 - Rizki Alif Salman A.

13516089 - Priagung S.

13516140 - Ilham Firdausi P.

---
## Petunjuk penggunaan :
1. Jalankan program *signal server* pada ip address dan host yang diinginkan pada Signal.java
2. Jalankan program *text editor*, window *text editor* akan terbuka
3. Masukkan host address pada *text field* di bawah dengan format "[ip address]:[port]", misal "192.168.43.49:8885". 
4. Setelah itu, tekan tombol *connect* untuk memulai koneksi ke *signal server*. *signal server* akan secara otomatis memberitahukan host-host lainnya yang sedang terhubung ke *signal server*. Program akan menghubungkan perangkat ke host-host tersebut secara otomatis.
5. Program *collaborative text-editor* sudah dapat digunakan.
---
## Worklog
##### 13516005 - Rizki Alif Salman Alfarisy
* GUI
* Integrasi GUI dengan controller
* Laporan

##### 13516089 - Priagung Satygama
* CRDT
* Socket
* Version Vector
* Deletion Buffer

##### 13516140 - Ilham Firdausi Putra
* Signal Server
* Version Vector
* Deletion Buffer

---
## Laporan
### Cara Kerja Program
TBD

### Arsitektur Program
Program ini secara garis besar terdiri dari:
* Controller (Controller.java) - bagian utama dari program yang berfungsi untuk mengatur jalannya program.
* Conflict-free Replicated Data Types (CRDT.java) - data struktur utama yang mendasari cara kerja program ini. Data struktur CRDT ini telah memungkinkan kami untuk mengembangkan sebuah platform penyunting teks peer-to-peer.
* Model (Character.java, LocalCharacter.java, Position.java) - tipe dasar data yang kami gunakan dalam berkomunikasi antar node dalam koneksi.
* Editor (Editor.java) - graphical user interface (GUI) yang kami gunakan untuk berinteraksi dengan program.
* Messenger (Messenger.java) - bagian program yang berfungsi untuk berkomunikasi dengan node lain dalam koneksi.
* Version Vector (VersionVector.java) - bagian program yang berfungsi untuk mencatat versi karakter yang sudah diketik agar terhindar dari kasus di mana operasi delete dijalankan pada karakter yang belum ada.
* Signal (Signal.java) - bagian program yang berfungsi untuk melakukan inisialisasi koneksi kepada seluruh node yang ada pada koneksi.

<p align="center"> <img src="doc/arsitektur.png" alt="arsitektur" width="600"/> </p>

### Struktur Data
TBD

### Analisis Solusi
TBD

### Kasus Uji
1. Input karakter
2. Delete karakter
3. Input karakter dari 2 node berbeda secara bersamaan
4. Delete karakter dari 2 node berbeda secara bersamaan (Tes Idempoten)
5. Input karakter dan delete karakter pada indeks yang sama dari 2 node berbeda (Tes Komutatif)
6. Delete karakter pada node di mana karakter tersebut belum sampai (Tes Delete Buffer)

### Screenshot/Video Program
TBD

---
# Thank You