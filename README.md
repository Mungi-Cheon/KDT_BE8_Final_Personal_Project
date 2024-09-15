# 마이그레이션 For MSA & Kafka

<br>

## 프로젝트 개요

    최근 미니 프로젝트로 숙소 예약 사이트 애플리케이션을 진행했었습니다. 
    이번 파이널 프로젝트에서는 미니 프로젝트 팀원분들의 양해를 얻어 사용 허가를 받아
    모놀리식 아키텍처였던 미니 프로젝트를 MSA로 전환하고 Kafka를 접목해 사용하는 이유와
    사용 경험에 대해 체득해보는 경험을 얻고자 진행했습니다.

### ■ 기간

- 24.8.12 - 9.13

<br>

### ■ 참여 인원

|                                                                **천문기**                                                                 |
|:--------------------------------------------------------------------------------------------------------------------------------------:|
| <img src="https://github.com/Mungi-Cheon/HACK_MartService/assets/159132478/eff6bcf3-2bc8-4a57-a6f8-e8017cd170e9" height=150 width=250> |

<br>

## 기술 스택

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/spring_cloud-6DB33F?style=for-the-badge&logo=java&logoColor=white">
<br>
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/spring_security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
<br>
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"> <img src="https://img.shields.io/badge/kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white">
<br>
<img src="https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white"> <img src="https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white">
<br>
<img src="https://img.shields.io/badge/ec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"> <img src="https://img.shields.io/badge/rds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white">


<br>

## 아키텍처

<img src="https://private-user-images.githubusercontent.com/159132478/367576491-3d83d04b-87d9-4c1b-a5d8-4a44d6245b8f.jpg?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MjYzOTgxOTAsIm5iZiI6MTcyNjM5Nzg5MCwicGF0aCI6Ii8xNTkxMzI0NzgvMzY3NTc2NDkxLTNkODNkMDRiLTg3ZDktNGMxYi1hNWQ4LTRhNDRkNjI0NWI4Zi5qcGc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwOTE1JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDkxNVQxMDU4MTBaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0xMDcxYjdkYzg2MDU3YjI4YmM5N2U1M2ExYWM4ODExZmI2MzVjOTBiMWJmMjNiNjk5NWUyN2JhNzhhM2E5ODZkJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.nlVkqMqq5rHRKd4GmqUaHYj3KuddFfSCQmPs9mvQehg" alt="아키텍처">

## 구현 내용

- 공통
    - Config Service
        - Eureka, Gateway, 도메인 관련 환경 변수 관리
    - Eureka Server
        - 도메인 서비스 등록, 발견, 로드밸런싱
    - Gateway Service
        - 도메인 서비스 라우팅
        - JWT 인증/인가

- 서비스
    - 회원
        - 회원 가입, 회원 탈퇴
        - 시큐리티
            - 로그인, 로그아웃
            - 로그인 시 Access Token, Refresh Token 발급 및 레디스 등록
            - 로그아웃 시 Access Token을 블랙리스트로 등록
    - 숙소
        - 숙소 전체 조회
        - 숙소 상세 조회
    - 객실
        - 객실 전체 조회
        - 객실 상세 조회
    - 예약
        - 예약 내역 조회
        - 예약
        - 예약 취소

## Kafka

### 보상 트랜잭션 : Choreography based SAGA pattern

■ 채택 이유

- 프로젝트의 규모가 크지 않기에 오케스트레이션 패턴을 사용하기엔 과하다고 생각했습니다.
- Kafka에 대한 지식이 많은 편은 아닌 상태에서도 충분히 이해할 수 있고 빠르게 적용할 수 있는 패턴이라고 판단했습니다.

■ 프로세스

<img src="https://private-user-images.githubusercontent.com/159132478/367577771-1fefb50c-8553-4436-b556-ebd44fb99316.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MjYzOTk2NDksIm5iZiI6MTcyNjM5OTM0OSwicGF0aCI6Ii8xNTkxMzI0NzgvMzY3NTc3NzcxLTFmZWZiNTBjLTg1NTMtNDQzNi1iNTU2LWViZDQ0ZmI5OTMxNi5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwOTE1JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDkxNVQxMTIyMjlaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0wZTI3MWRhY2Y0NTA4YTAxMDRjYTM2M2MxYTAyMDU0YTMzMGNlMTA4OTkyMzljODIxN2E1ZjliODAxZTRiYjk5JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.uOFf8BAR5HzDwQDPVfmlLRe-orkTj5jmhVoRD4xHu2Y" alt="Kafka프로세스">

