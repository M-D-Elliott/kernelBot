function colorsMenu() {
    return document.getElementById('colorsMenu');
}

function colorsInp() {
    return document.getElementById('colorsInp');
}

function colorsKey() {
    return PREFIX + 'colors-data';
}

function colorsData() {
    return Storage.getParse(colorsKey());
}

/******************************************************************/

function settingsInit() {
    setRootColors(colorsData());
}

function settingsGUIInit() {
    let cData = colorsData();

    if (cData == null) {
        cData = newColorsDataObj();
        Storage.setParse(colorsKey(), cData);
    }

    const cMenu = colorsMenu();
    cMenu.innerHTML = "";

    for (const key in cData) {
        const option = document.createElement('option');
        option.value = cData[key];
        option.innerText = key;

        cMenu.append(option);
    }

    colorsSelect(cMenu);
}

function newColorsDataObj() {
    return {
        primary: '208, 100.0%, 49.0%',
        secondary: '210, 7.0%, 56.0%',
        success: '120, 39.0%, 54.0%',
        info: '194, 66.0%, 61.0%',
        warning: '35, 84.0%, 62.0%',
        danger: '2, 64.0%, 58.0%',
        inverse: '200, 4.0%, 17.0%',
        faded: '0, 0.01%, 97.0%',
        bg: '243, 100.0%, 13.1%'
    };
}

/******************************************************************/

function colorsSelect(cMenu) {
    const oValue = cMenu.value;
    const oValueSplit = oValue.split(', ');
    const h = parseFloat(oValueSplit[0]);
    const s = parseFloat(oValueSplit[1].replace('%', ''));
    const l = parseFloat(oValueSplit[2].replace('%', ''));

    colorsInp().value = ColorUtils.hslToHex(h, s, l);
}

function colorsChange(colorsInp) {
    const cMenu = colorsMenu();
    const option = cMenu.options[cMenu.selectedIndex];

    const hslString = ColorUtils.hexToHSLString(colorsInp.value);
    option.value = hslString;

    setRootColor(option.text, hslString);
}

function colorsSave() {
    Storage.setParse(colorsKey(), getColorsFromSelect());
}

function colorsDefault() {
    setColorsData(newColorsDataObj());
}

function copyColors() {
    const cData = getColorsFromSelect();
    const cDataJson = JSON.stringify(cData, null, " ");
    HTMLUtils.toClipboardForce(cDataJson);
}

function pasteColors() {
    HTMLUtils.pasteForce((text) => {
        try {
            setColorsData(JSON.parse(text));
        } catch (e) {
            return false;
        }
    });
}

/******************************************************************/

function setColorsData(obj) {
    Storage.setParse(colorsKey(), obj);
    settingsInit();
    settingsGUIInit();
}

function setRootColor(key, hslString) {
    const hslSplit = hslString.split(', ');

    const docElStyle = document.documentElement.style;
    const valuePrefix = '--' + key + '-';
    docElStyle.setProperty(valuePrefix + 'raw', hslSplit[0] + ', ' + hslSplit[1]);
    docElStyle.setProperty(valuePrefix + 'l', hslSplit[2]);
}

function setRootColors(cData) {
    if (cData != null) {
        for (const key in cData) {
            const value = cData[key];
            setRootColor(key, value);
        }
    }
}

function getColorsFromSelect() {
    const cMenu = colorsMenu();
    const cData = {};

    const options = cMenu.options;
    const optionsLength = options.length;

    for (let i = 0; i < optionsLength; i++) {
        const option = options[i];
        cData[option.text] = option.value;
    }

    return cData;
}