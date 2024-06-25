<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>가상머신 2번째버튼</title>
</head>
<link rel="stylesheet" href="./css/Darkdashboard5.css">
<link href="//maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
<script
  src="https://code.jquery.com/jquery-1.12.4.js"
  integrity="sha256-Qw82+bXyGq6MydymqBxNPYTaUXXq7c8v3CwiYwLLNXU="
  crossorigin="anonymous">
</script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"></script>
<script src="./js/Darkdashboard5.js"></script>
<!--그래프-->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-circle-progress/1.2.2/circle-progress.min.js"></script>
<body>
    <div id="header">
        <div id="header_right">
            <span>Rutil Vm</span>
        </div>

        <div id="header_left">
            <div>
                <i class="fa fa-hdd-o"></i>
            </div>
            <div>
                <i class="fa fa-bell"></i>
            </div>
            <div>
                <i class="fa fa-user"></i>
            </div>
        </div>
    </div>
    <div id="main_outer">
        
        <div id="aside_outer">
            <div id="aside">
                <div id="nav">
                    <div id="aside_popup_dashboard_btn">
                        <i class="fa fa-th-large"></i>
                    </div>
                    <div id="aside_popup_machine_btn">
                        <i class="fa fa-desktop"></i>
                    </div>
                    <div id="aside_popup_storage_btn">
                        <i class="fa fa-server"></i>
                    </div>
                    <div id="aside_popup_network_btn">
                        <i class="fa fa-database"></i>
                    </div>
                </div>
                <div id="setting_icon">
                    <i class="fa fa-cog"></i>
                </div>
            </div><!--aside끝-->
            
            <div id="aside_popup">
                <!--가상머신 차트-->
                <div id="virtual_machine_chart">
                    <div class="aside_popup_content" id="aside_popup_first">
                        <i class="fa fa-chevron-down"></i>
                        <i class="fa fa-building-o"></i>
                        <span>data_center</span>
                    </div>
                    <div class="aside_popup_content" id="aside_popup_second" style="display: none;">
                        <i class="fa fa-chevron-down"></i>
                        <i class="fa fa-building-o"></i>
                        <span>ITITINFO</span>
                    </div>
                    <div class="aside_popup_content" id="aside_popup_last" style="display: none;">
                        <div>
                            <i></i>
                            <i class="fa fa-microchip"></i>
                            <span>192.168.0.80</span>
                        </div>
                        <div>
                            <i></i>
                            <i class="fa fa-microchip"></i>
                            <span>HostedEngine</span>
                        </div>
                        <div>
                            <i></i>
                            <i class="fa fa-microchip"></i>
                            <span>on20-ap01</span>
                        </div>
                    </div>
                </div>
                <!--스토리지 차트-->
                <div id="storage_chart" style="display: none;">
                    <div class="aside_popup_content" id="aside_popup_first2">
                        <i class="fa fa-chevron-down"></i>
                        <i class="fa fa-building-o"></i>
                        <span>data_center</span>
                    </div>
                    <div class="aside_popup_content" id="aside_popup_second2" style="display: none;">
                        <i class="fa fa-chevron-down"></i>
                        <i class="fa fa-building-o"></i>
                        <span>Default</span>
                    </div>
                    
                    <div class="aside_popup_content" id="aside_popup_last2" style="display: none;">
                        <div>
                            <i></i>
                            <i class="fa fa-microchip"></i>
                            <span>hosted-storage</span>
                        </div>
                        <div>
                            <i></i>
                            <i class="fa fa-microchip"></i>
                            <span>NFS-Storage</span>
                        </div>
                    </div>
                </div>
                
                <!--네트워크 차트-->
                <div id="network_chart" style="display: none;">
                    <div class="aside_popup_content" id="aside_popup_first3">
                        <i class="fa fa-chevron-down"></i>
                        <i class="fa fa-building-o"></i>
                        <span>Default</span>
                    </div>
                    <div class="aside_popup_content" id="aside_popup_second3" style="display: none;">
                        <i class="fa fa-chevron-down" style="color: white;"></i>
                        <i class="fa fa-building-o"></i>
                        <span>ovirtmgmt</span>
                    </div> 
                </div>

                <!--설정 차트-->
                <div id="setting_chart" style="display: none;">
                    <div id="setting_normal_btn">
                        <i class="fa fa-cog"></i>
                        <span>활성 사용자 세션</span>
                    </div>
                    <div id="setting_miniset_btn">
                        <i class="fa fa-cog"></i>
                        <span>설정</span>
                    </div>
                    <div id="setting_user_btn">
                        <i class="fa fa-cog"></i>
                        <span>사용자</span>
                    </div>
                    <div id="setting_account_btn">
                        <i class="fa fa-cog"></i>
                        <span>계정설정</span>
                    </div>
                    
                </div>

            </div>
        </div><!--aside_outer끝-->
        <!--우클릭메뉴박스-->
        <div id="context_menu">
            <div>새로 만들기</div>
            <div>새로운 도메인</div>
            <div>도메인 가져오기</div>
            <div>도메인 관리</div>
            <div>삭제</div>
            <div>Connections</div>
        </div>
        
        <!--대쉬보드 section-->
        <div id="dash_board">
            <div id="dash_boxs">
                <div class="box">
                  <span>UPTIME</span>
                  <h1>2</h1>
                </div>
                <div class="box">
                  <span>데이터센터</span>
                  <h1>2</h1>
                  <div class="arrows">
                    <i class="fa fa-arrow-up">1</i>
                    <i class="fa fa-arrow-down">1</i>
                  </div>
                </div>
                <div class="box">
                  <span>클러스터</span>
                  <h1>2</h1>
                </div>
              
                <div class="box">
                  <span>호스트</span>
                  <h1>2</h1>
                  <div class="arrows">
                    <i class="fa fa-arrow-up">1</i>
                    <i class="fa fa-arrow-down">1</i>
                  </div>
                </div>
                <div class="box">
                  <span>데이터스토리지도메인</span>
                  <h1>2</h1>
                </div>
                <div class="box">
                  <span>가상머신</span>
                  <h1>2</h1>
                  <div class="arrows">
                    <i class="fa fa-arrow-up">1</i>
                    <i class="fa fa-arrow-down">1</i>
                  </div>
                </div>
                <div class="box">
                  <span>이벤트</span>
                  <h1>0</h1>
                  <div class="arrows">
                    <i class="fa fa-arrow-up">1</i>
                    <i class="fa fa-arrow-down">1</i>
                  </div>
                </div>
              </div><!--boxs 끝-->
        

            <div id="dash_section">
                <div class="dash_section_contents"> 
                    <h1>CPU</h1>
                    <div class="graphs">
                        <div class="graph-wrap active-on-visible" data-active-on-visible-callback-func-name="CircleRun">
                            <div class="circle-graph1 circle-graph"> <!-- 클래스명 수정 -->
                                <strong class="circle-percent"></strong>
                            </div>
                        </div>
                        <div><canvas id="bar-chart-horizontal"></canvas></div>
                    </div>
                    <span>USED 64 Core / Total 192 Core</span>
                    <div class="wave_graph">
                        <h2>
                            Per Host
                        </h2>

                        <div>
                            <canvas id="line-chart" width="546" height="142"></canvas>
                        </div>

                    </div>
                </div>

                <div class="dash_section_contents">
                <h1>Memory</h1>
                <div class="graphs">
                    <div class="graph-wrap active-on-visible" data-active-on-visible-callback-func-name="CircleRun">
                        <div class="circle-graph2 circle-graph"> <!-- 클래스명 수정 -->
                            <strong class="circle-percent"></strong>
                        </div>
                    </div>
                    <div><canvas id="bar-chart-horizontal2"></canvas></div>
                </div>
                <span>USED 64 Core / Total 192 Core</span>
                <div class="wave_graph">
                    <h2>
                        Per Host
                    </h2>

                    <div>
                        <canvas id="line-chart2" width="546" height="142"></canvas>
                    </div>

                </div>
                </div>
                <div class="dash_section_contents" style="border-right: none;">
                <h1>CPU</h1>
                    <div class="graphs">
                        <div class="graph-wrap active-on-visible" data-active-on-visible-callback-func-name="CircleRun">
                        <div class="circle-graph3 circle-graph"> <!-- 클래스명 수정 -->
                            <strong class="circle-percent"></strong>
                        </div>
                        </div>
                        <div><canvas id="bar-chart-horizontal3"></canvas></div>
                    </div>
                    <span>USED 64 Core / Total 192 Core</span>
                    <div class="wave_graph">
                        <h2>
                            Per Host
                        </h2>

                        <div>
                        <canvas id="line-chart3" width="546" height="142"></canvas>
                        </div>

                    </div>
                </div>
            </div><!--dash section 끝-->


            <div id="bar">
                <div>
                  <span>CPU(시간 경과에 따른 CPU사용량)</span>
                  <div>d</div>
                </div>
                <div>
                  <span>MEMORY(시간 경과에 따른 Memory사용량)</span>
                  <div>d</div>
                </div>
                <div>
                  <span>Ethernet(시간 경과에 따른 Ethernet속도)</span>
                  <div>d</div>
                </div>  
            </div> 

        </div><!--dash_board끝-->

        <!--가상머신 section-->
        <div id="section" style="display: none;">
            <div class="section_header">
                <div class="section_header_left">
                    <span>가상머신</span>
                    <div>on20-ap01</div>
                    <button><i class="fa fa-exchange"></i></button>
                </div>
            
                <div class="section_header_right">
                    <div class="article_nav">
                        <button id="edit_btn">편집</button>
                        <div>
                            <button>
                                <i class="fa fa-play"></i>실행
                            </button>
                        </div>
                        <button><i class="fa fa-pause"></i>일시중지</button>
                        <div>
                            <button>
                                <i class="fa fa-stop"></i>종료
                            </button>
                        </div>
                        <div>
                            <button>
                                <i class="fa fa-repeat"></i>재부팅
                            </button>

                        </div>
                        <button><i class="fa fa-desktop"></i>콘솔</button>
                        <button>스냅샷 생성</button>
                        <button id="migration_btn">마이그레이션</button>
                        <button id="popup_btn">
                            <i class="fa fa-ellipsis-v"></i>
                            <div id="popup_box">
                                <div>
                                    <div class="get_btn">가져오기</div>
                                    <div class="get_btn">가상 머신 복제</div>
                                </div>
                                <div>
                                    <div>삭제</div>
                                </div>
                                <div>
                                    <div>마이그레이션 취소</div>
                                    <div>변환 취소</div>
                                </div>
                                <div>
                                    <div id="template_btn">템플릿 생성</div>
                                </div>
                                <div style="border-bottom: none;">
                                    <div id="domain2">도메인으로 내보내기</div>
                                    <div id="domain">Export to Data Domai</div>
                                    <div id="ova_btn">OVA로 내보내기</div>
                                </div>
                            </div>
                            
                                        
                        </button>
                    </div>
            
                </div><!--article끝-->
            </div><!--section_header끝-->

            <div class="content_outer">
                <div class="content_header">
                    <div class="content_header_left">
                        <div class="active">일반</div>
                        <div>네트워크 인터페이스</div>
                        <div>디스크</div>
                        <div>스냅샷</div>
                        <div>애플리케이션</div>
                        <div>선호도 그룹</div>
                        <div>선호도 레이블</div>
                        <div>게스트 정보</div>
                        <div>권한</div>
                        <div>이벤트</div>
                    </div>
                   
                </div>
                
                <div class="tables">
                    <div class="table_container_left">
                        <table class="table">
                            <tr>
                                <th>이름:</th>
                                <td>on20-ap01</td>
                            </tr>
                            <tr>
                                <th>설명:</th>
                                <td></td>
                            </tr>
                            <tr>
                                <th>상태:</th>
                                <td>실행 중</td>
                            </tr>
                            <tr>
                                <th>업타임:</th>
                                <td>11 days</td>
                            </tr>
                            <tr class="empty">
                                <th>.</th>
                                <td style="color: white;">.</td>
                            </tr>
                            <tr>
                                <th>템플릿:</th>
                                <td>Blank</td>
                            </tr>
                            <tr>
                                <th>운영 시스템:</th>
                                <td>Linux</td>
                            </tr>
                            <tr class="empty">
                                <th>.</th>
                                <td style="color: white;">.</td>
                            </tr>
                            <tr>
                                <th>펌웨어/장치의 유형:</th>
                                <td>BIOS의 Q35 칩셋 <i class="fa fa-ban" style="margin-left: 13%;color:orange;"></i></td>                           
                            </tr>
                            <tr>
                                <th>우선 순위:</th>
                                <td>높음</td>
                            </tr>
                            <tr>
                                <th>최적화 옵션:</th>
                                <td>서버</td>
                            </tr>
                        </table>
                    </div>
                    <div id="table_container_center">
                        <table class="table">
                            <tr>
                                <th>설정된 메모리:</th>
                                <td>2048 MB</td>
                            </tr>
                            <tr>
                                <th>할당할 실제 메모리:</th>
                                <td>2048 MB</td>
                            </tr>
                            <tr class="empty">
                                <th>.</th>
                                <td style="color: white;">.</td>
                            </tr>
                            <tr>
                                <th>게스트 OS의 여유/캐시+비퍼</th>
                                <td>1003 / 0 MB</td>
                            </tr>
                            <tr>
                                <th>된 메모리:</th>
                                <td></td>
                            </tr>
                            <tr>
                                <th>CPU 코어 수:</th>
                                <td>2(2:1:1)</td>
                            </tr>
                            <tr>
                                <th>게스트 CPU 수:</th>
                                <td>2</td>
                            </tr>
                            <tr class="empty">
                                <th>.</th>
                                <td style="color: white;">.</td>
                            </tr>
                            <tr>
                                <th>게스트 CPU</th>
                                <td>Cascadelake-Server</td>
                                <td></td>
                            </tr>
                            <tr>
                                <th>고가용성:</th>
                                <td>예</td>
                            </tr>
                            <tr>
                                <th>모니터 수:</th>
                                <td>1</td>
                            </tr>
                            <tr>
                                <th>USB:</th>
                                <td>비활성화됨</td>
                            </tr>
                        </table>
                    </div>
                    <div id="table_container_right">
                        <table class="table">
                            <tr>
                                <th>작성자:</th>
                                <td>admin</td>
                            </tr>
                            <tr>
                                <th>소스:</th>
                                <td>oVirt</td>
                            </tr>
                            <tr>
                                <th>실행 호스트:</th>
                                <td>클러스터 내의 호스트</td>
                            </tr>
                            <tr>
                                <th>사용자 정의 속성:</th>
                                <td>설정되지 않음</td>
                            </tr>
                            <tr>
                                <th>클러스터 호환 버전:</th>
                                <td>4.7</td>
                            </tr>
                            <tr>
                                <th>가상 머신의 ID:</th>
                                <td>Linuxdddddddddddddddddddddd</td>
                            </tr>
                            
                            <tr class="empty" >
                                <th>.</th>
                                <td style="color: white;">.</td>
                            </tr>
                            <tr class="empty">
                                <th>.</th>
                                <td style="color: white;">.</td>
                            </tr>
                            <tr>
                                <th>FQDN:</th>
                                <td>on20-ap01</td>
                            </tr>
                            <tr>
                                <th>하드웨어 클럭의 시간 오프셋:</th>
                                <td>Asia/Seoul</td>
                            </tr>
                        </table>
                    </div>
                </div>

            </div><!--content_outer끝-->

            <!--네트워크 인터페이스-->
            <div id="network_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div>일반</div>
                        <div class="active">네트워크 인터페이스</div>
                        <div>디스크</div>
                        <div>스냅샷</div>
                        <div>애플리케이션</div>
                        <div>선호도 그룹</div>
                        <div>선호도 레이블</div>
                        <div>게스트 정보</div>
                        <div>권한</div>
                        <div>이벤트</div>
                    </div>
                    
                </div>

                <div id="network_content_outer">
                    <div  class="content_header_right">
                        <button id="network_popup_new">새로 만들기</button>
                        <button>수정</button>
                        <button>제거</button>                      
                    </div>
                    <div class="network_content">
                        <div>
                            <i class="fa fa-chevron-right"></i>
                            <i class="fa fa-arrow-circle-o-up" style="color: #21c50b; margin-left: 0.3rem;"></i>
                            <i class="fa fa-plug"></i>
                            <i class="fa fa-usb"></i>
                            <span>nic1</span>
                        </div>
                        <div>
                            <div>네트워크 이름</div>
                            <div>ovirtmgmt</div>
                        </div>
                        <div>
                            <div>IPv4</div>
                            <div>192.168.10.147</div>
                        </div>
                        <div>
                            <div>IPv6</div>
                            <div>192.168.10.147</div>
                        </div>
                        <div style="padding-right: 3%;">
                            <div>MAC</div>
                            <div>192.168.10.147</div>
                        </div>
                        
                    </div>
                    <div class="network_content">
                        <div>
                            <i class="fa fa-chevron-right"></i>
                            <i class="fa fa-arrow-circle-o-up" style="color: #21c50b; margin-left: 0.3rem;"></i>
                            <i class="fa fa-plug"></i>
                            <i class="fa fa-usb"></i>
                            <span>nic1</span>
                        </div>
                        <div>
                            <div>네트워크 이름</div>
                            <div>ovirtmgmt</div>
                        </div>
                        <div>
                            <div>IPv4</div>
                            <div>192.168.10.147</div>
                        </div>
                        <div>
                            <div>IPv6</div>
                            <div>192.168.10.147</div>
                        </div>
                        <div style="padding-right: 3%;">
                            <div>MAC</div>
                            <div>192.168.10.147</div>
                        </div>
                        
                    </div>
                </div>

            </div><!--network_outer끝-->
            <!--네트워크인터페이스 팝업창-->
            <div class="network_popup_outer">
                <div class="network_popup">
                    <div class="network_popup_header">
                        <h1>네트워크 인터페이스 수정</h1>
                        <button><i class="fa fa-times"></i></button>
                    </div>

                    <div class="network_popup_content">
                        <div class="input_box">
                            <sapn>이름</sapn>
                            <input type="text"/>
                        </div>
                        <div class="select_box">
                            <label for="profile">프로파일</label>
                            <select id="profile">
                                <option value="test02">ovirtmgmt/ovirtmgmt</option>
                            </select>
                        </div>
                        <div class="select_box">
                            <label for="type" style="color: gray;">유형</label>
                            <select id="type" disabled>
                                <option value="test02">VirtIO</option>
                            </select>
                        </div>
                        <div class="select_box2" style="margin-bottom: 2%;">
                            <div>
                                <input type="checkbox" id="custom_mac_box" disabled>
                                <label for="custom_mac_box" style="color: gray;">
                                    사용자 지정 MAC 주소
                                </label>
                            </div>
                            <div>
                                <select id="mac_address" disabled>
                                    <option value="test02">VirtIO</option>
                                </select>
                            </div>
                        </div>

                        <div class="plug_radio_btn">
                            <span>링크 상태</span>
                            <div>
                                <div class="radio_outer">
                                    <div>
                                        <input type="radio" name="status" id="status_up">
                                        <img src=".//img/스크린샷 2024-05-24 150455.png">
                                        <label for="status_up">Up</label>
                                    </div>
                                    <div>
                                        <input type="radio" name="status" id="status_down">
                                        <img src=".//img/Down.png">
                                        <label for="status_down">Down</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="plug_radio_btn">
                            <span>카드 상태</span>
                            <div>
                                <div class="radio_outer">
                                    <div>
                                        <input type="radio" name="connection_status" id="connected">
                                        <img src=".//img/연결됨.png">
                                        <label for="connected">연결됨</label>
                                    </div>
                                    <div>
                                        <input type="radio" name="connection_status" id="disconnected">
                                        <img src=".//img/분리.png">
                                        <label for="disconnected">분리</label>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>

                    <div class="network_parameter_outer">
                        <span>네트워크 필터 매개변수</span>
                        <div>
                            <div>
                                <span>이름</span>
                                <input type="text"/>
                            </div>
                            <div>
                                <span>값</span>
                                <input type="text"/>
                            </div>
                            <div id="buttons">
                                <button>+</button>
                                <button>-</button>
                            </div>
                        </div>
                    </div>

                    <div class="edit_footer">
                        <button style="display: none;"></button>
                        <button>OK</button>
                        <button>취소</button>
                    </div>
                    

                </div>
            </div> <!--새로만들기(네트워크인터페이스) 팝업끝-->

            <!--디스크 버튼-->
            <div id="disk_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div >일반</div>
                        <div>네트워크 인터페이스</div>
                        <div class="active">디스크</div>
                        <div>스냅샷</div>
                        <div>애플리케이션</div>
                        <div>선호도 그룹</div>
                        <div>선호도 레이블</div>
                        <div>게스트 정보</div>
                        <div>권한</div>
                        <div>이벤트</div>
                    </div>
                    
                </div>
                
                <div id="disk_content">
                    <div  class="content_header_right">
                        <button id="disk_popup_new">새로 만들기</button>
                        <button id="join_popup_btn">연결</button>
                        <button>수정</button>
                        <button>제거</button>
                        <button class="content_header_popup_btn">
                            <i class="fa fa-ellipsis-v"></i>
                            <div class="content_header_popup" style="display: none;">
                                <div>활성</div>
                                <div>비활성화</div>
                                <div>이동</div>
                                <div>LUN 새로고침</div>
                            </div>
                        </button>
                    </div>
                    <div class="disk_content_header">
                        <span>디스크 유형:</span>
                        <button>모두</button>
                        <button>이미지</button>
                        <button>직접 LUN</button>
                        <button>관리되는 블록</button>                  
                    </div>

                    <table>
                        <thead>
                            <tr>
                                <th></th>
                                <th>변경</th>
                                <th><i class="fa fa-glass"></i></th>
                                <th><i class="fa fa-glass"></i></th>
                                <th><i class="fa fa-glass"></i></th>
                                <th>가상 크기</th>
                                <th>연결 대상</th>
                                <th>인터페이스</th>
                                <th>논리적 이름</th>
                                <th>상태</th>
                                <th>유형</th>
                                <th>설명</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><i class="fa fa-caret-up" style="color: #1DED00;"></i></td>
                                <td>on20-ap01</td>
                                <td><i class="fa fa-glass"></i></td>
                                <td><i class="fa fa-glass"></i></td>
                                <td><i class="fa fa-glass"></i></td>
                                <td>on20-ap01</td>
                                <td>VirtIO-SCSI</td>
                                <td>/dev/sda</td>
                                <td>OK</td>
                                <td>이미지</td>
                                <td></td>
                                <td></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                
            </div><!--disk_outer끝-->
            <!--새로만들기(디스크)팝업-->
            <div class="disk_popup_outer">
                <div class="disk_popup">
                    <div class="network_popup_header">
                        <h1>새 가상 디스크</h1>
                        <button><i class="fa fa-times"></i></button>
                    </div>
                    <div id="disk_new_nav">
                        <div id="new_img_btn">이미지</div>
                        <div id="directlun_btn">직접LUN</div>
                        <div id="managed_block_btn">관리되는 블록</div>
                    </div>
                    <!--이미지-->
                    <div class="disk_new_img">
                        <div class="disk_new_img_left">

                            <div class="img_input_box">
                                <span>크기(GIB)</span>
                                <input type="text">
                            </div>
                            <div class="img_input_box">
                                <span>별칭</span>
                                <input type="text">
                            </div>
                            <div class="img_input_box">
                                <span>설명</span>
                                <input type="text">
                            </div>
                            <div class="img_select_box">
                                <label for="os">운영 시스템</label>
                                <select id="os">
                                    <option value="linux">Linux</option>
                                </select>
                            </div>
                            <div class="img_select_box">
                                <label for="os">스토리지 도메인</label>
                                <select id="os">
                                    <option value="linux">Linux</option>
                                </select>
                            </div>
                            <div class="img_select_box">
                                <label for="os">할당 정책</label>
                                <select id="os">
                                    <option value="linux">Linux</option>
                                </select>
                            </div>
                            <div class="img_select_box">
                                <label for="os">디스크 프로파일</label>
                                <select id="os">
                                    <option value="linux">Linux</option>
                                </select>
                            </div>

                        </div>
                        <div class="disk_new_img_right">
                            <div>
                                <input type="checkbox" class="disk_activation" checked>
                                <label for="disk_activation">디스크 활성화</label>
                            </div>
                            <div>
                                <input type="checkbox" id="reset_after_deletion">
                                <label for="reset_after_deletion">삭제 후 초기화</label>
                            </div>
                            <div>
                                <input type="checkbox" class="bootable" disabled>
                                <label for="bootable" style="color: gray;">부팅 가능</label>
                            </div>
                            <div>
                                <input type="checkbox" class="shareable">
                                <label for="shareable">공유 가능</label>
                            </div>
                            <div>
                                <input type="checkbox" class="read_only">
                                <label for="read_only">읽기전용</label>
                            </div>                            
                            <div>
                                <input type="checkbox" id="cancellable">
                                <label for="cancellable">취소 활성화</label>
                            </div>
                            <div>
                                <input type="checkbox" id="incremental_backup" checked>
                                <label for="incremental_backup">중복 백업 사용</label>
                            </div>
                        </div>
                    </div><!--disk_new_img 팝업 끝-->

                    <!--직접LUN-->
                    <div id="directlun_outer" style="display: none;">
                        <div>
                            <div id="disk_managed_block_left">
                                <div class="img_input_box">
                                    <span>별칭</span>
                                    <input type="text" value="on20-ap01_Disk1">
                                </div>
                                <div class="img_input_box">
                                    <span>설명</span>
                                    <input type="text">
                                </div>
                                <div class="img_select_box">
                                    <label for="os">인터페이스</label>
                                    <select id="os">
                                        <option value="linux">Linux</option>
                                    </select>
                                </div>
                                <div class="img_select_box">
                                    <label for="os">호스트</label>
                                    <select id="os">
                                        <option value="linux">Linux</option>
                                    </select>
                                </div>
                                <div class="img_select_box">
                                    <label for="os">스토리지타입</label>
                                    <select id="os">
                                        <option value="linux">Linux</option>
                                    </select>
                                </div>
                            </div>

                            <div id="disk_managed_block_right">
                                <div>
                                    <input type="checkbox" class="disk_activation" checked>
                                    <label for="disk_activation">디스크 활성화</label>
                                </div>
                                <div>
                                    <input type="checkbox" class="bootable" disabled>
                                    <label for="bootable">부팅 가능</label>
                                </div>
                                <div>
                                    <input type="checkbox" class="shareable">
                                    <label for="shareable">공유 가능</label>
                                </div>
                                <div>
                                    <input type="checkbox" class="read_only">
                                    <label for="read_only">읽기전용</label>
                                </div>
                                <div>
                                    <input type="checkbox" class="read_only" disabled>
                                    <label for="read_only">취소 활성화</label>
                                </div>
                                <div>
                                    <input type="checkbox" class="read_only">
                                    <label for="read_only">SCSI 통과 활성화</label>
                                </div>
                                <div>
                                    <input type="checkbox" class="read_only" disabled>
                                    <label for="read_only">권한 부여</label>
                                </div>
                                <div>
                                    <input type="checkbox" class="read_only" disabled>
                                    <label for="read_only">SCSI 혜택사용</label>
                                </div>
                            </div>
                        </div>

                        <div class="target_search">
                            <div class="target_buttons">
                                <div>대상 > LUN</div>
                                <div>LUN > 대상</div>
                            </div>
                            <div class="target_info">
                                <div>
                                    <div style="margin-bottom: 0.2rem;">
                                        <span>주소</span>
                                        <input type="text">
                                    </div>
                                    <div>
                                        <span>포트</span>
                                        <input type="text">
                                    </div>
                                </div>
                                <div>
                                    <div>
                                        <input type="checkbox" class="disk_activation">
                                        <label for="disk_activation">사용자 인증</label>
                                    </div>
                                    <div>
                                        <div class="target_input_text" style="margin-bottom: 0.1rem;">
                                            <span>CHWP사용자 이름</span>
                                            <input type="text" disabled>
                                        </div>
                                        <div class="target_input_text">
                                            <span>CHAP 암호</span>
                                            <input type="text" disabled>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="disk_search_btn">
                                <div>검색</div>
                                <div>전체 로그인</div>
                            </div>
                            <div class="target_table">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>대상 이름</th>
                                            <th>주소</th>
                                            <th>포트</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr >
                                            <td>
                                                <label for="diskActivation">디스크 활성화</label>    
                                            </td>
                                            <td>
                                                <label for="diskActivation">디스크 활성화</label>                            
                                            </td>
                                            <td>
                                                <label for="diskActivation">디스크 활성화</label>                            
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>

                        </div>
                    </div>
                    <!--직접LUN끝-->
                    
                    <!--관리되는 블록-->
                    <div id="managed_block_outer" style="display: none;">
                        <div id="disk_managed_block_left">
                            <div class="img_input_box">
                                <span>크기(GIB)</span>
                                <input type="text" disabled>
                            </div>
                            <div class="img_input_box">
                                <span>별칭</span>
                                <input type="text" value="on20-ap01_Disk1" disabled>
                            </div>
                            <div class="img_input_box">
                                <span>설명</span>
                                <input type="text" disabled>
                            </div>
                            <div class="img_select_box">
                                <label for="os">할당 정책</label>
                                <select id="os" disabled>
                                    <option value="linux">Linux</option>
                                </select>
                            </div>
                            <div class="img_select_box">
                                <label for="os">디스크 프로파일</label>
                                <select id="os" disabled>
                                    <option value="linux">Linux</option>
                                </select>
                            </div>
                            <span>해당 데이터 센터에 디스크를 생성할 수 있는 권한을 갖는 사용 가능한 관리 블록 스토리지 도메인이 없습니다.</span>
                        </div>

                        <div id="disk_managed_block_right">
                            <div>
                                <input type="checkbox" class="disk_activation" disabled>
                                <label for="disk_activation">디스크 활성화</label>
                            </div>
                            <div>
                                <input type="checkbox" class="bootable" disabled>
                                <label for="bootable">부팅 가능</label>
                            </div>
                            <div>
                                <input type="checkbox" class="shareable" disabled>
                                <label for="shareable">공유 가능</label>
                            </div>
                            <div>
                                <input type="checkbox" class="read_only" disabled>
                                <label for="read_only">읽기전용</label>
                            </div>
                        </div>

                    </div>
                    <!--관리되는 블록끝-->

                    <div class="edit_footer">
                        <button style="display: none;"></button>
                        <button>OK</button>
                        <button>취소</button>
                    </div>

                </div>
            </div> <!--새로만들기(디스크) 팝업끝-->
            
            <!--연결(디스크)팝업-->
            <div id="join_popup_outer">
                <div id="join_popup">
                    <div class="network_popup_header">
                        <h1>가상 디스크 연결</h1>
                        <button><i class="fa fa-times"></i></button>
                    </div>

                    <div id="join_header">
                        <div id="join_new_nav">
                            <div id="img_btn2">이미지</div>
                            <div id="directlun_btn2">직접LUN</div>
                            <div id="managed_block_btn2">관리되는 블록</div>
                        </div>
                        <div>
                            <input type="checkbox" id="diskActivation" checked>
                            <label for="diskActivation">디스크 활성화</label>                            
                        </div>
                    </div>

                    <div id="join_img_content">
                        <table>
                            <thead>
                                <tr id="join_img_th">
                                    <th>별칭</th>
                                    <th>설명</th>
                                    <th>ID</th>
                                    <th>가상 크기</th>
                                    <th>실제 크기</th>
                                    <th>스토리지 도메인</th>
                                    <th>인터페이스</th>
                                    <th>R/O</th>
                                    <th><i class="fa fa-external-link"></i></th>
                                    <th><i class="fa fa-external-link"></i></th>
                                </tr>
                                <tr id="join_directlun_th" style="display: none;">
                                    <th>별칭</th>
                                    <th>설명</th>
                                    <th>LUN ID</th>
                                    <th>ID</th>
                                    <th>크기</th>
                                    <th>#경로</th>
                                    <th>벤더ID</th> 
                                    <th>제품ID</th>
                                    <th>시리얼</th>
                                    <th>인터페이스</th>
                                    <th>R/O</th>
                                    <th><i class="fa fa-external-link"></i></th>
                                    <th><i class="fa fa-external-link"></i></th>
                                </tr>
                                <tr id="join_managed_th" style="display: none;">
                                    <th>별칭</th>
                                    <th>설명</th>
                                    <th>ID</th>
                                    <th>가상 크기</th>
                                    <th>스토리지 도메인</th>
                                    <th>인터페이스</th> 
                                    <th>R/O</th>
                                    <th><i class="fa fa-external-link"></i></th>
                                    <th><i class="fa fa-external-link"></i></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr class="join_img_td" >
                                    <td>OK</td>
                                    <td>test02</td>
                                    <td>asd</td>
                                    <td>5</td>
                                    <td></td>
                                    <td>소프트</td>
                                    <td></td>
                                    <td>소프트</td>
                                    <td>멤버없음</td>
                                    <td>레이블없음</td>
                                </tr>
                                <tr class="join_directlun_td" style=" display: none;">
                                    <td >OK</td>
                                    <td>test02</td>
                                    <td>asd</td>
                                    <td>5</td>
                                    <td></td>
                                    <td>소프트</td>
                                    <td></td>
                                    <td>소프트</td>
                                    <td>멤버없음</td>
                                    <td>레이블없음</td>
                                    <td>레이블없음</td>
                                    <td>레이블없음</td>
                                    <td>레이블없음</td>
                                </tr>
                                <tr class="join_managed_td" style=" display: none;">
                                    <td >OK</td>
                                    <td>test02</td>
                                    <td>asd</td>
                                    <td>5</td>
                                    <td></td>
                                    <td>소프트</td>
                                    <td></td>
                                    <td>소프트</td>
                                    <td>멤버없음</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    


                    <div class="edit_footer">
                        <button style="display: none;"></button>
                        <button>OK</button>
                        <button>취소</button>
                    </div>

                </div>
            </div>
            <!--연결 디스크 팝업끝-->

            <!--스냅샷-->
            <div id="snapshot_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div >일반</div>
                        <div>네트워크 인터페이스</div>
                        <div>디스크</div>
                        <div class="active">스냅샷</div>
                        <div>애플리케이션</div>
                        <div>선호도 그룹</div>
                        <div>선호도 레이블</div>
                        <div>게스트 정보</div>
                        <div>권한</div>
                        <div>이벤트</div>
                    </div>
                    
                </div>
                

                <div id="snapshot_content_outer">

                    <div  class="content_header_right">
                        <button class="snap_create_btn">생성</button>
                        <button>미리보기</button>
                        <button>커밋</button>
                        <button>되돌리기</button>
                        <button>삭제</button>
                        <button>복제</button>
                        <button>템플릿 생성</button>
                    </div>

                    <div class="snapshot_content">
                        <div class="snapshot_content_left">
                            <div><i class="fa fa-camera"></i></div>
                            <span>Active VM</span>
                        </div>
                        <div class="snapshot_content_right">
                            <div>
                                <i class="fa fa-chevron-right"></i>
                                <span>일반</span>
                                <i class="fa fa-eye"></i>
                            </div>
                            <div>
                                <i class="fa fa-chevron-right"></i>
                                <span>디스크</span>
                                <i class="fa fa-trash-o"></i>
                            </div>
                            <div>
                                <i class="fa fa-chevron-right"></i>
                                <span>네트워크 인터페이스</span>
                                <i class="fa fa-server"></i>
                            </div>
                            <div>
                                <i class="fa fa-chevron-right"></i>
                                <span>설치된 애플리케이션</span>
                                <i class="fa fa-newspaper-o"></i>
                            </div>
                        </div>
                    </div>
                    <div class="snapshot_content">
                        <div class="snapshot_content_left">
                            <div><i class="fa fa-camera"></i></div>
                            <span>Active VM</span>
                        </div>
                        <div class="snapshot_content_right">
                            <div>
                                <i class="fa fa-chevron-right"></i>
                                <span>일반</span>
                                <i class="fa fa-eye"></i>
                            </div>
                            <div>
                                <i class="fa fa-chevron-right"></i>
                                <span>디스크</span>
                                <i class="fa fa-trash-o"></i>
                            </div>
                            <div>
                                <i class="fa fa-chevron-right"></i>
                                <span>네트워크 인터페이스</span>
                                <i class="fa fa-server"></i>
                            </div>
                            <div>
                                <i class="fa fa-chevron-right"></i>
                                <span>설치된 애플리케이션</span>
                                <i class="fa fa-newspaper-o"></i>
                            </div>
                        </div>
                    </div>

                </div>
                
            </div><!--snapshot_outer끝-->
            <!--스냅샷 팝업(생성)-->
            <div class="snap_create_outer" style="display: none;">
                <div class="snap_create">
                    <div class="network_popup_header">
                        <h1>스냅샷 생성</h1>
                        <button><i class="fa fa-times"></i></button>
                    </div>
                    <div class="snap_create_inputbox">
                        <span>크기(GIB)</span>
                        <input type="text">
                    </div>
                    
                    <div id="snap_create_table">
                        <span>포함할 디스크:</span>
                        <div>
                            <table>
                                <thead>
                                    <tr>
                                        <th></th>
                                        <th>별칭</th>
                                        <th>설명</th>
                                    
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr >
                                        <td>
                                            <input type="checkbox" id="diskActivation" checked>  
                                        </td>
                                        <td>
                                            <label for="diskActivation">디스크 활성화</label>                            
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>

                    </div>
                    <div id="snap_create_warning">
                        <i class="fa fa-exclamation" style="color: orange;"></i>
                        <span>메모리를 저장하는 도중 가상 머신이 중지됨</span>
                    </div>
                    <div class="edit_footer">
                        <button style="display: none;"></button>
                        <button>OK</button>
                        <button>취소</button>
                    </div>
                </div>
            </div>
            <!--스냅샷 팝업(생성)창끝-->

            <!--애플리케이션-->
            <div id="application_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div >일반</div>
                        <div>네트워크 인터페이스</div>
                        <div>디스크</div>
                        <div>스냅샷</div>
                        <div class="active">애플리케이션</div>
                        <div>선호도 그룹</div>
                        <div>선호도 레이블</div>
                        <div>게스트 정보</div>
                        <div>권한</div>
                        <div>이벤트</div>
                    </div>
                </div>
                
                <div id="application_content">
                    <div class="application_content_header">
                        <button><i class="fa fa-chevron-left"></i></button>
                        <div>1-2</div>
                        <button><i class="fa fa-chevron-right"></i></button>
                        <button><i class="fa fa-ellipsis-v"></i></button>            
                    </div>

                    <table>
                        <thead>
                            <tr>
                                <th>설치된 애플리케이션</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>kernel-3.10.0-1062.el7.x86_64</td>
                            </tr>
                            <tr>
                                <td>qemu-guest-agent-2.12.0</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                
            </div><!--application_outer끝-->

            <!--선호도 그룹-->
            <div id="pregroup_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div >일반</div>
                        <div>네트워크 인터페이스</div>
                        <div>디스크</div>
                        <div>스냅샷</div>
                        <div>애플리케이션</div>
                        <div class="active">선호도 그룹</div>
                        <div>선호도 레이블</div>
                        <div>게스트 정보</div>
                        <div>권한</div>
                        <div>이벤트</div>
                    </div>
                    
                </div>
                
                <div class="pregroup_content">
                    <div  class="content_header_right">
                        <button id="pregroup_create_btn">새로만들기</button>
                        <button>편집</button>
                        <button>제거</button>
                    </div>
                    <div class="application_content_header">
                        <button><i class="fa fa-chevron-left"></i></button>
                        <div>1-2</div>
                        <button><i class="fa fa-chevron-right"></i></button>
                        <button><i class="fa fa-ellipsis-v"></i></button>            
                    </div>

                    <table>
                        <thead>
                            <tr>
                                <th>상태</th>
                                <th>이름</th>
                                <th>설명</th>
                                <th>우선 순위</th>
                                <th>가상 머신 축 극성</th>
                                <th>가상 머신 강제 적ㅇㅇㅇ</th>
                                <th>호스트 축 극성</th>
                                <th>호스트 강제 적용</th>
                                <th>가상 머신 멤버</th>
                                <th>가상 머신 레이블</th>
                                <th>내용1</th>
                                <th>내용2</th>
                                <th>내용3</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td style="text-align: center;">
                                    <i class="fa fa-exclamation"></i>
                                    중단
                                </td>
                                <td>test02</td>
                                <td>asd</td>
                                <td>5</td>
                                <td></td>
                                <td>소프트</td>
                                <td></td>
                                <td>소프트</td>
                                <td>멤버없음</td>
                                <td>레이블없음</td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td style="text-align: center;">OK</td>
                                <td>test02</td>
                                <td>asd</td>
                                <td>5</td>
                                <td></td>
                                <td>소프트</td>
                                <td></td>
                                <td>소프트</td>
                                <td>멤버없음</td>
                                <td>레이블없음</td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div><!--pregroup_outer끝-->
            <!--선호도그룹(새로만들기)팝업-->
            <div class="pregroup_create_outer" style="display: none;">
                <div class="pregroup_create">
                    <div class="network_popup_header">
                        <h1>새 선호도 그룹</h1>
                        <button><i class="fa fa-times"></i></button>
                    </div>

                    <div id="pregroup_create_content">
                        <div class="snap_create_inputbox">
                            <span>이름</span>
                            <input type="text">
                        </div>
                        <div class="snap_create_inputbox">
                            <span>설명</span>
                            <input type="text">
                        </div>
                        <div class="snap_create_inputbox" style="padding-left: 0.34rem;">
                            <div>
                                <span>우선 순위</span>
                                <i class="fa fa-info-circle"></i>
                            </div>
                            <input type="text">
                        </div>
                        <div class="snap_create_inputbox" style="padding-left: 0.34rem;">
                            <div>
                                <label for="disk_profile">가상 머신 선호도 규칙</label>
                                <i class="fa fa-info-circle"></i>
                            </div>
                            <div class="pregroup_create_select">
                                <div>
                                    <select id="disk_profile">
                                        <option value="disabled">비활성화됨</option>
                                    </select>
                                </div>
                                <div>
                                    <input type="checkbox" id="enforce_disk_profile">
                                    <label for="enforce_disk_profile">강제 적용</label>
                                </div>
                            </div>
                        </div>
                        <div class="snap_create_inputbox" style="padding-left: 0.34rem;">
                            <div>
                                <label for="host_preference_rule">호스트 선호도 규칙</label>
                                <i class="fa fa-info-circle"></i>
                            </div>
                            <div class="pregroup_create_select">
                                <div>
                                    <select id="host_preference_rule">
                                        <option value="disabled">비활성화됨</option>
                                    </select>
                                </div>
                                <div>
                                    <input type="checkbox" id="enforce_rule">
                                    <label for="enforce_rule">강제 적용</label>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="pregroup_create_buttons">
                        <div class="pregroup_buttons_content">
                            <label for="cluster">가상머신</label>
                            <div class="pregroup_buttons_select">
                                <div>
                                    <select id="cluster">
                                        <option value="default">가상머신:on20-ap01</option>   
                                    </select>
                                </div>
                                <div>
                                    <button>+</button>
                                    <button>-</button>
                                </div>
                            </div>
                        </div>


                        <div class="pregroup_buttons_content">
                            <label for="cluster">호스트</label>
                            <div class="pregroup_buttons_select">
                                <div>
                                    <select id="cluster">
                                        <option value="default">호스트 선택</option>   
                                    </select>
                                </div>
                                <div>
                                    <button>+</button>
                                    <button>-</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="edit_footer">
                        <button style="display: none;"></button>
                        <button>OK</button>
                        <button>취소</button>
                    </div>
                </div>
            </div>
            <!--선호도그룹(새로만들기)팝업 끝-->
            <!--선호도 레이블-->
            <div id="pregroup_lable_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div >일반</div>
                        <div>네트워크 인터페이스</div>
                        <div>디스크</div>
                        <div>스냅샷</div>
                        <div>애플리케이션</div>
                        <div>선호도 그룹</div>
                        <div class="active">선호도 레이블</div>
                        <div>게스트 정보</div>
                        <div>권한</div>
                        <div>이벤트</div>
                    </div>
                    
                </div>
                
                <div class="pregroup_content">
                    <div  class="content_header_right">
                        <button id="lable_create_btn">새로 만들기</button>
                        <button>편집</button>
                        <button>제거</button>
                    </div>
                    <div class="application_content_header">
                        <button><i class="fa fa-chevron-left"></i></button>
                        <div>1-2</div>
                        <button><i class="fa fa-chevron-right"></i></button>
                        <button><i class="fa fa-ellipsis-v"></i></button>            
                    </div>

                    <table>
                        <thead>
                            <tr>
                                <th>이름</th>
                                <th>가상머신 멤버</th>
                                <th>호스트 멤버</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>test</td>
                                <td>HostedEngine</td>
                                <td>host02.ititinfo.com</td>
                                <td style="text-align: right;"><i class="fa fa-caret-up"></i></td>
                            </tr>
                            <tr>
                                <td>test</td>
                                <td>HostedEngine</td>
                                <td>host02.ititinfo.com</td>
                                <td style="text-align: right;"><i class="fa fa-caret-up"></i></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div><!--pregroup_lable_outer끝-->
            <!--선호도 레이블(새로만들기)팝업창-->
            <div class="lable_create_outer" style="display: none;">
                <div class="lable_create">
                    <div class="network_popup_header">
                        <h1>새로운 선호도 레이블</h1>
                        <button><i class="fa fa-times"></i></button>
                    </div>

                    <div>
                        <div class="snap_create_inputbox" style="padding: 0.5rem;">
                            <span>이름</span>
                            <input type="text">
                        </div>
                    </div>

                    <div class="pregroup_create_buttons" style="padding-top: 0;">
                        <div class="pregroup_buttons_content">
                            <label for="cluster">가상머신</label>
                            <div class="pregroup_buttons_select">
                                <div>
                                    <select id="cluster">
                                        <option value="default">가상머신:on20-ap01</option>   
                                    </select>
                                </div>
                                <div>
                                    <button>+</button>
                                    <button>-</button>
                                </div>
                            </div>
                        </div>


                        <div class="pregroup_buttons_content">
                            <label for="cluster">호스트</label>
                            <div class="pregroup_buttons_select">
                                <div>
                                    <select id="cluster">
                                        <option value="default">호스트 선택</option>   
                                    </select>
                                </div>
                                <div>
                                    <button>+</button>
                                    <button>-</button>
                                </div>
                            </div>
                        </div>
                    </div>



                    <div class="edit_footer">
                        <button style="display: none;"></button>
                        <button>OK</button>
                        <button>취소</button>
                    </div>
                </div>
            </div>
            <!--선호도 레이블(새로만들기)팝업 끝-->

            <!--게스트 정보-->
            <div id="guest_info_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div >일반</div>
                        <div>네트워크 인터페이스</div>
                        <div>디스크</div>
                        <div>스냅샷</div>
                        <div>애플리케이션</div>
                        <div>선호도 그룹</div>
                        <div>선호도 레이블</div>
                        <div class="active">게스트 정보</div>
                        <div>권한</div>
                        <div>이벤트</div>
                    </div>
                </div>
                
                <div class="tables">
                    <div class="table_container_left">
                        <table class="table">
                            <tr>
                                <th>유형:</th>
                                <td>Linux</td>
                            </tr>
                            <tr>
                                <th>아키텍쳐:</th>
                                <td>x86_64</td>
                            </tr>
                            <tr>
                                <th>운영체제:</th>
                                <td>CentOS Linux 7</td>
                            </tr>
                            <tr>
                                <th>커널 버전</th>
                                <td>3.10.0-1062.el7_x86_64</td>
                            </tr>
                        </table>
                    </div>
                    <div class="table_container_center">
                        <table class="table">
                            <tr>
                                <th>시간대:</th>
                                <td>KST (UTC + 09:00)</td>
                            </tr>
                        </table>
                    </div>
                    <div class="table_container_right">
                        <table class="table">
                            <tr>
                                <th>로그인된 사용자:</th>
                                <td></td>
                            </tr>
                            <tr>
                                <th>콘솔 사용자:</th>
                                <td></td>
                            </tr>
                            <tr>
                                <th>콘솔 클라이언트 IP:</th>
                                <td></td>
                            </tr>
                        </table>
                    </div>
                </div>
                
            </div><!--게스트정보 끝-->

            <!--권한-->
            <div id="power_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div >일반</div>
                        <div>네트워크 인터페이스</div>
                        <div>디스크</div>
                        <div>스냅샷</div>
                        <div>애플리케이션</div>
                        <div>선호도 그룹</div>
                        <div>선호도 레이블</div>
                        <div>게스트 정보</div>
                        <div class="active">권한</div>
                        <div>이벤트</div>
                    </div>
                    
                </div>
                
                <div class="pregroup_content">
                    <div  class="content_header_right">
                        <button id="power_add_btn">추가</button>
                        <button>제거</button>
                    </div>
                    <div class="application_content_header">
                        <span>Permission Filters:</span>
                        <button>All</button>
                        <button>Direct</button>            
                    </div>

                    <table>
                        <thead>
                            <tr>
                                <th></th>
                                <th>사용자</th>
                                <th>인증 공급자</th>
                                <th>네임스페이스</th>
                                <th>역할</th>
                                <th>생성일</th>
                                <th>Inherited From</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><i class="fa fa-user"></i></td>
                                <td>ovirt-administrator</td>
                                <td></td>
                                <td>*</td>
                                <td>SuperUser</td>
                                <td>2023. 12. 29. AM 11:40:58</td>
                                <td>(시스템)</td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-user"></i></td>
                                <td>admin (admin)</td>
                                <td>internal-authz</td>
                                <td>*</td>
                                <td>SuperUser</td>
                                <td>2023. 12. 29. AM 11:40:58</td>
                                <td>(시스템)</td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-user"></i></td>
                                <td>Everyone</td>
                                <td></td>
                                <td>*</td>
                                <td>UserProfileEditor</td>
                                <td>2017. 3. 16. PM 6:52:29</td>
                                <td>(시스템)</td>
                            </tr>
                        </tbody>
                    </table>
                    
                    
                </div>
            </div><!--power_outer(권한)끝-->
            <!--권한(추가)팝업창-->
            <div class="power_add_outer" style="display: none;">
                <div class="power_add">
                    <div class="network_popup_header">
                        <h1>사용자에게 권한 추가</h1>
                        <button><i class="fa fa-times"></i></button>
                    </div>

                    <div class="power_radio_group">
                        <input type="radio" id="user" name="option" checked>
                        <label for="user">사용자</label>
                        
                        <input type="radio" id="group" name="option">
                        <label for="group">그룹</label>
                        
                        <input type="radio" id="all" name="option">
                        <label for="all">모두</label>
                        
                        <input type="radio" id="my_group" name="option">
                        <label for="my_group">내 그룹</label>
                    </div>

                    <div class="power_contents_outer">
                        <div>
                            <label for="cluster">검색:</label>
                            <select id="cluster">
                                <option value="default">Default</option>   
                            </select>
                        </div>
                        <div>
                            <label for="cluster">네임스페이스:</label>
                            <select id="cluster">
                                <option value="default">Default</option>   
                            </select>
                        </div>
                        <div>
                            <label for="cluster" style="color: white;">.</label>
                            <select id="cluster">
                                <option value="default">Default</option>   
                            </select>
                        </div>
                        <div>
                            <div style="color: white;">.</div>
                            <input type="submit" value="검색">
                        </div>
                    </div>
                    

                    <div class="power_table">
                        <table>
                            <thead>
                                <tr>
                                    <th>이름</th>
                                    <th>성</th>
                                    <th>사용자 이름</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>dddddddddddddddddddddd</td>
                                    <td>2024. 1. 17. PM 3:14:39</td>
                                    <td>Snapshot 'on2o-ap01-Snapshot-2024_01_17' been completed.</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="power_last_content">
                        <label for="cluster">할당된 역할:</label>
                        <select id="cluster"  style="width: 65%;">
                            <option value="default">UserRole</option>   
                        </select>
                    </div>

                    



                    <div class="edit_footer">
                        <button style="display: none;"></button>
                        <button>OK</button>
                        <button>취소</button>
                    </div>
                </div>
            </div>
            <!--권한(추가)팝업창 끝-->

            <!--이벤트-->
            <div id="event_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div >일반</div>
                        <div>네트워크 인터페이스</div>
                        <div>디스크</div>
                        <div>스냅샷</div>
                        <div>애플리케이션</div>
                        <div>선호도 그룹</div>
                        <div>선호도 레이블</div>
                        <div>게스트 정보</div>
                        <div>권한</div>
                        <div class="active">이벤트</div>
                    </div>
                    
                </div>
                
                <div class="pregroup_content">
                    <div  class="content_header_right">
                        <button>새로 만들기</button>
                        <button>편집</button>
                        <button>제거</button>
                    </div>
                    <div class="application_content_header">
                        <button><i class="fa fa-chevron-left"></i></button>
                        <div>1-2</div>
                        <button><i class="fa fa-chevron-right"></i></button>
                        <button><i class="fa fa-ellipsis-v"></i></button>            
                    </div>

                    <table>
                        <thead>
                            <tr>
                                <th></th>
                                <th>시간</th>
                                <th>메세지</th>
                                <th>상관 관계 ID</th>
                                <th>소스</th>
                                <th>사용자 지정 이벤트 ID</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><i class="fa fa-check"></i></td>
                                <td>2024. 1. 17. PM 3:14:39</td>
                                <td>Snapshot 'on2o-ap01-Snapshot-2024_01_17' creation for 'VM on2o-ap01' has been completed.</td>
                                <td>4b4b417a-c...</td>
                                <td>oVirt</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-check"></i></td>
                                <td>2024. 1. 17. PM 3:14:21</td>
                                <td>Snapshot 'on2o-ap01-Snapshot-2024_01_17' creation for 'VM on2o-ap01' was initiated by admin@intern...</td>
                                <td>4b4b417a-c...</td>
                                <td>oVirt</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-times"></i></td>
                                <td>2024. 1. 5. AM 8:37:54</td>
                                <td>Failed to restart VM on2o-ap01 on host host01.ititinfo.com</td>
                                <td>3400e0dc</td>
                                <td>oVirt</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-times"></i></td>
                                <td>2024. 1. 5. PM 8:37:10</td>
                                <td>VM on2o-ap01 is down with error. Exit message: VM terminated with error.</td>
                                <td>3400e0dc</td>
                                <td>oVirt</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-check"></i></td>
                                <td>2024. 1. 5. PM 8:34:29</td>
                                <td>Trying to restart VM on2o-ap01 on host host01.ititinfo.com</td>
                                <td>3400e0dc</td>
                                <td>oVirt</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-exclamation"></i></td>
                                <td>2024. 1. 5. PM 8:29:10</td>
                                <td>VM on2o-ap01 was set to the Unknown status.</td>
                                <td>3400e0dc</td>
                                <td>oVirt</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-check"></i></td>
                                <td>2023. 12. 29. PM 12:55:08</td>
                                <td>VM on2o-ap01 started on Host host01.ititinfo.com</td>
                                <td>a99b6ae8-8d...</td>
                                <td>oVirt</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-check"></i></td>
                                <td>2023. 12. 29. PM 12:54:48</td>
                                <td>VM on2o-ap01 was started by admin@internal-authz (Host: host01.ititinfo.com).</td>
                                <td>a99b6ae8-8d...</td>
                                <td>oVirt</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-check"></i></td>
                                <td>2023. 12. 29. PM 12:54:18</td>
                                <td>VM on2o-ap01 configuration was updated by admin@internal-authz.</td>
                                <td>e3b8355e-06...</td>
                                <td>oVirt</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-check"></i></td>
                                <td>2023. 12. 29. PM 12:54:15</td>
                                <td>VM on2o-ap01 configuration was updated by admin@internal-authz.</td>
                                <td>793fb95e-6df...</td>
                                <td>oVirt</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-check"></i></td>
                                <td>2023. 12. 29. PM 12:53:53</td>
                                <td>VM on2o-ap01 has been successfully imported from the given configuration.</td>
                                <td>ede53bc8-c6...</td>
                                <td>oVirt</td>
                                <td></td>
                            </tr>
                        </tbody>
                    </table>
                    
                </div>
            </div><!--event_outer(이벤트)끝-->


            <!--footer-->
            <div class="footer_outer">
                <div class="footer">
                    <button><i class="fa fa-chevron-down"></i></button>
                    <div>
                        <a>최근 작업</a>
                        <a>경보</a>
                    </div>
                </div>
                <div class="footer_content">
    
                    <div class="footer_nav">
                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>
    
                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>
    
                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>
    
                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>
    
                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>
    
                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>
    
                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>
    
                        <div style="border-right: none;">
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>
                    </div>
    
                    <div class="footer_img">
                        <img src="img/화면 캡처 2024-04-30 164511.png">
                        <span>항목을 찾지 못했습니다</span>
                    </div>
    
                </div>
            </div><!--footer끝-->
        </div><!--section(중간)끝-->
        
        <!--팝업창들-->
