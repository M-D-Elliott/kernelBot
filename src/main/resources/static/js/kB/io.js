function clearHTML(el){
    el.innerHTML = "";
}

function kbInp(){
    return document.getElementById("kbInp");
}

function setInp(str){
    kbInp().value = str;
}

function clearInp() {
    kbInp().value = "";
}

function kbDisplay(){
    return document.getElementById("kbDisplay");
}

function sendInp(){
    kBUtils.send(kbInp().value, fillDisplay);
}

function fillDisplay(json){
    const display = kbDisplay();
    clearHTML(display);
    json.resp.forEach(item =>{
        const pItem = document.createElement(item ? 'pre' : 'br');
        pItem.innerHTML = item;
        display.append(pItem);
    });
}