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
    static textMemoryQueue(el, par, key, maxItems) {
        BootstrapUtils.queue(el, par, maxItems);
        const innerTextAsJson = BootstrapUtils.childrenInnerText(par);
        Storage.setParse(PREFIX + key, innerTextAsJson);
    }

    static queue(el, par, maxItems = 12) {
        par.prepend(BootstrapUtils.queueWrapper(el, maxItems));
        if (par.childElementCount >= maxItems + 1) par.removeChild(par.lastChild);
    }

    static queueWrapper(el, maxItems = 12) {
        const rows = 2;
        const itemsPerRow = maxItems / rows;
        const wrapper = document.createElement('div');
        wrapper.classList.add(`col-md-${12 / itemsPerRow}`, 'col-6', 'p-1');

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