<!--가져오기-->
<div id="get_popup_bg">
    <div id="get_popup">
        <div class="domain_header">
            <h1>가상머신 가져오기</h1>
            <button><i class="fa fa-times"></i></button>
        </div>

        <div id="get_article1">
            <div>
                <label for="center">데이터 센터</label>
                <select name="data_center" id="center">
                    <option value="javascript">Default</option>
                    <option value="php">PHP</option>
                    <option value="java">Java</option>
                </select>
                
            </div>
            <div>
                <label for="source">소스</label>
                <select name="data_source" id="source">
                    <option value="javascript">가상 어플라이언스(OVA)</option>
                    <option value="php">PHP</option>
                    <option value="java">Java</option>
                </select>
            </div>

        </div>

        <div id="get_article2">
            <div>
                <label for="get_host">호스트</label>
                <select name="get_popup_host" id="get_host">
                    <option value="javascript">host01.ititinfo.com</option>
                    <option value="php">PHP</option>
                    <option value="java">Java</option>
                </select>
            </div>
            <div id="file_path">
                <div>
                    <span>파일경로</span>
                    <i class="fa fa-info-circle" style="color: blue;"></i>
                </div>
                <input type="text"/>
            </div>

            <button>로드</button>

            <div id="get_article3">
                <div class="get_boxs">
                    <span>소스 상의 가상 머신</span>
                    <div class="get_box">
                        <div>
                            <input type="checkbox" id="get_name_source">
                            <label for="get_name_source">이름</label>
                        </div>
                        <div>
                            
                        </div>
                    </div>
                </div>

                <div>
                    <i class="fa fa-arrow-right"></i>
                    <i class="fa fa-arrow-left"></i>
                </div>

                <div>
                    <span>가져오기할 가상 머신</span>
                    <div class="get_box">
                        <div>
                            <input type="checkbox" id="get_name_source">
                            <label for="get_name_source">이름</label>
                        </div>
                        <div>
                            
                        </div>
                    </div>
                </div>

            </div>
        </div><!--get-article2끝-->

        <div id="get_footer">
            <div >
                <button>다음</button>
                <button id="get_footer_btn">취소</button>
            </div>
        </div>

    </div>
