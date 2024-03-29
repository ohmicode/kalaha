function loginUser() {
    const userLogin = document.getElementById("login").value;
    var xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        var jsonResponse = JSON.parse(xhttp.response);
        if (this.readyState == 4 && this.status == 200) {
            window.location.href = "/front/selectgame.html?playerId=" + jsonResponse.id;
        } else {
            alert(jsonResponse.message);
        }
    };
    xhttp.onerror = function() {
        const jsonResponse = JSON.parse(xhttp.response);
        alert(jsonResponse.message);
    };
    xhttp.open("GET", "/api/player/login?login=" + userLogin, true);
    xhttp.send();
}

function createUser() {
    const newLogin = document.getElementById("newlogin").value;
    const nickname = document.getElementById("nickname").value;
    var xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        var jsonResponse = JSON.parse(xhttp.response);
        if (this.readyState == 4 && this.status == 200) {
            window.location.href = "/front/selectgame.html?playerId=" + jsonResponse.id;
        } else {
            alert(jsonResponse.message);
        }
    };
    xhttp.onerror = function() {
        const jsonResponse = JSON.parse(xhttp.response);
        alert(jsonResponse.message);
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
        const jsonResponse = JSON.parse(xhttp.response);
        if (this.readyState == 4 && this.status == 200) {
            window.location.href = "/front/board.html?gameId=" + jsonResponse.id + "&playerId=" + playerId;
        } else {
            alert(jsonResponse.message);
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
        const jsonResponse = JSON.parse(xhttp.response);
        if (this.readyState == 4 && this.status == 200) {
            window.location.href = "/front/board.html?gameId=" + jsonResponse.id + "&playerId=" + playerId;
        } else {
            alert(jsonResponse.message);
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
        const jsonResponse = JSON.parse(xhttp.response);
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("gameName").innerHTML = jsonResponse.name + " | " + jsonResponse.gameStatus;
            updateStatuses(jsonResponse.myTurn, jsonResponse.mySide);
            const mySide = (jsonResponse.myTurn ? jsonResponse.mySide : 0);
            const tableContent = buildBoardTable(jsonResponse.pits1, jsonResponse.pits2, jsonResponse.pool1, jsonResponse.pool2, mySide);
            document.getElementById("gameBoard").innerHTML = tableContent;
        } else {
            alert(jsonResponse.message);
        }
    };
    xhttp.onerror = function() {
        const jsonResponse = JSON.parse(xhttp.response);
        alert(jsonResponse.message);
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
    const playerId = getPlayerId();
    const gameId = getGameId();
    var xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        if (this.readyState == 4 && this.status == 200) {
            updateBoard();
        } else {
            const jsonResponse = JSON.parse(xhttp.response);
            alert(jsonResponse.message);
        }
    };
    xhttp.onerror = function() {
        const jsonResponse = JSON.parse(xhttp.response);
        alert(jsonResponse.message);
    };
    xhttp.open("POST", "/api/game/" + gameId + "/move?playerId=" + playerId + "&move=" + pit, true);
    xhttp.send();
}

function loadLeaderboard() {
    var xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        const jsonResponse = JSON.parse(xhttp.response);
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("leaderboard").innerHTML = buildLeaderboard(jsonResponse);
        } else {
            alert(jsonResponse.message);
        }
    };
    xhttp.onerror = function() {
        const jsonResponse = JSON.parse(xhttp.response);
        alert(jsonResponse.message);
    };
    xhttp.open("GET", "/api/player/leaderboard", true);
    xhttp.send();
}

function buildLeaderboard(players) {
    let table = "<tr><th><div>Nickname</div></th><th><div>Wins</div></th><th><div>Loses</div></th></tr>";
    for (let i = 0; i < players.length; i++) {
        table += "<tr><td><div>" + players[i].nickname + "</div></td><td><div>" + players[i].wins + "</div></td><td><div>" + players[i].loses + "</div></td></tr>";
    }
    return table;
}

function loadGameList() {
    var xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        const jsonResponse = JSON.parse(xhttp.response);
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("gameList").innerHTML = buildGameList(jsonResponse);
        } else {
            alert(jsonResponse.message);
        }
    };
    xhttp.onerror = function() {
        const jsonResponse = JSON.parse(xhttp.response);
        alert(jsonResponse.message);
    };
    xhttp.open("GET", "/api/game/last", true);
    xhttp.send();
}

function buildGameList(games) {
    let table = "<tr><th><div>Game</div></th><th><div>Status</div></th></tr>";
    for (let i = 0; i < games.length; i++) {
        const link = "/front/board.html?gameId=" + games[i].id + "&playerId=";
        table += "<tr><td><div><a href='" + link + "'>" + games[i].name + "</a></div></td><td><div>" + games[i].gameStatus + "</div></td></tr>";
    }
    return table;
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
