# 📄 **Currency API Documentation**

## Base URL

```
http://localhost:8080/api/currency
```

---

# 🚀 Endpoints

---

## 1. `POST /exchange-rate`

**Description**:  
Retrieve the current exchange rate between two currencies.

### Request Body:

```json
{
  "sourceCurrency": "EUR",
  "targetCurrency": "TRY"
}
```

### Response:

```json
{
  "sourceCurrency": "EUR",
  "targetCurrency": "TRY",
  "rate": 34.567
}
```

### Possible Status Codes:
- `200 OK` – Successful retrieval
- `400 Bad Request` – Validation error
- `500 Internal Server Error` – Server failure

---

## 2. `POST /convert`

**Description**:  
Convert a specified amount from a source currency to a target currency.

### Request Body:

```json
{
  "amount": 100,
  "sourceCurrency": "EUR",
  "targetCurrency": "JPY"
}
```

### Response:

```json
{
  "transactionId": "b1234cd56ef",
  "convertedAmount": 15900.00
}
```

### Possible Status Codes:
- `200 OK` – Successful conversion
- `400 Bad Request` – Invalid request payload
- `500 Internal Server Error` – Conversion error

---

## 3. `GET /conversion-history`

**Description**:  
Retrieve historical conversion transactions, filtered optionally by:
- `transactionId`
- `date` (format: `yyyy-MM-dd`)

### Query Parameters:
| Parameter       | Type        | Required | Example          | Description                            |
|:----------------|:------------|:---------|:-----------------|:---------------------------------------|
| transactionId   | String      | Optional | b1234cd56ef       | Filter by transaction ID              |
| date            | LocalDate   | Optional | 2025-04-27        | Filter by conversion date (yyyy-MM-dd) |
| page            | Integer     | Optional | 0                 | Page number (default: 0)               |
| size            | Integer     | Optional | 10                | Page size (default: 10)                |

### Response (paged):

```json
{
  "content": [
    {
      "transactionId": "b1234cd56ef",
      "convertedAmount": 15900.00
    }
  ],
  "pageable": {...},
  "totalPages": 1,
  "totalElements": 1,
  ...
}
```

---

## 4. `POST /bulk-upload`

**Description**:  
Perform multiple currency conversions by sending a batch of conversion requests.

### Request Body:

```json
{
  "conversions": [
    {
      "amount": 100,
      "sourceCurrency": "EUR",
      "targetCurrency": "TRY"
    },
    {
      "amount": 50,
      "sourceCurrency": "EUR",
      "targetCurrency": "JPY"
    }
  ]
}
```

### Response:

```json
{
  "conversions": [
    {
      "transactionId": "trx001",
      "convertedAmount": 3450.00
    },
    {
      "transactionId": "trx002",
      "convertedAmount": 7950.00
    }
  ]
}
```

### Notes:
- The server processes **each item one by one**.
- Only `sourceCurrency = EUR` is allowed for now (due to API restrictions).

---

## 5. `POST /bulk-upload-csv`

**Description**:  
Upload a **CSV file** containing multiple conversion requests.

### CSV Format:
| amount | sourceCurrency | targetCurrency |
|:------:|:--------------:|:--------------:|
| 100    | EUR            | TRY            |
| 200    | EUR            | USD            |

- **Headers must exist** exactly like: `amount`, `sourceCurrency`, `targetCurrency`.
- **Only `EUR` source currency** is supported currently.

### Response:

Same as `/bulk-upload`, returns a list of transactions:

```json
{
  "conversions": [
    {
      "transactionId": "trx003",
      "convertedAmount": 5000.00
    }
  ]
}
```

---

# 📦 Error Responses (Common)

| Status Code | Message Example                           | Cause                         |
|:-----------:|:-----------------------------------------|:------------------------------|
| 400         | "Validation failed"                      | Missing required fields       |
| 401         | "Invalid API key"                         | Wrong external API key         |
| 500         | "Internal server error" / "Invalid API response" | External API or server failure |

---

# Important Notes
- Dates must be provided in **`yyyy-MM-dd`** format.
- If using CSV upload, **only valid files** (UTF-8 encoded) are accepted.
- **Thread.sleep(1500)** is added intentionally for slow bulk simulation.
- Exchange rates are **fetched live** from **Fixer.io** (limited to EUR-based conversions on free plan).
Just say: `Prepare Swagger block`.
