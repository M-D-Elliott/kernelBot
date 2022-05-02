const PREFIX = 'kbcookie__';

function init(){
    pingLinks();
}

function pingLinks(){
    const kbLinks = document.getElementsByClassName('kb-links');
    for (var i = 0; i < kbLinks.length; i++) {

        const link = kbLinks.item(i);
        const linkA = link.children[0];
        const linkHREF = linkA.href;

        linkA.className = 'nav-link text-warning';

        APIs.ping(linkHREF,
        (resp) => {
            linkA.className = 'nav-link text-success';
        },
        (err) => {
            linkA.className = 'nav-link text-danger';
        },
        10000);
    }
}
