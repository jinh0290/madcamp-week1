# madcamp-week1
2020 여름 KAIST 몰입캠프 1주차 공통과제

[팀원] : 김규리, 이찬희, 최진혁

[프로젝트 이름] : TabsWithAnimatedSwipe

[전체적인 구조] : 
  - 간단한 IntroActivity를 삽입하여 스플래시 화면을 구현하였음.
  - ViewPager2의 PageTransformer를 사용하여 스와이프 할 때의 애니메이션을 적용하였음.
  - TabLayout과 ViewPager2를 활용하여 스와이프로 탭 전환이 가능하게 구현하였음.
  - 각각의 탭에는 그 탭의 기능을 구현하는 Fragment를 연결하였음.
  - 외부 라이브러리를 활용하여 탭 아이콘의 GIF를 재생할 수 있게 하였음.
  - checkSelfPermission와 requestPermissions를 이용해서 권한이 필요한 작업을 실행할 때 권한 확인 & 요청을 할 수 있게하였음.
  
[각 탭에 대한 설명] :

  <1번탭> 
  - RecyclerView를 이용해서 연락처 정보를 표시함
  - EditText와 TextWatcher를 이용해 목록 내에서 특정 연락처를 검색할 수 있는 기능을 구현함
  - 검색창 옆의 ImageButton을 눌렀을 때 Popupmenu를 표시하여 단말기의 연락처 정보를 불러오는 기능과 목록을 비우는 기능을 선택할 수 있게 하였음.
  - 우측 하단의 FloatingActionBUtton을 눌러서 임의의 연락처를 추가하는 기능
  - 연락처 항목을 터치하면 Alertdialog를 만들어서 전화앱과 연결, SMS앱과 연결, 연락처 정보 편집하기, 연락처 삭제하기 기능들을 수행할 수 있게 하였음.
  - 연락처의 프로필 사진을 불러오거나 초기화 할 수 있는 기능
  
  <2번탭> 
  - 채워주세요~~
  
  <3번탭> 
  - OnDraw 와 OnTouchEvent를 이용하여 그림판의 View를 지정해줘서 Canvas 와 Paint로 기본적인 그림판의 틀을 구현.
  - Button 을 이용해 색상 변경, 지우개, 비우기등의 기능을 onClickEvent로 구현, 변경된 색상은 지정된 버튼에 저장하는 기능도 구현.
  - Button 클릭후 menuitem을 불러와 블러 효과, 엠보싱, 배경설정등의 기능들을 구현
  - SeekBar을 이용해 선의 굵기를 지정해주는 기능을 구현
  - 파일이름을 지정하여 휴대폰의 외부저장소에 저장하는 기능을 구현
  - 불러오기 기능을 구현하여 외부저장소에 있는 사용자가 그린 그림을 가지고 오는 것은 가능하나, 그위에다가 더 그리는 기능은 구현 안됨
  
  ![alt text](https://github.com/[chanhee015]/[madcamp-week1]/blob/[master]/bonobono.jpg?raw=true)
  