</div>
<!--템플릿 생성-->
<div id="template_bg">
    <div id="template_popup">
        <div class="domain_header">
            <h1>새 템플릿</h1>
            <button><i class="fa fa-times"></i></button>
        </div>

        <div id="template_article_outer">
            <div class="template_content">
                <span>이름</span>
                <input type="text"/>
            </div>

            <div class="template_content">
                <span>설명</span>
                <input type="text"/>
            </div>

            <div class="template_content">
                <span>코멘트</span>
                <input type="text"/>
            </div>

            <div class="template_content">
                <label for="lang" style="font-size: 0.36rem;">클러스터</label>
                <select name="languages" id="lang">
                    <option value="javascript">JavaScript</option>
                    <option value="php">PHP</option>
                    <option value="java">Java</option>
                </select>
            </div>

            <div class="template_content">
                <label for="lang" style="font-size: 0.36rem;">CPU프로파일</label>
                <select name="languages" id="lang">
                    <option value="javascript">JavaScript</option>
                    <option value="php">PHP</option>
                    <option value="java">Java</option>
                </select>
            </div>
        </div>

        <div id="template_article_outer2">
            <span>디스크할당 :</span>

            <div id="template_boxs">
                <div id="template_boxs_header">
                    <div>별칭</div>
                    <div>가상 크기</div>
                    <div>포맷</div>
                    <div>대상</div>
                    <div>디스크 프로파일</div>
                </div>
                
                <div id="template_boxs_nav">
                    <div>on20-ap02</div>
                    <div>200 GIB</div>
                    
                        <select name="languages" id="lang">
                            <option value="javascript">JavaScript</option>
                            <option value="php">PHP</option>
                            <option value="java">Java</option>
                        </select>
                    
                        <select name="languages" id="lang">
                            <option value="javascript">JavaScript</option>
                            <option value="php">PHP</option>
                            <option value="java">Java</option>
                        </select>
                    
                    
                        <select name="languages" id="lang">
                            <option value="javascript">JavaScript</option>
                            <option value="php">PHP</option>
                            <option value="java">Java</option>
                        </select>
                    
                    
                </div>
            </div>

            <div id="template_article_footer">
                <div class="checkbox-group">
                    <div>
                        <input type="checkbox" id="template_checkbox"/>
                        <label for="template_checkbox">모든 사용자에게 이 템플릿 접근을 허용</label>
                    </div>
                    <div>
                        <input type="checkbox" id="template_checkbox2"/>
                        <label for="template_checkbox2">가상 머신 권한 복사</label>
                    </div>
                </div>
            </div>
        </div>

      
        <div id="template_footer">
            <div>
                <button>OK</button>
                <button id="template_footer_btn">취소</button>
            </div>
        </div>
            

    </div>
</div>
<!--Export to Data Domain-->
<div id="popup_bg">
    <div id="domain_popup">
        <div class="domain_header">
            <h1>가상머신 내보내기</h1>
            <button><i class="fa fa-times"></i></button>
        </div>
        <div id="domain_article_outer">
            <div class="domain_article">
                <div>가상 머신 이름</div>
                <div style="background-color: #EDEDED;">on20-ap02</div>
            </div>
            <div class="domain_article">
                <div>내보내기된 가상 머신 이름</div>
                <div>on20-ap02</div>
            </div>
            <div class="domain_article">
                <label for="lang" style="font-size: 0.36rem;">스토리지 도메인</label>
                <select name="languages" id="lang">
                    <option value="javascript">JavaScript</option>
                    <option value="php">PHP</option>
                    <option value="java">Java</option>
                </select>
            </div>
            <div id="domain_checkbox">
                <input type="checkbox" id="snapshot">
                <label for="snapshot">스냅샷 축소</label>
            </div>
            <div id="domain_footer">
                <a>Export to Data Domain</a>
                <a>취소</a>
            </div>
        </div>
    </div>
</div>

<!--도메인으로 내보내기-->
<div id="popup_bg2">
    <div id="domain_popup2">
        <div class="domain_header">
            <h1>가상머신 내보내기</h1>
            <button><i class="fa fa-times"></i></button>
        </div>
        <div id="domain2_article_outer">

            <div id="domain2_checkboxs">
                <div>
                    <input type="checkbox" id="coercion">
                    <label for="coercion">강제 적용</label>
                </div>
                <div>
                    <input type="checkbox" id="snapshot2">
                    <label for="snapshot2">스냅샷 축소</label>
                </div>
            </div>

            <div id="warning">
                <i class="fa fa-ban"></i>
                <div>
                    가상머신을 백업할 내보내기 도메인이 없습니다.<br>가상머신의 데이터 센터에 내보내기 도메인을 연결하십시오.
                </div>
            </div>

        </div>

        <div id="domain2_footer">
            <button>종료</button>
        </div>
    </div>
</div>

<!--마이그레이션-->
<div id="migration_popup_outer">
    <div id="migration_popup">
        <div class="domain_header">
            <h1>가상머신 마이그레이션</h1>
            <button><i class="fa fa-times"></i></button>
        </div>
        <div id="migration_article_outer">
            <span>1대의 가상 머신이 마이그레이션되는 호스트를 선택하십시오.</span>
            <div id="migration_article">
                <div>
                    <div id="migration_dropdown">
                        <label for="host">대상 호스트 <i class="fa fa-info-circle"></i></label>
                        <select name="host_dropdown" id="host">
                            <option value="">호스트 자동 선택</option>
                            <option value="">PHP</option>
                            <option value="">Java</option>
                        </select>
                    </div>
                </div>
                <div>
                    <div>가상머신</div>
                    <div>on20-ap02</div>
                </div>
            </div>
            <div id="migration_footer">
                <a>마이그레이션</a>
                <a>취소</a>
            </div>
        </div>
    </div>
</div> 


<!--OVA로 내보내기-->
<div id="ova_outer">
    <div id="ova_popup">

        <div class="domain_header">
            <h1>가상 어플라이언스로 가상 머신 내보내기</h1>
            <button><i class="fa fa-times"></i></button>
        </div>

        <div id="ova_article_outer">

            <div id="ova_dropdown">
                <label for="ova_host">호스트</label>
                <select name="ova_host_dropdown" id="ova_host">
                    <option value="">host01.ititinfo.com</option>
                    <option value="">PHP</option>
                    <option value="">Java</option>
                </select>
            </div>

            <div>
                <span>디렉토리</span>
                <input type="text">
            </div>

            <div>
                <span>이름</span>
                <div>on20-ap02.ova</div>
            </div>
        </div>

        <div id="ova_footer">
            <button>OK</button>
            <button>취소</button>
        </div>

    </div>
