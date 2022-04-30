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

    if(recentCommandsPar.childElementCount >= 13){
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
        kBUtils.send(btn.innerText, fillDisplay);
    };
    btn.classList.add('btn', 'btn-primary', 'w-100', 'h-100');

    const btnWrapper = document.createElement('div');
    btnWrapper.classList.add('col-md-2', 'col-6', 'p-1');
    btnWrapper.appendChild(btn);

    return btnWrapper;
}

function guiInit(){
    const arr = Storage.getParse(PREFIX + 'recent-commands');
    const recentCommandsPar = document.getElementById("recentCommands");
    if(arr != null){
        arr.forEach(str => {
            const btn = cloneButton(str);
            recentCommandsPar.append(btn);
        });
    }
}