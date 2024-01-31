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

        		$("#add").submit();
        	});


        	$("#cancel").click(function(){
        	    window.close();
        	});


        });

    </script>
</head>
<body>


    <div style="padding: 2rem;">
        <hr>
        <div>
            <h2>데이터센터 생성</h2>
            <form id="add" autocomplete="off" method="get" action="datacenter-add2">
                이름 &emsp; <input type="text" id="name" name="name" size="20" style="width: 128px;" autofocus>   <br><br>

                설명 &emsp; <input type="text" id="description" name="description">   <br><br>

                스토리지 유형 &emsp;
                <select id="storageType" name="storageType">
                    <option value="false">공유됨</option>
                    <option value="true">로컬</option>
                </select><br><br>

                호환 버전 &emsp;
                <select id="version" name="version" >
                    <option value="4.7">4.7</option>
                    <option value="4.6">4.6</option>
                    <option value="4.5">4.5</option>
                    <option value="4.4">4.4</option>
                    <option value="4.3">4.3</option>
                    <option value="4.2">4.2</option>
                </select><br><br>

                쿼터 모드 &emsp;
                <select id="quotaMode" name="quotaMode">
                    <option value="DISABLED">비활성화됨</option>
                    <option value="AUDIT">감사</option>
                    <option value="ENABLED">강제 적용</option>
                </select><br><br>

                코멘트 &emsp; <input type="text" id="comment" name="comment">
                <br><br>
                <hr>

                <input type="submit" id="ok" value="OK">
                <input type="reset" id="cancel" value="취소">
            </form>
        </div>

    </div>

</body>
</html>
