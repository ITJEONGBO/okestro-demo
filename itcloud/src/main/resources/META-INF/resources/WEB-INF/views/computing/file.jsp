<!DOCTYPE html>
<html lang="en">
<head></head>
<body>
    <form method="post" action="/uploadFile" enctype="multipart/form-data">
        <input type="file" name="file">
    </form>

    <button onclick="sendFile()">button</button>

    <script>
        const sendFile = () => {
            const formElement = document.querySelector("form");
            const request = new XMLHttpRequest();
            request.open("POST", "/uploadFile");
            request.send(new FormData(formElement));
        }
    </script>
</body>
</html>
