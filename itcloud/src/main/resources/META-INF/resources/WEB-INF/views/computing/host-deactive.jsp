<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script type="text/javascript">

    $(document).ready(function(){

        $("#ok").click(function(){
            $("#deactive").submit();
            setTimeout(function() {
                window.close();
            }, 100);
        });


        $("#cancel").click(function(){
            window.close();
        });
    });

    </script>
</head>
<body>
    <div style="magin: auto">
        <form id="deactive" autocomplete="off" method="post" action="host/deactive2">
            <input type="hidden" id="id" name="id" value="${id}" />
            <h3> ${name} 를 유지관리 모드로 설정하시겠습니까??</h3>

            <input type="button" id="ok" value="OK">
            <input type="reset" id="cancel" value="취소">
        </form>
    </div>

</body>
</html>
