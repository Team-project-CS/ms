const port = 8082;
const apiUrl = `${location.protocol}//${location.hostname}`;

const endpointId = getParameterByName("endpointId");
console.log(endpointId);
getRequestsHistory(`${apiUrl}/log/${endpointId}`, 
    getRequestsHistoryHandler);

// source: https://stackoverflow.com/questions/53757395/how-to-pass-variable-on-href
function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

function getRequestsHistory(url, handler) {
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

function getRequestsHistoryHandler(json) {
    console.log(json);

    var table = document.getElementById("restApiHistoryTable");
    const offset = new Date().getTimezoneOffset();
    json.forEach(request => {
        const date = new Date(Date.parse(request.creationDate) - offset * 60 * 1000);
        var row = document.createElement("tr");
        var timestampField = document.createElement("td");
        timestampField.innerHTML = `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()} ${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`;
        row.appendChild(timestampField);
        var requestBodyField = document.createElement("td");
        requestBodyField.innerHTML = buildBodyDescription(request.input);
        row.appendChild(requestBodyField);
        var responseBodyField = document.createElement("td");
        responseBodyField.innerHTML = buildBodyDescription(request.output);
        row.appendChild(responseBodyField);

        table.appendChild(row);
    });
}

function buildBodyDescription(body) {
    var fields = [];
    for (const [key, value] of Object.entries(body)) {
        fields.push(`${key}: ${value}`);
    }
    return fields.join('<br>'); 
}
