<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <script type="text/javascript">
        function openPopUp() {
            window.open("datacenter-add2", "mypopup", "width=450, height=250, top=150, left=200");
        }

    </script>
</head>
<body>

    <div style="magin: auto">
        <form>
            이름: <input type="text" id="name" name="name">   <br>
            설명: <input type="text" id="description" name="description">   <br>
            스토리지 유형:
            <select name="storageType" id="storageType">
                <option value="shared">공유됨</option>
                <option value="local">로컬</option>
            </select><br>
            호환 버전:
            <select name="version" id="version">
                <option value="4.2">4.2</option>
                <option value="4.3">4.3</option>
                <option value="4.4">4.4</option>
                <option value="4.5">4.5</option>
                <option value="4.6">4.6</option>
                <option value="4.7">4.7</option>
            </select><br>
            쿼터 모드:
            <select name="mode" id="mode">
                <option value="none">비활성화됨</option>
                <option value="gs">감사</option>
                <option value="enforced">강제 적용</option>
            </select><br>
            코멘트: <input type="text" id="comment" name="comment">
            <br><br><br><br>

            <button type="button" class="btn btn-secondary" onclick="openPopUp()">OK</button>
            <button type="button" class="btn btn-secondary">취소</button>
        </form>
    </div>

</body>
</html>
