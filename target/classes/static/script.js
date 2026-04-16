document.addEventListener('DOMContentLoaded', function() {
    const commandInput = document.getElementById('command-input');
    const submitBtn = document.getElementById('submit-btn');
    const gameOutput = document.getElementById('game-output');
    const actionBtns = document.querySelectorAll('.action-btn');
    const directionBtns = document.querySelectorAll('.direction-btn');
    const directionButtonsContainer = document.getElementById('direction-buttons');
    const healthElement = document.getElementById('health');
    const damageElement = document.getElementById('damage');
    const roomElement = document.getElementById('room');

    let awaitingDirection = false;

    // 初始化游戏
    initializeGame();

    // 事件监听器
    submitBtn.addEventListener('click', handleCommand);
    commandInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            handleCommand();
        }
    });

    actionBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const command = this.dataset.command;
            if (command === 'go') {
                showDirectionButtons();
            } else {
                sendCommand(command);
            }
        });
    });

    directionBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const direction = this.dataset.direction;
            sendCommand(`go ${direction}`);
            hideDirectionButtons();
        });
    });

    function initializeGame() {
        // 从后端获取初始游戏状态
        sendCommand('look');
        getGameStatus();
    }

    function handleCommand() {
        const command = commandInput.value.trim();
        if (command) {
            if (awaitingDirection) {
                sendCommand(`go ${command}`);
                awaitingDirection = false;
                hideDirectionButtons();
            } else {
                sendCommand(command);
            }
            commandInput.value = '';
        }
    }

    function sendCommand(command) {
        // 调用后端API
        fetch('/api/game/command', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ command: command })
        })
        .then(response => response.json())
        .then(data => {
            // 清空当前输出
            gameOutput.innerHTML = '';
            // 添加新的输出
            data.output.forEach(line => {
                addOutput(line);
            });
            // 更新状态
            updateStatus(data.health, data.damage, data.room);
        })
        .catch(error => {
            console.error('Error:', error);
            addOutput('An error occurred while processing your command.');
        });
    }

    function getGameStatus() {
        // 获取游戏状态
        fetch('/api/game/status')
        .then(response => response.json())
        .then(data => {
            updateStatus(data.health, data.damage, data.room);
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }

    function addOutput(text) {
        const outputLine = document.createElement('div');
        outputLine.className = 'output-line';
        outputLine.textContent = text;
        gameOutput.appendChild(outputLine);
        gameOutput.scrollTop = gameOutput.scrollHeight;
    }

    function showDirectionButtons() {
        directionButtonsContainer.style.display = 'flex';
    }

    function hideDirectionButtons() {
        directionButtonsContainer.style.display = 'none';
        awaitingDirection = false;
    }

    function updateStatus(health, damage, room) {
        healthElement.textContent = `${health}/100`;
        damageElement.textContent = damage;
        roomElement.textContent = room;
    }

    function updateRoom(name, description, exits, monster) {
        addOutput(`You are in ${name}.`);
        addOutput(description);
        if (monster) {
            addOutput(`There is a ${monster} here!`);
        }
        addOutput(`Exits: ${exits}`);
        updateStatus(parseInt(healthElement.textContent.split('/')[0]), 20, name);
    }
});
