#!/bin/bash

# Màu sắc cho output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}Bắt đầu khởi tạo dự án...${NC}"

# Kiểm tra Docker
echo -e "${YELLOW}Kiểm tra Docker...${NC}"
if ! command -v docker &> /dev/null; then
    echo -e "${RED}Docker chưa được cài đặt. Vui lòng cài đặt Docker trước.${NC}"
    exit 1
fi

# Kiểm tra Docker Compose
echo -e "${YELLOW}Kiểm tra Docker Compose...${NC}"
if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}Docker Compose chưa được cài đặt. Vui lòng cài đặt Docker Compose trước.${NC}"
    exit 1
fi

# Kiểm tra Java
echo -e "${YELLOW}Kiểm tra Java...${NC}"
if ! command -v java &> /dev/null; then
    echo -e "${RED}Java chưa được cài đặt. Vui lòng cài đặt Java 17 trước.${NC}"
    exit 1
fi

# Kiểm tra Maven
echo -e "${YELLOW}Kiểm tra Maven...${NC}"
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}Maven chưa được cài đặt. Vui lòng cài đặt Maven trước.${NC}"
    exit 1
fi

# Tạo file .env nếu chưa tồn tại
if [ ! -f .env ]; then
    echo -e "${YELLOW}Tạo file .env từ mẫu...${NC}"
    cp .env.example .env
    echo -e "${GREEN}Đã tạo file .env. Vui lòng cập nhật các biến môi trường.${NC}"
fi

# Build các service
echo -e "${YELLOW}Build các service...${NC}"
mvn clean package -DskipTests

# Tạo network Docker nếu chưa tồn tại
echo -e "${YELLOW}Tạo Docker network...${NC}"
docker network create movie-ticket-network 2>/dev/null || true

# Pull các image cần thiết
echo -e "${YELLOW}Pull các Docker image...${NC}"
docker-compose pull

# Khởi động các container database
echo -e "${YELLOW}Khởi động databases...${NC}"
docker-compose up -d postgres redis rabbitmq

# Đợi database khởi động
echo -e "${YELLOW}Đợi databases khởi động...${NC}"
sleep 30

# Khởi tạo schema database
echo -e "${YELLOW}Khởi tạo database schema...${NC}"
docker-compose exec -T postgres psql -U postgres -f /docker-entrypoint-initdb.d/init-multiple-dbs.sql

# Khởi động các service
echo -e "${YELLOW}Khởi động các service...${NC}"
docker-compose up -d eureka-server
sleep 10
docker-compose up -d api-gateway
sleep 10
docker-compose up -d user-service movie-service showtime-service seat-service booking-service payment-service notification-service

echo -e "${GREEN}Khởi tạo hoàn tất!${NC}"
echo -e "${GREEN}Eureka Server: http://localhost:8761${NC}"
echo -e "${GREEN}API Gateway: http://localhost:8080${NC}" 