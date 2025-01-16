# Redis 분산락(Distributed Lock)을 이용한 재고 관리 구현

이번 프로젝트에서는 분산 시스템 환경에서 발생하는 다양한 문제 상황중, 서로 다른 요청이 하나의 데이터에 몰리게 되어 발생하는 레이스 컨디션(Race Condition) 문제를 분산 락을 이용해 해결하는 미니 프로젝트를 진행하고 이를 정리했습니다.

프로젝트 진행과정은 아래 블로그에 자세히 정리되어 있습니다.  
https://turtledev.tistory.com/71

---
### 기술 스택
- Spring Boot 3.4.1  
- Spring Data JPA  
- Java 17  
- Redis 7.0  
- Kafka  
- H2 Database
### 주요 구현 사항
**주요 기능**
- 재고 등록: 제품과 초기 재고량을 Redis에 저장.
- 재고 감소: 주문 요청 시 재고를 감소시키고, 남은 재고가 없으면 처리 실패
- 재고 조회: 현재 재고 상태를 조회
- 분산락 적용: 동시 재고 감소 요청 시 일관성 유지

**비기능 요구 사항**
- 동시성 제어: 분산락을 사용하여 레이스 컨디션 방지
- 에러 처리: 재고 부족, 락 획득 실패 등의 상황 처리

### 실행 방법
1. 프로젝트 최상위 `docker-compose.yml` 파일을 사용해 Redis Cluster를 시작합니다.
2. Spring Boot 애플리케이션을 실행합니다.

### 인프라 구성
![](https://private-user-images.githubusercontent.com/34668108/403645532-a4de3e9f-c8f7-49f3-8529-e06c9c61d32b.jpg?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MzY5OTQzMjUsIm5iZiI6MTczNjk5NDAyNSwicGF0aCI6Ii8zNDY2ODEwOC80MDM2NDU1MzItYTRkZTNlOWYtYzhmNy00OWYzLTg1MjktZTA2YzljNjFkMzJiLmpwZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTAxMTYlMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUwMTE2VDAyMjAyNVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPWRjNTYxZDk2YzlmNjk5YjcxNzk1NTgzMTA3ODhhOTU3ZDI2NmRiNzNmNzY0NGE2ZmZmMGY0ZTQxYWZkZjZjMDAmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.kRtGtcP--QwVAtrdEkscTfFKdhHurhAABTAAdTyP7eo)