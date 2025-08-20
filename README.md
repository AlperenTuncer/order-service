# Order Service

Order Service, mikroservis tabanlı bir e-ticaret sisteminin sipariş yönetim bileşenidir.  
Java 21 ve Spring Boot 3.5.4 kullanılarak geliştirilmiştir.

Servis, **SOLID prensiplerine** uygun katmanlı mimariyle tasarlanmış, test odaklı geliştirilmiş ve ileride **Saga Pattern** ile Stock Service ve Payment Service ile entegre edilmek üzere yapılandırılmıştır.

---

## 🚀 Özellikler

- Sipariş oluşturma, görüntüleme ve iptal etme (CRUD)
- H2 (in-memory) veritabanı desteği (geliştirme için)
- PostgreSQL uyumluluğu (prod ortamına geçiş için)
- Bean Validation ile DTO seviyesinde doğrulama
- Global Exception Handler ile tutarlı hata yanıtları
- Katmanlı mimari:
  - **Domain** (iş kuralları, invariants)
  - **Repository** (arayüz)
  - **Persistence** (JPA adaptörleri, mapping)
  - **Service** (use-case mantığı)
  - **API/Controller** (REST endpoint’leri)
- Unit, Slice ve Integration testleri (JUnit 5, Mockito, Spring Boot Test)

---

## 🛠 Kullanılan Teknolojiler

- Java 21
- Spring Boot 3.5.4
- Spring Web
- Spring Data JPA
- H2 Database
- Lombok
- Jakarta Bean Validation
- JUnit 5, Mockito, AssertJ
- Spring Boot Actuator

---

## 📂 Proje Yapısı

```text
src/main/java/com/alperen/order
 ├── api/             # DTO'lar, Mapper'lar, Exception Handler
 ├── controller/      # REST endpoint'leri
 ├── domain/          # İş kuralları ve domain modelleri
 ├── repository/      # Repository arayüzleri
 ├── persistence/     # JPA entity, mapper ve adaptörler
 ├── service/         # İş mantığı (OrderService interface & impl)
 └── OrderServiceApplication.java
```

---

## ⚙️ Çalıştırma

### 1. Bağımlılıkları indir

```bash
mvn clean install
```

### 2. Geliştirme profili ile çalıştır

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

> `application-dev.yml` içinde H2 veritabanı ve `server.port: 8081` ayarları bulunur.

### 3. Prod (PostgreSQL) için

`application-prod.yml` içinde PostgreSQL ayarlarını yap ve:

```bash
java -jar target/order-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

---

## 📡 API Endpoint’leri

### 1. Sipariş Oluştur

**POST** `/api/v1/orders`

```json
{
  "customerId": "11111111-1111-1111-1111-111111111111",
  "items": [
    {
      "productId": "22222222-2222-2222-2222-222222222222",
      "quantity": 2,
      "unitPrice": 120.5
    }
  ]
}
```

Yanıt (201 Created):

```json
{
  "id": "33333333-3333-3333-3333-333333333333",
  "customerId": "11111111-1111-1111-1111-111111111111",
  "status": "CREATED",
  "totalAmount": 241.0,
  "items": [
    {
      "productId": "22222222-2222-2222-2222-222222222222",
      "quantity": 2,
      "unitPrice": 120.5,
      "lineTotal": 241.0
    }
  ]
}
```

### 2. Sipariş Görüntüle

**GET** `/api/v1/orders/{id}`

### 3. Sipariş İptal

**POST** `/api/v1/orders/{id}/cancel`

---

## 🧪 Testler

Testler Maven üzerinden çalıştırılır:

```bash
mvn test
```

Test katmanları:

- **Domain Unit Test**: İş kuralları (Order, OrderItem)
- **Service Unit Test**: `OrderServiceImpl` (Mockito ile repository mock)
- **Repository Slice Test**: JPA mapping & H2 testleri
- **Controller Test**: @WebMvcTest ile API + Validasyon
- **Integration Test**: @SpringBootTest ile uçtan uca akış

---

## 📈 Geliştirme Planı

- [x] Order Service (H2 + CRUD)
- [ ] Stock Service (REST tabanlı prototip)
- [ ] Payment Service (REST tabanlı prototip)
- [ ] Saga Pattern (RabbitMQ üzerinden event-driven)
- [ ] Redis (idempotency, cache, kısa süreli state)
- [ ] Spring Cloud (Config Server, Gateway, Circuit Breaker)

---

## 👤 Yazar

**Alperen Tuncer**  
Backend Developer

---

## 📜 Lisans

MIT License. Bu proje özgürce kullanılabilir ve geliştirilebilir.
