const apiUrl = "http://178.21.11.9:8082/api";
var idToTableRow = {};
var table;

window.onload = function () {
    table = document.getElementById("usersApiModelsTable");
    getUsersApiModels(apiUrl, createTableFromJSON);
};

function getUsersApiModels(url, handler) {
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

    json.forEach(endpoint => {
        var row = document.createElement("tr");
        var nameField = document.createElement("td");
        nameField.innerHTML = endpoint.title;
        row.appendChild(nameField);
        var methodField = document.createElement("td");
        methodField.innerHTML = endpoint.method.toUpperCase();
        row.appendChild(methodField);
        var idField = document.createElement("td");
        idField.innerHTML = `<div class="tooltip"><a id="endpointIdField" onmouseout="outFunc(this)" role="button" href="#" onclick="return endpointIdOnClickHandle('${endpoint.id}', this)" class="tooltip"><span class="tooltiptext" id="myTooltip">Copy URL to clipboard</span>${endpoint.id}</a></div>`;
        row.appendChild(idField);
        var descriptionField = document.createElement("td");
        descriptionField.innerHTML = endpoint.description;
        row.appendChild(descriptionField);

        var requestFields = document.createElement("td");
        var fieldsHTML = [];
        endpoint.bodyTemplate.forEach(field => {
            fieldsHTML.push(createFieldDescription(field));
        });
        requestFields.innerHTML = fieldsHTML.join("<br>");
        row.appendChild(requestFields);

        var responseFields = document.createElement("td");
        fieldsHTML = [];
        endpoint.responseTemplate.forEach(field => {
            fieldsHTML.push(createFieldDescription(field));
        });
        responseFields.innerHTML = fieldsHTML.join("<br>");
        row.appendChild(responseFields);

        var manageField = document.createElement("td");
        var deleteButton = document.createElement("button");
        deleteButton.innerHTML = "Delete";
        deleteButton.addEventListener("click", deleteEndpoint);
        deleteButton.endpointId = endpoint.id;
        //manageField.innerHTML = `<button onclick=deleteEndpoint(this)>Delete</button>`;
        manageField.appendChild(deleteButton);
        row.appendChild(manageField);

        table.appendChild(row);
        idToTableRow[endpoint.id] = row;
    });
}

function createFieldDescription(field) {
    var result = `<b>key:</b> ${field.key}<br>
                  <b>type:</b> ${field.type}<br>`;
    const fieldType = field.type.toLowerCase();
    if (fieldType == "fixed" || fieldType == "regex") {
        result = result.concat(`<b>value:</b> ${field.value}`);
    }

    return result.concat("<br>");
}

function endpointIdOnClickHandle(id, idField) {
    const copyText = apiUrl + "/use/" + id;
    navigator.clipboard.writeText(copyText);
    var tooltip = idField.querySelector("#myTooltip");
    tooltip.innerHTML = "Copied: " + copyText;
}

function outFunc(idField) {
    var tooltip = idField.querySelector("#myTooltip");
    tooltip.innerHTML = "Copy to clipboard";
}

function deleteEndpoint(event) {
    var doDelete = confirm("Do you really want to delete endpoint?");
    if (!doDelete)
        return;

    const id = event.currentTarget.endpointId;
    const url = apiUrl + '/' + id;
    var requestOptions = {
        method: 'DELETE',
        redirect: 'follow',
        //mode: 'no-cors'
    };

    fetch(url, requestOptions)
        .then(response => response.json())
        .then(deleteEndpointHandler)
        .catch(error => console.log('error', error));
}

function deleteEndpointHandler(json) {
    alert(`Endpoint with id ${json.id} was successfully deleted.`);
    var tableRow = idToTableRow[json.id];
    table.removeChild(tableRow);
}