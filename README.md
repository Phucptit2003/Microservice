# Hệ Thống Đặt Vé Xem Phim

## Tổng Quan
Hệ thống `movie_ticket_booking` là một ứng dụng microservice cho phép đặt vé xem phim trực tuyến. Hệ thống cho phép người dùng duyệt phim, chọn suất chiếu, đặt chỗ ngồi, thanh toán và nhận thông báo. Hệ thống được xây dựng bằng **Spring Boot**, **Docker** và **Spring Cloud**, với **Eureka Server** để khám phá dịch vụ và **API Gateway** để định tuyến yêu cầu. Bảo mật được thực thi bằng **JWT**.

## Cấu Trúc Dự Án
```
microservices-assignment-starter/
├── README.md                       # Tệp hướng dẫn này
├── .env.example                    # Biến môi trường mẫu
├── docker-compose.yml              # Cấu hình Docker cho tất cả dịch vụ
├── docs/                           # Thư mục tài liệu
│   ├── architecture.md             # Mô tả thiết kế hệ thống
│   ├── analysis-and-design.md      # Tài liệu phân tích và thiết kế
│   ├── asset/                      # Lưu trữ hình ảnh, sơ đồ và tài liệu trực quan
│   └── api-specs/                  # Đặc tả API theo OpenAPI (YAML)
│       ├── user-service.yaml
│       ├── movie-service.yaml
│       ├── showtime-service.yaml
│       ├── seat-service.yaml
│       ├── booking-service.yaml
│       ├── payment-service.yaml
│       └── notification-service.yaml
├── scripts/                        # Script tiện ích và triển khai
│   └── init.sh
├── services/                       # Các microservice của ứng dụng
│   ├── user-service/
│   │   ├── Dockerfile
│   │   ├── src/
│   │   └── readme.md
│   ├── movie-service/
│   │   ├── Dockerfile
│   │   ├── src/
│   │   └── readme.md
│   ├── showtime-service/
│   │   ├── Dockerfile
│   │   ├── src/
│   │   └── readme.md
│   ├── seat-service/
│   │   ├── Dockerfile
│   │   ├── src/
│   │   └── readme.md
│   ├── booking-service/
│   │   ├── Dockerfile
│   │   ├── src/
│   │   └── readme.md
│   ├── payment-service/
│   │   ├── Dockerfile
│   │   ├── src/
│   │   └── readme.md
│   └── notification-service/
│       ├── Dockerfile
│       ├── src/
│       └── readme.md
└── gateway/                        # API Gateway / reverse proxy
    ├── Dockerfile
    └── src/
```


## Tính Năng
- Đăng ký và xác thực người dùng
- Duyệt phim và suất chiếu theo rạp
- Chọn và đặt chỗ ngồi cho suất chiếu
- Xử lý thanh toán an toàn
- Gửi thông báo xác nhận đặt vé
- Chức năng quản trị để quản lý phim và suất chiếu
- Theo dõi tình trạng ghế ngồi theo thời gian thực
- Xử lý thanh toán an toàn với nhiều phương thức
- Thông báo email cho xác nhận đặt vé và cập nhật

## Kiến Trúc
Hệ thống tuân theo kiến trúc microservice, với các thành phần sau:

### Hạ Tầng Cốt Lõi
- **API Gateway**: Định tuyến yêu cầu, xử lý xác thực JWT và quản lý các vấn đề chung
- **Eureka Server**: Máy chủ đăng ký và khám phá dịch vụ

### Các Dịch Vụ
1. **Dịch Vụ Người Dùng** (`user-service`)
   - Xác thực và phân quyền người dùng
   - Quản lý hồ sơ
   - Lịch sử và tùy chọn người dùng

2. **Dịch Vụ Phim** (`movie-service`)
   - Quản lý danh mục phim
   - Thông tin và metadata phim
   - Đánh giá và nhận xét phim

3. **Dịch Vụ Suất Chiếu** (`showtime-service`)
   - Lập lịch và quản lý suất chiếu
   - Quản lý rạp và phòng chiếu
   - Theo dõi tình trạng

4. **Dịch Vụ Ghế Ngồi** (`seat-service`)
   - Quản lý kho ghế
   - Cấu hình và sơ đồ ghế ngồi
   - Theo dõi trạng thái ghế theo thời gian thực

5. **Dịch Vụ Đặt Vé** (`booking-service`)
   - Điều phối đặt vé
   - Quản lý đặt chỗ
   - Lịch sử đặt vé

6. **Dịch Vụ Thanh Toán** (`payment-service`)
   - Xử lý thanh toán
   - Hỗ trợ nhiều phương thức thanh toán
   - Quản lý giao dịch
   - Theo dõi trạng thái thanh toán

7. **Dịch Vụ Thông Báo** (`notification-service`)
   - Thông báo email
   - Xác nhận đặt vé
   - Cập nhật trạng thái thanh toán
   - Cảnh báo hệ thống

### Chi Tiết Kỹ Thuật
- **Cơ Sở Dữ Liệu**: Mỗi dịch vụ duy trì cơ sở dữ liệu riêng
- **Giao Tiếp**: 
  - REST APIs với Feign Client cho giao tiếp đồng bộ
  - Kiến trúc hướng sự kiện cho các hoạt động bất đồng bộ
- **Bảo Mật**: Xác thực và phân quyền dựa trên JWT
- **Container**: Docker cho triển khai nhất quán
- **Service Discovery**: Eureka cho đăng ký và khám phá dịch vụ
- **API Gateway**: Spring Cloud Gateway cho định tuyến

## Bắt Đầu

### Yêu Cầu Hệ Thống
- Docker và Docker Compose
- Java 17
- Maven
- Git

### Cài Đặt
1. Clone repository:
   ```bash
   git clone <repository-url>
   cd microservices-assignment-starter
   ```

2. Sao chép file môi trường mẫu:
   ```bash
   cp .env.example .env
   ```

3. Cập nhật `.env` với cấu hình của bạn

4. Chạy script khởi tạo:
   ```bash
   chmod +x scripts/init.sh
   ./scripts/init.sh
   ```

5. Khởi động tất cả dịch vụ:
   ```bash
   docker-compose up --build
   ```
   
    ```bash
    http-server
    ```

6. Kiểm tra các dịch vụ đang chạy:
   - Eureka Server: http://localhost:8761
   - API Gateway: http://localhost:8080

### Cổng Dịch Vụ
- Eureka Server: 8761
- API Gateway: 8080
- User Service: 9090
- Movie Service: 9090
- Showtime Service: 9090
- Seat Service: 9090
- Booking Service: 9090
- Payment Service: 9090
- Notification Service: 9090


