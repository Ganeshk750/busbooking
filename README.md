---
title: BusBookingAPI Documentation
description: API documentation for BusBooking system
date: 2025-09-05
---
# BusBookingAPI

This repository contains the Postman collection for the Bus Booking API.

## API Endpoints


## 🔐 Authentication Notes
Currently, the API does not specify authentication headers in the Postman collection. If authentication is required, consider using JWT tokens or API keys in the request headers.
Example header:
```http
Authorization: Bearer <your_token_here>
```

## ⚠️ Error Handling Guidelines
All endpoints should return appropriate HTTP status codes:
- `200 OK`: Successful request
- `400 Bad Request`: Invalid input or missing parameters
- `401 Unauthorized`: Authentication failed
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server-side error

## 📦 Sample Response Format
```json
{
  "status": "success",
  "data": { /* response data */ },
  "message": "Operation completed successfully."
}
```

## 👥 Role-Based Access Control
Endpoints are categorized based on user roles:

### USER Endpoints
- `POST /api/v1/user/register`
- `POST /api/v1/user/login`
- `GET /api/v1/buses/search`
- `POST /api/v1/bookings`
- `POST /api/v1/bookings/history`
- `PUT /api/v1/bookings/{bookingId}/cancel`

### ADMIN Endpoints
- `POST /api/v1/admin/buses`
- `GET /api/v1/admin/booking/history`
- `PUT /api/v1/admin/bookings/{bookingId}/cancel`

Ensure that appropriate authentication and authorization mechanisms are in place to restrict access based on roles.
