function clearHTML(el){
    el.innerHTML = "";
}

function kbInp(){
    return document.getElementById("kb-inp");
}

function setInp(str){
    kbInp().value = str;
}

function clearInp() {
    kbInp().value = "";
}

function kbDisplay(){
    return document.getElementById("kb-display");
}

function sendInp(){
    send(kbInp().value);
}

function send(commandString) {
    fetch('/', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: commandString
        })
        .then(resp => resp.json())
        .then(resp => {
            fillDisplay(resp.resp);
        });
}

function fillDisplay(items){
    const display = kbDisplay();
    clearHTML(display);
    items.forEach(item =>{
        const pItem = document.createElement('pre');
        pItem.innerHTML = item;
        pItem.classList.add('text-white');
        display.append(pItem);
    });
}

function cloneButton(menuItem){
    const btn = document.createElement('button');
    btn.innerText = menuItem.innerText;
    const recentCommands = document.getElementById("recent-commands");
    btn.onclick = function(){
        setInp(this.innerText);
        send(this.innerText);
    };
    btn.classList.add('btn', 'btn-primary', 'mx-1', 'p-3', 'col-2', 'flex-grow-1');
    recentCommands.prepend(btn);

    if(recentCommands.childElementCount >= 6){
        recentCommands.removeChild(recentCommands.lastChild);
    }
}
