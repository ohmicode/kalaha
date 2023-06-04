function loginUser() {
    var userLogin = document.getElementById("login").value;
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
    var newLogin = document.getElementById("newlogin").value;
    var nickname = document.getElementById("nickname").value;
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
            document.getElementById("gameName").innerHTML = jsonResponse.name + " | " + jsonResponse.gameState;
            // TODO: fill the board
        }
    };
    xhttp.open("GET", "/api/game/" + getGameId(), true);
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
