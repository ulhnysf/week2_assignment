# DevCluster — Donanım Kaynak Yönetim Simülatörü

Mobil Sistemlerde Uygulama Geliştirme dersi kapsamında Kotlin ve
Jetpack Compose kullanılarak geliştirilmiş Android uygulamasıdır.

Uygulama, sabit bir donanım veri kümesi üzerinde arama, kategori
filtreleme, kaynak ayırma ve toplam harcama takibi sunar.
İşlemler simülasyon amaçlıdır; gerçek satın alma yapılmaz.

## Özellikler

- 8 donanımın kaydırılabilir kartlarla listelenmesi
- Donanım adı ve kategoriye göre arama
- Aramayla birlikte çalışan kategori filtresi
- Karta dokunarak bir adet kaynak ayırma
- Kaynak ayırıldığında stok ve toplam harcamanın güncellenmesi
- Stoku tükenen donanımlarda işlemin engellenmesi
- Ekran döndürüldüğünde arama, kategori ve stok bilgilerinin korunması

## Kullanılan Teknolojiler ve Yapılar

- Kotlin
- Jetpack Compose ve Material 3
- Değişmez veri modeli: `@Immutable`, `data class` ve `val`
- Durum yönetimi: `remember` ve `rememberSaveable`
- Filtre sonuçlarının `remember` anahtarlarıyla önbelleğe alınması
- `LazyColumn`, `LazyRow` ve benzersiz liste anahtarları
- State hoisting ve lambda callbacks
- Stateless arayüz bileşenleri

## Çalıştırma

1. Bu depoyu bilgisayarınıza klonlayın veya ZIP olarak indirin.
2. Proje klasörünü Android Studio ile açın.
3. Gradle senkronizasyonunun tamamlanmasını bekleyin.
4. Uyumlu bir Android emülatörü veya gerçek cihaz seçin.
5. `app` çalıştırma yapılandırmasını seçip Run düğmesine basın.

İlk kurulumda gerekli bağımlılıkların indirilmesi için internet gerekir.

## Kullanım

Arama alanına bir donanım adı veya kategori yazın.
Kategori seçeneklerini yatay kaydırarak filtreyi değiştirebilirsiniz.
Stokta bulunan bir donanımın kartına dokunarak bir adet kaynak ayırabilirsiniz.

## Ana Kaynak Dosyası

[MainActivity.kt](app/src/main/java/com/example/week2/MainActivity.kt)

## Veri Saklama

Uygulama veritabanı veya sunucu kullanmaz.
`rememberSaveable`, ekran döndürme gibi durumlarda ekran durumunu korur;
kalıcı veri depolama sağlamaz.
