class BootstrapUtils {
    static childrenInnerText(el) {
        const ret = [];
        const children = el.childNodes;
        let index = 0;
        children.forEach(item => {
            ret[index++] = item.firstChild.innerText;
        });

        return ret;
    }
    static textMemoryQueue(el, par, key) {
        BootstrapUtils.queue(el, par);
        const innerTextAsJson = BootstrapUtils.childrenInnerText(par);
        Storage.setParse(PREFIX + key, innerTextAsJson);
    }

    static queue(el, par) {
        par.prepend(BootstrapUtils.queueWrapper(el));
        if (par.childElementCount >= 13) par.removeChild(par.lastChild);
    }

    static queueWrapper(el) {
        const wrapper = document.createElement('div');
        wrapper.classList.add('col-md-2', 'col-6', 'p-1');

        el.classList.add('w-100', 'h-100');
        wrapper.appendChild(el);

        return wrapper;
    }

    static button(text, onclick) {
        const btn = document.createElement('button');
        btn.innerText = text;

        btn.onclick = onclick;
        btn.classList.add('btn', 'btn-primary');

        return btn;
    }
}