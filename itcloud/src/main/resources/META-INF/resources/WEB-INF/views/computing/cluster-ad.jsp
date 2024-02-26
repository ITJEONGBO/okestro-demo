<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>네트워크 선택</title>
</head>
<script>
    function updateNetworks() {
        var datacenterSelect = document.getElementById("datacenterId");
        var networkSelect = document.getElementById("networkId");

        var selectedDatacenterId = datacenterSelect.value;

        networkSelect.innerHTML = '';

        if (selectedDatacenterId === "1") {
            var networks = [
                { id: 1, name: "datacenter 1-1" },
                { id: 2, name: "datacenter 1-2" }
            ];
        } else if (selectedDatacenterId === "2") {
            var networks = [
                { id: 3, name: "datacenter 2-1" },
                { id: 4, name: "datacenter 2-2" }
            ];
        }

        networks.forEach(function(network) {
            var option = document.createElement('option');
            option.value = network.id;
            option.textContent = network.name;
            networkSelect.appendChild(option);
        });
    }

    window.onload = updateNetworks;
</script>

<body>
    <table>
        <tr>
            <td>datacenter</td>
            <td>&emsp;
                <select id="datacenterId" name="datacenterId" onchange="updateNetworks()">
                    <option value="1">datacenter 1</option>
                    <option value="2">datacenter 2</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>network</td>
            <td>&emsp;
                <select id="networkId" name="networkId">
                </select>
            </td>
        </tr>
    </table>

</body>
</html>