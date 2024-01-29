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

        		$("#add").submit();
        	});


        	$("#cancel").click(function(){
        	    window.close();
        	});



        });

    </script>
</head>
<body>
    <h2>데이터센터 생성</h2>
    <div style="padding: 2rem;">
        <div>
            <form id="add" autocomplete="off" method="get" action="datacenter-add2">

                이름: <input type="text" id="name" name="name" size="20">   <br>
                설명: <input type="text" id="description" name="description">   <br>

                스토리지 유형:
                <select id="storageType" name="storageType">
                    <option value="false">공유됨</option>
                    <option value="true">로컬</option>
                </select><br>

                호환 버전:
                <select id="version" name="version" >
                    <option value="4.7">4.7</option>
                    <option value="4.6">4.6</option>
                    <option value="4.5">4.5</option>
                    <option value="4.4">4.4</option>
                    <option value="4.3">4.3</option>
                    <option value="4.2">4.2</option>
                </select><br>

                쿼터 모드:
                <select id="quotaMode" name="quotaMode">
                    <option value="DISABLED">비활성화됨</option>
                    <option value="AUDIT">감사</option>
                    <option value="ENABLED">강제 적용</option>
                </select><br>

                코멘트: <input type="text" id="comment" name="comment">
                <br><br><br>

                <input type="submit" id="ok" value="OK">
                <input type="reset" id="cancel" value="취소">
            </form>
        </div>

    </div>

</body>
</html>
