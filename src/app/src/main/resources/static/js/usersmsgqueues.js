const location = window.location;
const apiUrl = `${location.protocol}//${location.hostname}:${location.port}`;

getUsersQueues(`${apiUrl}/queue/list`, createTableFromJSON);

function getUsersQueues(url, handler) {
    var requestOptions = {
        method: 'GET',
        redirect: 'follow',
        //mode: 'no-cors'
    };

    fetch(url, requestOptions)
        .then(response => response.json())
        .then(handler)
        .catch(error => console.log('error', error));
}

function createTableFromJSON(json) {
    console.log(json);

    var table = document.getElementById("usersMsgQueuesTable");
    json.forEach(queue => {
        var row = document.createElement("tr");
        var nameField = document.createElement("td");
        nameField.innerHTML = `<a href="msg_history.html?queue_name=${queue.queue_name}">${queue.queue_name}</a>`;
        row.appendChild(nameField);
        var pushUrlField = document.createElement("td");
        const pushUrl = apiUrl;
        pushUrlField.innerHTML = `<div class="tooltip"><a id="pushUrlField" onmouseout="outFunc(this)" role="button" href="#" onclick="return urlOnClickHandle('${pushUrl}', this)" class="tooltip"><span class="tooltiptext" id="myTooltip">Copy URL to clipboard</span>${pushUrl}</a></div>`;
        row.appendChild(pushUrlField);
        var pullUrlField = document.createElement("td");
        const pullUrl = apiUrl + `?queue=${queue.queue_name}`;
        pullUrlField.innerHTML = `<div class="tooltip"><a id="pullUrlField" onmouseout="outFunc(this)" role="button" href="#" onclick="return urlOnClickHandle('${pullUrl}', this)" class="tooltip"><span class="tooltiptext" id="myTooltip">Copy URL to clipboard</span>${pullUrl}</a></div>`;
        row.appendChild(pullUrlField);
        var pushBodyStructureField = document.createElement("td");
        pushBodyStructureField.innerHTML = `{ "queue_name": "${queue.queue_name}", "message": "***" }`;
        row.appendChild(pushBodyStructureField);

        table.appendChild(row);
    });
}

function urlOnClickHandle(url, idField) {
    navigator.clipboard.writeText(url);
    var tooltip = idField.querySelector("#myTooltip");
    tooltip.innerHTML = "Copied: " + url;
}

function outFunc(idField) {
    var tooltip = idField.querySelector("#myTooltip");
    tooltip.innerHTML = "Copy to clipboard";
}