</div>




        <!--스토리지 section-->
        <div id="storage_section">
            <div class="section_header">
                <div class="section_header_left">
                    <span>데이터 센터</span>
                    <div>Default</div>
                    <button><i class="fa fa-exchange"></i></button>
                </div>
            
                <div class="section_header_right">
                    <div class="article_nav">
                        <button>편집</button>
                        <button>삭제</button>
                        <button id="popup_btn">
                            <i class="fa fa-ellipsis-v"></i>
                            <div id="popup_box">
                                <div>
                                    <div class="get_btn">가져오기</div>
                                    <div class="get_btn">가상 머신 복제</div>
                                </div>
                                <div>
                                    <div>삭제</div>
                                </div>
                                <div>
                                    <div>마이그레이션 취소</div>
                                    <div>변환 취소</div>
                                </div>
                                <div>
                                    <div id="template_btn">템플릿 생성</div>
                                </div>
                                <div style="border-bottom: none;">
                                    <div id="domain2">도메인으로 내보내기</div>
                                    <div id="domain">Export to Data Domai</div>
                                    <div id="ova_btn">OVA로 내보내기</div>
                                </div>
                            </div> 
                        </button>
                    </div>
            
                </div><!--article끝-->
            </div><!--section_header끝-->

            <div class="content_outer">
                <div class="content_header">
                    <div class="content_header_left">
                        <div class="active">디스크</div>
                        <div>도메인</div>
                        <div>볼륨</div>
                        <div>스토리지</div>
                        <div>논리 네트워크</div>
                        <div>클러스터</div>
                        <div>권한</div>
                        <div>이벤트</div>
                    </div>
                    
                </div>

                <div class="section_content_outer">
                    <div class="content_header_right">
                        <button id="storage_disk_new_btn">새로 만들기</button>
                        <button>수정</button>
                        <button>제거</button>
                        <button>이동</button>
                        <button>복사</button>
                        <button id="storage_disk_upload">업로드</button>
                        <button>다운로드</button>
                        <button class="content_header_popup_btn">
                            <i class="fa fa-ellipsis-v"></i>
                            <div class="content_header_popup" style="display: none;">
                                <div>활성</div>
                                <div>비활성화</div>
                                <div>이동</div>
                                <div>LUN 새로고침</div>
                            </div>
                        </button>
                    </div>
                    <div class="section_option">
                        <div>
                            <label for="disk_type">디스크 유형:</label>
                            <select id="disk_type">
                                <option value="default">모두</option>   
                                <option value="image">이미지</option> 
                                <option value="direct_lun">직접LUN</option> 
                                <option value="managed_block">관리되는 블록</option> 
                            </select>
                        </div>
                        <div>
                            <label for="content_type">컨텐츠 유형:</label>
                            <select id="content_type">
                                <option value="default">모두</option>   
                            </select>
                        </div>
                        <div class="search_box">
                            <input type="text"/>
                            <button><i class="fa fa-search"></i></button>
                        </div>
                    </div>

                    <div class="section_table_outer">
                        <button><i class="fa fa-refresh"></i></button>
                        <table>
                            <thead>
                                <tr>
                                    <th>별칭</th>
                                    <th>ID</th>
                                    <th><i class="fa fa-glass"></i></th>
                                    <th></th>
                                    <th>연결 대상</th>
                                    <th>스토리지 도메인</th>
                                    <th>가상 크기</th>
                                    <th>상태</th>
                                    <th>유형</th>
                                    <th>설명</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>he_metadata</td>
                                    <td style="width: 10%;">289137398279301798</td>
                                    <td></td>
                                    <td><i class="fa fa-glass"></i></td>
                                    <td>on20-ap01</td>
                                    <td>VirtIO-SCSI</td>
                                    <td>/dev/sda</td>
                                    <td>OK</td>
                                    <td>이미지</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>he_metadata</td>
                                    <td>289137398279301798</td>
                                    <td></td>
                                    <td><i class="fa fa-glass"></i></td>
                                    <td>on20-ap01</td>
                                    <td>VirtIO-SCSI</td>
                                    <td>/dev/sda</td>
                                    <td>OK</td>
                                    <td>이미지</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>he_metadata</td>
                                    <td style="width: 10%;">289137398279301798</td>
                                    <td></td>
                                    <td><i class="fa fa-glass"></i></td>
                                    <td>on20-ap01</td>
                                    <td>VirtIO-SCSI</td>
                                    <td>/dev/sda</td>
                                    <td>OK</td>
                                    <td>이미지</td>
                                    <td></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                 <!--스토리지 우클릭메뉴박스-->
                <div id="storage_context_menu">
                    <div>새로 만들기</div>
                    <div>새로운 도메인</div>
                    <div>도메인 가져오기</div>
                    <div>도메인 관리</div>
                    <div>삭제</div>
                    <div>Connections</div>
                </div>
            </div><!--section_content_outer끝-->

            <!--스토리지 디스크 새로만들기팝업-->
            <div class="storage_disk_new_outer">
                <div class="storage_disk_new_popup">
                    <div class="network_popup_header">
                        <h1>새 가상 디스크</h1>
                        <button><i class="fa fa-times"></i></button>
                    </div>
                    <div id="disk_new_nav">
                        <div id="storage_img_btn">이미지</div>
                        <div id="storage_directlun_btn">직접LUN</div>
                        <div id="storage_managed_btn">관리되는 블록</div>
                    </div>
                    <div class="disk_new_img">
                        <div class="disk_new_img_left">
                            <div class="img_input_box">
                                <span>크기(GIB)</span>
                                <input type="text">
                            </div>
                            <div class="img_input_box">
                                <span>별칭</span>
                                <input type="text">
                            </div>
                            <div class="img_input_box">
                                <span>설명</span>
                                <input type="text">
                            </div>
                            <div class="img_select_box">
                                <label for="os">데이터 센터</label>
                                <select id="os">
                                    <option value="linux">Linux</option>
                                </select>
                            </div>
                            <div class="img_select_box">
                                <label for="os">스토리지 도메인</label>
                                <select id="os">
                                    <option value="linux">Linux</option>
                                </select>
                            </div>
                            <div class="img_select_box">
                                <label for="os">할당 정책</label>
                                <select id="os">
                                    <option value="linux">Linux</option>
                                </select>
                            </div>
                            <div class="img_select_box">
                                <label for="os">디스크 프로파일</label>
                                <select id="os">
                                    <option value="linux">Linux</option>
                                </select>
                            </div>
                        </div>
                        <div class="disk_new_img_right">
                            <div>
                                <input type="checkbox" id="reset_after_deletion">
                                <label for="reset_after_deletion">삭제 후 초기화</label>
                            </div>
                            <div>
                                <input type="checkbox" class="shareable">
                                <label for="shareable">공유 가능</label>
                            </div>
                            <div>
                                <input type="checkbox" id="incremental_backup" checked>
                                <label for="incremental_backup">중복 백업 사용</label>
                            </div>
                        </div>
                    </div><!--disk_new_img 팝업 끝-->

                    <!--직접LUN-->
                    <div id="storage_directlun_outer" style="display: none;">
                        <div id="storage_lun_first">
                            <div class="disk_new_img_left">
                                <div class="img_input_box">
                                    <span>별칭</span>
                                    <input type="text">
                                </div>
                                <div class="img_input_box">
                                    <span>설명</span>
                                    <input type="text">
                                </div>
                                <div class="img_select_box">
                                    <label for="os">데이터 센터</label>
                                    <select id="os">
                                        <option value="linux">Linux</option>
                                    </select>
                                </div>
                                <div class="img_select_box">
                                    <label for="os">호스트</label>
                                    <select id="os">
                                        <option value="linux">Linux</option>
                                    </select>
                                </div>
                                <div class="img_select_box">
                                    <label for="os">스토리지 타입</label>
                                    <select id="os">
                                        <option value="linux">Linux</option>
                                    </select>
                                </div>
                            </div>
                            <div class="disk_new_img_right">
                                <div>
                                    <input type="checkbox" class="shareable">
                                    <label for="shareable">공유 가능</label>
                                </div>
                            </div>
                        </div>

                    </div>
                    <!--직접LUN끝-->
                    <!--관리되는 블록-->
                    <div id="storage_managed_outer" style="display: none;">
                        <div id="disk_managed_block_left">
                            <div class="img_input_box">
                                <span>크기(GIB)</span>
                                <input type="text" disabled>
                            </div>
                            <div class="img_input_box">
                                <span>별칭</span>
                                <input type="text" value="on20-ap01_Disk1" disabled>
                            </div>
                            <div class="img_input_box">
                                <span>설명</span>
                                <input type="text" disabled>
                            </div>
                            <div class="img_select_box">
                                <label for="data_center_select">데이터 센터</label>
                                <select id="data_center_select" disabled>
                                    <option value="dc_linux">Linux</option>
                                </select>
                            </div>
                            <div class="img_select_box">
                                <label for="storage_domain_select">스토리지 도메인</label>
                                <select id="storage_domain_select" disabled>
                                    <option value="sd_linux">Linux</option>
                                </select>
                            </div>
                            <span>해당 데이터 센터에 디스크를 생성할 수 있는 권한을 갖는 사용 가능한 관리 블록 스토리지 도메인이 없습니다.</span>
                        </div>

                        <div id="disk_managed_block_right">
                            <div>
                                <input type="checkbox" id="disk_shared_option" disabled>
                                <label for="disk_shared_option">공유 가능</label>
                            </div>
                        </div>

                    </div>
                    <!--관리되는 블록끝-->

                    <div class="edit_footer">
                        <button style="display: none;"></button>
                        <button>OK</button>
                        <button>취소</button>
                    </div>
        
                </div>
            </div>

            <!--스토리지 디스크 새로만들기팝업-->
            <div class="storage_disk_upload_outer">
                <div class="storage_disk_upload_popup">
                    <div class="network_popup_header">
                        <h1>이미지 업로드</h1>
                        <button><i class="fa fa-times"></i></button>
                    </div>

                    <div class="storage_upload_first">
                        <button>파일 선택</button>
                        <div>선택된 파일 없음</div>
                    </div>

                    <div class="storage_upload_second">
                        <div class="disk_option">
                            디스크옵션
                        </div>
                        <div class="disk_new_img" style=" padding-top: 0.4rem;">
                            <div class="disk_new_img_left">
                                <div class="img_input_box">
                                    <span>크기(GIB)</span>
                                    <input type="text" disabled>
                                </div>
                                <div class="img_input_box">
                                    <span>별칭</span>
                                    <input type="text">
                                </div>
                                <div class="img_input_box">
                                    <span>설명</span>
                                    <input type="text">
                                </div>
                                <div class="img_select_box">
                                    <label for="data_hub">데이터 센터</label>
                                    <select id="data_hub">
                                        <option value="linux">Linux</option>
                                    </select>
                                </div>
                                <div class="img_select_box">
                                    <label for="storage_zone">스토리지 도메인</label>
                                    <select id="storage_zone">
                                        <option value="linux">Linux</option>
                                    </select>
                                </div>
                                <div class="img_select_box">
                                    <label for="disk_pattern">디스크 프로파일</label>
                                    <select id="disk_pattern">
                                        <option value="nfs_storage">NFS-Storage</option>
                                    </select>
                                </div>
                                <div class="img_select_box">
                                    <label for="compute_unit">호스트</label>
                                    <select id="compute_unit">
                                        <option value="host01">host01.ititinfo.com</option>
                                    </select>
                                </div>
                            </div>
                            <div class="disk_new_img_right">    
                                <div>
                                    <input type="checkbox" id="reset_after_deletion">
                                    <label for="reset_after_deletion">삭제 후 초기화</label>
                                </div>
                                <div>
                                    <input type="checkbox" class="shareable">
                                    <label for="shareable">공유 가능</label>
                                </div>
                                <div style="margin-bottom: 0.4rem;">
                                    <input type="checkbox" id="incremental_backup" checked>
                                    <label for="incremental_backup">중복 백업 사용</label>
                                </div>
                                <div>
                                    <button>연결테스트</button>
                                </div>
                            </div>
                        </div><!--disk_new_img 팝업 끝-->

                    </div>




                    
                    <div class="edit_footer">
                        <button style="display: none;"></button>
                        <button>OK</button>
                        <button>취소</button>
                    </div>
                </div>
            </div>

             <!--스토리지 도메인 버튼-->
             <div id="storage_domain_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div >디스크</div>
                        <div>도메인</div>
                        <div>볼륨</div>
                        <div>스토리지</div>
                        <div>논리 네트워크</div>
                        <div>클러스터</div>
                        <div>권한</div>
                        <div>이벤트</div>
                    </div>
                    
                </div>
                
                <div class="storage_domain_content">
                    <div  class="content_header_right">
                        <button id="new_domain_btn">새로운 도메인</button>
                        <button id="get_domain_btn">도메인 가져오기</button>
                        <button id="administer_domain_btn">도메인 관리</button>
                        <button>삭제</button>
                        <button>Connections</button>
                        <button class="content_header_popup_btn">
                            <i class="fa fa-ellipsis-v"></i>
                            <div class="content_header_popup" style="display: none;">
                                <div>활성</div>
                                <div>비활성화</div>
                                <div>이동</div>
                                <div>LUN 새로고침</div>
                            </div>
                        </button>
                    </div>
                    <div class="search_box">
                        <input type="text"/>
                        <button><i class="fa fa-search"></i></button>
                    </div>
                    <table>
                        <thead>
                            <tr>
                                <th>상태</th>
                                <th></th>
                                <th>도메인 이름</th>
                                <th>코멘트</th>
                                <th>도메인 유형</th>
                                <th>스토리지 유형</th>
                                <th>포맷</th>
                                <th>데이터 센터간 상태</th>
                                <th>전체 공간(GB)</th>
                                <th>여유 공간(GB)</th>
                                <th>확보된 여유 공간(GB)</th>
                                <th>설명</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><i class="fa fa-caret-up" style="color: #1DED00;"></i></td>
                                <td><i class="fa fa-glass"></i></td>
                                <td></td>
                                <td></i></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-caret-up" style="color: #1DED00;"></i></td>
                                <td><i class="fa fa-glass"></i></td>
                                <td></td>
                                <td></i></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-caret-up" style="color: #1DED00;"></i></td>
                                <td><i class="fa fa-glass"></i></td>
                                <td></td>
                                <td></i></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div><!--스토리지 도메인 버튼끝-->

            <!--스토리지 도메인(새로운 도메인)팝업-->
            <div class="storage_domain_new_outer">
                <div class="storage_domain_new_popup">
                    <div class="network_popup_header">
                        <h1>새로운 도메인</h1>
                        <button><i class="fa fa-times"></i></button>
                    </div>
                    
                    <div class="storage_domain_new_first">
                        <div class="domain_new_left">
                            <div class="domain_new_select">
                                <label for="data_hub_location">데이터 센터</label>
                                <select id="data_hub_location">
                                    <option value="linux">Default(VS)</option>
                                </select>
                            </div>
                            <div class="domain_new_select">
                                <label for="domain_feature_set">도메인 기능</label>
                                <select id="domain_feature_set">
                                    <option value="linux">데이터</option>
                                </select>
                            </div>
                            <div class="domain_new_select">
                                <label for="storage_option_type">스토리지 유형</label>
                                <select id="storage_option_type">
                                    <option value="linux">NFS</option>
                                </select>
                            </div>
                            <div class="domain_new_select" style="margin-bottom: 0;">
                                <label for="host_identifier">호스트</label>
                                <select id="host_identifier">
                                    <option value="linux">host02.ititinfo.com</option>
                                </select>
                            </div>                           
                        </div>
                        <div class="domain_new_right">
                            <div class="domain_new_select">
                                <label>호스트</label>
                                <input type="text"/>
                            </div>
                            <div class="domain_new_select">
                                <label>설명</label>
                                <input type="text"/>
                            </div>
                            <div class="domain_new_select">
                                <label>코멘트</label>
                                <input type="text"/>
                            </div>
                        </div>
                    </div>

                    <div class="storage_domain_new_second">
                        <div>
                            <label for="data_hub">내보내기 경로</label>
                            <input type="text" placeholder="예:myserver.mydomain.com/my/local/path"/>
                        </div>

                        <div>
                            <i class="fa fa-chevron-circle-right" id="domain_hidden_box_btn"></i>
                            <span>사용자 정의 연결 매개 변수</span>
                            <div id="domain_hidden_box" style="display: none;">
                                <span>아래 필드에서 기본값을 변경하지 않을 것을 권장합니다.</span>
                                <div class="domain_new_select">
                                    <label for="data_hub">호스트</label>
                                    <select id="data_hub">
                                        <option value="linux">host02.ititinfo.com</option>
                                    </select>
                                </div>
                                <div class="domain_new_select">
                                    <label for="data_hub">재전송</label>
                                    <input type="text"/>
                                </div>
                                <div class="domain_new_select">
                                    <label for="data_hub">제한 시간(데시세컨드)</label>
                                    <input type="text"/>
                                </div>
                                <div class="domain_new_select">
                                    <label for="data_hub">추가 마운트 옵션</label>
                                    <input type="text"/>
                                </div>
                            </div>
                        </div>
                        <div>
                            <i class="fa fa-chevron-circle-right" id="domain_hidden_box_btn2"></i>
                            <span>고급 매개 변수</span>
                            <div id="domain_hidden_box2" style="display: none;">
                                <div class="domain_new_select">
                                    <label>디스크 공간 부족 경고 표시(%)</label>
                                    <input type="text"/>
                                </div>
                                <div class="domain_new_select">
                                    <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                                    <input type="text"/>
                                </div>
                                <div class="domain_new_select">
                                    <label>디스크 공간 부족 경고 표시(%)</label>
                                    <input type="text"/>
                                </div>
                                <div class="domain_new_select">
                                    <label for="format_type_selector" style="color: gray;">포맷</label>
                                    <select id="format_type_selector" disabled>
                                        <option value="linux">V5</option>
                                    </select>
                                </div>
                                <div class="network_checkbox_type2"> 
                                    <input type="checkbox" id="photo_separation" name="photo_separation">
                                    <label for="photo_separation">포토 분리</label>
                                </div>
                            </div>

                        </div>
                    </div>

                    <div class="edit_footer">
                        <button style="display: none;"></button>
                        <button>OK</button>
                        <button>취소</button>
                    </div>
                </div>
            </div>
            <!--스토리지 도메인(도메인 가져오기)팝업-->
            <div class="storage_domain_get_outer">
                <div class="storage_domain_get_popup">
                    <div class="network_popup_header">
                        <h1>사전 구성된 도메인 가져오기</h1>
                        <button><i class="fa fa-times"></i></button>
                    </div>
                    
                    <div class="storage_domain_new_first">
                        <div class="domain_new_left">
                            <div class="domain_new_select">
                                <label for="data_hub_location">데이터 센터</label>
                                <select id="data_hub_location">
                                    <option value="linux">Default(VS)</option>
                                </select>
                            </div>
                            <div class="domain_new_select">
                                <label for="domain_feature_set">도메인 기능</label>
                                <select id="domain_feature_set">
                                    <option value="linux">데이터</option>
                                </select>
                            </div>
                            <div class="domain_new_select">
                                <label for="storage_option_type">스토리지 유형</label>
                                <select id="storage_option_type">
                                    <option value="linux">NFS</option>
                                </select>
                            </div>
                            <div class="domain_new_select" style="margin-bottom: 0;">
                                <label for="host_identifier">호스트</label>
                                <select id="host_identifier">
                                    <option value="linux">host02.ititinfo.com</option>
                                </select>
                            </div>                           
                        </div>
                        <div class="domain_new_right">
                            <div class="domain_new_select">
                                <label>이름</label>
                                <input type="text"/>
                            </div>
                            <div class="domain_new_select">
                                <label>설명</label>
                                <input type="text"/>
                            </div>
                            <div class="domain_new_select">
                                <label>코멘트</label>
                                <input type="text"/>
                            </div>
                        </div>
                    </div>

                    <div class="storage_domain_new_second">
                        <div>
                            <label for="data_hub">내보내기 경로</label>
                            <input type="text" placeholder="예:myserver.mydomain.com/my/local/path"/>
                        </div>

                        <div>
                            <i class="fa fa-chevron-circle-right" id="domain_hidden_box_btn"></i>
                            <span>사용자 정의 연결 매개 변수</span>
                            <div id="domain_hidden_box" style="display: none;">
                                <span>아래 필드에서 기본값을 변경하지 않을 것을 권장합니다.</span>
                                <div class="domain_new_select">
                                    <label for="data_hub">호스트</label>
                                    <select id="data_hub">
                                        <option value="linux">host02.ititinfo.com</option>
                                    </select>
                                </div>
                                <div class="domain_new_select">
                                    <label for="data_hub">재전송</label>
                                    <input type="text"/>
                                </div>
                                <div class="domain_new_select">
                                    <label for="data_hub">제한 시간(데시세컨드)</label>
                                    <input type="text"/>
                                </div>
                                <div class="domain_new_select">
                                    <label for="data_hub">추가 마운트 옵션</label>
                                    <input type="text"/>
                                </div>
                            </div>
                        </div>
                        <div>
                            <i class="fa fa-chevron-circle-right" id="domain_hidden_box_btn2"></i>
                            <span>고급 매개 변수</span>
                            <div id="domain_hidden_box2" style="display: none;">
                                <div class="domain_new_select">
                                    <label>디스크 공간 부족 경고 표시(%)</label>
                                    <input type="text"/>
                                </div>
                                <div class="domain_new_select">
                                    <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                                    <input type="text"/>
                                </div>
                                <div class="domain_new_select">
                                    <label>디스크 공간 부족 경고 표시(%)</label>
                                    <input type="text"/>
                                </div>
                                <div class="domain_new_select">
                                    <label for="format_type_selector" style="color: gray;">포맷</label>
                                    <select id="format_type_selector" disabled>
                                        <option value="linux">V5</option>
                                    </select>
                                </div>
                                <div class="network_checkbox_type2"> 
                                    <input type="checkbox" id="photo_separation" name="photo_separation">
                                    <label for="photo_separation">포토 분리</label>
                                </div>
                            </div>

                        </div>
                    </div>

                    <div class="edit_footer">
                        <button style="display: none;"></button>
                        <button>OK</button>
                        <button>취소</button>
                    </div>
                </div>
            </div>

            <!--스토리지 도메인(도메인 관리)팝업-->
            <div class="storage_domain_administer_outer">
                <div class="storage_domain_administer_popup">
                    <div class="network_popup_header">
                        <h1>도메인 관리</h1>
                        <button><i class="fa fa-times"></i></button>
                    </div>
                    
                    <div class="storage_domain_new_first">
                        <div class="domain_new_left">
                            <div class="domain_new_select">
                                <label for="data_hub_location">데이터 센터</label>
                                <select id="data_hub_location">
                                    <option value="linux">Default(VS)</option>
                                </select>
                            </div>
                            <div class="domain_new_select">
                                <label for="domain_feature_set">도메인 기능</label>
                                <select id="domain_feature_set">
                                    <option value="linux">데이터</option>
                                </select>
                            </div>
                            <div class="domain_new_select">
                                <label for="storage_option_type">스토리지 유형</label>
                                <select id="storage_option_type">
                                    <option value="linux">NFS</option>
                                </select>
                            </div>
                            <div class="domain_new_select" style="margin-bottom: 0;">
                                <label for="host_identifier">호스트</label>
                                <select id="host_identifier">
                                    <option value="linux">host02.ititinfo.com</option>
                                </select>
                            </div>                           
                        </div>
                        <div class="domain_new_right">
                            <div class="domain_new_select">
                                <label>이름</label>
                                <input type="text"/>
                            </div>
                            <div class="domain_new_select">
                                <label>설명</label>
                                <input type="text"/>
                            </div>
                            <div class="domain_new_select">
                                <label>코멘트</label>
                                <input type="text"/>
                            </div>
                        </div>
                    </div>

                    <div class="storage_domain_new_second">
                        <div>
                            <label for="data_hub">내보내기 경로</label>
                            <input type="text" placeholder="예:myserver.mydomain.com/my/local/path"/>
                        </div>

                        <div>
                            <i class="fa fa-chevron-circle-right" id="domain_hidden_box_btn"></i>
                            <span>사용자 정의 연결 매개 변수</span>
                            <div id="domain_hidden_box" style="display: none;">
                                <span>아래 필드에서 기본값을 변경하지 않을 것을 권장합니다.</span>
                                <div class="domain_new_select">
                                    <label for="data_hub">호스트</label>
                                    <select id="data_hub">
                                        <option value="linux">host02.ititinfo.com</option>
                                    </select>
                                </div>
                                <div class="domain_new_select">
                                    <label for="data_hub">재전송</label>
                                    <input type="text"/>
                                </div>
                                <div class="domain_new_select">
                                    <label for="data_hub">제한 시간(데시세컨드)</label>
                                    <input type="text"/>
                                </div>
                                <div class="domain_new_select">
                                    <label for="data_hub">추가 마운트 옵션</label>
                                    <input type="text"/>
                                </div>
                            </div>
                        </div>
                        <div>
                            <i class="fa fa-chevron-circle-right" id="domain_hidden_box_btn2"></i>
                            <span>고급 매개 변수</span>
                            <div id="domain_hidden_box2" style="display: none;">
                                <div class="domain_new_select">
                                    <label>디스크 공간 부족 경고 표시(%)</label>
                                    <input type="text"/>
                                </div>
                                <div class="domain_new_select">
                                    <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                                    <input type="text"/>
                                </div>
                                <div class="domain_new_select">
                                    <label>디스크 공간 부족 경고 표시(%)</label>
                                    <input type="text"/>
                                </div>
                                <div class="domain_new_select">
                                    <label for="format_type_selector" style="color: gray;">포맷</label>
                                    <select id="format_type_selector" disabled>
                                        <option value="linux">V5</option>
                                    </select>
                                </div>
                                <div class="network_checkbox_type2"> 
                                    <input type="checkbox" id="photo_separation" name="photo_separation">
                                    <label for="photo_separation">포토 분리</label>
                                </div>
                            </div>

                        </div>
                    </div>

                    <div class="edit_footer">
                        <button style="display: none;"></button>
                        <button>OK</button>
                        <button>취소</button>
                    </div>
                </div>
            </div>

            <!--스토리지 볼륨버튼-->
            <div id="storage_volume_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div >디스크</div>
                        <div  >도메인</div>
                        <div class="active">볼륨</div>
                        <div>스토리지</div>
                        <div>논리 네트워크</div>
                        <div>클러스터</div>
                        <div>권한</div>
                        <div>이벤트</div>
                    </div>
                    
                </div>
                
                <div class="storage_volume_content">
                    <div  class="content_header_right">
                        <button id="storage_volume_new_btn">새로 만들기</button>
                        <button>삭제</button>
                        <button>시작</button>
                        <button class="disabled_button">중지</button>
                        <button>프로파일링</button>
                        <div>
                            <button id="storage_volume_snap_btn"  style="margin: 0;">스냅샷</button>
                            <button id="storage_volume_option_boxbtn"><i class="fa fa-chevron-down"></i>
                                <div class="storage_volume_option_box" style="display: none;">
                                    <div>새로 만들기</div>
                                    <div>스케줄 편집</div>
                                    <div>옵션 - 클러스터</div>
                                    <div>옵션 - 볼륨</div>
                                </div>
                            </button>
                        </div>
                        <button>지역 복제</button> 
                        <button class="content_header_popup_btn">
                            <i class="fa fa-ellipsis-v"></i>
                            <div class="content_header_popup" style="display: none;">
                                <div>활성</div>
                                <div>비활성화</div>
                                <div>이동</div>
                                <div>LUN 새로고침</div>
                            </div>
                        </button>
                    </div>
                    <div class="search_box">
                        <input type="text"/>
                        <button><i class="fa fa-search"></i></button>
                    </div>

                    <div class="empty_table">
                        <table>
                            <thead>
                                <tr>
                                    <th>이름</th>
                                    <th>클러스터</th>
                                    <th>볼륨 유형</th>
                                    <th>브릭</th>
                                    <th>정보</th>
                                    <th>사용한 공간</th>
                                    <th>작업</th>
                                    <th>스냅샷 수</th>
                                </tr>
                            </thead>
                            <tbody>
                                <span class="empty_content">표시할 항복이 없습니다</span>
                                <tr>
                                    
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div><!--스토리지 볼륨버튼끝-->

            <!--스토리지 볼륨 새로만들기팝업-->
            <div class="storage_volume_new_outer">
                <div class="storage_volume_new_popup">
                    <div class="network_popup_header">
                        <h1>새 볼륨</h1>
                        <button><i class="fa fa-times"></i></button>
                    </div>

                    <div class="volume_first_content">
                        <div class="domain_new_select">
                            <label for="data_center_selector">데이터 센터</label>
                            <select id="data_center_selector">
                                <option value="linux">host02.ititinfo.com</option>
                            </select>
                        </div>
                        <div class="domain_new_select" style="margin-bottom: 0;">
                            <label for="volume_cluster_selector">볼륨 클러스터</label>
                            <select id="volume_cluster_selector">
                                <option value="linux">host02.ititinfo.com</option>
                            </select>
                        </div>
                    </div>
                    <div class="volume_second_content">
                        <div class="domain_new_select">
                            <label>볼륨 클러스터</label>
                            <input type="text"/>
                        </div>
                        <div class="domain_new_select">
                            <label for="vol_cluster_dropdown">볼륨 클러스터</label>
                            <select id="vol_cluster_dropdown">
                                <option value="linux">host02.ititinfo.com</option>
                            </select>
                        </div>
                        <div class="domain_new_select">
                            <label for="data_hub">볼륨 클러스터</label>
                            <input type="text" value="3" disabled/>
                        </div>
                        <div class="domain_new_select">
                            <label>전송 유형</label>
                            <div class="volume_checkboxs">
                                <div class="volume_checkbox" style="margin-right: 3rem;">
                                    <input type="checkbox">
                                    <label for="photo_separation">TCP</label>
                                </div>
                                <div class="volume_checkbox">
                                    <input type="checkbox">
                                    <label for="photo_separation">RDMA</label>
                                </div>
                            </div>
                        </div>
                        <div class="domain_new_select">
                            <label>브릭</label>
                            <button>브릭 추가</button>
                        </div>
                    </div>

                    <div class="volume_third_content" style="padding-top: 0;">
                        <h2>접근 프로토콜</h2>
                        <div class="volume_checkbox">
                            <input type="checkbox">
                            <label for="photo_separation">RDMA</label>
                        </div>
                        <div class="volume_checkbox">
                            <input type="checkbox">
                            <label for="photo_separation">RDMA</label>
                        </div>
                        <div class="volume_checkbox">
                            <input type="checkbox">
                            <label for="photo_separation">RDMA</label>
                        </div>
                        <div class="domain_new_select">
                            <label for="data_hub">액세스 허용할 호스트</label>
                            <input type="text"/>
                        </div>
                        <div class="volume_checkbox">
                            <input type="checkbox">
                            <label for="photo_separation">RDMA</label>
                        </div>
                    </div>

                    <div class="edit_footer">
                        <button style="display: none;"></button>
                        <button>OK</button>
                        <button>취소</button>
                    </div>
                </div>
            </div>

            <!--스토리지 볼륨 스냅샷 팝업-->
            <div class="storage_volume_snap_outer">
                <div class="storage_volume_snap_popup">
                    <div class="network_popup_header">
                        <h1>볼륨 스냅샷 - 클러스터 옵션</h1>
                        <button><i class="fa fa-times"></i></button>
                    </div>
                    
                    <div class="volume_snap_first_content">
                        <div class="domain_new_select">
                            <label for="vol_cluster_dropdown">볼륨 클러스터</label>
                            <select id="vol_cluster_dropdown">
                                <option value="linux">host02.ititinfo.com</option>
                            </select>
                        </div>

                        <h2>스냅샷 옵션</h2>
                        <div class="volume_snap_table">
                            <table>
                                <thead>
                                    <tr>
                                        <th>이름</th>
                                        <th>설명</th>
                                        
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>ㅇ</td>
                                        <td>ㅇ</td>
                                    </tr>
                                    
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="edit_footer">
                        <button style="display: none;"></button>
                        <button>업데이트</button>
                        <button>취소</button>
                    </div>
                </div>
            </div>

            <!--스토리지 스토리지버튼-->
            <div id="storage_storage_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div >디스크</div>
                        <div>도메인</div>
                        <div>볼륨</div>
                        <div class="active">스토리지</div>
                        <div>논리 네트워크</div>
                        <div>클러스터</div>
                        <div>권한</div>
                        <div>이벤트</div>
                    </div>
                    
                </div>
                
                <div class="storage_domain_content">
                    <div  class="content_header_right">
                        <button>데이터 연결</button>
                        <button>ISP 연결</button>
                        <button>내보내기 연결</button>
                        <button>분리</button>
                        <button>활성</button>
                        <button>유지보수</button>
                    </div>
                    <div>
                        <div class="application_content_header">
                            <button><i class="fa fa-chevron-left"></i></button>
                            <div>1-2</div>
                            <button><i class="fa fa-chevron-right"></i></button>
                            <button><i class="fa fa-ellipsis-v"></i></button>            
                        </div>
                    </div>
                    <table>
                        <thead>
                            <tr>
                                <th>상태</th>
                                <th></th>
                                <th>도메인 이름</th>
                                <th>코멘트</th>
                                <th>도메인 유형</th>
                                <th>스토리지 유형</th>
                                <th>포맷</th>
                                <th>데이터 센터간 상태</th>
                                <th>전체 공간(GB)</th>
                                <th>여유 공간(GB)</th>
                                <th>확보된 여유 공간(GB)</th>
                                <th>설명</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><i class="fa fa-caret-up" style="color: #1DED00;"></i></td>
                                <td><i class="fa fa-glass"></i></td>
                                <td></td>
                                <td></i></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-caret-up" style="color: #1DED00;"></i></td>
                                <td><i class="fa fa-glass"></i></td>
                                <td></td>
                                <td></i></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-caret-up" style="color: #1DED00;"></i></td>
                                <td><i class="fa fa-glass"></i></td>
                                <td></td>
                                <td></i></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div><!--스토리지 스토리지 4864버튼끝-->

            <!--스토리지 논리네트워크 버튼-->
            <div id="storage_logic_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div >디스크</div>
                        <div>도메인</div>
                        <div>볼륨</div>
                        <div>스토리지</div>
                        <div class="active">논리 네트워크</div>
                        <div>클러스터</div>
                        <div>권한</div>
                        <div>이벤트</div>
                    </div>
                    
                </div>
                
                <div class="storage_domain_content">
                    <div  class="content_header_right">
                        <button>새로만들기</button>
                        <button>편집</button>
                        <button>삭제</button>
                    </div>
                    <div>
                        <div class="application_content_header">
                            <button><i class="fa fa-chevron-left"></i></button>
                            <div>1-2</div>
                            <button><i class="fa fa-chevron-right"></i></button>
                            <button><i class="fa fa-ellipsis-v"></i></button>            
                        </div>
                    </div>
                    <table>
                        <thead>
                            <tr>
                                <th>이름</th>
                                <th>설명</th>
                                
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>ovirtmgmt</td>
                                <td>Management Network</td>
                            </tr>
                            
                        </tbody>
                    </table>
                </div>
            </div><!--스토리지 논리네트워크 버튼끝-->

            <!--스토리지 클러스터 버튼-->
            <div id="storage_cluster_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div >디스크</div>
                        <div>도메인</div>
                        <div>볼륨</div>
                        <div>스토리지</div>
                        <div class="active">논리 네트워크</div>
                        <div>클러스터</div>
                        <div>권한</div>
                        <div>이벤트</div>
                    </div>
                </div>
                
                <div class="storage_domain_content">
                    <div>
                        <div class="application_content_header">
                            <button><i class="fa fa-chevron-left"></i></button>
                            <div>1-2</div>
                            <button><i class="fa fa-chevron-right"></i></button>
                            <button><i class="fa fa-ellipsis-v"></i></button>            
                        </div>
                    </div>
                    <table>
                        <thead>
                            <tr>
                                <th>이름</th>
                                <th>호환 버전</th>
                                <th>설명</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>ovirtmgmt</td>
                                <td>Management Network</td>
                                <td>The default server cluster</td>
                            </tr>
                            
                        </tbody>
                    </table>
                </div>
            </div><!--스토리지 클러스터 버튼끝-->

            <!--스토리지 권한 버튼-->
            <div id="storage_right_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div>디스크</div>
                        <div>도메인</div>
                        <div>볼륨</div>
                        <div >스토리지</div>
                        <div>논리 네트워크</div>
                        <div>클러스터</div>
                        <div class="active">권한</div>
                        <div>이벤트</div>
                    </div>
                    
                </div>
                
                <div class="storage_domain_content">
                    <div  class="content_header_right">
                        <button>추가</button>
                        <button>제거</button>
                    </div>
                    <div class="storage_right_btns">
                        <span>Permission Filters:</span>
                        <div>
                            <button>All</button>
                            <button>Direct</button>
                        </div>
                    </div>
                    <div>
                        <div class="application_content_header">
                            <button><i class="fa fa-chevron-left"></i></button>
                            <div>1-2</div>
                            <button><i class="fa fa-chevron-right"></i></button>
                            <button><i class="fa fa-ellipsis-v"></i></button>            
                        </div>
                    </div>
                    <table>
                        <thead>
                            <tr>
                                <th></th>
                                <th>사용자</th>
                                <th>인증 공급자</th>
                                <th>네임스페이스</th>
                                <th>역할</th>
                                <th>생성일</th>
                                <th>Inherited From</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><i class="fa fa-user"></i></td>
                                <td>ovirtmgmt</td>
                                <td></td>
                                <td>*</td>
                                <td>SuperUser</td>
                                <td>2023.12.29 AM 11:40:58</td>
                                <td>(시스템)</td>
                            </tr>
                            
                        </tbody>
                    </table>
                </div>
            </div><!--스토리지 권한 버튼-->


            <!--footer-->
            <div class="footer_outer">
                <div class="footer">
                    <button><i class="fa fa-chevron-down"></i></button>
                    <div>
                        <a>최근 작업</a>
                        <a>경보</a>
                    </div>
                </div>
                <div class="footer_content">

                    <div class="footer_nav">
                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div style="border-right: none;">
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>
                    </div>

                    <div class="footer_img">
                        <img src="img/화면 캡처 2024-04-30 164511.png">
                        <span>항목을 찾지 못했습니다</span>
                    </div>

                </div>
            </div><!--footer끝-->
        </div><!--스토리지 section끝-->

