const port = 8081;
const apiUrl = `${location.protocol}//${location.hostname}`;

const queueName = getParameterByName('queue_name');
console.log(queueName);
getMessagesHistory(`${apiUrl}/queue/history/${queueName}`,
    getMessagesHistoryHandler);

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

function getMessagesHistory(url, handler) {
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

function getMessagesHistoryHandler(json) {
    console.log(json);

    var table = document.getElementById("msgHistoryTable");
    json.forEach(msg => {

        const date = getDateFromString(msg.creation);
        console.log(date);
        var row = document.createElement("tr");
        var timestampField = document.createElement("td");
        timestampField.innerHTML = `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()} ${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`;
        row.appendChild(timestampField);
        var msgField = document.createElement("td");
        msgField.innerHTML = msg.message;
        row.appendChild(msgField);
        var statusField = document.createElement("td");
        statusField.innerHTML = msg.direction;
        var color = "green";
        if (msg.direction == "OUT") {
            color = "red";
        }
        statusField.style.color = color;
        row.appendChild(statusField);

        table.appendChild(row);
    });
}

function getDateFromString(dateStr) {
    const parts = dateStr.split('-');
    const result = `${parts[1]}/${parts[0]}/${parts[2]}`;
    const offset = new Date().getTimezoneOffset();
    return new Date(Date.parse(result) - offset * 60 * 1000);
}
