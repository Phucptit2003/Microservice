# Hệ Thống Đặt Vé Xem Phim

## Thành viên nhóm

| Họ và tên       | MSSV                       |               
|-----------------|----------------------------|
| Lê Đình Phúc    | B21DCCN593                 |  
| Đồng Hoàng Minh | B21DCCN522                 |  

## Tổng Quan
Hệ thống `movie_ticket_booking` là một ứng dụng microservice cho phép đặt vé xem phim trực tuyến. Hệ thống cho phép người dùng duyệt phim, chọn suất chiếu, đặt chỗ ngồi, thanh toán và nhận thông báo. Hệ thống được xây dựng bằng **Spring Boot**, **Docker** và **Spring Cloud**, với **Eureka Server** để khám phá dịch vụ và **API Gateway** để định tuyến yêu cầu. Bảo mật được thực thi bằng **JWT**.

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
   docker-compose up -d
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

## Cấu Trúc Dự Án
```
microservices-assignment-starter/
├── README.md                 
├── prometheus.yml
├── .env.example                   
├── docker-compose.yml            
├── docs/                           
│   ├── architecture.md             
│   ├── analysis-and-design.md     
│   ├── asset/                     
│   └── api-specs/                  
│       ├── user-service.yaml
│       ├── movie-service.yaml
│       ├── showtime-service.yaml
│       ├── seat-service.yaml
│       ├── booking-service.yaml
│       ├── payment-service.yaml
│       └── notification-service.yaml
├── scripts/                        
│   └── init.sh
├── services/                      
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
└── gateway/                        
    ├── Dockerfile
    └── src/
```

## Công Nghệ Sử Dụng

- **Spring Boot**: Framework phát triển ứng dụng Java microservice
- **Spring Cloud**: Hỗ trợ các pattern microservice (service discovery, config, v.v.)
- **Spring Cloud Gateway**: API Gateway định tuyến và bảo mật
- **Eureka Server**: Service Discovery
- **Docker & Docker Compose**: Đóng gói, triển khai và quản lý container
- **MySQL**: Cơ sở dữ liệu cho từng service
- **Prometheus**: Thu thập và giám sát metrics hệ thống
- **Grafana**: Hiển thị dashboard giám sát realtime
- **JWT (JSON Web Token)**: Xác thực và phân quyền
- **Feign Client**: Giao tiếp REST giữa các service
- **Spring Security**: Bảo mật ứng dụng
- **RabbitMQ**: Message broker cho giao tiếp bất đồng bộ giữa các service.


### Sử dụng RabbitMQ cho Notification

- **RabbitMQ** được sử dụng để truyền thông điệp bất đồng bộ giữa payment-service và notification-service.
- Khi thanh toán thành công, payment-service gửi message vào queue `payment-notification-queue` trên RabbitMQ.
- notification-service lắng nghe queue này và gửi email thông báo cho khách hàng.
- Để kiểm tra, có thể truy cập RabbitMQ Management UI tại [http://localhost:15672](http://localhost:15672) (user/pass: guest/guest).

## Luồng Xử Lý Chính

### Đặt Vé
1. Client gửi yêu cầu qua API Gateway
2. API Gateway xác thực JWT
3. Booking Service nhận yêu cầu
4. Kiểm tra ghế với Seat Service
5. Tạo đơn đặt vé
6. Xử lý thanh toán qua Payment Service
7. Gửi thông báo qua Notification Service

### Quản Lý Phim
1. Admin đăng nhập qua User Service
2. Thêm/sửa thông tin phim trong Movie Service
3. Cập nhật lịch chiếu trong Showtime Service
4. Notification Service gửi thông báo cập nhật

## Bảo Mật
- JWT cho xác thực
- HTTPS cho truyền tải dữ liệu
- Mã hóa mật khẩu
- Phân quyền chi tiết
- Rate limiting tại API Gateway

## Khả Năng Mở Rộng
- Thiết kế cho phép thêm service mới dễ dàng
- Có thể scale từng service độc lập
- Sử dụng container cho triển khai linh hoạt
- Load balancing tự động 