<!------------------------------------------------->
        <!--네트워크 section-->
        <div id="network_section">
            <div class="section_header">
                <div class="section_header_left">
                    <div>Default</div>
                    <button><i class="fa fa-exchange"></i></button>
                </div>
            
                <div class="section_header_right">
                    <div class="article_nav">
                        <button id="network_first_edit_btn">편집</button>
                        <button>삭제</button>
                        <button id="popup_btn">
                            <i class="fa fa-ellipsis-v"></i>
                            <div id="popup_box">
                                <div>
                                    <div class="get_btn">가져오기</div>
                                    <div class="get_btn">가상 머신 복제</div>
                                </div>
                                <div>
                                    <div>삭제</div>
                                </div>
                                <div>
                                    <div>마이그레이션 취소</div>
                                    <div>변환 취소</div>
                                </div>
                                <div>
                                    <div id="template_btn">템플릿 생성</div>
                                </div>
                                <div style="border-bottom: none;">
                                    <div id="domain2">도메인으로 내보내기</div>
                                    <div id="domain">Export to Data Domai</div>
                                    <div id="ova_btn">OVA로 내보내기</div>
                                </div>
                            </div> 
                        </button>
                    </div>
            
                </div><!--article끝-->
            </div><!--section_header끝-->

            <div class="content_outer">
                <div class="content_header">
                    <div class="content_header_left">
                        <div class="active">논리 네트워크</div>
                    </div>
                    
                </div>

                <div class="storage_domain_content">
                    <div class="content_header_right">
                        <button id="network_new_btn">새로 만들기</button>
                        <button id="network_bring_btn">가져오기</button>
                        <button >편집</button>
                        <button>삭제</button>
                    </div>
                    <div>
                        <div class="application_content_header">
                            <button><i class="fa fa-chevron-left"></i></button>
                            <div>1-2</div>
                            <button><i class="fa fa-chevron-right"></i></button>
                            <button><i class="fa fa-ellipsis-v"></i></button>            
                        </div>
                    </div>
                    <table>
                        <thead>
                            <tr>
                                <th>이름</th>
                                <th>설명</th>
                                
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>ovirtmgmt</td>
                                <td>Management Network</td>
                            </tr>
                            
                        </tbody>
                    </table>
                </div>
            </div><!--section_content_outer끝-->

            <!--네트워크 논리네트워크 버튼-->
            <div id="network_logic_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div >디스크</div>
                        <div>도메인</div>
                        <div>볼륨</div>
                        <div>스토리지</div>
                        <div class="active">논리 네트워크</div>
                        <div>클러스터</div>
                        <div>권한</div>
                        <div>이벤트</div>
                    </div>
                    
                </div>
                
                <div class="storage_domain_content">
                    <div  class="content_header_right">
                        <button>새로만들기</button>
                        <button>편집</button>
                        <button>삭제</button>
                    </div>
                    <div>
                        <div class="application_content_header">
                            <button><i class="fa fa-chevron-left"></i></button>
                            <div>1-2</div>
                            <button><i class="fa fa-chevron-right"></i></button>
                            <button><i class="fa fa-ellipsis-v"></i></button>            
                        </div>
                    </div>
                    <table>
                        <thead>
                            <tr>
                                <th>이름</th>
                                <th>설명</th>
                                
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>ovirtmgmt</td>
                                <td>Management Network</td>
                            </tr>
                            
                        </tbody>
                    </table>
                </div>
            </div><!--스토리지 논리네트워크 버튼끝-->


            <!--footer-->
            <div class="footer_outer">
                <div class="footer">
                    <button><i class="fa fa-chevron-down"></i></button>
                    <div>
                        <a>최근 작업</a>
                        <a>경보</a>
                    </div>
                </div>
                <div class="footer_content">

                    <div class="footer_nav">
                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div style="border-right: none;">
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>
                    </div>

                    <div class="footer_img">
                        <img src="img/화면 캡처 2024-04-30 164511.png">
                        <span>항목을 찾지 못했습니다</span>
                    </div>

                </div>
            </div><!--footer끝-->
        </div><!--네트워크 section끝-->

        <!--네트워크 -> 새로만들기 팝업-->
        <div class="network_new_outer">
            <div class="network_new_popup">
                <div class="network_popup_header">
                    <h1>새 논리적 네트워크</h1>
                    <button><i class="fa fa-times"></i></button>
                </div>

                <div class="network_new_nav">
                    <div id="network_new_common_btn" class="active">일반</div>
                    <div id="network_new_cluster_btn">클러스터</div>
                    <div id="network_new_vnic_btn" style="border-right: none;">vNIC 프로파일</div>
                </div>

                <!--일반-->
                <form id="network_new_common_form">
                    <div class="network_first_contents">
                        <div class="network_form_group">
                            <label for="cluster">데이터 센터</label>
                            <select id="cluster">
                                <option value="default">Default</option>   
                            </select>
                        </div>
                        <div class="network_form_group">
                            <div>
                                <label for="name">이름</label>    
                                <i class="fa fa-info-circle" style="color: #1ba4e4;"></i> 
                            </div>                       
                            <input type="text" id="name">
                        </div>
                        <div class="network_form_group">
                            <label for="description">설명</label>
                            <input type="text" id="description">
                        </div>
                        <div class="network_form_group">
                            <label for="comment">코멘트</label>
                            <input type="text" id="comment">
                        </div>
                    </div>

                    <div class="network_second_contents">
                        <span>네트워크 매개변수</span>
                        <div class="network_form_group">
                            <label for="network_label">네트워크 레이블</label>                            
                            <input type="text" id="network_label">
                        </div>
                        <div class="network_checkbox_type1">
                            <div>
                                <input type="checkbox" id="valn_tagging" name="valn_tagging">
                                <label for="valn_tagging">VALN 태깅 활성화</label>
                            </div>
                            <input type="text" id="valn_tagging_input" disabled>
                        </div>
                        <div class="network_checkbox_type2"> 
                            <input type="checkbox" id="vm_network" name="vm_network">
                            <label for="vm_network">가상 머신 네트워크</label>
                        </div>
                        <div class="network_checkbox_type2"> 
                            <input type="checkbox" id="photo_separation" name="photo_separation">
                            <label for="photo_separation">포토 분리</label>
                        </div>
                        <div class="network_radio_group">
                            <div style="margin-top: 0.2rem;">MTU</div>
                            <div>
                                <div class="radio_option">
                                    <input type="radio" id="default_mtu" name="mtu" value="default" checked>
                                    <label for="default_mtu">기본값 (1500)</label>
                                </div>
                                <div class="radio_option">
                                    <input type="radio" id="user_defined_mtu" name="mtu" value="user_defined">
                                    <label for="user_defined_mtu">사용자 정의</label>
                                </div>
                            </div>
                        </div>
                        <div class="network_checkbox_type2"> 
                            <input type="checkbox" id="dns_settings" name="dns_settings">
                            <label for="dns_settings">DNS 설정</label>
                        </div>
                        <span>DB서버</span>
                        <div class="network_checkbox_type3">
                            <input type="text" id="name" disabled>
                            <div>
                                <button>+</button>
                                <button>-</button>
                            </div>
                        </div>
                        <div class="network_checkbox_type2"> 
                            <input type="checkbox" id="external_vendor_creation" name="external_vendor_creation">
                            <label for="external_vendor_creation">외부 업체에서 작성</label>
                        </div>
                        <span>외부</span>
                        <div class="network_form_group"  style="padding-top: 0;">
                            <label for="external_provider">외부 공급자</label>
                            <select id="external_provider">
                                <option value="default">ovirt-provider-ovn</option>   
                            </select>
                        </div>
                        <div class="network_form_group">
                            <label for="network_port_security">네트워크 포트 보안</label>
                            <select id="network_port_security">
                                <option value="default">활성화</option>   
                            </select>
                        </div>
                        <div class="network_checkbox_type2"> 
                            <input type="checkbox" id="connect_to_physical_network" name="connect_to_physical_network">
                            <label for="connect_to_physical_network">물리적 네트워크에 연결</label>
                        </div>
                    </div>
                </form>

                <!--클러스터-->
                <form id="network_new_cluster_form">
                    <span>클러스터에서 네트워크를 연결/분리</span>
                    <div>
                        <table class="network_new_cluster_table">
                            <thead>
                                <tr>
                                    <th>이름</th>
                                    <th><input type="checkbox" id="connect_all"><label for="connect_all"> 모두 연결</label></th>
                                    <th><input type="checkbox" id="require_all"><label for="require_all"> 모두 필요</label></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>Default</td>
                                    <td class="checkbox-group"><input type="checkbox" id="connect_default"><label for="connect_default"> 연결</label></td>
                                    <td class="checkbox-group"><input type="checkbox" id="require_default"><label for="require_default"> 필수</label></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </form>
                
                <!--vNIC프로파일-->
                <form id="network_new_vnic_form">
                    <span>vNIC 프로파일</span>
                    <div>
                        
                        <input type="text" id="vnic_profile">
                        
                        <div>
                            <input type="checkbox" id="public" disabled>
                            <label for="public">공개</label>
                            <i class="fa fa-info-circle" style="color: rgb(83, 163, 255);"></i>
                        </div>
                        
                        
                        <label for="qos">QoS</label>
                        <select id="qos">
                            <option value="none">제한 없음</option>
                        </select>
                        
                        <div class="network_new_vnic_buttons">
                            <button>+</button>
                            <button>-</button>
                        </div>
                    </div>
                </form>

                <div class="edit_footer">
                    <button style="display: none;"></button>
                    <button>OK</button>
                    <button>취소</button>
                </div>
            </div>
        </div> <!--네트워크(새로만들기) 팝업끝-->

        <!--네트워크(가져오기) 팝업-->
        <div class="network_bring_outer">
            <div class="network_bring_popup">
                <div class="network_popup_header">
                    <h1>네트워크 가져오기</h1>
                    <button><i class="fa fa-times"></i></button>
                </div>

                <div class="network_form_group">
                    <label for="cluster">네트워크 공급자</label>
                    <select id="cluster">
                        <option value="default">Default</option>   
                    </select>
                </div>

                <div id="network_bring_table_outer">
                    <span>공급자 네트워크</span>
                    <div>
                        <table>
                            <thead>
                                <tr>
                                    <th>
                                        <input type="checkbox" id="diskActivation" checked>
                                    </th>
                                    <th>
                                        <label for="diskActivation">이름</label>
                                    </th>
                                    <th>
                                        <label for="diskActivation">공급자의 네트워크 ID</label>
                                    </th>
                                
                                </tr>
                            </thead>
                            <tbody>
                                <tr >
                                    <td>
                                        <input type="checkbox" id="diskActivation" checked>  
                                    </td>
                                    <td>
                                        <label for="diskActivation">디스크 활성화</label>                            
                                    </td>
                                    <td>
                                        <label for="diskActivation">디스크 활성화</label>                            
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div id="network_bring_table_outer">
                    <span>가져올 네트워크</span>
                    <div>
                        <table>
                            <thead>
                                <tr>
                                    <th>
                                        <input type="checkbox" id="diskActivation" checked>
                                    </th>
                                    <th>
                                        <label for="diskActivation">이름</label>
                                    </th>
                                    <th>
                                        <label for="diskActivation">공급자의 네트워크 ID</label>
                                    </th>
                                    <th>
                                        <label for="diskActivation">데이터 센터</label>
                                    </th>
                                    <th>
                                        <label for="diskActivation">모두허용</label>
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr >
                                    <td>
                                        <input type="checkbox" id="diskActivation" checked>  
                                    </td>
                                    <td>
                                        <label for="diskActivation">디스크 활성화</label>                            
                                    </td>
                                    <td>
                                        <label for="diskActivation">디스크 활성화</label>                            
                                    </td>
                                    <td>
                                        <label for="diskActivation">디스크 활성화</label>                            
                                    </td>
                                    <td>
                                        <label for="diskActivation">디스크 활성화</label>                            
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="edit_footer">
                    <button style="display: none;"></button>
                    <button>가져오기</button>
                    <button>취소</button>
                </div>
            </div>
        </div>
        <!--네트워크(편집) 팝업끝-->

        <!--네트워크 논리네트워크 버튼-->
        <div id="network_logic_outer" style="display: none;">
        <div class="content_header">
            <div class="content_header_left">
                <div >디스크</div>
                <div>도메인</div>
                <div>볼륨</div>
                <div>스토리지</div>
                <div class="active">논리 네트워크</div>
                <div>클러스터</div>
                <div>권한</div>
                <div>이벤트</div>
            </div>
            
        </div>
        
        <div class="storage_domain_content">
            <div  class="content_header_right">
                <button>새로만들기</button>
                <button>편집</button>
                <button>삭제</button>
            </div>
            <div>
                <div class="application_content_header">
                    <button><i class="fa fa-chevron-left"></i></button>
                    <div>1-2</div>
                    <button><i class="fa fa-chevron-right"></i></button>
                    <button><i class="fa fa-ellipsis-v"></i></button>            
                </div>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>이름</th>
                        <th>설명</th>
                        
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>ovirtmgmt</td>
                        <td>Management Network</td>
                    </tr>
                    
                </tbody>
            </table>
        </div>
        </div><!--네트워크 논리네트워크 버튼끝-->