1. Pending 상태의 예약 데이터 생성
2. 예약 토픽 이벤트 발행 (Producer : 예약 서비스)
3. 예약 토픽을 구독하고 있는 객실 서비스에서 메세지 수신 (Consumer : 객실 서비스)
4. 해당 객실 카운트 감소
5. 트랜잭션 결과 토픽 이벤트 발행(Producer : 객실 서비스)
   <br> 예외 발생시 Failure 상태를 메세지에 담고 정상인 경우 Success 상태를 담는다.
6. 트랜잭션 결과 토픽을 구독하고 있는 예약 서비스에서 메세지 수신 (Consumer : 예약 서비스)
7. 메세지에 저장된 상태에 따라 예약 데이터의 상태를 갱신

## Redis

### Redisson 분산 락

미니 프로젝트에서의 동시성 제어를 비관적 락으로 해결했었고 리팩토링때 분산락으로 전환하기로 했지만, 리팩토링 업무 증가와 파이널 프로젝트 준비등을 이유로 마지막까지 전환을
못했습니다.

또한 MSA로 전환하기 때문에 모놀리식 아키텍처에 적합한 비관적 락에서 Redisson 분산 락을 전환을 시도했습니다.

이로 인해, 직접 락을 걸어 병목 현상을 발생시킬 수 있었던 우려점을 해결하였고 msa로 전환함에 따라 오히려 분산 환경이 됨으로써 더 사용하기 적합한 환경이 되었습니다.

## MSA

### 장점

- 하나의 서비스에서 장애가 발생해도 서비스 전체가 중단되지 않습니다.
- 모놀리식에서는 어느 서비스의 로직을 수정하게 되면 다른 서비스도 영향을 받을 수 밖에 없는 구조였지만 MSA에서는 서비스들이 독립적이기에 유지 보수가 쉬웠습니다.
- 모놀리식의 경우 어느 로직을 수정하더라도 모든 서비스에 대한 빌드, 배포가 필요했지만 MSA의 경우 수정한 로직의 서비스만 빌드, 배포가 가능합니다.

### 단점

- 서비스들이 독립적이지만 동기적 통신이 필요한 경우 완전히 독립된 서비스를 구현할 수 없습니다.
    - 숙소 조회시 입력된 체크인, 체크아웃 조건으로 예약 가능한 객실을 가지고 있는 숙소를 반환해야해서 OpenFeign을 통해 객실 서비스에 요청을 보냈어야 했습니다.
- 애플리케이션 구축이나 유지 보수는 쉬웠지만 인프라는 할 일이 많아질 듯 합니다.
    - 모놀리식에서는 하나의 서버만으로도 충분할지 모르지만 MSA의 경우 각 서비스마다 서버를 구축하게 되면 유지 보수 해야할 양이 많아지겠구나 라고 생각이 들었습니다.
- MSA 전환은 좋았지만 처음과 달리 필요한 기술 스택들과 고려해야할 사항들이 많았습니다.
    - 기술 스택
        - 멀티 모듈
        - Spring Cloud
            - Eureka
            - Gateway
            - OpenFeign
            - Config
        - Kafka
    - 고려 사항
        - 트랜잭션 처리 : Kafka, Choreography based SAGA pattern
        - 서비스간 통신 시 장애 발생 대응 : Resilience4j, Circuit Breaker 패턴 등
            - 아쉬운 점에서 언급하겠지만 이번 프로젝트에선 기술을 적용하지 않았습니다.

## 좋았던 점

- MSA와 Kafka에 대한 최소한의 지식으로 시작해 자료조사를 시작으로 오랜만에 스스로 답을 찾아가는 시간을 최대한 가질 수 있어서 좋았습니다.
- 프로젝트를 생각하면 MSA, Kafka를 적용하는 것 부터 이미 오버 엔지니어링이었지만 이번 기회를 통해서 여러 기술 스택에 대한 사용 경험을 쌓을 수 있는 기회가 되었습니다.

## 아쉬운 점

- 서비스간 통신 시 장애 발생 대응을 하지 못했습니다.
    - 개인 프로젝트로 진행함에 따라 시간을 최대한 효율적으로 사용하고자 했지만 결과적으로 시간이 부족해 진행하지 못했습니다.
    - 추후 장애 발생 대응을 추가적으로 해보고자 합니다.
- AWS 프리티어의 한계로 무중단 배포, 스케일 아웃, 스트레스 테스트 진행을 할 수 없었습니다.
    - 개인 프로젝트를 진행하게 되면 서버지원이 불가하다는 원칙이 있어 프리티어로 진행할 수 밖에 없었고 서버 리소스가 큰 MSA, Kafka를 운용하기 위해서는 위의 사항들을
      포기할 수 밖에 없었습니다.
    - 다만, 무중단 배포, 스케일 아웃에 대한 개념과 대규모 트래픽 발생시 생길 수 있는 문제 대응 방법을 따로 조사해 블로깅을 해볼 예정입니다.