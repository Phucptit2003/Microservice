# Payment Service

Dịch vụ này sử dụng  Stripe để xử lý thanh toán cho các đơn hàng. Nó cho phép tạo phiên thanh toán, xử lý webhook và theo dõi trạng thái thanh toán.

## Features

- Tạo phiên thanh toán
- Xử lý webhook từ Stripe
- Theo dõi trạng thái thanh toán

## Kiểm thử với Postman

### 1.  Tạo phiên thanh toán

**Endpoint**: `POST /api/payments/create-checkout-session`

**Request Body**:
```json
{
  "orderId": "order-123",
  "amount": 29.99,
  "currency": "usd",
  "description": "Purchase of Movie Tickets",
  "userId": "user-456",
  "successUrl": "http://localhost:9090/payment/success?session_id={CHECKOUT_SESSION_ID}",
  "cancelUrl": "http://localhost:9090/payment/cancel",
  "customerEmail": "customer@example.com"
}
```

**Note**: The `successUrl` and `cancelUrl` trỏ tới trang thành công và thất bại đã được cài định .

**Response**:
```json
{
  "paymentId": "cs_test_a1b2c3d4e5f6g7h8i9j0",
  "orderId": "order-123",
  "amount": 29.99,
  "currency": "usd",
  "status": "PENDING",
  "checkoutUrl": "https://checkout.stripe.com/pay/cs_test_a1b2c3d4e5f6g7h8i9j0",
  "sessionId": "cs_test_a1b2c3d4e5f6g7h8i9j0",
  "createdAt": "2023-07-01T12:34:56",
  "message": "Payment session created successfully"
}
```

### 2. Hoàn tất thanh toán

1. Truy cập url  `checkoutUrl` 
2. Nhập thông tin thanh toán:
   - Card number: 4242 4242 4242 4242
   - Expiry date: Bất kỳ tháng/năm trong tương lai
   - CVC: Any 3 digits
   - Name: Bất 
3. Nhấn nút "Pay" để hoàn tất thanh toán
   - Nếu thanh toán thành công, bạn sẽ được chuyển hướng đến trang thành công
   - Nếu thanh toán thất bại, bạn sẽ được chuyển hướng đến trang hủy

### 3. Lấy thông tin thanh toán

**Endpoint**: `GET /api/payments/session/{sessionId}`

Thay `{sessionId}` bằng ID phiên thanh toán mà bạn đã nhận được khi tạo phiên thanh toán.

**Response**:
```json
{
  "paymentId": "cs_test_a1b2c3d4e5f6g7h8i9j0",
  "orderId": "order-123",
  "amount": 29.99,
  "currency": "usd",
  "status": "COMPLETED",
  "createdAt": "2023-07-01T12:34:56",
  "message": "Payment retrieved successfully"
}
```

## Tích hợp Webhook Stripe

### Cấu hình Webhook

1. **Thiết lập Webhook trên Stripe Dashboard:**
   - Đăng nhập vào [Stripe Dashboard](https://dashboard.stripe.com)
   - Vào mục Developers > Webhooks
   - Click "Add endpoint"
   - Nhập URL webhook của bạn: `https://your-domain/api/payments/webhook`
   - Chọn sự kiện cần lắng nghe: `checkout.session.completed`
   - Lưu lại và lấy Webhook Signing Secret

2. **Cấu hình trong ứng dụng:**
   ```properties
   # application.properties hoặc application.yml
   stripe.webhook.secret=whsec_your_webhook_signing_secret
   ```

### Quy trình xử lý Webhook

1. **Nhận Webhook:**
   - Service sẽ nhận POST request từ Stripe tại endpoint `/api/payments/webhook`
   - Mỗi request đều chứa signature header để xác thực

2. **Xác thực Webhook:**
   ```java
   Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
   ```
   - Kiểm tra signature để đảm bảo request đến từ Stripe
   - Ngăn chặn các request giả mạo

3. **Xử lý sự kiện:**
   - Khi nhận sự kiện `checkout.session.completed`:
     - Kiểm tra trạng thái thanh toán (`payment_status = "paid"`)
     - Kiểm tra trạng thái phiên (`status = "complete"`)
     - Cập nhật trạng thái thanh toán trong database
     - Lưu phương thức thanh toán được sử dụng

4. **Cập nhật Database:**
   - Trạng thái thanh toán được cập nhật thành `COMPLETED`
   - Thời gian cập nhật được ghi lại
   - Phương thức thanh toán được lưu (nếu có)

### Logging và Debugging

- Tất cả các sự kiện webhook đều được ghi log
- Các lỗi xác thực hoặc xử lý được ghi lại với chi tiết đầy đủ
- Có thể theo dõi quá trình xử lý qua các log:
  ```
  INFO: Received webhook from Stripe
  INFO: Payment completed for session: {session_id}
  WARN: Payment not marked as paid or session not complete
  ERROR: Invalid signature
  ```

### Xử lý Lỗi

1. **Lỗi Xác thực:**
   - Nếu signature không hợp lệ: HTTP 400 Bad Request
   - Log chi tiết lỗi xác thực

2. **Lỗi Xử lý:**
   - Nếu không tìm thấy payment: HTTP 404 Not Found
   - Lỗi database: HTTP 500 Internal Server Error
   - Tất cả lỗi đều được ghi log đầy đủ

### Kiểm tra Webhook

1. **Sử dụng Stripe CLI:**
   ```bash
   ./stripe listen --forward-to localhost:9090/api/payments/webhook
   ```
### Ví dụ Payload Webhook

```json
{
  "id": "evt_xxx",
  "type": "checkout.session.completed",
  "data": {
    "object": {
      "id": "cs_test_xxx",
      "payment_status": "paid",
      "status": "complete",
      "payment_method_types": ["card"],
      "metadata": {
        "orderId": "order-123",
        "userId": "user-456"
      }
    }
  }
}
```




