<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Template</h1>
                    컴퓨팅 > <a href="/computing/templates" style="text-decoration-line: none">템플릿</a> > ${template.name} <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/computing/template?id=${id}">일반</a> |
                                <a href="/computing/template-vm?id=${id}" style="text-decoration-line: none">가상머신</a> |
                                <a href="/computing/template-nic?id=${id}" style="text-decoration-line: none">네트워크 인터페이스</a> |
                                <a href="/computing/template-disk?id=${id}" style="text-decoration-line: none">디스크</a> |
                                <a href="/computing/template-storage?id=${id}" style="text-decoration-line: none">스토리지</a> |
                                <a href="/computing/template-permission?id=${id}" style="text-decoration-line: none">권한</a> |
                                <a href="/computing/template-event?id=${id}" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                    <div>
                        이름: ${template.name}<br>
                        설명: ${template.description}<br>
                        호스트 클러스터: <br><br>

                        운영시스템: ${template.osType}<br>
                        칩셋/펌웨어 유형: ${template.chipsetFirmwareType}<br>
                        최적화 옵션: ${template.optimizeOption}<br><br>

                        설정된 메모리: ${template.memory /(1024*1024)}MB<br>
                        CPU 코어수: ${template.cpuCnt}(${template.cpuCoreCnt}:${template.cpuSocketCnt}:${template.cpuThreadCnt})<br>
                        모니터 수: ${template.monitor}<br><br>

                        고가용성: <br>
                        우선순위: ${template.priority}<br>
                        USB: ${template.usb == "true" ? "활성화됨" : "비활성화됨"}<br><br>

                        소스: <br>
                        상태 비저장: <br>
                        템플릿 ID: ${template.id}<br>
                    </div>

                    <br><br><br><br><br><br>
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
