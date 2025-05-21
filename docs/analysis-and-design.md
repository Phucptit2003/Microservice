# Phân Tích và Thiết Kế Hệ Thống

## 1. Phân Tích Yêu Cầu

### 1.1 Yêu Cầu Chức Năng

#### Quản Lý Người Dùng
- Đăng ký tài khoản mới
- Đăng nhập/Đăng xuất
- Quản lý thông tin cá nhân
- Xem lịch sử đặt vé

#### Quản Lý Phim
- Thêm/sửa/xóa thông tin phim
- Quản lý danh mục phim
- Tìm kiếm phim
- Đánh giá và bình luận

#### Quản Lý Suất Chiếu
- Tạo lịch chiếu mới
- Quản lý rạp và phòng chiếu
- Cập nhật trạng thái suất chiếu

#### Quản Lý Ghế
- Hiển thị sơ đồ ghế
- Đặt/hủy đặt ghế
- Kiểm tra trạng thái ghế

#### Đặt Vé
- Chọn phim và suất chiếu
- Chọn ghế ngồi
- Thanh toán
- Nhận vé điện tử

#### Thanh Toán
- Tích hợp nhiều phương thức thanh toán
- Xử lý giao dịch an toàn
- Quản lý hoàn tiền

#### Thông Báo
- Gửi email xác nhận đặt vé
- Thông báo thay đổi lịch chiếu
- Thông báo khuyến mãi

### 1.2 Yêu Cầu Phi Chức Năng
- Bảo mật cao
- Khả năng mở rộng tốt
- Hiệu năng nhanh
- Sẵn sàng cao
- Dễ bảo trì

## 2. Thiết Kế Hệ Thống

### 2.1 Thiết Kế Cơ Sở Dữ Liệu

#### User Service DB
```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    email VARCHAR(100) UNIQUE,
    password_hash VARCHAR(255),
    role VARCHAR(20),
    created_at TIMESTAMP
);
```

#### Movie Service DB
```sql
CREATE TABLE movies (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    duration INT,
    release_date DATE,
    genre VARCHAR(50),
    rating DECIMAL
);
```

#### Showtime Service DB
```sql
CREATE TABLE showtimes (
    id SERIAL PRIMARY KEY,
    movie_id INT,
    theater_id INT,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    price DECIMAL
);
```

#### Seat Service DB
```sql
CREATE TABLE seats (
    id SERIAL PRIMARY KEY,
    theater_id INT,
    row_number VARCHAR(5),
    seat_number VARCHAR(5),
    status VARCHAR(20)
);
```

#### Booking Service DB
```sql
CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    user_id INT,
    showtime_id INT,
    seat_id INT,
    status VARCHAR(20),
    created_at TIMESTAMP
);
```

#### Payment Service DB
```sql
CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    booking_id INT,
    amount DECIMAL,
    payment_method VARCHAR(50),
    status VARCHAR(20),
    transaction_id VARCHAR(100)
);
```

### 2.2 API Endpoints

#### User Service
- POST /api/users/register
- POST /api/users/login
- GET /api/users/profile
- PUT /api/users/profile

#### Movie Service
- GET /api/movies
- GET /api/movies/{id}
- POST /api/movies
- PUT /api/movies/{id}
- DELETE /api/movies/{id}

#### Showtime Service
- GET /api/showtimes
- GET /api/showtimes/{id}
- POST /api/showtimes
- PUT /api/showtimes/{id}

#### Seat Service
- GET /api/seats/theater/{theaterId}
- POST /api/seats/reserve
- POST /api/seats/release

#### Booking Service
- POST /api/bookings
- GET /api/bookings/{id}
- GET /api/bookings/user/{userId}

#### Payment Service
- POST /api/payments
- GET /api/payments/{id}
- POST /api/payments/refund

#### Notification Service
- POST /api/notifications/email
- POST /api/notifications/sms

### 2.3 Luồng Xử Lý

#### Quy Trình Đặt Vé
1. Người dùng chọn phim và suất chiếu
2. Kiểm tra và đặt ghế
3. Tạo đơn đặt vé
4. Xử lý thanh toán
5. Gửi xác nhận qua email

#### Quy Trình Hoàn Tiền
1. Yêu cầu hoàn tiền
2. Kiểm tra điều kiện
3. Xử lý hoàn tiền
4. Cập nhật trạng thái đặt vé
5. Gửi thông báo

## 3. Kế Hoạch Triển Khai

### 3.1 Môi Trường
- Development
- Staging
- Production

### 3.2 Monitoring
- Logging
- Metrics
- Alerting

### 3.3 Backup
- Database backup
- Configuration backup
- Disaster recovery plan 