<!------------------------------------------------->
        <!--설정 section-->
        <div id="setting_section" style="display: none;">
            <div class="section_header">
                <div class="section_header_left">
                    <div style="color: gray;">관리 > </div>
                    <div>활성 사용자 세션</div>
                </div>
                
            </div><!--section_header끝-->

            <div class="content_outer">
                <div class="storage_domain_content">
                    <div>
                        <div class="application_content_header">
                            <button><i class="fa fa-chevron-left"></i></button>
                            <div>1-2</div>
                            <button><i class="fa fa-chevron-right"></i></button>
                            <button><i class="fa fa-ellipsis-v"></i></button>  
                            <div class="search_box">
                                <input type="text"/>
                                <button><i class="fa fa-search"></i></button>
                            </div>         
                            <button>세션종료</button> 
                        </div>
                        
                    </div>
                    <table>
                        <thead>
                            <tr>
                                <th>세션 DB ID</th>
                                <th>사용자 이름</th>
                                <th>인증 공급자</th>
                                <th>사용자 ID</th>
                                <th>소스 IP</th>
                                <th>세션 시작 시간</th>
                                <th>마지막 세션 활성</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>3204</td>
                                <td>admin</td>
                                <td>internal-authz</td>
                                <td>b5e54b30-a5f3-11ee-81fa-00163...</td>
                                <td>192.168.0.218</td>
                                <td>2024. 1. 19. PM 1:04:09</td>
                                <td>2024. 1. 19. PM 4:45:55</td>
                            </tr>
                            <tr>
                                <td>3206</td>
                                <td>admin</td>
                                <td>internal-authz</td>
                                <td>b5e54b30-a5f3-11ee-81fa-00163...</td>
                                <td>192.168.0.214</td>
                                <td>2024. 1. 19. PM 3:46:55</td>
                                <td>2024. 1. 19. PM 4:45:55</td>
                            </tr>
                            <tr>
                                <td>3204</td>
                                <td>admin</td>
                                <td>internal-authz</td>
                                <td>b5e54b30-a5f3-11ee-81fa-00163...</td>
                                <td>192.168.0.218</td>
                                <td>2024. 1. 19. PM 1:04:09</td>
                                <td>2024. 1. 19. PM 4:45:55</td>
                            </tr>
                            <tr>
                                <td>3206</td>
                                <td>admin</td>
                                <td>internal-authz</td>
                                <td>b5e54b30-a5f3-11ee-81fa-00163...</td>
                                <td>192.168.0.214</td>
                                <td>2024. 1. 19. PM 3:46:55</td>
                                <td>2024. 1. 19. PM 4:45:55</td>
                            </tr>
                                                        
                        </tbody>
                    </table>
                </div>
            </div><!--section_content_outer끝-->

            <!--footer-->
            <div class="footer_outer">
                <div class="footer">
                    <button><i class="fa fa-chevron-down"></i></button>
                    <div>
                        <a>최근 작업</a>
                        <a>경보</a>
                    </div>
                </div>
                <div class="footer_content">

                    <div class="footer_nav">
                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div style="border-right: none;">
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>
                    </div>

                    <div class="footer_img">
                        <img src="img/화면 캡처 2024-04-30 164511.png">
                        <span>항목을 찾지 못했습니다</span>
                    </div>

                </div>
            </div><!--footer끝-->
        </div><!--설정 section끝-->

        <!--설정(설정)팝업-->
        <div class="setting_setting_outer">
            <div class="setting_setting_popup">
                <div class="network_popup_header">
                    <h1>설정</h1>
                    <button><i class="fa fa-times"></i></button>
                </div>

                <div class="network_new_nav">
                    <div id="setting_part_btn">역할</div>
                    <div id="setting_system_btn">시스템 권한</div>
                    <div id="setting_schedule_btn">스케줄링 정책</div>
                    <div id="setting_instant_btn">인스턴스 유형</div>
                    <div id="setting_mac_btn">MAC주소 풀</div>
                </div>

                <!--역할-->
                <form id="setting_part_form">
                    <div>보기</div>
                    <div class="setting_part_nav">
                        <div class="radio_toolbar">
                            <div>
                                <input type="radio" id="all_roles" name="roles" value="all" checked>
                                <label for="all_roles">모든역할</label>
                            </div>
                            <div>
                                <input type="radio" id="admin_roles" name="roles" value="admin">
                                <label for="admin_roles">관리자 역할</label>
                            </div>
                            <div>
                                <input type="radio" id="user_roles" name="roles" value="user">
                                <label for="user_roles">사용자 역할</label>
                            </div>
                        </div>

                        <div class="setting_buttons">
                            <div id="setting_part_new_btn">새로 만들기</div>
                            <div>편집</div>
                            <div>복사</div>
                            <div>삭제</div>
                        </div>
                    </div>

                    <div class="setting_part_table_outer">
                        <div class="application_content_header">
                            <button><i class="fa fa-chevron-left"></i></button>
                            <div>1-36</div>
                            <button><i class="fa fa-chevron-right"></i></button>
                            <button><i class="fa fa-ellipsis-v"></i></button>            
                        </div>

                        <table class="network_new_cluster_table">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th></th>
                                    <th>이름</th>
                                    <th>설명</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><i class="fa fa-heart"></i></td>
                                    <td><i class="fa fa-heart"></i></td>
                                    <td>dddddddddddddddddddddd</td>
                                    <td>ddddddddddddddddddddddㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ</td>
                                </tr>
                                <tr>
                                    <td><i class="fa fa-heart"></i></td>
                                    <td><i class="fa fa-heart"></i></td>
                                    <td>dddddddddddddddddddddd</td>
                                    <td>ddddddddddddddddddddddㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ</td>
                                </tr>
                            </tbody>
                        </table>

                    </div>
                </form>
                <!--역할 새로만들기 팝업-->
                <div class="setting_part_new_outer">
                    <div class="setting_part_new_popup">
                        <div class="network_popup_header">
                            <h1>새 역할</h1>
                            <button><i class="fa fa-times"></i></button>
                        </div>
                        
                        <div class="set_part_text">
                            <div>
                                <div>
                                    <label for="name">이름</label><br>
                                    <input type="text" id="name" value="test02">
                                </div>
                                <div>
                                    <label for="name">설명</label><br>
                                    <input type="text" id="name" value="test02">
                                </div>
                            </div>
                            <span>계정 유형:</span>
                            <div>
                                <div>
                                    <input type="radio" id="all_roles" name="roles" value="all" checked>
                                    <label for="all_roles" style="margin-right: 0.3rem;">사용자</label>
                                </div>
                                <div>
                                    <input type="radio" id="admin_roles" name="roles" value="admin">
                                    <label for="admin_roles">관리자</label>
                                </div>
                            </div>
                        </div>

                        <div class="set_part_checkboxs">
                            <span>작업 허용을 위한 확인란</span>
                            <div class="set_part_buttons" >
                                <div>모두 확장</div>
                                <div>모두 축소</div>
                            </div>
                            <div class="checkbox_toolbar">
                                <div>
                                    <input type="checkbox" id="memory_balloon" name="memory_balloon">
                                    <label for="memory_balloon">시스템</label>
                                </div>
                                <div>
                                    <input type="checkbox" id="memory_balloon" name="memory_balloon">
                                    <label for="memory_balloon">네트워크</label>
                                </div>
                                <div>
                                    <input type="checkbox" id="memory_balloon" name="memory_balloon">
                                    <label for="memory_balloon">템플릿</label>
                                </div>
                                <div>
                                    <input type="checkbox" id="memory_balloon" name="memory_balloon">
                                    <label for="memory_balloon">가상머신</label>
                                </div>
                                <div>
                                    <input type="checkbox" id="memory_balloon" name="memory_balloon">
                                    <label for="memory_balloon">가상머신 풀</label>
                                </div>
                                <div>
                                    <input type="checkbox" id="memory_balloon" name="memory_balloon">
                                    <label for="memory_balloon">디스크</label>
                                </div>
                            </div>

                        </div>


                        <div class="edit_footer">
                            <button style="display: none;"></button>
                            <button>OK</button>
                            <button>취소</button>
                        </div>
                    </div>
                </div>

                <!--시스템 권한-->
                <form id="setting_system_form">
                    <div class="setting_part_nav">
                        <div class="radio_toolbar">
                            <div>
                                <input type="radio" id="all_roles" name="roles" value="all" checked>
                                <label for="all_roles">모든역할</label>
                            </div>
                            <div>
                                <input type="radio" id="admin_roles" name="roles" value="admin">
                                <label for="admin_roles">관리자 역할</label>
                            </div>
                            <div>
                                <input type="radio" id="user_roles" name="roles" value="user">
                                <label for="user_roles">사용자 역할</label>
                            </div>
                        </div>

                        <div class="setting_buttons">
                            <div id="setting_system_add_btn">추가</div>
                            <div>제거</div>
                        </div>
                    </div>

                    <div class="setting_part_table_outer">
                        <div class="application_content_header">
                            <button><i class="fa fa-chevron-left"></i></button>
                            <div>1-3</div>
                            <button><i class="fa fa-chevron-right"></i></button>
                            <button><i class="fa fa-ellipsis-v"></i></button>            
                        </div>

                        <table class="network_new_cluster_table">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th>사용자</th>
                                    <th>인증 공급자</th>
                                    <th>네임 스페이스</th>
                                    <th>역할</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><i class="fa fa-heart"></i></td>
                                    <td>ovirt-administrator</td>
                                    <td></td>
                                    <td>*</td>
                                    <td>SuperUser</td>
                                </tr>
                                <tr>
                                    <td><i class="fa fa-heart"></i></td>
                                    <td>ovirt-administrator</td>
                                    <td></td>
                                    <td>*</td>
                                    <td>SuperUser</td>
                                </tr>
                            </tbody>
                        </table>

                    </div>
                </form>
                <!--시스템 권한 추가 팝업-->
                <div class="setting_system_new_outer" style="display: none;">
                    <div class="setting_system_new_popup">
                        <div class="network_popup_header">
                            <h1>사용자에게 권한 추가</h1>
                            <button><i class="fa fa-times"></i></button>
                        </div>
    
                        <div class="power_radio_group">
                            <input type="radio" id="user" name="option" checked>
                            <label for="user">사용자</label>
                            
                            <input type="radio" id="group" name="option">
                            <label for="group">그룹</label>
                        </div>
    
                        <div class="power_contents_outer">
                            <div>
                                <label for="cluster">검색:</label>
                                <select id="cluster">
                                    <option value="default">Default</option>   
                                </select>
                            </div>
                            <div>
                                <label for="cluster">네임스페이스:</label>
                                <select id="cluster">
                                    <option value="default">Default</option>   
                                </select>
                            </div>
                            <div>
                                <label style="color: white;">.</label>
                                <input type="text" id="name" value="test02">
                            </div>
                            <div>
                                <div style="color: white;">.</div>
                                <input type="submit" value="검색">
                            </div>
                        </div>
                        
    
                        <div class="power_table">
                            <table>
                                <thead>
                                    <tr>
                                        <th>이름</th>
                                        <th>성</th>
                                        <th>사용자 이름</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>dddddddddddddddddddddd</td>
                                        <td>2024. 1. 17. PM 3:14:39</td>
                                        <td>Snapshot 'on2o-ap01-Snapshot-2024_01_17' been completed.</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
    
                        <div class="power_last_content" style="padding: 0.1rem 0.3rem;">
                            <label for="cluster">할당된 역할:</label>
                            <select id="cluster"  style="width: 65%;">
                                <option value="default">UserRole</option>   
                            </select>
                        </div>
    
                        
    
    
    
                        <div class="edit_footer">
                            <button style="display: none;"></button>
                            <button>OK</button>
                            <button>취소</button>
                        </div>
                    </div>
                </div>

                <!--스케줄링 정책-->
                <form id="setting_schedule_form">
                    <div class="setting_part_nav">
                        <div class="setting_buttons">
                            <div id="setting_schedule_new_btn">새로 만들기</div>
                            <div>편집</div>
                            <div>복사</div>
                            <div>제거</div>
                            <div id="setting_schedule_unit">정책 유닛 관리</div>
                        </div>
                    </div>

                    <div class="setting_part_table_outer">
                        <div class="application_content_header">
                            <button><i class="fa fa-chevron-left"></i></button>
                            <div>1-5</div>
                            <button><i class="fa fa-chevron-right"></i></button>
                            <button><i class="fa fa-ellipsis-v"></i></button>            
                        </div>

                        <table class="network_new_cluster_table">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th>이름</th>
                                    <th>설명</th>
                                    
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><i class="fa fa-heart"></i></td>
                                    <td>ovirt-administrator</td>
                                    <td>ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ</td>
                                </tr>
                                <tr>
                                    <td><i class="fa fa-heart"></i></td>
                                    <td>ovirt-administrator</td>
                                    <td></td>
                                </tr>
                            </tbody>
                        </table>

                    </div>
                </form>
                <!--스케줄링 정책 새로만들기 팝업-->
                <div class="setting_schedule_new_outer">
                    <div class="setting_schedule_new_popup">
                        <div class="network_popup_header">
                            <h1>새 스케줄링 정책</h1>
                            <button><i class="fa fa-times"></i></button>
                        </div>
                        
                        <div class="set_part_text" style="border-bottom: none;">
                            <div>
                                <div>
                                    <label for="name">이름</label><br>
                                    <input type="text" id="name" value="test02">
                                </div>
                                <div>
                                    <label for="name">설명</label><br>
                                    <input type="text" id="name" value="test02">
                                </div>
                            </div>
                        </div>

                        <div class="set_schedule_contents">
                            <div class="set_schedule_contents_left">
                                <div>
                                    <h1>필터 모듈</h1>
                                    <div style="font-size: 0.26rem;">드래그하거나 또는 컨텍스트 메뉴를 사용하여 변경 활성화된 필터</div>
                                    <div></div>
                                </div>
                                <div>
                                    <h1>필터 모듈</h1>
                                    <div style="font-size: 0.26rem;">드래그하거나 또는 컨텍스트 메뉴를 사용하여 변경 활성화된 필터</div>
                                    <div></div>
                                </div>
                            </div>
                            <div class="set_schedule_contents_right">
                                <div>
                                    <span>비활성화된 필터</span>
                                    <div class="schedule_boxs">
                                        <div>Migration</div>
                                        <div>Migration</div>
                                        <div>Migration</div>
                                        <div>Migration</div>
                                        <div>Migration</div>
                                        <div>Migration</div>
                                    </div>
                                </div>
                                <div>
                                    <span>비활성화된 가중치</span>
                                    <div class="schedule_boxs">
                                        <div>Migration</div>
                                        <div>Migration</div>
                                        <div>Migration</div>
                                        <div>Migration</div>
                                        <div>Migration</div>
                                        <div>Migration</div>
                                        <div>Migration</div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="set_schedule_balance">
                            <label for="network_port_security">
                                비활성화된 필터 <i class="fa fa-info-circle" style="color:#1ba4e4;"></i>
                            </label>
                            <select>
                                <option value="default">활성화</option>   
                            </select>
                        </div>

                        <div class="edit_footer">
                            <button style="display: none;"></button>
                            <button>OK</button>
                            <button>취소</button>
                        </div>
                    </div>
                </div>

                <!--인스턴스 유형-->
                <form id="setting_instant_form">
                    <div class="setting_part_nav">
                        <div class="setting_buttons">
                            <div id="setting_instant_new_btn">새로 만들기</div>
                            <div>편집</div>
                            <div>제거</div>
                        </div>
                    </div>

                    <div class="setting_part_table_outer">
                        <div class="application_content_header">
                            <button><i class="fa fa-chevron-left"></i></button>
                            <div>1-5</div>
                            <button><i class="fa fa-chevron-right"></i></button>
                            <button><i class="fa fa-ellipsis-v"></i></button>            
                        </div>

                        <table class="network_new_cluster_table">
                            <thead>
                                <tr>
                                    <th>이름</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>     
                                    <td>ovirt-administrator</td>
                                </tr>
                                <tr>
                                    <td>ovirt-administrator</td>
                                </tr>
                            </tbody>
                        </table>

                    </div>
                </form>

                <!--MAC주소 풀-->
                <form id="setting_mac_form">
                    <div class="setting_part_nav">
                        <div class="setting_buttons">
                            <div id="setting_mac_new_btn">새로 만들기</div>
                            <div id="setting_mac_edit_btn">편집</div>
                            <div>제거</div>
                        </div>
                    </div>

                    <div class="setting_part_table_outer" style="border-bottom: none;">
                        <div class="application_content_header">
                            <button><i class="fa fa-chevron-left"></i></button>
                            <div>1-5</div>
                            <button><i class="fa fa-chevron-right"></i></button>
                            <button><i class="fa fa-ellipsis-v"></i></button>            
                        </div>

                        <table class="network_new_cluster_table">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th>이름</th>
                                    <th>설명</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><i class="fa fa-heart"></i></td>
                                    <td>ovirt-administrator</td>
                                    <td>ovirt-administrator</td>
                                </tr>
                                <tr>
                                    <td><i class="fa fa-heart"></i></td>
                                    <td>ovirt-administrator</td>
                                    <td>ovirt-administrator</td>
                                </tr>
                            </tbody>
                        </table>

                    </div>

                    <div class="setting_part_table_outer">
                        <div class="application_content_header">
                            <button><i class="fa fa-chevron-left"></i></button>
                            <div>1-5</div>
                            <button><i class="fa fa-chevron-right"></i></button>
                            <button><i class="fa fa-ellipsis-v"></i></button>            
                        </div>

                        <table class="network_new_cluster_table">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th>사용자</th>
                                    <th>인증 공급자</th>
                                    <th>네임 스페이스</th>
                                    <th>역할</th>
                                    <th>생성일</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><i class="fa fa-heart"></i></td>
                                    <td>ovirt-administrator</td>
                                    <td>ovirt-administrator</td>
                                    <td>*</td>
                                    <td>ovirt-adm</td>
                                    <td>2023.12.29AM11:40:58</td>
                                </tr>
                                <tr>
                                    <td><i class="fa fa-heart"></i></td>
                                    <td>ovirt-administrator</td>
                                    <td>ovirt-administrator</td>
                                    <td>*</td>
                                    <td>ovirt-adm</td>
                                    <td>2023.12.29AM11:40:58</td>
                                </tr>
                            </tbody>
                        </table>

                    </div>
                </form>
                <!--MAC주소 풀(새로만들기)팝업-->
                <div class="setting_mac_new_outer">
                    <div class="setting_mac_new_popup">
                        <div class="network_popup_header">
                            <h1>새 MAC주소 풀</h1>
                            <button><i class="fa fa-times"></i></button>
                        </div>
                        
                        <div class="setting_mac_textboxs">
                            <div>
                                <span>이름</span>
                                <input type="text">
                            </div>
                            <div>
                                <span>설명</span>
                                <input type="text">
                            </div>
                        </div>
                        <div class="setting_mac_checkbox">
                            <input type="checkbox" id="allow_duplicate" name="allow_duplicate">
                            <label for="allow_duplicate">중복 허용</label>
                        </div>
                        
                        <div class="network_parameter_outer">
                            <span>MAC 주소 범위</span>
                            <div style="margin-bottom: 0.2rem;">
                                <div>
                                    <span style="margin-right: 0.3rem;">범위 시작</span>
                                    <input type="text"/>
                                </div>
                                <div>
                                    <span>범위끝</span>
                                    <input type="text"/>
                                </div>
                                <div id="buttons">
                                    <button>+</button>
                                    <button>-</button>
                                </div>
                            </div>
                            <div>
                                MAC수 : 해당없음
                            </div>
                        </div>

                        <div class="edit_footer">
                            <button style="display: none;"></button>
                            <button>OK</button>
                            <button>취소</button>
                        </div>
                    </div>
                </div>

                <!--MAC주소 풀(편집)팝업-->
                <div class="setting_mac_edit_outer">
                    <div class="setting_mac_edit_popup">
                        <div class="network_popup_header">
                            <h1>새 MAC주소 풀</h1>
                            <button><i class="fa fa-times"></i></button>
                        </div>
                        
                        <div class="setting_mac_textboxs">
                            <div>
                                <span>이름</span>
                                <input type="text">
                            </div>
                            <div>
                                <span>설명</span>
                                <input type="text">
                            </div>
                        </div>
                        <div class="setting_mac_checkbox">
                            <input type="checkbox" id="allow_duplicate" name="allow_duplicate">
                            <label for="allow_duplicate">중복 허용</label>
                        </div>
                        
                        <div class="network_parameter_outer">
                            <span>MAC 주소 범위</span>
                            <div style="margin-bottom: 0.2rem;">
                                <div>
                                    <span style="margin-right: 0.3rem;">범위 시작</span>
                                    <input type="text"/>
                                </div>
                                <div>
                                    <span>범위끝</span>
                                    <input type="text"/>
                                </div>
                                <div id="buttons">
                                    <button>+</button>
                                    <button>-</button>
                                </div>
                            </div>
                            <div>
                                MAC수 : 해당없음
                            </div>
                        </div>

                        <div class="edit_footer">
                            <button style="display: none;"></button>
                            <button>OK</button>
                            <button>취소</button>
                        </div>
                    </div>
                </div>


                <div class="edit_footer">
                    <button style="display: none;"></button>
                    <button>OK</button>
                    <button>취소</button>
                </div>
                

            </div>
        </div> <!--설정(설정) 팝업끝-->

        <!--사용자 section-->
        <div id="setting_user_section" style="display: none;">
            <div class="section_header">
                <div class="section_header_left">
                    <div style="color: gray;">관리 > </div>
                    <div>사용자</div>
                </div>
                <div class="setting_user_buttons">
                    <div id="set_user_add_btn">추가</div>
                    <div>삭제</div>
                    <div>태그 설정</div>
                </div>
            </div><!--section_header끝-->

            <div class="content_outer">
                <div class="storage_domain_content">
                    <div>
                        <div class="application_content_header">
                            <button><i class="fa fa-chevron-left"></i></button>
                            <div>1-2</div>
                            <button><i class="fa fa-chevron-right"></i></button>
                            <button><i class="fa fa-ellipsis-v"></i></button>  
                            <div class="search_box">
                                <input type="text"/>
                                <button><i class="fa fa-search"></i></button>
                            </div>         
                        </div>
                        
                    </div>
                    <table>
                        <thead>
                            <tr>
                                <th></th>
                                <th>이름</th>
                                <th>성</th>
                                <th>사용자 이름</th>
                                <th>인증 공급자</th>
                                <th>네임 스페이스</th>
                                <th>이메일</th>
                                
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><i class="fa fa-user"></i></td>
                                <td>admin</td>
                                <td>internal-authz</td>
                                <td>admin</td>
                                <td>192.168.0.218</td>
                                <td>*</td>
                                <td>2024. 1. 19. PM 4:45:55</td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-user"></i></td>
                                <td>admin</td>
                                <td>internal-authz</td>
                                <td>admin</td>
                                <td>192.168.0.214</td>
                                <td>*</td>
                                <td>2024. 1. 19. PM 4:45:55</td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-user"></i></td>
                                <td>admin</td>
                                <td>internal-authz</td>
                                <td>admin</td>
                                <td>192.168.0.218</td>
                                <td>*</td>
                                <td>2024. 1. 19. PM 4:45:55</td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-user"></i></td>
                                <td>admin</td>
                                <td>internal-authz</td>
                                <td>admin</td>
                                <td>192.168.0.214</td>
                                <td>*</td>
                                <td>2024. 1. 19. PM 4:45:55</td>
                            </tr>
                                                        
                        </tbody>
                    </table>
                </div>
            </div><!--section_content_outer끝-->


            <!--footer-->
            <div class="footer_outer">
                <div class="footer">
                    <button><i class="fa fa-chevron-down"></i></button>
                    <div>
                        <a>최근 작업</a>
                        <a>경보</a>
                    </div>
                </div>
                <div class="footer_content">

                    <div class="footer_nav">
                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div style="border-right: none;">
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>
                    </div>

                    <div class="footer_img">
                        <img src="img/화면 캡처 2024-04-30 164511.png">
                        <span>항목을 찾지 못했습니다</span>
                    </div>

                </div>
            </div><!--footer끝-->
        </div><!--사용자 section끝-->

        <!--설정(사용자->추가버튼)팝업-->
        <div class="setting_user_new_outer" style="display: none;">
            <div class="setting_user_new_popup">
                <div class="network_popup_header">
                    <h1>사용자에게 권한 추가</h1>
                    <button><i class="fa fa-times"></i></button>
                </div>

                <div class="power_radio_group">
                    <input type="radio" id="user" name="option" checked>
                    <label for="user">사용자</label>
                    
                    <input type="radio" id="group" name="option">
                    <label for="group">그룹</label>
                </div>

                <div class="power_contents_outer">
                    <div>
                        <label for="cluster">검색:</label>
                        <select id="cluster">
                            <option value="default">Default</option>   
                        </select>
                    </div>
                    <div>
                        <label for="cluster">네임스페이스:</label>
                        <select id="cluster">
                            <option value="default">Default</option>   
                        </select>
                    </div>
                    <div>
                        <label style="color: white;">.</label>
                        <input type="text" id="name">
                    </div>
                    <div>
                        <div style="color: white;">.</div>
                        <input type="submit" value="검색">
                    </div>
                </div>
                

                <div class="power_table">
                    <table>
                        <thead>
                            <tr>
                                <th>이름</th>
                                <th>성</th>
                                <th>사용자 이름</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>dddddddddddddddddddddd</td>
                                <td>2024. 1. 17. PM 3:14:39</td>
                                <td>Snapshot 'on2o-ap01-Snapshot-2024_01_17' been completed.</td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="edit_footer">
                    <button style="display: none;"></button>
                    <button>OK</button>
                    <button>취소</button>
                </div>
            </div>
        </div>
