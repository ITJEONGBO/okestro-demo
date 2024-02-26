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
                var biosTypeSelect = document.getElementById("biosType");

                $("#cpuType option").remove();

                if( arc === "X86_64"){
                    biosTypeSelect.disabled = false;
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
                    biosTypeSelect.disabled = true;
                    $("#cpuType").append("<option value='IBM POWER8'>IBM POWER8</option>");
                    $("#cpuType").append("<option value='IBM POWER9'>IBM POWER9</option>");
                }
                if( arc === "S390X"){
                    biosTypeSelect.disabled = true;
                    $("#cpuType").append( "<option value='IBM z114, z196'>IBM z114, z196</option>");
                    $("#cpuType").append("<option value='IBM zBC12, zEC12'>IBM zBC12, zEC12</option>");
                    $("#cpuType").append("<option value='IBM z13s, z13'>IBM z13s, z13</option>");
                    $("#cpuType").append("<option value='IBM z14'>IBM z14</option>");
                }
                if( arc === "UNDEFINED"){
                    biosTypeSelect.disabled = true;
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



            $("#policy").change(function(){
                var pol = $("#policy option:selected").val();
                var p = $("#policyP").text();

                if( pol === "downTime"){
                    $("#policyP").text("일반적인 상황에서 가상머신을 마이그레이션 할 수있는 정책입니다. 가상머신에 심각한 다운타임이 발생하면 안됩니다. 가상머신 마이그레이션이 오랫동안 수렴되지 않으면 마이그레이션이 중단됩니다. 게스트 에이전트 후크 매커니즘을 사용할 수 있습니다.");
                }
                if( pol === "copyMigration"){
                    $("#policyP").text("가상 머신에 심각한 다운타임이 발생하면 안 됩니다. 가상 머신 마이그레이션이 오랫동안 수렴되지 않으면 마이그레이션이 사후 복사 모드로 전환됩니다. 게스트 에이전트 후크 메커니즘을 사용할 수 있습니다.");
                }
                if( pol === "pause"){
                    $("#policyP").text("과도한 워크로드를 실행하는 가상 머신을 포함하여 대부분의 상황에서 가상 머신을 마이그레이션할 수 있는 정책입니다. 반면에 가상 머신의 다운타임은 더 심각할 수 있습니다. 과도한 워크로드에도 마이그레이션이 중단될 수 있습니다. 게스트 에이전트 후크 메커니즘을 사용할 수 있습니다.");
                }
                if(pol === "verLarge"){
                    $("#policyP").text("The VM cannot be migrated using any of the other policies, a possibly risky migration mechanism is accepted and the migration need not be encrypted. The VM may experience a significant downtime. The migration may still be aborted if it cannot converge. The guest agent hook mechanism is enabled.");
                }

            });


        	$("#ok").click(function(){

        		if($("#name").val() == ''){
        			alert("이름를 입력해주세요");
        			$("#name").focus();
        			return;
        		}

        		var v = $("input:checkbox[id='virtService']").is(":checked") ;
                var g = $("input:checkbox[id='glusterService']").is(":checked") ;

                $("input:checkbox[id='virtService']").val(v);
                $("input:checkbox[id='glusterService']").val(g);

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
            <form id="add" autocomplete="off" method="post" action="cluster-add2">
                <hr><br>

                <h3>일반</h3>
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
                                <option value="UNDEFINED" selected>정의되지 않음</option>
                                <option value="X86_64">x86_64</option>
                                <option value="PPC64">ppc64</option>
                                <option value="S390X">s390x</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>CPU 유형</td>
                        <td>&emsp;
                            <select id="cpuType" name="cpuType" style="width:200px;">
                                <!-- cpu type: x86_64, ppc64, s390x -->
                                <option value="">자동 감지</option>
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

                                <option value="IBM POWER8">IBM POWER8</option>
                                <option value="IBM POWER9">IBM POWER9</option>

                                <option value="IBM z114, z196">IBM z114, z196</option>
                                <option value="IBM zBC12, zEC12">IBM zBC12, zEC12</option>
                                <option value="IBM z13s, z13">IBM z13s, z13</option>
                                <option value="IBM z14">IBM z14</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>칩셋/펌웨어 유형</td>
                        <td>&emsp;
                            <select id="biosType" name="biosType" disabled>
                                <option value="CLUSTER_DEFAULT">자동 감지</option>
                                <option value="I440FX_SEA_BIOS">BIOS의 1440FX 칩셋</option>
                                <option value="Q35_OVMF">UEFI의 Q35 칩셋</option>
                                <option value="Q35_SEA_BIOS">BIOS의 Q35 칩셋</option>
                                <option value="Q35_SECURE_BOOT">UEFI SecureBoot의 Q35 칩셋</option>
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


                    <tr>
                        <td>기본 네트워크 공급자</td>
                        <td>&emsp;
                            <select id="networkProvider" name="networkProvider" >
                                <option value="true">ovirt-provider-ovn</option>
                                <option value="false">기본 공급자가 없습니다.</option>
                            </select>
                        </td>
                    </tr>


                    <tr>
                        <td>로그의 최대 메모리 한계</td>
                        <td> &emsp;<input type="text" id="logMaxMemory" name="logMaxMemory" size="10" value="90"/></td>
                        <td>
                            <select id="logMaxType" name="logMemoryType" >
                                <option value="PERCENTAGE">%</option>
                                <option value="ABSOLUTE_VALUE_IN_MB">MB</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2"><input type="checkbox" id="virtService" name="virtService" checked/>&emsp; Virt 서비스 활성화</td>
                    </tr>

                    <tr>
                        <td colspan="2"><input type="checkbox" id="glusterService" name="glusterService" />&emsp; Gluster 서비스 활성화</td>
                    </tr>

                </table>
                <hr>

                <div>
                <h3>마이그레이션 정책</h3>

                마이그레이션 정책 &emsp;
                <select id="policy" name="policy" >
                    <option value="downTime" selected>최소 다운타임</option>
                    <option value="copyMigration">사후 복사 마이그레이션</option>
                    <option value="pause">필요에 따라 작업을 일시중단</option>
                    <option value="verLarge">Very large VMs</option>
                </select><br>

                <p id="policyP"></p>

                <p><b>대역폭</b></p>
                마이그레이션 대역폭 제한(Mbps) &emsp;
                <select id="bandwidth" name="bandwidth">
                    <option value="AUTO">자동</option>
                    <option value="HYPERVISOR_DEFAULT">하이퍼바이저 기본</option>
                    <option value="CUSTOM">사용자 정의</option>
                </select><br><br>

                <p><b>복구정책</b></p>
                    <input type='radio' id="recoveryPolicy" name='recoveryPolicy' value='MIGRATE' checked/> 가상머신을 마이그레이션 함 <br>
                    <input type='radio' id="recoveryPolicy" name='recoveryPolicy' value='MIGRATE_HIGHLY_AVAILABLE' /> 고가용성 가상머신만 마이그레이션 <br>
                    <input type='radio' id="recoveryPolicy" name='recoveryPolicy' value='DO_NOT_MIGRATE' /> 가상머신은 마이그레이션 하지 않음 <br>
                <br>

                <p>추가 속성</p>
                마이그레이션 암호화 사용 &emsp;
                    <select id="encrypted" name="encrypted">
                        <option value="INHERIT">시스템 기본값(암호화하지 마십시오)</option>
                        <option value="TRUE">암호화</option>
                        <option value="FALSE">암호화하지 마십시오</option>
                    </select><br><br>

                <!--
                Parallel Migrations &emsp;
                    <select id="parallel" name="parallel">
                        <option value="auto">Auto</option>
                        <option value="auto_parallel">Auto Parallel</option>
                        <option value="disabled">Disabled</option>
                        <option value="custom">Custom</option>
                    </select><br><br>
                -->

                Number of VM Migration Connections <br>
                </div><br>

                <input type="button" id="ok" value="OK">
                <input type="reset" id="cancel" value="취소">
            </form>
        </div>

    </div>

</body>
</html>
