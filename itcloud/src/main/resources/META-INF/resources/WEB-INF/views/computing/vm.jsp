<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Vm</h1>
                    컴퓨팅 > <a href="/computing/vms" style="text-decoration-line: none">가상머신</a> > 일반 <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/computing/vm?id=${id}">일반</a> |
                                <a href="/computing/vm-nic?id=${id}" style="text-decoration-line: none">네트워크 인터페이스</a> |
                                <a href="/computing/vm-disk?id=${id}" style="text-decoration-line: none">디스크</a> |
                                <a href="/computing/vm-snapshot?id=${id}" style="text-decoration-line: none">스냅샷</a> |
                                <a href="/computing/vm-application?id=${id}" style="text-decoration-line: none">애플리케이션</a> |
                                <a href="/computing/vm-affGroup?id=${id}" style="text-decoration-line: none">선호도 그룹</a> |
                                <a href="/computing/vm-affLabel?id=${id}" style="text-decoration-line: none">선호도 레이블</a> |
                                <a href="/computing/vm-guest?id=${id}" style="text-decoration-line: none">게스트 정보</a> |
                                <a href="#" style="text-decoration-line: none">권한</a> |
                                <a href="#" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                    <table>
                        <tr>
                            <td width="260px" style="word-break:break-all">
                                이름: ${vm.name} <br>
                                설명: ${vm.description} <br>
                                상태: ${vm.status} <br>
                                업타임: <br>
                                템플릿: <br>
                                운영 시스템: ${vm.osSystem} <br><br>

                                칩셋/펌웨어 유형: ${vm.chipsetFirmwareType} <br>
                                우선순위: ${vm.priority} <br>
                                최적화 옵션: ${vm.optimizeOption}
                            </td>
                            <td width="260px" style="word-break:break-all">
                                설정된 메모리: ${vm.memory / (1024*1024)}MB <br>
                                할당할 실제 메모리: ${vm.memoryActual/ (1024*1024)}MB <br><br>

                                게스트 OS의 여유/캐시+버퍼된 메모리: <br>
                                CPU 코어 수: ${vm.cpuCoreCnt} <br>
                                게스트 CPU 수: ${vm.guestCpuCnt} <br><br>

                                게스트 CPU: ${vm.guestCpu} <br>
                                고가용성: ${vm.ha == "true" ? "예":"아니요"} <br>
                                모니터 수: ${vm.monitor} <br>
                                USB: ${vm.usb == "true" ? "활성화" : "비활성화"}
                            </td>
                            <td width="300px" style="word-break:break-all">
                                작성자: <br>
                                소스: <br>
                                실행 호스트: <br>
                                사용자 정의 속성: <br>
                                클러스터 호환 버전: <br>
                                가상 머신의 ID: ${vm.id} <br><br>

                                FQDN: ${vm.fqdn} <br>
                                하드웨어 클럭의 시간 오프셋: ${vm.hwTimeOffset}
                            </td>
                        </tr>
                    </table>


                    </div>
                </div>
            </main>
        </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    <script src="js/scripts.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js" crossorigin="anonymous"></script>
    <script src="assets/demo/chart-area-demo.js"></script>
    <script src="assets/demo/chart-bar-demo.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/umd/simple-datatables.min.js" crossorigin="anonymous"></script>
    <script src="js/datatables-simple-demo.js"></script>
    </body>
</html>