<!------------------------------------------------->
        <!--content_detail_section-->
        <div class="content_detail_section">
            <div class="section_header">
                <div class="section_header_left">
                    <span>데이터 센터 > </span>
                    <span>스토리지 도메인 > </span>
                    <div>hosted_storage</div>
                    <button><i class="fa fa-exchange"></i></button>
                </div>
            
                <div class="section_header_right">
                    <div class="article_nav">
                        <button>도메인 관리</button>
                        <button>삭제</button>
                        <button>Connections</button>
                        <button id="popup_btn">
                            <i class="fa fa-ellipsis-v"></i>
                            <div id="popup_box">
                                <div>
                                    <div class="get_btn">가져오기</div>
                                    <div class="get_btn">가상 머신 복제</div>
                                </div>
                                <div>
                                    <div>삭제</div>
                                </div>
                                <div>
                                    <div>마이그레이션 취소</div>
                                    <div>변환 취소</div>
                                </div>
                                <div>
                                    <div id="template_btn">템플릿 생성</div>
                                </div>
                                <div style="border-bottom: none;">
                                    <div id="domain2">도메인으로 내보내기</div>
                                    <div id="domain">Export to Data Domai</div>
                                    <div id="ova_btn">OVA로 내보내기</div>
                                </div>
                            </div> 
                        </button>
                    </div>
            
                </div><!--article끝-->
            </div><!--section_header끝-->

            <div class="content_outer">
                <div class="content_header">
                    <div class="content_header_left">
                        <div class="active">일반</div>
                        <div>데이터 센터</div>
                        <div>가상머신</div>
                        <div>템플릿</div>
                        <div>디스크</div>
                        <div>디스크 스냅샷</div>
                        <div>임대</div>
                        <div>디스크 프로파일</div>
                        <div>이벤트</div>
                        <div>권한</div>
                    </div>
                </div>

                <div class="section_content_outer">
                    <div class="table_container_left">
                        <table class="table">
                            <tr>
                                <th>ID:</th>
                                <td>on20-ap01</td>
                            </tr>
                            <tr>
                                <th>설명:</th>
                                <td></td>
                            </tr>
                            <tr>
                                <th>상태:</th>
                                <td>실행 중</td>
                            </tr>
                            <tr>
                                <th>업타임:</th>
                                <td>11 days</td>
                            </tr>
                            <tr>
                                <th>템플릿:</th>
                                <td>Blank</td>
                            </tr>
                            <tr>
                                <th>운영 시스템:</th>
                                <td>Linux</td>
                            </tr>
                            <tr>
                                <th>펌웨어/장치의 유형:</th>
                                <td>BIOS의 Q35 칩셋 <i class="fa fa-ban" style="margin-left: 13%;color:orange;"></i></td>                           
                            </tr>
                            <tr>
                                <th>우선 순위:</th>
                                <td>높음</td>
                            </tr>
                            <tr>
                                <th>최적화 옵션:</th>
                                <td>서버</td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div><!--section_content_outer끝-->

            <!--디테일 데이터센터 버튼-->
            <div id="detail_datacenter_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div>일반</div>
                        <div  class="active">데이터 센터</div>
                        <div>가상머신</div>
                        <div>템플릿</div>
                        <div>디스크</div>
                        <div>디스크 스냅샷</div>
                        <div>임대</div>
                        <div>디스크 프로파일</div>
                        <div>이벤트</div>
                        <div>권한</div>
                    </div>
                    
                </div>
                
                <div>dd</div>
            </div>

            <!--디테일 가상머신 버튼-->
            <div id="detail_machine_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div class="active">일반</div>
                        <div>데이터 센터</div>
                        <div>가상머신</div>
                        <div>템플릿</div>
                        <div>디스크</div>
                        <div>디스크 스냅샷</div>
                        <div>임대</div>
                        <div>디스크 프로파일</div>
                        <div>이벤트</div>
                        <div>권한</div>
                    </div>
                </div>
                
                <div>dd</div>
            </div>

            <!--디테일 템플릿 버튼-->
            <div id="detail_template_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div class="active">일반</div>
                        <div>데이터 센터</div>
                        <div>가상머신</div>
                        <div>템플릿</div>
                        <div>디스크</div>
                        <div>디스크 스냅샷</div>
                        <div>임대</div>
                        <div>디스크 프로파일</div>
                        <div>이벤트</div>
                        <div>권한</div>
                    </div>
                </div>
                
                <div>
                    dd
                </div>
            </div>

            <!--디테일 디스크 버튼-->
            <div id="detail_disk_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div class="active">일반</div>
                        <div>데이터 센터</div>
                        <div>가상머신</div>
                        <div>템플릿</div>
                        <div>디스크</div>
                        <div>디스크 스냅샷</div>
                        <div>임대</div>
                        <div>디스크 프로파일</div>
                        <div>이벤트</div>
                        <div>권한</div>
                    </div>
                </div>
            </div>

            <!--디테일 디스크스냅샷버튼-->
            <div id="detail_snapshot_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div class="active">일반</div>
                        <div>데이터 센터</div>
                        <div>가상머신</div>
                        <div>템플릿</div>
                        <div>디스크</div>
                        <div>디스크 스냅샷</div>
                        <div>임대</div>
                        <div>디스크 프로파일</div>
                        <div>이벤트</div>
                        <div>권한</div>
                    </div>
                </div>
                
                <div class="storage_domain_content">
                    <div  class="content_header_right">
                        <button>데이터 연결</button>
                        <button>ISP 연결</button>
                        <button>내보내기 연결</button>
                        <button>분리</button>
                        <button>활성</button>
                        <button>유지보수</button>
                    </div>
                    <div>
                        <div class="application_content_header">
                            <button><i class="fa fa-chevron-left"></i></button>
                            <div>1-2</div>
                            <button><i class="fa fa-chevron-right"></i></button>
                            <button><i class="fa fa-ellipsis-v"></i></button>            
                        </div>
                    </div>
                    <table>
                        <thead>
                            <tr>
                                <th>상태</th>
                                <th></th>
                                <th>도메인 이름</th>
                                <th>코멘트</th>
                                <th>도메인 유형</th>
                                <th>스토리지 유형</th>
                                <th>포맷</th>
                                <th>데이터 센터간 상태</th>
                                <th>전체 공간(GB)</th>
                                <th>여유 공간(GB)</th>
                                <th>확보된 여유 공간(GB)</th>
                                <th>설명</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><i class="fa fa-caret-up" style="color: #1DED00;"></i></td>
                                <td><i class="fa fa-glass"></i></td>
                                <td></td>
                                <td></i></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-caret-up" style="color: #1DED00;"></i></td>
                                <td><i class="fa fa-glass"></i></td>
                                <td></td>
                                <td></i></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-caret-up" style="color: #1DED00;"></i></td>
                                <td><i class="fa fa-glass"></i></td>
                                <td></td>
                                <td></i></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <!--디테일 임대 버튼-->
            <div id="detail_rent_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div class="active">일반</div>
                        <div>데이터 센터</div>
                        <div>가상머신</div>
                        <div>템플릿</div>
                        <div>디스크</div>
                        <div>디스크 스냅샷</div>
                        <div>임대</div>
                        <div>디스크 프로파일</div>
                        <div>이벤트</div>
                        <div>권한</div>
                    </div>
                    
                </div>
                
                <div class="storage_domain_content">
                    <div  class="content_header_right">
                        <button>새로만들기</button>
                        <button>편집</button>
                        <button>삭제</button>
                    </div>
                    <div>
                        <div class="application_content_header">
                            <button><i class="fa fa-chevron-left"></i></button>
                            <div>1-2</div>
                            <button><i class="fa fa-chevron-right"></i></button>
                            <button><i class="fa fa-ellipsis-v"></i></button>            
                        </div>
                    </div>
                    <table>
                        <thead>
                            <tr>
                                <th>이름</th>
                                <th>설명</th>
                                
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>ovirtmgmt</td>
                                <td>Management Network</td>
                            </tr>
                            
                        </tbody>
                    </table>
                </div>
            </div>

            <!--디테일 디스크 프로파일버튼-->
            <div id="detail_profile_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div class="active">일반</div>
                        <div>데이터 센터</div>
                        <div>가상머신</div>
                        <div>템플릿</div>
                        <div>디스크</div>
                        <div>디스크 스냅샷</div>
                        <div>임대</div>
                        <div>디스크 프로파일</div>
                        <div>이벤트</div>
                        <div>권한</div>
                    </div>
                </div>
                
                <div class="storage_domain_content">
                    <div>
                        <div class="application_content_header">
                            <button><i class="fa fa-chevron-left"></i></button>
                            <div>1-2</div>
                            <button><i class="fa fa-chevron-right"></i></button>
                            <button><i class="fa fa-ellipsis-v"></i></button>            
                        </div>
                    </div>
                    <table>
                        <thead>
                            <tr>
                                <th>이름</th>
                                <th>호환 버전</th>
                                <th>설명</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>ovirtmgmt</td>
                                <td>Management Network</td>
                                <td>The default server cluster</td>
                            </tr>
                            
                        </tbody>
                    </table>
                </div>
            </div>

            <!--디스크 이벤트버튼-->
            <div id="detail_event_outer" style="display: none;">
                <div class="content_header">
                    <div class="content_header_left">
                        <div class="active">일반</div>
                        <div>데이터 센터</div>
                        <div>가상머신</div>
                        <div>템플릿</div>
                        <div>디스크</div>
                        <div>디스크 스냅샷</div>
                        <div>임대</div>
                        <div>디스크 프로파일</div>
                        <div>이벤트</div>
                        <div>권한</div>
                    </div>
                    
                </div>
                
                <div class="storage_domain_content">
                    <div  class="content_header_right">
                        <button>추가</button>
                        <button>제거</button>
                    </div>
                    <div class="storage_right_btns">
                        <span>Permission Filters:</span>
                        <div>
                            <button>All</button>
                            <button>Direct</button>
                        </div>
                    </div>
                    <div>
                        <div class="application_content_header">
                            <button><i class="fa fa-chevron-left"></i></button>
                            <div>1-2</div>
                            <button><i class="fa fa-chevron-right"></i></button>
                            <button><i class="fa fa-ellipsis-v"></i></button>            
                        </div>
                    </div>
                    <table>
                        <thead>
                            <tr>
                                <th></th>
                                <th>사용자</th>
                                <th>인증 공급자</th>
                                <th>네임스페이스</th>
                                <th>역할</th>
                                <th>생성일</th>
                                <th>Inherited From</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><i class="fa fa-user"></i></td>
                                <td>ovirtmgmt</td>
                                <td></td>
                                <td>*</td>
                                <td>SuperUser</td>
                                <td>2023.12.29 AM 11:40:58</td>
                                <td>(시스템)</td>
                            </tr>
                            
                        </tbody>
                    </table>
                </div>
            </div>


            <!--footer-->
            <div class="footer_outer">
                <div class="footer">
                    <button><i class="fa fa-chevron-down"></i></button>
                    <div>
                        <a>최근 작업</a>
                        <a>경보</a>
                    </div>
                </div>
                <div class="footer_content">

                    <div class="footer_nav">
                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div>
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>

                        <div style="border-right: none;">
                            <div>작업이름</div>
                            <div><i class="fa fa-filter"></i></div>
                        </div>
                    </div>

                    <div class="footer_img">
                        <img src="img/화면 캡처 2024-04-30 164511.png">
                        <span>항목을 찾지 못했습니다</span>
                    </div>

                </div>
            </div><!--footer끝-->
        </div><!--content_detail_section끝-->

    </div><!--main_outer끝-->

    <script>
        /*font-size반응형*/
        function adjustFontSize() {
            var width = window.innerWidth;
            var fontSize = width / 40; // Adjust this value as needed

            document.documentElement.style.fontSize = fontSize + 'px';
        }

        // Call the adjustFontSize function when the window size changes
        window.onresize = function() {
            adjustFontSize();
        };

        // Call the adjustFontSize function initially
        adjustFontSize();

    </script>
