function guiInit() {
    const key = 'recent-commands';
    const arr = Storage.getParse(PREFIX + key);
    const par = document.getElementById("recentCommands");
    if (arr != null) {
        arr.forEach(str => {
            const wrapper = BootstrapUtils.queueWrapper(BootstrapUtils.button(str, sendSetInpFillDisplayEvent));
            par.append(wrapper);
        });
    }
}

function sendSetInpFillDisplayEvent(event) {
    sendSetInpFillDisplay(event.target.innerText);
}

function sendSetInpFillDisplay(command) {
    setInp(command);
    kBUtils.send(command, fillDisplay);
}

function commandBankDropDownClick(item) {

    sendSetInpFillDisplay(item.innerText);
    const btn = BootstrapUtils.button(item.innerText, sendSetInpFillDisplayEvent);

    const parent = document.getElementById('recentCommands');
    BootstrapUtils.textMemoryQueue(btn, parent, 'recent-commands');
}