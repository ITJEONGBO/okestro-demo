<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script type="text/javascript">

        $(document).ready(function(){

        	$("#ok").click(function(){

        		if($("#name").val() == ''){
        			alert("이름를 입력해주세요");
        			$("#name").focus();
        			return;
        		}

        		if($("#hostIp").val() == ''){
        			alert("호스트이름/IP를 입력해주세요");
        			$("#hostIp").focus();
        			return;
        		}

        		if($("#password").val() == ''){
        			alert("암호를 입력해주세요");
        			$("#password").focus();
        			return;
        		}

        		$("#add").submit();
        	});


        	$("#cancel").click(function(){
        	    window.close();
        	});


        });
    </script>

    <style>
        table, tr, td{
            border:none;
        }
        #hostT{
            width: 140px;
        }

    </style>

</head>
<body>
    <div style="padding: 1rem;">
        <div>
            <h2>새 가상머신</h2>

            <form id="add" autocomplete="off" method="get" action="vm-add2">
                <hr>
                <h3>일반</h3>
                <table>
                    <tr>
                        <td id="hostT">클러스터</td>
                        <td>
                            <select id="clusterId" name="clusterId">
                                <c:forEach var="c" items="${c}" varStatus="status">
                                    <option value="${c.id}">${c.name} (데이터 센터: ${c.datacenterName})</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td id="hostT">템플릿</td>
                        <td>
                            <select id="templateId" name="templateId">
                                <c:forEach var="template" items="${template}" varStatus="status">
                                    <option value="${template.id}">${template.name}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td id="hostT">코멘트</td>
                        <td>
                            <select id="os" name="os">
                                <option value="Debian 7+">Debian 7+</option>
                                <option value="Debian 9+">Debian 9+</option>
                                <option value="FreeBSD 9.2">FreeBSD 9.2</option>
                                <option value="FreeBSD 9.2 x64">FreeBSD 9.2 x64</option>
                                <option value="Linux">Linux</option>
                                <option value="Other Linux">Other Linux (kernel 4.x)</option>
                                <option value="Other OS" selected>Other OS</option>
                                <option value="Red Hat Atomic 7.x x64">Red Hat Atomic 7.x x64</option>
                                <option value="Red Hat Enterprise Linux 3.x">Red Hat Enterprise Linux 3.x</option>
                                <option value="Red Hat Enterprise Linux 3.x x64">Red Hat Enterprise Linux 3.x x64</option>
                                <option value="Red Hat Enterprise Linux 4.x">Red Hat Enterprise Linux 4.x</option>
                                <option value="Red Hat Enterprise Linux 4.x x64">Red Hat Enterprise Linux 4.x x64</option>
                                <option value="Red Hat Enterprise Linux 5.x">Red Hat Enterprise Linux 5.x</option>
                                <option value="Red Hat Enterprise Linux 5.x x64">Red Hat Enterprise Linux 5.x x64</option>
                                <option value="Red Hat Enterprise Linux 6.x">Red Hat Enterprise Linux 6.x</option>
                                <option value="Red Hat Enterprise Linux 6.x x64">Red Hat Enterprise Linux 6.x x64</option>
                                <option value="Red Hat Enterprise Linux 7.x x64">Red Hat Enterprise Linux 7.x x64</option>
                                <option value="Red Hat Enterprise Linux 8.x x64">Red Hat Enterprise Linux 8.x x64</option>
                                <option value="Red Hat Enterprise Linux 9.x x64">Red Hat Enterprise Linux 9.x x64</option>
                                <option value="Red Hat Enterprise Linux CoreOS">Red Hat Enterprise Linux CoreOS</option>
                                <option value="SUSE Linux Enterprise Server 11+">SUSE Linux Enterprise Server 11+</option>
                                <option value="Ubuntu Bionic Beaver LTS+">Ubuntu Bionic Beaver LTS+</option>
                                <option value="Ubuntu Precise Pangolin LTS">Ubuntu Precise Pangolin LTS</option>
                                <option value="Ubuntu Quantal Quetzal">Ubuntu Quantal Quetzal</option>
                                <option value="Ubuntu Raring Ringtails">Ubuntu Raring Ringtails</option>
                                <option value="Ubuntu Saucy Salamander">Ubuntu Saucy Salamander</option>
                                <option value="Ubuntu Trusty Tahr LTS+">Ubuntu Trusty Tahr LTS+</option>
                                <option value="Window 10">Window 10</option>
                                <option value="Window 10 x64">Window 10 x64</option>
                                <option value="Window 11">Window 11</option>
                                <option value="Window 2003">Window 2003</option>
                                <option value="Window 2003 x64">Window 2003 x64</option>
                                <option value="Window 2008">Window 2008</option>
                                <option value="Window 2008 R2 x64">Window 2008 R2 x64</option>
                                <option value="Window 2008 x64">Window 2008 x64</option>
                                <option value="Window 2012 x64">Window 2012 x64</option>
                                <option value="Window 2012 R2 x64">Window 2012 R2 x64</option>
                                <option value="Window 2016 x64">Window 2016 x64</option>
                                <option value="Window 2019 x64">Window 2019 x64</option>
                                <option value="Window 2022">Window 2022</option>
                                <option value="Window 7">Window 7</option>
                                <option value="Window 7 x64">Window 7 x64</option>
                                <option value="Window 8">Window 8</option>
                                <option value="Window 8 x64">Window 8 x64</option>
                                <option value="Window XP">Window XP</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td id="hostT">칩셋/펌웨어 유형</td>
                        <td>
                            <select id="chipsetType" name="chipsetType">
                                <option value="i440fx_sea_bios">BIOS의 1440FX 칩셋</option>
                                <option value="q35_ovmf">BIOS의 Q35 칩셋</option>
                                <option value="q35_sea_bios" selected>UEFI의 Q35 칩셋</option>
                                <option value="q35_secure_boot">UEFI SecureBoot의 Q35 칩셋</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td id="hostT">최적화 옵션</td>
                        <td>
                            <select id="option" name="option">
                                <option value="desktop">데스크탑</option>
                                <option value="server" selected>서버</option>
                                <option value="high_performance">고성능</option>
                            </select>
                        </td>
                    </tr>
                    <tr> <td><br></td> </tr>

                    <tr>
                        <td id="hostT">이름</td>
                        <td><input type="text" id="name" name="name"></td>
                    </tr>
                    <tr>
                        <td id="hostT">설명</td>
                        <td><input type="text" id="description" name="description"></td>
                    </tr>
                    <tr>
                        <td id="hostT">코멘트</td>
                        <td><input type="text" id="comment" name="comment"></td>
                    </tr>
                    <tr>
                        <td id="hostT">가상머신 ID</td>
                        <td><input type="text" id="id" name="id" disabled></td>
                    </tr>

                    <tr>
                        <td><br></td>
                    </tr>
                    <tr>
                        <td style="width: 150px;">
                            <input type="checkbox" id="statusSave" name="statusSave"/> 상태 비저장
                        </td>
                        <td style="width: 15px;">
                            <input type="checkbox" id="statusSave" name="statusSave"/> 일시정지 모드에서 시작
                        </td>
                        <td>
                            <input type="checkbox" id="deleteProtected" name="deleteProtected" value="true" checked/> 삭제 방지
                        </td>
                    </tr>

                    <tr>
                        <td><br></td>
                    </tr>
                    <tr>
                        <td>인스턴스 이미지</td>
                    </tr>
                    <tr>
                        <td style="width: 180px;">
                            <input type="button" id="connect" value="연결">
                            <input type="button" id="addDisk" value="생성">
                            <input type="button" id="plus" value="+">
                            <input type="button" id="minus" value="-">
                        </td>
                    </tr>
                    <tr>
                        <td><br></td>
                    </tr>
                    <tr>
                        <td colspan="2">vNIC 프로파일을 선택하여 가상 머신 네트워크 인터페이스를 인스턴스화합니다.</td>
                    </tr>
                    <tr>
                        <td>nic1</td>
                        <td>
                            <select id="nic" name="nic">
                                <c:forEach var="nic" items="${nic}" varStatus="status">
                                    <option value="${nic.id}">항목을 선택하십시오...</option>
                                    <option value="${nic.id}">${nic.name}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td>
                            <input type="button" id="nicPlus" value="+">
                            <input type="button" id="nicMinus" value="-">
                        </td>
                    </tr>
                </table>
                <hr>

                <h3>시스템</h3>
                <table>
                    <tr>
                        <td id="hostT">메모리 크기</td>
                        <td><input type="text" id="memorySize" name="memorySize"></td>
                    </tr>
                    <tr>
                        <td id="hostT">최대 메모리</td>
                        <td><input type="text" id="memoryMax" name="memoryMax"></td>
                    </tr>
                    <tr>
                        <td id="hostT">할당할 실제 메모리</td>
                        <td><input type="text" id="memoryActual" name="memoryActual"></td>
                    </tr>
                    <tr>
                        <td id="hostT">총 가상 CPU</td>
                        <td><input type="text" id="vCpuCnt" name="vCpuCnt"></td>
                    </tr>
                    <tr>
                        <td><br></td>
                    </tr>

                    <tr>
                        <td><strong>일반</strong></td>
                    </tr>
                    <tr>
                        <td>인스턴스 유형</td>
                        <td>
                            <select id="instanceType" name="instanceType">
                                <option value="">사용자 정의 (인스턴스 유형 없음)</option>
                                <option value="">Large: Large instance type</option>
                                <option value="">Medium: Medium instance type</option>
                                <option value="">Small: Small instance type</option>
                                <option value="">Tiny: Tiny instance type</option>
                                <option value="">XLarge: Extra Large instance type</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 220px;">하드웨어 클럭의 시간 오프셋</td>
                        <td>
                            <select id="timeOffset" name="timeOffset">
                                <option value="">기본값: (GMTZ) Greenwich Standard Time</option>
                                <option value="">(GMT+09:00) Korea Standard Time</option>
                            </select>
                        </td>
                    </tr>
                </table>
                <hr>

                <h3>호스트</h3>
                <table>

                    <tr>
                        <td>
                            <input type='radio' id="clusterHost" name='clusterHost'/> 클러스터 내의 호스트 <br>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type='radio' id="selectHost" name='selectHost'/> 특정 호스트 <br>
                        </td>
                        <td>
                            <select id="hostId" name="hostId">
                                <option value=""></option>
                                <option value=""></option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td><br></td>
                    </tr>
                    <tr>
                        <td><strong>마이그레이션 옵션: </strong></td>
                    </tr>
                    <tr>
                        <td>마이그레이션 모드</td>
                        <td>
                            <select id="migrationMode" name="migrationMode">
                                <option value="inherit">수동 및 자동 마이그레이션 허용</option>
                                <option value="true">수동 마이그레이션만 허용</option>
                                <option value="false">마이그레이션을 허용하지 않음</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>마이그레이션 정책</td>
                        <td>
                            <select id="migrationPolicy" name="migrationPolicy">
                                <option value="inherit">클러스터 기본값(Minimal downtime)</option>
                                <option value="true">Minimal downtime</option>
                                <option value="false">Post-copy migration</option>
                                <option value="false">Suspend workload if needed</option>
                                <option value="false">Very large VMs</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>마이그레이션 암호화 사용</td>
                        <td>
                            <select id="migrationEncoding" name="migrationEncoding">
                                <option value="inherit">클러스터 기본값(암호화하지 마십시오)</option>
                                <option value="true">암호화</option>
                                <option value="false">암호화하지 마십시오</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>Parallel Migrations</td>
                        <td>
                            <select id="parallelMigration" name="parallelMigration">
                                <option value="inherit">클러스터 기본값(disabled)</option>
                                <option value="true">Auto</option>
                                <option value="true">Auto Parallel</option>
                                <option value="true">Disabled</option>
                                <option value="true">Custom</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>Number of VM Migration Connections</td>
                        <td><input type="text" id="numOfVmMigration" name="numOfVmMigration" disabled></td>
                    </tr>
                </table>
                <hr>

                <h3>고가용성</h3>
                <table>
                    <tr>
                        <td>
                            <input type='checkbox' id="ha" name='ha'/> 고가용성 <br>
                        </td>
                    </tr>
                    <tr>
                        <td>가상머신 임대 대상 스토리지 도메인</td>
                        <td>
                            <select id="vmStorageDomain" name="vmStorageDomain">
                                <option value="">가상머신 임대 없음</option>
                                <option value="">forEach</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>재개 동작</td>
                        <td>
                            <select id="resumeOperation" name="resumeOperation">
                                <option value="">자동 재개</option>
                                <option value="">일시 정지</option>
                                <option value="">강제 종료</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><br></td>
                    </tr>

                    <tr>
                        <td><strong>실행/마이그레이션 큐에서 우선 순위:</strong></td>
                    </tr>

                    <tr>
                        <td>우선 순위</td>
                        <td>
                            <select id="priority" name="priority">
                                <option value="">낮음</option>
                                <option value="">중간</option>
                                <option value="">높음</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td><br></td>
                    </tr>
                    <tr>
                        <td><strong>워치독</strong></td>
                    </tr>
                    <tr>
                        <td>워치독 모델</td>
                        <td>
                            <select id="watchDogModel" name="watchDogModel">
                                <option value="">감시 장치 없음</option>
                                <option value="">forEach</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>워치독 작업</td>
                        <td>
                            <select id="watchDogWork" name="watchDogWork" disabled>
                                <option value="">없음</option>
                            </select>
                        </td>
                    </tr>
                </table>
                <hr>


                <h3>리소스 할당</h3>
                <table>
                    <tr>
                        <td><strong>CPU 할당:</strong></td>
                    </tr>
                    <tr>
                        <td>CPU 프로파일</td>
                        <td>
                            <select id="cpuProfile" name="cpuProfile">
                                <option value="">Default</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>CPU 공유</td>
                        <td>
                            <select id="cpuShare" name="cpuShare">
                                <option value="">비활성화됨</option>
                                <option value="512">낮음</option>
                                <option value="1024">중간</option>
                                <option value="2048">높음</option>
                                <option value="">사용자 지정</option>
                            </select>
                        </td>
                        <td>
                            <input type="text" id="cpuShareCnt" name="cpuShareCnt" style="width: 100px;" disabled>
                        </td>
                    </tr>
                    <tr>
                        <td>CPU Pinning Policy</td>
                        <td>
                            <select id="cpuPinningPolicy" name="cpuPinningPolicy">
                                <option value="">None</option>
                                <option value="">Resize and Pin NUMA</option>
                                <option value="">Dedicated</option>
                                <option value="">Isolate Threads</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>CPU 피닝 토폴로지</td>
                        <td>
                            <input type="text" id="cpuShareCnt" name="cpuShareCnt" style="width: 100px;" disabled>
                        </td>
                    </tr>

                    <tr>
                        <td><strong>I/O 스레드:</strong></td>
                    </tr>
                    <tr>
                        <td>
                            <input type='checkbox' id="ha" name='ha'/> I/O 스레드 활성화 <br>
                        </td>
                        <td>
                            <input type="text" id="ioThreadCnt" name="ioThreadCnt"  style="width: 100px; value="1">
                        </td>
                    </tr>
                </table>
                <hr>


                <h3>부트옵션</h3>
                <table>
                    <tr>
                        <td><strong>부트 순서:</strong></td>
                    </tr>
                    <tr>
                        <td>첫 번째 장치</td>
                        <td>
                            <select id="firstDevice" name="firstDevice">
                                <option value="">하드 디스크</option>
                                <option value="">CD-ROM</option>
                                <option value="">네트워크(PXE)</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>두 번째 장치</td>
                        <td>
                            <select id="secondDevice" name="secondDevice">
                                <option value="">없음</option>
                                <option value="">CD-ROM</option>
                                <option value="">네트워크(PXE)</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <input type='checkbox' id="cdDvdConn" name='cdDvdConn'/> CD/DVD 연결 <br>
                        </td>
                        <td>
                            <input type="checkbox" id="connection" name="connection" value="1">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type='checkbox' id="cdDvdConn" name='cdDvdConn'/> 부팅 메뉴를 활성화 <br>
                        </td>
                    </tr>
                </table>
                <hr>

                <h3>선호도</h3>
                <table>
                    <tr>
                        <td>선호도 그룹을 선택하십시오</td>
                        <td>
                            <select id="hostEngine" name="hostEngine">
                                <option value="false">없음</option>
                                <option value="true">배포</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><br></td>
                    </tr>
                    <tr>
                        <td>선택된 선호도 그룹</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td><br></td>
                    </tr>
                    <tr>
                        <td>선호도 레이블 선택</td>
                        <td>
                            <select id="hostEngine" name="hostEngine">
                                <option value="false">없음</option>
                                <option value="true">배포</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><br></td>
                    </tr>
                   <tr>
                       <td>선택한 선호도 레이블</td>
                       <td> </td>
                   </tr>
                </table>
                <hr>

                <input type="button" id="ok" value="OK">
                <input type="reset" id="cancel" value="취소">
            </form>
        </div>

    </div>

</body>
</html>
