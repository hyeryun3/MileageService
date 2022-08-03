# 클럽 마일리지 서비스

### [요구 사항]
- [x] SQL  설계
- [x] 포인트 적립 API 작성
- [x] 포인트 조회 API 작성
- [x] 테스트 코드 작성 

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
- 리뷰작성 시, 포인트 증가
  - 사진 추가 시 +1
  - 해당 장소의 첫 리뷰 시 +1
- 리뷰수정 시, 포인트 증가 or 감소
  - 사진 없는 리뷰에서 사진 추가 시 +1
  - 사진 있는 리뷰에서 사진 삭제 시 -1
- 리뷰삭제 시, 포인트 감소
  - 해당 리뷰로 부여된 포인트 회수


### [API]
|Method|URI|Description| 
|:---|:---|:---| 
|POST|/events|리뷰이벤트(작성/수정/삭제)| 
|GET|/userPoint/{userId}|유저 포인트 조회| 
|GET|/pointRecord/{userId}|유저 포인트 적립 기록 조회| 


### [API 테스트 방법]
- POST /events
```
{
"type": "REVIEW",
"action": "ADD", /* "MOD", "DELETE" */
"reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
"content": "좋아요!",
"attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-
851d-4a50-bb07-9cc15cbdc332"],
"userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
"placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
}
```
1. 코드 실행
2. http://localhost:8080 ( 위의 데이터로 임시 userId & placeId 생성 )
3. POST /events 로 위의 데이터 전송
4. /userPoint/{userId}를 통해 유저 포인트 조회
5. /pointRecord/{userId}를 통해 유저 포인트 적립 기록 조회
