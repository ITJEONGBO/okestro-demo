<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script type="text/javascript">
        function openPopUp() {
            alert("test");
        }

    </script>
</head>
<body>

    <div style="magin: auto">
    <h1>${result}</h1>
        <form>
            클러스터 설정:
            <select name="cluster" id="cluster">
                <option value="shared">공유됨</option>
                <option value="local">로컬</option>
            </select><br>

            <button type="button" class="btn btn-secondary" onclick="openPopUp()">나중에 설정</button>
        </form>
    </div>

</body>
</html>
