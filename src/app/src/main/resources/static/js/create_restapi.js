var fieldDescriptionTemplate =
    `
    <li>
            <div>
                <p>
                    <a href="#" class="sc" onclick="return UnHide(this)">&#9658;</a>
                    <input placeholder="field name" id="fieldNameInputField"></input>
                    <button onclick=removeField(this)>-</button>
                </p>
            </div>
            <ul>
            <li><div><p><select id="fieldTypeSelector" onchange=onFieldTypeChanged(this)>
                <option>String</option>
                <option>Integer</option>
                <option>Fixed</option>
                <option>Regex</option>
            </select></p></div></li>
            <li><div><p>
                <input placeholder="value" id="fieldValueInputField" disabled></input>
            </p></div></li>
            </ul>
    </li>
    `;

var requestBodyTemplate = function (requestName) {
    return `
    <li class="cl">
        <div id="requestHeader">
            <p>
                <a href="#" class="sc" onclick="return UnHide(this)">&#9658;</a>
                <a id="requestNameLabel">${requestName}</a>
                <button onclick=removeRequest(this)>-</button>
            </p>
        </div>
        <ul>
            <li>
            <div>
                <p>
                    <input placeholder="description" id="requestDescriptionInputField"></input>
                </p>
            </div>
            </li>
            <li>
            <div>
                <p>
                    <select id="requestTypeSelector">
                        <option>GET</option>
                        <option>POST</option>
                    </select>
                </p>
            </div>
            </li>
            <li>
            <div>
                <p>
                    <a>Request fields</a>
                    <button onclick=addField(this) id="addRequestFieldButton">+</button>
                </p>
            </div>
            </li>
           
        </ul>
        <ul>
            <li>
            <div>
                <p>
                    <a>Response fields</a>
                    <button onclick=addField(this) id="addResponseFieldButton">+</button>
                </p>
            </div>
            </li>
            
        </ul>
    </li>
    `;
}

var requests = [];
var fieldTypeTransformMap = {
    string: "str",
    integer: "int",
    regex: "regex",
    fixed: "fixed"
};

function UnHide(eThis) {
    if (eThis.innerHTML.charCodeAt(0) == 9658) {
        eThis.innerHTML = '&#9660;'
        eThis.parentNode.parentNode.parentNode.className = '';
    } else {
        eThis.innerHTML = '&#9658;'
        eThis.parentNode.parentNode.parentNode.className = 'cl';
    }
    return false;
}

function createElementFromHTML(htmlString) {
    var div = document.createElement('div');
    div.innerHTML = htmlString.trim();

    // Change this to div.childNodes to support multiple top-level nodes.
    return div.firstChild;
}

function addRequest() {
    var requestNameInputField = document.getElementById("requestNameInputField");
    var requestName = requestNameInputField.value.trim();
    requestNameInputField.value = "";
    if (requestName.length == 0) {
        return;
    }

    var tree = document.getElementById("requestsList");
    var requestBody = createElementFromHTML(requestBodyTemplate(requestName));
    tree.appendChild(requestBody);
    requests.push({request: requestBody, fields: [], responseFields: []});
    addField(document.getElementById("addRequestFieldButton"), true);
    addField(document.getElementById("addResponseFieldButton"), false);
}

function removeRequest(buttonClicked) {
    if (requests.length == 0)
        return;

    const key = buttonClicked.parentNode.parentNode.parentNode;
    const index = requests.findIndex(element => element.request == key);
    requests.at(index).request.remove();
    requests.splice(index, 1);
}

function addField(buttonClicked) {
    if (requests.length == 0)
        return;

    var fieldDescription = createElementFromHTML(fieldDescriptionTemplate);
    buttonClicked.parentNode.parentNode.parentNode.parentNode.appendChild(fieldDescription);
    const key = buttonClicked.parentNode.parentNode.parentNode.parentNode;
    const index = requests.findIndex(element => element.request == key);
    if (buttonClicked.id == "addRequestFieldButton")
        requests.at(index).fields.push(fieldDescription);
    else
        requests.at(index).responseFields.push(fieldDescription);
}

function removeField(buttonClicked) {
    if (requests.length == 0)
        return;

    const field = buttonClicked.parentNode.parentNode.parentNode;
    field.remove();

    const request = field.parentNode;
    const index = requests.findIndex(element => element.request == request);
    const fieldIndex = requests.at(index).fields.findIndex(field);
    requests.at(index).fields.splice(fieldIndex, 1);
}

function createEndpoint(index) {
    const req = requests.at(index);
    const requestTitle = req.request.querySelector("#requestNameLabel").innerHTML;
    const description = req.request.querySelector("#requestDescriptionInputField").value;
    const method = req.request.querySelector("#requestTypeSelector").value.toLowerCase().trim();
    var result = {
        title: requestTitle,
        description: description,
        method: method,
        bodyTemplate: [],
        responseTemplate: []
    };
    req.fields.forEach(field => {
        const key = field.querySelector("#fieldNameInputField").value.trim();
        const typeSelector = field.querySelector("#fieldTypeSelector");
        const type = fieldTypeTransformMap[typeSelector.options[typeSelector.selectedIndex].text.toLowerCase()];
        const value = field.querySelector("#fieldValueInputField").value.trim();
        result.bodyTemplate.push({key: key, type: type, value: value});
    });
    req.responseFields.forEach(field => {
        const key = field.querySelector("#fieldNameInputField").value.trim();
        const typeSelector = field.querySelector("#fieldTypeSelector");
        const type = fieldTypeTransformMap[typeSelector.options[typeSelector.selectedIndex].text.toLowerCase()];
        const value = field.querySelector("#fieldValueInputField").value.trim();
        result.responseTemplate.push({key: key, type: type, value: value});
    });

    console.log(JSON.stringify(result));

    return result;
}

function onFieldTypeChanged(fieldTypeSelector) {
    const fieldDescription = fieldTypeSelector.parentNode.parentNode.parentNode.parentNode;
    var fieldValueInput = fieldDescription.querySelector("#fieldValueInputField");
    const typeSelected = fieldTypeSelector.value.toLowerCase();
    fieldValueInput.disabled =
        (typeSelected != "fixed" && typeSelected != "regex");
}

function sendRestApiModel() {
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var endpoint = createEndpoint(0);
    var raw = JSON.stringify(endpoint);

    var requestOptions = {
        method: "POST",
        headers: myHeaders,
        body: raw,
        redirect: 'follow',
        //mode: 'no-cors'
    };

    fetch("http://178.21.11.9:8082/api", requestOptions)
        .then(response => response.json())
        .then(handleRestApiModelCreation)
        .catch(error => console.log('error', error));
}

function handleRestApiModelCreation(json) {
    console.log(json);
    alert(`Endpoint with id ${json.id} was successfully created.`);

    window.location.href = "usersapimodels.html";
    window.location.replace("usersapimodels.html");
}
