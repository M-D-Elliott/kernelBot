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
    display.innerHTML = "";

    const images = json.images;
    if (images != null && images.length > 0) {
        const imgSrc = 'data:image/png;base64, ' + images[0];
        const thumbnail = `<img src='${imgSrc}' alt=""
                            style="width: 7rem;max-width: 100%;height: auto;vertical-align: 
                            middle;border-style: none;">`
        display.innerHTML = thumbnail;
    }

    json.resp.forEach(item => {
        const pEl = document.createElement(item ? 'pre' : 'br');
        pEl.innerHTML = item;
        display.append(pEl);
    });
}