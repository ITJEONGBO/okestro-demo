<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<script>
    var msg = "<c:out value='${result}' />";
    alert(msg);
    window.close();
</script>
