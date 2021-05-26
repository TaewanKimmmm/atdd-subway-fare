# atdd-subway-fareByDistance

## 구현할 기능 목록
- [x] **배포하기**
    - [x] Nginx로 Reverse Proxy 구성하기
        - [x] TLS 설정
    - [x] application.properties를 통한 다양한 환경의 DB 구성
        - [x] Test 환경 : H2
        - [x] Local 환경 : Mysql (Docker)
        - [x] Production 환경 : Mysql (Server)
        - [x] Remote 환경 : Mysql (다른 인스턴스 Server)

- [ ] **전체 조회 기능**
    - [ ] GET: /stations/transfer로 환승 가능한 역의 목록을 노선 이름과 함께 반환

- [x] **요금 조회 기능**
    - [x] 거리에 따른 운임 계산 로직 구현
        - [x] 기본 요금 : 1250원
        - [x] 10km 초과 ~ 50km 이하 : 5km마다 100원
        - [x] 50km 초과 시 : 8km마다 100원

- [x] **요금 정책 추가**
    - [x] 노선별 추가 요금 구현
        - [x] 노선이 추가 요금에 대한 정보를 가지도록 CRUD 로직 수정
        - [x] 경로 조회 시, 추가 요금이 가장 높은 노선의 금액을 운임 계산에 포함
    - [x] 로그인 사용자의 경우 연령별 요금으로 계산
        - [x] 청소년 *(13세 이상 ~ 19세 미만)* : 운임에서 350원을 공제한 금액의 20% 할인
        - [x] 어린이 *(6세 이상 ~ 13세 미만)* : 운임에서 350원을 공제한 금액의 50% 할인
        - [x] 로그인이 되지 않은 사용자는 앞선 계산 로직으로 요금을 산정한다
    
## 구현할 예외 처리 기능 목록
- [x] 동일한 이름의 지하철 역은 등록할 수 없다. - CONFLICT(409)
- [x] 존재하지 않는 지하철 역을 삭제할 수 없다. - NOT_FOUND(404)
- [x] 존재하지 않는 지하철 역을 검색할 수 없다. - NOT_FOUND(404)
- [] 지하철 역의 이름은 두 글자 이상의 한글 또는 숫자로 이루어져야만 한다. - BAD_REQUEST(400)
- [] 공백 문자를 포함하지 않는다. - BAD_REQUEST(400)