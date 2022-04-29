const PREFIX = 'kbcookie__';

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

function childrenInnerText(el){
    const ret = [];
    const children = el.childNodes;
    let index = 0;
    children.forEach(item => {
        ret[index++] = item.firstChild.innerText;
    });

    return ret;
}

function cloneButtonEl(el){
    const recentCommandsPar = document.getElementById("recentCommands");
    const btn = cloneButton(el.innerText);

    recentCommandsPar.prepend(btn);

    if(recentCommandsPar.childElementCount >= 7){
        recentCommandsPar.removeChild(recentCommandsPar.lastChild);
    }

    const recentCommands = childrenInnerText(recentCommandsPar);
    Storage.setParse(PREFIX + 'recent-commands', recentCommands);
}

function cloneButton(text){
    const btn = document.createElement('button');
    btn.innerText = text;

    btn.onclick = function(){
        setInp(btn.innerText);
        send(btn.innerText);
    };
    btn.classList.add('btn', 'btn-primary', 'w-100', 'h-100', 'px-1');

    const btnWrapper = document.createElement('div');
    btnWrapper.classList.add('col-2');
    btnWrapper.appendChild(btn);

    return btnWrapper;
}

function init(){
    const arr = Storage.getParse(PREFIX + 'recent-commands');
    const recentCommandsPar = document.getElementById("recentCommands");
    if(arr != null){
        arr.forEach(str => {
            const btn = cloneButton(str);
            recentCommandsPar.append(btn);
        });
    }
}
