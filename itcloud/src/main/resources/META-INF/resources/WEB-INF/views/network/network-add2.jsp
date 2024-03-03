<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script type="text/javascript">
        function openPopUp() {
            window.close();
        }

    </script>
</head>
<body>

    <div style="magin: auto">
    <h1>${result}</h1>

        <label>Head: ${message}</label><br>
        <label>Body: ${body}</label><br>

        <form>

            <button type="button" class="btn btn-secondary" onclick="openPopUp()">나중에 설정</button>
        </form>
    </div>

</body>
</html>
