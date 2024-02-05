<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script type="text/javascript">


        $(document).ready(function(){

            $("#cpuArc").change(function(){
                var arc = $("#cpuArc option:selected").val();

                $("#cpuType option").remove();

                if( arc === "X86_64"){
                    $("#cpuType").append("<option value='Intel Nehalem Family'>Intel Nehalem Family</option>");
                    $("#cpuType").append("<option value='Secure Intel Nehalem Family'>Secure Intel Nehalem Family</option>");
                    $("#cpuType").append("<option value='Intel Westmere Family'>Intel Westmere Family</option>");
                    $("#cpuType").append("<option value='Secure Intel Westmere Family'>Secure Intel Westmere Family</option>");
                    $("#cpuType").append("<option value='Intel SandyBridge Family'>Intel SandyBridge Family</option>");
                    $("#cpuType").append("<option value='Secure Intel SandyBridge Family'>Secure Intel SandyBridge Family</option>");
                    $("#cpuType").append("<option value='Intel IvyBridge Family'>Intel IvyBridge Family</option>");
                    $("#cpuType").append("<option value='Secure Intel IvyBridge Family'>Secure Intel IvyBridge Family</option>");
                    $("#cpuType").append("<option value='Intel Haswell Family'>Intel Haswell Family</option>");
                    $("#cpuType").append("<option value='Secure Intel Haswell Family'>Secure Intel Haswell Family</option>");
                    $("#cpuType").append("<option value='Intel Broadwell Family'>Intel Broadwell Family</option>");
                    $("#cpuType").append("<option value='Secure Intel Broadwell Family'>Secure Intel Broadwell Family</option>");
                    $("#cpuType").append("<option value='Intel Skylake Client Family'>Intel Skylake Client Family</option>");
                    $("#cpuType").append("<option value='Secure Intel Skylake Client Family'>Secure Intel Skylake Client Family</option>");
                    $("#cpuType").append("<option value='Intel Skylake Server Family'>Intel Skylake Server Family</option>");
                    $("#cpuType").append("<option value='Secure Intel Skylake Server Family'>Secure Intel Skylake Server Family</option>");
                    $("#cpuType").append("<option value='Intel Cascadelake Server Family'>Intel Cascadelake Server Family</option>");
                    $("#cpuType").append("<option value='Secure Intel Cascadelake Server Family'>Secure Intel Cascadelake Server Family</option>");
                    $("#cpuType").append("<option value='Intel Icelake Server Family'>Intel Icelake Server Family</option>");
                    $("#cpuType").append("<option value='Secure Intel Icelake Server Family'>Secure Intel Icelake Server Family</option>");
                    $("#cpuType").append("<option value='AMD Opteron G4'>AMD Opteron G4</option>");
                    $("#cpuType").append("<option value='AMD Opteron G5'>AMD Opteron G5</option>");
                    $("#cpuType").append("<option value='AMD EPYC'>AMD EPYC</option>");
                    $("#cpuType").append("<option value='Secure AMD EPYC'>Secure AMD EPYC</option>");
                }
                if( arc === "PPC64"){
                    $("#cpuType").append("<option value='IBM POWER8'>IBM POWER8</option>");
                    $("#cpuType").append("<option value='IBM POWER9'>IBM POWER9</option>");
                }
                if( arc === "S390X"){
                    $("#cpuType").append( "<option value='IBM z114, z196'>IBM z114, z196</option>");
                    $("#cpuType").append("<option value='IBM zBC12, zEC12'>IBM zBC12, zEC12</option>");
                    $("#cpuType").append("<option value='IBM z13s, z13'>IBM z13s, z13</option>");
                    $("#cpuType").append("<option value='IBM z14'>IBM z14</option>");
                }
                if( arc === "UNDEFINED"){
                    $("#cpuType").append("<option>자동 감지</option>");
                    $("#cpuType").append("<option value='Intel Nehalem Family'>Intel Nehalem Family</option>");
                    $("#cpuType").append("<option value='Secure Intel Nehalem Family'>Secure Intel Nehalem Family</option>");
                    $("#cpuType").append("<option value='Intel Westmere Family'>Intel Westmere Family</option>");
                    $("#cpuType").append("<option value='Secure Intel Westmere Family'>Secure Intel Westmere Family</option>");
                    $("#cpuType").append("<option value='Intel SandyBridge Family'>Intel SandyBridge Family</option>");
                    $("#cpuType").append("<option value='Secure Intel SandyBridge Family'>Secure Intel SandyBridge Family</option>");
                    $("#cpuType").append("<option value='Intel IvyBridge Family'>Intel IvyBridge Family</option>");
                    $("#cpuType").append("<option value='Secure Intel IvyBridge Family'>Secure Intel IvyBridge Family</option>");
                    $("#cpuType").append("<option value='Intel Haswell Family'>Intel Haswell Family</option>");
                    $("#cpuType").append("<option value='Secure Intel Haswell Family'>Secure Intel Haswell Family</option>");
                    $("#cpuType").append("<option value='Intel Broadwell Family'>Intel Broadwell Family</option>");
                    $("#cpuType").append("<option value='Secure Intel Broadwell Family'>Secure Intel Broadwell Family</option>");
                    $("#cpuType").append("<option value='Intel Skylake Client Family'>Intel Skylake Client Family</option>");
                    $("#cpuType").append("<option value='Secure Intel Skylake Client Family'>Secure Intel Skylake Client Family</option>");
                    $("#cpuType").append("<option value='Intel Skylake Server Family'>Intel Skylake Server Family</option>");
                    $("#cpuType").append("<option value='Secure Intel Skylake Server Family'>Secure Intel Skylake Server Family</option>");
                    $("#cpuType").append("<option value='Intel Cascadelake Server Family'>Intel Cascadelake Server Family</option>");
                    $("#cpuType").append("<option value='Secure Intel Cascadelake Server Family'>Secure Intel Cascadelake Server Family</option>");
                    $("#cpuType").append("<option value='Intel Icelake Server Family'>Intel Icelake Server Family</option>");
                    $("#cpuType").append("<option value='Secure Intel Icelake Server Family'>Secure Intel Icelake Server Family</option>");
                    $("#cpuType").append("<option value='AMD Opteron G4'>AMD Opteron G4</option>");
                    $("#cpuType").append("<option value='AMD Opteron G5'>AMD Opteron G5</option>");
                    $("#cpuType").append("<option value='AMD EPYC'>AMD EPYC</option>");
                    $("#cpuType").append("<option value='Secure AMD EPYC'>Secure AMD EPYC</option>");
                    $("#cpuType").append("<option value='IBM POWER8'>IBM POWER8</option>");
                    $("#cpuType").append("<option value='IBM POWER9'>IBM POWER9</option>");
                    $("#cpuType").append( "<option value='IBM z114, z196'>IBM z114, z196</option>");
                    $("#cpuType").append("<option value='IBM zBC12, zEC12'>IBM zBC12, zEC12</option>");
                    $("#cpuType").append("<option value='IBM z13s, z13'>IBM z13s, z13</option>");
                    $("#cpuType").append("<option value='IBM z14'>IBM z14</option>");
                }

            });


        	$("#ok").click(function(){

        		if($("#name").val() == ''){
        			alert("이름를 입력해주세요");
        			$("#name").focus();
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
    </style>

</head>
<body>
    <div style="padding: 1rem;">
        <div>
            <h2>새 클러스터</h2>
            <form id="add" autocomplete="off" method="get" action="cluster-add2">
                <h3>일반</h3>
                <br>

                <table>
                    <tr>
                        <td>데이터 센터</td>
                        <td>&emsp;
                            <select id="datacenterId" name="datacenterId">
                                <c:forEach var="dc" items="${dc}" varStatus="status">
                                    <option value="${dc.id}">${dc.name}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>이름</td>
                        <td>&emsp; <input type="text" id="name" name="name" size="20"></td>
                    </tr>
                    <tr>
                        <td>설명</td>
                        <td>&emsp; <input type="text" id="description" name="description"></td>
                    </tr>
                    <tr>
                        <td>코멘트</td>
                        <td>&emsp; <input type="text" id="comment" name="comment"></td>
                    </tr>

                    <tr>
                        <td>관리 네트워크</td>
                        <td>&emsp;
                            <select id="networkId" name="networkId">
                                <c:forEach var="dc" items="${dc}" varStatus="status">
                                    <c:forEach var="net" items="${dc.networkList}" varStatus="status">
                                        <c:if test="${empty net}">
                                            <option></option>
                                        </c:if>
                                        <c:if test="${dc.id eq net.datacenterId}">
                                            <option value="${net.id}">${net.name}, (${dc.name})</option>
                                        </c:if>
                                    </c:forEach>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>CPU 아키텍처</td>
                        <td>&emsp;
                            <select id="cpuArc" name="cpuArc" >
                                <option value="UNDEFINED">정의되지 않음</option>
                                <option value="X86_64" selected>x86_64</option>
                                <option value="PPC64">ppc64</option>
                                <option value="S390X">s390x</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>CPU 유형</td>
                        <td>
                            &emsp;<select id="cpuType" name="cpuType" style="width:200px;">

                                <!-- x86_64 -->
                                <!--
                                <option value="Intel Nehalem Family">Intel Nehalem Family</option>
                                <option value="Secure Intel Nehalem Family">Secure Intel Nehalem Family</option>
                                <option value="Intel Westmere Family">Intel Westmere Family</option>
                                <option value="Secure Intel Westmere Family">Secure Intel Westmere Family</option>
                                <option value="Intel SandyBridge Family">Intel SandyBridge Family</option>
                                <option value="Secure Intel SandyBridge Family">Secure Intel SandyBridge Family</option>
                                <option value="Intel IvyBridge Family">Intel IvyBridge Family</option>
                                <option value="Secure Intel IvyBridge Family">Secure Intel IvyBridge Family</option>
                                <option value="Intel Haswell Family">Intel Haswell Family</option>
                                <option value="Secure Intel Haswell Family">Secure Intel Haswell Family</option>
                                <option value="Intel Broadwell Family">Intel Broadwell Family</option>
                                <option value="Secure Intel Broadwell Family">Secure Intel Broadwell Family</option>
                                <option value="Intel Skylake Client Family">Intel Skylake Client Family</option>
                                <option value="Secure Intel Skylake Client Family">Secure Intel Skylake Client Family</option>
                                <option value="Intel Skylake Server Family">Intel Skylake Server Family</option>
                                <option value="Secure Intel Skylake Server Family">Secure Intel Skylake Server Family</option>
                                <option value="Intel Cascadelake Server Family">Intel Cascadelake Server Family</option>
                                <option value="Secure Intel Cascadelake Server Family">Secure Intel Cascadelake Server Family</option>
                                <option value="Intel Icelake Server Family">Intel Icelake Server Family</option>
                                <option value="Secure Intel Icelake Server Family">Secure Intel Icelake Server Family</option>
                                <option value="AMD Opteron G4">AMD Opteron G4</option>
                                <option value="AMD Opteron G5">AMD Opteron G5</option>
                                <option value="AMD EPYC">AMD EPYC</option>
                                <option value="Secure AMD EPYC">Secure AMD EPYC</option>
                                -->

                                <!-- ppc64 -->
                                <!--
                                <option value="IBM POWER8">IBM POWER8</option>
                                <option value="IBM POWER9">IBM POWER9</option>
                                -->

                                <!-- s390x -->
                                <!--
                                <option value="IBM z114, z196">IBM z114, z196</option>
                                <option value="IBM zBC12, zEC12">IBM zBC12, zEC12</option>
                                <option value="IBM z13s, z13">IBM z13s, z13</option>
                                <option value="IBM z14">IBM z14</option>
                                -->
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>칩셋/펌웨어 유형</td>
                        <td>&emsp;
                            <select id="biosType" name="biosType" disabled>
                                <option value="CLUSTER_DEFAULT">자동 감지</option>
                                <!--  cpu 유형 선택에 따라 달라짐. 기본은 자동 감지   -->
                                <option value="I440FX_SEA_BIOS">I440FX_SEA_BIOS</option>
                                <option value="Q35_OVMF">Q35_OVMF</option>
                                <option value="Q35_SEA_BIOS">Q35_SEA_BIOS</option>
                                <option value="Q35_SECURE_BOOT">Q35_SECURE_BOOT</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>FIPS 모드</td>
                        <td>&emsp;
                            <select id="fipsMode" name="fipsMode">
                                <option value="UNDEFINED">자동 감지</option>
                                <option value="DISABLED">비활성화됨</option>
                                <option value="ENABLED">활성화됨</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>호환 버전</td>
                        <td>&emsp;
                            <select id="version" name="version" >
                                <option value="4.7">4.7</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>스위치 유형</td>
                        <td>&emsp;
                            <select id="switchType" name="switchType" >
                                <option value="LEGACY">Linux Bridge</option>
                                <option value="OVS">OVS(기술 프리뷰)</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>방화벽 유형</td>
                        <td>&emsp;
                            <select id="firewallType" name="firewallType" >
                                <option value="FIREWALLD">firewalled</option>
                                <option value="IPTABLES">iptables</option>
                            </select>
                        </td>
                    </tr>

                <!--
                    <tr>
                        <td>기본 네트워크 공급자</td>
                        <td>&emsp;
                            <select id="networkProvider" name="networkProvider" >
                                <option value="">ovirt-provider-ovn</option>
                            </select>
                        </td>
                    </tr>
                -->

                    <tr>
                        <td>로그의 최대 메모리 한계</td>
                        <td> &emsp;<input type="text" id="logMaxMemory" name="logMaxMemory" size="10" value="95"></td>
                        <td>
                            <select id="logMemory" name="logMemoryUnit" >
                                <option value="percent">%</option>
                            <!-- <option value="MB">MB</option> -->
                            </select>
                        </td>
                    </tr>

                   <!-- <tr>
                        <td colspan="2"><input type="checkbox" id="virt" name="virt" checked/>&emsp; Virt 서비스 활성화</td>
                    </tr>

                    <tr>
                        <td colspan="2"><input type="checkbox" id="gluster" name="gluster"/>&emsp; Gluster 서비스 활성화</td>
                    </tr> -->

                    <tr>
                        <td colspan="2"><p>추가 난수 생성기 소스 </p></td>
                    </tr>

                    <tr>
                        <td colspan="2"><input type="checkbox" id="hwrng" name="hwrng" value=""/>&emsp; /dev/hwrng 소스</td>
                    </tr>

                </table>

                <hr><br>

            <!--
                <h3>마이그레이션 정책</h3>
                <br>

                마이그레이션 정책 &emsp;
                    <select id="version" name="version" >
                        <option value="">최소 다운타임</option>
                        <option value="">사후 복사 마이그레이션</option>
                        <option value="">필요에 따라 작업을 일시중단</option>
                        <option value="">Very large VMs</option>
                    </select><br>
                    <br>

                <p>최소 다운타임</p>
                <p>
                    일반적인 상황에서 가상머신을 마이그레이션 할 수있는 정책입니다. 가상머신에 심각한 다운타임이 발생하면 안됩니다.
                    가상머신 마이그레이션이 오랫동안 수렴되지 않으면 마이그레이션이 중단됩니다. 게스트 에이전트 후크 매커니즘을 사용할 수 있습니다.
                </p>

                <p>대역폭</p>
                마이그레이션 대역폭 제한(Mbps) &emsp;
                    <select id="bandwidth" name="bandwidth">
                        <option value="auto">자동</option>
                        <option value="hypervisor_default">하이퍼바이저 기본</option>
                        <option value="custom">사용자 정의</option>
                    </select><br><br>

                <p>복구정책</p>
                    <input type='radio' id="recoveryPolicy" name='recoveryPolicy' value='migrate' checked/> 가상머신을 마이그레이션 함 <br>
                    <input type='radio' id="recoveryPolicy" name='recoveryPolicy' value='migrate_highly_available' /> 고가용성 가상머신만 마이그레이션 <br>
                    <input type='radio' id="recoveryPolicy" name='recoveryPolicy' value='do_not_migrate' /> 가상머신은 마이그레이션 하지 않음 <br>
                <br>

                <p>추가 속성</p>
                마이그레이션 암호화 사용 &emsp;
                    <select id="encrypted" name="encrypted">
                        <option value="inherit">시스템 기본값(암호화하지 마십시오)</option>
                        <option value="true">암호화</option>
                        <option value="false">암호화하지 마십시오</option>
                    </select><br><br>

                Parallel Migrations &emsp;
                    <select id="Parallel" name="Parallel">
                        <option value="auto">Auto</option>
                        <option value="auto_parallel">Auto Parallel</option>
                        <option value="disabled">Disabled</option>
                        <option value="custom">Custom</option>
                    </select><br><br>

                Number of VM Migration Connections <br>
                -->

                <br><br><br>

                <input type="submit" id="ok" value="OK">
                <input type="reset" id="cancel" value="취소">
            </form>
        </div>

    </div>

</body>
</html>
