const port = 8081;
const apiUrl = `${location.protocol}//${location.hostname}`;

function buildQueueCreationRequest() {
    var queueName = document.getElementById("queueName").value;

    return [{queue_name: queueName}];
}

function sendQueue() {
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify(buildQueueCreationRequest());

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow',
        //mode: 'no-cors'
    };

    fetch(`${apiUrl}/queue/list`, requestOptions)
        .then(response => response.json())
        .then(handleQueueCreation)
        .catch(error => console.log('error', error));
}

function handleQueueCreation(json) {
    alert(`Queue ${json[0].queue_name} was successfully created.`);
    window.location.href = "usersmsgqueues";
    window.location.replace("usersmsgqueues");
}