<!--편집팝업-->
<div id="edit_popup_bg">
    <div id="edit_popup">
        <div class="edit_header">
            <h1>템플릿 수정</h1>
            <button><i class="fa fa-times"></i></button>
        </div>
        <div class="edit_body">
            <div class="edit_aside">
                    <div id="common_btn">
                        <span>일반</span>
                    </div>
                    <div id="system_btn">
                        <span>시스템</span>
                    </div>
                    <div id="start_run">
                        <span>초기 실행</span>
                    </div>
                    <div id="console_btn">
                        <span>콘솔</span>
                    </div> 
            </div>
            <div class="edit_aside">
                    <div id="host_btn">
                        <span>호스트</span>
                    </div>
                    <div id="ha_mode_btn">
                        <span>고가용성</span>
                    </div>
                    <div id="res_alloc_btn">
                        <span>리소스 할당</span>
                    </div>
                    <div  id="boot_btn">
                        <span>부트 옵션</span>
                    </div>
                    <!--
                    <div  id="preference_btn">
                        <span>선호도</span>
                    </div>
                    -->
            </div>

            <form action="#">
                <!--일반-->
                <div id="common_outer">
                    <div class="edit_first_content">
                        <div>
                            <label for="cluster">클러스터</label>
                            <select id="cluster">
                                <option value="default">Default</option>   
                            </select>
                            <div>데이터센터 Default</div>
                        </div>
                        
                        <div>
                            <label for="template" style="color: gray;">템플릿에 근거</label>
                            <select id="template" disabled >
                                <option value="test02">test02</option>
                            </select>
                        </div>
                        <div>
                            <label for="os">운영 시스템</label>
                            <select id="os">
                                <option value="linux">Linux</option>
                            </select>
                        </div>
                        <div>
                            <label for="firmware">칩셋/펌웨어 유형</label>
                            <select id="firmware">
                                <option value="bios">BIOS의 Q35 칩셋</option>
                            </select>
                        </div>
                        <div style="margin-bottom: 2%;">
                            <label for="optimization">최적화 옵션</label>
                            <select id="optimization">
                                <option value="server">서버</option>
                            </select>
                        </div>
                    </div>

                    <div class="edit_second_content">
                        <div>
                            <label for="name">이름</label>
                            <input type="text" id="name" value="test02">
                        </div>
                        <div>
                            <label for="base-version" style="color: gray;">하위 버전 이름</label>
                            <input type="text" id="base-version" value="base version" disabled>
                        </div>
                        <div>
                            <label for="description">설명</label>
                            <input type="text" id="description">
                        </div>
                    </div>
                </div><!--common_outer끝-->

                 <!--시스템-->
                 <div id="system_outer">
                    <div class="edit_first_content">
                        <div>
                            <label for="cluster">클러스터</label>
                            <select id="cluster">
                                <option value="default">Default</option>   
                            </select>
                            <div>데이터센터 Default</div>
                        </div>
                        
                        <div>
                            <label for="template" style="color: gray;">템플릿에 근거</label>
                            <select id="template" disabled >
                                <option value="test02">test02</option>
                            </select>
                        </div>
                        <div>
                            <label for="os">운영 시스템</label>
                            <select id="os">
                                <option value="linux">Linux</option>
                            </select>
                        </div>
                        <div>
                            <label for="firmware">칩셋/펌웨어 유형</label>
                            <select id="firmware">
                                <option value="bios">BIOS의 Q35 칩셋</option>
                            </select>
                        </div>
                        <div style="margin-bottom: 2%;">
                            <label for="optimization">최적화 옵션</label>
                            <select id="optimization">
                                <option value="server">서버</option>
                            </select>
                        </div>
                    </div>

                    <div class="edit_second_content">
                        <div>
                            <label for="memory_size">메모리 크기</label>
                            <input type="text" id="memory_size" value="2048 MB" readonly>
                        </div>
                        <div>
                            <div>
                                <label for="max_memory">최대 메모리</label>
                                <i class="fa fa-info-circle"></i>
                            </div>
                            <input type="text" id="max_memory" value="8192 MB" readonly>
                        </div>
                        
                        <div>
                            <div>
                                <label for="actual_memory">할당할 실제 메모리</label>
                                <i class="fa fa-info-circle"></i>
                            </div>
                            <input type="text" id="actual_memory" value="2048 MB" readonly>
                        </div>
                        
                        <div>
                            <div>
                                <label for="total_cpu">총 가상 CPU</label>
                                <i class="fa fa-info-circle"></i>
                            </div>
                            <input type="text" id="total_cpu" value="1" readonly>
                        </div>
                        <div>
                            <div>
                                <i class="fa fa-arrow-circle-o-right" style="color: rgb(56, 56, 56);"></i>
                                <span>고급 매개 변수</span>
                            </div>
                        </div>
                        <div style="font-weight: 600;">일반</div>
                        <div style="padding-top: 0;padding-bottom: 4%">
                            <div>
                                <label for="time_offset">하드웨어 클릭의 시간 오프셋</label>
                                <i class="fa fa-info-circle"></i>
                            </div>
                            <select id="time_offset">
                                <option value="(GMT+09:00) Korea Standard Time">(GMT+09:00) Korea Standard Time</option>
                            </select>
                        </div>                      
                    </div>
                </div><!--system_outer끝-->
                
                <!--초기실행-->

                <!--콘솔-->
                <div id="console_outer">
                    <div class="edit_first_content">
                        <div>
                            <label for="cluster">클러스터</label>
                            <select id="cluster">
                                <option value="default">Default</option>   
                            </select>
                            <div>데이터센터 Default</div>
                        </div>
                        
                        <div>
                            <label for="template" style="color: gray;">템플릿에 근거</label>
                            <select id="template" disabled >
                                <option value="test02">test02</option>
                            </select>
                        </div>
                        <div>
                            <label for="os">운영 시스템</label>
                            <select id="os">
                                <option value="linux">Linux</option>
                            </select>
                        </div>
                        <div>
                            <label for="firmware">칩셋/펌웨어 유형</label>
                            <select id="firmware">
                                <option value="bios">BIOS의 Q35 칩셋</option>
                            </select>
                        </div>
                        <div style="margin-bottom: 2%;">
                            <label for="optimization">최적화 옵션</label>
                            <select id="optimization">
                                <option value="server">서버</option>
                            </select>
                        </div>
                    </div>

                    <div class="res_alloc_checkbox" style="margin-bottom: 0;">
                        <span>그래픽 콘솔</span>
                        <div>
                            <input type="checkbox" id="memory_balloon" name="memory_balloon">
                            <label for="memory_balloon">헤드릭스(headless)모드</label>
                            <i class="fa fa-info-circle" style="color: #1ba4e4;"></i>
                        </div>
                    </div>

                    <div class="edit_second_content">
                        <div style="padding-top: 0;">
                            <label for="memory_size">비디오 유형</label>
                            <input type="text" id="memory_size" value="VGA" readonly>
                        </div>
                        <div>
                            <div>
                                <label for="max_memory">그래픽 프로토콜</label>
                              
                            </div>
                            <input type="text" id="max_memory" value="VNC" readonly>
                        </div>
                        
                        <div>
                            <div>
                                <label for="actual_memory">VNC 키보드 레이아웃</label>
                            </div>
                            <input type="text" id="actual_memory" value="기본값[en-us]" readonly>
                        </div>
                        
                        <div>
                            <div>
                                <label for="total_cpu">콘솔 분리 작업</label>
                               
                            </div>
                            <input type="text" id="total_cpu" value="화면 잠금" readonly>
                        </div>
                        <div>
                            <div>
                                <label for="disconnect_action_delay">Disconnect Action Delay in Minutes</label>
                            </div>
                            <input type="text" id="disconnect_action_delay" value="0" disabled>
                        </div>
                        <div id="monitor">
                            <label for="screen">모니터</label>
                            <select id="screen">
                                <option value="test02">1</option>
                            </select>
                        </div>              
                    </div>

                    <div class="console_checkboxs">
                        <div class="console_checkbox">
                            <input type="checkbox" id="memory_balloon" name="memory_balloon" disabled>
                            <label  style="color: #A1A1A1;" for="memory_balloon">USB활성화</label>
                        </div>
                        <div class="console_checkbox">
                            <input type="checkbox" id="memory_balloon" name="memory_balloon" disabled>
                            <label  style="color: #A1A1A1;" for="memory_balloon">스마트카드 사용가능</label>
                        </div>
                        <span>단일 로그인 방식</span>
                        <div class="console_checkbox">
                            <input type="checkbox" id="memory_balloon" name="memory_balloon">
                            <label for="memory_balloon">USB활성화</label>
                        </div>
                        <div class="console_checkbox">
                            <input type="checkbox" id="memory_balloon" name="memory_balloon">
                            <label for="memory_balloon">스마트카드 사용가능</label>
                        </div>
                    </div>
                </div><!--console_outer끝-->
 
                <!--호스트-->
                <div id="host_outer">
                    <div class="edit_first_content">
                        <div>
                            <label for="cluster">클러스터</label>
                            <select id="cluster">
                                <option value="default">Default</option>   
                            </select>
                            <div>데이터센터 Default</div>
                        </div>
                        
                        <div>
                            <label for="template" style="color: gray;">템플릿에 근거</label>
                            <select id="template" disabled >
                                <option value="test02">test02</option>
                            </select>
                        </div>
                        <div>
                            <label for="os">운영 시스템</label>
                            <select id="os">
                                <option value="linux">Linux</option>
                            </select>
                        </div>
                        <div>
                            <label for="firmware">칩셋/펌웨어 유형</label>
                            <select id="firmware">
                                <option value="bios">BIOS의 Q35 칩셋</option>
                            </select>
                        </div>
                        <div style="margin-bottom: 2%;">
                            <label for="optimization">최적화 옵션</label>
                            <select id="optimization">
                                <option value="server">서버</option>
                            </select>
                        </div>
                    </div>

                    <div id="host_second_content">
                        <div style="font-weight: 600;">실행 호스트:</div>
                        <div class="form_checks">
                            <div>
                                <input class="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault1" checked>
                                <label class="form-check-label" for="flexRadioDefault1">
                                클러스터 내의 호스트
                                </label>
                            </div>
                            <div>
                                <div>
                                    <input class="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault2">
                                    <label class="form-check-label" for="flexRadioDefault2">
                                    특정 호스트
                                    </label>
                                </div>
                                <div>
                                    <select id="specific_host_select">
                                        <option value="host02.ititinfo.com">host02.ititinfo.com</option>
                                    </select>
                                </div>
                            </div>
                        </div> 
                        <div class="host_checkboxs">
                            <span>CPU 옵션:</span>
                            <div class="host_checkbox">
                                <input type="checkbox" id="host_cpu_passthrough" name="host_cpu_passthrough">
                                <label for="host_cpu_passthrough">호스트 CPU 통과</label>
                                <i class="fa fa-info-circle"></i>
                            </div>
                            <div class="host_checkbox">
                                <input type="checkbox" id="tsc_migration" name="tsc_migration">
                                <label for="tsc_migration">TSC 주파수가 동일한 호스트에서만 마이그레이션</label>
                                <i class="fa fa-info-circle"></i>
                            </div>
                        </div>                 
                    </div>

                    <div id="host_third_content">
                        <div style="font-weight: 600;">마이그레이션 옵션:</div>
                        <div>
                            <div>
                                <span>마이그레이션 모드</span>
                                <i class="fa fa-info-circle"></i>
                            </div>
                            <select id="migration_mode">
                                <option value="수동 및 자동 마이그레이션 허용">수동 및 자동 마이그레이션 허용</option>
                            </select>
                        </div>
                        <div>
                            <div>
                                <span>마이그레이션 정책</span>
                                <i class="fa fa-info-circle"></i>
                            </div>
                            <select id="migration_policy">
                                <option value="클러스터 기본값(Minimal downtime)">클러스터 기본값(Minimal downtime)</option>
                            </select>
                        </div>
                        <div>
                            <div>
                                <span>마이그레이션 암호화 사용</span>
                            </div>
                            <select id="migration_encryption">
                                <option value="클러스터 기본값(암호화하지 마십시오)">클러스터 기본값(암호화하지 마십시오)</option>
                            </select>
                        </div>
                        <div>
                            <div>
                                <span>Parallel Migrations</span>
                                <i class="fa fa-info-circle"></i>
                            </div>
                            <select id="parallel_migrations"  readonly >
                                <option value="클러스터 기본값(Disabled)">클러스터 기본값(Disabled)</option>
                            </select>
                        </div>
                        <div>
                            <div style="padding-bottom: 4%;">
                                <span style="color: gray;">Number of VM Migration Connection</span>
                            </div>
                            <select id="vm_migration_connections" disabled>
                                <option value=""></option>
                            </select>
                        </div>
                        
                    </div>
                </div><!--host_outer끝-->

                <!-- 고가용성 -->
                <div id="ha_mode_outer">
                    <div class="edit_first_content">
                        <div>
                            <label for="cluster">클러스터</label>
                            <select id="cluster">
                                <option value="default">Default</option>   
                            </select>
                            <div>데이터센터 Default</div>
                        </div>
                        
                        <div>
                            <label for="template" style="color: gray;">템플릿에 근거</label>
                            <select id="template" disabled >
                                <option value="test02">test02</option>
                            </select>
                        </div>
                        <div>
                            <label for="os">운영 시스템</label>
                            <select id="os">
                                <option value="linux">Linux</option>
                            </select>
                        </div>
                        <div>
                            <label for="firmware">칩셋/펌웨어 유형</label>
                            <select id="firmware">
                                <option value="bios">BIOS의 Q35 칩셋</option>
                            </select>
                        </div>
                        <div style="margin-bottom: 2%;">
                            <label for="optimization">최적화 옵션</label>
                            <select id="optimization">
                                <option value="server">서버</option>
                            </select>
                        </div>
                    </div>

                    <div id="ha_mode_second_content">
                        <div class="check_box">
                            <input class="check_input" type="checkbox" value="" id="ha_mode_box">
                            <label class="check_label" for="ha_mode_box">
                              고가용성
                            </label>
                        </div>
                        <div>
                            <div>
                                <span>가상 머신 임대 대상 스토리지 도메인</span>
                                <i class="fa fa-info-circle"></i>
                            </div>
                            <select id="no_lease"  disabled>
                                <option value="가상 머신 임대 없음">가상 머신 임대 없음</option>
                            </select>
                        </div>
                        <div>
                            <div>
                                <span>재개 동작</span>
                                <i class="fa fa-info-circle"></i>
                            </div>
                            <select id="force_shutdown">
                                <option value="강제 종료">강제 종료</option>
                            </select>
                        </div>
                        <div class="ha_mode_article">
                            <span>실행/마이그레이션 큐에서 우선순위</span>
                            <div>
                                <span>우선 순위</span>
                                <select id="priority">
                                    <option value="낮음">낮음</option>
                                </select>
                            </div>
                        </div>

                        <div class="ha_mode_article">
                            <span>위치독</span>
                            <div>
                                <span>위치독 모델</span>
                                <select id="watchdog_model">
                                    <option value="감시 장치 없음">감시 장치 없음</option>
                                </select>
                            </div>
                            <div>
                                <span style="color: gray;">위치독 작업</span>
                                <select id="watchdog_action" disabled>
                                    <option value="없음">없음</option>
                                </select>
                            </div>
                        </div>
                    </div>

                </div><!--ha_mode_outer끝-->

                <!-- 리소스 할당 -->
                <div id="res_alloc_outer">
                    <div class="edit_first_content">
                        <div>
                            <label for="cluster">클러스터</label>
                            <select id="cluster">
                                <option value="default">Default</option>   
                            </select>
                            <div>데이터센터 Default</div>
                        </div>
                        
                        <div>
                            <label for="template" style="color: gray;">템플릿에 근거</label>
                            <select id="template" disabled >
                                <option value="test02">test02</option>
                            </select>
                        </div>
                        <div>
                            <label for="os">운영 시스템</label>
                            <select id="os">
                                <option value="linux">Linux</option>
                            </select>
                        </div>
                        <div>
                            <label for="firmware">칩셋/펌웨어 유형</label>
                            <select id="firmware">
                                <option value="bios">BIOS의 Q35 칩셋</option>
                            </select>
                        </div>
                        <div style="margin-bottom: 2%;">
                            <label for="optimization">최적화 옵션</label>
                            <select id="optimization">
                                <option value="server">서버</option>
                            </select>
                        </div>
                    </div>

                    <div class="res_second_content">
                        <div class="cpu_res">
                            <span style="font-weight: 600;">CPU 할당:</span>
                            <div>
                                <span>CPU 프로파일</span>
                                <select id="watchdog_action">
                                    <option value="없음">Default</option>
                                </select>
                            </div>
                            <div>
                                <span>CPU 공유</span>
                                <div id="cpu_sharing">
                                    <select id="watchdog_action" style="width: 63%;">
                                        <option value="없음">비활성화됨</option>
                                    </select>
                                    <input type="text" value="0" disabled>
                                </div>
                            </div>
                            <div>
                                <span>CPU Pinning Policy</span>
                                <select id="watchdog_action">
                                    <option value="없음">None</option>
                                </select>
                            </div>
                            <div>
                                <div>
                                    <span>CPU 피닝 토폴로지</span>
                                    <i class="fa fa-info-circle"></i>
                                </div>
                                <input type="text" disabled/>
                            </div>
                        </div>
                        
                        <span style="font-weight: 600;">I/O 스레드:</span>
                        <div id="threads">
                            
                            <div>
                                <input type="checkbox" id="enableIOThreads" name="enableIOThreads">
                                <label for="enableIOThreads">I/O 스레드 활성화</label>
                            </div>
                            <div>
                                <input type="text"/>
                                <i class="fa fa-info-circle"></i>
                            </div>
                        </div>
                    </div>


                </div><!--res_alloc_outer끝-->

                <!--부트옵션-->
                <div id="boot_outer">
                    <div class="edit_first_content">
                        <div>
                            <label for="cluster">클러스터</label>
                            <select id="cluster">
                                <option value="default">Default</option>   
                            </select>
                            <div>데이터센터 Default</div>
                        </div>
                        
                        <div>
                            <label for="template" style="color: gray;">템플릿에 근거</label>
                            <select id="template" disabled >
                                <option value="test02">test02</option>
                            </select>
                        </div>
                        <div>
                            <label for="os">운영 시스템</label>
                            <select id="os">
                                <option value="linux">Linux</option>
                            </select>
                        </div>
                        <div>
                            <label for="firmware">칩셋/펌웨어 유형</label>
                            <select id="firmware">
                                <option value="bios">BIOS의 Q35 칩셋</option>
                            </select>
                        </div>
                        <div style="margin-bottom: 2%;">
                            <label for="optimization">최적화 옵션</label>
                            <select id="optimization">
                                <option value="server">서버</option>
                            </select>
                        </div>
                    </div>

                    <div class="res_second_content">
                        <div class="cpu_res">
                            <span style="font-weight: 600;">부트순서:</span>
                            <div>
                                <span>첫 번째 장치</span>
                                <select id="watchdog_action">
                                    <option value="없음">하드디스크</option>
                                </select>
                            </div>
                            <div>
                                <span>두 번째 장치</span>
                                <select id="watchdog_action">
                                    <option value="없음">Default</option>
                                </select>
                            </div>
                        </div>
                        
                        
                        <div id="boot_checkboxs">
                            <div>
                                <div>
                                    <input type="checkbox" id="connectCdDvd" name="connectCdDvd">
                                    <label for="connectCdDvd">CD/DVD 연결</label>
                                </div>
                                <div>
                                    <input type="text"disabled/>
                                    <i class="fa fa-info-circle"></i>
                                </div>
                            </div>

                            <div>
                                <input type="checkbox" id="enableBootMenu" name="enableBootMenu">
                                <label for="enableBootMenu">부팅 메뉴를 활성화</label>
                            </div>

                        </div>
                    </div>


                </div><!--boot_outer끝-->

                <div id="preference_outer">
                    <div class="edit_first_content">
                        <div>
                            <label for="cluster">클러스터</label>
                            <select id="cluster">
                                <option value="default">Default</option>   
                            </select>
                            <div>데이터센터 Default</div>
                        </div>
                        
                        <div>
                            <label for="template" style="color: gray;">템플릿에 근거</label>
                            <select id="template" disabled >
                                <option value="test02">test02</option>
                            </select>
                        </div>
                        <div>
                            <label for="os">운영 시스템</label>
                            <select id="os">
                                <option value="linux">Linux</option>
                            </select>
                        </div>
                        <div>
                            <label for="firmware">칩셋/펌웨어 유형</label>
                            <select id="firmware">
                                <option value="bios">BIOS의 Q35 칩셋</option>
                            </select>
                        </div>
                        <div style="margin-bottom: 2%;">
                            <label for="optimization">최적화 옵션</label>
                            <select id="optimization">
                                <option value="server">서버</option>
                            </select>
                        </div>
                    </div>

                    <div id="preference_content">
                        <div class="preference_select">
                            <label for="preference_group">선호도 그룹을 선택하십시오</label>
                            <div>
                                <select id="preference_group" name="preference_group">
                                    <option value="test01">test01</option>
                                </select>
                                <button>추가</button>
                            </div>
                        </div>
                        <div class="pre_selected">
                            <span>선택된 선호도 그룹</span>
                            <div>
                                <a>test02</a>
                            </div>
                        </div>
                        <div class="preference_select">
                            <label for="preference_label">선호도 레이블 선택</label>
                            <div>
                                <select id="preference_label" name="preference_label">
                                    <option value="test">test</option>
                                </select>
                                <button>추가</button>
                            </div>
                        </div>
                        <div class="pre_selected">
                            <span>선택된 선호도 그룹</span>
                            <div>
                                <a>test02</a>
                            </div>
                        </div>
                    </div>


                </div><!--preference_outer끝-->


                <!--content_nav버튼들-->

            </form>
        </div><!--edit_body끝-->

        <div class="edit_footer">
            <button>고급 옵션 숨기기</button>
            <button>OK</button>
            <button>취소</button>
        </div>
    </div>

    
</div>


</body>
<!--그래프들-->
<script>
    /*막대 그래프*/
    new Chart(document.getElementById("bar-chart-horizontal"), {
        type: 'horizontalBar',
        data: {
          labels: ["vm1", "vm2", "vm3"],
          datasets: [
            {
              label: "Population (millions)",
              backgroundColor: ["#7C7DEA", "#1597E5","#69DADB"],
              data: [900,600,700,400]
            }
          ]
        },
        options: {
          legend: { display: false },
          title: {
            display: true,
            text: 'TOP 3'
          },
          scales: {
            x: {
              ticks: {
                display: false // x축 눈금 숨기기
              }
            },
            y: {
              ticks: {
                display: false // y축 눈금 숨기기
              }
            }
          }
        }
    });
    new Chart(document.getElementById("bar-chart-horizontal2"), {
        type: 'horizontalBar',
        data: {
          labels: ["vm1", "vm2", "vm3"],
          datasets: [
            {
              label: "Population (millions)",
              backgroundColor: ["#7C7DEA", "#1597E5","#69DADB"],
              data: [2478,5267,734,784,433]
            }
          ]
        },
        options: {
          legend: { display: false },
          title: {
            display: true,
            text: 'TOP 3'
          }
        }
    });
    new Chart(document.getElementById("bar-chart-horizontal3"), {
        type: 'horizontalBar',
        data: {
          labels: ["vm1", "vm2", "vm3"],
          datasets: [
            {
              label: "Population (millions)",
              backgroundColor: ["#7C7DEA", "#1597E5","#69DADB"],
              data: [2478,5267,734,784,433]
            }
          ]
        },
        options: {
          legend: { display: false },
          title: {
            display: true,
            text: 'TOP 3'
          }
        }
    });
    
    /*도넛*/
    $(window).ready(function() {
        if ($(window).width() >= 3000) { // 뷰포트 너비가 3000px 이상인 경우
            createCircleGraph('.circle-graph1', 70, 200, 13);
            createCircleGraph('.circle-graph2', 60, 200, 13);
            createCircleGraph('.circle-graph3', 90, 200, 13);
        } else { // 그렇지 않은 경우
            createCircleGraph('.circle-graph1', 30, 130, 8);
            createCircleGraph('.circle-graph2', 20, 130, 8);
            createCircleGraph('.circle-graph3', 70, 130, 8);
        }
    });
    
    function createCircleGraph(classname, perNum, size, thickness) {
        let circleGraph = $(classname);
        let color;
        
        if (perNum < 30) {
            color = 'green'; // 30 미만이면 초록색
        } else if (perNum < 70) {
            color = '#FFC100'; // 30 이상 70 미만이면 노란색
        } else {
            color = 'red'; // 70 이상이면 빨간색
        }
    
        circleGraph.circleProgress({
            size: size, // 그래프 크기 조정
            value: perNum / 100, // 그래프에 표시될 값
            startAngle: 300, // 시작지점
            thickness: thickness, // 그래프 두께 조정
            fill: { // 그래프 선 색
                gradient: [color],
                gradientAngle: 0
            },
            animation: {
                duration: 2000,
                easing: "swing"
            },
            lineCap: "butt", // 그래프 선 모양
            reverse: false // 그래프가 진행되는 방향
        }).on('circle-animation-progress', function(event, progress) {
            // 그래프 애니메이션이 진행되는 동안
            // progress - 현재 진행 상태 0.0 ~ 1.0
            $(this).find('.circle-percent').html(Math.round(perNum * progress) + '<i>%</i>');
        });
    }
    
    /*물선그래프*/
    const ctx = document.getElementById('myChart');
    new Chart(document.getElementById("line-chart"), {
      type: 'line',
      data: {
        labels: [1500,1600,1700,1750,1800,1850,1900,1950,1999,2050],
        datasets: [{ 
            data: [100,200,300,400,500,700,900,1000],
            label: "Africa",
            borderColor: "#1597E5",
            fill: false
          }, { 
            data: [1000,500,600,200],
            label: "Asia",
            borderColor: "#69DADB",
            fill: false
          }, { 
            data: [1200,900,700,500,400,200],
            label: "Asia333",
            borderColor: "red",
            fill: false
          }
        ]
      },
      options: {
        responsive: false,
      }
    });
    new Chart(document.getElementById("line-chart2"), {
      type: 'line',
      data: {
        labels: [1500,1600,1700,1750,1800,1850,1900,1950,1999,2050],
        datasets: [{ 
            data: [100,200,300,400,500,700,900,1000],
            label: "Africa",
            borderColor: "#1597E5",
            fill: false
          }, { 
            data: [1000,500,600,200],
            label: "Asia",
            borderColor: "#69DADB",
            fill: false
          }, { 
            data: [1200,900,700,500,400,200],
            label: "Asia333",
            borderColor: "red",
            fill: false
          }
        ]
      },
      options: {
        responsive: false,
      }
    });
    new Chart(document.getElementById("line-chart3"), {
      type: 'line',
      data: {
        labels: [1500,1600,1700,1750,1800,1850,1900,1950,1999,2050],
        datasets: [{ 
            data: [100,200,300,400,500,700,900,1000],
            label: "Africa",
            borderColor: "#1597E5",
            fill: false
          }, { 
            data: [1000,500,600,200],
            label: "Asia",
            borderColor: "#69DADB",
            fill: false
          }, { 
            data: [1200,900,700,500,400,200],
            label: "Asia333",
            borderColor: "red",
            fill: false
          }
        ]
      },
      options: {
        responsive: false,
      }
    });
    
</script>
</html>