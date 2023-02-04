<!-- HEADER -->
  ![rect](https://capsule-render.vercel.app/api?type=rect&color=gradient&text=%20TEAM4%20&fontAlign=27&fontSize=30&textBg=true&desc=Welcome%20to%20%27team4-server%27%20Repository!&descAlign=62&descAlignY=50)
  
   
<!-- PROJECT LOGO -->

<br />
<div align="center">
  <a href="https://github.com/wafflestudio/snutt-ios"><img src="https://user-images.githubusercontent.com/90292371/216655038-faca7e09-9012-4e56-a0a0-5127655dedc8.png" alt="Logo" width="110" height="110">
  </a>
  <h3 align="center">MUSIN4A</h3>

  <p align="center">
    Wafflestudio Rookies 20.5th, Toy Project: MUSINSA clone-coding (22.12.18.-23.02.04.)
    <div style=" padding-bottom: 1rem;">
    <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"/>
    <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/>
    </div>
  </p>
  <a href="https://bold-tin-2e7.notion.site/MUSIN4-73b8495ff4eb487fa03c954e9129aa06">Notion</a>
    ·  
  <a href="https://dllflfuvssxc9.cloudfront.net/">Server</a>
    ·
  <a href="https://musin4.snucha.shop">Deployment</a>
  
</div>

<br>



<!-- ABOUT THE PROJECT -->

## :hammer_and_pick: Built with ..
| | :crown: 차동주 | 서보성 | 양찬혁 |
|---|:---:|:---:|:---:|
|Github Id|@dongjoocha|@Boseong-Seo|@yangchanhk98|
|Profile  | <img src="https://res.cloudinary.com/dehnixjg0/image/upload/v1675487751/IMG_2316-%E1%84%89%E1%85%AE%E1%84%8C%E1%85%A5%E1%86%BC_hwkjfj.jpg" width="180px" height="240px">| <img src="https://user-images.githubusercontent.com/90292371/216748303-dc61d1ff-3033-4c84-a10d-0a40d809fba0.jpg" width="180px" height="240px" style='border-radius: 50%'></a>|  <img src="https://user-images.githubusercontent.com/90292371/216748416-92993327-e40c-4a5a-942e-03cb75f32a63.jpg" width="180px" height="240px"  style='border-radius: 50%'></a>|
|Role     | - login<br> - auth<br> - 배포<br> (\+Frontend..)| - items<br> - item-inquries<br> - style  | - mypage<br> - review<br> - comment|

<br>
  


:speaking_head: 6주간 고생 많았던 백엔드 팀원분들 ! 소감 한 마디씩 부탁해요  
> 동주 : _"할 말 하 않"_ <br>
> 보성 : _"아 리드미 너무 힘들다"_ <br>
> 찬혁 : _"동주님 보성님 찬양합니다"_ <br>



<br>

<!-- Project structure & ERD -->



## :card_file_box: Project Structure
저희 프로젝트는 대략적으로 아래와 같은 구조로 되어 있어요. <br>
    
```bash
src/main/kotlin/com/wafflestudio/toyproject/team4
├─common       
├─config       
├─core          
│  ├─board      // review, comment, inquiry 관련 API 담당
│  ├─image      // 이미지 업로드 API
│  ├─item
│  ├─purchase
│  ├─style
│  └─user
└─oauth        
```
그리고 `core`의 (`image` 패키지를 제외한) 모든 패키지는 아래와 같은 구조를 가지고 있어요.  
  
이때, Controller - Service - Repository 각 layer 단에서 어떤 역할을 하는지 거듭해서 고민하고,  
특정 패키지의 한 layer에 역할이 가중되지 않도록 주의해가며 코드를 작성했어요.
```bash
├─api
│  ├─request
│  ├─response
│  └─xxController.kt
├─database
│  ├─xxEntity.kt
│  └─xxRepository.kt
├─domain
└─service
   └─xxService.kt
```
  
## :card_file_box: ERD
![ERD](https://user-images.githubusercontent.com/90292371/216746923-864bccf4-a2aa-4ef2-9328-538d2c7c50fd.png)
  
<br>



## :angel: API
저희가 구현한 API를 표로 요약해 나타내자면 다음과 같아요!  
  
최대한 페이지별로, 기능별로 묶어서 정리했어요  
페이지 관련 API들을 중심으로 작성했고, 때문에 저희가 구현한 것 중 포함되지 않은 API도 일부 있어요.  
  
이때 자물쇠 표시(:lock:)은, _해당 API에서 `@RequestHeader`로 AccessToken을 요하고 있다_, 라는 의미예요.  
  
보다 자세한 설명은 [**이 노션 페이지**](https://www.notion.so/Rest-API-e92a1784fecf4f42832601e65d1e53b5)를 참고해주세요 :wink:    
  

| Page/Feature        | GET | POST | PUT | PATCH | DELETE | API                              | Authenticated |
|---------------------|:---:|:----:|:---:|:-----:|:------:|--------------------------------------------|:---:|
|**My page**          | ✔️ |      |      | ✔️   |        | /api/user/me                                |🔒  |
|└ My purchases       | ✔️ | ✔️   |     |       |        | /api/user/me/purchases                      |🔒  |
|└ My reviews         | ✔️ |      |      |       |        | /api/user/me/reviews                        |🔒 |
|                     |    | ✔️   | ✔️  |       |        | /api/user/me/review                         |🔒  |
|                     |    |       |     |       | ✔️     | /api/user/me/review/ `review.id`            |🔒 |
|└ My item-inquiries  | ✔️ |      | ✔️  |       |        | /api/user/me/item-inquiries                 |🔒  |
|                     |    |       |     |       | ✔️     | /api/user/me/item-inquiry/`item-inquiry.id` |🔒 |
|└ My recently-viewed | ✔️ | ✔️   |     |       |        | /api/user/me/recently-viewed                |🔒  |
|**My shopping-cart** | ✔️ | ✔️   |     | ✔️    |        | /api/user/me/shopping-cart                 |🔒  |
|                     |    |       |     |       | ✔️     | /api/user/me/shopping-cart/`cart-item.id`  |🔒  |
|**My/Other's closet**| ✔️ |      |      |      |        | /api/user/`user.id`                         |(🔒)|
|                     | ✔️ |      |      |      |        | /api/user/`user.id`/styles                  |    |
|└ Follow             |    | ✔️   |      |       |  ✔️   | /api/user/me/`user.id`/follow               |🔒  |
|                     | ✔️ |      |       |      |       | /api/user/me/`user.id`/followers             |   |
|                     | ✔️ |      |       |      |       | /api/user/me/`user.id`/followings            |   |
|└ Style              |    | ✔️   |      |       |       | /api/style                                   |🔒 |
|**Search**           | ✔️ |      |      |       |       | /api/user/search                             |   |
|                     | ✔️ |      |      |       |       | /api/search                                  |   |
|**Item list page**   | ✔️ |      |      |       |       | /api/items                                   |   |
|**Item detail page** | ✔️ |      |      | ✔️   |        | /api/item/`item.id`                          |   |
|└ Reviews            | ✔️ |      |      | ✔️   |        | /api/item/`item.id`/reviews                  |   |
|  └ Comments         | ✔️ |      |      |      |        | /api/item/`item.id`/comments                |    |
|                     |    | ✔️   |      |      |        | /api/comment                                |    |
|                     |    |      |  ✔️  |      |  ✔️    | /api/comment/`comment.id`                   |    |
|└ Item-inquiries     | ✔️ |      |      |      |        | /api/item/`item.id`/inquiries               |    |
|                     |    | ✔️   |      |      |        | /api/item/`item.id`/inquiry                 |🔒  |
|**Style list page**  | ✔️ |      |      |      |        | /api/styles                                 |    |
|└ Style Modal        | ✔️ |      |      |      |        | /api/style/`style.id`                       |(🔒)|
|  └ Like             |    |  ✔️  |      |      |   ✔️  | /api/style/`style.id`/like                   | 🔒 |
|**Upload Image**     |    |  ✔️  |      |      |        | /api/image-upload                           | 🔒 |

 
  
  
<br>

## :waffle: Getting Started
마지막으로 제일 중요한 :star:로컬 상에서의 실행 방법:star:을 알려드릴게요!  

### Prerequisites
:white_check_mark: Docker(Desktop)  
:white_check_mark: Docker Compose  
:white_check_mark: DataGrip  
:white_check_mark: intelliJ IDEA  
  
### How to run
1. 현 repository 주소를 본인이 원하는 로컬 상 주소에다가 복제해주세요.
    ```
    git clone https://github.com/wafflestudio20-5/team4-server.git
    ```

2. 'Docker Desktop'을 실행한 다음, 아래의 명령어를 터미널에 입력해주세요.
    ``` 
    docker-compose up -d
    ```
    이때, 아래 첨부한 사진과 같이 뜨는지 확인해주세요!
      
    <img src="https://user-images.githubusercontent.com/74580163/210161547-74fff8db-a2d3-477b-8af9-86a831b71b70.png" alt="docker-compose 실행 화면" width="800" height="450">

3. 'IntelliJ'를 실행한 다음, 조금 전에 복제해온 repository를 열어주세요. 그런 다음, 프로젝트를 build 하고서, build가 꼭 완료된 후에 application(Team4Application)을 run해주세요   
      
    <img src="https://user-images.githubusercontent.com/74580163/210161740-012a71e3-3d68-49e2-92cd-9b823801cedf.png" alt="빌드 후 run" width="590">

4. 'DataGrip'을 실행한 다음, 로컬에서 database를 확인할 수 있도록 아래의 설정을 해주세요!
    - `File` -> `New` -> `Data Source` -> `MySQL` 순서로 클릭한 다음, 아래와 같이 설정해주세요.  
    - 이때, password 입력해주셔야 하는 거 잊지 마세요 :heavy_exclamation_mark: : `team4`   
         
      <img src="https://user-images.githubusercontent.com/74580163/210161569-b67a4db2-e77f-4afa-ae39-208ff4b66f2e.png" alt="data-grip 설정 화면" width="570">  
        
    - 설정하고나서, 꼭 !! `Test Connection`이 잘 이루어지는지도 확인해주셔야 해요  
          
      <img src="https://user-images.githubusercontent.com/74580163/210161829-e2e0ff9c-b94c-41fe-a2ef-53101757c935.png" width="570">
