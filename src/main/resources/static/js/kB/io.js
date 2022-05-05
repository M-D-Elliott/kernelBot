function clearHTML(el) {
    el.innerHTML = "";
}

function kbInp() {
    return document.getElementById("kbInp");
}

function setInp(str) {
    kbInp().value = str;
}

function clearInp() {
    kbInp().value = "";
}

function kbDisplay() {
    return document.getElementById("kbDisplay");
}

function sendInp() {
    kBUtils.send(kbInp().value, fillDisplay);
}

function fillDisplay(json) {
    const display = kbDisplay();
    clearHTML(display);

    const images = json.images;
    if (images != null && images.length > 0) {
        const imgEl = new Image();
        imgEl.src = 'data:image/png;base64, ' + images[0];
        display.append(imgEl);
    }

    json.resp.forEach(item => {
        const pEl = document.createElement(item ? 'pre' : 'br');
        pEl.innerHTML = item;
        display.append(pEl);
    });
}