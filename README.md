# 클럽 마일리지 서비스

### [요구 사항]
- SQL  설계
- 포인트 적립 API 작성
- 포인트 조회 API 작성

### [개발 환경]
- Java11
- Spring Boot
- Spring Data JPA
- MySQL5.7

### [Data Base 설계]
![image](https://user-images.githubusercontent.com/97225760/175811328-31986d80-4ea6-411a-96d5-f8d695b38088.png)

<b> - user </b><br/>
user_id : user의 sequence <br/>
user_uuid : user의 UUID <br/>
point : 보유포인트 <br/>

<b> - place</b><br/>
place_id : place의 sequence <br/>
place_uuid : place의 UUID <br/>

<b> - review</b><br/>
review_id : review의 sequence <br/>
review_uuid : review의 UUID <br/>
content : 리뷰 내용 <br/>
date : 리뷰 작성일 <br/>
first_review : 장소의 첫 리뷰(1/0) <br/>

<b> - photo</b><br/>
photo_id : photo의 sequence <br/>
attached_photo : 첨부된 사진 id <br/>

<b> - point</b><br/>
point_id : point의 sequence <br/>
info : 적립 내용 <br/>
point : 적립 포인트(1/-1) <br/>
data : 적립일 <br/>


### [상황]
- 리뷰작성 시, 포인트 증가 <br/>
리뷰작성 버튼 -> 사용자포인트조회 버튼 및 포인트이력조회 버튼 클릭 <br/>
- 리뷰수정 시, 포인트 증가 or 감소 <br/>
리뷰수정 버튼 -> 사용자포인트조회 버튼 및 포인트이력조회 버튼 클릭 <br/>
- 리뷰삭제 시, 포인트 감소 <br/>
리뷰삭제 버튼 -> 사용자포인트조회 버튼 및 포인트이력조회 버튼 클릭 <br/>

### [실행]
- application.properties 에서 url,username,password 수정.
- 작성된 DDL 사용 혹은 ddl-auto=create로 변경 후 실행.
  - resources\static\DDL.sql 에서 DDL 확인 가능.
- review.html 에서 전송데이터 수정 가능.
- http://localhost:8080 에서 버튼클릭을 통해 결과를 확인할 수 있음.
