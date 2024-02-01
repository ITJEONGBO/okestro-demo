<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4ddQ3GYNS7NTKfAdVQSZe" crossorigin="anonymous"></script>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
    <script src="js/scripts.js"></script>

    <script type="text/javascript">


        $(document).ready(function(){

        	$("#ok").click(function(){
        		if($("#name").val() == ''){
        			alert("이름를 입력해주세요");
        			$("#name").focus();
        			return;
        		}
        		$("#edit").submit();
        	});

        	$("#cancel").click(function(){
        	    window.close();
        	});

        });

    </script>
</head>
<body>

    <div style="padding: 2rem;">
        <h2>클러스터 수정</h2>
        <hr>

        <div>
            <form id="edit" autocomplete="off" method="get" action="cluster-edit2">
                <input type="hidden" id="id" name="id" value="${c.id}" />
                <h3>일반</h3>
                <br>

                데이터 센터 &emsp;
                <select id="datacenterId" name="datacenterId" >
                    <option value="${c.datacenterId}">${c.datacenterName}</option>
                </select><br>
                <br>

                이름 &emsp; <input type="text" id="name" name="name" size="20" value="${c.name}">   <br>
                설명 &emsp; <input type="text" id="description" name="description" value="${c.description}">   <br>
                코멘트 &emsp; <input type="text" id="comment" name="comment" value="${c.comment}"> <br>
                <br>

                관리 네트워크 &emsp;
                <select id="networkId" name="networkId">
                    <option value="${c.networkId}">${c.networkName}</option>
                </select><br>


                CPU 아키텍처 &emsp;
                <select id="cpuArc" name="cpuArc" >
                    <option value="UNDEFINED">정의되지 않음</option>
                    <option value="X86_64">x86_64</option>
                    <option value="PPC64">ppc64</option>
                    <option value="S390X">s390x</option>
                </select><br>

                CPU 유형 &emsp;
                <select id="cpuType" name="cpuType" >
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
                </select><br>

                칩셋/펌웨어 유형 &emsp;
                <select id="biosType" name="biosType" disabled>
                    <option value="CLUSTER_DEFAULT">자동 감지</option>
                    <!--
                    <option value="I440FX_SEA_BIOS">I440FX_SEA_BIOS</option>
                    <option value="Q35_OVMF">Q35_OVMF</option>
                    <option value="Q35_SEA_BIOS">Q35_SEA_BIOS</option>
                    <option value="Q35_SECURE_BOOT">Q35_SECURE_BOOT</option>
                    -->
                </select><br><br>


                FIPS 모드 &emsp;
                <select id="fipsMode" name="fipsMode">
                    <option value="UNDEFINED">자동 감지</option>
                    <option value="DISABLED">비활성화됨</option>
                    <option value="ENABLED">활성화됨</option>
                </select><br>

                호환 버전 &emsp;
                <select id="version" name="version" >
                    <option value="4.7">4.7</option>
                </select><br><br>

                스위치 유형 &emsp;
                <select id="switchType" name="switchType" >
                    <option value="LEGACY">Linux Bridge</option>
                    <option value="OVS">OVS(기술 프리뷰)</option>
                </select><br><br>

                방화벽 유형 &emsp;
                <select id="firewallType" name="firewallType" >
                    <option value="FIREWALLD">firewalled</option>
                    <option value="IPTABLES">iptables</option>
                </select><br><br>

            <!--
                기본 네트워크 공급자 &emsp;
                <select id="externalProvider" name="version" >
                    <option value="FIREWALLD">firewalled</option>
                    <option value="IPTABLES">iptables</option>
                </select><br><br>
            -->

                로그의 최대 메모리 한계 &emsp;
                <input type="text" id="logMaxMemory" name="logMaxMemory" size="10">
                <select id="logMemory" name="logMemoryUnit" >
                    <option value="percent">%</option>
                    <option value="MB">MB</option>
                </select><br><br>

                <input type="checkbox" id="virtService" name="virtService" checked/> Virt 서비스 활성화 <br><br>
                <input type="checkbox" id="glusterService" name="glusterService" /> Gluster 서비스 활성화 <br><br>

                <p>추가 난수 생성기 소스 </p>
                <input type="checkbox" id="hwrng" name="hwrng" value=""/> /dev/hwrng 소스 <br><br>

                <hr><br>

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
                    <select id="quotaMode" name="quotaMode">
                        <option value="DISABLED">자동</option>
                        <option value="AUDIT">하이퍼바이저 기본</option>
                        <option value="ENABLED">사용자 정의</option>
                    </select><br><br>

                <p>복구정책</p>
                    <input type='radio' name='recover' value='' checked/> 가상머신을 마이그레이션 함 <br>
                    <input type='radio' name='recover' value='' /> 고가용성 가상머신만 마이그레이션 <br>
                    <input type='radio' name='recover' value='' /> 가상머신은 마이그레이션 하지 않음 <br>
                <br>

                <p>추가 속성</p>
                마이그레이션 암호화 사용 &emsp;
                    <select id="quotaMode" name="quotaMode">
                        <option value="DISABLED">시스템 기본값(암호화하지 마십시오)</option>
                        <option value="AUDIT">암호화</option>
                        <option value="ENABLED">암호화하지 마십시오</option>
                    </select><br><br>

                Parallel Migrations &emsp;
                    <select id="quotaMode" name="quotaMode">
                        <option value="DISABLED">Auto</option>
                        <option value="DISABLED">Auto Parallel</option>
                        <option value="DISABLED">Disabled</option>
                        <option value="DISABLED">Custom</option>
                    </select><br><br>

                Number of VM Migration Connections <br>

                <br><br><br>

                <input type="submit" id="ok" value="OK">
                <input type="reset" id="cancel" value="취소">
            </form>
        </div>

    </div>

</body>
</html>
