<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <script type="text/javascript">
        function openPopUp() {
            alert("test");
        }

    </script>
</head>
<body>

    <div style="magin: auto">
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
