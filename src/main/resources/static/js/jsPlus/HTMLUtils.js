class HTMLUtils {
    static fromS(str) {
        var parser = new DOMParser();
        var doc = parser.parseFromString(str, 'text/html');
        return doc.body.firstChild;
    }

    static toClipboardForce(value) {
        if (typeof (navigator.clipboard) == 'undefined') {
            const textArea = document.createElement("textarea");
            textArea.value = value;
            textArea.style.position = "fixed";
            document.body.appendChild(textArea);
            textArea.focus();
            textArea.select();

            let ret = false;
            try { ret = document.execCommand('copy'); } catch (err) { console.log('Copy failed'); } finally { document.body.removeChild(textArea); }
            return ret;
        } else {
            navigator.clipboard.writeText(value);
        }
    }

    static pasteForce(cb) {
        if (typeof (navigator.clipboard) == 'undefined') {

            const pasteTarget = document.createElement("div");
            pasteTarget.contentEditable = true;
            const actElem = document.activeElement.appendChild(pasteTarget).parentNode;
            pasteTarget.focus();
            document.execCommand("paste", null, null);
            const paste = pasteTarget.innerText;
            actElem.removeChild(pasteTarget);

            cb(paste);

        } else {
            navigator.clipboard.readText()
                .then(text => {
                    cb(text);
                });
        }
    }
}