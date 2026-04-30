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
    const goldElement = document.getElementById('gold');
    const inventoryItems = document.getElementById('inventory-items');
    const shopModal = document.getElementById('shop-modal');
    const shopItemsContainer = document.getElementById('shop-items');
    const openShopBtn = document.getElementById('open-shop-btn');
    const closeShopBtn = document.getElementById('close-shop-btn');

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
            } else if (command === 'inventory') {
                getInventory();
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

    openShopBtn.addEventListener('click', openShop);
    closeShopBtn.addEventListener('click', closeShop);

    function initializeGame() {
        sendCommand('look');
        getGameStatus();
        getInventory();
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
        fetch('/api/game/command', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ command: command })
        })
        .then(response => response.json())
        .then(data => {
            gameOutput.innerHTML = '';
            data.output.forEach(line => {
                addOutput(line);
            });
            updateStatus(data.health, data.damage, data.room);
        })
        .catch(error => {
            console.error('Error:', error);
            addOutput('An error occurred while processing your command.');
        });
    }

    function getGameStatus() {
        fetch('/api/game/status')
        .then(response => response.json())
        .then(data => {
            updateStatus(data.health, data.damage, data.room);
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }

    function getInventory() {
        fetch('/api/game/inventory')
        .then(response => response.json())
        .then(data => {
            updateInventory(data.items, data.gold);
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }

    function updateInventory(items, gold) {
        goldElement.textContent = gold;
        
        if (items.length === 0) {
            inventoryItems.innerHTML = '<div class="inventory-empty">No items in inventory</div>';
        } else {
            inventoryItems.innerHTML = '';
            items.forEach(item => {
                const itemElement = document.createElement('div');
                itemElement.className = 'inventory-item';
                itemElement.innerHTML = `
                    <span class="item-name">${item.name}</span>
                    <span class="item-type">${item.type}</span>
                    <button class="sell-btn" data-item-id="${item.id}" data-item-price="${item.price}">Sell</button>
                `;
                inventoryItems.appendChild(itemElement);
            });
            
            document.querySelectorAll('.sell-btn').forEach(btn => {
                btn.addEventListener('click', function() {
                    const itemId = this.dataset.itemId;
                    sellItem(itemId, 1);
                });
            });
        }
    }

    function sellItem(itemId, quantity) {
        fetch('/api/game/sell', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ itemId: itemId, quantity: quantity })
        })
        .then(response => response.json())
        .then(data => {
            addOutput(data.message);
            getInventory();
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }

    function openShop() {
        fetch('/api/game/shop')
        .then(response => response.json())
        .then(data => {
            goldElement.textContent = data.playerGold;
            renderShopItems(data.items);
            shopModal.style.display = 'flex';
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }

    function closeShop() {
        shopModal.style.display = 'none';
        getInventory();
    }

    function renderShopItems(items) {
        shopItemsContainer.innerHTML = '';
        items.forEach(item => {
            const itemElement = document.createElement('div');
            itemElement.className = 'shop-item';
            itemElement.innerHTML = `
                <div class="item-info">
                    <span class="item-name">${item.name}</span>
                    <span class="item-desc">${item.description}</span>
                    <span class="item-price">Price: ${item.price} 💰</span>
                </div>
                <button class="buy-btn" data-item-id="${item.id}" data-item-price="${item.price}" data-item-name="${item.name}">Buy</button>
            `;
            shopItemsContainer.appendChild(itemElement);
        });
        
        document.querySelectorAll('.buy-btn').forEach(btn => {
            btn.addEventListener('click', function() {
                const itemId = this.dataset.itemId;
                buyItem(itemId, 1);
            });
        });
    }

    function buyItem(itemId, quantity) {
        fetch('/api/game/buy', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ itemId: itemId, quantity: quantity })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                addOutput(data.message);
                getInventory();
                fetch('/api/game/shop')
                .then(response => response.json())
                .then(shopData => {
                    goldElement.textContent = shopData.playerGold;
                });
            } else {
                alert(data.message);
            }
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
});
