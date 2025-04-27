# 💱 Exchange Currency API

A Spring Boot REST API for currency exchange and conversion operations.  
Real-time data is fetched from Fixer.io API.

---

## 🛠 Technologies

- Java 17
- Spring Boot 3
- H2 Database
- OpenAPI 3 (Swagger UI)
- Docker
- Apache Commons CSV
- Maven
- Lombok

---

## 🚀 API Endpoints

| Method | Endpoint | Description |
|:---|:---|:---|
| POST | `/api/currency/exchange-rate` | Get real-time exchange rate |
| POST | `/api/currency/convert` | Convert currency amount |
| GET  | `/api/currency/conversion-history` | View conversion history |
| POST | `/api/currency/bulk-upload` | Bulk currency conversion (JSON) |
| POST | `/api/currency/bulk-upload-csv` | Bulk conversion via CSV upload |

---

## 📦 Example Requests

### `/api/currency/exchange-rate`
```json
{
  "sourceCurrency": "EUR",
  "targetCurrency": "TRY"
}
```

### `/api/currency/convert`
```json
{
  "amount": 100,
  "sourceCurrency": "EUR",
  "targetCurrency": "TRY"
}
```

### `/api/currency/bulk-upload`
```json
{
  "conversions": [
    {
      "amount": 100,
      "sourceCurrency": "EUR",
      "targetCurrency": "JPY"
    },
    {
      "amount": 200,
      "sourceCurrency": "EUR",
      "targetCurrency": "TRY"
    }
  ]
}
```

### `/api/currency/bulk-upload-csv`
CSV Format:
```
amount,sourceCurrency,targetCurrency
100,EUR,JPY
200,EUR,TRY
```

---

## 📝 Notes

- Only **EUR** is allowed as the base currency with free Fixer.io API.
- All dates must follow `yyyy-MM-dd` format (e.g., `2025-04-27`).
- Error handling and validations are implemented.

---

## 📄 Swagger UI

Access Swagger UI at:

> [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🐳 Docker Support

### Build and Run with Docker:

```bash
docker build -t exchange-currency-api .
docker run -p 8080:8080 exchange-currency-api
```

---

## 🧪 Testing

- JUnit 5 + Mockito tests.
- Coverage includes: exchange rate, conversion, and bulk upload.

---

## ✨ Author

- Berke Bayrakçı
- GitHub: [berkebayrakci](https://github.com/berkebayrakci)
