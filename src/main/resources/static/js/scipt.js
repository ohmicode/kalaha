function loginUser() {
    const userLogin = document.getElementById("login").value;
    var xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        if (this.readyState == 4 && this.status == 200) {
            var jsonResponse = JSON.parse(xhttp.response);
            window.location.href = "/front/selectgame.html?playerId=" + jsonResponse.id;
        }
    };
    xhttp.open("GET", "/api/player/login?login=" + userLogin, true);
    xhttp.send();
}

function createUser() {
    const newLogin = document.getElementById("newlogin").value;
    const nickname = document.getElementById("nickname").value;
    var xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        if (this.readyState == 4 && this.status == 200) {
            var jsonResponse = JSON.parse(xhttp.response);
            window.location.href = "/front/selectgame.html?playerId=" + jsonResponse.id;
        }
    };
    xhttp.open("POST", "/api/player?login=" + newLogin + "&nickname=" + nickname, true);
    xhttp.send();
}

function resetToDefault() {
    document.getElementById("pits").value = "6";
    document.getElementById("stones").value = "6";
}

function createGame(pits, stones) {
    const playerId = getPlayerId();
    var xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        if (this.readyState == 4 && this.status == 200) {
            const jsonResponse = JSON.parse(xhttp.response);
            window.location.href = "/front/board.html?gameId=" + jsonResponse.id + "&playerId=" + playerId;
        }
    };
    xhttp.onerror = function() {
        const jsonResponse = JSON.parse(xhttp.response);
        alert(jsonResponse.message);
    };
    xhttp.open("POST", "/api/game?playerId=" + playerId + "&size=" + pits + "&stones=" + stones, true);
    xhttp.send();
}

function joinGame(gameId) {
    const playerId = getPlayerId();
    var xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        if (this.readyState == 4 && this.status == 200) {
            const jsonResponse = JSON.parse(xhttp.response);
            window.location.href = "/front/board.html?gameId=" + jsonResponse.id + "&playerId=" + playerId;
        }
    };
    xhttp.onerror = function() {
        const jsonResponse = JSON.parse(xhttp.response);
        alert(jsonResponse.message);
    };
    xhttp.open("PUT", "/api/game/" + gameId + "/join?playerId=" + playerId, true);
    xhttp.send();
}

function startBoardRefresh() {
    document.getElementById("gameNumber").innerHTML = getGameId();
    setInterval(updateBoard, 1500);
}

function updateBoard() {
    var xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        if (this.readyState == 4 && this.status == 200) {
            const jsonResponse = JSON.parse(xhttp.response);
            document.getElementById("gameName").innerHTML = jsonResponse.name + " | " + jsonResponse.gameStatus;
            updateStatuses(jsonResponse.myTurn, jsonResponse.mySide);
            const mySide = (jsonResponse.myTurn ? jsonResponse.mySide : 0);
            const tableContent = buildBoardTable(jsonResponse.pits1, jsonResponse.pits2, jsonResponse.pool1, jsonResponse.pool2, mySide);
            document.getElementById("gameBoard").innerHTML = tableContent;
        }
    };
    xhttp.open("GET", "/api/game/" + getGameId() + "?playerId=" + getPlayerId(), true);
    xhttp.send();
}

function updateStatuses(myTurn, mySide) {
    if (myTurn) {
        document.getElementById("yourTurn").classList.remove("hidden");
    } else {
        document.getElementById("yourTurn").classList.add("hidden");
    }
    if (mySide == 0) {
        document.getElementById("spectatorMode").classList.remove("hidden");
    } else {
        document.getElementById("spectatorMode").classList.add("hidden");
    }
    if (mySide == 1) {
        document.getElementById("inviteLink").classList.remove("hidden");
    } else {
        document.getElementById("inviteLink").classList.add("hidden");
    }
}

function buildBoardTable(pits1, pits2, pool1, pool2, mySide) {
    let row1 = "";
    for (let i = 0; i < pits2.length; i++) {
        let action = (mySide == 2 ? "onclick='makeMove(" + i + ");'" : "");
        row1 = "<td><div id='pit2" + i + "' " + action + ">" + pits2[i] + "</div></td>" + row1;
    }
    row1 = "<td rowspan=2><div id='pool2'>" + pool2 + "</div></td>" + row1 + "<td rowspan=2><div id='pool1'>" + pool1 + "</div></td>";

    let row2 = "";
    for (let i = 0; i < pits1.length; i++) {
        let action = (mySide == 1 ? "onclick='makeMove(" + i + ");'" : "");
        row2 += "<td><div id='pit1" + i + "' " + action + ">" + pits1[i] + "</div></td>";
    }

    return "<tr>" + row1 + "</tr><tr>" + row2 + "</tr>";
}

function makeMove(pit) {
    const playerId = getPlayerId()
    const gameId = getGameId()
    // alert("Game " + gameId + ", Player " + playerId + " is going to move to cell " + pit);
    var xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        if (this.readyState == 4 && this.status == 200) {
            updateBoard();
        }
    };
    xhttp.onerror = function() {
        const jsonResponse = JSON.parse(xhttp.response);
        alert(jsonResponse.message);
    };
    xhttp.open("POST", "/api/game/" + gameId + "/move?playerId=" + playerId + "&move=" + pit, true);
    xhttp.send();
}

function getPlayerId() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    return urlParams.get('playerId');
}

function getGameId() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    return urlParams.get('gameId');
}
